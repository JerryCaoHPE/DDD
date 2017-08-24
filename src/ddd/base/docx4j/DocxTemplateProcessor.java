/*
 *  Copyright 2007-2008, Plutext Pty Ltd.
 *   
 *  This file is part of docx4j.

    docx4j is licensed under the Apache License, Version 2.0 (the "License"); 
    you may not use this file except in compliance with the License. 

    You may obtain a copy of the License at 

        http://www.apache.org/licenses/LICENSE-2.0 

    Unless required by applicable law or agreed to in writing, software 
    distributed under the License is distributed on an "AS IS" BASIS, 
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
    See the License for the specific language governing permissions and 
    limitations under the License.

 */

package ddd.base.docx4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.docx4j.Docx4J;
import org.docx4j.Docx4jProperties;
import org.docx4j.TextUtils;
import org.docx4j.TraversalUtil;
import org.docx4j.XmlUtils;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.dml.picture.Pic;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.finders.ClassFinder;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.model.fields.FieldUpdater;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.io3.Load3;
import org.docx4j.openpackaging.io3.Save;
import org.docx4j.openpackaging.io3.stores.UnzippedPartStore;
import org.docx4j.openpackaging.io3.stores.ZipPartStore;
import org.docx4j.openpackaging.packages.OpcPackage;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Body;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

import ddd.base.docx4j.pool.DocxTemplateProcessorPoolableFactory;
import ddd.base.docx4j.wml.VmDirective;
import ddd.base.util.FileUtil;
import ddd.base.util.SpringContextUtil;
import ddd.base.util.VelocityUtil;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.systemConfig.SystemConfigService;

/**
 * To see what parts comprise your docx, try the PartsList sample.
 * 
 * There will always be a MainDocumentPart, usually called document.xml. This
 * sample shows you what objects are in that part.
 * 
 * It also shows a general approach for traversing the JAXB object tree in the
 * Main Document part. It can also be applied to headers, footers etc.
 * 
 * It is an alternative to XSLT, and doesn't require marshalling/unmarshalling.
 * 
 * If many cases, the method getJAXBNodesViaXPath would be more convenient, but
 * there are 3 JAXB bugs which detract from that (see Getting Started).
 * 
 * See related classes SingleTraversalUtilVisitorCallback and
 * CompoundTraversalUtilVisitorCallback
 * 
 * @author jharrop
 *
 */
public class DocxTemplateProcessor  {

