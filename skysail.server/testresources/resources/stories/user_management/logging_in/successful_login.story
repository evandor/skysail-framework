Meta:

Narrative:

In order to be authorized and use protected parts of a skysail application
As a user 
I want to able to login.

Scenario: Login with post
Meta:

Given an existing user wants to login
When the user submits the login form with the username <username> and the password <password>
Then the request is successful

Examples:
|username|password|
|linus|linus|
|izzy|izzy|
