@run
Feature: As a user, I can divide reading tips into categories "read" and "waiting to be read"

    Scenario: user can mark a reading tip as read
	Given reading tip with title "Atlas Shrugged", type "book", and author "Ayn Rand" is created
	And command mark as read is selected
	When reading tip id "1" is given
	Then system's response contains "Read: 1"

    Scenario: user can mark a reading tip as unread
        Given reading tip with title "Atlas Shrugged", type "book", and author "Ayn Rand" is created
	And command mark as unread is selected
	When reading tip id "1" is given
	Then system's response contains "Read: 0"
