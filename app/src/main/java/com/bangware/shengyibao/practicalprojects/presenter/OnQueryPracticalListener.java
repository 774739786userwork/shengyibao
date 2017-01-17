package com.bangware.shengyibao.practicalprojects.presenter;

import com.bangware.shengyibao.practicalprojects.model.entity.MyBean;

import java.util.List;


public interface OnQueryPracticalListener {
	void onLoadQueryPracticalSuccess(List<MyBean> Bean);
	void onLoadQueryPracticalFailure(String errorMessage);
}
