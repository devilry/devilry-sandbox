
import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

class NodeHandler extends DefaultHandler {
	Stack<Integer> parentIdStack = new Stack<Integer>();
	
    public void startDocument() {}
    public void endDocument() {}

	public void startElement(String namespaceURI, String lname, String qname, Attributes attrs) {
		System.out.print("ClassName: " + lname);

		if(parentIdStack.empty())
			System.out.print(", parentId: -1");
		else
			System.out.print(", parentId: " + parentIdStack.peek());

		for(int i=0; i<attrs.getLength(); i++) {
			String localName = attrs.getLocalName(i);
			String value = attrs.getValue(i);

			System.out.print(", " + localName + ": " + value);

			if(localName.equals("id")) {
				parentIdStack.push(Integer.valueOf(value));
			}
		}

		System.out.println("");
	}

    public void endElement(String uri, String localName, String qName) {
    	parentIdStack.pop();
    }

    public void error (SAXParseException e) throws SAXException {
    	System.out.println(e.getMessage());
    	System.exit(-1);
    }
}

public class SAXExample1 {
	public static void main(String[] args) throws Exception {
		SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema s;

		s = sf.newSchema(new File("schemas/nodeTree.xsd"));

		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		spf.setSchema(s);
		SAXParser sp;

		sp = spf.newSAXParser();
		sp.parse(new File("schemas/test_tree.xml"), new NodeHandler());
	}
}
