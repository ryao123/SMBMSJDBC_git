package cn.smbms.service.provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smbms.dao.BaseDao;
import cn.smbms.dao.bill.BillDao;
import cn.smbms.dao.bill.BillDaoImpl;
import cn.smbms.dao.provider.ProviderDao;
import cn.smbms.dao.provider.ProviderDaoImpl;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
@Service
public class ProviderServiceImpl implements ProviderService {
	@Autowired
	private ProviderDao providerDao;
	private BillDao  billDao;
	public ProviderServiceImpl(){
		providerDao = new ProviderDaoImpl();
		billDao = new BillDaoImpl();
	}
	@Override
	public boolean add(Provider provider) {
		// TODO Auto-generated method stub
		boolean flag = false;
		Connection connection = null;
		try {
			connection = BaseDao.getConnection();
			connection.setAutoCommit(false);//寮�鍚疛DBC浜嬪姟绠＄悊
			if(providerDao.add(connection,provider) > 0)
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

	@Override
	public List<Provider> getProviderList(String proName,String proCode) {
		// TODO Auto-generated method stub
		Connection connection = null;
		List<Provider> providerList = null;
		System.out.println("query proName ---- > " + proName);
		System.out.println("query proCode ---- > " + proCode);
		try {
			connection = BaseDao.getConnection();
			providerList = providerDao.getProviderList(connection, proName,proCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			BaseDao.closeResource(connection, null, null);
		}
		return providerList;
	}

	/**
	 * 涓氬姟锛氭牴鎹甀D鍒犻櫎渚涘簲鍟嗚〃鐨勬暟鎹箣鍓嶏紝闇�瑕佸厛鍘昏鍗曡〃閲岃繘琛屾煡璇㈡搷浣�
	 * 鑻ヨ鍗曡〃涓棤璇ヤ緵搴斿晢鐨勮鍗曟暟鎹紝鍒欏彲浠ュ垹闄�
	 * 鑻ユ湁璇ヤ緵搴斿晢鐨勮鍗曟暟鎹紝鍒欎笉鍙互鍒犻櫎
	 * 杩斿洖鍊糱illCount
	 * 1> billCount == 0  鍒犻櫎---1 鎴愬姛 锛�0锛� 2 涓嶆垚鍔� 锛�-1锛�
	 * 2> billCount > 0    涓嶈兘鍒犻櫎 鏌ヨ鎴愬姛锛�0锛夋煡璇笉鎴愬姛锛�-1锛�
	 * 
	 * ---鍒ゆ柇
	 * 濡傛灉billCount = -1 澶辫触
	 * 鑻illCount >= 0 鎴愬姛
	 */
	@Override
	public int deleteProviderById(String delId) {
		// TODO Auto-generated method stub
		Connection connection = null;
		int billCount = -1;
		try {
			connection = BaseDao.getConnection();
			connection.setAutoCommit(false);
			billCount = billDao.getBillCountByProviderId(connection,delId);
			if(billCount == 0){
				providerDao.deleteProviderById(connection, delId);
			}
			connection.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			billCount = -1;
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			BaseDao.closeResource(connection, null, null);
		}
		return billCount;
	}

	@Override
	public Provider getProviderById(String id) {
		// TODO Auto-generated method stub
		Provider provider = null;
		Connection connection = null;
		try{
			connection = BaseDao.getConnection();
			provider = providerDao.getProviderById(connection, id);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			provider = null;
		}finally{
			BaseDao.closeResource(connection, null, null);
		}
		return provider;
	}

	@Override
	public boolean modify(Provider provider) {
		// TODO Auto-generated method stub
		Connection connection = null;
		boolean flag = false;
		try {
			connection = BaseDao.getConnection();
			if(providerDao.modify(connection,provider) > 0)
				flag = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			BaseDao.closeResource(connection, null, null);
		}
		return flag;
	}

}
