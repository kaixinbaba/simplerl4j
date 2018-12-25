package org.rl.simple.agent.traditional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.RandomUtils;
import org.rl.simple.agent.Agent;
import org.rl.simple.env.Action;
import org.rl.simple.env.DispersedEnviroment;
import org.rl.simple.env.Reward;
import org.rl.simple.env.State;
import org.rl.simple.env.maze.MazeState;

import java.io.IOException;
import java.util.*;

import static org.rl.simple.agent.traditional.SarsaAgent.EPSILON_MIN;

/**
 * mc 蒙特卡洛
 */
public class MCAgent implements Agent {


    @Getter
    protected Map<State, List<Double>> qtable = new HashMap<>();

    protected int actionCount;
    protected double epsilon;
    protected double epsilonDecay;
    protected double epsilonMin;
    protected double learningRate;
    protected double gamma;
    protected Action[] actionSpace;
    protected DispersedEnviroment enviroment;
    private boolean isFirstVisit = true;
    private List<Episode> episodes = new ArrayList<>();

    public MCAgent(DispersedEnviroment enviroment, Object... args) {
        this(enviroment, 1.0D, 0.01D, 0.1D, 0.1D, 0.9D, args);
    }

    public MCAgent(DispersedEnviroment enviroment, double epsilonStart, double epsilonDecay,
                   double epsilonMin, double learningRate, double gamma, Object ... args) {
        this.enviroment = enviroment;
        this.actionCount = enviroment.actionCount();
        this.actionSpace = enviroment.actionSpace();
        this.epsilon = epsilonStart;
        this.epsilonDecay = epsilonDecay;
        this.epsilonMin = epsilonMin;
        this.learningRate = learningRate;
        this.gamma = gamma;
        if (args.length == 1) {
            this.isFirstVisit = (boolean) args[0];
        }
    }

    @Override
    public Action chooseAction(State state) {
        Action result;
        if (this.qtable.containsKey(state)) {
            if (RandomUtils.nextDouble(0, 1) < this.epsilon) {
                // random choice
                result = this.enviroment.sampleAction();
            } else {
                // max
                List<Double> actionExpectedReward = this.qtable.get(state);
                result = this.actionSpace[this.getMaxActionIndex(actionExpectedReward)];
            }
        } else {
            // put in table
            this.qtable.put(state, initActionRowList());
            // random choice
            result = this.enviroment.sampleAction();
        }
        return result;
    }

    /**
     * 为了防止有多个最大值
     *
     * @param actionExpectedReward
     * @return
     */
    protected int getMaxActionIndex(List<Double> actionExpectedReward) {
        Double max = Collections.max(actionExpectedReward);
        List<Integer> maxIndex = new ArrayList<>(actionExpectedReward.size());
        for (int i = 0; i < actionExpectedReward.size(); i++) {
            Double current = actionExpectedReward.get(i);
            if (max.equals(current)) {
                maxIndex.add(i);
            }
        }
        return maxIndex.get(RandomUtils.nextInt(0, maxIndex.size()));
    }

    protected List<Double> initActionRowList() {
        List<Double> result = new ArrayList<>(this.actionCount);
        for (int i = 0; i < this.actionCount; i++) {
            result.add(0.0D);
        }
        return result;
    }

    @Override
    public void learn(State state, State nextState, Action action, Action nextAction, Reward reward, boolean done) {


        double epsilon = this.epsilon - this.epsilonDecay;
        if (epsilon > this.epsilonMin) {
            this.epsilon = epsilon;
        } else {
            this.epsilon = this.epsilonMin;
        }
    }

    @Override
    public void save() throws IOException {

    }

    @Override
    public void load() throws IOException {

    }

    @Override
    public String getSaveFilePath() {
        return "mc.json";
    }

    @Override
    public void printQTable() {

    }

    public void learn() {
        Map<StateAction, Double> currentEpisodeQValue = getAvgQvalue();
        updateQTable(currentEpisodeQValue);
        double epsilon = this.epsilon - this.epsilonDecay;
        if (epsilon > this.epsilonMin) {
            this.epsilon = epsilon;
        } else {
            this.epsilon = this.epsilonMin;
        }
        this.episodes.clear();
    }

    private void updateQTable(Map<StateAction, Double> currentEpisodeQValue) {
        for (StateAction sa : currentEpisodeQValue.keySet()) {
            State state = sa.getState();
            int action = sa.getAction();
            Double currentReward = currentEpisodeQValue.get(sa);
            List<Double> actionValues = this.qtable.get(state);
            Double oldQValue = actionValues.get(action);
            // 重点
            Double newQValue = oldQValue + this.learningRate * (currentReward - oldQValue);
            actionValues.set(action, newQValue);
        }
    }

    private Map<StateAction, Double> getAvgQvalue() {
        Map<StateAction, Double> result = new HashMap<>();
        for (int i = 0; i < this.episodes.size(); i++) {
            Episode e = this.episodes.get(i);
            StateAction sa = new StateAction(e.getState(), e.getAction());
            // just first visit
            if (result.containsKey(sa) && !this.isFirstVisit) {
                continue;
                // TODO every visit
            } else {
                Double qValue = getTotalQvalue(i, this.episodes);
                result.put(sa, qValue);
            }
        }
        return result;
    }

    private Double getTotalQvalue(int i, List<Episode> episodes) {
        Double totalReward = 0.0D;
        int gammaPow = 0;
        for (int j = i; j < episodes.size(); j++) {
            Episode e = this.episodes.get(j);
            totalReward += Math.pow(this.gamma, gammaPow) * e.getReward();
            gammaPow++;
        }
        return totalReward;
    }

    public void storeEpisode(MazeState state, Action action, double reward) {
        this.episodes.add(new Episode(state, action.getCode(), reward));
    }

    @Data
    @AllArgsConstructor
    private class StateAction {
        private MazeState state;
        private Integer action;

        @Override
        public int hashCode() {
            return state.hashCode() + action;
        }

        @Override
        public boolean equals(Object object) {
            return this.hashCode() == object.hashCode();
        }
    }


    @Data
    @AllArgsConstructor
    private class Episode {

        private MazeState state;
        private Integer action;
        private double reward;

        @Override
        public int hashCode() {
            return state.hashCode() + action;
        }

        @Override
        public boolean equals(Object object) {
            return this.hashCode() == object.hashCode();
        }
    }
}
