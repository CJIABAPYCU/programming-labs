package Attacks;

import ru.ifmo.se.pokemon.*;

public final class Growl extends StatusMove {
    public Growl() {
        super(Type.NORMAL, 0, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon def) {
        def.setMod(Stat.ATTACK, -1);
    }

    @Override
    protected String describe() {
        return "использует Growl";
    }
}
