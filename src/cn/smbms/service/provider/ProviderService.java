package cn.smbms.service.provider;

import java.util.List;

import cn.smbms.pojo.Provider;

public interface ProviderService {
	/**
	 * 澧炲姞渚涘簲鍟�
	 * @param provider
	 * @return
	 */
	public boolean add(Provider provider);


	/**
	 * 閫氳繃渚涘簲鍟嗗悕绉般�佺紪鐮佽幏鍙栦緵搴斿晢鍒楄〃-妯＄硦鏌ヨ-providerList
	 * @param proName
	 * @return
	 */
	public List<Provider> getProviderList(String proName,String proCode);
	
	/**
	 * 閫氳繃proId鍒犻櫎Provider
	 * @param delId
	 * @return
	 */
	public int deleteProviderById(String delId);
	
	
	/**
	 * 閫氳繃proId鑾峰彇Provider
	 * @param id
	 * @return
	 */
	public Provider getProviderById(String id);
	
	/**
	 * 淇敼鐢ㄦ埛淇℃伅
	 * @param user
	 * @return
	 */
	public boolean modify(Provider provider);
	
}
