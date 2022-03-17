package exo2;

import java.util.ArrayList;

public class Solution implements Comparable<Solution> {

	private ArrayList<Boolean> instance;
	private int valeur;

	public Solution(ArrayList<Boolean> instance, int valeur) {
		super();
		this.instance = instance;
		this.valeur = valeur;
	}

	public Solution() {
		super();
		
	}

	public ArrayList<Boolean> getInstance() {
		return instance;
	}

	public void setInstances(ArrayList<Boolean> instance) {
		this.instance = instance;
	}

	public int getValeur() {
		return valeur;
	}

	public void setValeur(int valeur) {
		this.valeur = valeur;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((instance == null) ? 0 : instance.hashCode());
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
		Solution other = (Solution) obj;
		if (instance == null) {
			if (other.instance != null)
				return false;
		} else if (!instance.equals(other.instance))
			return false;
		if (valeur != other.valeur)
			return false;
		return true;
	}

	public Solution clone() {
		ArrayList<Boolean> instan = (ArrayList<Boolean>) this.instance.clone();
		int valeur2 = this.valeur + 0 ;
		return new Solution(instan, valeur2);
	}

	@Override
	public int compareTo(Solution o) {
		return this.valeur - o.valeur;
	}

	
}
