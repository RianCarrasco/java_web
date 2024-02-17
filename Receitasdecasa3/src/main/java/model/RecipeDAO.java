package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class RecipeDAO extends DAO {
	public void cadastrarReceita(Recipe receita) {
		// comando a ser executado no banco de dados
		String create = "insert into Receitas (titulo,conteudo,data_publicacao,qntLike,qntDislike,autor_id) values (?,?,?,?,?,?)";

		try {
			Connection con = conectar(); // abrir conexão

			PreparedStatement pst = con.prepareStatement(create); // preparar query a ser executada
			
			// enviando os valores para query
			pst.setString(1, receita.getTitulo());
			pst.setString(2, receita.getConteudo());
			java.sql.Date dataSql = new java.sql.Date(receita.getData().getTime());
			pst.setDate(3, dataSql);
			pst.setInt(4, 0);
			pst.setInt(5, 0);
			pst.setInt(6, receita.getAutor().getId());

			// falta atualizar a tabela receitascategorias com as categorias da receita
			// criada

			pst.executeUpdate(); // executando comando
			
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public ArrayList<Recipe> listarReceitas() {
		// comando a ser executado no banco de dados
		String read = "select * from Receitas order by data_publicacao desc";
		ArrayList<Recipe> receitas = new ArrayList<>();

		try {
			Connection con = conectar(); // abrir conexão

			PreparedStatement pst = con.prepareStatement(read); // preparando a querry com o comando a ser executado
			ResultSet rs = pst.executeQuery(); // executando o comando trazendo todos os dados para o ResultSet
												// (importado)
			// percorrento todos os registros do resultado da requisição
			while (rs.next()) {
				int id = rs.getInt(1); // pegando so valores do result set
				String titulo = rs.getString(2);
				String conteudo = rs.getString(3);
				Date data = rs.getDate(4);
				int qntLike = rs.getInt(5);
				int qntDislike = rs.getInt(6);
				int autorid = rs.getInt(7);

				User autor = getAuthorById(autorid); // implemente o metodo
				ArrayList<Category> categorias = getCategoriesById(id); // implemente o metodo

				receitas.add(new Recipe(id, titulo, conteudo, data, qntLike, qntDislike, autor, categorias));
			}
			con.close();
					
			return receitas;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
    public Recipe getReceitaPorId(int idReceita) {
        Recipe receita = null;
        String query = "SELECT r.*, u.nome AS autor_nome " +
                       "FROM Receitas r " +
                       "INNER JOIN Usuarios u ON r.autor_id = u.usuario_id " +
                       "WHERE r.receita_id = ?";//Tive que fazer essa gambiarra p/ comseguir o nome do autor
        try {
            Connection con = conectar();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, idReceita);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                receita = new Recipe();
                receita.setId(rs.getInt("receita_id"));
                receita.setTitulo(rs.getString("titulo"));
                receita.setConteudo(rs.getString("conteudo"));
                receita.setData(rs.getDate("data_publicacao"));
                receita.setQntGostei(rs.getInt("qntLikes"));
                receita.setQntNaoGostei(rs.getInt("qntDislikes"));
                
                // Definindo o autor da receita
                 User autor = getAuthorById(rs.getInt("autor_id"));
                //User autor = new User();
                //autor.setId(rs.getInt("autor_id"));
               // autor.setNome(rs.getString("autor_nome"));
                receita.setAutor(autor);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("Erro ao obter receita: " + e.getMessage());
        }
        return receita;
    }
    
    public ArrayList<Recipe> getReceitasPorCategoria(int idCategoria) {
    	ArrayList<Recipe> receitas = new ArrayList<>();
        String query = "SELECT r.*, u.nome AS autor_nome " +
                       "FROM Receitas r " +
                       "INNER JOIN Usuarios u ON r.autor_id = u.usuario_id " +
                       "INNER JOIN ReceitasCategorias rc ON r.receita_id = rc.receita_id " +
                       "WHERE rc.categoria_id = ?";
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
                receita.setQntGostei(rs.getInt("qntLikes"));
                receita.setQntNaoGostei(rs.getInt("qntDislikes"));

                // Definindo o autor da receita
                User autor = getAuthorById(rs.getInt("autor_id"));
                //User autor = new User();
                //autor.setId(rs.getInt("autor_id"));
                //autor.setNome(rs.getString("autor_nome"));
                receita.setAutor(autor);

                receitas.add(receita);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("Erro ao obter receitas por categoria: " + e.getMessage());
        }
        return receitas;
    }

}
