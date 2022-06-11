package com.aqua.anroid.policynoticeapp.Favorite;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.aqua.anroid.policynoticeapp.Calendar.EventEditActivity;
import com.aqua.anroid.policynoticeapp.User.MenuActivity;
import com.aqua.anroid.policynoticeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/*즐겨찾기 클릭 시*/
public class FavoriteActivity extends AppCompatActivity implements FavoriteAdapter.OnItemClick {

    private static String IP_ADDRESS = "10.0.2.2";
    private static String TAG = "phptest";
    private static final String TAG_JSON="root";

    public static Context mcontext;
    ArrayList<FavoriteData> items= new ArrayList<>();

    public ArrayList<String> items_name= new ArrayList<>();
    public ArrayList<String> items_content= new ArrayList<>();
    public ArrayList<String> servIDs= new ArrayList<>();
    public ArrayList<String> CloseDts= new ArrayList<>();

    FavoriteAdapter favoriteAdapter;
    String searchServID; //서비스아이디값

    String userID;
    ImageView menubtn;
    String mJsonString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        mcontext=this;

        //메뉴버튼 클릭 시 메뉴화면으로 이동
        menubtn = findViewById(R.id.menubtn);
        menubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FavoriteActivity.this, MenuActivity.class);
                startActivity(intent);

            }
        });

        ListView listView = findViewById(R.id.listView_favorite_list);
        favoriteAdapter = new FavoriteAdapter(this, this);
        listView.setAdapter(favoriteAdapter);


        SharedPreferences sharedPreferences = getSharedPreferences("userID",MODE_PRIVATE);
        userID  = sharedPreferences.getString("userID","");

        Log.d("유저id_to_favo",userID);

        GetData task = new GetData();
        task.execute(userID);

    }

    @Override
    public void onClick(String value) {
        searchServID = value;
        Log.d("searchServID",searchServID);
        if(value.charAt(0)=='W'){
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("servID",value);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, WorkDetailActivity.class);
            intent.putExtra("servID",value);
            startActivity(intent);
        }


    }


    class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        progressDialog = ProgressDialog.show(FavoriteActivity.this,
                "Please Wait", null, true, true);


        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result != null) {
                mJsonString = result;

                showResult();
            }

        }


        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];

            String serverURL = "http://10.0.2.2/favorite_query.php";
            String postParameters = "userID=" + searchKeyword1;

            Log.d(TAG, "userID_favorite : " + searchKeyword1);

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }


        private void showResult() {

            try {
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);
                    Log.d(TAG, "JSONObject : " + item);

                    String item_name = item.getString("item_name");
                    String item_content = item.getString("item_content");
                    String servID = item.getString("servID");
                    String CloseDt = item.getString("CloseDt");

                    items_name.add(item_name); //items_name ArrayList에 php에서 받아온 item_name 추가
                    items_content.add(item_content); //items_content ArrayList에 php에서 받아온 item_content 추가
                    servIDs.add(servID); //servIDs ArrayList에 php에서 받아온 item_content 추가
                    CloseDts.add(CloseDt); //servIDs ArrayList에 php에서 받아온 item_content 추가

                    items.add(new FavoriteData(items_name.get(i),items_content.get(i), servIDs.get(i), CloseDts.get(i) ));

                    favoriteAdapter.addItem(items.get(i));
                    favoriteAdapter.notifyDataSetChanged();

                }

            } catch (JSONException e) {

                Log.d(TAG, "showResult_member : ", e);
            }

        }
    }


}