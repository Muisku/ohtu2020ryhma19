package library.domain;

/**
 * A ReadingTip of type BlogPostTip.
 */
public class BlogPostTip implements ReadingTip {

    private int id;
    private String title;
    private String author;
    private String url;
    private String[] tags;
    private String[] relatedCourses;
    private final String type = "blogpost";
    private int read;

    public BlogPostTip(String title) {
        this.title = title;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String[] getTags() {
        return tags;
    }

    @Override
    public String[] getRelatedCourses() {
        return relatedCourses;
    }

    @Override
    public void setMoreInfo1(String author) {
        this.author = author;
    }

    @Override
    public void setMoreInfo2(String url) {
        this.url = url;
    }

    @Override
    public String getMoreInfo1() {
        return author;
    }

    @Override
    public String getMoreInfo2() {
        return url;
    }

    @Override
    public void setRead(int read) {
        this.read = read;
    }

    @Override
    public int getRead() {
        return read;
    }

    @Override
    public String toString() {
        return "ID: " + id + "\nTitle: " + title + "\nAuthor: " + author
                + "\nURL: " + url + "\nType: " + type;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

}