	private static GenericKeyedObjectPool<String, DocxTemplateProcessor> docxTemplateProcessorPool;
	public static JAXBContext context ;
	static
	{
		context = org.docx4j.jaxb.Context.jc;
		
		DocxTemplateProcessorPoolableFactory keyFactory 
		= new DocxTemplateProcessorPoolableFactory();
		docxTemplateProcessorPool = new GenericKeyedObjectPool<String, DocxTemplateProcessor>(keyFactory);
		docxTemplateProcessorPool.setMaxTotalPerKey(1);
		docxTemplateProcessorPool.setMaxTotal(10);
	}
	private String sourceImaBaseImagePath = "C:\\java\\upload\\";
	private String targetBasePath ="";
	private String workPath = "C:\\angular\\OUT\\";
	
	
	
	
	SystemConfigService systemConfig = (SystemConfigService) SpringContextUtil.getBean("systemConfigServiceBean");
	public static void main(String[] args) throws Exception {
		
		long start =0;
		
//		FileUtils.copyDirectory(new File("D:\\angular\\workspace\\SWP\\OUT\\1"), new File("d:\\b"));
//		System.out.println("copyDirectory:"+(System.currentTimeMillis()-start));
		
// 
//		
//		docxTemplateProcessor.openUnzippedAndSaveZipped("D:\\angular\\workspace\\SWP\\OUT\\VariableClean", "D:\\angular\\workspace\\SWP\\OUT\\1.docx");
//		
//		return ;
	
		String inputfilepath = "D:\\angular\\workspace\\DDD3\\sample-docs\\word\\reportDemo1.docx";
		//String targetPath = "D:\\angular\\workspace\\SWP\\OUT\\1";
		String outputFile = "D:\\angular\\workspace\\SWP\\out_VariableClean.docx";
		
		DocxTemplateProcessor docxTemplateProcessor =  DocxTemplateProcessor.getInstance(inputfilepath);
				
		start = System.currentTimeMillis();
		docxTemplateProcessor.compile(inputfilepath);
		System.out.println("compile:"+(System.currentTimeMillis()-start));
		
		Map<String,Object> datas = docxTemplateProcessor.genTestData();
 
		
		start = System.currentTimeMillis();
		String targetPath = docxTemplateProcessor.getCompiledPath()+File.separator+"word\\1.docx";
		 
		docxTemplateProcessor.generate( datas, targetPath);
		System.out.println("generate:"+(System.currentTimeMillis()-start));
//		
//
//		start = System.currentTimeMillis();
//		OutputStream outputStream = new FileOutputStream(new File("D:\\angular\\workspace\\SWP\\out_VariableClean.pdf"));
//		docxTemplateProcessor.generatePdf(targetPath, datas, outputStream);
//		outputStream.close();
//		System.out.println("generateHTML:"+(System.currentTimeMillis()-start));
//
//		start = System.currentTimeMillis();
//		 outputStream = new FileOutputStream(new File("D:\\angular\\workspace\\SWP\\out_VariableClean.pdf"));
//		docxTemplateProcessor.generatePdf(targetPath, datas, outputStream);
//		outputStream.close();
//		System.out.println("generateHTML:"+(System.currentTimeMillis()-start));
//		
//		start = System.currentTimeMillis();
//		 outputStream = new FileOutputStream(new File("D:\\angular\\workspace\\SWP\\out_VariableClean.html"));
//		docxTemplateProcessor.generateHtml(targetPath, datas, outputStream);
//		outputStream.close();
//		System.out.println("generatePdf:"+(System.currentTimeMillis()-start));	
		docxTemplateProcessorPool.returnObject(inputfilepath, docxTemplateProcessor);
	}
	public DocxTemplateProcessor(String templateFileNameName) {
		super();
		this.templateFileName = templateFileNameName;
		
		
		String workPath = this.systemConfig.findSystemConfigValueBykey("workPath");
		if(workPath != null && "".equals(workPath)){
			this.workPath = workPath;
		}
		
		this.compiledPath =this.workPath +File.separator+ this.templateFileName.replace('\\', '_').replace('.', '_').replace(':', '_')+File.separator;
		this.compiledtemplateFileName = this.getCompiledPath()+File.separator+"1.docx";
	}
	private static DocxTemplateProcessor docxTemplateProcessor ;
	public static DocxTemplateProcessor getInstance(String templateFileNameName)
	{
		try {
			DocxTemplateProcessor docxTemplateProcessor = docxTemplateProcessorPool.borrowObject(templateFileNameName, 3000);
			return docxTemplateProcessor;
		} catch (Exception e) {
			docxTemplateProcessorPool.returnObject(templateFileNameName, docxTemplateProcessor);
			System.out.println("从池中得到word处理对象出错，原因是："+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	public static void releaseInstance(DocxTemplateProcessor docxTemplateProcessor)
	{
		try {
			docxTemplateProcessorPool.returnObject(docxTemplateProcessor.templateFileName, docxTemplateProcessor);
		} catch (Exception e) {
			System.out.println("归还word处理对象到池中出错，原因是："+e.getMessage());
			e.printStackTrace();
		}
	}	
	private String templateFileName;
	public void generate(Map<String,Object> datas,String outputFile) throws Exception {
		this.generate( datas);
		String targetPath = this.getCompiledPath()+File.separator+"out";
		this.openUnzippedAndSaveZipped(targetPath, outputFile);
	}
	public void generate(Map<String,Object> datas,OutputStream outputStream) throws Exception {
		
		this.generate( datas);
		String targetPath = this.getCompiledPath()+File.separator+"out";
		this.openUnzippedAndSaveZipped(targetPath, outputStream);
	}	
	public void generate(Map<String,Object> datas) throws Exception {
		
		File compiledtemplateFile =new File( this.getCompiledtemplateFileName());
		File templateFile = new File(templateFileName);
		
		if(compiledtemplateFile.exists() == false || templateFile.lastModified() != compiledtemplateFile.lastModified())
		{
			this.compile(this.templateFileName);
		}
		String compiledPath = this.getCompiledPath()+File.separator+"compiled";
		String targetPath = this.getCompiledPath()+File.separator+"out";
		
		FileUtils.copyDirectory(new File(compiledPath), new File(targetPath));
		
		String xml = FileUtil.readeString(new File(targetPath + "/word/document.xml"));
		xml = this.velocityMerge(xml,datas);
		FileUtils.writeStringToFile(new File(targetPath + "/word/document.xml"), xml, "UTF-8");
		//FileUtil.writeToFile(new File(targetPath + "/word/document.xml"), xml);
	}	
	public void generateHtml(Map<String,Object> datas,OutputStream outputStream) throws Exception {
		ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
		this.generate( datas,outputStream2);
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream2.toByteArray());
		WordprocessingMLPackage wordprocessingMLPackage = WordprocessingMLPackage.load(inputStream);

		this.convert2Html( wordprocessingMLPackage, outputStream);
	}
	public void generateHtml(Map<String,Object> datas) throws Exception {
		 
		String targetPath = this.getCompiledPath()+File.separator+"html"+File.separator+"1.html";
		OutputStream outputStream  = new FileOutputStream(targetPath);
		
		this.generateHtml(datas, outputStream);
	}	
	public void generatePdf(Map<String,Object> datas) throws Exception 
	{
		String targetPath = this.getCompiledPath()+File.separator+"pdf"+File.separator+"1.pdf";
		OutputStream outputStream  = new FileOutputStream(targetPath);
		
		this.generatePdf( datas, outputStream);
	}
	public void generatePdf(Map<String,Object> datas,OutputStream outputStream) throws Exception {
		ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
		this.generate( datas,outputStream2);
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream2.toByteArray());
		WordprocessingMLPackage wordprocessingMLPackage = WordprocessingMLPackage.load(inputStream);
		this.convert2Pdf(wordprocessingMLPackage, outputStream);
	}	
	private void openUnzippedAndSaveZipped(String sourcePath,String outputFile) throws Exception {
		
		// Save it zipped
		File docxFile = new File(outputFile); 
		FileUtils.forceMkdir(new File( FilenameUtils.getFullPath(outputFile)));
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(docxFile);
			this.openUnzippedAndSaveZipped(sourcePath, fos);
		} catch (FileNotFoundException e) {
			throw new Docx4JException("Couldn't save " + docxFile.getPath(), e);
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}
	private OpcPackage openUnzipped(String sourcePath) throws Exception {
		// Load the docx
		File baseDir = new File(sourcePath); 
		UnzippedPartStore partLoader = new UnzippedPartStore(baseDir);
		final Load3 loader = new Load3(partLoader);
		OpcPackage opc = loader.get();
		return opc;
	}
	private void openUnzippedAndSaveZipped(String sourcePath,OutputStream outputStream) throws Exception {
		// Load the docx
		OpcPackage opc = this.openUnzipped(sourcePath);
		 
		ZipPartStore zps = new ZipPartStore();
		zps.setSourcePartStore(opc.getSourcePartStore());
		
		Save saver = new Save(opc, zps);
		FileOutputStream fos = null;
		try {
			saver.save(outputStream);
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}	
	private String compiledPath;
	private String getCompiledPath()
	{
		return this.compiledPath;
	}
	private String compiledtemplateFileName;
	private String getCompiledtemplateFileName()
	{
		return this.compiledtemplateFileName;
	}	
	public void compile(String templateFileName) throws Exception {

		
		String targetPath = this.getCompiledPath()+File.separator+"compiled";
		String targetFile = this.getCompiledtemplateFileName();
		FileUtils.forceMkdir(new File(targetPath));
		
		FileUtils.copyFile(new File(templateFileName), new File(targetFile),true);
		
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage
				.load(new java.io.File(templateFileName));
		MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

		org.docx4j.wml.Document wmlDocumentEl = (org.docx4j.wml.Document) documentPart
				.getJaxbElement();
		Body body = wmlDocumentEl.getBody();

		this.compileImages( body);
		
		this.compileVmDirectives(body);
		
		this.compileTables(body);
		
		// Save it unzipped
		File baseDir = new File(targetPath);
		baseDir.mkdir();

		UnzippedPartStore ups = new UnzippedPartStore(baseDir);
		ups.setSourcePartStore(wordMLPackage.getSourcePartStore());
		Save saver = new Save(wordMLPackage, ups);

		saver.save(null);
		
		String xml = FileUtil.readeString(new File(targetPath + "/word/document.xml"));
		
		xml = this.cleanVMD(xml);
		
		FileUtil.writeToFile(new File(targetPath + "/word/document.xml"), xml);
		
		this.handleContentTypes();
		
		//this.velocityMerge( xml);
	}

	// //#tables(student,students,4)
	private String getPara(String params, int paramIndex) {
		int startIndex = params.indexOf("(");
		int endIndex = params.lastIndexOf("");
		params = params.substring(startIndex + 1, endIndex-1);
		String[] paramList = params.split(",");
		return paramList[paramIndex];
	}
	private void compileVmDirectives(Object body)
	{
		List<P> paragraphs = this.getAllElementFromObject(body, P.class);

		for (Object paragraph : paragraphs) {
			P paragraph1 = (P) paragraph;

			String text = this.extractText(paragraph1);

			if (text.indexOf("$") != -1) {
				cleanVariable((P) paragraph);
			}
			if (text.indexOf("#") != -1) {
				handleVMDirective((P) paragraph, text);
			}
			System.out.println(text);
		}		
	}
	private void compileImages(Object body)
	{
		List<Drawing> drawings = this.getAllElementFromObject(body, Drawing.class);
		String str = null;
		for(Drawing drawing : drawings)
		{
			Inline inline = (Inline)drawing.getAnchorOrInline().get(0);
			String descr = inline.getDocPr().getDescr();
			System.out.println(descr); 
			JAXBElement<Pic> picElement =null;
			try
			{
				picElement = (JAXBElement<Pic>) (inline.getGraphic().getGraphicData().getAny().get(0));
				str = picElement.getValue().getBlipFill().getBlip().getEmbed();
			}
			catch(Exception e)
			{
				break;
				//如果不是图片 Drawing，则这个地方要报错，以未来的升级中做处理
			}
			System.out.println(str);
			if(descr != null && descr.startsWith("#image"))
			{
				picElement.getValue().getBlipFill().getBlip().setEmbed("$dddImageIndex");
			}
		}
		System.out.println(drawings);
		
	}
	private void handleContentTypes() throws IOException
	{
		String targetPath = this.getCompiledPath()+File.separator+"compiled";
		
		String contentTypesFileName = targetPath+File.separator+"[Content_Types].xml";
		
		String contentTypes = FileUtils.readFileToString(new File(contentTypesFileName));
		StringBuilder newContentTypes = new StringBuilder();
		if(contentTypes.indexOf("<Default Extension=\"emf\" ContentType=\"image/x-emf\"/>") == -1)
		{
			newContentTypes.append("<Default Extension=\"emf\" ContentType=\"image/x-emf\"/>\n");
		}
		if(contentTypes.indexOf("<Default Extension=\"png\" ContentType=\"image/png\"/>") == -1)
		{
			newContentTypes.append("<Default Extension=\"png\" ContentType=\"image/png\"/>\n");
		}		
		if(contentTypes.indexOf("<Default Extension=\"jpg\" ContentType=\"image/jpeg\"/>") == -1)
		{
			newContentTypes.append("<Default Extension=\"jpg\" ContentType=\"image/jpeg\"/>\n");
		}
		if(contentTypes.indexOf("<Default Extension=\"jpeg\" ContentType=\"image/jpeg\"/>") == -1)
		{
			newContentTypes.append("<Default Extension=\"jpeg\" ContentType=\"image/jpeg\"/>\n");
		}
		if(contentTypes.indexOf("<Default Extension=\"bmp\" ContentType=\"image/bmp\"/>") == -1)
		{
			newContentTypes.append("<Default Extension=\"bmp\" ContentType=\"image/bmp\"/>\n");
		}	
		if(contentTypes.indexOf("<Default Extension=\"gif\" ContentType=\"image/gif\"/>") == -1)
		{
			newContentTypes.append("<Default Extension=\"gif\" ContentType=\"image/gif\"/>\n");
		}
		int startIndex = contentTypes.indexOf("</Types>");
		newContentTypes.insert(0, contentTypes.substring(0, startIndex)).append(contentTypes.substring(startIndex));
		FileUtils.writeStringToFile(new File(contentTypesFileName), newContentTypes.toString());
	}
	private void compileTables(Object body)
	{
		List<Tbl> tables = this.getAllElementFromObject(body, Tbl.class);

		for (Object tableObject : tables) {
			Tbl table = (Tbl) tableObject;
			List<Tr> rows = this.getAllElementFromObject(tableObject, Tr.class);
			// #tables(student,students,4)
			for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
				Tr row = rows.get(rowIndex);

				boolean rowHandled = false;

				List<Tc> cells = this.getAllElementFromObject(row,
						Tc.class);

				for (Tc cell : cells) {
					List<VmDirective> vmDirectives = this.getAllElementFromObject(cell,
							VmDirective.class);
					for (VmDirective vmDirective1 : vmDirectives) {
						cell.getContent().remove(vmDirective1);
						String cellText = vmDirective1.getValue();
						if (cellText.startsWith("#table")) 
						{
							String foreachDirecText = "#foreach("
									+ this.getPara(cellText, 0) + " in "
									+ this.getPara(cellText, 1) + ")";
							int endRowIndex = Integer.parseInt(this.getPara(
									cellText, 2));

							VmDirective vmDirective = new VmDirective();
							vmDirective.setValue(foreachDirecText);

							ContentAccessor contentAccessor = (ContentAccessor) row
									.getParent();
							contentAccessor.getContent().add(
									contentAccessor.getContent().indexOf(row),
									vmDirective);

							Tr endRow = rows.get(endRowIndex);

							vmDirective = new VmDirective();
							vmDirective.setValue("#end");

							contentAccessor = (ContentAccessor) row.getParent();
							contentAccessor.getContent().add(
									contentAccessor.getContent()
											.indexOf(endRow) + 1, vmDirective);

							rowHandled = true;
							break;
						}
					}
					if(rowHandled) break;
				}
			}
		}		
	}
	private String extractText(Object o) {

		StringWriter stringWriter = new StringWriter();
		try {
			TextUtils.extractText(o, stringWriter);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		String text = stringWriter.toString();
		return text;
	}

	public List<Object> getChildren(Object o) {
		return TraversalUtil.getChildrenImpl(o);
	}

	public void handleVMDirective(P paragraph, String text) {
		if (paragraph.getParent() instanceof ContentAccessor) {
			ContentAccessor contentAccessor = (ContentAccessor) paragraph
					.getParent();

			VmDirective vmDirective = new VmDirective();
			vmDirective.setValue(text);

			contentAccessor.getContent().set(
					contentAccessor.getContent().indexOf(paragraph),
					vmDirective);
		}
	}

	private void cleanVariable(P parent) {
		List children = getChildren(parent);
		int firstIndex = -1;
		int lasIndex = -1;
		String variableName = "";
		if (children != null) {

			for (int i = 0; i < children.size(); i++) {
				Object o = children.get(i);
				o = XmlUtils.unwrap(o);
				if (o instanceof R) {
					R r = (R) o;
					Text textElement = this.getRText(r);

					if (textElement != null && textElement.getValue() != null
							&& textElement.getValue().length() > 0) {
						if (firstIndex == -1) {
							if (textElement.getValue().indexOf("$") >= 0) {
								firstIndex = i;
								variableName = "";
							}
						}
						if (firstIndex >= 0) {
							variableName += textElement.getValue();
							if (textElement.getValue().indexOf("}") >= 0) {
								lasIndex = i;
							}
						}
						if (firstIndex >= 0 && lasIndex >= 0) {
							R firstR = (R) children.get(firstIndex);
							Text firstTextElement = this.getRText(firstR);
							firstTextElement.setValue( variableName);
							this.removeElements(children, firstIndex + 1,
									lasIndex);
							firstIndex = -1;
							lasIndex = -1;

						}
					}
				}
			}
		}
	}

	private void removeElements(List elements, int startIndex, int endIndex) {
		if (startIndex < 0 || startIndex >= elements.size() || endIndex < 0
				|| endIndex >= elements.size() || startIndex > endIndex) {
			return;
		}
		for (int i = endIndex; i >= startIndex; i--) {
			elements.remove(i);
		}
	}

	private Text getRText(R r) {
		List children = getChildren(r);
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				Object o = children.get(i);
				o = XmlUtils.unwrap(o);
				if (o instanceof Text) {
					return (Text) o;
				}
			}
		}
		return null;
	}

