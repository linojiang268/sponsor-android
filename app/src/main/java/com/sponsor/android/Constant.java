package com.sponsor.android;

import com.sponsor.android.manager.PhoneManager;

public final class Constant {

	public static final boolean DEBUG = BuildConfig.DEBUG;
	// jihe:RDPTQSUB1AKR7LO9Y17BTK2YC0PBAJ0L  jhla:F1C86DC81A8CBCE4EEB9D219B68D6E66
	public static final String SIGN_KEY = "PQYfcoazBZvc525XUAt4cob55NExEt3T";

	public static final String REQUEST_HOST = "http://192.168.1.3:8090/";



	public static final String DEFOULT_TEST_REQUEST_URL = "http://staging.jhla.com.cn/";// 测试数据访问地址
	public static final String DEFOULT_REQUEST_URL = "http://app.jh008.com/";// 外网数据访问地址

	/**
	 * 分享
	 */
	public static final String WX_APPID = "wxdaf869f9bd24b02f"; //微信ID
	public static final String WX_APPSECRET = "09e00845d4575e4515a5c72d4a8781e5"; //微信ID
	public static final String QQ_APPID = "1103292660"; //QQ登录ID
	public static final String SINA_APPID = "2247106580";	//新浪登录ID

	/**
	 *YUNBA
	 */
	public static final String YUNBA_APPKEY = "55c99e8b9477ebf52469571a";
	public static final String YUNBA_DEBUG_APPKEY = "55ffb4f94a481fa955f396d0";

    /**
     * 微信支付
     */
    //  API密钥，在商户平台设置
    public static final  String WX_PAY_API_KEY="9bfe7c6249f79ab1012e726fd84d872b";



	public static final String APP_DIR = PhoneManager.getSdCardRootPath() + "/JiHe/";//app文件目录
	public static final String IMAGE_CACHE_DIR_PATH = APP_DIR + "cache/";// 图片缓存地址
	public static final String UPLOAD_FILES_DIR_PATH = APP_DIR + "upload/";//上传文件、零时文件存放地址
	public static final String DOWNLOAD_DIR_PATH = APP_DIR + "download/";// 下载文件存放地址
	public static final String IDENTITY_DIR_PATH = APP_DIR + "identity/";// 身份二维码存放地址

	public static final int HEADPORTRAIT_SIZE  = 400; //头像大小

}