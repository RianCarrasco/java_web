package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Category;
import model.Comment;
import model.CommentDAO;
import model.Recipe;
import model.RecipeDAO;
import model.User;

@WebServlet(urlPatterns = { "/comentar", "/cadastrarReceita", "/receita" })
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	HttpSession session;
	private CommentDAO comentarioDAO = new CommentDAO();
	private RecipeDAO recipeDAO = new RecipeDAO();
	private Recipe receita = new Recipe();
	private User autor = new User();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		// Controle das requisições ao acessar as rotas desse servlet
		if (action.equals("/comentar")) {
			comentar(request, response);
		}else if (action.equals("/receita")) {
			visualizarReceita(request, response);
		} else if (action.equals("/cadastrarReceita")) {
			cadastrarReceita(request, response);
		}

	}

	protected void comentar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Obtendo os parâmetros do formulário de comentário
		String conteudo = request.getParameter("conteudo"); // Supondo que "conteudo" seja o nome do campo de texto do comentário
		int idReceita = Integer.parseInt(request.getParameter("receita_id")); // Supondo que "idReceita" seja o ID da receita à qual o comentário será associado
		
		session = request.getSession();
		autor = (User) session.getAttribute("usuario"); // pegar usuario logado atualmente

		// Criando um objeto Comment com os dados fornecidos
		Recipe receita = new Recipe();
		receita.setId(idReceita); // Definindo o ID da receita associada ao comentário

		Comment novoComentario = new Comment();
		novoComentario.setConteudo(conteudo);
		novoComentario.setAutor(autor);
		novoComentario.setReceita(receita);

		// Adicionando o comentário ao banco de dados usando o CommentDAO
		comentarioDAO.adicionarComentario(novoComentario);
		comentarioDAO.atualizaQntComentario(idReceita, 1);

		// Redirecionando de volta para a página da receita ou realizando outra ação desejada
		response.sendRedirect("receita?receita_id="+request.getParameter("receita_id")); 
	}

	protected void cadastrarReceita(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		autor = (User) session.getAttribute("usuario");
		ArrayList<Category> vet_categorias = new ArrayList<Category>();
		int id_cat = 0;

		// Armazenando vetor de categorias
		String[] cache_checkbox = request.getParameterValues("categorias[]");
		
		//verifica se foi selecionado uma ou mais categorias
		if(cache_checkbox!= null && cache_checkbox.length> 0) {
			
			for (String categoria : cache_checkbox) {	//preenchendo o ArrayList de categorias de acordo com o forms
				id_cat = recipeDAO.getCategoria(categoria);
				vet_categorias.add(new Category(id_cat, categoria));
			}
			
			//falta atualizar a qntRecipeCriada na classe user
			
			// Inserção da Receita
			receita.setTitulo(request.getParameter("titulo"));
			receita.setIngredientes(request.getParameter("ingredientes"));
			receita.setConteudo(request.getParameter("conteudo"));
			receita.setCategorias(vet_categorias);
			receita.setAutor(autor);
			recipeDAO.cadastrarReceita(receita);
			recipeDAO.cadastrarCategoria(receita);
			recipeDAO.atualiza_qntReceita(autor.getId(),1);
			
			autor.atualizarQntReceitasCriadas(1); //atualizando na sessao o valor correto de qnt receitas
			session.setAttribute("usuario", autor);
			
			response.sendRedirect("index.html"); // redirecionar para pagina inicial
		}
		else {
			//Se nenhuma categoria for selecionada, enviar um alerta ao usuario.
			String alerta = "Selecione pelo menos 1 categoria";
			request.setAttribute("alerta", alerta);
			request.getRequestDispatcher("/novareceita.jsp").forward(request, response);
		}
	}
	
	protected void visualizarReceita(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // Obtendo o parâmetro "IDreceitas" da requisição
	    int IDreceita = Integer.parseInt(request.getParameter("receita_id"));
	    
	    // Obtendo a receita com base no ID
	    receita = recipeDAO.getReceitaPorId(IDreceita);
	    ArrayList<Comment> comentarios = comentarioDAO.getComentariosPorReceita(receita.getId());
	            
	    // Enviando a receita para o JSP
	    request.setAttribute("receita", receita);
	    request.setAttribute("comentarios", comentarios);
	    RequestDispatcher rd = request.getRequestDispatcher("/receita.jsp");
	    rd.forward(request, response);
	}

}
