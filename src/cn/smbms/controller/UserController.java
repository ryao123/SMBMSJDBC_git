package cn.smbms.controller;

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

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.PageSupport;

// �������߱���������Ĺ���
@Controller
// ������������ģ�����û�ģ��
@RequestMapping("/user")
public class UserController {
    // ����һ��ҵ���Ķ���
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    // ����һ�����Է���ҳ������󷽷�
    @RequestMapping("/login.html")
    public String login() {
        // ֱ�ӷ���login.jspҳ��
        return "login";
    }
    
    // ����һ��dologin�����󷽷�������ȥ�����ݿ��б������Ϣ����ƥ�䣬
    // ����ɹ��ͽ���ҳ�����ת�����ʧ�ܾ��ڻص�login.jspҳ��
    @RequestMapping(value = "/dologin.html",method = RequestMethod.POST)
    public String dologin(@RequestParam("userCode") String userCode,
        @RequestParam("userPassword") String userPassword,
        HttpServletRequest request,
        HttpSession session) throws Exception {
        // ����ҵ����и���userCode��userPassword��������ʵ�ֵ�¼�ķ���
        User user = userService.login(userCode, userPassword);
        if(user != null) {
            // ��ת����ҳ��
            // return "�߼���ͼ��";���Ƿ������˵�һ����ת������ʹ�õ���ת��
            // ���Ҫʹ���ض�����������ת
            // response.sendRedirect(��ת������·��);
            // ��SpringMVC����п���ʹ�� redirect����ʶ �ض���
            // ʹ��forward����ʶ ת��
            // forward:�߼���ͼ��������No mapping for POST /SMBMSJDBC/user/frame �������
            // ��������ʶ ���������һ�������url
            // redirect:�����url
            // ����¼�ɹ����û����浽session�Ự��
            session.setAttribute("user", user);
            return "redirect:frame.html";
        }else {
            // ������ʽ�Ƿ������˵���ת(ת��)
            /*request.setAttribute("error", "�û�����������󣡣�");
            return "login";*/
            throw new Exception("�û�����������󣡣�");
        }
    }
    
    // ����һ��ʵ��ת�������󷽷�
    @RequestMapping("/frame.html")
    public String main() {
        return "frame";
    }
    
    // ģ���쳣��¼�ķ���
    @RequestMapping(value = "/exlogin.html",method = RequestMethod.GET)
    public String exLogin(@RequestParam("userCode") String userCode,
        @RequestParam("userPassword") String userPassword) {
        User user = userService.login(userCode, userPassword);
        if (user == null) {
            // �׳�һ���쳣
            throw new RuntimeException("�û������������");
        }
        return "redirect:frame.html";
    }
    // �����쳣��¼�Ĳ���
    // @ExceptionHandler ֧��HandlerExceptionResolver��ͼ��һ�ַ�ʽ
    // HandlerExceptionResolver��ͼ����һ��resolverHandler������
    // ������쳣�ʹ���û���쳣�Ͳ�������
    /*@ExceptionHandler(value = RuntimeException.class)
    public String handlerException(RuntimeException e,
        HttpServletRequest req) {
        req.setAttribute("exception", e);
        return "error";
    }*/
    
    @RequestMapping(value="/loginOut",method = RequestMethod.GET)
    public String loginOut(HttpSession session) {
        if (session != null) {
            // ���session
            session.invalidate();
        }
        return "redirect:login.html";
    }
    
