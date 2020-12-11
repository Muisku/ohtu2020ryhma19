@run
Feature: As a user I want to be able to search for reading tips

    Scenario: user can search reading tips by existing author
	Given reading tip with title "Leikkiminen kielletty", type "book", and author "Jukka Laajarinne" is created
        And command search is selected
        And search criteria "A" is selected
        When search term "Jukka Laajarinne" is given
        Then system's response contains "Leikkiminen kielletty"

    Scenario: user can search reading tips by existing type
        Given reading tip with title "Kaiken käsikirja", type "book", and author "Esko Valtaoja" is created
        And command search is selected
        And search criteria "M" is selected
        When search term "book" is given
        Then system's response contains "Kaiken käsikirja"

    Scenario: user can search reading tips by existing title
        Given reading tip with title "Operating systems", type "book", and author "William Stallinger" is created
        And command search is selected
        And search criteria "T" is selected
        When search term "Operating systems" is given
        Then system's response contains "William Stallinger"

    Scenario: user can search reading tips by read
        Given reading tip with title "Humanity", type "book", and author "Jonathan Glover" is created
        And reading tip with title "Baudolino", type "book", and author "Umberto Eco" is created
        And reading tip "1" is marked as read
        And command search is selected
        And search criteria "R" is selected
        When search term "r" is given
        Then system's response contains "Jonathan Glover"

    Scenario: user can search reading tips by unread
        Given reading tip with title "Humanity", type "book", and author "Jonathan Glover" is created
        And reading tip with title "Baudolino", type "book", and author "Umberto Eco" is created
        And reading tip "1" is marked as read
        And command search is selected
        And search criteria "R" is selected
        When search term "u" is given
        Then system's response contains "Umberto Eco"

