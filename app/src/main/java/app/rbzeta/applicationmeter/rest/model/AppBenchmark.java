package app.rbzeta.applicationmeter.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Robyn on 26/10/2016.
 */

public class AppBenchmark {
    @SerializedName("application_id")
    private int applicationId;

    @SerializedName("application_name")
    private String applicationName;

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
