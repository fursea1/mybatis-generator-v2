#首先想说说写这个项目的目的<br/>
1.mybatis自带的生成配置太繁杂<br/>
2.想弄一个定制化的mybaits文件生成<br/>
项目采用assembly打包，打出来的包可以直接解压到linux或者window上面运行<br/>
项目运行：<br/>
修改根目录下mybatis-config.xml<br/>
<configuration><br/>
    <settings><br/>  
        <!-- changes from the defaults for testing -->   
        <setting name="cacheEnabled" value="false" />   
        <setting name="useGeneratedKeys" value="true" />   
        <setting name="defaultExecutorType" value="REUSE" />   
    </settings>   
    <typeAliases>   
       <typeAlias alias="ColumnEntity" type="org.app.mybatis.db.ColumnEntity"/>   
       <typeAlias alias="TableEntity" type="org.app.mybatis.db.TableEntity"/>   
    </typeAliases>   
    <environments default="development">   
       <environment id="development">   
           <transactionManager type="jdbc"/>   
           <dataSource type="POOLED">   
              <property name="driver" value="com.mysql.jdbc.Driver"/>   
              <property name="url" value="jdbc:mysql://127.0.0.1/ams"/>   
              <property name="username" value="root"/>   
              <property name="password" value="root"/>   
           </dataSource>   
       </environment>   
    </environments>   
    <mappers>   
        <mapper resource="mappers/mysqlmappers.xml" />   
    </mappers>   
</configuration>

替换三项本地配置
<property name="url" value="jdbc:mysql://127.0.0.1/ams"/>
<property name="username" value="root"/>
<property name="password" value="root"/>

数据库建立标准sql文本
drop table if exists ams_operator_channel;
/*==============================================================*/
/* Table: ams_operator_channel                                  */
/*==============================================================*/
create table ams_operator_channel
(
   id                   char(19) not null comment '主键',
   channel_id           varchar(100) comment '渠道ID',
   chinese_desc         varchar(100) comment '中文描述',
   supplier_id          char(19) comment '渠道商ID',
   throw_flag           tinyint(1) comment '弃包标识(0  不弃包  1 弃包)',
   paint_flag           tinyint(1) comment '看板标识(0 不可看 1 可看)',
   create_name          varchar(20) comment '创建人',
   create_datetime      timestamp comment '创建时间',
   update_name          varchar(20) comment '修改人',
   update_datetime      timestamp comment '修改时间',
   primary key (id)
);
alter table ams_operator_channel comment '运营渠道表';

表名ams_operator_channel对应配置system.table.sub=_
字段名update_datetime对应配置system.column.sub=_




配置说明：
配置文件在src/main/resources
-- 本地导入项目建议配置全开，以免遇到错误
-- database name(数据库名称)
system.db.name=ams
-- package name (包名称)
system.packagename=com.sym.ams
-- freemarker to generate the file path(模板输出文件位置)
system.file.output=e:\\
-- discard table prefix(表前缀替换 非必须)
system.throw.tableprefix=ams
-- table sub(表分隔符号 非必要)
system.table.sub=_
-- column sub(字段分隔符号 非必要)
system.column.sub=_
-- 实体后缀(例如：Entity,Dto等 非必要)
system.entity.suffix=
-- database mapper(覆盖原有的MySQL数据库和java的字段映射 非必要) 
system.dbmapper=TIMESTAMP|String,DATETIME|String
-- template file location (模板文件位置  非必须 默认获取classpath下ftl文件夹中的模板)
system.freemarker.filepath=e:\\ftl

如果启用system.throw.tableprefix=ams,则生成的类会把ams前缀去掉
ams_operator_channel 表对应的实体名称为 OperatorChannel

其余功能不累赘介绍，大家可以下载代码看。
