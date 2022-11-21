package com.example.smartpurifier;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SingerAdapter extends BaseAdapter {
    //어댑터에 데이터를 받기위해 생성자 만든다.
    // 컨텍스트와 리스트는 받아오지만 인플레이터는 안받아온다.
    Context context;
    ArrayList<SingerDTO> list;
    Point size;
    LayoutInflater inflater;
    AlertDialog dialog;

    public SingerAdapter(Context context, ArrayList<SingerDTO> list, Point size) {
        this.context = context;
        this.list = list;
        this.size = size;

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //인플레이터는 시스템에서 가져온다.
    }

    //리스트(list)에 항목을 추가해 줄 메서드 작성
    public void addDTO(SingerDTO dto) {
        list.add(dto);
    }

    //리스트의 항목을 삭제할 메서드 작성
    public void delDTO(int position) {
        list.remove(position);
    }

    //리스트의 항목을 모두 삭제할 메서드 작성
    public void removeDTOs() {
        list.clear();
    }

    //getCount() : 리스트에서 항목을 몇개나 가져와서 몇개의 화면을 만들 것인지 정하는 메서드
    @Override
    public int getCount() {
        return list.size();
    }

    //getItem() : 리스트에서 해당하는 인덱스의 데이터(사진, 이름, 전번)를 모두 가져오는 메서드
    //Object를 알아서 캐스팅해서 사용하라는 의미로 반환 타입이 Object
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SingerViewHolder viewHolder;

        //화면 구성
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.singerview, parent, false);
            viewHolder = new SingerViewHolder();
            viewHolder.tvName = convertView.findViewById(R.id.tvName);
            viewHolder.tvPhoneNum = convertView.findViewById(R.id.tvPhoneNum);
            viewHolder.tvAge_Sex = convertView.findViewById(R.id.tvAge_Sex);
            viewHolder.imageView = convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SingerViewHolder) convertView.getTag();
        }

        //DTO에서 데이터를 찾음
        SingerDTO dto = list.get(position);
        String name = dto.getName();
        String phoneNum = dto.getPhoneNum();
        int age = dto.getAge();
        String sex = dto.getSex();
        int resId = dto.getResId();

        //XML의 화면에 찾은 데이터 표시
        viewHolder.tvName.setText(name);
        //viewHolder.tvName.setText(dto.getName()); // 이렇게 써도 같음
        viewHolder.tvPhoneNum.setText(phoneNum);
        viewHolder.tvAge_Sex.setText(age+"살"+"/"+sex);
        viewHolder.imageView.setImageResource(resId);

        //이미지만 클릭했을때 기능 추가
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "선택 : " + (position+1) +"번 사용자"
                        + "\n이름 : " + list.get(position).getName(), Toast.LENGTH_SHORT).show();

                //미리 만들어둔 XML과 팝업창을 연결해서 보여줌
                popupImgXml(list.get(position).getResId(), list.get(position).getName(), list.get(position).getMed());
            }
        });

        return convertView;
    }

    //따로 새 자바 파일을 만들지 않고 XML의 내용을 볼 수 있게끔 만든 클래스
    public class SingerViewHolder {
        public ImageView imageView;
        public TextView tvName, tvPhoneNum, tvAge_Sex;
    }

    //XML 불러오기
    public void popupImgXml(int resId, String name, String med) {
        //일단 res에 popupimg.xml 만든다
        //그 다음 화면을 inflate 시켜 setView 한다

        //팝업창에 xml 붙이기///////////////
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popupimg, null);
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textView = view.findViewById(R.id.textView);

        imageView.setImageResource(resId);
        textView.setTextSize(20);
        textView.setText("이름 : " +name + "\n");
        textView.append("복용 약품 : "+med);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(name +"님의 복약 정보")
                .setView(view);

        builder.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        dialog = builder.create();
        dialog.show();

        //디바이스 사이즈를 받아 팝업 크기창을 조절한다.
        int sizeX = size.x;
        int sizeY = size.y;

        //AlertDialog에서 위치 크기 수정
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

        params.x = (int) Math.round(sizeX * 0.005); // X위치
        params.y = (int) Math.round(sizeY * 0.01); // Y위치
        params.width = (int) Math.round(sizeX * 0.9);
        params.height = (int) Math.round(sizeY * 0.8);
        dialog.getWindow().setAttributes(params);
    }
}