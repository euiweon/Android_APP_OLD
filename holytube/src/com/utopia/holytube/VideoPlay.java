package com.utopia.holytube;


import com.euiweonjeong.base.BaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlay  extends BaseActivity {
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
        videoView.setMediaController(mediaController);
        videoView.setVideoPath(path);
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
