package cz.mowin.communication;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import java.io.IOException;

import cz.mowin.communication.response.AuthorizableRequest;

/**
 * Wrapper around GsonRequest for use with
 * an {@link android.accounts.AccountManager}. The actual {@link com.android.volley.Request}
 * is executed when an authentication token has been obtained.
 */
public class AuthenticatedRequestCallback implements AccountManagerCallback<Bundle> {
    public static final String TAG = AuthenticatedRequestCallback.class.getSimpleName();

    public static final String AUTH_TOKEN_PARAM = "token";
    public static final String AUTH_TOKEN_HEADER = "X-Auth-Token";

    private Request mRequest;
    private RequestQueue mRequestQueue;

    private final AuthenticationErrorListener mErrorListener;

    /**
     * Callback interface to listen for errors thrown by the
     * {@link android.accounts.AccountManager}.
     */
    public interface AuthenticationErrorListener {
        public void onAuthenticationError(AuthenticatorException e);
    }

    public AuthenticatedRequestCallback(Request request, RequestQueue requestQueue,
                                        AuthenticationErrorListener listener) {
        this.mRequest = request;
        this.mRequestQueue = requestQueue;
        this.mErrorListener = listener;
    }

    @Override
    public void run(AccountManagerFuture<Bundle> result) {
        Bundle bundle;
        try {
            bundle = result.getResult();
        } catch (OperationCanceledException | IOException e) {
            if (mErrorListener != null) {
                mErrorListener.onAuthenticationError(new AuthenticatorException(e.getMessage()));
            }
            return;
        } catch (AuthenticatorException e) {
            if (mErrorListener != null) {
                mErrorListener.onAuthenticationError(e);
            }
            return;
        }

        String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);

        if (!TextUtils.isEmpty(authToken)) {
            Log.d(TAG, "Received authentication token " + authToken);
            try {
                ((AuthorizableRequest) mRequest)
                        .addHeader(AUTH_TOKEN_HEADER, authToken);
            } catch (ClassCastException e) {
                throw new ClassCastException(mRequest.toString()
                        + " must implement " + AuthorizableRequest.class.getSimpleName());
            }
            // Queue the request for execution
            mRequestQueue.add(mRequest);
        } else {
            if (mErrorListener != null) {
                mErrorListener.onAuthenticationError(
                        new AuthenticatorException("Authentication token is empty."));
            }
        }
    }
}
