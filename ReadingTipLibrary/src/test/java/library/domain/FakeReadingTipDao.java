package library.domain;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import library.dao.ReadingTipDao;
import library.domain.ReadingTip;

public class FakeReadingTipDao implements ReadingTipDao {

    List<ReadingTip> readingTips;
    int nextId;

    public FakeReadingTipDao() {
        readingTips = new ArrayList<>();
        nextId = 1;
    }

    @Override
    public boolean addTip(ReadingTip bookTip){
        bookTip.setId(nextId);
        readingTips.add(bookTip);
        nextId++;
        return true;
    }

    @Override
    public List<ReadingTip> getAllTips(){
        return readingTips;
    }


    @Override
    public boolean removeTip(String id){
        for (int i = 0; i < readingTips.size(); i++) {
            if (readingTips.get(i).getId() == Integer.parseInt(id)) {
                readingTips.remove(i);
            }
        }
        
        return true;
    }

    @Override
    public List<ReadingTip> searchTip(String searchTerm, String searchType){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean modifyTip(String id, String title, String info1, String info2){
        for (ReadingTip r : readingTips) {
            if (r.getId() == Integer.parseInt(id)) {
                if (!title.isEmpty()) {
                    r.setTitle(title);
                }
                if (!info1.isEmpty()) {
                    r.setMoreInfo1(info1);
                }
                if (!info2.isEmpty()) {
                    r.setMoreInfo2(info2);
                }
            }
        }
        
        return true;
    }

    @Override
    public ReadingTip getOneTip(String id) {
        for (ReadingTip r : readingTips) {
            if (r.getId() == Integer.parseInt(id)) {
                return r;
            }
        }
        return null;
    }

    @Override
    public boolean markAsRead(String id) {
        for (ReadingTip r : readingTips) {
            if (r.getId() == Integer.parseInt(id)) {
                r.setRead(1);
            }
        }
        
        return true;
    }

    @Override
    public boolean markAsUnread(String id) {
        for (ReadingTip r : readingTips) {
            if (r.getId() == Integer.parseInt(id)) {
                r.setRead(0);
            }
        }
        return true;
    }
    
    @Override
    public void deleteDatabaseContents() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean modifyTags(String id, String[] newTags, boolean replace){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
