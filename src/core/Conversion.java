package core;

public class Conversion {
	public final static float vitesseMetreSeconde(int vitesseKPH)
	{
		return (float) (vitesseKPH / 3.6);
	}
	
	public final static String tempsJoli(float sec)
	{
		//calcul des heures
		int h = (int)sec / 3600;
		//calcul des minutes
		int mins = (int)((sec / 60)%60);
		
		//calcul des secondes
		sec = sec % 60;
		
		String buffer = "";
		if(h != 0)
			buffer += h + " h";
		
		if(mins != 0)
			buffer += mins + " min ";
		
		buffer += sec + " s";
		
		return buffer;
	}

}
