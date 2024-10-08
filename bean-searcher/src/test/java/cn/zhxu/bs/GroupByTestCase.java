package cn.zhxu.bs;

import cn.zhxu.bs.bean.Cluster;
import cn.zhxu.bs.bean.DbField;
import cn.zhxu.bs.bean.SearchBean;
import cn.zhxu.bs.operator.*;
import cn.zhxu.bs.util.MapUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

public class GroupByTestCase {

    @SearchBean(groupBy = "course_id")
    public static class StudentCourse {
        private long courseId;
        @DbField("sum(score)")
        private int sumScore;
        @DbField("avg(score)")
        private int avgScore;
        public long getCourseId() {
            return courseId;
        }
        public int getSumScore() {
            return sumScore;
        }
        public int getAvgScore() {
            return avgScore;
        }
    }

    @Test
    public void test_count() {
        SqlExecutor sqlExecutor = new SqlExecutor() {
            @Override
            public <T> SqlResult<T> execute(SearchSql<T> searchSql) {
                System.out.println(searchSql.getClusterSqlString());
                Assertions.assertNull(searchSql.getListSqlString());
                Assertions.assertTrue(searchSql.getListSqlParams().isEmpty());
                Assertions.assertEquals("select count(*) s_count from (select course_id c_0, sum(score) c_1, avg(score) c_2 from student_course group by course_id) t_", searchSql.getClusterSqlString());
                Assertions.assertTrue(searchSql.getClusterSqlParams().isEmpty());
                return new SqlResult<>(searchSql, null, columnLabel -> 100);
            }
        };

        MapSearcher mapSearcher = SearcherBuilder.mapSearcher().sqlExecutor(sqlExecutor).build();
        Assertions.assertEquals(100, mapSearcher.searchCount(StudentCourse.class, new HashMap<>()));
        Assertions.assertEquals(100, mapSearcher.searchCount(StudentCourse.class, null));

        BeanSearcher beanSearcher = SearcherBuilder.beanSearcher().sqlExecutor(sqlExecutor).build();
        Assertions.assertEquals(100, beanSearcher.searchCount(StudentCourse.class, new HashMap<>()));
        Assertions.assertEquals(100, beanSearcher.searchCount(StudentCourse.class, null));
        System.out.println("\ttest_count ok!");
    }

    @Test
    public void test_sum() {
        SqlExecutor sqlExecutor = new SqlExecutor() {
            @Override
            public <T> SqlResult<T> execute(SearchSql<T> searchSql) {
                Assertions.assertNull(searchSql.getListSqlString());
                Assertions.assertTrue(searchSql.getListSqlParams().isEmpty());
                Assertions.assertEquals("select sum(c_1) c_1_sum_ from (select course_id c_0, sum(score) c_1, avg(score) c_2 from student_course group by course_id) t_", searchSql.getClusterSqlString());
                Assertions.assertTrue(searchSql.getClusterSqlParams().isEmpty());
                return new SqlResult<>(searchSql, null, columnLabel -> 100);
            }
        };

        MapSearcher mapSearcher = SearcherBuilder.mapSearcher().sqlExecutor(sqlExecutor).build();
        Assertions.assertEquals(100, mapSearcher.searchSum(StudentCourse.class, new HashMap<>(), "sumScore"));
        Assertions.assertEquals(100, mapSearcher.searchSum(StudentCourse.class, new HashMap<>(), StudentCourse::getSumScore));
        Assertions.assertArrayEquals(new Number[]{100}, mapSearcher.searchSum(StudentCourse.class, new HashMap<>(), new String[]{"sumScore"}));
        Assertions.assertEquals(100, mapSearcher.searchSum(StudentCourse.class, null, "sumScore"));
        Assertions.assertEquals(100, mapSearcher.searchSum(StudentCourse.class, null, StudentCourse::getSumScore));
        Assertions.assertArrayEquals(new Number[]{100}, mapSearcher.searchSum(StudentCourse.class, null, new String[]{"sumScore"}));

        BeanSearcher beanSearcher = SearcherBuilder.beanSearcher().sqlExecutor(sqlExecutor).build();
        Assertions.assertEquals(100, beanSearcher.searchSum(StudentCourse.class, new HashMap<>(), "sumScore"));
        Assertions.assertArrayEquals(new Number[]{100}, beanSearcher.searchSum(StudentCourse.class, new HashMap<>(), new String[]{"sumScore"}));
        Assertions.assertEquals(100, beanSearcher.searchSum(StudentCourse.class, null, "sumScore"));
        Assertions.assertArrayEquals(new Number[]{100}, beanSearcher.searchSum(StudentCourse.class, null, new String[]{"sumScore"}));
        System.out.println("\ttest_sum ok!");
    }

