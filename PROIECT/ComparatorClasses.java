import java.util.Comparator;
import java.util.List;

class ActorComparator implements Comparator<Actor> {
    @Override
    public int compare(Actor actor1, Actor actor2) {
        return actor1.getActorName().compareToIgnoreCase(actor2.getActorName());
    }
}

class ProductionComparator implements Comparator<Production> {
    @Override
    public int compare(Production production1, Production production2) {
        return production1.getTitle().compareTo(production2.getTitle());
    }
}

class RatingComparator implements Comparator<Production> {
    @Override
    public int compare(Production production1, Production production2) {
        return Integer.compare(production2.getRatings().size(), production1.getRatings().size());
    }
}

class GenreComparator implements Comparator<Production> {
    @Override
    public int compare(Production production1, Production production2) {
        List<Genre> genres1 = production1.getGenres();
        List<Genre> genres2 = production2.getGenres();

        for (int i = 0; i < Math.min(genres1.size(), genres2.size()); i++) {
            int genreComparison = genres1.get(i).compareTo(genres2.get(i));
            if (genreComparison != 0) {
                return genreComparison;
            }
        }

        return -Integer.compare(genres1.size(), genres2.size());
    }
}