package com.javalearn.xml.parser;

import com.javalearn.xml.entity.Author;
import com.javalearn.xml.entity.Book;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class StaxParser {

    private static boolean numberOfPagesFlag;
    private static boolean nameFlag;
    private static boolean publisherFlag;
    private static boolean authorFlag;
    private static boolean authorFirstNameFlag;
    private static boolean authorLastNameFlag;
    private static boolean authorSecondNameFlag;


    public List<Book> parse(File fileName) {
        List<Book> bookList = new ArrayList<>();
        Book book = null;
        Author author = null;

        XMLInputFactory factory = XMLInputFactory.newInstance();
        try {
            XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(fileName));

            int event = reader.getEventType();

            while (true) {
                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:

                        switch (reader.getLocalName()) {
                            case "Book":
                                book = new Book();
                                break;
                            case "numberOfPages":
                                numberOfPagesFlag = true;
                                break;
                            case "name":
                                nameFlag = true;
                                break;
                            case "publisher":
                                publisherFlag = true;
                                break;
                            case "Author":
                                author = new Author();
                                authorFlag = true;
                                break;
                            case "firstName":
                                authorFirstNameFlag = true;
                                break;
                            case "lastName":
                                authorLastNameFlag = true;
                                break;
                            case "secondName":
                                authorSecondNameFlag = true;
                                break;
                        }
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        if (numberOfPagesFlag) {
                            book.setNumberOfPages(Integer.parseInt(reader.getText()));
                            numberOfPagesFlag = false;
                        } else if (nameFlag) {
                            book.setName(reader.getText());
                            nameFlag = false;
                        } else if (publisherFlag) {
                            book.setPublisher(reader.getText());
                            publisherFlag = false;
                        } else if (authorFirstNameFlag) {
                            author.setFirstName(reader.getText());
                            authorFirstNameFlag = false;
                        } else if (authorLastNameFlag) {
                            author.setLastName(reader.getText());
                            authorLastNameFlag = false;
                        } else if (authorSecondNameFlag) {
                            author.setSecondName(reader.getText());
                            authorSecondNameFlag = false;
                        } else if (authorFlag) {
                            book.setAuthor(author);
                            authorFlag = false;
                        }
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        if (reader.getLocalName().equals("Book")) {
                            bookList.add(book);
                        }
                        break;
                }
                if (!reader.hasNext())
                    break;

                event = reader.next();
            }

        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }
        return bookList;
    }
}

