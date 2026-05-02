package Pokemons;

import Attacks.*;
import ru.ifmo.se.pokemon.*;

public final class MeowsticMale extends Espurr {
    public MeowsticMale(String name, int level) {
        super(name, level);
        setStats(74, 48, 76, 83, 81, 104);
        addMove(new Charm());
    }
}
