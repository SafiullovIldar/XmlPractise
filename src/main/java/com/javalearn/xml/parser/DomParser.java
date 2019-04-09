package com.javalearn.xml.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;


public class DomParser {

    public String parse(File file) {

        Document document = createDocument(file);
        DOMSource domSource = new DOMSource(document);

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);

        transformDocument(domSource, result);

        return writer.toString();
    }

    public void createValidBook() {

        Document document = createDocument();

        Element rootElement = document.createElement("Books");
        document.appendChild(rootElement);

        Element book = document.createElement("Book");

        Element numberOfPages = document.createElement("numberOfPages");
        numberOfPages.setTextContent(String.valueOf(600));
        book.appendChild(numberOfPages);

        Element name = document.createElement("name");
        name.setTextContent("Master and Margarita");
        book.appendChild(name);

        Element publisher = document.createElement("publisher");
        publisher.setTextContent("O'Reilly");
        book.appendChild(publisher);

        Element author = document.createElement("Author");

        Element firstName = document.createElement("firstName");
        firstName.setTextContent("Mikhail");
        author.appendChild(firstName);

        Element lastName = document.createElement("lastName");
        lastName.setTextContent("Bulgakov");
        author.appendChild(lastName);

        Element secondName = document.createElement("secondName");
        secondName.setTextContent("Afanasyevich");
        author.appendChild(secondName);

        rootElement.appendChild(book);
        book.appendChild(author);

        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(
                System.getProperty("user.home") + File.separator + "validBooks.xml"));

        transformDocument(source, result);
    }

    public void createInvalidBook() {

        Document document = createDocument();

        Element rootElement = document.createElement("InvalidBooks");
        document.appendChild(rootElement);

        Element book = document.createElement("InvalidBook");
        Element numberOfPages = document.createElement("InvalidNumberOfPages");
        numberOfPages.setTextContent(String.valueOf(600));

        book.appendChild(numberOfPages);
        rootElement.appendChild(book);

        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(
                System.getProperty("user.home") + File.separator + "invalidBooks.xml"));

        transformDocument(source, result);
    }

    public void modify(File file){

        Document document = createDocument(file);

        Node book = document.getElementsByTagName("PLANT").item(0);
        NodeList nodes = book.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {

            Node element = nodes.item(i);
            if ("COMMON".equals(element.getNodeName())) {
                element.setTextContent("somethingNew");
            }

            if ("AVAILABILITY".equals(element.getNodeName())) {
                book.removeChild(element);
            }
        }

        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(
                System.getProperty("user.home") + File.separator + "modifiedFile.xml"));

        transformDocument(source, result);

    }

    private Document createDocument(){

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return docBuilder.newDocument();
    }

    private Document createDocument(File file){

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    private void transformDocument(DOMSource source, StreamResult result){

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
