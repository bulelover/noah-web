<!DOCTYPE html>
<html lang="en">
<head>
    <#include "common/public.ftlh"/>
	<meta charset="UTF-8">
	<title>Noah</title>
	<style>
        .box {
            height: 500px;
            width: 49%;
            overflow: auto;
            display: inline-block;
            vertical-align: top;
        }
        table {
            border: 1px solid #dddddd;
	        border-spacing: 0;
            border-collapse: collapse;
        }
        td,th {
            border: 1px solid #dddddd;
	        padding: 6px 8px;
        }
	</style>
</head>
<body>
<div style="padding: 20px">
	<h2>
        ${name} <a target="_self" href="${ctx}/doc.html">点击查看接口文档→</a>
	</h2>
	<#--
	<h3>
        2021年国家统计局地区代码数据抓取
	</h3>
	<div>
&lt;#&ndash;        <button onclick="get2021('34.html', 'http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2021/')">获取安徽省数据</button>&ndash;&gt;
        <button onclick="get2021('51.html', 'http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2021/')">获取四川省数据</button>
&lt;#&ndash;		<button onclick="get2021('34/3401.html', 'http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2021/')">获取合肥市数据</button>&ndash;&gt;
&lt;#&ndash;		<button onclick="view()">查看本次数据</button>&ndash;&gt;
		<button onclick="stopGet()">停止</button>
		<button id="continue" onclick="continueGet()" style="display: none;">继续</button>
		<button onclick="exportExcel()">导出Excel</button>
	</div>
	<div id="table" class="box">
		<table id="tableExcel">
			<thead>
			<tr>
				<th>no</th>
				<th>code</th>
				<th>name</th>
				<th>p_code</th>
				<th>p_name</th>
				<th>s_code</th>
				<th>level</th>
			</tr>
			</thead>
			<tbody id="tbody">

			</tbody>
		</table>
	</div>
	<div id="tips" class="box"></div>
	<div id="container"></div>
	-->
