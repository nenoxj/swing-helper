package cn.note.swing.view.qacard.service;

import cn.note.swing.view.qacard.bean.QACategory;

import java.util.List;

public interface QACategoryService {


    /**
     * 分类列表
     */
    List<QACategory> categoryList();


    /**
     * 添加分类
     */
    boolean add(QACategory qaCategory) throws Exception;


    /**
     * 修改分类
     */
    boolean update(QACategory qaCategory) throws Exception;


    /**
     * 删除分类
     */
    boolean delete(QACategory qaCategory) throws Exception;


    /**
     * 保存分类
     */
    boolean save(QACategory qaCategory) throws Exception;


    /**
     * 是否包含分类
     */
    boolean contains(String categoryName);
}
