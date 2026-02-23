Feature: Round Management
	As a tournament organizer
	I want to manage matches within a round
	So that the tournament structure remains consistent

	Scenario: Adding matches to a round via Database
		Given a new Round with number 1 is saved
		When I add 3 matches via repository
		Then the database should show 3 matches for this round

	Scenario: Removing a match and verifying orphan removal
		Given a new Round with number 2 is saved
		And I add 1 matches via repository
		When I remove a match from the saved round
		Then the match should be deleted from the database