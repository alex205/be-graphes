package core;

public class LabelStar implements Comparable<LabelStar>{

	private boolean marquage;
	private float cout;
	private Sommet pere;
	private Sommet courant;
	private double heuristic;
	private double grad;
	
	public LabelStar(boolean marquage, float cout,double heuristic, Sommet pere, Sommet courant) {
		this.marquage = marquage;
		this.cout = cout;
		this.pere = pere;
		this.courant = courant;
		this.heuristic = heuristic;
		this.grad=heuristic;
	}

	public boolean isMarquage() {
		return marquage;
	}

	public void setMarquage(boolean marquage) {
		this.marquage = marquage;
	}

	public float getCout() {
		return cout;
	}

	public void setCout(float cout) {
		this.cout = cout;
	}

	public Sommet getPere() {
		return pere;
	}

	public void setPere(Sommet pere) {
		this.pere = pere;
	}

	public Sommet getCourant() {
		return courant;
	}

	public void setCourant(Sommet courant) {
		this.courant = courant;
	}

	@Override
	public int compareTo(LabelStar l) {
		int result=0;
		if(this.cout+this.heuristic < l.cout+l.heuristic)
			result = -1;
		else if(this.cout+this.heuristic == l.cout+l.heuristic){
			result =0;
			if(this.heuristic < l.heuristic){
				result =-1;
			} else if(this.heuristic > l.heuristic){
				result =1;
			} else {
				result =0;
			}
		} else {
			result = 1;
		}
		return result;
	}
	
	public double getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(double heuristic) {
		this.heuristic = heuristic;
	}

	public String toString(){
		return "Marque " + this.marquage + " || cout : " +this.cout +" || heuristic : " +this.heuristic +" || pere : "+ this.pere + " || courant "+ this.courant;
	}
	
}
