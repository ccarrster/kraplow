![alt tag](./WebContent/logo2.png)

# kraplow
A web Western Card Game you can play online - Implements the same rules as a western card game!
Multiplayer, Single Player or a mix of AI and Humans.
Features Chat, Game Log, Ajax Polling.

## Setup and deployment
You will need ant, so download it, add the `ANT_HOME c:\ant`
to environment variables and add `c:\ant\bin` to your `PATH`.
You will need `JUnit`, so download it, add `JUNIT_HOME` to
environment variables and set it to the folder where you put
the `JUnit` `.jar` file. Also create a `JUNIT_VERSION`
environment variable and set it to the version number of the
jar file (e.g. '4.12' for junit-4.12.jar).

## To build a new westerncardgame.war
Fire up a new console window
```
ant clean
ant
```
Then put the `westerncardgame.war` from the dist folder into
your apache tomcat webapps folder and restart tomcat.
Go to url `http://localhost:8080/westerncardgame`.
Port may be different depending on your tomcat setup (80?).

Most of the UI is in `index.html` and is driven from the API.
The `index.html` file has a `getServletUrl()` function. If you
name your project something other than "westerncardgame" in
the `build.xml/war` file you will need to change that function.

When deploying I change the owner group to `tomcat:tomcat` with
```
sudo chown -R tomcat:tomcat kraplow
```

### Deployment via Docker
There is also a `Dockerfile` for building an image that is based
on the official [`tomcat:9.0`](https://hub.docker.com/_/tomcat) docker image
and contains all dependencies. During the building process all configured
tests are run with ant. To build the image run
```
docker build -t kraplow:latest .
```

To run the image:
```
docker run -it --rm -p 8888:8080 kraplow
```
This will expose port 8080 from the container to `localhost:8888`.

## Miscellaneous
The code uses an API.
I called mine /chat - you can edit this in `web.xml`.
All of the code depends on `/chat` being there.
You will need to change this in the `getServletUrl()` function.
Messages are sent as XML or strings.

The main code for the AI is a function named `somethingAI()`.
From there the AI choose how to respond to actions.

The main frontend loops poll on what action to do next and chat.

There is no database, everything is stored in session memory.

## TODO
Working on Sidestep Township expansion.  
Need to add images for new blue, brown, green cards.  
Need to show card suits for Apache Kid.  
Need to fix bugs I created.  
