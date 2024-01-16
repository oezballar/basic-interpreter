# A Basic Interpreter

## Introduction
The task of Puzzlee 2023 is to build a compiler or interpreter that can process one or more games written in Basic from the zip file Chris gave.
I decided to get the aceyducey.bas game running because the language elements used seemed easy to implement. To avoid making a considerable effort, I use the JavaCC parser generator, which provides me with a visitor interface to traverse the abstract syntax tree easily.
The grammar for autocreating the tokenizer and parser is in the src/main/jjtree minimal-basic-paerser.jjt file.

I have implemented two visitors. One first loads all Basic statements into a HashMap, which I then use in the second visitor, the interpreter,  to jump back and forth between the lines when using the GOTO or IF statements.

## Get the game running
To get the game running, you need to clone the repository and run the following command in the root directory:
```mvn clean install```
This will create a jar file in the target directory. To run the game, you need to execute the following command:
```java -jar target/interpreter-0.0.1-SNAPSHOT.jar src/main/resources/aceyducey.bas```
