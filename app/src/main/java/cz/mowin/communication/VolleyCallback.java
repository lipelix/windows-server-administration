package cz.mowin.communication;

/**
 * Interface of webserver response callback
 */
public interface VolleyCallback {

    /**
     * Called after successful response
     * @param response response from webserver
     */
    void onSuccess(String response);

}