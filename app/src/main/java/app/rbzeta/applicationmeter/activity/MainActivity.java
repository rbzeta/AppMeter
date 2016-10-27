package app.rbzeta.applicationmeter.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import app.rbzeta.applicationmeter.R;
import app.rbzeta.applicationmeter.bc.ConnectivityReceiver;
import app.rbzeta.applicationmeter.dialog.EditTextDialogFragment;
import app.rbzeta.applicationmeter.helper.UIHelper;
import app.rbzeta.applicationmeter.rest.ApiClient;
import app.rbzeta.applicationmeter.rest.ApiInterface;
import app.rbzeta.applicationmeter.rest.model.AppBenchmark;
import app.rbzeta.applicationmeter.rest.model.BenchmarkResult;
import app.rbzeta.applicationmeter.rest.model.ResponseMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements EditTextDialogFragment.ButtonOKDialogListener{
    Button butnstart, butnreset,butnsubmit,buttonChooseApp,buttonChooseBranch;
    TextView textMinute,textSecond,textMillis;
    long starttime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedtime = 0L;
    int t = 1;
    int secs = 0;
    int mins = 0;
    int milliseconds = 0;
    int mBranchCode = 0;
    int mApplicationId = -1;
    Handler handler = new Handler();

    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        butnstart = (Button) findViewById(R.id.start);
        butnreset = (Button) findViewById(R.id.reset);
        butnsubmit = (Button) findViewById(R.id.submit);
        buttonChooseApp = (Button) findViewById(R.id.buttonChooseApp);
        buttonChooseBranch = (Button) findViewById(R.id.buttonBranchCode);
        textMinute = (TextView) findViewById(R.id.timerMinute);
        textSecond = (TextView) findViewById(R.id.timerSecond);
        textMillis = (TextView) findViewById(R.id.timerMillis);

        butnstart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!(mApplicationId >= 0)){
                    UIHelper.showCustomSnackBar(v,getString(R.string.err_app_id_not_choosen),Color.RED);
                    return;
                }

                if (mBranchCode == 0){
                    UIHelper.showCustomSnackBar(v,getString(R.string.err_branch_id_not_fill),Color.RED);
                    return;
                }
                if (t == 1) {
                    butnstart.setText("Pause");
                    starttime = SystemClock.uptimeMillis();
                    handler.postDelayed(updateTimer, 0);
                    t = 0;
                    butnsubmit.setVisibility(View.GONE);
                } else {
                    butnstart.setText(getString(R.string.start));
                    //time.setTextColor(Color.BLUE);
                    timeSwapBuff += timeInMilliseconds;
                    handler.removeCallbacks(updateTimer);
                    butnsubmit.setVisibility(View.VISIBLE);
                    t = 1;
                }}
        });

        butnreset.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

               resetTimer();
            }});


        buttonChooseApp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ConnectivityReceiver.isConnected()){
                    UIHelper.showCustomSnackBar(v,
                            getString(R.string.err_no_network_connection), Color.WHITE);

                }else{
                    populateAppListFromServer();
                }

            }


        });
        
        buttonChooseBranch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputBranchId();
            }
        });

        butnsubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ConnectivityReceiver.isConnected()){
                    UIHelper.showCustomSnackBar(v,
                            getString(R.string.err_no_network_connection), Color.WHITE);

                }else{
                    submitResult();
                }

            }
        });
    }

    private void populateAppListFromServer() {
        final ProgressDialog progress = ProgressDialog.show(this,
                getString(R.string.dialog_title_fetch_app),
                getString(R.string.please_wait),true,false);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseMessage> call = apiService.fetchApplicationList();
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                progress.dismiss();

                if (response.body() != null){

                    ResponseMessage msg = response.body();

                    if (msg.isSuccess()){

                        if (msg.getAppBenchmarkList() != null) {

                            List<Map<String,String>> items = new ArrayList<>();

                            for (AppBenchmark app :msg.getAppBenchmarkList()) {
                                HashMap<String,String> appMap = new HashMap<>();
                                appMap.put("id",String.valueOf(app.getApplicationId()));
                                appMap.put("name",app.getApplicationName());

                                items.add(appMap);

                            }

                            adapter = new SimpleAdapter(MainActivity.this,items ,
                                    R.layout.application_row_item,new String[] {"id","name"},
                                    new int[] {R.id.textAppId,R.id.textAppName});
                            showSelectAppDialog();

                        }else{
                            UIHelper.showCustomSnackBar(buttonChooseApp,getString(R.string.err_no_app),
                                    Color.RED);
                        }



                    }else {
                        UIHelper.showCustomSnackBar(butnsubmit,msg.getMessage(),
                                Color.RED);
                    }

                }else {
                    UIHelper.showCustomSnackBar(butnsubmit,getString(R.string.err_sending_result),
                            Color.RED);
                }

            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                progress.dismiss();
                UIHelper.showCustomSnackBar(butnsubmit,getString(R.string.err_sending_result),
                        Color.RED);

            }
        });
    }

    private void submitResult() {
        if (mBranchCode != 0 && mApplicationId >= 0){
            sendResult();
        }else
            UIHelper.showCustomSnackBar(butnsubmit,getString(R.string.err_submit_result),Color.RED);
    }

    private void sendResult() {

        final ProgressDialog progress = ProgressDialog.show(this,
                getString(R.string.dialog_title_send_result),
                getString(R.string.please_wait),true,false);

        long timemillis = TimeUnit.MINUTES.toMillis(mins) +
                TimeUnit.SECONDS.toMillis(secs) +
                + milliseconds;
        BenchmarkResult result = new BenchmarkResult();
        result.setApplicationId(mApplicationId);
        result.setBranchId(mBranchCode);
        result.setTimeMillis(timemillis);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseMessage> call = apiService.sendResultData(result);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                progress.dismiss();

                if (response.body() != null){

                    ResponseMessage msg = response.body();

                    if (msg.isSuccess()){

                        UIHelper.showCustomSnackBar(butnsubmit,msg.getMessage(),
                                Color.WHITE);

                        resetTimer();
                        resetView();

                    }else {
                        UIHelper.showCustomSnackBar(butnsubmit,msg.getMessage(),
                                Color.RED);
                    }

                }else {
                    UIHelper.showCustomSnackBar(butnsubmit,getString(R.string.err_sending_result),
                            Color.RED);
                }

            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                progress.dismiss();
                UIHelper.showCustomSnackBar(butnsubmit,getString(R.string.err_sending_result),
                        Color.RED);

            }
        });

    }

    private void resetView() {
        mBranchCode = 0;
        mApplicationId = -1;

        buttonChooseApp.setText(getString(R.string.button_choose_app));
        buttonChooseBranch.setText(getString(R.string.button_choose_branch_code));

        butnsubmit.setVisibility(View.GONE);
    }

    private void resetTimer() {
        starttime = 0L;
        timeInMilliseconds = 0L;
        timeSwapBuff = 0L;
        updatedtime = 0L;
        t = 1;
        secs = 0;
        mins = 0;
        milliseconds = 0;
        butnstart.setText("Start");
        handler.removeCallbacks(updateTimer);
        textMinute.setText(getString(R.string.start_time_hour));
        textSecond.setText(getString(R.string.start_time_hour));
        textMillis.setText(getString(R.string.start_time_hour));
        butnsubmit.setVisibility(View.GONE);
    }

    private void showInputBranchId() {
        EditTextDialogFragment dialog = new EditTextDialogFragment().newInstance(
                getString(R.string.dialog_title_input_branch),
                getString(R.string.dialog_title_input_branch_hint),
                buttonChooseBranch.getText().toString());
        dialog.show(getSupportFragmentManager(),"edit_text_dialog");
    }

    private void showSelectAppDialog() {
        //final String[] items = {"Buka Blokir Bripens","Tarik Setor Teller","Interface Brinet Las"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.choose_app_dialog_title));
        /*builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedIndex = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                if(selectedIndex >= 0){
                    if (!(mApplicationId == selectedIndex))
                        resetTimer();
                    //buttonChooseApp.setText(items[selectedIndex]);
                    mApplicationId = selectedIndex;


                }else{
                    buttonChooseApp.setText(getString(R.string.button_choose_app));
                    mApplicationId = selectedIndex;
                    butnsubmit.setVisibility(View.GONE);
                    resetTimer();
                }



            }
        });*/
        builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedIndex = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                HashMap<String,String> item = (HashMap)((AlertDialog)dialog)
                        .getListView().getItemAtPosition(which);

                int itemId = Integer.parseInt(item.get("id"));

                if(selectedIndex >= 0){
                    if (!(mApplicationId == itemId))
                        resetTimer();
                    buttonChooseApp.setText(item.get("name"));
                    mApplicationId = itemId;

                }else{
                    buttonChooseApp.setText(getString(R.string.button_choose_app));
                    mApplicationId = itemId ;
                    butnsubmit.setVisibility(View.GONE);
                    resetTimer();
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public Runnable updateTimer = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - starttime;
            updatedtime = timeSwapBuff + timeInMilliseconds;
            secs = (int) (updatedtime / 1000);
            mins = secs / 60;
            secs = secs % 60;
            milliseconds = (int) (updatedtime % 1000);
            textMinute.setText(String.format("%02d", mins));
            textSecond.setText(String.format("%02d", secs));
            textMillis.setText(String.format("%03d", milliseconds));
            handler.postDelayed(this, 0);
        }};

    @Override
    public void onButtonOKDialogPressed(String text) {
        text = text.trim();
        if (!text.equals("")) {

                if (text.length() > 4) {
                    UIHelper.showCustomSnackBar(textMinute,
                            getString(R.string.err_user_branchid_length), Color.RED);
                    return;


                } else if (Integer.valueOf(text) == 0) return;

                if (mBranchCode == Integer.parseInt(text)) return;

                buttonChooseBranch.setText(text);
                mBranchCode = Integer.parseInt(text);
                butnsubmit.setVisibility(View.GONE);
                resetTimer();


        }

    }
}