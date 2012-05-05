<%@page import="com.chriscarr.bang.*"%>
<%@page import="java.util.*"%>

<html>
<head>
<title>Kraplow!</title>
</head>
<body>
<p>
<h1>Kraplow!</h1>
<% String players = request.getParameter("countPlayers");
String gameStarted = request.getParameter("gameStarted");
String webStart = request.getParameter("webStart");
String user = request.getParameter("user");
if(webStart != null){
	WebGame.start();
	%>
	<form method="POST">
	<input type="hidden" name="user" value="<%=user%>">
	<input type="hidden" name="countPlayers" value="<%=players%>">
	<input type="hidden" name="gameStarted" value="true">
	<input type="submit">
	</form>
	<%
} else if(players == null){%>
<form method="POST">
<select id="countPlayers" name="countPlayers">
<option value="4">4</option>
<option value="5">5</option>
<option value="6">6</option>
<option value="7">7</option>
</select>
<input type="submit">
</form>
<%
} else if(gameStarted == null) {
%>
Players <%= players %>
<%
	//This needs to be on its own thread so waiting is ok
	WebInit webInit = new WebInit();
	JSPUserInterface x = new JSPUserInterface();
	webInit.setup(Integer.parseInt(players), x, x);
	%>
	<form method="POST">
	<input type="hidden" name="countPlayers" value="<%=players%>">
	<input type="hidden" name="gameStarted" value="true">
	<input type="submit">
	</form>
	<%
} else {
	JSPUserInterface userInterface = (JSPUserInterface)WebInit.userInterface;
	GameState gameState = userInterface.getGameState();	
	if(gameState.getDeckSize() != 0){
	%>
	<img src="card.png" width="30" alt="Draw pile" title="<%=gameState.getDeckSize()%>">
	<%
	}
	GameStateCard discardTopCard = gameState.discardTopCard();
	if(discardTopCard != null){
		%>
		<img src="cardface.png" width="30" alt="Discard pile" title="<%=discardTopCard.name%>">
		<%
	}
	if(gameState.isGameOver()){
		out.println("Game over");	
	}
	List<GameStatePlayer> gameStatePlayers = gameState.getPlayers();
	for(GameStatePlayer player : gameStatePlayers){
		if(gameState.getCurrentName().equals(player.name)){
			%><div style="border: 2px solid #0F0"><%	
		} else {
			%><div style="border: 2px solid #000"><%
		}
		%>
		<img src="player.png" width="30" alt="Player" title="<%=player.name%> <%= player.specialAbility %>">
		<%
		if(player.isSheriff){
			%>
			<img src="sheriff.png" width="30" alt="Role Sheriff">
			<%
		}
		%><div><%
		for(int i = 0; i < player.health; i++){
			%>
			<img src="bullet.png" width="30" alt="Health token">
			<%
		}		
		for(int i = player.health; i < player.maxHealth; i++){
			%>
			<img src="emptybullet.png" width="30" alt="Empty health token">
			<%
		}
		%></div><div><%
		for(int i = 0; i < player.handSize; i++){
			%>
			<img src="card.png" width="30" alt="Hand card">
			<%
		}
		%></div><div><%		
		GameStateCard gun = player.gun;
		if(gun != null){
			%>
			<img src="cardface.png" width="30" alt="In play gun" title="<%=gun.name%>">
			<%
		}
		List<GameStateCard> inPlay = player.inPlay;
		for(GameStateCard inPlayCard : inPlay){
			%>
			<img src="cardface.png" width="30" alit="In play card" title="<%=inPlayCard.name%>">
			<%
		}
		%></div></div><%
	}
	

	List<String> messages;
	if(user != null){
		messages = ((WebGameUserInterface)userInterface).getMessages(user);
	} else {
		messages = userInterface.messages;
	}
	
	String previousResult = request.getParameter("result");	
	if(previousResult != null){		
		String[] previousResults = request.getParameterValues("result");
		String tempResult = "";
		if(previousResults.length > 1){
			for(int i = 0; i < previousResults.length; i++){
				tempResult += previousResults[i] + ",";
			}
			previousResult = tempResult;
		}
		if(user == null){
			userInterface.responses.add(previousResult);
		} else {
			((WebGameUserInterface)userInterface).addResponse(user, previousResult);
		}
		messages.remove(0);
	} else if(request.getParameter("twoForLife") != null){
		//Hack no result checkboxes checked, no result param sent.
		if(user == null){
			userInterface.responses.add("");
		} else {
			((WebGameUserInterface)userInterface).addResponse(user, "");
		}
		messages.remove(0);
	} else if(request.getParameter("twoMisses") != null){
		//Hack no result checkboxes checked, no result param sent.
		if(user == null){
			userInterface.responses.add("");
		} else {
			((WebGameUserInterface)userInterface).addResponse(user, "");
		}
		messages.remove(0);
	}
	
	if(!messages.isEmpty()){
		String message = messages.get(0);
		if(message.contains("-")){
			String[] splitMessage = message.split("-");
			String name = splitMessage[0];
			String commandData = splitMessage[1];
			%>
			<img src="player.png" width="30" alt="Player" title="<%=name%>">
			<%
			String role = userInterface.getRoleForName(name);
			if(role.equals("Sheriff")){
				%>
				<img src="sheriff.png" width="30" alt="Role sheriff" title="<%=userInterface.getGoalForName(name)%>">
				<%				
			} else if(role.equals("Outlaw")){
				%>
				<img src="outlaw.png" width="30" alt="Role outlaw" title="<%=userInterface.getGoalForName(name)%>">
				<%				
			} else if(role.equals("Deputy")){
				%>
				<img src="deputy.png" width="30" alt="Role deputy" title="<%=userInterface.getGoalForName(name)%>">
				<%
			} else if(role.equals("Renegade")){
				%>
				<img src="renegade.png" width="30" alt="Role renegade" title="<%=userInterface.getGoalForName(name)%>">
				<%
			}			
			int commandIndex = commandData.indexOf(" ");
			if(commandIndex != -1){
				String command = commandData.substring(0, commandIndex);
				out.println(command);
				if(command.equals("chooseTwoDiscardForLife")){
					String[] splitData;
					String data = commandData.substring(commandIndex + 1);
					splitData = data.split(", ");
					%>
					<form method="POST">
					<input type="hidden" name="countPlayers" value="<%=players%>">
					<input type="hidden" name="gameStarted" value="true">
					<input type="hidden" name="user" value="<%=user%>">
					<input type="hidden" name="twoForLife" value="true">
					<%
					for(int i = 0; i < splitData.length; i++){
						%>
						<input type="checkbox" name="result" value="<%=i%>">
						<img src="cardface.png" width="30" alt="Discard for life card" title="<%=splitData[i]%>">
						<br/>					
						<%
					}
					%>
					<input type="submit">
					</form>
					<%
							
				} else if(command.equals("respondTwoMiss")){
					String[] splitData;
					String data = commandData.substring(commandIndex + 1);
					splitData = data.split(", ");
					%>
					<form method="POST">
					<input type="hidden" name="countPlayers" value="<%=players%>">
					<input type="hidden" name="gameStarted" value="true">
					<input type="hidden" name="user" value="<%=user%>">
					<input type="hidden" name="twoMisses" value="true">
					<%
					for(int i = 0; i < splitData.length; i++){
						String[] nameCanPlaySplit = splitData[i].split("@");
						String cardName = nameCanPlaySplit[0];
						String canPlay = nameCanPlaySplit[1];
						String disabled = "";
						if("false".equals(canPlay)){
							disabled = "disabled='true'";
						}
						%>
						<input type="checkbox" name="result" value="<%=i%>" <%= disabled %>>
						<img src="cardface.png" width="30" alt="Respond card" title="<%=cardName%>">
						<br/>					
						<%
					}
					%>
					<input type="submit">
					</form>
					<%
							
				} else {
					String[] splitData;
					String data = commandData.substring(commandIndex + 1);
					splitData = data.split(", ");
					if(command.equals("askOthersCard")){
						if(splitData[0].equals("true")){
						%>
						<form method="POST">
						<input type="hidden" name="countPlayers" value="<%=players%>">
						<input type="hidden" name="gameStarted" value="true">
						<input type="hidden" name="result" value="-1">
						<input type="hidden" name="user" value="<%=user%>">
						<input type="submit" value="Hand"><img src="card.png" width="30" alt="Players hand">
						</form>
						<%
						}
						if(splitData[1].equals("true")){
						%>
						<form method="POST">
						<input type="hidden" name="countPlayers" value="<%=players%>">
						<input type="hidden" name="gameStarted" value="true">
						<input type="hidden" name="result" value="-2">
						<input type="hidden" name="user" value="<%=user%>">
						<input type="submit" value="Gun"><img src="cardface.png" width="30" alt="Players gun">
						</form>
						<%
						}
						String[] tempSplitData = new String[splitData.length - 2];
						System.arraycopy(splitData, 2, tempSplitData, 0, splitData.length - 2);
						splitData = tempSplitData;
					} else if(command.equals("askPlay") || command.equals("respondBeer") || command.equals("respondBang") || command.equals("respondMiss")){
						%>
						<form method="POST">
						<input type="hidden" name="countPlayers" value="<%=players%>">
						<input type="hidden" name="gameStarted" value="true">
						<input type="hidden" name="result" value="-1">
						<input type="hidden" name="user" value="<%=user%>">
						<input type="submit" value="Done Playing">
						</form>
						<%
					}					
					for(int i = 0; i < splitData.length; i++){
						String targets = "";
						Boolean canPlay = true;
						if(command.equals("askPlay") || command.equals("respondBeer") || command.equals("respondBang") || command.equals("respondMiss")){
							String[] canPlayTargets = splitData[i].split("@");							
							splitData[i] = canPlayTargets[0];							
							if(canPlayTargets.length == 3){
								if("false".equals(canPlayTargets[1])){
									canPlay = false;
								} else {
									targets = canPlayTargets[2];
								}
							} else if(canPlayTargets.length == 2){
								if("false".equals(canPlayTargets[1])){
									canPlay = false;
								}
							}
						}
						if(!"".equals(splitData[i].trim())){
							String image = "cardface.png";
							if(command.equals("askPlayer")){
								image = "player.png";
							}
							%>
							<form method="POST">
							<input type="hidden" name="countPlayers" value="<%=players%>">
							<input type="hidden" name="gameStarted" value="true">
							<input type="hidden" name="result" value="<%=i%>">
							<input type="hidden" name="user" value="<%=user%>">
							<input type="submit" value="<%=splitData[i]%>" <% if(!canPlay){out.print("disabled='true'");}%>>							
							<img src="<%=image%>" width="30" alt="Action" title="<%=splitData[i]%> <%= targets %>">
							</form>
							<%
						}
					}
				}				
			} else {
				out.println("Command: " + commandData);
				if(commandData.equals("chooseFromPlayer") || commandData.equals("chooseDiscard")){
					%>
					<form method="POST">
					<input type="hidden" name="countPlayers" value="<%=players%>">
					<input type="hidden" name="gameStarted" value="true">
					<input type="hidden" name="user" value="<%=user%>">
					<input type="hidden" name="result" value="true">
					<input type="submit" value="true">
					</form>
					<form method="POST">
					<input type="hidden" name="countPlayers" value="<%=players%>">
					<input type="hidden" name="gameStarted" value="true">
					<input type="hidden" name="result" value="false">
					<input type="hidden" name="user" value="<%=user%>">
					<input type="submit" value="false">
					</form>
					<%
				}
			}
			
			
			
		} else {
			out.println("info: " + message);
			messages.remove(0);
		}
	}
}
%>
<form method="POST">
<input type="hidden" name="countPlayers" value="<%=players%>">
<input type="hidden" name="gameStarted" value="true">
<input type="hidden" name="user" value="<%=user%>">
<input type="submit" value="refresh" id="refresh">
</form>
</p>


</body>
</html>