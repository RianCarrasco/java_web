package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class RecipeDAO extends DAO {	
	public void cadastrarReceita(Recipe receita) {	//método responsável por cadastrar uma nova receita no BD
		// comando a ser executado no banco de dados
		String create = "insert into Receitas (titulo,ingredientes,conteudo,data_publicacao,autor_id) values (?,?,?,now(),?)";
	    int id_gerada = 0;

		try {
			Connection con = conectar(); // abrir conexão

			PreparedStatement pst = con.prepareStatement(create, PreparedStatement.RETURN_GENERATED_KEYS); // preparar query a ser executada
			// enviando os valores para query
			pst.setString(1, receita.getTitulo());
			pst.setString(2, receita.getIngredientes());
			pst.setString(3, receita.getConteudo());
			User usuario_id = receita.getAutor();
			pst.setInt(4, usuario_id.getId());
			pst.executeUpdate(); // executando comando
			
		    ResultSet rs=pst.getGeneratedKeys(); 
		    if (rs.next()) {
		    	id_gerada = rs.getInt(1); 
		        receita.setId(id_gerada); //Salva o id da receita na classe.
		    }
			con.close();
			
		} catch (Exception e) {
			System.out.println("Erro ao cadastrar receita: " + e.getMessage());
		}
	}
	
	public int getCategoria(String categoriaName) {	//Método responsável por retornar o ID de uma categoria através de um nome, préviamente cadastrado no BD
		int ID = 0;
		try {
			String create = "select categoria_id from categorias where nome = ?";	//comando sql a ser executado

			Connection con = conectar(); // abrir conexão
			PreparedStatement pst = con.prepareStatement(create); // preparar query a ser executada
			
			pst.setString(1, categoriaName);
			
			ResultSet rs = pst.executeQuery();
			
			if (rs.next()) {
				ID = rs.getInt(1);
			}
			con.close();
					
		} catch (Exception e) {
			System.out.println("Erro ao solicitar o ID da categoria: " + e.getMessage());
		}
		return ID;
	}
	
	public String getCategoria(int idCategoria) {	//sobrecarga getCategoria, este método retorna o nome da categoria atraves do ID
		String categoriaName = null;
		try {
			String create = "select nome from categorias where categoria_id = ?";	//comando sql a ser executado

			Connection con = conectar(); // abrir conexão
			PreparedStatement pst = con.prepareStatement(create); // preparar query a ser executada
			
			pst.setInt(1, idCategoria);
			
			ResultSet rs = pst.executeQuery();
			
			if (rs.next()) {
				categoriaName = rs.getString(1);
			}
			con.close();
					
		} catch (Exception e) {
			System.out.println("Erro ao solicitar o nome da categoria: " + e.getMessage());
		}
		
		return categoriaName;
	}
	
	public void cadastrarCategoria(Recipe receita) {//Método responsável por preencher a tabela categoriasreceita ao cadastro de uma nova receita
		try {
			String create2 = "insert into receitascategorias(receita_id,categoria_id) values (?,?)";

			Connection con = conectar(); // abrir conexão
			PreparedStatement pst = con.prepareStatement(create2); // preparar query a ser executada
			
			ArrayList<Category> vet_categorias = receita.getCategorias();	//categorias da receita criada

			for (int i = 0; i < vet_categorias.size(); i++) {	//preenchendo os valores na tabela de acordo com cada uma das categorias
				Category vet_atual = vet_categorias.get(i);		
				pst.setInt(1, receita.getId());
				pst.setInt(2, vet_atual.getId());
				pst.executeUpdate();
			}				
			con.close();
		}
		catch (Exception e) {
			System.out.println("Erro ao cadastrar a lista de categorias no BD " + e.getMessage());
		}
		
	}

	public void atualiza_qntReceita(int idAutor, int qnt) {	//método responsável por acrescentar somar ou subtrair a quantidade de receitas criadas por um usuario
		try {
			String atualizar = "update usuarios set qntReceitas = qntReceitas + ? where usuario_id = ?";

			Connection con = conectar(); // abrir conexão
			PreparedStatement pst = con.prepareStatement(atualizar); // preparar query a ser executada
			
			pst.setInt(1, qnt);
			pst.setInt(2, idAutor);
			pst.executeUpdate();
			con.close();
			
		} catch (Exception e) {
			System.out.println("Erro ao atualizar quantidade de receitas: " + e.getMessage());
		}
	}
	
	public ArrayList<Recipe> listarReceitas() {		//método responsável por retornar uma lista com todas as receitas do BD, por ordem de publicação
		// comando a ser executado no banco de dados
		String read = "select * from Receitas order by data_publicacao desc";
		ArrayList<Recipe> receitas = new ArrayList<>();

		try {
			Connection con = conectar(); // abrir conexão

			PreparedStatement pst = con.prepareStatement(read); // preparando a querry com o comando a ser executado
			ResultSet rs = pst.executeQuery(); // executando o comando trazendo todos os dados para o ResultSet (importado)
			
			// percorrento todos os registros do resultado da requisição
			while (rs.next()) {
				int id = rs.getInt(1); // pegando so valores do result set
				String titulo = rs.getString(2);
				String ingredientes = rs.getString(3);
				String conteudo = rs.getString(4);
				Date data = rs.getDate(5);
				int qntComentario = rs.getInt(6);
				int autorid = rs.getInt(7);

				User autor = getAuthorById(autorid);					//método da classe pai DAO
				ArrayList<Category> categorias = getCategoriesById(id); //método da classe pai DAO

				receitas.add(new Recipe(id, titulo, ingredientes, conteudo, data, qntComentario, autor, categorias));
			}
			con.close();

			return receitas;
		} catch (Exception e) {
			System.out.println("Erro ao listar receitas: " + e.getMessage());
			return null;
		}
	}

	public Recipe getReceitaPorId(int idReceita) {	//método responsável por retornar uma receita através de seu ID
		Recipe receita = null;
		String query = "SELECT * FROM Receitas WHERE receita_id = ?";
		
		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, idReceita);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				receita = new Recipe();
				receita.setId(rs.getInt("receita_id"));
				receita.setTitulo(rs.getString("titulo"));
				receita.setIngredientes(rs.getString("ingredientes"));
				receita.setConteudo(rs.getString("conteudo"));
				receita.setData(rs.getDate("data_publicacao"));
				
				// Definindo o autor da receita
				User autor = getAuthorById(rs.getInt("autor_id"));
				receita.setAutor(autor);
				
				// Definindo categorias da receita
				ArrayList<Category> categorias = getCategoriesById(receita.getId());//método da classe pai DAO
				receita.setCategorias(categorias);	
			}
			con.close();
		} catch (SQLException e) {
			System.out.println("Erro ao obter receita: " + e.getMessage());
		}
		return receita;
	}

	public ArrayList<Recipe> getReceitasPorCategoria(int idCategoria) {	//método responsável por retornar uma lista de receitas que contenham uma categoria cujo id seja idCategoria
		ArrayList<Recipe> receitas = new ArrayList<>();
		String query = "SELECT r.*, u.nome AS autor_nome " + "FROM Receitas r "
				+ "INNER JOIN Usuarios u ON r.autor_id = u.usuario_id "
				+ "INNER JOIN ReceitasCategorias rc ON r.receita_id = rc.receita_id " + "WHERE rc.categoria_id = ?";
		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, idCategoria);
			
			ResultSet rs = pst.executeQuery();
			
			while (rs.next()) {
				Recipe receita = new Recipe();
				receita.setId(rs.getInt("receita_id"));
				receita.setTitulo(rs.getString("titulo"));
				receita.setConteudo(rs.getString("conteudo"));
				receita.setData(rs.getDate("data_publicacao"));
				receita.setQntComentario(rs.getInt("qntComentario"));

				// Definindo o autor da receita
				User autor = getAuthorById(rs.getInt("autor_id")); //método da classe pai DAO

				receita.setAutor(autor);

				receitas.add(receita);
			}
			con.close();
		} catch (SQLException e) {
			System.out.println("Erro ao obter receitas por categoria: " + e.getMessage());
		}
		return receitas;
	}
	
    public void deletarReceita(int idReceita) {	//método responsável por deletar uma Receita atraves de um ID fornecido
        String query = "DELETE FROM Receitas WHERE receita_id = ?";
        try {
            Connection con = conectar();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, idReceita);
            pst.executeUpdate();
            con.close();
        } catch (SQLException e) {
            System.out.println("Erro ao deletar receita: " + e.getMessage());
        }
    }
}
