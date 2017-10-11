package cn.simma.bookstore.category.servlet.admin;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import cn.simma.bookstore.category.domain.Category;
import cn.simma.bookstore.category.service.CategoryException;
import cn.simma.bookstore.category.service.CategoryService;

public class AdminCategoryServlet extends BaseServlet {
	CategoryService categoryService=new CategoryService();
	/**
	 * 查看所有图书分类
	 * @param request
	 * @param response
	 * @return
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws javax.servlet.ServletException ,java.io.IOException {
		List<Category> categoryList=categoryService.findByAll();
	request.setAttribute("categories",categoryList);
	return "f:/adminjsps/admin/category/list.jsp";
	}
	public String add(HttpServletRequest request, HttpServletResponse response)
			throws javax.servlet.ServletException ,java.io.IOException {
		Category category=CommonUtils.toBean(request.getParameterMap(),Category.class);
		category.setCid(CommonUtils.uuid());
		categoryService.add(category);
		
	return findAll(request,response);
	}
	public String delete(HttpServletRequest request, HttpServletResponse response)
			throws javax.servlet.ServletException ,java.io.IOException {
		String cid=request.getParameter("cid");
		try{
		categoryService.delete(cid);
		}
		catch(CategoryException e){
			request.setAttribute("msg", e.getMessage());
			return "/adminjsps/msg.jsp";
		}
	return findAll(request,response);
	}
	public String editPre(HttpServletRequest request, HttpServletResponse response)
			throws javax.servlet.ServletException ,java.io.IOException {
			String cid=request.getParameter("cid");
			request.setAttribute("category", categoryService.editPre(cid));
			return "/adminjsps/admin/category/mod.jsp";
		}
	public String edit(HttpServletRequest request, HttpServletResponse response)
			throws javax.servlet.ServletException ,java.io.IOException {
			Category category=CommonUtils.toBean(request.getParameterMap(),Category.class);
			categoryService.edit(category);
			request.setAttribute("msg", "修改成功");
			return findAll(request, response);
	}
	}

