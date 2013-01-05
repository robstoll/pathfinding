/*
 * Copyright 2012 Robert Stoll <rstoll@tutteli.ch>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package ch.tutteli.pathfinding;

import ch.tutteli.pathfinding.utils.IntegerHelper;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class AStar extends APathFinder implements IPathFinder
{
    public AStar(World world) {
        super(world);
    }

    @Override
    public void calculatePath(Tile theStart, Tile theGoal) {
        start = theGoal;
        goal = theStart;
        calculatePath();
    }

    private void calculatePath() {
        addToQueue(start);
        start.currentCost = 0;
        start.bestCost = start.currentCost + getHeuristicEstimate(start);
        while (!queuedTiles.isEmpty()) {
            currentTile = queuedTiles.poll();
            if (currentTile == goal) {
                break;
            }
            calculateNeighbours();
            visitedTiles.add(currentTile);
        }        
    }

    @Override
    protected void calculateTransition(World.ITransition transition) {
        if (transition.isNotAtTheCorrespondingBorder() && transition.isTransitFree()) {
            Tile startTile = transition.getStartTile();
            Tile endTile = transition.getEndTile();
            transition.setViaCost(IntegerHelper.plusWithoutOverflow(transition.getEnterCost(), endTile.currentCost));

            int newCost = endTile.currentCost + transition.getViaCost();
            if (!((queuedTiles.contains(startTile) || visitedTiles.contains(startTile)) && startTile.currentCost <= newCost)) {
                world.setAction(startTile, transition.getAction());
                startTile.currentCost = endTile.currentCost + transition.getEnterCost();
                startTile.bestCost = getHeuristicEstimate(startTile);
                if (visitedTiles.contains(startTile)) {
                    visitedTiles.remove(startTile);
                }
                if (!queuedTiles.contains(startTile)) {
                    addToQueue(startTile);
                }
            }
        }
    }

    private int getHeuristicEstimate(Tile startTile) {
        return Math.abs(goal.getPosX() - startTile.getPosX()) + Math.abs(goal.getPosY() - startTile.getPosY());
    }

    @Override
    public void recalculatePath(Tile newStart) {
        resetAStar();
        goal = newStart;
        calculatePath();
    }

    private void resetAStar() {
        queuedTiles = new PriorityQueue<>();
        visitedTiles = new HashSet<>();
        Tile[][] tiles = world.getTiles();
        int height = world.getHeight();
        int width = world.getWidth();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                tiles[x][y].currentCost = 0;
                tiles[x][y].bestCost = 0;
                world.setAction(x, y,null);
            }
        }
    }
}
