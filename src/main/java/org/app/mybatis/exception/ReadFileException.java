package org.app.mybatis.exception;

/**
 * 
 * @ClassName: ReadFileException
 * @Description: 读取模板文件异常类
 * @author hncdyj123@163.com
 * @date 2015年11月21日 下午12:25:26
 *
 */
@SuppressWarnings("serial")
public class ReadFileException extends Exception {
	public ReadFileException() {

	}

	public ReadFileException(String message) {
		super(message);
	}
}
