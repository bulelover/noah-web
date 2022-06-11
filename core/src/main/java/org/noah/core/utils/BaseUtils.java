package org.noah.core.utils;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import org.noah.core.pojo.SimpleTreeNode;
import org.noah.core.pojo.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class BaseUtils {

    private final static Logger logger = LoggerFactory.getLogger(BeanUtils.class);

    public static HttpServletRequest getRequest(){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        return sra == null ? null : sra.getRequest();
    }

    public static String getRemoteHost(){
        return getRemoteHost(getRequest());
    }
    /**
     * 获得访问地ip
     * @return ip地址
     */
    public static String getRemoteHost(HttpServletRequest request){
        if(request == null){
            return "--";
        }
        // 这个一般是Nginx反向代理设置的参数
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多IP的情况（只取第一个IP）
        if (ip != null && ip.contains(",")) {
            String[] ipArray = ip.split(",");
            ip = ipArray[0];
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ?"127.0.0.1":ip;
    }

    public static String  getCurrentIp() {
        Enumeration<NetworkInterface> nis;
        String ip = null;
        try {
            nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    InetAddress ia = ias.nextElement();
                    if (ia instanceof Inet4Address && !ia.getHostAddress().equals("127.0.0.1")) {
                        ip = ia.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            return null;
        }
        return ip;
    }

    /**
     * 获取树列表工具类（传输列表自动转换） 只保留id和label（节省流量）
     * @param list 常规列表数据
     * @param rootValue 根节点值
     * @param <T> 实体类型
     * @return 结果集合
     */
    public static <T extends TreeNode> List<SimpleTreeNode> getSimpleTreeList(List<T> list, String rootValue){
        if(list == null || list.size() == 0){
            return null;
        }
        if(rootValue == null){
            rootValue = "";
        }
        List<SimpleTreeNode> treeNodes = new ArrayList<>();
        List<T> surplus = new ArrayList<>();
        try {
            for(T t : list){
                if(rootValue.equals(t.getNodeParentId())){
                    SimpleTreeNode node = new SimpleTreeNode();
                    node.setId(t.getId());
                    node.setLabel(t.getNodeLabel());
                    treeNodes.add(node);
                }else {
                    surplus.add(t);
                }
            }
            if(treeNodes.size() == 0){
                logger.error("转换树结构失败，未找到根节点数据");
                return null;
            }
            combineSimpleTreeData(treeNodes, surplus);
        }catch ( InstantiationException | IllegalAccessException ignored){
            logger.error("转换树结构失败，泛型实例化异常");
            return null;
        }
        return treeNodes;
    }

    private static <T extends TreeNode> void combineSimpleTreeData(List<SimpleTreeNode> treeNodes, List<T> surplus) throws InstantiationException, IllegalAccessException {
        if(surplus.size() == 0){
            return;
        }
        for(SimpleTreeNode root : treeNodes){
            List<SimpleTreeNode> children = new ArrayList<>();
            for(T t : surplus){
                if(root.getId().equals(t.getNodeParentId())){
                    SimpleTreeNode node = new SimpleTreeNode();
                    node.setId(t.getId());
                    node.setLabel(t.getNodeLabel());
                    children.add(node);
                }
                root.setChildren(children);
                combineSimpleTreeData(children, surplus);
            }
        }
    }

    /**
     * 获取树列表工具类（传输列表自动转换）
     * @param list 常规列表数据
     * @param rootValue 根节点值
     * @param <T> 实体类型
     * @return 结果集合
     */
    public static <T extends TreeNode> List<TreeNode> getTreeList(List<T> list, String rootValue){
        if(list == null || list.size() == 0){
            return null;
        }
        if(rootValue == null){
            rootValue = "";
        }
        List<TreeNode> treeNodes = new ArrayList<>();
        List<T> surplus = new ArrayList<>();
        try {
            for(T t : list){
                if(rootValue.equals(t.getNodeParentId())){
                    TreeNode node = list.get(0).getClass().newInstance();
                    node.setId(t.getId());
                    node.setLabel(t.getNodeLabel());
                    BeanUtils.copyProperties(t, node);
                    treeNodes.add(node);
                }else {
                    surplus.add(t);
                }
            }
            if(treeNodes.size() == 0){
                logger.error("转换树结构失败，未找到根节点数据");
                return null;
            }
            combineTreeData(treeNodes, surplus);
        }catch ( InstantiationException | IllegalAccessException ignored){
            logger.error("转换树结构失败，泛型实例化异常");
            return null;
        }
        return treeNodes;
    }

    private static <T extends TreeNode> void combineTreeData(List<TreeNode> treeNodes, List<T> surplus) throws InstantiationException, IllegalAccessException {
        if(surplus.size() == 0){
            return;
        }
        for(TreeNode root : treeNodes){
            List<TreeNode> children = new ArrayList<>();
            for(T t : surplus){
                if(root.getId().equals(t.getNodeParentId())){
                    TreeNode node = surplus.get(0).getClass().newInstance();
                    node.setId(t.getId());
                    node.setLabel(t.getNodeLabel());
                    BeanUtils.copyProperties(t, node);
                    children.add(node);
                }
                root.setChildren(children);
                combineTreeData(children,surplus);
            }
        }
    }

    /**
     * 功能：驼峰命名转下划线命名
     * 小写和大写紧挨一起的地方,加上分隔符,然后全部转小写
     */
    public static String camel2under(String c)
    {
        String separator = "_";
        c = c.replaceAll("([a-z])([A-Z])", "$1"+separator+"$2").toLowerCase();
        return c;
    }
    /**
     * 功能：下划线命名转驼峰命名
     * 将下划线替换为空格,将字符串根据空格分割成数组,再将每个单词首字母大写
     * @param s
     * @return
     */
    private static String under2camel(String s)
    {
        String separator = "_";
        String under="";
        s = s.toLowerCase().replace(separator, " ");
        String sarr[]=s.split(" ");
        for(int i=0;i<sarr.length;i++)
        {
            String w=sarr[i].substring(0,1).toUpperCase()+sarr[i].substring(1);
            under +=w;
        }
        return under;
    }


    /**
     * 获取发起请求的浏览器名称
     */
    public static String getBrowserName(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(header);
        Browser browser = userAgent.getBrowser();
        return browser.getName();
    }

    /**
     * 获取发起请求的浏览器版本号
     */
    public static String getBrowserVersion(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(header);
        // 获取浏览器信息
        Browser browser = userAgent.getBrowser();
        // 获取浏览器版本号
        Version version = browser.getVersion(header);
        return version.getVersion();
    }

    /**
     * 获取发起请求的操作系统名称
     */
    public static String getOsName(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(header);
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        return operatingSystem.getName();
    }
}
