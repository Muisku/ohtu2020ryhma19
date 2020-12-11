@run
Feature: As a user, I can create a reading tip with type and title

    Scenario: Create a book tip
        Given command add is selected
        When a reading tip "New Tip" with type "book" is created
        Then system will respond with "Give a command:"

    Scenario: Create a blog post tip
        Given command add is selected
        When a reading tip "New Tip" with type "blogpost" is created
        Then system will respond with "Give a command:"

    Scenario: Create a video tip
        Given command add is selected
        When a reading tip "New Tip" with type "video" is created
        Then system will respond with "Give a command:"

    Scenario: Create a podcast tip
        Given command add is selected
        When a reading tip "New Tip" with type "podcast" is created
        Then system will respond with "Give a command:"

