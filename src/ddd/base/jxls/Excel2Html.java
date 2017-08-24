package ddd.base.jxls;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.converter.ExcelToHtmlUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.util.XMLHelper;


public class Excel2Html {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		suite();
	}
	   public static void suite()
	    {
 

	        File directory = new File(
	                "H:\\Techology\\POI\\poi-src-3.12-20150511\\poi-3.12\\test-data\\spreadsheet\\ddd" );
	        for ( final File child : directory.listFiles( ))
	        {
	        	 try {
					testFo( child );
					 testHtml( child );
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	             
	        }
	    }	
    protected static void testFo( File child ) throws Exception
    {
        HSSFWorkbook workbook;
        try
        {
            workbook = ExcelToHtmlUtils.loadXls( child );
        }
        catch ( Exception exc )
        {
            // unable to parse file -- not WordToFoConverter fault
            return;
        }

        ExcelToHtmlConverter excelToHtmlConverter = new ExcelToHtmlConverter(
                XMLHelper.getDocumentBuilderFactory().newDocumentBuilder().newDocument() );
        excelToHtmlConverter.processWorkbook( workbook );

        StringWriter stringWriter = new StringWriter();

        Transformer transformer = TransformerFactory.newInstance()
                .newTransformer();
        transformer.setOutputProperty( OutputKeys.ENCODING, "utf-8" );
        transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
        transformer.setOutputProperty( OutputKeys.METHOD, "xml" );
        transformer.transform(
                new DOMSource( excelToHtmlConverter.getDocument() ),
                new StreamResult( stringWriter ) );
    }

    protected static void testHtml( File child ) throws Exception
    {
        HSSFWorkbook workbook;
        try
        {
            workbook = ExcelToHtmlUtils.loadXls( child );
        }
        catch ( Exception exc )
        {
        	System.out.println(child);
            exc.printStackTrace();
            return;
        }

        ExcelToHtmlConverter excelToHtmlConverter = new ExcelToHtmlConverter(
                XMLHelper.getDocumentBuilderFactory().newDocumentBuilder().newDocument() );
        excelToHtmlConverter.setOutputColumnHeaders(false);
        excelToHtmlConverter.setOutputRowNumbers(false);
        excelToHtmlConverter.setOutputSheetHeaders(false);
        excelToHtmlConverter.processWorkbook( workbook,1 );
       
        FileWriter fileWriter=new FileWriter(child.getAbsolutePath()+".html");

        Transformer transformer = TransformerFactory.newInstance()
                .newTransformer();
        transformer.setOutputProperty( OutputKeys.ENCODING, "utf-8" );
        transformer.setOutputProperty( OutputKeys.INDENT, "no" );
        transformer.setOutputProperty( OutputKeys.METHOD, "html" );
        transformer.transform(
                new DOMSource( excelToHtmlConverter.getDocument() ),
                new StreamResult( fileWriter ) );
        System.out.println(fileWriter.toString());
        
        fileWriter.flush();
        fileWriter.close();
    }
}
