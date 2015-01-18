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
	 * ��½
	 * @return
	 */
	@InputConfig(resultName="loginINPUT")
	public String login(){
		User logonUser = userService.login(user);
		//�ж�
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
	private String uploadFileName; //��ʵ�ļ���
	
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
	
	//��users����ֵջ�У���Ϊaction����ֵջ��
	private List<User> users;
	public List<User> getUsers() {
		return users;
	}
	
	/**
	 * Ա��ɾ��
	 * @return
	 */
	public String delete() {
		//ɾ������
		user = userService.findById(user.getUserId());
		if(user.getPath()!=null) {
			File file = new File(ServletActionContext.getServletContext().getRealPath(user.getPath()));
			file.delete();
		}
		//ɾ�����ݱ�
		userService.delete(user);
		return "deleteSUCCESS";
	}
	
	/**
	 * �鿴Ա��
	 * @return
	 */
	public String view() {
		user = userService.findById(user.getUserId());
		return "viewSUCCESS";
	}
	
	/**
	 * �ļ������� (��������2��ͷ��Ϣ)
	 * @return
	 */
	public String download() {
		user = userService.findById(user.getUserId());
		return"downloadSUCCESS";
	}
	
	//�����ļ���
	public InputStream getInputStream() throws FileNotFoundException {
		File file = new File(ServletActionContext.getServletContext().getRealPath(user.getPath()));
		return new FileInputStream(file);
	}
	
	//�����ļ���MIME����
	public String getContentType() {
		return ServletActionContext.getServletContext().getMimeType(user.getFilename());
	}
	
	//�����ļ���
	public String getDownloadFilename() throws IOException {
		return encodeDownloadFilename(user.getFilename(), ServletActionContext.getRequest().getHeader("user-agent"));
	}
	
	//�����ļ�ʱ����Բ�ͬ����������и������ı���
	public String encodeDownloadFilename(String filename, String agent)
			throws IOException {
		if (agent.contains("Firefox")) { // ��������
			filename = "=?UTF-8?B?"
					+ new BASE64Encoder().encode(filename.getBytes("utf-8"))
					+ "?=";
		} else { // IE�����������
			filename = URLEncoder.encode(filename, "utf-8");
		}
		return filename;
	}
	
	/**
	 * �༭��Ϣ����
	 * @return
	 */
	public String editview() {
		user = userService.findById(user.getUserId());
		return "editviewSUCCESS";
	}
	
	/**
	 * �޸�Ա����Ϣ
	 * @return
	 * @throws IOException 
	 */
	@InputConfig(resultName="editINPUT")
	public String edit() throws IOException {
		if(upload!=null) {
			//�ϴ��¼���
			String uuidName = UUID.randomUUID().toString();
			String path = "/WEB-INF/upload/"+uuidName;
			File destFile = new File(ServletActionContext.getServletContext().getRealPath(path));
			FileUtils.copyFile(upload, destFile);
			user.setPath(path);
			user.setFilename(uploadFileName);
			//ɾ���ɼ���
			User oldUser = userService.findById(user.getUserId());
			File oldFile = new File(ServletActionContext.getServletContext().getRealPath(oldUser.getPath()));
			oldFile.delete();
			//�༭
			userService.edit(user, true);
		}else{
			//Ա��û���޸ļ���
			userService.edit(user, false);
		}
		return "editSUCCESS";
	}
}
