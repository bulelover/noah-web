package org.noah.web.login.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.noah.core.annotation.Log;
import org.noah.core.common.BaseController;
import org.noah.core.common.BaseResult;
import org.noah.core.exception.BusinessException;
import org.noah.core.satoken.LoginUser;
import org.noah.core.utils.BeanUtils;
import org.noah.core.utils.CheckUtils;
import org.noah.core.utils.IDUtils;
import org.noah.core.utils.MD5Utils;
import org.noah.web.sys.pojo.user.SysUser;
import org.noah.web.sys.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "登录管理")
@ApiSort(100)
@RestController
public class LoginController extends BaseController {

    private final ISysUserService sysUserService;

    @Autowired
    public LoginController(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @ApiOperation(value = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码"),
            @ApiImplicitParam(name = "rememberMe", value = "记住我 1是 0否")
    })
    @PostMapping("/login")
    @Log(value = "系统登录", type = 2)
    public BaseResult<SaTokenInfo> doLogin(@RequestParam(required = false) String username,
                                           @RequestParam(required = false) String password,
                                           @RequestParam(required = false) String rememberMe) {
        CheckUtils.init().set(username, "用户名").required()
                .set(password, "密码").required()
                .checkError();
        //验证用户合法性
        SysUser user = this.checkUser(username, password);
        StpUtil.login(user.getId(), "1".equals(rememberMe));
        SaSession session = StpUtil.getTokenSession();
        LoginUser loginUser = BeanUtils.parse(user, LoginUser.class);
        session.refreshDataMap(BeanUtils.toMap(loginUser));
        return success(StpUtil.getTokenInfo());
    }

    private SysUser checkUser(String username, String password) {
        SysUser user = this.sysUserService.getByLoginName(username);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        //验证密码
        String pwd = MD5Utils.encodeSalt(password, user.getId());
        if (!user.getLoginPwd().equals(pwd)) {
            throw new BusinessException("密码错误");
        }
        //用户锁定无法登录
        if (!"1".equals(user.getState())) {
            throw new BusinessException("用户已被锁定");
        }
        return user;
    }

    @RequestMapping("/error/unLogin")
    public BaseResult<String> unLogin() {
        return BaseResult.failure("未登录或登录已过期");
    }

    @ApiOperation(value = "注销登录")
    @PostMapping("/logout")
    public BaseResult<String> logout() {
        StpUtil.logout();
        return success();
    }


    @GetMapping("/index")
    public ModelAndView index(ModelAndView model) {
        model.setViewName("index");
        model.addObject("name", "API接口服务启动成功!");
        return model;
    }

    @GetMapping("/readHtml")
    public BaseResult<String> index(String url, Integer sleep) {
        try {
            Thread.sleep(sleep);
            return BaseResult.succeed("获取成功！",getUrlContent(url));
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("请求失败："+url);
        }
        return BaseResult.failure("获取失败");
    }

    private static String getUrlContent(String urlString) throws Exception {    //传入URL
        URL url = new URL(urlString);
        // 打开URL
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setUseCaches(false);
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setConnectTimeout(3000);
        urlConnection.setReadTimeout(3000);
        OutputStream os = urlConnection.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
//        osw.write(sendText);
        osw.flush();
        osw.close();
        os.close();
        // 得到输入流，即获得了网页的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
        String line;
        StringBuilder sb=new StringBuilder();
        while((line=reader.readLine())!=null){
            sb.append(line);
        }
        //返回结果
        return sb.toString();
    }

    @GetMapping("/admin/{pagePath}")
    public ModelAndView router(@PathVariable String pagePath, @RequestParam(required = false) String params) {
        ModelAndView mv = new ModelAndView("admin/" + pagePath.replaceAll("\\|", "/"));
        try {
            boolean not = true;
            if (StringUtils.isBlank(params)) {
                mv.addObject("params", "");
                not = false;
            } else {
                if (params.startsWith("{")) {
                    mv.addObject("params", JSONObject.parseObject(params));
                    not = false;
                }
                if (params.startsWith("[")) {
                    mv.addObject("params", JSONObject.parseArray(params));
                    not = false;
                }
            }
            if (not) {
                mv.addObject("params", params);
            }
        } catch (Exception e) {
            throw new BusinessException("传入参数解析有误（只能接收JSON格式的字符串）");
        }
        return mv;
    }


    @ApiOperation(value = "生成唯一ID")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "num", value = "生成几个（默认1个）")
    })
    @GetMapping("/public/generateId")
    public BaseResult<List<String>> generateId(@RequestParam(value = "num", required = false, defaultValue = "1") Integer num) {
        List<String> ids = new ArrayList<>();
        if (num < 1) {
            return BaseResult.failure("最少1个");
        }
        if (num > 100) {
            return BaseResult.failure("最少100个");
        }
        for(int i=0; i<num; i++){
            ids.add(IDUtils.generate());
        }
        return BaseResult.succeed(ids);
    }
}
