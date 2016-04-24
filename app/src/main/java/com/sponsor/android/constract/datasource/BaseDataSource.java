package com.sponsor.android.constract.datasource;

import android.content.Intent;

import com.orhanobut.logger.Logger;
import com.shizhefei.HttpResponseStatus;
import com.shizhefei.mvc.IDataSource;
import com.sponsor.android.Constant;
import com.sponsor.android.SponsorApplication;
import com.sponsor.android.constract.ui.activity.BaseActivity;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseDataSource<DATA> implements IDataSource<DATA> {
    @Override
    public DATA refresh() throws Exception {
        return null;
    }

    @Override
    public DATA loadMore() throws Exception {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public HttpResponseStatus onResp(Object resp) {
        HttpResponseStatus resStatus = new HttpResponseStatus();
        resStatus.setResult("请求失败，请重试");
        resStatus.setSuccess(false);

        Response response = null;
        if (resp != null && resp instanceof Response) {
            response = (Response) resp;
            if (response.isSuccessful()) {
                try {
                    String responseData = response.body().string();
                    if (Constant.DEBUG){
                        Logger.json(responseData);
                    }
                    JSONObject object = new JSONObject(responseData);
                    int code = object.getInt("code");
                    switch (code) {
                        case 0://请求成功
                            resStatus.setSuccess(true);
                            resStatus.setResult(responseData);
                            break;
                        case 10101://cookie失效，需要重新登录
                            resStatus.setResult(object.getString("message"));
                            Intent intent = new Intent();
                            intent.putExtra("TYPE", 1);
                            intent.setAction(BaseActivity.BROADCAST_FLAG);
                            SponsorApplication.getInstance().sendBroadcast(intent);
                            break;
                        default:
                            resStatus.setResult(object.getString("message"));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    resStatus.setResult("数据格式错误");
                } catch (Exception e) {
                    e.printStackTrace();
                    resStatus.setResult("返回数据错误");
                }
            }
        }
        return resStatus;
    }
}
