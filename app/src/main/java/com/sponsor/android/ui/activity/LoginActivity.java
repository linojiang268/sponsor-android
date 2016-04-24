package com.sponsor.android.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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

import com.alibaba.fastjson.JSON;
import com.sponsor.android.Constant;
import com.sponsor.android.R;
import com.sponsor.android.constract.ui.activity.BaseActivity;
import com.sponsor.android.data.UserPref;
import com.sponsor.android.entity.UserEntity;
import com.sponsor.android.net.OkHttpManager;
import com.sponsor.android.net.OkHttpPostRequestBuilder;
import com.sponsor.android.net.OkHttpRequestBuilderFactory;
import com.sponsor.android.util.Checker;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    private Intent resultIntent;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView metMobile;
    private EditText metPass;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        resultIntent = getIntent();

        // Set up the login form.
        metMobile = (AutoCompleteTextView) findViewById(R.id.etMobile);

        metPass = (EditText) findViewById(R.id.etPass);
        metPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.btLogin);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mMobileRegsitButton = (Button) findViewById(R.id.btRegist);
        mMobileRegsitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(LoginActivity.this, RegistActivity.class), 1);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        metMobile.setError(null);
        metPass.setError(null);

        // Store values at the time of the login attempt.
        String mobile = metMobile.getText().toString();
        String pass = metPass.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(pass) && !isPasswordValid(pass)) {
            metPass.setError(getString(R.string.error_invalid_password));
            focusView = metPass;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mobile)) {
            metMobile.setError("手机号必填");
            focusView = metMobile;
            cancel = true;
        } else if (!isMobileValid(mobile)) {
            metMobile.setError("手机号格式错误");
            focusView = metMobile;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(mobile, pass);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isMobileValid(String mobile) {
        return Checker.isMobilePhone(mobile);
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mMobile;
        private final String mPassword;

        UserLoginTask(String mobile, String password) {
            mMobile = mobile;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                OkHttpPostRequestBuilder builder = OkHttpRequestBuilderFactory.createPostRequestBuilder(Constant.REQUEST_HOST + "api/user/login");
                builder.put("mobile", mMobile);
                builder.put("password", mPassword);

                final Response response = OkHttpManager.syncPost(builder);
                if (null != response && response.isSuccessful()) {
                    String content = response.body().string();
                    JSONObject data = new JSONObject(content);
                    if (0 == data.getInt("code")) {
                        UserEntity user = JSON.parseObject(content, UserEntity.class);
                        UserPref.getInstance().resetUser(user);
                        setLoginSuccessResult();
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                metPass.setError(getString(R.string.error_incorrect_password));
                metPass.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private void setLoginSuccessResult() {
        if (null != resultIntent) {
            setResult(ACTIVITY_RESULT_CODE_INTENT_AFTER_LOGIN, resultIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 10006:
                metMobile.setText(data.getStringExtra("mobile"));
                metPass.setText(data.getStringExtra("password"));
                attemptLogin();
                break;
        }
    }
}

