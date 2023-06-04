package advisor;

import java.util.Arrays;
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

    @Override
    public String toString() {
        return "%s\n%s\n%s".formatted(name, Arrays.toString(artists.toArray()), link);
    }
}
