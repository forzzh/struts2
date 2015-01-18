package struts.user.exception;

/**
 * 自定义的异常
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
