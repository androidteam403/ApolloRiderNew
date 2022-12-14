package com.apollo.epos.activity.login;

import android.content.Context;
import android.os.Build;

import com.apollo.epos.BuildConfig;
import com.apollo.epos.activity.login.model.FirebaseTokenRequest;
import com.apollo.epos.activity.login.model.GetDetailsRequest;
import com.apollo.epos.activity.login.model.LoginRequest;
import com.apollo.epos.activity.login.model.LoginResponse;
import com.apollo.epos.activity.login.model.OrderPaymentTypeResponse;
import com.apollo.epos.activity.login.model.SaveUserDeviceInfoRequest;
import com.apollo.epos.activity.login.model.SaveUserDeviceInfoResponse;
import com.apollo.epos.activity.orderdelivery.model.DeliveryFailreReasonsResponse;
import com.apollo.epos.db.SessionManager;
import com.apollo.epos.fragment.profile.model.ComplaintReasonsListResponse;
import com.apollo.epos.model.GetRiderProfileResponse;
import com.apollo.epos.network.ApiClient;
import com.apollo.epos.network.ApiInterface;
import com.apollo.epos.service.NetworkUtils;
import com.apollo.epos.utils.ActivityUtils;
import com.apollo.epos.utils.AppConstants;
import com.apollo.epos.utils.CommonUtils;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityController {
    private Context context;
    private LoginActivityCallback mListener;

    public LoginActivityController(Context context, LoginActivityCallback mListener) {
        this.context = context;
        this.mListener = mListener;
    }

//    public void loginApiCall(String userName, String password, String firebaseToken) {
//        if (NetworkUtils.isNetworkConnected(context)) {
//            ActivityUtils.showDialog(context, "Please wait.");
//            ApiInterface apiInterface = ApiClient.getApiService();
//            LoginRequest loginRequest = new LoginRequest();
//            loginRequest.setAppUserName(userName);
//            loginRequest.setAppPassword(password);
//            Call<LoginResponse> call = apiInterface.DO_LOGIN_API_CALL(loginRequest);
//            call.enqueue(new Callback<LoginResponse>() {
//                @Override
//                public void onResponse(@NotNull Call<LoginResponse> call, @NotNull Response<LoginResponse> response) {
//
//                    if (response.body() != null && response.body().getData() != null && response.body().getSuccess()) {
//                        mListener.onSuccessLoginApi(response.body());
//                        orderPaymentTypelistApiCall();
//                        saveUserDeviceInfoApiCall(firebaseToken);
//                        firebaseToken(firebaseToken);
//                    } else if (response.body() != null && !response.body().getSuccess()) {
//                        ActivityUtils.hideDialog();
//                        mListener.onFailureLoginApi(response.body().getMessage());
//
//                    }
//                }
//
//                @Override
//                public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
//                    ActivityUtils.hideDialog();
//                    mListener.onFailureLoginApi(t.getMessage());
//                }
//            });
//        } else {
//            mListener.onFailureLoginApi("Something went wrong.");
//        }
//    }



    public void loginApiCall(String userName, String password, String firebaseToken) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setAppUserName(userName);
            loginRequest.setAppPassword(password);
            Gson gson = new Gson();
            String jsonLoginRequest = gson.toJson(loginRequest);
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(BuildConfig.BASE_URL + "login");
            getDetailsRequest.setRequestjson(jsonLoginRequest);
            getDetailsRequest.setHeadertokenkey("");
            getDetailsRequest.setHeadertokenvalue("");
            getDetailsRequest.setRequesttype("POST");
            Call<ResponseBody> calls = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);
            calls.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null) {
                        String resp = null;
                        try {
                            resp = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (resp != null) {
                            String res = BackSlash.removeBackSlashes(resp);
                            Gson gson = new Gson();
                            LoginResponse loginResponse = gson.fromJson(BackSlash.removeSubString(res), LoginResponse.class);
                            if (loginResponse != null && loginResponse.getData() != null && loginResponse.getSuccess()) {
                                mListener.onSuccessLoginApi(loginResponse);
                                orderPaymentTypelistApiCall();
                                saveUserDeviceInfoApiCall(firebaseToken);
                                firebaseToken(firebaseToken);
                            } else if (loginResponse != null && !loginResponse.getSuccess()) {
                                ActivityUtils.hideDialog();
                                mListener.onFailureLoginApi(loginResponse.getMessage());

                            }
                        }
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFailureLoginApi(t.getMessage());
                }
            });

        } else {
            mListener.onFailureLoginApi("Something went wrong.");
        }
    }

    public void getRiderProfileDetailsApi(String token) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(BuildConfig.BASE_URL + "api/user/select/rider-profile-select");
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setHeadertokenvalue("Bearer " + token);
            getDetailsRequest.setRequestjson("The");
            getDetailsRequest.setRequesttype("GET");
            Call<ResponseBody> call = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null) {
                        String resp = null;
                        try {
                            resp = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (resp != null) {
                            String res = BackSlash.removeBackSlashes(resp);
                            Gson gson = new Gson();
                            GetRiderProfileResponse riderProfileResponse = gson.fromJson(BackSlash.removeSubString(res), GetRiderProfileResponse.class);
                            if (riderProfileResponse != null && riderProfileResponse.getData() != null && riderProfileResponse.getSuccess()) {
                                mListener.onSuccessGetProfileDetailsApi(riderProfileResponse);

                            } else {
                                ActivityUtils.hideDialog();
                                mListener.onFailureGetProfileDetailsApi("No Data Found");

                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFailureGetProfileDetailsApi(t.getMessage());
                }
            });

        } else {
            mListener.onFailureGetProfileDetailsApi("Something went wrong.");
        }
    }


    //    public void saveUserDeviceInfoApiCall(String firebaseToken) {
