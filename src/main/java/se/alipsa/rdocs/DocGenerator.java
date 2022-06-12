package se.alipsa.rdocs;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import se.alipsa.rdocs.roxygen2.Roxygen2Parser;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import static se.alipsa.rdocs.DocStyle.ROXYGEN2;

public class DocGenerator {

  DocParser docParser;
  HtmlConverter htmlConverter = new HtmlConverter();

  public DocGenerator(DocStyle docStyle) {

    if (docStyle == ROXYGEN2) {
      docParser = new Roxygen2Parser();
    } else {
      throw new RuntimeException("Sorry, Only ROXYGEN2 format support for now");
    }
  }

  public void generate(File rFile, Writer output) throws IOException, DocumentException {
    Document doc = docParser.parse(rFile);

    System.out.println("-------------");
    OutputFormat format = OutputFormat.createPrettyPrint();
    var writer = new XMLWriter(System.out, format);
    writer.write(doc);
    System.out.println("-------------");

    htmlConverter.convert(doc, output);
  }
}
