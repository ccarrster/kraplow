<%@page import="com.chriscarr.bang.*"%>

<html>
<head>
<title>Kraplow!</title>
</head>
<body>
<p>
<h1>Kraplow!</h1>
<% String players = request.getParameter("countPlayers");
String gameStarted = request.getParameter("gameStarted");
if(players == null){%>
<form>
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
	<form>
	<input type="hidden" name="countPlayers" value="<%=players%>">
	<input type="hidden" name="gameStarted" value="true">
	<input type="submit">
	</form>
	<%
} else {
	JSPUserInterface userInterface = (JSPUserInterface)WebInit.userInterface;
	GameState gameState = userInterface.getGameState();
	out.println("deck size: " + gameState.getDeckSize());
	out.println("is game over: " + gameState.isGameOver());
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
		userInterface.responses.add(previousResult);
		try{
			Thread.sleep(100);
		}catch(Exception e){
			//ignore
		}
	} else if(request.getParameter("twoForLife") != null){
		//Hack no result checkboxes checked, no result param sent.
		userInterface.responses.add("");
		try{
			Thread.sleep(100);
		}catch(Exception e){
			//ignore
		}
	}
	
	if(!userInterface.messages.isEmpty()){
		String message = userInterface.messages.remove(0);
		if(message.contains("-")){
			String[] splitMessage = message.split("-");
			String name = splitMessage[0];
			String commandData = splitMessage[1];
			out.println("Name: " + name);
			int commandIndex = commandData.indexOf(" ");
			if(commandIndex != -1){
				String command = commandData.substring(0, commandIndex);
				out.println("Command: " + command);				
				if(command.equals("respondBang")){
					String data = commandData.substring(commandIndex + 1);
					out.println("You have: " + data);
					%>					
					<form>
					<input type="hidden" name="countPlayers" value="<%=players%>">
					<input type="hidden" name="gameStarted" value="true">
					<input type="hidden" name="result" value="true">
					<input type="submit" value="true">
					</form>
					<form>
					<input type="hidden" name="countPlayers" value="<%=players%>">
					<input type="hidden" name="gameStarted" value="true">
					<input type="hidden" name="result" value="false">
					<input type="submit" value="false">
					</form>
					<%
				} else if(command.equals("respondBeer")){
					String data = commandData.substring(commandIndex + 1);
					out.println("You have: " + data);
					%>
					<form>
					<input type="hidden" name="countPlayers" value="<%=players%>">
					<input type="hidden" name="gameStarted" value="true">
					<input type="hidden" name="result" value="true">
					<input type="submit" value="true">
					</form>
					<form>
					<input type="hidden" name="countPlayers" value="<%=players%>">
					<input type="hidden" name="gameStarted" value="true">
					<input type="hidden" name="result" value="false">
					<input type="submit" value="false">
					</form>
					<%
				} else if(command.equals("respondMiss")){
					String data = commandData.substring(commandIndex + 1);
					String[] splitData = data.split(" ");
					out.println("You have: " + splitData[0] + " req:" + splitData[1]);
					%>
					<form>
					<input type="hidden" name="countPlayers" value="<%=players%>">
					<input type="hidden" name="gameStarted" value="true">
					<input type="hidden" name="result" value="true">
					<input type="submit" value="true">
					</form>
					<form>
					<input type="hidden" name="countPlayers" value="<%=players%>">
					<input type="hidden" name="gameStarted" value="true">
					<input type="hidden" name="result" value="false">
					<input type="submit" value="false">
					</form>
					<%
				} else if(command.equals("respondBangMiss")){
					String data = commandData.substring(commandIndex + 1);
					String[] splitData = data.split(" ");
					out.println("You have bangs: " + splitData[0] + " misses:" + splitData[1] + " required:" + splitData[2]);
					%>
					<form>
					<input type="hidden" name="countPlayers" value="<%=players%>">
					<input type="hidden" name="gameStarted" value="true">
					<input type="hidden" name="result" value="0">
					<input type="submit" value="Bang">
					</form>
					<form>
					<input type="hidden" name="countPlayers" value="<%=players%>">
					<input type="hidden" name="gameStarted" value="true">
					<input type="hidden" name="result" value="1">
					<input type="submit" value="Miss">
					</form>
					<% 
					if(splitData[2].equals("2")){
					%>
						<form>
						<input type="hidden" name="countPlayers" value="<%=players%>">
						<input type="hidden" name="gameStarted" value="true">
						<input type="hidden" name="result" value="3">
						<input type="submit" value="One each">
						</form>
					<%
					}
					%>
					<form>
					<input type="hidden" name="countPlayers" value="<%=players%>">
					<input type="hidden" name="gameStarted" value="true">
					<input type="hidden" name="result" value="2">
					<input type="submit" value="None">
					</form>
					<%	
				} else if(command.equals("chooseTwoDiscardForLife")){
					String[] splitData;
					String data = commandData.substring(commandIndex + 1);
					splitData = data.split(", ");
					%>
					<form>
					<input type="hidden" name="countPlayers" value="<%=players%>">
					<input type="hidden" name="gameStarted" value="true">
					<input type="hidden" name="twoForLife" value="true">
					<%
					for(int i = 0; i < splitData.length; i++){
						%>
						<input type="checkbox" name="result" value="<%=i%>"><%=splitData[i]%><br/>					
						<%
					}
					%>
					<input type="submit">
					</form>
					<%
							
				}else {
					String[] splitData;
					String data = commandData.substring(commandIndex + 1);
					splitData = data.split(", ");
					out.println("Data: ");
					if(command.equals("askOthersCard")){
						if(splitData[0].equals("true")){
						%>
						<form>
						<input type="hidden" name="countPlayers" value="<%=players%>">
						<input type="hidden" name="gameStarted" value="true">
						<input type="hidden" name="result" value="-1">
						<input type="submit" value="Hand">
						</form>
						<%
						}
						if(splitData[1].equals("true")){
						%>
						<form>
						<input type="hidden" name="countPlayers" value="<%=players%>">
						<input type="hidden" name="gameStarted" value="true">
						<input type="hidden" name="result" value="-2">
						<input type="submit" value="Gun">
						</form>
						<%
						}
						String[] tempSplitData = new String[splitData.length - 2];
						System.arraycopy(splitData, 2, tempSplitData, 0, splitData.length - 2);
						splitData = tempSplitData;
					} else if(command.equals("askPlay")){
						%>
						<form>
						<input type="hidden" name="countPlayers" value="<%=players%>">
						<input type="hidden" name="gameStarted" value="true">
						<input type="hidden" name="result" value="-1">
						<input type="submit" value="Done Playing">
						</form>
						<%
					}					
					for(int i = 0; i < splitData.length; i++){
						String targets = "";
						Boolean canPlay = true;
						if(command.equals("askPlay")){
							String[] canPlayTargets = splitData[i].split("@");							
							splitData[i] = canPlayTargets[0];							
							if(canPlayTargets.length == 3){
								if("false".equals(canPlayTargets[1])){
									canPlay = false;
								} else {
									targets = canPlayTargets[2];
								}
							}
						}
						if(!"".equals(splitData[i].trim())){
							%>
							<form>
							<input type="hidden" name="countPlayers" value="<%=players%>">
							<input type="hidden" name="gameStarted" value="true">
							<input type="hidden" name="result" value="<%=i%>">
							<input type="submit" value="<%=splitData[i]%>" <% if(!canPlay){out.print("disabled='true'");}%>><%= targets %>
							</form>
							<%
						}
					}
				}				
			} else {
				out.println("Command: " + commandData);
				if(commandData.equals("chooseFromPlayer") || commandData.equals("chooseDiscard")){
					%>
					<form>
					<input type="hidden" name="countPlayers" value="<%=players%>">
					<input type="hidden" name="gameStarted" value="true">
					<input type="hidden" name="result" value="true">
					<input type="submit" value="true">
					</form>
					<form>
					<input type="hidden" name="countPlayers" value="<%=players%>">
					<input type="hidden" name="gameStarted" value="true">
					<input type="hidden" name="result" value="false">
					<input type="submit" value="false">
					</form>
					<%
				}
			}
			
			
			
		} else {
			out.println("info: " + message);
		}
	}
}
%>

</p>


</body>
</html>