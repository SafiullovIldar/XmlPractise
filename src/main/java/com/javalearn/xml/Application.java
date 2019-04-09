package com.javalearn.xml;

import com.javalearn.xml.entity.Book;
import com.javalearn.xml.parser.DomParser;
import com.javalearn.xml.parser.StaxParser;
import com.javalearn.xml.validator.XmlValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Slf4j
public class Application {
    public static void main(String[] args) {

        XmlValidator validator = new XmlValidator();
        DomParser domParser = new DomParser();
        StaxParser staxParser = new StaxParser();

        /*1. Downloading file into memory and then saving it as a new file through logger.*/
        File xmlExample = null;
        try {
            URL exampleUrl = new URL("http://okitgo.ru/misc/xml/plant_catalog.xml");
            xmlExample = new File(System.getProperty("user.home") + File.separator + "example.xml");
            FileUtils.copyURLToFile(exampleUrl, xmlExample);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info(domParser.parse(xmlExample));

        /*2. Creating valid and invalid files, and then validating it using xsd schema.*/
        domParser.createValidBook();
        domParser.createInvalidBook();

        URL xsd = Application.class.getResource("/books.xsd");
        File validBooksFile = new File(System.getProperty("user.home") + File.separator + "validBooks.xml");
        File invalidBooksFile = new File(System.getProperty("user.home") + File.separator + "invalidBooks.xml");

        boolean validateValid = validator.validate(validBooksFile, xsd);
        boolean validateInvalid = validator.validate(invalidBooksFile, xsd);

        System.out.println("Validated valid document: " + validateValid);
        System.out.println("Validated invalid document: " + validateInvalid);

        /*3. Downloading file into memory, modifying and then saving it as a new file.*/
        File logFile = new File(System.getProperty("user.home") + File.separator + "log.xml");
        domParser.modify(logFile);


        List<Book> bookList = staxParser.parse(validBooksFile);
        for (Book book : bookList) {
            System.out.println(book.toString());
        }

    }
}
