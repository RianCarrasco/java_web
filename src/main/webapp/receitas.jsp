<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.Recipe"%>
<%@ page import="java.util.ArrayList"%>
<%
ArrayList<Recipe> receitas = (ArrayList<Recipe>) request.getAttribute("receitas");
String categoria = (String) request.getAttribute("categoria");
%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
<meta charset="UTF-8">
<title>Receitas de casa</title>
<link rel="stylesheet" href="./styles/configs.css">
<link rel="stylesheet" href="./styles/receitas.css">
<link class="favicon" rel="shortcut icon" type="png"
	href="./imgs/favicon.png">
</head>
<body>
	<%@ include file="header.jsp"%>

	<div class="categorias">
		<h2>Categorias</h2>
		<div class="categoria-item">
			<a href="receitas?categoria_id=1"> <img src="./imgs/imagem1.jpg">
				<p>Café da Manhã</p>
			</a>
		</div>
		<div class="categoria-item">
			<a href="receitas?categoria_id=2"> <img src="./imgs/imagem2.jpg">
				<p>Almoço</p>
			</a>
		</div>
		<div class="categoria-item">
			<a href="receitas?categoria_id=3"> <img src="./imgs/imagem5.jpg">
				<p>Doces e Sobremesas</p>
			</a>
		</div>

		<div class="categoria-item">
			<a href="receitas?categoria_id=4"> <img src="./imgs/imagem3.jpg">
				<p>Café da tarde</p>
			</a>
		</div>
		<div class="categoria-item">
			<a href="receitas?categoria_id=5"> <img src="./imgs/imagem4.jpg">
				<p>Jantar</p>
			</a>
		</div>

		<div class="categoria-item">
			<a href="receitas?categoria_id=6"> <img src="./imgs/imagem6.jpg">
				<p>Massas</p>
			</a>
		</div>
		<div class="categoria-item">
			<a href="receitas?categoria_id=7"> <img src="./imgs/imagem7.jpg">
				<p>Bebidas</p>
			</a>
		</div>
	</div>

	<div class="receitasRecentes">
		<h2>
			Receitas Recentes
			<%
		if (categoria != null) {
		%>
			: Categoria =
			<%=categoria%>
			<%
			}
			%>
		</h2>

		<%
		for (int i = 0; i < receitas.size() && i < 12; i++) {
		%>
		<div class="quadroReceita">
			<p class="usuarioPub">
				Publicado por:
				<%=receitas.get(i).getAutor().getNome()%></p>
			<a href="receita?receita_id=<%=receitas.get(i).getId()%>"
				class="tituloReceita"><%=receitas.get(i).getTitulo()%></a>
			<p class="qntComentarios">
				Quantidade de comentários:
				<%=receitas.get(i).getQntComentario()%></p>
			<p class="dataPub"><%=receitas.get(i).getData()%></p>
		</div>
		<%
		}
		%>
	</div>
	
	<%@ include file="footer.jsp"%>
</body>
</html>