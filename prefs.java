/*
package co.in.drh.Tokens;

public class prefs
{
}
*/
package co.in.drh.Tokens;
import android.preference.*;
import android.content.*;
import android.widget.*;
import android.os.*;
import java.util.*;
import android.util.*;
import android.app.*;
import android.view.View.*;
import android.view.*;
import java.util.zip.*;
import java.lang.annotation.*;
import java.nio.*;
import android.widget.Magnifier.*;
import java.util.prefs.*;

public class prefs extends PreferenceActivity
{
	private static final int TOKEN_START_TIME_ID=0,TOKEN_CLOSE_TIME_ID=1,OP_START_TIME_ID=2,OP_CLOSE_TIME_ID=3;
    public static final String OPTIONS="OPTIONS";
	Context context;
	String key,Pkey="";
	Preference pref=null;
	TimePicker tp;
	DatePicker dp;
	Preference token_starting_time,token_stop_time,op_start_time,
			   op_close_time,wkly_holiday,second_holiday,tokens,tag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		/*TOKEN_START_TIME = getString(R.string.token_starting_time_summary);
		TOKEN_CLOSE_TIME = getString(R.string.token_stop_time_summary);
		OP_START_TIME = getString(R.string.op_start_time_summary);
		OP_CLOSE_TIME = getString(R.string.op_close_time_summary);*/
		super.onCreate(savedInstanceState);
		Log.e("TOKENS", "in Tokens prefs onCreate()"); 
		context = getApplicationContext();
		addPreferencesFromResource(R.xml.prefs);
		wkly_holiday=getPreferenceManager().findPreference(getString(R.string.holiday_key));
		getSummary(R.string.holiday_key, wkly_holiday);
		second_holiday=getPreferenceManager().findPreference(getString(R.string.holiday_extra_key));
		getSummary(R.string.holiday_extra_key, second_holiday);
		tokens=getPreferenceManager().findPreference(getString(R.string.tokens_key));
		getSummary(R.string.tokens_key, tokens);
		
