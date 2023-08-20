/*
package co.in.drh.Tokens;

public class sendSms
{
}
*/
package co.in.drh.Tokens;
import android.app.*;
import android.content.*;
import android.os.*;
import android.telephony.*;
import android.util.*;
import android.widget.*;
import com.android.internal.telephony.*;
import java.lang.reflect.*;
import android.view.*;
import android.hardware.input.*;
import android.view.inputmethod.*;
import android.provider.*;
import android.media.*;
import java.util.*;
import android.telecom.*;
import android.content.pm.*;
import android.support.v4.content.ContextCompat;
import android.support.annotation.*;

public class sendSms extends Activity
{

	private static final String TAG="TOKENS",SOURCE="SOURCE",
	PHONE_CALL="PHONE_CALL",NAME="NAME",PHONE_NUMBER="PHONE_NUMBER";
	String name,number,icn;
	EditText etName,etPhoneNumber;

	private static final int ANSWER_CALLS = 2020,PHONE_STATE=1010;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ss); //send_sms);
		l("in SendSms onCreate()");
		etName=findViewById(R.id.sendsmsEditTextName);
		etPhoneNumber=findViewById(R.id.sendsmsEditTextPhoneNumber);
		SharedPreferences pref=getSharedPreferences("phoneNumber",MODE_PRIVATE);
		etPhoneNumber.setText( pref.getString("icn",icn));
		Toast.makeText(getBaseContext(),"in SendSms onCreate()",Toast.LENGTH_SHORT).show();
		if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.MODIFY_PHONE_STATE)==PackageManager.PERMISSION_DENIED){
			l("android.Manifest.permission.MODIFY_PHONE_STATE NOT GRANTED");
			requestPermissions(new String[]{android.Manifest.permission.MODIFY_PHONE_STATE},9090);
		}else{l("android.Manifest.permission.MODIFY_PHONE_STATE GRANTED");}
		
		Intent incomingIntent=getIntent();
		if(incomingIntent.getStringExtra(SOURCE)!=null){l(SOURCE+"  not null ");
		if(incomingIntent.getStringExtra(SOURCE).equals(PHONE_CALL)){
			name=incomingIntent.getStringExtra(NAME);
			number=incomingIntent.getStringExtra(PHONE_NUMBER);
			l(name+"  "+number);
			etPhoneNumber.setText(number);
			etName.setText(name);
		}
		}
	//  findViewById(R.id.sendsmsTextViewEndCall).setBackground(getDrawable(R.drawable.buttonshape1));
	//	findViewById(R.id.sendsmsTextViewSendSms).setBackground(getDrawable(R.drawable.buttonshape1));
	//	findViewById(R.id.sendsmsTextViewTokenNumber).setBackground(getDrawable(R.drawable.buttonshape1));
	}

	@Override
	protected void onDestroy()
	{
		l("in SendSms onDestroy()");
		super.onDestroy();
	}

	@Override
	protected void onPause()
	{
		l("in SendSms onPause()");
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		l("in SendSms onResume()");
		
		Toast.makeText(getBaseContext(),"in SendSms onResume()",Toast.LENGTH_SHORT).show();
		super.onResume();
	}
	
	void l(Object o){Log.i(TAG, o.toString());}
	
	public void ssocl(View v){
		l("in SendSms ssocl");
		Toast.makeText(getBaseContext(),"in SendSms ssocl",Toast.LENGTH_SHORT).show(); 
	//	new Tokens().t("in SendSms ssocl");
	//	new Tokens().l("in SendSms ssocl");
		if(v.getId()==R.id.sendsmsTextViewEndCall){endConnection(); finish();}
		if(v.getId()==R.id.sendsmsEditText2){
			showKeyBoard();
			findViewById(R.id.sendsmsEditTextPhoneNumber).requestFocus();
			}
		if(v.getId()==R.id.sendsmsEditText1){
			showKeyBoard();
				findViewById(R.id.sendsmsEditTextName).requestFocus();
			showKeyBoard();	}
		if(v.getId()==R.id.sendsmsTextViewSendSms){
			l("in SendSms sendSms Clicked");
			manualSms();
		if(v.getId()==R.id.sendsmsTextViewTokenNumber){
			l("----------------in sendSms ssocl sendsmsTextViewTokenNumber clicked");
			answerCall();
		}
		}
	}

	private void manualSms()
	{
		l("in sendSms manualSms()");
		Intent sendSmsIntent=new Intent(this, Intentservice.class);
		sendSmsIntent.putExtra("Source", "manual");
		sendSmsIntent.putExtra("Address",
							   ((EditText)findViewById(R.id.sendsmsEditTextPhoneNumber)).
							   getText().toString());
		sendSmsIntent.putExtra("Name", "APM " +
							   ((EditText)findViewById(R.id.sendsmsEditTextName)).
							   getText().toString());
		startService(sendSmsIntent);
	}
	
	public void endConnection(){
		cutTheCall();
		/*
	//	Intent killTriger=new Intent("KILL");
	//	sendBroadcast(killTriger);
	//	Toast.makeText(con,ts+" endConnection()",500).show();
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		//Java Reflections
		Class c = null;
		try {
			c = Class.forName(telephonyManager.getClass().getName());
			l("in endCall Class c  "+c);
			Method m = null;
			java.lang.reflect.Method[] metods= c.getDeclaredMethods();
			l(metods);
			m = c.getDeclaredMethod("getITelephony");
			l("in endCall method m  "+m);
			m.setAccessible(true);
			ITelephony
			telephonyService = (ITelephony)m.invoke(telephonyManager);
			telephonyService.endCall();
		} catch (Exception e) {Log.e(TAG,"endCall exeption  "+e); e.printStackTrace();
		cutTheCall();
			Log.e(TAG,">>>>>>>>>>>>   telephonyManager.getSimOperator()  "+telephonyManager.getSimOperator());
		}
		*/
		AudioManager am=(AudioManager)getSystemService(AUDIO_SERVICE);
		am.setMode(AudioManager.MODE_IN_CALL);
		am.setSpeakerphoneOn(false);
		
		}
		
	private void cutTheCall() {
		l("in cutTheCall()");
		TelecomManager telecomManager = (TelecomManager) getApplicationContext().getSystemService(TELECOM_SERVICE);
		if (ContextCompat. checkSelfPermission(this,android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){  // || telecomManager == null) {
			requestPermissions(new String[] {android.Manifest.permission.READ_PHONE_STATE },PHONE_STATE);
			l("requesting  android.Manifest.permission.READ_PHONE_STATE");
		//	cutCall();
		return;
		}
		l("android.Manifest.permission.READ_PHONE_STATE Granted");
		if (telecomManager.isInCall()) {
			l("telecomManager.isInCall()  true");
			l("Build.VERSION.SDK_INT "+Build.VERSION.SDK_INT);
			l(Build.VERSION.SDK);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
				l("Build.VERSION.SDK_INT "+Build.VERSION.SDK_INT);
				//callDisconnected = 
				telecomManager.endCall();
			//	telecomManager.acceptRingingCall();
			}
		}
	}
	
	void answerCall(){
		l("in answerCall()");
		TelecomManager telecomManager = (TelecomManager) getApplicationContext().getSystemService(TELECOM_SERVICE);
		if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ANSWER_PHONE_CALLS)==PackageManager.PERMISSION_DENIED){
			l("android.Manifest.permission.ANSWER_PHONE_CALLS NOT GRANTED");
			requestPermissions(new String[]{android.Manifest.permission.ANSWER_PHONE_CALLS},8080);
		}else{l("android.Manifest.permission.ANSWER_PHONE_CALLS GRANTED");}
		if(telecomManager.isInCall()){
			telecomManager.acceptRingingCall();
			
		}
	}
	
	/*
	private void cutCall(){
		requestPermissions(new String[] {android. Manifest.permission.READ_PHONE_STATE },PHONE_STATE);
	}
	*/

@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
	l("in onRequestPermissionResult  requestCode  "+requestCode);
    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        if (requestCode == PHONE_STATE) {
            requestPermissions(new String[] { android.Manifest.permission.ANSWER_PHONE_CALLS }, ANSWER_CALLS);
        } else if (requestCode == ANSWER_CALLS) {
       //     cutTheCall;
        }
    }
}
		
	private void hideKeyBoard(){
		InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);}

	private void showKeyBoard(){//l("in showKeyBoard");
		InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}
	
}


