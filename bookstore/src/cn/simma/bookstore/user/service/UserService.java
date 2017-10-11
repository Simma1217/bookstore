package cn.simma.bookstore.user.service;

import cn.simma.bookstore.user.dao.UserDao;
import cn.simma.bookstore.user.domain.User;

public class UserService {
	UserDao userDao=new UserDao();
	public void regist(User form) throws UserException{
		User user=userDao.findByUserName(form.getUsername());
		if(user!=null){
			throw new UserException("用户名已存在！");
		}
		user=userDao.findByEmail(form.getEmail());
		if(user!=null){
			throw new UserException("邮箱已经被注册！");
		}
		userDao.add(form);
	}
	public  void active(String code) throws UserException {
		User user=userDao.findByCode(code);
		if(user==null){
			throw new UserException("激活码无效");
		}
		if(user.isState()){
			throw new UserException("您已激活过，请不要重复激活");			
		}
		userDao.updateState(user.getUid(), true);
	}
	public User login(User form) throws UserException {
		User user=userDao.findByUserName(form.getUsername());
		if(user==null){
			throw new UserException("用户名不存在");
		}
		if(!form.getPassword().equals(user.getPassword())){
			throw new UserException("密码错误");
		}
		if(!user.isState()){
			throw new UserException("您尚未激活，请先激活！");
		}
		return user;
	}
}
