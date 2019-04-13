package com.example.testquest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sergivonavi.materialbanner.Banner;
import com.sergivonavi.materialbanner.BannerInterface;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SignUpFragment extends Fragment {

    private EditText mNickname;
    private EditText mEmail;
    private EditText mPassword;
    private Banner mBanner;
    private Button mSignUpBtn;
    private boolean mIsKeyboardActive;
    private ImageView mPersonIcon;
    private FragmentManager fragmentManager;
    private View.OnFocusChangeListener mCloseBanner;

    public SignUpFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        mNickname = rootView.findViewById(R.id.nickname_tv);
        mEmail = rootView.findViewById(R.id.email_et);
        mPassword = rootView.findViewById(R.id.password_et);
        mBanner = getActivity().findViewById(R.id.banner);
        mSignUpBtn = rootView.findViewById(R.id.sign_up_btn);
        mPersonIcon = rootView.findViewById(R.id.person_iv);
        mCloseBanner = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) mBanner.dismiss();
            }
        };
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
    }

    private void setListeners() {
        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                    SystemUtils.closeKeyboard(getActivity());

                if (SystemUtils.isOnline(getContext())) {
                    String nickname = mNickname.getText().toString();
                    String email = mEmail.getText().toString();
                    String password = mPassword.getText().toString();
                    if (CheckUtils.checkAll(nickname, email, password)) {
                        mBanner.dismiss();
                        NetworkUtils.register(getContext(), getFragmentManager(), nickname, email, password);
                    } else {
                        mBanner.setMessage(CheckUtils.getErrorMsg(getContext(), nickname, email, password));
                        mBanner.show();
                    }
                } else {
                    SystemUtils.showNoInternetAlert(getContext(), getFragmentManager());
                }
            }
        });

        mNickname.setOnFocusChangeListener(mCloseBanner);
        mEmail.setOnFocusChangeListener(mCloseBanner);
        mPassword.setOnFocusChangeListener(mCloseBanner);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (isOpen) {
                            TranslateAnimation animate
                                    = new TranslateAnimation(0, 0, 0, -mPersonIcon.getWidth());
                            animate.setDuration(300);
                            animate.setFillAfter(true);
                            mPersonIcon.startAnimation(animate);
                            mPersonIcon.setVisibility(View.GONE);
                        } else {
                            TranslateAnimation animate
                                    = new TranslateAnimation(0, 0, -mPersonIcon.getWidth(), 0);
                            animate.setDuration(300);
                            animate.setFillAfter(true);
                            mPersonIcon.startAnimation(animate);
                            mPersonIcon.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}
