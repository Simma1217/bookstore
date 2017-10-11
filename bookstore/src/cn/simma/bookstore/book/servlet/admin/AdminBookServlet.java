package cn.simma.bookstore.book.servlet.admin;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import cn.simma.bookstore.book.domain.Book;
import cn.simma.bookstore.book.service.BookService;
import cn.simma.bookstore.category.domain.Category;
import cn.simma.bookstore.category.service.CategoryService;

public class AdminBookServlet extends BaseServlet {
	BookService bookService=new BookService();
	CategoryService categoryService=new CategoryService();
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("bookList",bookService.findByAll());
		return "f:/adminjsps/admin/book/list.jsp";
	}
	public String load(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String bid=request.getParameter("bid");
		request.setAttribute("book",bookService.findByBid(bid));
		request.setAttribute("categoryList", categoryService.findByAll());
		return "f:/adminjsps/admin/book/desc.jsp";
	}
	public String addPre(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("categoryList", categoryService.findByAll());
		return "f:/adminjsps/admin/book/add.jsp";
	}
	public String delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String bid=request.getParameter("bid");
		bookService.delete(bid);
		return findAll(request, response);
	}
	public String edit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Book book=CommonUtils.toBean(request.getParameterMap(),Book.class);
		Category category=CommonUtils.toBean(request.getParameterMap(),Category.class);
		book.setCategory(category);
		bookService.edit(book);
		return findAll(request, response);
	}
	
}
