public class ProductionDetails {
    private String title;
    private String type;
    public ProductionDetails(String title, String type) {
        this.title = title;
        this.type = type;
    }
    public String getTitle() {
        return title;
    }
    public String getType() {
        return type;
    }
    public String toString() {
        return title + " (" + type + ")";
    }
}