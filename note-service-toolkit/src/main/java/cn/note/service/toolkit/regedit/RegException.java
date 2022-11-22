package cn.note.service.toolkit.regedit;

/**
 * 注册表异常类
 *
 * @author jee
 * @version 1.0
 */
public class RegException extends Exception {

    public RegException(String error, Exception e) {
        super(error, e);
    }

}
