package com.chriscarr.bang;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class AjaxServlet extends HttpServlet {

  public void doGet(HttpServletRequest request, HttpServletResponse response)
                               throws ServletException, IOException {
	  response.setContentType("text/xml"); 
      response.setHeader("Cache-Control", "no-cache");
      
      String messageType = request.getParameter("messageType");
      if(messageType != null && !messageType.equals("")){
    	if(messageType.equals("GETGAMESTATE")){
	    	JSPUserInterface userInterface = (JSPUserInterface)WebInit.userInterface;
	    	if(userInterface != null){
	    		GameState gameState = userInterface.getGameState();
	    		response.getWriter().write("<gamestate>");
		    		response.getWriter().write("<players>");
		    		for(GameStatePlayer player : gameState.getPlayers()){
		    			writePlayer(player, response);
		    		}
		    		response.getWriter().write("</players>");
		    		if(gameState.isGameOver()){
		    			response.getWriter().write("<gameover/>");
		    		}
		    		response.getWriter().write("<currentname>");
		    		response.getWriter().write(gameState.getCurrentName());
		    		response.getWriter().write("</currentname>");
		    		response.getWriter().write("<decksize>");
		    		response.getWriter().write(Integer.toString(gameState.getDeckSize()));
		    		response.getWriter().write("</decksize>");
		    		GameStateCard topCard = gameState.discardTopCard();
		    		if(topCard != null){
		    			response.getWriter().write("<discardtopcard>");
		    			writeCard(topCard, response);
		    			response.getWriter().write("</discardtopcard>");
		    		}
	    		response.getWriter().write("</gamestate>");
	    	} else {
	    		response.getWriter().write("<gamestate/>");
	    	}
    	} else if(messageType.equals("JOIN")){
    		String user = WebGame.join();
    		if(user != null){
    			response.getWriter().write("<user>");
    			response.getWriter().write(user);
    			response.getWriter().write("</user>");
    		} else {
    			response.getWriter().write("<fail/>");
    		}
    	} else if(messageType.equals("LEAVE")){
    		String user = request.getParameter("user");
    		WebGame.leave(user);
    		response.getWriter().write("<ok/>");
    	} else if(messageType.equals("COUNTPLAYERS")){
    		response.getWriter().write("<playercount>");
    		response.getWriter().write(Integer.toString(WebGame.getCountPlayers()));
    		response.getWriter().write("</playercount>");
    	} else if(messageType.equals("CANSTART")){
    		if(WebGame.canStart()){
    			response.getWriter().write("<yes/>");
    		} else {
    			response.getWriter().write("<no/>");
    		}
    	} else if(messageType.equals("START")){
    		WebGame.start();
    		response.getWriter().write("<ok/>");
    	} else if(messageType.equals("GETMESSAGE")){
    		String user = request.getParameter("user");
    		JSPUserInterface userInterface = (JSPUserInterface)WebInit.userInterface;
    		List<String> messages = ((WebGameUserInterface)userInterface).getMessages(user);
    		if(!messages.isEmpty()){
    			response.getWriter().write("<message>");
    			response.getWriter().write(messages.get(0));
    			response.getWriter().write("</message>");
    		} else {
    			response.getWriter().write("<ok/>");
    		}
    	} else if(messageType.equals("SENDRESPONSE")){
    		String user = request.getParameter("user");
    		String responseMessage = request.getParameter("response");
    		JSPUserInterface userInterface = (JSPUserInterface)WebInit.userInterface;
    		List<String> messages = ((WebGameUserInterface)userInterface).getMessages(user);
    		messages.remove(0);
    		if(!"".equals(responseMessage)){
    			((WebGameUserInterface)userInterface).addResponse(user, responseMessage);
    		}
    		response.getWriter().write("<ok/>");
    	} else if(messageType.equals("GETPLAYERINFO")){
    		String user = request.getParameter("user");
    		JSPUserInterface userInterface = (JSPUserInterface)WebInit.userInterface;    		
    		String name = ((WebGameUserInterface)userInterface).getPlayerForUser(user);
    		String role = userInterface.getRoleForName(name);
    		String goal = userInterface.getGoalForName(name);
    		response.getWriter().write("<userinfo>");
    			response.getWriter().write("<name>");
    			response.getWriter().write(name);
    			response.getWriter().write("</name>");
    			response.getWriter().write("<role>");
    			response.getWriter().write(role);
    			response.getWriter().write("</role>");
    			response.getWriter().write("<goal>");
    			response.getWriter().write(goal);
    			response.getWriter().write("</goal>");
    		response.getWriter().write("</userinfo>");
    	}
      }
  }
  
  private void writePlayer(GameStatePlayer player, HttpServletResponse response) throws IOException{
	  	response.getWriter().write("<player>");
	  	response.getWriter().write("<name>");
		response.getWriter().write(player.name);
		response.getWriter().write("</name>");
		response.getWriter().write("<specialability>");
		response.getWriter().write(player.specialAbility);
		response.getWriter().write("</specialability>");
		response.getWriter().write("<health>");
		response.getWriter().write(Integer.toString(player.health));
		response.getWriter().write("</health>");
		response.getWriter().write("<maxhealth>");
		response.getWriter().write(Integer.toString(player.maxHealth));
		response.getWriter().write("</maxhealth>");
		response.getWriter().write("<handsize>");
		response.getWriter().write(Integer.toString(player.handSize));
		response.getWriter().write("</handsize>");
		if(player.isSheriff){
			response.getWriter().write("<issheriff/>");
		}
		if(player.gun != null){
			response.getWriter().write("<gun>");
			writeCard(player.gun, response);
			response.getWriter().write("</gun>");
		}
		List<GameStateCard> inPlay = player.inPlay;
		if(inPlay != null && !inPlay.isEmpty()){
			response.getWriter().write("<inplay>");
			for(GameStateCard inPlayCard : inPlay){
				response.getWriter().write("<inplaycard>");
				writeCard(inPlayCard, response);
				response.getWriter().write("</inplaycard>");
			}			
			response.getWriter().write("</inplay>");
		}
		response.getWriter().write("</player>");
  }
  
  private void writeCard(GameStateCard card, HttpServletResponse response) throws IOException{	  	
		response.getWriter().write("<name>");
		response.getWriter().write(card.name);
		response.getWriter().write("</name>");
		response.getWriter().write("<suit>");
		response.getWriter().write(card.suit);
		response.getWriter().write("</suit>");
		response.getWriter().write("<value>");
		response.getWriter().write(card.value);
		response.getWriter().write("</value>");
		response.getWriter().write("<type>");
		response.getWriter().write(card.type);
		response.getWriter().write("</type>");
  }
}