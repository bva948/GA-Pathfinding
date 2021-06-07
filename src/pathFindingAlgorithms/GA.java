package pathFindingAlgorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import model.Grid;
import model.Tile;

public class GA extends PathFindingAlgorithm {
	
	
	private final int POP_NUM = 100;
	private final int ITER_MAX = 200;
	private final double SELECTION_RATE = 0.3;
	private final double MUTATION_RATE = 0.005;
	private final float MAX_POINT = 100.0f;


	private int minStep;
	private int cont = 1;
	
	ArrayList<Individual> population = new ArrayList<Individual>();
	ArrayList<Individual> selectedInd = new ArrayList<Individual>();
	
	Individual best;
	
	public Grid grid;
	public boolean[][] wall = new boolean[100][100];
	
	public GA() {
        super();
    }
	
	public void executeGA(int minStep) {

        this.minStep = minStep;
        init();
        //int cont = 1; // continue flag
        for (int i = 0; i < ITER_MAX; i++) {
            //System.out.println("GENERATION: " + i);
            this.selection();
            this.crossover();
            this.mutation(); //System.out.println(minStep);
            if (this.calFitness() == 1) {
            	cont = 0;
            	break;
            }
//            Collections.sort(population);
//            best = population.get(0);
//            for (int j = 0; j < minStep; j++) {
//            	System.out.print(best.genes[j] + " ");
//            }
//            System.out.println();
        }
	}
        
	
//	public ArrayList<Individual> init(Grid grid) {
//		int i = 0;
//
//		ArrayList<Individual> population = new ArrayList<Individual>();
//		for (; i < POP_NUM; i++)
//			population.add(new Individual(grid.getRoot().getX(), grid.getRoot().getY(), grid.getTarget().getX(), grid.getTarget().getY(), grid.getYSize()));
//
//		return population;
//	}
	
	public void init() {
		population.clear();
		for (int i = 0; i < POP_NUM; i++) {
			population.add(new Individual(minStep));
			population.get(i).randomInit();
//			for (int j = 0; j < minStep; j++)
//				System.out.print(population.get(i).genes[j] + " ");
//			System.out.println();
		}
	}
	
	public void selection() {
		Collections.sort(population);

		selectedInd.clear();

		for (int j = 0; j < (SELECTION_RATE * POP_NUM); j++) {
			selectedInd.add(population.get(j));
		}
	}
	
	public void crossover() {
		population.clear();
		Random rand = new Random();
		for (int k = 0; k < POP_NUM / 2 + POP_NUM % 2; k++) {

			int ind1 = (int) (rand.nextDouble() * SELECTION_RATE * POP_NUM); // selected ind1 index
			int ind2; // selected ind2 index
			do {
				ind2 = (int) (rand.nextDouble() * SELECTION_RATE * POP_NUM); // update ind2 if ind1==ind2
			} while (ind2 == ind1);

			int crossoverPoint = rand.nextInt(minStep) + 1;

			population.add(new Individual(minStep));
			population.add(new Individual(minStep));

			for (int j = 0; j < minStep; j++) {
				if (j < crossoverPoint) {
					population.get(2 * k).genes[j] = selectedInd.get(ind1).genes[j];
					population.get(2 * k + 1).genes[j] = selectedInd.get(ind2).genes[j];
				} else {
					population.get(2 * k).genes[j] = selectedInd.get(ind2).genes[j];
					population.get(2 * k + 1).genes[j] = selectedInd.get(ind1).genes[j];
				}
			}
		}
	}
	
	public void mutation() {
		Random rand = new Random();
		int dir;

		for (int i = 0; i < POP_NUM; i++) {
			for (int j = 0; j < minStep; j++) {
				if (rand.nextFloat() < MUTATION_RATE) {
					dir = rand.nextInt(4);
					population.get(i).genes[j] = dir;
				}
			}
		}
	}
	
	public void calFitness(Individual ind) {
		int i, cont;
		int currX = grid.getRoot().getX(), currY = grid.getRoot().getY();
		int rootX = grid.getRoot().getX(), rootY = grid.getRoot().getY();
		int targetX = grid.getTarget().getX(), targetY = grid.getTarget().getY();
		float maxDist, currDist;
		
		i = 0;
		cont = 1; // continue
		while (i < ind.size && cont > 0) {
			switch (ind.genes[i]) {
			case 0: 
				currX--;
				break;
			case 1: 
				currY++;
				break;
			case 2: 
				currX++;
				break;
			case 3:
				currY--;
				break;
			}
			//System.out.println(currX + " " + currY);
			// Found target
			if (currX == targetX && currY == targetY) {
				cont = 0;
			}
			// Hit the wall
			else if (isObstacle(currX, currY)) {
				cont = -1;
			}

			i++;
		}
		//numberOfSuccessfulMove = i - 1;
		//ind.numberOfSuccessfulMove = numberOfSuccessfulMove;
//System.out.println();
		// Found target
		if (cont == 0) {
			ind.fitness = MAX_POINT;
		} else {
			maxDist = Math.abs(rootX - targetX) + Math.abs(rootY - targetY);;
			currDist = Math.abs(currX - targetX) + Math.abs(currY - targetY);
			ind.fitness = (float) (MAX_POINT * (1.0f - (currDist / maxDist)));
		}
	}
	
	public boolean isObstacle(int currX, int currY) {
		if (currX < 0 || currY < 0 || currX >= grid.getXSize() || currY >= grid.getYSize())
			return true;
		if (wall[currX][currY]) return true;
		return false;
	}
	
	public int calFitness() {
		for (Individual ind : population) {
			calFitness(ind);
			if (ind.fitness >= MAX_POINT) {
				best = ind; //System.out.println(ind.fitness);
				return 1;
			}
		}
		return 0;
	}
	
	@Override
    public int runPathfinder(Grid grid, List<Tile> path)
    {
        Tile root = grid.getRoot();
        Tile target = grid.getTarget();
        
        this.grid = grid;
        
        for (int x = 0; x < grid.getXSize(); x++) {
        	for (int y = 0; y < grid.getYSize(); y++) {
        		if (grid.getGrid()[x][y].isWall())
        			this.wall[x][y] = true;
        		else this.wall[x][y] = false;
        	}
        }
        
        path.clear();
        
        int minStep = Math.abs(root.getX() - target.getX()) + Math.abs(root.getY() - target.getY());
        int maxStep = minStep + (grid.getXSize() - 1) * (grid.getYSize() - 1);
       
        int i;
        for (i = minStep; i < maxStep; i++) {
	        executeGA(i);
	        if (cont == 0)
	        	break;
        }
        if (i == maxStep) {
        	System.out.println("Path not found");
        	return 0;
        }
        
        
        int step = 0;
        
    	for (int j = 0; j < i; j++) {
    		System.out.print(best.genes[j] + " ");
    	}
    	System.out.println();
        path.add(root);
        int currX = root.getX(), currY = root.getY();System.out.println(currX + "," +currY);
        int j = 0;
        while ((currX != target.getX() || currY != target.getY()) && (j < i - 1))
        //for (int j = 0; j < cost - 1; j++)
        {
        	switch (best.genes[j]) {
			case 0: 
				currX--;
				break;
			case 1: 
				currY++;
				break;
			case 2: 
				currX++;
				break;
			case 3:
				currY--;
				break;
			}
        	path.add(grid.getGrid()[currX][currY]);
        	System.out.println(currX + "," +currY);
        	j++;
        }
        path.add(target);System.out.println(target.getX() + "," +target.getY());
        step = j + 1;
	    
        System.out.println(step);
        return step;
    }

}
