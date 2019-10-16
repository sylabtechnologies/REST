# REST - caution: Glassfish 4.1.1 bombs Json

-- RESTful web services --

ER: person(PK personId) <-> address(PK id, FK personId)
                        <-> communication(PK id, FK personId)

Get Glassfish

Create Derby database jdbc:derby://localhost:1527/contacts [root on ROOT] w/ username ROOT,
run createTables

Start Netbeans and run both services

Use these services for webapp,
as a scrollpane w/ contacts and add edit delete buttons

