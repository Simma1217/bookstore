package cn.simma.bookstore.user.servlet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import cn.itcast.servlet.BaseServlet;
import cn.simma.bookstore.cart.domain.Cart;
import cn.simma.bookstore.user.domain.User;
import cn.simma.bookstore.user.service.UserException;
import cn.simma.bookstore.user.service.UserService;

public class UserServlet extends BaseServlet {
	UserService userService=new UserService();
	/**
	 * 注册方法
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String regist(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		User form=CommonUtils.toBean(request.getParameterMap(), User.class);
		form.setUid(CommonUtils.uuid());
		form.setCode(CommonUtils.uuid()+CommonUtils.uuid());
		Map<String,String> errors=new HashMap<String,String>();
		String userName=form.getUsername();
		if(userName==null||userName.trim().isEmpty()){
			errors.put("userName","用户名不能为空");	
			}
		else if(userName.length()<3|| userName.length() > 10){
			errors.put("userName","用户名必须在3到10位之间");	
		}
		
		String password=form.getPassword();
		if(password==null||password.trim().isEmpty()){
			errors.put("password","用户名不能为空！");	
			}
		else if(password.length()<3||password.length() > 10){
			errors.put("password","用户名必须在3到10位之间！");	
		}
		String email=form.getEmail();
		if(email==null||email.trim().isEmpty()){
			errors.put("email","密码不能为空！");	
			}
		else if(email.matches("\\w+@\\w+\\.\\w+")){
			errors.put("email","请正确填写邮箱格式！");	
		}
		if(errors.size()>1){
			request.setAttribute("errors",errors);
			request.setAttribute("user",form);
			return "f:/jsps/user/regist.jsp";
		}
		try {
			userService.regist(form);
			/**
			 * 发邮件
			 */
			
			Properties p=new Properties();
			p.load(this.getClass().getClassLoader().getResourceAsStream("email.properties"));
			String host=p.getProperty("host");
			String from=p.getProperty("from");
			String username=p.getProperty("username");
			String psw=p.getProperty("password");
			String subject=p.getProperty("subject");
			String content=MessageFormat.format(p.getProperty("content"),form.getCode());
			System.out.println(content);
			Session session=MailUtils.createSession(host,username,psw);
			Mail mail=new Mail(from, form.getEmail(), subject, content);
			try {
				MailUtils.send(session, mail);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			request.setAttribute("msg","恭喜你，注册成功！请马上到邮箱激活 ");
			return "f:/jsps/msg.jsp";
			
		} catch (UserException e) {
			request.setAttribute("msg",e.getMessage());
			request.setAttribute("user",form);
			return "f:/jsps/user/regist.jsp";
		}
	}
/**
 * 激活方法
 * @param request
 * @param response
 * @return
 * @throws ServletException
 * @throws IOException
 */
		public String active(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			response.setContentType("text/html;charset=utf-8");
			String code=request.getParameter("code");
			System.out.println(code);
			try {
				userService.active(code);
				request.setAttribute("msg","您已激活成功，请登录！");
			} catch (UserException e) {
				request.setAttribute("msg",e.getMessage());
			}
			return "f:/jsps/msg.jsp";				

		}
		/**
		 * 登录方法
		 */
		public String login(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			System.out.println("login");
			response.setContentType("text/html;charset=utf-8");
			User form=CommonUtils.toBean(request.getParameterMap(), User.class);
			try {
				User user=userService.login(form);
				request.getSession().setAttribute("session_user",user);
				System.out.println("我执行了");
				request.getSession().setAttribute("cart",new Cart());
				return "r:/index.jsp";
			} catch (UserException e) {
				request.setAttribute("msg",e.getMessage());
				request.setAttribute("form",form);
				return "f:/jsps/user/login.jsp";	
			}
		}
		public String quit(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			request.getSession().invalidate();
			return "r:/jsps/user/login.jsp";
		}
		
}
