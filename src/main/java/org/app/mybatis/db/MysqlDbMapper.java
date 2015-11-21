package org.app.mybatis.db;

import java.sql.SQLException;
import java.util.List;

/**
 * 
 * @ClassName: MysqlDbMapper
 * @Description: 查询表信息
 * @author hncdyj123@163.com
 * @date 2015年11月21日 下午12:23:01
 *
 */
public interface MysqlDbMapper {
	/** 获取所有的表信息 **/
	public List<TableEntity> getAllTable(String dbName) throws SQLException;

	/** 获取表信息 **/
	public List<ColumnEntity> getTableColumns(ColumnEntity columnEntity) throws SQLException;
}
