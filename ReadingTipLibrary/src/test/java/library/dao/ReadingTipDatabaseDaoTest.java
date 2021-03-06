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
import library.domain.ReadingTip;

public class ReadingTipDatabaseDaoTest {

    ReadingTipDao testDbDao;
    List<ReadingTip> readingTips;

    @Before
    public void setUp() throws Exception {
        testDbDao = new ReadingTipDatabaseDao("jdbc:sqlite:test.db");

        ReadingTip testikirja1 = new ReadingTip("Taijan kirja", "book");
        testikirja1.setMoreInfo1("Teppo Testaaja");
        testikirja1.setMoreInfo2("1-1111-1-1111");

        ReadingTip testikirja2 = new ReadingTip("Testikirja 2", "book");
        testikirja2.setMoreInfo1("Taija Testaaja");
        testikirja2.setMoreInfo2("2-2222-2-2222");
        
        ReadingTip testikirja3 = new ReadingTip("Testikirja 3", "book");
        testikirja3.setMoreInfo1("Turkka Dataaja");
        testikirja3.setMoreInfo2("3-3333-3-3333");
        
        String[] tags1 = new String[]{"aihe1", "aihe2"};
        String[] tags2 = new String[]{"aihe1", "aihe3"};        
        String[] tags3 = new String[]{"aihe3"}; 
  
        
        testikirja1.setTags(tags1);
        testikirja2.setTags(tags2);
        testikirja3.setTags(tags3);
 
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
    public void addingReadingTipAddsReadingTipToTheDb() throws Exception {
        assertEquals(3, testDbDao.getAllTips().size());
        ReadingTip rt = new ReadingTip("Testikirja 4", "book");
        rt.setTags(new String[]{"aihe1", "aihe2"});
        testDbDao.addTip(rt);
        assertEquals(4, testDbDao.getAllTips().size());
        assertEquals("book", testDbDao.getOneTip("4").getType());
        assertEquals("Testikirja 4", testDbDao.getOneTip("4").getTitle());
    }

    @Test
    public void modifyingReadingTipOfTypeBookSavesTheModificationsToTheDatabase() throws Exception {
        testDbDao.modifyTip("1", "new title", "new info 1", "new info 2");
        ReadingTip book = (ReadingTip) testDbDao.getOneTip("1");
        assertEquals("new title", book.getTitle());
        assertEquals("new info 1", book.getMoreInfo1());
        assertEquals("new info 2", book.getMoreInfo2());
    }
    
    @Test
    public void modifyingReadingTipOfTypeVideoSavesTheModificationsToTheDatabase() throws Exception {
        ReadingTip v = new ReadingTip("Testivideo", "video");
        v.setTags(new String[]{"aihe1", "aihe2"});
        testDbDao.addTip(v);
        testDbDao.modifyTip("4", "new video title", "new info 1", "new info 2");
        ReadingTip video = (ReadingTip) testDbDao.getOneTip("4");
        assertEquals("new video title", video.getTitle());
        assertEquals("new info 1", video.getMoreInfo1());
        assertEquals("new info 2", video.getMoreInfo2());
    }
    
    @Test
    public void modifyingReadingTipOfTypePodcastSavesTheModificationsToTheDatabase() throws Exception {
        ReadingTip p = new ReadingTip("Testipodcast", "podcast");
        p.setTags(new String[]{"aihe1", "aihe2"});
        testDbDao.addTip(p);
        testDbDao.modifyTip("4", "new podcast title", "new info 1", "new info 2");
        ReadingTip video = (ReadingTip) testDbDao.getOneTip("4");
        assertEquals("new podcast title", video.getTitle());
        assertEquals("new info 1", video.getMoreInfo1());
        assertEquals("new info 2", video.getMoreInfo2());
    }
    
    @Test
    public void modifyingReadingTipOfTypeBlogPostSavesTheModificationsToTheDatabase() throws Exception {
        ReadingTip b = new ReadingTip("Testiblogpost", "blogpost");
        b.setTags(new String[]{"aihe1", "aihe2"});
        testDbDao.addTip(b);
        testDbDao.modifyTip("4", "new blogpost title", "new info 1", "new info 2");
        ReadingTip video = (ReadingTip) testDbDao.getOneTip("4");
        assertEquals("new blogpost title", video.getTitle());
        assertEquals("new info 1", video.getMoreInfo1());
        assertEquals("new info 2", video.getMoreInfo2());
    }

    @Test
    public void testMarkAsRead() throws Exception {
        testDbDao.markAsRead("1");
        ReadingTip book = (ReadingTip) testDbDao.getOneTip("1");
        assertEquals(1, book.getRead());
    }

    @Test
    public void testMarkAsUnread() throws Exception {
        testDbDao.markAsRead("1");
        testDbDao.markAsUnread("1");
        ReadingTip book = (ReadingTip) testDbDao.getOneTip("1");
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

    @Test
    public void removingExistingReadingTipRemovesTheReadingTipFromDb() throws Exception {
        assertEquals("Taijan kirja", testDbDao.getOneTip("1").getTitle());
        testDbDao.removeTip("1");
        assertEquals(null, testDbDao.getOneTip("1"));
    }
    
    @Test
    public void removingNonExistingReadingTipIsNoOp() throws Exception {
        assertEquals(3, testDbDao.getAllTips().size());
        testDbDao.removeTip("4");
        assertEquals(3, testDbDao.getAllTips().size());
    }
    
    @Test
    public void addingReadingTipWithTagAddsTheTagToTheDb() throws Exception {
        assertEquals("aihe1", testDbDao.getOneTip("1").getTags()[0]);
        assertEquals("aihe2", testDbDao.getOneTip("1").getTags()[1]);
    }
    
    @Test
    public void modifyingTagsOfCertainReadingTipSavesTheModificationsToTheDatabase() throws Exception {
        String[] newTags = new String[]{"aihe4"};
        testDbDao.modifyTags("1", newTags, false);
        assertEquals("aihe4", testDbDao.getOneTip("1").getTags()[2]);
        testDbDao.modifyTags("1", newTags, true);
        assertEquals("aihe4", testDbDao.getOneTip("1").getTags()[0]);
    }
    
    @Test
    public void searchingByAuthor() throws Exception{
    readingTips = testDbDao.searchTip("Taijan kirja", "title");
        assertEquals("Teppo Testaaja", readingTips.get(0).getMoreInfo1());
        assertEquals(1, readingTips.size());
    }
    
    @Test
    public void searchingByTitile()throws Exception{
        readingTips = testDbDao.searchTip("Taijan kirja", "title");
        assertEquals("Taijan kirja", readingTips.get(0).getTitle());
        assertEquals(1, readingTips.size());
    }
    @Test
    public void searchingByType() throws Exception{
        readingTips = testDbDao.searchTip("book", "type");
        assertEquals("Taijan kirja", readingTips.get(0).getTitle());
        assertEquals("book", readingTips.get(0).getType());
        assertEquals("Testikirja 2", readingTips.get(1).getTitle());
        assertEquals("book", readingTips.get(1).getType());
        assertEquals("Testikirja 3", readingTips.get(2).getTitle());
        assertEquals("book", readingTips.get(2).getType());
        
        assertEquals(3, readingTips.size());
    }
    @Test
    public void searchingByTags()throws Exception{
        readingTips = testDbDao.searchTip("aihe1", "tags");
        assertEquals("Taijan kirja", readingTips.get(0).getTitle());
        assertEquals("Testikirja 2", readingTips.get(1).getTitle());
        assertEquals(2, readingTips.size());
    }
    
    
}
