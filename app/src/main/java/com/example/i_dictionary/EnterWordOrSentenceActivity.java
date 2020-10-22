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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    TextView definition;
    Button b;
    Context context;
    SearchView enter_word_or_sentance_edittext_main;
    static LinearLayout result_linear_layout;
    static TextView result_word;
    static String url_audio = "";
    static MediaPlayer pronunciation_sound;





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
        result_linear_layout = findViewById(R.id.result_linear_layout);
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

                result_linear_layout.removeAllViews();
                url_entry = dictionaryEntries(s);
                sendrequest();
                return false;
            }


            @Override
            public boolean onQueryTextChange(String s) {
                result_linear_layout.removeAllViews();

                ValueAnimator anim = ValueAnimator.ofInt(enter_word_or_sentance_edittext_main.getMeasuredHeight(), 200);
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
    public void sendrequest(){
        IDictionaryRequest iDictionaryRequest = new IDictionaryRequest(EnterWordOrSentenceActivity.this);
        iDictionaryRequest.execute(url_entry);
    }


    private String dictionaryEntries(String word) {
        final String language = "en-gb";
        final String fields = "definitions";
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void play_pronunciation(String url_audio) {
        pronunciation_sound.start();
        pronunciation_sound.release();
        pronunciation_sound = null;
    }


    public static class IDictionaryRequest extends AsyncTask<String, Integer, String> {

        final String app_id = "8012edd5";
        final String app_key = "6896b290b8852c222903f50269063375";
        String myurl;
        Context context;
        int i=0;



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

                //result_word.setText(resultsJSONObject.getString("id"));

                JSONArray lexicalEntries = resultsJSONObject.getJSONArray("lexicalEntries");
                JSONObject lexicalEntriesJSONObject = null;
                String type = "";


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
                        result_linear_layout.addView(parent);
                        TextView tv = new TextView(context);
                        tv.setTextColor(Color.BLACK);
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
                    }

                    lexicalEntriesJSONObject = lexicalEntries.getJSONObject(0);
                    JSONArray entries_audio = lexicalEntriesJSONObject.getJSONArray("entries");
                    JSONObject entriesJSONObject_audio = entries_audio.getJSONObject(0);
                    JSONArray pronunciations = entriesJSONObject_audio.getJSONArray("pronunciations");
                    JSONObject pronunciationsJSONObject_audio = pronunciations.getJSONObject(0);


                    url_audio = pronunciationsJSONObject_audio.getString("audioFile");
                    Log.v("au", url_audio);
                    pronunciation_sound = new MediaPlayer();
                    pronunciation_sound.setAudioAttributes(
                            new AudioAttributes.Builder()
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .build()
                    );
                    pronunciation_sound.setDataSource(url_audio);
                    pronunciation_sound.prepare();
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

        }
    }
}