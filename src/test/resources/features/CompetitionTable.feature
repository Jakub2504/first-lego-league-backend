Feature: Competition Table Management
	As a tournament organizer
	I want to manage tables and their referees
	So that each match has proper supervision

	Scenario: Adding a referee to a table
    	Given a new Competition Table with id "Table-A"
    	When I add a referee named "John Doe" to the table
    	Then the table should have 1 referee
    	And the referee "John Doe" should be supervising "Table-A"

	Scenario: Exceeding the maximum number of referees
    	Given a new Competition Table with id "Table-B"
    	And the table already has 3 referees
		When I try to add another referee to the table
    	Then the validation should prevent adding a 4th referee