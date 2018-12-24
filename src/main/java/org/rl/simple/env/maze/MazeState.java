package org.rl.simple.env.maze;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.rl.simple.env.State;

@Data
@AllArgsConstructor
public class MazeState implements State {

    private int x;
    private int y;

    @Override
    public int hashCode() {
        return this.x * 100 + this.y * 10;
    }

    @Override
    public boolean equals(Object object) {
        return this.hashCode() == object.hashCode();
    }
}
