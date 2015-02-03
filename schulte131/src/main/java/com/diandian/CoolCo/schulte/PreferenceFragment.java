package com.diandian.CoolCo.schulte;


import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Date;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListView;



@SuppressLint("ValidFragment")
public class PreferenceFragment extends ListFragment {  
	  
    private PreferenceManager mPreferenceManager;  
  
    /** 
     * The starting request code given out to preference framework. 
     */  
    private static final int FIRST_REQUEST_CODE = 100;  
    private static final int MSG_BIND_PREFERENCES = 0;  
  
    private ListView lv;  
    private int xmlId;  
  
    @SuppressLint("HandlerLeak") private Handler mHandler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case MSG_BIND_PREFERENCES:  
                bindPreferences();  
                break;  
            }  
        }  
    };  
  
    // must be provided  
     public PreferenceFragment() {  
      
     }  
  
    public PreferenceFragment(int xmlId) {  
        this.xmlId = xmlId;  
    }  
  
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle b) {  
        postBindPreferences();  
        return lv;  
    }  
  
    @Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

        Preference user = findPreference("user");
        if (user != null) {
        	Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(getActivity());
        	Long expireTime = token.getExpiresTime();
        	Resources res = getResources();
        	if (0 == expireTime) {
        		user.setTitle(res.getString(R.string.have_not_login));
			} else if (System.currentTimeMillis() >= expireTime) {
				user.setTitle(res.getString(R.string.expired));
			} else {
				user.setTitle(res.getString(R.string.have_login));
			}
        	
		}
	}

	@Override  
    public void onDestroyView() {  
        super.onDestroyView();  
        ViewParent p = lv.getParent();  
        if (p != null) {  
            ((ViewGroup) p).removeView(lv);  
        }  
    }  
  
    @Override  
    public void onCreate(Bundle b) {  
        super.onCreate(b);  
        if (b != null) {  
            xmlId = b.getInt("xml");  
        }  
        mPreferenceManager = onCreatePreferenceManager();  
        lv = (ListView) LayoutInflater.from(getActivity()).inflate(  
                R.layout.preference_list_content, null);  
        lv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);  
        addPreferencesFromResource(xmlId);  
        postBindPreferences();  
        
    }  
  
    @Override  
    public void onStop() {  
        super.onStop();  
        try {  
            Method method = PreferenceManager.class  
                    .getDeclaredMethod("dispatchActivityStop");  
            method.setAccessible(true);  
            method.invoke(mPreferenceManager);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    @Override  
    public void onDestroy() {  
        super.onDestroy();  
        lv = null;  
        try {  
            Method method = PreferenceManager.class  
                    .getDeclaredMethod("dispatchActivityDestroy");  
            method.setAccessible(true);  
            method.invoke(mPreferenceManager);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    @Override  
    public void onSaveInstanceState(Bundle outState) {  
        outState.putInt("xml", xmlId);  
        super.onSaveInstanceState(outState);  
  
    }  
  
    @Override  
    public void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        try {  
            Method method = PreferenceManager.class.getDeclaredMethod(  
                    "dispatchActivityResult", int.class, int.class,  
                    Intent.class);  
            method.setAccessible(true);  
            method.invoke(mPreferenceManager, requestCode, resultCode, data);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * Posts a message to bind the preferences to the list view. 
     * <p> 
     * Binding late is preferred as any custom preference types created in 
     * {@link #onCreate(Bundle)} are able to have their views recycled. 
     */  
    private void postBindPreferences() {  
        if (mHandler.hasMessages(MSG_BIND_PREFERENCES)) {  
            return;  
        }  
        mHandler.obtainMessage(MSG_BIND_PREFERENCES).sendToTarget();  
    }  
  
    private void bindPreferences() {  
        final PreferenceScreen preferenceScreen = getPreferenceScreen();  
        if (preferenceScreen != null) {  
            preferenceScreen.bind(lv);  
        }  
    }  
  
    /** 
     * Creates the {@link PreferenceManager}. 
     *  
     * @return The {@link PreferenceManager} used by this activity. 
     */  
    private PreferenceManager onCreatePreferenceManager() {  
        try {  
            Constructor<PreferenceManager> c = PreferenceManager.class  
                    .getDeclaredConstructor(Activity.class, int.class);  
            c.setAccessible(true);  
            PreferenceManager preferenceManager = c.newInstance(  
                    this.getActivity(), FIRST_REQUEST_CODE);  
            return preferenceManager;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
  
    /** 
     * Returns the {@link PreferenceManager} used by this activity. 
     *  
     * @return The {@link PreferenceManager}. 
     */  
    public PreferenceManager getPreferenceManager() {  
        return mPreferenceManager;  
    }  
  
    /** 
     * Sets the root of the preference hierarchy that this activity is showing. 
     *  
     * @param preferenceScreen 
     *            The root {@link PreferenceScreen} of the preference hierarchy. 
     */  
    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {  
        try {  
            Method method = PreferenceManager.class.getDeclaredMethod(  
                    "setPreferences", PreferenceScreen.class);  
            method.setAccessible(true);  
            boolean result = (Boolean) method.invoke(mPreferenceManager,  
                    preferenceScreen);  
            if (result && preferenceScreen != null) {  
                postBindPreferences();  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * Gets the root of the preference hierarchy that this activity is showing. 
     *  
     * @return The {@link PreferenceScreen} that is the root of the preference 
     *         hierarchy. 
     */  
    public PreferenceScreen getPreferenceScreen() {  
        try {  
            Method method = PreferenceManager.class  
                    .getDeclaredMethod("getPreferenceScreen");  
            method.setAccessible(true);  
            return (PreferenceScreen) method.invoke(mPreferenceManager);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
  
    /** 
     * Adds preferences from activities that match the given {@link Intent}. 
     *  
     * @param intent 
     *            The {@link Intent} to query activities. 
     */  
    public void addPreferencesFromIntent(Intent intent) {  
        throw new RuntimeException("too lazy to include this bs");  
    }  
  
    /** 
     * Inflates the given XML resource and adds the preference hierarchy to the 
     * current preference hierarchy. 
     *  
     * @param preferencesResId 
     *            The XML resource ID to inflate. 
     */  
    public void addPreferencesFromResource(int preferencesResId) {  
        try {  
            Method method = PreferenceManager.class.getDeclaredMethod(  
                    "inflateFromResource", Context.class, int.class,  
                    PreferenceScreen.class);  
            method.setAccessible(true);  
            PreferenceScreen prefScreen = (PreferenceScreen) method.invoke(  
                    mPreferenceManager, getActivity(), preferencesResId,  
                    getPreferenceScreen());  
            setPreferenceScreen(prefScreen);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * Finds a {@link Preference} based on its key. 
     *  
     * @param key 
     *            The key of the preference to retrieve. 
     * @return The {@link Preference} with the key, or null. 
     * @see PreferenceGroup#findPreference(CharSequence) 
     */  
    public Preference findPreference(CharSequence key) {  
        if (mPreferenceManager == null) {  
            return null;  
        }  
        return mPreferenceManager.findPreference(key);  
    }  
  
    public interface OnPreferenceAttachedListener {  
        public void onPreferenceAttached(PreferenceScreen root, int xmlId);  
    }  
  
}
