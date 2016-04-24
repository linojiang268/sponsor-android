package com.sponsor.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sponsor.android.Constant;
import com.sponsor.android.R;
import com.sponsor.android.constract.ui.activity.BaseActivity;
import com.sponsor.android.data.UserPref;
import com.sponsor.android.entity.SponsorshipEntity;
import com.sponsor.android.net.OkHttpManager;
import com.sponsor.android.net.OkHttpPostRequestBuilder;
import com.sponsor.android.net.OkHttpRequestBuilderFactory;
import com.sponsor.android.net.UiHandlerCallBack;
import com.sponsor.android.util.Checker;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ApplySponsorshipActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.btBack)
    Button btBack;
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    @Bind(R.id.tvSponsorName)
    TextView tvSponsorName;
    @Bind(R.id.etTeamOrPersonalName)
    EditText etTeamOrPersonalName;
    @Bind(R.id.etContactPhone)
    EditText etContactPhone;
    @Bind(R.id.etApplicationReason)
    EditText etApplicationReason;
    @Bind(R.id.btApply)
    Button btApply;
    private SponsorshipEntity sponsorshipEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_sponsorship);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        sponsorshipEntity = (SponsorshipEntity) intent.getExtras().getSerializable("sponsorship");
        this.initView();
        this.login(null);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!UserPref.getInstance().hasLogin()) {
            finish();
        }
    }

    private void initView() {
        btBack.setOnClickListener(this);
        btApply.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btApply:
                apply(etTeamOrPersonalName.getText().toString(),
                        etContactPhone.getText().toString(),
                        etApplicationReason.getText().toString());
                break;
        }
    }

    private void apply(String name, String contact, String reason) {
        if (Checker.isEmpty(name)) {
            toast("请填写团体或个人");
        }

        if (Checker.isEmpty(reason)) {
            toast("请填写申请理由");
        }

        if (!Checker.isMobilePhone(contact)) {
            toast("联系方式格式错误");
        }

        OkHttpPostRequestBuilder builder = OkHttpRequestBuilderFactory.createPostRequestBuilder(
                Constant.REQUEST_HOST + "api/sponsorships/" + sponsorshipEntity.getId() + "/" + "applications");
        builder.put("team_name", name);
        builder.put("mobile", contact);
        builder.put("contact_user", name);
        builder.put("application_reason", reason);
        OkHttpManager.post(builder, new UiHandlerCallBack() {
            @Override
            public void success(JSONObject data) {
                toast("申请成功");
                finish();
            }

            @Override
            public void error(int code, String data) {
                toast(data);
            }

            @Override
            public void progress(int progress) {

            }

            @Override
            public void failed(int code, String msg) {
                toast(msg);
            }
        });

    }
}
