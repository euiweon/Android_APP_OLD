package com.euiweonjeong.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.Window;


public class UISizeConverter {

	int mStatusBarHeight; 
	Context m_Context;



    private static final int LOW_DPI_STATUS_BAR_HEIGHT = 19;
    private static final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25;
    private static final int HIGH_DPI_STATUS_BAR_HEIGHT = 38;
    
    public UISizeConverter( Context context, int StatusBarHeight )
    {
    	mStatusBarHeight = StatusBarHeight;
    	m_Context = context;
    }
    
    
    // Linear LayOut 크기변경
    public void ConvertLinearLayout( LinearLayout layout)
    {
    	Display defaultDisplay = ((WindowManager)m_Context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	int windowswidth = defaultDisplay.getWidth();
		int windowsheight= defaultDisplay.getHeight();
		int originwidth = 480;
		int originheight = 800;
		
		int layoutheight = layout.getLayoutParams().height;
		int layoutwidth = layout.getLayoutParams().width;
		
		LinearLayout.LayoutParams margin = (android.widget.LinearLayout.LayoutParams) layout.getLayoutParams();
		
		
		int posx = margin.leftMargin;
		int posy = margin.topMargin;
		
	
		layout.getLayoutParams().width = (int) ( (float) layoutwidth * ( (float)(windowswidth)/ (float)(originwidth) )  );
		layout.getLayoutParams().height = (int) ( (float) layoutheight * ( (float)(windowsheight)/ (float)(originheight) )  );
		
		posx = (int) ( (float) posx * ( (float)(windowswidth)/ (float)(originwidth) )  );  
		//posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight - HIGH_DPI_STATUS_BAR_HEIGHT ); 
		posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight); 
		
		margin.setMargins(posx , posy, 0, 0);
		layout.setLayoutParams(margin);
		

    }
    
    public void ParentLinearConvertFrameLayout( FrameLayout layout)
    {
    	Display defaultDisplay = ((WindowManager)m_Context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	int windowswidth = defaultDisplay.getWidth();
		int windowsheight= defaultDisplay.getHeight();
		int originwidth = 480;
		int originheight = 800;
		
		int layoutheight = layout.getLayoutParams().height;
		int layoutwidth = layout.getLayoutParams().width;
		
		LinearLayout.LayoutParams margin = (android.widget.LinearLayout.LayoutParams) layout.getLayoutParams();
		
		
		int posx = margin.leftMargin;
		int posy = margin.topMargin;
		
	
		layout.getLayoutParams().width = (int) ( (float) layoutwidth * ( (float)(windowswidth)/ (float)(originwidth) )  );
		layout.getLayoutParams().height = (int) ( (float) layoutheight * ( (float)(windowsheight)/ (float)(originheight) )  );
		
		posx = (int) ( (float) posx * ( (float)(windowswidth)/ (float)(originwidth) )  );  
		//posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight - HIGH_DPI_STATUS_BAR_HEIGHT ); 
		posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight); 
		
		margin.setMargins(posx , posy, 0, 0);
		layout.setLayoutParams(margin);
    }
    
