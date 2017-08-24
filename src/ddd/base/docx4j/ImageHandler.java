package ddd.base.docx4j;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class ImageHandler {
	
	private Map<String,String> images = new HashMap<String, String>();
	
	private String sourceBasePath;
	private String targetBasePath;
	private String relsFileName;
	private StringBuilder newRels = new StringBuilder();
	
	public ImageHandler(String sourceBasePath, String targetBasePath,String relsFileName) {
		super();
		this.sourceBasePath = sourceBasePath;
		this.targetBasePath = targetBasePath;
		this.relsFileName = relsFileName;
	}
	private int currentImageIndex = 1;
	
	public String createImageRID(String imageName)
	{
		String rId = null;
		if(images.containsKey(imageName))
		{
			rId = images.get(imageName).toString();
		}
		else
		{
			rId = "rImageId"+currentImageIndex++;
			this.createImage(imageName, rId);
		}
		return rId;
	}
	public void createImage(String imageName,String rId)
	{
		String sourceFile = this.sourceBasePath+File.separator+imageName;
		String targetRelFile ="media/"+rId+"."+ FilenameUtils.getExtension(sourceFile);
		String targetFile = this.targetBasePath+File.separator+"\\word\\media"+File.separator+rId+"."+ FilenameUtils.getExtension(sourceFile);
		try {
			FileUtils.copyFile(new File(sourceFile),new File(targetFile));
			
			newRels.append("<Relationship Target=\"").append(targetRelFile)
			.append("\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/image\"" )
			.append(" Id=\""+rId+"\"/>").append("\n");
			
		} catch (IOException e) {
			System.out.println("复制文件出错：源："+sourceFile+",目标："+targetFile+",原因："+e.getMessage());
			e.printStackTrace();
		}
	}
	public void saveRels() throws IOException
	{
		String relsContent = FileUtils.readFileToString(new File(this.relsFileName));
		int startIndex = relsContent.indexOf("</Relationships>");
		newRels.insert(0, relsContent.substring(0, startIndex)).append(relsContent.substring(startIndex));
		FileUtils.writeStringToFile(new File(this.relsFileName), newRels.toString());
	}
	
}
