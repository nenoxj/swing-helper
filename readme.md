## JNoteHelper

  使用swing构建的java程序, 主要基于miglayout,swingx,flatlatf, 本来打算作为个人笔记的助手,
  但是事与愿违, 发现理想和现实很骨感. 
  
 > 项目基于windows测试及开发 ,所以运行效果图片都基于windows 截图   
 
 ### 模块说明
  
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
 
### 系统兼容可能存在的问题
  
  + SystemFileManager.updateSystemDir2Default() 默认使用了D:/note-helper 作为系统
  应用的主目录 ,可能造成与其他系统不兼容
  
  + GitRemote 管理,默认使用了注册表存储git信息, 需要修改git保存的相关逻辑
  
  + 其他应该都是通用的文件存储代码

### 页面入口

   note-swing-framework
   > cn.note.swing.SwingViewApplication 
       
   可以作为整体效果目录
   
   
           