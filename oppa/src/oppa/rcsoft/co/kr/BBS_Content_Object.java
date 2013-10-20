package oppa.rcsoft.co.kr;

import java.util.ArrayList;

import android.graphics.Bitmap;

// 게시판 뷰어 데이터 클래스 


public class BBS_Content_Object 
{

	public String  BBStype = "commu";
	public boolean isMainContent;
	public boolean isOverLapReply;
	
	public String mb_grade;
	public boolean isDelete = false;
	public Bitmap th_img = null;
	public boolean isLine = false;
	private Integer is_notice;		// 공지사항인지...
	private Integer wr_id;			// 게시물 번호
	private String ca_name;			// 카테고리 명
	private Integer reply_len;		// 답글 단계 ( 별 쓸모 없는듯 ...)
	private Integer is_secret;		// 비밀글인지 
	private String wr_subject;		// 제목
	private String wr_content;		// 내용
	private String wr_name;			// 작성자
	private String mb_id;			// 작성자 아이디 
	private String wr_datetime;		// 작성 시점
	private Integer wr_hit;			// 조회수
	private String wr_5;			// 업체 아이디
	private Integer wr_6;			// 평점
	private String wr_8;			// 신고된 게시물
	private Integer comment_cnt;	// 댓글 갯수 
	private ArrayList<Bitmap>  image;		// 첨부파일 이미지 
	public Integer cmt_id;				// 댓글 고유번호
	
	
	public Integer getIsNotice()
	{
		return is_notice;
	}
	
	public void setIsNotice(int notice)
	{
		this.is_notice = notice;
	}
	
	public Integer getWrId()
	{
		return wr_id;
	}
	
	public void setWrId(int id)
	{
		this.wr_id = id;
	}
	
	public String getCaName()
	{
		return ca_name;
	}
	
	public void setCaName(String name)
	{
		this.ca_name = name;
	}
	
	public Integer getReplyLen()
	{
		return reply_len;
	}
	
	public void setReplyLen(int len)
	{
		this.reply_len = len;
	}
	
	public Integer getIsSecret()
	{
		return is_secret;
	}
	
	public void setIsSecret(int secret)
	{
		this.is_secret = secret;
	}
	
	public String getWrSubject()
	{
		return wr_subject;
	}
	
	public void setWrSubject(String subject)
	{
		this.wr_subject = subject;
	}
	
	public String getWrContent()
	{
		return wr_content;
	}
	
	public void setWrContent(String content)
	{
		this.wr_content = content;
	}
	
	public String getWrName()
	{
		return wr_name;
	}
	
	public void setWrName(String name)
	{
		this.wr_name = name;
	}
	
	public String getMbId()
	{
		return mb_id;
	}
	
	public void setMbId(String id)
	{
		this.mb_id = id;
	}
	
	public String getWrDatetime()
	{
		return wr_datetime;
	}
	
	public void setWrDatetime(String time)
	{
		this.wr_datetime = time;
	}
	
	public Integer getWrHit()
	{
		return wr_hit;
	}
	
	public void setWrHit(int hit)
	{
		this.wr_hit = hit;
	}
	
	public String getWr5()
	{
		return wr_5;
	}
	
	public void setWr5(String no)
	{
		this.wr_5 = no;
	}
	
	public Integer getWr6()
	{
		return wr_6;
	}
	
	public void setWr6(int no)
	{
		this.wr_6 = no;
	}
	
	public String getWr8()
	{
		return wr_8;
	}
	
	public void setWr8(String no)
	{
		this.wr_8 = no;
	}
	
	public Integer getCommentCnt()
	{
		return comment_cnt;
	}
	
	public void setCommentCnt(int cnt)
	{
		this.comment_cnt = cnt;
	}
	
	public ArrayList<Bitmap> getBitmap()
	{
		return image;
	}
	
	public void setBitmap(ArrayList<Bitmap> cnt)
	{
		this.image = cnt;
	}
	


}
