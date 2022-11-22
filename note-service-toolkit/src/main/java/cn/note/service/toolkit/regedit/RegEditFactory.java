package cn.note.service.toolkit.regedit;

import java.util.prefs.Preferences;

/**
 * @author jee
 * @version 1.0
 */
public class RegEditFactory {

    /**
     * 默认的注册表存储
     */
    private static final String NOTE_STORE = "note-store";

    public static RegEditTemplate defaultRegEdit() {
        return userRegEdit(NOTE_STORE);
    }

    /**
     * @param names 存储名称
     * @return 用户注册表操作对象
     */
    public static RegEditTemplate userRegEdit(String... names) {
        Preferences preferences = build(RegType.user, names);
        return new RegEditTemplate(preferences);
    }

    /**
     * @param names 存储名称
     * @return 系统注册表操作对象
     */
    public static RegEditTemplate systemRegEdit(String... names) {
        Preferences preferences = build(RegType.admin, names);
        return new RegEditTemplate(preferences);
    }


    private static Preferences build(RegType regType, String... names) {
        Preferences preferences = null;
        switch (regType) {
            case user:
                preferences = Preferences.userRoot();
                break;
            case admin:
                preferences = Preferences.systemRoot();
                break;
        }
        for (String name : names) {
            preferences = preferences.node(name);
        }
        return preferences;
    }

    enum RegType {

        /**
         * 用户
         */
        user,
        /**
         * 管理员
         */
        admin;
    }
}
