package com.example.umang.githubaccount;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.github.sdk.bean.dto.response.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by umang on 22/8/16.
 */
public class git_singleton {

        private static git_singleton mInstance = null;
        private User userobj;
        private List<Repo> reposp;

        private git_singleton(){

            userobj=new User();
            reposp=new ArrayList<>();
        }
        public static git_singleton getInstance(){
            if(mInstance == null)
            {
                mInstance = new git_singleton();
            }
            return mInstance;
        }
        public void setUserdetails(User user){
            userobj=user;


        }
}
