package library.ui;

import java.util.ArrayList;
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
    int maxTags = 8; // Maximum tags per reading tip

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

            if (command.toLowerCase().equals("a")) {
                createReadingTip();
            } else if (command.toLowerCase().equals("m")) {
                modifyTip();
            } else if (command.toLowerCase().equals("l")) {
                getAllTips();
            } else if (command.toLowerCase().equals("s")) {
                searchTip();
            } else if (command.toLowerCase().equals("d")) {
                removeTip();
            } else if (command.toLowerCase().equals("r")) {
                markAsRead();
            } else if (command.toLowerCase().equals("u")) {
                markAsUnread();
            } else if (command.toLowerCase().equals("t")) {
                modifyTags();
            } else if (command.toLowerCase().equals("q")) {
                break;
            } else {
                io.print("Oops, command does not exist! Try again.");
            }
        }
    }

    private void removeTip() throws Exception {
        String id = io.readLine("What is the id of the reading tip you want to delete?");
        if (checkIfInputIsNumber(id)) {
            if (getOneTip(id) == null) {
                io.print("Reading tip doesn't exist.");
            } else {
                service.removeTip(id);
                io.print("Reading tip is deleted.");
            }            
        }
    }

    private void modifyTip() throws Exception {
        String id = io.readLine("What is the id of the reading tip you want to modify?");
        if (checkIfInputIsNumber(id)) {
            if (getOneTip(id) == null) {
                io.print("Reading tip doesn't exist.");
            } else {
                io.print(getOneTip(id).toString());
                io.print("\nLeave field empty to not modify.\n");
                String newTitle = io.readLine("What is the new title of the reading tip?");
                String[] otherInfo = askMoreInfoByType(getOneTip(id).getType());

                service.modifyTip(id, newTitle, otherInfo[0], otherInfo[1]);
                io.print(getOneTip(id).toString());
            }
        }
    }

    private void modifyTags() throws Exception {
        String id = io.readLine("What is the id of the reading tip for add/remove tags?");
        if (checkIfInputIsNumber(id)) {
            if (getOneTip(id) == null) {
                io.print("Reading tip doesn't exist.");
            } else {
                io.print(getOneTip(id).toString());

                String[] newTags;

                String input = io.readLine("(A)dd or (R)emove tags?");
                if (input.equals("")) {
                    return;
                }

                if (input.toLowerCase().equals("r")) {
                    newTags = askForTagsToRemove(getOneTip(id).getTags());
                    service.modifyTags(id, newTags, true);
                    io.print(getOneTip(id).toString());
                }

                if (input.toLowerCase().equals("a")) {
                    newTags = askForTags(maxTags - getOneTip(id).getTags().length);
                    service.modifyTags(id, newTags, false);
                    io.print(getOneTip(id).toString());
                }
            }
        }
    }

    private String[] askForTagsToRemove(String[] tags) {

        String[] newTags = new String[tags.length - 1];
        String input = io.readLine("Type tag to remove, or enter to end?");

        if ("".equals(input)) {
            return tags;
        }

        boolean found = false;

        for (int i = 0; i < tags.length; i++) {
            if (tags[i].equals(input)) {
                for (int j = 0; j < i; j++) {
                    newTags[j] = tags[j];
                }
                for (int j = i + 1; j < tags.length; j++) {
                    if (j > 0) {
                        newTags[j - 1] = tags[j];
                    }
                }
                io.print("Tag was deleted.");
                found = true;
                break;
            }
        }
        if (!found) {
            io.print("Not found.");
            return askForTagsToRemove(tags);
        }
        if (newTags.length > 0) {
            return askForTagsToRemove(newTags);
        }
        return newTags;
    }

    private void markAsRead() throws Exception {
        String id = io.readLine("What is the id of the reading tip you want to mark as read?");
        if (checkIfInputIsNumber(id)) {
            if (getOneTip(id) == null) {
                io.print("Reading tip doesn't exist.");
            } else if (checkIfInputIsNumber(id)) {
                service.markAsRead(id);
                io.print(getOneTip(id).toString());
            }
        }
    }

    private void markAsUnread() throws Exception {
        String id = io.readLine("What is the id of the reading tip you want to mark as unread?");
        if (checkIfInputIsNumber(id)) {
            if (getOneTip(id) == null) {
                io.print("Reading tip doesn't exist.");
            } else {
                service.markAsUnread(id);
                io.print(getOneTip(id).toString());
            }
        }
    }

    private void createReadingTip() throws Exception {
        String title = io.readLine("What is the title of the reading tip?");
        if (title.equals("")) {
            System.out.println("Invalid input");
            title = io.readLine("What is the title of the reading tip?");
            if (title.equals("")) {
                System.out.println("Invalid input");
            }
        }
        if (!title.equals("")) {
            printTypes();
            String type = io.readLine("What kind of reading tip is it?");
            if (type.equals("")) {
                System.out.println("Invalid input");
                type = io.readLine("What kind of reading tip is it?");
                if (type.equals("")) {
                    System.out.println("Invalid input");
                }
            }
            if (!type.equals("")) {
                String[] additionalInfo = askMoreInfoByType(type.toLowerCase());
                String[] tags = askForTags(maxTags);
                ReadingTip tip = service.createTip(type.toLowerCase(), title,
                        additionalInfo[0], additionalInfo[1], tags);
            }
            
        }
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

    private String[] askForTags(int maxTags) {

        ArrayList<String> tags = new ArrayList<>();

        io.print("Input tags one by one. Empty input escapes. Max tags = " + maxTags);

        int i = 0;
        while (i < maxTags) {
            String tag = io.readLine("Input tag:");

            if (tag.isEmpty()) {
                break;
            }
            
            if (!tags.contains(tag)) {
                tags.add(tag);
                i++;
            } else {
                io.print("Tag was already added!"); 
            } 
        }

        String[] tagsCompact = new String[i];
        tags.toArray(tagsCompact);

        return tagsCompact;
    }

    private void printOptions() {
        io.print("+----------------------------------------+");
        io.print("| You can...                             |");
        io.print("| (A)dd a new reading tip                |");
        io.print("| (M)odify an existing reading tip       |");
        io.print("| (D)elete a reading tip                 |");
        io.print("| (L)ist all reading tips                |");
        io.print("| (S)earch reading tips by criteria      |");
        io.print("| Mark reading tip as (R)ead or (U)nread |");
        io.print("| (T)ags, add or remove                  |");
        io.print("| (Q)uit                                 |");
        io.print("+----------------------------------------+");
        io.print("");
    }

    private void printTypes() {
        io.print("+----------+");
        io.print("| Options: |");
        io.print("| Blogpost |");
        io.print("| Book     |");
        io.print("| Podcast  |");
        io.print("| Video    |");
        io.print("+----------+");
    }

    private void printSearchFields() {
        io.print("+------------------------------------------+");
        io.print("| Which field would you like to search in? |");
        io.print("| Options:                                 |");
        io.print("| (T)itle                                  |");
        io.print("| (M)edia type                             |");
        io.print("| (A)uthor / host                          |");
        io.print("| (I)SBN                                   |");
        io.print("| Ta(G)s                                   |");
        io.print("| Leave empty to search in all fields      |");
        io.print("+------------------------------------------+");
        io.print("");
    }

    private void getAllTips() throws Exception {
        searchResults = service.browseReadingTips();
        listSearchResults();
    }

    private ReadingTip getOneTip(String id) throws Exception {
        if (checkIfInputIsNumber(id)) {
            ReadingTip tip = service.getOneTip(id);
            return tip;
        }
        return new ReadingTip("", "");
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
        if (searchResults.isEmpty()) {
            io.print("No reading tips found.");
        }
        
        listSearchResults();
    }

    private String fieldFromUserInput(String fieldOption) {

        if (fieldOption.toLowerCase().equals("t")) {
            return "title";
        } else if (fieldOption.toLowerCase().equals("m")) {
            return "type";
        } else if (fieldOption.toLowerCase().equals("a")) {
            return "info1";
        } else if (fieldOption.toLowerCase().equals("i")) {
            return "info2";
        } else if (fieldOption.toLowerCase().equals("g")) {
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
        searchResults = service.browseReadingTips();
    }

    private boolean checkIfInputIsNumber(String input) {
        try {
            int number = Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            io.print("Invalid input");
            return false;
        }
        return true;
    }
    
    
}
