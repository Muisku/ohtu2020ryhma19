@run
Feature: As a user I want to be able to add tags for a reading tip

    Scenario: Create tag for existing reading tip
        Given reading tip with title "Dune", type "book", and author "Herbert" is created
        And command tags is selected
        And reading tip id "1" is given
        And choose add or remove "A" is given
        When tags "Best book" is given
        Then system will respond with "Input tag:"
        When tags "" is given
        Then system will respond with "Give a command:"
        

    Scenario: Tag cannot be added to nonexisting reading tip
        Given reading tip with title "Dune", type "book", and author "Herbert" is created
        And command tags is selected
        And reading tip id "99" is given
        Then system will respond with "Reading tip doesn't exist."

    Scenario: Remove tag from existing reading tip
    Given reading tip with title "Dune", type "book", and author "Herbert" is created
        And command tags is selected
        And reading tip id "1" is given
        And choose add or remove "R" is given
        When tags "tag" is given
        Then system will respond with "Give a command:"
       