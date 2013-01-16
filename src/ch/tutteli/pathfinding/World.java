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
public class World
{

    private Tile[][] tiles;
    private Action[][] actions;
    private ActualWorld actualWorld;

    public World(ActualWorld theActualWorld, int worldWidth, int worldHeight) {
        this(theActualWorld, worldWidth, worldHeight, 1);
    }

    public World(ActualWorld theActualWorld, int worldWidth, int worldHeight, int initialCost) {
        actualWorld = theActualWorld;
        actions = new Action[worldWidth][worldHeight];
        createTiles(worldWidth, worldHeight, initialCost);
    }

    public void resetTiles() {
        int worldWidth = tiles.length;
        int worldHeight = tiles[0].length;

        actions = new Action[worldWidth][worldHeight];

        for (int x = 0; x < worldWidth; ++x) {
            for (int y = 0; y < worldHeight; ++y) {
                tiles[x][y].bestCost = 0;
                tiles[x][y].currentCost = 0;
                tiles[x][y].viaCost = new Cost(0);
                actions[x][y] = null;
            }
        }

    }

    private void createTiles(int width, int height, int initialCost) {
        tiles = new Tile[width][height];
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                tiles[x][y] = new Tile(x, y, initialCost);
            }
        }
    }

    public int getWidth() {
        return tiles.length;
    }

    public int getHeight() {
        return tiles[0].length;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
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
                case GoLeft:
                    transition = new LeftTransition(state);
                    break;
                case GoRight:
                    transition = new RightTransition(state);
                    break;
            }
        }
        return transition;
    }

    public Action getAction(Tile tile) {
        return getAction(tile.getPosX(), tile.getPosY());
    }

    public Action getAction(int x, int y) {
        return actions[x][y];
    }

    public void setAction(Tile tile, Action action) {
        setAction(tile.getPosX(), tile.getPosY(), action);
    }

    public void setAction(int x, int y, Action action) {
        actions[x][y] = action;
    }

    public ActualWorld getActualWorld() {
        return actualWorld;
    }

    public interface ITransition
    {

        Action getAction();

        int getViaCost();

        void setViaCost(int cost);

        int getEnterCost();

        int getActualEnterCost();

        int getReverseEnterCost();

        boolean hasBetterPath();

        Tile getEndTile();

        Tile getStartTile();

        boolean isNotAtTheCorrespondingBorder();

        boolean isTransitFree();

        public void setEnterCost(int actualCost);

        public Action getReverseAction();
    }

    public abstract class ATransition implements ITransition
    {

        protected Tile startTile;
        protected Tile endTile;

        public ATransition(Tile theStartTile, Tile theEndTile) {
            startTile = theStartTile;
            endTile = theEndTile;
        }

        protected abstract int getSpecificEnterCost(Cost cost);

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

        @Override
        public int getActualEnterCost() {
            int x = endTile.getPosX();
            int y = endTile.getPosY();
            Cost cost = World.this.actualWorld.getActualEnterCost(x, y);
            return getSpecificEnterCost(cost != null ? cost : World.this.tiles[x][y].enterCost);
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
        public void setReverseViaCost(int cost) {
            endTile.viaCost.bottom = cost;
        }

        @Override
        public Action getAction() {
            return Action.GoUp;
        }

        @Override
        public Action getReverseAction() {
            return Action.GoDown;
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
        protected int getSpecificEnterCost(Cost cost) {
            return cost.bottom;
        }

        @Override
        public int getReverseEnterCost() {
            return endTile.viaCost.bottom;
        }

        @Override
        public void setEnterCost(int actualCost) {
            endTile.enterCost.bottom = actualCost;
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
        public void setReverseViaCost(int cost) {
            endTile.viaCost.top = cost;
        }

        @Override
        public Action getAction() {
            return Action.GoDown;
        }

        @Override
        public Action getReverseAction() {
            return Action.GoUp;
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
        protected int getSpecificEnterCost(Cost cost) {
            return cost.top;
        }

        @Override
        public int getReverseEnterCost() {
            return endTile.viaCost.top;
        }

        @Override
        public void setEnterCost(int actualCost) {
            endTile.enterCost.top = actualCost;
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
        public void setReverseViaCost(int cost) {
            endTile.viaCost.right = cost;
        }

        @Override
        public Action getAction() {
            return Action.GoLeft;
        }

        @Override
        public Action getReverseAction() {
            return Action.GoRight;
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
        protected int getSpecificEnterCost(Cost cost) {
            return cost.right;
        }

        @Override
        public int getReverseEnterCost() {
            return endTile.viaCost.right;
        }

        @Override
        public void setEnterCost(int actualCost) {
            endTile.enterCost.right = actualCost;
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
        public void setReverseViaCost(int cost) {
            endTile.viaCost.left = cost;
        }

        @Override
        public Action getAction() {
            return Action.GoRight;
        }

        @Override
        public Action getReverseAction() {
            return Action.GoLeft;
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
        protected int getSpecificEnterCost(Cost cost) {
            return cost.left;
        }

        @Override
        public int getReverseEnterCost() {
            return endTile.viaCost.left;
        }

        @Override
        public void setEnterCost(int actualCost) {
            endTile.enterCost.left = actualCost;
        }
    }
}
