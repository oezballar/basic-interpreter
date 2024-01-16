package com.cision.basic.interpreter;

import com.cision.basic.MinimalBasicParser;
import com.cision.basic.ParseException;
import com.cision.basic.SimpleNode;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InterpreterApplication {

   public static void main(String[] args) throws FileNotFoundException, ParseException {
      SpringApplication.run(InterpreterApplication.class, args);

      Map<Integer, SimpleNode> statementArray = new HashMap<>();

      var stream = new FileInputStream(args[0]);
      var parser = new MinimalBasicParser(stream, "utf-8");
      var ast = parser.program();

      ast.jjtAccept(new StatementArrayCreator(), statementArray);
      ast.jjtAccept(new BasicInterpreter(), statementArray);

      System.exit(0);
   }

}
