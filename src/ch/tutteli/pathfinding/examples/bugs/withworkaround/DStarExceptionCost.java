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
package ch.tutteli.pathfinding.examples.bugs.withworkaround;

import ch.tutteli.pathfinding.*;
import ch.tutteli.pathfinding.utils.IntegerHelper;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class DStarExceptionCost extends APathFinder implements IPathFinder
{

    public DStarExceptionCost(World world) {
        super(world);
    }

    @Override
    public void calculatePath(Tile theStart, Tile theGoal) {
        start = theStart;
        goal = theGoal;
        dijkstraBackward();
    }

    private void dijkstraBackward() {
        addToQueue(goal);
        currentTile = null;
        while (currentTile != start) {
            stentzsAlgorithm();
        }

    }

    private void stentzsAlgorithm() {
        currentTile = queuedTiles.poll();
        if (costOfCurrentHasIncreased()) {
            searchForBetterPathViaNeighbours();
        }
        calculateNeighbours();
        visitedTiles.add(currentTile);
    }

    private boolean costOfCurrentHasIncreased() {
        return currentTile.bestCost < currentTile.currentCost;
    }

    private void searchForBetterPathViaNeighbours() {

        World.ITransition[] transitions = new World.ITransition[]{
            world.new TopTransition(currentTile),
            world.new BottomTransition(currentTile),
            world.new LeftTransition(currentTile),
            world.new RightTransition(currentTile)
        };

        int x = currentTile.getPosX();
        int y = currentTile.getPosY();
        for (World.ITransition transition : transitions) {
            if (transition.hasBetterPath()) {
                if (visitedTiles.contains(transition.getEndTile())) {
                    currentTile.currentCost = transition.getViaCost();
                    world.setAction(x, y, transition.getAction());
                } else {
                    throw new DStarCalculationException();
                }
            }
        }
    }

    @Override
    public void recalculatePath(Tile currentStart) {
        start = currentStart;
        addToQueue(currentStart);

        int maxCost = world.getWidth() * world.getHeight() / 10 * 8;
        try {
            while (thereIsABetterPathInQueue()) {
                stentzsAlgorithm();
                // we assume that something is wrong when currentTile.currentCost is higher than 
                // the cost to walk 80% of the tiles
                if (currentTile.currentCost != Integer.MAX_VALUE && currentTile.currentCost > maxCost) {
                    throw new DStarCalculationException();
                }
            }
        } catch (DStarCalculationException e) {
            reset();
            calculatePath(start, goal);
        }

    }

    /**
     * There is a better path, if current cost of start is bigger than a best cost of a tile in queue
     *
     * @return
     */
    private boolean thereIsABetterPathInQueue() {
        boolean betterPathFound = false;
        for (Tile tile : queuedTiles) {
            if (start.currentCost > tile.bestCost) {
                betterPathFound = true;
                break;
            }
        }
        return betterPathFound;
    }

    @Override
    protected void calculateTransition(World.ITransition transition) {
        if (transition.isTransitFree()) {
            Tile neighbour = transition.getStartTile();
            int costToEnterCurrentTile = transition.getEnterCost();
            transition.setViaCost(IntegerHelper.plusWithoutOverflow(costToEnterCurrentTile, currentTile.currentCost));
            int costToGoalViaCurrentTile = transition.getViaCost();
            if (!visitedTiles.contains(neighbour)) {
                neighbour.currentCost = costToGoalViaCurrentTile;
                neighbour.bestCost = neighbour.currentCost;
                world.setAction(neighbour, transition.getAction());
                addToQueue(neighbour);
            }
            if (isNeighbourPointingToCurrentTile(transition) && neighbour.currentCost != costToGoalViaCurrentTile) {
                neighbour.currentCost = costToGoalViaCurrentTile;
                addToQueue(neighbour);
            }
            if (isItBetterToTakeVia(transition)) {
                takeViaInsteadOfCurrentAction(transition);
            }
            int costToEnterNeighbour = transition.getReverseEnterCost();
            if (visitedTiles.contains(neighbour) && !queuedTiles.contains(neighbour) && !isNeighbourPointingToCurrentTile(transition) && costToEnterNeighbour < currentTile.currentCost && currentTile.currentCost > currentTile.bestCost) {
                neighbour.bestCost = neighbour.currentCost;
                addToQueue(neighbour);
            }
        }
    }

    private boolean isItBetterToTakeVia(World.ITransition transition) {
        return !isNeighbourPointingToCurrentTile(transition) && transition.getViaCost() < transition.getStartTile().currentCost;
    }

    private boolean isNeighbourPointingToCurrentTile(World.ITransition transition) {
        return world.getAction(transition.getStartTile()) == transition.getAction();
    }

    private void takeViaInsteadOfCurrentAction(World.ITransition transition) {
        Tile neighbour = transition.getStartTile();

        if (currentTile.currentCost == currentTile.bestCost) {
            world.setAction(neighbour, transition.getAction());
            neighbour.currentCost = transition.getViaCost();
            neighbour.bestCost = neighbour.currentCost;
            addToQueue(neighbour);
        } else {
            currentTile.bestCost = currentTile.currentCost;
            addToQueue(currentTile);
        }
    }

    @Override
    public void reset() {
        world.resetTiles();
        queuedTiles = new PriorityQueue<>();
        visitedTiles = new HashSet<>();
    }
}