    @Test
    public void test_where() {
        SqlExecutor sqlExecutor = new SqlExecutor() {
            @Override
            public <T> SqlResult<T> execute(SearchSql<T> searchSql) {
                Assertions.assertNull(searchSql.getClusterSqlString());
                Assertions.assertTrue(searchSql.getClusterSqlParams().isEmpty());
                Assertions.assertEquals("select course_id c_0, sum(score) c_1, avg(score) c_2 from student_course where (course_id = ?) group by course_id limit ?, ?", searchSql.getListSqlString());
                List<Object> params = searchSql.getListSqlParams();
                Assertions.assertEquals(3, params.size());
                Assertions.assertEquals(5L, params.get(0));
                Assertions.assertEquals(0L, params.get(1));
                Assertions.assertEquals(1, params.get(2));
                return new SqlResult<>(searchSql, new SqlResult.ResultSet() {
                    @Override
                    public boolean next() {
                        return false;
                    }
                    @Override
                    public Object get(String columnLabel) {
                        return 0;
                    }
                }, columnLabel -> 100);
            }
        };
        MapSearcher mapSearcher = SearcherBuilder.mapSearcher().sqlExecutor(sqlExecutor).build();
        mapSearcher.searchFirst(StudentCourse.class, MapUtils.builder().field(StudentCourse::getCourseId, 5).build());
        BeanSearcher beanSearcher = SearcherBuilder.beanSearcher().sqlExecutor(sqlExecutor).build();
        beanSearcher.searchFirst(StudentCourse.class, MapUtils.builder().field(StudentCourse::getCourseId, 5).build());
        System.out.println("\ttest_where ok!");
    }


    @Test
    public void test_having() {
        SqlExecutor sqlExecutor = new SqlExecutor() {
            @Override
            public <T> SqlResult<T> execute(SearchSql<T> searchSql) {
                Assertions.assertNull(searchSql.getClusterSqlString());
                Assertions.assertTrue(searchSql.getClusterSqlParams().isEmpty());
                Assertions.assertEquals("select course_id c_0, sum(score) c_1, avg(score) c_2 from student_course group by course_id having (c_2 > ?) limit ?, ?", searchSql.getListSqlString());
                List<Object> params = searchSql.getListSqlParams();
                Assertions.assertEquals(3, params.size());
                Assertions.assertEquals(80, params.get(0));
                Assertions.assertEquals(0L, params.get(1));
                Assertions.assertEquals(1, params.get(2));
                return new SqlResult<>(searchSql, new SqlResult.ResultSet() {
                    @Override
                    public boolean next() {
                        return false;
                    }
                    @Override
                    public Object get(String columnLabel) {
                        return 0;
                    }
                }, columnLabel -> 100);
            }
        };
        MapSearcher mapSearcher = SearcherBuilder.mapSearcher().sqlExecutor(sqlExecutor).build();
        mapSearcher.searchFirst(StudentCourse.class, MapUtils.builder().field(StudentCourse::getAvgScore, 80).op(GreaterThan.class).build());
        BeanSearcher beanSearcher = SearcherBuilder.beanSearcher().sqlExecutor(sqlExecutor).build();
        beanSearcher.searchFirst(StudentCourse.class, MapUtils.builder().field(StudentCourse::getAvgScore, 80).op(GreaterThan.class).build());
        System.out.println("\ttest_having ok!");
    }


