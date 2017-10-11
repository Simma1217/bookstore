package cn.simma.bookstore.order.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.jdbc.JdbcUtils;
import cn.simma.bookstore.order.dao.OrderDao;
import cn.simma.bookstore.order.domain.Order;

public class OrderService {
	private OrderDao orderDao=new OrderDao();
	public void add(Order order){
		try{
			JdbcUtils.beginTransaction();
			orderDao.addOrder(order);
			orderDao.addOrderItemList(order.getOrderItemList());
			JdbcUtils.commitTransaction();
		}
		catch(Exception e){
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
			}
			throw new RuntimeException(e);
		}
	}
	public List<Order> myOrders(String uid) {
		
		return orderDao.findByUid(uid);
	}
	public void confirm(String oid) throws OrderException {
		if(orderDao.getStateByOid(oid)!=3) throw new OrderException("非法操作，小心被抓！！！");
		orderDao.updateState(oid,4);
	}
	public Order load(String oid) {
		
		return orderDao.load(oid);
	}
	public void zhiFu(String r6_Order) {
		int state=orderDao.getStateByOid(r6_Order);
		if(state==1){
			orderDao.updateState(r6_Order, 2);
		}
	}
}
