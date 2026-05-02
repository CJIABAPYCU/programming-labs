package Attacks;

import ru.ifmo.se.pokemon.*;

public final class Confusion extends SpecialMove {
    public Confusion() {
        super(Type.PSYCHIC, 50, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon def) {
        if (Math.random() <= 0.1) {
            Effect.confuse(def);
        }
    }

    @Override
    protected String describe() {
        return "использует Confusion";
    }
}
