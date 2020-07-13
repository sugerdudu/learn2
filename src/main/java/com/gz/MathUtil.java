package com.gz;

import java.math.BigDecimal;

public class MathUtil {
    /**
     * 做除法并保留小数位数
     *
     * @param a     被除数
     * @param b     除数
     * @param scale 保留小数位
     */
    public static String divide(long a, long b, int scale) {
        return BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b), scale, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    /**
     * 做除法保留6位小数，纳秒转毫秒
     * @param nano 纳秒
     * @return
     */
    public static String divideNanoToMilli(long nano) {
        return BigDecimal.valueOf(nano).divide(BigDecimal.valueOf(1000000), 6, BigDecimal.ROUND_HALF_UP).toPlainString();
    }
}
