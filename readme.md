## JNoteHelper

  使用swing构建的java程序, 主要基于miglayout,swingx,flatlatf, 本来打算作为个人笔记的助手,
  但是事与愿违, 发现理想和现实很骨感. 
  
 > 项目基于windows测试及开发 ,所以运行效果图片都基于windows 截图   
###  1. 项目地址
 [JNoteHelper  github地址](https://github.com/nenoxj/JNoteHelper.git)
 
 
### 2. 模块说明
  
 + note-swing-core
   
   封装了一些常用的swing组件, 可以更简单的创建swing组件
 
 + note-swing-toolkit
   
   可以用来构建简单的swing小工具 ,里边有部分示例
 
 + note-swing-editor
   
   打算专注于写笔记管理的,封装了jnote文件(记录笔记) 和qa文件(记录问题) ,并且集成了jgit
  作为远程同步使用. 
     
 + note-service-toolkit
 
  使用java封装业务逻辑的 java 工具类或工具包, 与swing无关 

 + note-swing-framework 
  
  整合toolkit 和editor页面构建入口应用, 使用bsaf可以缓存上次打开窗口大小.
 
###  3. 系统兼容可能存在的问题
  
  + SystemFileManager.updateSystemDir2Default() 默认使用了D:/note-helper 作为系统
  应用的主目录 ,可能造成与其他系统不兼容
  
  + GitRemote 管理,默认使用了注册表存储git信息, 需要修改git保存的相关逻辑
  
  + 其他应该都是通用的文件存储代码

### 4. 页面介绍
   整体主要受VSCODE 框架影响 ,使用左侧选项卡, 中间目录结构,右侧编辑面板的风格.
 
####  4.1 SwingViewApplication  页面总入口
   note-swing-framework
   > cn.note.swing.SwingViewApplication 
       
   可以作为整体效果目录, 继承了bsaf 的SingleFrameApplication ,可以记录上次运行后的效果. 
   当然内嵌 NoteEditorView /  QACardView /ToolkitView  也同样可以作为单独应用使用

```java
 ToggleLRCardPanel toggleLRCardPanel = new ToggleLRCardPanel();
        toggleLRCardPanel.addTab(SvgIconFactory.Note.editor, "笔记管理", new NoteEditorView(true), true);
        toggleLRCardPanel.addTab(SvgIconFactory.Note.question, "问答管理", new QACardView(true));
        toggleLRCardPanel.addTab(SvgIconFactory.Note.plugins, "插件管理", new ToolkitView(true));
```


![在这里插入图片描述](https://img-blog.csdnimg.cn/bd52670434f24f64ab59563eef48630f.png)
#### 4.2  NoteEditorView 
  主要实现了JTree /JTabbedPane 及JTextPane , 实现笔记文件的记录, 主要关心 普通文本,代码块及图片  . 主要实现了查找和全文搜索功能
   因为代码属于早期代码, 可能存在很多不合理的设计
   主要功能特色:
    + 仿IDE效果, 左侧文件树,右侧选项卡
    + 实现了文本编辑器的按键绑定
    + 扩展了jtextpane的一些特色
    
   主要缺点:
      jtextpane 特性了解不够深层, 存在很多bug. 在当作富文本编辑器使用时,存在很多bug.  好多样式效果使用绑定效果,  因为没有时间重构了,所以搁浅了

   F1 可以查看主要功能操作
   ![在这里插入图片描述](https://img-blog.csdnimg.cn/a418d6f3e0944620be03a19e2b6130f0.png)

   页面查找
![在这里插入图片描述](https://img-blog.csdnimg.cn/664b4dea47714fd9a4ea4bb21fe826f0.png)
   全文搜索
   ![在这里插入图片描述](https://img-blog.csdnimg.cn/a7d1e64a9960405282f51c8b6d9d4cf9.png)

#### 4.3 QACardView
 初始的设计,是为了记录代码过程中,简短的问题Q和答案A , 左侧实现动态questtion添加, 右侧实现动态answer添加 .
  
  主要功能特色
  + 仿制之前的h5页面效果 
  +  实现了动态大小的调整 
  +  无弹窗的页内编辑
![在这里插入图片描述](https://img-blog.csdnimg.cn/ee81498c1ebf4674939f4c223445bfb9.png)
![在这里插入图片描述](https://img-blog.csdnimg.cn/9824fb27d8784748b29476d38619fed2.png)
#### 4.4 ToolkitView 
   动态扩展工具类视图, 左侧为 工具目录 自定义扩展了(JXTaskPane), 右侧为小工具视图 

  小工具必须为swing 组件 (必须存在无参构造), 只要你的小工具可以使用main方法运行, 那么在类上使用标记@ItemView注解. 即可自定加入左侧视图控制.

```java
@ItemView(value = "Form示例", order = 101, category = GroupConstants.FORM, description = "Form示例")
```
 主要功能特色
   + 使用@ItemView 注解实现了解耦
   + 使用反射动态扫描
   
![在这里插入图片描述](https://img-blog.csdnimg.cn/137f61cc1ca2434dbbe027f27264f044.png)

### 5. note-swing-core 部分功能介绍

   #### 5.1 item包  
   借助MenuItemView  ,可以轻松实现 toolkitview 布局, toolkitview的代码也相当简洁
```java

public class ToolkitView extends AbstractMigCard {

    private MenuItemView menuItemView;

    public ToolkitView() {

    }

    public ToolkitView(boolean card) {
        super(card);
    }

    @Override
    public LRCard getCardView() {
        return new LRCard(this.getClass(), menuItemView.getItemSelector().getSelectorComponent(), menuItemView.getItemContainer());
    }

    @Override
    protected void init() {
        List<ItemNode> menuItems = ItemScanner.scanAllItemView("cn.note.swing.toolkit");
        menuItemView = new MenuItemView(menuItems);
    }

    /**
     * render视图
     */
    @Override
    protected void render() {
        view.add(menuItemView.create(), "grow");
        menuItemView.setDefaultSelectedItem();
    }

    public static void main(String[] args) {
        ThemeFlatLaf.install();
        FrameUtil.launchTime(ToolkitView.class);
    }

}
```
#### 5.2 form包
  受react 的rc-form的影响 ,为了在swing 中轻松构建简单的form 元素,快速抒写小工具 . 以下为FormExample的示例, 点击submit, 可以轻松将form 对象值 获取至map对象
    

```java
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
```
![在这里插入图片描述](https://img-blog.csdnimg.cn/80b4611ea5f5436cb015a4217ecb02a2.png)

#### 5.3 theme包
 利用接口, 实现了在swing中构建 统一UI 和组件的思路, 当然可能由于自己知识的浅薄, 可能存在不合理 

#### 5.4 其他包
 比如弹框, 模态框, 各种loading ,颜色 等等不再一一介绍.

### 6.  最后
希望该工具对你在构建自己的swing小工具 或java桌面程序有帮助和参考,   因为从零构建一个系统工具 并不是一件轻松的工作.



 
            