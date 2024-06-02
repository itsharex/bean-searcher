package cn.zhxu.bs.dialect;

import cn.zhxu.bs.SqlWrapper;
import cn.zhxu.bs.param.Paging;

/**
 * 数据库方言
 * @author Troy.Zhou
 * @since v1.0
 */
public interface Dialect {

	/**
	 * 把字段 dbField 转换为大写
	 * @param builder sql builder
	 * @param dbField 数据库字段
	 */
	default void toUpperCase(StringBuilder builder, String dbField) {
		builder.append("upper").append("(").append(dbField).append(")");
	}

	/**
	 * 分页
	 * @param fieldSelectSql 查询语句
	 * @param fromWhereSql 条件语句
	 * @param paging 分页参数（可空，为空时表示不分页）
	 * @return 分页 Sql
	 */
	SqlWrapper<Object> forPaginate(String fieldSelectSql, String fromWhereSql, Paging paging);

	/**
	 * @return 是否支持 ilike 语法
	 * @since v3.7.0
	 */
	default boolean hasILike() {
		return false;
	}

	/**
	 * @return 是否允许在 having 语句中使用别名
	 * @since v4.3.0
	 */
	default boolean allowHavingAlias() {
		// MySql: true
		// PostgreSql: false
		// SqlServer: false
		// Oracle: false
		return false;
	}

}
