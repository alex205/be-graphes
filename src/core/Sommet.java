package core;

import java.util.*;
import java.lang.Float;

public class Sommet {
	private int id;
	private float latitude;
	private float longitude;
	private ArrayList<Arc> voisins;
	private int nbSuccesseurs;
	
	
	public Sommet(int id, float latitude, float longitude, int nbSuccesseurs) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.voisins = new ArrayList<Arc>();
		this.nbSuccesseurs = nbSuccesseurs;
	}
	
	public void ajouterVoisin(Arc a)  {
		this.voisins.add(a);
	}
	
	public int getNbSuccesseurs() {
		return nbSuccesseurs;
	}

	public int getId() {
		return id;
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}
	
	public ArrayList<Arc> getVoisins(){
		return voisins;
	}
	
	public Arc arcPlusCourtDistanceVersVoisin(Sommet s){
		float min = Float.MAX_VALUE;
		Arc expectedArc = null;
		for(Arc a : this.voisins){
			if(s.getId() == a.getDestination()){
				if(a.getLongueur()< min){
					min = a.getLongueur();
					expectedArc = a;
				}
			}
		}
		return expectedArc;
	}
	
	
	public Arc arcPlusCourtTempsVersVoisin(Sommet s){
		float min = Float.MAX_VALUE;
		Arc expectedArc = null;
		for(Arc a : this.voisins){
			if(s.getId() == a.getDestination()){
				if((float)a.getLongueur()/(Conversion.vitesseMetreSeconde(a.getDescripteur().vitesseMax()))< min){ //PENSER 0 FAIRE LA CO?VERSIOn : D en metre et vit en KM/h actuellement
					min = (float)a.getLongueur()/(float)a.getDescripteur().vitesseMax();
					expectedArc = a;
				}
			}
		}
		return expectedArc;
	}
	
	
	public String toString(){
		return "n°"+ this.id;
	}
}
