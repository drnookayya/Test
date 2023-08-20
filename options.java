package co.in.drh.Tokens;
import android.preference.*;
import android.view.*;
//import android.support.v7.app.*;
import android.os.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.util.*;
import java.util.*;
import android.app.*;
import android.view.View.*;
import java.text.*;
import android.widget.Magnifier.*;
import org.apache.http.auth.*;
import android.widget.AutoCompleteTextView.*;

public class options extends Activity
{
   static final String lf="\n",sp="  ",s="#",TAG="TOKENS",OPTIONS="OPTIONS",SpOptions="sp_options",nul="null",Holiday="Holiday";
   String[] keys,values,vals;
   ArrayList<String> alkey=new ArrayList<String>();
   ArrayList<String> alVal=new ArrayList<String>();
   
   String mFormat="E  dd/MM/yy",delKey;
   String dateNumber="0",spOptionHoliday="false",spOptionStartTime="0",spOptionCloseTime="0",spOptionTokens="0";
   TextView tvDate,tvTitle,tvTokens,tvStartTime,tvCloseTime;
   final static int OPTIONS_DATE=0,HOLIDAY=0,OPTIONS_TAG=1,OPTIONS_OP_START=2,OPTIONS_OP_CLOSE=3,
   					TOKENS=4,DateOfEvent=5, OPTIONS_START=1,OPTIONS_CLOSE=2;
   int DATE_NUMBER,OP_START_MINUTE,OP_CLOSE_MINUTE,MINUTE,totalTokens;
   Button btnUpdate;
   EditText etTag;
   AutoCompleteTextView act;
   boolean blnHoliday=false;
   Calendar myCalendar= Calendar.getInstance();
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
	  super.onCreate(savedInstanceState);
	//  setRequestedOrientation(
	/*
	  setContentView(R.layout.options_table);
	  TextView btnAdd=findViewById(R.id.optionstableButtonAdd);
	  btnAdd.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View p1)
			{
			 setOptions();
			}
		 });
		 */
		 displayOptions();
		 }
	public void displayOptions(){
	   setContentView(R.layout.options_table);
	   TextView btnAdd=findViewById(R.id.optionstableButtonAdd);
	   btnAdd.setOnClickListener(new OnClickListener(){

			 @Override
			 public void onClick(View p1)
			 {
				setOptions();
			 }
		  });
		SharedPreferences sp_options=getSharedPreferences(getString(R.string.SpOptions),MODE_PRIVATE);
	  Map<String,?> optionKeys= sp_options.getAll();
	  keys=new String[optionKeys.keySet().size()];
	  values=new String[keys.length];
	  int x=0;
	  for(String key:optionKeys.keySet()){
		 l(x+"  "+key+"     "+sp_options.getString(key,"OOO"));
		 keys[x]=key;values[x]=sp_options.getString(key,"OOO");
		 x++;
		 }
	   alkey=new ArrayList<String>();
	   alVal=new ArrayList<String>();
	  
	  Set set=optionKeys.entrySet();
	  Iterator it=set.iterator();
	  while (it.hasNext())
	  {
		 Map.Entry entry=(Map.Entry) it.next();
		 alkey.add(entry.getKey().toString());
		 alVal.add(entry.getValue().toString());
		 
   	   }
   		for(int y=0;y<alkey.size();y++){
		   keys[y]=alkey.get(y);
		   values[y]=alVal.get(y);
		}
   		ListView lv=findViewById(R.id.optionstableListView1);
		lv.setAdapter(new adptr<String>(getBaseContext(),keys));
 		}
   private class adptr<String> extends ArrayAdapter<String>{
	  
	  public adptr(Context con, String[] optarray){
		 super(con,R.layout.optionsview,optarray);
		 
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent)
	  {
		 final int pos=position;
		 LayoutInflater li=getLayoutInflater();
		 View view= li.inflate(R.layout.optionsview,parent,false);
		 TextView tv_op_view_date = view.findViewById(R.id.optionsviewTextViewDate);
		 TextView tv_op_view_date_num = view.findViewById(R.id.optionsviewTextViewDateNum);		 
		 TextView tv_op_view_Holiday = view.findViewById(R.id.optionsviewTextViewHoliday);
		 TextView tv_op_view_start = view.findViewById(R.id.optionsviewTextViewStart);
		 TextView tv_op_view_close = view.findViewById(R.id.optionsviewTextViewClose);
		 TextView tv_op_view_tokens = view.findViewById(R.id.optionsviewTextViewTokens);
		 TextView tv_op_view_tag = view.findViewById(R.id.optionsviewTextViewTag);
		 TextView tv_op_view_delete = view.findViewById(R.id.optionsviewTextViewDelete);
		 tv_op_view_date_num.setText(keys[position]);
		 vals=alVal.get(position).split(s);
		 if(vals.length>DateOfEvent)tv_op_view_date.setText(vals[DateOfEvent]);
		 else{
		 myCalendar.set(Calendar.DAY_OF_YEAR,Integer.parseInt(keys[position]));
		 SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy");
		 tv_op_view_date.setText(sdf.format(myCalendar.getTime()));}
		 tv_op_view_Holiday.setText(vals[HOLIDAY]);
		 tv_op_view_tag.setText(vals[OPTIONS_TAG]);
		 tv_op_view_tokens.setText(vals[TOKENS]);
		 tv_op_view_start.setText(backToMinutes( vals[ OPTIONS_OP_START]));
		 tv_op_view_close.setText(backToMinutes( vals[OPTIONS_OP_CLOSE]));
		 tv_op_view_delete.setOnClickListener(
		 	new OnClickListener(){

			   @Override
			   public void onClick(View p1)
			   {
				  delKey=(alkey.get(pos));
				  SharedPreferences spr= getSharedPreferences(SpOptions,MODE_PRIVATE);
			   	  SharedPreferences.Editor spre= spr.edit();
				  spre.remove(delKey);
				  spre.commit();
				  displayOptions();
				  }
			});
		 return view; 
		 //super.getView(position, convertView, parent);
	  }
	  
   }
   
   public String backToMinutes(String val){
	  String rtnVal="00:00";
	  int time=Integer.parseInt(val);
	  int gantalu=time/60;
	  int nimishalu=time - (gantalu*60);
	  rtnVal=String.format("%02d",gantalu)+":"+String.format("%02d",nimishalu);
	  return rtnVal;
   }
   
   public void OptionsOcl(View v){
	  switch(v.getId()){
		 case R.id.optionsettingsTextViewDateChange:
		 case R.id.optionsettingsTextViewDate:
		//	t("tvDate.optionsOcl");
			l("tvDate.optionsOcl");
			showDialog(OPTIONS_DATE);
			break;
		 case R.id.optionsettingsTextViewOpStart :
			showDialog(OPTIONS_START);
			break;
		 case R.id.optionsettingsTextViewOpClose  : 
			showDialog(OPTIONS_CLOSE);
		 break;
		 case R.id.optionsettingsTextViewTokens : break;
	  }
   }
   
   void setOptions(){
	  setContentView(R.layout.option_settings);
	  tvDate=findViewById(R.id.optionsettingsTextViewDate);
	  tvStartTime=findViewById(R.id.optionsettingsTextViewOpStart);
	  tvCloseTime=findViewById(R.id.optionsettingsTextViewOpClose);
	  tvTokens=findViewById(R.id.optionsettingsTextViewTokens);
	  etTag=findViewById(R.id.optionsettingsEditTextTag);
	  btnUpdate=findViewById(R.id.optionsettingsButtonUpdate);
	  myCalendar=Calendar.getInstance();
	  
	 
	  btnUpdate.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View p1)
			{
			   saveOptions();displayOptions();
			}
		 });
	  Switch swich=findViewById(R.id.optionsettingsSwitcHoliday);
	  swich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton p1, boolean p2)
			{
			  if(p2){t("hoiday true");
			  tvStartTime.setEnabled(false);
			  tvCloseTime.setEnabled(false);
			  tvTokens.setEnabled(false);
				 tvStartTime.setText("00:00");
				 tvCloseTime.setText("00:00");
				 tvTokens.setText("00");
			  etTag.requestFocus();
			  blnHoliday=true;
			  }else {t("Not =======  Holiday");
			  	 blnHoliday=false;
				 tvStartTime.setEnabled(true);
				 tvCloseTime.setEnabled(true);
				 tvTokens.setEnabled(true);
			  }
			}
		 });
	updateLabel(OPTIONS_DATE);
   }

   private void saveOptions(){
	  String spOptionsValue=null,tag=etTag.getText().toString(),spOptionTokens=tvTokens.getText().toString(),
	  date=tvDate.getText().toString();
	  if(blnHoliday){
	  spOptionsValue =Holiday+s+tag+s+"0"+s+"0"+s+"0"+s+date;}
	  else{
		 spOptionsValue = 	nul+s+tag+s+String.valueOf(OP_START_MINUTE)+s+String.valueOf(OP_CLOSE_MINUTE)+s+spOptionTokens+s+date;
	  }
	  l("spOptionsValue    "+  spOptionsValue);
	  SharedPreferences sp=getSharedPreferences(SpOptions,MODE_PRIVATE);		
	  SharedPreferences.Editor spe=sp.edit();
	  dateNumber=String.valueOf(DATE_NUMBER);
	  l("dateNumber   "+DATE_NUMBER); 
	  spe.putString(dateNumber,spOptionsValue);
	  spe.commit();
   }
   
   private void updateLabel(int view){
	//  t("in Updte label");
	  l("in Updtelabel");
	  String myFormat;
	   myFormat = (view==OPTIONS_DATE) ? "E  dd/MM/yy" :  "hh:mm a";
	  
	  SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat);
	  if(view==OPTIONS_DATE){
	  tvDate.setText(dateFormat.format(myCalendar.getTime()));
	  DATE_NUMBER=myCalendar.get(Calendar.DAY_OF_YEAR);}
	  if(view==OPTIONS_START){
		 tvStartTime.setText(dateFormat.format(myCalendar.getTime())+MINUTE);OP_START_MINUTE=MINUTE;}
	  if(view==OPTIONS_CLOSE){
		 tvCloseTime.setText(dateFormat.format(myCalendar.getTime())+MINUTE);OP_CLOSE_MINUTE=MINUTE;}
	  
	  
   }

   @Override
   protected Dialog onCreateDialog(int id)
   {
	  final int idd=id;
	  if(id==OPTIONS_DATE){
	  DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
		 @Override
		 public void onDateSet(DatePicker view, int year, int month, int day) {
			myCalendar.set(Calendar.YEAR, year);
			myCalendar.set(Calendar.MONTH,month);
			myCalendar.set(Calendar.DAY_OF_MONTH,day);
			updateLabel(idd);
		 }
	  };
	  DatePickerDialog dpd=new DatePickerDialog(options.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
	  
	  dpd.show();
	  }
	  if(id==OPTIONS_START|id==OPTIONS_CLOSE){
		 TimePickerDialog.OnTimeSetListener timeset=new TimePickerDialog.OnTimeSetListener(){

			@Override
			public void onTimeSet(TimePicker p1, int p2, int p3)
			{
			 myCalendar.set(Calendar.HOUR_OF_DAY,p2);
			 myCalendar.set(Calendar.MINUTE,p3);
			 MINUTE=p2*60+p3;
			 updateLabel(idd);  
			}
		 };
		 
		 TimePickerDialog tpd=new TimePickerDialog(options.this,timeset,23,59,false);//,myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE));
		 tpd.show();
	  }
	  return super.onCreateDialog(id);

   }	 
   
   public void l(Object o){Log.i(TAG,o.toString());}
   public void t(Object o){Toast.makeText(getBaseContext(),o.toString(),Toast.LENGTH_LONG).show();
	  Toast tost=new Toast(this);
	  tost.setGravity(Gravity.CENTER,0,0);
	  TextView ttv = new TextView(this);
	  ttv.setBackgroundColor(Color.WHITE);
	  ttv.setPadding(10,10,10,10);
	  ttv.setTextColor(Color.BLUE);
	  ttv.setGravity(Gravity.CENTER);
	  ttv.setText(o.toString());
	  tost.setView(ttv);
	  tost.setDuration(Toast.LENGTH_LONG);
	  tost.show();

   }
   
}

