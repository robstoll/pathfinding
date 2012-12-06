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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private Tile[][] tiles;
    private List<Action> path = new ArrayList<>();
    private Tile start;
    private Tile goal;
    private Tile currentTile;
    private World world;

    public DStar(Tile[][] theTiles, World aWorld) {
        tiles = theTiles;
        world = aWorld;
    }

    @Override
    public void calculatePath(Tile theStart, Tile theGoal) {
        start = theStart;
        goal = theGoal;
        dijkstraBackward();
    }

    @Override
    public void recalculatePath(Tile start) {
        addToQueue(start);
        while (thereIsABetterPathInQueue()) {
            stentzsAlgorithm();
        }
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
//        print();
//        System.out.println("-------------------");
    }

    private boolean costOfCurrentHasIncreased() {
        return currentTile.bestCost < currentTile.currentCost;
    }

    private void print() {
        String secondLine = "";
        String thirdLine = "";
        for (int y = 0; y < tiles[0].length; ++y) {
            if (y != 0) {
                System.out.print("\n");
                System.out.println(secondLine);
                System.out.println(thirdLine);
                System.out.println("-----|-----|-----|-----|-----|-----|");
                secondLine = "";
                thirdLine = "";
            }
            for (int x = 0; x < tiles.length; ++x) {
                Tile tile = tiles[x][y];
                System.out.print((tile == currentTile ? "o " : "  ") + getCostForPrint(tile.viaCost.top) + "  |");

                secondLine += getCostForPrint(tile.viaCost.left) + " " + getCostForPrint(tile.currentCost) + " " + getCostForPrint(tile.viaCost.right) + "|";
                thirdLine += getCostForPrint(tile.bestCost) + " " + getCostForPrint(tile.viaCost.bottom) + getActionForPrint(tile) + "|";
            }
        }
        System.out.print("\n");
        System.out.println(secondLine);
        System.out.println(thirdLine);
        System.out.println("-----|-----|-----|-----|-----|-----|");
        System.out.println("");
    }

    private String getCostForPrint(int cost) {
        return cost != Integer.MAX_VALUE ? "" + cost : "x";
    }

    private String getActionForPrint(Tile tile) {
        Action action = world.getAction(tile);
        return action != null ? action.getSign() : "  ";
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
                currentTile.currentCost = transition.getCurrentTransitionCost();
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
//
//    private void printQueue() {
//        for (Tile tile : queuedTiles) {
//            System.out.println("(" + tile.getPosX() + "," + tile.getPosY() + ")->" + tile.currentCost + "{" + tile.bestCost + "}");
//        }
//    }

    public void calculateTransition(World.ITransition transition) {
        if (transition.isNotAtTheCorrespondingBorder() && transition.isTransitFree()) {
            Tile startTile = transition.getStartTile();
            Tile endTile = transition.getEndTile();
            transition.setTransitionCost(IntegerHelper.plusWithoutOverflow(transition.getEnterCost(), endTile.currentCost));

            if (!visitedTiles.contains(startTile)) {
                startTile.currentCost = transition.getCurrentTransitionCost();
                startTile.bestCost = startTile.currentCost;
                world.setAction(startTile, transition.getAction());
                addToQueue(startTile);
            }
            if (isActionPointToEndTile(transition) && startTile.currentCost != transition.getCurrentTransitionCost()) {
                startTile.currentCost = transition.getCurrentTransitionCost();
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
        return !isActionPointToEndTile(transition) && transition.getCurrentTransitionCost() < transition.getStartTile().currentCost;
    }

    private boolean isActionPointToEndTile(World.ITransition transition) {
        return world.getAction(transition.getStartTile()) == transition.getAction();
    }

    private void takeViaInsteadOfCurrentAction(World.ITransition transition) {
        Tile startTile = transition.getEndTile();
        Tile endTile = transition.getEndTile();

        if (endTile.currentCost == endTile.bestCost) {
            world.setAction(startTile, transition.getAction());
            startTile.currentCost = transition.getCurrentTransitionCost();
            startTile.bestCost = transition.getCurrentTransitionCost();
            addToQueue(startTile);
        } else {
            endTile.bestCost = endTile.currentCost;
            addToQueue(endTile);
        }
    }
}
