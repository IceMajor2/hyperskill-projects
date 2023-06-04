package advisor;

public class HttpRequestService {

    // singleton
    private static HttpRequestService INSTANCE;

    private HttpRequestService() {}

    public static HttpRequestService getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new HttpRequestService();
        }

        return INSTANCE;
    }
}
