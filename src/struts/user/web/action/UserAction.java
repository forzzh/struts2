package struts.user.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import struts.user.domain.User;
import struts.user.service.UserService;
import sun.misc.BASE64Encoder;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.interceptor.annotations.InputConfig;

public class UserAction extends ActionSupport implements ModelDriven<User>{
	private User user = new User();
	private UserService userService = new UserService();
	@Override
	public User getModel() {
		return user;
	}
	
	/**
	 * 登陆
	 * @return
	 */
	@InputConfig(resultName="loginINPUT")
	public String login(){
		User logonUser = userService.login(user);
		//判断
		if(logonUser==null) {
			this.addActionError(this.getText("loginfail"));
			return "loginINPUT";
		}else{
			ServletActionContext.getRequest().getSession().setAttribute("user", logonUser);
			return "loginSUCCESS";
		}
	}
	
	@InputConfig(resultName="addINPUT")
	public String add() throws IOException {
		if(upload!=null){
			String uuidName = UUID.randomUUID().toString();
			String path = "/WEB-INF/upload/"+uuidName;
			File destFile = new File(ServletActionContext.getServletContext().getRealPath(path));
			FileUtils.copyFile(upload, destFile);
			user.setPath(path);
			user.setFilename(uploadFileName);
		}
		userService.add(user);
		return "addSUCCESS";
	}
	
	private File upload;
	private String uploadContentType;
	private String uploadFileName; //真实文件名
	
	public void setUpload(File upload) {
		this.upload = upload;
	}
	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	public File getUpload() {
		return upload;
	}
	public String getUploadContentType() {
		return uploadContentType;
	}
	public String getUploadFileName() {
		return uploadFileName;
	}
	
	public String list() {
		users = userService.list(user);
		return "listSUCCESS";
	}
	
	//将users放入值栈中，因为action就在值栈中
	private List<User> users;
	public List<User> getUsers() {
		return users;
	}
	
	/**
	 * 员工删除
	 * @return
	 */
	public String delete() {
		//删除简历
		user = userService.findById(user.getUserId());
		if(user.getPath()!=null) {
			File file = new File(ServletActionContext.getServletContext().getRealPath(user.getPath()));
			file.delete();
		}
		//删除数据表
		userService.delete(user);
		return "deleteSUCCESS";
	}
	
	/**
	 * 查看员工
	 * @return
	 */
	public String view() {
		user = userService.findById(user.getUserId());
		return "viewSUCCESS";
	}
	
	/**
	 * 文件的下载 (下载流，2个头信息)
	 * @return
	 */
	public String download() {
		user = userService.findById(user.getUserId());
		return"downloadSUCCESS";
	}
	
	//返回文件流
	public InputStream getInputStream() throws FileNotFoundException {
		File file = new File(ServletActionContext.getServletContext().getRealPath(user.getPath()));
		return new FileInputStream(file);
	}
	
	//返回文件的MIME类型
	public String getContentType() {
		return ServletActionContext.getServletContext().getMimeType(user.getFilename());
	}
	
	//返回文件名
	public String getDownloadFilename() throws IOException {
		return encodeDownloadFilename(user.getFilename(), ServletActionContext.getRequest().getHeader("user-agent"));
	}
	
	//下载文件时，针对不同浏览器，进行附件名的编码
	public String encodeDownloadFilename(String filename, String agent)
			throws IOException {
		if (agent.contains("Firefox")) { // 火狐浏览器
			filename = "=?UTF-8?B?"
					+ new BASE64Encoder().encode(filename.getBytes("utf-8"))
					+ "?=";
		} else { // IE及其他浏览器
			filename = URLEncoder.encode(filename, "utf-8");
		}
		return filename;
	}
	
	/**
	 * 编辑信息回显
	 * @return
	 */
	public String editview() {
		user = userService.findById(user.getUserId());
		return "editviewSUCCESS";
	}
	
	/**
	 * 修改员工信息
	 * @return
	 * @throws IOException 
	 */
	@InputConfig(resultName="editINPUT")
	public String edit() throws IOException {
		if(upload!=null) {
			//上传新简历
			String uuidName = UUID.randomUUID().toString();
			String path = "/WEB-INF/upload/"+uuidName;
			File destFile = new File(ServletActionContext.getServletContext().getRealPath(path));
			FileUtils.copyFile(upload, destFile);
			user.setPath(path);
			user.setFilename(uploadFileName);
			//删除旧简历
			User oldUser = userService.findById(user.getUserId());
			File oldFile = new File(ServletActionContext.getServletContext().getRealPath(oldUser.getPath()));
			oldFile.delete();
			//编辑
			userService.edit(user, true);
		}else{
			//员工没有修改简历
			userService.edit(user, false);
		}
		return "editSUCCESS";
	}
}
