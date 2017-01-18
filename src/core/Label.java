package core;

public class Label implements Comparable<Label>{

	protected boolean marquage;
	protected float cout;
	protected Sommet pere;
	protected Sommet courant;
	protected double heuristic;
	protected double grad;
	
	
	public Label(boolean marquage, float cout,double heuristic,double grad, Sommet pere, Sommet courant) {
		this.marquage = marquage;
		this.cout = cout;
		this.pere = pere;
		this.courant = courant;
		this.heuristic = heuristic;
		this.grad=grad;
		
	}

	public double getGrad() {
		return grad;
	}

	public void setGrad(double grad) {
		this.grad = grad;
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

	public double getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(double heuristic) {
		this.heuristic = heuristic;
	}
	
	
	

	
	public int compareTo(Label l) {
		int result=0;
			if(this.cout+this.heuristic < l.cout+l.heuristic)
				result = -1;
			else if(this.cout+this.heuristic == l.cout+l.heuristic){
				result =0;
				if(this.heuristic < l.heuristic){
					result =-1;
				} else if(this.heuristic> l.heuristic){
					result =1;
				} else {
					result =0;
				}
			} else {
				result = 1;
			}
		return result;
	}
	
//	@Override
//	public int compareTo(Label l) {
//		int result=0;
//		if(this.cout==l.cout)
//			result = 0;
//		else if(this.cout < l.cout)
//			result = -1;
//		else 
//			result = 1;
//		return result;
//		//return (int) (this.cout - l.cout);
//	}
	
	public String toString(){
		return "Marque " + this.marquage + " || cout : " +this.cout +" || pere : " + this.pere + " || courant "+ this.courant;
	}
	
}
