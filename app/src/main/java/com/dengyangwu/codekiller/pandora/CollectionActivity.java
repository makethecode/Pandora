package com.dengyangwu.codekiller.pandora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<News> newsList;
    private RecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);


        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);

        initPersonData();
        adapter=new RecyclerViewAdapter(newsList,CollectionActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initPersonData() {
        newsList =new ArrayList<>();
        //添加新闻
        newsList.add(new News(getString(R.string.news_one_title),getString(R.string.news_one_desc),R.mipmap.news_one));
        newsList.add(new News(getString(R.string.news_two_title),getString(R.string.news_two_desc),R.mipmap.news_two));
        newsList.add(new News(getString(R.string.news_three_title),getString(R.string.news_three_desc),R.mipmap.news_three));
        newsList.add(new News(getString(R.string.news_four_title),getString(R.string.news_four_desc),R.mipmap.news_four));
        newsList.add(new News(getString(R.string.news_five_title),getString(R.string.news_five_desc),R.mipmap.news_five));
        newsList.add(new News(getString(R.string.news_six_title),getString(R.string.news_six_desc),R.mipmap.news_six));
    }
}
