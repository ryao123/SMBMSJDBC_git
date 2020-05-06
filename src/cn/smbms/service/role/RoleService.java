package cn.smbms.service.role;

import java.util.List;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Role;

public interface RoleService {
	
	public List<Role> getRoleList();
	
	
	//Ôö¼Ó
	public boolean add(Role role);


	//²éÑ¯
	public List<Role> getRoleList(String roleCode,String roleName);
	
}