	private <T> List<T> getAllElementFromObject(Object obj, Class<T> toSearch) {
		ClassFinder finder = new ClassFinder(toSearch); // <----- change this to
														// suit
		new TraversalUtil(obj, finder);
		return (List<T>) finder.results;
	}

	private void replaceParagraph(String placeholder, String textToAdd,
			WordprocessingMLPackage template, ContentAccessor addTo) {
		// 1. get the paragraph
		List<P> paragraphs = getAllElementFromObject(
				template.getMainDocumentPart(), P.class);

		P toReplace = null;
		for (Object p : paragraphs) {
			List<Text> texts = getAllElementFromObject(p, Text.class);
			for (Object t : texts) {
				Text content = (Text) t;
				if (content.getValue().equals(placeholder)) {
					toReplace = (P) p;
					break;
				}
			}
		}

		// we now have the paragraph that contains our placeholder: toReplace
		// 2. split into seperate lines
		String as[] = StringUtils.splitPreserveAllTokens(textToAdd, '\n');

		for (int i = 0; i < as.length; i++) {
			String ptext = as[i];

			// 3. copy the found paragraph to keep styling correct
			P copy = (P) XmlUtils.deepCopy(toReplace);

			// replace the text elements from the copy
			List<?> texts = getAllElementFromObject(copy, Text.class);
			if (texts.size() > 0) {
				Text textToReplace = (Text) texts.get(0);
				textToReplace.setValue(ptext);
			}

			// add the paragraph to the document
			addTo.getContent().add(copy);
		}

		// 4. remove the original one
		((ContentAccessor) toReplace.getParent()).getContent()
				.remove(toReplace);

	}

 
	
	
//	public void velocityMerge(String filename,String outputFile) throws Exception {
//		Map<String,Object> datas = new HashMap<String,Object>();
//		datas.put("age", 12);
//		List<Map<String,Object>> students = new ArrayList<Map<String,Object>>();
//		for(int i = 0;i<10;i++)
//		{
//			Map<String,Object> student = new HashMap<String, Object>();
//			student.put("name", "name"+i);
//			student.put("age", i);
//			students.add(student);
//		}
//		datas.put("students", students);
//		VelocityUtil.generateStream(filename, datas, outputFile);
//	}
	
