package com.aqua.anroid.policynoticeapp.API_Data;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aqua.anroid.policynoticeapp.LocalIp;
import com.aqua.anroid.policynoticeapp.R;
import com.aqua.anroid.policynoticeapp.Worknet_Parser.WorkDataList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class WorkParsingAdapter extends BaseAdapter {
    private static String TAG = "phptest";
    private Context context;

    ArrayList<WorkDataList> workDataLists = new ArrayList<WorkDataList>();

    String IP_ADDRESS;
    private Activity activity;
    private OnItemClick listener;

    String AuthNo; //선택한 정책의 서비스 아이디 저장 변수
    String userID; //로그인 한 유저의 아이디 저장 변수

    // WorkParsingAdapter 생성자
    public WorkParsingAdapter(Context context, ArrayList<WorkDataList> workDataLists, OnItemClick listener, Activity activity) {
        this.context = context;
        this.workDataLists = workDataLists;
        this.listener = listener;
        this.activity = activity;
    }

    //Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return workDataLists.size();
    }

    //뷰홀더 추가
    class ViewHolder {
        TextView list_text_company;
        TextView list_text_title;
        TextView list_text_salary;
        TextView list_text_region;
        TextView list_text_date;
    }

    //i에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    @Override
    public View getView(int i, View view, ViewGroup parent) {
        IP_ADDRESS = ((LocalIp) activity.getApplication()).getIp();

        Context context = parent.getContext();
        final ViewHolder holder;//아이템 내 view들을 저장할 holder 생성
        userID = ((WorkActivity)WorkActivity.work_context).userID; //WorkActivity의 userID값을 가져옴

        //workDataLists 아이템들을 workDataList_item에 대입
        final WorkDataList workDataList_item = workDataLists.get(i);


        //"item_list" Layout을 inflate하여 view 참조 획득
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //최초 생성 view인 경우, inflation -> ViewHolder 생성 -> 해당 View에 setTag 저장
            view = inflater.inflate(R.layout.work_parsing_list, parent, false);

            holder = new ViewHolder();

            //화면에 표시될 View(Layoutㅇ inflate된)으로부터 위젯에 대한 참조 획득
            holder.list_text_company = (TextView) view.findViewById(R.id.list_text_company);
            holder.list_text_title = (TextView) view.findViewById(R.id.list_text_title);
            holder.list_text_salary = (TextView) view.findViewById(R.id.list_text_salary);
            holder.list_text_region = (TextView) view.findViewById(R.id.list_text_region);
            holder.list_text_date = (TextView) view.findViewById(R.id.list_text_date);

            //해당 view에 setTag로 Holder 객체 저장
            view.setTag(holder);
        } else {
            //view가 이미 생성된 적이 있다면, 저장되어 있는 Holder 가져오기
            holder = (ViewHolder) view.getTag();
        }

        holder.list_text_company.setText(workDataList_item.getCompany());   //list_text_company에 회사명을 set함
        holder.list_text_title.setText(workDataList_item.getTitle());       //list_text_title에 채용제목을 set함
        holder.list_text_salary.setText(workDataList_item.getSalTpNm());    //list_text_salary에 임금유형을 set함
        holder.list_text_region.setText(workDataList_item.getRegion());     //list_text_region에 근무지역을 set함
        holder.list_text_date.setText(workDataList_item.getCloseDt());      //list_text_date에 마감일자를 set함


        //각 정책(리니어레이아웃) 클릭 시 해당 서비스 아이디를 가져와서 onClick 함수 호출
        LinearLayout select_work_item = (LinearLayout) view.findViewById(R.id.select_work_item);
        select_work_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthNo = workDataList_item.getWantedAuthNo();
                Log.d("AuthNo", AuthNo);
                listener.onClick(AuthNo);
            }
        });

        //즐겨찾기 버튼 클릭 시 favorite 테이블에 해당 정책 저장
        Button add_work_favorite = (Button) view.findViewById(R.id.add_work_favorite);
        add_work_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavoriteInsertData task = new FavoriteInsertData();
                task.execute("http://" + IP_ADDRESS + "/favorite.php", userID, holder.list_text_company.getText().toString(), holder.list_text_title.getText().toString(), workDataList_item.getWantedAuthNo(),workDataList_item.getCloseDt());

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
        return workDataLists.get(i);
    }


    //지정한 위치(i)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴
    @Override
    public long getItemId(int i) {
        return i;
    }

    //DB에 Insert하기위해 서버와 연결
    class FavoriteInsertData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "POST response  - " + result);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            alertDialogBuilder
                    .setMessage(result)
                    .setCancelable(true)
                    .setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int arg1) {
                                }
                            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {

            //인자로 받아온 값들을 php에 전달
            //POST 방식 HTTP 통신의 아규먼트로 하여 서버에 있는 PHP파일 실행
            String userID = (String)params[1];
            String item_name = (String)params[2];
            String item_content = (String)params[3];
            String servID = (String)params[4];
            String CloseDt = (String)params[5];


            String serverURL = (String)params[0];
            String postParameters = "userID=" + userID + "& item_name=" + item_name + "& item_content=" + item_content + "& servID=" + servID + "& CloseDt=" + CloseDt;


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