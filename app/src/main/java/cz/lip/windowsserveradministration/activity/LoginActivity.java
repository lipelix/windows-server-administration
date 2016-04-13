package cz.lip.windowsserveradministration.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import cz.lip.windowsserveradministration.AppController;
import cz.lip.windowsserveradministration.R;
import cz.lip.windowsserveradministration.communication.Api;
import cz.lip.windowsserveradministration.communication.VolleyCallback;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity {

    private String TAG = "LoginActivity";
    private AutoCompleteTextView mLogingView;
    private AutoCompleteTextView mHostView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private ProgressDialog progressDialog;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mLoginFormView = findViewById(R.id.login_form);
        mLogingView = (AutoCompleteTextView) findViewById(R.id.login_input);
        mHostView = (AutoCompleteTextView) findViewById(R.id.host_input);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        api = Api.getInstance(this);
    }

    /**
     * Attempts to sign in account specified by the login form.
     * If there are form errors (invalid login, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mLogingView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String login = mLogingView.getText().toString();
        String password = mPasswordView.getText().toString();
        final String host = mHostView.getText().toString().replaceAll("/*$", "");

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(host)) {
            mHostView.setError(getString(R.string.error_field_required));
            focusView = mHostView;
            cancel = true;
        } else if (!isHostValid(host)) {
            mHostView.setError(getString(R.string.error_invalid_host));
            focusView = mHostView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(login)) {
            mLogingView.setError(getString(R.string.error_field_required));
            focusView = mLogingView;
            cancel = true;
        } else if (!isLoginValid(login)) {
            mLogingView.setError(getString(R.string.error_invalid_login));
            focusView = mLogingView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            final Intent intent = new Intent(this, MainActivity.class);
            api.setHost(host);

            api.getToken(login, password, new VolleyCallback() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject json = new JSONObject(response);
                        AppController.save("access_token", json.getString("access_token"));

                        Date now = new Date(System.currentTimeMillis());
                        AppController.saveLong("access_token_expires", now.getTime() + (json.getLong("expires_in") * 1000));

                        AppController.save("login", login);
                        AppController.save("host", host);
                        api.setHost(host);

                        startActivity(intent);
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(VolleyError error) {

                }

                @Override
                public void onScriptError(String error) {

                }
            });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "resumed");
        api.setActivityContext(this);

        ArrayList<String> logins = new ArrayList<String>();
        logins.add(AppController.getPref().getString("login", ""));
        ArrayAdapter<String> loginAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, logins);
        AutoCompleteTextView actvLogin = (AutoCompleteTextView)findViewById(R.id.login_input);
        actvLogin.setThreshold(1);
        actvLogin.setAdapter(loginAdapter);

        actvLogin.setText(AppController.getPref().getString("login", ""));

        ArrayList<String> hosts = new ArrayList<String>();
        hosts.add(AppController.getPref().getString("host", ""));
        ArrayAdapter<String> hostAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, hosts);
        AutoCompleteTextView actvHost = (AutoCompleteTextView)findViewById(R.id.host_input);
        actvHost.setThreshold(1);
        actvHost.setAdapter(hostAdapter);

        actvHost.setText(AppController.getPref().getString("host", ""));
    }

    private boolean isLoginValid(String login) {
        return login.matches("[a-zA-Z0-9]+");
    }

    private boolean isPasswordValid(String password) {
        return password.matches("[a-zA-Z0-9]+");
    }

    private boolean isHostValid(String host) {
        return Patterns.WEB_URL.matcher(host).matches();
    }
}