//        if (NetworkUtils.isNetworkConnected(context)) {
//            ApiInterface apiInterface = ApiClient.getApiService();
//            SaveUserDeviceInfoRequest saveUserDeviceInfoRequest = new SaveUserDeviceInfoRequest();
//            SaveUserDeviceInfoRequest.DeviceInfo deviceInfo = new SaveUserDeviceInfoRequest.DeviceInfo();
//            deviceInfo.setMac_id(CommonUtils.getDeviceId(context));
//            deviceInfo.setDevice_name(CommonUtils.getDeviceName());
//            deviceInfo.setManufacture(Build.MANUFACTURER);
//            deviceInfo.setBrand(Build.BRAND);
//            deviceInfo.setUser(Build.USER);
//            deviceInfo.setVersion_sdk(Build.VERSION.SDK);
//            deviceInfo.setFingerprint(Build.FINGERPRINT);
//            deviceInfo.setApp_version_code(String.valueOf(BuildConfig.VERSION_CODE));
//            deviceInfo.setApp_version_name(BuildConfig.VERSION_NAME);
//            saveUserDeviceInfoRequest.setDeviceInfo(deviceInfo);
//            saveUserDeviceInfoRequest.setFirebaseId(firebaseToken);
//
//            Call<SaveUserDeviceInfoResponse> call = apiInterface.SAVE_USER_DEVICE_INFO_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), saveUserDeviceInfoRequest);
//            call.enqueue(new Callback<SaveUserDeviceInfoResponse>() {
//                @Override
//                public void onResponse(@NotNull Call<SaveUserDeviceInfoResponse> call, @NotNull Response<SaveUserDeviceInfoResponse> response) {
//                    if (response.isSuccessful() && response.body() != null && response.body().getSuccess()) {
//
//                    }
//                }
//
//                @Override
//                public void onFailure(@NotNull Call<SaveUserDeviceInfoResponse> call, @NotNull Throwable t) {
//                    ActivityUtils.hideDialog();
//                    mListener.onFialureMessage(t.getMessage());
//                }
//            });
//        } else {
//            mListener.onFialureMessage("Something went wrong.");
//        }
//    }
    public void saveUserDeviceInfoApiCall(String firebaseToken) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            SaveUserDeviceInfoRequest saveUserDeviceInfoRequest = new SaveUserDeviceInfoRequest();
            SaveUserDeviceInfoRequest.DeviceInfo deviceInfo = new SaveUserDeviceInfoRequest.DeviceInfo();
            deviceInfo.setMac_id(CommonUtils.getDeviceId(context));
            deviceInfo.setDevice_name(CommonUtils.getDeviceName());
            deviceInfo.setManufacture(Build.MANUFACTURER);
            deviceInfo.setBrand(Build.BRAND);
            deviceInfo.setUser(Build.USER);
            deviceInfo.setVersion_sdk(Build.VERSION.SDK);
            deviceInfo.setFingerprint(Build.FINGERPRINT);
            deviceInfo.setApp_version_code(String.valueOf(BuildConfig.VERSION_CODE));
            deviceInfo.setApp_version_name(BuildConfig.VERSION_NAME);
            saveUserDeviceInfoRequest.setDeviceInfo(deviceInfo);
            saveUserDeviceInfoRequest.setFirebaseId(firebaseToken);
            Gson gson = new Gson();
            String jsonUserDeviceRequest = gson.toJson(saveUserDeviceInfoRequest);
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(BuildConfig.BASE_URL + "api/user/save-update/update-user-device-info");
            getDetailsRequest.setRequestjson(jsonUserDeviceRequest);
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setHeadertokenvalue("Bearer " + new SessionManager(context).getLoginToken());
            getDetailsRequest.setRequesttype("POST");
            Call<ResponseBody> calls = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);
            calls.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null) {
                        String resp = null;
                        try {
                            resp = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (resp != null) {
                            String res = BackSlash.removeBackSlashes(resp);
                            Gson gson = new Gson();
                            SaveUserDeviceInfoResponse saveUserDeviceInfoResponse = gson.fromJson(BackSlash.removeSubString(res), SaveUserDeviceInfoResponse.class);
                            if (saveUserDeviceInfoResponse != null && saveUserDeviceInfoResponse.getSuccess() != null) {

                            } else if (saveUserDeviceInfoResponse != null && !saveUserDeviceInfoResponse.getSuccess()) {
                                ActivityUtils.hideDialog();
                                mListener.onFialureMessage(saveUserDeviceInfoResponse.getMessage());

                            }
                        }
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage(t.getMessage());
                }
            });

        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }


    public void firebaseToken(String firebaseToken) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();
            FirebaseTokenRequest firebaseTokenRequest = new FirebaseTokenRequest();
            firebaseTokenRequest.setFirebaseToken(firebaseToken);
            Gson gson = new Gson();
            String jsonfirebaseTokenRequest = gson.toJson(firebaseToken);
            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(BuildConfig.BASE_URL + "updateFirebaseToken");
            getDetailsRequest.setRequestjson(jsonfirebaseTokenRequest);
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setHeadertokenvalue("Bearer " + new SessionManager(context).getLoginToken());
            getDetailsRequest.setRequesttype("POST");
            Call<ResponseBody> calls = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);
            calls.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null) {
                        String resp = null;
                        try {
                            resp = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (resp != null) {
                            String res = BackSlash.removeBackSlashes(resp);
                            Gson gson = new Gson();
                            Object object = gson.fromJson(BackSlash.removeSubString(res), Object.class);
                            if (object != null) {

                            } else if (object != null) {
                                ActivityUtils.hideDialog();
                                mListener.onFialureMessage("Something went wrong");

                            }
                        }
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage(t.getMessage());
                }
            });

        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }

    public void deliveryFailureReasonApiCall() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();

            Gson gson = new Gson();

            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(BuildConfig.BASE_URL + "api/choose-data/delivery_failure_reasons");
            getDetailsRequest.setRequestjson("The");
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setHeadertokenvalue("Bearer " + new SessionManager(context).getLoginToken());
            getDetailsRequest.setRequesttype("POST");
            Call<ResponseBody> calls = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);
            calls.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null) {
                        String resp = null;
                        try {
                            resp = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (resp != null) {
                            String res = BackSlash.removeBackSlashes(resp);
                            Gson gson = new Gson();
                            DeliveryFailreReasonsResponse deliveryFailreReasonsResponse = gson.fromJson(BackSlash.removeSubString(res), DeliveryFailreReasonsResponse.class);
                            if (deliveryFailreReasonsResponse != null && deliveryFailreReasonsResponse.isSuccess()) {
                                mListener.onSuccessDeliveryReasonApiCall(deliveryFailreReasonsResponse);

                            } else {
                                ActivityUtils.hideDialog();
                                mListener.onFialureMessage("No data found.");
                            }
                        }
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage(t.getMessage());
                }
            });

        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }

    public void getComplaintReasonsListApiCall() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();

            Gson gson = new Gson();

            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(BuildConfig.BASE_URL + "api/choose-data/complaint_reason");
            getDetailsRequest.setRequestjson("The");
            getDetailsRequest.setHeadertokenkey("authorization");
            getDetailsRequest.setHeadertokenvalue("Bearer " + new SessionManager(context).getLoginToken());
            getDetailsRequest.setRequesttype("POST");
            Call<ResponseBody> calls = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);
            calls.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null) {
                        String resp = null;
                        try {
                            resp = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (resp != null) {
                            String res = BackSlash.removeBackSlashes(resp);
                            Gson gson = new Gson();
                            ComplaintReasonsListResponse complaintReasonsListResponse = gson.fromJson(BackSlash.removeSubString(res), ComplaintReasonsListResponse.class);
                            if (complaintReasonsListResponse != null && complaintReasonsListResponse.getSuccess()) {
                                new SessionManager(context).setComplaintReasonsListResponse(complaintReasonsListResponse);
                            } else if (response.code() == 401) {
                            }
                        }
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage(t.getMessage());
                }
            });

        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }

    public void orderPaymentTypelistApiCall() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            ApiInterface apiInterface = ApiClient.getApiService();

            Gson gson = new Gson();

            GetDetailsRequest getDetailsRequest = new GetDetailsRequest();
            getDetailsRequest.setRequesturl(BuildConfig.BASE_URL + "api/choose-data/order_payment_type");
            getDetailsRequest.setRequestjson("The");
            getDetailsRequest.setHeadertokenkey("");
            getDetailsRequest.setHeadertokenvalue("");
            getDetailsRequest.setRequesttype("GET");
            Call<ResponseBody> calls = apiInterface.getDetails(AppConstants.PROXY_URL, AppConstants.PROXY_TOKEN, getDetailsRequest);
            calls.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null) {
                        String resp = null;
                        try {
                            resp = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (resp != null) {
                            String res = BackSlash.removeBackSlashes(resp);
                            Gson gson = new Gson();
                            OrderPaymentTypeResponse orderPaymentTypeResponse = gson.fromJson(BackSlash.removeSubString(res), OrderPaymentTypeResponse.class);
                            if (orderPaymentTypeResponse != null && orderPaymentTypeResponse.getSuccess()) {
                                new SessionManager(context).setOrderPaymentTypeList(orderPaymentTypeResponse);
                            }
                        }
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage(t.getMessage());
                }
            });

        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }

}
