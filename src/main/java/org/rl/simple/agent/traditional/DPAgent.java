package org.rl.simple.agent.traditional;

import org.apache.commons.lang3.RandomUtils;
import org.rl.simple.agent.Agent;
import org.rl.simple.env.*;
import org.rl.simple.env.maze.MazeState;

import java.io.IOException;
import java.util.*;

public class DPAgent implements Agent {

    private static final Double THETA = 0.0000001D;
    private static final Double GAMMA = 1.0D;
    private Map<MazeState, List<Double>> piStar;

    private ModelBasedDispersedEnviroment enviroment;

    public DPAgent(ModelBasedDispersedEnviroment enviroment) {
        this.enviroment = enviroment;

        System.out.println("开始看攻略！");
        Map<MazeState, List<Double>> policy = initPolicy(enviroment);
        int i = 0;
        while (true) {
            // 策略迭代
            Map<MazeState, Double> v = evalStateValue(policy, enviroment);
            Map<MazeState, List<Double>> newPolicy = impovePolicy(v, enviroment);
            if (ifStable(policy, newPolicy)) {
                this.piStar = newPolicy;
                break;
            }
            policy = newPolicy;
            System.out.println(String.format("迭代 %s 次", ++i));
            if (i % 10 == 0) {
                System.out.println(policy);
                System.out.println(newPolicy);
                System.out.println("---------------");
            }
        }
        System.out.println("攻略看完了！");
    }

    private boolean ifStable(Map<MazeState, List<Double>> policy, Map<MazeState, List<Double>> newPolicy) {
        for (MazeState s : policy.keySet()) {
            List<Double> p = policy.get(s);
            List<Double> np = newPolicy.get(s);
            if (!(p.containsAll(np) && np.containsAll(p))) {
                return false;
            }
        }
        return true;
    }

    private Map<MazeState, List<Double>> impovePolicy(Map<MazeState, Double> v, ModelBasedDispersedEnviroment enviroment) {
        Map<MazeState, List<Double>> policy = new HashMap<>();
        Map<MazeState, List<Double>> q= v2q(v, enviroment);
        for (MazeState s : v.keySet()) {
            List<Double> actionValues = q.get(s);
            List<Integer> index = getMaxIndex(actionValues);
            Double prop = 1.0 / index.size();
            List<Double> newProps = new ArrayList<>();
            for (int i = 0; i < enviroment.actionCount(); i++) {
                if (index.contains(i)) {
                    newProps.add(prop);
                } else {
                    newProps.add(0.0D);
                }
            }
            policy.put(s, newProps);
        }
        return policy;
    }

    private List<Integer> getMaxIndex(List<Double> actionValues) {
        Double max = Collections.max(actionValues);
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < actionValues.size(); i++) {
            if (actionValues.get(i).equals(max)) {
                result.add(i);
            }
        }
        return result;
    }

    private Map<MazeState, List<Double>> v2q(Map<MazeState, Double> v, ModelBasedDispersedEnviroment enviroment) {
        Map<MazeState, List<Double>> q = new HashMap<>();
        for (MazeState s : v.keySet()) {
            List<Double> actionValue = new ArrayList<>();
            for (int i = 0; i < enviroment.actionCount(); i++) {
                ModelBasedDispersedEnviroment.StateTransitionProbability p = enviroment.p(s, i);
                if (p == null) {
                    continue;
                }
                Double av = p.getReward() + GAMMA * v.getOrDefault(p.getNextState(), 0.0D);
                actionValue.add(av);
            }
            q.put(s, actionValue);
        }
        return q;
    }

    private Map<MazeState, Double> evalStateValue(Map<MazeState, List<Double>> policy, ModelBasedDispersedEnviroment enviroment) {
        Map<MazeState, Double> v = new HashMap<>();
        int count = 0;
        while (true) {
            Double delta = 0.0D;
            for (MazeState s : policy.keySet()) {
                Double value = 0.0D;
                if (this.enviroment.isDoneState(s)) {
                    v.put(s, 0.0D);
                    continue;
                }
                List<Double> props = policy.get(s);
                for (int i = 0; i < props.size(); i++) {
                    Double prop = props.get(i);
                    ModelBasedDispersedEnviroment.StateTransitionProbability p = enviroment.p(s, i);
                    State nextState = p.getNextState();
                    Double tranProp = p.getProp();
                    Double reward = p.getReward();
                    boolean done = p.isDone();
                    Double nextValue = 0.0D;
                    if (!done) {
                        nextValue = v.getOrDefault(nextState, 0.0D);
                    }
                    value += tranProp * prop * (reward + GAMMA * nextValue);
                }
                delta = Math.max(delta, Math.abs(v.getOrDefault(s, 0.0D) - value));
                v.put(s, value);
            }
            if (delta < THETA) {
                System.out.println(delta);
                System.out.println(THETA);
                break;
            }
            if (count % 10000 == 0) {
                System.out.println(String.format("评估迭代 %s 次", count));
                System.out.println(delta);
                System.out.println(THETA);
                System.out.println("---------------");
            }
            count++;

        }
        return v;
    }

    private Map<MazeState, List<Double>> initPolicy(ModelBasedDispersedEnviroment enviroment) {
        Map<MazeState, List<Double>> policy = new HashMap<>();
        Double probability = 1.0 / enviroment.actionCount();
        for (State s : enviroment.getStates()) {
            List<Double> actionProp = new ArrayList<>();
            for (int i = 0; i < enviroment.actionCount(); i++) {
                actionProp.add(probability);
            }
            policy.put((MazeState)s, actionProp);
        }
        return policy;
    }


    @Override
    public Action chooseAction(State state) {
        List<Double> doubles = this.piStar.get(state);
        List<Integer> maxIndex = getMaxIndex(doubles);
        Action a = this.enviroment.actionSpace()[maxIndex.get(RandomUtils.nextInt(0, maxIndex.size()))];
        return a;
    }

    @Override
    public void learn(State state, State nextState, Action action, Action nextAction, Reward reward, boolean done) {

    }

    @Override
    public void save() throws IOException {

    }

    @Override
    public void load() throws IOException {

    }

    @Override
    public String getSaveFilePath() {
        return null;
    }

    @Override
    public void printQTable() {
        System.out.println(piStar);
    }
}
