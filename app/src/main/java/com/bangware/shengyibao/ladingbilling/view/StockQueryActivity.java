package com.bangware.shengyibao.ladingbilling.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.ladingbilling.adapter.StockQueryAdapter;
import com.bangware.shengyibao.ladingbilling.presenter.StockPresenter;
import com.bangware.shengyibao.ladingbilling.presenter.impl.StockPresenterImpl;
import com.bangware.shengyibao.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * 余货查询
 */
public class StockQueryActivity extends BaseActivity implements StockQueryView{
    private ImageView backImg;
    private ListView stocklistview;
    private StockQueryAdapter stockQueryAdapter;
    private List<Product> stocklist = new ArrayList<Product>();
    private StockPresenter stockPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_query);

        findViews();
        setListener();
    }


    public void findViews(){
        backImg = (ImageView) findViewById(R.id.backImg);
        stocklistview = (ListView) findViewById(R.id.stockListView);

        stockPresenter = new StockPresenterImpl(this);
        stockPresenter.onLoadStock();
        stockQueryAdapter = new StockQueryAdapter(this,stocklist);
        stocklistview.setAdapter(stockQueryAdapter);
    }

    public void setListener(){
        MyOnClickListener listener = new MyOnClickListener();
        backImg.setOnClickListener(listener);
    }

    private class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.backImg){
                finish();
            }
        }
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
