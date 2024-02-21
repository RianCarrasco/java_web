package model;

public class Category extends JavaBeans{
	private static final long serialVersionUID = 1L;
	private String nome;
	
	public Category(){

	}
	
	public Category(int id, String nome){
		super(id);
		this.nome=nome;
	}
	
	//Gets e Sets
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome=nome;
	}
}