</div>
</body>
<script src="${ctx}/static/plugins/jquery/jquery-1.11.3.min.js"></script>
<script src="${ctx}/static/plugins/jquery/jquery.table2excel.js"></script>
<script>
  var $container = $('#container');
  var $area, areas = [], todoHrefs = [], hi = 0, stop = false, area = {}, no=1;

  function view() {
    localStorage.setItem('areas', JSON.stringify(areas));
    console.log(areas);
  }

  function exportExcel() {
    $("#tableExcel").table2excel({
      exclude: ".noExl", //过滤位置的 css 类名
      filename: "地区导出表" + new Date().getTime(), //文件名称
      name: "地区代码",
      fileext: ".xlsx",
      //是否排除导出图片
      exclude_img: true,
      //是否排除导出超链接
      exclude_links: true,
      //是否排除导出输入框中的内容
      exclude_inputs: true,
      number2Text: true, //数字转为文本
    });
  }

  function stopGet() {
    stop = true;
  }

  function continueGet() {
    $('#continue').hide();
    hi--;
    if (todoHrefs.length > hi) {
      get2021(todoHrefs[hi].path, todoHrefs[hi].prefix);
      hi++;
    }
  }

  function toArea($item, codeIndex, nameIndex) {
    var code = $item.find('td:eq(' + codeIndex + ') a').text();
    var name = $item.find('td:eq(' + nameIndex + ') a').text();
    if (!code) {
      code = $item.find('td:eq(' + codeIndex + ')').text();
      name = $item.find('td:eq(' + nameIndex + ')').text();
    }
    var p_code = '', s_code = '', level = 9;
    s_code = code.replace('0000000000', '').replace('00000000', '').replace('000000', '');
    if (s_code.length === 12 && s_code.substring(9, 12) === '000') {
      s_code = code.substring(0, 9);
    }
    switch (s_code.length) {
      case 2:
        p_code = '1';
        level = 1;
        break;
      case 4:
        p_code = code.substring(0, 2);
        level = 2;
        break;
      case 6:
        p_code = code.substring(0, 4);
        level = 3;
        break;
      case 9:
        p_code = code.substring(0, 6);
        level = 4;
        break;
      case 12:
        p_code = code.substring(0, 9);
        level = 5;
        break;
      default:
        s_code = code;
    }
    if(s_code.length < 12){
      area[s_code] = name;
    }
    return {
      code: code,
      name: name,
      p_code: p_code,
      p_name: area[p_code]?area[p_code]:'',
      s_code: s_code,
      level: level
    }
  }

  var pushAreas = function ($cont, selector, codeIndex, nameIndex, tempUp) {
    var htm = '', obj;
    $area.find(selector).each(function (index, item) {
      var href = $(item).find('td:eq(0) a').attr('href');
      if (href) {
        todoHrefs.push({
          path: href,
          prefix: tempUp
        });
      }
      obj = toArea($(item), codeIndex, nameIndex);
      htm += '<tr>' +
          '<td>' + (no++) + '</td>' +
          '<td>' + obj.code + '</td>' +
          '<td>' + obj.name + '</td>' +
          '<td>' + obj.p_code + '</td>' +
          '<td>' + obj.p_name + '</td>' +
          '<td>' + obj.s_code + '</td>' +
          '<td>' + obj.level + '</td>' +
          '</tr>'
      areas.push(obj);
    });
    $('#tbody').append(htm);
    setTimeout(function () {
      $('#table').scrollTop(99999999);
    }, 500);
    $cont.remove();
    if (todoHrefs[hi]) {
      get2021(todoHrefs[hi].path, todoHrefs[hi].prefix);
      hi++;
    }else{
      $('#table').append('抓取完毕！共'+areas.length+'条');
    }
  }

  function get2021(path, urlPrefix) {
    if (stop) {
      return;
    }
    var tempUp;
    //readHtml注意控制sleep参数（延迟毫秒数）防止被屏蔽
    $.get('${ctx}/readHtml?sleep=500&url=' + urlPrefix + path, function (res, status) {
      if (status === 'success' && res && res.success) {
        $('#tips').append('<p style="color: #00a900">['+ hi +'/'+ todoHrefs.length +']获取成功：' + urlPrefix + path+'</p>');
        setTimeout(function () {
          $('#tips').scrollTop(99999999);
        }, 500)
        var code;
        var id = 'ID' + new Date().getTime();
        $container.append('<div id="' + id + '">' + res.data + '</div>');
        var $cont = $('#' + id);
        $area = $cont.find('.citytable');
        if ($area.length) {
          tempUp = urlPrefix;
          pushAreas($cont, '.citytr', 0, 1, tempUp);
          return;
        }
        $area = $cont.find('.countytable');
        if ($area.length) {
          code = $area.find('.countytr:eq(0) td:eq(0) a').text();
          if (!code) {
            code = $area.find('.countytr:eq(0) td:eq(0)').text();
          }
          tempUp = urlPrefix + code.substring(0, 2) + "/";
          pushAreas($cont, '.countytr', 0, 1, tempUp);
          return;
        }
        $area = $cont.find('.towntable');
        if ($area.length) {
          code = $area.find('.towntr:eq(0) td:eq(0) a').text();
          if (!code) {
            code = $area.find('.towntr:eq(0) td:eq(0)').text();
          }
          tempUp = urlPrefix + code.substring(2, 4) + "/";
          pushAreas($cont, '.towntr', 0, 1, tempUp);
          return;
        }
        $area = $cont.find('.villagetable');
        if ($area.length) {
          pushAreas($cont, '.villagetr', 0, 2);
        }

      } else {
        $('#tips').append('<p style="color: red;">获取失败：' + urlPrefix + path +'，自动重试</p>');
        setTimeout(function () {
          $('#tips').scrollTop(99999999);
          $('#continue').show();
          $('#continue').click();
        }, 500)
      }
    });
  }
</script>
</html>
