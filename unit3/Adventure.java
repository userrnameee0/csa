/*=============================================================================
 |   Assignment:  Program 1.3.8:  Choose Your Path
 |       Author:  Issai Catano
 |      Partner:  None.
 |
 |  Course Name:  AP CSA
 |   Instructor:  Mr. Virak
 |     Due Date:  Wed Oct 30, 2024 11:59pm
 |
 |  Description:  The program tells of a storyline, with user input determining
 |                the next choice in the story dynamically, all to eventually
 |                lead to an ending based on the options previously selected.
 |                There is only one starting point, with each subsequent choice
 |                getting closer or farther to an ending
 |
 |     Language:  Java version 8
 | Ex. Packages:  None.
 |                
 | Deficiencies:  if-else statements are nested via recursion, so theyre not
 |                found in the code as explicitly
 *===========================================================================*/

import java.util.Scanner;
import java.util.ArrayList;
import java.util.function.Function;

public class Adventure {

    // an individual `Choice`, represented with an identifying `name`, a `description`, and
    // its available `options[]` and corresponding `Choice`s to those `options[]`
    private static class Choice {
        public String name, description;
        public String[] options, redirects;
    
        Choice(String[] identifiers, String[][] choices) {
            this.name = identifiers[0];
            this.description = identifiers[1];
            this.options = choices[0];
            this.redirects = choices[1];
            if (this.options.length != this.redirects.length) {
                throw new RuntimeException("invalid choice format");
            }
        }
    }
    
    private static Scanner console = new Scanner(System.in);
    private static ArrayList<Choice> choices = new ArrayList<>();

    // the adventure choices, formatted as follows
    /* 
     * String[][] adventureChoice = {
     *     {name, description},
     *     {options}, {redirects},
     * }
     */
    // where `description` and `options[]` are told to the user
    // and `redirects[]` are the next corresponding `adventureChoice` youll get to
    // so a `redirect` must be an existing `name`, and the length of `options[]` and `redirects[]` are equal
    private static String[][][] rawChoices = {
        {
            {"start", "u decide ur going hiking YAYYY"}, {"YAYYYYYYY"}, {"paranoia"},
        },
        {
            {"paranoia", "its nice to go outside!! it feels like something is following me tho"}, {"something always follows everyone, NOTHING NEW", "lemme look around :o"}, {"denial", "solace"},
        },
        {
            {"denial", "im not gonna lie to myself, this is bothering me a bit"}, {"dont manifest that which is only probable", "lemme look around :o"}, {"ignorance", "solace"},
        },
        {
            {"ignorance", "i move on!"}, {"wahoo!"}, {"terror"},
        },
        {
            {"terror", "out in the distance, on a tree, i see a centipede"}, {"EWWWW"}, {"terror2"},
        },
        {
            {"terror2", "i walk by quickly! but wait, another one appears?!"}, {"AHHHHH GET OUTTTT", "whuh?? im getting scared :("}, {"terror3", "terror3"},
        },
        {
            {"terror3", "i check my surroundings.... THERES ONE ON MY LEG"}, {"WHAHHHHH STOP ITTTT STOPPPP", "AAAAAAAAAAAH NOOOO STOP STOP STOP STOP AHHH", "GET OUT GET OUT SO SCARRYY AAAAA"}, {"terrifying", "terrifying", "terrifying"},
        },
        {
            {"terrifying", "you can no longer tell what is real and what is not"}, {}, {},
        },
        {
            {"solace", "NOTHINGS THEREEEEE but i do find a nice spot to eat food"}, {"maybe i can find a better view somewhere else", "let me double check my surroundings", "YAYYYYYY IM SUPER DUPER HUNGRY"}, {"ignorance", "terror", "temptation"},
        },
        {
            {"temptation", "the yummy food is SUPERRR YUMMYYY"}, {"ILL SAVOR IT FOR LATER IM NOT DONE YET", "YAYYYYY I WANT MOREEEE"}, {"temptation2", "bliss"},
        },
        {
            {"temptation2", "as i hike to the top.... i trip and spill all the leftovers!"}, {"GRRRR >:(", "aw man :(", "THE FOOD WAS YUMMYYY NOOO!! ILL EAT FROM THE FLOOR NOW"}, {"terror", "temptation3", "bliss"},
        },
        {
            {"temptation3", "the view up top is really nice tho!"}, {"i kinda wanna see if that other spot has an even nicer view...", "yayyy"}, {"terror", "satisfaction"},
        },
        {
            {"satisfaction", "you climb back down, it was a pretty nice hike"}, {}, {},
        },
        {
            {"bliss", "THE FOOD IS YUMMYYY IMM SOO HAPPYYYY"}, {"YAYYYYYYY", "WAHHOOOOO"}, {"blissful", "blissful"},
        },
        {
            {"blissful", "something felt off, but you couldnt tell and ignored it"}, {}, {},
        },
    };

