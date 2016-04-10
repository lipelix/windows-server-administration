package cz.lip.windowsserveradministration.communication.response;

/**
 * An interface for implementation in a {@link com.android.volley.Request} to
 * support custom authentication headers.
 */
public interface AuthorizableRequest {

    public void addHeader(String header, String value);

}
