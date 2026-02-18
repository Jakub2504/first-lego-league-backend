Feature: Manage Scientific Project
    In order to manage scientific project evaluations
    As a user
    I want to be able to create, retrieve, edit and delete scientific projects

    Scenario: Create a scientific project
        Given There is a registered user with username "user" and password "password" and email "user@sample.app"
        And I login as "user" with password "password"
        When I create a new scientific project with score 85 and comments "Great innovation"
        Then The response code is 201


