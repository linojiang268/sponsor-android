package com.sponsor.android.net;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.orhanobut.logger.Logger;
import com.sponsor.android.Constant;
import com.sponsor.android.SponsorApplication;
import com.sponsor.android.constract.ui.activity.BaseActivity;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public abstract class UiHandlerCallBack extends Handler implements com.squareup.okhttp.Callback {
    public final static int SUCCESS = 10001;
    public final static int ERROR = 10002;
    public final static int PROGRESS = 10003;
    public final static int FAILED = 10004;

    public final static int FAILED_NETWORK = -11;
    public final static int ERROR_JSON = -12;
    public final static int ERROR_RESPONSE = -13;
    public final static int ERROR_RESPONSE_CODE = -14;

    public void onFailure(Request request, IOException e) {
        sendFailedMessage(FAILED_NETWORK, "无法连接，请检查网络设置");
    }

    public void onResponse(Response response) throws IOException {
        if (response != null && response.isSuccessful()){
            try {
                String responseContent = response.body().string();
                if (Constant.DEBUG) {
                    Logger.i(responseContent);
                }
                JSONObject responseData = new JSONObject(responseContent);
                int code = responseData.getInt("code");
                switch (code){
                    case 0://请求成功
                        sendSuccessMessage(responseData);
                        break;
                    case 10101://cookie失效，需要重新登录
                        Intent intent = new Intent();
                        intent.putExtra("TYPE", 1);
                        intent.setAction(BaseActivity.BROADCAST_FLAG);
                        SponsorApplication.getInstance().sendBroadcast(intent);
                        sendErrorMessage(code, responseData.getString("message"));
                        break;
                    default:
                        sendErrorMessage(code, responseData.getString("message"));
                        break;
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                sendErrorMessage(ERROR_JSON, "数据格式错误");
            }
            catch (Exception e) {
                sendErrorMessage(ERROR_RESPONSE, "响应数据异常");
            }
        } else {
            sendErrorMessage(ERROR_RESPONSE_CODE, "请求失败，请重试");
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case SUCCESS:
                success((JSONObject) msg.obj);
                break;
            case ERROR:
                error(msg.arg1, (String) msg.obj);
                break;
            case PROGRESS:
                progress(msg.arg1);
                break;
            case FAILED:
                failed(msg.arg1, (String) msg.obj);
                break;
        }
    }

    public void sendSuccessMessage(JSONObject responseData){
        Message message = new Message();
        message.what = SUCCESS;
        message.obj = responseData;
        sendMessage(message);
    }

    public void sendErrorMessage(int code, String msg){
        Message message = new Message();
        message.what = ERROR;
        message.arg1 = code;
        message.obj = msg;
        sendMessage(message);
    }

    public void sendProgressMessage(int progress){
        Message message = new Message();
        message.what = PROGRESS;
        message.arg1 = progress;
        sendMessage(message);
    }

    public void sendFailedMessage(int code, String msg){
        Message message = new Message();
        message.what = FAILED;
        message.arg1 = code;
        message.obj = msg;
        sendMessage(message);
    }

    public abstract void success(JSONObject data);

    public abstract void error(int code, String data);

    public abstract void progress(int progress);

    public abstract void failed(int code, String msg);
}
