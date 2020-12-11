@run
Feature: As a user, I can search reading tips of any type by different search criteria

    Scenario: user can search reading tips by existing author
	Given reading tip with title "Leikkiminen kielletty", type "book", and extra info "Jukka Laajarinne" is created
        And command search is selected
        And search criteria "A" is selected
        When search term "Jukka Laajarinne" is given
        Then system's response contains "Leikkiminen kielletty"

    Scenario: user can search reading tips by existing type book
        Given reading tip with title "Kaiken käsikirja", type "book", and extra info "Esko Valtaoja" is created
        And command search is selected
        And search criteria "M" is selected
        When search term "book" is given
        Then system's response contains "Kaiken käsikirja"

    Scenario: user can search reading tips by existing type video
        Given reading tip with title "Writing good c++ 14", type "video", and extra info "www.youtube.com/video1" is created
        And command search is selected
        And search criteria "M" is selected
        When search term "video" is given
        Then system's response contains "Writing good c++ 14"

    Scenario: user can search reading tips by existing type blogpost
        Given reading tip with title "Java is beautiful", type "blogpost", and extra info "www.blogs.com/blogpost1" is created
        And command search is selected
        And search criteria "M" is selected
        When search term "blogpost" is given
        Then system's response contains "Java is beautiful"

    Scenario: user can search reading tips by existing type podcast
        Given reading tip with title "Jotta jokainen voisi oppia", type "podcast", and extra info "Koodarikuiskaaja" is created
        And command search is selected
        And search criteria "M" is selected
        When search term "podcast" is given
        Then system's response contains "Jotta jokainen voisi oppia"

    Scenario: user can search reading tips by existing title
        Given reading tip with title "Operating systems", type "book", and extra info "William Stallinger" is created
        And command search is selected
        And search criteria "T" is selected
        When search term "Operating systems" is given
        Then system's response contains "William Stallinger"

    Scenario: user can search reading tips by read
        Given reading tip with title "Humanity", type "book", and extra info "Jonathan Glover" is created
        And reading tip with title "Baudolino", type "book", and extra info "Umberto Eco" is created
        And reading tip "1" is marked as read
        And command search is selected
        And search criteria "R" is selected
        When search term "r" is given
        Then system's response contains "Jonathan Glover"

Scenario: user can search reading tips by read
        Given reading tip with title "Humanity", type "book", and extra info "Jonathan Glover" is created
        And reading tip with title "Baudolino", type "book", and extra info "Umberto Eco" is created
        And reading tip "1" is marked as read
        And command search is selected
        And search criteria "R" is selected
        When search term "r" is given
        Then system's response contains "Jonathan Glover" 2 times

Scenario: user cannot find unread reading tips by searching read reading tips
        Given reading tip with title "Humanity", type "book", and extra info "Jonathan Glover" is created
        And reading tip with title "Baudolino", type "book", and extra info "Umberto Eco" is created
        And reading tip "1" is marked as read
        And command search is selected
        And search criteria "R" is selected
        When search term "r" is given
        Then system's response does not contain "Umberto Eco"

    Scenario: user can search reading tips by unread
        Given reading tip with title "Humanity", type "book", and extra info "Jonathan Glover" is created
        And reading tip with title "Baudolino", type "book", and extra info "Umberto Eco" is created
        And reading tip "1" is marked as read
        And command search is selected
        And search criteria "R" is selected
        When search term "u" is given
        Then system's response contains "Umberto Eco"

Scenario: user cannot find read reading tips by searching unread reading tips
        Given reading tip with title "Humanity", type "book", and extra info "Jonathan Glover" is created
        And reading tip with title "Baudolino", type "book", and extra info "Umberto Eco" is created
        And reading tip "1" is marked as read
        And command search is selected
        And search criteria "R" is selected
        When search term "u" is given
        Then system's response contains "Jonathan Glover" 1 times
