// experienta primita pentru adaugarea unei recenzii
class AddReviewStrategy implements ExperienceStrategy {
    @Override
    public int calculateExperience() {
        return 3;
    }
}

// rezolvarea unei cereri de tipul ACTOR_ISSUE
class ActorIssueStrategy implements ExperienceStrategy {
    @Override
    public int calculateExperience() {
        return 6;
    }
}

// rezolvarea unei cereri de tipul MOVIE_ISSUE
class MovieIssueStrategy implements ExperienceStrategy {
    @Override
    public int calculateExperience() {
        return 7;
    }
}

// experienta primita pentru adaugarea unei productii
class AddProductStrategy implements ExperienceStrategy {
    @Override
    public int calculateExperience() {
        return 9;
    }
}

// experienta primita pentru adaugarea unui actor
class AddActorStrategy implements ExperienceStrategy {
    @Override
    public int calculateExperience() {
        return 10;
    }
}

class AdminStrategy implements ExperienceStrategy {
    @Override
    public int calculateExperience() {
        return 999;
    }
}
