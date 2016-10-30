package com.hdcy.app.fragment.fourth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hdcy.app.R;
import com.hdcy.app.basefragment.BaseLazyMainFragment;
import com.hdcy.app.fragment.first.FirstFragment;
import com.hdcy.app.fragment.fourth.child.FourthPagesFragment;
import com.hdcy.app.fragment.second.SecondFragment;
import com.hdcy.app.fragment.second.childpages.SecondPagesFragment;
import com.hdcy.app.model.LeaderInfo;
import com.hdcy.app.model.NewsCategory;
import com.hdcy.app.view.CustomViewPager;
import com.hdcy.base.utils.net.NetHelper;
import com.hdcy.base.utils.net.NetRequestCallBack;
import com.hdcy.base.utils.net.NetRequestInfo;
import com.hdcy.base.utils.net.NetResponseInfo;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import me.nereo.multi_image_selector.bean.Image;

/**
 * Created by WeiYanGeorge on 2016-10-07.
 */

public class FourthFragment extends BaseLazyMainFragment{


    private ImageView iv_leader_fl_bt;
    private TabLayout mTab;
    private CustomViewPager mViewPager;
    private BGABanner leaderBanner;
    private String[] pagetitles = new String[]{"全部","个人","机构"};
    private List<LeaderInfo> leaderBannerInfo = new ArrayList<>();

    private List<String> imgurls = new ArrayList<>();
    private List<String> tips = new ArrayList<>();

    private AlertDialog alertDialogAsk;
    private AlertDialog alertDialogOrg;

    private TextView tv_leader_orga_apply;

    public static FourthFragment newInstance() {
        Bundle args = new Bundle();
        FourthFragment fragment = new FourthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
        mViewPager.setAdapter(new ViewPageFragmentAdapter(getChildFragmentManager()));
        mTab.setupWithViewPager(mViewPager);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fourth_page,container,false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view){
        mTab = (TabLayout) view.findViewById(R.id.tab);
        mViewPager = (CustomViewPager) view.findViewById(R.id.viewPager);
        mViewPager.setFocusable(false);
        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());
        leaderBanner = (BGABanner) view.findViewById(R.id.leader_banners);
        leaderBanner.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                Glide.with(banner.getContext()).load(model).placeholder(R.mipmap.icon_chat_camera).error(R.mipmap.icon_chat_camera).dontAnimate().thumbnail(0.1f).into((ImageView) view);
            }
        });
        iv_leader_fl_bt = (ImageView) view.findViewById(R.id.iv_leader_fl_bt);
        iv_leader_fl_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowLeaderApplyforDialog();
            }
        });
        setData();
    }

    private void initData(){
        GetLeaderBanner();
    }

    private void setData(){
        mViewPager.setAdapter(new ViewPageFragmentAdapter(getChildFragmentManager()));
        mTab.setupWithViewPager(mViewPager);
    }

    private void ShowLeaderApplyforDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alertdialog_applyleaders,null);
        tv_leader_orga_apply = (TextView) view.findViewById(R.id.tv_leader_orga_apply);
        tv_leader_orga_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowOrganizationDialog();
            }
        });
        builder.setView(view);
        builder.create();
        alertDialogAsk = builder.create();
        Window wm = alertDialogAsk.getWindow();
        wm.setGravity(Gravity.CENTER);
        alertDialogAsk.show();
    }

    private void ShowOrganizationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alertdialog_leader_organization,null);
        builder.setView(view);
        builder.create();
        alertDialogOrg = builder.create();
        Window wm = alertDialogOrg.getWindow();
        wm.setGravity(Gravity.CENTER);
        alertDialogOrg.show();
        alertDialogAsk.dismiss();
    }

    private void setData1(){
        leaderBanner.setData(imgurls,tips);
    }

    private void GetLeaderBanner(){
        NetHelper.getInstance().GetLeaderBanner(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                leaderBannerInfo.clear();
                leaderBannerInfo = responseInfo.getLeaderInfo();
                for (int i = 0; i < leaderBannerInfo.size(); i++) {
                    imgurls.add(i, leaderBannerInfo.get(i).getTopImage());
                    tips.add(i, leaderBannerInfo.get(i).getName());
                }
                setData1();

            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }
        });

    }


    public class ViewPageFragmentAdapter extends FragmentPagerAdapter {
        public ViewPageFragmentAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return FourthPagesFragment.newInstance("whole");
            }else if(position ==1){
                return FourthPagesFragment.newInstance("false");
            }else if(position ==2){
                return FourthPagesFragment.newInstance("true");
            }else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pagetitles[position];
        }
    }










}
