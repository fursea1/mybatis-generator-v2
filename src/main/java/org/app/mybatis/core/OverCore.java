package org.app.mybatis.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.app.mybatis.constant.Constant;
import org.app.mybatis.db.ColumnEntity;
import org.app.mybatis.db.MysqlDbMapper;
import org.app.mybatis.db.TableEntity;
import org.app.mybatis.domain.Column;
import org.app.mybatis.domain.PropertyClass;
import org.app.mybatis.util.FileUtil;
import org.app.mybatis.util.MyBatisUtil;
import org.app.mybatis.util.ResManager;
import org.app.mybatis.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @Title: OverCore.java
 * @Description: 核心类
 * @Package org.app.mybatis.core
 * @author hncdyj123@163.com
 * @date 2012-7-20
 * @version V1.0
 * 
 */
public class OverCore {

	private static final Logger LOGGER = LoggerFactory.getLogger(WriteClass.class);

	private static SqlSessionFactory sqlSessionFactory = null;

	// 存放所有数据库表映射实体类信息
	private static List<PropertyClass> propertyClassList = new ArrayList<PropertyClass>();

	// 存放模板文件目录下所有模板定义的文件名 map key - > 文件名 value - > 文件路径 不包含文件名
	@SuppressWarnings("rawtypes")
	private static Map templateMap = new HashMap();

	static {
		sqlSessionFactory = MyBatisUtil.getSqlSessionFactory();
	}
	// 外部文件模板位置
	private String outFtlFilePath = ResManager.getString("system.freemarker.filepath");

	/**
	 * 
	 * @Description: 获取映射数据库表信息
	 * @param dbName
	 *            数据库名称
	 * @throws Exception
	 * @return void
	 * @throws
	 */
	public void getAllFileInfo(String dbName) throws Exception {
		// 获取模板文件位置
		templateMap = FileUtil.listFile(StringUtil.isEmptyString(outFtlFilePath) ? (this.getClass().getResource("/").getPath() + File.separator + "ftl") : outFtlFilePath);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		MysqlDbMapper dbMapper = sqlSession.getMapper(MysqlDbMapper.class);
		List<TableEntity> tableEntityList = dbMapper.getAllTable(dbName); // 获取所有的表名称
		if (tableEntityList != null && tableEntityList.size() > 0) {
			PropertyClass propertyClass = new PropertyClass();
			for (TableEntity tableEntity : tableEntityList) {
				propertyClass.setTableName(tableEntity.getTableName()); // 设置表名称
				ColumnEntity tempcolumnEntity = new ColumnEntity(tableEntity.getTableName(), dbName);
				List<ColumnEntity> columnEntityList = dbMapper.getTableColumns(tempcolumnEntity); // 存放表字段
				if (columnEntityList != null && columnEntityList.size() > 0) {
					List<Column> columnList = new ArrayList<Column>();
					for (ColumnEntity columnEntity : columnEntityList) {
						Column column = new Column(columnEntity.getColumnName(), columnEntity.getDataType(), columnEntity.getColumnComment());
						columnList.add(column);
					}
					propertyClass.setPackageName(ResManager.getString("system.packagename")); // 设置包名称
					propertyClass.setColumns(columnList); // 存放当前表的列信息
				}
				propertyClassList.add(propertyClass);
			}
		}
	}

	/**
	 * 
	 * @Description: 创建生成文件的位置
	 * @param path
	 *            文件位置
	 * @throws IOException
	 * @return void
	 * @throws
	 */
	public void createAllFolder(String path) throws IOException {
		if (propertyClassList.size() > 0) {
			for (PropertyClass pro : propertyClassList) {
				// 创建文件
				FileUtil.createFolder(ResManager.getString("system.file.output"), pro.getPackageName());
			}
		}
	}

	/**
	 * 
	 * @Description: 生成文件
	 * @param ftlNames
	 *            模板文件名不定参数
	 * @return void
	 * @throws
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void startThread(String... ftlNames) {
		String filePath = ResManager.getString("system.file.output"); // 文件输出路径
		// 模板定义文件路径
		String ftlPath = StringUtil.isEmptyString(outFtlFilePath) ? (this.getClass().getResource("/").getPath() + File.separator + "ftl") : outFtlFilePath;
		// 模板中所需要map数据
		Map paramMap = new HashMap();
		if (propertyClassList.size() > 0) {
			paramMap.put("proList", propertyClassList);
			int i = 0;
			for (String ftlName : ftlNames) {
				// 文件前缀 例如：UserService.java Service为前缀
				String filePrefix = "";
				if (ftlName.equals(Constant.SERVICEENTITY_TEMPLATE_FILENAME)) {
					filePrefix = Constant.SERVICE_FILE_PREFIX;
				} else if (ftlName.equals(Constant.SERVICEIMPLENTITY_TEMPLATE_FILENAME)) {
					filePrefix = Constant.SERVICEIMPL_FILE_PREFIX;
				} else if (ftlName.equals(Constant.DAOENTITY_TEMPLATE_FILENAME)) {
					filePrefix = Constant.DAO_FILE_PREFIX;
				} else if (ftlName.equals(Constant.DAOIMPLENTITY_TEMPLATE_FILENAME)) {
					filePrefix = Constant.DAOIMPL_FILE_PREFIX;
				} else {
					filePrefix = StringUtil.isEmptyString(ResManager.getString("system.entity.suffix")) ? Constant.ENTITY_FILE_PREFIX : ResManager.getString("system.entity.suffix");
				}
				if (i != ftlNames.length - 1) {
					if (templateMap.get(ftlName) != null) {
						LOGGER.info("文件名是: " + ftlName + " | 文件后缀是: " + filePrefix);
						// 启动生成java文件线程 paramMap 解析到的所有实体类信息 map 存放所有文件信息的map
						new Thread(new WriteClass(filePath, ftlPath, paramMap, filePrefix, ftlName)).start();
					}
				}

				if (i == ftlNames.length - 1) {
					if (templateMap.get(ftlName) != null) {
						// XML 文件前缀(此处前缀包含了文件后缀)
						filePrefix = Constant.XML_FILE_SUFFIX;
						LOGGER.info("文件名是: " + ftlName + " | 文件后缀是: " + filePrefix);
						// 启动生成XML文件线程
						new Thread(new WriteXml(filePath, ftlPath, paramMap, filePrefix, ftlName)).start();
					}
				}
				i++;
			}
		}
	}
}
