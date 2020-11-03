package com.example.i_dictionary;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;


public class EnterWordOrSentenceActivity extends AppCompatActivity {

    String url_entry;
    SearchView enter_word_or_sentance_edittext_main;
    static LinearLayout result_linearlayout;
    static LinearLayout examples_linearlayout;
    static LinearLayout if_notfound;
    static LinearLayout linearLayout1;
    static TextView result_word;
    static String url_audio = "";
    static MediaPlayer pronunciation_sound;
    static ScrollView scrollview_main;
    static TextView synotv;
    static TextView syno;
    String synostring = "";
    boolean flag ;
    static String infection_of = "";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        StatusBarUtil.setTransparent(EnterWordOrSentenceActivity.this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_enter_word_or_sentence);

        TextView selected_language1 = findViewById(R.id.selected_language1);
        TextView selected_language2 = findViewById(R.id.selected_language2);
        linearLayout1 = findViewById(R.id.linearLayout1);
        result_word = findViewById(R.id.result_word);
        if_notfound = findViewById(R.id.if_notfound);
        if_notfound.setVisibility(View.GONE);
        scrollview_main = findViewById(R.id.scrollview_main);
        scrollview_main.setVisibility(View.GONE);

        result_linearlayout = findViewById(R.id.def_linearlayout);
        examples_linearlayout = findViewById(R.id.examples_linearlayout);

        syno = findViewById(R.id.syno);
        synotv = findViewById(R.id.synotv);

