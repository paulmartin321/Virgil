package request;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class RequestBase {
	
	protected String RequestName;
	protected String[] RequestArgs;
	protected List<Object> ResponseData;
	protected Document ResponseDocument = null;
	protected Element ResponseElement = null;
	
	public RequestBase(String RequestName,String[] RequestArgs) {
		this.RequestName = RequestName;
		this.RequestArgs = RequestArgs;
	}
	public String RequestText() {
		String Request = "<?xml version=\"1.0\"?>";
		Request += "<request>";
		Request += "<requestname>";
		Request += this.RequestName;
		Request += "</requestname>";
		Request += "<arguments>";		
		int a = 0;
		while (a < this.RequestArgs.length) {			
			Request += "<" + this.RequestArgs[a].substring(1) + ">";
			Request += this.RequestArgs[a + 1];			
			Request += "</" + this.RequestArgs[a].substring(1) + ">";
			a = a + 2;
		}		
		Request += "</arguments>";
		Request += "</request>";
		return Request;
	}
	
	public boolean LoadResponseText(String ResponseText) {
		return false;
	}
	public List<Object> GetResponseData() {
		return ResponseData;
	}
	protected boolean Initialize(String ResponseText) {
		this.ResponseData = new ArrayList<Object>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			return false;
		}
		try {
			this.ResponseDocument = db.parse(new InputSource(new StringReader(ResponseText)));
			this.ResponseElement = this.ResponseDocument.getDocumentElement();
		} catch (SAXException e) {
		    return false;
		} catch (IOException e) {
		    return false;
		}
		return true;
	}
	protected  boolean ErrorResponse() {
		if (this.ResponseElement.getTagName().equalsIgnoreCase("error")) {
			NodeList MessageNodes = this.ResponseElement.getElementsByTagName("message");			
			ResponseError NewError = new ResponseError();
			NewError.SetMessage(MessageNodes.item(0).getTextContent());
			ResponseData.add(NewError);		
			return true;
		}
		return false;
	}

}

class ResponseError {
	private String Message = "";
	
	public void SetMessage(String Message) {
		this.Message = Message;
	}
	public ResponseError() {
		
	}	
	
	public String toString() {
		return this.Message;
	}
}
