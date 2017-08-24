package ddd.simple.entity.attachment;

import java.io.Serializable;
import java.util.Date;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;
import ddd.simple.entity.organization.Employee;

@ddd.base.annotation.Entity(name="attachment",label="附件")
public class Attachment extends Entity implements Serializable{

	private static final long serialVersionUID = 1L;

	/**附件名称真实名称	上传和上传完成后显示的文件名 **/
	@Column(label="附件的真实名称")
	private String attachmentRealName;
	
	/**保存后的文件名	上传后保存到服务器上的文件名 **/
	@Column(label="保存文件名")
	private String attachmentLogicalName;
	
	@Column(label="上传时间")
	private Date uploadTime;
	
	@Column(label="上传人",name="uploadPeople_eId",FKName="A_FK_E")
	private Employee uploadPeople;
	
	@Column(label="附件地址")
	private String attachmentAddr;
	
	@Column(label="关联表单Id")
	private Long associateFormId;
	
	@Column(label="关联表单名称")
	private String associateFormName;
	
	@Column(label="附件大小")
	private Long associateSize;
	
	@Column(label="是否有效")
	private Boolean isEffective = true;

	public String getAttachmentRealName() {
		lazyLoad();
		return attachmentRealName;
	}

	public void setAttachmentRealName(String attachmentRealName) {
		this.attachmentRealName = attachmentRealName;
	}

	public String getAttachmentLogicalName() {
		lazyLoad();
		return attachmentLogicalName;
	}

	public void setAttachmentLogicalName(String attachmentLogicalName) {
		this.attachmentLogicalName = attachmentLogicalName;
	}

	public Date getUploadTime() {
		lazyLoad();
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public Employee getUploadPeople() {
		lazyLoad();
		return uploadPeople;
	}

	public void setUploadPeople(Employee uploadPeople) {
		this.uploadPeople = uploadPeople;
	}

	public String getAttachmentAddr() {
		lazyLoad();
		return attachmentAddr;
	}

	public void setAttachmentAddr(String attachmentAddr) {
		this.attachmentAddr = attachmentAddr;
	}

	public Long getAssociateFormId() {
		lazyLoad();
		return associateFormId;
	}

	public void setAssociateFormId(Long associateFormId) {
		this.associateFormId = associateFormId;
	}

	public String getAssociateFormName() {
		lazyLoad();
		return associateFormName;
	}

	public void setAssociateFormName(String associateFormName) {
		this.associateFormName = associateFormName;
	}

	public Long getAssociateSize() {
		lazyLoad();
		return associateSize;
	}

	public void setAssociateSize(Long associateSize) {
		this.associateSize = associateSize;
	}

	public Boolean getIsEffective() {
		lazyLoad();
		return isEffective;
	}

	public void setIsEffective(Boolean isEffective) {
		this.isEffective = isEffective;
	}
	
}
