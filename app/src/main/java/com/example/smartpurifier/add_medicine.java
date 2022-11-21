package com.example.smartpurifier;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class add_medicine extends AppCompatActivity {

    String name;
    String engname;
    String phoneNum;
    String age;
    String sex;
    EditText join_med;
    CheckBox checkBox1;
    CheckBox checkBox2;
    CheckBox checkBox3;
    CheckBox checkBox4;
    CheckBox checkBox5;
    CheckBox checkBox6;
    CheckBox checkBox7;
    CheckBox checkBox8;


    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.top_nav_addmed,menu);
        return true;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_medicine_page);
        init();
        getSupportActionBar().setTitle("약물 등록");

        Intent intent = getIntent();
        if(intent.hasExtra("name") && (intent.hasExtra("engname")) && (intent.hasExtra("phone")) && (intent.hasExtra("birth")) && (intent.hasExtra("sex"))){
            name = intent.getStringExtra("name");
            engname = intent.getStringExtra("engname");
            phoneNum = intent.getStringExtra("phone");
            age = intent.getStringExtra("birth");
            sex = intent.getStringExtra("sex");
        }
    }
    private void init(){
        join_med=findViewById(R.id.join_med);
        checkBox1 = (CheckBox) findViewById(R.id.checkBox1) ;
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2) ;
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3) ;
        checkBox4 = (CheckBox) findViewById(R.id.checkBox4) ;
        checkBox5 = (CheckBox) findViewById(R.id.checkBox5) ;
        checkBox6 = (CheckBox) findViewById(R.id.checkBox6) ;
        checkBox7 = (CheckBox) findViewById(R.id.checkBox7) ;
        checkBox8 = (CheckBox) findViewById(R.id.checkBox8) ;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.complete:

                String input_med = join_med.getText().toString();
                int resId;
                if(checkBox2.isChecked()){
                    resId=R.drawable.med3;
                } else if(checkBox3.isChecked()){
                    resId=R.drawable.med4;
                } else if(checkBox4.isChecked()){
                    resId=R.drawable.med5;
                } else if(checkBox5.isChecked()){
                    resId=R.drawable.med6;
                } else if(checkBox6.isChecked()){
                    resId=R.drawable.med7;
                } else if(checkBox7.isChecked()){
                    resId=R.drawable.med8;
                } else if(checkBox8.isChecked()){
                    resId=R.drawable.med9;
                } else{
                    resId=R.drawable.med1;
                }
                if(input_med.length()>0){
                    Intent intent = new Intent(add_medicine.this, MainActivity.class);
                    intent.putExtra("med",input_med);
                    intent.putExtra("name",name);
                    intent.putExtra("engname",engname);
                    intent.putExtra("resId",resId);
                    intent.putExtra("phone",phoneNum);
                    intent.putExtra("birth",age);
                    intent.putExtra("sex",sex);

                    Toast.makeText(getApplicationContext(), "사용자 추가가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    //메인으로 이동
                    startActivity(intent);
                    return true;
                }
                else {
                    Toast.makeText(getApplicationContext(), "약물 이름을 기입해주세요!", Toast.LENGTH_SHORT).show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}