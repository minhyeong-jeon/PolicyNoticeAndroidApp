package com.aqua.anroid.policynoticeapp.User;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;


import com.aqua.anroid.policynoticeapp.Favorite.FavoriteActivity;
import com.aqua.anroid.policynoticeapp.Favorite.FavoriteAdapter;
import com.aqua.anroid.policynoticeapp.Favorite.FavoriteData;
import com.aqua.anroid.policynoticeapp.Parser.PublicDataDetail;
import com.aqua.anroid.policynoticeapp.Parser.PublicDataList;
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

public class ParsingAdapter extends BaseAdapter {
    private static String TAG = "phptest";

    String userID;
    private Context context;

    ArrayList<PublicDataList> publicDataLists = new ArrayList<PublicDataList>();

    private static String IP_ADDRESS = "10.0.2.2";
    private Activity activity;
    private OnItemClick listener;

    String servID;


    public ParsingAdapter(Context context, ArrayList<PublicDataList> publicDataLists, OnItemClick listener, Activity activity) {
        this.context = context;
        this.publicDataLists = publicDataLists;
        this.listener = listener;
        this.activity = activity;
    }

    //Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return publicDataLists.size();
    }

    //뷰홀더 추가
    class ViewHolder {
        TextView list_text_name;
        TextView list_text_content;

    }

    //i에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    @Override
    public View getView(int i, View view, ViewGroup parent) {
        //int pos = i;
        Context context = parent.getContext();
        final ViewHolder holder;//아이템 내 view들을 저장할 holder 생성
        userID = ((MemberActivity)MemberActivity.context).userID;

        final PublicDataList publicDataList_item = publicDataLists.get(i);

        Log.d(TAG, "items_adapter : " + publicDataLists.toString());


        //"item_list" Layout을 inflate하여 view 참조 획득
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //최초 생성 view인 경우, inflation -> ViewHolder 생성 -> 해당 View에 setTag 저장
            view = inflater.inflate(R.layout.parsing_list, parent, false);

            holder = new ViewHolder();

            //화면에 표시될 View(Layoutㅇ inflate된)으로부터 위젯에 대한 참조 획득
            holder.list_text_name = (TextView) view.findViewById(R.id.list_text_name);
            holder.list_text_content = (TextView) view.findViewById(R.id.list_text_content);

            //해당 view에 setTag로 Holder 객체 저장
            view.setTag(holder);
        } else {
            //view가 이미 생성된 적이 있다면, 저장되어 있는 Holder 가져오기
            holder = (ViewHolder) view.getTag();
        }

        holder.list_text_name.setText(publicDataList_item.getServNm());
        holder.list_text_content.setText(publicDataList_item.getServDgst());


        LinearLayout select_item = (LinearLayout) view.findViewById(R.id.select_item);

        select_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servID = publicDataList_item.getServID();
                Log.d("servID", servID);
                listener.onClick(servID);
            }
        });
        Button add_favorite = (Button) view.findViewById(R.id.add_favorite);
        add_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavoriteInsertData task = new FavoriteInsertData();
                task.execute("http://" + IP_ADDRESS + "/favorite.php", userID, holder.list_text_name.getText().toString(), holder.list_text_content.getText().toString());

            }
        });
        //해당 view 반납
        return view;
    }

    public interface OnItemClick {
        void onClick (String value);
    }


    //지정한 위치(i)에 있는 데이터 리턴턴
    @Override
    public Object getItem(int i) {
        return publicDataLists.get(i);
    }


    //지정한 위치(i)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴
    @Override
    public long getItemId(int i) {
        return i;
    }


    class FavoriteInsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //progressDialog = ProgressDialog.show(MainActivity.this,
            //"Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("즐찾결과",result);
            //Toast.makeText(context.getApplicationContext(), result, Toast.LENGTH_SHORT).show();

            Log.d(TAG, "POST response  - " + result);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            alertDialogBuilder
                    .setMessage(result)
                    .setCancelable(true)
                    .setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int arg1) {
                                    //context.startActivity(new Intent(context, FavoriteActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                }
                            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {

            String userID = (String)params[1];
            String item_name = (String)params[2];
            String item_content = (String)params[3];

            String serverURL = (String)params[0];
            String postParameters = "userID=" + userID + "& item_name=" + item_name + "& item_content=" + item_content;
            Log.d("즐찾디비",postParameters);


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }

    }
}