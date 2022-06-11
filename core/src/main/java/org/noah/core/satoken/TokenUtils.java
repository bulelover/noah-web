package org.noah.core.satoken;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import org.noah.core.utils.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class TokenUtils {

    public static LoginUser getLoginUser(){
        try{
            SaSession session = StpUtil.getTokenSession();
            LoginUser user = BeanUtils.parse(session.getDataMap(), LoginUser.class);
            if(user == null || user.getId() == null){
                return null;
            }
            return user;
        }catch (Exception e){
            return null;
        }
    }

    public static void freshData(LoginUser user){
        try{
            SaSession session = StpUtil.getTokenSession();
            session.refreshDataMap(BeanUtils.toMap(user));
        }catch (Exception ignored){

        }
    }

    public static LoginUser getLoginUser(HttpServletRequest request){
        try{
            SaSession session = StpUtil.getTokenSessionByToken(request.getHeader(StpUtil.getTokenName()));
            LoginUser user = BeanUtils.parse(session.getDataMap(), LoginUser.class);
            if(user == null || user.getId() == null){
                return null;
            }
            return user;
        }catch (Exception e){
            return null;
        }
    }

    public static String getLoginUserId(){
        return Objects.requireNonNull(getLoginUser()).getId();
    }

    public static String getLoginName(){
        return Objects.requireNonNull(getLoginUser()).getLoginName();
    }

}
