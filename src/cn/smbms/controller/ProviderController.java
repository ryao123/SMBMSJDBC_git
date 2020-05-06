package cn.smbms.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysql.jdbc.StringUtils;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.service.provider.ProviderServiceImpl;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.PageSupport;

@Controller
@RequestMapping("/provider")
public class ProviderController {
    @Autowired
    private ProviderService providerService;
    @Autowired
    private RoleService roleService;
    
    

    
    // ��ѯ��Ӧ���б�ķ���
    @RequestMapping(value="/providerlist.html",method=RequestMethod.GET)
    public String query(
    		@RequestParam(name = "queryProName",required = false) String queryProName ,
    		@RequestParam(name = "queryProCode",required = false) String queryProCode ,
    		Model model) {
    		if(StringUtils.isNullOrEmpty(queryProName)){
    			queryProName = "";
    		}
    		if(StringUtils.isNullOrEmpty(queryProCode)){
    			queryProCode = "";
    		}
    		List<Provider> providerList = new ArrayList<Provider>();
    		ProviderService providerService = new ProviderServiceImpl();
    		providerList = providerService.getProviderList(queryProName,queryProCode);
    		model.addAttribute("providerList", providerList);
    		model.addAttribute("queryProName", queryProName);
    		model.addAttribute("queryProCode", queryProCode);
    		return "providerlist";
    }
    
    
    
    
    
    //���ӹ�Ӧ��
    @RequestMapping(value = "/provideradd.html" ,method =RequestMethod.GET )
    public String addPro(@ModelAttribute("provider") Provider provider) {
		return "provideradd";
    	
    }
    
    //��������
    @RequestMapping(value = "/prosave.html",method = RequestMethod.POST)
    public String savePro(Provider provider,HttpSession session) {
        int createdBy = ((User)session.getAttribute("user")).getId();
        provider.setCreatedBy(createdBy);
        provider.setCreationDate(new Date());
        
        // ���������û��ķ���
        boolean isOk =providerService.add(provider);
        if (isOk) {
            return "redirect:providerlist.html";
        }else {
            return "provideradd";
        }
    }
    
    
    
    
    
   
}
