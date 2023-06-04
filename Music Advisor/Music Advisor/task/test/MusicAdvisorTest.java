import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.mocks.web.WebServerMock;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;
import org.junit.AfterClass;

@SuppressWarnings("unused")
public class MusicAdvisorTest extends StageTest<String> {

    private static final String fictiveAuthCode = "123123";
    private static final String fictiveAccessToken = "456456";
    private static final String fictiveRefreshToken = "567567";


    private static final int accessServerPort = 45678;
    private static final int resourceServerPort = 56789;

    private static final String accessServerUrl = "http://127.0.0.1:" + accessServerPort;
    private static final String resourceServerUrl = "http://127.0.0.1:" + resourceServerPort;

    private static final String[] arguments = new String[]{
            "-access",
            accessServerUrl,
            "-resource",
            resourceServerUrl
    };

    private static final String tokenResponse = "{" +
            "\"access_token\":\"" + fictiveAccessToken + "\"," +
            "\"token_type\":\"Bearer\"," +
            "\"expires_in\":3600," +
            "\"refresh_token\":" + "\"" + fictiveRefreshToken + "\"," +
            "\"scope\":\"\"" +
            "}";

    // TODO handle auth code argument to get the token.
    private static final WebServerMock accessServer = new WebServerMock(accessServerPort)
            .setPage("/api/token", tokenResponse);


    private static final String spotifyServerUrl = "https://api\\.spotify\\.com";

    private static final String apiCategoriesResponse = """
        {
            "categories": {
                "href": "https://api.spotify.com/v1/browse/categories?offset=0&limit=20",
                "items": [
                    {
                        "href": "https://api.spotify.com/v1/browse/categories/toplists",
                        "icons": [
                            {
                                "height": 275,
                                "url": "https://datsnxq1rwndn.cloudfront.net/media/derived/toplists_11160599e6a04ac5d6f2757f5511778f_0_0_275_275.jpg",
                                "width": 275
                            }
                        ],
                        "id": "toplists",
                        "name": "Top Lists"
                    },
                    {
                        "href": "https://api.spotify.com/v1/browse/categories/mood",
                        "icons": [
                            {
                                "height": 274,
                                "url": "https://datsnxq1rwndn.cloudfront.net/media/original/mood-274x274_976986a31ac8c49794cbdc7246fd5ad7_274x274.jpg",
                                "width": 274
                            }
                        ],
                        "id": "mood",
                        "name": "Super Mood"
                    },
                    {
                        "href": "https://api.spotify.com/v1/browse/categories/party",
                        "icons": [
                            {
                                "height": 274,
                                "url": "https://datsnxq1rwndn.cloudfront.net/media/derived/party-274x274_73d1907a7371c3bb96a288390a96ee27_0_0_274_274.jpg",
                                "width": 274
                            }
                        ],
                        "id": "party",
                        "name": "Party Time"
                    }
                ],
                "limit": 20,
                "next": null,
                "offset": 0,
                "previous": null,
                "total": 3
            }
        }""".replaceAll(spotifyServerUrl, resourceServerUrl);


