
## MigLayout 使用笔记

### insets: 组件内边距,类似于h5 padding,默认值10
  new MigLayout("insets 0,gapy 0")

#### gap: 组件外间距 ,类似于h5 margin,默认值为5
 new MigLayout("insets 0,gapy 0")
 new MigLayout("gapx 0")
 new MigLayout("gap 0,10")
 
###  wrap: 控制换行
 + 可以在总样式中,控制每行最多几个元素
   wrap  2
 + 也可以在内部样式中,控制什么时候换行
   
   view.add(item, "wrap"); 

### nogrid: 不规则的布局
   new MigLayout("nogrid")
   