package Attacks;

import ru.ifmo.se.pokemon.*;

public final class Charm extends StatusMove {
    public Charm() {
        super(Type.FAIRY, 0, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon def) {
        def.setMod(Stat.ATTACK, -2);
    }

    @Override
    protected String describe() {
        return "использует Charm";
    }
}
