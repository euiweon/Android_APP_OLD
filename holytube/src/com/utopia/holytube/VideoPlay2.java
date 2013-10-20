package com.utopia.holytube;


import com.euiweonjeong.base.BaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlay2  extends BaseActivity {
	private MyVideoView videoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoview);
        
        // 파일명을  넘겨 받는다 
        mainSingleton myApp = (mainSingleton) getApplication();
        String path = myApp.VideoPath;

        videoView = (MyVideoView)findViewById(R.id.videoView1);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        Uri file = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.holytube_logo_final);	
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(file);
        videoView.requestFocus();
        videoView.start();
 
    }

    
    @Override    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		videoView.stopPlayback();
    		videoView.clearFocus();
    		finish();
    	}
    	return false;
    }
}
