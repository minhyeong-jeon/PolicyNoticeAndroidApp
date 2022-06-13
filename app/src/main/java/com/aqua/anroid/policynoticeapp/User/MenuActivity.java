package com.aqua.anroid.policynoticeapp.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.aqua.anroid.policynoticeapp.Calendar.CalendarActivity;
import com.aqua.anroid.policynoticeapp.Chatbot_Main;
import com.aqua.anroid.policynoticeapp.Favorite.FavoriteActivity;
import com.aqua.anroid.policynoticeapp.API_Data.PublicActivity;
import com.aqua.anroid.policynoticeapp.R;


public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }
    //검색버튼 클릭 시 검색화면으로 이동
    public void menu_search(View view){
        Intent intent = new Intent(MenuActivity.this, PublicActivity.class);
        startActivity(intent);
    }

    //즐겨찾기버튼 클릭 시 즐겨찾기화면으로 이동
    public void menu_favorite(View view){
        Intent intent = new Intent(MenuActivity.this, FavoriteActivity.class);
        startActivity(intent);
    }

    //캘린더 클릭 시 캘린더화면으로 이동
    public void menu_calendar(View view){
        Intent intent = new Intent(MenuActivity.this, CalendarActivity.class);
        startActivity(intent);
    }

    //문의버튼 클릭 시 문의화면으로 이동
    public void menu_chatbot(View view){
        Intent intent = new Intent(MenuActivity.this, Chatbot_Main.class);
        startActivity(intent);
    }

    //설정버튼 클릭 시 설정화면으로 이동
    public void menu_setting(View view){
        Intent intent = new Intent(MenuActivity.this, SettingActivity.class);
        startActivity(intent);
    }
}
