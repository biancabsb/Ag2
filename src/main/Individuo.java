package main;

import java.util.List;

public abstract class Individuo {
	protected double avaliacao; 
	protected boolean avaliado = false;
	protected boolean maximizacao = true; 

	public abstract List <Individuo> recombinar(Individuo p2);
	public abstract Individuo mutar();
	public abstract double avaliar ();
	

	
	public double getAvaliacao() {
		if(!avaliado) {
			avaliacao = avaliar();
		    avaliado = true;
		}
		return avaliacao;
	}
	public boolean isMaximizacao() {
		return maximizacao;
	}
}
