package model;

import java.io.Serializable;

abstract class JavaBeans implements Serializable{	//visivel apenas neste pacote
	private static final long serialVersionUID = 1L;
	private int id;
	
	public JavaBeans() {
		
	}
	
	public JavaBeans(int id) {
		this.id=id;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id=id;
	}
}
