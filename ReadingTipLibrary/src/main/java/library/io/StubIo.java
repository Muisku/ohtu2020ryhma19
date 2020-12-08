package library.io;

import java.util.ArrayList;
import java.util.List;

/**
 * Prints output and receives input from tests.
 */
public class StubIo implements Io {

    private List<String> lines;
    private int iterator;
    private ArrayList<String> prints;

    public StubIo(List<String> values) {
        this.lines = values;
        prints = new ArrayList<>();
    }

    public void print(String toPrint) {
        prints.add(toPrint);
    }

    public ArrayList<String> getPrints() {
        return prints;
    }

    /**
     * Get input from user or test.
     *
     * @param prompt Ask input from user.
     */
    public String readLine(String prompt) {
        print(prompt);
        if (iterator < lines.size()) {
            return lines.get(iterator++);
        }
        return "";
    }
}
