package com.example.testquest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InfoFragment extends Fragment {

    private TextView mName;
    private TextView mActivity;
    private TextView mAge;
    private TextView mEmail;


    public InfoFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);
        mName = rootView.findViewById(R.id.name_tv);
        mActivity = rootView.findViewById(R.id.activity_tv);
        mAge = rootView.findViewById(R.id.age_tv);
        mEmail = rootView.findViewById(R.id.email_tv);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NetworkUtils.getUserInfo(getContext(), mName, mActivity, mAge, mEmail);
    }
}
