package com.virgil.virgilagent;

import com.virgil.virgilagent.request.Request;
import com.virgil.virgilagent.request.RequestParser;
import com.virgil.virgilagent.response.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class ScrollingActivity extends AppCompatActivity {

    public static final int SERVERPORT = 31415;

    boolean ServerRunning = false;
    Handler ServerLogHandler;
    ServerThreadRunnable serverThreadRunnable = null;
    Button StartStopBtn;
    FloatingActionButton fab;
    TextView LogTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        StartStopBtn = findViewById(R.id.StartStopBtn);
        LogTV = (TextView) findViewById(R.id.AppLogTV);
        ServerLogHandler = new Handler(Looper.getMainLooper());
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());
        StartStopBtn.setOnClickListener(new StartStopClickListener(this));
    }

    class StartStopClickListener implements View.OnClickListener {
        private Context InvokerContext;

        public StartStopClickListener(Context InvokerContext) {
            super();
            this.InvokerContext = InvokerContext;
        }
        @Override
        public void onClick(View view) {
            if (ServerRunning) {
                LogTV.append("\nServer Stopped");
                StartStopBtn.setText("Start");
                ShutDownServer();
                ServerRunning = false;
            }
            else {
                LogTV.append("\nServer Started");
                StartStopBtn.setText("Stop");
                StartUpServer(this.InvokerContext);
                ServerRunning = true;
            }
        }
    }

    private void ShutDownServer() {
        serverThreadRunnable.Stop();
    }
    private void StartUpServer(Context InvokerContext) {
        serverThreadRunnable = new ScrollingActivity.ServerThreadRunnable(InvokerContext,ServerLogHandler);
        Thread serverThread = new Thread(serverThreadRunnable);
        serverThread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ShutDownServer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class ServerThreadRunnable implements Runnable {
        private Handler ServerLogHandler;
        private Context InvokerContext;
        private boolean KeepRunning = true;
        private int SocketTimeOut = 3*1000;
        private String LogTag = "VirgilAgent-ServerThread";

        public synchronized void Stop() {
            this.KeepRunning = false;
        }
        private synchronized boolean KeepRunning() {
            return this.KeepRunning;
        }
        public ServerThreadRunnable(Context InvokerContext,Handler ServerLogHandler) {
            super();
            this.InvokerContext = InvokerContext;
            this.ServerLogHandler = ServerLogHandler;
        }

        public void run() {
            List<CommunicationThreadRunnable> CommThreadRunnables = new ArrayList<CommunicationThreadRunnable>();
            ServerSocket serverSocket = null;
            Socket socket = null;
            try {
                serverSocket = new ServerSocket(SERVERPORT);
                serverSocket.setSoTimeout(SocketTimeOut);
            } catch (IOException e) {
                Log.e(this.LogTag,e.getMessage());
                return;
            }
            while (KeepRunning() && !Thread.currentThread().isInterrupted())
                try {
                    try {
                        socket = serverSocket.accept();
                    }
                    catch (SocketTimeoutException e) {
                        continue;
                    }
                    CommunicationThreadRunnable commThreadRunnable = new CommunicationThreadRunnable(socket,this.InvokerContext,this.ServerLogHandler);
                    new Thread(commThreadRunnable).start();
                    CommThreadRunnables.add(commThreadRunnable);
                    this.ServerLogHandler.post(new updateUIThread("Accepted Connection"));
                } catch (IOException e) {
                    continue;
                }
            try {
                for (CommunicationThreadRunnable commThreadRun : CommThreadRunnables)
                    commThreadRun.Stop();
                serverSocket.close();
            } catch (IOException e) {
                Log.e(this.LogTag,e.getMessage());
                return;
            }
        }
    }

    class CommunicationThreadRunnable implements Runnable {
        private Socket clientSocket;
        private BufferedReader input;
        private PrintWriter out;
        private Context InvokerContext;
        private boolean KeepRunning = true;
        private Handler ServerLogHandler;
        private String LogTag = "VirgilAgent-CommThread";

        public synchronized void Stop() {
            this.KeepRunning = false;
        }
        private synchronized boolean KeepRunning() {
            return this.KeepRunning;
        }

        public CommunicationThreadRunnable(Socket clientSocket,Context InvokerContext, Handler ServerLogHandler) {
            this.clientSocket = clientSocket;
            this.InvokerContext = InvokerContext;
            this.ServerLogHandler = ServerLogHandler;
            try {
                this.input = new BufferedReader( new InputStreamReader(this.clientSocket.getInputStream()));
                this.out = new PrintWriter(this.clientSocket.getOutputStream(),true);
            } catch (IOException e) {
                Log.e(this.LogTag,e.getMessage());
                return;
            }
        }

        public void run() {
            while (KeepRunning() && !Thread.currentThread().isInterrupted()) {
                try {
                    String RequestLine = input.readLine();
                    if (RequestLine.equalsIgnoreCase("close")) {
                        this.ServerLogHandler.post(new updateUIThread("Closing Connection"));
                        break;
                    }
                    Request CurrentRequest = RequestParser.ParseRequest(RequestLine);
                    if (CurrentRequest == null)
                        this.out.println("<error><message>Unknown request: " + RequestParser.GetErrorMessage() + "</message></error>");
                    else {
                        ResponseBase CurrentResponse = ResponseFactory.CreateResponse(CurrentRequest);
                        if (CurrentResponse == null)
                            this.out.println("<error><message>Unknown request: " + CurrentRequest.GetRequestName() + " " + CurrentRequest.GetRequestArgs() + " " + ResponseFactory.GetErrorMessage() + "</message></error>");
                        else {
                            this.ServerLogHandler.post(new updateUIThread("Executing " + CurrentRequest.GetRequestName()));
                            CurrentResponse.Execute(this.InvokerContext);
                            this.out.println(CurrentResponse.GetResponseText());
                        }
                    }
                }
                catch (IOException e) {
                    Log.e(this.LogTag,e.getMessage());
                    continue;
                }
            }
            try {
                this.input.close();
                this.out.close();
                this.clientSocket.close();
                this.clientSocket = null;
            } catch (IOException e) {
                Log.e(this.LogTag,e.getMessage());
                return;
            }
        }
    }

    class updateUIThread implements Runnable {
        private String msg;
        public updateUIThread(String str) {
            this.msg = str;
        }
        @Override
        public void run() {
            LogTV.append("\n" + this.msg);
            return;
        }
    }
}