package com.example.clarence.utillibrary;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class MobileUtils {

    /**
     * 匹配手机号的规则：[3578]是手机号第二位可能出现的数字
     */
    public static final String REGEX_MOBILE = "^[1][3578][0-9]{9}$";

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 1、手机号开头集合
     * 166，
     * 176，177，178
     * 180，181，182，183，184，185，186，187，188，189
     * 145，147
     * 130，131，132，133，134，135，136，137，138，139
     * 150，151，152，153，155，156，157，158，159
     * 198，199
     * 2、正则表达式
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isChinaPhoneLegal(String str)
            throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(166)|(17[0-8])|(18[0-9])|(19[8-9])|(147,145))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
