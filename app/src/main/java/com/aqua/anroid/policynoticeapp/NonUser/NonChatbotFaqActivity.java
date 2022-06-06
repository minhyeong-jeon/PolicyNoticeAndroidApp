package com.aqua.anroid.policynoticeapp.NonUser;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.aqua.anroid.policynoticeapp.R;

public class NonChatbotFaqActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.non_chatbot_faq);

        Button faq1 = (Button) findViewById(R.id.Faq1);
        Button faq2 = (Button) findViewById(R.id.Faq2);
        Button faq3 = (Button) findViewById(R.id.Faq3);
        Button faq4 = (Button) findViewById(R.id.Faq4);

        ImageView backBtn = findViewById(R.id.backbtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NonChatbotFaqActivity.this, NonChatbotMainActivity.class);
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
                        .setMessage("A 제목 / 내용 / 제목+내용을 선택하고 \n 생애주기, 가구유형, 관심주제를 설정하여 \n 원하는 정보를 검색할 수 있어요! ")
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
                        .setMessage("A 프로필정보 변경은 가능합니다! ")
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
                alertDialogBuilder.setTitle("Q 한개 더 뭐 없나,,?");
                alertDialogBuilder
                        .setMessage("A 흐으으으으음 ")
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