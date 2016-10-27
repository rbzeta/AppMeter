package app.rbzeta.applicationmeter.rest;

import app.rbzeta.applicationmeter.app.AppConfig;
import app.rbzeta.applicationmeter.rest.model.BenchmarkResult;
import app.rbzeta.applicationmeter.rest.model.ResponseMessage;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Robyn on 25/10/2016.
 */

public interface ApiInterface {

    @POST(AppConfig.SEND_RESULT_URL)
    Call<ResponseMessage> sendResultData(@Body BenchmarkResult result);

    @POST(AppConfig.FETCH_APPLICATION_URL)
    Call<ResponseMessage> fetchApplicationList();

}
