package com.example.umang.githubaccount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alorma.github.sdk.bean.dto.response.User;
import com.alorma.github.sdk.services.user.GetAuthUserClient;
import com.alorma.github.sdk.services.user.TwoFactorAuthException;
import com.alorma.github.sdk.services.user.UnauthorizedException;
import com.google.gson.Gson;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by umang on 19/8/16.
 */
public class Login extends AppCompatActivity {

    EditText user_name, password;
    Button signIn;
    ProgressDialog progress;
    sharedPreferences shared;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        shared = new sharedPreferences(Login.this);
        user_name = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
        signIn = (Button) findViewById(R.id.sign_in);
        gson = new Gson();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isEmptyString(user_name.getText().toString())) {
                    user_name.setError("Enter User Name");
                } else if (Utility.isEmptyString(password.getText().toString())) {
                    password.setError("Enter Password");
                } else {
                    progress = new ProgressDialog(Login.this);
                    progress.setMessage("Please Wait");
                    progress.setIndeterminate(true);
                    progress.setCancelable(false);
                    progress.show();

                    new GetAuthUserClient(user_name.getText().toString(), password.getText().toString()).observable()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.newThread())
                            .subscribe(new Action1<User>() {

                                @Override
                                public void call(User user) {
                                    Log.e("hello", "@@" + user_name.getText().toString());
                                    shared.save(sharedPreferences.USERNAME, user_name.getText().toString());
                                    String userde = gson.toJson(user);
                                    shared.save(sharedPreferences.USER_DE, userde);
                                    Log.e("LOGIN", user.toString());
                                    git_singleton.getInstance().setUserdetails(user);
                                    progress.dismiss();
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    finish();
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    if (throwable instanceof UnauthorizedException) {
                                        Toast.makeText(Login.this, "incorrect credentials", Toast.LENGTH_SHORT).show();
                                    } else if (throwable instanceof TwoFactorAuthException) {

                                    } else {
                                        Log.e("LOGIN", throwable.getMessage());
                                    }
                                    progress.dismiss();
                                }
                            });
                }
            }
        });

    }


}


