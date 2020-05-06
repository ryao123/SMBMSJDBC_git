package cn.smbms.dao.role;

import java.sql.Connection;
import java.util.List;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Role;

public interface RoleDao {
	
	public List<Role> getRoleList(Connection connection)throws Exception;

	
	//����
	public int add(Connection connection,Role role)throws Exception;

	//��ѯ
	public List<Role> getRoleList(Connection connection,String roleCode,String roleName)throws Exception;
}
