package library.dao;

import java.util.ArrayList;
import java.util.List;
import library.dao.ReadingTipDatabaseDao;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import library.dao.ReadingTipDao;
import library.domain.BookTip;
import library.domain.ReadingTip;

public class ReadingTipDatabaseDaoTest {

    ReadingTipDao testDbDao;
    List<ReadingTip> readingTips;

    @Before
    public void setUp() throws Exception {

        testDbDao = new ReadingTipDatabaseDao("jdbc:sqlite:test.db");

        ReadingTip testikirja1 = new BookTip("Taijan kirja");
        testikirja1.setMoreInfo1("Teppo Testaaja");
        testikirja1.setMoreInfo2("1-1111-1-1111");

        ReadingTip testikirja2 = new BookTip("Testikirja 2");
        testikirja2.setMoreInfo1("Taija Testaaja");
        testikirja2.setMoreInfo2("2-2222-2-2222");
        
        ReadingTip testikirja3 = new BookTip("Testikirja 3");
        testikirja3.setMoreInfo1("Turkka Dataaja");
        testikirja3.setMoreInfo2("3-3333-3-3333");

        testDbDao.addTip(testikirja1);
        testDbDao.addTip(testikirja2);
        testDbDao.addTip(testikirja3);
        
    }

    @After
    public void tearDown() {
        testDbDao.deleteDatabaseContents();
    }

    @Test
    public void testGetAllTips() throws Exception {
        assertTrue(testDbDao.getAllTips().size() > 0);
    }

    @Test
    public void testModifyTip() throws Exception {
        testDbDao.modifyTip("1", "new title", "new info 1", "new info 2");
        BookTip book = (BookTip) testDbDao.getOneTip("1");
        assertEquals("new title", book.getTitle());
        assertEquals("new info 1", book.getMoreInfo1());
        assertEquals("new info 2", book.getMoreInfo2());
    }

    @Test
    public void testMarkAsRead() throws Exception {
        testDbDao.markAsRead("1");
        BookTip book = (BookTip) testDbDao.getOneTip("1");
        assertEquals(1, book.getRead());
    }

    @Test
    public void testMarkAsUnread() throws Exception {
        testDbDao.markAsUnread("1");
        BookTip book = (BookTip) testDbDao.getOneTip("1");
        assertEquals(0, book.getRead());
    }

    @Test
    public void searchingAnyFieldReturnsCorrectResult() throws Exception {
        readingTips = testDbDao.searchTip("taija", "");
        assertEquals("Taijan kirja", readingTips.get(0).getTitle());
        assertEquals(2, readingTips.size());
        

    }

    @Test
    public void searchingIndividualFieldReturnsCorrectResult() throws Exception {
        readingTips = testDbDao.searchTip("taija", "info1");
        assertEquals("Testikirja 2", readingTips.get(0).getTitle());
        assertEquals(1, readingTips.size());

    }

}
