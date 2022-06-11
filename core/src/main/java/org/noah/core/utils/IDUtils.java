package org.noah.core.utils;

import cn.hutool.system.oshi.OshiUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.noah.core.cache.SysCache;
import org.noah.core.config.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SouthXia
 */
@Component
public class IDUtils {
    private static final String LENGTH = "%04d";
    private static final Map<String, Integer> uuid = new ConcurrentHashMap<>();
    private static String SERVER_ID = "";
    private static String SERVER_FLAG = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

    private static ConfigProperties configProperties;

    @Autowired
    public void setAppProperties(ConfigProperties configProperties) {
        IDUtils.configProperties = configProperties;
    }

    @PostConstruct
    public void init() {
        if(StringUtils.isBlank(configProperties.getServerId()) || !"auto".equalsIgnoreCase(configProperties.getServerId())){
            SERVER_ID = configProperties.getServerId();
            System.out.println("==============================================================================================");
            System.out.println("== 获取[SERVER_ID]标识为"+SERVER_ID+"，注意检查所有集群环境下此标识的唯一性，以防生成重复的ID ============");
            System.out.println("==============================================================================================");
            return;
        }
        String flag = SysCache.get("config:server-flag:"+ getServerDeviceId());
        if(StringUtils.isBlank(flag)){
            //没有服务器标识时自动获取
            String ids = SysCache.get("config:server-flags");
            if(ids == null){
                ids = "";
            }
            String serverFlag = SERVER_FLAG.replace(ids, "").substring(0,1);
            SysCache.put("config:server-flag:"+ getServerDeviceId(), serverFlag);
            SysCache.put("config:server-id:"+ getServerDeviceId(), "01");
            SysCache.put("config:server-flags"+ getServerDeviceId(), ids+serverFlag);
            SERVER_ID = serverFlag+"01";
        }else{
            String id = SysCache.get("config:server-id:"+ getServerDeviceId());
            int i = Integer.parseInt(id);
            i++;
            if(i>99){
                i = 1;
            }
            id = i<10?"0"+i: ""+i;
            SERVER_ID = flag + id;
            SysCache.put("config:server-id:"+ getServerDeviceId(), id);
        }
        System.out.println("==============================================================================================");
        System.out.println("== 获取[SERVER_ID]标识为"+SERVER_ID+"，注意检查所有集群环境下此标识的唯一性，以防生成重复的ID ============");
        System.out.println("==============================================================================================");
    }

    //定时清除key
    static {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                removeInValidKeys();
            }
            //每隔5分钟启动一次
        }, 60*5000, 60*5000);
    }


    /**
     * 定时清除1分钟之前的数据
     */
    private static void removeInValidKeys(){
        uuid.keySet().forEach(key->{
            if(uuid.get(key) < (System.currentTimeMillis()-60000)/1000){
                uuid.remove(key);
            }
        });
        System.gc();
    }

    /**
     * 获取唯一ID值，不同的服务容器 需要配置不同的 config.server-id （服务启动标识两位字母+数字）以保证绝对的唯一
     * 需要运行环境
     * @return id
     */
    public static synchronized String generate() {
        long curr = System.currentTimeMillis()/1000;
        String module = ""+curr;
        Integer id = uuid.get(module);
        if (id == null) {
            id = 0;
        } else {
            id++;
        }
        uuid.put(module, id);
        return moveInsert(moveRight(module), SERVER_ID +(id>9999?id:String.format(LENGTH,id)));
    }
    private static String moveRight(String str){
        for(int i = 0; i< 3; i++){
            str = str.substring(1) + str.charAt(0);
        }
        return str;
    }
    private static String moveInsert(String str, String ins){
        String[] insArr = ins.split("");
        for(String s : insArr){
            int rd=(int)(Math.random()*(str.length()));
            str = str.substring(0, rd)+ s + str.substring(rd);
        }
        return str;
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String getServerDeviceId() {
        return OshiUtil.getHardware().getProcessor().getProcessorIdentifier().getProcessorID()+"-"+
                JSON.toJSONString(OshiUtil.getSystem().getSerialNumber())+"-"+OshiUtil.getSystem().getBaseboard().getSerialNumber();
    }

    public static void main(String[] args) {
//        System.out.println("cpu-info:"+JSON.toJSONString(OshiUtil.getCpuInfo()));
        //mac:"b0-25-aa-21-fd-c9"
        //hard:"sCWqIf3J"
        System.out.println("cpu:"+JSON.toJSONString(OshiUtil.getHardware().getProcessor().getProcessorIdentifier().getProcessorID()));
        System.out.println("hard-serial:"+JSON.toJSONString(OshiUtil.getSystem().getSerialNumber())); //417056H31750480041
        System.out.println("board-serial:"+JSON.toJSONString(OshiUtil.getSystem().getBaseboard())); //417056H31750480041
        System.out.println(SERVER_ID);
        System.out.println(generate());
        System.out.println(generateUUID());
    }

}
