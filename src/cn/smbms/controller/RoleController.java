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

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.bill.BillService;
import cn.smbms.service.bill.BillServiceImpl;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.service.provider.ProviderServiceImpl;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.role.RoleServiceImpl;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.PageSupport;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    
    

    
    // 查询供应商列表的方法
    @RequestMapping(value="/rolelist.html",method=RequestMethod.GET)
    public String query(
    		@RequestParam(name = "queryroleCode",required = false) String queryroleCode ,
    		@RequestParam(name = "queryroleName",required = false) String queryroleName ,
    		Model model) {
    		if(StringUtils.isNullOrEmpty(queryroleCode)){
    			queryroleCode = "";
    		}
    		else if(StringUtils.isNullOrEmpty(queryroleName)){
    			queryroleName = "";
    		}
    		List<Role> roleList = new ArrayList<Role>();
    		RoleService roleService = new RoleServiceImpl();
    		roleList = roleService.getRoleList(queryroleCode,queryroleName);
    		model.addAttribute("roleList", roleList);
    		model.addAttribute("queryroleCode", queryroleCode);
    		model.addAttribute("queryroleName", queryroleName);
    		return "rolelist";
    
    }
    
    
    
    
	
	  //增加角色
	  @RequestMapping(value = "/roleadd.html" ,method =RequestMethod.GET ) 
	  public String addPro(@ModelAttribute("role") Role role) {
		  
		return "roleadd";
	  
	  }
	  
	  
	
	  //保存
	  @RequestMapping(value = "/rolesave.html",method = RequestMethod.POST) 
	  public String savePro(Role role ,HttpSession session) { 
		 int createdBy = ((User)session.getAttribute("user")).getId(); 
		 role.setCreatedBy(createdBy);;
		 role.setCreationDate(new Date());
	  
	  // 调用增加角色的方法 
		 boolean isOk =roleService.add(role); 
		 if(isOk) {
			 
			 return "redirect:rolelist.html"; 
		 }else {
			 return "roleadd";
		 }
		 
	  }
		 
	 
    
    
    
    
    
   
}
