package Attacks;

import ru.ifmo.se.pokemon.*;

public final class EarthPower extends SpecialMove {
    public EarthPower() {
        super(Type.GROUND, 90, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon def) {
        if (Math.random() <= 0.1) {
            def.setMod(Stat.SPECIAL_DEFENSE, -1);
        }
    }

    @Override
    protected String describe() {
        return "использует Earth Power";
    }
}
