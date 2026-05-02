package Attacks;

import ru.ifmo.se.pokemon.*;

public final class StoneEdge extends PhysicalMove {
    public StoneEdge() {
        super(Type.ROCK, 100, 80);
    }

    @Override
    protected double calcCriticalHit(Pokemon att, Pokemon def) {
        return Math.random() < (1.0 / 8.0) ? 2.0 : 1.0;
    }

    @Override
    protected String describe() {
        return "использует Stone Edge";
    }
}