    public static void main(String[] args) {
        // convert the raw data of choices into `Choice`s in a list
        for (String[][] choiceData: rawChoices) {
            String[][] formattedChoices = {choiceData[1], choiceData[2]};
            choices.add(new Choice(choiceData[0], formattedChoices));
        }

        // the first raw choice entry is the start
        followChoice(choices.get(0));
    }

    /*---------------------------------------------------------------------
        |  Method followChoice
        |
        |  Purpose: outputs the provided `Choice` and asks for an `option`, 
        |      which leads to the next `Choice` automatically with recursion
        |
        |  Pre-condition: the `Choice`, if it has `redirects[]`, they must
        |      point to an existing `Choice` with the same `name`
        |
        |  Post-condition: Another `Choice` is being displayed recursively,
        |      or the program ends if the `Choice` has no `options[]`
        |
        |  Parameters:
        |      location -- the next `Choice` to display and follow its
        |          `options[]` provided via user input
        |
        |  Returns: None.
        *-------------------------------------------------------------------*/
    private static void followChoice(Choice location) {
        System.out.println(location.description);
        int totalOptions = location.options.length;

        // no `options[]` means the end has been reached
        if (totalOptions == 0) {
            return;
        }

        // display the `options[]` and associate them with their index number + 1
        for (int i = 0; i < totalOptions; i++) {
            System.out.printf("%d | %s\n", i + 1, location.options[i]);
        }

        // get the `option` that the user selects
        int redirectIndex = giveChoiceUpTo(totalOptions);

        // `find`s the corresponding `Choice` from the `option`s corresponding `redirect` and follows it
        followChoice(find(choices, c -> c.name == location.redirects[redirectIndex]));
    }
    
    /*---------------------------------------------------------------------
        |  Method find
        |
        |  Purpose: finds the first item in a `list` that passes the `check`
        |      provided, similar to `Array.prototype.find` in javascript
        |
        |  Pre-condition: When `check`ing `list` items, it is evaluated
        |       depending on the `Boolean` returned, and will not work
        |       properly if it instead throws an exception to signify failure
        |
        |  Post-condition: The `list` given is not modified
        |
        |  Parameters:
        |      list -- the `ArrayList` to search the items of, the items
        |         themselves dont have to be a specific type
        |      check -- the method of verification for whether an item in the
        |          `list` is the one wanted, therefore the `check` function
        |          accepts an item from the `list` and returns `true`/`false`
        |          to accept/deny the item
        |
        |  Returns: The first item which satisfies the `check` in the `list`,
        |      otherwise `null` if none were found
        *-------------------------------------------------------------------*/
    private static <T> T find(ArrayList<T> list, Function<T, Boolean> check) {
        for (T item: list) {
            if (check.apply(item)) {
                return item;
            }
        }
        return null;
    }


    // 
    /*---------------------------------------------------------------------
        |  Method giveChoiceUpTo
        |
        |  Purpose: gives an integer from the user between `0` and
        |      `max - 1` based off a user selection from `1` to `max`,
        |      both inclusive. Used to select an `option` from `options[]`
        |      in a `Choice`, with each numbered relative to their index
        |
        |  Pre-condition: `max` is an unsigned int corresponding to the amount
        |      of `options[]` in a `Choice`
        |
        |  Post-condition: A new `Choice` is selected by the user from the previous
        |      ones available `options[]`
        |
        |  Parameters:
        |      max -- an integer representing the amount of available `options[]`
        |
        |  Returns: the index of the `option` selected by the user in a `Choice`
        *-------------------------------------------------------------------*/
    private static int giveChoiceUpTo(int max) {
        System.out.println();
        while (true) {
            System.out.print("> ");
            try {
                int choice = console.nextInt();
                if (choice >= 1 && choice <= max) {
                    System.out.println();
                    return choice - 1;

                } else {
                    throw new Exception();
                }
            } catch (Exception error) {

                System.out.println("invalid choice");
            }
        }
    }
}