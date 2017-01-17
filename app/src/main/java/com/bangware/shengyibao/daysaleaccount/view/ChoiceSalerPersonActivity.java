package com.bangware.shengyibao.daysaleaccount.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bangware.shengyibao.activity.BaseActivity;
import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.daysaleaccount.adapter.ChoiceSalerPersonAdapter;
import com.bangware.shengyibao.daysaleaccount.model.entity.ChoicePersonBean;
import com.bangware.shengyibao.daysaleaccount.presenter.SalerPersonPresenter;
import com.bangware.shengyibao.daysaleaccount.presenter.impl.SalerPersonPresenterImpl;
import com.bangware.shengyibao.user.model.entity.User;
import com.bangware.shengyibao.utils.AppContext;
import com.bangware.shengyibao.utils.CharacterParser;
import com.bangware.shengyibao.utils.ClearEditText;
import com.bangware.shengyibao.utils.PinyinComparator;
import com.bangware.shengyibao.view.SideBar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 记量选择业务员
 */
public class ChoiceSalerPersonActivity extends BaseActivity implements SalerPersonView{
    //UI控件的封装
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private ChoiceSalerPersonAdapter adapter;
    private ClearEditText mClearEditText;
    private ImageView saler_backimage;

    //汉字转换成拼音的类 ChoicePersonBean
    private CharacterParser characterParser;
    private List<ChoicePersonBean> SourceDateList = new ArrayList<ChoicePersonBean>();

    //根据拼音来排列Listview里面的数据类
    private PinyinComparator pinyinComparator;

    private User user;
    private SalerPersonPresenter personPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //规定输入法弹出模式 避免挤压屏幕与覆盖控件
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_choice_saler_person);

        SharedPreferences sharedPreferences = this.getSharedPreferences(User.SHARED_NAME,MODE_PRIVATE);
        user = AppContext.getInstance().readFromSharedPreferences(sharedPreferences);

        initViews();
    }

    private void initViews() {
        //返回键监听
        saler_backimage = (ImageView) findViewById(R.id.saler_backimage);
        saler_backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //实例化汉字转拼音类
        characterParser = CharacterParser.getCharacterParser();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    sortListView.setSelection(position);
                }
            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
                Intent intent = new Intent();
                intent.putExtra("employeeName",((ChoicePersonBean)adapter.getItem(position)).getName());
                intent.putExtra("employeedId",((ChoicePersonBean) adapter.getItem(position)).getId());
                setResult(2001,intent);
                finish();
            }
        });

        personPresenter = new SalerPersonPresenterImpl(this);
        personPresenter.loadSalerPersonData(user);
//        SourceDateList = filledData(getResources().getStringArray(R.array.person));
        adapter = new ChoiceSalerPersonAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);

        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 为ListView填充数据
     * @param plist
     * @return
     */
    private List<ChoicePersonBean> filledData(List<ChoicePersonBean> plist){
        List<ChoicePersonBean> mSortList = new ArrayList<ChoicePersonBean>();

        for(int i=0; i<plist.size(); i++){
            ChoicePersonBean choicePersonBean = new ChoicePersonBean();
            choicePersonBean.setName(plist.get(i).getName());
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(plist.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                choicePersonBean.setSortLetters(sortString.toUpperCase());
            }else{
                choicePersonBean.setSortLetters("#");
            }

            mSortList.add(choicePersonBean);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * @param filterStr
     */
    private void filterData(String filterStr){
        List<ChoicePersonBean> filterDateList = new ArrayList<ChoicePersonBean>();

        if(TextUtils.isEmpty(filterStr)){
            filterDateList.addAll(SourceDateList);
        }else{
            filterDateList.clear();
            for(ChoicePersonBean sortModel : SourceDateList){
                String name = sortModel.getName();
                if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    @Override
    public void doLoadSalePersonData(List<ChoicePersonBean> personBeanList) {
        if (personBeanList.size() > 0){
            SourceDateList.addAll(personBeanList);
            // 根据a-z进行排序源数据
            Collections.sort(SourceDateList, pinyinComparator);
            adapter.notifyDataSetChanged();
        }else {
            showToast("查无此数据");
            adapter.notifyDataSetChanged();
        }
    }
}
