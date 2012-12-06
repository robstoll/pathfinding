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
package ch.tutteli.dstar.utils;

import ch.tutteli.dstar.Action;
import ch.tutteli.dstar.Cost;
import ch.tutteli.dstar.Tile;
import ch.tutteli.dstar.World;
import java.util.List;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class WorldHelper
{
    private WorldHelper(){}
    
    public static void setAsBlock(World world, int x, int y) {
        world.setEnterCost(x, y, new Cost(Integer.MAX_VALUE));
    }
     private void printPath(List<Action> path) {
        for (Action action : path) {
            System.out.println(action.getSign());
        }
    }
     public static Tile[][] createTiles(int width, int height){
        return createTiles(width, height,1);
    }
    public static Tile[][] createTiles(int width, int height,int initialCost){
        Tile[][] states = new Tile[width][height];
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                states[x][y] = new Tile(x, y);
            }
        }
        return states;
    }
}
