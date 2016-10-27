package app.rbzeta.applicationmeter.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import app.rbzeta.applicationmeter.R;
import app.rbzeta.applicationmeter.dialog.GeneralEditTextDialogFragment;
import app.rbzeta.applicationmeter.helper.SessionManager;
import app.rbzeta.applicationmeter.helper.UIHelper;

public class UserProfileActivity extends AppCompatActivity
        implements GeneralEditTextDialogFragment.ButtonOKDialogListener{

    private String mUserName,mPersonalNumber,mPhoneNumber;
    private Button btnUserName,btnPersonalNumber,btnPhoneNumber,btnSaveProfile;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        session = new SessionManager(this,MODE_PRIVATE);

        btnPersonalNumber = (Button)findViewById(R.id.buttonFillPN);
        btnUserName = (Button)findViewById(R.id.buttonFillName);
        btnPhoneNumber = (Button)findViewById(R.id.buttonFillPhone);
        btnSaveProfile = (Button)findViewById(R.id.buttonSaveProfile);

        if(session.getUserName() != "" &&
                session.getUserEmployeeId() != 0 &&
                session.getUserPhone() != ""){
            mUserName = session.getUserName();
            mPersonalNumber = String.valueOf(session.getUserEmployeeId());
            mPhoneNumber = session.getUserPhone();

            btnUserName.setText(mUserName);
            btnPhoneNumber.setText(mPhoneNumber);
            btnPersonalNumber.setText(mPersonalNumber);
        }

        btnUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "";
                if(btnUserName.getText().toString() != getString(R.string.button_input_name)){
                    text = btnUserName.getText().toString();
                }
                GeneralEditTextDialogFragment dialog = new GeneralEditTextDialogFragment().newInstance(
                        getString(R.string.dialog_title_input_name),
                        getString(R.string.dialog_title_input_name_hint),
                        text,
                        getString(R.string.dialog_title_input_name_hint),
                        getString(R.string.text));
                dialog.show(getSupportFragmentManager(),"edit_text_dialog");

            }
        });

        btnPersonalNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "";
                if(btnPersonalNumber.getText().toString() != getString(R.string.button_input_pn)){
                    text = btnPersonalNumber.getText().toString();
                }
                GeneralEditTextDialogFragment dialog = new GeneralEditTextDialogFragment().newInstance(
                        getString(R.string.dialog_title_input_pn),
                        getString(R.string.dialog_title_input_pn_hint),
                        text,
                        getString(R.string.dialog_title_input_pn_hint),
                        getString(R.string.number));
                dialog.show(getSupportFragmentManager(),"edit_text_dialog");
            }
        });

        btnPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "";
                if(btnPhoneNumber.getText().toString() != getString(R.string.button_input_phone)){
                    text = btnPhoneNumber.getText().toString();
                }
                GeneralEditTextDialogFragment dialog = new GeneralEditTextDialogFragment().newInstance(
                        getString(R.string.dialog_title_input_phone),
                        getString(R.string.dialog_title_input_phone_hint),
                        text,
                        getString(R.string.dialog_title_input_phone_hint),
                        getString(R.string.phone));
                dialog.show(getSupportFragmentManager(),"edit_text_dialog");
            }
        });

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });

    }

    private void saveUserProfile() {
        if (mUserName != null &&
                mPersonalNumber != null &&
                mPhoneNumber != null) {

            session.setUserName(mUserName);
            session.setUserEmployeeId(Integer.parseInt(mPersonalNumber));
            session.setUserPhone(mPhoneNumber);

            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();

        }else
            UIHelper.showCustomSnackBar(btnSaveProfile,
                    getString(R.string.err_complete_profile),
                    Color.RED);

    }

    @Override
    public void onButtonOKPressed(String text, String editTextType) {

        text = text.trim();

        if (!text.equals("")){
            if (editTextType.equals(getString(R.string.dialog_title_input_name_hint))) {
                if (!session.getUserName().equals(text)){
                    if (text.length() > getResources().getInteger(R.integer.max_name)){
                        UIHelper.showCustomSnackBar(btnUserName,
                                getString(R.string.err_user_name_length), Color.RED);
                        return;
                    }

                    mUserName = text;
                    btnUserName.setText(text);
                }

            }else if (editTextType.equals(getString(R.string.dialog_title_input_phone_hint))) {
                if (!session.getUserPhone().equals(text)) {
                    if (text.length() > getResources().getInteger(R.integer.max_phone_number) ||
                            text.length() < getResources().getInteger(R.integer.min_phone_number)){
                        UIHelper.showCustomSnackBar(btnPhoneNumber,
                                getString(R.string.err_user_phone_length),Color.RED);
                        return;
                    }else if (Long.parseLong(text) == 0)return;

                    mPhoneNumber = text;
                    btnPhoneNumber.setText(text);

                }
            }else if (editTextType.equals(getString(R.string.dialog_title_input_pn_hint))) {
                if (!String.valueOf(session.getUserEmployeeId()).equals(text)) {
                    if (text.length() > getResources().getInteger(R.integer.max_emp_id)){
                        UIHelper.showCustomSnackBar(btnPersonalNumber,
                                getString(R.string.err_user_empid_length),Color.RED);
                        return;
                    }else if (Integer.valueOf(text) == 0)return;

                    mPersonalNumber = text;
                    btnPersonalNumber.setText(text);
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
