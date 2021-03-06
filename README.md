# Infix Calculator

The program works by reading the input file line by line, convert the expression
into a postfix expression, and then evaluating that expression, and printing to
the output file (out.txt) line by line as well.

A line is read from the input file by using the Scanner class. The method
`splitLine()` takes in the line read as a string returns a string array with each
element being an operand or an operator. This works by first removing the spaces
from the string, and then inserting spaces using the StringBuilder class in a
manner that separates the operators and operands (integer and decimal) with a
space character and then splits this string using `String.split()` method.

The method `convertInput()` converts this string array into an object array where
the operands are instances of Integer or Double classes and the operators are
instances of the String class. This helps to carry out calculations later.

The method `infix2postfix()` takes in object array representing an infix expression
and returns an object array representing a postfix expression. This works by using
a stack and queue to implement the shunting yard algorithm. It goes through the 
infix object array element by element and if it encounters an operand, it enqueues
it in the queue, and if an operator, it pushes it onto the stack. Here, the
precedence of operators is taken care of. If the current operator is of lower
precedence than the operator on top of the stack, it pops the stack and enqeues
that element. It repeats this process until the top of the stack is an operator
with lower precedence or if the stack is empty. In a similar manner, parenthesis
are also handled. If a opening parenthesis '(' is detected, it pushes it to the
stack like a normal operator and keeps going until it detects the closing
parenthesis ')'. When it does that, it pops all elments on the stack and enqueues
them until the opening parenthesis is reached and then discards the parenthesis.
This method also deals with invalid operators. If an invalid operator is detected,
it prints an error message in the output file and returns null. This is then
detected in the main method which moves onto the next line. After reading the whole
line, it empties the queue into an object array first and then empties the stack
into the array, and this array is returned.

The `postfixEval()` method simply evaluates the postfix expression. It goes through the
postfix expression and adds the operands to a stack if detected. If an operator is
detected, it performs the respective operation. It pops two elements off the stack
if the operator requires it and one if the operator is a unary one. There is only one
unary operator supported which is the logical NOT operator (!). This method assumes
that the postfix expression is correct and does not account for any errors. If an
invalid postfix expression (incorrect order, or invalid operator etc) is given to it,
the program might give a null pointer exception (due to popping the stack) and crash.
But, the `infix2postfix()` method takes care of this, and guarantees error detection
so this method will always get a correct postfix expression.

The `precedence()` method returns the precedence of the operator and is designed as such
that it - and subsequently the whole program - can be modified to support another
operator given that it is a single char one like '+', '&' etc.

The sample test file which is used to test code is 'test_input.txt'.

List of files:
* Main.java
* Stack.java
* Queue.java
* LinkedList.java
* Node.java

* test_input.txt :	example test cases
* out.txt 	 :	output for 'test_input.txt'

