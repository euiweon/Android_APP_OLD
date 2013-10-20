package oppa.rcsoft.co.kr;

import android.graphics.Bitmap;

// 업소 정보 관련 클래스 
public class Shop_Main_List_Object 
{
	private String sh_id;
	private String sh_name;
	private String c1_name;
	private String c2_name;
	private String sh_address;
	private String sh_eval_point;
	private Bitmap sh_img;
	private Integer distance;
	private String sh_tel;
	private String sh_profile;
	private String sh_eval_cnt;
	
	private int m_height = 0;
	
	public Integer getHeight()
	{
		return m_height;
	}
	
	public void setHeight(int cnt)
	{
		this.m_height = cnt;
	}
	
	
	public String getShId()
	{
		return sh_id;
	}
	
	public void setShId(String id)
	{
		this.sh_id = id;
	}
	
	public String getShName()
	{
		return sh_name;
	}
	
	public void setShName(String name)
	{
		this.sh_name = name;
	}
	
	public String getShAddress()
	{
		return sh_address;
	}
	
	public void setShAddress(String address)
	{
		this.sh_address = address;
	}
	
	public String getShEvalPoint()
	{
		return sh_eval_point;
	}
	
	public void setShEvalPoint(String eval_point)
	{
		this.sh_eval_point = eval_point;
	}
	
	public Bitmap getShImg()
	{
		return sh_img;
	}
	
	public void setShImg(Bitmap img)
	{
		this.sh_img = img;
	}
	
	public Integer getDistance()
	{
		return distance;
	}
	
	public void setDistance(int distance)
	{
		this.distance = distance;
	}
	
	public String getShTel()
	{
		return sh_tel;
	}
	
	public void setShTel(String tel)
	{
		this.sh_tel = tel;
	}
	
	public String getShProfile()
	{
		return sh_profile;
	}
	
	public void setShProfile(String profile)
	{
		this.sh_profile = profile;
	}
	
	public String getEvalCnt()
	{
		return sh_eval_cnt;
	}
	
	public void setEvalCnt(String string)
	{
		this.sh_eval_cnt = string;
	}
	
	public String getC1Name()
	{
		return c1_name;
	}
	
	public void setC1Name(String name)
	{
		this.c1_name = name;
	}
	
	
	public String getC2Name()
	{
		return c2_name;
	}
	
	public void setC2Name(String name)
	{
		this.c2_name = name;
	}
	
}
