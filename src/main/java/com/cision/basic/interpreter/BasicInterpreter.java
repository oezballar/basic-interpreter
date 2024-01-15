package com.cision.basic.interpreter;

import com.cision.basic.ASTAddOperator;
import com.cision.basic.ASTAssignment;
import com.cision.basic.ASTCondition;
import com.cision.basic.ASTEndLine;
import com.cision.basic.ASTEqualOperator;
import com.cision.basic.ASTExpression;
import com.cision.basic.ASTFactor;
import com.cision.basic.ASTGoto;
import com.cision.basic.ASTIf;
import com.cision.basic.ASTInput;
import com.cision.basic.ASTInteger;
import com.cision.basic.ASTMultOperator;
import com.cision.basic.ASTNumber;
import com.cision.basic.ASTNumberVariable;
import com.cision.basic.ASTPrintBlock;
import com.cision.basic.ASTPrintSatement;
import com.cision.basic.ASTProgram;
import com.cision.basic.ASTQuoatedString;
import com.cision.basic.ASTRandom;
import com.cision.basic.ASTRelationOperator;
import com.cision.basic.ASTStatementLine;
import com.cision.basic.ASTString;
import com.cision.basic.ASTStringCondition;
import com.cision.basic.ASTStringVariable;
import com.cision.basic.ASTTab;
import com.cision.basic.ASTTerm;
import com.cision.basic.ASTValue;
import com.cision.basic.ASTVariable;
import com.cision.basic.MinimalBasicParserVisitor;
import com.cision.basic.SimpleNode;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class BasicInterpreter implements MinimalBasicParserVisitor {

   Deque<Object> runtimeStack = new LinkedList<>();
   Map<String, Object> variables = new HashMap<>();

   @Override
   public Object visit(SimpleNode node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTProgram node, Object data) {
      System.out.print("\033[H\033[2J");
      System.out.flush();
      var lineIndex = 0;
      while (true) {
         var statement = getNextStatementByLineIndex(lineIndex, data);
         lineIndex = Integer.parseInt((String) statement.jjtGetValue()) + 1;
         if (statementIsEnd(statement)) {
            System.out.println("Program terminated");
            break;
         }
         if (statementIsGoto(statement)) {
            lineIndex = getLineNumberGotoIsPointing(statement);
            continue;
         }
         if (statementIsIf(statement)) {
            statement.childrenAccept(this, data);
            lineIndex = getLineNumberIfIsPointing(statement, lineIndex);
            continue;
         }
         statement.childrenAccept(this, data);
      }
      return null;
   }

   @Override
   public Object visit(ASTStatementLine node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTEndLine node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTAssignment node, Object data) {
      var variable = (String) node.jjtGetValue();
      var object = node.childrenAccept(this, data);
      var value = (Double) runtimeStack.pop();
      variables.put(variable, value);
      return object;
   }

   @Override
   public Object visit(ASTExpression node, Object data) {
      var object = node.childrenAccept(this, data);
      var value = (Double) runtimeStack.pop();
      var valueCount = node.jjtGetNumChildren()/2;
      for (int i = 0; i < valueCount ; i++) {
         var operator = runtimeStack.pop();
         if (operator.equals("+")) {
            value = (Double) runtimeStack.pop() + value;
         }
         else {
            value = (Double) runtimeStack.pop() - value;
         }
      }
      runtimeStack.push(value);
      return object;
   }

   @Override
   public Object visit(ASTAddOperator node, Object data) {
      runtimeStack.push(node.jjtGetValue());
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTTerm node, Object data) {
      var object = node.childrenAccept(this, data);
      var value = (Double) runtimeStack.pop();
      var valueCount = node.jjtGetNumChildren()/2;
      for (int i = 0; i < valueCount ; i++) {
         var operator = runtimeStack.pop();
         if (operator.equals("*")) {
            value = (Double) runtimeStack.pop() * value;
         }
         else {
            value = (Double) runtimeStack.pop() / value;
         }
      }
      runtimeStack.push(value);
      return object;
   }

   @Override
   public Object visit(ASTMultOperator node, Object data) {
      runtimeStack.push(node.jjtGetValue());
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTFactor node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTValue node, Object data) {
      var object = node.childrenAccept(this, data);
      var value = runtimeStack.pop();
      if (value instanceof String) {
         value = variables.get(value);
      }
      runtimeStack.push(value);
      return object;
   }

   @Override
   public Object visit(ASTNumber node, Object data) {
      runtimeStack.push(Double.valueOf((String) node.jjtGetValue()));
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTRandom node, Object data) {
      var object =  node.childrenAccept(this, data);
      runtimeStack.pop();
      runtimeStack.push(Math.random());
      return object;
   }

   @Override
   public Object visit(ASTInteger node, Object data) {
      var object = node.childrenAccept(this, data);
      var value = (Double) runtimeStack.pop();
      runtimeStack.push(Math.floor(value));
      return object;
   }

   @Override
   public Object visit(ASTPrintSatement node, Object data) {
      var numChildren = node.jjtGetNumChildren();
      var object = node.childrenAccept(this, data);
      StringBuilder s = new StringBuilder();
      for (int i = 0; i < numChildren; i++) {
         s.insert(0, getPop() + " ");
      }
      System.out.println(s);
      return object;
   }

   private Object getPop() {
      var pop = runtimeStack.pop();
      if (pop instanceof Double) {
         return String.format("%,.0f", pop);
      }
      return pop;
   }

   @Override
   public Object visit(ASTPrintBlock node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTQuoatedString node, Object data) {
      node.childrenAccept(this, data);
      var object = node.childrenAccept(this, data);
      var str = (String) node.jjtGetValue();
      str = str.substring(1, str.length() - 1);
      runtimeStack.push(str);
      return object;
   }

   @Override
   public Object visit(ASTTab node, Object data) {
      var object = node.childrenAccept(this, data);
      var numTabs = Integer.parseInt((String) node.jjtGetValue());
      runtimeStack.push("  ".repeat(Math.max(0, numTabs)));
      return object;
   }

   @Override
   public Object visit(ASTVariable node, Object data) {
      var variable = (String) node.jjtGetValue();
      runtimeStack.push(variables.get(variable));
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTGoto node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTIf node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTCondition node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTRelationOperator node, Object data) {
      runtimeStack.push(node.jjtGetValue());
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTEqualOperator node, Object data) {
      runtimeStack.push(node.jjtGetValue());
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTStringCondition node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTString node, Object data) {
      var object = node.childrenAccept(this, data);
      if (node.jjtGetChild(0) instanceof ASTStringVariable) {
         var variable = (String) runtimeStack.pop();
         runtimeStack.push(variables.get(variable));
      }
      return object;
   }

   @Override
   public Object visit(ASTInput node, Object data) {
      Scanner in = new Scanner(System.in);
      var object = node.childrenAccept(this, data);
      var variable = runtimeStack.pop();
      System.out.println(runtimeStack.pop());
      if (node.jjtGetChild(1) instanceof ASTNumberVariable) {
         var i = in.nextInt();
         variables.put((String) variable, (double) i);
      }
      else {
         var s = in.nextLine();
         variables.put((String) variable, s);
      }
      return object;
   }

   @Override
   public Object visit(ASTStringVariable node, Object data) {
      runtimeStack.push(node.jjtGetValue());
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTNumberVariable node, Object data) {
      runtimeStack.push(node.jjtGetValue());
      return node.childrenAccept(this, data);
   }

   private boolean statementIsEnd(SimpleNode statement) {
      return statement instanceof ASTEndLine;
   }

   private boolean statementIsGoto(SimpleNode statement) {
      return statement.jjtGetChild(0) instanceof ASTGoto;
   }

   private boolean statementIsIf(SimpleNode statement) {
      return statement.jjtGetChild(0) instanceof ASTIf;
   }

   private int getLineNumberGotoIsPointing(SimpleNode statement) {
      return Integer.parseInt((String) ((ASTGoto) statement.jjtGetChild(0)).jjtGetValue());
   }

   private int getLineNumberIfIsPointing(SimpleNode statement, int lineIndex) {
      var right = runtimeStack.pop();
      var operator = (String) runtimeStack.pop();
      var left = runtimeStack.pop();
      switch (operator) {
         case "=":
            if (Objects.equals(left, right)) {
               return Integer.parseInt((String) ((ASTIf) statement.jjtGetChild(0)).jjtGetValue());
            }
            break;
         case "<>":
            if (!Objects.equals(left, right)) {
               return Integer.parseInt((String) ((ASTIf) statement.jjtGetChild(0)).jjtGetValue());
            }
            break;
         case "<":
            if ((Double) left < (Double) right) {
               return Integer.parseInt((String) ((ASTIf) statement.jjtGetChild(0)).jjtGetValue());
            }
            break;
         case ">":
            if ((Double) left > (Double) right) {
               return Integer.parseInt((String) ((ASTIf) statement.jjtGetChild(0)).jjtGetValue());
            }
            break;
         case "<=":
            if ((Double) left <= (Double) right) {
               return Integer.parseInt((String) ((ASTIf) statement.jjtGetChild(0)).jjtGetValue());
            }
            break;
         case ">=":
            if ((Double) left >= (Double) right) {
               return Integer.parseInt((String) ((ASTIf) statement.jjtGetChild(0)).jjtGetValue());
            }
            break;
      }
      return lineIndex;
   }

   private SimpleNode getNextStatementByLineIndex(int lineIndex, Object data) {
      for (int i = lineIndex; true; i++) {
         if (((HashMap<Integer, SimpleNode>) data).containsKey(i)) {
            return ((HashMap<Integer, SimpleNode>) data).get(i);
         }
      }
   }
}
