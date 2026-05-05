Feature: Get matches by competition table

	Scenario: Retrieve matches for a table with scheduled matches
		Given a competition table with 2 matches exists
		When I request matches for the target competition table
		Then The response code is 200
		And the competition table matches response contains 2 matches

	Scenario: Retrieve matches for a table with no matches
		Given a competition table without matches exists
		When I request matches for the target competition table
		Then The response code is 200
		And the competition table matches response contains 0 matches

	Scenario: Retrieve matches for a non-existing table returns not found
		When I request matches for competition table "NON_EXISTING_TABLE"
		Then The response code is 404
		And the competition table matches error is "COMPETITION_TABLE_NOT_FOUND"