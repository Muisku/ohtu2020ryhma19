/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.domain;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReadingTipServiceTest {

    ReadingTipService service;
    

    public ReadingTipServiceTest() {
    }

    @Before
    public void setUp() throws Exception {
        FakeReadingTipDao readingTip = new FakeReadingTipDao();
        readingTip.addTip(new ReadingTip("First title", "book"));
        readingTip.addTip(new ReadingTip("Second title", "book"));
        readingTip.addTip(new ReadingTip("Third title", "book"));
        
        service = new ReadingTipService(readingTip);
        
    }

    @Test
    public void atStartListContainsSetUpValues() throws Exception {
        List<ReadingTip> readingTips = service.browseReadingTips();
        assertEquals(3, readingTips.size());
    }

    @Test
    public void readingTipContainsAllValues() throws Exception {
        List<ReadingTip> readingTips = service.browseReadingTips();

    }

    @Test
    public void readingTipCanBeFound() throws Exception {
        assertEquals("First title", service.getOneTip("1").getTitle());
    }
    
    @Test
    public void readingTipCanBeAdded() throws Exception {
        service.createTip("book", "Title", "Writer", "1234");
        ReadingTip r = service.getOneTip("4");
        assertEquals(4, service.browseReadingTips().size());
        assertEquals("Title", r.getTitle());
    }

    @Test
    public void readingTipCanBeModified() throws Exception {
        service.modifyTip("1", "", "Herbert", "1234");
        ReadingTip r = service.getOneTip("1");
        assertEquals("Herbert", r.getMoreInfo1());
    }
    
    @Test
    public void readingTipCanBeDeleted() throws Exception {
        service.removeTip("1");
        assertEquals(2, service.browseReadingTips().size());
    }
    
    @Test
    public void readingTipCanBeMarkedAsReadAndUnread() throws Exception {
        service.markAsRead("2");
        ReadingTip r = service.getOneTip("2");
        assertEquals(1, r.getRead());
        
        service.markAsUnread("2");
        r = service.getOneTip("2");
        assertEquals(0, r.getRead());
    }
}
