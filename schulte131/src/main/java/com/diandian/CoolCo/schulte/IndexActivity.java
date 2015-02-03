package com.diandian.CoolCo.schulte;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class IndexActivity extends FragmentActivity {

	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), this));
	}

	public static class IndexFragment extends Fragment {

		public IndexFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.frament_start, container, false);
			rootView.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivity(new Intent(getActivity(), MainActivity.class));
				}
			});
			return rootView;
		}
	}

	public class PagerAdapter extends FragmentPagerAdapter {

		Fragment indexFragment;
		PreferenceFragment preferenceFragment;
		String[] titles;

		public PagerAdapter(FragmentManager fragmentManager, Context context) {
			super(fragmentManager);

			indexFragment = new IndexFragment();
			
			preferenceFragment = new PreferenceFragment(R.xml.preference);
			
			titles = new String[2];
			titles[0] = context.getString(R.string.index);
			titles[1] = context.getString(R.string.preference);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				return indexFragment;
			} else {
				return preferenceFragment;
			}
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public String getPageTitle(int position) {
			return titles[position];
		}
	}
}
