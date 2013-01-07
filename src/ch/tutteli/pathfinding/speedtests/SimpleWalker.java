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
package ch.tutteli.pathfinding.speedtests;

import ch.tutteli.pathfinding.Action;
import ch.tutteli.pathfinding.IPathFinder;
import ch.tutteli.pathfinding.Tile;
import ch.tutteli.pathfinding.World;
import ch.tutteli.pathfinding.utils.IntegerHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class SimpleWalker
{

    private World world;
    private IPathFinder pathFinder;
    private Stack<List<Action>> paths = new Stack<>();

    public SimpleWalker(World theWorld, IPathFinder aPathFinder) {
        world = theWorld;
        pathFinder = aPathFinder;
    }

    public Stack<List<Action>> getPath() {
        return paths;
    }

    public void walk(Tile start, Tile goal) {
        pathFinder.calculatePath(start, goal);

        //Start from starting point
        Tile tmpTile = start;

        while (tmpTile != goal) {
            //save the different parts of a path in multiple lists
            List<Action> path = new ArrayList<>();
            
            while (tmpTile != goal) {
                //Get the stored transition for the tmpTile
                World.ITransition transition = world.getTransitionAccordingToAction(tmpTile);
                //Determine the endTile (the tile to which we are walking now)
                Tile endTile = transition.getEndTile();
                
                //Determine whether the cost has increased in the meantime. We don't care if the cost has decreased
                int actualCost = transition.getActualEnterCost();
                if (actualCost > transition.getViaCost()) {
                    //If it has increased we update via and enter cost and set the current cost to the new value
                    transition.setViaCost(IntegerHelper.plusWithoutOverflow(actualCost, endTile.currentCost));
                    transition.setEnterCost(actualCost);
                    tmpTile.currentCost = transition.getViaCost();
                    //Then we quite the while loop and recalculate the path
                    break;
                } else {
                    //cost hasn't increased - we add the walked path to the list
                    path.add(transition.getAction());
                }
                tmpTile = endTile;

            }
            //we add the walked part of the whole path to the whole path stack 
            paths.add(path);

            //if we should have reached the goal we quite
            if (tmpTile == goal) {
                break;
            }
            //recalculate the path and than we continue walking
            pathFinder.recalculatePath(tmpTile);

        }
    }
}
