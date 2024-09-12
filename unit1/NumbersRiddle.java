/*=============================================================================
 |   Assignment:  Project 1.6 Numbers Riddle
 |       Author:  Issai Catano
 |      
 |  Course Name:  AP Computer Science A
 |   Instructor:  Mr. Jonathan Virak
 |     Due Date:  unknown
 |
 |  Description:  The program asks for a number, and inputs it into a series
 |                of arithmetic operations that can be described as a function
 |                where `f(x) = (2x + 6) / 2 - x`. The program goes through
 |                each operation individually, and proves it always evaluates
 |                to `f(x) = 3` when the number given is substituted for x.
 |
 |     Language:  [java]           
 |                
 | Deficiencies:  None, the program runs correctly and implements fail safety
 *===========================================================================*/

import java.util.function.Function;
import java.util.Scanner;

public class NumbersRiddle {
    // define our Scanner to recieve input and number for the riddle itself
    private static Scanner console = new Scanner(System.in);
    private static double number;

    public static void main(String[] args) {
        // hook to the riddle
        System.out.println("choose any number, double it, add six, divide it in half, and subtract the number you started with. the answer is always three!");
        // keep asking the user until they input a number (or something that evaluates to a double)
        double originalNum = askQuestion("welcome to the numbers riddle! any number entered will result in three! enter a starting number to begin", Double::valueOf);
        // record the original unmodified number for the last step
        number = originalNum;
        
        
        // follow the steps described with the help of `evalNumberExpr` to explain it to the user
        // multiply by 2
        evalNumberExpr("multiply", 2, '*', x -> x * 2);
        // add by 6
        evalNumberExpr("add", 6, '+', x -> x + 6);
        // divide by 2
        evalNumberExpr("divide", 2, '/', x -> x / 2);
        // and subtract by the original number
        evalNumberExpr("use the original number and subtract", originalNum, '-', x -> x - originalNum);
        
        // return the result of the arithmetic sequence, which is three
        System.out.println("voila! you ended up with " + number);
    }

    // describes an arithmetic operation to the riddle number provided at the start
    // `arithmeticDesc` is the word to describe the operation
    // `quantity` is what the number will be evaluated against (what is on the right of the operation)
    // `exprType` is the math symbol for the operation
    // `expr` is a lambda function which applies the operation to number
    private static void evalNumberExpr(String arithmeticDesc, double quantity, char exprType, Function<Double, Double> expr) {
        // first tell that itll <arithmeticDesc> <number> by <quantity>
        System.out.println(String.format("lets %s %2$.1f by %3$.1f", arithmeticDesc, number, quantity));
        // then print out the first half of the equation, <number> <exprType> <quantity> = ...
        System.out.print(String.format("%1$.1f %2$s %3$.1f = ", number, exprType, quantity));
        number = expr.apply(number);
        // then fill out the result, or what number is now after the `expr` was applied
        System.out.println(String.format("%.2f", number));
        System.out.print('\n');

        // to not flood the outputs, a question is asked to pause the output
        askQuestion("makes sense right? (enter to continue)", r -> true);
    }

    // generic way to ask a question through the output
    // `question` is what the user reads and responds to
    // `check` is given the user input and attempts to validate 
    //         it through a function. the function must not crash to pass
    private static <T> T askQuestion(String question, Function<String, T> check) {
        System.out.println(question);

        // repeatedly ask the question again until `check` doesnt crash
        while (true) {
            System.out.print("> ");
            String response = console.nextLine();
            System.out.print('\n');
            
            try {
                return check.apply(response);

            } catch (Exception err) {
                System.out.println("Invalid input");
            }
        }

    }
}
