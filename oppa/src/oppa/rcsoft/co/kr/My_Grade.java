package oppa.rcsoft.co.kr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


// µî±ÞÇ¥ 
public class My_Grade  extends  BaseActivity  implements OnClickListener
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
       
        
        setContentView(R.layout.my_grade);  
        
		///////////////////////////////////////////////////////////////////////////////////////////////////
		// Tab
		{
    		Button TabBTN2 = (Button)findViewById(R.id.mypage1_main_tab_btn_1);
    		TabBTN2.setOnClickListener(this);
    		Button TabBTN3 = (Button)findViewById(R.id.mypage1_main_tab_btn_2);
    		TabBTN3.setOnClickListener(this);
    		Button TabBTN4 = (Button)findViewById(R.id.mypage1_main_tab_btn_3);
    		TabBTN4.setOnClickListener(this);
    		Button TabBTN5 = (Button)findViewById(R.id.mypage1_main_tab_btn_4);
    		TabBTN5.setOnClickListener(this);
		}
    }
    
    
	public void onClick(View v )
    {
		
    	switch(v.getId())
    	{

    		
    	case R.id.mypage1_main_tab_btn_2:

    	{
    		Intent intent;
	        intent = new Intent().setClass(this, Shop_MainList.class);
	        startActivity( intent );  
    	}
    		break;
    		
    	case R.id.mypage1_main_tab_btn_3:
 
    	{
    		Intent intent;
	        intent = new Intent().setClass(this, ToyMainList.class);
	        startActivity( intent );   
    	}
    		break;
    		
    	case R.id.mypage1_main_tab_btn_4:
    	{
    		Intent intent;
	        intent = new Intent().setClass(this, Community_Main.class);
	        startActivity( intent );  
    	}
    		break;
    		
    	case R.id.mypage1_main_tab_btn_1:
    	{
    		Intent intent;
	        intent = new Intent().setClass(this, Home.class);
	        startActivity( intent );  
    	}
    		break;

    	}
    }
    

}
