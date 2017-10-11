package cn.simma.bookstore.category.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import cn.simma.bookstore.category.domain.Category;
import cn.simma.bookstore.category.service.CategoryService;

public class CategoryServlet extends BaseServlet {
	private CategoryService categoryService=new CategoryService();
	public String findByAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Category> categoryList=categoryService.findByAll();
		request.setAttribute("categories",categoryList);
		return "f:/jsps/left.jsp";
	}
}
