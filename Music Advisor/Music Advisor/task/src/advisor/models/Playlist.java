package advisor.models;

public class Playlist {

    private String name;
    private String link;

    public Playlist(String name, String link) {
        this.name = name;
        this.link = link;
    }

    @Override
    public String toString() {
        return "%s\n%s".formatted(name, link);
    }
}
