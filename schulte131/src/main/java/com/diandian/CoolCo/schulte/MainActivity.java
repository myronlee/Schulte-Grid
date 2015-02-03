package com.diandian.CoolCo.schulte;


import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements OnClickListener {

	private int scale;
	private boolean flag_disappear;
	private boolean flag_vibrate;
	private boolean flag_sound;
	private boolean flag_promt;
	private boolean flagCount;
	private Button btns[][];
	private AbsoluteLayout btns_layout;
	private int screen_width;
	private int screen_height;
	private Vibrator vibrator;
	private int prs_num;
	private long start_time;
	private int num_seq[];
	private float density;
	private TextView next_num;
	private SoundPool sp;
	private int sound;

	private ViewFlipper mViewFlipper;
	private View mCountView;
	private View mGameView;

	private ImageView mIvCount;

	private Handler mHandler = new Handler();

	private Runnable Runnable2 = new Runnable() {
		@Override
		public void run() {
//			mIvCount.setImageDrawable(getResources().getDrawable(
//					R.drawable.ic_count_2));
			mIvCount.setImageResource(R.drawable.ic_count_2);
			mHandler.postDelayed(Runnable1, 1000);
		}
	};

	private Runnable Runnable1 = new Runnable() {
		@Override
		public void run() {
//			mIvCount.setImageDrawable(getResources().getDrawable(
//					R.drawable.ic_count_1));
			mIvCount.setImageResource(R.drawable.ic_count_1);
			mHandler.postDelayed(RunnableGo, 1000);
		}
	};

	private Runnable RunnableGo = new Runnable() {
		@Override
		public void run() {
//			mIvCount.setImageDrawable(getResources().getDrawable(
//					R.drawable.ic_count_go));
			mIvCount.setImageResource(R.drawable.ic_count_go);
			mHandler.postDelayed(RunnableGame, 1000);
		}
	};

	private Runnable RunnableGame = new Runnable() {
		@Override
		public void run() {
			startGame();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		sound = sp.load(this, R.raw.effect_tick, 1);

		DisplayMetrics dm = getResources().getDisplayMetrics();
		density = dm.density;
		screen_width = dm.widthPixels;
		screen_height = dm.heightPixels;

		btns = new Button[5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				btns[i][j] = new Button(this);
			}
		}

		setContentView(R.layout.activity_main);
		mViewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
		LayoutInflater inflater = getLayoutInflater();
		mCountView = inflater.inflate(R.layout.count, null);
		mGameView = inflater.inflate(R.layout.game, null);
		mViewFlipper.addView(mCountView, 0);
		mViewFlipper.addView(mGameView, 1);
		
		Animation inAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		Animation outAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		mViewFlipper.setInAnimation(inAnimation);
		mViewFlipper.setOutAnimation(outAnimation);

		start();
	}

	private void createNumView() {
		// TODO Auto-generated method stub

		next_num = (TextView) findViewById(R.id.next_num_tv);
		next_num.setTextSize((float) (screen_width / 2 / density * 1.5));
		Typeface next_num_face = Typeface.createFromAsset(getBaseContext()
				.getAssets(), "fonts/Roboto-MediumItalic.ttf");
		next_num.setTypeface(next_num_face);

		btns_layout = (AbsoluteLayout) findViewById(R.id.nums_layout);
		btns_layout.removeAllViews();

		float grid_width = screen_width / scale;
		float rect_width = (float) (grid_width * 0.9);
		float numSize = (float) (screen_width / scale / 4 / density * 1.5);
		float offset_y = (screen_height - screen_width) / 2;

		Typeface face = Typeface.createFromAsset(getBaseContext().getAssets(),
				"fonts/Roboto-Light.ttf");
		int numColor = getResources().getColor(R.color.num);
		LayoutParams layoutParams;

		for (int i = 0; i < scale; i++) {
			for (int j = 0; j < scale; j++) {
				btns[i][j].setTypeface(face);
				btns[i][j].setTextSize(numSize);
				btns[i][j].setTextColor(numColor);
				btns[i][j].setOnClickListener(this);
				btns[i][j].setBackgroundResource(R.drawable.num_btn_bg_selector);
				
				layoutParams = new LayoutParams((int) rect_width, (int) rect_width, (int) (j * grid_width + grid_width*0.05), (int) (offset_y + i * grid_width));
				if (btns[i][j].getParent() != null) {
					ViewGroup vg = (ViewGroup) (btns[i][j].getParent());
					vg.removeView(btns[i][j]);
				}
				btns_layout.addView(btns[i][j], layoutParams);
			}
		}
	}

	private void setRanSeq() {
		// TODO Auto-generated method stub
		for (int i = 0; i < scale; i++) {
			for (int j = 0; j < scale; j++) {
				btns[i][j].setText(String.valueOf(num_seq[i * scale + j]));
				btns[i][j].setVisibility(View.VISIBLE);
			}
		}
	}

	public void genRanSeq() {
		num_seq = new int[scale * scale];
		boolean has_generated[] = new boolean[scale * scale + 1];
		Random rand = new Random();
		int randomNum;
		int cnt = 0;
		while (cnt < scale * scale) {
			randomNum = rand.nextInt(scale * scale) + 1;
			if (!has_generated[randomNum]) {
				num_seq[cnt++] = randomNum;
				has_generated[randomNum] = true;
			}
		}
	}

	public void loadPreference() {

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		scale = Integer.valueOf(settings.getString("scale", "4"));
		flag_disappear = settings.getBoolean("disappear", true);
		flag_vibrate = settings.getBoolean("vibrate", true);
		flag_sound = settings.getBoolean("sound", true);
		flag_promt = settings.getBoolean("promt", true);
		flagCount = settings.getBoolean("tick", true);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (flag_vibrate) {
			vibrator.vibrate(50);
		}
		if (flag_sound) {
			sp.play(sound, 1, 1, 0, 0, 1);
		}

		if (((Button) view).getText().equals(String.valueOf(prs_num))) {
			next_num.setText("");
			if (flag_disappear) {
				view.setVisibility(View.INVISIBLE);
			}
			prs_num++;
			if (prs_num > scale * scale) {
				long time = System.currentTimeMillis() - start_time;
				Intent intent = new Intent(this, RankActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
//				RankDialog rankDialog = new RankDialog(this, this, time);
//				rankDialog.show();
			}
		} else {
			if (flag_promt) {
				next_num.setText(String.valueOf(prs_num));
			}
		}
	}


	public void start() {
		loadPreference();
		if (flagCount) {
			startCount();
		} else {
			startGame();
		}
	}

	private void startCount() {
//		setContentView(R.layout.count);
		mViewFlipper.setDisplayedChild(0);
		mIvCount = (ImageView) findViewById(R.id.iv_count);
//		mIvCount.setImageDrawable(getResources().getDrawable(
//				R.drawable.ic_count_3));
		mIvCount.setImageResource(R.drawable.ic_count_3);
		mHandler.postDelayed(Runnable2, 1000);
	}

	public void startGame() {
//		setContentView(R.layout.activity_main);
		mViewFlipper.setDisplayedChild(1);
		createNumView();
		genRanSeq();
		setRanSeq();
		prs_num = 1;
		start_time = System.currentTimeMillis();
	}

}
