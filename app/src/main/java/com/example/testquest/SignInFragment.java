package com.example.testquest;

import android.content.Context;
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

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SignInFragment extends Fragment {

    private ImageView mPersonIcon;
    private Button mSignInBtn;
    private EditText mNickname;
    private EditText mPassword;
    private Banner mBanner;

    public SignInFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mPersonIcon = rootView.findViewById(R.id.person_iv);
        mSignInBtn = rootView.findViewById(R.id.sign_in_btn);
        mBanner = getActivity().findViewById(R.id.banner);
        mNickname = rootView.findViewById(R.id.nickname_tv);
        mPassword = rootView.findViewById(R.id.password_et);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                    SystemUtils.closeKeyboard(getActivity());
                if (SystemUtils.isOnline(getContext())) {
                    String nickname = mNickname.getText().toString();
                    String password = mPassword.getText().toString();
                    if (CheckUtils.checkAll(nickname, password)) {
                        mBanner.dismiss();
                        NetworkUtils.authorize(getContext(), getFragmentManager(), nickname, password);
                    } else {
                        mBanner.setMessage(CheckUtils.getErrorMsg(getContext(), nickname, password));
                        mBanner.show();
                    }
                } else {
                    SystemUtils.showNoInternetAlert(getContext(), getFragmentManager());
                }

            }
        });
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
