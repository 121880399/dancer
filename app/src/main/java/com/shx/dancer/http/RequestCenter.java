package com.shx.dancer.http;


/**
 * Created by 邵鸿轩 on 2016/12/6.
 */

public class RequestCenter {
    public static final String REGIST="/register";
    public static final String LOGIN="/login";
    public static final String UPLOAD_APPID="/user/uploadAppid";


    /**
     * 更新APPid
     *
     * */
    public static void uploadAppid(int userId , String appId, HttpCallBack callBack){
        ZCRequest request=new ZCRequest();
        request.setUrl(UPLOAD_APPID);
        request.putParams("userId",String.valueOf(userId));
        request.putParams("appId",appId);
        HttpManager.getInstance().doPost(request,callBack);
    }




    /**
     * 验证手机注册
     * */
    public static void regist(String phone, String password,String verifyCode , HttpCallBack callBack){
        ZCRequest request=new ZCRequest();
        request.setUrl(REGIST);
        request.putParams("tel",phone);
        request.putParams("password",password);
        request.putParams("code",verifyCode);
        HttpManager.getInstance().doPost(request,callBack);
    }
    /**
     * 登录接口
     * */
    public static void login(String username, String password, HttpCallBack callBack){
        ZCRequest request=new ZCRequest();
        request.setUrl(LOGIN);
        request.putParams("username",username);
        request.putParams("password",password);
        HttpManager.getInstance().doPost(request,callBack);
    }
    /**
     * 得到手机验证码
     * */
    public static void getVerifyCode(String phone, HttpCallBack callBack){
        ZCRequest request=new ZCRequest();
//        request.setUrl(GET_VERIFYCODE);
        request.putParams("phone",phone);
        HttpManager.getInstance().doPost(request,callBack);
    }

//    /**
//     * 注册
//     * */
//    public static void regist(RequestRegisterInfo info, HttpCallBack callBack){
//        ZCRequest request=new ZCRequest();
//        request.setUrl(REGIST);
//        request.putParams("loginName",info.getLoginName());
//        request.putParams("nickName",info.getNickName());
//        request.putParams("realName",info.getRealName());
//        request.putParams("departmentId",info.getDepartmentId());
//        request.putParams("regionId",info.getRegionId());
//        request.putParams("email",info.getEmail());
//        request.putParams("idNo",info.getIdNo());
//        request.putParams("jobId",info.getJobId());
//        request.putParams("phone",info.getPhone());
//        request.putParams("sex",info.getSex());
//        request.putParams("userType",info.getUserType());
//        request.putParams("licenseType",info.getLicenseType());
//        HttpManager.getInstance().doPost(request,callBack);
//    }




    /**
     * 获取用户信息
     * */
    public static void getUserInfo(String userId, HttpCallBack callBack){
        ZCRequest request=new ZCRequest();
//        request.setUrl(GET_USERINFO);
        request.putParams("userId",userId);
        HttpManager.getInstance().doPost(request,callBack);
    }


}

