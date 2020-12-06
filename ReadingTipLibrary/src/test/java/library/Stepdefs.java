package library;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import java.io.File;
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
    ReadeinTipService service;
    
    
    @Before
    public void setup(){
        inputLines = new ArrayList<>();     
        io = new StubIo(inputLines);
        ui = new ReadingTipUi(io);
        service = new ReadeinTipService(new ReadingTipDatabaseDao("jdbc:sqlite:ReadingTipTest.db"));
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
            if (s.contains(expectedOutput)) {
                contains = true;
            }
        }
        assertTrue(contains);
    }
   
    @When("all reading tips are listed")
    public void readingTipIsSaved() throws Exception {
        inputLines.add("L");
        io = new StubIo(inputLines);
        ui = new ReadingTipUi(io);
        ui.start(service);        
    }

    @Given("reading tip with title {string}, type {string}, and author {string} is created")
    public void readingTipWithTitleAndTypeIsCreated(String title, String type, String author) throws Exception {
	inputLines.add("A");
	inputLines.add(title);
        inputLines.add(type);
        inputLines.add(author);
        inputLines.add("isbn");
    }

    @Given("command delete is selected")
    public void commandDeleteIsSelected() {
        inputLines.add("D");
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
    

//    @Then("a reading tip {string} with type {string} is printed")
//    public void systemWillRespondWith(String title, String type) {
//        assertTrue(io.getPrints().contains(title));
//        assertTrue(io.getPrints().contains(type));
//    }

}
