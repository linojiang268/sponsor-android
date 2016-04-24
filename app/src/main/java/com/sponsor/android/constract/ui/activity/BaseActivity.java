package com.sponsor.android.constract.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.sponsor.android.constract.lifecycle.IComponentContainer;
import com.sponsor.android.constract.lifecycle.LifeCycleComponent;
import com.sponsor.android.constract.lifecycle.LifeCycleComponentManager;
import com.sponsor.android.data.UserPref;
import com.sponsor.android.manager.AppManage;
import com.sponsor.android.net.OkHttpManager;
import com.sponsor.android.ui.activity.LoginActivity;

public class BaseActivity extends AppCompatActivity implements IComponentContainer, ActivityInter {
    public static final String BROADCAST_FLAG = "com.sponsor.broadcast.common";
    public static final int ACTIVITY_RESULT_CODE_INTENT_AFTER_LOGIN = 10001;
    public static final String ACTIVITY_RESULT_KEY_TARGET_ACTIVITY = "target_activity";

    private LifeCycleComponentManager mComponentContainer = new LifeCycleComponentManager();
    private static LinearInterpolator interpolator = new LinearInterpolator();

    private CommonBroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManage.getInstance().addActivity(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void finish() {
        AppManage.getInstance().finishActivity(this);
    }

    @Override
    public void finishActivity() {
        super.finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mComponentContainer.onBecomesVisibleFromTotallyInvisible();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mComponentContainer.onBecomesVisibleFromPartiallyInvisible();

        if (mBroadcastReceiver == null){
            mBroadcastReceiver = new CommonBroadcastReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(BROADCAST_FLAG);
        registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mComponentContainer.onBecomesPartiallyInvisible();
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onStop() {
        super.onStop();
        mComponentContainer.onBecomesTotallyInvisible();
        OkHttpManager.instance().cancel(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mComponentContainer.onDestroy();
    }

    @Override
    public void addComponent(LifeCycleComponent component) {
        mComponentContainer.addComponent(component);
    }

    /**
     * 开始执行contentView动画
     */
    protected void startContentViewAnimation(View contentView, AnimatorListenerAdapter onAnimationEnd) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(contentView, "alpha", 1),
                ObjectAnimator.ofFloat(contentView, "translationY", 0)
        );
        set.setDuration(400).start();
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(onAnimationEnd);
    }

    /**
     * toast message
     *
     * @param text
     */
    protected void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * toast message
     *
     * @param resource
     */
    protected void toast(int resource) {
        Toast.makeText(this, resource, Toast.LENGTH_SHORT).show();
    }

    protected boolean login(Class targetClass) {
        if (UserPref.getInstance().hasLogin()) {
            if (null != targetClass) {
                startActivity(new Intent(BaseActivity.this, targetClass));
                return true;
            }
        } else {
            Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
            if (null != targetClass) {
                intent.putExtra(ACTIVITY_RESULT_KEY_TARGET_ACTIVITY, targetClass);
            }
            startActivityForResult(intent, 1);
        }
        return false;
    }

    @Override
      protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case ACTIVITY_RESULT_CODE_INTENT_AFTER_LOGIN:
                if (null != data && null != data.getExtras() && data.getExtras().containsKey(ACTIVITY_RESULT_KEY_TARGET_ACTIVITY)) {
                    if (UserPref.getInstance().hasLogin()) {
                        startActivity(new Intent(BaseActivity.this, (Class) data.getExtras().get(ACTIVITY_RESULT_KEY_TARGET_ACTIVITY)));
                    }
                }
                break;
        }
    }

    private class CommonBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.hasExtra("TYPE")){
                int type = intent.getIntExtra("TYPE", -1);
                switch (type){
                    case 1://cookie失效，需要重新登录
                        toast("您的登录状态已经失效，请重新登录");
                        UserPref.getInstance().clear();
                        AppManage.getInstance().finishOther();
                        startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                        break;
                }
            }
        }
    }

//    private void showWarningDialog(String content, String btnTxt,OnBtnClickL l){
//        mWarningDialog = DialogCreater.createTipsDialog(this, "温馨提示", content, btnTxt, false, l);
//        mWarningDialog.show();
//    }
}
