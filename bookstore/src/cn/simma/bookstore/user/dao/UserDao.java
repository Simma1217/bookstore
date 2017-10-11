package cn.simma.bookstore.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.itcast.jdbc.TxQueryRunner;
import cn.simma.bookstore.user.domain.User;

public class UserDao {
	private QueryRunner qr=new TxQueryRunner();
	/**
	 * 按用户名查询
	 * @param Simma
	 * @return
	 */
	public User findByUserName(String uname){
		String sql="select * from user where username=?";
		try {
			return qr.query(sql, new BeanHandler<User>(User.class),uname);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 按邮箱查询
	 * @param Simma
	 * @return
	 */
	public User findByEmail(String email){
		String sql="select * from user where email=?";
		try {
			return qr.query(sql, new BeanHandler<User>(User.class),email);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 添加用户
	 * @param user
	 */
	public void add(User user){
		String sql="insert into user values(?,?,?,?,?,?);";
		Object[] params={user.getUid(),user.getUsername(),user.getPassword(),
							user.getEmail(),user.getCode(),user.isState()};
		try {
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 按激活码查询
	 * @param Simma
	 * @return
	 */
	public User findByCode(String code){
		String sql="select * from user where code=?";
		try {
			return qr.query(sql, new BeanHandler<User>(User.class),code);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}	
	/**
	 * 更新激活状态
	 * @param Simma
	 * @return
	 */
	
	public void updateState(String uid,boolean state){
		String sql="update user set state=? where uid=?;";
		try {
			qr.update(sql, state,uid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
