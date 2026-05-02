import Pokemons.*;
import ru.ifmo.se.pokemon.*;

public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();

        Pokemon stunfisk = new Stunfisk("Станфиск", 1);
        Pokemon espurr = new Espurr("Еспур", 1);
        Pokemon meowsticMale = new MeowsticMale("Mеовстик-Мэл", 25);
        Pokemon pichu = new Pichu("Пичу", 1);
        Pokemon pikachu = new Pikachu("Пикачу", 1);
        Pokemon raichu = new Raichu("Рейчу", 1);

        b.addAlly(stunfisk);
        b.addAlly(espurr);
//        b.addAlly(pichu);

        b.addFoe(meowsticMale);
        b.addFoe(pikachu);
        b.addFoe(raichu);

        b.go();
    }
}