    @Test
    public void test_where_having_1() {
        SqlExecutor sqlExecutor = new SqlExecutor() {
            @Override
            public <T> SqlResult<T> execute(SearchSql<T> searchSql) {
                Assertions.assertNull(searchSql.getClusterSqlString());
                Assertions.assertTrue(searchSql.getClusterSqlParams().isEmpty());
                Assertions.assertEquals("select course_id c_0, sum(score) c_1, avg(score) c_2 from student_course where (course_id = ?) group by course_id having (c_2 > ?) limit ?, ?", searchSql.getListSqlString());
                List<Object> params = searchSql.getListSqlParams();
                Assertions.assertEquals(4, params.size());
                Assertions.assertEquals(5L, params.get(0));
                Assertions.assertEquals(80, params.get(1));
                Assertions.assertEquals(0L, params.get(2));
                Assertions.assertEquals(1, params.get(3));
                return new SqlResult<>(searchSql, new SqlResult.ResultSet() {
                    @Override
                    public boolean next() {
                        return false;
                    }
                    @Override
                    public Object get(String columnLabel) {
                        return 0;
                    }
                }, columnLabel -> 100);
            }
        };
        MapSearcher mapSearcher = SearcherBuilder.mapSearcher().sqlExecutor(sqlExecutor).build();
        mapSearcher.searchFirst(StudentCourse.class, MapUtils.builder().field(StudentCourse::getCourseId, 5)
                .field(StudentCourse::getAvgScore, 80).op(GreaterThan.class).build());
        BeanSearcher beanSearcher = SearcherBuilder.beanSearcher().sqlExecutor(sqlExecutor).build();
        beanSearcher.searchFirst(StudentCourse.class, MapUtils.builder().field(StudentCourse::getCourseId, 5)
                .field(StudentCourse::getAvgScore, 80).op(GreaterThan.class).build());
        System.out.println("\ttest_where_having_1 ok!");
    }

    @SearchBean(groupBy = "course_id", having = "sum_score > 100")
    public static class StudentCourse2 {
        private long courseId;
        @DbField(value = "sum(score)", alias = "sum_score")
        private int sumScore;
        @DbField("avg(score)")
        private int avgScore;
    }

    @Test
    public void test_count_having_1() {
        SqlExecutor sqlExecutor = new SqlExecutor() {
            @Override
            public <T> SqlResult<T> execute(SearchSql<T> searchSql) {
                System.out.println(searchSql.getClusterSqlString());
                Assertions.assertNull(searchSql.getListSqlString());
                Assertions.assertTrue(searchSql.getListSqlParams().isEmpty());
                Assertions.assertEquals("select count(*) s_count from (select course_id c_1, sum(score) sum_score, avg(score) c_2 from student_course2 group by course_id having (sum_score > 100)) t_", searchSql.getClusterSqlString());
                Assertions.assertTrue(searchSql.getClusterSqlParams().isEmpty());
                return new SqlResult<>(searchSql, null, columnLabel -> 100);
            }
        };

        MapSearcher mapSearcher = SearcherBuilder.mapSearcher().sqlExecutor(sqlExecutor).build();
        Assertions.assertEquals(100, mapSearcher.searchCount(StudentCourse2.class, new HashMap<>()));
        Assertions.assertEquals(100, mapSearcher.searchCount(StudentCourse2.class, null));

        BeanSearcher beanSearcher = SearcherBuilder.beanSearcher().sqlExecutor(sqlExecutor).build();
        Assertions.assertEquals(100, beanSearcher.searchCount(StudentCourse2.class, new HashMap<>()));
        Assertions.assertEquals(100, beanSearcher.searchCount(StudentCourse2.class, null));
        System.out.println("\ttest_count_having_1 ok!");
    }

