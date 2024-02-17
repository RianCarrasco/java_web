package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.CommentDAO;
import model.Recipe;
import model.RecipeDAO;
import model.User;
import model.Comment;
import model.DAO;
import java.util.Date;

@WebServlet(urlPatterns = { "/comentar", "/cadastrarReceita" })
public class Controller extends HttpServlet{
	private static final long serialVersionUID = 1L;
	Comment comentario = new Comment();
	CommentDAO comentarioDAO = new CommentDAO();
	RecipeDAO receitaDAO = new RecipeDAO();
	DAO dao = new DAO();
	Date dataAtual = new Date();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();

		// Controle das requisições ao acessar as rotas desse servlet
		if (action.equals("comentar")) {
			comentar(request, response);
		}

		if (action.equals("cadastrarReceita")) {
			cadastrarReceita(request, response);
		}

	}
	
	protected void comentar(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    // Obtendo os parâmetros do formulário de comentário
	    String conteudo = request.getParameter("conteudo"); // Supondo que "conteudo" seja o nome do campo de texto do comentário
	    int idUsuario = Integer.parseInt(request.getParameter("idUsuario")); // Supondo que "idUsuario" seja o ID do usuário logado
	    int idReceita = Integer.parseInt(request.getParameter("idReceita")); // Supondo que "idReceita" seja o ID da receita à qual o comentário será associado
	    
	    // Criando um objeto Comment com os dados fornecidos
	    User autor = new User();
	    autor.setId(idUsuario); // Definindo o ID do usuário autor do comentário
	    Recipe receita = new Recipe();
	    receita.setId(idReceita); // Definindo o ID da receita associada ao comentário
	    
	    Comment novoComentario = new Comment();
	    novoComentario.setConteudo(conteudo);
	    novoComentario.setAutor(autor);
	    novoComentario.setReceita(receita);
	    
	    // Adicionando o comentário ao banco de dados usando o CommentDAO
	    comentarioDAO.adicionarComentario(novoComentario);
	    
	    // Redirecionando de volta para a página da receita ou realizando outra ação desejada
	    response.sendRedirect(""); // Colocar a rota 
	}


	protected void cadastrarReceita(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	    try {
	    	DAO dao = new DAO();
	        // Obtendo os parâmetros do formulário de cadastro de receita
	        String titulo = request.getParameter("titulo"); // Supondo que "titulo" seja o nome do campo de texto do título
	        String conteudo = request.getParameter("conteudo"); // Supondo que "conteudo" seja o nome do campo de texto do conteúdo da receita

	        LocalDate dataAtual = LocalDate.now();
	        LocalDateTime localDateTime = dataAtual.atStartOfDay();
	        Date data = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());//olha isso efieoinfoiwenfoewnfoewf
	        User autor = dao.getAuthorById(Integer.parseInt(request.getParameter("autor_id"))); // Supondo que "idAutor" seja o ID do autor da receita
	        int qntLikes = Integer.parseInt(request.getParameter("qntLikes"));
	        int qntDislikes = Integer.parseInt(request.getParameter("qntDislikes"));
	        
	        // Criando um objeto Recipe com os dados fornecidos
	        
	        Recipe novaReceita = new Recipe();
	        novaReceita.setTitulo(titulo);
	        novaReceita.setConteudo(conteudo);
	        novaReceita.setData(data);
	        novaReceita.setAutor(autor);
	        novaReceita.setQntGostei(qntLikes);
	        novaReceita.setQntNaoGostei(qntDislikes);
	        // Defina outros atributos da receita, se aplicável
	        
	        // Adicionando a receita ao banco de dados usando o RecipeDAO
	        receitaDAO.cadastrarReceita(novaReceita);
	        
	        // Redirecionando para a página de receitas ou realizando outra ação desejada
	        response.sendRedirect("/receitas"); // Redirecionando para a página de receitas
	    } catch (NumberFormatException e) {
	        // Tratamento para o caso em que algum parâmetro não é um número válido
	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        response.getWriter().println("Parâmetro inválido");
	    } catch (Exception e) {
	        // Tratamento para outros tipos de exceções
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        response.getWriter().println("Ocorreu um erro inesperado: " + e.getMessage());
	    }
	}

}
