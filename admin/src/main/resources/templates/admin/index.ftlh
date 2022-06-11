<!DOCTYPE html>
<html lang="en">
<head>
  <#include "../common/public.ftlh"/>
  <#include "../common/meta.ftlh"/>
  <title>Noah</title>
  <style>
    .main-menu{
      position: absolute;
      top: 0;
      bottom: 0;
      left: 0;
      width: 22rem;
      border-right: 1px solid #dddddd;
    }
    .main-body{
      position: absolute;
      top: 0;
      bottom: 0;
      left: 22rem;
      right: 0;
    }
  </style>
</head>
<body>
<header>
 <span style="font-size: 2.2rem">欢迎您！</span>
  <a id="logout" href="#">注销</a>
</header>
<div class="main-container">
  <div id="mainMenu" class="main-menu"></div>
  <div id="mainBody" class="main-body">
    <div id="mainTag" class="tag" style="height: 100%;">
      <div class="tag-header">
        <div data-id="index" class="active">首页</div>
      </div>
      <div class="tag-container" style="height:calc(100% - 40px);">
        <div data-id="index" class="active" style="height:100%;">
          <div data-toggle="page" data-url="${ctx}/admin/home|welcome" class="page-iframe"></div>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
<script src="${ctx}/static/plugins/jquery/jquery-1.11.3.min.js"></script>
<script src="${ctx}/static/js/plugin.js?_v=${version!}"></script>
<script>
  $(function () {
    var tag = $('#mainTag').initTag();
    $('#mainBody').initPlugin();

    $.ajaxAuto('${ctx}/sys/menu/getUserMenuList',{
      callback: function (data) {
        $('#frame2').contents().find('body').html(JSON.stringify(data));
        $('#mainMenu').initTree(data, {
          onlyOne: true,
          click: function (data) {
            if(!$.isEmpty(data.url)){
              tag.add(data.code, data.name+'<span class="tag-remove iconfont icon-close"></span>',
                '<div data-toggle="page" data-url="${ctx}/admin/'+data.url.replaceAll('/','|')+'"class="page-iframe"></div>');
              // $('#mainBody').initPage();
              tag.goto(data.code);
            }
          }
        });
      }
    });


    $('#logout').on('click', function () {
      $.ajax('${ctx}/logout', {
        type: 'POST',
        success: function (res) {
          location.href = '${ctx}/admin/login'
        },
        error: function (xhr, error) {
          $('#res').text(error)
        }
      });
    });
  })
</script>
</html>
