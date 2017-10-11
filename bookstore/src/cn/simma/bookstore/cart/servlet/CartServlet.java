package cn.simma.bookstore.cart.servlet;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.itcast.servlet.BaseServlet;
import cn.simma.bookstore.book.domain.Book;
import cn.simma.bookstore.book.service.BookService;
import cn.simma.bookstore.cart.domain.Cart;
import cn.simma.bookstore.cart.domain.CartItem;

public class CartServlet extends BaseServlet {	
	//添加条目
	public String add(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//获取购物车，添加条目
		Cart cart=(Cart) req.getSession().getAttribute("cart");
		String bid=req.getParameter("bid");
		Book book=new BookService().findByBid(bid);
		int count=Integer.parseInt(req.getParameter("count"));
		CartItem cartItem=new CartItem();
		cartItem.setBook(book);
		cartItem.setCount(count);
		cart.add(cartItem);
		req.getSession().setAttribute("cart",cart);
		return "f:/jsps/cart/list.jsp";
	}
	//清空购物车
		public String clear(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			//获取购物车
			Cart cart=(Cart) req.getSession().getAttribute("cart");
			cart.clear();
			req.getSession().setAttribute("cart",cart);
			return "f:/jsps/cart/list.jsp";
		}
	//删除条目
			public String delete(HttpServletRequest req, HttpServletResponse resp)
					throws ServletException, IOException {
				//获取购物车
				Cart cart=(Cart) req.getSession().getAttribute("cart");
				String bid=req.getParameter("bid");
				cart.delete(bid);
				req.getSession().setAttribute("cart",cart);
				return "f:/jsps/cart/list.jsp";
			}	
}
