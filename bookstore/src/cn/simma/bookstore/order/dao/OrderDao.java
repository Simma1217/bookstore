package cn.simma.bookstore.order.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import cn.simma.bookstore.book.domain.Book;
import cn.simma.bookstore.order.domain.Order;
import cn.simma.bookstore.order.domain.OrderItem;

public class OrderDao {
	QueryRunner qr=new TxQueryRunner();
	public int getStateByOid(String oid){
		try {
		String sql="select state from orders where oid=?";
		return (Integer)qr.query(sql,new ScalarHandler(),oid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public void updateState(String oid,int state){
		try {
		String sql="update orders set state=? where oid=?";
		qr.update(sql,state,oid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public void addOrder(Order order){
		try {
		//处理util的Date转化成sql的Timestamp
		String sql="insert into orders values(?,?,?,?,?,?);";
		Object[] params={order.getOid(),new Timestamp(order.getOrdertime().getTime()),order.getTotal(),order.getState(),order.getOwner().getUid(),order.getAddress()};
			qr.update(sql,params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	public void addOrderItemList(List<OrderItem> orderItemsList){
		try {
		String sql="insert into orderitem values(?,?,?,?,?);";
		Object[][] params=new Object[orderItemsList.size()][];
		for(int i=0;i<orderItemsList.size();i++){
			OrderItem orderItem=orderItemsList.get(i);
			params[i]=new Object[]{orderItem.getIid(),orderItem.getCount(),orderItem.getSubtotal(),orderItem.getOrder().getOid(),
					orderItem.getBook().getBid()};
			}
		qr.batch(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public List<Order> findByUid(String uid){
		try {
			//得到当前用户的所有订单
			String sql="select * from orders where uid=?";
			//得到当前用户的全部订单
			List<Order> orderList=qr.query(sql,new BeanListHandler<Order>(Order.class),uid);
			for(Order order:orderList){
				loadOrderItems(order);
			}
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}		
	}
	//加载订单条目
	private void loadOrderItems(Order order) throws SQLException {
		String sql1="select * from orderitem o,book b where o.bid=b.bid and oid=?";
		List<Map<String, Object>> mapList;
			mapList = qr.query(sql1,new MapListHandler(),order.getOid());
			List<OrderItem> orderItemList=toOrderItemList(mapList);
			order.setOrderItemList(orderItemList);	
	}
	//将查询条目转换为订单条目
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		List<OrderItem> orderItemList=new ArrayList<OrderItem>();
		for(Map<String,Object> map:mapList){
			OrderItem orderItem=add(map);
			orderItemList.add(orderItem);
		}

		return orderItemList;
	}
	//将map转换为OrderItem
	private OrderItem add(Map<String, Object> map) {
		OrderItem orderItem=CommonUtils.toBean(map,OrderItem.class);
		Book book=CommonUtils.toBean(map,Book.class);
		orderItem.setBook(book);
		return orderItem;
	}
	public Order load(String oid) {
		//获取oid对应的订单
		String sql="select * from orders where oid=?";
		try {
			Order order=qr.query(sql,new BeanHandler<Order>(Order.class),oid);
			//获取订单对应的订单条目
			loadOrderItems(order);
			return order;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
