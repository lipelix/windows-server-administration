package cz.mowin.communication.response;

/**
 * Model class for error response. Error response from server api is deserialized to this class.
 * @author Libor Vachal
 */
public class ErrorResponse {

    private String error;

    @Override
    public String toString() {
        return error;
    }
}
