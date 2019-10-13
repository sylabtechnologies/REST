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


-- ContactPayments batch helper --

design as a batch loop

load database connection properties from xml

use JDBC rowset with cache

run as :  ContactPayments CsvFileName (w/o extension)

create a table w/ CsvFileName

use 11 columns for validation plus row# as primary key,
throw IllegalArgumentException if not 11

test on Apache Derby

