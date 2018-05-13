package com.dengyangwu.codekiller.pandora;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ShowfleamarketActivity extends AppCompatActivity {

    private TextView type,demand,price;
    private ImageView img1,img2,img3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showfleamarket);
        type= (TextView) findViewById(R.id.show_flea_type);
        demand= (TextView) findViewById(R.id.show_flea_demand);
        price= (TextView) findViewById(R.id.show_flea_price);
        img1= (ImageView) findViewById(R.id.show_flea_img1);
        img2= (ImageView) findViewById(R.id.show_flea_img2);
        img3= (ImageView) findViewById(R.id.show_flea_img3);

        Intent intent=getIntent();
        Fleamarket item= (Fleamarket) intent.getSerializableExtra("store");
        type.setText("类型："+item.type);
        demand.setText("需求："+item.demand);
        price.setText("价格："+item.price+"￥");
        switch (item.photo.size()) {
            case 0:
                break;
            case 1:
                Glide.with(ShowfleamarketActivity.this).load(item.photo.get(0)).into(img1);
                break;
            case 2:
                Glide.with(ShowfleamarketActivity.this).load(item.photo.get(0)).into(img1);
                Glide.with(ShowfleamarketActivity.this).load(item.photo.get(1)).into(img2);
                break;
            default:
                Glide.with(ShowfleamarketActivity.this).load(item.photo.get(0)).into(img1);
                Glide.with(ShowfleamarketActivity.this).load(item.photo.get(1)).into(img2);
                Glide.with(ShowfleamarketActivity.this).load(item.photo.get(2)).into(img3);
                break;
        }
    }
}
