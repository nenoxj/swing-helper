package cn.note.swing.core.util;

import cn.hutool.core.util.StrUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 使用cmd测试常用功能
 * @author jee
 */
public class CmdUtil {

    /**
     * 显示cmd窗口
     *
     * @param callable 回调实现
     */
    public static void show(CmdCallable callable) {

        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader buffer = new BufferedReader(is);
        try {
            System.out.println("exit退出命令模式!");
            while (true) {
                String cmd = buffer.readLine().trim();
                if (cmd.equals("exit")) {
                    System.out.println("程序已退出!!");
                    System.exit(0);
                    break;
                }
                callable.call(cmd);
            }
        } catch (IOException e) {
            System.err.println("获取操作指令异常!!");
            e.printStackTrace();
        }

    }


    /**
     * @param msg    打印主内容
     * @param others 其他参数
     */
    public static void print(String msg, Object... others) {
        System.out.println(StrUtil.format(msg, others));
    }


    /**
     * 命令回调
     */
    public interface CmdCallable {

        /**
         * @param cmd 命令参数
         */
        void call(String cmd);
    }

}
