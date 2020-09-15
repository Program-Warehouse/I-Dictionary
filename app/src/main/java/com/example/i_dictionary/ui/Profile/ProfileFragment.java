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
import com.example.i_dictionary.PrivacyPolicyActivity;
import com.example.i_dictionary.R;
import com.example.i_dictionary.SatisfactionSurveyActivity;

public class ProfileFragment extends Fragment {


    private ProfileViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        RelativeLayout feedback_layout = root.findViewById(R.id.feedback_layout);
        feedback_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                        Intent feedback_layout_intent = new Intent(getContext(), FeedbackActivity.class);
                        startActivity(feedback_layout_intent);
            }
        });
        RelativeLayout privacy_policy_layout = root.findViewById(R.id.privacy_policy_layout);
        privacy_policy_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent privacy_policy_intent = new Intent(getContext(), PrivacyPolicyActivity.class);
                startActivity(privacy_policy_intent);
            }
        });
        RelativeLayout satisfaction_survey_layout = root.findViewById(R.id.satisfaction_survey_layout);
        satisfaction_survey_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent satisfaction_survey_intent = new Intent(getContext(), SatisfactionSurveyActivity.class);
                startActivity(satisfaction_survey_intent);
            }
        });
        return root;

    }
}