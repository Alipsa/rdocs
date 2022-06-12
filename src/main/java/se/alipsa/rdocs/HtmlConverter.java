package se.alipsa.rdocs;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class HtmlConverter {

  public void convert(Document doc, Writer output) throws IOException, DocumentException {

    var htmlDoc = DocumentHelper.createDocument();
    htmlDoc.addProcessingInstruction("DOCTYPE", "html PUBLIC '-//W3C//DTD XHTML 1.1//EN' 'http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd'");
    var html = htmlDoc.addElement("html");
    html.addNamespace("xmlns", "http://www.w3.org/1999/xhtml");
    var head = html.addElement("head");
    head.addElement("title").addText("R documentation");
    var body = html.addElement("body");

    var root = doc.getRootElement();
    for (int i = 0, size = root.nodeCount(); i < size; i++) {
      Element element = (Element) root.node(i);
      if ("function".equals(element.getName())) {
        var function = body.addElement("div");
        function.addAttribute("class", "function");
        function.addElement("h2").addText(element.attributeValue("name"));
        var description = element.element("description");
        if (description != null) {
          function.addElement("p").addText(description.getText());
        }
        function.addElement("h3").addText("Usage");
        function.addElement("code").addText(element.attributeValue("name") + "(" + element.attributeValue("signature") + ")<br/>");
        var params = element.elements("param");
        if (params != null && params.size() > 0) {
          function.addElement("h3").addText("Arguments");
          for (var param : params) {
            function.addElement("b").addAttribute("class", "attributeName").addText(param.attributeValue("name"));
            function.addText(param.getText()).addElement("br");
          }
        }
        var value = element.element("return");
        if (value != null) {
          function.addElement("h3").addText("Return value");
          try(StringWriter writer = new StringWriter()) {
            value.write(writer);
            function.addElement("p").setContent(DocumentHelper.parseText(writer.toString()).getRootElement().content());
          }
        }

        var examples = element.element("examples");
        if (examples != null) {
          function.addElement("h3").addText("Examples");
          function.addElement("pre").addText(examples.getText());
        }
      }
    }
    OutputFormat format = OutputFormat.createPrettyPrint();
    format.setXHTML(true);
    StringWriter content = new StringWriter();
    var writer = new XMLWriter(content, format);
    writer.write(htmlDoc);
    writer.close();
    String resolvedContent = content.toString()
        .replace("&lt;", "<")
        .replace("&gt;", ">");
    output.write(resolvedContent);
    writer.close();
  }
}
