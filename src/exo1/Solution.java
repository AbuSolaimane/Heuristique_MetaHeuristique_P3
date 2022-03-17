package exo1;

import java.util.Arrays;

public class Solution implements Comparable<Solution> {
	
	private int[] chemin;
	private int fonctionObjectif;
	
	
	public int[] getChemin() {
		return chemin;
	}
	public void setChemin(int[] chemin) {
		this.chemin = chemin;
	}
	public int getFonctionObjectif() {
		return fonctionObjectif;
	}
	public void setFonctionObjectif(int fonctionObjectif) {
		this.fonctionObjectif = fonctionObjectif;
	}
	
	public Solution clone() {
		
		Solution solution = new Solution();
		int[] chemin2 = this.chemin.clone();
		solution.setChemin(chemin2);
		solution.setFonctionObjectif(this.fonctionObjectif);
		return solution;
		
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(chemin);
		result = prime * result + fonctionObjectif;
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
		if (!Arrays.equals(chemin, other.chemin))
			return false;
		if (fonctionObjectif != other.fonctionObjectif)
			return false;
		return true;
	}
	@Override
	public int compareTo(Solution o) {
		
		return o.fonctionObjectif - this.fonctionObjectif;
	}

}
