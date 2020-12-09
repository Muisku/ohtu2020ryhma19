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
    public void addTip(ReadingTip bookTip) throws Exception {
        bookTip.setId(nextId);
        readingTips.add(bookTip);
        nextId++;
    }

    @Override
    public List<ReadingTip> getAllTips() throws Exception {
        return readingTips;
    }


    @Override
    public void removeTip(String id) throws Exception {
        for (int i = 0; i < readingTips.size(); i++) {
            if (readingTips.get(i).getId() == Integer.parseInt(id)) {
                readingTips.remove(i);
            }
        }
    }

    @Override
    public List<ReadingTip> searchTip(String searchTerm, String searchType) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void modifyTip(String id, String title, String info1, String info2) throws Exception {
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
    }

    @Override
    public ReadingTip getOneTip(String id) throws Exception {
        for (ReadingTip r : readingTips) {
            if (r.getId() == Integer.parseInt(id)) {
                return r;
            }
        }
        return null;
    }

    @Override
    public void markAsRead(String id) {
        for (ReadingTip r : readingTips) {
            if (r.getId() == Integer.parseInt(id)) {
                r.setRead(1);
            }
        }
    }

    @Override
    public void markAsUnread(String id) {
        for (ReadingTip r : readingTips) {
            if (r.getId() == Integer.parseInt(id)) {
                r.setRead(0);
            }
        }
    }
    
    @Override
    public void deleteDatabaseContents() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void modifyTags(String id, String[] newTags) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
