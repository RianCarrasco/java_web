<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String alerta = (String)request.getAttribute("alerta"); %>    
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nova Receita</title>
    <link rel="stylesheet" href="./styles/novareceita.css">
    <link rel="stylesheet" href="./styles/configs.css">
    <link class="favicon" rel="shortcut icon" type="png" href="./imgs/favicon.png">
</head>
<body>
    <%@ include file="header.jsp" %>
	<% 
		if(usuarioOnline == null || !usuarioOnline.getAdmin()){
			response.sendRedirect("login.jsp");
		}
	%>
	<%  if (alerta != null) { %>
		    <p class="erro_msg"><%= alerta %></p>
	<%  } %>
    <div class="container">
        <h2>Nova Receita</h2>
        <form action="cadastrarReceita">
            <div class="recipe">
            	<h3>Titulo</h3>
            	<input type="text" name = "titulo" required>
            	<h3>Ingredientes</h3>
            	<textarea name="ingredientes" required></textarea>
                <h3>Modo de preparo</h3>
                <textarea name="conteudo" required></textarea>
            </div>
            <div class="categorias">
                <h3>Categorias da receita</h3>
                <div class="categorias-inputs">
                    <div class="categorias-group">
                        <input type="checkbox" id="cafedamanha" name="categorias[]" value="Café da manhã">
                        <label for="cafedamanha">Café da manhã</label>
                    </div>
                    <div class="categorias-group">
                        <input type="checkbox" id="almoco" name="categorias[]" value="Almoço">
                        <label for="almoco">Almoço</label>
                    </div>
                    <div class="categorias-group">
                        <input type="checkbox" id="doce" name="categorias[]" value="Doces e Sobremesas">
                        <label for="doce">Doces e Sobremesas</label>
                    </div>
                    <div class="categorias-group">
                        <input type="checkbox" id="cafedatarde" name="categorias[]" value="Café da tarde">
                        <label for="cafedatarde">Café da tarde</label>
                    </div>
                    <div class="categorias-group">
                        <input type="checkbox" id="jantar" name="categorias[]" value="Jantar">
                        <label for="jantar">Jantar</label>
                    </div>
                    <div class="categorias-group">
                        <input type="checkbox" id="massas" name="categorias[]" value="Massas">
                        <label for="massas">Massas</label>
                    </div>
                    <div class="categorias-group">
                        <input type="checkbox" id="bebidas" name="categorias[]" value="Bebidas">
                        <label for="bebidas">Bebidas</label>
                    </div>
                </div>
            </div>
            <div class="button">
                <button type="submit">Criar</button>
            </div>
        </form>
    </div>
    <%@ include file="footer.jsp"%>
</body>
</html>