	public String velocityMerge(String xml,Map<String,Object> datas) throws Exception {

		String targetPath = this.getCompiledPath()+File.separator+"out";
		
		String relsFileName = targetPath+"\\word\\_rels\\document.xml.rels";
		
		
		String sourceImaBaseImagePath = this.systemConfig.findSystemConfigValueBykey("sourceImaBaseImagePath");
		if(sourceImaBaseImagePath != null && "".equals(sourceImaBaseImagePath)){
			this.sourceImaBaseImagePath = sourceImaBaseImagePath;
		}
		ImageHandler imageHandler = new ImageHandler(this.sourceImaBaseImagePath, targetPath,relsFileName);
		
		datas.put("imageHandler", imageHandler);
		
		xml = VelocityUtil.generateString(xml, datas);
		
		imageHandler.saveRels();
		
		return xml;
	}	
	public String cleanVMD(String xml) {
 
			xml = xml.replaceAll("\\Q<vmd>\\E", " \n ");
			xml = xml.replaceAll("\\Q</vmd>\\E", " \n ");
			
			return xml;
	}
    public void convert2Html(WordprocessingMLPackage wordMLPackage,OutputStream os)
            throws Exception {

		// HTML exporter setup (required)
		// .. the HTMLSettings object
    	
    	String targetPath = this.getCompiledPath()+File.separator+"html";
    	
    	HTMLSettings htmlSettings = Docx4J.createHTMLSettings();

    	String compiledPath = this.getCompiledPath()+File.separator+"compiled";
    	
    	htmlSettings.setImageDirPath(compiledPath + "_files");
    	htmlSettings.setImageTargetUri(compiledPath.substring(compiledPath.lastIndexOf("/")+1)
    			+ "_files");
    	htmlSettings.setWmlPackage(wordMLPackage);
    	
    	
    	/* CSS reset, see http://itumbcom.blogspot.com.au/2013/06/css-reset-how-complex-it-should-be.html 
    	 * 
    	 * motivated by vertical space in tables in Firefox and Google Chrome.
        
	        If you have unwanted vertical space, in Chrome this may be coming from -webkit-margin-before and -webkit-margin-after
	        (in Firefox, margin-top is set to 1em in html.css)
	        
	        Setting margin: 0 on p is enough to fix it.
	        
	        See further http://www.css-101.org/articles/base-styles-sheet-for-webkit-based-browsers/    	
    	*/
    	String userCSS = "html, body, div, span, h1, h2, h3, h4, h5, h6, p, a, img,  ol, ul, li, table, caption, tbody, tfoot, thead, tr, th, td " +
    			"{ margin: 0; padding: 0; border: 0;}" +
    			"body {line-height: 1;} ";
    	htmlSettings.setUserCSS(userCSS);
    	
    	
    	//Other settings (optional)
//    	htmlSettings.setUserBodyTop("<H1>TOP!</H1>");
//    	htmlSettings.setUserBodyTail("<H1>TAIL!</H1>");
		
		// Sample sdt tag handler (tag handlers insert specific
		// html depending on the contents of an sdt's tag).
		// This will only have an effect if the sdt tag contains
		// the string @class=XXX
//			SdtWriter.registerTagHandler("@class", new TagClass() );
		
//		SdtWriter.registerTagHandler(Containerization.TAG_BORDERS, new TagSingleBox() );
//		SdtWriter.registerTagHandler(Containerization.TAG_SHADING, new TagSingleBox() );
		
		// If you want XHTML output
    	Docx4jProperties.setProperty("docx4j.Convert.Out.HTML.OutputMethodXML", true);

		//Don't care what type of exporter you use
//		Docx4J.toHTML(htmlSettings, os, Docx4J.FLAG_NONE);
		//Prefer the exporter, that uses a xsl transformation
		Docx4J.toHTML(htmlSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);
		//Prefer the exporter, that doesn't use a xsl transformation (= uses a visitor)
//		Docx4J.toHTML(htmlSettings, os, Docx4J.FLAG_EXPORT_PREFER_NONXSL);

		// Clean up, so any ObfuscatedFontPart temp files can be deleted 
//		if (wordMLPackage.getMainDocumentPart().getFontTablePart()!=null) {
//			wordMLPackage.getMainDocumentPart().getFontTablePart().deleteEmbeddedFontTempFiles();
//		}		
		// This would also do it, via finalize() methods
		htmlSettings = null;
		wordMLPackage = null;	
	
    }
    public  void convert2Pdf(WordprocessingMLPackage wordMLPackage,OutputStream os) 
            throws Exception {
 
		// Font regex (optional)
		// Set regex if you want to restrict to some defined subset of fonts
		// Here we have to do this before calling createContent,
		// since that discovers fonts
		String regex = null;
		// Windows:
		// String
		// regex=".*(calibri|camb|cour|arial|symb|times|Times|zapf).*";
		//regex=".*(calibri|camb|cour|arial|times|comic|georgia|impact|LSANS|pala|tahoma|trebuc|verdana|symbol|webdings|wingding).*";
		// Mac
		// String
		// regex=".*(Courier New|Arial|Times New Roman|Comic Sans|Georgia|Impact|Lucida Console|Lucida Sans Unicode|Palatino Linotype|Tahoma|Trebuchet|Verdana|Symbol|Webdings|Wingdings|MS Sans Serif|MS Serif).*";
		PhysicalFonts.setRegex(regex);

		// Refresh the values of DOCPROPERTY fields 
		FieldUpdater updater = new FieldUpdater(wordMLPackage);
		updater.update(true);
		
		// Set up font mapper (optional)
		Mapper fontMapper = new IdentityPlusMapper();
		wordMLPackage.setFontMapper(fontMapper);
		
		// .. example of mapping font Times New Roman which doesn't have certain Arabic glyphs
		// eg Glyph "ي" (0x64a, afii57450) not available in font "TimesNewRomanPS-ItalicMT".
		// eg Glyph "ج" (0x62c, afii57420) not available in font "TimesNewRomanPS-ItalicMT".
		// to a font which does
		PhysicalFont font 
				= PhysicalFonts.get("Arial Unicode MS"); 
			// make sure this is in your regex (if any)!!!
//		if (font!=null) {
//			fontMapper.put("Times New Roman", font);
//			fontMapper.put("Arial", font);
//		}
//		fontMapper.put("Libian SC Regular", PhysicalFonts.get("SimSun"));

		// FO exporter setup (required)
		// .. the FOSettings object
    	FOSettings foSettings = Docx4J.createFOSettings();
 
		foSettings.setWmlPackage(wordMLPackage);
		
		// Document format: 
		// The default implementation of the FORenderer that uses Apache Fop will output
		// a PDF document if nothing is passed via 
		// foSettings.setApacheFopMime(apacheFopMime)
		// apacheFopMime can be any of the output formats defined in org.apache.fop.apps.MimeConstants eg org.apache.fop.apps.MimeConstants.MIME_FOP_IF or
		// FOSettings.INTERNAL_FO_MIME if you want the fo document as the result.
		//foSettings.setApacheFopMime(FOSettings.INTERNAL_FO_MIME);
		
		// Specify whether PDF export uses XSLT or not to create the FO
		// (XSLT takes longer, but is more complete).
		
		// Don't care what type of exporter you use
		Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);
		
		// Prefer the exporter, that uses a xsl transformation
		// Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);
		
		// Prefer the exporter, that doesn't use a xsl transformation (= uses a visitor)
		// .. faster, but not yet at feature parity
		// Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_NONXSL);
    	
		// Clean up, so any ObfuscatedFontPart temp files can be deleted 
//		if (wordMLPackage.getMainDocumentPart().getFontTablePart()!=null) {
//			wordMLPackage.getMainDocumentPart().getFontTablePart().deleteEmbeddedFontTempFiles();
//		}		
		// This would also do it, via finalize() methods
		updater = null;
		foSettings = null;
		wordMLPackage = null;
		
    }   
    
	//记录从池中取出次数
	private int num;
	private boolean active;
	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}    
	private Map<String,Object> genTestData()
	{
		Map<String,Object> datas = new HashMap<String,Object>();
		datas.put("age", 12);
		List<Map<String,Object>> students = new ArrayList<Map<String,Object>>();
		for(int i = 1;i<10;i++)
		{
			Map<String,Object> student = new HashMap<String, Object>();
			student.put("name", "name"+i);
			student.put("age", i);
			student.put("image", "image1 ("+i+").jpg");
			students.add(student);
		}
		datas.put("students", students);
		
		return datas;
	}
}
