package com.apollo.epos.activity.orderdelivery;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.apollo.epos.BuildConfig;
import com.apollo.epos.activity.neworder.model.OrderDetailsRequest;
import com.apollo.epos.activity.neworder.model.OrderDetailsResponse;
import com.apollo.epos.activity.orderdelivery.model.DeliveryFailreReasonsResponse;
import com.apollo.epos.activity.orderdelivery.model.FileDataResponse;
import com.apollo.epos.activity.orderdelivery.model.OrderHandoverSaveUpdateRequest;
import com.apollo.epos.activity.orderdelivery.model.OrderHandoverSaveUpdateResponse;
import com.apollo.epos.activity.orderdelivery.model.OrderPaymentUpdateRequest;
import com.apollo.epos.activity.orderdelivery.model.OrderSaveUpdateStausRequest;
import com.apollo.epos.activity.orderdelivery.model.OrderSaveUpdateStausResponse;
import com.apollo.epos.activity.orderdelivery.model.OrderStatusHistoryListRequest;
import com.apollo.epos.activity.orderdelivery.model.OrderStatusHitoryListResponse;
import com.apollo.epos.db.SessionManager;
import com.apollo.epos.network.ApiClient;
import com.apollo.epos.network.ApiInterface;
import com.apollo.epos.service.NetworkUtils;
import com.apollo.epos.utils.ActivityUtils;
import com.apollo.epos.utils.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderDeliveryActivityController {
    private Context context;
    private OrderDeliveryActivityCallback mListener;

    public OrderDeliveryActivityController(Context context, OrderDeliveryActivityCallback mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    public void orderDetailsApiCall(String token, String orderNumber) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");

            OrderDetailsRequest orderDetailsRequest = new OrderDetailsRequest();
            orderDetailsRequest.setOrderNumber(orderNumber);

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<OrderDetailsResponse> call = apiInterface.ORDER_DETAILS_API_CALL("Bearer " + token, orderDetailsRequest);
            call.enqueue(new Callback<OrderDetailsResponse>() {
                @Override
                public void onResponse(@NotNull Call<OrderDetailsResponse> call, @NotNull Response<OrderDetailsResponse> response) {
                    if (response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessOrderDetailsApiCall(response.body());
                        deliveryFailureReasonApiCall();
                    } else {
                        ActivityUtils.hideDialog();
                        mListener.onFialureMessage("No data found.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderDetailsResponse> call, @NotNull Throwable t) {
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

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<DeliveryFailreReasonsResponse> call = apiInterface.DELIVERY_FAILURE_REASONS_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), "application/json");
            call.enqueue(new Callback<DeliveryFailreReasonsResponse>() {
                @Override
                public void onResponse(@NotNull Call<DeliveryFailreReasonsResponse> call, @NotNull Response<DeliveryFailreReasonsResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null && response.body().isSuccess()) {
                        mListener.onSuccessDeliveryReasonApiCall(response.body());
                    } else {
                        mListener.onFialureMessage("No data found.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<DeliveryFailreReasonsResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }

    public void orderStatusHistoryListApiCall(String uid) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            OrderStatusHistoryListRequest orderStatusHistoryListRequest = new OrderStatusHistoryListRequest();
            orderStatusHistoryListRequest.setUid(uid);

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<OrderStatusHitoryListResponse> call = apiInterface.ORDER_STATUS_HISTORY_LIST_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), orderStatusHistoryListRequest);
            call.enqueue(new Callback<OrderStatusHitoryListResponse>() {
                @Override
                public void onResponse(@NotNull Call<OrderStatusHitoryListResponse> call, @NotNull Response<OrderStatusHitoryListResponse> response) {
                    ActivityUtils.hideDialog();
                    if (response.body() != null) {
                        mListener.onSuccessOrderStatusHistoryListApiCall(response.body());
                    } else {
                        mListener.onFialureMessage("No data found.");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderStatusHitoryListResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }

    public void ordersSaveUpdateStatusApiCall(String orderStatus, String orderUid, String orderCancelReason, String comment) {
        if (NetworkUtils.isNetworkConnected(context)) {
            OrderSaveUpdateStausRequest orderSaveUpdateStausRequest = new OrderSaveUpdateStausRequest();
            orderSaveUpdateStausRequest.setUid(orderUid);
            OrderSaveUpdateStausRequest.OrderStatus orderStatus1 = new OrderSaveUpdateStausRequest.OrderStatus();
            orderStatus1.setUid(orderStatus);
            orderSaveUpdateStausRequest.setOrderStatus(orderStatus1);
            OrderSaveUpdateStausRequest.DeliveryFailureReason deliveryFailureReason = new OrderSaveUpdateStausRequest.DeliveryFailureReason();
            deliveryFailureReason.setUid(orderCancelReason);
            orderSaveUpdateStausRequest.setDeliveryFailureReason(deliveryFailureReason);
            orderSaveUpdateStausRequest.setComment(comment);
            ApiInterface apiInterface = ApiClient.getApiService();

            Call<OrderSaveUpdateStausResponse> call = apiInterface.ORDER_SAVE_UPDATE_STATUS_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), orderSaveUpdateStausRequest);
            call.enqueue(new Callback<OrderSaveUpdateStausResponse>() {
                @Override
                public void onResponse(@NotNull Call<OrderSaveUpdateStausResponse> call, @NotNull Response<OrderSaveUpdateStausResponse> response) {

                    if (response.body() != null && response.body().getSuccess()) {
                        mListener.onSuccessOrderSaveUpdateStatusApi(orderStatus);

                    } else {
                        ActivityUtils.hideDialog();
//                        mListener.onFialureOrderSaveUpdateStatusApi(response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderSaveUpdateStausResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }

    public void uploadFile(Bitmap bmp, String orderuid, String orderNumber, String customerName) throws IOException {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.");
            File f = new File(context.getCacheDir(), "signature.jpg");
            f.createNewFile();

            Bitmap bitmap = bmp;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            RequestBody requestBody = null;
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("file", f.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), f));
            requestBody = builder.build();

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .addInterceptor(logging)
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("deviceMode", "ANDROID")
                                .header("mobile", "false")
                                .header("Authorization", "Bearer " + new SessionManager(context).getLoginToken())
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    })
                    .build();

            //The gson builder
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            //creating retrofit object
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.UPLOAD_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();

            //creating our api
            ApiInterface api = retrofit.create(ApiInterface.class);

            //creating a call and calling the upload image method
            Call<FileDataResponse> call = api.UPLOAD_IMAGE_API_CALL(requestBody);

            //finally performing the call
            call.enqueue(new Callback<FileDataResponse>() {

                @Override
                public void onResponse(Call<FileDataResponse> call, retrofit2.Response<FileDataResponse> response) {
                    if (response.isSuccessful()) {
                        Log.e("Success", "" + response.isSuccessful());
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                orderHandoverSaveUpdate(response.body(), bmp, orderuid, orderNumber, customerName);
                            }
                        } else if (response.code() == 401) {
                            ActivityUtils.hideDialog();
                        }
                    } else if (response.code() == 401) {
                        ActivityUtils.hideDialog();
                    }
                    Log.e("onResponse", "" + response.toString());
                }

                @Override
                public void onFailure(Call<FileDataResponse> call, Throwable t) {
                    Log.e("error_details", "" + t.toString());
                    ActivityUtils.hideDialog();
                }
            });
        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }

    public void orderHandoverSaveUpdate(FileDataResponse imageFullPath, Bitmap bitmap, String orderUid, String orderNumber, String customerName) {
        if (NetworkUtils.isNetworkConnected(context)) {
            OrderHandoverSaveUpdateRequest orderHandoverSaveUpdateRequest = new OrderHandoverSaveUpdateRequest();
            orderHandoverSaveUpdateRequest.setHandoverTo(customerName);
            orderHandoverSaveUpdateRequest.setSignature(imageFullPath.getData());
            OrderHandoverSaveUpdateRequest.Order order = new OrderHandoverSaveUpdateRequest.Order();
            order.setUid(orderUid);
            order.setOrderNumber(orderNumber);
            orderHandoverSaveUpdateRequest.setOrder(order);

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<OrderHandoverSaveUpdateResponse> call = apiInterface.ORDER_HANDOVER_SAVE_UPDATE_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), orderHandoverSaveUpdateRequest);
            call.enqueue(new Callback<OrderHandoverSaveUpdateResponse>() {
                @Override
                public void onResponse(@NotNull Call<OrderHandoverSaveUpdateResponse> call, @NotNull Response<OrderHandoverSaveUpdateResponse> response) {
                    if (response.body() != null && response.body().getSuccess()) {
                        ActivityUtils.hideDialog();
                        mListener.onSuccessOrderHandoverSaveUpdateApi(bitmap);
                    } else {
                        ActivityUtils.hideDialog();
                        if (response.body() != null && response.body().getMessage() != null)
                            mListener.onFailureOrderHandoverSaveUpdateApi(response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OrderHandoverSaveUpdateResponse> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }

    public void orderPaymentUpdateApiCall(OrderDetailsResponse orderDetailsResponse, String paymentType, String deviceType1) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please Wait");

            OrderPaymentUpdateRequest orderPaymentUpdateRequest = new OrderPaymentUpdateRequest();
            orderPaymentUpdateRequest.setUid(orderDetailsResponse.getData().getUid());

            OrderPaymentUpdateRequest.OrderPayment orderPayment = new OrderPaymentUpdateRequest.OrderPayment();
            orderPayment.setAmount(String.valueOf(orderDetailsResponse.getData().getCrateAmount()));
            orderPayment.setTxnDate(CommonUtils.getCurrentTimeDate());

            OrderPaymentUpdateRequest.Type type = new OrderPaymentUpdateRequest.Type();
            type.setUid(paymentType);
            orderPayment.setType(type);

            OrderPaymentUpdateRequest.DeviceType deviceType = new OrderPaymentUpdateRequest.DeviceType();
            deviceType.setUid(deviceType1);

            orderPayment.setDeviceType(deviceType);

            orderPaymentUpdateRequest.setOrderPayment(orderPayment);

            ApiInterface apiInterface = ApiClient.getApiService();
            Call<Object> call = apiInterface.ORDER_PAYMENT_UPDATE_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), orderPaymentUpdateRequest);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NotNull Call<Object> call, @NotNull Response<Object> response) {
                    if (response.body() != null) {
                        ActivityUtils.hideDialog();
                        mListener.onSuccessOrderPaymentUpdateApiCall();
                    } else {
                        ActivityUtils.hideDialog();
                        mListener.onFailureOrderPaymentApiCall();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<Object> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }
    public void getOrderPaymentType() {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please Wait");



            ApiInterface apiInterface = ApiClient.getApiService();
            Call<Object> call = apiInterface.ORDER_PAYMENT_UPDATE_API_CALL("Bearer " + new SessionManager(context).getLoginToken(), null);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NotNull Call<Object> call, @NotNull Response<Object> response) {
                    if (response.body() != null) {
                        ActivityUtils.hideDialog();
                        mListener.onSuccessOrderPaymentUpdateApiCall();
                    } else {
                        ActivityUtils.hideDialog();
                        mListener.onFailureOrderPaymentApiCall();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<Object> call, @NotNull Throwable t) {
                    ActivityUtils.hideDialog();
                    mListener.onFialureMessage(t.getMessage());
                }
            });
        } else {
            mListener.onFialureMessage("Something went wrong.");
        }
    }
}
