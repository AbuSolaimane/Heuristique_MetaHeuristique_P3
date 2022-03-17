package exo1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TSP {

	private int nbrVilles;
	private int[][] distances;
	
	public TSP(int nbrVilles, int[][] distances) {
		super();
		this.nbrVilles = nbrVilles;
		this.distances = distances;
	}
	
	public Solution TSPNearestNeighbour(int premierVille) {
		Solution solution = new Solution();
		int n = nbrVilles + 1;
		int[] chemin = new int[n] ;
		initialiser(chemin, n);
		chemin[0] =   premierVille;
		chemin[nbrVilles] = premierVille;
		
		for(int k=1; k<n-1;k++) {
			int[] dis = distances[chemin[k-1] ];
			chemin[k] = min(dis, chemin);
		}
		
		int fonctionObjectif = distanceTravelled(chemin);
		
		solution.setChemin(chemin);
		solution.setFonctionObjectif(fonctionObjectif);
		
		return solution;
	}

	private int distanceTravelled(int[] chemin) {
		int fonctionObjectif = 0;
		int n = chemin.length;
		for(int k=1; k<n;k++) {
			fonctionObjectif += distances[chemin[k-1] ][chemin[k]];
		}
		return fonctionObjectif;
	}

	private void initialiser(int[] chemin, int n) {
		for(int i=0;i<n;i++) {
			chemin[i] = n;
		}
	}

	private int min(int[] dis, int[] chemin) {
		int min = Integer.MAX_VALUE;
		int min_dis = Integer.MAX_VALUE;
		int k = 0;
		for(int i : dis) {
			if(i<min_dis && !Exist(k, chemin)) {
				min_dis = i;
				min = k;
			}
			k++;
		}
		return min;
	}

	private boolean Exist(int k, int[] chemin) {
		for(int i : chemin) {
			if(i == k)
				return true;
		}
		return false;
	}
	
	
	
	public Solution descente(Solution s0) {
		Solution sol = s0.clone();
		Set<Solution> listesVoisins;
		Solution VoisinMin;
		int initCout = sol.getFonctionObjectif();
		int coutVoisinMin;
		
		while (true) {
			listesVoisins = this.generatesVosins(sol);
			VoisinMin = getVoisinAvecCoutMin(listesVoisins);
            coutVoisinMin = VoisinMin.getFonctionObjectif();
            if (coutVoisinMin<initCout) {
                sol = VoisinMin.clone();
                initCout = coutVoisinMin;
            } else {
                return sol;
            }
		}
	}
	
	private Solution getVoisinAvecCoutMin(Set<Solution> listesSolution) {
		Solution solMin = new Solution();
		int min = Integer.MAX_VALUE;
		for (Solution s: listesSolution) {
			if(s.getFonctionObjectif()<min) {
				min = s.getFonctionObjectif();
				solMin = s.clone();
			}
			
		}
		
		return solMin;
	}

	private Set<Solution> generatesVosins(Solution sol) {
		Set<Solution> voisins = new HashSet<Solution>();
		for(int i=0; i < nbrVilles; i++) {
			for (int j = i + 1; j < nbrVilles; j++) {
				voisins.add(this.TwoOpt(sol.clone(), i, j));
			}
		}
		return voisins;
	}
	
	public Solution fourOpt(Solution solution,  int city1,  int city2, int city3, int city4)
    {
		Solution solution1 = TwoOpt(solution, city1, city2);
		Solution solution2 = TwoOpt(solution1, city2, city3);
		Solution solution3 = TwoOpt(solution2, city3, city4);
		return solution3;
    }
	
	public Solution ThreeOpt(Solution solution,  int city1,  int city2, int city3)
    {
		Solution solution1 = TwoOpt(solution, city1, city2);
		Solution solution2 = TwoOpt(solution1, city2, city3);
		return solution2;
    }
	
	public Solution TwoOpt(Solution solution,  int city1,  int city2)
    {
		int [] start = solution.getChemin();
        int route [] = new int[nbrVilles + 1];
        System.arraycopy(start, 0, route, 0, route.length);
        int middle [] = new int[city2 - city1 + 1];
        int counter = 0;
        for (int i = city2; i >= city1; i--)
        {
            middle[counter] = route[i];
            counter++;
        }
        System.arraycopy(middle, 0, route, city1, middle.length);
        if(city1 == 0)  {
        	route [nbrVilles] = route [0];
        }
        int fonctionObjective = this.distanceTravelled(route);
        
        Solution nvSolution = new Solution();
        nvSolution.setChemin(route);
        nvSolution.setFonctionObjectif(fonctionObjective);
        return nvSolution;
    }
	
	public Solution Tabou(Solution initialSolution) {
		Solution sol = initialSolution.clone(); 	//sommet initial
		Set<Solution> voisins;	//list des voisins generer
        Solution voisinAvecCoutMin;	//le voisin optimom
        Set<Solution> tabou=new HashSet<Solution>();	//Initialisation de la liste tabou T
        tabou.add(sol);
        int i=0;	//nombre d'iteration
        while (i++<1000) {
            voisins = this.generatesVosins(sol);	 //generation du voisin de l'indice j
            voisins = this.voisinNonVisites(voisins, tabou); //generation des voinsins qui sont different de la liste Tabou
            voisinAvecCoutMin = this.getVoisinAvecCoutMin(voisins);	//le meilleur voisin
            tabou.add(voisinAvecCoutMin);
            sol = voisinAvecCoutMin;
        }
        return this.getVoisinAvecCoutMin(tabou);
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
					while(k < 3) {
						voisinAlea = generateVoisinK_Opt(s, k); //Generer une solution aleatoire voisinAlea a partir du voisinage Nk(s)
						vosinDescente=  this.descente(voisinAlea);
						if  (vosinDescente.getFonctionObjectif() < s.getFonctionObjectif()) {
							s = vosinDescente;
							k = 0;
						}
						else
							k = k + 1;
					}
        }
		return s;
		
	}
	
	private Solution generateVoisinK_Opt(Solution sol, int k) {
		Set<Solution> voisins = new HashSet<Solution>();
		if(k==0) {
			for(int i=0; i < nbrVilles; i++) {
				for (int j = i + 1; j < nbrVilles; j++) {
					voisins.add(this.TwoOpt(sol.clone(), i, j));
				}
			}
		}
		else if(k==1) {
			for(int i=0; i < nbrVilles - 1; i++) {
				for (int j = i + 1; j < nbrVilles - 1; j++) {
					for(int k1 = j + 1; k1 < nbrVilles; k1++) {
						voisins.add(this.ThreeOpt(sol.clone(), i, j, k1));
					}
				}
			}
		}
		else {
			for(int i=0; i < nbrVilles - 2; i++) {
				for (int j = i + 1; j < nbrVilles - 1; j++) {
					for(int k1 = j + 1; k1 < nbrVilles; k1++) {
						for(int k2 = k1 + 1; k2 < nbrVilles; k2++) {
							voisins.add(this.fourOpt(sol.clone(), i, j, k1, k2));
						}
					}
				}
			}
		}
		return this.getVoisinAleatoire(voisins);
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
		int pointHybridation = rand.nextInt(nbrVilles);
		
		int[] chemin1 = new int[nbrVilles + 1];
		int[] chemin2 = new int[nbrVilles + 1];
		this.initialiser(chemin1, nbrVilles + 1);
		this.initialiser(chemin2, nbrVilles + 1);
		
		for (int i = 0; i <= pointHybridation; i++) {
			chemin1[i] = solution.getChemin()[i];
			chemin2[i] = solution2.getChemin()[i];
		}
		
		int a = pointHybridation + 1;
		int b = pointHybridation + 1;
		
		for(int i = 0; i < nbrVilles; i++ ) {
			if(!this.Exist(solution2.getChemin()[i], chemin1)) {
				chemin1[a] = solution2.getChemin()[i];
				a++;
			}
			if(!this.Exist(solution.getChemin()[i], chemin2)) {
				chemin2[b] = solution.getChemin()[i];
				b++;
			}
		}
		chemin1[nbrVilles] = chemin1[0];
		chemin2[nbrVilles] = chemin1[0];
		
		Solution enfant1 = new Solution();
		enfant1.setChemin(chemin1);
		enfant1.setFonctionObjectif(this.distanceTravelled(chemin1));
		Solution enfant2 = new Solution();
		enfant2.setChemin(chemin2);
		enfant2.setFonctionObjectif(this.distanceTravelled(chemin2));
		deuxEnfants.add(enfant1);
		deuxEnfants.add(enfant2);
		
		return deuxEnfants;
	}

	private Set<Solution> SelectMutation(Set<Solution> population) {
		
		return this.SelectCroisement(population);
	}

	private Set<Solution> GenereteMutation(Set<Solution> populationSelectedMutation) {
		
		Set<Solution> populationApresMutation = new HashSet<Solution>();
		for (Solution solution : populationSelectedMutation) {
			populationApresMutation.add(this.TwoOpt(solution, 0, nbrVilles - 1));
		}
		
		return populationApresMutation;
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
	
}
