import java.util.Scanner;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.function.Function;

public class Sevens {
    static Scanner console = new Scanner(System.in);
    
    // helpful generic methods for array modification, and
    // since lists are immutable, it returns the new list
    static class arrayMod {
        // adds an `item` to a `list`
        static <T> T[] add(T[] list, T item) {
            T[] result = Arrays.copyOf(list, list.length + 1);
            result[list.length] = item;
            return result;
        }    
        
        // combines two lists together
        static <T> T[] combine(T[] list1, T[] list2) {
            int newSize = list1.length + list2.length;
            T[] result = Arrays.copyOfRange(list1, 0, newSize);
            
            for (int i = list1.length; i < newSize; i++) {
                result[i] = list2[i - list1.length];
            }    
        
            return result;
        }    
        
        // removes the item of the `list` at index `i`
        static <T> T[] removeIndex(T[] list, int i) {
            return combine(Arrays.copyOfRange(list, 0, i), Arrays.copyOfRange(list, i + 1, list.length));
        }

        // removes `item` from `list
        static <T> T[] removeItem(T[] list, T item) {
            for (int i = 0; i < list.length; i++) {
                if (list[i] == item) {
                    return removeIndex(list, i);
                }
            }
            return list;
        }
    }    

    // returns the sum of a `list`
    static int sumOf(Integer[] list) {
        return Arrays.stream(list).mapToInt(Integer::intValue).sum();
    }

    // overloaded methods, which automatically convert an `Integer` `list` into a stream and back into
    // an array, with a provided `modifier` to handle the stream made
    static Integer[] streamlineInts(Integer[] list, Function<Stream<Integer>, Stream<Integer>> modifier) {
        return modifier.apply(Arrays.stream(list)).toArray(Integer[]::new);
    }
    static Integer[][] streamlineInts(Integer[][] list, Function<Stream<Integer[]>, Stream<Integer[]>> modifier) {
        return modifier.apply(Arrays.stream(list)).toArray(Integer[][]::new);
    }

    // given a set of `options`, returns the possible combinations that make the sum of
    // `start` and any set of numbers from `options` equal to the `goal`.
    static Integer[][] startCombinationsToGoal(Integer[] start, Integer[] options, int goal) {
        Integer[][] combinations = {};

        int currentSum = sumOf(start);
        
        // attempt to return the `start` sum if it reaches the goal, otherwise
        // return an empty set since no `options` allow for more combinations
        if (options.length == 0) {
            return currentSum == goal ? new Integer[][] { start } : combinations;
        }
        
        // for each of the `option`s, add it to the sum and remove it from the original
        // `options`, along with the `option`s which dont cause the sum to exceed
        // the `goal`. then find the possible combinations recursively with that set and
        // add it to the current total combinations
        for (int i = 0; i < options.length; i++) {
            int option = options[i];

            combinations = arrayMod.combine(combinations, startCombinationsToGoal(
                arrayMod.add(start, option),
                streamlineInts(arrayMod.removeIndex(options, i), stream -> stream.filter(o -> currentSum + option + o <= goal)),
                goal
            ));
        }

        // the recursive loop doesnt apply memoization, so duplicate sets are
        // removed manually by checking if their sorted combinations are equal
        return streamlineInts(combinations, stream ->
            stream.map(set -> Arrays.asList(streamlineInts(set, s -> s.sorted())))
                .distinct()
                .map(set -> set.toArray(Integer[]::new))
        );
    }
    
    // asks the user a `question` and presents the `options` given to select via a
    // number, which is then given back when its from `1` to `options.length`
    static int getUserChoice(String question, String[] options) {
        // if there is no `question`, none is printed
        if (question != null) {
            System.out.println(question);
        };
        // if an `option` is `null`, its not presented to the user but is still selectable
        for (int i = 0; i < options.length; i++) {
            String option = options[i];
            if (option == null) { continue; }

            System.out.printf("%d: %s\n", i + 1, option);
        }

        // while the user doesnt give an option from `1` to
        // `options.length`, keep telling them to try again
        while (true) {
            try {
                System.out.print("\n> ");
                int userResponse = Integer.valueOf(console.nextLine());
                if (userResponse < 1 || userResponse > options.length) { throw new Exception(); }

                System.out.println();
                return userResponse;
                
            } catch (Exception err) { System.out.print("try again"); }
        }
    }
    
    // represents a player which is able to roll, with a
    // display `name` and recorded `points` over turns
    static class player {
        // options available to the `player` after a reroll
        static String[] rollChoices = {"roll again!", "quit my turn"};
        
        String name;
        int points = 0;

        player(String name) {
            this.name = name;
        }

        // lets the `player` have the option to roll the dice a total of `maxRolls`
        // times, with each roll having the `player` remove any combinations of `7` dice,
        // then returning the amount of times the `player` rolled
        int roll(int maxRolls) {
            // the `player` starts of with `6` possible dice
            System.out.printf("%s has %d rolls allowed\n", name, maxRolls);
            Integer[] rolls = new Integer[6];

            for (int rollsDone = 1;; rollsDone++) {
                // roll the each of the dice available using `Math.random()`
                rolls = streamlineInts(rolls, stream -> stream.map(x -> (int) (Math.random() * 6) + 1));
                System.out.printf("u roll %d dice\n", rolls.length);

                // while there are dice that add up to `7`, ask the `player` to remove one of the
                // sets that do so, since multiple sets may contain the same dice, and the
                // `player` can strategize which ones to remove
                while (true) {
                    Integer[][] removables = startCombinationsToGoal(new Integer[0], rolls, 7);
                    if (removables.length == 0) { break; }
                    
                    for (int die: removables[-1 + getUserChoice(
                        String.format("u got some that add up to seven! from %s choose one to remove", Arrays.toString(rolls)),
                        Arrays.stream(removables).map(set -> Arrays.toString(set)).toArray(String[]::new)
                    )]) {
                        rolls = arrayMod.removeItem(rolls, die);
                    }
                }

                // tell the `player` the results of their roll and removing of dice sets, and
                // if they have zero dice, or done all their rolls, or they choose to quit,
                // their turn ends. afterwards their turn is finished and turn results are displayed
                int rollSum = sumOf(rolls);
                System.out.printf("ur dice rolls of %s add to %d\n", Arrays.toString(rolls), rollSum);
                if (rolls.length == 0 || rollsDone >= maxRolls || getUserChoice(null, rollChoices) == 2) {
                    points += rollSum;
                    System.out.printf(
                        "%s finishes using %d/%d turns and earns %dpts, with a total score of %dpts\npress enter to continue\n",
                        name, rollsDone, maxRolls, rollSum, points
                    );
                    console.nextLine();
                    return rollsDone;
                }
            }
        }
    }

    // runs the sevens dice game with two players
    public static void main(String[] args) {
        // setup rounds and players
        int totalRounds = getUserChoice("welcome to sevens! how many rounds would u like? u can pick 1-10", new String[10]);
        player p1 = new player("player1");
        player p2 = new player("player2");

        // for each sevens round, have `p1` roll a maximum of
        // `3` times, and `p2` roll the amount of times `p1` did
        for (int round = 0; round < totalRounds; round++) {
            System.out.printf("round %d/%d\n", round + 1, totalRounds);
            p2.roll(p1.roll(3));
        }

        // display the round results, along with which player won
        System.out.printf(
            "in %d rounds, %s finishes with %dpts, and %s with %dpts\n%s won!",
            totalRounds, p1.name, p1.points, p2.name, p2.points,
            (p1.points > p2.points ? p1.name : p2.points > p1.points ? p2.name : "both")
        );
    }
}