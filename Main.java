/** @file   Main.java
 *  @brief  Driver code for the Infix Calculator. Contains methods to parse lines
 *          from input file, convert it to an object array containing operands and
 *          operators in infix manner, convert infix expressions to postfix 
 *          expressions, and evaluate postfix expressions.
 *  @author Mustafa Siddiqui
 *  @date   03/01/2021
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {
    /*
        Decimal number format specifier.
    */
    private static DecimalFormat df2 = new DecimalFormat("0.00");

    /*
        Main method of the infix calculator. Reads file, converts infix expressions
        to postfix expressions, evaluates them, and prints them in output file
        named 'output.txt'.
    */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // exit if no input file specified
        if (args.length == 0) {
            System.out.println("Usage: java Main <filename.txt>");
            System.exit(0);
        }

        // open input file stream
        try (FileInputStream inputFile = new FileInputStream(args[0])) {
            Scanner input = new Scanner(inputFile);

            // create output file & set output stream
            PrintStream out = new PrintStream(new File("out.txt"));
            PrintStream console = System.out;
            System.setOut(out);

            while (input.hasNextLine()) {
                // get line and convert to array
                String temp = input.nextLine();
                String[] line = splitLine(temp);
                int numOfParenthesis = numParenthesis(line);
                
                // convert elements into integer, double, (operands) or string (operators) types
                Object[] expression = convertInput(line, numOfParenthesis);
                
                // call infix to postfix fn
                Object[] expr = infix2postfix(expression, numOfParenthesis);

                // if invalid expression, move to next (error message displayed in method)
                if (expr == null)
                    continue;
                
                // call postfix eval fn
                Double result = postfixEval(expr);
                
                // print in output file upto 2 decimal places
                System.out.println(df2.format(result.doubleValue()));
            }

            // reset output stream to stdout and close scanner
            System.setOut(console);
            input.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("'" + args[0] + "' does not exist.");
        }
    }

    /*
        Method returns the number of parenthesis in the string array
        parsed from input file.
    */
    public static int numParenthesis(String[] strings) {
        int length = 0;
        for (String s : strings) {
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '(')
                    length++;
                if (s.charAt(i) == ')')
                    length++;
            }
        }

        return length;
    }

    /*
        Takes in a sentence read from file and returns a string array
        of which each element is an operator or an operand.
    */
    public static String[] splitLine(String sentence) {
        // remove spaces from string
        String line = sentence.replaceAll("\\s", "");
        StringBuilder temp = new StringBuilder();

        // iterate over characters while adding spaces between operands and operators
        boolean passed = false;
        for (int i = 0; i < line.length(); i++) {
            if ((line.charAt(i) >= '0' && line.charAt(i) <= '9') || (line.charAt(i) == '.')) {
                temp.append(line.charAt(i));
                passed = true;
                continue;
            }
            if (passed) {
                temp.append(" ");
                passed = false;
            }
                
            temp.append(line.charAt(i));
            temp.append(" ");
        }

        return temp.toString().split(" ");
    }

    /*  
        Method returns an object array with each element being an
        operand or operator.
    */
    public static Object[] convertInput(String[] line, int numOfParenthesis) {
        // create array accounting for any parenthesis
        Object[] expression = new Object[line.length];

        // separate operands from operators and store in array
        // tries for int, if not then for double, if not then for string
        // string has two cases - operator or parenthesis with operand
        int i = 0;
        for (String s : line) {
            try {
                expression[i] = Integer.parseInt(s);
            }
            catch (NumberFormatException e) {
                try {
                    expression[i] = Double.parseDouble(s);
                }
                catch (NumberFormatException e1) {
                    expression[i] = s;
                }
            }
            i++;
        }

        return expression;
    }

    /*
        Method returns the precedence of operator provided as argument.
        Returns -1 if operator not valid.
    */
    public static int precedence(Object operator) {
        Object[] operatorList = {"!", "|", "&", "=", "<", ">", "-", "+", "/", "*", "^", "(", ")"};

        for (int i = 0; i < operatorList.length; i++) {
            if (operator.equals(operatorList[i]))
                return i;
        }

        return -1;
    }

    /*
        Converts infix expressions to postfix (Reverse Polish) expressions.
        Takes and returns object arrays which hold operators and operands.
    */
    public static Object[] infix2postfix(Object[] infixExpr, int numOfParenthesis) {
        Stack stack = new Stack();
        Queue queue = new Queue();
        boolean stackEmpty = true;

        for (Object c : infixExpr) {
            // add to queue if operand
            if (c instanceof Integer || c instanceof Double)
                queue.enqueue(c);
            
            // if operator
            else if (c instanceof String) {
                int curr = precedence(c);

                // if present in list of operators and not a parenthesis ')'
                if (curr != -1 && curr < 12) {
                    if (stackEmpty) {
                        stack.push((Object)c);
                        stackEmpty = false;
                    }
                    else {
                        // check precedence of operator on stack
                        Object topOfStack = null;
                        int prev = -1;
                        try {
                            topOfStack = stack.pop();
                            prev = precedence((String)topOfStack);
                        }
                        catch (NullPointerException e) {
                            prev = -1;
                        }

                        // add to queue until a lower precedence operator is found on stack
                        boolean flag = false;
                        while ((prev > curr) && (prev != 11) && !stackEmpty) {
                            queue.enqueue(topOfStack);
                            try {
                                topOfStack = stack.pop();
                            }
                            catch (NullPointerException e1) {
                                flag = true;
                                break;
                            }
                            prev = precedence((String)topOfStack);
                        }

                        // push only if top of stack is not null and has not been added to the queue
                        if (topOfStack != null && flag == false)
                            stack.push(topOfStack);
                        stack.push((Object)c);

                        // update stack empty flag
                        if (stack.isEmpty())
                            stackEmpty = true;
                    }
                }
                // if a closing parenthesis ')' put items in queue until opening
                // parenthesis '(' is reached and discard the parenthesis
                else if (curr == 12) {
                    Object temp = stack.pop();
                    while (temp.equals("(") == false) {
                        queue.enqueue(temp);
                        temp = stack.pop();
                    }
                }
                // operator not supported
                else {
                    System.out.println("Invalid expression. Skipping..");
                    return null;
                }
            }
        }

        // put items from queue and stack into object array in order
        Object[] postfixExpr = new Object[infixExpr.length - numOfParenthesis];
        for (int i = 0; i < (infixExpr.length - numOfParenthesis); i++) {
            try {
                postfixExpr[i] = queue.dequeue();
            }
            catch (NullPointerException e) {
                // queue empty so empty stack now
                try {
                    postfixExpr[i] = stack.pop();
                    continue;
                }
                catch (NullPointerException s) {
                    break;
                }
            }
        }

        return postfixExpr;
    }

    /*
        Evaluates postfix (Reverse Polish) expressions and returns the result
        of the expression as a double number.
    */
    public static Double postfixEval(Object[] postfix) {
        Stack stack = new Stack();

        Double right = null;
        Double left = null;
        Double result = null;

        for (Object element : postfix) {
            if (element instanceof String) {
                if (precedence(element) > 0) {
                    Number rightNum = (Number)stack.pop();
                    Number leftNum = (Number)stack.pop();

                    // convert operands into Double objects
                    if (rightNum instanceof Double)
                        right = rightNum.doubleValue();
                    else
                        right = (double)rightNum.intValue();

                    if (leftNum instanceof Double)
                        left = leftNum.doubleValue();
                    else
                        left = (double)leftNum.intValue();

                    // perform operations depending on operator
                    if (element.equals("+"))            // addition
                        result = left + right;
                    else if (element.equals("-"))       // subtraction
                        result = left - right;
                    else if (element.equals("*"))       // multiplication
                        result = left * right;
                    else if (element.equals("/"))       // division
                        result = left / right;
                    else if (element.equals("^"))       // exponentiation
                        result = Math.pow(left, right);
                    else if (element.equals(">")) {     // greater than
                        if (left > right)
                            result = 1.0;
                        else
                            result = 0.0;
                    }
                    else if (element.equals("<")) {     // less than
                        if (left < right)
                            result = 1.0;
                        else
                            result = 0.0;
                    }
                    else if (element.equals("=")) {     // equals to
                        if (left.equals(right))
                            result = 1.0;
                        else
                            result = 0.0;
                    }
                    else if (element.equals("&")) {     // logical AND
                        if ((left > 0) && (right > 0))
                            result = 1.0;
                        else
                            result = 0.0;
                    }
                    else if (element.equals("|")) {     // logical OR
                        if ((left > 0) || (right > 0))
                            result = 1.0;
                        else
                            result = 0.0;
                    }
                    else {
                        System.out.println("Invalid operator");
                        System.exit(0);
                    }
                    stack.push(result);
                }
                // logical NOT operator: ! (precedence = 0)
                else {
                    Number operand = (Number)stack.pop();
                    Double num = null;
                    if (operand instanceof Double)
                        num = operand.doubleValue();
                    else
                        num = (double)operand.intValue();

                    result = 0.0;
                    if (num == 0) {
                        result = 1.0;
                    }
                    stack.push(result);
                }
            }
            else {
                stack.push(element);
            }
        }

        return (Double)stack.pop();
    }
}