    @Test
    public void test_count_having_2() {
        SqlExecutor sqlExecutor = new SqlExecutor() {
            @Override
            public <T> SqlResult<T> execute(SearchSql<T> searchSql) {
                System.out.println(searchSql.getClusterSqlString());
                Assertions.assertNull(searchSql.getListSqlString());
                Assertions.assertTrue(searchSql.getListSqlParams().isEmpty());
                Assertions.assertEquals("select count(*) s_count from (select course_id c_1, sum(score) sum_score, avg(score) c_2 from student_course2 where (course_id = ?) group by course_id having (sum_score > 100)) t_", searchSql.getClusterSqlString());
                List<Object> params = searchSql.getClusterSqlParams();
                Assertions.assertEquals(1, params.size());
                Assertions.assertEquals(5L, params.get(0));
                return new SqlResult<>(searchSql, null, columnLabel -> 100);
            }
        };

        MapSearcher mapSearcher = SearcherBuilder.mapSearcher().sqlExecutor(sqlExecutor).build();
        Assertions.assertEquals(100, mapSearcher.searchCount(StudentCourse2.class, MapUtils.builder().field(StudentCourse::getCourseId, 5).build()));
        System.out.println("\ttest_count_having_2 ok!");
    }


    @Test
    public void test_count_having_3() {
        SqlExecutor sqlExecutor = new SqlExecutor() {
            @Override
            public <T> SqlResult<T> execute(SearchSql<T> searchSql) {
                System.out.println(searchSql.getClusterSqlString());
                Assertions.assertNull(searchSql.getListSqlString());
                Assertions.assertTrue(searchSql.getListSqlParams().isEmpty());
                Assertions.assertEquals("select count(*) s_count from (select course_id c_1, sum(score) sum_score, avg(score) c_2 from student_course2 group by course_id having (sum_score > 100) and (c_2 > ?)) t_", searchSql.getClusterSqlString());
                List<Object> params = searchSql.getClusterSqlParams();
                Assertions.assertEquals(1, params.size());
                Assertions.assertEquals(80, params.get(0));
                return new SqlResult<>(searchSql, null, columnLabel -> 100);
            }
        };

        MapSearcher mapSearcher = SearcherBuilder.mapSearcher().sqlExecutor(sqlExecutor).build();
        Assertions.assertEquals(100, mapSearcher.searchCount(StudentCourse2.class, MapUtils.builder().field(StudentCourse::getAvgScore, 80).op(GreaterThan.class).build()));
        System.out.println("\ttest_count_having_3 ok!");
    }

    @Test
    public void test_count_having_4() {
        SqlExecutor sqlExecutor = new SqlExecutor() {
            @Override
            public <T> SqlResult<T> execute(SearchSql<T> searchSql) {
                System.out.println(searchSql.getClusterSqlString());
                Assertions.assertNull(searchSql.getListSqlString());
                Assertions.assertTrue(searchSql.getListSqlParams().isEmpty());
                Assertions.assertEquals("select count(*) s_count from (select course_id c_1, sum(score) sum_score, avg(score) c_2 from student_course2 where (course_id = ?) group by course_id having (sum_score > 100) and (c_2 > ?)) t_", searchSql.getClusterSqlString());
                List<Object> params = searchSql.getClusterSqlParams();
                Assertions.assertEquals(2, params.size());
                Assertions.assertEquals(5L, params.get(0));
                Assertions.assertEquals(80, params.get(1));
                return new SqlResult<>(searchSql, null, columnLabel -> 100);
            }
        };

        MapSearcher mapSearcher = SearcherBuilder.mapSearcher().sqlExecutor(sqlExecutor).build();
        Assertions.assertEquals(100, mapSearcher.searchCount(StudentCourse2.class, MapUtils.builder().field(StudentCourse::getCourseId, 5)
                .field(StudentCourse::getAvgScore, 80).op(GreaterThan.class).build()));
        System.out.println("\ttest_count_having_4 ok!");
    }

    @SearchBean(
        tables = "course c, student_course sc",
        where = "c.id = sc.course_id",
        groupBy = "sc.course_id",
        fields = {
            @DbField(name = "no", mapTo = "c"),
            @DbField(name = "name", mapTo = "c", onlyOn = Contain.class),
        },
        autoMapTo = "sc"
    )
    public static class CourseScore {
        private long courseId;
        @DbField("sum(sc.score)")
        private int sumScore;
        @DbField("avg(sc.score)")
        private int avgScore;
        public long getCourseId() {
            return courseId;
        }
        public int getSumScore() {
            return sumScore;
        }
        public int getAvgScore() {
            return avgScore;
        }
    }

