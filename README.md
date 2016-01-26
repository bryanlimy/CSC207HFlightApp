# Flight App from Software Design Final Project

Before the application is run, some files located in the Testing Files directory must be 
pushed to the internal storage directory located at:
/data/data/app_name/files/

Files to push to device:
- passwords.txt
- admins.csv
- default_clients.csv

Location of csv files:
Any csv files to be uploaded should be also be placed in the internal storage 
directory located at:
/data/data/app_name/files/

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
