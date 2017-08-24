package ddd.simple.action.modelFile;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.attachment.Attachment;
import ddd.simple.entity.modelFile.ModelFile;
import ddd.simple.entity.modelFile.StatsReport;
import ddd.simple.service.attachment.AttachmentService;
import ddd.simple.service.modelFile.ModelFileService;

@Action
@RequestMapping("/ModelFile")
@Controller
public class ModelFileAction
{
	@Resource(name="modelFileServiceBean")
	private ModelFileService modelFileService;
	
	
	@Resource(name = "attachmentServiceBean")
	private AttachmentService	attachmentService;
	
	public ModelFile saveModelFile(ModelFile modelFile)
	{
		try {
			ModelFile saveModelFile = this.modelFileService.saveModelFile(modelFile);
			return saveModelFile;
		} catch (DDDException e) {
			throw e;
		}
	}

	public int deleteModelFile(Long modelFileId){
		
		try {
			return this.modelFileService.deleteModelFile(modelFileId);
		} catch (DDDException e) {
			throw e;
		}
		
	}

	public ModelFile updateModelFile(ModelFile modelFile) {
		try {
			ModelFile updateModelFile = this.modelFileService.updateModelFile(modelFile);
			return updateModelFile;
		} catch (DDDException e) {
			throw e;
		}
	}

	public ModelFile findModelFileById(Long modelFileId){
		try {
			ModelFile findModelFile = this.modelFileService.findModelFileById(modelFileId);
			return  findModelFile;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public EntitySet<ModelFile> findAllModelFile(){
		try{
			EntitySet<ModelFile> allModelFile = this.modelFileService.findAllModelFile();
			return allModelFile;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public String findModelFileAttachmentsId(String key){
		ModelFile  modelFile = this.modelFileService.findModelFileByKey(key);
		if(modelFile != null){
			EntitySet<Attachment> attachments = this.attachmentService.findAttachmentByForm(modelFile.getEId(), "modelFileForm");
			Iterator<Attachment> ite = attachments.iterator();
			while(ite.hasNext()){
				Attachment temp = ite.next();
				if(temp != null){
					return String.valueOf(temp.getEId());
				}
			}
		}
		return null;
	}
	
	public List<StatsReport> getAllReportFormGroupByType(){
		try{
			return this.modelFileService.getAllReportFormGroupByType();
		} catch (DDDException e) {
			throw e;
		}
		
	}

}