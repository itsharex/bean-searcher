package cn.zhxu.bs.bean;

import cn.zhxu.bs.DbMapping;
import cn.zhxu.bs.implement.DefaultDbMapping;
import cn.zhxu.bs.implement.DefaultSqlExecutor;

import javax.sql.DataSource;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于注解一个 SearchBean
 * v3.0.0 后该注解可以缺省，缺省时根据 {@link DbMapping } 自动映射数据库表
 * @author Troy.Zhou @ 2017-03-20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SearchBean {

    /**
     * 指定数据源
     * @see DefaultSqlExecutor#setDataSource(String name, DataSource)
     * @since v3.0.0
     * @return 数据源名称（name of DataSource）
     */
    String dataSource() default "";

    /**
     * 参与检索的数据库表名，例如:
     * users u, user_role ur, roles r
     * v3.0.0 后可空，为空时以类名映射表名
     * @return tables
     */
    String tables() default "";

    /**
     * Where 条件，例如：
     * u.id = ur.user_id and ur.role_id = r.id
     * @return where condition
     * @since v3.8.0
     */
    String where() default "";

    /**
     * 声明额外的条件字段，可根据字段参数动态生成 where 条件，用法：
     * \@SearchBean(fields = {
     *     \@DbField(name = "name"),
     *     \@DbField(name = "age")    // 这里的 name 是必填的
     * })
     * // 或者：
     * \@SearchBean(fields = @DbField(name = "name"))
     * 此处 {@link DbField#cluster()} 如果不显式指定，将自动推断为 {@link Cluster#FALSE }
     * @return 额外的条件字段
     * @since v4.1.0
     */
    DbField[] fields() default {};

    /**
     * 分组字段，例如
     * u.id,r.name
     * @return group information
     */
    String groupBy() default "";

    /**
     * 分组过滤条件，有 {@link #groupBy()} 时才会生效
     * @return having clause
     * @since v3.8.0
     */
    String having() default "";

    /**
     * 是否 distinct 结果
     * @return distinct
     * */
    boolean distinct() default false;

    /**
     * 当某字段同时未指定 {@link DbField#value()} 与 {@link DbField#mapTo()} 时，该属性指定它自动映射到哪张表 <p>
     * 只有在 {@link #tables()} 指定了多张表时起作用 <p>
     * 当多表映射中，如果该属性为空，则表示未被 @DbField 注解的字段不需要映射
     * @since v3.0.0
     * @return 自动映射的表名 或 别名
     */
    String autoMapTo() default "";

    /**
     * 继承类型
     * @since v3.2.0
     * @return InheritType
     */
    InheritType inheritType() default InheritType.DEFAULT;

    /**
     * @since v3.4.0
     * @return 需要忽略的属性名
     */
    String[] ignoreFields() default {};

    /**
     * @since v3.6.0
     * @return 默认排序字段信息
     */
    String orderBy() default "";

    /**
     * 排序约束类型：
     * 为 {@link SortType#ALLOW_PARAM } 时，表示：允许使用 检索参数 重新指定排序字段，
     * 为 {@link SortType#ONLY_ENTITY } 时，表示：只可以 使用本注解的 {@link #orderBy()} 属性指定排序字段，并会忽略 检索参数中的排序信息
     * 为 {@link SortType#DEFAULT } 时，表示：根据检索器的实例级配置 {@link DefaultDbMapping#getDefaultSortType()} 来决定 是否允许使用 检索参数 重新指定排序字段
     * @since v3.6.0
     * @return 排序约束类型
     */
    SortType sortType() default SortType.DEFAULT;

    /**
     * @return 单条 SQL 执行超时时间，单位：秒，0 表示永远不超时
     */
    int timeout() default 0;

}
