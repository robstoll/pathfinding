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
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class GreatMap
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int worldWidth = 87;
        int worldHeight = 100;
        Toolkit toolkit =  Toolkit.getDefaultToolkit ();
        Dimension dim = toolkit.getScreenSize();
        int pixelFactor = (dim.height-50) / worldHeight;
        ActualWorld actualWorld = ActualWorld.getInstance();
        World world = new World(actualWorld, worldWidth, worldHeight);

        setWalls(actualWorld);

        BufferedImage image = new BufferedImage(worldWidth * pixelFactor, worldHeight * pixelFactor, BufferedImage.TYPE_INT_RGB);

        Tile startTile = world.getTile(10, 71);
        Tile endTile = world.getTile(77, 95);


        ImageHelper.setPoint(image, startTile.getPosX(), startTile.getPosY(), pixelFactor, Color.YELLOW);
        ImageHelper.setPoint(image, endTile.getPosX(), endTile.getPosY(), pixelFactor, Color.GREEN);

        WorldView worldView = WorldHelper.setupWorldView(world, image, pixelFactor);
        worldView.setVisible(true);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
        }
        IPathFinder pathFinder = PathFinderFactory.create(world);

        Walker walker = new Walker(world, pathFinder, worldView, pixelFactor);
        walker.walkSilent(startTile, endTile, 20);

    }

    public static void setWalls(ActualWorld actualWorld) {

        WorldHelper.horizontalWall(actualWorld, 5, 59, 78);
        WorldHelper.horizontalWall(actualWorld, 7, 3, 23);
        WorldHelper.horizontalWall(actualWorld, 9, 31, 48);
        WorldHelper.horizontalWall(actualWorld, 18, 40, 57);
        WorldHelper.horizontalWall(actualWorld, 19, 2, 9);
        WorldHelper.horizontalWall(actualWorld, 20, 73, 86);
        WorldHelper.horizontalWall(actualWorld, 26, 0, 25);
        WorldHelper.horizontalWall(actualWorld, 29, 49, 57);
        WorldHelper.horizontalWall(actualWorld, 30, 30, 36);
        WorldHelper.horizontalWall(actualWorld, 35, 28, 78);
        WorldHelper.horizontalWall(actualWorld, 45, 45, 62);
        WorldHelper.horizontalWall(actualWorld, 48, 2, 23);
        WorldHelper.horizontalWall(actualWorld, 54, 67, 80);
        WorldHelper.horizontalWall(actualWorld, 60, 69, 86);
        WorldHelper.horizontalWall(actualWorld, 62, 26, 50);
        WorldHelper.horizontalWall(actualWorld, 75, 16, 74);
        WorldHelper.horizontalWall(actualWorld, 81, 6, 24);
        WorldHelper.horizontalWall(actualWorld, 83, 47, 86);
        WorldHelper.horizontalWall(actualWorld, 89, 71, 84);
        WorldHelper.horizontalWall(actualWorld, 91, 36, 48);
        WorldHelper.horizontalWall(actualWorld, 92, 9, 23);

        WorldHelper.verticalWall(actualWorld, 5, 54, 64);
        WorldHelper.verticalWall(actualWorld, 6, 34, 44);
        WorldHelper.verticalWall(actualWorld, 7, 8, 18);
        WorldHelper.verticalWall(actualWorld, 11, 0, 4);
        WorldHelper.verticalWall(actualWorld, 12, 13, 56);
        WorldHelper.verticalWall(actualWorld, 13, 62, 79);
        WorldHelper.verticalWall(actualWorld, 15, 86, 91);
        WorldHelper.verticalWall(actualWorld, 17, 93, 99);
        WorldHelper.verticalWall(actualWorld, 20, 9, 22);
        WorldHelper.verticalWall(actualWorld, 35, 76, 92);
        WorldHelper.verticalWall(actualWorld, 37, 40, 53);
        WorldHelper.verticalWall(actualWorld, 42, 64, 74);
        WorldHelper.verticalWall(actualWorld, 44, 92, 99);
        WorldHelper.verticalWall(actualWorld, 49, 4, 14);
        WorldHelper.verticalWall(actualWorld, 66, 49, 68);
        WorldHelper.verticalWall(actualWorld, 68, 13, 34);
        WorldHelper.verticalWall(actualWorld, 71, 87, 99);
        WorldHelper.verticalWall(actualWorld, 77, 9, 19);

        WorldHelper.block(actualWorld, 48, 57, 52, 56);
        WorldHelper.block(actualWorld, 27, 37, 12, 15);


    }
}
