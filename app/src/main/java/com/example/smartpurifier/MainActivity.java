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
    public static Context context_main; // context ?????? ??????
    public int var; // ?????? Activity?????? ????????? ??????

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        //???????????? ????????? ?????????
        Point size = getDeviceSize();
        //?????? ?????????
        button = findViewById(R.id.button);
        listView = findViewById(R.id.listView);

        list = new ArrayList<>();

        //???????????? list??? ????????? ??????
        adapter = new SingerAdapter(MainActivity.this, list, size);
        adapter.addDTO(new SingerDTO("?????????", "010-3850-6250", 25,"??????", R.drawable.med1,"?????????"));
        adapter.addDTO(new SingerDTO("?????????", "010-2112-8406", 26,"??????", R.drawable.med4,"?????????"));
        adapter.addDTO(new SingerDTO("?????????", "010-5587-7520", 25,"??????", R.drawable.med5,"????????????"));
        adapter.addDTO(new SingerDTO("?????????", "010-4097-1755", 26,"??????", R.drawable.med9,"????????????"));

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
        //??????????????? ???????????? ????????????
        listView.setAdapter(adapter);

        //??????????????? ????????? ??????????????? ????????? ??????
        //AdapterView<?> parent : ????????? ????????? ????????????
        //View view : ???????????? ?????????, ????????? ??? ?????? ??? ???
        //int position : ????????? ????????? ??? ?????? ??????(position)
        //long id : ????????? ???????????? row id
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SingerDTO dto = (SingerDTO) adapter.getItem(position);
                Toast.makeText(MainActivity.this, position+1+"???" + "\n?????? : " + dto.getName()
                        + "\n???????????? : " + dto.getPhoneNum() +"\n?????? : " + dto.getAge(), Toast.LENGTH_SHORT).show();
            }
        });

        //?????? ??????
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

                builder.setTitle("?????? ??? ????????? ??????????????????.\n(?????? 2??? ?????? ??????)");
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

                builder.setPositiveButton("??????", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int pos) {}});

                builder.setNegativeButton("??????", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        //?????? ?????????
                        //adapter.delDTO(2);
                        Toast.makeText(getApplicationContext(), "?????? ?????? ?????????..." ,Toast.LENGTH_LONG).show();
                    }
                });

                builder.setNeutralButton("??????", new DialogInterface.OnClickListener(){
                    String SeletedItemsString = "";
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        //???????????????
                        for(int i =0; i<selectedItems.size();i++)
                        {
                            SeletedItemsString =  SeletedItemsString + " " + selectedItems.get(i);
                        }
                        boolean success=false;
                        Intent Intent = new Intent(MainActivity.this, Ble_Activity.class);
                        if(SeletedItemsString.contains("?????????")){
                            Intent.putExtra("name1","JeongCheol");
                            Intent.putExtra("med1","Tylenol");
                            success=true;
                        }
                        if(SeletedItemsString.contains("?????????")){
                            Intent.putExtra("name2","JuHo");
                            Intent.putExtra("med2","Allestin");
                            success=true;
                        }
                        if(SeletedItemsString.contains("?????????")){
                            Intent.putExtra("name3","JunHo");
                            Intent.putExtra("med3","Allestin");
                            success=true;
                        }
                        if(SeletedItemsString.contains("?????????")){
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
                            Toast.makeText(getApplicationContext(), "????????? ????????? :" + SeletedItemsString,Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "????????? ????????? ????????????." ,Toast.LENGTH_LONG).show();
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
        if(!clicked){ //????????????
            fb_med_btn.setVisibility(fb_add_btn.VISIBLE);
            fb_user_btn.setVisibility(fb_user_btn.VISIBLE);
        }else{ //?????????
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