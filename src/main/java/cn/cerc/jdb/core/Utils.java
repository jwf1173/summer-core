package cn.cerc.jdb.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

    public static String serializeToString(Object obj) throws IOException {
        if (obj == null)
            return null;
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject(obj);
        return byteOut.toString("ISO-8859-1");// 此处只能是ISO-8859-1,但是不会影响中文使用;
    }

    public static Object deserializeToObject(String str) throws IOException, ClassNotFoundException {
        if (str == null)
            return null;
        ByteArrayInputStream byteIn = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        return objIn.readObject();
    }

}
