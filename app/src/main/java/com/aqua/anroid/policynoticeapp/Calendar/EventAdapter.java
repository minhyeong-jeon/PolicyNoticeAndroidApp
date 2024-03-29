package com.aqua.anroid.policynoticeapp.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aqua.anroid.policynoticeapp.LocalIp;
import com.aqua.anroid.policynoticeapp.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/* 이벤트 유형의 배열 어댑터 확장 */
public class EventAdapter extends BaseAdapter{
    private static String TAG = "phptest";
    String IP_ADDRESS;

    ArrayList<Event> events= new ArrayList<Event>();
    Context context;
    Activity activity;

    public EventAdapter(Context context, Activity activity, ArrayList<Event> events)
    {
        this.context = context;
        this.activity = activity;
        this.events = events;
    }

    //뷰홀더 추가
    class ViewHolder {
        TextView eventTitleTV;
        TextView eventStartDateTV;
        TextView eventEndDateTV;
    }

    //Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return events.size();
    }

    //지정한 위치(i)에 있는 데이터 리턴턴
    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    //지정한 위치(i)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        final int po = position; // 리스트 위치
        IP_ADDRESS = ((LocalIp) activity.getApplication()).getIp();

        final Context context = parent.getContext();

        final ViewHolder holder;//아이템 내 view들을 저장할 holder 생성

        final Event event_item = events.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.event_cell, parent, false);

            holder = new ViewHolder();

            holder.eventTitleTV = (TextView) convertView.findViewById(R.id.eventTitleTV);
            holder.eventStartDateTV = (TextView) convertView.findViewById(R.id.eventStartDateTV);
            holder.eventEndDateTV = (TextView) convertView.findViewById(R.id.eventEndDateTV);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.eventTitleTV.setText(event_item.getTitle());
        holder.eventStartDateTV.setText(event_item.getStartdate());
        holder.eventEndDateTV.setText(event_item.getEnddate());

        ImageButton eventDeleteBtn = (ImageButton) convertView.findViewById(R.id.eventDeleteBtn);
        eventDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("delete_item",event_item.getID());
                DeleteEvent task = new DeleteEvent();
                task.execute(event_item.getID());
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder
                        .setMessage("이벤트 삭제")
                        .setCancelable(true)
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int arg1) {
                                        context.startActivity(new Intent(context, CalendarActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        ImageButton eventEditBtn = (ImageButton) convertView.findViewById(R.id.eventEditBtn);
        eventEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EventEditActivity.class);
                intent.putExtra("title", events.get(po).getTitle());
                intent.putExtra("startdate", events.get(po).getStartdate());
                intent.putExtra("enddate", events.get(po).getEnddate());
                intent.putExtra("alarmactive", events.get(po).getAlarmactive());

                //수정일때(버튼클릭시) 해당 리스트의 ID 보냄
                intent.putExtra(Event.Event_EDIT_EXTRA, events.get(po).getID());

                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class DeleteEvent extends AsyncTask<String, Void, String> {
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

            String serverURL = "http://"+IP_ADDRESS+"/event_delete.php";
            String postParameters = "ID=" + searchKeyword1;

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

                // Log.d(TAG, "DeleteData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

}