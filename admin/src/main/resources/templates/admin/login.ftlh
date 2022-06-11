<!DOCTYPE html>
<html lang="en">
<head>
  <title>Noah</title>
  <#include "../common/public.ftlh"/>
  <#include "../common/meta.ftlh"/>
  <style>
    .login-card {
      width: 500px;
      position: absolute;
      top: 50%;
      left: 50%;
      margin-top: -240px;
      margin-left: -250px;
    }

    .login-card .card-body {
      height: 330px;
    }
  </style>
</head>
<body style="background-color: #3c98ff;">
<div class="card login-card">
  <div class="card-header" style="font-size: 18px;">欢迎登录</div>
  <div class="card-body" style="padding: 20px 40px;">
    <form data-toggle="validate" class="form top" style="font-size: 1.6rem">
      <label class="form-item" style="margin-top: 18px;">
        <span class="iconfont icon-user">用户名</span>
        <input class="large" name="username" data-rule="required" data-label="用户名" maxlength="50">
      </label>
      <label class="form-item" style="margin-top: 14px;">
        <span class="iconfont icon-lock">密码</span>
        <input class="large" name="password" data-rule="required" data-label="密码" maxlength="50">
      </label>
      <label class="form-item" style="text-align: center;margin-top: 12px;">
        <button style="width: 100%" class="primary large" type="button" data-click="login">
          <i data-loading="loading"></i>登 录
        </button>
      </label>
    </form>
  </div>
</div>
</body>
<script src="${ctx}/static/plugins/jquery/jquery-1.11.3.min.js"></script>
<script src="${ctx}/static/js/plugin.js?_v=${version!}"></script>
<script>
  $(function () {
    var m = $('body').initPlugin({
      methods: function () {
        return {
          login: function () {
            if (!m.form.checkAll()) {
              return;
            }
            m.$loading.open('loading');
            $.ajaxAuto('${ctx}/login', {
              type: 'POST',
              data: m.form.$this.serializeMap(),
              callback: function (res, msg) {
                location.href = '${ctx}/admin/index';
              },
              final: function () {
                m.$loading.close('loading');
              }
            })
          }
        }
      }
    });

  })
</script>
</html>