package oppa.rcsoft.co.kr;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;




// Home 로그인후 메인화면 
public class Home extends  BaseActivity   
{
    /** Called when the activity is first created. */
	
	Home  self;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);
        
        self = this;
        Button Home_Main_BTN1 = (Button)findViewById(R.id.home_main_button_1);
        Home_Main_BTN1.setOnClickListener(new Home_Main_ClickListen(this));
        Button Home_Main_BTN2 = (Button)findViewById(R.id.home_main_button_2);
        Home_Main_BTN2.setOnClickListener(new Home_Main_ClickListen(this));
        Button Home_Main_BTN3 = (Button)findViewById(R.id.home_main_button_3);
        Home_Main_BTN3.setOnClickListener(new Home_Main_ClickListen(this));
        Button Home_Main_BTN4 = (Button)findViewById(R.id.home_main_button_4);
        Home_Main_BTN4.setOnClickListener(new Home_Main_ClickListen(this));
        Button Home_Main_BTN5 = (Button)findViewById(R.id.home_main_button_5);
        Home_Main_BTN5.setOnClickListener(new Home_Main_ClickListen(this));
        Button Home_Main_BTN6 = (Button)findViewById(R.id.home_main_button_6);
        Home_Main_BTN6.setOnClickListener(new Home_Main_ClickListen(this));
        

        
        
        ///////////////////////////////////////////////////////////////////////////////////////////////////
        // Tab
        {
            Button TabBTN2 = (Button)findViewById(R.id.home_tab_btn_2);
            TabBTN2.setOnClickListener(new Home_Main_ClickListen(this));
            Button TabBTN3 = (Button)findViewById(R.id.home_tab_btn_3);
            TabBTN3.setOnClickListener(new Home_Main_ClickListen(this));
            Button TabBTN4 = (Button)findViewById(R.id.home_tab_btn_4);
            TabBTN4.setOnClickListener(new Home_Main_ClickListen(this));
            Button TabBTN5 = (Button)findViewById(R.id.home_tab_btn_5);
            TabBTN5.setOnClickListener(new Home_Main_ClickListen(this));
        }

     
    }
    
    @Override
    public void onDestroy()
    {
    	ActivityManager am  = (ActivityManager)this.getSystemService(Activity.ACTIVITY_SERVICE);
        am.killBackgroundProcesses(this.getPackageName());
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK)
		{
			
			new AlertDialog.Builder(self)
			 .setTitle("종료 확인")
			 .setMessage("정말 종료 하겠습니까?") //줄였음
			 .setPositiveButton("예", new DialogInterface.OnClickListener() 
			 {
			     public void onClick(DialogInterface dialog, int whichButton)
			     {   
					moveTaskToBack(true);
					finish();
				    android.os.Process.killProcess(android.os.Process.myPid());
				    
				    ActivityManager am  = (ActivityManager)self.getSystemService(Activity.ACTIVITY_SERVICE);
		    	    am.restartPackage(self.getPackageName());
			     }
			 })
			 .setNegativeButton("아니요", new DialogInterface.OnClickListener() 
			 {
			     public void onClick(DialogInterface dialog, int whichButton) 
			     {
			         //...할일
			     }
			 })
			 .show();
			

			return false;
		}
		else
		{
			return true;
		}
		
		
	}
    
    
    
    
    public  class Home_Main_ClickListen implements OnClickListener
    {
    	Home Parentactivity;
    	public Home_Main_ClickListen( Home activity)
    	{
    		Parentactivity = activity;
    	}

    	public void onClick(View v )
        {
    		
    		MyInfo myApp = (MyInfo) getApplication();
        	switch(v.getId())
        	{
        	case R.id.home_main_button_1:
        	{
        		myApp.SetShopKind(1);
        		Intent intent;
		        intent = new Intent().setClass(self, Shop_MainList.class);
		        startActivity( intent );  
        		    		
        	}
        		break;
        	case R.id.home_main_button_2:
        	{
        		myApp.SetShopKind(2);
        		Intent intent;
		        intent = new Intent().setClass(self, Shop_MainList.class);
		        startActivity( intent );  
        		
        	}
        		break;
        		
        	case R.id.home_main_button_3:
        	{
        		myApp.SetShopKind(3);
        		Intent intent;
		        intent = new Intent().setClass(self, Shop_MainList.class);
		        startActivity( intent );  
        		
        	}
        		break;
        		
        	case R.id.home_main_button_4:
        	{
        		myApp.SetShopKind(4);
        		Intent intent;
		        intent = new Intent().setClass(self, Shop_MainList.class);
		        startActivity( intent );  
        		
        	}
        		break;
        		
        	case R.id.home_main_button_5:
        	{
        		myApp.SetShopKind(5); 
        		Intent intent;
		        intent = new Intent().setClass(self, Shop_MainList.class);
		        startActivity( intent );  
        	}
        		break;
        		
        	case R.id.home_main_button_6:
        	{
        		myApp.SetShopKind(0);
        		Intent intent;
		        intent = new Intent().setClass(self, Shop_MainList.class);
		        startActivity( intent );  
        	}
        		break;
        	case R.id.home_tab_btn_2:
        	{
        		Intent intent;
		        intent = new Intent().setClass(self, Shop_MainList.class);
		        startActivity( intent );  
        	}
        		break;
        		
        	case R.id.home_tab_btn_3:
        	{
        		Intent intent;
		        intent = new Intent().setClass(self, ToyMainList.class);
		        startActivity( intent );   
        	}
        		break;
        		
        	case R.id.home_tab_btn_4:
        	{
        		Intent intent;
		        intent = new Intent().setClass(self, Community_Main.class);
		        startActivity( intent );  
        	}
        		break;
        		
        	case R.id.home_tab_btn_5:
        	{
        		Intent intent;
		        intent = new Intent().setClass(self, MyPage_Main.class);
		        startActivity( intent );  
        	}
        		break;
        		
        		

        		
        	}
        }

    }
}
