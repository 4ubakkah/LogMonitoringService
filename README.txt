1.) To run application start SpringBootWebApplication. 
2.) To run test start AllTestsRunner.
3.) When tomcat starts web client is accessible via following link: http://localhost:8080/
4.) application.properties file is used to configure monitoring interval, path to log file, monitoring thread sleep timeout.
When monitoring service is started web client kicks back-end service each 5 seconds, consuming conforming log entries (the ones withing monitoring interval window), rest log records are silently ignored.
"Start monitoring" button is used to start monitoring thread which reads new entries and goes to sleep for configured period of time.
"Stop monitoring" stops monitoring thread and cleans up list of log entries read from log file.
"Update configuration" button switches to form for configuration update. When "Update" button is clicked monitoring thread is stopped and started with updated configuration.
"Switch to log list" returns you back to main page displaying count of log entries for each type of log (ERROR, WARNING, INFO).

P.S. to be able to use application you have to have Java 8,Maven, IDE of your choice installed and properly setup on your local environment.