
### richtext 可能应该放入swing-core包,
但是感觉编辑器更适合note-swing-editor,
暂时放入editor下边吧

### 打算暂时以这样的目录更合适的梳理结构

+ core: 存放与该应用不是息息相关的类,可以被其他工具包共享
+ event: 存放事件类
+ store: 存放存储相关
+ style: 存放样式相关
+ view: 存放二级视图
+ 也许应该加个constant 看情况吧