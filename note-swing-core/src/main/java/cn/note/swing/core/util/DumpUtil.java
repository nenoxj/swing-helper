package cn.note.swing.core.util;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

/**
 * 堆栈帮助类
 *
 * @author jee
 */
public class DumpUtil {
    private static final long MB_SIZE = 1024 * 1024;

    /**
     * 格式化数据
     *
     * @param size
     * @return
     */
    private static String parseSizeData(String title, long size) {
        return title.concat(size / MB_SIZE + "MB");
    }

    /**
     * 读取内存镜像
     *
     * @return
     */
    public static String getDumpInfo(String title) {
        MemoryMXBean mxb = ManagementFactory.getMemoryMXBean();
        return parseSizeData(title, mxb.getHeapMemoryUsage().getUsed());
    }


    /**
     * 堆栈信息
     */
    public static void debugDumpDetail() {
        MemoryMXBean mxb = ManagementFactory.getMemoryMXBean();
        //Heap
        System.out.println("Max:" + mxb.getHeapMemoryUsage().getMax() / 1024 / 1024 + "MB");
        System.out.println("Init:" + mxb.getHeapMemoryUsage().getInit() / 1024 / 1024 + "MB");
        System.out.println("Committed:" + mxb.getHeapMemoryUsage().getCommitted() / 1024 / 1024 + "MB");
        System.out.println("Used:" + mxb.getHeapMemoryUsage().getUsed() / 1024 / 1024 + "MB");
        System.out.println(mxb.getHeapMemoryUsage().toString());

        //Non heap
        System.out.println("Max:" + mxb.getNonHeapMemoryUsage().getMax() / 1024 / 1024 + "MB");
        System.out.println("Init:" + mxb.getNonHeapMemoryUsage().getInit() / 1024 / 1024 + "MB");
        System.out.println("Committed:" + mxb.getNonHeapMemoryUsage().getCommitted() / 1024 / 1024 + "MB");
        System.out.println("Used:" + mxb.getNonHeapMemoryUsage().getUsed() / 1024 / 1024 + "MB");
        System.out.println(mxb.getNonHeapMemoryUsage().toString());

    }

}
