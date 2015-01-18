package struts.user.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import struts.user.domain.User;
import struts.user.utils.JDBCUtils;

public class UserDao {
	private QueryRunner qr = new QueryRunner(JDBCUtils.getDataSource());
	/**
	 * 登陆查询
	 * @param user
	 * @return
	 */
	public User findUser(User user) {
		String sql = "select * from s_user where logonName=? and logonPwd=?";
		User logonUser;
		try {
			logonUser = qr.query(sql, new BeanHandler<User>(User.class), user.getLogonName(), user.getLogonPwd());
			return logonUser;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 保存用户
	 * @param user
	 */
	public void save(User user) {
		String sql = "insert into s_user values (null,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] args = {user.getUserName(),user.getLogonName(),user.getLogonPwd(),user.getSex()
				,user.getBirthday(),user.getEducation(),user.getTelephone(),user.getInterest(),
				user.getPath(),user.getFilename(),user.getRemark()}; 
		try {
			qr.update(sql,args);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 组合查询
	 * @param user
	 * @return
	 */
	public List<User> list(User user) {
		String sql = "select * from s_user where 1=1";
		//封装数据
		List<String> argList = new ArrayList<String>();
		String name = user.getUserName();
		if(name!=null && !name.trim().isEmpty()){
			//注意空格, 模糊查询
			sql = sql + " and userName like ?";
			argList.add("%"+name+"%");
		}
		
		String sex = user.getSex();
		if(sex!=null && !sex.trim().isEmpty()){
			sql = sql + " and sex=?";
			argList.add(sex);
		}
		
		String education = user.getEducation();
		if(education!=null && !education.trim().isEmpty()){
			sql = sql + " and education=?";
			argList.add(education);
		}
		
		String isUpload = user.getIsUpload();
		if(isUpload!=null && !isUpload.trim().isEmpty()){
			if(isUpload.equals("1")){
				//上传过了简历
				sql += " and filename is not null";
			}else if(isUpload.equals("2")) {
				sql += " and filename is null";
			}
		}
		
		try {
			List<User> userList = qr.query(sql, new BeanListHandler<User>(User.class), argList.toArray());
			return userList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void delete(User user) {
		String sql = "delete from s_user where userID = ?";
		try {
			qr.update(sql, user.getUserId());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据id查询
	 * @param userId
	 * @return
	 */
	public User findById(int userId) {
		String sql = "select * from s_user where userID = ?";
		try {
			User user = qr.query(sql, new BeanHandler<User>(User.class), userId);
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void edit(User user, boolean b) {
		if(b) {
			String sql = "update s_user set userName=?, logonName=?, logonPwd=?, sex=?, birthday=?"
					+ ", education=?, telephone=?, interest=?, path=?, filename=?, remark=? where"
					+ " userID=?";
			Object[] args = {user.getUserName(),user.getLogonName(),user.getLogonPwd(),user.getSex()
					,user.getBirthday(),user.getEducation(),user.getTelephone(),user.getInterest(),
					user.getPath(),user.getFilename(),user.getRemark(),user.getUserId()}; 
			try {
				qr.update(sql, args);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		} else {
			String sql = "update s_user set userName=?, logonName=?, logonPwd=?, sex=?, birthday=?"
					+ ", education=?, telephone=?, interest=?, remark=? where"
					+ " userID=?";
			Object[] args = {user.getUserName(),user.getLogonName(),user.getLogonPwd(),user.getSex()
					,user.getBirthday(),user.getEducation(),user.getTelephone(),user.getInterest(),
					user.getRemark(),user.getUserId()}; 
			
			try {
				qr.update(sql, args);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
	}
}
