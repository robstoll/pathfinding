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
package ch.tutteli.pathfinding.examples.bugs.withworkaround;

import ch.tutteli.pathfinding.ActualWorld;
import ch.tutteli.pathfinding.DStar;
import ch.tutteli.pathfinding.IPathFinder;
import ch.tutteli.pathfinding.Tile;
import ch.tutteli.pathfinding.Walker;
import ch.tutteli.pathfinding.World;
import ch.tutteli.pathfinding.examples.GreatMap;
import ch.tutteli.pathfinding.examples.bugs.*;
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
public class GreatMapDStar
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int worldWidth = 87;
        int worldHeight = 100;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        int pixelFactor = (dim.height - 60) / worldHeight;
        ActualWorld actualWorld = ActualWorld.getInstance();
        World world = new World(actualWorld, worldWidth, worldHeight);

        GreatMap.setWalls(actualWorld);

        BufferedImage image = new BufferedImage(worldWidth * pixelFactor, worldHeight * pixelFactor, BufferedImage.TYPE_INT_RGB);

        Tile startTile = world.getTile(46, 88);
        Tile endTile = world.getTile(16, 80);


        ImageHelper.setPoint(image, startTile.getPosX(), startTile.getPosY(), pixelFactor, Color.YELLOW);
        ImageHelper.setPoint(image, endTile.getPosX(), endTile.getPosY(), pixelFactor, Color.GREEN);

        WorldView worldView = WorldHelper.setupWorldView("Workarounds - GreatMapDStar",world, image, pixelFactor);
        worldView.setVisible(true);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
        }
        IPathFinder pathFinder = new DStar(world);

        Walker walker = new Walker(world, pathFinder, worldView, pixelFactor);
        walker.walkSilent(startTile, endTile, 20);
    }
}
