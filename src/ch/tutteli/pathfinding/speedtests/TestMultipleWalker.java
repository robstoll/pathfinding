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
import ch.tutteli.pathfinding.IPathFinder;
import ch.tutteli.pathfinding.Tile;
import ch.tutteli.pathfinding.Walker;
import ch.tutteli.pathfinding.World;
import ch.tutteli.pathfinding.examples.GreatMap;
import ch.tutteli.pathfinding.examples.PathFinderFactory;
import ch.tutteli.pathfinding.utils.ImageHelper;
import ch.tutteli.pathfinding.utils.WorldHelper;
import ch.tutteli.pathfinding.view.WorldView;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class TestMultipleWalker
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        int worldWidth = 87;
        int worldHeight = 100;
         Toolkit toolkit =  Toolkit.getDefaultToolkit ();
        Dimension dim = toolkit.getScreenSize();
        int pixelFactor = (dim.height-50) / worldHeight;

        BufferedImage image = new BufferedImage(worldWidth * pixelFactor, worldHeight * pixelFactor, BufferedImage.TYPE_INT_RGB);
        World world = new World(ActualWorld.getInstance(), worldWidth, worldHeight);
        GreatMap.setWalls(ActualWorld.getInstance());
        WorldView worldView = WorldHelper.setupWorldView(world, image, pixelFactor);
        worldView.setVisible(true);
        CountDownLatch startSignal = new CountDownLatch(1);
        for (int i = 1; i <= 60; ++i) {
            
                new Thread(new Tester(startSignal, worldView, image)).start();
            
        }
        Thread.sleep(2000);
        startSignal.countDown();
    }

    private static class Tester implements Runnable
    {

        CountDownLatch startSignal;
        SpeedTestHelper helper;
        BufferedImage image;
        WorldView worldView;

        Tester(CountDownLatch theStartSignal, WorldView aWorldView, BufferedImage bufferedImage) {
            startSignal = theStartSignal;

            worldView = aWorldView;
            image = bufferedImage;

        }

        @Override
        public void run() {
            try {
                startSignal.await();
                int worldWidth = 87;
                int worldHeight = 100;
                int pixelFactor = 10;
                World world = new World(ActualWorld.getInstance(), worldWidth, worldHeight);
                
                Tile startTile = WorldHelper.getRandomTile(world, null);
                Tile endTile = WorldHelper.getRandomTile(world, startTile);
                
                //draw the start and end point to the buffer image
                ImageHelper.setPoint(image, startTile.getPosX(), startTile.getPosY(), pixelFactor, Color.YELLOW);
                ImageHelper.setPoint(image, endTile.getPosX(), endTile.getPosY(), pixelFactor, Color.GREEN);
                worldView.repaint();
                
                IPathFinder pathFinder = PathFinderFactory.create(world);
                Walker walker = new Walker(world, pathFinder, worldView, pixelFactor);
                walker.useSingleColourLine();
                walker.useRandomStartColour();
                walker.walkSilent(startTile, endTile, 20);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            } finally {
                
            }

        }
    }
}
