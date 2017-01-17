package com.bangware.shengyibao.utils.OnScrollGridView;

import java.util.List;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.practicalprojects.model.entity.MyBean;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyListAdapter extends BaseAdapter{

	private List<MyBean> mList;
	private LayoutInflater mInflater;
	private Context mContext;
	
	public MyListAdapter(Context context, List<MyBean> list) {
		mInflater = LayoutInflater.from(context);
		mContext=context;
		this.mList=list;
	}

	@Override
	public int getCount() {
		return mList==null?0:mList.size();
	}

	@Override
	public MyBean getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.list_item, null);
			holder.name=(TextView)convertView.findViewById(R.id.name);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.gridView=(NoScrollGridView)convertView.findViewById(R.id.gridView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final MyBean bean = getItem(position);

		holder.name.setText(bean.getEmployee_name());
		holder.content.setText(bean.getContent());
		if(bean.getImages().size()>0){
			holder.gridView.setVisibility(View.VISIBLE);
			holder.gridView.setAdapter(new MyGridAdapter(bean.getImages(),mContext));
			holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					imageBrower(position,bean);
				}
			});
		}else{
			holder.gridView.setVisibility(View.GONE);
		}
		return convertView;
	}
	private static class ViewHolder {

		public TextView name;
		TextView content;
		NoScrollGridView gridView;
	}

	private void imageBrower(int position, MyBean myBean) {
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, myBean);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mContext.startActivity(intent);
	}
}
