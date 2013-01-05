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
import ch.tutteli.dstar.utils.ImageHelper;
import ch.tutteli.dstar.utils.WorldHelper;
import ch.tutteli.dstar.view.WorldView;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class TestGreatMap
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int worldWidth = 87;
        int worldHeight = 100;
        int pixelFactor = 10;
        Tile[][] tiles = WorldHelper.createTiles(worldWidth, worldHeight);
        World world = new World(tiles);

        setWalls(world);

        BufferedImage image = new BufferedImage(worldWidth * pixelFactor, worldHeight * pixelFactor, BufferedImage.TYPE_INT_RGB);

        Tile startTile = tiles[56][71];// WorldHelper.getRandomTile(world, null);
        Tile endTile = tiles[77][95];// WorldHelper.getRandomTile(world, startTile);

        ImageHelper.setPoint(image, startTile.getPosX(), startTile.getPosY(), pixelFactor, Color.YELLOW);
        ImageHelper.setPoint(image, endTile.getPosX(), endTile.getPosY(), pixelFactor, Color.GREEN);

        WorldView worldView = WorldHelper.setupWorldView(world, image, pixelFactor);
        worldView.setVisible(true);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            
        }
        DStar dstar = new DStar(world);

        Walker walker = new Walker(world, dstar, worldView, pixelFactor);
        walker.walkSilent(startTile, endTile,20);

    }

    private static void setWalls(World world) {

        WorldHelper.horizontalWall(world, 5, 59, 78);
        WorldHelper.horizontalWall(world, 7, 3, 23);
        WorldHelper.horizontalWall(world, 9, 31, 48);
        WorldHelper.horizontalWall(world, 18, 40, 57);
        WorldHelper.horizontalWall(world, 19, 2, 9);
        WorldHelper.horizontalWall(world, 20, 73, 86);
        WorldHelper.horizontalWall(world, 26, 0, 25);
        WorldHelper.horizontalWall(world, 29, 49, 57);
        WorldHelper.horizontalWall(world, 30, 30, 36);
        WorldHelper.horizontalWall(world, 35, 28, 78);
        WorldHelper.horizontalWall(world, 45, 45, 62);
        WorldHelper.horizontalWall(world, 48, 2, 23);
        WorldHelper.horizontalWall(world, 54, 67, 80);
        WorldHelper.horizontalWall(world, 60, 69, 86);
        WorldHelper.horizontalWall(world, 62, 26, 50);
        WorldHelper.horizontalWall(world, 75, 16, 74);
        WorldHelper.horizontalWall(world, 81, 6, 24);
        WorldHelper.horizontalWall(world, 83, 47, 86);
        WorldHelper.horizontalWall(world, 89, 71, 84);
        WorldHelper.horizontalWall(world, 91, 36, 48);
        WorldHelper.horizontalWall(world, 92, 9, 23);

        WorldHelper.verticalWall(world, 5, 54, 64);
        WorldHelper.verticalWall(world, 6, 34, 44);
        WorldHelper.verticalWall(world, 7, 8, 18);
        WorldHelper.verticalWall(world, 11, 0, 4);
        WorldHelper.verticalWall(world, 12, 13, 56);
        WorldHelper.verticalWall(world, 13, 62, 79);
        WorldHelper.verticalWall(world, 15, 86, 91);
        WorldHelper.verticalWall(world, 17, 93, 99);
        WorldHelper.verticalWall(world, 20, 9, 22);
        WorldHelper.verticalWall(world, 35, 76, 92);
        WorldHelper.verticalWall(world, 37, 40, 53);
        WorldHelper.verticalWall(world, 42, 64, 74);
        WorldHelper.verticalWall(world, 44, 92, 99);
        WorldHelper.verticalWall(world, 49, 4, 14);
        WorldHelper.verticalWall(world, 66, 49, 68);
        WorldHelper.verticalWall(world, 68, 13, 34);
        WorldHelper.verticalWall(world, 71, 87, 99);
        WorldHelper.verticalWall(world, 77, 9, 19);

        WorldHelper.block(world, 48, 57, 52, 56);
        WorldHelper.block(world, 27, 37, 12, 15);


    }
}