    private static final String apiPlaylistsPartyResponse = """
        {
            "playlists": {
                "href": "https://api.spotify.com/v1/browse/categories/party/playlists?offset=0&limit=20",
                "items": [
                    {
                        "collaborative": false,
                        "external_urls": {
                            "spotify": "http://open.spotify.com/user/spotifybrazilian/playlist/4k7EZPI3uKMz4aRRrLVfen"
                        },
                        "href": "https://api.spotify.com/v1/users/spotifybrazilian/playlists/4k7EZPI3uKMz4aRRrLVfen",
                        "id": "4k7EZPI3uKMz4aRRrLVfen",
                        "images": [
                            {
                                "height": 300,
                                "url": "https://i.scdn.co/image/bf6544c213532e9650088dfef76c8521093d970e",
                                "width": 300
                            }
                        ],
                        "name": "Noite Eletronica",
                        "owner": {
                            "external_urls": {
                                "spotify": "http://open.spotify.com/user/spotifybrazilian"
                            },
                            "href": "https://api.spotify.com/v1/users/spotifybrazilian",
                            "id": "spotifybrazilian",
                            "type": "user",
                            "uri": "spotify:user:spotifybrazilian"
                        },
                        "public": null,
                        "snapshot_id": "PULvu1V2Ps8lzCxNXfNZTw4QbhBpaV0ZORc03Mw6oj6kQw9Ks2REwhL5Xcw/74wL",
                        "tracks": {
                            "href": "https://api.spotify.com/v1/users/spotifybrazilian/playlists/4k7EZPI3uKMz4aRRrLVfen/tracks",
                            "total": 100
                        },
                        "type": "playlist",
                        "uri": "spotify:user:spotifybrazilian:playlist:4k7EZPI3uKMz4aRRrLVfen"
                    },
                    {
                        "collaborative": false,
                        "external_urls": {
                            "spotify": "http://open.spotify.com/user/spotifybrazilian/playlist/4HZh0C9y80GzHDbHZyX770"
                        },
                        "href": "https://api.spotify.com/v1/users/spotifybrazilian/playlists/4HZh0C9y80GzHDbHZyX770",
                        "id": "4HZh0C9y80GzHDbHZyX770",
                        "images": [
                            {
                                "height": 300,
                                "url": "https://i.scdn.co/image/be6c333146674440123073cb32c1c8b851e69023",
                                "width": 300
                            }
                        ],
                        "name": "Festa Indie",
                        "owner": {
                            "external_urls": {
                                "spotify": "http://open.spotify.com/user/spotifybrazilian"
                            },
                            "href": "https://api.spotify.com/v1/users/spotifybrazilian",
                            "id": "spotifybrazilian",
                            "type": "user",
                            "uri": "spotify:user:spotifybrazilian"
                        },
                        "public": null,
                        "snapshot_id": "V66hh9k2HnLCdzHvtoXPv+tm0jp3ODM63SZ0oISfGnlHQxwG/scupDbKgIo99Zfz",
                        "tracks": {
                            "href": "https://api.spotify.com/v1/users/spotifybrazilian/playlists/4HZh0C9y80GzHDbHZyX770/tracks",
                            "total": 74
                        },
                        "type": "playlist",
                        "uri": "spotify:user:spotifybrazilian:playlist:4HZh0C9y80GzHDbHZyX770"
                    }
                ],
                "limit": 20,
                "next": null,
                "offset": 0,
                "previous": null,
                "total": 2
            }
        }""".replaceAll(spotifyServerUrl, resourceServerUrl);

    private static final String testErrorMessage = "Test unpredictable error message";

    private static final String apiTestErrorResponse = "{\"error\":{\"status\":404,\"message\":\"" + testErrorMessage + "\"}}";

    private static final String apiNewReleasesResponse = """
        {
            "albums": {
                "href": "https://api.spotify.com/v1/browse/new-releases?offset=0&limit=20",
                "items": [
                    {
                        "album_type": "single",
                        "artists": [
                            {
                                "external_urls": {
                                    "spotify": "https://open.spotify.com/artist/2RdwBSPQiwcmiDo9kixcl8"
                                },
                                "href": "https://api.spotify.com/v1/artists/2RdwBSPQiwcmiDo9kixcl8",
                                "id": "2RdwBSPQiwcmiDo9kixcl8",
                                "name": "Pharrell Williams",
                                "type": "artist",
                                "uri": "spotify:artist:2RdwBSPQiwcmiDo9kixcl8"
                            }
                        ],
                        "available_markets": [
                            "AD"
                        ],
                        "external_urls": {
                            "spotify": "https://open.spotify.com/album/5ZX4m5aVSmWQ5iHAPQpT71"
                        },
                        "href": "https://api.spotify.com/v1/albums/5ZX4m5aVSmWQ5iHAPQpT71",
                        "id": "5ZX4m5aVSmWQ5iHAPQpT71",
                        "name": "Runnin'",
                        "type": "album",
                        "uri": "spotify:album:5ZX4m5aVSmWQ5iHAPQpT71"
                    },
                    {
                        "album_type": "single",
                        "artists": [
                            {
                                "external_urls": {
                                    "spotify": "https://open.spotify.com/artist/3TVXtAsR1Inumwj472S9r4"
                                },
                                "href": "https://api.spotify.com/v1/artists/3TVXtAsR1Inumwj472S9r4",
                                "id": "3TVXtAsR1Inumwj472S9r4",
                                "name": "Drake2",
                                "type": "artist",
                                "uri": "spotify:artist:3TVXtAsR1Inumwj472S9r4"
                            },
                            {
                                "external_urls": {
                                    "spotify": "https://open.spotify.com/artist/3TVXtAsR1Inumwj472S9r4"
                                },
                                "href": "https://api.spotify.com/v1/artists/3TVXtAsR1Inumwj472S9r4",
                                "id": "3TVXtAsR1Inumwj472S9r4",
                                "name": "Drake3",
                                "type": "artist",
                                "uri": "spotify:artist:3TVXtAsR1Inumwj472S9r4"
                            }
                        ],
                        "available_markets": [
                            "AD"
                        ],
                        "external_urls": {
                            "spotify": "https://open.spotify.com/album/0geTzdk2InlqIoB16fW9Nd"
                        },
                        "href": "https://api.spotify.com/v1/albums/0geTzdk2InlqIoB16fW9Nd",
                        "id": "0geTzdk2InlqIoB16fW9Nd",
                        "name": "Sneakin'",
                        "type": "album",
                        "uri": "spotify:album:0geTzdk2InlqIoB16fW9Nd"
                    }
                ],
                "limit": 20,
                "next": null,
                "offset": 0,
                "previous": null,
                "total": 2
            }
        }""".replaceAll(spotifyServerUrl, resourceServerUrl);


