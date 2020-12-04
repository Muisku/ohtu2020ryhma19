package library.domain;

/**
 * A ReadingTip of type VideoTip.
 */
public class VideoTip implements ReadingTip {

    private int id;
    private String title;
    private String url;
    private String comment;
    private String[] tags;
    private String[] relatedCourses;
    private final String type = "video";
    private int read;

    public VideoTip(String title) {
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
    public void setMoreInfo1(String url) {
        this.url = url;
    }

    @Override
    public void setMoreInfo2(String comment) {
        this.comment = comment;
    }

    @Override
    public String getMoreInfo1() {
        return url;
    }

    @Override
    public String getMoreInfo2() {
        return comment;
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
        return "ID: " + id + "\nTitle: " + title + "\nURL: " + url + "\nType: " + type;
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
