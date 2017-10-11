package cn.simma.bookstore.order.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import cn.simma.bookstore.cart.domain.Cart;
import cn.simma.bookstore.cart.domain.CartItem;
import cn.simma.bookstore.order.domain.Order;
import cn.simma.bookstore.order.domain.OrderItem;
import cn.simma.bookstore.order.service.OrderException;
import cn.simma.bookstore.order.service.OrderService;
import cn.simma.bookstore.user.domain.User;


public class OrderServlet extends BaseServlet {
	private OrderService orderService=new OrderService();
	/*
	 * 支付网关回调
	 */
	public String back(HttpServletRequest request, HttpServletResponse response) throws IOException{
		System.out.println("调用back");
		/*
		 * 1. 获取11 + 1
		 */
		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");
		String hmac = request.getParameter("hmac");
		/*
		 * 2. 校验访问者是否为易宝！
		 */
		Properties props = new Properties();
		InputStream input = this.getClass().getClassLoader()
				.getResourceAsStream("merchantInfo.properties");
		props.load(input);
		String keyValue = props.getProperty("keyValue");

		boolean bool = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
				r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
				r8_MP, r9_BType, keyValue);
		if(!bool){
			request.setAttribute("msg","就知道你不是什么好东西！！！");
			return "f:/jsps/msg.jsp";
		}
		/*
		 * 3. 获取状态订单，确定是否要修改订单状态，以及添加积分等业务操作
		 */
		orderService.zhiFu(r6_Order);//有可能对数据库进行操作，有可能什么都不做
		/*
		 * 4. 判断当前回调方式
		 *   如果为点对点，需要回馈以success开头的字符串
		 */
		if(r9_BType.equals("2")) {
			response.getWriter().print("success");
		}
		/*
		 * 5. 保存成功信息，转发到msg.jsp
		 */
		request.setAttribute("msg","支付成功，请耐心等待发货！！！");
		return "f:/jsps/msg.jsp";
	}
	
	public String zhiFu(HttpServletRequest request, HttpServletResponse response) throws IOException{
		Properties props=new Properties();
		InputStream input=this.getClass().getClassLoader().getResourceAsStream("merchantInfo.properties");
		props.load(input);
		/*
		 * 准备13参数
		 */
		String p0_Cmd = "Buy";
		String p1_MerId = props.getProperty("p1_MerId");
		String p2_Order = request.getParameter("oid");
		System.out.println(p2_Order);
		String p3_Amt = "0.01";
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		String p8_Url = props.getProperty("p8_Url");
		String p9_SAF = "";
		String pa_MP = "";
		String pd_FrpId = request.getParameter("pd_FrpId");
		String pr_NeedResponse = "1";
		String keyValue = props.getProperty("keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid,
				p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);
		StringBuilder url=new StringBuilder(props.getProperty("url"));
		url.append("?p0_Cmd=").append(p0_Cmd);
		url.append("&p1_MerId=").append(p1_MerId);
		url.append("&p2_Order=").append(p2_Order);
		url.append("&p3_Amt=").append(p3_Amt);
		url.append("&p4_Cur=").append(p4_Cur);
		url.append("&p5_Pid=").append(p5_Pid);
		url.append("&p6_Pcat=").append(p6_Pcat);
		url.append("&p7_Pdesc=").append(p7_Pdesc);
		url.append("&p8_Url=").append(p8_Url);
		url.append("&p9_SAF=").append(p9_SAF);
		url.append("&pa_MP=").append(pa_MP);
		url.append("&pd_FrpId=").append(pd_FrpId);
		url.append("&pr_NeedResponse=").append(pr_NeedResponse);
		url.append("&hmac=").append(hmac);
		response.sendRedirect(url.toString());
		return null;
	}
	
	//生成订单
	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//将session中的购物车的信息添加到order
		Cart cart=(Cart) request.getSession().getAttribute("cart");
		Order order=new Order();
		order.setOid(CommonUtils.uuid());
		order.setOrdertime(new Date());
		order.setOwner((User)request.getSession().getAttribute("session_user"));
		order.setState(1);
		order.setTotal(cart.getTotal());
		//将购物车条目中的信息添加到订单条目中
		List<OrderItem> orderItemList=new ArrayList<OrderItem>();
		for(CartItem cartItem:cart.getCartItemList()){
			OrderItem orderItem=new OrderItem();
			orderItem.setIid(CommonUtils.uuid());//id
			orderItem.setBook(cartItem.getBook());//图书
			orderItem.setCount(cartItem.getCount());//数目
			orderItem.setOrder(order);//所属订单
			orderItem.setSubtotal(cartItem.getSubTotal());//小计
			orderItemList.add(orderItem);
		}
		order.setOrderItemList(orderItemList);
		//将订单信息保存到request
		orderService.add(order);
		cart.clear();
		request.setAttribute("order",order);
		return "f:/jsps/order/desc.jsp";
	}
	public String myOrders(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user=(User) request.getSession().getAttribute("session_user");
		List<Order> orderList=orderService.myOrders(user.getUid());
		request.setAttribute("orderList",orderList);
		return "f:/jsps/order/list.jsp";
	}
	public String confirm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String oid=request.getParameter("oid");
		try{
		orderService.confirm(oid);
		request.setAttribute("msg","恭喜，交易成功");
		}
		catch(OrderException e){
			request.setAttribute("msg",e.getMessage());

		}
		return "f:/jsps/msg.jsp";
	}
	
	public String load(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String oid=request.getParameter("oid");
		request.setAttribute("order",orderService.load(oid));

		return "f:/jsps/order/desc.jsp";
	}
}
