<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Recipe"%>
<%@ page import="model.Comment"%>
<%@ page import="java.util.ArrayList"%>
<%
	Recipe receita = (Recipe) request.getAttribute("receita");
	ArrayList<Comment> comentarios = (ArrayList<Comment>) request.getAttribute("comentarios");
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Receitas de casa</title>
	<link rel="stylesheet" href="./styles/configs.css">
	<link rel="stylesheet" href="./styles/receita.css">
	<link class="favicon" rel="shortcut icon" type="png" href="./imgs/favicon.png">
</head>
<body>
	<%@ include file="header.jsp"%>
	
	<div class="container">
		<h2><%= receita.getTitulo() %></h2>
	
		<p class="autor">
			<span>Publicado por: <%= receita.getAutor().getNome() %></span>
			<span><%= receita.getData() %></span>
		</p>
		
		<p class="categorias">
			<%for(int i=0;i<receita.getCategorias().size();i++){ %>
				<span><%= receita.getCategorias().get(i).getNome() %></span>
			<%} %>
		</p>
		
		<% if(usuarioOnline != null && usuarioOnline.getAdmin()){ %>
		<a class="deleteRecipe" href="deletarRecipe?receita_id=<%= receita.getId() %>">Deletar Receita</a>
		<%} %>
		
		<div class="quad">
			<p>Ingredientes</p>
		</div>
	
		<div class="texto">
			<p><%= receita.getIngredientes().replace("\n", "<br>") %></p>
		</div>
	
		<div class="quad">
			<p>Modo de Preparo</p>
		</div>
	
		<div class="texto">
			<p><%= receita.getConteudo().replace("\n", "<br>") %></p>
		</div>
	
		<div class="quad">
			<p>Comentários</p>
		</div>
		<div class="comentarios">
			<% if(usuarioOnline != null){ %>
			<form class="adicionar-comentario" action="comentar">
				<textarea placeholder="Escreva seu comentário" name="conteudo" required></textarea>
				<input type="hidden" name="receita_id" value="<%= receita.getId() %>">
				<button type="submit">Publicar</button>
			</form>
			<%}else{ %>
			<p class="erro_msg large">Voce precisa estar logado para realizar comentarios</p>
			<%} %>
			<% for(int i=0;i<comentarios.size(); i++){%>
			<div class="comentario">
				<div class="info-usuario">
					<p class="nome-usuario"><%= comentarios.get(i).getAutor().getNome() %></p>
					<p class="texto-comentario"><%= comentarios.get(i).getConteudo() %></p>
					<p class="data-comentario"><%= comentarios.get(i).getData_criacao() %></p>
				</div>
			</div>
			<%}%>
			
		</div>
	</div>
	<%@ include file="footer.jsp"%>
</body>
</html>