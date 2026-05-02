package Attacks;

import ru.ifmo.se.pokemon.*;

public final class DisarmingVoice extends SpecialMove {
    public DisarmingVoice() {
        super(Type.FAIRY, 40, 100);
    }

    @Override
    protected boolean checkAccuracy(Pokemon att, Pokemon def) {
        return true;
    }

    @Override
    protected String describe() {
        return "использует Disarming Voice";
    }
}
