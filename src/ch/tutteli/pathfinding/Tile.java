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
package ch.tutteli.pathfinding;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class Tile implements Comparable<Tile>
{
    public int bestCost;
    public int currentCost;
    public Cost viaCost;
    public Cost enterCost;
    private int posX;
    private int posY;

    public Tile(int aPosX, int aPosY) {
        this(aPosX, aPosY, 1);
    }

    public Tile(int aPosX, int aPosY, int initialCost) {
        this(aPosX, aPosY, initialCost, initialCost, initialCost, initialCost);
    }

    public Tile(int aPosX, int aPosY, int enterCostTop, int enterCostBottom, int enterCostLeft, int enterCostRight) {
        posX = aPosX;
        posY = aPosY;
        enterCost = new Cost(enterCostTop, enterCostBottom, enterCostLeft, enterCostRight);
        viaCost = new Cost(0);
    }

    @Override
    public int compareTo(Tile o) {
        return this.bestCost - o.bestCost;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean canBeEnteredFromTheTop() {
        return enterCost.top < Integer.MAX_VALUE;
    }

    public boolean canBeEnteredFromTheBottom() {
        return enterCost.bottom < Integer.MAX_VALUE;
    }

    public boolean canBeEnteredFromTheLeft() {
        return enterCost.left < Integer.MAX_VALUE;
    }

    public boolean canBeEnteredFromTheRight() {
        return enterCost.right < Integer.MAX_VALUE;
    }
}
