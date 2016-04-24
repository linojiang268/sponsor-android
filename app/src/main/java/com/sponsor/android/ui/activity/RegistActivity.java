package com.sponsor.android.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.orhanobut.logger.Logger;
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

public class RegistActivity extends BaseActivity {

    private UserRegistTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mMobileView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mRegistFormView;
    private EditText metNickName, metCaptcha;
    private SimpleDraweeView mivCaptcha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist);

        mMobileView = (AutoCompleteTextView) findViewById(R.id.mobile);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptRegist();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.mobile_regist_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegist();
            }
        });

        mRegistFormView = findViewById(R.id.regist_form);
        mProgressView = findViewById(R.id.regist_progress);
        metNickName = (EditText) findViewById(R.id.etNickName);
        metCaptcha = (EditText) findViewById(R.id.etCaptcha);

        mivCaptcha = (SimpleDraweeView) findViewById(R.id.ivCaptcha);
        mivCaptcha.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshCaptcha();
            }
        });
        this.refreshCaptcha();
        mMobileView.setText("15882334812");
        mPasswordView.setText("123456");
        metNickName.setText("ouArea");
    }

    private void refreshCaptcha() {
        Uri uri = Uri.parse(Constant.REQUEST_HOST + "captcha");
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(mivCaptcha.getController())
                .build();
        mivCaptcha.setController(controller);
    }

    public void attemptRegist() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mMobileView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mMobileView.getText().toString();
        String password = mPasswordView.getText().toString();
        String nickName = metNickName.getText().toString();
        String captcha = metCaptcha.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mMobileView.setError(getString(R.string.error_field_required));
            focusView = mMobileView;
            cancel = true;
        } else if (!isMobileValid(email)) {
            mMobileView.setError(getString(R.string.error_invalid_email));
            focusView = mMobileView;
            cancel = true;
        } else if (TextUtils.isEmpty(nickName)) {
            metNickName.setError("昵称未填写");
            focusView = metNickName;
            cancel = true;
        } else if (TextUtils.isEmpty(captcha)) {
            metCaptcha.setError("验证码未填写");
            focusView = metCaptcha;
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
            mAuthTask = new UserRegistTask(email, password, nickName, captcha);
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

            mRegistFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegistFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegistFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegistFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegistTask extends AsyncTask<Void, Void, Boolean> {

        private final String mMobile;
        private final String mPassword;
        private final String mNickName;
        private final String mCaptcha;

        UserRegistTask(String mobile, String password, String nickName, String captcha) {
            mMobile = mobile;
            mPassword = password;
            mNickName = nickName;
            mCaptcha = captcha;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Logger.i("zhixingle");
                OkHttpPostRequestBuilder builder = OkHttpRequestBuilderFactory.createPostRequestBuilder(Constant.REQUEST_HOST + "api/user/register");
                builder.put("mobile", mMobile);
                builder.put("password", mPassword);
                builder.put("nick_name", mNickName);
                builder.put("captcha", mCaptcha);

                final Response response = OkHttpManager.syncPost(builder);
                Logger.i(response.code() + "");
Logger.i(response.message());
                if (null != response && response.isSuccessful()) {
                    String content = response.body().string();
                    JSONObject data = new JSONObject(content);
                    if (0 == data.getInt("code")) {
                        UserEntity user = JSON.parseObject(content, UserEntity.class);
                        UserPref.getInstance().resetUser(user);
                        setRegistSuccessResult();
                        return true;
                    }
                }
                Logger.i("wanle");
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
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private void setRegistSuccessResult() {
        Intent intent = new Intent();
        intent.putExtra("mobile", mMobileView.getText().toString());
        intent.putExtra("password", mPasswordView.getText().toString());
        setResult(10006, intent);
    }
}

