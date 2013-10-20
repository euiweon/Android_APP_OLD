package oppa.rcsoft.co.kr;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


 // 인트로 
public class OppaActivity extends Activity {    
	private Handler handler = new Handler();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
       
        
        setContentView(R.layout.introlayout);          // 인트로 레이아웃 출력      
        
        handler.postDelayed(TimerRunner, 2000);   		// 2초 뒤에 호출
    }
    
    
    // **********************************************************************
    // 타이머 처리
    // **********************************************************************    
    private Runnable TimerRunner = new IntroRunnAble(this);
    public class IntroRunnAble implements Runnable 
    { 
    	 
    	Object parentActivity;
    	public IntroRunnAble(Object parameter)
    	{
    		   
    	    // store parameter for later user 
    	   parentActivity = parameter;
    	} 
    	 
    	public void run() 
    	{ 
           handler.removeCallbacks(TimerRunner);   // 재실행 제거
              
           Intent intent;
            // Create an Intent to launch an Activity for the tab (to be reused)
           intent = new Intent().setClass((OppaActivity)parentActivity, login.class);
          
           startActivity( intent );           
               
    	} 
    } 
}