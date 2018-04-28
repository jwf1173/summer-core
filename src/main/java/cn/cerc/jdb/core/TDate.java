package cn.cerc.jdb.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期对象
 * 
 * @author 张弓
 *
 */
public class TDate extends TDateTime {
    private static final long serialVersionUID = 1L;

    public TDate(Date date) {
        this.setData(date);
    }

    public TDate(String date) {
        TDateTime val = TDateTime.fromDate(date);
        if (val == null)
            throw new RuntimeException(String.format("%s 不是一个有效的日期格式！", date));
        this.setData(val.getData());
    }

    // 当天，不带时分秒
    public static TDate Today() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String str = sdf.format(date);
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("日期类型转换错误");
        }
        return new TDate(date);
    }

    @Override
    public String toString() {
        return this.getDate();
    }

    public String getShortValue() {
        String year = this.getYearMonth().substring(2, 4);
        int month = this.getMonth();
        int day = this.getDay();
        if (TDateTime.Now().compareYear(this) != 0) {
            return String.format("%s年%d月", year, month);
        } else {
            return String.format("%d月%d日", month, day);
        }
    }

    public static void main(String[] args) {
        TDate val;
        val = new TDate(TDateTime.Now().incMonth(-13).getData());
        System.out.println(val.getShortValue());
        val = TDate.Today();
        System.out.println(val.getShortValue());
    }
}