    @Test
    public void test_where_having_2() {
        SqlExecutor sqlExecutor = new SqlExecutor() {
            @Override
            public <T> SqlResult<T> execute(SearchSql<T> searchSql) {
                Assertions.assertNull(searchSql.getClusterSqlString());
                Assertions.assertTrue(searchSql.getClusterSqlParams().isEmpty());
                System.out.println(searchSql.getListSqlString());
                Assertions.assertEquals("select sc.course_id c_2, sum(sc.score) c_3, avg(sc.score) c_4 from course c, student_course sc " +
                        "where (c.id = sc.course_id) and (c.no like ?) and (c.name like ?) and (sc.course_id = ?) group by sc.course_id having (c_4 > ?)", searchSql.getListSqlString());
                List<Object> params = searchSql.getListSqlParams();
                System.out.println(params);
                Assertions.assertEquals(4, params.size());
                Assertions.assertEquals("100%", params.get(0));
                Assertions.assertEquals("%Jack%", params.get(1));
                Assertions.assertEquals(5L, params.get(2));
                Assertions.assertEquals(80, params.get(3));
                return new SqlResult<>(searchSql, new SqlResult.ResultSet() {
                    @Override
                    public boolean next() {
                        return false;
                    }
                    @Override
                    public Object get(String columnLabel) {
                        return 0;
                    }
                }, columnLabel -> 100);
            }
        };
        MapSearcher mapSearcher = SearcherBuilder.mapSearcher().sqlExecutor(sqlExecutor).build();
        mapSearcher.searchAll(CourseScore.class, MapUtils.builder()
                .field(CourseScore::getCourseId, 5)
                .field("name", "Jack")
                .field("no", "100").op(StartWith.class)
                .field(CourseScore::getAvgScore, 80).op(GreaterThan.class)
                .build());
        BeanSearcher beanSearcher = SearcherBuilder.beanSearcher().sqlExecutor(sqlExecutor).build();
        beanSearcher.searchAll(CourseScore.class, MapUtils.builder()
                .field(CourseScore::getCourseId, 5)
                .field("name", "Jack")
                .field("no", "100").op(StartWith.class)
                .field(CourseScore::getAvgScore, 80).op(GreaterThan.class)
                .build());
        System.out.println("\ttest_where_having_2 ok!");
    }

    @SearchBean(
        tables = "course c, student_course sc",
        where = "c.id = sc.course_id",
        groupBy = "course_id",
        fields = {
            @DbField(name = "avgScore", value = "avg(sc.score)", cluster = Cluster.TRUE),
            @DbField(name = "name", mapTo = "c", onlyOn = Contain.class),
        },
        autoMapTo = "sc"
    )
    public static class CourseScore3 {
        // 将映射为 “sc.course_id”，并不在 groupBy 里，这里使用 Cluster.FALSE 来标记非聚合字段
        @DbField(cluster = Cluster.FALSE)
        private long courseId;
        @DbField("sum(sc.score)")
        private int sumScore;
        public long getCourseId() {
            return courseId;
        }
        public int getSumScore() {
            return sumScore;
        }
    }

