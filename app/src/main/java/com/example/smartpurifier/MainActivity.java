package com.example.smartpurifier;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int SHOW_PREFERENCES =1;
    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;

    FloatingActionButton fb_add_btn;
    FloatingActionButton fb_med_btn;
    FloatingActionButton fb_user_btn;

    Button button;
    ListView listView;
    ArrayList<SingerDTO> list;
    SingerAdapter adapter;
    private boolean clicked = false;

    String name ;
    String phoneNum_;
    String age_;
    String sex;
    String med;
    int resId;
    String engname;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.top_nav_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.bluethooth:
                Intent intent_ble = new Intent(getApplicationContext(), Ble_Activity.class);
                startActivity(intent_ble);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public static Context context_main; // context 변수 선언
    public int var; // 다른 Activity에서 접근할 변수

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        //디바이스 사이즈 구하기
        Point size = getDeviceSize();
        //객체 초기화
        button = findViewById(R.id.button);
        listView = findViewById(R.id.listView);

        list = new ArrayList<>();

        //어댑터의 list에 데이터 추가
        adapter = new SingerAdapter(MainActivity.this, list, size);
        adapter.addDTO(new SingerDTO("박정철", "010-3850-6250", 25,"남자", R.drawable.med1,"감기약"));
        adapter.addDTO(new SingerDTO("이주호", "010-2112-8406", 26,"남자", R.drawable.med4,"감기약"));
        adapter.addDTO(new SingerDTO("정준호", "010-5587-7520", 25,"남자", R.drawable.med5,"타이레놀"));
        adapter.addDTO(new SingerDTO("조성진", "010-4097-1755", 26,"남자", R.drawable.med9,"타이레놀"));

        Intent intent = getIntent();
        if(intent.hasExtra("name") && (intent.hasExtra("phone")) && (intent.hasExtra("birth")) && (intent.hasExtra("sex"))&& (intent.hasExtra("med"))&& (intent.hasExtra("resId"))&& (intent.hasExtra("engname"))){
            name = intent.getStringExtra("name");
            phoneNum_ = intent.getStringExtra("phone");
            String phoneNum = phoneNum_.substring(0,3)+"-"+phoneNum_.substring(3,7)+"-"+phoneNum_.substring(7,11);
            age_ = intent.getStringExtra("birth");
            int age =2022-Integer.parseInt(age_.substring(0,4))+1;
            sex = intent.getStringExtra("sex");
            med = intent.getStringExtra("med");
            resId  = intent.getIntExtra("resId", R.drawable.med1);
            engname = intent.getStringExtra("engname");

            SingerDTO dto = new SingerDTO(name, phoneNum, age, sex, resId, med);
            adapter.addDTO(dto);
        }
        //리스트뷰에 어댑터를 붙여준다
        listView.setAdapter(adapter);

        //리스트뷰의 아이템 클릭했을때 이벤트 추가
        //AdapterView<?> parent : 클릭이 발생한 어댑터뷰
        //View view : 어댑터뷰 내부의, 클릭이 된 바로 그 뷰
        //int position : 어댑터 내부의 그 뷰의 위치(position)
        //long id : 클릭된 아이템의 row id
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SingerDTO dto = (SingerDTO) adapter.getItem(position);
                Toast.makeText(MainActivity.this, position+1+"번" + "\n이름 : " + dto.getName()
                        + "\n전화번호 : " + dto.getPhoneNum() +"\n나이 : " + dto.getAge(), Toast.LENGTH_SHORT).show();
            }
        });

        //편집 화면
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final ArrayList<String> selectedItems = new ArrayList<String>();
                String[] items = getResources().getStringArray(R.array.list);

                Intent intent = getIntent();
                if(intent.hasExtra("name")){
                    String name = intent.getStringExtra("name");
                    List<String> newitems = new ArrayList<>(Arrays.asList(items));
                    newitems.add(name);
                    items = new String[ newitems.size() ];
                    newitems.toArray(items);
                }

                builder.setTitle("편집 할 대상을 선택해주세요.\n(최대 2명 선택 가능)");
                String[] finalItems = items;
                builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int pos, boolean isChecked)
                    {
                        if(isChecked == true)
                        {
                            selectedItems.add(finalItems[pos]);
                        }
                        else
                        {
                            selectedItems.remove(pos);
                        }
                    }
                });

                builder.setPositiveButton("닫기", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int pos) {}});

                builder.setNegativeButton("삭제", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        //삭제 이벤트
                        //adapter.delDTO(2);
                        Toast.makeText(getApplicationContext(), "삭제 구현 해야함..." ,Toast.LENGTH_LONG).show();
                    }
                });

                builder.setNeutralButton("전송", new DialogInterface.OnClickListener(){
                    String SeletedItemsString = "";
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        //전송이벤트
                        for(int i =0; i<selectedItems.size();i++)
                        {
                            SeletedItemsString =  SeletedItemsString + " " + selectedItems.get(i);
                        }
                        boolean success=false;
                        Intent Intent = new Intent(MainActivity.this, Ble_Activity.class);
                        if(SeletedItemsString.contains("박정철")){
                            Intent.putExtra("name1","JeongCheol");
                            Intent.putExtra("med1","Tylenol");
                            success=true;
                        }
                        if(SeletedItemsString.contains("이주호")){
                            Intent.putExtra("name2","JuHo");
                            Intent.putExtra("med2","Allestin");
                            success=true;
                        }
                        if(SeletedItemsString.contains("정준호")){
                            Intent.putExtra("name3","JunHo");
                            Intent.putExtra("med3","Allestin");
                            success=true;
                        }
                        if(SeletedItemsString.contains("조성진")){
                            Intent.putExtra("name4","SeongJin");
                            Intent.putExtra("med4","Tylenol");
                            success=true;
                        }
                        Intent intent = getIntent();
                        if(intent.hasExtra("name") &&intent.hasExtra("engname") && intent.hasExtra("med")){
                            if(SeletedItemsString.contains(name)){
                                Intent.putExtra("name5",name);
                                Intent.putExtra("engname5",engname);
                                Intent.putExtra("phoneNum5",phoneNum_);
                                Intent.putExtra("age5",age_);
                                Intent.putExtra("sex5",sex);
                                Intent.putExtra("resId5",resId);
                                Intent.putExtra("med5",med);
                                success=true;
                            }
                        }
                        if(success){
                            startActivity(Intent);
                            Toast.makeText(getApplicationContext(), "전송할 항목은 :" + SeletedItemsString,Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "전송할 항목이 없습니다." ,Toast.LENGTH_LONG).show();
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationview);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.calendar:
                        Intent intent2 = new Intent(getApplicationContext(), Calender_Memo.class);
                        startActivity(intent2);
                        return true;
                    case R.id.setting:
                        Intent intent3 = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(intent3);
                        return true;
                }
                return false;
            }
        });

        fb_add_btn = findViewById(R.id.fb_add_btn);
        fb_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddButtonClicked();
            }
        });

        fb_med_btn = findViewById(R.id.fb_med_btn);
        fb_med_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), alram_activity.class);
                startActivity(intent);
            }
        });

        fb_user_btn = findViewById(R.id.fb_user_btn);
        fb_user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), add_user.class);
                startActivity(intent);
            }
        });
    }

    private void onAddButtonClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;
    }

    private void setVisibility(boolean clicked) {
        if(!clicked){ //보여진다
            fb_med_btn.setVisibility(fb_add_btn.VISIBLE);
            fb_user_btn.setVisibility(fb_user_btn.VISIBLE);
        }else{ //숨긴다
            fb_med_btn.setVisibility(fb_add_btn.INVISIBLE);
            fb_user_btn.setVisibility(fb_user_btn.INVISIBLE);
        }
    }

    private void setAnimation(boolean clicked) {
        if(!clicked){
            fb_med_btn.startAnimation(fromBottom);
            fb_user_btn.startAnimation(fromBottom);
            fb_add_btn.startAnimation(rotateOpen);
        }else{
            fb_med_btn.startAnimation(toBottom);
            fb_user_btn.startAnimation(toBottom);
            fb_add_btn.startAnimation(rotateClose);
        }
    }
    public Point getDeviceSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        return size;
    }
}