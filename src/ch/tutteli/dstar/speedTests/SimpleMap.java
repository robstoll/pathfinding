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

import ch.tutteli.dstar.DStar;
import ch.tutteli.dstar.Tile;
import ch.tutteli.dstar.Walker;
import ch.tutteli.dstar.World;
import ch.tutteli.dstar.utils.WorldHelper;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class SimpleMap
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         int worldWidth = 6;
        int worldHeight = 6;
        Tile[][] tiles = WorldHelper.createTiles(worldWidth, worldHeight);
        World world = new World(WorldHelper.createTiles(worldWidth, worldHeight));
        WorldHelper.setAsBlock(world, 3, 2);
        WorldHelper.setAsBlock(world, 2, 1);
        WorldHelper.setAsBlock(world, 3, 3);
        WorldHelper.setAsBlock(world, 2, 4);
        
        DStar dstar = new DStar(tiles, world);
        Walker walker = new Walker(world, dstar);
        walker.walk(tiles[0][2],tiles[5][2]);
        
        
    }
}
