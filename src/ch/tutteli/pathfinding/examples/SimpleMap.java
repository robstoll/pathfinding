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

import ch.tutteli.pathfinding.ActualWorld;
import ch.tutteli.pathfinding.IPathFinder;
import ch.tutteli.pathfinding.Tile;
import ch.tutteli.pathfinding.Walker;
import ch.tutteli.pathfinding.World;
import ch.tutteli.pathfinding.utils.ImageHelper;
import ch.tutteli.pathfinding.utils.WorldHelper;
import ch.tutteli.pathfinding.view.WorldView;
import java.awt.Color;
import java.awt.image.BufferedImage;

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
        //defining the world with and height
        int worldWidth = 6;
        int worldHeight = 6;
        //is used to scale the image on the gui
        int pixelFactor = 100;
        //define the actual world with the corresponding obstacles
        ActualWorld actualWorld = ActualWorld.getInstance();
        WorldHelper.setAsObstacle(actualWorld, 3, 2);
        WorldHelper.setAsObstacle(actualWorld, 2, 1);
        WorldHelper.setAsObstacle(actualWorld, 3, 3);
        WorldHelper.setAsObstacle(actualWorld, 2, 4);

        //A world is used by every bot. A world of a bot does not necessarily know every actual obstacle
        World world = new World(actualWorld, worldWidth, worldHeight);

        //buffer image which is used later on on the gui to represent the world
        BufferedImage image = new BufferedImage(worldWidth * pixelFactor, worldHeight * pixelFactor, BufferedImage.TYPE_INT_RGB);

        //define start and end point
        Tile startTile = world.getTile(0, 2);
        Tile endTile = world.getTile(5, 2);

        //draw the start and end point to the buffer image
        ImageHelper.setPoint(image, startTile.getPosX(), startTile.getPosY(), pixelFactor, Color.YELLOW);
        ImageHelper.setPoint(image, endTile.getPosX(), endTile.getPosY(), pixelFactor, Color.GREEN);

        //the gui with the world
        WorldView worldView = WorldHelper.setupWorldView("SimpleMap", world, image, pixelFactor);
        worldView.setVisible(true);

        //It is manually set in PathFinderFactory which algorithm will be used - either D* or A*
        IPathFinder pathFinder = PathFinderFactory.create(world);

        //A walker used to walk from start to end
        Walker walker = new Walker(world, pathFinder, worldView, pixelFactor);

        //we wait a short time thus the user can find the start and end point
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
        }

        //The walker will now use the path finding algorithm to determine the shortest path and walk along.
        //If the cost changes it recalculates the path using the same path finding algorithm as before
        walker.walkVerbose(startTile, endTile, 200);


    }
}
