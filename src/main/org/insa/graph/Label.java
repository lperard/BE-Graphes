package org.insa.graph;


public class Label implements Comparable<Label>{
	
	protected Node sommet_courant;
	
	protected boolean marque;
	
	protected double cout;
	
	protected Arc arc_pere;
	
	protected boolean in_heap;

	public Label(Node sommet_courant, boolean marque, Arc arc_pere) {
		this.sommet_courant = sommet_courant;
		this.marque = marque;
		this.cout = this.arc_pere.getLength();
		this.arc_pere = arc_pere;
		this.in_heap = false;
	}
	public Label(Node sommet_courant, boolean marque) {
		this.sommet_courant = sommet_courant;
		this.marque = marque;
		this.cout = Double.POSITIVE_INFINITY;
		this.arc_pere = null;
		this.in_heap = false;
	}
	public Label(Node sommet_courant, boolean marque, double cout) {
		this.sommet_courant = sommet_courant;
		this.marque = marque;
		this.cout = cout;
		this.arc_pere = null;
		this.in_heap = false;
	}
	
	public Node getSommet_courant() {
		return sommet_courant;
	}

	public void setSommet_courant(Node sommet_courant) {
		this.sommet_courant = sommet_courant;
	}

	public boolean isMarque() {
		return marque;
	}

	public void setMarque(boolean marque) {
		this.marque = marque;
	}

	public double getCout() {
		return cout;
	}

	public void setCout(double cout) {
		this.cout = cout;
	}

	public Arc getArc_pere() {
		return arc_pere;
	}

	public void setArc_pere(Arc arc_pere) {
		this.arc_pere = arc_pere;
	}
	
	public boolean getInheap() {
		return in_heap;
	}
	
	public void setInheap (boolean b) {
		this.in_heap = b;
	}
	
	@Override
	public int compareTo(Label o) {
		double cout_other = o.getCout();
		if (cout_other > this.getCout()) {
			return -1;
		}
		if (cout_other == this.getCout()) {
			return 0;
		}
		else {
			return 1;
		}
	}
}
