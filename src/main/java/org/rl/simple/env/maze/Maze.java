package org.rl.simple.env.maze;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomUtils;
import org.rl.simple.env.*;
import org.rl.simple.env.Action;

import javax.swing.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.rl.simple.env.maze.Constants.*;

/**
 * 简单的走迷宫的环境
 */
public class Maze extends JFrame implements DispersedEnviroment {


    private JPanel panel;

    @Getter
    @Setter
    private MazeMap map;

    @Getter
    private int mazeWidth;

    @Getter
    private int mazeHeight;

    private int actionCount;

    @Getter
    private long everyStepSleepTime;

    private boolean needRender;
    @Getter
    private boolean isHumanPlay;
    @Getter
    private boolean isSlippery;
    @Getter
    private double unSlipperyProp;
    private Action[] actionSpace;

    public Maze(int width, int height) {
        this(width, height, 100L);
    }

    public Maze(int width, int height, long everyStepSleepTime) {
        this(width, height, everyStepSleepTime, true, false);
    }

    public Maze(int width, int height, long everyStepSleepTime, boolean needRender, boolean isHumanPlay) {
        this(width, height, everyStepSleepTime, needRender, isHumanPlay, false, 0.0D);
    }
    public Maze(int width, int height, long everyStepSleepTime, boolean needRender, boolean isHumanPlay, boolean isSlippery, double unSlipperyProp) {
        this(width, height, everyStepSleepTime, needRender, isHumanPlay, isSlippery, unSlipperyProp, false);
    }

    public Maze(int width, int height, long everyStepSleepTime, boolean needRender, boolean isHumanPlay, boolean isSlippery, double unSlipperyProp, boolean dp) {
        init(width, height, everyStepSleepTime, needRender, isHumanPlay, isSlippery, unSlipperyProp, dp);
    }

    protected void init(int width, int height, long everyStepSleepTime, boolean needRender, boolean isHumanPlay,
                      boolean isSlippery, double unSlipperyProp, boolean dp) {
        //设置Frame标题
        setTitle("走迷宫");
        //设置窗口背景色
        setBackground(Color.WHITE);
        //初始化窗口大小(默认读取配置文件中的宽高，为游戏最小宽高)
        setSize(width * CUBE_PIXEL, height * CUBE_PIXEL + 20);
        //不能缩放
        setResizable(false);
        //设置居中
        setLocationRelativeTo(null);
        //设置窗口关闭策略
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //////////////////////////////////////////

        this.mazeWidth = width;
        this.mazeHeight = height;
        this.map = new MazeMap(width, height, dp);
        this.panel = new MazePanel(this.map);
        this.setContentPane(this.panel);
        this.actionSpace = new Action[]{
                new Action(UP),
                new Action(RIGHT),
                new Action(DOWN),
                new Action(LEFT),
        };
        this.actionCount = this.actionCount();
        this.everyStepSleepTime = everyStepSleepTime;
        this.needRender = needRender;
        this.isHumanPlay = isHumanPlay;
        this.isSlippery = isSlippery;
        this.unSlipperyProp = unSlipperyProp;
    }

    @Override
    public State reset() {
        return this.map.reset(false);
    }

    @Override
    public Action sampleAction() {
        return this.actionSpace[RandomUtils.nextInt(0, this.actionCount)];
    }

    @Override
    public StepResult step(Action action) {
        try {
            Thread.sleep(this.everyStepSleepTime);
        } catch (InterruptedException e) {
        }
        int actionCode = action.getCode();
        if (isSlippery) {
            if (RandomUtils.nextDouble(0.0D, 1.0D) > this.unSlipperyProp) {
                List<Integer> other = new ArrayList<>();
                other.addAll(Arrays.asList(this.actionSpace)
                        .stream().map(a -> a.getCode()).collect(Collectors.toList()));
                other.remove(actionCode);
                actionCode = other.get(RandomUtils.nextInt(0, other.size()));
            }
        }
        return this.map.step(actionCode);
    }

    @Override
    public void render() {
        if (this.needRender) {
            //设置窗口可见
            setVisible(true);
            this.panel.repaint();
        }
    }

    @Override
    public int actionCount() {
        return this.actionSpace.length;
    }

    @Override
    public int stateCount() {
        return this.mazeWidth * this.mazeHeight;
    }

    @Override
    public Action[] actionSpace() {
        return this.actionSpace;
    }
}
