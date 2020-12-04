package library;

import library.io.ConsoleIo;
import library.io.Io;
import library.ui.ReadingTipUi;


/**
 * Main class. Launches UI.
 */
public class Main {

    /**
     * Starts the program and launches UI.
     *
     * @param args not used
     */
    public static void main(String[] args) throws Exception {
        Io io = new ConsoleIo();
        ReadingTipUi ui = new ReadingTipUi(io);
        ui.start();
    }

}
