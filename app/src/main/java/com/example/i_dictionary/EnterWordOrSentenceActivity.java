package com.example.i_dictionary;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import com.jaeger.library.StatusBarUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;


public class EnterWordOrSentenceActivity extends AppCompatActivity {

    String url_entry;
    TextView definition;
    Button b;
    Context context;
    SearchView enter_word_or_sentance_edittext_main;
    static TextView t5, type;

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
        t5 = findViewById(R.id.textView5);
        type = findViewById(R.id.textViewType);

        ImageView back_img = findViewById(R.id.back_button_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back_button_img_intent = new Intent(EnterWordOrSentenceActivity.this, MainActivity.class);
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
                isNetworkStatusAvialable(EnterWordOrSentenceActivity.this);
                url_entry = dictionaryEntries(s);
                sendrequest();

                return false;
            }


            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }


    public void sendrequest(){
        Log.v("clicked", "sendrequest_method");
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
        return "https://od-api.oxforddictionaries.com/api/v2/" + fields + "/" + l_source + "/" + l_target + "/" + word_id + "?strictMatch=" + strictMatch + "&fields=" + fields;
    }
    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
                return netInfos.isConnected();
        }
        return false;
    }

    public static class IDictionaryRequest extends AsyncTask<String, Integer, String> {

        final String app_id = "8012edd5";
        final String app_key = "6896b290b8852c222903f50269063375";
        String myurl;
        Context context;
        ArrayList<String> dr = new ArrayList<>();


        public IDictionaryRequest(Context context){
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



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.v("jsonscript",s);
            

        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray results = jsonObject.getJSONArray("results");
            JSONObject resultsJSONObject = results.getJSONObject(0);

            JSONArray lexicalEntries = resultsJSONObject.getJSONArray("lexicalEntries");

            JSONObject lexicalEntriesJSONObject = lexicalEntries.getJSONObject(0);

            JSONArray entries = lexicalEntriesJSONObject.getJSONArray("entries");
            JSONObject entriesJSONObject = entries.getJSONObject(0);

            JSONArray senses = entriesJSONObject.getJSONArray("senses");
            JSONObject sensesJSONObject = senses.getJSONObject(0);

            for(int j=0; j<5; j++){
                JSONArray definitions = sensesJSONObject.getJSONArray("definitions");
                dr.add(definitions.getString(0));

                try{
                    JSONArray subsenses = sensesJSONObject.getJSONArray("subsenses");
                    JSONObject subsensesJSONObject = subsenses.getJSONObject(0);
                    JSONArray subdefinitions = subsensesJSONObject.getJSONArray("definitions");
                    JSONObject lexicalCategory = (JSONObject) lexicalEntriesJSONObject.get("lexicalCategory");
                    Log.d("testing", "testing: " + lexicalCategory.get("text"));
                    dr.add(subdefinitions.getString(0));
                    StringBuilder r = new StringBuilder();
                    for(int i = 0; i<dr.size(); i++){
                        r.append(". ").append(dr.get(i));
                    }
                    type.setText(String.valueOf(lexicalCategory.get("text")));
                    t5.setText(r);
                }
                catch (Exception e){}
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        }
    }
}