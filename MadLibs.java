// MadLibs.java

import java.util.Scanner;
import java.util.ArrayList;

public class MadLibs {
    // to request user input on madLib
    private static Scanner console = new Scanner(System.in);
    
    // the story itself, blank. just about an indecisive cat
    private static String templateMadLib = 
        "One day, a <color> cat tripped and got covered in dirty <something that makes a mess>! " +
        "Oh no! Thankfully, there was a <location> with a nearby pond. " +
        "It felt <positive emotion> to be able to cleanse itself. " +
        "But, as it walked and jumped through the <obstacles>, the cat had time to ponder. " +
        "It then thought \"wait, getting wet makes me feel <negative emotion>!\" " +
        "So the cat stood there, <adverb>, not knowing what to do.";
    
    // empty parts are dynamically interpreted and filled in
    // this means that as long as `templateMadLib` identifies blanks with brackets <>,
    // the madLib can be swapped and still work
    
    // stores user input
    private static ArrayList<String> spots = new ArrayList<>();
    // seperates madLib by its blank spots, indicated by a string wrapped in brackets <>
    private static String[] emptyMadLib = templateMadLib.split("<[^<>]*>");
    // the end result to be populated later on
    private static String completeMadLib = "";
    
    public static void main(String[] args) {
        System.out.println("hello! to fill out your madlib, please answer the following questions\n");
        
        // seperate the madLib by the parts that arent blanks, as a result giving just the blanks
        for (String identifier: templateMadLib.split("(>|^)[^<>]*(<|$)")) {
            if (identifier.isEmpty()) { continue; }
            // then ask the user to give what the blank specified
            spots.add(giveMe(identifier));
        }

        // fill madLib by combining the non-blank spots with the blank spots
        for (int i = 0; i < emptyMadLib.length; i++) {
            // use ternary operator to accomodate for the fact that `spots.size() == emptyMadLib.length - 1`
            completeMadLib += emptyMadLib[i] + (i != emptyMadLib.length - 1 ? spots.get(i) : "");
        }

        // show the finished madLib
        System.out.println("heres the finished result!\n" + completeMadLib);
    }

    // formats and asks for user input through the output, then returns it
    private static String giveMe(String identifier) {
        System.out.println("give me " + identifier);
        System.out.print("> ");
        String res = console.nextLine();

        System.out.println();
        return res;
    }
}