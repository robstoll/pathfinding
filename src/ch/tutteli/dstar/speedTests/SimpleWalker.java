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
package ch.tutteli.dstar.speedTests;

import ch.tutteli.dstar.*;
import ch.tutteli.dstar.utils.IntegerHelper;
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

        Tile tmpTile = start;

        while (tmpTile != goal) {
            List<Action> path = new ArrayList<>();
            while (tmpTile != goal) {
                World.ITransition transition = world.getTransitionAccordingToAction(tmpTile);
                Tile endTile = transition.getEndTile();

                int actualCost = transition.getActualEnterCost();
                if (actualCost > transition.getViaCost()) {

                    transition.setViaCost(IntegerHelper.plusWithoutOverflow(actualCost, endTile.currentCost));
                    tmpTile.currentCost = transition.getViaCost();

                    break;
                } else {
                    path.add(transition.getAction());
                }
                tmpTile = endTile;

            }

            paths.add(path);


            if (tmpTile == goal) {
                break;
            }

            pathFinder.recalculatePath(tmpTile);

        }
    }
}
