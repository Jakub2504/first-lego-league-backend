Feature: Round Management
	As a tournament organizer
	I want to manage matches within a round
	So that the tournament structure remains consistent

	Scenario: Adding matches to a round
    	Given a new Round with number 1
    	When I add 3 new matches to this round
 		Then the round should contain 3 matches
    	And each match should reference the round with number 1

	Scenario: Removing a match from a round
    	Given a new Round with number 2
    	And the round has 2 matches
    	When I remove one match from the round
    	Then the round should contain 1 match
    	And the removed match should no longer reference the round