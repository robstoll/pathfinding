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
package ch.tutteli.pathfinding.examples;

import ch.tutteli.pathfinding.Action;
import ch.tutteli.pathfinding.Tile;
import ch.tutteli.pathfinding.World;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class DebugWorld
{

    private Tile[][] debugTiles;
    private World world;

    public DebugWorld() {
    }

    public void debug(World theWorld, int startPosX, int startPosY, int endPosX, int endPosY) {
        world = theWorld;
        debugTiles = new Tile[endPosX - startPosX + 1][endPosY - startPosY + 1];
        Tile[][] tiles = world.getTiles();
        for (int i = startPosX; i <= endPosX; ++i) {
            for (int j = startPosY; j <= endPosY; ++j) {
                debugTiles[ i - startPosX][j - startPosY] = tiles[i][j];
            }
        }
        DebugWorldPrinter printer = new DebugWorldPrinter(this);
        printer.print(null);

    }

    public Tile[][] getTiles() {
        return debugTiles;
    }

    public Action getAction(Tile tile) {
        return world.getAction(tile);
    }
}
