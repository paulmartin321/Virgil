package com.virgil.virgilagent.request;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

public class RequestParser {
	private static String RequestNameTag =  "requestname";
	private static String RequestArgsTag =  "arguments";
	private static String ErrorMessage = "";

	public static String GetErrorMessage() {
		return ErrorMessage;
	}
    public static Request ParseRequest(String RequestText) {
    	try {
			ErrorMessage = "";
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = null;
			try {
				db = dbf.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				return null;
			}
			Document doc = null;
			try {
				doc = db.parse(new InputSource(new StringReader(RequestText)));
			} catch (SAXException e) {
				ErrorMessage = e.getMessage();
				return null;
			} catch (IOException e) {
				ErrorMessage = e.getMessage();
				return null;
			}
			Element RequestEle = doc.getDocumentElement();
			Node NameNode = RequestEle.getElementsByTagName(RequestNameTag).item(0);
			Request NewRequest = new Request(NameNode.getTextContent());
			NodeList ArgsNodes = RequestEle.getElementsByTagName(RequestArgsTag);
			Node ArgNode = ArgsNodes.item(0).getFirstChild();
			while (ArgNode != null) {
				String ArgName = ArgNode.getNodeName();
				String ArgValue = ArgNode.getTextContent();
				NewRequest.AddArg(ArgName, ArgValue);
				ArgNode = ArgNode.getNextSibling();
			}
			return NewRequest;
		}
    	catch (Exception e) {
			ErrorMessage = e.getMessage();
    		return null;
		}
    }
}
