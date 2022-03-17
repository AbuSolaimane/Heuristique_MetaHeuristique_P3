package exo2;

public class objet implements Comparable<objet> {

	private int valeur;
	private int poids;

	public objet(int valeur, int poids) {
		
		super();
		this.valeur = valeur;
		this.poids = poids;
	}

	public int getValeur() {
		return valeur;
	}

	public void setValeur(int valeur) {
		this.valeur = valeur;
	}

	public int getPoids() {
		return poids;
	}

	public void setPoids(int poids) {
		this.poids = poids;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + poids;
		result = prime * result + valeur;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		objet other = (objet) obj;
		if (poids != other.poids)
			return false;
		if (valeur != other.valeur)
			return false;
		return true;
	}

	@Override
	public int compareTo(objet o) {
		double a1 = this.valeur/this.poids;
		double a2 = o.valeur/o.poids;
		if(a1>a2)
			return 1;
		if(a2>a1)
			return -1;
		if(this.valeur>o.valeur)
			return 1;
		if(o.valeur>this.valeur)
			return -1;
		return 0;
	}

	@Override
	public String toString() {
		return "objet (" + valeur + ",  " + poids + ")";
	}

	
	
}
