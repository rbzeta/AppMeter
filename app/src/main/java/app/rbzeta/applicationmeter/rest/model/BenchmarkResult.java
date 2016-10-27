package app.rbzeta.applicationmeter.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Robyn on 25/10/2016.
 */

public class BenchmarkResult {
    @SerializedName("application_id")
    int applicationId;

    @SerializedName("branch_id")
    int branchId;

    @SerializedName("time")
    long timeMillis;

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public long getTimeMillis() {
        return timeMillis;
    }

    public void setTimeMillis(long time) {
        this.timeMillis = time;
    }
}
