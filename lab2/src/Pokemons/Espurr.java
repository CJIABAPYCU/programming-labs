package Pokemons;

import Attacks.*;
import ru.ifmo.se.pokemon.*;

public class Espurr extends Pokemon {
    public Espurr(String name, int level) {
        super(name, level);
        setType(Type.PSYCHIC);
        setStats(62, 48, 54, 63, 60, 68);
        addMove(new Confusion());
        addMove(new DisarmingVoice());
        addMove(new Rest());
    }
}
