Feature: Match Management
	As a tournament organizer
	I want to create matches within rounds and tables
	So that I can schedule the competition properly

	Scenario: Successfully create a match with time and associations
		Given a Round exists
    	And a Competition Table exists
    	When I create a new match starting at "10:00" and ending at "11:00"
		Then the match should be linked to the round and the table
		And the match duration should be "60" minutes