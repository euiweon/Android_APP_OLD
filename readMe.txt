안드로이드 어플용 Base 프레임워크입니다. 

- 제작동기
안드로이드 어플들을 외주 받다보니 비슷한 내용으로 만들어 쓰는 기능들이 많아서,
'몇몇 기능 몇몇 기능을 라이브러리화 시켜볼까?' 라고 해서 만든 기능이 이 프레임 워크 입니다. 

-설명 

각 파일의 기능을 설명 하도록 합니다. 

View 관련

CTextView - 텍스트의 줄바꿈을 공백기준이 아닌 한글자 단위로 한다.( 인터넷참조 )
CustomScrollView - HorizontalScrollView 와 ScrollView 같이 사용하기 위한 클래스입니다. (http://stackoverflow.com/questions/2646028/android-horizontalscrollview-within-scrollview-touch-handling)
GalleryForOneFling - Gallery에서 이미지 하나씩 스크롤링 하기 위한 커스텀 클래스입니다 ( 인터넷 참조 )
MyVideoView - VideoView를 전체회면으로 보기위한 커스텀 클래스
ObservableScrollView - 스크롤 리스너에게 스크롤이 되고 있는지 통지 해주는 기능이 들어간 커스텀스크롤뷰 클래스( 구글 소스 참조 )
ScrollViewListener - ObservableScrollView 와 관련된 리스너


컨트롤 및 레이아웃 각종 유틸리티 관련
AnimationButton - 
AniTouchLinearLayout -  
BaseActivity - Activity에 몇몇 로그를 추가한 클래스입니다.
BaseActivityGroup - ActivityGroup에 로그를 추가한 클래스 입니다.  테스트용으로 작성되었으며 사용하지 않습니다. 
BaseListActivity - ListActivity에 로그를 추가한 클래스입니다. 처음에는 사용했지만, 리스트뷰와 스크롤뷰의 활용법을 익히게 되어 현재는 사용하지 않는 클래스 입니다.
BaseTabActivity - TabActivity 에 로그를 추가한 클래스입니다. 처음에는 사용했지만, 그냥 버튼 형태로 사용하고, View를 hide 시키고 Visible 시키는것이 편해서,  현재는 사용하지 않는 클래스 입니다.
BitmapManager - 웹을 통해서 그림파일을 가져와야 할경우, 사용하는 클래스 입니다. 그림파일이 다운로드가 완료되면 ImageView에 그림이 보여집니다. (인터넷 참조)
CookieHTTP - 웹관련 클래스입니다. HTTP를 사용해서 하는 작업의 경우, 모두 이클래스를 통해 이루어 집니다. ( apache-mime4j-core-0.7.jar, httpmime-4.1.1.jar 필요 )
CustomMultipartEntity - 파일 전송 콴련 클래스입니다. 기능은 완성되어 있으나 테스트를 제대로 하지 못했습니다.
ImageViewDoubleTab - ImageView에 더블탭을을 했을경우에 대한 이벤트를 추가한 클래스입니다. 
ImageViewDoubleTabListener - ImageViewDoubleTab 와 관련된 리스너 입니다. 
KakaoLink - 카카오톡에 접근할수 있는 클래스입니다( 카카오톡 제공 )
RangeSeekBar - 범위 지정 SeekBar입니다. 최소 최대값을 조정할수 있습니다. ( 인터넷 참조 )
RecycleUtils - 메모리 관련 클래스입니다 ( 인터넷 참조 )
StoryLink - 카카오 스토리에 접근할수 있는 클래스 입니다.( 카카오 스토리 개발자 사이트에서 소스 가져옴 )
UISizeConverter - 안드로이드 기기마다 해상도가 다르기 때문에 그에 맞춰 View의 크기 변환을 해주는 클래스 입니다. 일부 제대로 동작하지 않는 기기가 있습니다. ( 소니 엑스페리아시리즈 )



Slidingmenu 관련
페이스북의 슬라이딩 메뉴를 구현한 클래스입니다. ( 인터넷에서 가져옴 )