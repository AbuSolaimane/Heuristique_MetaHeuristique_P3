package exo2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SacDos {

	private ArrayList<objet> objets;
	private int capacite;
	
	public SacDos(ArrayList<objet> objets, int capacite) {
		super();
		this.objets = objets;
		this.capacite = capacite;
	}
	
	public Solution glouton1() {
		int size = objets.size();
		ArrayList<Boolean> instance = new ArrayList<Boolean>(Arrays.asList(new Boolean[size]));
		Collections.fill(instance, Boolean.FALSE);
		int valeur = 0;
		
		ArrayList<objet> copy_objets = (ArrayList<objet>) objets.clone();
		Collections.sort(copy_objets, Collections.reverseOrder());
		
		int i = 0;
		int capaciteDisponible = this.capacite;
		
		while (i<size && capaciteDisponible>0) {
			if(capaciteDisponible >= copy_objets.get(i).getPoids()) {
				
				valeur += copy_objets.get(i).getValeur();
				int v = objets.indexOf(copy_objets.get(i));
				instance.set(v, true);
				capaciteDisponible -= copy_objets.get(i).getPoids();
			}
			i++;
		}

		Solution solution = new Solution(instance, valeur);
		return solution;
	}
	
	public Solution hamming(Solution s, int i) {
		Solution solution = s;
		ArrayList<Boolean> nvInstance = solution.getInstance();
		int nvValeur = solution.getValeur();
		
		if(nvInstance.get(i)==true) {
			nvInstance.set(i,false);
			nvValeur -= this.objets.get(i).getValeur();
			}
		
		else if(nvInstance.get(i)==false && capacite > this.poidTotal(solution.getInstance()) + objets.get(i).getPoids()) {
			nvInstance.set(i,true);
			nvValeur += this.objets.get(i).getValeur();
			}
		Solution nvSolution = new Solution();
		nvSolution.setInstances(nvInstance);
		nvSolution.setValeur(nvValeur);
		return nvSolution;
	}
	
	public Solution hamming2(Solution s, int i, int j) {
		Solution s1 = this.hamming(s, i);
		Solution s2 = this.hamming(s1, j);
		return s2;
	}
	
	public Solution hamming3(Solution s, int i, int j, int k) {
		Solution s1 = this.hamming(s, i);
		Solution s2 = this.hamming(s1, j);
		Solution s3 = this.hamming(s2, k);
		return s3;
	}
	
	public Solution descente(Solution s0) {
		Solution sol = s0;
		Set<Solution> listesVoisins;
		Solution VoisinMax;
		int initVal = sol.getValeur();
		int ValVoisinMax;
		
		while (true) {
			listesVoisins = this.generatesVosins(sol);
			VoisinMax = getVoisinAvecValMax(listesVoisins);
			ValVoisinMax = VoisinMax.getValeur();
            if (initVal<ValVoisinMax) {
                sol = VoisinMax;
                initVal = ValVoisinMax;
            } else {
                return sol;
            }
		}
	}
	
	private Solution getVoisinAvecValMax(Set<Solution> listesVoisins) {
		Solution solMax = new Solution();
		int max = Integer.MIN_VALUE;
		for (Solution s: listesVoisins) {
			if(s.getValeur() > max) {
				max = s.getValeur();
				solMax = s.clone();
			}
			
		}
		
		return solMax;
	}

	private Set<Solution> generatesVosins(Solution s) {
		Solution sol = s;
		Set<Solution> solutions = new HashSet<Solution>();
		for (int i = 0; i < objets.size(); i++) {
			solutions.add(hamming(sol, i));
		}
		return solutions;
	}
	
	public int getCapacite() {
		return capacite;
	}

	public void setCapacite(int capacite) {
		this.capacite = capacite;
	}
	
	public int poidTotal(ArrayList<Boolean> Instance) {
		int poids = 0;
		int i = 0;
		for (Boolean boolean1 : Instance) {
			if(boolean1)
				poids += objets.get(i).getPoids();
			i++;
		}
		return poids;
	}
	
	public Solution Tabou(Solution initialSolution) {
		Solution sol = initialSolution.clone(); 	//sommet initial
		Set<Solution> voisins;	//list des voisins generer
        Solution voisinAvecValMax;	//le voisin optimom
        Set<Solution> tabou=new HashSet<Solution>();	//Initialisation de la liste tabou T
        tabou.add(sol);
        int i=0;	//nombre d'iteration
        while (i++<1000) {
            voisins = this.generatesVosins(sol);	 //generation du voisin de l'indice j
            voisins = this.voisinNonVisites(voisins, tabou); //generation des voinsins qui sont different de la liste Tabou
            voisinAvecValMax = this.getVoisinAvecValMax(voisins);	//le meilleur voisin
            tabou.add(voisinAvecValMax);
            sol = voisinAvecValMax;
        }
        return this.getVoisinAvecValMax(tabou);
    }
	
	private Set<Solution> voisinNonVisites(Set<Solution> voisins, Set<Solution> tabou) {
		Set<Solution> voisinNonVisites = new HashSet<Solution>();
		for (Solution solution : voisins) {
			if(!tabou.contains(solution))
				voisinNonVisites.add(solution);
		}
		return voisinNonVisites;
	}

public Solution VNS(Solution initialSolution) {
		
		Solution s = initialSolution.clone();  // solution initiale genere
		Solution voisinAlea;  //le voisin aleatoire
		Solution vosinDescente; //Descente du voisin aleatoire
		int i=0;	//nombre d'iteration
        while (i++<1000) {
					int k = 0;
					while(k++ < 3) {
						voisinAlea = generateVoisinHammingK(s, k); //Generer une solution aleatoire voisinAlea a partir du voisinage Nk(s)
						vosinDescente=  this.descente(voisinAlea);
						if  (vosinDescente.getValeur() > s.getValeur()) {
							s = vosinDescente;
							k = 0;
						}
						else
							k = k + 1;
					}
        }
		return s;
	}
	
private Solution generateVoisinHammingK(Solution s, int k) {
	Set<Solution> listesVoisins = new HashSet<Solution>();
		if(k==0) {
			for (int i = 0; i < objets.size(); i++) {
				listesVoisins.add(this.hamming(s, i));
			}
		}
		else if(k==1) {
			for (int i = 0; i < objets.size() - 1; i++) {
				for (int j = i + 1; j < objets.size(); j++) {
					listesVoisins.add(this.hamming2(s, i, j));
				}
			}
		}
		else {
			for (int i = 0; i < objets.size() - 2; i++) {
				for (int j = i + 1; j < objets.size() - 1; j++) {
					for (int j2 = 0; j2 < objets.size(); j2++) {
						listesVoisins.add(this.hamming3(s, i, j, j2));
					}
				}
			}
		}
		return this.getVoisinAleatoire(listesVoisins);
}

private Solution getVoisinAleatoire(Set<Solution> listesVoisins) {
	int size = listesVoisins.size();
	Random rand = new Random();
	int i = rand.nextInt(size);
	int j = 0;
	for (Solution solution : listesVoisins) {
		if(i==j) 
			return solution;
		j++;
	}
	return null;
}

public Solution genetique(Solution initialSolution) {
		
		Set<Solution> population = this.generatesVosins(initialSolution); //Generation de la population initial
		Set<Solution> populationSelectedCroisement; //population pour le croisement 
		Set<Solution> populationCroisement;
		Set<Solution> populationSelectedMutation;
		Set<Solution> populationMutation;
		int i = 0;
		while (i++ < 1000) {
			populationSelectedCroisement = this.SelectCroisement(population);	//Selection de la population pour le croisement 
			populationCroisement = this.GenereteCroisement(populationSelectedCroisement); //Generation des enfants par croisement
			populationSelectedMutation = this.SelectMutation(population); //Selection de la population pour la mutation 
			populationMutation = this.GenereteMutation(populationSelectedMutation );  //Generation des enfants par mutation
			population = this.SelectPopulation(population, populationCroisement, populationMutation);
		}
		
		return initialSolution;
		
	}

private Set<Solution> SelectPopulation(Set<Solution> population, Set<Solution> populationCroisement,
		Set<Solution> populationMutation) {

		Set<Solution> nvPopulation = new HashSet<Solution>();
		List<Solution> sortedList = new ArrayList<Solution>(population);
		sortedList.addAll(populationCroisement);
		sortedList.addAll(populationMutation);
		Collections.sort(sortedList, Collections.reverseOrder());
		for (int i = 0; i < population.size(); i++) {
			nvPopulation.add(sortedList.get(i));
		}
		return nvPopulation;
}

private Set<Solution> GenereteMutation(Set<Solution> populationSelectedMutation) {
	Set<Solution> populationApresMutation = new HashSet<Solution>();
	for (Solution solution : populationSelectedMutation) {
		populationApresMutation.add(this.hamming(solution, new Random().nextInt(objets.size())));
	}
	
	return populationApresMutation;
}

private Set<Solution> SelectMutation(Set<Solution> population) {
	return this.SelectCroisement(population);
}

private Set<Solution> GenereteCroisement(Set<Solution> populationSelectedCroisement) {
	Set<Solution> populationCroisement = new HashSet<Solution>();
	int n = populationSelectedCroisement.size();
	Solution[] populationSelectedCroisementCopy = new Solution[n];
	int j = 0;
	
	for (Solution solution : populationSelectedCroisement) {
		populationSelectedCroisementCopy[j] = solution;
		j++;
	}
	
	for (int i = 0; i < n - 1; i++) {
		Set<Solution> deuxEnfants = this.croiser(populationSelectedCroisementCopy[i],
																												populationSelectedCroisementCopy[i+1]);
		populationCroisement.addAll(deuxEnfants);
	}
	return populationCroisement;
}

private Set<Solution> croiser(Solution solution, Solution solution2) {
	
	Set<Solution> deuxEnfants = new HashSet<Solution>();
	Random rand = new Random();
	int pointHybridation = rand.nextInt(objets.size());
	
	ArrayList<Boolean> instance1 = new ArrayList<>();
	ArrayList<Boolean> instance2 = new ArrayList<>();
	
	for (int i = 0; i <= pointHybridation; i++) {
		instance1.set(i, solution.getInstance().get(i));
		instance2.set(i, solution2.getInstance().get(i));
	}
	
	int a = pointHybridation + 1;
	
	for(int i = a; i < objets.size(); i++ ) {
		if(!solution.getInstance().get(i))
			instance2.set(i, solution.getInstance().get(i));
		
		else if(capacite > this.poidTotal(instance2) + objets.get(i).getPoids())
			instance2.set(i, solution.getInstance().get(i));
		
		else
			instance2.set(i, false);
		
		if(!solution2.getInstance().get(i))
			instance1.set(i, solution2.getInstance().get(i));
		
		else if(capacite > this.poidTotal(instance1) + objets.get(i).getPoids())
			instance1.set(i, solution2.getInstance().get(i));
		
		else
			instance1.set(i, false);
	}
	
	Solution enfant1 = new Solution();
	enfant1.setInstances(instance1);
	enfant1.setValeur(this.valeurTotalInstance(instance1));
	Solution enfant2 = new Solution();
	enfant2.setInstances(instance2);
	enfant2.setValeur(this.valeurTotalInstance(instance2));
	deuxEnfants.add(enfant1);
	deuxEnfants.add(enfant2);
	
	return deuxEnfants;
}

private int valeurTotalInstance(ArrayList<Boolean> instance1) {
	int valeurTotal = 0;
	int i = 0;
	for (Boolean boolean1 : instance1) {
		if(boolean1)
			valeurTotal += objets.get(i).getValeur();
		i++;
	}
	return valeurTotal;
}

private Set<Solution> SelectCroisement(Set<Solution> population) {
	int n = population.size();
	List<Solution> populationCopy = new ArrayList<Solution>(population);
	Collections.sort(populationCopy, Collections.reverseOrder());
	Set<Solution> popSeleced = new HashSet<Solution>();
	for (int i = 0; i < n/2; i++) {
		popSeleced.add(populationCopy.get(i));
	}
	return popSeleced;
}
}
