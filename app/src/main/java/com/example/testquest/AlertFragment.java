package com.example.testquest;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AlertFragment extends DialogFragment {

    private Button mButton;
    private Dialog mDialog;
    private TextView mTitle;
    private TextView mMessage;
    private LottieAnimationView mLottie;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.dialog_sign_up, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitle = view.findViewById(R.id.title_tv);
        mMessage = view.findViewById(R.id.message_tv);
        mLottie = view.findViewById(R.id.animated_icon);
        mButton = view.findViewById(R.id.action_btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });

        if (getArguments() != null) {
            String title = getArguments().getString(NetworkUtils.TITLE_KEY);
            String message = getArguments().getString(NetworkUtils.MESSAGE_KEY);
            mTitle.setText(title);
            mMessage.setText(message);
            if (title.equals(getString(R.string.error))
                    || title.equals(getString(R.string.error_no_internet))) {
                setWrong();
            }
        }
    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        mDialog = super.onCreateDialog(savedInstanceState);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = mDialog.getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        return mDialog;
    }

    private void setWrong() {
        mLottie.setAnimation(R.raw.connection_error);
        mButton.setBackground(getResources().getDrawable(R.drawable.rounded_btn_wrong_ok));
    }
}