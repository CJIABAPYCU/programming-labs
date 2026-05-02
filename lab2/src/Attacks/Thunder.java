package Attacks;

import ru.ifmo.se.pokemon.*;

public final class Thunder extends SpecialMove {
    public Thunder() {
        super(Type.ELECTRIC, 110, 70);
    }

    @Override
    protected void applyOppEffects(Pokemon def) {
        if (Math.random() <= 0.3) {
            Effect.paralyze(def);
        }
    }

    @Override
    protected String describe() {
        return "использует Thunder";
    }
}
