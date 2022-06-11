package com.aqua.anroid.policynoticeapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.aqua.anroid.policynoticeapp.User.MemberUpdateActivity;

public class Chatbot_Faq extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbot_faq);

        Button faq1 = (Button) findViewById(R.id.Faq1);
        Button faq2 = (Button) findViewById(R.id.Faq2);
        Button faq3 = (Button) findViewById(R.id.Faq3);
        Button faq4 = (Button) findViewById(R.id.Faq4);

        ImageView backBtn = findViewById(R.id.backbtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chatbot_Faq.this, Chatbot_Main.class);
                startActivity(intent);

            }
        });


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        faq1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogBuilder.setTitle("Q 어떻게 이용하나요?");
                alertDialogBuilder
                        .setMessage("A 복지서비스와 일자리서비스 중 원하는 서비스를 선택하여 조건을 입력하면 해당 조건에 맞는 정보를 검색할 수 있어요! ")
                        .setCancelable(true)
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int arg1) {
                                        // Handle Positive Button
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        faq2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogBuilder.setTitle("Q 회원가입이 필요한가요?");
                alertDialogBuilder
                        .setMessage("A 회원가입은 선택사항 입니다. \n 단, 비회원으로 접속 시 \n 부가기능을 사용하실 수 없고 \n 검색 기능만 가능합니다. ")
                        .setCancelable(true)
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int arg1) {
                                        // Handle Positive Button
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        faq3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogBuilder.setTitle("Q 프로필 정보 변경이 가능한가요?");
                alertDialogBuilder
                        .setMessage("A 회원정보수정 페이지에서 변경 가능합니다. ")
                        .setCancelable(true)
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int arg1) {
                                        // Handle Positive Button
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        faq4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogBuilder.setTitle("Q 즐겨찾기를 하면 좋은점이 뭔가요?");
                alertDialogBuilder
                        .setMessage("A 즐겨찾기를 하시면 해당 일자리정책 마감일자에 알람을 받을 수 있습니다. ")
                        .setCancelable(true)
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int arg1) {
                                        // Handle Positive Button
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });


    }
}