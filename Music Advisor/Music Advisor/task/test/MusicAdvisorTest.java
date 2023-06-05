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
            resourceServerUrl,
            "-page",
            "1"
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
        }""";


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
                        "images": [
                            {
                                "height": 640,
                                "url": "https://i.scdn.co/image/e6b635ebe3ef4ba22492f5698a7b5d417f78b88a",
                                "width": 640
                            },
                            {
                                "height": 300,
                                "url": "https://i.scdn.co/image/92ae5b0fe64870c09004dd2e745a4fb1bf7de39d",
                                "width": 300
                            },
                            {
                                "height": 64,
                                "url": "https://i.scdn.co/image/8a7ab6fc2c9f678308ba0f694ecd5718dc6bc930",
                                "width": 64
                            }
                        ],
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
                        "images": [
                            {
                                "height": 640,
                                "url": "https://i.scdn.co/image/d40e9c3d22bde2fbdb2ecc03cccd7a0e77f42e4c",
                                "width": 640
                            },
                            {
                                "height": 300,
                                "url": "https://i.scdn.co/image/dff06a3375f6d9b32ecb081eb9a60bbafecb5731",
                                "width": 300
                            },
                            {
                                "height": 64,
                                "url": "https://i.scdn.co/image/808a02bd7fc59b0652c9df9f68675edbffe07a79",
                                "width": 64
                            }
                        ],
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
        }""";

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

    private void checkAlbum1(String reply) {
        String album1 = """
            Runnin'
            [Pharrell Williams]
            https://open.spotify.com/album/5ZX4m5aVSmWQ5iHAPQpT71"""
            .replaceAll(spotifyServerUrl, resourceServerUrl);

        String album2 = """
            Sneakin'
            [Drake2]
            https://open.spotify.com/album/0geTzdk2InlqIoB16fW9Nd"""
            .replaceAll(spotifyServerUrl, resourceServerUrl);

        if (!reply.contains(album1)) {
            throw new WrongAnswer("Album from page 1 not appeared on \"new\" action");
        }
        if (reply.contains(album2)) {
            throw new WrongAnswer("Album from page 2 appeared on page 1 on \"new\" action");
        }
        if (!reply.contains("---PAGE 1 OF 2---")) {
            throw new WrongAnswer("Something wrong with pagination format. Not found ---PAGE 1 OF 2---");
        }
    }

    private void checkAlbum2(String reply) {
        String album1 = """
            Runnin'
            [Pharrell Williams]
            https://open.spotify.com/album/5ZX4m5aVSmWQ5iHAPQpT71"""
            .replaceAll(spotifyServerUrl, resourceServerUrl);

        String album2 = """
            Sneakin'
            [Drake2]
            https://open.spotify.com/album/0geTzdk2InlqIoB16fW9Nd"""
            .replaceAll(spotifyServerUrl, resourceServerUrl);

        if (!reply.contains(album2)) {
            throw new WrongAnswer("Album from page 2 not appeared on \"new\" action");
        }
        if (reply.contains(album1)) {
            throw new WrongAnswer("Album from page 1 appeared on page 2 on \"new\" action");
        }

        if (!reply.contains("---PAGE 2 OF 2---")) {
            throw new WrongAnswer("Something wrong with pagination format. Not found ---PAGE 2 OF 2---");
        }
    }

    private int countAppearances(String str, String findStr) {
        int lastIndex = 0;
        int count = 0;

        while (lastIndex != -1) {
            lastIndex = str.indexOf(findStr, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += findStr.length();
            }
        }
        return count;
    }

    @DynamicTest
    CheckResult testAuth() {

        TestedProgram userProgram = new TestedProgram();
        userProgram.start(arguments);
        userProgram.setReturnOutputAfterExecution(false);

        auth(userProgram);

        userProgram.execute("exit");
        userProgram.stop();

        return CheckResult.correct();

    }

    void testNewAlbums(TestedProgram userProgram) {
        userProgram.execute("new");

        String output = userProgram.getOutput();
        checkAlbum1(output);

        userProgram.execute("prev");
        output = userProgram.getOutput();
        if (!output.contains("No more pages")) {
            throw new WrongAnswer("Your output should be `No more pages` on -1 page.");
        }

        userProgram.execute("next");
        output = userProgram.getOutput();
        checkAlbum2(output);

        userProgram.execute("next");
        output = userProgram.getOutput();
        if (!output.contains("No more pages")) {
            throw new WrongAnswer("Your output should be `No more pages` after the last page.");
        }

        userProgram.execute("prev");
        output = userProgram.getOutput();
        checkAlbum1(output);

        userProgram.execute("exit");
    }

    @DynamicTest
    CheckResult testNew() {

        TestedProgram userProgram = new TestedProgram();
        userProgram.start(arguments);
        userProgram.setReturnOutputAfterExecution(false);
        auth(userProgram);

        if(!resourceServerMock.getAccess_token().contains(fictiveAccessToken)) {
            return CheckResult.wrong("Access Token is incorrect!");
        }

        testNewAlbums(userProgram);
        testNewAlbums(userProgram);

        userProgram.stop();

        return CheckResult.correct();
    }

    @DynamicTest
    CheckResult testCategoriesNextPrev() {

        TestedProgram userProgram = new TestedProgram();
        userProgram.start(arguments);
        userProgram.setReturnOutputAfterExecution(false);
        auth(userProgram);

        String category1 = "Top Lists";
        String category2 = "Super Mood";
        String category3 = "Party Time";



        userProgram.execute("categories");

        if(!resourceServerMock.getAccess_token().contains(fictiveAccessToken)) {
            return CheckResult.wrong("An error occurred while retrieving the category list:\n" +
                    "Access token is incorrect!");
        }

        String output = userProgram.getOutput();
        if (!output.contains("---PAGE 1 OF 3---")) {
            return CheckResult.wrong("Something wrong with pagination format. Not found ---PAGE 1 OF 3---");
        }
        if (countAppearances(output, category1) != 1
                || countAppearances(output, category2) != 0
                || countAppearances(output, category3) != 0) {
            return CheckResult.wrong("Something wrong with showing categories and pages.\n" +
                                     "Should include \"" + category1 + "\" once, " +
                                     "exclude \"" + category2 + "\", and \"" + category3 + "\".");
        }

        userProgram.execute("next");
        output = userProgram.getOutput();
        if (!output.contains("---PAGE 2 OF 3---")) {
            return CheckResult.wrong("Something wrong with pagination format. Not found ---PAGE 2 OF 3---");
        }
        if (countAppearances(output, category1) != 0
                || countAppearances(output, category2) != 1
                || countAppearances(output, category3) != 0) {
            return CheckResult.wrong("Something wrong with showing categories and pages.\n" +
                                     "Should include \"" + category2 + "\" once, " +
                                     "exclude \"" + category1 + "\", and \"" + category3 + "\".");
        }

        userProgram.execute("next");
        output = userProgram.getOutput();
        if (!output.contains("---PAGE 3 OF 3---")) {
            return CheckResult.wrong("Something wrong with pagination format. Not found ---PAGE 3 OF 3---");
        }
        if (countAppearances(output, category1) != 0
                || countAppearances(output, category2) != 0
                || countAppearances(output, category3) != 1) {
            return CheckResult.wrong("Something wrong with showing categories and pages.\n" +
                                     "Should include \"" + category3 + "\" once, " +
                                     "exclude \"" + category1 + "\", and \"" + category2 + "\".");
        }

        userProgram.execute("prev");
        output = userProgram.getOutput();
        if (!output.contains("---PAGE 2 OF 3---")) {
            return CheckResult.wrong("Something wrong with pagination format. Not found ---PAGE 2 OF 3---");
        }
        if (countAppearances(output, category1) != 0
                || countAppearances(output, category2) != 1
                || countAppearances(output, category3) != 0) {
            return CheckResult.wrong("Something wrong with showing categories and pages.\n" +
                                     "Should include \"" + category2 + "\" once, " +
                                     "exclude \"" + category1 + "\", and \"" + category3 + "\".");
        }

        userProgram.execute("prev");
        output = userProgram.getOutput();
        if (!output.contains("---PAGE 1 OF 3---")) {
            return CheckResult.wrong("Something wrong with pagination format. Not found ---PAGE 1 OF 3---");
        }
        if (countAppearances(output, category1) != 1
                || countAppearances(output, category2) != 0
                || countAppearances(output, category3) != 0) {
            return CheckResult.wrong("Something wrong with showing categories and pages.\n" +
                                     "Should include \"" + category1 + "\" once, " +
                                     "exclude \"" + category2 + "\", and \"" + category3 + "\".");
        }

        userProgram.stop();
        return CheckResult.correct();
    }

    @DynamicTest
    CheckResult testFeatured() {

        TestedProgram userProgram = new TestedProgram();
        userProgram.start(arguments);
        userProgram.setReturnOutputAfterExecution(false);
        auth(userProgram);

        String featured1 =
                "Monday Morning Mood\n" +
                "http://open.spotify.com/user/spotify/playlist/6ftJBzU2LLQcaKefMi7ee7"
                .replaceAll(spotifyServerUrl, resourceServerUrl);

        String featured2 =
                "Upp och hoppa!\n" +
                "http://open.spotify.com/user/spotify__sverige/playlist/4uOEx4OUrkoGNZoIlWMUbO"
                .replaceAll(spotifyServerUrl, resourceServerUrl);

        userProgram.execute("featured");

        if(!resourceServerMock.getAccess_token().contains(fictiveAccessToken)) {
            return CheckResult.wrong("Access Token is incorrect!");
        }

        String output = userProgram.getOutput();
        if (!output.contains("---PAGE 1 OF 2---")) {
            return CheckResult.wrong("Something wrong with pagination format. Not found ---PAGE 1 OF 2---");
        }
        if (countAppearances(output, featured1) != 1
                || countAppearances(output, featured2) != 0) {
            return CheckResult.wrong("Something wrong with showing featured playlists and pages");
        }

        userProgram.execute("next");
        output = userProgram.getOutput();
        if (!output.contains("---PAGE 2 OF 2---")) {
            return CheckResult.wrong("Something wrong with pagination format. Not found ---PAGE 2 OF 2---");
        }
        if (countAppearances(output, featured1) != 0
                || countAppearances(output, featured2) != 1) {
            return CheckResult.wrong("Something wrong with showing featured playlists and pages");
        }

        userProgram.execute("prev");
        output = userProgram.getOutput();
        if (!output.contains("---PAGE 1 OF 2---")) {
            return CheckResult.wrong("Something wrong with pagination format. Not found ---PAGE 1 OF 2---");
        }
        if (countAppearances(output, featured1) != 1
                || countAppearances(output, featured2) != 0) {
            return CheckResult.wrong("Something wrong with showing featured playlists and pages");
        }

        userProgram.execute("exit");
        if (!userProgram.isFinished()) {
            userProgram.stop();
        }

        return CheckResult.correct();
    }

    @DynamicTest
    CheckResult testPlayList() {

        TestedProgram userProgram = new TestedProgram();
        userProgram.start(arguments);
        userProgram.setReturnOutputAfterExecution(false);
        auth(userProgram);

        String playlist1 =
                "Noite Eletronica\n" +
                "http://open.spotify.com/user/spotifybrazilian/playlist/4k7EZPI3uKMz4aRRrLVfen"
                .replaceAll(spotifyServerUrl, resourceServerUrl);

        String playlist2 =
                "Festa Indie\n" +
                "http://open.spotify.com/user/spotifybrazilian/playlist/4HZh0C9y80GzHDbHZyX770"
                .replaceAll(spotifyServerUrl, resourceServerUrl);

        userProgram.execute("playlists Party Time");

        if(!resourceServerMock.getAccess_token().contains(fictiveAccessToken)) {
            return CheckResult.wrong("Access Token is incorrect!");
        }

        String output = userProgram.getOutput();
        if (!output.contains("---PAGE 1 OF 2---")) {
            return CheckResult.wrong("Something wrong with pagination format. Not found ---PAGE 1 OF 2---");
        }
        if (countAppearances(output, playlist1) != 1
                || countAppearances(output, playlist2) != 0) {
            return CheckResult.wrong("Something wrong with showing playlists and pages");
        }

        userProgram.execute("next");
        output = userProgram.getOutput();
        if (!output.contains("---PAGE 2 OF 2---")) {
            return CheckResult.wrong("Something wrong with pagination format. Not found ---PAGE 2 OF 2---");
        }
        if (countAppearances(output, playlist1) != 0
                || countAppearances(output, playlist2) != 1) {
            return CheckResult.wrong("Something wrong with showing playlists and pages");
        }

        userProgram.execute("prev");
        output = userProgram.getOutput();
        if (!output.contains("---PAGE 1 OF 2---")) {
            return CheckResult.wrong("Something wrong with pagination format. Not found ---PAGE 1 OF 2---");
        }
        if (countAppearances(output, playlist1) != 1
                || countAppearances(output, playlist2) != 0) {
            return CheckResult.wrong("Something wrong with showing playlists and pages");
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