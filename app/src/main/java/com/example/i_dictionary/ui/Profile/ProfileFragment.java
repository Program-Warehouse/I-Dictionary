package com.example.i_dictionary.ui.Profile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
        RelativeLayout share_with_friends_layout = root.findViewById(R.id.share_with_friends_layout);
        share_with_friends_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent sendmsgIntent = new Intent();
                sendmsgIntent.setAction(Intent.ACTION_SEND);
                sendmsgIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendmsgIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendmsgIntent, null);
                startActivity(shareIntent);
            }
        });
        RelativeLayout rate_it_layout = root.findViewById(R.id.rate_it_layout);
        rate_it_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                rateIt();
            }
        });

        RelativeLayout settings_layout = root.findViewById(R.id.settings_layout);
        settings_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settings_intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(settings_intent);
            }
        });


        return root;
    }



    public void rateIt(){
        final Dialog rate_it_dialouge = new Dialog(getContext());
        rate_it_dialouge.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rate_it_dialouge.setContentView(R.layout.dialoge_rate_it_layout);
        rate_it_dialouge.getWindow().setLayout((int) pxFromDp(getContext(),382),(int) pxFromDp(getContext(),320));
        rate_it_dialouge.show();
        final TextView rate_it_ratetxt_short = rate_it_dialouge.findViewById(R.id.rate_it_ratetxt_short);
        final RatingBar rate_it_ratingbar = rate_it_dialouge.findViewById(R.id.rate_it_ratingBar);
        final TextView rate_it_ratetxt_long = rate_it_dialouge.findViewById(R.id.rate_it_ratetxt_long);
        final TextView rate_it_sure_or_feedback = rate_it_dialouge.findViewById(R.id.rate_it_sure_or_feedback);
        rate_it_ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            // Called when the user swipes the RatingBar
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate_it_dialouge.getWindow().setLayout((int) pxFromDp(getContext(),382),(int) pxFromDp(getContext(),452));
                if(rate_it_ratingbar.getRating()==1){
                    rate_it_ratetxt_short.setText("Hated it");
                    rate_it_ratetxt_long.setText("Your suggestions help I-Dictionary do better. Tell us your feedback!");
                    rate_it_sure_or_feedback.setText("FEEDBACK");
                }
                else if(rate_it_ratingbar.getRating()==2){
                    rate_it_ratetxt_short.setText("Disliked it");
                    rate_it_ratetxt_long.setText("Your suggestions help I-Dictionary do better. Tell us your feedback!");
                    rate_it_sure_or_feedback.setText("FEEDBACK");
                }
                else if(rate_it_ratingbar.getRating()==3){
                    rate_it_ratetxt_short.setText("It's OK");
                    rate_it_ratetxt_long.setText("Your suggestions help I-Dictionary do better. Tell us your feedback!");
                    rate_it_sure_or_feedback.setText("FEEDBACK");
                }
                else if(rate_it_ratingbar.getRating()==4){
                    rate_it_ratetxt_short.setText("Liked it");
                    rate_it_ratetxt_long.setText("Why not show some love with a nice comment on Google Play?");
                    rate_it_sure_or_feedback.setText("  SURE  ");
                }
                else if(rate_it_ratingbar.getRating()==5){
                    rate_it_ratetxt_short.setText("Loved it");
                    rate_it_ratetxt_long.setText("Why not show some love with a nice comment on Google Play?");
                    rate_it_sure_or_feedback.setText("  SURE  ");
                }
            }
        });
        TextView not_now = rate_it_dialouge.findViewById(R.id.not_now);
        not_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rate_it_dialouge.cancel();
            }
        });
        rate_it_sure_or_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rate_it_sure_or_feedback.getText()=="FEEDBACK"){
                    Intent rate_it_feedback_intent = new Intent(getContext(),FeedbackActivity.class);
                    startActivity(rate_it_feedback_intent);
                }
            }
        });
    }
    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}