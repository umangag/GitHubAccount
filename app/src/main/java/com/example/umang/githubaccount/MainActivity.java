package com.example.umang.githubaccount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.github.sdk.services.repos.GithubReposClient;
import com.alorma.github.sdk.services.repos.UserReposClient;
import com.alorma.gitskarios.core.Pair;
import com.alorma.gitskarios.core.client.TokenProvider;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    RecyclerView Reclycer;
    GithubReposClient client;
    List<Repo> repos = new ArrayList<>();
    User userdetails;
    Gson gson;
    ReposAdapter adpater;
    sharedPreferences shared;
    Utility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Reclycer= (RecyclerView) findViewById(R.id.Reclycer);
        utility=new Utility(this);
        gson=new Gson();
        shared=new sharedPreferences(this);
        Reclycer.setHasFixedSize(true);
        Reclycer.setLayoutManager(new LinearLayoutManager(this));

        GetReposDetails();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                shared.clearpref();
                startActivity(new Intent(this,Login.class));
                finish();
                break;
        }
        return true;
    }

    private void GetReposDetails() {
        String userId=shared.getvalue(sharedPreferences.USER_DE);
        System.out.println("++"+userId);
        userdetails=gson.fromJson(userId,User.class);

        System.out.println("++Mainactivity"+userdetails.login);
        client = new UserReposClient(userdetails.login, "full_name");

        client.observable().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<Pair<List<Repo>, Integer>>() {
                    @Override
                    public void call(Pair<List<Repo>, Integer> listIntegerPair) {
                        System.out.println("+++++" + listIntegerPair.hashCode());

                        repos = listIntegerPair.first;
                        System.out.println("@@@hello"+repos);
                        if(repos!=null&&repos.size()>0) {
                            System.out.println("@@adapter if true");
                            // git_singleton.getInstance().setReposDetails(repos);
                            adpater = new ReposAdapter(repos, MainActivity.this,"dm");
                            System.out.print("@@adapter    "+adpater);
                            Reclycer.setAdapter(adpater);
                        }
                    }
                });
    }
}
