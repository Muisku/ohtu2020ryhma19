package library.domain;

/** ReadingTip interface, used to define different types of reading tips. */
public interface ReadingTip {
    
    int getId();
    
    void setId(int id);
    
    String getType();

    String getTitle();
    
    void setTitle(String title);
    
    String[] getTags();
    
    String[] getRelatedCourses();
    
    void setMoreInfo1(String info);
    
    void setMoreInfo2(String info);
    
    void setRead(int read);
    
    int getRead();
    
    String getMoreInfo1();
    
    String getMoreInfo2();
    
    

}
