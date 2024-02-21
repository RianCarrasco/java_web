package model;

import java.util.Date;

public class Comment extends JavaBeans{
	private static final long serialVersionUID = 1L;
	private String conteudo;
	private Date data_criacao;
	private User autor;
	private Recipe receita;
	
	public Comment() {

	}
	
	public Comment(int id, String conteudo, Date data_criacao, User autor, Recipe receita) {
		super(id);
		this.conteudo = conteudo;
		this.data_criacao = data_criacao;
		this.autor = autor;
		this.receita = receita;
	}

	//Gets e Sets
	public String getConteudo() {
		return conteudo;
	}
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public Date getData_criacao() {
		return data_criacao;
	}
	public void setData_criacao(Date data_criacao) {
		this.data_criacao = data_criacao;
	}
	
	public User getAutor() {
		return autor;
	}
	public void setAutor(User autor) {
		this.autor = autor;
	}

	public Recipe getReceita() {
		return receita;
	}
	public void setReceita(Recipe receita) {
		this.receita = receita;
	}
	
}
