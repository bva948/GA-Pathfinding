package pathFindingAlgorithms;

import java.util.Random;

public class Individual implements Comparable<Individual>  {
	/**
	 * Size of individual gene
	 */
	public int size;

	/**
	 * Gene permutation
	 */
	public int genes[];

	/**
	 * Current fitness
	 */
	public float fitness;
	
	public Individual(int size) {
		this.size = size;
		genes = new int[size];
	}
	
	public void randomInit() {
		Random rand = new Random();
		int dir;
		for (int i = 0; i < size; i++) {
			dir = rand.nextInt(4);
			//genes.add(choice);
			genes[i] = dir;
		}
	}
	
	@Override
	public int compareTo(Individual object) {
		Individual f = (Individual) object;

		if (fitness == f.fitness) {
			return 0;
		} else if (fitness > f.fitness)
			return -1;
		else
			return 1;
	}
}
