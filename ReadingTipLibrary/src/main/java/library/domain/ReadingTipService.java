package library.domain;

import java.util.List;
import library.dao.ReadingTipDao;
import library.dao.ReadingTipDatabaseDao;

/**
 * Provides methods for handling ReadingTips.
 */
public class ReadingTipService {

    private ReadingTipDao readingTipDao;

    public ReadingTipService() {
        this(new ReadingTipDatabaseDao("jdbc:sqlite:readingtip.db"));
    }

    public ReadingTipService(ReadingTipDao rtd) {
        readingTipDao = rtd;
    }

    /**
     * Creates ReadingTip.
     *
     * @param type The type of the ReadingTip
     * @param title title
     * @param info1 The content of this field will depend on the type.
     * @param info2 The content of this field will depend on the type.
     */
    public boolean createTip(String type, String title, String info1, String info2,
            String[] tags) {
        
        ReadingTip rt = new ReadingTip(title, type);
        rt.setMoreInfo1(info1);
        rt.setMoreInfo2(info2);
        rt.setTags(tags);

        return readingTipDao.addTip(rt);
    }

    /**
     * Lists all ReadingTips.
     *
     * @return A list of all ReadingTips.
     */
    public List<ReadingTip> browseReadingTips() {
        List<ReadingTip> tipList = readingTipDao.getAllTips();
        return tipList;
    }

    public List<ReadingTip> searchTip(String searchTerm, String searchField) {
        List<ReadingTip> tipList = readingTipDao.searchTip(searchTerm, searchField);
        return tipList;
    }

    public ReadingTip getOneTip(String id) {
        ReadingTip readingTip = readingTipDao.getOneTip(id);
        return readingTip;
    }

    public boolean removeTip(String id) {
        return readingTipDao.removeTip(id);
    }

    public boolean modifyTip(String id, String newTitle, String newInfo1, String newInfo2) {
        return readingTipDao.modifyTip(id, newTitle, newInfo1, newInfo2);
    }

    public boolean modifyTags(String id, String[] newTags, boolean replace) {
        return readingTipDao.modifyTags(id, newTags, replace);
    }

    public boolean markAsRead(String id) {
        return readingTipDao.markAsRead(id);
    }

    public boolean markAsUnread(String id) {
        return readingTipDao.markAsUnread(id);
    }

    public void deleteDatabaseContents() {
        readingTipDao.deleteDatabaseContents();
    }

}
