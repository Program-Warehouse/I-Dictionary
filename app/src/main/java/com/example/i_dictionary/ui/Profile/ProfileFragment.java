package com.example.i_dictionary.ui.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.i_dictionary.FeedbackActivity;
import com.example.i_dictionary.R;

public class ProfileFragment extends Fragment {

    private ProfileViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        RelativeLayout main_layout = root.findViewById(R.id.feedback_layout);
        main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                        Intent feedback_layout_intent = new Intent(getContext(), FeedbackActivity.class);
                        startActivity(feedback_layout_intent);
            }
        });

        return root;

    }
}