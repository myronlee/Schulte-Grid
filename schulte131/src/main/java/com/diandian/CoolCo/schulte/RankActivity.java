package com.diandian.CoolCo.schulte;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;
import android.view.View;

public class RankActivity extends FragmentActivity {
	
	private View mTabLocalRank;
	private View mTabWebRank;
	private View[] mTabViews;
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rank);
		
		mTabViews = new View[2];
		mTabViews[0] = findViewById(R.id.tab_local_rank);
		mTabViews[1] = findViewById(R.id.tab_web_rank);
//		mTabLocalRank = findViewById(R.id.tab_local_rank);
//		mTabWebRank = findViewById(R.id.tab_web_rank);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		
		mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), this));
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int index) {
				// TODO Auto-generated method stub
//				mTabViews[index].setVisibility(View.VISIBLE);
//				mTabViews[(index+1)%2].setVisibility(View.INVISIBLE);
			}
			
			@Override
			public void onPageScrolled(int index, float percent, int position) {
				// TODO Auto-generated method stub
				//mTabViews[index].setTranslationX(position/2);
//				Toast.makeText(getBaseContext(), String.valueOf(index), Toast.LENGTH_SHORT).show();
//				Toast.makeText(getBaseContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
//				Log.e("ddddddddd", String.valueOf(position));
//				if (0 == index) {
//					mTabViews[index].setTranslationX(position);
//				} else {
//					mTabViews[(index+1)%2].setTranslationX(-position);
//					
//				}
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
    public class MyPagerAdapter extends FragmentPagerAdapter {  

//		LocalRankFragment mLocalRankFragment; 
//		WebRankFragment mWebRankFragment;
		Fragment[] fragments;
        String[] titles;  
  
        public MyPagerAdapter(FragmentManager fm, Context context) {  
            super(fm);  
            fragments = new Fragment[7];
            fragments[0] = new LocalRankFragment();  
            fragments[1] = new WebRankFragment();
            fragments[2] = new LocalRankFragment();  
            fragments[3] = new LocalRankFragment();  
            fragments[4] = new WebRankFragment();
            fragments[5] = new LocalRankFragment();  
            fragments[6] = new LocalRankFragment();  
//            mLocalRankFragment = new LocalRankFragment();  
//            mWebRankFragment = new WebRankFragment();  
  
            titles = new String[7];  
            titles[0] = "星期一";  
            titles[1] = "星期二"; 
            titles[2] = "星期三";
            titles[3] = "星期四";  
            titles[4] = "星期五"; 
            titles[5] = "星期六";
            titles[6] = "星期日";  
        }  
  
        @Override  
        public Fragment getItem(int position) {  
//            if (0 == position) {
//				return mLocalRankFragment;
//			} else {
//				return mWebRankFragment;
//			}  
        	return fragments[position];
        }  
  
        @Override  
        public int getCount() {  
            return fragments.length;  
        }  
  
        @Override  
        public String getPageTitle(int position) {  
            return titles[position];  
        }  
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
        case android.R.id.home:
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                // This activity is NOT part of this app's task, so create a new task
                // when navigating up, with a synthesized back stack.
                TaskStackBuilder.create(this)
                        // Add all of this activity's parents to the back stack
                        .addNextIntentWithParentStack(upIntent)
                        // Navigate up to the closest parent
                        .startActivities();
            } else {
                // This activity is part of this app's task, so simply
                // navigate up to the logical parent activity.
                NavUtils.navigateUpTo(this, upIntent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
