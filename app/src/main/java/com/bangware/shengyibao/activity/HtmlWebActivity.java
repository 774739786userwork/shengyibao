package com.bangware.shengyibao.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bangware.shengyibao.activity.entity.Good;
import com.bangware.shengyibao.config.ViewHolder;

import java.util.ArrayList;

public class HtmlWebActivity extends BaseActivity {
    private ListView listView;
    private ImageView htmlback;

    private ArrayList<Good> listItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_web);

        initView();
    }

    private void initView(){
        listView = (ListView) findViewById(R.id.listView_one);
        htmlback = (ImageView) findViewById(R.id.html_back);

        listItem = getDate();
        MyAdapter mAdapter = new MyAdapter(this,listItem);//得到一个MyAdapter对象
        listView.setAdapter(mAdapter);

        htmlback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    /**添加一个得到数据的方法，方便使用*/
    private ArrayList<Good> getDate(){
        listItem = new ArrayList<Good>();
        /**为动态数组添加数据*/
        Good good = new Good();
        good.setImg(R.drawable.lvmandi);
        good.setGoodname("绿满地K11渗透结晶防水");
        good.setGoodsendtime("发布时间：2016/06/01");
        good.setGoodtitle("标签：多邦建材 多邦防水");
        good.setGoodcontent("产品特点 多邦K11渗透结晶防水浆料是一种双组份、丙烯酸类高聚物改性水泥基防水材料。粉料与乳液混合后，发生化学反应，形成一层坚韧的、富弹性的防水膜，该膜对混凝土和灰浆有良好的粘附性，能与之形成紧密牢固...");
        listItem.add(good);

        Good good1 = new Good();
        good1.setImg(R.drawable.toumingfanshui);
        good1.setGoodname("透明防水剂");
        good1.setGoodsendtime("发布时间：2016/05/26");
        good1.setGoodtitle("标签：多邦建材 多邦防水");
        good1.setGoodcontent("材料特点 ◆ 多邦透明防水胶是以高分子共聚物乳液为主要材料，掺和表面活性剂、成膜助剂复合而成的新型水性防水涂膜胶； ◆ 本产品涂膜后无色透明，不会影响原有饰面的装饰作用； ◆ 本产品为水性环保涂料，无溶剂污...");
        listItem.add(good1);

        Good good2=new Good();
        good2.setImg(R.drawable.zhongguomeng);
        good2.setGoodname("通用瓷砖胶 中国梦");
        good2.setGoodsendtime("发布时间：2016/05/06");
        good2.setGoodtitle("标签：多邦建材多邦瓷砖胶通用瓷砖胶 中国梦");
        good2.setGoodcontent("产品特点 1、粘结力较强：大大超过传统水泥砂浆、水泥净浆的粘结力。 2、施工简便快捷：无需浸砖湿墙，施工工艺的改革使得比传统旧工艺快2-3倍。 3、耐老化、抗渗性良好：产品是经改良的无机合成材料。 4、工程造...");
        listItem.add(good2);

        Good good3=new Good();
        good3.setImg(R.drawable.zhengnengliang);
        good3.setGoodname("正能量强力瓷砖胶");
        good3.setGoodsendtime("发布时间：2016/05/07");
        good3.setGoodtitle("标签：多邦建材正能量强力瓷砖胶");
        good3.setGoodcontent("产品特性 (1)施工简便，无需浸砖湿墙，工效提高； (2)采用新工艺一刮齿法施工，用量少且均匀，避免出现空鼓，较传统水泥砂浆粘贴法更安全牢固； (3)具有良好的保水性能，足够时间对粘结瓷砖进行调整； (4)优良的...");
        listItem.add(good3);

        Good good4=new Good();
        good4.setImg(R.drawable.neiqiang);
        good4.setGoodname("内墙耐水腻子王");
        good4.setGoodsendtime("发布时间：2016/09/21");
        good4.setGoodtitle("标签：多邦建材多邦腻子粉");
        good4.setGoodcontent("应用范围 用于室内水泥砂浆、混凝土、石膏板等基层表面的嵌补、罩面、地下室等需防潮、防霉的基面。不宜用于水泥浆压光基面。 性能特点 粘结力强，对墙体及涂料具有极强的粘结力。 抗渗性，耐水性好。受潮、遇水...");
        listItem.add(good4);

        Good good5=new Good();
        good5.setImg(R.drawable.waiqiang);
        good5.setGoodname("外墙耐水腻子粉 假货克星「福临门」");
        good5.setGoodsendtime("发布时间：2016/08/11");
        good5.setGoodtitle("标签：外墙耐水腻子粉多邦建材");
        good5.setGoodcontent("产品特性 粘结力强、对墙体及涂料具有极强的粘结力； 抗裂性好，耐水性佳； 具有良好的耐水性及透气性，确保墙体及漆面的干爽； 质感自然，确保涂料的装饰效果能彻底体现。 表面处理 底材表面应结实平整、清洁、...");
        listItem.add(good5);
        return listItem;

    }

    /** 新建一个类继承BaseAdapter，实现视图与数据的绑定
     */
    private class MyAdapter extends BaseAdapter

    {
        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
        private Context context;
        private ArrayList<Good> listItem;
        /**构造函数*/
        public MyAdapter(Context context,ArrayList<Good> listItem) {
            this.context = context;
            this.listItem = listItem;
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount () {
            return getDate().size();//返回数组的长度
        }

        @Override
        public Object getItem ( int position){
            return null;
        }

        @Override
        public long getItemId ( int position){
            return 0;
        }

        @Override
        public View getView (final int position, View convertView, ViewGroup parent){
            //观察convertView随ListView滚动情况
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_good_one, null);
            }
            ImageView goods_img = ViewHolder.get(convertView,R.id.ItemGoodsImage);
            TextView goods_name = ViewHolder.get(convertView,R.id.ItemGoodsName);
            TextView goods_time = ViewHolder.get(convertView,R.id.ItemGoodSendTime);
            TextView goods_title = ViewHolder.get(convertView,R.id.ItemGoodTitle);
            TextView goods_content = ViewHolder.get(convertView,R.id.ItemGoodContent);

            /**设置TextView显示的内容，即我们存放在动态数组中的数据*/
            goods_img.setImageResource(listItem.get(position).getImg());
            goods_name.setText(listItem.get(position).getGoodname());
            goods_time.setText(listItem.get(position).getGoodsendtime());
            goods_title.setText(listItem.get(position).getGoodtitle());
            goods_content.setText(listItem.get(position).getGoodcontent());
            return convertView;
        }
    }
}