    private static final String apiFeaturedPlaylistsResponse = """
        {
            "message": "Monday morning music, coming right up!",
            "playlists": {
                "href": "https://api.spotify.com/v1/browse/featured-playlists?offset=0&limit=20",
                "items": [
                    {
                        "collaborative": false,
                        "external_urls": {
                            "spotify": "http://open.spotify.com/user/spotify/playlist/6ftJBzU2LLQcaKefMi7ee7"
                        },
                        "href": "https://api.spotify.com/v1/users/spotify/playlists/6ftJBzU2LLQcaKefMi7ee7",
                        "id": "6ftJBzU2LLQcaKefMi7ee7",
                        "images": [
                            {
                                "height": 300,
                                "url": "https://i.scdn.co/image/7bd33c65ebd1e45975bbcbbf513bafe272f033c7",
                                "width": 300
                            }
                        ],
                        "name": "Monday Morning Mood",
                        "owner": {
                            "external_urls": {
                                "spotify": "http://open.spotify.com/user/spotify"
                            },
                            "href": "https://api.spotify.com/v1/users/spotify",
                            "id": "spotify",
                            "type": "user",
                            "uri": "spotify:user:spotify"
                        },
                        "public": null,
                        "snapshot_id": "WwGvSIVUkUvGvqjgj/bQHlRycYmJ2TkoIxYfoalWlmIZT6TvsgvGMgtQ2dGbkrAW",
                        "tracks": {
                            "href": "https://api.spotify.com/v1/users/spotify/playlists/6ftJBzU2LLQcaKefMi7ee7/tracks",
                            "total": 245
                        },
                        "type": "playlist",
                        "uri": "spotify:user:spotify:playlist:6ftJBzU2LLQcaKefMi7ee7"
                    },
                    {
                        "collaborative": false,
                        "external_urls": {
                            "spotify": "http://open.spotify.com/user/spotify__sverige/playlist/4uOEx4OUrkoGNZoIlWMUbO"
                        },
                        "href": "https://api.spotify.com/v1/users/spotify__sverige/playlists/4uOEx4OUrkoGNZoIlWMUbO",
                        "id": "4uOEx4OUrkoGNZoIlWMUbO",
                        "images": [
                            {
                                "height": 300,
                                "url": "https://i.scdn.co/image/24aa1d1b491dd529b9c03392f350740ed73438d8",
                                "width": 300
                            }
                        ],
                        "name": "Upp och hoppa!",
                        "owner": {
                            "external_urls": {
                                "spotify": "http://open.spotify.com/user/spotify__sverige"
                            },
                            "href": "https://api.spotify.com/v1/users/spotify__sverige",
                            "id": "spotify__sverige",
                            "type": "user",
                            "uri": "spotify:user:spotify__sverige"
                        },
                        "public": null,
                        "snapshot_id": "0j9Rcbt2KtCXEXKtKy/tnSL5r4byjDBOIVY1dn4S6GV73EEUgNuK2hU+QyDuNnXz",
                        "tracks": {
                            "href": "https://api.spotify.com/v1/users/spotify__sverige/playlists/4uOEx4OUrkoGNZoIlWMUbO/tracks",
                            "total": 38
                        },
                        "type": "playlist",
                        "uri": "spotify:user:spotify__sverige:playlist:4uOEx4OUrkoGNZoIlWMUbO"
                    }
                ],
                "limit": 20,
                "next": null,
                "offset": 0,
                "previous": null,
                "total": 2
            }
        }""".replaceAll(spotifyServerUrl, resourceServerUrl);

