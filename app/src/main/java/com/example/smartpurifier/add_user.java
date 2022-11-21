package com.example.smartpurifier;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class add_user extends AppCompatActivity {
    EditText join_name;
    EditText join_engname;
    EditText join_phone;
    EditText join_birth;
    EditText join_sex;

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.top_nav_adduser,menu);
        return true;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user_page);
        init();
        //SettingListener();
        getSupportActionBar().setTitle("사용자 등록");
    }
    private void init(){
        join_name=findViewById(R.id.join_name);
        join_engname=findViewById(R.id.join_engname);
        join_phone=findViewById(R.id.join_phone);
        join_birth=findViewById(R.id.join_birth);
        join_sex=findViewById(R.id.join_sex);

    }
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case R.id.save:
                String input_name = join_name.getText().toString();
                String input_engname = join_engname.getText().toString();
                String input_phone = join_phone.getText().toString();
                String input_birth = join_birth.getText().toString();
                String input_sex = join_sex.getText().toString();

                if(input_name.length()>0&&input_engname.length()>0&&input_phone.length()>0&&input_birth.length()>0&&input_sex.length()>0){
                    Intent intent = new Intent(add_user.this, add_medicine.class);

                    intent.putExtra("name",input_name);
                    intent.putExtra("engname",input_engname);
                    intent.putExtra("phone",input_phone);
                    intent.putExtra("birth",input_birth);
                    intent.putExtra("sex",input_sex);

                    startActivity(intent);
                    return true;
                }
                else {
                    Toast.makeText(getApplicationContext(), "모든 항목에 기입해주세요!", Toast.LENGTH_SHORT).show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
