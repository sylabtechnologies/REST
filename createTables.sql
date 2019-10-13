/*
ER:

Person(PK personId) --- address(PK id, FK personId)
                    --- communication(PK id, FK personId)

*/

use contacts;

CREATE TABLE person (
personId int NOT NULL,
FirstName varchar(40),
LastName varchar(40),
DOB DATE,
Gender  varchar(2),
Title varchar(40),
PRIMARY KEY (personId) );

CREATE TABLE address (
entryId int NOT NULL,
personId int NOT NULL,
type varchar(40),
number varchar(20),
street varchar(40),
Unit varchar(20),
City varchar(40),
State varchar(4),
zipcode varchar(10),
PRIMARY KEY (entryId) );

CREATE TABLE communication (
entryId int NOT NULL,
personId int NOT NULL,
type varchar(10),
value varchar(80),
preferred int,
PRIMARY KEY (entryId) );
