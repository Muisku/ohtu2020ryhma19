/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.domain;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import library.domain.ReadingTip;

public class ReadingTipServiceTest {

    FakeReadingTipDao readingTip;
    ReadingTipService service;
    List<ReadingTip> readingTips;

    public ReadingTipServiceTest() {
    }

    @Before
    public void setUp() throws Exception {
        
        readingTip = new FakeReadingTipDao();
        service = new ReadingTipService();
        readingTips = new ArrayList<>();
        readingTip.addTip(new BookTip("First title"));
        readingTip.addTip(new BookTip("Second title"));
        readingTip.addTip(new BookTip("Third title"));
        readingTips.add(new BookTip("Dune"));

    }

    @Test
    public void atStartListContainsSetUpValues() throws Exception {
        List<ReadingTip> readingTips = readingTip.getAllTips();
        assertEquals(3, readingTips.size());
    }

    @Test
    public void readingTipContainsAllValues() throws Exception {
        List<ReadingTip> readingTips = readingTip.getAllTips();

    }

    @Test
    public void readingTipContainsSpecificTip() throws Exception {
        List<ReadingTip> readingTips = readingTip.getAllTips();

        assertEquals(readingTips.get(0).getTitle(), "First title");
    }

    @Test
    public void anAuthorAndIsbnCanBeAddedForBook() throws Exception {
        //readingTips.get(0).setMoreInfo1("Herbert");
        //readingTips.get(0).setMoreInfo2("3105121");
        // service.getOneTip("1");
        readingTips.get(0).setMoreInfo1("Herbert");
        readingTips.get(0).setMoreInfo2("1234");
        //assertEquals(service.getOneTip("1").getMoreInfo1(), "herb");
        assertEquals(readingTips.get(0).getMoreInfo2(), "1234");
    }
    
}
