package com.bangware.shengyibao.view;

/**
 * 案例独有上拉下拉加载监听事件
 * Created by bangware on 2017/1/10.
 */

public interface OnCasesRefreshListener {
    /**
     * 下拉刷新
     */
    void onDownPullRefresh();

    /**
     * 上拉加载更多
     */
    void onLoadingMore();
}
