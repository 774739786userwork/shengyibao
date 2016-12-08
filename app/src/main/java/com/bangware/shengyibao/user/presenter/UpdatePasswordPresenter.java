package com.bangware.shengyibao.user.presenter;

public interface UpdatePasswordPresenter {
	void doUpdatePassword( String oldPassword,String newPassword);
	
	void destroy();
}
