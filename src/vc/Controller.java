package vc;

import pathFindingAlgorithms.PathFindingAlgorithm;
import factory.AlgorithmFactory;
import model.Grid;
import model.Tile;

public class Controller
{
    private final Grid model;
    private final View view;
    
    public Controller(Grid model, View view)
    {
        this.model = model;
        this.view = view;
        this.view.setTriggers(this);
        this.view.createGrid();
        
        this.model.addObserver(view);
    }
    
    public void doClearGrid()
    {
        this.model.clearGrid();
    }
    
    public void doChangeClickType(Tile.Type type)
    {   
        this.model.changeClickType(type);
    }
    
  
//    public void doAddRandomWalls()
//    {
//        this.model.addRandomWalls();
//    }
    
    
    public boolean doShortestPathAlgorithm(PathFindingAlgorithm.Algorithms algorithm) throws InterruptedException
    {
        PathFindingAlgorithm pathFindingAlgorithm = AlgorithmFactory.getPathFindingAlgorithm(algorithm);
        return this.model.executePathfinding(pathFindingAlgorithm);
    }
}
