package library.ui;

import java.util.List;
import library.domain.ReadingTip;
import library.domain.ReadingTipService;
import library.io.Io;

/**
 * The user interface.
 */
public class ReadingTipUi {

    ReadingTipService service;
    private Io io;
    private List<ReadingTip> searchResults;

    public ReadingTipUi(Io io) {
        this.io = io;
    }

    public void start() throws Exception {
        start(new ReadingTipService());
    }

    /**
     * Starts the user interface.
     */
    public void start(ReadingTipService rts) throws Exception {

        service = rts;
        searchResults = service.browseReadingTips();

        io.print("Hello user!");

        while (true) {
            printOptions();
            String command = io.readLine("Give a command:");

            if (command.isEmpty()) {
                break;
            }

            if (command.equals("A")) {
                createReadingTip();
            } else if (command.equals("M")) {
                modifyTip();
            } else if (command.equals("L")) {
                getAllTips();
            } else if (command.equals("S")) {
                searchTip();
            } else if (command.equals("D")) {
                removeTip();
            } else if (command.equals("R")) {
                markAsRead();
            } else if (command.equals("U")) {
                markAsUnread();
            } else if (command.equals("Q")) {
                break;
            } else {
                io.print("Oops, command does not exist! Try again.");
            }
        }
    }

    private void removeTip() throws Exception {
        String id = io.readLine("What is the id of the reading tip you want to delete?");
        if (getOneTip(id) == null) {
            io.print("Reading tip doesn't exist.");
        } else {
            service.removeTip(id);
            io.print("Reading tip is deleted.");
        }
    }

    private void modifyTip() throws Exception {
        String id = io.readLine("What is the id of the reading tip you want to modify?");
        if (getOneTip(id) == null) {
            io.print("Reading tip doesn't exist.");
        } else {
            io.print(getOneTip(id).toString());
            String newTitle = io.readLine("What is the new title of the reading tip?");
            String[] otherInfo = askMoreInfoByType(getOneTip(id).getType());

            String[] newTags;
            if ("Y".equals(io.readLine("Do you want to edit the tags? Y or N"))) {
                newTags = askForTags();
            } else {
                newTags = new String[0];
            }

            service.modifyTip(id, newTitle, otherInfo[0], otherInfo[1]);
            service.modifyTags(id, newTags);
            io.print(getOneTip(id).toString());
        }
    }

    private void markAsRead() throws Exception {
        String id = io.readLine("What is the id of the reading tip you want to mark as read?");
        if (getOneTip(id) == null) {
            io.print("Reading tip doesn't exist.");
        } else {
            service.markAsRead(id);
            io.print(getOneTip(id).toString());
        }
    }

    private void markAsUnread() throws Exception {
        String id = io.readLine("What is the id of the reading tip you want to mark as unread?");
        if (getOneTip(id) == null) {
            io.print("Reading tip doesn't exist.");
        } else {
            service.markAsUnread(id);
            io.print(getOneTip(id).toString());
        }
    }

    private void createReadingTip() throws Exception {
        String title = io.readLine("What is the title of the reading tip?");
        printTypes();
        String type = io.readLine("What kind of reading tip is it?");
        String[] additionalInfo = askMoreInfoByType(type.toLowerCase());
        String[] tags = askForTags();
        ReadingTip tip = service.createTip(type.toLowerCase(), title,
                additionalInfo[0], additionalInfo[1], tags);

        //io.print(tip.toString());
    }

    private String[] askMoreInfoByType(String type) {

        String[] additionalInfo = new String[2];

        if (type.equals("book")) {
            additionalInfo[0] = io.readLine("Who is the author?");
            additionalInfo[1] = io.readLine("What is the ISBN number?");

        } else if (type.equals("blogpost")) {
            additionalInfo[0] = io.readLine("Who is the author?");
            additionalInfo[1] = io.readLine("What is the URL of the blogpost?");

        } else if (type.equals("podcast")) {
            additionalInfo[0] = io.readLine("Who is/are the host(s) of the podcast?");
            additionalInfo[1] = io.readLine("What is the name of the podcast?");

        } else if (type.equals("video")) {
            additionalInfo[0] = io.readLine("What is the URL of the video?");
        }

        return additionalInfo;
    }

    private String[] askForTags() {

        String[] tags = new String[8];

        io.print("Input tags one by one. Empty input escapes. Max tags = " + tags.length);

        int i = 0;
        while (i < tags.length) {
            String tag = io.readLine("Input tag:");

            if (tag.isEmpty()) {
                break;
            } else {
                tags[i] = tag;
                i++;
            }
        }

        String[] tagsCompact = new String[i];
        System.arraycopy(tags, 0, tagsCompact, 0, tagsCompact.length);

        return tagsCompact;
    }

    private void printOptions() {
        io.print("You can...");
        io.print("(A)dd a new reading tip");
        io.print("(M)odify an existing reading tip");
        io.print("(D)elete a reading tip");
        io.print("(L)ist all reading tips");
        io.print("(S)earch reading tips by criteria");
        io.print("Mark reading tip as (R)ead or (U)nread");
        io.print("Add or remove (T)ags");
        io.print("(Q)uit");
    }

    private void printTypes() {
        io.print("Options:");
        io.print("Blogpost");
        io.print("Book");
        io.print("Podcast");
        io.print("Video");
    }

    private void printSearchFields() {
        io.print("Which field would you like to search in?");
        io.print("Options:");
        io.print("(T)itle");
        io.print("(M)edia type");
        io.print("(A)uthor / host");
        io.print("(I)SBN");
        io.print("Ta(G)s");
        io.print("Leave empty to search in all fields");
    }

    private void getAllTips() throws Exception {
        searchResults = service.browseReadingTips();
        listSearchResults();
    }

    private ReadingTip getOneTip(String id) throws Exception {
        ReadingTip tip = service.getOneTip(id);
        return tip;
    }

    private void searchTip() throws Exception {

        String searchField;

        while (true) {
            printSearchFields();
            String fieldOption = io.readLine("");
            searchField = fieldFromUserInput(fieldOption.toLowerCase());

            if (!searchField.equals("error")) {
                break;
            }

            io.print("Invalid command!\n");
        }

        String searchTerm = io.readLine("Input search term:");

        searchResults = service.searchTip(searchTerm, searchField);
        listSearchResults();
    }

    private String fieldFromUserInput(String fieldOption) {

        if (fieldOption.equals("t")) {
            return "title";
        } else if (fieldOption.equals("m")) {
            return "type";
        } else if (fieldOption.equals("a")) {
            return "info1";
        } else if (fieldOption.equals("i")) {
            return "info2";
        } else if (fieldOption.equals("g")) {
            return "tags";
        } else if ((fieldOption.equals(""))) {
            return "";
        } else {
            return "error";
        }
    }

    private void listSearchResults() throws Exception {
        for (int i = 1; i <= searchResults.size(); i++) {
            io.print(searchResults.get(i - 1).toString());
            io.print("");
        }
    }
}
