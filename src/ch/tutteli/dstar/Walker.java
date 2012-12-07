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
package ch.tutteli.dstar;

import ch.tutteli.dstar.utils.ImageHelper;
import ch.tutteli.dstar.utils.IntegerHelper;
import ch.tutteli.dstar.view.WorldView;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class Walker
{

    private World world;
    private IPathFinder pathFinder;
    private Stack<List<Action>> paths = new Stack<>();
    private WorldView worldView;
    private int pixelFactor;
    private int walkingColorCount = 0;
    private Color[] walkingColors = new Color[]{Color.RED, Color.BLUE, Color.ORANGE, Color.MAGENTA, Color.GRAY, Color.YELLOW};

    public Walker(World theWorld, IPathFinder aPathFinder, WorldView theWorldView, int aPixelFactor) {
        world = theWorld;
        pathFinder = aPathFinder;
        worldView = theWorldView;
        pixelFactor = aPixelFactor;
    }

    public Stack<List<Action>> getPath() {
        return paths;
    }

    public void walkVerbose(Tile start, Tile goal, int sleepInMilliseconds) {
        walk(start, goal, sleepInMilliseconds, true);
    }

    public void walkSilent(Tile start, Tile goal, int sleepInMilliseconds) {
        walk(start, goal, sleepInMilliseconds, false);
    }

    private void walk(Tile start, Tile goal, int sleepInMilliseconds, boolean isVerbose) {
        WorldPrinter printer = new WorldPrinter(world);

        pathFinder.calculatePath(start, goal);

        if (isVerbose) {
            printer.print(start);
        }

        Tile tmpTile = start;
        Color walkingColor = Color.RED;

        while (tmpTile != goal) {
            List<Action> path = new ArrayList<>();
            BufferedImage image = worldView.getImage();
            while (tmpTile != goal) {
                ImageHelper.setPoint(image, tmpTile.getPosX(), tmpTile.getPosY(), pixelFactor, walkingColor);
                worldView.repaint();
                try {
                    Thread.sleep(sleepInMilliseconds);
                } catch (InterruptedException ex) {
                }
                World.ITransition transition = world.getTransitionAccordingToAction(tmpTile);
                Tile endTile = transition.getEndTile();

                int actualCost = transition.getActualEnterCost();
                if (actualCost > transition.getViaCost()) {
                    ImageHelper.setPoint(image, endTile.getPosX(), endTile.getPosY(), pixelFactor, Color.CYAN);
                    transition.setViaCost(IntegerHelper.plusWithoutOverflow(actualCost, endTile.currentCost));
                    tmpTile.currentCost = transition.getViaCost();
                    walkingColor = changeWalkingColor();

                    break;
                } else {
                    path.add(transition.getAction());
                }
                tmpTile = endTile;

            }
            ImageHelper.setPoint(image, tmpTile.getPosX(), tmpTile.getPosY(), pixelFactor, Color.RED);
            paths.add(path);


            if (isVerbose) {
                printer.printPath(path);
                printer.print(tmpTile);
            }
            if (tmpTile == goal) {
                break;
            }

            pathFinder.recalculatePath(tmpTile);

            if (isVerbose) {
                printer.print(tmpTile);
            }
        }
    }

    private Color changeWalkingColor() {
        ++walkingColorCount;
        if (walkingColorCount >= walkingColors.length) {
            walkingColorCount = 0;
        }
        return walkingColors[walkingColorCount];

    }
}
