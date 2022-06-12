package test.alipsa.rdocs;

import org.dom4j.DocumentException;
import org.junit.jupiter.api.Test;
import se.alipsa.rdocs.DocGenerator;
import se.alipsa.rdocs.DocStyle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

public class AddTest {

  @Test
  public void testAdd() throws IOException, DocumentException {
    File rCode = new File(getClass().getResource("/add.R").getFile());
    DocGenerator generator = new DocGenerator(DocStyle.ROXYGEN2);
    try(StringWriter sw = new StringWriter(); FileWriter fileWriter = new FileWriter("./target/add.html")) {
      generator.generate(rCode, sw);
      System.out.println(sw);
      fileWriter.write(sw.toString());
    }
  }
}
