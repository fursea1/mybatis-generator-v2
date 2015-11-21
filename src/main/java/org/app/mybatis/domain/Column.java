package org.app.mybatis.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import org.app.mybatis.util.ResManager;
import org.app.mybatis.util.StringUtil;

/**
 * 
 * @ClassName: Column
 * @Description: 相关列配置信息
 * @author hncdyj123@163.com
 * @date 2015年11月21日 下午12:24:00
 *
 */
public class Column {
	/** MYSQL对应java数据类型映射 **/
	private static Map<String, String> dbMysqlMapper = new LinkedHashMap<String, String>();

	static {
		dbMysqlMapper.put("BIGINT", "long");
		dbMysqlMapper.put("BIT", "byte[]");
		dbMysqlMapper.put("BLOB", "byte[]");
		dbMysqlMapper.put("CHAR", "String");
		dbMysqlMapper.put("DATE", "Date");
		dbMysqlMapper.put("DATETIME", "Timestamp");
		dbMysqlMapper.put("DECIMAL", "BigDecimal");
		dbMysqlMapper.put("DOUBLE", "double");
		dbMysqlMapper.put("DOUBLE PRECISION", "double");
		dbMysqlMapper.put("ENUM", "String");
		dbMysqlMapper.put("FLOAT", "float");
		dbMysqlMapper.put("INT", "int");
		dbMysqlMapper.put("INTEGER", "int");
		dbMysqlMapper.put("LONGBLOB", "byte[]");
		dbMysqlMapper.put("LONGTEXT", "String");
		dbMysqlMapper.put("MEDIUMBLOB", "byte[]");
		dbMysqlMapper.put("MEDIUMINT", "int");
		dbMysqlMapper.put("MEDIUMTEXT", "String");
		dbMysqlMapper.put("NUMERIC", "_");
		dbMysqlMapper.put("REAL", "_");
		dbMysqlMapper.put("SET", "String");
		dbMysqlMapper.put("SMALLINT", "int");
		dbMysqlMapper.put("TEXT", "String");
		dbMysqlMapper.put("TIME", "Time");
		dbMysqlMapper.put("TIMESTAMP", "Timestamp");
		dbMysqlMapper.put("TINYBLOB", "byte[]");
		dbMysqlMapper.put("TINYINT", "boolean");
		dbMysqlMapper.put("TINYTEXT", "String");
		dbMysqlMapper.put("VARCHAR", "String");
		dbMysqlMapper.put("YEAR", "Date");

		// load配置中的数据映射
		String dbmapperStr = (StringUtil.isEmptyString(ResManager.getString("system.dbmapper")) ? "" : ResManager.getString("system.dbmapper"));
		if (!StringUtil.isEmptyString(dbmapperStr)) {
			String[] dbmappers = dbmapperStr.split("\\,");
			for (int i = 0; i < dbmappers.length; i++) {
				String[] dbtype = dbmappers[i].split("\\|");
				if (dbMysqlMapper.containsKey(dbtype[0])) {
					dbMysqlMapper.remove(dbtype[0]);
				}
				dbMysqlMapper.put(dbtype[0], dbtype[1]);
			}
		}
	}
	/** 字段名 **/
	private String fieldName;
	/** 数据库字段名 **/
	private String databaseName;
	/** 数据库类型 **/
	private String dataType;
	/** java数据类型 **/
	private String jdkType;
	/** 字段描述 **/
	private String fieldDesc;

	public Column(String databaseName, String dataType, String fieldDesc) {
		super();
		this.databaseName = databaseName;
		this.dataType = dataType;
		this.fieldDesc = fieldDesc;
	}

	public String getFieldName() {
		// 字段分隔前缀
		String prefix = StringUtil.isEmptyString(ResManager.getString("system.column.sub")) ? "_" : ResManager.getString("system.column.sub");
		String[] fields = databaseName.split(prefix);
		fieldName = "";
		for (int i = 0; i < fields.length; i++) {
			if (i == 0) {
				fieldName += fields[0];
				continue;
			}
			fieldName += StringUtil.captureName(fields[i]);
		}
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getJdkType() {
		jdkType = dbMysqlMapper.get(dataType.toUpperCase());
		if (StringUtil.isEmptyString(jdkType)) {
			jdkType = "String";
		}
		return jdkType;
	}

	public void setJdkType(String jdkType) {
		this.jdkType = jdkType;
	}

	public String getFieldDesc() {
		return fieldDesc;
	}

	public void setFieldDesc(String fieldDesc) {
		this.fieldDesc = fieldDesc;
	}

}
