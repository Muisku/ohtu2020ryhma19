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
public class ReadeinTipService {

    private ReadingTipDao readingTipDao;

    public ReadeinTipService() {
        this(new ReadingTipDatabaseDao("jdbc:sqlite:readingtip.db"));
    }

    public ReadeinTipService(ReadingTipDao rtd) {
        readingTipDao = rtd;
    }

    /**
     * Creates ReadingTip.
     *
     * @param type The type of the ReadingTip
     * @param title title
     * @param author The content of this field will depend on the type.
     * @param info2 The content of this field will depend on the type.
     */
    public ReadingTip createTip(String type, String title, String author, String info2)
            throws Exception {
        ReadingTip rt = new ReadingTip(title, type);
        rt.setAuthor(author);
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

    public void modifyTip(String id, String newTitle, String newAuthor, String newInfo2)
            throws Exception {
        readingTipDao.modifyTip(id, newTitle, newAuthor, newInfo2);
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

}
