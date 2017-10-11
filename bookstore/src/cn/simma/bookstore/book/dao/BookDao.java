package cn.simma.bookstore.book.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import cn.simma.bookstore.book.domain.Book;
import cn.simma.bookstore.category.domain.Category;

public class BookDao {
	QueryRunner qr= new TxQueryRunner();
	/*
	 * 查询全部书籍
	 */
	public List<Book> findByAll(){
		String sql="select * from book where del=false;";
		try {
			List<Book> bookList=qr.query(sql,new BeanListHandler<Book>(Book.class));
			return bookList;
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	public int getCountByCid(String cid){
		String sql="select count(*) from book where cid=? and del=false;";
		try {
			Number number=(Number) qr.query(sql,new ScalarHandler(),cid);
			return number.intValue();
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	/*
	 * 按分类查询
	 */
	public List<Book> findByCategory(String cid){
		String sql="select * from book where cid=? and del=false;";
		try {
			List<Book> bookList=qr.query(sql,new BeanListHandler<Book>(Book.class),cid);
			return bookList;
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	/*
	 * 按bid查询
	 */
	public Book findByBid(String bid){
		String sql="select * from book where bid=?;";
		try {
			Map map=qr.query(sql,new MapHandler(),bid);
			Book book=CommonUtils.toBean(map,Book.class);
			Category category=CommonUtils.toBean(map,Category.class);
			book.setCategory(category);
			return book;
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	/**
	 * 添加图书
	 * @param book
	 */
	public void add(Book book){
		String sql="insert into book values(?,?,?,?,?,?,?)";
		try {
			Object[] params={book.getBid(), book.getBname(), book.getPrice(),
					book.getAuthor(), book.getImage(), book.getCategory().getCid(),false};
			qr.update(sql,params);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	/**
	 * 删除图书
	 * @param book
	 */
	public void delete(String bid){
		String sql="update book set del=true where bid=?";
		try {
			qr.update(sql,bid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	public void edit(Book book) {
		try {
			String sql = "update book set bname=?, price=?,author=?, image=?, cid=? where bid=?";
			Object[] params = {book.getBname(), book.getPrice(),
					book.getAuthor(), book.getImage(), 
					book.getCategory().getCid(), book.getBid()};
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
		
	}
}
