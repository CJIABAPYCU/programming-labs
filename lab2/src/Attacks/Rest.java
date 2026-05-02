package Attacks;

import ru.ifmo.se.pokemon.*;

public final class Rest extends StatusMove {
    public Rest() {
        super(Type.PSYCHIC, 0, 100);
    }

    @Override
    protected void applySelfEffects(Pokemon p) {
        p.restore();
        Effect.sleep(p);
    }

    @Override
    protected String describe() {
        return "использует Rest и засыпает, восстанавливая здоровье";
    }
}
