# SNOMED Cave Proof of concept

## Running the application
From the same directory as this README.md file run the following commands

```
$ rm -rf ~/.sc-poc
$ mvn clean install tomcat7:run
```

And wait for the following line to appear in the output
> INFO: Starting ProtocolHandler ["http-bio-8080"]

After that, the application will be available on http://localhost:8080. Stop the app server by pressing `CTRL-C`.

### Re-importing data
Importing should be done automatically when building the project with

```
rm -rf ~/.sc-poc
mvn clean install
```
