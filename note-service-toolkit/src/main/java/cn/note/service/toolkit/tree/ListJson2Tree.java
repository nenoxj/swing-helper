package cn.note.service.toolkit.tree;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

/**
 * ListJson 转tree 工具
 *
 * @author jee
 * @version 1.0
 */
public class ListJson2Tree {

    public static final String ID_KEY = "idKey";
    public static final String PARENT_KEY = "parentKey";
    public static final String DEEP_KEY = "deepKey";
    public static final String CHILDREN_KEY = "childrenKey";
    public static final String TREE_FIELDS = "treeFields";
    public static final String ORIGIN_KEY = "originKey";
    public static final String NICK_KEY = "nickKey";
    /**
     * 模板格式
     * {
     * "idKey":"",
     * "parentKey":"",
     * "childrenKey":"",
     * "idType":"",
     * "rootId":"",
     * "treeFields":[{
     * "originKey":"",
     * "nickKey":"",
     * }]
     * }
     */
    private JSONObject configJson;


    private List<JSONObject> listData;

    public ListJson2Tree(@Nonnull List<JSONObject> listData, @Nonnull JSONObject configJson) {
        this.listData = listData;
        this.configJson = configJson;
    }


    /**
     * 构建树配置信息
     * 三要素: id ,parentId,chidren
     * 及深度deep
     */
    private TreeNodeConfig buildTreeConfig() {
        TreeNodeConfig config = new TreeNodeConfig();
        config.setIdKey(configJson.getStr(ID_KEY));
        config.setParentIdKey(configJson.getStr(PARENT_KEY));
        //是否设置遍历深度
        if (configJson.containsKey(DEEP_KEY)) {
            config.setDeep(configJson.getInt(DEEP_KEY));
        }
        config.setChildrenKey(configJson.getStr(CHILDREN_KEY));
        return config;

    }


    /**
     * 构建树结构
     *
     * @param parentId 父节点ID
     * @param <E>      父节点类型
     * @return 转换后得树结构
     */
    public <E> List<Tree<E>> buildTree(E parentId) {
        TreeNodeConfig config = buildTreeConfig();
        return TreeUtil.build(listData, parentId, config, (treeViewModel, tree) -> {
            JSONObject newTree = ObjectUtil.cloneByStream(treeViewModel);
            // 映射key处理
            JSONArray treeFields = configJson.getJSONArray(TREE_FIELDS);
            for (int i = 0; i < treeFields.size(); i++) {
                JSONObject field = treeFields.getJSONObject(i);
                String originKey = field.getStr(ORIGIN_KEY);
                String nickKey = field.getStr(NICK_KEY);
                if (newTree.containsKey(originKey)) {
                    Object originValue = newTree.get(originKey);
                    if (!newTree.containsKey(nickKey)) {
                        newTree.remove(originKey);
                        newTree.putOpt(nickKey, originValue);
                    }
                }
            }
            // 数据处理
            Set<String> keys = newTree.keySet();
            for (String key : keys) {
                tree.putExtra(key, newTree.get(key));
            }
        });
    }
}
