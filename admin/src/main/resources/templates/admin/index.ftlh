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
<#include "layout/header.ftlh">
<div class="main-container">
  <#include "layout/left_menu.ftlh">
  <div id="mainBody" class="main-body">
    <div id="mainTag" class="tag" style="height: 100%;">
      <div class="tag-header">
        <div data-id="index" class="active">首页</div>
      </div>
      <div class="tag-container" style="height:calc(100% - 40px);">
        <div data-id="index" class="active" style="height:100%;">
          <iframe src="${ctx}/admin/sys/menu/list" class="page-iframe"></iframe>
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
