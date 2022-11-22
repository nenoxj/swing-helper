package cn.note.swing.view.qacard.service;

import cn.note.swing.view.qacard.bean.QACard;

import java.util.List;

/**
 * 全量更新
 */
public interface QACardService {


    /**
     * 卡片列表
     */
    List<QACard> queryQaCardList();

    /**
     * 保存卡片
     */
    void save(List<QACard> qaCardList) throws Exception;


}
