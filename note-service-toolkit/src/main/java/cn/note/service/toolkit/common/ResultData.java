package cn.note.service.toolkit.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 数据包裹类
 *
 * @param <T>
 */
@Setter
@Getter
@AllArgsConstructor
public class ResultData<T> {

    private boolean success;
    private T data;

    public static <T> ResultData<T> ok() {
        return new ResultData<T>(true, null);
    }

    public static <T> ResultData<T> ok(T data) {
        return new ResultData<T>(true, data);
    }

    public static <T> ResultData<T> fail() {
        return new ResultData<T>(false, null);
    }

    public static <T> ResultData<T> fail(T data) {
        return new ResultData<T>(false, data);
    }

}
