package entity.po;
// default package
// Generated 2017-4-6 20:57:37 by Hibernate Tools 4.3.1

/**
 * Role generated by hbm2java
 */
public class Role implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer roleid;
	private String rolename;
	private String rolecode;

	public Role() {
	}

	public Role(Integer roleid, String rolename, String rolecode) {
		this.roleid = roleid;
		this.rolename = rolename;
		this.rolecode = rolecode;
	}

	public Integer getRoleid() {
		return this.roleid;
	}

	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}

	public String getRolename() {
		return this.rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getRolecode() {
		return this.rolecode;
	}

	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}

}
