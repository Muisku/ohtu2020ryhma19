package library.domain;

public class ReadingTip {

    private int id;
    private final String title;
    private String type;
    private String[] tags;
    private String[] relatedCourses;
    private String info1;
    private String info2;
    private int read;

    public ReadingTip(String title, String type) {
        this.title = title;
        this.type = type;
        this.tags = new String[8];
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {

    }

    public String getType() {
        return type;
    }

    public String[] getTags() {
        return tags;
    }

    public String[] getRelatedCourses() {
        return relatedCourses;
    }

    public void setMoreInfo1(String author) {
        this.info1 = author;
    }

    public void setMoreInfo2(String isbn) {
        this.info2 = isbn;
    }

    public String getMoreInfo1() {
        return info1;
    }

    public String getMoreInfo2() {
        return info2;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getRead() {
        return read;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    private String toYesNo(int read) {
        if (read == 0) {
            return "No";
        }
        return "Yes";
    }

    private String tagsToString() {
        String result = "";
        for (int i = 0; i < tags.length; i++) {
            if (i > 0) {
                result = result + ", ";
            }
            result = result + tags[i];
        }
        return result;
    }

    @Override
    public String toString() {

        if (type.equals("book")) {
            return "ID: " + id + "\nAuthor: " + info1 + "\nTitle: " + title
                    + "\nType: " + type + "\nISBN: " + info2 + "\nRead: "
                    + toYesNo(read) + "\nTags: " + tagsToString();

        } else if (type.equals("blogpost")) {
            return "ID: " + id + "\nTitle: " + title + "\nAuthor: " + info1
                    + "\nURL: " + info2 + "\nType: " + type + "\nRead: "
                    + toYesNo(read) + "\nTags: " + tagsToString();

        } else if (type.equals("podcast")) {
            return "ID: " + id + "\nHost: " + info1 + "\nPodcast name: "
                    + info2 + "\nTitle: " + title + "\nType: " + type + "\nRead: "
                    + toYesNo(read) + "\nTags: " + tagsToString();

        } else if (type.equals("video")) {
            return "ID: " + id + "\nTitle: " + title + "\nURL: " + info1
                    + "\nType: " + type + "\nRead: " + toYesNo(read)
                    + "\nTags: " + tagsToString();

        } else {
            return "Invalid type!";
        }
    }
}
