package org.rl.simple.common.enums;

public enum Algorithm {

    Q_LEARNING("q-learning"),
    SARSA("sarsa"),
    DYNAMIC_PROGRAMMING("dp"),
    DQN("dqn"),
    DPG("dpg");

    private String code;

    Algorithm(String code) {
        this.code = code;
    }

    public String code() {
        return this.code;
    }

}
