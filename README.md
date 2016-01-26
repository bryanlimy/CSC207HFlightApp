# Flight App from CSC207H Final Project
********************************************************************************
*                              Flight Application                              *
********************************************************************************
The location of the Android project is PIII/CSC207Project.
The Activity classes are located in /src/cs/g0365/csc207project.
The back-end classes are located in /src/cs/g0365/csc207project/backend.
The Driver class is located in /src/driver.

Before the application is run, some files located in the PIII directory must be 
pushed to the internal storage directory located at:
/data/data/cs.g0365.csc207project/files/

Files to push to device:
- passwords.txt
- admins.csv
- default_clients.csv

Location of csv files:
Any csv files to be uploaded should be also be placed in the internal storage 
directory located at:
/data/data/cs.g0365.csc207project/files/

Logging in:
The format of the passwords.txt files is, on each line, the username followed by
the password.
For example, for the line:
admin,admin
The username would be admin and password would be admin.
The username corresponds to the email of the user.
The first four users are administrators and the rest are clients.

New users:
Newly uploaded users can log in by using their email and the word 'password' as
their password. 
