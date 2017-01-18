package core;
import java.awt.Color;
import java.util.*;

import base.Dessin;

public class Chemin {
	
	private ArrayList<Sommet> chemin;
	
	
	public Chemin(){
		this.chemin = new ArrayList<Sommet>();
	}
	
	public void addSommet(Sommet s){
		chemin.add(s);
	}
	
	public ArrayList<Sommet> getChemin(){return chemin;}
	
	
	public float calculerDistanceTotale(){
		float distance = 0;
		for(int i=1;i<chemin.size();i++){
			distance += chemin.get(i-1).arcPlusCourtDistanceVersVoisin(chemin.get(i)).getLongueur();
		}
		return distance;
	}
	
	public float calculerTempsTotal(){
		float temps = 0;
		for(int i=1;i<chemin.size();i++){
			temps += (float) (chemin.get(i-1).arcPlusCourtTempsVersVoisin(chemin.get(i)).getLongueur() / 
					Conversion.vitesseMetreSeconde(chemin.get(i-1).arcPlusCourtTempsVersVoisin(chemin.get(i)).getDescripteur().vitesseMax()));
		}
		//chemin_0x100_2_139
		return temps;
	}
	
	public void dessinerChemin(Dessin d)
	{
		
		for(int i=0; i < chemin.size()-1; i++)
		{
			d.drawLine(chemin.get(i).getLongitude(), chemin.get(i).getLatitude(), chemin.get(i+1).getLongitude(), chemin.get(i+1).getLatitude()) ;
		}
	}

	public void setChemin(ArrayList<Sommet> chemin) {
		this.chemin = chemin;
	}
	
	public void reverse(){
		if(!this.chemin.isEmpty())
			Collections.reverse(getChemin());
	}
	
	
}
