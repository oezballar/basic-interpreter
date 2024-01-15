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
import java.util.HashMap;

public class StatementArrayCreator implements MinimalBasicParserVisitor {

   @Override
   public Object visit(SimpleNode node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTProgram node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTStatementLine node, Object data) {
      ((HashMap<Integer, SimpleNode>) data).put(Integer.valueOf((String) node.jjtGetValue()) , node);
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTEndLine node, Object data) {
      ((HashMap<Integer, SimpleNode>) data).put(Integer.valueOf((String) node.jjtGetValue()) , node);
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTAssignment node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTExpression node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTAddOperator node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTTerm node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTMultOperator node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTFactor node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTValue node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTNumber node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTRandom node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTInteger node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTPrintSatement node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTPrintBlock node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTQuoatedString node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTTab node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTVariable node, Object data) {
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
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTEqualOperator node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTStringCondition node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTString node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTInput node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTStringVariable node, Object data) {
      return node.childrenAccept(this, data);
   }

   @Override
   public Object visit(ASTNumberVariable node, Object data) {
      return node.childrenAccept(this, data);
   }
}
