<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
 <meta charset="utf-8">
 <meta http-equiv="X-UA-Compatible" content="IE=edge">
 <meta name="viewport" content="width=device-width, initial-scale=1">
 <link rel="stylesheet" type="text/css" href="css/menu.css">
</head>

<body>
	<input type="checkbox" id="menu-toggle" />
	<label for="menu-toggle"></label>
	<ul id="menu">
		<li><a href="#">Home</a></li>
		<li><a href="#">Connect to Apps</a></li>
	</ul>
	<aside>
	<h1>Welcome!</h1>
	<h3><%=request.getAttribute("UserName") %></h3>
	</aside>
</body>
</html>