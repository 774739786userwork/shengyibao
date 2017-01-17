package com.bangware.shengyibao.ladingbilling.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.customer.model.entity.Contacts;
import com.bangware.shengyibao.deliverynote.model.entity.DeliveryNote;
import com.bangware.shengyibao.deliverynote.view.BluetoothPrinterActivity;
import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenBean;
import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenGoods;
import com.bangware.shengyibao.ladingbilling.presenter.StockPresenter;
import com.bangware.shengyibao.model.Product;
import com.bangware.shengyibao.purchaseorder.presenter.PurchaseOrderPresenter;
import com.bangware.shengyibao.shopcart.model.entity.ShopCartGoods;
import com.bangware.shengyibao.shopcart.view.ProductPopupWindow;
import com.bangware.shengyibao.utils.NumberUtils;

import java.io.Serializable;


public class DisburdenPopupWindow extends PopupWindow {

	private View mMenuView;
    private Product mProduct;
    private Context mContext;
    private DisburdenGoods goods;
    private TextView disburden_pname;
    private TextView disburden_Stock,disburden_Stock_unit,disburden_sum_unit;
    private EditText disburden_sum;
    private Button disburden_dialog_cancelbtn,disburden_dialog_okbtn;
    public DisburdenPopupWindow(final Activity context, final Product product, final StockPresenter presenter) {
        super(context);
        this.mContext = context;
        this.mProduct = product;
        goods=presenter.getDisburdenBean().getGoods(product.getId());
        if (goods == null) {
            goods = new DisburdenGoods();
            goods.setProduct(product);
        }
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.show_disburdenpopupwindow, null);
        findView();
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
    /*    ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);*/
        this.setOutsideTouchable(true);
        disburden_Stock.setText(String.valueOf(product.getStock()));
        disburden_Stock_unit.setText(product.getUnit());
        disburden_sum_unit.setText(product.getUnit());
        disburden_sum.setText(String.valueOf(product.getStock()));
        disburden_pname.setText(product.getName());
        disburden_sum.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        disburden_dialog_okbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(disburden_sum.getText().toString())<=product.getStock()){
                    goods.setDisburden(disburden_sum.getText().toString());
                    goods.setSurplus(String.valueOf(mProduct.getStock()-Integer.valueOf(disburden_sum.getText().toString())));
                    if (Integer.valueOf(disburden_sum.getText().toString()) == 0) {
                    Log.e("11122","sssssssssssssssssssssss");
                    presenter.removeProductGoods(product);
                } else {
                    Log.e("goods",goods.getDisburden());
                    presenter.addProductGoods(goods);
                }
                DisburdenPopupWindow.this.dismiss();
                }else
                {
                    Toast.makeText(mContext,"卸货数量不能大于车库存",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void findView() {
        disburden_Stock= (TextView) mMenuView.findViewById(R.id.disburden_Stock);
        disburden_Stock_unit= (TextView) mMenuView.findViewById(R.id.disburden_Stock_unit);
        disburden_sum_unit= (TextView) mMenuView.findViewById(R.id.disburden_sum_unit);
        disburden_sum= (EditText) mMenuView.findViewById(R.id.disburden_sum);
        disburden_dialog_cancelbtn= (Button) mMenuView.findViewById(R.id.disburden_dialog_cancelbtn);
        disburden_dialog_okbtn= (Button) mMenuView.findViewById(R.id.disburden_dialog_okbtn);
        disburden_pname= (TextView) mMenuView.findViewById(R.id.disburden_pname);
        disburden_dialog_cancelbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DisburdenPopupWindow.this.dismiss();
            }
        });

    }


    /**
     * 显示popupWindow
     *
     */
    public void showPopupWindow(View view) {
        if (!this.isShowing()) {
            this.showAtLocation(view, Gravity.CENTER
                    | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else {
            this.dismiss();
        }
    }
}