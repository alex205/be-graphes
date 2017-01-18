package core;

import java.awt.Color;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Set;

import base.Dessin;
import base.Readarg;

public class Covoiturage extends Algo{
	
//	private static int vitessePieton = 4;
//	private static int vitesseVoiture = 130;
//	private static int vitesseGirardi = 170;
	
	private int vitesse1 = 0;
	private int vitesse2 = 0;
	private int origine1;
	private int origine2;
	private int destination;
	private Readarg readarg;
	
	public Covoiturage(Graphe gr, PrintStream fichierSortie, Readarg readarg) {
		super(gr, fichierSortie, readarg);
		this.readarg = readarg;
		System.out.println("Numero du sommet d'origine 1 ?");
		this.origine1 = graphe.sommetClick();
		this.vitesse1 = readarg.lireInt ("à Combien roulez-vous ?");
		System.out.println("Numero du sommet d'origine 2 ?");
		this.origine2 = graphe.sommetClick();
		this.vitesse2 = readarg.lireInt ("à Combien roulez-vous ?");
		System.out.println("Numero du sommet de destination ?");
		this.destination = graphe.sommetClick();
		// TODO Auto-generated constructor stub
	}

	



	public int getOrigine1() {
		return origine1;
	}
	public void setOrigine1(int origine1) {
		this.origine1 = origine1;
	}
	public int getOrigine2() {
		return origine2;
	}
	public void setOrigine2(int origine2) {
		this.origine2 = origine2;
	}
	public int getDestination() {
		return destination;
	}
	public void setDestination(int destination) {
		this.destination = destination;
	}

	private HashMap<Sommet, Label> sommerAssoc(HashMap<Sommet, Label> assoc1,HashMap<Sommet, Label> assoc2,HashMap<Sommet, Label> assoc3){
		
		HashMap<Sommet, Label> assocResult = new HashMap<Sommet, Label>();
		Set <Sommet> inter = assoc1.keySet();
		inter.retainAll(assoc2.keySet());
		inter.retainAll(assoc3.keySet());
		float coef = (float)this.vitesse1/(float)this.vitesse2;
		
		for(Sommet s : inter){
			if(assoc1.containsKey(s) && assoc2.containsKey(s) && assoc3.containsKey(s)){
				if(coef>1){
					assocResult.put(s, new Label(true,(float)(assoc1.get(s).getCout())+assoc2.get(s).getCout()*coef+assoc3.get(s).getCout(),0,0,s,s));
				} else {
					assocResult.put(s, new Label(true,(float)(assoc1.get(s).getCout())+assoc2.get(s).getCout()*coef+assoc3.get(s).getCout(),0,0,s,s));
				}
				
			}
		}
		return assocResult;
	}

	private Sommet minAssoc(HashMap<Sommet, Label> assoc){
		double coutMin = Integer.MAX_VALUE;
		Sommet sommetMin = null;
		for(Sommet s : assoc.keySet()){
			if(assoc.get(s).getCout()<coutMin){
				coutMin = assoc.get(s).getCout();
				sommetMin = s;
			}
				
		}
		return sommetMin;
	}

	@Override
	public void run() {
		Dessin d = graphe.getDessin();
		
		double d1 = graphe.distance(graphe.getSommetI(this.origine1).getLongitude(), graphe.getSommetI(this.origine1).getLatitude(), graphe.getSommetI(this.destination).getLongitude(), graphe.getSommetI(this.destination).getLatitude());
		double d2 = graphe.distance(graphe.getSommetI(this.origine2).getLongitude(), graphe.getSommetI(this.origine2).getLatitude(), graphe.getSommetI(this.destination).getLongitude(), graphe.getSommetI(this.destination).getLatitude());
		
		Pcc pcc1 = null;
		try {
			pcc1 = new Pcc(graphe, this.sortie, this.readarg, this.origine1, destination,0) ;
		} catch (SommetInvalide e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			return;
		}
		Pcc pcc2 = null;
		try {
			pcc2 = new Pcc(graphe, this.sortie, this.readarg, this.origine2, destination,0) ;
		} catch (SommetInvalide e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		Pcc pcc3 = null;
			if(d1>d2)
				try {
					pcc3 = new Pcc(graphe, this.sortie, this.readarg, destination, this.origine1,0) ;
					return;
				} catch (SommetInvalide e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
					return;
				}
			else
				try {
					pcc3 = new Pcc(graphe, this.sortie, this.readarg, destination, this.origine2,0) ;
				} catch (SommetInvalide e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
					return;
				}
		HashMap<Sommet, Label> assoc1 = pcc1.runCovoiturage(2, this.vitesse1, 0);
		HashMap<Sommet, Label> assoc2 = pcc2.runCovoiturage(2, this.vitesse2, 0);
		
		HashMap<Sommet, Label> assoc3 = pcc3.runCovoiturage(2, (this.vitesse1<this.vitesse2?this.vitesse2:this.vitesse1), 0);
		Sommet rencontre = minAssoc(sommerAssoc(assoc1, assoc2, assoc3));
		
		Chemin court = new Chemin();
		if(assoc1.get(rencontre).getPere()!=null){
			Sommet aux = rencontre;
			
			while(aux != graphe.getSommetI(this.origine1)) {
				court.addSommet(aux);
				aux = assoc1.get(aux).getPere();
			}
			court.addSommet(aux);
			court.reverse();
			
			//dessin du chemin
			d.setColor(new Color(255*(this.vitesse1/130),255-255*(this.vitesse1/130),0));
			d.setColor(Color.red);
			d.setWidth(3);
			court.dessinerChemin(d);
		}
		
		if(assoc2.get(rencontre).getPere()!=null){
			Sommet aux = rencontre;
			
			while(aux != graphe.getSommetI(this.origine2)) {
				court.addSommet(aux);
				aux = assoc2.get(aux).getPere();
			}
			court.addSommet(aux);
			court.reverse();
			
			//dessin du chemin
			d.setWidth(3);
			d.setColor(new Color(255*(this.vitesse2/130),255-255*(this.vitesse2/130),0));
			d.setColor(Color.green);
			court.dessinerChemin(d);
		}
		
		Pcc pcc4 = null;
		try {
			pcc4 = new Pcc(graphe, this.sortie, this.readarg, rencontre.getId(), destination,1) ;
		} catch (SommetInvalide e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			return;
		}
		pcc4.runType(2, 130, pcc4.getAstar());
		
	}
	

	
	/**
	 * Algo :
	 * On choisit 2 points de départ. Alice (Point 1) et Bob (Point 2)
	 * On choisit une destination commune (Poulailler) à Alice et Boob
	 * Vu que l'on doit prendre en compte les vitesses de déplacement d'Alice et Bob, 
	 * nous utiliserons uniquement des Dijkstras en Temps.
	 * On effetcue un DIjkstra en temps depuis Alice vers le Poulailler
	 * On effectue un DIjkstra en temps depuis Bob vers le Poulailler
	 * On somme les elements communs aux tableaux associatif issus 
	 * des dits-dijkstras suscités précédemment (cette phrase est redondante comme le fenouil)
	 * On récupère l'élément le plus petit dans la table associative finale
	 * Si ce point est différent du Poulailler, il est ainsi qualifier de point de rencontre
	 * Dans ce cas, un dernier A* en Temps est lancé depuis ce dernier en direction du Poulailler
	 * dans le but final que Bob et Alice arrivent au Poulailler le plus rapidement possible
	 * en adéquation avec l'objectif initial suggéré par l'énoncé de cette problématique
	 */

}
