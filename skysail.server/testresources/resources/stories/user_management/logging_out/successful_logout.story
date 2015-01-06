Meta:

Narrative:

In order to be de-authorized and let no other use protected parts of a skysail application
As a user 
I want to able to logout.

Scenario: Logout with post
Meta:

Given an existing user wants to logout
When the user submits the login form with the username <username> and the password <password>
And the users logs out
Then the request is successful
And the user is logged out

Examples:
|username|password|
|linus|linus|
|izzy|izzy|
