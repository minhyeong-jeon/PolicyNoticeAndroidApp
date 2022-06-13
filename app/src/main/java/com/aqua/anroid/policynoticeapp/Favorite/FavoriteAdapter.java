package com.aqua.anroid.policynoticeapp.Favorite;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;


import com.aqua.anroid.policynoticeapp.Calendar.Event;
import com.aqua.anroid.policynoticeapp.Calendar.EventEditActivity;
import com.aqua.anroid.policynoticeapp.Public_Parser.PublicDataList;
import com.aqua.anroid.policynoticeapp.R;
import com.aqua.anroid.policynoticeapp.Worknet_Parser.WorkDataList;
import com.aqua.anroid.policynoticeapp.Worknet_Parser.WorkDataParser;
import com.aqua.anroid.policynoticeapp.Worknet_Parser.WorkWantedList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FavoriteAdapter extends BaseAdapter {
    private static String TAG = "phptest";

    private ArrayList<PublicDataList> publicDataLists = new ArrayList<PublicDataList>(); //목록조회 데이터
    ArrayList<FavoriteData> favoriteData= new ArrayList<FavoriteData>();


    private static String IP_ADDRESS = "192.168.35.237";
    private Activity activity;
    String servID, userID, CloseDate, eventTitle;
    private OnItemClick listener;

    public FavoriteAdapter(Activity activity, OnItemClick listener) {
        this.activity = activity;
        this.listener = listener;

    }

    //Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return favoriteData.size();
    }

    public interface OnItemClick {
        void onClick (String value);
    }

    //뷰홀더 추가
    class ViewHolder {
        TextView textview_list_name;
        TextView textview_list_content;
    }

    //i에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    @Override
    public View getView(int i, View view, ViewGroup parent) {
        Context context = parent.getContext();
        final ViewHolder holder;//아이템 내 view들을 저장할 holder 생성

        final FavoriteData favoriteData_item = favoriteData.get(i);

        Log.d(TAG, "items_adapter : " + favoriteData.toString());
        userID = ((FavoriteActivity)FavoriteActivity.mcontext).userID;


        //"item_list" Layout을 inflate하여 view 참조 획득
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //최초 생성 view인 경우, inflation -> ViewHolder 생성 -> 해당 View에 setTag 저장
            view = inflater.inflate(R.layout.favorite_item_list, parent, false);

            holder = new ViewHolder();

            //화면에 표시될 View(Layoutㅇ inflate된)으로부터 위젯에 대한 참조 획득
            holder.textview_list_name = (TextView) view.findViewById(R.id.textView_list_name);
            holder.textview_list_content = (TextView) view.findViewById(R.id.textView_list_content);

            //해당 view에 setTag로 Holder 객체 저장
            view.setTag(holder);
        } else {
            //view가 이미 생성된 적이 있다면, 저장되어 있는 Holder 가져오기
            holder = (ViewHolder) view.getTag();
        }
        holder.textview_list_name.setText(favoriteData_item.getItem_name());
        holder.textview_list_content.setText(favoriteData_item.getItem_content());

        ImageView add_calender_button = view.findViewById(R.id.add_calender_button);
        if(favoriteData_item.getServID().charAt(0)=='K'){
            add_calender_button.setVisibility(View.VISIBLE);
        }
        else{
            add_calender_button.setVisibility(View.INVISIBLE);

        }

        LinearLayout select_favorite_item = (LinearLayout) view.findViewById(R.id.select_favorite_item);
        select_favorite_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servID = favoriteData_item.getServID();
                Log.d("servID", servID);
                listener.onClick(servID);
            }
        });


        //캘린더 추가 아이콘 클릭 시 해당 아이템의 이름과 마감일자가 EventEditActivity로 넘어감
        add_calender_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventTitle = favoriteData_item.getItem_name();

                CloseDate = favoriteData_item.getCloseDt();

                //CloseDate에서 문자제거 후 숫자만 남김
                CloseDate = CloseDate.replaceAll("[^0-9]", "");

                Intent intent = new Intent(context, EventEditActivity.class);
                intent.putExtra("title",eventTitle);
                intent.putExtra("CloseDate",CloseDate);
                context.startActivity(intent);

            }
        });

        //삭제 버튼 클릭 시
        ImageView deletebutton = (ImageView) view.findViewById(R.id.deletebutton);
        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteData task = new DeleteData(); // DB에서 삭제하는 함수 호출
                task.execute(userID, holder.textview_list_name.getText().toString());
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder
                        .setMessage( " 삭제 완료")
                        .setCancelable(true)
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    //AlertDialog 출력 후 즐겨찾기 목록 화면으로 이동
                                    public void onClick(DialogInterface dialog, int arg1) {
                                        context.startActivity(new Intent(context, FavoriteActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        });
        //해당 view 반납
        return view;
    }

    //지정한 위치(i)에 있는 데이터 리턴턴
    @Override
    public Object getItem(int i) {
        return favoriteData.get(i);
    }



    //지정한 위치(i)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴
    @Override
    public long getItemId(int i) {
        return i;
    }


    // 아이템 데이터 추가를 위한 함수.
    public void addItem(FavoriteData items) {
        favoriteData.add(items);
    }

    // DB와 연결하여 즐겨찾기 삭제 실행
    class DeleteData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }


        @Override
        protected String doInBackground(String... params) {


            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];

            String serverURL = "http://192.168.35.237/favorite_delete.php";
            String postParameters = "userID=" + searchKeyword1 + "& item_name=" + searchKeyword2;

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

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                return new String("Error: " + e.getMessage());
            }

        }
    }
}