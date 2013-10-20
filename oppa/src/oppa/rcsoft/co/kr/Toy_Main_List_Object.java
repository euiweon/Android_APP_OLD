package oppa.rcsoft.co.kr;

import android.graphics.Bitmap;

public class Toy_Main_List_Object 
{

	private String gr_id;
	private String gr_name;
	private Bitmap gr_img;
	private String gr_profile;
	private String girl_update;
	private String c1_name;
	private String sh_name;

	
	public String getGrProfile()
	{
		return gr_profile;
	}
	
	public void setGrProfile(String profile)
	{
		this.gr_profile = profile;
	}
	
	public String getGrId()
	{
		return gr_id;
	}
	
	public void setGrId(String id)
	{
		this.gr_id = id;
	}
	
	public String getGrName()
	{
		return gr_name;
	}
	
	public void setGrName(String name)
	{
		this.gr_name = name;
	}
	
	public Bitmap getImg()
	{
		return gr_img;
	}
	
	public void setImg(Bitmap img)
	{
		this.gr_img = img;
	}
	
	public String getGirlUpdate()
	{
		return girl_update;
	}
	
	public void setGirlUpdate(String update)
	{
		this.girl_update = update;
	}
	
	public String getC1Name()
	{
		return c1_name;
	}
	
	public void setC1Name(String name)
	{
		this.c1_name = name;
	}
	
	public String getShName()
	{
		return sh_name;
	}
	
	public void setShName(String name)
	{
		this.sh_name = name;
	}

}
