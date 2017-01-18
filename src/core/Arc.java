package core;

import base.*;

public class Arc {
	private int destination;
	private int longueur;
	private Descripteur descripteur;
	
	public Arc(int dest,int longueur, Descripteur d) {
		this.destination = dest;
		this.longueur=longueur;
		this.descripteur = d;
	}
	
	public Descripteur getDescripteur(){
		return descripteur;
	}

	public int getLongueur() {
		return longueur;
	}
	
	public int getDestination() {
		return destination;
	}
	
	
	
}
