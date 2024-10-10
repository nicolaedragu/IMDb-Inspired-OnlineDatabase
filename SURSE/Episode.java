public class Episode {
    String episodeName;
    String duration;

    public Episode(String episodeName, String duration) {
        this.episodeName = episodeName;
        this.duration = duration;
    }
    public String getEpisodeName() {
        return episodeName;
    }
    public String getDuration() {
        return duration;
    }

    public String toString() {
        return episodeName + " (" + duration + ") ";
    }

}
