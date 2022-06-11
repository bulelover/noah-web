package org.noah.core.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.noah.core.annotation.Check;
import org.noah.core.exception.BusinessException;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author SouthXia
 */
public class CheckUtils implements Serializable {
    private static final long serialVersionUID = -4060081488564591535L;
    private static final Pattern INTEGER_PATTERN = Pattern.compile("^[-+]?\\d+$");
    //正整数，不包括零
    private static final Pattern INTEGER_POSITIVE_PATTERN = Pattern.compile("^[1-9]\\d*$");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]*$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^((0\\d{2,3}(-?)\\d{7,8})|(1[3456789]\\d{9}))$");
    private static final Pattern DATE_PATTERN = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-/\\s]?((((0?[13578])|(1[02]))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-/\\s]?((((0?[13578])|(1[02]))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3])):([0-5]?[0-9])((\\s)|(:([0-5]?[0-9])))))?$");


    public CheckUtils() {

    }

    enum Signal {USERNAME, PASSWORD, MAIL, CODE}

    public static String getPattern(Signal signal) {
        String pattern = "";
        switch (signal) {
            case USERNAME: pattern = "^[a-zA-Z0-9_-]{4,16}$"; break;
            //字母数字下划线横杆
            case CODE: pattern = "^[a-zA-Z0-9_-]*$"; break;
            case PASSWORD: pattern = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[a-zA-Z0-9\\W_]{6,20}$"; break;
            case MAIL: pattern = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$"; break;
            default:
        }
        return pattern;
    }

    public CheckUtils code(){
        return this.pattern(CheckUtils.getPattern(Signal.CODE), "格式有误，只能输入字母，数字，下划线，减号中的字符");
    }
    public CheckUtils mail(){
        return this.pattern(CheckUtils.getPattern(Signal.MAIL), "格式有误");
    }
    public CheckUtils username(){
        return this.pattern(CheckUtils.getPattern(Signal.USERNAME), "格式有误，只能输入4到16位（字母，数字，下划线，减号）字符");
    }
    public CheckUtils password(){
        return this.pattern(CheckUtils.getPattern(Signal.PASSWORD), "格式有误，只能输入6~16位（大写字母，小写字母，数字，特殊符号）字符，且至少包含的其中三种");
    }

    /**
     * 需要验证的值x
     */
    private Object value;

    /**
     * 需要验证的字段名
     */
    private String column;

    /**
     * 一个验证流程返回的错误消息
     */
    private StringBuilder errorMsg;


    public static CheckUtils init(){
        return new CheckUtils();
    }

    public CheckUtils set(Object value, String column){
        return this.setValue(value).setColumn(column);
    }

    public CheckUtils pattern(String pattern, String error){
        //过滤空值
        if(valueIsBlank()){
            return this;
        }
        String source = obj2str(this.value);
        if(!Pattern.compile(pattern).matcher(source).matches()){
            this.append("【").append(this.getColumn()).append("】"+error).append("；\n");
        }
        return this;
    }

    // 验证字典编码值域
    public CheckUtils dictValue(String dictCode){
        //过滤空值
        if(valueIsBlank()){
            return this;
        }
//        String label = DictUtils.getDictLabel(this.value.toString(), dictCode, "");
//        if(StringUtils.isBlank(label)){
//            this.append("【").append(this.getColumn()).append("】不在（").append(dictCode)
//                    .append("）字典中").append("；\n");
//        }
        return this;
    }

    // 验证字典名称值域
    public CheckUtils dictKey(String dictCode){
        //过滤空值
        if(valueIsBlank()){
            return this;
        }
//        String value = DictUtils.getDictValue(this.value.toString(), dictCode, "");
//        if(StringUtils.isBlank(value)){
//            this.append("【").append(this.getColumn()).append("】不在（").append(dictCode)
//                    .append("）字典中").append("；\n");
//        }
        return this;
    }


    //验证值域
    public CheckUtils fields(String[] strs){
        //过滤空值
        if(valueIsBlank()){
            return this;
        }
        if(!ArrayUtils.contains(strs,this.value.toString())){
            this.append("【").append(this.getColumn()).append("】必须是").append(Arrays.toString(strs))
                    .append("其中一种").append("；\n");
        }
        return this;
    }

    private String toString(Object[] a) {
        if (a == null) {
            return "null";
        }

        int iMax = a.length - 1;
        if (iMax == -1) {
            return "[]";
        }

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(a[i]);
            b.append("(").append(a[i].getClass().getSimpleName()).append(")");
            if (i == iMax) {
                return b.append(']').toString();
            }
            b.append(", ");
        }
    }

    /** 验证整数 */
    public CheckUtils integer(){
        //过滤空值
        if(valueIsBlank()){
            return this;
        }
        String source = obj2str(this.value);
        if(!INTEGER_PATTERN.matcher(source).matches()){
            this.append("【").append(this.getColumn()).append("】必须是数字且必须是整数").append("；\n");
        }
        return this;
    }
    /** 验证最大长度为len的整数  */
    public CheckUtils integer(int len){
        //过滤空值
        if(valueIsBlank()){
            return this;
        }
        String source = obj2str(this.value);
        if(!INTEGER_PATTERN.matcher(source).matches()){
            this.append("【").append(this.getColumn()).append("】必须是数字且必须是整数").append("；\n");
        }else if(source.length() > len){
            this.append("【").append(this.getColumn()).append("】数字长度不能超过").append(len).append("；\n");
        }
        return this;
    }

    /** 验证最大长度为len的正整数  */
    public CheckUtils positiveInteger(int len){
        //过滤空值
        if(valueIsBlank()){
            return this;
        }
        String source = obj2str(this.value);
        if(!INTEGER_POSITIVE_PATTERN.matcher(source).matches()){
            this.append("【").append(this.getColumn()).append("】必须是数字且必须是正整数").append("；\n");
        }else if(source.length() > len){
            this.append("【").append(this.getColumn()).append("】数字长度不能超过").append(len).append("；\n");
        }

        return this;
    }

    /** 验证数字 */
    public CheckUtils number(){
        //过滤空值
        if(valueIsBlank()){
            return this;
        }
        String source =  obj2str(this.value);
        if(!NUMBER_PATTERN.matcher(source).matches()){
            this.append("【").append(this.getColumn()).append("】必须是数字").append("；\n");
        }
        return this;
    }

    /**
     * 固定电话和手机号同时验证
     */
    public CheckUtils phone(){
        //过滤空值
        if(valueIsBlank()){
            return this;
        }

        String source = this.value.toString();
        if(!PHONE_PATTERN.matcher(source).matches()){
            this.append("【").append(this.getColumn()).append("】电话号格式有误").append("；\n");
        }
        return this;
    }

    /**
     * 验证小数位  DP : decimal place
     * @param max 支持最大支持 max位小数
     */
    public CheckUtils maxDecimalPlace(int max){
        //过滤空值
        if(valueIsBlank()){
            return this;
        }
        if(max == 0){
            return this.integer();
        }
        String source = obj2str(this.value);
        Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]{0,"+max+"})?$");
        if(!pattern.matcher(source).matches()){
            this.append("【").append(this.getColumn()).append("】必须是数字且最多支持").append(max).append("位小数").append("；\n");
        }
        return this;
    }

    /**
     * 按照 类似Oracle数字类型 验证精度
     * @param length 长度
     * @param dp 精度
     * @return CheckUtils
     */
    public CheckUtils precision(int length,int dp){
        //过滤空值
        if(valueIsBlank()){
            return this;
        }
        if(dp == 0){
            return this.integer(length);
        }
        String source = obj2str(this.value);
        Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]{0,"+dp+"})?$");
        if(!pattern.matcher(source).matches() || source.replace(".","").length() > length){
            this.append("【").append(this.getColumn()).append("】超出精度范围（").append(length).append(",")
                    .append(dp).append("）").append("；\n");
        }
        return this;
    }

    /** 年份校验 */
    public CheckUtils year(){
        //过滤空值
        if(valueIsBlank()){
            return this;
        }
        try {
            int i = Integer.parseInt(String.valueOf(this.value));
            if(i<1000 || i>9999){
                this.append("【").append(this.getColumn()).append("】超范围（1000~9999）").append("；\n");
            }
        }catch (Exception e){
            this.append("【").append(this.getColumn()).append("】格式有误，YYYY").append("；\n");
        }
        return this;
    }
    /** 月份校验 */
    public CheckUtils month(){
        //过滤空值
        if(valueIsBlank()){
            return this;
        }
        try {
            int i = Integer.parseInt(String.valueOf(this.value));
            if(i<1 || i>12){
                this.append("【").append(this.getColumn()).append("】超范围（1~12）").append("；\n");
            }
        }catch (Exception e){
            this.append("【").append(this.getColumn()).append("】格式有误，MM").append("；\n");
        }
        return this;
    }

    //验证非空
    public CheckUtils required(){
        if(valueIsBlank()){
            this.append("【").append(this.getColumn()).append("】不能为空").append("；\n");
        }
        return this;
    }

    /**
     * 验证范围  支持类型 Integer String Double Short Float
     * @param max 最大值
     * @param min 最小值
     * @return CheckUtils
     */
    public CheckUtils range(int min,int max){
        //过滤空值
        if(valueIsBlank()){
            return this;
        }
        this.range(Integer.valueOf(min).doubleValue(),Integer.valueOf(max).doubleValue());
        return this;
    }

    /**
     * 验证范围  支持类型 Integer Double Short Float
     * @param max 最大值
     * @param min 最小值
     * @return
     */
    public CheckUtils range(double min, double max){
        //过滤空值
        if(valueIsBlank()){
            return this;
        }
        try {
            if (this.value instanceof Integer || this.value instanceof Double || this.value instanceof String
                    || this.value instanceof Float || this.value instanceof Short  || this.value instanceof BigDecimal) {
                BigDecimal b;
                if(this.value instanceof BigDecimal){
                    b = (BigDecimal) this.value;
                }else {
                    b = new BigDecimal(String.valueOf(this.value));
                }
                double d = b.doubleValue();
                if (d > max) {
                    this.append("【").append(this.getColumn()).append("】超出最大允许范围").append(max).append("；\n");
                }
                if (d < min) {
                    this.append("【").append(this.getColumn()).append("】低于最小允许范围").append(min).append("；\n");
                }
            } else {
                this.append("【").append(this.getColumn()).append("】校验失败：传入值类型不支持").append("；\n");
            }
        }catch (Exception e){
            this.append("【").append(this.getColumn()).append("】范围校验失败：数字类型转换失败").append("；\n");
        }
        return this;
    }

    public Object getValue() {
        return value;
    }

    public CheckUtils setValue(Object value) {
        this.value = value;
        return this;
    }

    public String getColumn() {
        return column; //+"（传入值<"+obj2str(this.getValue())+">）";
    }

    public CheckUtils setColumn(String column) {
        this.column = column;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg==null?"":errorMsg.toString();
    }

    public CheckUtils append(Object errorMsg) {
        if(this.errorMsg == null){
            this.errorMsg = new StringBuilder();
            this.errorMsg.append(errorMsg);
            return this;
        }
        this.errorMsg.append(errorMsg);
        return this;
    }
    public void flushErrorMsg() {
        this.errorMsg = null;
    }

    public CheckUtils maxlength(int max){
        //过滤空值
        if(isNullOrString()){
            return this;
        }
        if(this.value instanceof String){
            String str = (String) this.value;
            if(str.length() > max){
                this.append("【").append(this.getColumn()).append("】超出最大允许长度").append(max).append("；\n");
            }
        }
        return this;
    }


    private boolean valueIsBlank(){
        if(this.getValue() == null){
            return true;
        }
        return this.getValue() instanceof String && StringUtils.isEmpty((String) this.getValue());
    }

    private boolean isNullOrString(){
        //过滤空值
        if(valueIsBlank()){
            return true;
        }
        if(!(this.value instanceof String)) {
            this.append("【").append(this.getColumn()).append("】校验失败：传入值不是String类型").append("；\n");
            return true;
        }
        return false;
    }
    /**
     * 验证yyyy-MM-dd时间格式
     * @return
     */
    public CheckUtils date(){
        //过滤空值
        if(isNullOrString()){
            return this;
        }
        String v = (String) this.getValue();
        if(v.length() == 10){
            return date("yyyy-MM-dd");
        }
        this.append("【").append(this.getColumn()).append("】日期格式不匹配,正确格式：").append("yyyy-MM-dd").append("；\n");
        return this;
    }

    /**
     * 验证yyyy-MM-dd HH:mm:ss 时间格式
     * @return
     */
    public CheckUtils datetime(){
        //过滤空值
        if(isNullOrString()){
            return this;
        }
        String v = (String) this.getValue();
        if(v.length() == 19){
            return date("yyyy-MM-dd HH:mm:ss");
        }
        this.append("【").append(this.getColumn()).append("】日期格式不匹配,正确格式：").append("yyyy-MM-dd HH:mm:ss").append("；\n");
        return this;
    }

    public CheckUtils date(String format){
        //过滤空值
        if(isNullOrString()){
            return this;
        }
        String v = (String) this.getValue();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
        sdf.setLenient(false);
        try {
            sdf.parse(v);
        } catch (ParseException e) {
            this.append("【").append(this.getColumn()).append("】日期格式或值域有误,正确格式：").append(format).append("；\n");
        }
        return this;
    }

    /**
     * 身份证号验证
     * @param simple  true 简单长度验证   false 严格身份证号验证
     * @return CheckUtils
     */
    public CheckUtils idCard(boolean simple){
        //过滤空值
        if(isNullOrString()){
            return this;
        }
        String v = (String) this.getValue();
        String errMsg;
        if(simple){
            errMsg = simpleIdCardValid(v);
        }else {
            errMsg = identityCardVerification(v);
        }
        if(StringUtils.isNotEmpty(errMsg)){
            this.append("【").append(this.getColumn()).append("】").append(errMsg).append("；\n");
        }
        return this;
    }

    private String obj2str(Object o){
        if(o instanceof String) {
            return (String) o;
        }
        if(o instanceof Double || o instanceof Float) {
            NumberFormat nf = NumberFormat.getInstance();
            // 是否以逗号隔开, 默认true
            nf.setGroupingUsed(false);
            // 设置数的小数部分所允许的最小位数
            nf.setMinimumFractionDigits(0);
            // 设置数的小数部分所允许的最大位数
            nf.setMaximumFractionDigits(8);
            return nf.format(o);
        }
        return o==null?"":o.toString();
    }

    public void checkError(){
        if(StringUtils.isNotBlank(this.getErrorMsg())){
            throw new BusinessException(this.getErrorMsg());
        }
    }

    /**
     * 通过注解验证值域合法性
     * @param entity 要验证的实体类
     * @return 错误消息
     */
    public static CheckUtils checkAllFields(Object entity){
        return checkByAnnotation(entity,null,null);
    }

    public static CheckUtils checkIncludeFields(Object entity, String... usedField){
        return checkByAnnotation(entity,usedField,null);
    }

    public static CheckUtils checkExcludeFields(Object entity, String... excludes){
        return checkByAnnotation(entity,null,excludes);
    }

    /**
     * 校验 entity 的值域
     * @param entity 被校验的数据
     * @param usedField 需要校验的字段  传null校验全部
     * @param excludes 需要排除校验的字段  传null校验全部
     * @return CheckUtils
     */
    public static CheckUtils checkByAnnotation(Object entity, String[] usedField, String[] excludes){
        CheckUtils valid = new CheckUtils();
        //获取实体类的所有属性，返回Field数组
        Field[] fields = null;
        fields = BeanUtils.getBeanFields(entity.getClass(), fields);
        //过滤已经注解的字段 记录日志
        for (Field field : fields) {
            boolean hasAnnotation = field.isAnnotationPresent(Check.class);
            if (hasAnnotation) {
                // 获取属性的名字
                String name = field.getName();
                if(usedField!=null && !ArrayUtils.contains(usedField,name)){
                    continue;
                }
                if(excludes!=null && ArrayUtils.contains(excludes,name)){
                    continue;
                }
                //关键。。。可访问私有变量
                field.setAccessible(true);
                // 将属性的首字母大写
                String Name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1)
                        .toUpperCase());
                Check c =field.getAnnotation(Check.class);
                String vName = c.value();
                Method m = null;
                // 调用getter方法获取属性值
                try {
                    m = entity.getClass().getMethod("get" + Name);
                    Object value = m.invoke(entity);
                    if(StringUtils.isBlank(vName)){
                        vName = name;
                    }
                    valid.set(value,vName);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    throw new BusinessException("校验出错"+e.getLocalizedMessage());
                }
                //开始校验
                doValid(c,valid);
            }
        }
        return valid;
    }

    private static void doValid(Check c, CheckUtils valid){
        //空校验
        if(c.required()) {
            valid.required();
        }

        //范围校验
        if(c.range().length == 2) {
            valid.range(c.range()[0],c.range()[1]);
        }
        //整数校验
        if(c.integer()) {
            valid.integer();
        }

        //正整数校验
        if(c.positiveInteger() > 0) {
            valid.positiveInteger(c.positiveInteger());
        }

        //类Oracle 精度校验
        if(c.precision().length == 2) {
            valid.precision(c.precision()[0],c.precision()[1]);
        }

        //验证最大小数位
        if(c.maxDecimalPlace()>=0) {
            valid.maxDecimalPlace(c.maxDecimalPlace());
        }

        //验证字符串日期合法性
        if(StringUtils.isNotBlank(c.date())) {
            valid.date(c.date());
        }

        //验证年份
        if(c.year()) {
            valid.year();
        }
        //验证月份
        if(c.month()) {
            valid.month();
        }

        //验证字典
        if(StringUtils.isNotBlank(c.dictValue())) {
            valid.dictValue(c.dictValue());
        }
        if(StringUtils.isNotBlank(c.dictKey())) {
            valid.dictKey(c.dictKey());
        }

        //值域校验
        if(c.fields().length>0) {
            valid.fields(c.fields());
        }

        //验证字符串最大长度
        if(c.maxlength() > 0) {
            valid.maxlength(c.maxlength());
        }

        //身份证号 1标准验证 2简单验证
        if(c.idCard() == Check.IC_STANDARD) {
            valid.idCard(false);
        }
        if(c.idCard() == Check.IC_SIMPLE) {
            valid.idCard(true);
        }

        //电话号码 1手机号 2固定电话
        if(c.phone()) {
            valid.phone();
        }

        //自定义正则
        if(StringUtils.isNotBlank(c.pattern())) {
            valid.pattern(c.pattern(), c.msg());
        }
        if(c.mail()){
            valid.mail();
        }

        if(c.username()) {
            valid.username();
        }

        if(c.password()) {
            valid.password();
        }
        if(c.code()) {
            valid.code();
        }

    }

    /**
     * 简单身份证校验
     */
    private static String simpleIdCardValid(String idStr){
        //判断号码的长度 15位或18位
        if (idStr.length() != 15 && idStr.length() != 18) {
            return "身份证号码长度应该为15位或18位";
        }
        return "";
    }

    /**
     *身份证验证
     * @param idStr 身份证号
     * @return 验证失败错误消息，成功返回空字符串
     */
    private static String identityCardVerification(String idStr){
        String[] wf = { "1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2" };
        String[] checkCode = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };
        String iDCardNo = "";
        try {
            //判断号码的长度 15位或18位
            if (idStr.length() != 15 && idStr.length() != 18) {
                return "身份证号码长度应该为15位或18位";
            }
            if (idStr.length() == 18) {
                iDCardNo = idStr.substring(0, 17);
            }
            if (idStr.length() == 15) {
                iDCardNo = idStr.substring(0, 6) + "19" + idStr.substring(6, 15);
            }
            if (!isStrNum(iDCardNo)) {
                return "身份证15位号码都应为数字;18位号码除最后一位外,都应为数字";
            }
            //判断出生年月
            String strYear = iDCardNo.substring(6, 10);// 年份
            String strMonth = iDCardNo.substring(10, 12);// 月份
            String strDay = iDCardNo.substring(12, 14);// 月份
            if (!isStrDate(strYear + "-" + strMonth + "-" + strDay)) {
                return "身份证生日无效";
            }
            GregorianCalendar gc = new GregorianCalendar();
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                return "身份证生日不在有效范围";
            }
            if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
                return "身份证月份无效";
            }
            if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
                return "身份证日期无效";
            }
            //判断最后一位
            int theLastOne = 0;
            for (int i = 0; i < 17; i++) {
                theLastOne = theLastOne + Integer.parseInt(String.valueOf(iDCardNo.charAt(i))) * Integer.parseInt(checkCode[i]);
            }
            int modValue = theLastOne % 11;
            String strVerifyCode = wf[modValue];
            iDCardNo = iDCardNo + strVerifyCode;
            if (idStr.length() == 18 &&!iDCardNo.equalsIgnoreCase(idStr)) {
                return "身份证无效，不是合法的身份证号码";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断字符串是否为数字
     * @param str 字符串
     */
    private static boolean isStrNum(String str) {
        Matcher isNum = NUMBER_PATTERN.matcher(str);
        return isNum.matches();
    }
    /**
     * 判断字符串是否为日期格式
     * @param strDate 字符串
     */
    private static boolean isStrDate(String strDate) {
        Matcher m = DATE_PATTERN.matcher(strDate);
        return m.matches();
    }

    public static void main(String[] args) {

    }

}
