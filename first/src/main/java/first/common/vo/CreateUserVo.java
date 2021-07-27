package first.common.vo;


public class CreateUserVo {
	private String User_id;
	private String User_name;
	private String Expire;
	private String Depthname;
	private String Phone;
	private String Email;
	private String Desc;
	private String Office_code;
	public String getUser_id() {
		return User_id;
	}
	public void setUser_id(String user_id) {
		User_id = user_id;
	}
	public String getUser_name() {
		return User_name;
	}
	public void setUser_name(String user_name) {
		User_name = user_name;
	}
	public String getExpire() {
		return Expire;
	}
	public void setExpire(String expire) {
		Expire = expire;
	}
	public String getDepthname() {
		return Depthname;
	}
	public void setDepthname(String depthname) {
		Depthname = depthname;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getDesc() {
		return Desc;
	}
	public void setDesc(String desc) {
		Desc = desc;
	}
	public String getOffice_code() {
		return Office_code;
	}
	public void setOffice_code(String office_code) {
		Office_code = office_code;
	}
	@Override
	public String toString() {
		return "CreateUserVo [User_id=" + User_id + ", User_name=" + User_name + ", Expire=" + Expire + ", Depthname="
				+ Depthname + ", Phone=" + Phone + ", Email=" + Email + ", Desc=" + Desc + ", Office_code="
				+ Office_code + "]";
	}

}
