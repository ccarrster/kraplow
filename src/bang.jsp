<%@page import="com.chriscarr.bang.*"%>
<%@page import="java.util.*"%>

<html>
<head>
<title>Kraplow!</title>
</head>
<body>
<%
String join = request.getParameter("join");
String start = request.getParameter("start");
String leave = request.getParameter("leave");
if(join != null){
	String user = WebGame.join();
	if(user != null){	
		out.print(user);
		out.print(" - ");
		int countPlayers = WebGame.getCountPlayers();
		out.print(countPlayers);
		%>
			<form method="POST">			
				<input type="hidden" name="leave" value="<%= user %>">
				<input type="submit" value="Leave">			
			</form>
		<%
		if(WebGame.canStart()){
			%>
			<form method="POST" action="index.jsp">				
				<input type="hidden" name="user" value="<%= user %>">
				<input type="hidden" name="webStart" value="true">
				<input type="submit" value="Start Game">			
			</form>
			<%
		}
	} else {
		out.print("Join fail");
		%>
		<form method="POST">
			<input type="hidden" name="join" value="true">
			<input type="submit" value="Join Game">
		</form>
		<%
	}
} else if(leave != null){
	WebGame.leave(leave);
	int countPlayers = WebGame.getCountPlayers();
	out.print(countPlayers);
	%>
	<form method="POST">
		<input type="hidden" name="join" value="true">
		<input type="submit" value="Join Game">
	</form>
	<%
} else {
	int countPlayers = WebGame.getCountPlayers();
	out.print(countPlayers);
	%>
	<form method="POST">
		<input type="hidden" name="join" value="true">
		<input type="submit" value="Join Game">
	</form>
	<%
}
%>
</body>
</html>