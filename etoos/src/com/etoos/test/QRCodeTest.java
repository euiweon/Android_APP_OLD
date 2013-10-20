package com.etoos.test;





import com.google.zxing.client.android.integration.IntentIntegrator;
import com.google.zxing.client.android.integration.IntentResult;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;



public class QRCodeTest extends EtoosBaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);
		
		ImageView btn = (ImageView)findViewById(R.id.logo);
        btn.setOnClickListener(mScan);



		AfterCreate();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// QR코드/바코드를 스캔한 결과 값을 가져옵니다.
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		
		// 결과값 출력
		new AlertDialog.Builder(this)
			.setTitle(R.string.app_name)
			.setMessage(result.getContents() + " [" + result.getFormatName() + "]")
			.setPositiveButton("확인", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			})
			.show();
	}
	public ImageView.OnClickListener mScan = new ImageView.OnClickListener() 
	{
		public void onClick(View v) 
		{
			switch(v.getId())
			{
				case R.id.logo:
					IntentIntegrator.initiateScan(QRCodeTest.this);
					break;
			}
	    }
	};






}
