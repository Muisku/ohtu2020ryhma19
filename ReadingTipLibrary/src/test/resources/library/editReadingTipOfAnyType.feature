@run
Feature: As a user, I can edit reading tips of any type

    Scenario: User can modify a reading tip of type video
        When command add is selected
        When a reading tip "New Tip" with type "video" is created
        Given command modify is selected
        And reading tip id "1" is given
        And new title "Video-test" is given
        And new url "www.www" is given
        Then system's response contains "Title: Video-test"

    Scenario: User can modify a reading tip of type podcast
        When command add is selected
        When a reading tip "New Tip" with type "podcast" is created
        Given command modify is selected
        And reading tip id "1" is given
        And new title "Podcast-test" is given
        And new author "test" is given
        When enter is pressed
        Then system's response contains "Title: Podcast-test"

    Scenario: User can modify a reading tip of type blogpost
        When command add is selected
        When a reading tip "New Tip" with type "blogpost" is created
        Given command modify is selected
        And reading tip id "1" is given
        And new title "Blogpost-test" is given
        And new author "test" is given
        When enter is pressed
        Then system's response contains "Title: Blogpost-test"
 
    Scenario: User can modify a reading tip of type book
        When command add is selected
        When a reading tip "New Tip" with type "book" is created
        Given command modify is selected
        And reading tip id "1" is given
        And new title "Book-test" is given
        And new author "test" is given
        When enter is pressed
        Then system's response contains "Title: Book-test"

    