    @Test
    public void test_where_having_3() {
        SqlExecutor sqlExecutor = new SqlExecutor() {
            @Override
            public <T> SqlResult<T> execute(SearchSql<T> searchSql) {
                Assertions.assertNull(searchSql.getClusterSqlString());
                Assertions.assertTrue(searchSql.getClusterSqlParams().isEmpty());
                System.out.println(searchSql.getListSqlString());
                Assertions.assertEquals("select sc.course_id c_2, sum(sc.score) c_3 from course c, student_course sc " +
                        "where (c.id = sc.course_id) and (c.name like ?) and (sc.course_id = ?) group by course_id having (avg(sc.score) > ?)", searchSql.getListSqlString());
                List<Object> params = searchSql.getListSqlParams();
                System.out.println(params);
                Assertions.assertEquals(3, params.size());
                Assertions.assertEquals("%Jack%", params.get(0));
                Assertions.assertEquals(5L, params.get(1));
                Assertions.assertEquals(80, params.get(2));
                return new SqlResult<>(searchSql, new SqlResult.ResultSet() {
                    @Override
                    public boolean next() {
                        return false;
                    }
                    @Override
                    public Object get(String columnLabel) {
                        return 0;
                    }
                }, columnLabel -> 100);
            }
        };
        MapSearcher mapSearcher = SearcherBuilder.mapSearcher().sqlExecutor(sqlExecutor).build();
        mapSearcher.searchAll(CourseScore3.class, MapUtils.builder()
                .field(CourseScore3::getCourseId, 5)
                .field("name", "Jack")
                .field("avgScore", 80).op(GreaterThan.class)
                .build());
        BeanSearcher beanSearcher = SearcherBuilder.beanSearcher().sqlExecutor(sqlExecutor).build();
        beanSearcher.searchAll(CourseScore3.class, MapUtils.builder()
                .field(CourseScore3::getCourseId, 5)
                .field("name", "Jack")
                .field("avgScore", 80).op(GreaterThan.class)
                .build());
        System.out.println("\ttest_where_having_3 ok!");
    }

    @Test
    public void test_group_expr_1() {
        SqlExecutor sqlExecutor = new SqlExecutor() {
            @Override
            public <T> SqlResult<T> execute(SearchSql<T> searchSql) {
                Assertions.assertNull(searchSql.getClusterSqlString());
                Assertions.assertTrue(searchSql.getClusterSqlParams().isEmpty());
                System.out.println(searchSql.getListSqlString());
                Assertions.assertEquals("select sc.course_id c_2, sum(sc.score) c_3 from course c, student_course sc " +
                        "where (c.id = sc.course_id) and ((sc.course_id = ?) or (c.name like ?)) group by course_id having ((avg(sc.score) > ?) or (c_3 <= ?))", searchSql.getListSqlString());
                List<Object> params = searchSql.getListSqlParams();
                Assertions.assertEquals(4, params.size());
                Assertions.assertEquals(5L, params.get(0));
                Assertions.assertEquals("%Jack%", params.get(1));
                Assertions.assertEquals(80, params.get(2));
                Assertions.assertEquals(1000, params.get(3));
                return new SqlResult<>(searchSql, new SqlResult.ResultSet() {
                    @Override
                    public boolean next() {
                        return false;
                    }
                    @Override
                    public Object get(String columnLabel) {
                        return 0;
                    }
                }, columnLabel -> 100);
            }
        };
        SearcherBuilder.mapSearcher().sqlExecutor(sqlExecutor).build().searchAll(CourseScore3.class,
                MapUtils.builder()
                    .group("A")
                    .field(CourseScore3::getCourseId, 5)
                    .group("B")
                    .field("name", "Jack")
                    .group("C")
                    .field("avgScore", 80).op(GreaterThan.class)
                    .group("D")
                    .field("sumScore", 1000).op(FieldOps.LessEqual)
                    .groupExpr("(A|B)&(C|D)")
                    .build()
        );
        System.out.println("\ttest_group_expr_1 ok!");
    }

    @Test
    public void test_group_expr_2() {
        SqlExecutor sqlExecutor = new SqlExecutor() {
            @Override
            public <T> SqlResult<T> execute(SearchSql<T> searchSql) {
                Assertions.assertNull(searchSql.getClusterSqlString());
                Assertions.assertTrue(searchSql.getClusterSqlParams().isEmpty());
                System.out.println(searchSql.getListSqlString());
                Assertions.assertEquals("select sc.course_id c_2, sum(sc.score) c_3 from course c, student_course sc " +
                        "where (c.id = sc.course_id) and ((sc.course_id = ?) or (c.name like ?)) group by course_id having (avg(sc.score) > ?) and (c_3 <= ?)", searchSql.getListSqlString());
                List<Object> params = searchSql.getListSqlParams();
                Assertions.assertEquals(4, params.size());
                Assertions.assertEquals(5L, params.get(0));
                Assertions.assertEquals("%Jack%", params.get(1));
                Assertions.assertEquals(80, params.get(2));
                Assertions.assertEquals(1000, params.get(3));
                return new SqlResult<>(searchSql, new SqlResult.ResultSet() {
                    @Override
                    public boolean next() {
                        return false;
                    }
                    @Override
                    public Object get(String columnLabel) {
                        return 0;
                    }
                }, columnLabel -> 100);
            }
        };
        SearcherBuilder.mapSearcher().sqlExecutor(sqlExecutor).build().searchAll(CourseScore3.class,
                MapUtils.builder()
                        .group("A")
                        .field(CourseScore3::getCourseId, 5)
                        .field("avgScore", 80).op(GreaterThan.class)
                        .field("sumScore", 1000).op(FieldOps.LessEqual)
                        .group("B")
                        .field("name", "Jack")
                        .field("avgScore", 80).op(GreaterThan.class)
                        .field("sumScore", 1000).op(FieldOps.LessEqual)
                        .groupExpr("A|B")
                        .build()
        );
        System.out.println("\ttest_group_expr_2 ok!");
    }

