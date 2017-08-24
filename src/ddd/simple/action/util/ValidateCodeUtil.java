package ddd.simple.action.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

public class ValidateCodeUtil {
	    //设置字母的大小,大小     
	    private static Font mFont = new Font("Times New Roman", Font.PLAIN, 30);     
	    private static int width = 100;
	    private static int height = 35;
	    private static Random random = new Random();
	    
	    static  Color getRandColor(int fc,int bc)     
	    {     
	        Random random = new Random();     
	        if(fc>255) fc=255;     
	        if(bc>255) bc=255;     
	        int r=fc+random.nextInt(bc-fc);     
	        int g=fc+random.nextInt(bc-fc);     
	        int b=fc+random.nextInt(bc-fc);     
	        return new Color(r,g,b);     
	    }     

	    //产生随机验证码
	   public static String getCodeStr()
	   {
		   char[] chars = {'0','1','2','3','4','5','6','7','8','9',
				   'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
				   'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
		                     };
		  StringBuilder sb = new StringBuilder();
		   for(int i=0;i<4;i++)
		   { 
			   int index=(int) Math.floor(Math.random()*chars.length);
			   sb.append(chars[index]);
		   }
		   return sb.toString();
	   }
	
      //绘制图片
	   public  static BufferedImage drawPic()
	   {
		    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);     
	        Graphics g = image.getGraphics();  
       
	        g.setColor(getRandColor(200,250));//字体颜色     
	        g.fillRect(1, 1, width-1, height-1);//填充矩形
	        g.setColor(new Color(102,102,102)); //图片背景色    
	        g.drawRect(0, 0, width-1, height-1);//绘制长方形     
	         
	        g.setColor(getRandColor(160,200));     
	    
	        //画随机线     
	        for (int i=0;i<155;i++)     
	        {     
	            int x = random.nextInt(width - 1);     
	            int y = random.nextInt(height - 1);     
	            int xl = random.nextInt(6) + 1;     
	            int yl = random.nextInt(12) + 1;     
	            g.drawLine(x,y,x + xl,y + yl);     
	        }     
	    
	        //从另一方向画随机线     
	        for (int i = 0;i < 70;i++)     
	        {     
	            int x = random.nextInt(width - 1);     
	            int y = random.nextInt(height - 1);     
	            int xl = random.nextInt(12) + 1;     
	            int yl = random.nextInt(6) + 1;     
	            g.drawLine(x,y,x - xl,y - yl);     
	        }  
	        g.dispose();
	        
	        return image;
	   }
	   //生成验证码图片二进制流
	   public static byte[] genCodePicBtye(String code)
	   {
		  BufferedImage image = drawPic();
		  Graphics g = image.getGraphics();  
		  
	      for(int i=0;i<code.length();i++)
	      {
	    	  g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));     
	          g.drawString(String.valueOf(code.charAt(i)),18*i+10,20); 
	          g.setFont(mFont); //设置字体   
	      }
	      g.dispose();//释放资源
	      ByteArrayOutputStream baos = new ByteArrayOutputStream();    
	      try {
			 ImageIO.write(image, "JPEG",baos);
			 byte[] bytes = baos.toByteArray(); 
			 baos.close();
			 return bytes;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	   }
}