	    token_starting_time = getPreferenceManager().findPreference(getString(R.string.token_starting_time_key));
		getSummary(R.string.token_starting_time_key,token_starting_time);
        assert token_starting_time != null;
  		token_stop_time = getPreferenceManager().findPreference(getString(R.string.token_stop_time_key));
        assert token_stop_time != null;
		getSummary(R.string.token_stop_time_key,token_stop_time);
		op_start_time = getPreferenceManager().findPreference(getString(R.string.op_start_key));
        assert op_start_time != null;
		getSummary(R.string.op_start_key,op_start_time);
		op_close_time = getPreferenceManager().findPreference(getString(R.string.op_close_key));
        assert op_close_time != null;
		getSummary(R.string.op_close_key,op_close_time);
		Preference.OnPreferenceChangeListener pcl =new Preference.OnPreferenceChangeListener(){

			@Override
			public boolean onPreferenceChange(Preference p1, Object p2)
			{
				new Tokens().l("public boolean onPreferenceChange(Preference p1, Object p2) "+p1+"  "+p2+" p1.getkey  "+p1.getKey());
				Toast.makeText(context,".setOnPreferenceChangeListener(pcl);  "+p1+"  Object  "+p2,Toast.LENGTH_LONG).show();
				getSummary(100,p1);
				return false;
			}
		};
		Preference.OnPreferenceClickListener timePicker = new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(final Preference preference)
			{
			//	 final Preference pref = preference;
			
				if (preference.getKey() == getString(R.string.token_starting_time_key))showDialog(TOKEN_START_TIME_ID);
				if (preference.getKey() == getString(R.string.token_stop_time_key))showDialog(TOKEN_CLOSE_TIME_ID);
				if (preference.getKey() == getString(R.string.op_start_key))showDialog(OP_START_TIME_ID);
				if (preference.getKey() == getString(R.string.op_close_key))showDialog(OP_CLOSE_TIME_ID);

				return false;}};


		token_starting_time.setOnPreferenceClickListener(timePicker);
		token_stop_time.setOnPreferenceClickListener(timePicker);
		op_start_time.setOnPreferenceClickListener(timePicker);
		op_close_time.setOnPreferenceClickListener(timePicker);
		 String tag = getPrefValue(getString(R.string.tag_key));
		 Preference prefTag=getPreferenceManager().
		 findPreference(getString(R.string.tag_key));
		prefTag.setSummary(getString(R.string.tag_summary)+"   "+ tag);
		
		/*
		wkly_holiday.setOnPreferenceChangeListener(pcl);
		second_holiday.setOnPreferenceChangeListener(pcl);
		token_starting_time.setOnPreferenceChangeListener(pcl);
		token_stop_time.setOnPreferenceChangeListener(pcl);
		prefTag.setOnPreferenceChangeListener(pcl);
		op_start_time.setOnPreferenceChangeListener(pcl);
		op_close_time.setOnPreferenceChangeListener(pcl);
		*/
	}
	
	
	
	
	void getSummary(int prf,Preference prfr){
		
		String sum=prfr.getSummary().toString();
		//String val = prfr. getSharedPreferences().getString(prfr.getKey(),"000")
		String val = prfr. getSharedPreferences().getString(getString(prf),"000");
		new Tokens().l("Preference prfr  "+prfr+ "  val   "+val+" key "+prfr.getKey()+"  "+getString( R.string.holiday_key));
		if(val.equals("000")){val="1";}
		if(prf==R.string.holiday_key|prf==R.string.holiday_extra_key){
	//	if(prfr.getKey()==getString( R.string.holiday_key)|prfr.getKey()==getString( R.string.holiday_extra_key)){
		val =	getResources().getStringArray(R.array.weekdays)[Integer.parseInt(val)-1];
		}
		prfr.setSummary(sum+"      ("+val+")");
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		String title="",summary="",message="";
		int h,m;
	//	Preference pref;
		switch (id)
		{
			case TOKEN_START_TIME_ID:
				title = getString(R.string.token_starting_time_title);
				message = getString(R.string.token_starting_time_summary);
				Pkey = getString(R.string.token_starting_time_key);
				pref=token_starting_time;
				break;
			case TOKEN_CLOSE_TIME_ID:
				title = getString(R.string.token_stop_time_title);
				message = getString(R.string.token_stop_time_summary);
				Pkey = getString(R.string.token_stop_time_key);
				pref=token_stop_time;

				break;
			case OP_START_TIME_ID:
				title = getString(R.string.op_start_time_title);
				message = getString(R.string.op_start_time_summary);
				Pkey = getString(R.string.op_start_key);
				pref=op_start_time;

				break;
			case OP_CLOSE_TIME_ID:
				title = getString(R.string.op_close_time_title);
				message = getString(R.string.op_close_time_summary);
				Pkey = getString(R.string.op_close_key);
				pref=op_close_time;

				break;

		}

	//	Calendar cal = Calendar.getInstance();
		Toast.makeText(getApplicationContext(), "++++>>>>>>>-=++++++-+-+ PREFERECE CLICKED <<<<<<<<<<<<<<+++",2000).show();
		final AlertDialog.Builder dialog=new AlertDialog.Builder(prefs.this);
		dialog.setTitle(title);
		dialog.setMessage(message);
		LayoutInflater inflater=getLayoutInflater();
		View v=inflater.inflate(R.layout.prefs_time_picker, null);
		dialog.setView(v);
		tp = v.findViewById(R.id.prefstimepickerTimePicker1);
	//	dp = v.findViewById(R.id.prefstimepickerDatePicker1);
		String tm= PreferenceManager.getDefaultSharedPreferences(context).
			getString(Pkey, "0000");
		new Tokens().l("+++++>>>>>>> in prefs tm "+Pkey+"  "+tm);
		//	tm="471";
		if(tm.contains(":"))
		{h=Integer.parseInt(tm.substring(tm.indexOf(":") + 1));
		 m=Integer.parseInt(tm.substring(0,tm.indexOf(":")));}
		else{
			int tmi=Integer.parseInt(tm);
			h=tmi/60;
			m=tmi%60;
			}
			new Tokens().l("h and m  "+h+":"+m);
	//	String storedH=(":")),storedM=tm.substring(tm.indexOf(":") + 1);
		tp.setHour(h);//  (Integer.parseInt(storedH));
		tp.setMinute(m);//Integer.parseInt(storedM));
		dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					Log.d("Tokens", "p1   " + p1 + "  p2    " + p2);
					p1.dismiss();
				}
			});
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					int h = tp.getHour();
					int m = tp.getMinute();
					String time=String.valueOf(h) + ":" + String.valueOf(m);
					new Tokens().l("===============>>>>>>> "+time);
					Toast.makeText(getApplicationContext(), time, 2000).show();
				/*	PreferenceManager.getDefaultSharedPreferences(context).edit().
					putString(Pkey,String.valueOf((h*60)+m)).apply();
				*/
					pref.setDefaultValue(time);
				//	pref.putString(Pkey, time).apply();//String.valueOf(h)+":"+String.valueOf(m));
					putPrefValue(Pkey,time);
					p1.dismiss();
				}
			});
		dialog.create().show();

		return super.onCreateDialog(id);

	}
	public String getPrefValue(String key){
       return PreferenceManager.getDefaultSharedPreferences(context)
	   .getString(key,null);
	}
	
   public void putPrefValue(String key,String value){
	   PreferenceManager.getDefaultSharedPreferences(context).edit()
		 .putString(key,value).commit();
   }
	
}



