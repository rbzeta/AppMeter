package app.rbzeta.applicationmeter.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import app.rbzeta.applicationmeter.R;
import app.rbzeta.applicationmeter.helper.UIHelper;

/**
 * Created by Robyn on 25/10/2016.
 */

public class EditTextDialogFragment extends DialogFragment implements View.OnClickListener{

    private String mTitle,mHint,mValue;
    private EditText editTextInput;

    public EditTextDialogFragment() {
        super();
    }

    public static EditTextDialogFragment newInstance(String title, String hint, String value){
        EditTextDialogFragment fragment = new EditTextDialogFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        args.putString("hint",hint);
        args.putString("value",value);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.MyDialogFullScreen);
        Bundle args = getArguments();
        mTitle = args.getString("title");
        mHint = args.getString("hint");
        mValue = args.getString("value");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //setCancelable(false);

        View view = inflater.inflate(R.layout.fragment_dialog_edittext,container);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbarDialogEditText) ;
        toolbar.setTitle(mTitle);
        toolbar.setTitleTextColor(Color.WHITE);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextInput = (EditText)view.findViewById(R.id.editTextDialogFragment);
        editTextInput.setHint(mHint);
        editTextInput.setText(mValue);
        editTextInput.setSelectAllOnFocus(true);
        editTextInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        UIHelper.requestFocus(getDialog().getWindow(),editTextInput);

        Button buttonCancel = (Button)view.findViewById(R.id.buttonDialogEditTextCancel);
        Button buttonOk = (Button)view.findViewById(R.id.buttonDialogEditTextOK);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        buttonOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ButtonOKDialogListener listener = (ButtonOKDialogListener)getActivity();
        listener.onButtonOKDialogPressed(editTextInput.getText().toString().trim());
        dismiss();

    }

    public interface ButtonOKDialogListener{
        void onButtonOKDialogPressed(String text);
    }
}