    public void ParentFrameConvertLinearLayout( LinearLayout layout)
    {
    	Display defaultDisplay = ((WindowManager)m_Context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	int windowswidth = defaultDisplay.getWidth();
		int windowsheight= defaultDisplay.getHeight();
		int originwidth = 480;
		int originheight = 800;
		
		int layoutheight = layout.getLayoutParams().height;
		int layoutwidth = layout.getLayoutParams().width;
		
		FrameLayout.LayoutParams margin = (android.widget.FrameLayout.LayoutParams) layout.getLayoutParams();
		
		
		int posx = margin.leftMargin;
		int posy = margin.topMargin;
		
	
		layout.getLayoutParams().width = (int) ( (float) layoutwidth * ( (float)(windowswidth)/ (float)(originwidth) )  );
		layout.getLayoutParams().height = (int) ( (float) layoutheight * ( (float)(windowsheight)/ (float)(originheight) )  );
		
		posx = (int) ( (float) posx * ( (float)(windowswidth)/ (float)(originwidth) )  );  
		//posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight - HIGH_DPI_STATUS_BAR_HEIGHT ); 
		posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight); 
		
		margin.setMargins(posx , posy, 0, 0);
		layout.setLayoutParams(margin);
    }
    
    
    public void ParentRelativeConvertLinearLayout( LinearLayout layout)
    {
    	Display defaultDisplay = ((WindowManager)m_Context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	int windowswidth = defaultDisplay.getWidth();
		int windowsheight= defaultDisplay.getHeight();
		int originwidth = 480;
		int originheight = 800;
		
		int layoutheight = layout.getLayoutParams().height;
		int layoutwidth = layout.getLayoutParams().width;
		
		RelativeLayout.LayoutParams margin = (android.widget.RelativeLayout.LayoutParams) layout.getLayoutParams();
		
		
		int posx = margin.leftMargin;
		int posy = margin.topMargin;
		
	
		layout.getLayoutParams().width = (int) ( (float) layoutwidth * ( (float)(windowswidth)/ (float)(originwidth) )  );
		layout.getLayoutParams().height = (int) ( (float) layoutheight * ( (float)(windowsheight)/ (float)(originheight) )  );
		
		posx = (int) ( (float) posx * ( (float)(windowswidth)/ (float)(originwidth) )  );  
		//posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight - HIGH_DPI_STATUS_BAR_HEIGHT ); 
		posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight); 
		
		margin.setMargins(posx , posy, 0, 0);
		layout.setLayoutParams(margin);
    }
    
    
    public void ParentLinearConvertLinearLayout( LinearLayout layout)
    {
    	Display defaultDisplay = ((WindowManager)m_Context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	int windowswidth = defaultDisplay.getWidth();
		int windowsheight= defaultDisplay.getHeight();
		int originwidth = 480;
		int originheight = 800;
		
		int layoutheight = layout.getLayoutParams().height;
		int layoutwidth = layout.getLayoutParams().width;
		
		LinearLayout.LayoutParams margin = (android.widget.LinearLayout.LayoutParams) layout.getLayoutParams();
		
		
		int posx = margin.leftMargin;
		int posy = margin.topMargin;
		
	
		layout.getLayoutParams().width = (int) ( (float) layoutwidth * ( (float)(windowswidth)/ (float)(originwidth) )  );
		layout.getLayoutParams().height = (int) ( (float) layoutheight * ( (float)(windowsheight)/ (float)(originheight) )  );
		
		posx = (int) ( (float) posx * ( (float)(windowswidth)/ (float)(originwidth) )  );  
		//posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight - HIGH_DPI_STATUS_BAR_HEIGHT ); 
		posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight); 
		
		margin.setMargins(posx , posy, 0, 0);
		layout.setLayoutParams(margin);
    }
    
    
       
