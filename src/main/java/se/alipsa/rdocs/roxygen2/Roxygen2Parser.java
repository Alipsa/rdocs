package se.alipsa.rdocs.roxygen2;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Text;
import se.alipsa.rdocs.DocParser;

import java.io.*;

public class Roxygen2Parser implements DocParser {

  Element root;
  boolean functionStarted = false;
  boolean examplesStarted = false;
  Element function;
  Element examples;

  @Override
  public Document parse(File rCode) throws IOException {
    var doc = DocumentHelper.createDocument();
    root = doc.addElement("rdoc");
    try(var reader = new BufferedReader(new FileReader(rCode))) {
      String line;
      int fileLine = 0;
      int rDocLine = 0;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        fileLine++;
        if (line.startsWith("#' ")) {
          processRdocLine(line, fileLine, rDocLine++);
        } else if (functionStarted) {
          processFunctionLine(line, fileLine, rDocLine);
        }
      }
    }
    return doc;
  }

  private void processFunctionLine(String line, int fileLine, int rDocLine) {
    if (line.contains("<-")) {
      String functionName = line.substring(0, line.indexOf("<-")).trim();
      String signature = line.substring(line.indexOf('(')+1, line.indexOf(')'));
      function.addAttribute("name", functionName);
      function.addAttribute("signature", signature);
      examplesStarted = false;
      functionStarted = false;
    }
  }

  private void processRdocLine(String line, int fileLine, int rdocLine) {
    String rdocPart = line.substring("#' ".length());
    if (!functionStarted) {
      function = root.addElement("function");
      functionStarted = true;
    }
    if (rdocPart.startsWith("@")) {
      if (rdocPart.startsWith("@param")) {
        String paramPart = rdocPart.substring("@param ".length());
        var param = function.addElement("param");
        param.addAttribute("name", paramPart.substring(0, paramPart.indexOf(' ')));
        param.addText(paramPart.substring(paramPart.indexOf(' ')));
      } else if (rdocPart.startsWith("@return")) {
        function.addElement("return").addText(translateFormats(rdocPart.substring("@return ".length())));
      } else if (rdocPart.startsWith("@examples")) {
        examples = function.addElement("examples");
        examplesStarted = true;
      }
    } else if (examplesStarted) {
      examples.addText(translateFormats(rdocPart) + "<br />");
    } else {
      function.addElement("description").addText(rdocPart);
    }
  }

  private String translateFormats(String rdocPart) {
    String translated = rdocPart;
    while (translated.contains("\\code{")) {
      int beginIdx = translated.indexOf("\\code{");
      int endIdx = translated.substring(beginIdx).indexOf('}');
      translated = translated.substring(0,beginIdx) + "<code>"
          + translated.substring(beginIdx + "\\code{".length(), beginIdx + endIdx)
          + "</code>"
          + translated.substring(beginIdx + endIdx +1);
    }
     return translated;
  }
}
