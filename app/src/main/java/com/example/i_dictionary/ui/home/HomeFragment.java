package com.example.i_dictionary.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.i_dictionary.EnterWordOrSentenceActivity;
import com.example.i_dictionary.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        RelativeLayout enter_word_or_sentence_layout = root.findViewById(R.id.enter_word_or_sentance_layout);
        enter_word_or_sentence_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent enter_word_or_sentance_intent = new Intent(getContext(), EnterWordOrSentenceActivity.class);
                enter_word_or_sentance_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(enter_word_or_sentance_intent);
            }
        });
        EditText enter_word_or_sentance_edittext = root.findViewById(R.id.enter_word_or_sentance_edittext);
        enter_word_or_sentance_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent enter_word_or_sentance_edittext_intent = new Intent(getContext(), EnterWordOrSentenceActivity.class);
                enter_word_or_sentance_edittext_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(enter_word_or_sentance_edittext_intent);
            }
        });
        return root;
    }
}