    // Frame Layout에 해당하는 버튼바꾸기
	public void ConvertFrameLayoutBtn( Button btn )
	{
    	Display defaultDisplay = ((WindowManager)m_Context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	
		int windowswidth = defaultDisplay.getWidth();
		int windowsheight= defaultDisplay.getHeight();
		int originwidth = 480;
		int originheight = 800;
		
		int btnheight = btn.getLayoutParams().height;
		int btnwidth = btn.getLayoutParams().width;
		
		FrameLayout.LayoutParams margin = (FrameLayout.LayoutParams )(btn.getLayoutParams());
		
		int posx = margin.leftMargin;
		int posy = margin.topMargin;
		
	
		btn.getLayoutParams().width = (int) ( (float) btnwidth * ( (float)(windowswidth)/ (float)(originwidth) )  );
		btn.getLayoutParams().height = (int) ( (float) btnheight * ( (float)(windowsheight)/ (float)(originheight) )  );
		
		posx = (int) ( (float) posx * ( (float)(windowswidth)/ (float)(originwidth) )  );  
		//posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight - HIGH_DPI_STATUS_BAR_HEIGHT ); 
		posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight); 
		
		margin.setMargins(posx , posy, 0, 0);
		btn.setLayoutParams(margin);
	}
	
	
	public void ConvertFrameLayoutImage( ImageView image )
	{
    	Display defaultDisplay = ((WindowManager)m_Context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	
		int windowswidth = defaultDisplay.getWidth();
		int windowsheight= defaultDisplay.getHeight();
		int originwidth = 480;
		int originheight = 800;
		
		int imageheight = image.getLayoutParams().height;
		int imagewidth = image.getLayoutParams().width;
		
		FrameLayout.LayoutParams margin = (FrameLayout.LayoutParams )(image.getLayoutParams());
		
		int posx = margin.leftMargin;
		int posy = margin.topMargin;
		
	
		image.getLayoutParams().width = (int) ( (float) imagewidth * ( (float)(windowswidth)/ (float)(originwidth) )  );
		image.getLayoutParams().height = (int) ( (float) imageheight * ( (float)(windowsheight)/ (float)(originheight) )  );
		
		posx = (int) ( (float) posx * ( (float)(windowswidth)/ (float)(originwidth) )  );  
		//posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight - HIGH_DPI_STATUS_BAR_HEIGHT ); 
		posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight); 
		margin.setMargins(posx , posy, 0, 0);
		image.setLayoutParams(margin);
	}
	
	
	public void ConvertFrameLayoutEditText( EditText image )
	{
    	Display defaultDisplay = ((WindowManager)m_Context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	
		int windowswidth = defaultDisplay.getWidth();
		int windowsheight= defaultDisplay.getHeight();
		int originwidth = 480;
		int originheight = 800;
		
		int imageheight = image.getLayoutParams().height;
		int imagewidth = image.getLayoutParams().width;
		
		FrameLayout.LayoutParams margin = (FrameLayout.LayoutParams )(image.getLayoutParams());
		
		int posx = margin.leftMargin;
		int posy = margin.topMargin;
		
	
		image.getLayoutParams().width = (int) ( (float) imagewidth * ( (float)(windowswidth)/ (float)(originwidth) )  );
		image.getLayoutParams().height = (int) ( (float) imageheight * ( (float)(windowsheight)/ (float)(originheight) )  );
		
		posx = (int) ( (float) posx * ( (float)(windowswidth)/ (float)(originwidth) )  );  
		//posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight - HIGH_DPI_STATUS_BAR_HEIGHT ); 
		posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight); 
		margin.setMargins(posx , posy, 0, 0);
		image.setLayoutParams(margin);
	}
	
	
	public void ConvertFrameLayoutTextView( TextView image )
	{
    	Display defaultDisplay = ((WindowManager)m_Context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	
		int windowswidth = defaultDisplay.getWidth();
		int windowsheight= defaultDisplay.getHeight();
		int originwidth = 480;
		int originheight = 800;
		
		int imageheight = image.getLayoutParams().height;
		int imagewidth = image.getLayoutParams().width;
		
		FrameLayout.LayoutParams margin = (FrameLayout.LayoutParams )(image.getLayoutParams());
		
		int posx = margin.leftMargin;
		int posy = margin.topMargin;
		
	
		image.getLayoutParams().width = (int) ( (float) imagewidth * ( (float)(windowswidth)/ (float)(originwidth) )  );
		image.getLayoutParams().height = (int) ( (float) imageheight * ( (float)(windowsheight)/ (float)(originheight) )  );
		
		posx = (int) ( (float) posx * ( (float)(windowswidth)/ (float)(originwidth) )  );  
		//posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight - HIGH_DPI_STATUS_BAR_HEIGHT ); 
		posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight); 
		margin.setMargins(posx , posy, 0, 0);
		image.setLayoutParams(margin);
	}
	
	
	
	
	public void ConvertFrameLayoutListView( ListView image )
	{
    	Display defaultDisplay = ((WindowManager)m_Context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	
		int windowswidth = defaultDisplay.getWidth();
		int windowsheight= defaultDisplay.getHeight();
		int originwidth = 480;
		int originheight = 800;
		
		int imageheight = image.getLayoutParams().height;
		int imagewidth = image.getLayoutParams().width;
		
		FrameLayout.LayoutParams margin = (FrameLayout.LayoutParams )(image.getLayoutParams());
		
		int posx = margin.leftMargin;
		int posy = margin.topMargin;
		
	
		image.getLayoutParams().width = (int) ( (float) imagewidth * ( (float)(windowswidth)/ (float)(originwidth) )  );
		image.getLayoutParams().height = (int) ( (float) imageheight * ( (float)(windowsheight)/ (float)(originheight) )  );
		
		posx = (int) ( (float) posx * ( (float)(windowswidth)/ (float)(originwidth) )  );  
		//posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight - HIGH_DPI_STATUS_BAR_HEIGHT ); 
		posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight); 
		margin.setMargins(posx , posy, 0, 0);
		image.setLayoutParams(margin);
	}
	
	
	public void ConvertLinearLayoutListView( ListView image )
	{
    	Display defaultDisplay = ((WindowManager)m_Context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	
		int windowswidth = defaultDisplay.getWidth();
		int windowsheight= defaultDisplay.getHeight();
		int originwidth = 480;
		int originheight = 800;
		
		int imageheight = image.getLayoutParams().height;
		int imagewidth = image.getLayoutParams().width;
		
		LinearLayout.LayoutParams margin = (LinearLayout.LayoutParams )(image.getLayoutParams());
		
		int posx = margin.leftMargin;
		int posy = margin.topMargin;
		
	
		image.getLayoutParams().width = (int) ( (float) imagewidth * ( (float)(windowswidth)/ (float)(originwidth) )  );
		image.getLayoutParams().height = (int) ( (float) imageheight * ( (float)(windowsheight)/ (float)(originheight) )  );
		
		posx = (int) ( (float) posx * ( (float)(windowswidth)/ (float)(originwidth) )  );  
		//posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight - HIGH_DPI_STATUS_BAR_HEIGHT ); 
		posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight); 
		margin.setMargins(posx , posy, 0, 0);
		image.setLayoutParams(margin);
	}
	
	
	public void ConvertLinearLayoutImage( ImageView image )
	{
    	Display defaultDisplay = ((WindowManager)m_Context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	
		int windowswidth = defaultDisplay.getWidth();
		int windowsheight= defaultDisplay.getHeight();
		int originwidth = 480;
		int originheight = 800;
		
		int imageheight = image.getLayoutParams().height;
		int imagewidth = image.getLayoutParams().width;
		
		LinearLayout.LayoutParams margin = (LinearLayout.LayoutParams )(image.getLayoutParams());
		
		int posx = margin.leftMargin;
		int posy = margin.topMargin;
		
	
		image.getLayoutParams().width = (int) ( (float) imagewidth * ( (float)(windowswidth)/ (float)(originwidth) )  );
		image.getLayoutParams().height = (int) ( (float) imageheight * ( (float)(windowsheight)/ (float)(originheight) )  );
		
		posx = (int) ( (float) posx * ( (float)(windowswidth)/ (float)(originwidth) )  );  
		//posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight - HIGH_DPI_STATUS_BAR_HEIGHT ); 
		posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight); 
		margin.setMargins(posx , posy, 0, 0);
		image.setLayoutParams(margin);
	}
	
	
	public void ConvertLinearLayoutScrollView( ScrollView image )
	{
    	Display defaultDisplay = ((WindowManager)m_Context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	
		int windowswidth = defaultDisplay.getWidth();
		int windowsheight= defaultDisplay.getHeight();
		int originwidth = 480;
		int originheight = 800;
		
		int imageheight = image.getLayoutParams().height;
		int imagewidth = image.getLayoutParams().width;
		
		LinearLayout.LayoutParams margin = (LinearLayout.LayoutParams )(image.getLayoutParams());
		
		int posx = margin.leftMargin;
		int posy = margin.topMargin;
		
	
		image.getLayoutParams().width = (int) ( (float) imagewidth * ( (float)(windowswidth)/ (float)(originwidth) )  );
		image.getLayoutParams().height = (int) ( (float) imageheight * ( (float)(windowsheight)/ (float)(originheight) )  );
		
		posx = (int) ( (float) posx * ( (float)(windowswidth)/ (float)(originwidth) )  );  
		//posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight - HIGH_DPI_STATUS_BAR_HEIGHT ); 
		posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight); 
		margin.setMargins(posx , posy, 0, 0);
		image.setLayoutParams(margin);
	}
	
	
	public void ConvertFrameLayoutScrollView( ScrollView image )
	{
    	Display defaultDisplay = ((WindowManager)m_Context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	
		int windowswidth = defaultDisplay.getWidth();
		int windowsheight= defaultDisplay.getHeight();
		int originwidth = 480;
		int originheight = 800;
		
		int imageheight = image.getLayoutParams().height;
		int imagewidth = image.getLayoutParams().width;
		
		FrameLayout.LayoutParams margin = (FrameLayout.LayoutParams )(image.getLayoutParams());
		
		int posx = margin.leftMargin;
		int posy = margin.topMargin;
		
	
		image.getLayoutParams().width = (int) ( (float) imagewidth * ( (float)(windowswidth)/ (float)(originwidth) )  );
		image.getLayoutParams().height = (int) ( (float) imageheight * ( (float)(windowsheight)/ (float)(originheight) )  );
		
		posx = (int) ( (float) posx * ( (float)(windowswidth)/ (float)(originwidth) )  );  
		//posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight - HIGH_DPI_STATUS_BAR_HEIGHT ); 
		posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight); 
		margin.setMargins(posx , posy, 0, 0);
		image.setLayoutParams(margin);
	}

	
	public void ConvertLinearLayoutEditText( EditText image )
	{
    	Display defaultDisplay = ((WindowManager)m_Context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	
		int windowswidth = defaultDisplay.getWidth();
		int windowsheight= defaultDisplay.getHeight();
		int originwidth = 480;
		int originheight = 800;
		
		int imageheight = image.getLayoutParams().height;
		int imagewidth = image.getLayoutParams().width;
		
		LinearLayout.LayoutParams margin = (LinearLayout.LayoutParams )(image.getLayoutParams());
		
		int posx = margin.leftMargin;
		int posy = margin.topMargin;
		
	
		image.getLayoutParams().width = (int) ( (float) imagewidth * ( (float)(windowswidth)/ (float)(originwidth) )  );
		image.getLayoutParams().height = (int) ( (float) imageheight * ( (float)(windowsheight)/ (float)(originheight) )  );
		
		posx = (int) ( (float) posx * ( (float)(windowswidth)/ (float)(originwidth) )  );  
		//posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight - HIGH_DPI_STATUS_BAR_HEIGHT ); 
		posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight); 
		margin.setMargins(posx , posy, 0, 0);
		image.setLayoutParams(margin);
	}
	
	
	public void ConvertLinearLayoutSpinner( Spinner image )
	{
    	Display defaultDisplay = ((WindowManager)m_Context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    	
		int windowswidth = defaultDisplay.getWidth();
		int windowsheight= defaultDisplay.getHeight();
		int originwidth = 480;
		int originheight = 800;
		
		int imageheight = image.getLayoutParams().height;
		int imagewidth = image.getLayoutParams().width;
		
		LinearLayout.LayoutParams margin = (LinearLayout.LayoutParams )(image.getLayoutParams());
		
		int posx = margin.leftMargin;
		int posy = margin.topMargin;
		
	
		image.getLayoutParams().width = (int) ( (float) imagewidth * ( (float)(windowswidth)/ (float)(originwidth) )  );
		image.getLayoutParams().height = (int) ( (float) imageheight * ( (float)(windowsheight)/ (float)(originheight) )  );
		
		posx = (int) ( (float) posx * ( (float)(windowswidth)/ (float)(originwidth) )  );  
		//posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight - HIGH_DPI_STATUS_BAR_HEIGHT ); 
		posy = (int) ( (float) posy * ( (float)(windowsheight)/ (float)(originheight) )  )- ( mStatusBarHeight); 
		margin.setMargins(posx , posy, 0, 0);
		image.setLayoutParams(margin);
	}
	
	
	
}
