package model;

import java.util.ArrayList;
import java.util.Date;

public class Recipe extends JavaBeans{
	private static final long serialVersionUID = 1L;
	private String titulo;
	private String ingredientes;
	private String conteudo;
	private Date data;
	private int qntComentario;
	private User autor;
	private ArrayList<Category> categorias;

	public Recipe() {

	}

	public Recipe(int id, String titulo, String ingredientes, String conteudo, Date data, int qntComentario, User autor, ArrayList<Category> categorias) {
		super(id);
		this.titulo = titulo;
		this.ingredientes = ingredientes;
		this.conteudo = conteudo;
		this.data = data;
		this.autor = autor;
		this.categorias = categorias;
		this.qntComentario = qntComentario;
	}

	// Gets e sets
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getIngredientes() {
		return ingredientes;
	}

	public void setIngredientes(String ingredientes) {
		this.ingredientes = ingredientes;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public User getAutor() {
		return autor;
	}

	public void setAutor(User autor) {
		this.autor = autor;
	}

	public ArrayList<Category> getCategorias() {
		return categorias;
	}

	public void setCategorias(ArrayList<Category> categorias) {
		this.categorias = categorias;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public int getQntComentario() {
		return qntComentario;
	}

	public void setQntComentario(int qntComentario) {
		this.qntComentario = qntComentario;
	}
}