    // ��ѯ�û��б�ķ���
    @RequestMapping(value="/userlist.html",method=RequestMethod.GET)
    public String query(
        @RequestParam(name="queryname",required = false) String queryname,
        @RequestParam(name="queryUserRole",required = false) String queryUserRole,
        @RequestParam(name="pageIndex",required = false) String pageIndex,
        Model model){
        // �����û���ɫid
        int _queryUserRole = 0;
        List<User> userList = null;
        //����ҳ������
        int pageSize = 5;
        //��ǰҳ��
        int currentPageNo = 1;
        if(queryname == null){
            queryname = "";
        }
        // �жϽ�ɫid�Ƿ�������
        if(queryUserRole != null && !queryUserRole.equals("")){
            _queryUserRole = Integer.parseInt(queryUserRole);
        }
        // �ж�ҳ���ֵ
        if(pageIndex != null){
            try{
                currentPageNo = Integer.valueOf(pageIndex);
            }catch(NumberFormatException e){
                // ʹ��ȫ���쳣�������쳣��Ϣ����ʾ
                throw new RuntimeException("ҳ���ֵ����ȷ����");
                //response.sendRedirect("error.jsp");
            }
        } 
        //����������    
        int totalCount  = userService.getUserCount(queryname,_queryUserRole);
        //��ҳ��
        PageSupport pages=new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        // �����ҳ��
        int totalPageCount = pages.getTotalPageCount();
        
        //������ҳ��βҳ
        if(currentPageNo < 1){
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }
        
        // �����ʾ���ݵļ���
        userList = userService.getUserList(queryname,_queryUserRole,currentPageNo, pageSize);
        // �����ݷ��뵽model������
        model.addAttribute("userList", userList);
        List<Role> roleList = null;
        roleList = roleService.getRoleList();
        model.addAttribute("roleList", roleList);
        // ��ҳ��jsp����Ҫ�Ĳ���
        model.addAttribute("queryUserName", queryname);
        model.addAttribute("queryUserRole", queryUserRole);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);
        return "userlist";
    }
    
    
    
    
    //���ӷ���һ
    
    // ����һ�����������û�ҳ��ķ���
    @RequestMapping(value = "/adduser.html",method = RequestMethod.GET)
    // ��ʽһ���������Ķ��󱣴浽����ģ����
    //public String addUser(User user,Model model) {
    // ��ʽ����ʹ��ע�⽫���󱣴浽����ģ����
    public String addUser(@ModelAttribute("user") User user) {
        // �˴���User���൱�ڴ���һ�������û���Ϣ�Ŀն���
        // ���ڱ����������ݺ����ݾͻ��Զ���װ�뵽���user������
        //model.addAttribute("user",user);
        return "useradd";
    }
    
    @RequestMapping(value = "/usersave.html",method = RequestMethod.POST)
    public String userSave(User user,HttpSession session) {
        // ��Ҫ�д�����id,��ʵ����ȥ��ñ�����session�е��û������idֵ
        int createdBy = ((User)session.getAttribute("user")).getId();
        // ��Ҫ����ʱ��,���ϵͳ�ĵ�ǰʱ��
        user.setCreatedBy(createdBy);
        user.setCreationDate(new Date());
        // ���������û��ķ���
        boolean isOk = userService.add(user);
        if (isOk) {
            // �ɹ���ҳ���ϵ�����Ҳ��Ҫˢ�£�ʹ���������Ƿ���
            return "redirect:userlist.html";
        }else {
            return "useradd";
        }
    }
    
    
    /*
    //���ӷ�����
    
    @RequestMapping(value="/add.html",method=RequestMethod.GET)
    public String add(@ModelAttribute("user") User user) {
        return "user/useradd";
    }
    
    // ��������Ҳʹ��ͬһ������
    @RequestMapping(value="/add.html",method=RequestMethod.POST)
    public String addsave(User user,
        HttpSession session) {
        int createdBy = ((User)session.getAttribute("user")).getId();
        // ��Ҫ����ʱ��,���ϵͳ�ĵ�ǰʱ��
        user.setCreatedBy(createdBy);
        user.setCreationDate(new Date());
        // ���������û��ķ���
        boolean isOk = userService.add(user);
        if (isOk) {
            // �ɹ���ҳ���ϵ�����Ҳ��Ҫˢ�£�ʹ���������Ƿ���
            return "redirect:userlist.html";
        }else {
            return "user/useradd";
        }
    }
    */
}
