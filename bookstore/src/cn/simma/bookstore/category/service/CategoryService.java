package cn.simma.bookstore.category.service;

import java.util.List;

import cn.simma.bookstore.book.dao.BookDao;
import cn.simma.bookstore.category.dao.CategoryDao;
import cn.simma.bookstore.category.domain.Category;

public class CategoryService {
	private CategoryDao categoryDao=new CategoryDao();
	private BookDao bookDao=new BookDao();
	public List<Category> findByAll(){
		return categoryDao.findByAll();
	}
	public void add(Category category) {
		categoryDao.add(category);
		
	}
	public void delete(String cid) throws CategoryException {
		int count=bookDao.getCountByCid(cid);
		if(count>0) throw new CategoryException("该分类下有图书，不能删除！！！");
		categoryDao.delete(cid);
	}
	public Category editPre(String cid) {
		
		return categoryDao.findByCid(cid);
				
	}
	public void edit(Category category) {
		
		categoryDao.edit(category);
	}
}
