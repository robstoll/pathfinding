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
import java.util.List;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class Walker
{

    private World world;
    private IPathFinder pathFinder;
    private List<Action> path = new ArrayList<>();

    public Walker(World theWorld, IPathFinder aPathFinder) {
        world = theWorld;
        pathFinder = aPathFinder;
    }

    public List<Action> getPath() {
        return getPath();
    }

    public void walk(Tile start, Tile goal) {
        
        pathFinder.calculatePath(start, goal);
        
        Tile tmpTile = start;
        while (tmpTile != goal) {

            while (tmpTile != goal) {
                World.ITransition transition = world.getTransitionAccordingToAction(tmpTile);
                Tile endTile = transition.getEndTile();

                int actualCost = transition.getActualEnterCost();
                if (actualCost > transition.getCurrentTransitionCost()) {
                    transition.setTransitionCost(IntegerHelper.plusWithoutOverflow(actualCost, endTile.currentCost));
                    tmpTile.currentCost = transition.getCurrentTransitionCost();
                    break;
                } else {
                    path.add(transition.getAction());
                }
                tmpTile = endTile;
            }
            if (tmpTile == goal) {
                break;
            }
            pathFinder.recalculatePath(tmpTile);
        }
    }
}
