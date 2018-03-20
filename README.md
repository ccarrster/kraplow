# kraplow
A web Western Card Game you can play online - Implements the same rules as a western card game!
![alt tag](https://raw.github.com/ccarrster/kraplow/master/westerncardgame/logo2.png)
Multiplayer, Single Player or a mix of AI and Humans.
Features Chat, Game Log, Ajax Polling

Setup and deployment
I have been using Eclipse and Apache Tomcat.
I build the project to a bin folder.
The bin folder I use as the root of the application so copy the bin folder contents to {tomcat path}/webapps/kraplow
In the bin folder there is also a "com" folder that contains the compiled java classes, these need to be moved to {tomcat path}/webbaps/kraplow/WEB-INF/classes
