Feature: Referee Specific Logic
	As a tournament organizer
	I want to manage referee expertise and table assignments
	To ensure each match is properly supervised

	Scenario: Setting referee expertise
		Given a new referee
		When I set the referee as an expert
		Then the referee should be recognized as an expert

	Scenario: Managing table assignment
		Given a new referee
		And a competition table exists with ID "Table-01"
		When I assign the referee to supervise "Table-01"
		Then the referee should reference "Table-01" as their assigned table
		And the table "Table-01" should list the referee in its staff