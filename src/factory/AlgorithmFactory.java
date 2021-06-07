package factory;

import pathFindingAlgorithms.GA;
import pathFindingAlgorithms.PathFindingAlgorithm;

public class AlgorithmFactory
{
    // Pathfinding Factory
    public static PathFindingAlgorithm getPathFindingAlgorithm(PathFindingAlgorithm.Algorithms algorithm)
    {
        switch(algorithm)
        {
            case GA:
            	return new GA();
            default:
                throw new IllegalArgumentException("Pathfinding algorithm not found!");
        }
    }
}
