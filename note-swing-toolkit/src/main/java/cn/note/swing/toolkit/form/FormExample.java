package cn.note.swing.toolkit.form;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import cn.note.swing.core.util.WinUtil;
import cn.note.swing.core.view.AbstractMigView;
import cn.note.swing.core.view.base.ButtonFactory;
import cn.note.swing.core.view.base.MessageBuilder;
import cn.note.swing.core.view.filechooser.ButtonFileChooser;
import cn.note.swing.core.view.filechooser.FileChooserBuilder;
import cn.note.swing.core.view.filechooser.FileChooserType;
import cn.note.swing.core.view.form.*;
import cn.note.swing.core.view.icon.SvgIconFactory;
import cn.note.swing.core.view.item.ItemView;
import cn.note.swing.toolkit.GroupConstants;
import net.miginfocom.swing.MigLayout;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.List;
import java.util.Map;


@ItemView(value = "Form示例", order = 101, category = GroupConstants.FORM, description = "Form示例")
@Component
public class FormExample extends AbstractMigView {

    /**
     * 定义migLayout布局
     *
     * @return migLayout布局
     */
    @Override
    protected MigLayout defineMigLayout() {
        return new MigLayout("wrap 1", "[grow]", "");
    }


    @Override
    protected void render() {

        // input
        InputFormItem username = new InputFormItem("姓名", "请输入姓名");
        username.setFieldValue("jee");
        username.useLineStyle(view.getBackground());
        username.validEmpty().validMaxLength(5);

        InputFormItem studentId = new InputFormItem("学号", "请输入学号");
        studentId.validMaxLength(3);

        InputFormItem idCard = new InputFormItem("身份证号", "请输入身份证号");
        idCard.validMinLength(3);


        // group button
        List<String> sexList = CollUtil.newArrayList("男", "女");
        RadioGroupFormItem sexRadioGroup = new RadioGroupFormItem("性别", sexList);


//        List<String> hobbyList = CollUtil.newArrayList("书法", "画画", "运动", "爬山");
//        CheckBoxGroupFormItem hobbyCheckBoxGroup = new CheckBoxGroupFormItem("爱好", hobbyList);

        // 文件
        JTextField fileChooser = FileChooserBuilder.inputFileChooser("选择文件");
//        fileChooser.setEnabled(false);
        InputFormItem addFile = new InputFormItem("文件", fileChooser, "选择文件");

        // select
        SelectedFormItem<SelectedItem> interest = new SelectedFormItem<SelectedItem>("兴趣");
        interest.useLineStyle(view.getBackground());
        interest.addSelectItem(new SelectedItem("football", "足球"));
        interest.addSelectItem(new SelectedItem("basketball", "篮球"));
        interest.addSelectItem(new SelectedItem("badminton", "羽毛球"));

        SelectedFormItem<String> code = new SelectedFormItem<String>("代码");
        code.setSelectItems(CollUtil.newArrayList("java", "html", "css"));

        // buttonFileChosser
        ButtonFileChooser saveFile = new ButtonFileChooser("保存", file -> {
            MessageBuilder.ok(this, "保存路径==>" + file);
        });
        saveFile.setFileChooserType(FileChooserType.Directory);

        // checkbox
        CheckBoxFormItem allowAgree = new CheckBoxFormItem("是否同意");
//        allowAgree.setLeftOffset(50);
        CheckBoxFormItem allowUse = new CheckBoxFormItem("是否使用代理", true);
        // form
        Form form = new Form();
        form.addTitleSeparator("input");
        form.addFormItem(username, "username");
        form.addFormItem(studentId, "studentId");
        form.addFormItem(idCard, "idCard");
        form.addFormItem(sexRadioGroup, "sex");
//        form.addFormItem(hobbyCheckBoxGroup, "hobby");

        form.addTitleSeparator("select");
        form.addFormItem(interest, "interest");
        form.addFormItem(code, "code");
        form.addTitleSeparator("fileChooser");
        form.addFormItem(addFile, "addFile");

        form.addTitleSeparator("checkbox");
        form.addFormItem(allowAgree, "allowAgree");
        form.addFormItem(allowUse, "allowUse");

        view.add(form, "center,w 400!");
        JButton submit = ButtonFactory.primaryButton("submit");
        view.add(submit, "center");

        view.add(saveFile, "center");

        submit.addActionListener((e) -> {
            boolean validError = form.validFields();
            if (!validError) {
                Map<String, Object> values = form.getFormValues();
                WinUtil.alertMulti(JSONUtil.toJsonPrettyStr(values));
            }
        });
    }
}