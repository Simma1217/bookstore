package cn.simma.bookstore.category.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import cn.itcast.jdbc.TxQueryRunner;
import cn.simma.bookstore.category.domain.Category;

public class CategoryDao {
	private QueryRunner qr=new TxQueryRunner();
	public List<Category> findByAll(){
		String sql="select * from category;";
		try {
			return qr.query(sql,new BeanListHandler<Category>(Category.class));
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	public void add(Category category) {
		String sql="insert into category values(?,?);";
		try {
			qr.update(sql,category.getCid(),category.getCname());
		} catch (SQLException e) {
			throw new RuntimeException();
		}		
	}
	public void delete(String cid) {
		String sql="delete from category where cid=?";
		try {
			qr.update(sql,cid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}		
	}
	public Category findByCid(String cid){
		String sql="select * from category where cid=?;";
		try {
			return qr.query(sql,new BeanHandler<Category>(Category.class),cid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	public void edit(Category category) {
		String sql="update category set cname=? where cid=?";
		try {
			qr.update(sql,category.getCname(),category.getCid());
		} catch (SQLException e) {
			throw new RuntimeException();
		}		
	}
}
