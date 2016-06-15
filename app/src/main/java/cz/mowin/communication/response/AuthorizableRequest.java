package cz.mowin.communication.response;

/**
 * An interface for implementation in a {@link com.android.volley.Request} to
 * support custom authentication headers.
 */
public interface AuthorizableRequest {

    /**
     * Add header parameter
     * @param header header key
     * @param value header value
     */
    void addHeader(String header, String value);

}
