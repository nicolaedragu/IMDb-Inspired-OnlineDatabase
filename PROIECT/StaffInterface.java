import java.util.List;

public interface StaffInterface {
    public void addProductionSystem(Production p, List<Production> productions);
    public void addActorSystem(Actor a, List<Actor> actors);

    // stergerea unei productii adaugata de EL
    public void removeProductionSystem(String name);

    // stergera unui actor adaugat de EL
    public void removeActorSystem(String name);

    // actualizarea informatiilor despre o productie adaugata de EL
    public void updateProduction(Production p);

    // actualizarea informatiilor despre un actor adaugat de EL
    public List<Actor> updateActor(Actor a, List<Actor> actors);

}
