package struts.user.web.interceptor;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * ��½������
 * @author zzh
 *
 */
public class LoginInterceptor extends AbstractInterceptor{
	private String excludeMethods;
	public void setEncludeMethod(String excludeMethods) {
		this.excludeMethods = excludeMethods;
	}



	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		if(ServletActionContext.getRequest().getSession().getAttribute("user")==null){
			//�ж��Ƿ����÷���
			if(excludeMethods != null) {
				String[] methods = excludeMethods.split(",");
				String currentMethod = ActionContext.getContext().getName();
				for (String method : methods) {
					if(method==currentMethod) {
						return invocation.invoke();
					}
				}
			}
			//δ��¼
			ActionSupport action = (ActionSupport) invocation.getAction();
			action.addActionError(action.getText("noLogin"));
			
			return "login";
		}else{
			//����
			return invocation.invoke();
		}
	}
	
}
