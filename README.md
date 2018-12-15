# DuangJike（今天是周五吗？）
即刻**今天是周五吗？**圈子周边产物
~~Make animation for Jike app~~

自定颜色和字体生成壁纸或头像，唯一不能定义的是时间

更多颜色的颜色值来自：http://nipponcolors.com/
字体是来自方正字库的四个免费字体

---

以下是11个月前的产物，代码还在，不过隐藏了入口

资源文件来自Jike app解包

每个动画用一个fragment装载，添加新动画步骤：
- 在layout文件夹中新建布局文件，里面只需一个自定义layout标签
    - 之所以用layout是因为有可能需要放置ImageView
    - 需要加id为`anim_view`，在fragment中才能取到
- 自定义layout，编写动画，使用animator或者自已用canvas画都可以
    - 自定义的layout请继承JikeView，里面定义了一些通用属性和方法
- 在MainActivity的初始化代码中按照例子添加即可
- 增加了保存gif的功能（右上角按钮），因为处理bitmap的时间较长，目前没有解决这个问题
需要手动调整动画播放时间来留出足够的时间处理bitmap；另外截图限定了fragment布局中心位置，宽度为屏幕宽度的正方形，
如果需要调整范围，请手动改动代码

目前已完成：
1. Logo掉下来的动画
2. Logo随手指波动翻转的动画（上下和左右），这个部分禁止拦截触摸事件，所以不能左右滑动切换fragment，需要点击顶上的tab切换
3. 仿flipBoard的Logo翻折动画
4. 绕圈点赞的动画


TODO:
- [ ] 改进gif保存功能