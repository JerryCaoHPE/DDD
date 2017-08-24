package ddd.simple.service.listview;

import java.io.IOException;  
import java.io.OutputStream;  
import java.io.UnsupportedEncodingException;  
import java.net.URLEncoder;  
  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
  
import org.apache.commons.codec.binary.Base64;  
import org.apache.poi.hssf.usermodel.HSSFSheet;  
import org.apache.poi.hssf.usermodel.HSSFWorkbook;  
import org.apache.poi.ss.usermodel.Row;  

/**
 * Servlet implementation class ExportListViewDataServlet
 */
public class ExportListViewDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExportListViewDataServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
        doPost(request, response);  
    }  
  
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
        response.setHeader("Connection", "close");  
        response.setContentType("octets/stream");  
        response.setHeader("Content-Type", "application/vnd.ms-excel;charset=UTF-8");  
        
        
        String filename = "会议记录清单.xls";  
        filename = encodeFileName(request, filename);  
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);  
          
        OutputStream out = response.getOutputStream();  
        out.close();  
    }  
      
    public  String encodeFileName(HttpServletRequest request,  
            String fileName) throws UnsupportedEncodingException {  
        String agent = request.getHeader("USER-AGENT");  
        if (null != agent && -1 != agent.indexOf("MSIE")) {  
            return URLEncoder.encode(fileName, "UTF-8");  
        } else if (null != agent && -1 != agent.indexOf("Mozilla")) {  
            return "=?UTF-8?B?"  
                    + (new String(Base64.encodeBase64(fileName  
                            .getBytes("UTF-8")))) + "?=";  
        } else {  
            return fileName;  
        }  
    }  

}
