package cn.note.swing.view.qacard.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.note.service.toolkit.filestore.RelativeFileStore;
import cn.note.service.toolkit.filestore.SystemFileManager;
import cn.note.swing.store.NoteConstants;
import cn.note.swing.view.qacard.bean.QACategory;
import cn.note.swing.view.qacard.service.QACategoryService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class QACategoryServiceImpl implements QACategoryService {

    /**
     * qa存储目录
     */
    private RelativeFileStore qaCategoryFileStore;

    /**
     * QA分类目录
     */
    private final String QA_CATEGORY_DIR = ".Q&A";

    /**
     * qa文件类型
     */
    private final String QA_FILE_TYPE = ".qa";

    public QACategoryServiceImpl() {
        qaCategoryFileStore = new RelativeFileStore(SystemFileManager.SYSTEM_DIR, NoteConstants.NOTE_NAME, QA_CATEGORY_DIR);

    }


    @Override
    public List<QACategory> categoryList() {
        List<QACategory> qaCategoryList = new ArrayList<>();
        File[] qaCategoryFileArray = qaCategoryFileStore.homeDir().listFiles();
        if (qaCategoryFileArray != null) {
            for (File qaCategoryFile : qaCategoryFileArray) {
                QACategory qaCategory = new QACategory();
                String fileName = qaCategoryFile.getName();
                String categoryName = StrUtil.subBefore(fileName, QA_FILE_TYPE, true);
                qaCategory.setCategoryName(categoryName);
                qaCategory.setCategoryFile(qaCategoryFile);
                qaCategoryList.add(qaCategory);
            }
        }
//        IntStream.range(1, 11).forEach(i -> {
//            QACategory qaCategory = new QACategory();
//            qaCategory.setCategoryName("question" + i);
//            qaCategory.setQaCardList(null);
//            qaCategoryList.add(qaCategory);
//        });
        return qaCategoryList;
    }

    @Override
    public boolean add(QACategory qaCategory) throws Exception {
        String fileName = qaCategory.getCategoryName().concat(QA_FILE_TYPE);
        File categoryFile = qaCategoryFileStore.createFile(fileName);
        qaCategory.setCategoryFile(categoryFile);
        return true;
    }

    @Override
    public boolean update(QACategory qaCategory) {
        File categoryFile = qaCategory.getCategoryFile();
        File newFile = qaCategoryFileStore.rename(categoryFile, qaCategory.getCategoryName());
        qaCategory.setCategoryFile(newFile);
        return true;
    }

    @Override
    public boolean delete(QACategory qaCategory) {
        File categoryFile = qaCategory.getCategoryFile();
        qaCategoryFileStore.deleteQuietly(categoryFile);
        return true;
    }

    @Override
    public boolean save(QACategory qaCategory) throws Exception {
        if (qaCategory.getCategoryFile() == null) {
            return add(qaCategory);
        } else {
            return update(qaCategory);
        }
    }

    @Override
    public boolean contains(String categoryName) {
        categoryName = categoryName.concat(QA_FILE_TYPE);
        String[] fileNameArray = qaCategoryFileStore.homeDir().list();
        return ArrayUtil.contains(fileNameArray, categoryName);
    }
}
