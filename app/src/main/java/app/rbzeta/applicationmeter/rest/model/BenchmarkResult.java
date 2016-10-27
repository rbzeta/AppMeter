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

    @SerializedName("name")
    String name;

    @SerializedName("phone")
    String phone;

    @SerializedName("personal_number")
    int personalNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(int personalNumber) {
        this.personalNumber = personalNumber;
    }

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
