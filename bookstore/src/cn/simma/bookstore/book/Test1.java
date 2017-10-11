package cn.simma.bookstore.book;

import java.util.List;

import org.junit.Test;

import cn.simma.bookstore.book.dao.BookDao;
import cn.simma.bookstore.book.domain.Book;

public class Test1 {
	@Test
	public void test(){
		BookDao b=new BookDao();
		List<Book> lb=b.findByAll();
		System.out.println(lb);
		System.out.println("hahah");
	}
}
