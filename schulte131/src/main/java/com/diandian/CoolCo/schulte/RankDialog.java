package com.diandian.CoolCo.schulte;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RankDialog extends Dialog{
	
	private MainActivity activity;
	private long record;

	public RankDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public RankDialog(Context context, MainActivity mainActivity, long time) {
		// TODO Auto-generated method stub
		super(context, R.style.AssessDialogTheme);
		this.activity = mainActivity;
		setContentView(R.layout.dialog_rank);
		
		Button btn_restart = (Button)findViewById(R.id.btn_restart);
		ImageView iv_icon = (ImageView)findViewById(R.id.iv_icon);
		Button btn_return  = (Button)findViewById(R.id.btn_return);
		TextView tv_time = (TextView) findViewById(R.id.tv_time);
		TextView tv_record = (TextView) findViewById(R.id.tv_record);
		
		this.record = getRecord();
		if (time < record) {
			iv_icon.setImageResource(R.drawable.ic_good);
			if (record == 1000000) {
				tv_time.setText("首开纪录"+numToString(time));
				tv_record.setText("");
			} else {
				tv_time.setText("刷新纪录"+numToString(time));
				tv_record.setText("(原纪录"+numToString(record)+")");
			}
			setRecord(time);
		} else {
			iv_icon.setImageResource(R.drawable.ic_info);
			tv_time.setText(numToString(time));
			tv_record.setText("(最快纪录:"+numToString(record)+")");
		}
		
		btn_restart.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				dismiss();
				RankDialog.this.activity.start();
			}
		});
		btn_return.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				dismiss();
				RankDialog.this.activity.finish();
			}
		});
		
		Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/HKHTW3.TTF");
		btn_restart.setTypeface(face);
		btn_return.setTypeface(face);
		tv_time.setTypeface(face);
		tv_record.setTypeface(face);
		
	    Window window = getWindow();
//	    window.setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
//	    		WindowManager.LayoutParams.FLAG_DIM_BEHIND);

	    WindowManager.LayoutParams params = window.getAttributes();
	    params.gravity = Gravity.CENTER;
//	    params.dimAmount = 0.3f;
	    window.setAttributes(params);
	}
	
	public long getRecord() {
		SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(getContext());
		int row = Integer.valueOf(setting.getString("scale", "4"));
		boolean flag_disappear = setting.getBoolean("disappear", true);
		String record_name = "record_"+String.valueOf(row)+"_"+String.valueOf(flag_disappear);
		long record = Integer.valueOf(setting.getString(record_name, "1000000"));
		return record;
	}
	
	public void setRecord(long record) {
		this.record = record;
		SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(getContext());
		Editor editor = setting.edit();
		int row = Integer.valueOf(setting.getString("scale", "4"));
		boolean flag_disappear = setting.getBoolean("disappear", true);
		String recordName = "record_"+String.valueOf(row)+"_"+String.valueOf(flag_disappear);
		editor.putString(recordName, String.valueOf(record));
		editor.commit();
	}

	public String numToString(long num) {
		String ans = String.valueOf(num);
		int len = ans.length();
		if (len > 3) {
			ans = ans.substring(0, len-3) + "," + ans.substring(len-3);
		}
		ans += "MS";
		return ans;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			dismiss();
			activity.start();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	
}
