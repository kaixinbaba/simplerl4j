package org.rl.simple.env.maze;

import lombok.Getter;
import org.apache.commons.lang3.RandomUtils;
import org.rl.simple.env.*;
import org.rl.simple.env.Action;

import javax.swing.*;
import java.awt.*;

import static org.rl.simple.env.maze.Constants.*;

/**
 * 简单的走迷宫的环境
 */
public class Maze extends JFrame implements DispersedEnviroment {


    private JPanel panel;

    private MazeMap map;

    @Getter
    private int width;

    @Getter
    private int height;

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
        init(width, height, everyStepSleepTime, needRender, isHumanPlay, isSlippery, unSlipperyProp);
    }

    private void init(int width, int height, long everyStepSleepTime, boolean needRender, boolean isHumanPlay,
                      boolean isSlippery, double unSlipperyProp) {
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

        this.width = width;
        this.height = height;
        this.map = new MazeMap(width, height);
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
        return this.map.reset();
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
        return this.map.step(action.getCode());
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
        return this.width * this.height;
    }

    @Override
    public Action[] actionSpace() {
        return this.actionSpace;
    }
}
