package cn.smbms.service.role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smbms.dao.BaseDao;
import cn.smbms.dao.role.RoleDao;
import cn.smbms.dao.role.RoleDaoImpl;
import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Role;
@Service
public class RoleServiceImpl implements RoleService{
	@Autowired
	private RoleDao roleDao;
	
	public RoleServiceImpl(){
		roleDao = new RoleDaoImpl();
	}
	
	@Override
	public List<Role> getRoleList() {
		// TODO Auto-generated method stub
		Connection connection = null;
		List<Role> roleList = null;
		try {
			connection = BaseDao.getConnection();
			roleList = roleDao.getRoleList(connection);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			BaseDao.closeResource(connection, null, null);
		}
		return roleList;
	}

	
	//增加
	@Override
	public boolean add(Role role) {
		// TODO Auto-generated method stub
				boolean flag = false;
				Connection connection = null;
				try {
					connection = BaseDao.getConnection();
					connection.setAutoCommit(false);//寮�鍚疛DBC浜嬪姟绠＄悊
					if(roleDao.add(connection,role) > 0)
						flag = true;
					connection.commit();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					try {
						System.out.println("rollback==================");
						connection.rollback();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}finally{
					//鍦╯ervice灞傝繘琛宑onnection杩炴帴鐨勫叧闂�
					BaseDao.closeResource(connection, null, null);
				}
				return flag;
	}

	//查询
	@Override
	public List<Role> getRoleList(String roleCode,String roleName) {
		// TODO Auto-generated method stub
		Connection connection = null;
		List<Role> roleList = null;
		
		try {
			connection = BaseDao.getConnection();
			roleList = roleDao.getRoleList(connection, roleCode,roleName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			BaseDao.closeResource(connection, null, null);
		}
		return roleList;
	}
	
}
