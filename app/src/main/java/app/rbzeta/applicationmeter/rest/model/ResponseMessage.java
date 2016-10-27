package app.rbzeta.applicationmeter.rest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Robyn on 25/10/2016.
 */

public class ResponseMessage {
    @SerializedName("code")
    private String code;

    @SerializedName("title")
    private String title;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("application")
    private List<AppBenchmark> appBenchmarkList;

    public List<AppBenchmark> getAppBenchmarkList() {
        return appBenchmarkList;
    }

    public void setAppBenchmarkList(List<AppBenchmark> appBenchmarkList) {
        this.appBenchmarkList = appBenchmarkList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
