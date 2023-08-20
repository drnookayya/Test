package co.in.drh.Tokens;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import java.io.*;
import android.util.*;
import android.content.*;
import android.view.View.*;
import android.widget.AbsListView.*;

public class search extends Activity
{

   static final int APTM=1,ALL=2,TOKEN=0,NAME=1,NUMBER=2;
   static final String aptm="aptm",all="all";
   EditText et;
   ListView lv;
   ArrayList<String> al;
   TextView tvSno,tvNumber,tvName;
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
	  // TODO: Implement this method
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.search);
	  et=findViewById(R.id.searchEditText1);
	  lv=findViewById(R.id.searchListView1);
	  
	  
   }
 
   public void searchOcl(View v){
	 Toast.makeText(getBaseContext(),"=======+++++++++++++\n   searchOcl    \n++++++++++++=======",Toast.LENGTH_LONG).show();
	 switch(v.getId()){
		case R.id.searchButtonExit:finish();break;
		case R.id.searchButtonAptm:show(APTM);break;
		case R.id.searchButtonAll:show(ALL);break;
	 } 
   }
   void show(int flag){
	  File fl=null;
	  if(flag==APTM){
		fl=new File(getFilesDir(),aptm);}
	  if(flag==ALL){
		fl=new File(getFilesDir(),all);}
		al=new ArrayList<String>(); 
		try
		 {
			BufferedReader br=new BufferedReader(new FileReader(fl));
			String string;
			while((string=br.readLine())!=null){
			   al.add(string);
			//   Log.d("TOKENS",string);
			}
			br.close();
			lv.setAdapter(new Adptr(this,al));
		
		 }
		 catch (Exception e)
		 {}
	  }
   private class Adptr extends ArrayAdapter<String>{
	  Context c;
	  public Adptr(Context con,ArrayList<String> al){ 
	  super(con,R.layout.search_view,al);
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent)
	  {
		 View view=getLayoutInflater().inflate(R.layout.search_view,parent,false);
		 final TextView Token=(view).findViewById(R.id.searchviewTextViewSno);
		 final TextView Name=(view).findViewById(R.id.searchviewTextViewName);
		 final TextView Number=(view).findViewById(R.id.searchviewTextViewPhoneNo);
		 TextView Click=(view).findViewById(R.id.searchviewTextViewclick);
	//	 Log.d("TOKENS","position  "+ position +"   "+al.get(position));
		 if(al.get(position).contains("#")){
		 String[] str=al.get(position).split("#");
	//	 Log.d("TOKENS","str.length   "+str.length+"  "+str[0]+"  "+str[1]+"  "+str[2]);
		 Token.setText(str[TOKEN]);
		 Name.setText(str[NAME]);
		 Number.setText(str[NUMBER]);
		 }
		 else
		 {
			Token.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		 	Token.setText(al.get(position)+"\n");
		 }
		 Click.setOnClickListener(new OnClickListener(){

			   @Override
			   public void onClick(View p1)
			   {
				  Toast.makeText(getBaseContext(),"in Search show() clicked "+Number.getText().toString(),Toast.LENGTH_LONG).show();

				  }
			});
		 return view;// super.getView(position, convertView, parent);
	  }
	  
	  
   }
}
