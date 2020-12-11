@run
Feature: As a user I want to be able to edit my reading tip
    Scenario: User can modify a reading tip
    Given reading tip with title "Dune", type "book", and extra info "Herbert" is created
    Given command modify is selected
    And reading tip id "1" is given
    And new title "Dyyni" is given
    And new author "Frank Herbert" is given
    And new isbn "9780441172719" is given
    Then system's response contains "Title: Dyyni"


    Scenario: User cannot modify a nonexisting reading tip
    Given reading tip with title "Dune", type "book", and extra info "Herbert" is created
    And command modify is selected
    When reading tip id "99" is given
    Then system will respond with "Reading tip doesn't exist."

    Scenario: Readint tip is unchanged if user does not enter values
    Given reading tip with title "Dune", type "book", and extra info "Herbert" is created
    Given command modify is selected
    And reading tip id "1" is given
    And new title "" is given
    And new author "" is given
    And new isbn "" is given
    Then system's response contains "Title: Dune"
    
