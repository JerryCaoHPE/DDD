/**
* @Title: Docx4jGenerator.java
* @Package ddd.base.docx4j
* @Description: TODO(用一句话描述该文件做什么)
* @author matao@cqrainbowsoft.com
* @date 2015年9月27日 下午11:33:17
* @version V1.0
*/
package ddd.base.docx4j;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import ddd.base.exception.DDDException;
import ddd.base.jxls.AbstractTemplateGenerator;

/**
 * 项目名称：DDD3
 * 类名称：Docx4jGenerator
 * 类描述：   
 * 创建人：DDD
 * 创建时间：2015年9月27日 下午11:33:17
 * 修改人：DDD
 * 修改时间：2015年9月27日 下午11:33:17
 * 修改备注：   
 * @version 1.0
 * Copyright (c) 2015  DDD
 */
public class Docx4jGenerator extends AbstractTemplateGenerator
{
	public  void generateHtml(String templateFileName,Map<String,Object> params,HttpServletResponse response)
	{
		
		DocxTemplateProcessor docxTemplateProcessor =  DocxTemplateProcessor.getInstance(templateFileName);
		try
		{
			long start =0;
//			start = System.currentTimeMillis();
//			docxTemplateProcessor.compile(templateFileName);
//			System.out.println("compile:"+(System.currentTimeMillis()-start));
			response.setContentType("application/pdf");
			start = System.currentTimeMillis();
			params = this.getParams(templateFileName, params);
			docxTemplateProcessor.generateHtml(params, response.getOutputStream());
			System.out.println("compile:"+(System.currentTimeMillis()-start));
			
			//docxTemplateProcessor.generate(datas,"D:\\angular\\workspace\\SWP\\OUT\\D__angular_workspace_DDD3_sample-docs_word_reportDemo1_docx\\2.docx");
		} catch (Exception e)
		{
			throw new DDDException("通过Excel模板生成HTML文件出错，原因："+e.getMessage(),e);
		}
		finally
		{
			DocxTemplateProcessor.releaseInstance(docxTemplateProcessor);
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
		
		DocxTemplateProcessor docxTemplateProcessor =  DocxTemplateProcessor.getInstance(templateFileName);
		try
		{
			long start =0;
//			start = System.currentTimeMillis();
//			docxTemplateProcessor.compile(templateFileName);
//			System.out.println("compile:"+(System.currentTimeMillis()-start));
			response.setContentType("text/html");
			start = System.currentTimeMillis();
			params = this.getParams(templateFileName, params);
			docxTemplateProcessor.generatePdf(params, response.getOutputStream());
			System.out.println("compile:"+(System.currentTimeMillis()-start));
			
			//docxTemplateProcessor.generate(datas,"D:\\angular\\workspace\\SWP\\OUT\\D__angular_workspace_DDD3_sample-docs_word_reportDemo1_docx\\2.docx");
		} catch (Exception e)
		{
			throw new DDDException("通过Excel模板生成HTML文件出错，原因："+e.getMessage(),e);
		}
		finally
		{
			DocxTemplateProcessor.releaseInstance(docxTemplateProcessor);
		}
		
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
	public void generate(String templateFileName, Map<String, Object> params, HttpServletResponse response) throws FileNotFoundException
	{
		
		DocxTemplateProcessor docxTemplateProcessor =  DocxTemplateProcessor.getInstance(templateFileName);
		try
		{
			long start =0;
//			start = System.currentTimeMillis();
//			docxTemplateProcessor.compile(templateFileName);
//			System.out.println("compile:"+(System.currentTimeMillis()-start));
			response.setContentType("application/msword");
			start = System.currentTimeMillis();
			params = this.getParams(templateFileName, params);
			docxTemplateProcessor.generate(params, response.getOutputStream());
			System.out.println("compile:"+(System.currentTimeMillis()-start));
			
			//docxTemplateProcessor.generate(datas,"D:\\angular\\workspace\\SWP\\OUT\\D__angular_workspace_DDD3_sample-docs_word_reportDemo1_docx\\2.docx");
		} catch (Exception e)
		{
			throw new DDDException("通过Excel模板生成HTML文件出错，原因："+e.getMessage(),e);
		}
		finally
		{
			DocxTemplateProcessor.releaseInstance(docxTemplateProcessor);
		}
		
	}	
}
