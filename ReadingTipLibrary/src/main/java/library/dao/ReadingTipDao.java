package library.dao;

import java.util.List;
import library.domain.ReadingTip;

/**
 * ReadingTipDao Interface.
 */
public interface ReadingTipDao {

    boolean addTip(ReadingTip bookTip);
    
    boolean removeTip(String id);
    
    boolean modifyTip(String id, String title, String info1, String info2);
    
    boolean modifyTags(String id, String[] newTags, boolean replace);
    
    ReadingTip getOneTip(String id);

    List<ReadingTip> getAllTips();

    List<ReadingTip> searchTip(String searchTerm, String searchField);

    public boolean markAsRead(String id);
    
    public boolean markAsUnread(String id);
    
    public void deleteDatabaseContents();
}
