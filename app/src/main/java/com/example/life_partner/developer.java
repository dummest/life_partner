package com.example.life_partner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class developer extends AppCompatActivity {

    Button btn1;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.developer);
        btn1 = (Button) findViewById(R.id.btn1);
        email="gkahsl13@naver.com";
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mail_intent = new Intent(Intent.ACTION_SEND);
                mail_intent .setType("*/*");

                mail_intent.putExtra(Intent.EXTRA_EMAIL, "gkahsl13@naver.com"); // 받는 사람 이메일
                mail_intent.putExtra(Intent.EXTRA_SUBJECT, "문의하기"); // 메일 제목
                mail_intent.putExtra(Intent.EXTRA_TEXT, "Email Text"); // 메일 내용
                startActivity(mail_intent);

            }
        });
    }
}