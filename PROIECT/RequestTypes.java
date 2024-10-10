public enum RequestTypes {
    DELETE_ACCOUNT, // cerere pentru stergea contului (facuta de Regular, Contributor pentru Admini)
    ACTOR_ISSUE, // cerere in legatura cu datele unui actor (facuta de Regular, Contributor pentru contributorul/adminul care a adaugat actorul)
    MOVIE_ISSUE, // cerere in legatura cu datele unui film (facuta de Regular, Contributor pentru contributorul/adminul care a adaugat filmul)
    OTHERS // cereri care nu se incadreaza in cele de mai sus (destinate doar Adminilor) [ex: data nasterii contului personal completata gresit si necesita actualizare]
}
