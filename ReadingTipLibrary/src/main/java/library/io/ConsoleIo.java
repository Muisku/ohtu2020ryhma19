package library.io;

import java.util.Scanner;

/**
 * Prints output and receives input from user.
 */
public class ConsoleIo implements Io {

    private Scanner scanner = new Scanner(System.in);

    /**
     * Prints text to console.
     *
     * @param toPrint The text to print.
     */
    public void print(String toPrint) {
        System.out.println(toPrint);
    }

    /**
     * Gets input from user.
     *
     * @param prompt Ask input from user. 
     */
    public String readLine(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine();
    }

}
