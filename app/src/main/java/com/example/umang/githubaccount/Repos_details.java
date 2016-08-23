package com.example.umang.githubaccount;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

/**
 * Created by umang on 22/8/16.
 */
public class Repos_details extends AppCompatActivity {


    TabLayout tabLayout;
    FragmentManager manger;
    FragmentTransaction transaction;
    FrameLayout container;
    String Reponame=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repository);

        tabLayout= (TabLayout) findViewById(R.id.tab_layout);
        container= (FrameLayout) findViewById(R.id.container);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.tab)));


        tabLayout.setTabTextColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.cardview_dark_background));

        Reponame=getIntent().getExtras().getString("Reponame");
        manger=getFragmentManager();

        transaction = manger.beginTransaction();

        transaction.replace(R.id.container, issues.newInstance(Reponame)).commit();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:

                        transaction = manger.beginTransaction();
                        transaction.replace(R.id.container, issues.newInstance(Reponame)).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}