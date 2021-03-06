package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static java.util.Arrays.fill;

public class MainActivity extends AppCompatActivity {

    public class Task extends AsyncTask<String, Void, String>{

        private ListView listView = findViewById(R.id.myListView);

        @Override
        protected String doInBackground(String... urls) {

            Log.i("URL", urls[0]);
            OkHttpClient client = new OkHttpClient();

            try {
                Request request = new Request.Builder().url(urls[0]).build();
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed";
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            List<Brewery> breweryList;

            Gson gson = new Gson();
            breweryList = gson.fromJson(s, new TypeToken<ArrayList<Brewery>>() {}.getType());
            fillList(breweryList, listView);

        }
    }

    public void fillList(List<Brewery> breweryList, ListView listView){

        List<LinearLayout> breweryLayoutlist = new ArrayList<>();

        for (Brewery currentBrewery : breweryList){
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

            TextView titleView = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            titleView.setLayoutParams(layoutParams);
            titleView.setText(currentBrewery.getName());
            layout.addView(titleView);

            TextView subtitleView = new TextView(this);
            LinearLayout.LayoutParams subtitleLayoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            titleView.setLayoutParams(subtitleLayoutParams);
            titleView.setText(currentBrewery.getStreet() + " " + currentBrewery.getCity());
            layout.addView(subtitleView);

            ImageView imageView = new ImageView(this);
            layout.addView(imageView);

            switch (currentBrewery.getState()){
                case "Alabama":
                    imageView.setImageResource(R.drawable.alabama);
                    break;
                case "Alaska":
                    imageView.setImageResource(R.drawable.alaska);
                    break;
                case "Arizona":
                    imageView.setImageResource(R.drawable.arizona);
                    break;
                case "Arkansas":
                    imageView.setImageResource(R.drawable.arkansas);
                    break;
                default:
                    imageView.setImageResource(R.drawable.usa);
                    break;
            }

            setContentView(layout);
            breweryLayoutlist.add(layout);

        }


        ArrayAdapter<LinearLayout> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, breweryLayoutlist);

        listView.setAdapter(arrayAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Task task = new Task();

        String result = "";

        try {
            result =  task.execute("https://api.openbrewerydb.org/breweries").get();

        } catch (Exception e){
            e.printStackTrace();
        }

        Log.i("Info", result);

    }

}

//class MyAdapter extends ArrayAdapter<String> {
//        Context context;
//
//        String rTitle[];
//        String rDescription[];
//        int[] rImgs = {R.drawable.alabama, };
//
//        MyAdapter(Context context, String[] title, String[] description, int[] imgs){
//            super(context, R.layout.row, R.id.textView1, title);
//
//            this.context = context;
//            this.rTitle = title;
//            this.rDescription = description;
//            this.rImgs = imgs;
//
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
//            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View row = layoutInflater.inflate(R.layout.row, parent, false);
//            ImageView images = row.findViewById(R.id.image);
//            TextView myTitle = row.findViewById(R.id.textView1);
//            TextView myDescription = row.findViewById(R.id.textView2);
//
////            change this for image depending on where brewery is located
//            images.setImageResource(rImgs[position]);
//            myTitle.setText(rTitle[position]);
//            myDescription.setText(rDescription[position]);
//
//
//
//            return row;
//        }
//
//    }