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

import java.util.List;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class WorldPrinter
{
    private World world;
    
    public WorldPrinter(World aWorld){
        world = aWorld;
    }
    
    public void print(Tile currentTile) {
        String secondLine = "";
        String thirdLine = "";
        Tile[][] tiles = world.getTiles();
        
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
                System.out.print((tile == currentTile ? "x " : "  ") + getCostForPrint(tile.viaCost.top) + "  |");
                secondLine += getCostForPrint(tile.viaCost.left) + " " + getCostForPrint(tile.currentCost) + " " + getCostForPrint(tile.viaCost.right) + "|";
                thirdLine += getCostForPrint(tile.bestCost) + " " + getCostForPrint(tile.viaCost.bottom) + getActionForPrint(tile) + "|";
            }
        }
        System.out.print("\n");
        System.out.println(secondLine);
        System.out.println(thirdLine);
        System.out.println("-----|-----|-----|-----|-----|-----|");
        System.out.println("");
        System.out.println("####################################");
        System.out.println("");
    }
    
    public void printPath(List<Action> actions){
        for(Action action : actions){
            System.out.println(action.getSign());
        }
    }
    
    private String getCostForPrint(int cost) {
        return cost != Integer.MAX_VALUE ? "" + cost : "x";
    }

    private String getActionForPrint(Tile tile) {
        Action action = world.getAction(tile);
        return action != null ? action.getSign() : "  ";
    }
}
