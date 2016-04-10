package cz.lip.windowsserveradministration.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import cz.lip.windowsserveradministration.AppController;
import cz.lip.windowsserveradministration.R;
import cz.lip.windowsserveradministration.communication.Api;
import cz.lip.windowsserveradministration.communication.VolleyCallback;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mLogingView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private ProgressDialog progressDialog;
    private Api api;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mLogingView = (AutoCompleteTextView) findViewById(R.id.login_input);

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

        mLoginFormView = findViewById(R.id.login_form);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.login_msg));

        pref = this.getSharedPreferences(AppController.PREF_NAME, this.MODE_PRIVATE);
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
        String login = mLogingView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

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

            api.getToken(login, password, new VolleyCallback() {
                @Override
                public void onSuccess(String response) {
                    Toast.makeText(AppController.getAppContext(), response, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject json = new JSONObject(response);
                        pref.edit().putString("access_token", json.getString("access_token"));
                        startActivity(intent);

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

    private boolean isLoginValid(String login) {
        return login.matches("[a-zA-Z0-9]+");
    }

    private boolean isPasswordValid(String password) {
        return password.matches("[a-zA-Z0-9]+");
    }
}