    @Test
    public void test_group_expr_3() {
        SqlExecutor sqlExecutor = new SqlExecutor() {
            @Override
            public <T> SqlResult<T> execute(SearchSql<T> searchSql) {
                Assertions.assertNull(searchSql.getClusterSqlString());
                Assertions.assertTrue(searchSql.getClusterSqlParams().isEmpty());
                System.out.println(searchSql.getListSqlString());
                Assertions.assertEquals("select sc.course_id c_2, sum(sc.score) c_3 from course c, student_course sc " +
                        "where (c.id = sc.course_id) and ((sc.course_id = ?) or (c.name like ?)) group by course_id", searchSql.getListSqlString());
                List<Object> params = searchSql.getListSqlParams();
                Assertions.assertEquals(2, params.size());
                Assertions.assertEquals(5L, params.get(0));
                Assertions.assertEquals("%Jack%", params.get(1));
                return new SqlResult<>(searchSql, new SqlResult.ResultSet() {
                    @Override
                    public boolean next() {
                        return false;
                    }
                    @Override
                    public Object get(String columnLabel) {
                        return 0;
                    }
                }, columnLabel -> 100);
            }
        };
        SearcherBuilder.mapSearcher().sqlExecutor(sqlExecutor).build().searchAll(CourseScore3.class,
                MapUtils.builder()
                        .group("A")
                        .field(CourseScore3::getCourseId, 5)
                        .group("B")
                        .field("name", "Jack")
                        .groupExpr("(A|B)")
                        .build()
        );
        System.out.println("\ttest_group_expr_3 ok!");
    }

    @Test
    public void test_group_expr_4() {
        SqlExecutor sqlExecutor = new SqlExecutor() {
            @Override
            public <T> SqlResult<T> execute(SearchSql<T> searchSql) {
                Assertions.assertNull(searchSql.getClusterSqlString());
                Assertions.assertTrue(searchSql.getClusterSqlParams().isEmpty());
                System.out.println(searchSql.getListSqlString());
                Assertions.assertEquals("select sc.course_id c_2, sum(sc.score) c_3 from course c, student_course sc " +
                        "where (c.id = sc.course_id) group by course_id having ((avg(sc.score) > ?) or (c_3 <= ?))", searchSql.getListSqlString());
                List<Object> params = searchSql.getListSqlParams();
                Assertions.assertEquals(2, params.size());
                Assertions.assertEquals(80, params.get(0));
                Assertions.assertEquals(1000, params.get(1));
                return new SqlResult<>(searchSql, new SqlResult.ResultSet() {
                    @Override
                    public boolean next() {
                        return false;
                    }
                    @Override
                    public Object get(String columnLabel) {
                        return 0;
                    }
                }, columnLabel -> 100);
            }
        };
        SearcherBuilder.mapSearcher().sqlExecutor(sqlExecutor).build().searchAll(CourseScore3.class,
                MapUtils.builder()
                        .group("A")
                        .field("avgScore", 80).op(GreaterThan.class)
                        .group("B")
                        .field("sumScore", 1000).op(FieldOps.LessEqual)
                        .groupExpr("A|B")
                        .build()
        );
        System.out.println("\ttest_group_expr_4 ok!");
    }

}
