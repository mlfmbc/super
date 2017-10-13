package com.mlfmbc.widget;

/**
 * Created by chang on 2017/10/11.
 */

public interface HeaderViewInterface {
    // 下拉加载
    void onPullDownLoading();
    //松开加载
    void onLoosenLoad();
    // 加载中
    void onLoading();
    // 加载结束
    void onLoadEnd();
    // 下拉进度
    void onPullDownProgressDiffY(int refreshDiffY);
    // 下拉恢复进度（松手回弹进度）
    void onPullDownResumeProgressDiffY(int refreshDiffY);

}
