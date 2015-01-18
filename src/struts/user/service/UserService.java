package struts.user.service;

import java.util.List;

import struts.user.dao.UserDao;
import struts.user.domain.User;

public class UserService {
	private UserDao dao = new UserDao();
	
	public User login(User user){
		return dao.findUser(user);
	}

	public void add(User user) {
		dao.save(user);
		
	}

	/**
	 * 员工列表查询
	 * @param user
	 * @return
	 */
	public List<User> list(User user) {
		List<User> list = dao.list(user);
		return list;
	}

	public void delete(User user) {
		//删除数据表
		dao.delete(user);
	}

	public User findById(int userId) {
		return dao.findById(userId);
	}

	/**
	 * 
	 * @param user	员工信息
	 * @param b		是否编辑简历
	 */
	public void edit(User user, boolean b) {
		dao.edit(user, b);
		
	}
}
