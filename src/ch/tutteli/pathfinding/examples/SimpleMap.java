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

import ch.tutteli.pathfinding.AStar;
import ch.tutteli.pathfinding.ActualWorld;
import ch.tutteli.pathfinding.DStar;
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
        int worldWidth = 6;
        int worldHeight = 6;
        int pixelFactor = 100;
        ActualWorld actualWorld = new ActualWorld();
        WorldHelper.setAsObstacle(actualWorld, 3, 2);
        WorldHelper.setAsObstacle(actualWorld, 2, 1);
        WorldHelper.setAsObstacle(actualWorld, 3, 3);
        WorldHelper.setAsObstacle(actualWorld, 2, 4);

        World world = new World(actualWorld,worldWidth,worldHeight);
        
        BufferedImage image = new BufferedImage(worldWidth * pixelFactor, worldHeight * pixelFactor, BufferedImage.TYPE_INT_RGB);

        Tile startTile = world.getTile(0, 2);
        Tile endTile =  world.getTile(5,2);

        ImageHelper.setPoint(image, startTile.getPosX(), startTile.getPosY(), pixelFactor, Color.YELLOW);
        ImageHelper.setPoint(image, endTile.getPosX(), endTile.getPosY(), pixelFactor, Color.GREEN);
        
        WorldView worldView = WorldHelper.setupWorldView(world, image, pixelFactor);
        worldView.setVisible(true);

        IPathFinder pathFinder = PathFinderFactory.create(world);
        Walker walker = new Walker(world, pathFinder, worldView, pixelFactor);
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
        }
        walker.walkSilent(startTile, endTile, 200);
        

    }
}