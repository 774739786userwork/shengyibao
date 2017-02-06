package com.bangware.shengyibao.ladingbilling.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.bangware.shengyibao.ladingbilling.model.entity.CarBean;
import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenBean;
import com.bangware.shengyibao.ladingbilling.model.entity.DisburdenGoods;
import com.bangware.shengyibao.ladingbilling.presenter.DisburdenPresenter;
import com.bangware.shengyibao.ladingbilling.presenter.StockPresenter;
import com.bangware.shengyibao.ladingbilling.presenter.impl.DisburenPresentImpl;
import com.bangware.shengyibao.ladingbilling.presenter.impl.StockPresenterImpl;
import com.bangware.shengyibao.model.Product;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
/**
 * 余货查询
 */
public class StockQueryActivity extends BaseActivity implements StockQueryView,DisburdenView{
    private ImageView backImg;
    private Button printerStockTextview,disburden_cache,printerSettleBtn;
    private TextView  query_disburden;
    private ListView stocklistview;
    private StockQueryAdapter stockQueryAdapter;
    private List<Product> stocklist = new ArrayList<Product>();
    private User user;
    private CarBean carBean;
    private List<CarBean> carBeanList;
    private String  serial_numbers;
    private long mExitTime = System.currentTimeMillis();
    private DisburdenPopupWindow mPopupWindow;
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
        disburden_cache= (Button) findViewById(R.id.disburden_cache);
        printerSettleBtn = (Button) findViewById(R.id.print_balancesettle_btn);
        stocklistview = (ListView) findViewById(R.id.stockListView);
        query_disburden= (TextView) findViewById(R.id.query_disburden);

        carBean=(CarBean)getIntent().getExtras().getSerializable("CarBean");
        carBeanList = (List<CarBean>) getIntent().getExtras().getSerializable("carList");
        stockPresenter = new StockPresenterImpl(this);
        stockPresenter.onLoadStock(user,carBean.getCar_id());

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
        query_disburden.setOnClickListener(listener);
        disburden_cache.setOnClickListener(listener);
        printerSettleBtn.setOnClickListener(listener);
    }

    @Override
    public void doSaveDisburdenSuccess(List<DisburdenGoods> disburdenGoodsList, String serial_number) {
        /**
         * 用intent传递List<Object>集合方法
         */
        serial_numbers=serial_number;
        Intent intent = new Intent(StockQueryActivity.this,StockBluetoothPrinterActivity.class);
        intent.putExtra("product", (Serializable) stocklist);
        intent.putExtra("carNumber", (Serializable) carBean);
        intent.putExtra("serial_num",serial_numbers);
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
            //卸货单打印并提交数据
            if (v.getId() == R.id.printer_stock_textview){
                if (stockPresenter.getDisburdenBean().getAllGoodsList().size()>0)
                {
                    presenter.doDisburenSave(user, stockPresenter.getDisburdenBean().getAllGoodsList(), carBean.getCar_id());
                }else {
                    showAlertDialog();
                }
            }
            //卸货单查询
            if (v.getId() == R.id.query_disburden){
                Intent intent=new Intent(StockQueryActivity.this,QueryDisburdenActivity.class);
                startActivity(intent);
            }
            //卸货单缓存
            if (v.getId() == R.id.disburden_cache){

            }
            //结算打印
            if (v.getId() == R.id.print_balancesettle_btn){
                Intent intent=new Intent(StockQueryActivity.this,SettleBluetoothPrinterActivity.class);
                intent.putExtra("carList", (Serializable) carBeanList);
                startActivity(intent);
            }
        }
    }


    private void showAlertDialog(){
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setTitle("请选择你需要卸掉的产品！");
        builer.setCancelable(false);
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            }
        });
        builer.show();
    }


    @Override
    public void doProcuctChanged(DisburdenBean bean) {
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
