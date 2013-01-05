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
package ch.tutteli.dstar.utils;

import ch.tutteli.dstar.Cost;
import ch.tutteli.dstar.Tile;
import ch.tutteli.dstar.World;
import ch.tutteli.dstar.view.WorldView;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class WorldHelper
{

    private WorldHelper() {
    }

    public static void setAsObstacle(World world, int x, int y) {
        world.setActualEnterCost(x, y, new Cost(Integer.MAX_VALUE));
    }

    public static void horizontalWall(World world, int y, int startX, int endX) {
        for (int x = startX; x < endX; ++x) {
            WorldHelper.setAsObstacle(world, x, y);
        }
    }

    public static void verticalWall(World world, int x, int startY, int endY) {
        for (int y = startY; y < endY; ++y) {
            WorldHelper.setAsObstacle(world, x, y);
        }
    }

    public static void block(World world, int startX, int endX, int startY, int endY) {
        //Would be faster, but then I cannot use the getRandomTile method (could be placed in a block
//        horizontalWall(world, startY, startX, endX);
//        horizontalWall(world, endY, startX, endX);
//        verticalWall(world, startX, startY - 1, endY - 1);
//        verticalWall(world, endX, startY - 1, endY - 1);
        for (int x = startX; x < endX; ++x) {
            for (int y = startY; y < endY; ++y) {
                setAsObstacle(world, x, y);
            }
        }
    }

    public static Tile[][] createTiles(int width, int height) {
        return createTiles(width, height, 1);
    }

    public static Tile[][] createTiles(int width, int height, int initialCost) {
        Tile[][] states = new Tile[width][height];
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                states[x][y] = new Tile(x, y);
            }
        }
        return states;
    }

    public static Tile getRandomTile(World world, Tile exceptThisTile) {
        Tile tile = null;
        int width = world.getWidth();
        int height = world.getHeight();
        int expectThisX = exceptThisTile != null ? exceptThisTile.getPosX() : -1;
        int expectThisY = exceptThisTile != null ? exceptThisTile.getPosY() : -1;

        Random random = new Random();
        while (tile == null) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if (world.getActualEnterCost(x, y).top != Integer.MAX_VALUE && (x != expectThisX || y != expectThisY)) {
                tile = world.getTiles()[x][y];
            }
        }
        return tile;
    }

    public static WorldView setupWorldView(World world, BufferedImage image, int pixelFactor) {
        WorldView worldView = new WorldView(image);

        int worldWidth = world.getWidth();
        int worldHeight = world.getHeight();
        worldView.setSize(worldWidth * pixelFactor + 20, worldHeight * pixelFactor + 50);

       
        for (int x = 0; x < worldWidth; ++x) {
            for (int y = 0; y < worldHeight; ++y) {
                if (world.getActualEnterCost(x, y).top == Integer.MAX_VALUE) {
                    ImageHelper.setPoint(image, x, y, pixelFactor, Color.WHITE);
                }
            }
        }
        return worldView;
    }
}
