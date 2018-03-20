![alt tag](https://raw.github.com/ccarrster/kraplow/master/westerncardgame/logo2.png)
```
# kraplow
A web Western Card Game you can play online - Implements the same rules as a western card game!
Multiplayer, Single Player or a mix of AI and Humans.
Features Chat, Game Log, Ajax Polling

Setup and deployment
I have been using Eclipse and Apache Tomcat.
I build the project to a bin folder.
The bin folder I use as the root of the application so copy the bin folder contents to {tomcat path}/webapps/kraplow
In the bin folder there is also a "com" folder that contains the compiled java classes, these need to be moved to {tomcat path}/webbaps/kraplow/WEB-INF/classes
After getting the files in place restart tomcat.
I have tomcat setup as a service "sudo service tomcat restart"
Most of the UI is in index.html and is driven from the API.

When deploying I change the owner group to tomcat:tomcat with sudo chown -R tomcat:tomcat kraplow

The code uses an API.
Messages are sent as XML or strings.

The main code for the AI is a function named somethingAI()
From there the AI choose how to respond to actions.

The main frontend loops poll on what action to do next and chat.
```