    private static final TempWebServerMock resourceServerMock = new TempWebServerMock(resourceServerPort)
            .setPage("/v1/browse/categories", apiCategoriesResponse)
            .setPage("/v1/browse/categories/party/playlists", apiPlaylistsPartyResponse)
            // unpredictable error on toplists request!!!
            .setPage("/v1/browse/categories/toplists/playlists", apiTestErrorResponse)
            .setPage("/v1/browse/new-releases", apiNewReleasesResponse)
            .setPage("/v1/browse/featured-playlists", apiFeaturedPlaylistsResponse);

    private static final MockTokenServer tokenServer = new MockTokenServer(accessServer);
    private static final MockTokenServer resourceServer = new MockTokenServer(resourceServerMock);

    public static void auth(TestedProgram userProgram) {

        Server server = new Server(userProgram, fictiveAuthCode);
        server.start();

        synchronized (accessServer) {
            if (!accessServer.isStarted())
                tokenServer.start();
        }

        synchronized (resourceServerMock) {
            if (!resourceServerMock.isStarted())
                resourceServer.start();
        }

        userProgram.goBackground();
        userProgram.execute("auth");

        try {
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (Server.checkResult != null) {
            throw new WrongAnswer(Server.checkResult.getFeedback());
        }

        userProgram.stopBackground();
    }

    @DynamicTest
    CheckResult testNewWithoutAuth() {

        TestedProgram userProgram = new TestedProgram();
        userProgram.start(arguments);
        userProgram.setReturnOutputAfterExecution(false);

        userProgram.execute("new");

        String outputAfterNew = userProgram.getOutput();

        if (!outputAfterNew.strip().startsWith("Please, provide access for application.")) {
            return CheckResult.wrong("When no access provided you should output " +
                    "\"Please, provide access for application.\"");
        }

        userProgram.execute("exit");
        if (!userProgram.isFinished()) {
            userProgram.stop();
        }

        return CheckResult.correct();
    }

    @DynamicTest
    CheckResult testFeaturedWithoutAuth() {

        TestedProgram userProgram = new TestedProgram();
        userProgram.start(arguments);
        userProgram.setReturnOutputAfterExecution(false);

        userProgram.execute("featured");

        String outputAfterNew = userProgram.getOutput();

        if (!outputAfterNew.strip().startsWith("Please, provide access for application.")) {
            return CheckResult.wrong("When no access provided you should output " +
                    "\"Please, provide access for application.\"");
        }

        userProgram.execute("exit");
        if (!userProgram.isFinished()) {
            userProgram.stop();
        }

        return CheckResult.correct();
    }

    @DynamicTest
    CheckResult testCategoriesWithoutAuth() {

        TestedProgram userProgram = new TestedProgram();
        userProgram.start(arguments);
        userProgram.setReturnOutputAfterExecution(false);

        userProgram.execute("categories");

        String outputAfterNew = userProgram.getOutput();

        if (!outputAfterNew.strip().startsWith("Please, provide access for application.")) {
            return CheckResult.wrong("When no access provided you should output " +
                    "\"Please, provide access for application.\"");
        }

        userProgram.execute("exit");
        if (!userProgram.isFinished()) {
            userProgram.stop();
        }

        return CheckResult.correct();
    }

    @DynamicTest
    CheckResult testPlaylistWithoutAuth() {

        TestedProgram userProgram = new TestedProgram();
        userProgram.start(arguments);
        userProgram.setReturnOutputAfterExecution(false);

        userProgram.execute("playlists Party Time");

        String outputAfterNew = userProgram.getOutput();

        if (!outputAfterNew.strip().startsWith("Please, provide access for application.")) {
            return CheckResult.wrong("When no access provided you should output " +
                    "\"Please, provide access for application.\"");
        }

        userProgram.execute("exit");
        if (!userProgram.isFinished()) {
            userProgram.stop();
        }

        return CheckResult.correct();
    }

    @DynamicTest
    CheckResult testAuth() {

        TestedProgram userProgram = new TestedProgram();

        userProgram.start(arguments);
        userProgram.setReturnOutputAfterExecution(false);

        auth(userProgram);

        userProgram.execute("exit");
        if (!userProgram.isFinished()) {
            userProgram.stop();
        }

        return CheckResult.correct();

    }

    @DynamicTest
    CheckResult testNew() {

        TestedProgram userProgram = new TestedProgram();

        userProgram.start(arguments);
        userProgram.setReturnOutputAfterExecution(false);

        auth(userProgram);

        userProgram.execute("new");

        if(!resourceServerMock.getAccess_token().contains(fictiveAccessToken)) {
            return CheckResult.wrong("Access token is incorrect!");
        }

        String outputAfterNew = userProgram.getOutput();

        String album1 =
                "Runnin'\n" +
                        "[Pharrell Williams]\n" +
                        "https://open.spotify.com/album/5ZX4m5aVSmWQ5iHAPQpT71"
                                .replaceAll(spotifyServerUrl, resourceServerUrl);

        String album2 =
                "Sneakin'\n" +
                        "[Drake2, Drake3]\n" +
                        "https://open.spotify.com/album/0geTzdk2InlqIoB16fW9Nd"
                                .replaceAll(spotifyServerUrl, resourceServerUrl);

        if (outputAfterNew.contains("Invalid access token")) {
            return CheckResult.wrong("Your answer was `Invalid access token` on `new` action. " +
                    "Make sure you use the server from -resource command line argument.");
        }

        if (!outputAfterNew.contains(album1) || !outputAfterNew.contains(album2)) {
            return CheckResult.wrong(
                    "There are no albums in correct format on \"new\" action. " +
                            "Make sure you use the server from -resource command line argument.");
        }

        userProgram.execute("exit");
        if (!userProgram.isFinished()) {
            userProgram.stop();
        }

        return CheckResult.correct();
    }

    @DynamicTest
    CheckResult testCategories() {

        TestedProgram userProgram = new TestedProgram();

        userProgram.start(arguments);
        userProgram.setReturnOutputAfterExecution(false);

        auth(userProgram);

        userProgram.execute("categories");

        if(!resourceServerMock.getAccess_token().contains(fictiveAccessToken)) {
            return CheckResult.wrong("An error occurred while retrieving the category list:\n" +
                    "Access token is incorrect!");
        }

        String outputAfterCategories = userProgram.getOutput();

        String category1 = "Top Lists";
        String category2 = "Super Mood";
        String category3 = "Party Time";

        if (!outputAfterCategories.contains(category1)
                || !outputAfterCategories.contains(category2)
                || !outputAfterCategories.contains(category3)) {

            return CheckResult.wrong("There are no categories in correct format on \"category\" action");
        }

        userProgram.execute("exit");
        if (!userProgram.isFinished()) {
            userProgram.stop();
        }

        return CheckResult.correct();
    }

    @DynamicTest
    CheckResult testFeatured() {

        TestedProgram userProgram = new TestedProgram();

        userProgram.start(arguments);
        userProgram.setReturnOutputAfterExecution(false);

        auth(userProgram);

        userProgram.execute("featured");

        if(!resourceServerMock.getAccess_token().contains(fictiveAccessToken)) {
            return CheckResult.wrong("Access token is incorrect!");
        }

        String outputAfterFeatured = userProgram.getOutput();

        String featured1 =
                "Monday Morning Mood\n" +
                        "http://open.spotify.com/user/spotify/playlist/6ftJBzU2LLQcaKefMi7ee7"
                                .replaceAll(spotifyServerUrl, resourceServerUrl);

        String featured2 =
                "Upp och hoppa!\n" +
                        "http://open.spotify.com/user/spotify__sverige/playlist/4uOEx4OUrkoGNZoIlWMUbO"
                                .replaceAll(spotifyServerUrl, resourceServerUrl);

        if (!outputAfterFeatured.contains(featured1)
                || !outputAfterFeatured.contains(featured2)) {

            return CheckResult.wrong("There are no featured playlists in correct format on \"featured\" action");
        }

        userProgram.execute("exit");
        if (!userProgram.isFinished()) {
            userProgram.stop();
        }

        return CheckResult.correct();
    }

    @DynamicTest
    CheckResult testPartyPlayList() {

        TestedProgram userProgram = new TestedProgram();

        userProgram.start(arguments);
        userProgram.setReturnOutputAfterExecution(false);

        auth(userProgram);

        userProgram.execute("playlists Party Time");

        if(!resourceServerMock.getAccess_token().contains(fictiveAccessToken)) {
            return CheckResult.wrong("Access token is incorrect!");
        }

        String outputAfterPartyPlaylist = userProgram.getOutput();

        String playlist1 =
                "Noite Eletronica\n" +
                        "http://open.spotify.com/user/spotifybrazilian/playlist/4k7EZPI3uKMz4aRRrLVfen"
                                .replaceAll(spotifyServerUrl, resourceServerUrl);

        String playlist2 =
                "Festa Indie\n" +
                        "http://open.spotify.com/user/spotifybrazilian/playlist/4HZh0C9y80GzHDbHZyX770"
                                .replaceAll(spotifyServerUrl, resourceServerUrl);

        if (!outputAfterPartyPlaylist.contains(playlist1)
                || !outputAfterPartyPlaylist.contains(playlist2)) {
            return CheckResult.wrong("There are no playlists in correct format on \"playlists {name}\" action. " +
                    "Make sure you correctly parsed the category name.");
        }

        userProgram.execute("exit");
        if (!userProgram.isFinished()) {
            userProgram.stop();
        }

        return CheckResult.correct();
    }

    @DynamicTest
    CheckResult testUnknownPlayList() {

        TestedProgram userProgram = new TestedProgram();

        userProgram.start(arguments);
        userProgram.setReturnOutputAfterExecution(false);

        auth(userProgram);

        userProgram.execute("playlists Party Time");

        if(!resourceServerMock.getAccess_token().contains(fictiveAccessToken)) {
            return CheckResult.wrong("Access token is incorrect!");
        }

        String outputAfterUnknownPlaylist = userProgram.getOutput();

        String playlist1 =
                "Noite Eletronica\n" +
                        "http://open.spotify.com/user/spotifybrazilian/playlist/4k7EZPI3uKMz4aRRrLVfen"
                                .replaceAll(spotifyServerUrl, resourceServerUrl);

        String playlist2 =
                "Festa Indie\n" +
                        "http://open.spotify.com/user/spotifybrazilian/playlist/4HZh0C9y80GzHDbHZyX770"
                                .replaceAll(spotifyServerUrl, resourceServerUrl);

        if (!outputAfterUnknownPlaylist.contains(playlist1)
                || !outputAfterUnknownPlaylist.contains(playlist2)) {

            return CheckResult.wrong("There are no playlists in correct format on \"playlists {name}\" action. " +
                    "Make sure you correctly parsed the category name.");
        }

        userProgram.execute("exit");
        if (!userProgram.isFinished()) {
            userProgram.stop();
        }

        return CheckResult.correct();
    }

    @DynamicTest
    CheckResult testTopPlayList() {

        TestedProgram userProgram = new TestedProgram();

        userProgram.start(arguments);
        userProgram.setReturnOutputAfterExecution(false);

        auth(userProgram);

        userProgram.execute("playlists Top Lists");

        if(!resourceServerMock.getAccess_token().contains(fictiveAccessToken)) {
            return CheckResult.wrong("Access token is incorrect!");
        }

        String outputAfterUnknownPlaylist = userProgram.getOutput();

        if (!outputAfterUnknownPlaylist.contains(testErrorMessage)) {
            return new CheckResult(false,
                    "You got a json with unpredictable error from the api. " +
                            "Error message should be parsed from the api response and printed.");
        }

        userProgram.execute("exit");
        if (!userProgram.isFinished()) {
            userProgram.stop();
        }

        return CheckResult.correct();
    }

    @AfterClass
    public static void afterTest() {
        tokenServer.stopMock();
        resourceServer.stopMock();
    }

}