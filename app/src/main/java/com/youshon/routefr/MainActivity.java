package com.youshon.routefr;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void test(){
       // RouteManager.intent2ActivityDemo2(this,"",1,new ArrayList<Integer>());
        Context context = (Context)this;
        int i = 0;
    }


}
