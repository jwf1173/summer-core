package cn.cerc.jdb.core;

public class Utils {
    /**
     * 保障查询安全，防范注入攻击
     * 
     * @param value
     *            用户输入值
     * @return 经过处理后的值
     */
    public static String safeString(String value) {
        return value == null ? "" : value.replaceAll("'", "''");
    }
}
