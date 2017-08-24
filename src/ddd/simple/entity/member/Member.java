package ddd.simple.entity.member;

import ddd.base.annotation.Column; 
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(label="会员",name="member")
public class Member extends Entity
{
	private static final long serialVersionUID = 1L;
	
	@Column(label="用户名",nullable=false,comment="")
	private String name;
    
	@Column(label="密码",comment="")
	private String password;
	
	@Column(label="唯一标识 UID",nullable=false,comment="")
	private String ckey;
	
	@Column(label="性别",comment="")
	private String sex;
	
	@Column(label="生日",comment="")
	private String birthday;
	
	@Column(label="电子邮件",comment="")
	private String email;
	
	@Column(label="联系电话",comment="")
	private String phone;
	
	@Column(label="教育程度",comment="")
	private String eduStatus;
	
	@Column(label="当前职业",comment="")
	private String currentCareer;
	
	@Column(label="血型",comment="")
	private String bloodType;
	
	@Column(label="婚姻状况",comment="")
	private String maritalStatus;
	
	@Column(label="地址",comment="")
	private String address;

	@Column(label="个人简介",comment="")
	private String introduce;
	
	@Column(label="喜欢的书籍",comment="")
	private String likeBooks;
	
	@Column(label="头像",comment="")
	private String avatarUrl;
	
	public String getAvatarUrl() {
		lazyLoad();
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getName() {
		lazyLoad();
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		lazyLoad();
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSex() {
		lazyLoad();
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		lazyLoad();
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		lazyLoad();
		return email;
	}
	public String getCkey() {
		lazyLoad();
		return ckey;
	}
	
	public void setCkey(String ckey) {
		this.ckey = ckey;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		lazyLoad();
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEduStatus() {
		lazyLoad();
		return eduStatus;
	}

	public void setEduStatus(String eduStatus) {
		this.eduStatus = eduStatus;
	}

	public String getCurrentCareer() {
		lazyLoad();
		return currentCareer;
	}

	public void setCurrentCareer(String currentCareer) {
		this.currentCareer = currentCareer;
	}

	public String getMaritalStatus() {
		lazyLoad();
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getAddress() {
		lazyLoad();
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getIntroduce() {
		lazyLoad();
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getLikeBooks() {
		lazyLoad();
		return likeBooks;
	}

	public void setLikeBooks(String likeBooks) {
		this.likeBooks = likeBooks;
	}
	
	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

}