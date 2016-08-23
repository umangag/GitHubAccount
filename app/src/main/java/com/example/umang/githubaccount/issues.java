package com.example.umang.githubaccount;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alorma.github.sdk.bean.dto.response.Issue;
import com.alorma.github.sdk.bean.info.IssueInfo;
import com.alorma.github.sdk.bean.info.RepoInfo;
import com.alorma.github.sdk.exception.ApiException;
import com.alorma.github.sdk.services.issues.GetIssuesClient;
import com.alorma.gitskarios.core.Pair;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by umang on 22/8/16.
 */
public class issues extends Fragment {
    View view;
    RecyclerView Reclycer_issue;
    Context context;
    String Reponame = null;
    private static final String ARG_PARAM1 = "param1";
    Gson gson;
    IssueAdapter issueAdapter;
    sharedPreferences shared;
    TextView noIssueTxt;
    Utility utility;

    public issues() {
    }

    // TODO: Rename and change types and number of parameters
    public static issues newInstance(String Reponame) {
        issues fragment = new issues();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, Reponame);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context contxt) {
        super.onAttach(context);
        context = contxt;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        utility = new Utility(getActivity());
        shared = new sharedPreferences(getActivity());
        if (getArguments() != null) {
            Reponame = getArguments().getString(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.issuefrag, container, false);
        Reclycer_issue = (RecyclerView) view.findViewById(R.id.Reclycer_issue);
        noIssueTxt = (TextView) view.findViewById(R.id.noissueTxt);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Reclycer_issue.setHasFixedSize(true);
        Reclycer_issue.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (Reponame != null) {
            GetIssues();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void GetIssues() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Wait");
        progressDialog.show();
        RepoInfo repoInfo = new RepoInfo();
        repoInfo.owner = shared.getvalue(sharedPreferences.USERNAME);
        repoInfo.branch = "master";
        repoInfo.name = Reponame;

        IssueInfo issueInfo = new IssueInfo();
        issueInfo.repoInfo = repoInfo;
        HashMap<String, String> bb = new HashMap<>();
        bb.put("all", "assigned");


        new GetIssuesClient(issueInfo, bb).observable().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<Pair<List<Issue>, Integer>>() {
                    @Override
                    public void call(Pair<List<Issue>, Integer> listIntegerPair) {
                        progressDialog.dismiss();
                        if (listIntegerPair != null) {
                            List<Issue> repos = listIntegerPair.first;
                            if (repos != null && repos.size() > 0) {
                                String issues = gson.toJson(repos);
                                issueAdapter = new IssueAdapter(repos, getActivity(), "dy");
                                Reclycer_issue.setAdapter(issueAdapter);
                            } else {
                                progressDialog.dismiss();
                                noIssueTxt.setVisibility(View.VISIBLE);

                                Toast.makeText(getActivity(), "No issues  ", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof ApiException) {
                            System.out.println("EXCEPTION+" + throwable.toString());
                        } else {
                            System.out.println("Issues+" + throwable.toString());
                        }
                    }
                });
    }
}