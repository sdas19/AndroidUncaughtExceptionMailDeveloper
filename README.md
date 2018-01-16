# AndroidUncaughtExceptionMailDeveloper
Mail will be sent to the developer with log file generated in case of uncaught exception


## Summary

 

This application is responsible to let the developer know about the reason of application crash by mailing developer the log document.

 

## Functionality

 

When the app crashes a background service starts and generates the log document file. If there is internet connectivity available the file is sent to the developer's email id and the file is removed from phone. Subsequently the service is destroyed.

 

If there is no internet connectivity the service places a broadcast receiver to look for the change of internet state. If state changes receiver looks for internet and if now internet is there the file is send to email id of developer and removed. The service is also destroyed after that.

 

## How to Use

 

In the LogService.java put the changes in sender's email, sender's password and recepient's email.

App will use the sender's email and password to send the mail from that account.

![changes in gmail credentials](https://user-images.githubusercontent.com/35428806/35002261-19dd381e-fb0f-11e7-9646-1fd1afea6256.png)

Make the changes here...

## Contributions are welcome :)
