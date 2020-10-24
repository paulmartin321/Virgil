# Virgil
Andriod app testing framework. Virgil consists of two projects: VirgilAgent and VirgilClient. 

VirgilAgent
An app that is loaded into an Android Phone or emulator. It receives commands from the client app and returns the result. This is in an Android studio project.

VirgilClient
An app that sends commands to the VirgilAgent. It has a command line interface and displays the results to the command line. This is in an Eclipse project.

To Run
1) Install VirgilAgent.apk on device or emulator
  This can be done with Android Debugger Bridge with the following command:
  adb install VirgilAgent.apk
2) Click the start button on the VirgilAgent on the device
3) Start the VirgilClient.jar file
4) Enter commands at the prompt
