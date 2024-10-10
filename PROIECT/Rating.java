public class Rating {
    String username;
    Integer rating;
    String comment;

    //    doar utilizatorii Regular pot evalua productiile
    //    un utilizator poate evalua o productie o singura data
    //    totusi, poate sterge evaluarea anterioara si sa o inlocuiasca cu una noua, insa nu va primi puncte de experienta pentru asta

    public Rating(String username, Integer rating, String comment) {
        this.username = username;
        this.rating = rating;
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }
    public Integer getRating() {
        return rating;
    }
    public String getComment() {
        return comment;
    }


    //  daca o productie pe care a evaluat-o un utilizator primeste o alta evaluare, utilizatorul REGULAR va primi o notificare
    //  daca o productie pe care a adaugat-o in sistem primeste o evaluare, utilizatorul CONTRIBUTOR / ADMIN va primi o notificare


    public String toString() {
        return username + ": " + "rating = " + rating + ", comment: " + comment;
    }

}
