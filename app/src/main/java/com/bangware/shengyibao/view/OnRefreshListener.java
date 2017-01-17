package com.bangware.shengyibao.view;

/**
 * 公共的上拉下拉事件
 */
public interface OnRefreshListener {
	/**
	 * 下拉刷新
	 */
	void onDownPullRefresh();

	/**
	 * 上拉加载更多
	 */
	void onLoadingMore();
}
