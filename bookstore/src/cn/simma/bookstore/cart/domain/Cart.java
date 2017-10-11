package cn.simma.bookstore.cart.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.simma.bookstore.order.domain.OrderItem;

public class Cart {
	private Map<String,CartItem> cartMap=new LinkedHashMap<String, CartItem>();;
	public double getTotal(){
		BigDecimal total=new BigDecimal("0");
		for(CartItem cartItem:cartMap.values()){
			BigDecimal subTotal=new BigDecimal(cartItem.getSubTotal()+"");
			total=total.add(subTotal);
		}
		return total.doubleValue();
	}
	//添加条目到购物车
	public void add(CartItem cartItem){
		if(cartMap.containsKey(cartItem.getBook().getBid())){
			CartItem _cartItem=cartMap.get(cartItem.getBook().getBid());
			_cartItem.setCount(_cartItem.getCount()+cartItem.getCount());
			cartMap.put(cartItem.getBook().getBid(), _cartItem);

		}
		else{
		cartMap.put(cartItem.getBook().getBid(), cartItem);
		}
	}
	//根据id删除条目
	public void delete(String Bid){
		cartMap.remove(Bid);
	}
	//清空购物车
	public void clear(){
		cartMap.clear();
	}
	public Collection<CartItem> getCartItemList(){
		return cartMap.values();
	}
	public Map<String,CartItem> getCartMap() {
		return cartMap;
	}

	public void setCartMap(Map<String,CartItem> cartMap) {
		this.cartMap = cartMap;
	}
	
}
