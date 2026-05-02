package Attacks;

import ru.ifmo.se.pokemon.*;

public final class MuddyWater extends SpecialMove {
    public MuddyWater() {
        super(Type.WATER, 90, 85);
    }

    @Override
    protected void applyOppEffects(Pokemon def) {
        if (Math.random() <= 0.3) {
            def.setMod(Stat.ACCURACY, -1);
        }
    }

    @Override
    protected String describe() {
        return "использует Muddy Water";
    }
}
