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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class World
{

    private Tile[][] tiles;
    private Map<String,Cost> actualEnterCosts = new HashMap<>();
    private Action[][] actions;

    public World(Tile[][] theTiles) {
        tiles = theTiles;
        actions = new Action[theTiles.length][theTiles[0].length];
    }
    public int getWidth(){
        return tiles.length;
    }
    public int getHeight(){
        return tiles[0].length;
    }
    
    public Tile[][] getTiles(){
        return tiles;
    }

    public void setActualEnterCost(int x, int y, Cost enterCost) {
        actualEnterCosts.put(x+","+y,enterCost);
    }

    public Cost getActualEnterCost(int x, int y) {
        return actualEnterCosts.containsKey(x+","+y) ? actualEnterCosts.get(x+","+y): tiles[x][y].enterCost;
    }

    public boolean isNotAtTheTopOfTheMap(Tile tile) {
        return tile.getPosY() > 0;
    }

    public Tile getTileAbove(Tile tile) {
        return tiles[tile.getPosX()][tile.getPosY() - 1];
    }

    public boolean isNotAtTheBottomOfTheMap(Tile tile) {
        return tile.getPosY() < tiles[0].length - 1;
    }

    public Tile getTileBelow(Tile tile) {
        return tiles[tile.getPosX()][tile.getPosY() + 1];
    }

    public boolean isNotOnTheLeftSideOfTheMap(Tile tile) {
        return tile.getPosX() > 0;
    }

    public Tile getTileOnTheLeft(Tile tile) {
        return tiles[tile.getPosX() - 1][tile.getPosY()];
    }

    public boolean isNotOnTheRightSideOfTheMap(Tile tile) {
        return tile.getPosX() < tiles.length - 1;
    }

    public Tile getTileOnTheRight(Tile tile) {
        return tiles[tile.getPosX() + 1][tile.getPosY()];
    }

    public ITransition getTransitionAccordingToAction(Tile state) {
        ITransition transition = null;
        Action action = getAction(state);
        if (action != null) {
            switch (action) {
                case GoUp:
                    transition = new TopTransition(state);
                    break;
                case GoDown:
                    transition = new BottomTransition(state);
                    break;
                case GoToTheLeft:
                    transition = new LeftTransition(state);
                    break;
                case GoToTheRight:
                    transition = new RightTransition(state);
                    break;
            }
        }
        return transition;
    }

    public Action getAction(Tile tile) {
        return getAction(tile.getPosX(),tile.getPosY());
    }
    public Action getAction(int x, int y) {
        return actions[x][y];
    }
    public void setAction(Tile tile, Action action){
        setAction(tile.getPosX(),tile.getPosY(),action);
    }
    public void setAction(int x, int y, Action action) {
        actions[x][y] = action;
    }

    public interface ITransition
    {

        Action getAction();

        int getViaCost();

        void setViaCost(int cost);

        int getEnterCost();

        int getActualEnterCost();

        int getEndTileReverseCost();

        boolean hasBetterPath();

        Tile getEndTile();

        Tile getStartTile();

        boolean isNotAtTheCorrespondingBorder();

        boolean isTransitFree();
    }

    public abstract class ATransition implements ITransition
    {

        protected Tile startTile;
        protected Tile endTile;

        public ATransition(Tile theStartTile, Tile theEndTile) {
            startTile = theStartTile;
            endTile = theEndTile;
        }

        @Override
        public boolean hasBetterPath() {
            if (isNotAtTheCorrespondingBorder()) {
                return isTransitFree() && getViaCost() < startTile.currentCost && endTile.currentCost <= startTile.bestCost;
            }
            return false;
        }

        @Override
        public Tile getStartTile() {
            return startTile;
        }

        @Override
        public Tile getEndTile() {
            return endTile;
        }
    }

    public class TopTransition extends ATransition implements ITransition
    {

        public TopTransition(Tile startTile) {
            super(startTile, isNotAtTheTopOfTheMap(startTile) ? getTileAbove(startTile) : null);
        }

        @Override
        public int getViaCost() {
            return startTile.viaCost.top;
        }

        @Override
        public void setViaCost(int cost) {
            startTile.viaCost.top = cost;
        }

        @Override
        public Action getAction() {
            return Action.GoUp;
        }

        @Override
        public boolean isNotAtTheCorrespondingBorder() {
            return isNotAtTheTopOfTheMap(startTile);
        }

        @Override
        public boolean isTransitFree() {
            return endTile.canBeEnteredFromTheBottom();
        }

        @Override
        public int getEnterCost() {
            return endTile.enterCost.bottom;
        }

        @Override
        public int getActualEnterCost() {
            return World.this.getActualEnterCost(endTile.getPosX(), endTile.getPosY()).bottom;
        }

        @Override
        public int getEndTileReverseCost() {
            return endTile.viaCost.bottom;
        }
    }

    public class BottomTransition extends ATransition implements ITransition
    {

        public BottomTransition(Tile startState) {
            super(startState, isNotAtTheBottomOfTheMap(startState) ? getTileBelow(startState) : null);
        }

        @Override
        public int getViaCost() {
            return startTile.viaCost.bottom;
        }

        @Override
        public void setViaCost(int cost) {
            startTile.viaCost.bottom = cost;
        }

        @Override
        public Action getAction() {
            return Action.GoDown;
        }

        @Override
        public boolean isNotAtTheCorrespondingBorder() {
            return isNotAtTheBottomOfTheMap(startTile);
        }

        @Override
        public boolean isTransitFree() {
            return endTile.canBeEnteredFromTheTop();
        }

        @Override
        public int getEnterCost() {
            return endTile.enterCost.top;
        }

        @Override
        public int getActualEnterCost() {
            return World.this.getActualEnterCost(endTile.getPosX(), endTile.getPosY()).top;
        }

        @Override
        public int getEndTileReverseCost() {
            return endTile.viaCost.top;
        }
    }

    public class LeftTransition extends ATransition implements ITransition
    {

        public LeftTransition(Tile startState) {
            super(startState, isNotOnTheLeftSideOfTheMap(startState) ? getTileOnTheLeft(startState) : null);
        }

        @Override
        public int getViaCost() {
            return startTile.viaCost.left;
        }

        @Override
        public void setViaCost(int cost) {
            startTile.viaCost.left = cost;
        }

        @Override
        public Action getAction() {
            return Action.GoToTheLeft;
        }

        @Override
        public boolean isNotAtTheCorrespondingBorder() {
            return isNotOnTheLeftSideOfTheMap(startTile);
        }

        @Override
        public boolean isTransitFree() {
            return endTile.canBeEnteredFromTheRight();
        }

        @Override
        public int getEnterCost() {
            return endTile.enterCost.right;
        }

        @Override
        public int getActualEnterCost() {
            return World.this.getActualEnterCost(endTile.getPosX(), endTile.getPosY()).right;
        }

        @Override
        public int getEndTileReverseCost() {
            return endTile.viaCost.right;
        }
    }

    public class RightTransition extends ATransition implements ITransition
    {

        public RightTransition(Tile startState) {
            super(startState, isNotOnTheRightSideOfTheMap(startState) ? getTileOnTheRight(startState) : null);
        }

        @Override
        public int getViaCost() {
            return startTile.viaCost.right;
        }

        @Override
        public void setViaCost(int cost) {
            startTile.viaCost.right = cost;
        }

        @Override
        public Action getAction() {
            return Action.GoToTheRight;
        }

        @Override
        public boolean isNotAtTheCorrespondingBorder() {
            return isNotOnTheRightSideOfTheMap(startTile);
        }

        @Override
        public boolean isTransitFree() {
            return endTile.canBeEnteredFromTheLeft();
        }

        @Override
        public int getEnterCost() {
            return endTile.enterCost.left;
        }

        @Override
        public int getActualEnterCost() {
            return World.this.getActualEnterCost(endTile.getPosX(), endTile.getPosY()).left;
        }

        @Override
        public int getEndTileReverseCost() {
            return endTile.viaCost.left;
        }
    }
}
