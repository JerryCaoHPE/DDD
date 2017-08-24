package ddd.base.jxls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.jxls.common.Context;
import org.jxls.template.SimpleExporter;
import org.jxls.util.JxlsHelper;

import ddd.base.exception.DDDException;
import ddd.base.persistence.SessionFactory;
import ddd.simple.entity.listview.DisplayAttribute;
import ddd.simple.entity.listview.ReportView;
import ddd.simple.util.Excel2HtmlUtil;

public class JxlsGenerator extends AbstractTemplateGenerator
{
 
	
	public static void exportReportView2Excel(Set<Map<String, Object>>data ,ReportView reportView,OutputStream out ) throws IOException
	{
		
//		returnMap.put("totalCount", totalCount);
//		returnMap.put("result", this.listViewDao.getReportViewData(findSQL));
		String template = "/resource/simple_export_template1.xls";
        // now let's show how to register custom template
        InputStream is = JxlsGenerator.class.getResourceAsStream(template);
       // OutputStream os2 = new FileOutputStream("target/simple_export_output2.xlsx");
        SimpleExporter exporter = new SimpleExporter();
        exporter.registerGridTemplate(is);
        
        
        List<String> headers = new ArrayList<String>();
        StringBuilder fields = new StringBuilder();
        
        for(DisplayAttribute displayAttribute : reportView.getDisplayAttributes())
        {
        	headers.add(displayAttribute.getAttributeValue());
        	fields.append(displayAttribute.getAttributeName()).append(",");
        }
        exporter.gridExport(headers, data, fields.toString(), out);
        
        is.close();
	}
	
	public  void generateHtml(String templateFileName,Map<String,Object> params,HttpServletResponse response) throws FileNotFoundException
	{
		try
		{
			response.setContentType("text/html");
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			this.generateExcel(templateFileName, params, out);
			Excel2HtmlUtil.toHtml(new ByteArrayInputStream(out.toByteArray()), response.getOutputStream());
		}
		catch(Exception e)
		{
			throw new DDDException("通过Excel模板生成HTML文件出错，原因："+e.getMessage(),e);
		}
	}

	/* (非 Javadoc) 
	* <p>Title: generatePdf</p> 
	* <p>Description: </p> 
	* @param templateFileName
	* @param params
	* @param response
	* @throws FileNotFoundException 
	* @see ddd.base.jxls.TemplateGenerator#generatePdf(java.lang.String, java.util.Map, javax.servlet.http.HttpServletResponse) 
	*/
	@Override
	public void generatePdf(String templateFileName, Map<String, Object> params, HttpServletResponse response) throws FileNotFoundException
	{
		 
	}

	/* (非 Javadoc) 
	* <p>Title: generate</p> 
	* <p>Description: </p> 
	* @param templateFileName
	* @param params
	* @param response
	* @throws FileNotFoundException 
	* @see ddd.base.jxls.TemplateGenerator#generate(java.lang.String, java.util.Map, javax.servlet.http.HttpServletResponse) 
	*/
	@Override
	public void generate(String templateFileName, Map<String, Object> params, HttpServletResponse response) 
	{
		try
		{
			response.setContentType("application/vnd.ms-excel");
			this.generateExcel(templateFileName, params, response.getOutputStream());
		}
		catch(IOException e)
		{
			throw new DDDException("通过Excel模板生成excel文件出错，原因："+e.getMessage(),e);
		}
		 	
	}
	
	private void generateExcel(String templateFileName, Map<String, Object> params, OutputStream out) throws FileNotFoundException
	{
		params = this.getParams(templateFileName, params);
		
		Context context = new Context(params);

		Connection conn = SessionFactory.getThreadSession().getQueryConnection();
		ReportManager reportManager = new ReportManagerImpl(conn); 
		context.putVar("rmm", reportManager);
		
		try {
			InputStream is =  new FileInputStream(templateFileName);
			JxlsHelper.getInstance().processTemplate(is, out, context);
//			FileOutputStream outputStream = new FileOutputStream("D:\\angular\\workspace\\DDD3\\jxls\\resources\\org\\jxls\\demo\\jdbc_template_out.xls");
//			outputStream.write(out.toByteArray());
//			outputStream.close();t 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
