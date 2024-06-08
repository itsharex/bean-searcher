package cn.zhxu.bs.operator;

import cn.zhxu.bs.FieldOp;
import cn.zhxu.bs.SqlWrapper;
import cn.zhxu.bs.dialect.MySqlDialect;
import cn.zhxu.bs.dialect.PostgreSqlDialect;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class StartWithTestCase {

    final StartWith startWith = new StartWith();

    @Test
    public void test_01() {
        Assertions.assertEquals("StartWith", startWith.name());
        Assertions.assertTrue(startWith.isNamed("sw"));
        Assertions.assertTrue(startWith.isNamed("StartWith"));
        Assertions.assertFalse(startWith.lonely());
        System.out.println("\ttest_01 ok!");
    }

    @Test
    public void test_02() {
        startWith.setDialect(new MySqlDialect());
        StringBuilder sb = new StringBuilder();
        List<Object> params = startWith.operate(sb, new FieldOp.OpPara(
                name -> new SqlWrapper<>("name"), false, new Object[]{ "abc" }
        ));
        Assertions.assertEquals("name like ?", sb.toString());
        Assertions.assertArrayEquals(new Object[] { "abc%" }, params.toArray());
        System.out.println("\ttest_02 ok!");
    }

    @Test
    public void test_03() {
        startWith.setDialect(new MySqlDialect());
        StringBuilder sb = new StringBuilder();
        List<Object> params = startWith.operate(sb, new FieldOp.OpPara(
                name -> new SqlWrapper<>("name"), true, new Object[]{ "abc" }
        ));
        Assertions.assertEquals("upper(name) like ?", sb.toString());
        Assertions.assertArrayEquals(new Object[] { "ABC%" }, params.toArray());
        System.out.println("\ttest_03 ok!");
    }

    @Test
    public void test_04() {
        startWith.setDialect(new MySqlDialect());
        StringBuilder sb = new StringBuilder();
        SqlWrapper<Object> fieldSql = new SqlWrapper<>("(select name from user where id = ?)");
        fieldSql.addPara(12);
        List<Object> params = startWith.operate(sb, new FieldOp.OpPara(
                name -> fieldSql, false, new Object[]{ "abc" }
        ));
        Assertions.assertEquals("(select name from user where id = ?) like ?", sb.toString());
        Assertions.assertArrayEquals(new Object[] { 12, "abc%" }, params.toArray());
        System.out.println("\ttest_04 ok!");
    }

    @Test
    public void test_05() {
        startWith.setDialect(new MySqlDialect());
        StringBuilder sb = new StringBuilder();
        SqlWrapper<Object> fieldSql = new SqlWrapper<>("(select name from user where id = ?)");
        fieldSql.addPara(12);
        List<Object> params = startWith.operate(sb, new FieldOp.OpPara(
                name -> fieldSql, true, new Object[]{ "abc" }
        ));
        Assertions.assertEquals("upper((select name from user where id = ?)) like ?", sb.toString());
        Assertions.assertArrayEquals(new Object[] { 12, "ABC%" }, params.toArray());
        System.out.println("\ttest_05 ok!");
    }

    @Test
    public void test_06() {
        startWith.setDialect(new PostgreSqlDialect());
        StringBuilder sb = new StringBuilder();
        List<Object> params = startWith.operate(sb, new FieldOp.OpPara(
                name -> new SqlWrapper<>("name"), false, new Object[]{ "abc" }
        ));
        Assertions.assertEquals("name like ?", sb.toString());
        Assertions.assertArrayEquals(new Object[] { "abc%" }, params.toArray());
        System.out.println("\ttest_06 ok!");
    }

    @Test
    public void test_07() {
        startWith.setDialect(new PostgreSqlDialect());
        StringBuilder sb = new StringBuilder();
        List<Object> params = startWith.operate(sb, new FieldOp.OpPara(
                name -> new SqlWrapper<>("name"), true, new Object[]{ "abc" }
        ));
        Assertions.assertEquals("name ilike ?", sb.toString());
        Assertions.assertArrayEquals(new Object[] { "abc%" }, params.toArray());
        System.out.println("\ttest_07 ok!");
    }

    @Test
    public void test_08() {
        startWith.setDialect(new PostgreSqlDialect());
        StringBuilder sb = new StringBuilder();
        SqlWrapper<Object> fieldSql = new SqlWrapper<>("(select name from user where id = ?)");
        fieldSql.addPara(12);
        List<Object> params = startWith.operate(sb, new FieldOp.OpPara(
                name -> fieldSql, false, new Object[] { "abc" }
        ));
        Assertions.assertEquals("(select name from user where id = ?) like ?", sb.toString());
        Assertions.assertArrayEquals(new Object[]{ 12, "abc%" }, params.toArray());
        System.out.println("\ttest_08 ok!");
    }

    @Test
    public void test_09() {
        startWith.setDialect(new PostgreSqlDialect());
        StringBuilder sb = new StringBuilder();
        SqlWrapper<Object> fieldSql = new SqlWrapper<>("(select name from user where id = ?)");
        fieldSql.addPara(12);
        List<Object> params = startWith.operate(sb, new FieldOp.OpPara(
                name -> fieldSql, true, new Object[] { "abc" }
        ));
        Assertions.assertEquals("(select name from user where id = ?) ilike ?", sb.toString());
        Assertions.assertArrayEquals(new Object[]{ 12, "abc%" }, params.toArray());
        System.out.println("\ttest_09 ok!");
    }

}
