package com.bangware.shengyibao.ladingbilling.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.ladingbilling.adapter.StockQueryAdapter;
import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenBean;
import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenGoods;
import com.bangware.shengyibao.ladingbilling.model.entity.LadingbillingQuery;
import com.bangware.shengyibao.ladingbilling.presenter.DisburdenPresenter;
import com.bangware.shengyibao.ladingbilling.presenter.StockPresenter;
import com.bangware.shengyibao.ladingbilling.presenter.impl.DisburenPresentImpl;
import com.bangware.shengyibao.ladingbilling.presenter.impl.StockPresenterImpl;
import com.bangware.shengyibao.model.Product;
import com.bangware.shengyibao.shopcart.view.ProductPopupWindow;
import com.bangware.shengyibao.shopcart.view.ShopCartAcitivity;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;
import com.nostra13.universalimageloader.utils.L;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.wch.wchusbdriver.CH34xAndroidDriver.TAG;

/**
 * 余货查询
 */
public class StockQueryActivity extends BaseActivity implements StockQueryView,DisburdenView{
    private ImageView backImg;
    private Button printerStockTextview;
    private TextView  query_disburden,disburden_cache;
    private ListView stocklistview;
    private StockQueryAdapter stockQueryAdapter;
    private List<Product> stocklist = new ArrayList<Product>();
    private User user;
    private long mExitTime = System.currentTimeMillis();
    private DisburdenPopupWindow mPopupWindow;
    private LadingbillingQuery ladingbillingQuery;

    private StockPresenter stockPresenter;
    private DisburdenPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_query);
        SharedPreferences sharedPreferences=this.getSharedPreferences(User.SHARED_NAME, MODE_PRIVATE);
        user= AppContext.getInstance().readFromSharedPreferences(sharedPreferences);
        findViews();
        setListener();
    }


    public void findViews(){
        backImg = (ImageView) findViewById(R.id.backImg);
        printerStockTextview = (Button) findViewById(R.id.printer_stock_textview);
        stocklistview = (ListView) findViewById(R.id.stockListView);
        query_disburden= (TextView) findViewById(R.id.query_disburden);
        disburden_cache= (TextView) findViewById(R.id.disburden_cache);
        stockPresenter = new StockPresenterImpl(this);
        stockPresenter.onLoadStock(user);

        presenter=new DisburenPresentImpl(this);
        stockQueryAdapter = new StockQueryAdapter(this,stocklist,stockPresenter);
        stocklistview.setAdapter(stockQueryAdapter);
        stocklistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ((System.currentTimeMillis() - mExitTime) > 1000) {
                    Product item=(Product) parent.getItemAtPosition(position);
                    if(item.getStock()>0){
                        mPopupWindow = new DisburdenPopupWindow(StockQueryActivity.this, item, stockPresenter);
                        mPopupWindow.showPopupWindow(view);
                        backgroundAlpha(0.4f);
                        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener(){
                            @Override
                            public void onDismiss() {
                                backgroundAlpha(1f);
                            }
                        });
                        mExitTime = System.currentTimeMillis();
                    }
                }
            }
        });
        disburden_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    private void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    public void setListener(){
        MyOnClickListener listener = new MyOnClickListener();
        backImg.setOnClickListener(listener);
        printerStockTextview.setOnClickListener(listener);
    }

    @Override
    public void doSaveDisburdenSuccess(List<DisburdenGoods> disburdenGoodsList) {
        /**
         * 用intent传递List<Object>集合方法
         */
        Intent intent = new Intent(StockQueryActivity.this,StockBluetoothPrinterActivity.class);
        intent.putExtra("product", (Serializable) stocklist);
        intent.putExtra("carNumber", (Serializable) ladingbillingQuery);
        intent.putExtra("DisburdenGoods",(Serializable) disburdenGoodsList);
        showToast("保存成功");
        startActivity(intent);
    }

    private class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.backImg){
                finish();
            }
            if (v.getId() == R.id.printer_stock_textview){
                if (stockPresenter.getDisburdenBean().getAllGoodsList().size()==0)
                {
                    Intent intent = new Intent(StockQueryActivity.this,StockBluetoothPrinterActivity.class);
                    intent.putExtra("product", (Serializable) stocklist);
                    intent.putExtra("carNumber", (Serializable) ladingbillingQuery);
                    intent.putExtra("DisburdenGoods",(Serializable) stockPresenter.getDisburdenBean().getAllGoodsList());
                    showToast("保存成功");
                    startActivity(intent);
                }else {
                    presenter.doDisburenSave(user, stockPresenter.getDisburdenBean().getAllGoodsList(), ladingbillingQuery.getCarId());
                }
            }
        }
    }

    //获取车牌号
    @Override
    public void doChanged(LadingbillingQuery ladingbillingQuery) {
        this.ladingbillingQuery = ladingbillingQuery;

    }

    @Override
    public void doProcuctChanged(DisburdenBean bean) {
//        stocklist=bean.getAllGoodsList();
        stockQueryAdapter.notifyDataSetChanged();
    }
    /**
     * 加载库存
     * @param productstockList
     */
    @Override
    public void loadProductStockData(List<Product> productstockList) {
        if (productstockList.size() >0){
            stocklist.addAll(productstockList);
            stockQueryAdapter.notifyDataSetChanged();
        }else{
            showToast("无余货记录！");
        }
    }

    public void onDestroy(){
        super.onDestroy();
        if(stockPresenter!=null)
            stockPresenter.destroy();
    }
}
