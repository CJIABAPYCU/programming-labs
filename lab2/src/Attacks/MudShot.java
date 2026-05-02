package Attacks;

import ru.ifmo.se.pokemon.*;

public final class MudShot extends SpecialMove {
    public MudShot() {
        super(Type.GROUND, 55, 95);
    }

    @Override
    protected void applyOppEffects(Pokemon def) {
        def.setMod(Stat.SPEED, -1);
    }

    @Override
    protected String describe() {
        return "использует Mud Shot";
    }
}
