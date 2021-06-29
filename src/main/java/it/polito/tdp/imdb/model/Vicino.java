package it.polito.tdp.imdb.model;

public class Vicino implements Comparable<Vicino>{
	
	private Director d;
	private Double peso;
	public Director getD() {
		return d;
	}
	public void setD(Director d) {
		this.d = d;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	public Vicino(Director d, Double peso) {
		super();
		this.d = d;
		this.peso = peso;
	}
	
	//decrescente
	@Override
	public int compareTo(Vicino other) {
		return (int)(other.peso-this.peso);
	}
	
	public String toString() {
		return this.d+" - "+this.peso;
	}
	

}
