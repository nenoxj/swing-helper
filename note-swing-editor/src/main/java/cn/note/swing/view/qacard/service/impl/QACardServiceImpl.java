package cn.note.swing.view.qacard.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import cn.note.swing.view.qacard.bean.QACard;
import cn.note.swing.view.qacard.bean.QACategory;
import cn.note.swing.view.qacard.service.QACardService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class QACardServiceImpl implements QACardService {

    private QACategory qaCategory;

    public QACardServiceImpl(QACategory qaCategory) {
        this.qaCategory = qaCategory;
    }


    @Override
    public List<QACard> queryQaCardList() {
        File file = qaCategory.getCategoryFile();
        List<QACard> qaCardList = new ArrayList<>();
        try {
            String content = FileUtils.readFileToString(file);
            if (StrUtil.isNotBlank(content)) {
                JSONArray array = JSONUtil.parseArray(content);
                qaCardList = JSONUtil.toList(array, QACard.class);
            }
//            else {
//                QACard qaCard = new QACard();
//                qaCard.setQuestion("question " + qaCategory.getCategoryName());
//                qaCard.setAnswer("answer " + qaCategory.getCategoryName());
//                qaCardList.add(qaCard);
//            }
        } catch (IOException e) {
            log.error("读取文件:{}信息失败", e, file.getAbsolutePath());
        }

        return qaCardList;
    }

    @Override
    public void save(List<QACard> qaCardList) throws Exception {
        File file = qaCategory.getCategoryFile();
        String qaCardJsonArray = JSONUtil.toJsonStr(qaCardList);
        System.out.println("save==>"+qaCardJsonArray);
        FileUtils.write(file, qaCardJsonArray);
    }
}
