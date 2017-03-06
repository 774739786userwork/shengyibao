package com.bangware.shengyibao.utils.OnScrollGridView;

import java.util.List;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.config.Model;
import com.bangware.shengyibao.practicalprojects.model.entity.MyBean;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyListAdapter extends BaseAdapter{
	private List<MyBean> mList;
	private LayoutInflater mInflater;
	private Context mContext;
	private CallBackVideo callbackvideo;
	/**
	 * 自定义回调接口
     */
	public interface CallBackVideo{
		void onClick(View item, View widget, int position, int which);
	}


	public MyListAdapter(Context context, List<MyBean> list,CallBackVideo mCallBack) {
		mInflater = LayoutInflater.from(context);
		mContext=context;
		this.mList=list;
		this.callbackvideo = mCallBack;
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
	public View getView(final int position, View convertView, final ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.list_item, null);
			holder.name=(TextView)convertView.findViewById(R.id.name);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.gridView=(NoScrollGridView)convertView.findViewById(R.id.gridView);
			holder.previewVideo_relLayout =(RelativeLayout) convertView.findViewById(R.id.preview_video_relLayout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final MyBean bean = getItem(position);

		holder.name.setText(bean.getEmployee_name());
		holder.content.setText(bean.getContent());
		if(bean.getImages().size()>0){
			holder.gridView.setVisibility(View.VISIBLE);
			holder.previewVideo_relLayout.setVisibility(View.GONE);
			holder.gridView.setAdapter(new MyGridAdapter(bean.getImages(),mContext));
			holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					imageBrower(position,bean);
				}
			});
		}else{
			holder.gridView.setVisibility(View.GONE);
			holder.previewVideo_relLayout.setVisibility(View.VISIBLE);
			/**
			 * 设置播放点击事件
			 */
			final int pos = position;
			final View view = convertView;
			final int v_id = holder.previewVideo_relLayout.getId();
			holder.previewVideo_relLayout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					callbackvideo.onClick(view,parent,pos,v_id);
				}
			});
		}
		return convertView;
	}

	private static class ViewHolder {

		public  TextView name;
		private TextView content;
		private NoScrollGridView gridView;
		private RelativeLayout previewVideo_relLayout;
	}

	private void imageBrower(int position, MyBean myBean) {
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, myBean);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mContext.startActivity(intent);
	}
}
