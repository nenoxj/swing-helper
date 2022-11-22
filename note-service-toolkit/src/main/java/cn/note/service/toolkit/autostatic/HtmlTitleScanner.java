package cn.note.service.toolkit.autostatic;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.note.service.toolkit.autostatic.tree.DataNode;
import cn.note.service.toolkit.autostatic.tree.HtmlFileVisitor;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * 扫描note-single-html静态文件夹,生成data数据
 */
public class HtmlTitleScanner {


    /**
     * @param dir note-single-html 文件位置
     * @return 生成后的json数据
     * @throws IOException
     */
    public static SingleData buildData(String dir) throws IOException {
        List<DataNode> nodes = HtmlFileVisitor.scanHtmls(Paths.get(dir, "single"));
        TreeNodeConfig config = new TreeNodeConfig();
        config.setIdKey("id");
        config.setParentIdKey("pid");
        // 最多遍历4层
        config.setDeep(4);
        //子节点名称
        config.setChildrenKey("children");
        // 3.转树，Tree<>里面泛型为id的类型
        List<Tree<Integer>> build = TreeUtil.build(nodes, 0, config, (treeNode, tree) -> {
            // 也可以使用 tree.setId(object.getId());等一些默认值
            tree.putExtra("id", treeNode.getId());
            tree.putExtra("pid", treeNode.getPid());
            tree.putExtra("title", treeNode.getTitle());
            tree.putExtra("open", treeNode.isOpen());
            tree.putExtra("url", treeNode.getUrl());
        });
        JSONArray data = new JSONArray();
        if (build.size() > 0) {
            data = JSONUtil.parseArray(build.get(0).getChildren());
        }
        return new SingleData(dir, data);
    }


}
