@run
Feature: As a user, I want to be able to delete a reading tip

    Scenario: user can delete an existing reading tip
	Given reading tip with title "Kaiken käsikirja", type "book", and author "Esko Valtaoja" is created
	And command delete is selected
	When reading tip id "1" is given
	Then system will respond with "Reading tip successfully removed!"

    Scenario: user cannot delete nonexisting reading tip
        Given command delete is selected
        When reading tip id "198" is given
        Then system will respond with "Reading tip doesn't exist."