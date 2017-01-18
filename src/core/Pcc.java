package core ;

import java.awt.Color;

import java.io.* ;
import java.util.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import base.Dessin;
import base.Readarg ;

public class Pcc extends Algo {
	
	static private int type = 0;
	

    // Numero des sommets origine et destination
    protected int zoneOrigine ;
    protected int origine ;

    private int astar;
    
    protected int zoneDestination ;
    protected int destination ;
    protected Readarg readarg;
    
    public Pcc(Graphe gr, PrintStream sortie, Readarg readarg,int choix) throws SommetInvalide {
    	super(gr, sortie, readarg) ;
    	this.readarg = readarg;
    	this.setAstar(choix);
    	this.zoneOrigine = gr.getZone () ;
    	System.out.println("Numero du sommet d'origine ? ");
    	this.origine = graphe.sommetClick();
    	System.out.println("Origine long " + graphe.getSommetI(this.origine).getLongitude() + " lat " + graphe.getSommetI(this.origine).getLatitude());
    	this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;
    	if(this.origine < 0 || this.origine > graphe.getSize())
    		throw new SommetInvalide("Sommet inexistant");

    	// Demander la zone et le sommet destination.
		this.zoneOrigine = gr.getZone () ;
		System.out.println("Numero du sommet destination ? ");
    	this.destination = graphe.sommetClick();
    	System.out.println("Destination long " + graphe.getSommetI(this.destination).getLongitude() + " lat " + graphe.getSommetI(this.destination).getLatitude());
		this.destination = readarg.lireInt ("Numero du sommet destination ? ");
    	if(this.origine < 0 || this.origine > graphe.getSize())
    		throw new SommetInvalide("Sommet inexistant");
    }
    
    public Pcc(Graphe gr, PrintStream sortie, Readarg readarg, int origine, int destination,int choix) throws SommetInvalide {
    	super(gr, sortie, readarg) ;
    	this.readarg = readarg;
    	this.setAstar(choix);
    	this.zoneOrigine = gr.getZone () ;

    	this.origine = origine;
    	if(this.origine < 0 || this.origine > graphe.getSize())
    		throw new SommetInvalide("Sommet inexistant");
    	//this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;

    	// Demander la zone et le sommet destination.
		this.zoneOrigine = gr.getZone () ;
    	this.destination = destination;
    	if(this.origine < 0 || this.origine > graphe.getSize())
    		throw new SommetInvalide("Sommet inexistant");
		//this.destination = readarg.lireInt ("Numero du sommet destination ? ");
    }

    
    public int getAstar() {
		return astar;
	}

	public void setAstar(int astar) {
		this.astar = astar;
	}

	protected double calculHeuristicDistance(Sommet s){
    	return Graphe.distance(s.getLongitude(), s.getLatitude(), graphe.getSommetI(destination).getLongitude(), graphe.getSommetI(destination).getLatitude());
    }
    
    protected double calculHeuristicTemps(Sommet s, int maxSpeed){
    	return Graphe.distance(s.getLongitude(), s.getLatitude(), graphe.getSommetI(destination).getLongitude(), graphe.getSommetI(destination).getLatitude())/Conversion.vitesseMetreSeconde(maxSpeed);
    }
    
