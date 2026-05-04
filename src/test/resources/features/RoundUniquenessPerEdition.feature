Feature: Round number uniqueness per edition
	As a tournament organizer
	I want round numbers to be unique only within an edition
	So that different editions can both have a "Round 1"

	Scenario: Two editions can both have a round with the same number
		Given I login as "admin" with password "password"
		And An edition "A" exists with year 2025 and venue "Uniq Venue A" and description "Edition A"
		And A round with number 1 exists for edition "A"
		And An edition "B" exists with year 2026 and venue "Uniq Venue B" and description "Edition B"
		And A round with number 1 exists for edition "B"
		When I search rounds by the edition id for edition "A"
		Then The response code is 200
		And The round search response should contain 1 rounds
		And The round search response should include round with number 1
		When I search rounds by the edition id for edition "B"
		Then The response code is 200
		And The round search response should contain 1 rounds
		And The round search response should include round with number 1
