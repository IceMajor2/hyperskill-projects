package advisor;

import java.util.List;

public class Album {

    private String name;
    private List<String> artists;
    private String link;

    public Album(String name, List<String> artists, String link) {
        this.name = name;
        this.artists = artists;
        this.link = link;
    }
}
