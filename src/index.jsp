<%@page import="com.chriscarr.bang.*"%>
<%@page import="java.util.*"%>

<html>
<head>
<title>Kraplow!</title>
</head>
<body onload="timedCount();">
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
	%>
	<div id="gameState"></div>
	<%
	JSPUserInterface userInterface = (JSPUserInterface)WebInit.userInterface;
	%><div style="float:left;"><%

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
			%><img src="<%=getImageForPlayer(name)%>" width="30" alt="Player" title="<%=name%>"><%
			String role = userInterface.getRoleForName(name);
			if(role.equals("Sheriff")){
				%><img src="sheriff.png" width="10" alt="Role sheriff" title="<%=userInterface.getGoalForName(name)%>" style="position:relative; left:-14px; bottom:5px;"><%				
			} else if(role.equals("Outlaw")){
				%><img src="outlaw.png" width="15" alt="Role outlaw" title="<%=userInterface.getGoalForName(name)%>" style="position:relative; left:-21px; bottom:13px;"><%				
			} else if(role.equals("Deputy")){
				%><img src="deputy.png" width="10" alt="Role deputy" title="<%=userInterface.getGoalForName(name)%>" style="position:relative; left:-14px; bottom:5px;"><%
			} else if(role.equals("Renegade")){
				%><img src="renegade.png" width="20" alt="Role renegade" title="<%=userInterface.getGoalForName(name)%>" style="position:relative; left:-24px; bottom:20px;"><%
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
						String image = getImageForCard(splitData[i]);
						%>
						<input type="checkbox" name="result" value="<%=i%>">
						<img src="<%=image%>" width="30" alt="Discard for life card" title="<%=splitData[i]%>">
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
						<form method="POST" name="hand">
						<input type="hidden" name="countPlayers" value="<%=players%>">
						<input type="hidden" name="gameStarted" value="true">
						<input type="hidden" name="result" value="-1">
						<input type="hidden" name="user" value="<%=user%>">
						<img src="card.png" width="30" alt="Players hand" onclick="document.hand.submit();">
						</form>
						<%
						}
						if(splitData[1].equals("true")){
						%>
						<form method="POST" name="gun">
						<input type="hidden" name="countPlayers" value="<%=players%>">
						<input type="hidden" name="gameStarted" value="true">
						<input type="hidden" name="result" value="-2">
						<input type="hidden" name="user" value="<%=user%>">
						<img src="cardface.png" width="30" alt="Players gun" onclick="document.gun.submit();">
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
							String image;
							if(command.equals("askPlayer")){
								image = getImageForPlayer(splitData[i]);
							} else {
								String cardName = splitData[i];
								image = getImageForCard(cardName);
							}
							%>
							<form method="POST" name="play<%=i%>">
							<input type="hidden" name="countPlayers" value="<%=players%>">
							<input type="hidden" name="gameStarted" value="true">
							<input type="hidden" name="result" value="<%=i%>">
							<input type="hidden" name="user" value="<%=user%>">
							<%
							if(canPlay){
								%><img src="<%=image%>" width="30" alt="Action" title="<%=splitData[i]%> <%= targets %>" onclick="document.play<%=i%>.submit();"><%
							} else {
								%><img src="<%=image%>" width="30" alt="Action" title="<%=splitData[i]%> <%= targets %>" style="opacity:0.4;"><%
							}
							%>
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
<%!
public String getImageForCard(String cardName){
	if(cardName.contains("Bang!")){
		return "bang.png";
	} else if(cardName.contains("Missed!")){
		return "missed.png";
	} else if(cardName.contains("Beer")){
		return "beer.png";
	} else if(cardName.contains("Barrel")){
		return "barrel.png";
	} else if(cardName.contains("Appaloosa")){
		return "appaloosa.png";
	} else if(cardName.contains("Mustang")){
		return "mustang.png";
	} else if(cardName.contains("Schofield")){
		return "schofield.png";
	} else if(cardName.contains("Remington")){
		return "remington.png";
	} else if(cardName.contains("Winchester")){
		return "winchester.png";
	} else if(cardName.contains("Volcanic")){
		return "volcanic.png";
	} else if(cardName.contains("Rev. Carbine")){
		return "carbine.png";
	} else if(cardName.contains("Jail")){
		return "jail.png";
	} else if(cardName.contains("Dynamite")){
		return "dynamite.png";
	} else if(cardName.contains("Gatling")){
		return "gatling.png";
	} else if(cardName.contains("Saloon")){
		return "saloon.png";
	} else if(cardName.contains("Panic!")){
		return "panic.png";
	} else if(cardName.contains("General Store")){
		return "generalstore.png";
	} else if(cardName.contains("Indians!")){
		return "indians.png";
	} else if(cardName.contains("Duel")){
		return "duel.png";
	} else if(cardName.contains("Stagecoach")){
		return "stagecoach.png";
	} else if(cardName.contains("Wells Fargo")){
		return "wellsfargo.png";
	} else if(cardName.contains("Cat Balou")){
		return "catbalou.png";
	} else {
		return "cardface.png";
	}
}

public String getImageForPlayer(String playerName){
	if(playerName.equals("Bart Cassidy")){
		return "bartcassidy.png";
	} else if(playerName.equals("Black Jack")){
		return "blackjack.png";
	} else if(playerName.equals("Calamity Janet")){
		return "calamityjanet.png";
	} else if(playerName.equals("El Gringo")){
		return "elgringo.png";
	} else if(playerName.equals("Jesse Jones")){
		return "jessejones.png";
	} else if(playerName.equals("Jourdonnais")){
		return "jourdonnais.png";
	} else if(playerName.equals("Kit Carlson")){
		return "kitcarlson.png";
	} else if(playerName.equals("Lucky Duke")){
		return "luckyduke.png";
	} else if(playerName.equals("Paul Regret")){
		return "paulregret.png";
	} else if(playerName.equals("Pedro Ramirez")){
		return "pedroramierz.png";
	} else if(playerName.equals("Rose Doolan")){
		return "rosedoolan.png";
	} else if(playerName.equals("Sid Ketchum")){
		return "sidketchum.png";
	} else if(playerName.equals("Slab the Killer")){
		return "slabthekiller.png";
	} else if(playerName.equals("Suzy Lafayette")){
		return "suzylafayette.png";
	} else if(playerName.equals("Vulture Sam")){
		return "vulturesam.png";
	} else if(playerName.equals("Willy the Kid")){
		return "willythekid.png";
	} else {
		return "player.png";
	}
}
%>
<form method="POST">
<input type="hidden" name="countPlayers" value="<%=players%>">
<input type="hidden" name="gameStarted" value="true">
<input type="hidden" name="user" value="<%=user%>">
<input type="submit" value="refresh" id="refresh">
</form>
</div>
</p>
<script language="javascript">
   function getServletUrl(){
       return "/bang/chat";
   }
   
   function initRequest() {
       if (window.XMLHttpRequest) {
	   return new XMLHttpRequest();
       } else if (window.ActiveXObject) {
	   isIE = true;
	   return new ActiveXObject("Microsoft.XMLHTTP");
       }
   }

   function sendXMLHttp(url, responseHandler){
	var req = initRequest();
	req.onreadystatechange = function() {
	   if (req.readyState == 4) {
		   if (req.status == 200) {
			   responseHandler(req.responseXML);
		   } else if (req.status == 204){
			   alert('error');
		   }
	   }
	};
	req.open("GET", url, true);
	req.send(null);
   }
   
   function parseGetGameState(responseXML) {
	var gameStateDiv = document.getElementById('gameState');
	if(gameStateDiv != null){
		var result = "";
		var deckSize = responseXML.getElementsByTagName("decksize")[0];
		var size = deckSize.childNodes[0].nodeValue;
		if(size > 0){
			result += '<img id="draw_pile" src="card.png" width="30" alt="Draw pile" title="' + size + '">';
		}
		
		var discardCard = responseXML.getElementsByTagName("discardtopcard")[0];
		if(discardCard != null){
			var discardName = discardCard.getElementsByTagName("name")[0];
			var cardName = discardName.childNodes[0].nodeValue;
			result += '<img id="discard_pile" src="' + getImageForCard(cardName) + '" width="30" alt="Discard pile" title="' + cardName + '">';
		}
	
		var currentName = responseXML.getElementsByTagName("currentname")[0];
		var name = currentName.childNodes[0].nodeValue;
		result += 'Current Player: ' + name;
		
		var gameOverNode = responseXML.getElementsByTagName("gameover")[0];
		if(gameOverNode != null){
			result += 'Game Over';
		}
		
		var playerNodes = responseXML.getElementsByTagName("player");
		for(var playerIndex = 0; playerIndex < playerNodes.length; playerIndex++){
			result += '<div>';
			var playerName = playerNodes[playerIndex].getElementsByTagName("name")[0].childNodes[0].nodeValue;
			result += '<img src="' + getImageForPlayer(playerName) + '" width="30" alt="Player" title="' + playerName + '">';
			if(playerNodes[playerIndex].getElementsByTagName("issheriff")[0] != null){
				result += '<img src="sheriff.png" width="10" alt="Role Sheriff">';
			}
			var playerHealth = playerNodes[playerIndex].getElementsByTagName("health")[0].childNodes[0].nodeValue;
			var playerMaxHealth = playerNodes[playerIndex].getElementsByTagName("maxhealth")[0].childNodes[0].nodeValue;
			result += playerHealth + '/' + playerMaxHealth;
			for(var healthIndex = 0; healthIndex < playerHealth; healthIndex++){
				result += '<img src="vbullet.png" width="10" alt="Health token">';
			}
			for(var maxHealthIndex = healthIndex; maxHealthIndex < playerMaxHealth; maxHealthIndex++){
				result += '<img src="vemptybullet.png" width="10" alt="Health token">';
			}
			var handSize = playerNodes[playerIndex].getElementsByTagName("handsize")[0].childNodes[0].nodeValue;
			for(var handIndex = 0; handIndex < handSize; handIndex++){
				result += '<img src="card.png" width="30" alt="Hand card">'
			}
			var gunNode = playerNodes[playerIndex].getElementsByTagName("gun")[0];
			if(gunNode != null){
				var gunName = gunNode.getElementsByTagName("name")[0].childNodes[0].nodeValue;
				result += '<img src="' + getImageForCard(gunName) + '" width="30" alt="Gun" title="' + gunName + '">';
			}
			var inPlayNodes = playerNodes[playerIndex].getElementsByTagName("inplaycard");
			for(var inPlayIndex = 0; inPlayIndex < inPlayNodes.length; inPlayIndex++){
				var inPlayName = inPlayNodes[inPlayIndex].getElementsByTagName("name")[0].childNodes[0].nodeValue;
				result += '<img src="' + getImageForCard(inPlayName) + '" width="30" alt="In Play" title="' + inPlayName + '">';
			}
			result += '</div>';
		}
		gameStateDiv.innerHTML = result;		
	}
   }
   
   function getGameState(servletUrl, responseHandler) {
   	sendXMLHttp(servletUrl + "?messageType=GETGAMESTATE", responseHandler);
   }
   
   function timedCount()
   {
        getGameState(getServletUrl(), parseGetGameState);
	var t=setTimeout("timedCount()",10000);
   }
   
   function getImageForCard(cardName){
   	if(cardName == "Bang!"){
   		return "bang.png";
   	} else if(cardName == "Missed!"){
   		return "missed.png";
   	} else if(cardName == "Beer"){
   		return "beer.png";
   	} else if(cardName == "Barrel"){
   		return "barrel.png";
   	} else if(cardName == "Appaloosa"){
   		return "appaloosa.png";
   	} else if(cardName == "Mustang"){
   		return "mustang.png";
   	} else if(cardName == "Schofield"){
   		return "schofield.png";
   	} else if(cardName == "Remington"){
   		return "remington.png";
   	} else if(cardName == "Winchester"){
   		return "winchester.png";
   	} else if(cardName == "Volcanic"){
   		return "volcanic.png";
   	} else if(cardName == "Rev. Carbine"){
   		return "carbine.png";
   	} else if(cardName == "Jail"){
   		return "jail.png";
   	} else if(cardName == "Dynamite"){
   		return "dynamite.png";
   	} else if(cardName == "Gatling"){
   		return "gatling.png";
   	} else if(cardName == "Saloon"){
   		return "saloon.png";
   	} else if(cardName == "Panic!"){
   		return "panic.png";
   	} else if(cardName == "General Store"){
   		return "generalstore.png";
   	} else if(cardName == "Indians!"){
   		return "indians.png";
   	} else if(cardName == "Duel"){
   		return "duel.png";
   	} else if(cardName == "Stagecoach"){
   		return "stagecoach.png";
   	} else if(cardName == "Wells Fargo"){
   		return "wellsfargo.png";
   	} else if(cardName == "Cat Balou"){
   		return "catbalou.png";
   	} else {
   		return "cardface.png";
   	}
   }
   
   function getImageForPlayer(playerName){
   	if(playerName == "Bart Cassidy"){
   		return "bartcassidy.png";
   	} else if(playerName == "Black Jack"){
   		return "blackjack.png";
   	} else if(playerName == "Calamity Janet"){
   		return "calamityjanet.png";
   	} else if(playerName == "El Gringo"){
   		return "elgringo.png";
   	} else if(playerName == "Jesse Jones"){
   		return "jessejones.png";
   	} else if(playerName == "Jourdonnais"){
   		return "jourdonnais.png";
   	} else if(playerName == "Kit Carlson"){
   		return "kitcarlson.png";
   	} else if(playerName == "Lucky Duke"){
   		return "luckyduke.png";
   	} else if(playerName == "Paul Regret"){
   		return "paulregret.png";
   	} else if(playerName == "Pedro Ramirez"){
   		return "pedroramierz.png";
   	} else if(playerName == "Rose Doolan"){
   		return "rosedoolan.png";
   	} else if(playerName == "Sid Ketchum"){
   		return "sidketchum.png";
   	} else if(playerName == "Slab the Killer"){
   		return "slabthekiller.png";
   	} else if(playerName == "Suzy Lafayette"){
   		return "suzylafayette.png";
   	} else if(playerName == "Vulture Sam"){
   		return "vulturesam.png";
   	} else if(playerName == "Willy the Kid"){
   		return "willythekid.png";
   	} else {
   		return "player.png";
   	}
   }
   
   
</script>
</body>
</html>