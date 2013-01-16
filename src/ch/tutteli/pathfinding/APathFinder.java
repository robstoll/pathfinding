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

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public abstract class APathFinder implements IPathFinder
{

    protected PriorityQueue<Tile> queuedTiles = new PriorityQueue<>();
    protected Set<Tile> visitedTiles = new HashSet<>();
    protected Tile start;
    protected Tile goal;
    protected Tile currentTile;
    protected World world;

    protected APathFinder(World aWorld) {
        world = aWorld;
    }

    protected void addToQueue(Tile tile) {
        if (!queuedTiles.contains(tile)) {
            queuedTiles.add(tile);
        }
    }

    /**
     * Check if there is a better through the currentTile for the neighbours etc. 
     */
    protected void calculateNeighbours() {
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

    protected abstract void calculateTransition(World.ITransition transition);

}
