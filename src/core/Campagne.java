package core;

import java.io.PrintStream;

import base.Readarg;

public class Campagne extends Algo{
	
	private int nbTests;
	private Readarg readarg;
	private float distances;
	private float temps;

	
	public Campagne(Graphe gr, PrintStream sortie, Readarg readarg){
		super(gr, sortie, readarg) ;
		this.nbTests=readarg.lireInt ("Combien voulez-vous effectuer de tests ? ");
		this.temps = 0;
		this.distances = 0;
	}

	public void run() {
		for(int i = 0; i<this.nbTests;i++){
			
			int origine = (int) (Math.floor(Math.random()*(graphe.getSize()+1)));
			int destination = (int) (Math.floor(Math.random()*(graphe.getSize()+1)));
			Pcc pcc1 = null;
			try {
				pcc1 = new Pcc(graphe, this.sortie, this.readarg, origine, destination,0) ;
			} catch (SommetInvalide e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			Pcc pcc2 = null;
			try {
				pcc2 = new Pcc(graphe, this.sortie, this.readarg, origine, destination,1) ;
			} catch (SommetInvalide e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			Chemin court = null;
//			sortie.println("-------------------------------------------------------");
//			sortie.println("TEST N�" + i);
//			sortie.println("-------------------------------------------------------");
//			sortie.print("SOMMET DE DEPART : " + origine);
//			sortie.print("SOMMET D ARRIVEE : " + destination);
			//D�roulement des algos sur un m�me chemin et r�cup�ration des datas
			
			for(int k=0;k<2;k++){
				for(int j = 0;j<2;j++){
					if(j==0){
						if(k==0){
							System.gc();
							court = pcc1.runType(1, 130,0);
						}else if(k==1){
							System.gc();
							court = pcc1.runType(2, 130,0);
						}
					}else if(j==1){
						if(k==0){
							System.gc();
							court = pcc2.runType(1, 130,1);
						}else if(k==1){
							System.gc();
							court = pcc2.runType(2, 130,1);
						}
					}
					
					distances = court.calculerDistanceTotale();
					temps = court.calculerTempsTotal();
					sortie.println(origine +","+destination +","+j +","+k +","+distances +","+temps);
					court =null;
				
					
//					sortie.println("TYPE :" +j +"  MODE : " +k);
//					sortie.println("DISTANCE TOTALE : "+distances + "  TEMPS TOTAL :" + Conversion.tempsJoli(temps));
//					sortie.println("\n");
				}
			}
			System.out.println("test N° :" + (i+1)+"/"+this.nbTests);

		}
		
	}
	
}
