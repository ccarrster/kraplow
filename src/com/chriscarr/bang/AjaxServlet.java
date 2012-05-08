package com.chriscarr.bang;

import java.io.IOException;
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
    	JSPUserInterface userInterface = (JSPUserInterface)WebInit.userInterface;
    	if(userInterface != null){
	    	GameState gameState = userInterface.getGameState();
	    	response.getWriter().write("<decksize>" + gameState.getDeckSize() + "</decksize>");
    	} else {
    		response.getWriter().write("<decksize>-1<decksize>");
    	}
      }
  }
}