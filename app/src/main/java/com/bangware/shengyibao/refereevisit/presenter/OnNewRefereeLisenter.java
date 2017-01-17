package com.bangware.shengyibao.refereevisit.presenter;

/**
 * Created by Administrator on 2016/12/26.
 */

public interface OnNewRefereeLisenter {
    void onAddRefereeSuccess(String successMessage);
    void onSaveSuccess();
    void onAddRefereeFailure(String errorMessage);
}
