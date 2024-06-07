package cn.zhxu.bs.filter;

import cn.zhxu.bs.BeanMeta;
import cn.zhxu.bs.IllegalParamException;
import cn.zhxu.bs.ParamFilter;
import cn.zhxu.bs.util.MapUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 数组参数值过滤器，用于配合 {@link MapUtils#flat(Map)} 与 {@link MapUtils#flatBuilder(Map)} 方法，兼容数组参数值的用法。<br>
 * 例如前端传参：age=20 & age=30 & age-op=bt
 * @author Troy.Zhou @ 2024-06-06
 * @since v4.3
 */
public class ArrValueParamFilter implements ParamFilter {

    private String separator = "-";

    @Override
    public <T> Map<String, Object> doFilter(BeanMeta<T> beanMeta, Map<String, Object> paraMap)
            throws IllegalParamException {
        List<String> keys = MapUtils.obtainList(paraMap, MapUtils.ARRAY_KEYS, false);
        if (keys == null) {
            return paraMap;
        }
        for (String key : keys) {
            if (key == null || key.contains(separator)) {
                continue;
            }
            Object value = paraMap.get(key);
            if (value instanceof String[]) {
                String[] vs = (String[]) value;
                for (int i = 0; i < vs.length; i++) {
                    paraMap.put(key + separator + i, vs[i]);
                }
            }
        }
        return paraMap;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = Objects.requireNonNull(separator);
    }

}
