package com.aqua.anroid.policynoticeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.aqua.anroid.policynoticeapp.Calendar.CalendarActivity;
import com.aqua.anroid.policynoticeapp.Favorite.FavoriteActivity;
import com.aqua.anroid.policynoticeapp.User.MemberActivity;


public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    public void menu_search(View view){
        Intent intent = new Intent(MenuActivity.this, MemberActivity.class);
        startActivity(intent);
    }

    public void menu_favorite(View view){
        Intent intent = new Intent(MenuActivity.this, FavoriteActivity.class);
        startActivity(intent);
    }

    public void menu_calendar(View view){
        Intent intent = new Intent(MenuActivity.this, CalendarActivity.class);
        startActivity(intent);
    }

    public void menu_chatbot(View view){
        Intent intent = new Intent(MenuActivity.this, Chatbot_Main.class);
        startActivity(intent);
    }

    public void menu_setting(View view){
        Intent intent = new Intent(MenuActivity.this, SettingActivity.class);
        startActivity(intent);
    }
}
