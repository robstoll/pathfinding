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

import ch.tutteli.pathfinding.ActualWorld;
import ch.tutteli.pathfinding.DStar;
import ch.tutteli.pathfinding.IPathFinder;
import ch.tutteli.pathfinding.Tile;
import ch.tutteli.pathfinding.World;
import ch.tutteli.pathfinding.examples.PathFinderFactory;
import ch.tutteli.pathfinding.examples.GreatMap;
import java.io.IOException;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class SpeedTestHelper
{

    public static final int PIXEL_FACTOR = 10;
    private ActualWorld actualWorld;

    public SpeedTestHelper() {
        actualWorld = ActualWorld.getInstance();

        GreatMap.setWalls(actualWorld);
        try {
            System.out.println("Press enter to start.");
            System.in.read();
        } catch (IOException ex) {
        }
        System.out.println("i\ttimeUsed");
    }

    public void run() {
        int worldWidth = 87;
        int worldHeight = 100;
        World world = new World(actualWorld, worldWidth, worldHeight);
        Tile startTile = world.getTile(10, 71);
        Tile endTile = world.getTile(77, 95);
        IPathFinder pathFinder = PathFinderFactory.create(world);
        SimpleWalker walker = new SimpleWalker(world, pathFinder);
        walker.walk(startTile, endTile);
    }
}
