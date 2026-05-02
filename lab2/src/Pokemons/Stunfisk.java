package Pokemons;

import Attacks.*;
import ru.ifmo.se.pokemon.*;

public final class Stunfisk extends Pokemon {
    public Stunfisk(String name, int level) {
        super(name, level);
        setType(Type.GROUND, Type.ELECTRIC);
        setStats(109, 66, 84, 81, 99, 32);
        addMove(new MudShot());
        addMove(new EarthPower());
        addMove(new StoneEdge());
        addMove(new MuddyWater());
    }
}
