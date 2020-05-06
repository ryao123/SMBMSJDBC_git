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
import cn.smbms.service.user.UserService;
import cn.smbms.tools.PageSupport;

@Controller
@RequestMapping("/bill")
public class BillController {
    @Autowired
    private ProviderService providerService;
    @Autowired
    private BillService billService;
    
    

    
    // 查询供应商列表的方法
    @RequestMapping(value="/billlist.html",method=RequestMethod.GET)
    public String query(
    		@RequestParam(name = "queryProductName",required = false) String queryProductName ,
    		@RequestParam(name = "queryProviderId",required = false) String queryProviderId ,
    		@RequestParam(name = "queryIsPayment",required = false) String queryIsPayment ,
    		Model model) {
    		List<Provider> providerList = new ArrayList<Provider>();
    		ProviderService providerService = new ProviderServiceImpl();
    		providerList = providerService.getProviderList("","");
    		model.addAttribute("providerList", providerList);
    		if(StringUtils.isNullOrEmpty(queryProductName)){
    			queryProductName = "";
    		}
    		List<Bill> billList = new ArrayList<Bill>();
    		BillService billService = new BillServiceImpl();
    		Bill bill = new Bill();
    		if(StringUtils.isNullOrEmpty(queryIsPayment)){
    			bill.setIsPayment(0);
    		}else{
    			bill.setIsPayment(Integer.parseInt(queryIsPayment));
    		}
    		
    		if(StringUtils.isNullOrEmpty(queryProviderId)){
    			bill.setProviderId(0);
    		}else{
    			bill.setProviderId(Integer.parseInt(queryProviderId));
    		}
    		bill.setProductName(queryProductName);
    		billList = billService.getBillList(bill);
    		model.addAttribute("billList", billList);
    		model.addAttribute("queryProductName", queryProductName);
    		model.addAttribute("queryProviderId", queryProviderId);
    		model.addAttribute("queryIsPayment", queryIsPayment);
    		return "billlist";
    
    }
    
    
    
    //增加订单
    @RequestMapping(value = "/billadd.html" ,method =RequestMethod.GET )
    public String addPro(@ModelAttribute("bill") Bill bill,Model model
    		) {
    	List<Provider> providerList = new ArrayList<Provider>();
		ProviderService providerService = new ProviderServiceImpl();
		providerList = providerService.getProviderList("","");
		model.addAttribute("providerList", providerList);
		return "billadd";
    	
    }
    
    
    //保存
    @RequestMapping(value = "/billsave.html",method = RequestMethod.POST)
    public String savePro(Bill bill ,HttpSession session) {
        int createdBy = ((Bill)session.getAttribute("bill")).getId();
        bill.setCreatedBy(createdBy);;
        bill.setCreationDate(new Date());
        
        // 调用增加订单的方法
        boolean isOk =billService.add(bill);
        if (isOk) {
            return "redirect:billlist.html";
        }else {
            return "billadd";
        }
    }
    
    
    
    
    
   
}
