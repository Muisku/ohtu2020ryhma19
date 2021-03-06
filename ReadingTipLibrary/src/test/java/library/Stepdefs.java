package library;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import library.dao.*;
import library.domain.*;
import library.io.*;
import library.ui.*;

public class Stepdefs {
    ReadingTipUi ui;
    List<String> inputLines;
    StubIo io;
    ReadingTipService service;
        
    @Before
    public void setup(){
        inputLines = new ArrayList<>();     
        io = new StubIo(inputLines);
        ui = new ReadingTipUi(io);
        service = new ReadingTipService(new ReadingTipDatabaseDao("jdbc:sqlite:ReadingTipTest.db"));
    }

    @After
    public void tearDown() {
        service.deleteDatabaseContents();
    }
    
    @Given("command add is selected")
    public void commandAddIsSelected() {
        inputLines.add("A");
    }
   
    @When("a reading tip {string} with type {string} is created")
    public void newReadingTipWithTypeIsCreated(String title, String type) throws Exception {
        inputLines.add(title);
        inputLines.add(type);
        inputLines.add("author");
        inputLines.add("tag");
        inputLines.add("");
        
        io = new StubIo(inputLines);
        ui = new ReadingTipUi(io);
        ui.start(service);
    }
      
    @Then("system will respond with {string}")
    public void readingTipCanBeFound(String expectedOutput) {
        assertTrue(io.getPrints().contains(expectedOutput));
    }
    
    @Then("system's response contains {string}")
    public void responseContains(String expectedOutput) {
        boolean contains = false;
        for (String s : io.getPrints()) {
            //System.out.println(s);
            if (s.contains(expectedOutput)) {
                contains = true;
            }
        }
        assertTrue(contains);
    }
    
    @Then("system's response contains {string} {int} times")
    public void responseContainsXTimes(String expectedOutput, int times) {
        int contains = 0;
        for (String s : io.getPrints()) {
            //System.out.println(s);
            if (s.contains(expectedOutput)) {
                contains++;
            }
        }
        assertEquals(times, contains);
    }
    
    @Then("system's response does not contain {string}")
    public void responseDoesNotContain(String notExpectedOutput) {
        boolean contains = false;
        for (String s : io.getPrints()) {
            if (s.contains(notExpectedOutput)) {
                contains = true;
            }
        }
        assertFalse(contains);
    }
   
    @When("all reading tips are listed")
    public void readingTipIsSaved() throws Exception {
        inputLines.add("L");
        io = new StubIo(inputLines);
        ui = new ReadingTipUi(io);
        ui.start(service);        
    }

    @Given("reading tip with title {string}, type {string}, and extra info {string} is created")
    public void ReadingTipWithTitleAndTypeIsCreated(String title, String type, String info) throws Exception {
	inputLines.add("A");
	inputLines.add(title);
        inputLines.add(type);
        inputLines.add(info);
        inputLines.add("isbn");
        inputLines.add("tag");
        inputLines.add("");
    }
    
    @Given("command delete is selected")
    public void commandDeleteIsSelected() {
        inputLines.add("D");
    }
    
    @Given("command mark as read is selected")
    public void commandMarkAsReadIsSelected() {
        inputLines.add("R");
    }
    
    @Given("command mark as unread is selected")
    public void commandMarkAsUnreadIsSelected() {
        inputLines.add("U");
    }

    @When("reading tip id {string} is given")
    public void readingTipIsDeleted(String id) throws Exception {
	inputLines.add(id);

	io = new StubIo(inputLines);
        ui = new ReadingTipUi(io);
        ui.start(service);
    }
    
    @Given("command search is selected")
    public void commandSearchIsSelected() {
        inputLines.add("S");
    }   
    
    @Given("search criteria {string} is selected")
    public void searchCriteriaIsSelected(String criteria) {
        inputLines.add(criteria);
    }
    
    @When("search term {string} is given")
    public void searchByTerm(String term) throws Exception {
        inputLines.add(term);

        io = new StubIo(inputLines);
        ui = new ReadingTipUi(io);
        ui.start(service);
    }
    
    @Given("command tags is selected")
    public void commandTagsIsSelected(){
        inputLines.add("T");
    }
    
    @Given("choose add or remove {string} is given")
    public void tagOptionIsSelected(String option){
        inputLines.add(option);
    }
    
    @When("tags {string} is given")
    public void addTagToTip(String tag) throws Exception{
        inputLines.add(tag);
    }
    
    @Given("command modify is selected")
    public void commandModifyIsSelected(){
        inputLines.add("M");
    }
    
    @Given("new title {string} is given")
    public void modifiedTitle(String title){
        inputLines.add(title);
    }
    
    @Given("new author {string} is given")
    public void modifiedAuthor(String author){
        inputLines.add(author);
    }

    @When("enter is pressed")
    public void enterPressed(){
        inputLines.add("");
        io = new StubIo(inputLines);
        ui = new ReadingTipUi(io);
        ui.start(service);
    }
    
    @When("new isbn {string} is given")
    public void modifiedIsbn(String isbn) throws Exception{
        inputLines.add(isbn);

	io = new StubIo(inputLines);
        ui = new ReadingTipUi(io);
        ui.start(service);
    }
    
    @When("new url {string} is given")
    public void modifiedUrl(String url) throws Exception{
        inputLines.add(url);

	io = new StubIo(inputLines);
        ui = new ReadingTipUi(io);
        ui.start(service);
    }
    
    @Given("reading tip {string} is marked as read")
    public void readingTipMarkedRead(String id) {
        inputLines.add("R");
        inputLines.add(id);
    }
}

