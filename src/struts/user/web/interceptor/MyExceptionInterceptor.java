package struts.user.web.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 自定义异常拦截器
 * @author zzh
 *
 */
public class MyExceptionInterceptor extends AbstractInterceptor{

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		try {
			return invocation.invoke();
		} catch (Exception e) {
			ActionSupport action = (ActionSupport) invocation.getAction();
			action.addActionError(e.getMessage());
			e.printStackTrace();
			return "error";
		}
	}
	
}
