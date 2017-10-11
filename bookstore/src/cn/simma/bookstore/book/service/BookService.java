package cn.simma.bookstore.book.service;

import java.util.List;

import cn.simma.bookstore.book.dao.BookDao;
import cn.simma.bookstore.book.domain.Book;

public class BookService {
	private BookDao bookDao=new BookDao();
	public List<Book> findByAll(){
		return bookDao.findByAll();
	}
	public List<Book> findByCategory(String cid){
		return bookDao.findByCategory(cid);
	}
	public Book findByBid(String bid){
		return bookDao.findByBid(bid);
	}
	public void add(Book book){
		bookDao.add(book);
	}
	public void delete(String bid){
		bookDao.delete(bid);
	}
	public void edit(Book book) {
		bookDao.edit(book);
		
	}
}
