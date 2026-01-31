package org.example.series;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.nio.file.Path;
import java.util.Map;

public class XmlStatisticsWriter {

    public static void write(Map<String, Integer> stats, String attribute, Path outputFile) {
        try {
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .newDocument();

            Element root = doc.createElement("statistics");
            root.setAttribute("by", attribute); // можна залишити, це плюс
            doc.appendChild(root);

            for (Map.Entry<String, Integer> e : stats.entrySet()) {
                Element item = doc.createElement("item");

                Element value = doc.createElement("value");
                value.setTextContent(e.getKey());

                Element count = doc.createElement("count");
                count.setTextContent(String.valueOf(e.getValue()));

                item.appendChild(value);
                item.appendChild(count);

                root.appendChild(item);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            transformer.transform(new DOMSource(doc), new StreamResult(outputFile.toFile()));
        } catch (Exception ex) {
            throw new RuntimeException("Failed to write XML: " + outputFile, ex);
        }
    }
}