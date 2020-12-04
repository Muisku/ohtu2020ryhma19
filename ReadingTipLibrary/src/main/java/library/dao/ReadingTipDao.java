package library.dao;

import java.util.List;
import library.domain.ReadingTip;

/**
 * ReadingTipDao Interface.
 */
public interface ReadingTipDao {

    void addTip(ReadingTip bookTip) throws Exception;
    
    void removeTip(String id) throws Exception;
    
    void modifyTip(String id, String title, String info1, String info2) throws Exception;
    
    ReadingTip getOneTip(String id) throws Exception;

    List<ReadingTip> getAllTips() throws Exception;

    List<ReadingTip> searchTip(String searchTerm, String searchField) throws Exception;

    public void markAsRead(String id);
    
    public void markAsUnread(String id);
    
    public void deleteDatabaseContents();
}
