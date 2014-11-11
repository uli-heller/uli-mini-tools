package org.uli.asciidoctor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.asciidoctor.Asciidoctor.Factory;
import org.asciidoctor.Asciidoctor;

public class Main {
  private static final Map<String,Object> EMPTY_MAP=new HashMap<String,Object>();
  public static void main(String[] args) {
    int result = Main.run(args);
    if (result != 0) {
      System.exit(result);
    }
  }
  
  public static int run(String[] args) {
    int result = 0;
    Asciidoctor asciidoctor = Factory.create();
    String rendered = asciidoctor.renderFile(new File(args[0]), EMPTY_MAP);
    System.out.println(rendered);
    return result;
  }  
}
