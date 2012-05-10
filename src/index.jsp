<%@page import="com.chriscarr.bang.*"%>
<%@page import="java.util.*"%>

<html>
<head>
<title>Kraplow!</title>
</head>
<body onload="timedCount();">
<p>
<h1>Kraplow!</h1>
<% 
String user = request.getParameter("user");
%>
<div id="gameSetup"></div>
<div id="gameState"></div>
<%

String webStart = null;
if(webStart != null){	
	JSPUserInterface userInterface = (JSPUserInterface)WebInit.userInterface;
	%><div style="float:left;"><%

	List<String> messages = ((WebGameUserInterface)userInterface).getMessages(user);
	
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
		((WebGameUserInterface)userInterface).addResponse(user, previousResult);		
		messages.remove(0);
	} else if(request.getParameter("twoForLife") != null){
		//Hack no result checkboxes checked, no result param sent.
		((WebGameUserInterface)userInterface).addResponse(user, "");
		messages.remove(0);
	} else if(request.getParameter("twoMisses") != null){
		//Hack no result checkboxes checked, no result param sent.
		((WebGameUserInterface)userInterface).addResponse(user, "");
		messages.remove(0);
	}
}
%>
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
		if(deckSize == undefined){
			return;
		} else {
			started = true;
		}
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
   
   function formatMessage(message){
   	var result = "";
		if(message.indexOf("-") != -1){
			var splitMessage = message.split("-");
			var name = splitMessage[0];
			var commandData = splitMessage[1];
			
			result += '<img src="' + getImageForPlayer(name) + '" width="30" alt="Player" title="' + name + '">';
			result += getImageForRole(role, goal);
			
			var commandIndex = commandData.indexOf(" ");
			if(commandIndex != -1){
				var command = commandData.substring(0, commandIndex);
				result += command;
				var data = commandData.substring(commandIndex + 1);
				var splitData = data.split(", ");				
								
				if(command.indexOf("chooseTwoDiscardForLife") != -1){
					//var selected = new Array();
					var selected = "not implemented";
					for(var i = 0; i < splitData.length; i++){
						var image = getImageForCard(splitData[i]);
						//TODO onclick populate array and then format array on submit to comma separated
						result += '<img src="' + image + '" width="30" alt="Discard for life card" title="' + splitData[i] + '">';						
					}
					result += "<div onclick='sendResponse(selected);'>Discard Selected</div>";
				} else if(command.indexOf("respondTwoMiss") != -1){
					//var selected = new Array();
					var selected = "not implemented";
					for(var i = 0; i < splitData.length; i++){
						var nameCanPlaySplit = splitData[i].split("@");
						var cardName = nameCanPlaySplit[0];
						var canPlay = nameCanPlaySplit[1];
						var disabled = "";
						if(canPlay.indexOf("false") != -1){
							disabled = "disabled='true'";
						}
						var image = getImageForCard(cardName);
						//TODO onclick populate array and then format array on submit to comma separated						
						result += '<img src="' + image + '" width="30" alt="Respond card" title="' + cardName + '">';
					}
					result += "<div onclick='sendResponse(selected);'>Misses Selected</div>";
				} else {
					if(command.indexOf("askOthersCard") != -1){
						if(splitData[0].indexOf("true") != -1){						
							result += "<img src='card.png' width='30' alt='Players hand' onclick='sendResponse(\"hand\");'>";
						}
						if(splitData[1].indexOf("true") != -1){						
							result += "<img src='cardface.png' width='30' alt='Players gun' onclick='sendResponse(\"gun\");'>";
						}
						splitData = splitData.slice(2, splitData.length);
					} else if(command.indexOf("askPlay") != -1 || command.indexOf("respondBeer") != -1 || command.indexOf("respondBang") != -1 || command.indexOf("respondMiss") != -1){
						result += "<div onclick='sendResponse(\"-1\");'>Done Playing</div>";
					}
					for(var i = 0; i < splitData.length; i++){						
						var targets = "";
						var canPlay = true;
						if(command.indexOf("askPlay") != -1 || command.indexOf("respondBeer") != -1 || command.indexOf("respondBang") != -1 || command.indexOf("respondMiss") != -1){
							var canPlayTargets = splitData[i].split("@");							
							splitData[i] = canPlayTargets[0];							
							if(canPlayTargets.length == 3){
								if(canPlayTargets[1].indexOf("false") != -1){
									canPlay = false;
								} else {
									targets = canPlayTargets[2];
								}
							} else if(canPlayTargets.length == 2){
								if(canPlayTargets[1].indexOf("false") != -1){
									canPlay = false;
								}
							}							
						}
						if("" != splitData[i].trim()){
							var image;
							if(command.indexOf("askPlayer") != -1){
								image = getImageForPlayer(splitData[i]);
							} else {
								var cardName = splitData[i];
								image = getImageForCard(cardName);
							}
							if(canPlay){
								result += '<img src="' + image + '" width="30" alt="Action" title="' + splitData[i] + ' ' + targets + '" onclick="onclick=\'sendResponse(i);\'">';
							} else {
								result += '<img src="' + image + '" width="30" alt="Action" title="' + splitData[i] + ' ' + targets + '" style="opacity:0.4;">';
							}
						}
					}
				}

			} else {
				result += commandData;
				result += "<div onclick='sendResponse(\"true\");'>true</div>";
				result += "<div onclick='sendResponse(\"false\");'>false</div>";
			}
		} else {
			result += 'info: ' + message;			
			//messages.remove(0);
		}
	
   	
   	return result;
   }
         
   function getGameState(servletUrl, responseHandler) {
   	sendXMLHttp(servletUrl + "?messageType=GETGAMESTATE", responseHandler);
   }
   
   function parseJoin(responseXML){
   	var userNode = responseXML.getElementsByTagName("user")[0];
   	if(userNode != null){
   		user = userNode.childNodes[0].nodeValue;
   	}
   	setup();
   }
   
   function join(servletUrl, responseHandler) {
      	sendXMLHttp(servletUrl + "?messageType=JOIN", responseHandler);
   }
   
   function parseLeave(responseXML){
   	var okNode = responseXML.getElementsByTagName("ok")[0];
      	if(okNode != null){      		
      		user = null;
   	}
   	setup();
   }
   
   function leave(servletUrl, responseHandler, user) {
      	sendXMLHttp(servletUrl + "?messageType=LEAVE&user=" + user, responseHandler);
   }
   
   function parseCountPlayers(responseXML){
   	playerCount = responseXML.getElementsByTagName("playercount")[0].childNodes[0].nodeValue;   
   }
   
   function countPlayers(servletUrl, responseHandler) {
      	sendXMLHttp(servletUrl + "?messageType=COUNTPLAYERS", responseHandler);
   }
   
   function parseCanStart(responseXML){
      var yesNode = responseXML.getElementsByTagName("yes")[0];
	if(yesNode != null){      		
		startable = true;
   	} else {
   		startable = false;
   	}
   }
      
   function canStart(servletUrl, responseHandler) {
      sendXMLHttp(servletUrl + "?messageType=CANSTART", responseHandler);
   }
   
   function parseStart(responseXML){
   	
   }
   
   function start(servletUrl, responseHandler) {
      	sendXMLHttp(servletUrl + "?messageType=START", responseHandler);
   }
   
   function parseGetMessage(responseXML){
      	var messageNode = responseXML.getElementsByTagName("message")[0];
      	if(messageNode != null){
      		message = messageNode.childNodes[0].nodeValue;      		
      	}
   }
   
   function getMessage(servletUrl, responseHandler, user) {
         sendXMLHttp(servletUrl + "?messageType=GETMESSAGE&user=" + user, responseHandler);
   }
   
   function sendResponse(response){
   	alert("sent " + response);
   }
   
   function parseGetPlayerInfo(responseXML){
	var nameNode = responseXML.getElementsByTagName("name")[0];
	if(nameNode != null){
		playerName = nameNode.childNodes[0].nodeValue;      		
	}
	var roleNode = responseXML.getElementsByTagName("role")[0];
	if(roleNode != null){
		role = roleNode.childNodes[0].nodeValue;      		
	}
	var goalNode = responseXML.getElementsByTagName("goal")[0];
	if(goalNode != null){
		goal = goalNode.childNodes[0].nodeValue;      		
	}
   }

   function getPlayerInfo(servletUrl, responseHandler, user) {
        sendXMLHttp(servletUrl + "?messageType=GETPLAYERINFO&user=" + user, responseHandler);
   }
   
   var user = null;
   var playerCount = null;
   var startable = null;
   var started = false;
   var message = null;
   
   var playerName = null;
   var role = null;
   var goal = null;
   
   function setup(){
   	var gameSetupDiv = document.getElementById('gameSetup');
   	var result = "";
   	if(!started){   	
		if(user == null){
			result += '<div onclick="join(getServletUrl(), parseJoin);">Join</div>';
		} else {
			result += user;
			result += '<div onclick="leave(getServletUrl(), parseLeave, user);">Leave</div>';
		}
		if(playerCount != null){
			result += '<div>Player count: ' + playerCount + '</div>';
		}   	
		if(startable != null && startable == true){
			result += '<div onclick="start(getServletUrl(), parseStart);">Start</div>';
		}		
		countPlayers(getServletUrl(), parseCountPlayers);
		canStart(getServletUrl(), parseCanStart);
   	} else {
   		if(playerName == null){
   			getPlayerInfo(getServletUrl(), parseGetPlayerInfo, user);
   		} else {
			if(message != null){				
				result += formatMessage(message);
			}
		}
   		getMessage(getServletUrl(), parseGetMessage, user);
   	}
	gameSetupDiv.innerHTML = result;
   }
   
   function timedCount()
   {
        getGameState(getServletUrl(), parseGetGameState);
	var t=setTimeout("timedCount()",10000);
	setup();	
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
   
   function getImageForRole(role, goal){
	if(role =="Sheriff"){
		return '<img src="sheriff.png" width="10" alt="Role sheriff" title="' + goal + '">';
	} else if(role == "Outlaw"){
		return '<img src="outlaw.png" width="15" alt="Role outlaw" title="' + goal + '">';
	} else if(role == "Deputy"){
		return '<img src="deputy.png" width="10" alt="Role deputy" title="' + goal + '">';
	} else if(role == "Renegade"){
		return '<img src="renegade.png" width="20" alt="Role renegade" title="' + goal + '">';
	}
   }
   
</script>
</body>
</html>