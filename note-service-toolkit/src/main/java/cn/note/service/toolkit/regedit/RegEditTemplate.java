package cn.note.service.toolkit.regedit;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.note.service.toolkit.util.AESUtil;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * 注册表信息
 * Preferences 按照习惯得方式设计
 * 根路径
 * \HKEY_CURRENT_USER\SOFTWARE\JavaSoft\Prefs
 *
 * @author jee
 * @version 1.0
 */
public class RegEditTemplate {

    private Preferences preferences;

    public RegEditTemplate(Preferences preferences) {
        this.preferences = preferences;
    }


    /**
     * 添加信息至注册表
     *
     * @param key   存放键
     * @param value 存放值
     */
    public void put(String key, String value) {
        this.preferences.put(key, value);
    }

    /**
     * 通过键获取存放信息
     *
     * @param key 存放键
     */
    public String get(String key) throws RegException {
        if (contains(key)) {
            return this.preferences.get(key, null);
        }
        return null;
    }

    /**
     * 通过键值删除存放信息
     *
     * @param key 移除键
     */
    public void remove(String key) {
        this.preferences.remove(key);
    }


    /**
     * 判断键值是否存在
     *
     * @param key 存放键
     * @return 是否包含结果
     */
    public boolean contains(String key) throws RegException {
        return keys().contains(key);

    }


    /**
     * 获取存放的数量
     *
     * @return 键值得数量
     */
    public int size() throws RegException {
        return keys().size();
    }


    /**
     * 获取所有的键值集合
     *
     * @return 返回集合键值
     */
    public List<String> keys() throws RegException {
        try {
            return Arrays.asList(this.preferences.keys());
        } catch (BackingStoreException e) {
            throw new RegException("获取键值集合异常", e);
        }
    }

    /**
     * 以json格式存储
     *
     * @param key   存储key
     * @param value 存储值
     */
    public void putJson(String key, JSONObject value) {
        put(key, JSONUtil.toJsonStr(value));
    }


    /**
     * 返回json 结构数据
     *
     * @param key 键值key
     * @return 键值对应对象
     * @throws RegException 存储异常
     */
    public JSONObject getJson(String key) throws RegException {
        String value = get(key);
        return JSONUtil.parseObj(value);
    }


    /**
     * 加密存储key 值
     *
     * @param key   存储key
     * @param value 存储值
     */
    public void putCrypt(String key, String value) {
        put(key, AESUtil.encrypt(value));
    }

    /**
     * 获取解密后的键值
     *
     * @param key 键值
     * @return 解密后的存储值
     * @throws RegException 获取值异常
     */
    public String getCrypt(String key) throws RegException {
        return AESUtil.decrypt(get(key));
    }
}
