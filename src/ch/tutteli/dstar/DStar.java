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
package ch.tutteli.dstar;

import ch.tutteli.dstar.utils.IntegerHelper;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class DStar implements IPathFinder
{

    private PriorityQueue<Tile> queuedTiles = new PriorityQueue<>();
    private Set<Tile> visitedTiles = new HashSet<>();
    private Tile start;
    private Tile goal;
    private Tile currentTile;
    private World world;

    public DStar(World aWorld) {
        world = aWorld;
    }

    @Override
    public void calculatePath(Tile theStart, Tile theGoal) {
        start = theStart;
        goal = theGoal;
        dijkstraBackward();
    }

    private void dijkstraBackward() {
        addToQueue(goal);
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
                currentTile.currentCost = transition.getViaCost();
                world.setAction(x, y, transition.getAction());
            }
        }
    }

    private void calculateNeighbours() {
        if (world.isNotAtTheTopOfTheMap(currentTile)) {
            calculateTransition(world.new BottomTransition(world.getTileAbove(currentTile)));
        }
        if (world.isNotAtTheBottomOfTheMap(currentTile)) {
            calculateTransition(world.new TopTransition(world.getTileBelow(currentTile)));
        }
        if (world.isNotOnTheLeftSideOfTheMap(currentTile)) {
            calculateTransition(world.new RightTransition(world.getTileOnTheLeft(currentTile)));
        }
        if (world.isNotOnTheRightSideOfTheMap(currentTile)) {
            calculateTransition(world.new LeftTransition(world.getTileOnTheRight(currentTile)));
        }
    }

    @Override
    public void recalculatePath(Tile currentStart) {
        this.start = currentStart;
        addToQueue(currentStart);
        while (thereIsABetterPathInQueue()) {
            stentzsAlgorithm();
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

    private void addToQueue(Tile tile) {
        if (queuedTiles.contains(tile)) {
            queuedTiles.remove(tile);
        }
        queuedTiles.add(tile);
    }

    private void calculateTransition(World.ITransition transition) {
        if (transition.isNotAtTheCorrespondingBorder() && transition.isTransitFree()) {
            Tile startTile = transition.getStartTile();
            Tile endTile = transition.getEndTile();
            transition.setViaCost(IntegerHelper.plusWithoutOverflow(transition.getEnterCost(), endTile.currentCost));

            if (!visitedTiles.contains(startTile)) {
                startTile.currentCost = transition.getViaCost();
                startTile.bestCost = startTile.currentCost;
                world.setAction(startTile, transition.getAction());
                addToQueue(startTile);
            }
            if (isActionPointToEndTile(transition) && startTile.currentCost != transition.getViaCost()) {
                startTile.currentCost = transition.getViaCost();
                addToQueue(startTile);
            }
            if (isItBetterToTakeVia(transition)) {
                takeViaInsteadOfCurrentAction(transition);
            }

            if (visitedTiles.contains(startTile) && !queuedTiles.contains(startTile) && !isActionPointToEndTile(transition) && transition.getEndTileReverseCost() < endTile.currentCost && endTile.currentCost > endTile.bestCost) {
                addToQueue(startTile);
            }
        }
    }

    private boolean isItBetterToTakeVia(World.ITransition transition) {
        return !isActionPointToEndTile(transition) && transition.getViaCost() < transition.getStartTile().currentCost;
    }

    private boolean isActionPointToEndTile(World.ITransition transition) {
        return world.getAction(transition.getStartTile()) == transition.getAction();
    }

    private void takeViaInsteadOfCurrentAction(World.ITransition transition) {
        Tile startTile = transition.getStartTile();
        Tile endTile = transition.getEndTile();

        if (endTile.currentCost == endTile.bestCost) {
            world.setAction(startTile, transition.getAction());
            startTile.currentCost = transition.getViaCost();
            startTile.bestCost = transition.getViaCost();
            addToQueue(startTile);
        } else {
            endTile.bestCost = endTile.currentCost;
            addToQueue(endTile);
        }
    }
}
