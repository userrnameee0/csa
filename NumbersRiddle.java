// NumbersRiddle, but uses f(x) = (x + 5) * 2 - x - x = 10
// Unit concept related comments start with # CONCEPT #

import java.util.function.Function;
import java.util.Scanner;

public class NumbersRiddle {
    // define our Scanner to recieve input and number for the riddle itself
    // # CONCEPT # use of primitive (double) and non-primitive (Scanner) types
    private static Scanner console = new Scanner(System.in);
    private static double number;

    public static void main(String[] args) {
        // intro/hook to the riddle
        System.out.println("choose any number, add five, double it, then subtract your original number twice. its always 10!");
        
        // keep asking the user until they input a number (something that evaluates to a double)
        double originalNum = askQuestion("welcome to the numbers riddle! enter a starting number to begin", Double::valueOf);
        number = originalNum;
        
        // follow the steps described to prove it to the user
        add(5); mul(2); add(-originalNum); add(-originalNum);
        
        // return the result of the arithmetic sequence, which should be three
        System.out.println("voila! you ended up with " + Math.round(number));
    }
    
    // describes the process of addition/subtraction depending on the value
    private static void add(double quantity) {
        // # CONCEPT # use of boolean expressions (quantity > 0) and if statements (if (...) {...} else {...})
        if (quantity > 0) {
            evalStartOfNumberExpr("add", quantity, '+');
        } else {
            // inputs are reversed because `x - num` is better than `x + -num`
            evalStartOfNumberExpr("subtract", -quantity, '-');
        }

        number += quantity;
        evalEndOfNumberExpr();
    }

    // describes the process of multiplication/division depending on the value
    private static void mul(double quantity) {
        if (quantity > 1) {
            evalStartOfNumberExpr("multiply", quantity, '*');
            
        } else {
            // inputs are reversed because `x / num` is better than `x * 1/num`
            evalStartOfNumberExpr("divide", 1.0 / quantity, '/');
        }

        number *= quantity;
        evalEndOfNumberExpr();
    }
    
    // describes the first part of the arithmetic operation to the riddle number
    // the parameters fill in the blanks of the equation
    private static void evalStartOfNumberExpr(String arithmeticDesc, double quantity, char exprType) {
        // # CONCEPT # use of objects (String) and methods (String.format())
        // first tell that itll '<arithmeticDesc> <number> by <quantity>'
        System.out.println(String.format("lets %s %2$.1f by %3$.1f", arithmeticDesc, number, quantity));

        // then print out the first half of the equation, '<number> <exprType> <quantity> = ...'
        System.out.print(String.format("%1$.1f %2$s %3$.1f = ", number, exprType, quantity));
    }
    
    // prints out the 2nd half of the equation from `evalStartOfNumberExpr` after the number is updated
    // to not flood the outputs, a question is asked to pause it
    private static void evalEndOfNumberExpr() {
        System.out.println(String.format("%.1f", number));
        System.out.print('\n');

        askQuestion("makes sense right? (enter to continue)", r -> true);
    }

    // generic way to ask a `question` through the output
    // when the response is `check`ed, the function must not crash and return the answer
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
