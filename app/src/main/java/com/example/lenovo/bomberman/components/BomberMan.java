package com.example.lenovo.bomberman.components;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import com.example.lenovo.bomberman.UI.Images;
import com.example.lenovo.bomberman.UI.activity.LoadGameActivity;
import com.example.lenovo.bomberman.UI.activity.MainActivity;
import com.example.lenovo.bomberman.UI.activity.NewGameActivity;
import com.example.lenovo.bomberman.UI.activity.StartActivity;
import com.example.lenovo.bomberman.components.cell.BlockCell;
import com.example.lenovo.bomberman.components.cell.Cell;
import com.example.lenovo.bomberman.components.cell.WallCell;
import com.example.lenovo.bomberman.components.power.PowerChanger;
import com.example.lenovo.bomberman.main.Game;
import com.example.lenovo.bomberman.main.Side;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class BomberMan {
    private Point realLocation;
    private Game game;
    public static int movementSize = Board.cellsSize / 5;
    private Side lastMove = Side.RIGHT_SIDE;
    private Bitmap lastArrayImage[];
    private int lastIndexImage = 0;
    private int crossGap;
    private Cell currentCell;
    public static boolean alive = true;
    private int speed = 40;

    private int bombLimit = 1;
    private boolean canControlBombs = false;
    private int bombRadius = 1;
    private int point;
    private List<Bomb> bombsCanControl = new ArrayList<>();
    private boolean ghostMood = false;


    public BomberMan(Game game) {
        realLocation = new Point(Board.cellsSize, Board.cellsSize);
        this.game = game;
        lastArrayImage = Images.bomberManWR;
        crossGap = Board.cellsSize / 3;
        game.getBoard().setBomberManPlace(realLocation);


        //for restart game
        alive = true;
        speed = 40;
        bombLimit = 1;
        canControlBombs = false;
        bombRadius = 1;
        ghostMood = false;


    }


    private int imageCounter = 0;
    private Side eventToDO = null;

    public void shouldMove(Side e) {
        eventToDO = e;
    }

    public void allowToMove() {

        if (alive && imageCounter % speed == 0 && eventToDO != null) {                                                       //change speed !!!!!!!!!!!!!!!!
            Side e = eventToDO;
            eventToDO = null;                                                          //agar in nabashe mishe modele snake kheili jalebtare!!!!
            int cellsSize = game.getGameFrame().getCellsSize();

            Point oldLocation = realLocation;

            if (e == Side.RIGHT_SIDE) {
                lastArrayImage = Images.bomberManWR;

                if (lastMove == Side.RIGHT_SIDE) {
                    lastIndexImage++;
                } else
                    lastIndexImage = 0;
                lastMove = Side.RIGHT_SIDE;

                if (realLocation.x < (game.getBoard().getHeight() * 2 + 1) * cellsSize) {

                    if (ghostMood) {
                        realLocation = new Point(realLocation.x + movementSize, realLocation.y);
                    } else {

                        if ((realLocation.y - cellsSize) % (2 * cellsSize) <= crossGap) {
                            realLocation = (new Point(realLocation.x + movementSize, realLocation.y - (realLocation.y - cellsSize) % (2 * cellsSize)));
                        } else if ((realLocation.y - cellsSize) % (2 * cellsSize) >= 2 * cellsSize - crossGap) {
                            realLocation = (new Point(realLocation.x + movementSize, realLocation.y - ((realLocation.y - cellsSize) % (2 * cellsSize)) + 2 * cellsSize));
                        }

                        if (game.getBoard().wallInLocation(realLocation.x - movementSize + cellsSize, realLocation.y)) {                   // for walls...........
                            realLocation = oldLocation;
                        }
                    }

                    if (game.getBoard().bombInLocation((realLocation.x - movementSize + cellsSize), realLocation.y)) {                   // for Bombs............
                        realLocation = oldLocation;
                    }

                }

            }
            if (e == Side.LEFT_SIDE) {

                lastArrayImage = Images.bomberManWL;

                if (lastMove == Side.LEFT_SIDE) {
                    lastIndexImage++;
                } else
                    lastIndexImage = 0;
                lastMove = Side.LEFT_SIDE;


                if (realLocation.x > cellsSize) {

                    if (ghostMood) {
                        realLocation = new Point(realLocation.x - movementSize, realLocation.y);
                    } else {

                        if ((realLocation.y - cellsSize) % (2 * cellsSize) <= crossGap) {
                            realLocation = (new Point(realLocation.x - movementSize, realLocation.y - (realLocation.y - cellsSize) % (2 * cellsSize)));
                        } else if ((realLocation.y - cellsSize) % (2 * cellsSize) >= 2 * cellsSize - crossGap) {
                            realLocation = (new Point(realLocation.x - movementSize, realLocation.y - ((realLocation.y - cellsSize) % (2 * cellsSize)) + 2 * cellsSize));
                        }
                        if (game.getBoard().wallInLocation((realLocation.x + movementSize - cellsSize), realLocation.y)) {               //for walls...............
                            realLocation = oldLocation;
                        }
                    }
                    if (game.getBoard().bombInLocation(realLocation.x + movementSize - cellsSize, realLocation.y)) {               //for bombs...............
                        realLocation = oldLocation;
                    }

                }

            }
            if (e == Side.UP_SIDE) {
                lastArrayImage = Images.bomberManWU;
                if (lastMove == Side.UP_SIDE) {
                    lastIndexImage++;
                } else
                    lastIndexImage = 0;
                lastMove = Side.UP_SIDE;

                if (realLocation.y > cellsSize) {

                    if (ghostMood) {
                        realLocation = new Point(realLocation.x, realLocation.y - movementSize);
                    } else {
                        if ((realLocation.x - cellsSize) % (2 * cellsSize) <= crossGap) {
                            realLocation = (new Point(realLocation.x - (realLocation.x - cellsSize) % (2 * cellsSize), realLocation.y - movementSize));
                        } else if ((realLocation.x - cellsSize) % (2 * cellsSize) >= 2 * cellsSize - crossGap) {
                            realLocation = (new Point(realLocation.x - ((realLocation.x - cellsSize) % (2 * cellsSize)) + 2 * cellsSize, realLocation.y - movementSize));
                        }
                        if (game.getBoard().wallInLocation(realLocation.x, realLocation.y + movementSize - cellsSize)) {          //for walls
                            realLocation = oldLocation;
                        }
                    }
                    if (game.getBoard().bombInLocation(realLocation.x, realLocation.y + movementSize - cellsSize)) {          //for bombs
                        realLocation = oldLocation;
                    }

                }
            }
            if (e == Side.DOWN_SIDE) {
                lastArrayImage = Images.bomberManWD;
                if (lastMove == Side.DOWN_SIDE) {
                    lastIndexImage++;
                } else
                    lastIndexImage = 0;
                lastMove = Side.DOWN_SIDE;

                if (realLocation.y < (game.getBoard().getWidth() * 2 + 1) * cellsSize) {
                    if (ghostMood) {
                        realLocation = new Point(realLocation.x, realLocation.y + movementSize);
                    } else {
                        if ((realLocation.x - cellsSize) % (2 * cellsSize) <= crossGap) {
                            realLocation = (new Point(realLocation.x - (realLocation.x - cellsSize) % (2 * cellsSize), realLocation.y + movementSize));
                        } else if ((realLocation.x - cellsSize) % (2 * cellsSize) >= 2 * cellsSize - crossGap) {
                            realLocation = (new Point(realLocation.x - ((realLocation.x - cellsSize) % (2 * cellsSize)) + 2 * cellsSize, realLocation.y + movementSize));

                        }
                        if (game.getBoard().wallInLocation(realLocation.x, realLocation.y - movementSize + cellsSize)) {          //for walls......
                            realLocation = oldLocation;
                        }
                    }
                    if (game.getBoard().bombInLocation(realLocation.x, realLocation.y - movementSize + cellsSize)) {          //for bombs......
                        realLocation = oldLocation;
                    }

                }


            }


            if (lastIndexImage == 4) {
                lastIndexImage = 0;
            }
            game.getBoard().setBomberManPlace(realLocation);


            loadCell();

            for (int i = currentCell.getInsideObjs().size() - 1; i > -1; i--) {
                Object object = currentCell.getInsideObjs().get(i);
                if (object instanceof PowerChanger && !((PowerChanger) object).isUsed()) {
                    ((PowerChanger) object).setUsed(true);
                    ((PowerChanger) object).doJob(this);
                }
                if (object instanceof Gate) {
                    if (((Gate) object).isOpen()) {
                        game.endGame();
                        int level = Game.level + 1;
                        List<Object> bomberManPowerChangers = new ArrayList<>();
                        bomberManPowerChangers.add(speed);
                        bomberManPowerChangers.add(bombLimit);
                        bomberManPowerChangers.add(canControlBombs);
                        bomberManPowerChangers.add(bombRadius);
                        bomberManPowerChangers.add(ghostMood);                                                                        //Default : monstersCount = 0 ; board.getMonstersCount()
                        NewGameActivity.init(level, game.getBoard().getWidth(), game.getBoard().getHeight(), Board.cellsSize, 0, bomberManPowerChangers, game.getGameFrame().hasVibration());
                    }
                }
            }


        }
    }

    public Point getRealLocation() {
        return realLocation;
    }

    public void setLastMove(Side lastMove) {
        this.lastMove = lastMove;
    }

    public void save() throws FileNotFoundException {
        PrintStream printStream = new PrintStream(MainActivity.instance.openFileOutput("bomberman.txt", Context.MODE_PRIVATE));
        printStream.print(realLocation.x + " " + realLocation.y + " " + speed + " " + bombLimit + " " + bombRadius + " " + point + " ");
        if (canControlBombs)
            printStream.print(1 + " ");
        else
            printStream.print(0 + " ");

        if (ghostMood)
            printStream.print(1 + " ");
        else
            printStream.print(0 + " ");
    }


    public void load() throws FileNotFoundException {

        Scanner scanner = new Scanner(LoadGameActivity.instance.openFileInput("bomberman.txt"));
        realLocation = new Point(scanner.nextInt(), scanner.nextInt());
        speed = scanner.nextInt();
        bombLimit = scanner.nextInt();
        bombRadius = scanner.nextInt();
        point = scanner.nextInt();
        if (scanner.nextInt() == 1)
            canControlBombs = true;
        if (scanner.nextInt() == 1)
            ghostMood = true;

        loadCell();
    }

    private void loadCell() {
        int cellsSize = game.getBoard().getCellsSize();
        if (currentCell != null)
            currentCell.removeInsideObj(this);
        currentCell = game.getBoard().getBackgroundCells().get(((realLocation.y + cellsSize / 2) / cellsSize) * (2 * game.getBoard().getHeight() + 3) + (realLocation.x + cellsSize / 2) / cellsSize);
        if (ghostMood) {
            for (WallCell wallCell : game.getBoard().getWallCells()) {
                if (wallCell.getLocation().equals(currentCell.getLocation()))
                    currentCell = wallCell;
            }
            for (BlockCell blockCell : game.getBoard().getBlockCells()) {
                if (blockCell.getLocation().equals(currentCell.getLocation()))
                    currentCell = blockCell;
            }
        }
        currentCell.addInsideObj(this);
    }


    public Cell getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }


    public synchronized void paint(int x, int y, Canvas canvas) {
        if (alive) {
            if (lastMove == null) {
                canvas.drawBitmap(Images.throwBomb, null, new Rect(realLocation.x - x, realLocation.y - y, realLocation.x - x + Board.cellsSize, realLocation.y - y + Board.cellsSize), null);
            } else {
                canvas.drawBitmap(lastArrayImage[lastIndexImage], null, new Rect(realLocation.x - x, realLocation.y - y, realLocation.x - x + Board.cellsSize, realLocation.y - y + Board.cellsSize), null);
            }
        } else {

            if (!dieCalled) {
                dieCalled = true;
                lastIndexImage = 0;
            }
            die(x, y, canvas);

        }
    }


    private boolean dieCalled = false;
    private int counter = 0;

    private void die(int x, int y, Canvas canvas)  {
        if (lastIndexImage < 5) {
            lastArrayImage = Images.bomberManDeath;
            canvas.drawBitmap(lastArrayImage[lastIndexImage % 5], null,
                    new Rect(realLocation.x - x,
                            realLocation.y - y,
                            realLocation.x - x + Board.cellsSize,
                            realLocation.y - y + Board.cellsSize), null);

            if (counter == 10) {                                                 //speed of death!!!!
                lastIndexImage++;
                counter = 0;
            }

        }else {

            if(counter>20) {
                game.endGame();
                Intent intent = new Intent(MainActivity.instance, StartActivity.class);
                MainActivity.instance.startActivity(intent);
            }


        }

        counter++;

    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        BomberMan.alive = alive;
    }

    public void increaseImageCounter() {
        imageCounter++;
    }


    public void increaseBombLimit() {
        bombLimit++;
    }

    public void decreaseBombLimit() {
        if (bombLimit >= 1)
            bombLimit--;
    }

    public void increaseSpeed() {
        if (speed > 10)
            speed -= 10;
    }

    public void decreaseSpeed() {
        speed += 10;
    }

    public synchronized void increasePoint(int point) {
        this.point += point;
    }

    public synchronized void decreasePoint(int point) {
        this.point -= point;
        if (this.point < 0)
            alive = false;
    }

    public int getBombRadius() {
        return bombRadius;
    }

    public void increaseBombRadius() {
        bombRadius++;
    }

    public void decreaseBombRadius() {
        if (bombRadius > 1)
            bombRadius--;
    }

    public void setCanControlBombs(boolean canControlBombs) {
        this.canControlBombs = canControlBombs;
    }

    public void addBomb(Bomb bomb) {
        bombsCanControl.add(bomb);
    }

    public boolean canControlBombs() {
        return canControlBombs;
    }

    public synchronized int explodeBomb() {
        if (!canControlBombs) {
            return 0;
        }
        if (bombsCanControl.size() != 0) {
            Bomb bomb = bombsCanControl.remove(0);
            bomb.setToExplode(0);
            bomb.setExploded(true);
            return 1;
        }
        return 2;
    }

    public int getBombLimit() {
        return bombLimit;
    }

    public int getPoint() {
        return point;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setBombLimit(int bombLimit) {
        this.bombLimit = bombLimit;
    }

    public void setBombRadius(int bombRadius) {
        this.bombRadius = bombRadius;
    }

    public List<Bomb> getBombsCanControl() {
        return bombsCanControl;
    }

    public void setGhostMood(boolean ghostMood) {
        this.ghostMood = ghostMood;
    }
}
