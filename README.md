#首先想说说写这个项目的目的<br/>
1.mybatis自带的生成配置太繁杂<br/>
2.想弄一个定制化的mybaits文件生成<br/>
<br/>

##项目采用assembly打包，打出来的包可以直接解压到linux或者window上面运行<br/>

##项目运行：<br/>
修改根目录下mybatis-config.xml<br/>
```javascript
<configuration><br/>
    <settings><br/>  
        <!-- changes from the defaults for testing --><br/>
        <setting name="cacheEnabled" value="false" /><br/>
        <setting name="useGeneratedKeys" value="true" /><br/> 
        <setting name="defaultExecutorType" value="REUSE" /><br/> 
    </settings><br/>
    <typeAliases><br/>
       <typeAlias alias="ColumnEntity" type="org.app.mybatis.db.ColumnEntity"/><br/>
       <typeAlias alias="TableEntity" type="org.app.mybatis.db.TableEntity"/><br/>
    </typeAliases><br/>
    <environments default="development"><br/>
       <environment id="development"><br/>
           <transactionManager type="jdbc"/><br/> 
           <dataSource type="POOLED"><br/>
              <property name="driver" value="com.mysql.jdbc.Driver"/><br/>
              <property name="url" value="jdbc:mysql://127.0.0.1/ams"/><br/>
              <property name="username" value="root"/><br/>
              <property name="password" value="root"/><br/>
           </dataSource><br/>
       </environment><br/>
    </environments><br/>
    <mappers><br/>
        <mapper resource="mappers/mysqlmappers.xml" /><br/>
    </mappers><br/>
</configuration><br/>
<br/>
```

##替换三项本地配置<br/>
```
<property name="url" value="jdbc:mysql://127.0.0.1/ams"/><br/>
<property name="username" value="root"/><br/>
<property name="password" value="root"/><br/>
```

##数据库建立标准sql文本<br/>
drop table if exists ams_operator_channel;<br/>
/*==============================================================*/<br/>
/* Table: ams_operator_channel                                  */<br/>
/*==============================================================*/<br/>
create table ams_operator_channel<br/>
(<br/>
   id                   char(19) not null comment '主键',<br/>
   channel_id           varchar(100) comment '渠道ID',<br/>
   chinese_desc         varchar(100) comment '中文描述',<br/>
   supplier_id          char(19) comment '渠道商ID',<br/>
   throw_flag           tinyint(1) comment '弃包标识(0  不弃包  1 弃包)',<br/>
   paint_flag           tinyint(1) comment '看板标识(0 不可看 1 可看)',<br/>
   create_name          varchar(20) comment '创建人',<br/>
   create_datetime      timestamp comment '创建时间',<br/>
   update_name          varchar(20) comment '修改人',<br/>
   update_datetime      timestamp comment '修改时间',<br/>
   primary key (id)<br/>
);<br/>
alter table ams_operator_channel comment '运营渠道表';<br/>

表名ams_operator_channel对应配置system.table.sub=_<br/>
字段名update_datetime对应配置system.column.sub=_<br/>
<br/>
<br/>
配置说明：<br/>
配置文件在src/main/resources<br/>
-- 本地导入项目建议配置全开，以免遇到错误<br/>
-- database name(数据库名称)<br/>
system.db.name=ams<br/>
-- package name (包名称)<br/>
system.packagename=com.sym.ams<br/>
-- freemarker to generate the file path(模板输出文件位置)<br/>
system.file.output=e:\\<br/>
-- discard table prefix(表前缀替换 非必须)<br/>
system.throw.tableprefix=ams<br/>
-- table sub(表分隔符号 非必要)<br/>
system.table.sub=_<br/>
-- column sub(字段分隔符号 非必要)<br/>
system.column.sub=_<br/>
-- 实体后缀(例如：Entity,Dto等 非必要)<br/>
system.entity.suffix=<br/>
-- database mapper(覆盖原有的MySQL数据库和java的字段映射 非必要)<br/>
system.dbmapper=TIMESTAMP|String,DATETIME|String<br/>
-- template file location (模板文件位置  非必须 默认获取classpath下ftl文件夹中的模板)<br/>
system.freemarker.filepath=e:\\ftl<br/>

如果启用system.throw.tableprefix=ams,则生成的类会把ams前缀去掉<br/>
ams_operator_channel 表对应的实体名称为 OperatorChannel<br/>
<br/>
其余功能不累赘介绍，大家可以下载代码看。<br/>
