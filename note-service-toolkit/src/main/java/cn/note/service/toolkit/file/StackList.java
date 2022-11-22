package cn.note.service.toolkit.file;

import java.util.ArrayList;
import java.util.List;

/**
 * 栈list结构
 */
public class StackList<T> {

    private int index = -1;
    private List<T> stackList;

    public StackList() {
        stackList = new ArrayList<>(100);
    }

    /**
     * 添加元素
     *
     * @param obj 元素对象
     * @return 对象信息
     */
    public T push(T obj) {
        index++;
        stackList.add(obj);
        return obj;
    }

    /**
     * 取出元素
     *
     * @return 对象信息
     */
    public T pop() {
        if (index == -1) {
            return null;
        }
        return stackList.remove(index--);
    }

    /**
     * 连续取出前一个
     *
     * @return 对象信息
     */
    public T popPrev() {
        this.pop();
        return this.pop();
    }
}
