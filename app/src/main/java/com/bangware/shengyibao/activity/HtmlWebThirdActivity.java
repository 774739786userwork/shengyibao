package com.bangware.shengyibao.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bangware.shengyibao.activity.entity.Good;
import com.bangware.shengyibao.config.ViewHolder;

import java.util.ArrayList;


public class HtmlWebThirdActivity extends Activity {
	private ImageView backImage;
	private ListView lv;
	private ArrayList<Good> listItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_html_webthird);

		backImage = (ImageView) findViewById(R.id.newgoods_back);
		backImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		lv = (ListView) findViewById(R.id.listView_two);
		listItem = getDate();
		MyAdapter mAdapter = new MyAdapter(this,listItem);//得到一个MyAdapter对象
		lv.setAdapter(mAdapter);
	}

	private ArrayList<Good> getDate(){
		listItem = new ArrayList<Good>();
		Good good = new Good();
		good.setImg(R.drawable.haoyunlai);
		good.setGoodname("通用Ⅱ型瓷砖胶");
		good.setGoodsendtime("发布时间：2016/05/11");
		good.setGoodtitle("标签：多邦建材瓷砖胶通用Ⅱ型瓷砖胶");
		good.setGoodcontent("产品特点 1、粘结力较强：大大超过传统水泥砂浆、水泥净浆的粘结力。 2、施工简便快捷：无需浸砖湿墙，施工工艺的改革使得比传统旧工艺快2-3倍。 3、耐老化、抗渗性良好：产品是经改良的无机合成材料。 4、工程造...");
		listItem.add(good);

		Good good1 = new Good();
		good1.setImg(R.drawable.tongyonghui);
		good1.setGoodname("通用瓷砖胶-灰");
		good1.setGoodsendtime("发布时间：2016/05/27");
		good1.setGoodtitle("标签：多邦建材多邦瓷砖胶通用瓷砖胶-灰");
		good1.setGoodcontent("产品特点 1、粘结力较强：大大超过传统水泥砂浆、水泥净浆的粘结力。 2、施工简便快捷：无需浸砖湿墙，施工工艺的改革使得比传统旧工艺快2-3倍。 3、耐老化、抗渗性良好：产品是经改良的无机合成材料。 4、工程造...");
		listItem.add(good1);

		Good good2=new Good();
		good2.setImg(R.drawable.qiangli);
		good2.setGoodname("多邦强力瓷砖胶");
		good2.setGoodsendtime("发布时间：2016/05/27");
		good2.setGoodtitle("标签：多邦建材多邦强力瓷砖胶多邦瓷砖胶");
		good2.setGoodcontent("产品特性 (1)施工简便，无需浸砖湿墙，工效提高； (2)采用新工艺一刮齿法施工，用量少且均匀，避免出现空鼓，较传统水泥砂浆粘贴法更安全牢固； (3)具有良好的保水性能，足够时间对粘结瓷砖进行调整； (4)优良的...");
		listItem.add(good2);

		Good good3=new Good();
		good3.setImg(R.drawable.zhanyuwangzi);
		good3.setGoodname("超强瓷砖胶 章鱼王子");
		good3.setGoodsendtime("发布时间：2016/05/07");
		good3.setGoodtitle("标签：多邦建材超强瓷砖胶 章鱼王子");
		good3.setGoodcontent("产品特点 1、 粘结力较强：大大超过传统水泥砂浆、水泥净浆的强度。 2、 施工简便快捷：无需浸砖湿墙，施工工艺的改革使得比传统旧工艺快2-3倍。 3、 耐老化、抗渗性良好：产品是经改良的无机合成材料。 4、 工程...");
		listItem.add(good3);

		Good good4=new Good();
		good4.setImg(R.drawable.chuntianli);
		good4.setGoodname("春天里通用瓷砖胶");
		good4.setGoodsendtime("发布时间：2016/04/29");
		good4.setGoodtitle("标签：多邦瓷砖胶通用瓷砖胶");
		good4.setGoodcontent("产品特点 1、粘结力较强：大大超过传统水泥砂浆、水泥净浆的粘结力。 2、施工简便快捷：无需浸砖湿墙，施工工艺的改革使得比传统旧工艺快2-3倍。 3、耐老化、抗渗性良好：产品是经改良的无机合成材料。 4、工程造...");
		listItem.add(good4);

		Good good5=new Good();
		good5.setImg(R.drawable.gaodashang);
		good5.setGoodname("高大尚-强力Ⅱ型瓷砖胶");
		good5.setGoodtitle("发布时间：2016/05/27");
		good5.setGoodtitle("标签：多邦建材多邦瓷砖胶高大尚-强力Ⅱ型瓷砖胶");
		good5.setGoodcontent("产品特性 (1)施工简便，无需浸砖湿墙，工效提高； (2)采用新工艺一刮齿法施工，用量少且均匀，避免出现空鼓，较传统水泥砂浆粘贴法更安全牢固； (3)具有良好的保水性能，足够时间对粘结瓷砖进行调整； (4)优良的...");
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
				convertView = mInflater.inflate(R.layout.item_good_two, null);
			}
			ImageView goods_img = ViewHolder.get(convertView,R.id.ItemGoodsTwoImage);
			TextView goods_name = ViewHolder.get(convertView,R.id.ItemGoodsTwoName);
			TextView goods_time = ViewHolder.get(convertView,R.id.ItemGoodTwoSendTime);
			TextView goods_title = ViewHolder.get(convertView,R.id.ItemGoodTwoTitle);
			TextView goods_content = ViewHolder.get(convertView,R.id.ItemGoodTwoContent);

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
