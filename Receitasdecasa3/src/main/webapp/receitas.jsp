<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.Recipe" %>
<%@ page import="java.util.ArrayList" %>
<%ArrayList<Recipe> receitas = (ArrayList<Recipe>) request.getAttribute("receitas");%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%@ include file="header.jsp" %>
	
	<%for(int i = 0; i < receitas.size(); i++){ %>
	<p><%=receitas.get(i).getTitulo()%></p>
	<%} %>
</body>
</html>