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

import model.Recipe;
import model.RecipeDAO;
import model.User;

@WebServlet(urlPatterns = { "/index.html", "/receitas", "/deletarRecipe" })
public class ControllerRecipes extends HttpServlet {
	private static final long serialVersionUID = 1L;
	HttpSession session;
	private RecipeDAO dao = new RecipeDAO();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();

		// Controle das requisições ao acessar as rotas desse servlet
		if (action.equals("/index.html")) {
			listarReceitas(request, response);
		} else if (action.equals("/receitas")) {
			visualizarReceitasDaCategoria(request, response);
		} else if (action.equals("/deletarRecipe")) {
			deletarReceita(request, response);
		}
	}

	protected void listarReceitas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<Recipe> receitas = dao.listarReceitas(); // pegar a lista de receitas no banco de dados

		/*
		 * //testando preenchimento da lista de receitas for(int
		 * i=0;i<receitas.size();i++) { System.out.println("Dados receita");
		 * System.out.println(receitas.get(i).getId());
		 * System.out.println(receitas.get(i).getTitulo());
		 * System.out.println(receitas.get(i).getIngredientes());
		 * System.out.println(receitas.get(i).getConteudo());
		 * System.out.println(receitas.get(i).getData());
		 * System.out.println(receitas.get(i).getQntComentario());
		 * System.out.println("Criador");
		 * System.out.println(receitas.get(i).getAutor().getId());
		 * System.out.println(receitas.get(i).getAutor().getNome());
		 * System.out.println(receitas.get(i).getAutor().getSenha());
		 * System.out.println(receitas.get(i).getAutor().getQntReceitas());
		 * System.out.println(receitas.get(i).getAutor().getAdmin());
		 * System.out.println("Categorias"); for(int
		 * j=0;j<receitas.get(i).getCategorias().size();j++) {
		 * System.out.print(receitas.get(i).getCategorias().get(j).getId()+"       ");
		 * System.out.println(receitas.get(i).getCategorias().get(j).getNome()); } }
		 */

		// redirecionar para a pagina inicial mandando a lista de receitas a ser
		// mostradas no jsp
		request.setAttribute("receitas", receitas);
		RequestDispatcher rd = request.getRequestDispatcher("/receitas.jsp");
		rd.forward(request, response);
	}

	protected void visualizarReceitasDaCategoria(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int idCategoria = Integer.parseInt(request.getParameter("categoria_id"));

		ArrayList<Recipe> receitas = dao.getReceitasPorCategoria(idCategoria);
		String categoriaName = dao.getCategoria(idCategoria);

		// Definindo as receitas como atributo da solicitação para enviar ao JSP
		request.setAttribute("receitas", receitas);
		request.setAttribute("categoria", categoriaName);

		// Encaminhando para o JSP para exibir as receitas
		RequestDispatcher rd = request.getRequestDispatcher("/receitas.jsp");
		rd.forward(request, response);
	}

	protected void deletarReceita(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int idReceita = Integer.parseInt(request.getParameter("receita_id"));
		session = request.getSession();
		User adminOn = (User) session.getAttribute("usuario");

		Recipe receita = dao.getReceitaPorId(idReceita);
		User autor = receita.getAutor();
		
		dao.deletarReceita(idReceita);
		dao.atualiza_qntReceita(autor.getId(), -1);
		
		if(adminOn.getId() == autor.getId()) {
			adminOn.atualizarQntReceitasCriadas(-1);	//atualizando na sessao o valor correto de qnt receitas
			session.setAttribute("usuario", adminOn);	
		}
		
		response.sendRedirect("index.html");
	}
}
