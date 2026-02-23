Feature: Match Management
    As a tournament organizer
    I want to create matches via API
    So that the data is persisted correctly in the database

    Scenario: Successfully create a match via REST API
        Given a Round with number 1 exists in the system
        And a Competition Table with ID "Table-A" exists in the system
        When I send a POST request to "/matches" with start "10:00", end "11:00", round 1 and table "Table-A"
        Then the response status should be 201
        And the database should contain a match between "10:00" and "11:00"
        And this match must be linked to Round 1 and Table "Table-A"