        ImageButton ic_pronunciation_sound = findViewById(R.id.ic_pronunciation_sound);
        ic_pronunciation_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(url_audio.length()!=0){
                    play_pronunciation(url_audio);
                }
            }
        });



        ImageView back_img = findViewById(R.id.back_button_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back_button_img_intent = new Intent(getApplicationContext(), MainActivity.class);
                back_button_img_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(back_button_img_intent);
                finish();
            }
        });

        enter_word_or_sentance_edittext_main = findViewById(R.id.enter_word_or_sentance_edittext_main);

        enter_word_or_sentance_edittext_main.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                result_linearlayout.removeAllViews();
                examples_linearlayout.removeAllViews();
                if_notfound.removeAllViews();
                synostring = "";
                ValueAnimator anim = ValueAnimator.ofInt(enter_word_or_sentance_edittext_main.getMeasuredHeight(), 100);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = enter_word_or_sentance_edittext_main.getLayoutParams();
                        layoutParams.height = val;
                        enter_word_or_sentance_edittext_main.setLayoutParams(layoutParams);
                    }
                });
                anim.setDuration(250);
                anim.start();

                if(pronunciation_sound!=null){
                    pronunciation_sound.release();
                    pronunciation_sound = null;
                }


                url_entry = inflections(s);
                sendrequest(url_entry);


                Log.v("infection",infection_of);
                url_entry = dictionaryEntries(infection_of);
                sendrequest(url_entry);

                return false;
            }


            @Override
            public boolean onQueryTextChange(String s) {

                scrollview_main.setVisibility(View.GONE);
                if_notfound.setVisibility(View.GONE);

                ValueAnimator anim = ValueAnimator.ofInt(enter_word_or_sentance_edittext_main.getMeasuredHeight(), 210);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = enter_word_or_sentance_edittext_main.getLayoutParams();
                        layoutParams.height = val;
                        enter_word_or_sentance_edittext_main.setLayoutParams(layoutParams);
                    }
                });
                anim.setDuration(250);
                anim.start();
                return false;
            }

        });
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void sendrequest(String url){
        IDictionaryRequest iDictionaryRequest = new IDictionaryRequest(EnterWordOrSentenceActivity.this);
        iDictionaryRequest.execute(url);
    }


    private String dictionaryEntries(String word) {
        final String language = "en-gb";
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "strictMatch=" + strictMatch;

    }
    private String dictionaryTranslation() {
        final String l_source = "en";
        final String l_target = "hi";
        final String word = "Ace";
        final String fields = "translations";
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com/api/v2/" + fields + "/" + l_source + "/" + l_target + "/" + word_id + "?strictMatch=" + strictMatch;
    }

    private String inflections(String inf_word) {
        final String language = "en";
        final String word_id = inf_word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v2/lemmas/" + language + "/" + word_id;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void play_pronunciation(String url_audio) {
        pronunciation_sound.start();
    }



    public class check_Infection extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }


    public class IDictionaryRequest extends AsyncTask<String, Integer, String> {

        final String app_id = "8012edd5";
        final String app_key = "6896b290b8852c222903f50269063375";
        String myurl;
        Context context;
        int c = 1;



        public IDictionaryRequest(Context context){
            super();
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            myurl=params[0];
            try {
                URL url = new URL(myurl);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept","application/json");
                urlConnection.setRequestProperty("app_id",app_id);
                urlConnection.setRequestProperty("app_key",app_key);

                // read the output from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                return stringBuilder.toString();

            }
            catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }



        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.v("jsonscript",s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                JSONArray results = jsonObject.getJSONArray("results");
                JSONObject resultsJSONObject = results.getJSONObject(0);

                JSONArray lexicalEntries = resultsJSONObject.getJSONArray("lexicalEntries");
                JSONObject lexicalEntriesJSONObject = null;
                String type = "";

                if(flag){
                    String word = jsonObject.getString("id");
                    result_word.setText(word);
                }
                else {
                    String word = resultsJSONObject.getString("id");
                    result_word.setText(word);
                }


                if(flag){
                    for(int i=0; i<lexicalEntries.length();i++){
                        lexicalEntriesJSONObject = lexicalEntries.getJSONObject(i);
                        assert lexicalEntriesJSONObject != null;
                        if(lexicalEntries.length()>0){
                            JSONObject lexicalCategory = (JSONObject) lexicalEntriesJSONObject.get("lexicalCategory");
                            if(lexicalCategory.get("id").equals("adjective")){
                                type = "adj.";
                            }
                            else if(lexicalCategory.get("id").equals("interjection")){
                                type = "intj.";
                            }
                            else if(lexicalCategory.get("id").equals("noun")){
                                type = "nou.";
                            }
                            else if(lexicalCategory.get("id").equals("adverb")){
                                type = "adv.";
                            }
                            else if(lexicalCategory.get("id").equals("pronoun")){
                                type = "pnou.";
                            }
                            else if(lexicalCategory.get("id").equals("verb")){
                                type = "verb.";
                            }
                            else {
                                type = "-";
                            }
                        }
                        JSONArray entries = lexicalEntriesJSONObject.getJSONArray("entries");
                        JSONObject entriesJSONObject = entries.getJSONObject(0);
                        JSONArray senses = entriesJSONObject.getJSONArray("senses");

                        for(int j=0; j<senses.length(); j++){

                            JSONObject sensesJSONObject = senses.getJSONObject(j);
                            JSONArray definitions = sensesJSONObject.getJSONArray("definitions");
                            LinearLayout parent = new LinearLayout(context);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            parent.setLayoutParams(layoutParams);
                            parent.setOrientation(LinearLayout.HORIZONTAL);
                            layoutParams.setMargins(0, 20, 0, 20);
                            result_linearlayout.addView(parent);
                            TextView tv = new TextView(context);
                            tv.setTextSize(15);
                            tv.setTypeface(Typeface.SANS_SERIF, Typeface.ITALIC);
                            TextView tv2 = new TextView(context);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(40,0,0,0);
                            tv2.setLayoutParams(params);
                            tv2.setTextColor(Color.BLACK);
                            tv2.setTextSize(15);
                            parent.addView(tv);
                            parent.addView(tv2);
                            tv2.setText(definitions.getString(0));
                            tv.setText(type);

                            try{
                                JSONArray exampleArray = sensesJSONObject.getJSONArray("examples");
                                for(int k = 0; k < exampleArray.length(); k++){
                                    JSONObject example = exampleArray.getJSONObject(k);
                                    LinearLayout parent2 = new LinearLayout(context);
                                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    parent2.setLayoutParams(layoutParams2);
                                    parent2.setOrientation(LinearLayout.HORIZONTAL);
                                    layoutParams2.setMargins(0, 20, 0, 20);
                                    examples_linearlayout.addView(parent2);
                                    TextView tv3 = new TextView(context);
                                    tv3.setTextSize(15);
                                    tv3.setTypeface(Typeface.SANS_SERIF, Typeface.ITALIC);
                                    TextView tv4 = new TextView(context);
                                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    params2.setMargins(40,0,0,0);
                                    tv4.setLayoutParams(params2);
                                    tv4.setTextColor(Color.BLACK);
                                    tv4.setTextSize(15);
                                    parent2.addView(tv3);
                                    parent2.addView(tv4);
                                    tv3.setText(String.format("%d    ", c++));
                                    tv4.setText(example.getString("text"));
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            try{
                                JSONArray synoArray = sensesJSONObject.getJSONArray("synonyms");
                                for(int k = 0; k < synoArray.length(); k++){
                                    JSONObject synoArrayJSONObject = synoArray.getJSONObject(k);
                                    if(synostring.isEmpty()){
                                        synostring = synostring + synoArrayJSONObject.getString("text");
                                    }
                                    else {
                                        synostring = synostring + " / " + synoArrayJSONObject.getString("text");
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    syno.setText(synostring);

                    lexicalEntriesJSONObject = lexicalEntries.getJSONObject(0);
                    JSONArray entries_audio = lexicalEntriesJSONObject.getJSONArray("entries");
                    JSONObject entriesJSONObject_audio = entries_audio.getJSONObject(0);
                    JSONArray pronunciations = entriesJSONObject_audio.getJSONArray("pronunciations");
                    JSONObject pronunciationsJSONObject_audio = pronunciations.getJSONObject(0);
                    url_audio = pronunciationsJSONObject_audio.getString("audioFile");

                    pronunciation_sound = new MediaPlayer();
                    pronunciation_sound.setAudioAttributes(
                            new AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .build()
                    );
                    pronunciation_sound.setDataSource(url_audio);
                    pronunciation_sound.prepare();
                    scrollview_main.setVisibility(View.VISIBLE);
                }
                else {
                    lexicalEntriesJSONObject = lexicalEntries.getJSONObject(0);
                    JSONArray inf_of_arr = lexicalEntriesJSONObject.getJSONArray("inflectionOf");
                    JSONObject inf_of_obj = inf_of_arr.getJSONObject(0);
                    infection_of = inf_of_obj.getString("text");
                    Log.v("inf","done!");

                    flag = true;
                    Log.v("flag","true!");
                }


            } catch (JSONException | IOException e) {
                e.printStackTrace();
                TextView tv = new TextView(context);
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(25);
                tv.setText("not found!");
                if_notfound.addView(tv);
                if_notfound.setVisibility(View.VISIBLE);
            }

        }
    }
}