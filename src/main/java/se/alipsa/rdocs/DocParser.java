package se.alipsa.rdocs;

import org.dom4j.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface DocParser {
  Document parse(File rCode) throws IOException;
}
