package cn.simma.bookstore.book.servlet;


import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import cn.simma.bookstore.book.domain.Book;
import cn.simma.bookstore.book.service.BookService;

public class BookServlet extends BaseServlet {
	private BookService bookService=new BookService();

	public String findByAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<Book> bookList=bookService.findByAll();
		req.setAttribute("bookList", bookList);
		return "f:/jsps/book/list.jsp";
	}
	public String findByCategory(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid=req.getParameter("cid");
		List<Book> bookList=bookService.findByCategory(cid);
		req.setAttribute("bookList", bookList);
		return "f:/jsps/book/list.jsp";
	}
	public String findByBid(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String bid=req.getParameter("bid");
		Book book=bookService.findByBid(bid);
		req.setAttribute("book", book);
		return "f:/jsps/book/desc.jsp";
	}
}