    protected void afficherResultats(Chemin c){
    	System.out.println("Distance totale du chemin : " + c.calculerDistanceTotale() + " Temps total du chemin : " + Conversion.tempsJoli(c.calculerTempsTotal()));
    }
    
    
    protected void gradient(Dessin d, float echelle, float echelleVersDebut, float echelleVersFin, Label current){
			if(current.getGrad()>echelle){
  				d.setColor(new Color(255,((int)(255*(current.getGrad()/echelleVersDebut))<255 && current.getGrad() < echelleVersDebut )?(int)(255-255*(current.getGrad()-echelle)/(echelleVersDebut-echelle)):0,0));
  			}else{
  				d.setColor(new Color(((int)(255*(current.getGrad()/echelle))< 255 && current.getGrad() < echelleVersFin)? (int)(255*(current.getGrad()/(echelleVersFin))):255,255,0));
  	  			
  			}
			if(current.getPere()!=null)
				d.drawLine(current.getCourant().getLongitude(), current.getCourant().getLatitude(), current.getPere().getLongitude(), current.getPere().getLatitude());
			
    }
    
    
  public void run(){
	  System.out.println("Choisissez un mode :");
	  System.out.println("1 - En Distance");
	  System.out.println("2 - En Temps");
	  int choix = readarg.lireInt("Votre choix :");
	 if(choix == 1){
		 type =1;
	 }else if(choix==2){
		 type =2;
	 }
	 this.runType(type, 130,this.astar);
  }
  
  
  public Chemin runType(int type, int maxSpeed, int astar) {
  	long debut = System.currentTimeMillis();
  	int maxSizeTas = 0;
  	Dessin d = graphe.getDessin();

  	int ite =0;
  	int sommetsMarques = 0;

  	Label current = null;
  	Label labelFin = new Label(false, Float.MAX_VALUE,Float.MAX_VALUE,Float.MAX_VALUE, null, graphe.getSommetI(this.destination));
  	Label labelDebut;
  	
  	float echelle;
  	if(type==1 && astar==0){
  		labelDebut = new Label(false, 0,0,calculHeuristicDistance(graphe.getSommetI(this.origine)), null, graphe.getSommetI(this.origine));
  		echelle = (float)graphe.distance(labelDebut.getCourant().getLongitude(), labelDebut.getCourant().getLatitude(), labelFin.getCourant().getLongitude(), labelFin.getCourant().getLatitude());
  	}else if(type ==1 && astar ==1) {
  		labelDebut = new Label(false, 0,calculHeuristicDistance(graphe.getSommetI(this.origine)),calculHeuristicDistance(graphe.getSommetI(this.origine)), null, graphe.getSommetI(this.origine));
  		echelle = (float)graphe.distance(labelDebut.getCourant().getLongitude(), labelDebut.getCourant().getLatitude(), labelFin.getCourant().getLongitude(), labelFin.getCourant().getLatitude());
  	}else if(type==2 && astar==0){
  		labelDebut = new Label(false, 0,0,calculHeuristicTemps(graphe.getSommetI(this.origine),maxSpeed), null, graphe.getSommetI(this.origine));
  		echelle = (float)graphe.distance(labelDebut.getCourant().getLongitude(), labelDebut.getCourant().getLatitude(), labelFin.getCourant().getLongitude(), labelFin.getCourant().getLatitude())/Conversion.vitesseMetreSeconde(maxSpeed); 	 
  	}else {
  		labelDebut = new Label(false, 0,calculHeuristicTemps(graphe.getSommetI(this.origine),maxSpeed),calculHeuristicDistance(graphe.getSommetI(this.origine)), null, graphe.getSommetI(this.origine));
  		echelle = (float)graphe.distance(labelDebut.getCourant().getLongitude(), labelDebut.getCourant().getLatitude(), labelFin.getCourant().getLongitude(), labelFin.getCourant().getLatitude())/Conversion.vitesseMetreSeconde(maxSpeed);
  	}
  	float echelleVersFin = (float) (0.5 * echelle);
    float echelleVersDebut= (float) (1.10*echelle);
  	HashMap<Sommet, Label> assoc = new HashMap<Sommet, Label>();
  	BinaryHeap<Label> tas = new BinaryHeap<Label>();
	
  	assoc.put(graphe.getSommetI(this.origine),labelDebut);
  	tas.insert(assoc.get(graphe.getSommetI(this.origine)));
  	
  	//mauvaise idée
  	assoc.put(graphe.getSommetI(this.destination), labelFin);
  	tas.insert(assoc.get(graphe.getSommetI(this.destination)));
  	
  	
		// A vous d'implementer la recherche de plus court chemin.
		while(!tas.isEmpty() && labelFin.getPere()==null){//(sommetsMarques != sommetsTotaux)){
			//System.out.println("tas dans la boucle : ");
			if(tas.size()>maxSizeTas)
				maxSizeTas=tas.size();

//			On recupère le premier du tas
			current = tas.deleteMin();

//			On ajoute ses voisins au tas + update des labels associés
			ite++;
			for(Arc a : current.getCourant().getVoisins()){
				//on ajoute le lien entre le nouveau sommet et son label s'il n'est pas deja dedans
				if(assoc.get(graphe.getSommetI(a.getDestination())) != null){
					if(!(assoc.get(graphe.getSommetI(a.getDestination())).isMarquage())){
						if(type==1){
							if(assoc.get(graphe.getSommetI(a.getDestination())).getCout() > current.getCout()+a.getLongueur()){
								assoc.get(graphe.getSommetI(a.getDestination())).setCout(current.getCout()+a.getLongueur());
								assoc.get(graphe.getSommetI(a.getDestination())).setPere(current.getCourant());
								tas.update(assoc.get(graphe.getSommetI(a.getDestination())));
							}
						}else if(type == 2){
							if(assoc.get(graphe.getSommetI(a.getDestination())).getCout() > current.getCout()+(a.getLongueur()/Conversion.vitesseMetreSeconde(a.getDescripteur().vitesseMax()))){
								assoc.get(graphe.getSommetI(a.getDestination())).setCout(current.getCout()+(a.getLongueur()/Conversion.vitesseMetreSeconde(a.getDescripteur().vitesseMax())));
								assoc.get(graphe.getSommetI(a.getDestination())).setPere(current.getCourant());
								tas.update(assoc.get(graphe.getSommetI(a.getDestination())));
							}
						}
						
					}
				} else {
					if(type==1 && astar == 0){
						assoc.put(graphe.getSommetI(a.getDestination()), new Label(false, current.getCout()+a.getLongueur(), 0,calculHeuristicDistance(current.getCourant()), current.getCourant(), graphe.getSommetI(a.getDestination())));
					}else if(type == 2 && astar == 0){
						assoc.put(graphe.getSommetI(a.getDestination()), new Label(false, current.getCout()+(a.getLongueur()/Conversion.vitesseMetreSeconde(a.getDescripteur().vitesseMax())),0,calculHeuristicTemps(current.getCourant(),maxSpeed), current.getCourant(), graphe.getSommetI(a.getDestination())));	
					}else if(type == 1 && astar==1){
						assoc.put(graphe.getSommetI(a.getDestination()), new Label(false, current.getCout()+a.getLongueur(),calculHeuristicDistance(graphe.getSommetI(a.getDestination())),calculHeuristicDistance(graphe.getSommetI(a.getDestination())) ,current.getCourant(), graphe.getSommetI(a.getDestination())));
					}else{
						assoc.put(graphe.getSommetI(a.getDestination()), new Label(false, current.getCout()+(a.getLongueur()/Conversion.vitesseMetreSeconde(a.getDescripteur().vitesseMax())),calculHeuristicTemps(graphe.getSommetI(a.getDestination()), maxSpeed),calculHeuristicTemps(graphe.getSommetI(a.getDestination()), maxSpeed) ,current.getCourant(), graphe.getSommetI(a.getDestination())));
					}
					
					//on l'insert dans notre TAS
					tas.insert(assoc.get(graphe.getSommetI(a.getDestination())));
				}
			}
//			On fait sauter la racine et on la marque
			current.setMarquage(true);
			sommetsMarques++;
			
			//affichage des arc marqués
			//gradient(d, echelle, echelleVersDebut, echelleVersFin, current);
	
////			voilà
		}

		Chemin court = new Chemin();
		if(assoc.get(graphe.getSommetI(this.destination)).getPere()!=null){
			Sommet aux = graphe.getSommetI(this.destination);
			
			while(aux != graphe.getSommetI(this.origine)) {
				court.addSommet(aux);
				aux = assoc.get(aux).getPere();
			}
			court.addSommet(aux);
			court.reverse();
			
			//dessin du chemin
			d.setColor(new Color(75,160,250));
			d.setWidth(3);
			court.dessinerChemin(d);
			this.afficherResultats(court);
			sortie.print((System.currentTimeMillis()-debut) +","+maxSizeTas+","+assoc.size()+",");
			System.out.println((System.currentTimeMillis()-debut) +","+maxSizeTas+","+assoc.size()+",");
		}else{
			System.out.println("Sommet destination non atteignable");
			System.out.println((System.currentTimeMillis()-debut) +","+maxSizeTas+","+assoc.size()+",");
			//Some easter egg ! Joue la chanson "Papaoutai" lorsque l'algo ne trouve pas le sommet père ...
/*			try 
			{ 
			    InputStream in = new FileInputStream("papaoutai.wav");
			    AudioStream as = new AudioStream(in);
			    AudioPlayer.player.start(as);
			}
			catch (IOException e) 
			{
			    System.err.println(e);  
			}*/
		}	
		return court;
  }
  
  
  
  
  public HashMap<Sommet, Label> runCovoiturage(int type, int maxSpeed, int astar) {
	  	long debut = System.currentTimeMillis();
	  	int maxSizeTas = 0;
	  	Dessin d = graphe.getDessin();

	  	int ite =0;
	  	int sommetsMarques = 0;

	  	Label current = null;
	  	Label labelFin = new Label(false, Float.MAX_VALUE,Float.MAX_VALUE,Float.MAX_VALUE, null, graphe.getSommetI(this.destination));
	  	Label labelDebut;
	  	
	  	float echelle;
	  	if(type==1 && astar==0){
	  		labelDebut = new Label(false, 0,0,calculHeuristicDistance(graphe.getSommetI(this.origine)), null, graphe.getSommetI(this.origine));
	  		echelle = (float)graphe.distance(labelDebut.getCourant().getLongitude(), labelDebut.getCourant().getLatitude(), labelFin.getCourant().getLongitude(), labelFin.getCourant().getLatitude());
	  	}else if(type ==1 && astar ==1) {
	  		labelDebut = new Label(false, 0,calculHeuristicDistance(graphe.getSommetI(this.origine)),calculHeuristicDistance(graphe.getSommetI(this.origine)), null, graphe.getSommetI(this.origine));
	  		echelle = (float)graphe.distance(labelDebut.getCourant().getLongitude(), labelDebut.getCourant().getLatitude(), labelFin.getCourant().getLongitude(), labelFin.getCourant().getLatitude());
	  	}else if(type==2 && astar==0){
	  		labelDebut = new Label(false, 0,0,calculHeuristicTemps(graphe.getSommetI(this.origine),maxSpeed), null, graphe.getSommetI(this.origine));
	  		echelle = (float)graphe.distance(labelDebut.getCourant().getLongitude(), labelDebut.getCourant().getLatitude(), labelFin.getCourant().getLongitude(), labelFin.getCourant().getLatitude())/Conversion.vitesseMetreSeconde(maxSpeed); 	 
	  	}else {
	  		labelDebut = new Label(false, 0,calculHeuristicTemps(graphe.getSommetI(this.origine),maxSpeed),calculHeuristicDistance(graphe.getSommetI(this.origine)), null, graphe.getSommetI(this.origine));
	  		echelle = (float)graphe.distance(labelDebut.getCourant().getLongitude(), labelDebut.getCourant().getLatitude(), labelFin.getCourant().getLongitude(), labelFin.getCourant().getLatitude())/Conversion.vitesseMetreSeconde(maxSpeed);
	  	}
	  	float echelleVersFin = (float) (0.5 * echelle);
	    float echelleVersDebut= (float) (1.10*echelle);
	  	HashMap<Sommet, Label> assoc = new HashMap<Sommet, Label>();
	  	BinaryHeap<Label> tas = new BinaryHeap<Label>();
		
	  	assoc.put(graphe.getSommetI(this.origine),labelDebut);
	  	tas.insert(assoc.get(graphe.getSommetI(this.origine)));
	  	
	  	//mauvaise idée
	  	assoc.put(graphe.getSommetI(this.destination), labelFin);
	  	tas.insert(assoc.get(graphe.getSommetI(this.destination)));
	  	
	  	
			// A vous d'implementer la recherche de plus court chemin.
			while(!tas.isEmpty() && labelFin.getPere()==null){//(sommetsMarques != sommetsTotaux)){
				//System.out.println("tas dans la boucle : ");
				if(tas.size()>maxSizeTas)
					maxSizeTas=tas.size();

//				On recupère le premier du tas
				current = tas.deleteMin();

//				On ajoute ses voisins au tas + update des labels associés
				ite++;
				for(Arc a : current.getCourant().getVoisins()){
					//on ajoute le lien entre le nouveau sommet et son label s'il n'est pas deja dedans
					if(assoc.get(graphe.getSommetI(a.getDestination())) != null){
						if(!(assoc.get(graphe.getSommetI(a.getDestination())).isMarquage())){
							if(type==1){
								if(assoc.get(graphe.getSommetI(a.getDestination())).getCout() > current.getCout()+a.getLongueur()){
									assoc.get(graphe.getSommetI(a.getDestination())).setCout(current.getCout()+a.getLongueur());
									assoc.get(graphe.getSommetI(a.getDestination())).setPere(current.getCourant());
									tas.update(assoc.get(graphe.getSommetI(a.getDestination())));
								}
							}else if(type == 2){
								if(assoc.get(graphe.getSommetI(a.getDestination())).getCout() > current.getCout()+(a.getLongueur()/Conversion.vitesseMetreSeconde(maxSpeed))){
									assoc.get(graphe.getSommetI(a.getDestination())).setCout(current.getCout()+(a.getLongueur()/Conversion.vitesseMetreSeconde(maxSpeed)));
									assoc.get(graphe.getSommetI(a.getDestination())).setPere(current.getCourant());
									tas.update(assoc.get(graphe.getSommetI(a.getDestination())));
								}
							}
							
						}
					} else {
						if(type==1 && astar == 0){
							assoc.put(graphe.getSommetI(a.getDestination()), new Label(false, current.getCout()+a.getLongueur(), 0,calculHeuristicDistance(current.getCourant()), current.getCourant(), graphe.getSommetI(a.getDestination())));
						}else if(type == 2 && astar == 0){
							assoc.put(graphe.getSommetI(a.getDestination()), new Label(false, current.getCout()+(a.getLongueur()/Conversion.vitesseMetreSeconde(a.getDescripteur().vitesseMax())),0,calculHeuristicTemps(current.getCourant(),maxSpeed), current.getCourant(), graphe.getSommetI(a.getDestination())));	
						}else if(type == 1 && astar==1){
							assoc.put(graphe.getSommetI(a.getDestination()), new Label(false, current.getCout()+a.getLongueur(),calculHeuristicDistance(graphe.getSommetI(a.getDestination())),calculHeuristicDistance(graphe.getSommetI(a.getDestination())) ,current.getCourant(), graphe.getSommetI(a.getDestination())));
						}else{
							assoc.put(graphe.getSommetI(a.getDestination()), new Label(false, current.getCout()+(a.getLongueur()/Conversion.vitesseMetreSeconde(a.getDescripteur().vitesseMax())),calculHeuristicTemps(graphe.getSommetI(a.getDestination()), maxSpeed),calculHeuristicTemps(graphe.getSommetI(a.getDestination()), maxSpeed) ,current.getCourant(), graphe.getSommetI(a.getDestination())));
						}
						
						//on l'insert dans notre TAS
						tas.insert(assoc.get(graphe.getSommetI(a.getDestination())));
					}
				}
//				On fait sauter la racine et on la marque
				current.setMarquage(true);
				sommetsMarques++;
				
				//affichage des arc marqués
				//gradient(d, echelle, echelleVersDebut, echelleVersFin, current);
		
////				voilà
			}

			Chemin court = new Chemin();
			if(assoc.get(graphe.getSommetI(this.destination)).getPere()!=null){
				Sommet aux = graphe.getSommetI(this.destination);
				
				while(aux != graphe.getSommetI(this.origine)) {
					court.addSommet(aux);
					aux = assoc.get(aux).getPere();
				}
				court.addSommet(aux);
				court.reverse();
				
				//dessin du chemin
				d.setColor(new Color(75,160,250));
				d.setWidth(3);
				//court.dessinerChemin(d);
				//this.afficherResultats(court);
				sortie.print((System.currentTimeMillis()-debut) +","+maxSizeTas+","+assoc.size()+",");
			}else{
				System.out.println("Sommet destination non atteignable");
				
			}	
			return assoc;
	  }
	  
  
  
}  
