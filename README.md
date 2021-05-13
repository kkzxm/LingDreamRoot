# LingDreamRoot
1.2版本开始:
将MybatisPlus依赖升级为3.4.2,
因此,对应修改了MyService相关,
在MyMapper文件中,
为应对复杂的一对多分页实现,
添加了一个
selectMyPage(Integer start,Integer size)方法.
(未来对此方法做可按条件查询)

最后,将MyPage类中的begin与end属性删除,
换为一个得到当前序号的方法:
如:
(每页显示10条的情况)
第1页的第3条数据:序号为3
第2页的第4条数据:序号为14
