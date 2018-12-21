package org.rl.simple.env.maze;

import lombok.Getter;
import org.rl.simple.env.Reward;
import org.rl.simple.env.State;
import org.rl.simple.env.StepResult;

import java.util.Arrays;

import static org.rl.simple.env.maze.Constants.*;

@Getter
public class MazeMap {

    private static final int START_X = 0;
    private static final int START_Y = 0;

    private int[][] map;

    private int width;
    private int height;
    private Player player;

    public MazeMap(int width, int height) {
        this(width, height, false);
    }
    public MazeMap(int width, int height, boolean dp) {
        // 全0初始化
        this.width = width;
        this.height = height;
        this.resetMap(dp);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                System.out.println(String.format("%s, %s  --> %s", i, j, this.map[i][j]));

            }
        }
    }

    private void resetMap(boolean dp) {
        this.map = new int[width][height];
        // 从左上角开始游戏
        this.map[START_X][START_Y] = PLAYER;
        // 终点在右下角
        this.map[width - 1][height - 1] = GOAL;
        if (dp) {
            // dp 不用陷阱 用 大山
            this.map[width - 1][0] = MOUNTAIN;
        } else {
            // 两个陷阱 写死位置
            this.map[width - 1][height - 2] = TRAP;
            this.map[width / 2][height / 2] = TRAP;
        }
        this.player = new Player(START_X, START_Y, this);
    }

    public void setState(MazeState state) {
        this.player.x = state.getX();
        this.player.y = state.getY();
    }

    public synchronized StepResult step(int action) {
        int oldX = this.player.x;
        int oldY = this.player.y;
        this.player.move(action);
        int newX = this.player.x;
        int newY = this.player.y;
        this.map[oldX][oldY] = 0;
        boolean done = isDone(this.player);
        Reward reward = reward(this.player);
        StepResult result = new StepResult();
        result.setDone(done);
        result.setReward(reward);
        result.setNextState(new MazeState(this.player.x, this.player.y));
        result.setWin(reward.getReward() == 10.0D);
        this.map[newX][newY] = PLAYER;
        return result;
    }

    private Reward reward(Player player) {
        int code = this.map[player.x][player.y];
        if (code == TRAP) {
            return new Reward(-10.0D);
        } else if (code == GOAL) {
            return new Reward(10.0D);
        } else if (code == MOUNTAIN) {
            return new Reward(-3.0D);
        } else {
            return new Reward(-0.1D);
        }
    }
    public boolean isDone(int x, int y) {
        int code = this.map[x][y];
        if (code == TRAP || code == GOAL) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isDone(Player player) {
        return isDone(player.x, player.y);
    }

    public State reset(boolean dp) {
        this.resetMap(dp);
        return new MazeState(START_X, START_Y);
    }

    class Player {
        private volatile int x;
        private volatile int y;
        private MazeMap map;
        public Player(int x, int y, MazeMap map) {
            this.x = x;
            this.y = y;
            this.map = map;
        }

        public synchronized void move(int action) {
            int newX = this.x;
            int newY = this.y;
            if (action == UP) {
                if (this.y != 0) {
                    newY -= 1;
                }
            } else if (action == RIGHT) {
                if (this.x != width - 1) {
                    newX += 1;
                }
            } else if (action == DOWN) {
                if (this.y != height - 1) {
                    newY += 1;
                }
            } else if (action == LEFT) {
                if (this.x != 0) {
                    newX -= 1;
                }
            }
            this.x = newX;
            this.y = newY;
        }
    }
}
