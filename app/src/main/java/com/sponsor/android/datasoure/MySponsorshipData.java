package com.sponsor.android.datasoure;


import com.alibaba.fastjson.JSON;
import com.shizhefei.HttpResponseStatus;
import com.sponsor.android.Constant;
import com.sponsor.android.constract.datasource.BaseDataSource;
import com.sponsor.android.entity.SponsorshipEntity;
import com.sponsor.android.net.OkHttpGetRequestBuilder;
import com.sponsor.android.net.OkHttpManager;
import com.sponsor.android.net.OkHttpRequestBuilderFactory;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.util.List;

public class MySponsorshipData extends BaseDataSource<List<SponsorshipEntity>> {
    private int page = 1;
    private int maxPage = 1;

    @Override
    public List<SponsorshipEntity> refresh() throws Exception {
        page = 1;
        maxPage = 1;
        return load(page);
    }

    @Override
    public List<SponsorshipEntity> loadMore() throws Exception {
        page++;
        return load(page + 1);
    }

    private List<SponsorshipEntity> load(int page) {
        try {
            OkHttpGetRequestBuilder builder = OkHttpRequestBuilderFactory.createGetRequestBuilder(
                    Constant.REQUEST_HOST + "api/user/sponsorships");
            builder.put("page", page);
            Response response = OkHttpManager.syncGet(builder);
            HttpResponseStatus status = onResp(response);

            if (status.isSuccess()) {
                JSONObject resultJson = new JSONObject(status.getResult());
                maxPage = resultJson.getInt("totalPages");
                return JSON.parseArray(resultJson.getString("sponsorships"), SponsorshipEntity.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean hasMore() {
        return page < maxPage;
    }
}
