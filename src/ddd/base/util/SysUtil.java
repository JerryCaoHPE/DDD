package ddd.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public class SysUtil {

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 功能：拼接分页时候的html代码
	 * author:hujun
	 * @param allCount	总的记录数
	 * @param currentPage	当前页数
	 * @param limit	每页显示记录数
	 * @param url	没有分页是的查询条件
	 * @return 拼接好了的html代码
	 */
	public static String createPage(int allCount,int currentPage,int limit,String url){
	  String pageStr = "";//存储最终拼接好的html
	  
	  if(allCount==0){
		  return "";
	  }
	  if(currentPage<=0){
		  currentPage = 1;
	  }
	  
	  synchronized (pageStr) {
	    int allPage = (int)Math.ceil((allCount*1.0)/limit);//计算出总有多少页
	    int i,j;//分页变量
	    String pageCnfig = url;
	    int index = pageCnfig.indexOf("?");//判断原来的url里面是否有参数，有参数就在原来的基础上添加，没有就加添加
	    
	    for(i = currentPage-1 ; i >= currentPage-4 ; i--){
	      pageCnfig = url;
	      if(i>=1){
	        if(index>0){
	          pageCnfig += "&currentPage="+i+"&limit="+limit;
	        }else{
	          pageCnfig += "?currentPage="+i+"&limit="+limit;
	        }
	        pageStr = "<span><a href='"+pageCnfig+"'>"+i+"</a></span>" + pageStr;
	      }else{
	        break;
	      }
	    }
	    
	    if(currentPage != 1){
	      pageCnfig = url;
	      if(index>0){
	        pageCnfig += "&currentPage="+(currentPage-1)+"&limit="+limit;
	      }else{
	        pageCnfig += "?currentPage="+(currentPage-1)+"&limit="+limit;
	      }
	      pageStr ="<span><a href='"+pageCnfig+"'>上一页</a></span>" + pageStr;
	    }
	    
	    for(j = currentPage ; j<=currentPage+4 ; j++){
	      pageCnfig = url;
	      if(j<=allPage){
	        if(index>0){
	          pageCnfig += "&currentPage="+j+"&limit="+limit;
	        }else{
	          pageCnfig += "?currentPage="+j+"&limit="+limit;
	        }
	        if(currentPage==j){
	          pageStr = pageStr + "<span><a href='"+pageCnfig+"' class='currentPage'>"+j+"</a></span>";
	        }else{
	          pageStr = pageStr + "<span><a href='"+pageCnfig+"'>"+j+"</a></span>";
	        }
	      }else{
	        break;
	      }
	      
	    }
	    
	    if(currentPage != allPage){
	      pageCnfig = url;
	      if(index>0){
	        pageCnfig += "&currentPage="+(currentPage+1)+"&limit="+limit;
	      }else{
	        pageCnfig += "?currentPage="+(currentPage+1)+"&limit="+limit;
	      }
	      pageStr += "<span><a href='"+pageCnfig+"'>下一页</a></span>";
	    }
	  }
	  
	  return pageStr;
	  
	}
	
	public static String formatDate(Date date){
		return format.format(date);
	}
	
	public static Date praseDate(String dateStr) throws ParseException{
		if(dateStr.length()==16){
			//有一种可能就是前端在选择时间的时候没有选择 秒，这这里我手动加上
			return format.parse(dateStr+":00");
		}else if(dateStr.length()==10){
			return format.parse(dateStr+" 00:00:00");
		}
		return format.parse(dateStr);
	}
	
	
	private static final String serverName = "recruitPortal";
	
	//找到对于客服机来说Servet的绝对路径
	public static String serverBasePath(HttpServletRequest request){
		return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/"+serverName;
	}
	
	public static void main(String[] args) throws ParseException {
		//System.out.println(praseDate("2014-01-14 00:00:06"));
		String code = "00010001";
		String parentCode = "0001";
		int preLevelLength = 4;
		int step = 1;
		//System.out.println(createTreeID(parentCode,code,preLevelLength,step));
		System.out.println(praseDate("2014-05-12 00:00:00"));
	}
	
	
	/**
	 * 创建树形编码
	 * @param parentCode 父亲的Code
	 * @param code 当前的code
	 * @param preLevelLength  每一级的长度
	 * @param step 每次增加的步长
	 * @return
	 */
	public static String createTreeID(String parentCode,String code,int preLevelLength,int step){
		/*当前code存在的时候*/
		boolean parentCodeIsNull = (parentCode==null || parentCode.equals(""));
		boolean codeIsNull = (code==null || code.equals(""));
		
		//当表里面的数据为空的时候
		if(parentCodeIsNull && codeIsNull){
			return String.format("%0"+preLevelLength+"d", 0+step);
		}
		
		//新建下一级
		if(!parentCodeIsNull && codeIsNull){
			return parentCode+String.format("%0"+preLevelLength+"d", 0+step);
		}
		
		//在现有的等级后面添加一个数据 
		
		int size = code.length();
		int level = size/preLevelLength;
		String preStr =  code.substring(0,(level-1)*preLevelLength);
		String needAddStr = code.substring((level-1)*preLevelLength);
		int toint = Integer.parseInt(needAddStr);
		String addResult = String.format("%0"+preLevelLength+"d", toint+step);
		return preStr+addResult;
	}
	
}
