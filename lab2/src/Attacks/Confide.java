package Attacks;

import ru.ifmo.se.pokemon.*;

public final class Confide extends StatusMove {
    public Confide() {
        super(Type.NORMAL, 0, 100);
    }

    @Override
    protected boolean checkAccuracy(Pokemon att, Pokemon def) {
        return true;
    }

    @Override
    protected void applyOppEffects(Pokemon def) {
        def.setMod(Stat.SPECIAL_ATTACK, -1);
    }

    @Override
    protected String describe() {
        return "использует Confide";
    }
}
