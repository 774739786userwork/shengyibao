package com.bangware.shengyibao.net.pickpicture;

import java.util.ArrayList;

import com.bangware.shengyibao.activity.R;
import com.bangware.shengyibao.customer.adapter.CaremaAdapter;
import com.bangware.shengyibao.customer.view.ShowImageActivity;
import com.bangware.shengyibao.utils.CommonUtil;
import com.bumptech.glide.Glide;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
/**
 * 多张照片选择
 * @author ccssll
 *
 */
public class PickPictureAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> listCaremaName; //照片绝对路径
	private boolean candelete; //是否可删除
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listCaremaName.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return listCaremaName.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int index, View view, ViewGroup arg2) {
		final ViewHolder holder;
		if(view == null){
			view = LayoutInflater.from(context).inflate(R.layout.fromalum_item_picture, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView)view.findViewById(R.id.pictureImage);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		String img_url = "file://" + listCaremaName.get(index);
		Glide.with(context).load(img_url).placeholder(R.drawable.no_pic_300).error(R.drawable.no_pic_300)
				.into(holder.imageView);
					
		//点击查看详细
		holder.imageView.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(context,ShowImageActivity.class);
				intent.putStringArrayListExtra("ImageUrl",listCaremaName);
				intent.putExtra(ShowImageActivity.EXTRA_IMAGE_INDEX,index);
				context.startActivity(intent);
			}
		});

		if(candelete){
			//长按弹出操作选项
			holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
				public boolean onLongClick(View arg0) {
					Dialog dialog = new AlertDialog.Builder(context)
							.setTitle("操作选项")
							.setItems(new String[]{"删除"}, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									switch (which) {
										//删除
										case 0:
											Dialog deleteDialog = new AlertDialog.Builder(context)
													.setTitle("提示")
													.setMessage("您确定要删除吗？")
													.setPositiveButton("确定", new DialogInterface.OnClickListener() {
														public void onClick(DialogInterface dialog, int which) {
															//刷新列表
															listCaremaName.remove(index);
															PickPictureAdapter.this.notifyDataSetChanged();
														}
													})
													.setNegativeButton("取消", new DialogInterface.OnClickListener() {
														public void onClick(DialogInterface dialog, int which) {
															dialog.dismiss();
														}
													})
													.create();
											deleteDialog.show();
											break;
									}
								}
							})
							.setNegativeButton("取消", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							})
							.create();
					dialog.show();
					return true;
				}
			});
		}
		
		return view;
	}
	
	public PickPictureAdapter(Context context, ArrayList<String> listCaremaName,boolean candelete) {
		super();
		this.context = context;
		this.listCaremaName = listCaremaName;
		this.candelete = candelete;
	}

	private class ViewHolder{
		ImageView imageView;
	}

}
