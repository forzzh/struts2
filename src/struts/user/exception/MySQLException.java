package struts.user.exception;

/**
 * �Զ�����쳣
 * @author zzh
 *
 */
public class MySQLException extends RuntimeException{
	public MySQLException(Throwable throwable) {
		super(throwable);
	}
	public MySQLException(String message) {
		super(message);
	}
	public MySQLException() {
		super();
	}
}
