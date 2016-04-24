package com.sponsor.android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.shizhefei.mvc.MVCHelper;
import com.sponsor.android.R;
import com.sponsor.android.adapter.MySponsorshipAdapter;
import com.sponsor.android.constract.ui.activity.BaseActivity;
import com.sponsor.android.datasoure.MySponsorshipData;
import com.sponsor.android.entity.SponsorshipEntity;
import com.sponsor.android.manager.MVCUltraHelper;
import com.sponsor.android.manager.PhoneManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.MaterialHeader;

public class MySponsorshipsActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.lvSponsorships)
    RecyclerView lvSponsorships;
    @Bind(R.id.ptrLayout)
    PtrClassicFrameLayout ptrLayout;
    @Bind(R.id.btBack)
    Button btBack;
    private MVCHelper<List<SponsorshipEntity>> sponsorshipViewHelper;

    private MySponsorshipData sponsorshipData;
    private MySponsorshipAdapter sponsorshipAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_sponsorships);
        ButterKnife.bind(this);
        this.initView();
    }

    private void initView() {
        btBack.setOnClickListener(this);

//        sponsorshipViewHelper.setLoadViewFractory(new NormalLoadViewFactory());
        lvSponsorships.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        final MaterialHeader header = new MaterialHeader(getApplicationContext());
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, PhoneManager.dip2px(15), 0, PhoneManager.dip2px(10));
        header.setPtrFrameLayout(ptrLayout);
        ptrLayout.setLoadingMinTime(800);
        ptrLayout.setDurationToCloseHeader(800);
        ptrLayout.setHeaderView(header);
        ptrLayout.addPtrUIHandler(header);

        sponsorshipData = new MySponsorshipData();
        sponsorshipAdapter = new MySponsorshipAdapter(MySponsorshipsActivity.this);
        sponsorshipViewHelper = new MVCUltraHelper<>(ptrLayout);
        sponsorshipViewHelper.setDataSource(sponsorshipData);
        sponsorshipViewHelper.setAdapter(sponsorshipAdapter);

        sponsorshipViewHelper.refresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btBack:
                finish();
                break;
        }
    }
}
