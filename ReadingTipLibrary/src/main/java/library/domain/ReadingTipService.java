package library.domain;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
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
    public ReadingTip createTip(String type, String title, String info1, String info2)
            throws Exception {
        ReadingTip rt = createTipWithType(type, title);
        rt.setMoreInfo1(info1);
        rt.setMoreInfo2(info2);
        readingTipDao.addTip(rt);
        return rt;
    }

    /**
     * Lists all ReadingTips.
     *
     * @return A list of all ReadingTips.
     */
    public List<ReadingTip> browseReadingTips() throws Exception {
        List<ReadingTip> tipList = readingTipDao.getAllTips();
        return tipList;
    }

    public List<ReadingTip> searchTip(String searchTerm, String searchField) throws Exception {
        List<ReadingTip> tipList = readingTipDao.searchTip(searchTerm, searchField);
        return tipList;
    }

    public ReadingTip getOneTip(String id) throws Exception {
        ReadingTip readingTip = readingTipDao.getOneTip(id);
        return readingTip;
    }

    public void removeTip(String id) throws Exception {
        readingTipDao.removeTip(id);
    }

    public void modifyTip(String id, String newTitle, String newInfo1, String newInfo2)
            throws Exception {
        readingTipDao.modifyTip(id, newTitle, newInfo1, newInfo2);
    }

    public void markAsRead(String id) {
        readingTipDao.markAsRead(id);
    }

    public void markAsUnread(String id) {
        readingTipDao.markAsUnread(id);
    }

    public void deleteDatabaseContents() {
        readingTipDao.deleteDatabaseContents();
    }

    private ReadingTip createTipWithType(String type, String title) {

        ReadingTip tip;

        if (type.equals("book")) {
            tip = new BookTip(title);
        } else if (type.equals("blogpost")) {
            tip = new BlogPostTip(title);
        } else if (type.equals("video")) {
            tip = new VideoTip(title);
        } else {
            tip = new PodcastTip(title);
        }

        return tip;
    }

}
