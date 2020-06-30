package com.example.android.camera2basic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Random;

public class PageFragment2 extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    int pageNumber;
    int backColor,backColor1;
    String name, inn, probability,ud_numb, id_p;
    ImageView imV;
    Bitmap bitmap;
    Button button;
    static PageFragment2 newInstance1(int page,String name,String inn,String probability,Bitmap bitmap1, String ud_numb, String id) {
        PageFragment2 pageFragment = new PageFragment2();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        arguments.putString("Name",name);
        arguments.putString("IIN",inn);
        arguments.putString("Probability", probability);
        arguments.putParcelable("Bitmap",bitmap1);
        arguments.putString("ud_numb",ud_numb);
        arguments.putString("id",id);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        name=getArguments().getString("Name");
        inn=getArguments().getString("IIN");
        probability=getArguments().getString("Probability");
        bitmap =getArguments().getParcelable("Bitmap");
        ud_numb=getArguments().getString("ud_numb");
        id_p=getArguments().getString("id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, null);
        TextView tvPage = (TextView) view.findViewById(R.id.tvPage1);
        TextView tvpage1 = (TextView) view.findViewById(R.id.textView);
        TextView tvpage2 = (TextView) view.findViewById(R.id.textView2);
        TextView tvpage3 = (TextView) view.findViewById(R.id.textView3);
        TextView tvpage4 = (TextView) view.findViewById(R.id.textView7);
        imV=view.findViewById(R.id.imageV);
        imV.setImageBitmap(bitmap);

        button =view.findViewById(R.id.button);
        tvPage.setText("Лицо №" + (pageNumber+1));
        tvpage1.setText("  ФИО: "+name);
        tvpage2.setText("  ИИН: "+id_p);
        tvpage3.setText("  № удостоверения:" +ud_numb);
        tvpage4.setText("  Вероятность: "+probability);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getView().getContext(),CameraActivity.class);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        return view;
    }


}
