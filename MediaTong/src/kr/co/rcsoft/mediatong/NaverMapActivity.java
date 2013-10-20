package kr.co.rcsoft.mediatong;

import android.graphics.Rect;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.euiweonjeong.base.BaseNaverMapActivity;

import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.NMapView.OnMapStateChangeListener;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager.OnCalloutOverlayListener;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;


public class NaverMapActivity extends BaseNaverMapActivity implements OnMapStateChangeListener , OnCalloutOverlayListener {
	
	
	// API-KEY
	public static final String API_KEY = "8641b3d08171d0fb0b2ca835bbdaa108";
	// 네이버 맵 객체
	NMapView mMapView = null;
	// 맵 컨트롤러
	NMapController mMapController = null;
	// 맵을 추가할 레이아웃
	LinearLayout MapContainer;
	
	NMapResourceProvider mMapViewerResourceProvider;
	NMapOverlayManager mOverlayManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navermap_layout);
		
		// 네이버 지도를 넣기 위한 LinearLayout 컴포넌트
		MapContainer = (LinearLayout) findViewById(R.id.naver_map_layout);

		// 네이버 지도 객체 생성
		mMapView = new NMapView(this);
		
		// 지도 객체로부터 컨트롤러 추출
		mMapController = mMapView.getMapController();

		// 네이버 지도 객체에 APIKEY 지정
		mMapView.setApiKey(API_KEY);

		// 생성된 네이버 지도 객체를 LinearLayout에 추가시킨다.
		MapContainer.addView(mMapView);

		// 지도를 터치할 수 있도록 옵션 활성화
		mMapView.setClickable(true);
		
		// 확대/축소를 위한 줌 컨트롤러 표시 옵션 활성화
		mMapView.setBuiltInZoomControls(true, null);	

		// 지도에 대한 상태 변경 이벤트 연결
		mMapView.setOnMapStateChangeListener(this);
		/******************* 지도 초기화 끝 ********************/
		
		
		/******************* 오버레이 관련 코드 시작 ********************/
		// 오버레이 리소스 관리객체 할당
		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

		// 오버레이 관리자 추가
		mOverlayManager = new NMapOverlayManager(this, mMapView, 
									mMapViewerResourceProvider);
		
		// 오버레이들을 관리하기 위한 id값 생성
		int markerId = NMapPOIflagType.PIN;

		// 표시할 위치 데이터를 지정한다. 마지막 인자가 오버레이를 인식하기 위한 id값
		NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
		poiData.beginPOIdata(1);
		AppManagement _AppManager = (AppManagement) getApplication();
		
		/*poiData.addPOIitem(Double.parseDouble(_AppManager.m_MapInfomation.Latitude), 
				Double.parseDouble(_AppManager.m_MapInfomation.Longitude), 
				_AppManager.m_MapInfomation.Name, markerId, 0);*/
		
		poiData.addPOIitem(Double.parseDouble(_AppManager.m_MapInfomation.Longitude), 
				Double.parseDouble(_AppManager.m_MapInfomation.Latitude), 
				_AppManager.m_MapInfomation.Name, markerId, 0);
		poiData.endPOIdata();

		// 위치 데이터를 사용하여 오버레이 생성
		NMapPOIdataOverlay poiDataOverlay 
			= mOverlayManager.createPOIdataOverlay(poiData, null);
		
		// id값이 0으로 지정된 모든 오버레이가 표시되고 있는 위치로
		// 지도의 중심과 ZOOM을 재설정
		poiDataOverlay.showAllPOIdata(0);
		
		// 오버레이 이벤트 등록
		mOverlayManager.setOnCalloutOverlayListener(this);
		/******************* 오버레이 관련 코드 끝 ********************/
	}

	/**
	 * 지도가 초기화된 후 호출된다.
	 * 정상적으로 초기화되면 errorInfo 객체는 null이 전달되며,
	 * 초기화 실패 시 errorInfo객체에 에러 원인이 전달된다
	 */
	public void onMapInitHandler(NMapView mapview, NMapError errorInfo) {
		if (errorInfo == null)
		{ 
			AppManagement _AppManager = (AppManagement) getApplication();

			// success
			mMapController.setMapCenter(
						new NGeoPoint(Double.parseDouble(_AppManager.m_MapInfomation.Longitude),
								Double.parseDouble(_AppManager.m_MapInfomation.Latitude)), 11);
		} else { // fail
			android.util.Log.e("NMAP", "onMapInitHandler: error=" 
						+ errorInfo.toString());
		}
	}

	/**
	 * 지도 레벨 변경 시 호출되며 변경된 지도 레벨이 파라미터로 전달된다.
	 */
	public void onZoomLevelChange(NMapView mapview, int level) {}

	/**
	 * 지도 중심 변경 시 호출되며 변경된 중심 좌표가 파라미터로 전달된다.
	 */
	public void onMapCenterChange(NMapView mapview, NGeoPoint center) {}

	/**
	 * 지도 애니메이션 상태 변경 시 호출된다.
	 * animType : ANIMATION_TYPE_PAN or ANIMATION_TYPE_ZOOM
	 * animState : ANIMATION_STATE_STARTED or ANIMATION_STATE_FINISHED
	 */
	public void onAnimationStateChange(
					NMapView arg0, int animType, int animState) {}

	public void onMapCenterChangeFine(NMapView arg0) {}
	
	
	/** 오버레이가 클릭되었을 때의 이벤트 */
	public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay arg0,
			NMapOverlayItem arg1, Rect arg2) {
		
		return new NMapCalloutBasicOverlay(arg0, arg1, arg2);

		/*Toast.makeText(this, arg1.getTitle(),
					Toast.LENGTH_SHORT).show();*/
		//return null;
	}
	
	
}
