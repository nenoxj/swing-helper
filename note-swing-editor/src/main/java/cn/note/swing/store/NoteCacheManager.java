package cn.note.swing.store;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LFUCache;

import javax.swing.*;

/**
 * 缓存管理
 *
 * @author jee
 * @version 1.0
 */
public class NoteCacheManager {

    private static LFUCache<String, JComponent> NOTE_TAB_CACHE;


    /**
     * 获取选项卡缓存
     * 采用LFU策略 最少使用策略
     */
    public static Cache<String, JComponent> getNoteTabCache() {
        if (NOTE_TAB_CACHE == null) {
            NOTE_TAB_CACHE = CacheUtil.newLFUCache(20);
        }
        return NOTE_TAB_CACHE;
    }
}
