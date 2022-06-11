/**
 * 基于JQuery-1.11.3
 * 使用前请保证引入了jqueryJS库
 */
/**
 * 判断字符串是否为空，null或undefined
 * @param val 字符串
 * @returns {boolean}
 */
$.isEmpty = function (val) {
  val = $.trim(val);
  return val === '' || val === undefined || val === null;
};
/**
 * 判断对象是否为null或undefined
 * @param val 对象
 * @returns {boolean}
 */
$.isNull = function (val) {
  return val === undefined || val === null;
};
/**
 * 获取随机的ID标识符
 * @returns {string}
 */
$.getRandomId = function () {
  return 'N' + ('' + Math.random()).substring(2) + ('' + new Date().getTime()).substr(0, 10);
};

$.alert = function (type, info, opts) {
  var defaults = {
    isPerm: false, //是否永久不消失
    methods: function () {
      return {};
    }
  };
  opts = $.extend({}, defaults, opts);
  if ($.isNull(info)) {
    info = type;
    type = 'info';
  }
  var color = '#909399';
  var bgColor = '#f1f1f3';
  var bdColor = '#d8d8da';
  var time = 2000;
  var icon = 'icon-info-circle-fill';
  if (type === 'success') {
    color = '#67C23A';
    bgColor = '#e1f3d8';
    bdColor = '#cbdcc3';
    icon = 'icon-check-circle-fill';
  }
  if (type === 'warning') {
    color = '#E6A23C';
    bgColor = '#faecd8';
    bdColor = '#eddfcc';
    time = 5000;
    icon = 'icon-warning-circle-fill';
  }
  if (type === 'danger') {
    color = '#F56C6C';
    bgColor = '#ffedea';
    bdColor = '#eed5d5';
    time = 5000;
    icon = 'icon-close-circle-fill';
  }
  var id = $.getRandomId();
  var $msgBox = $(top.document.body).find('#alertMsgBox');
  if (!$msgBox || $msgBox.length === 0) {
    $(top.document.body).append('<div id="alertMsgBox" class="alert-msg-box"></div>');
    $msgBox = $(top.document.body).find('#alertMsgBox');
  }
  var tpl = '<div class="alert" id="' + id + '" style="border:1px solid ' + bdColor + ';background-color: ' + bgColor + '; color: ' + color + ';">' +
    '<i class="iconfont ' + icon + '"></i>' + info + '</div>';
  $msgBox.append(tpl);
  var $tips = $msgBox.find('#' + id);
  $tips.animate({marginTop: '10px'}, 'fast');
  $tips.on('click', function () {
    $tips.fadeOut(function () {
      $tips.remove();
    });
  });
  if (!opts.isPerm) {
    setTimeout(function () {
      $tips.trigger('click');
    }, time);
  }
};

/**
 * 获取表单格式化后的对象
 * @returns {{}}
 */
$.fn.serializeMap = function () {
  var $this = $(this);
  var arr = $this.serializeArray();
  if (!arr || arr.length === 0) {
    return {};
  }
  var map = {};
  for (var i = 0; i < arr.length; i++) {
    map[arr[i].name] = arr[i].value;
  }
  return map;
};

/* 增强Ajax */
$.ajaxAuto = function (url, opts) {
  if (!opts) {
    opts = {};
  }
  $.ajax(url, $.extend({}, opts, {
    type: opts.type || 'GET',
    success: function (data) {
      if (data && data.success) {
        if (typeof opts.callback === 'function') {
          opts.callback(data.data, data.msg);
        }
      } else {
        $.alert('danger', data.msg);
        if (typeof opts.failure === 'function') {
          opts.failure(data.msg);
        }
      }
    },
    error: function (xhr, error) {
      $.alert('danger', error);
      if (typeof opts.error === 'function') {
        opts.error(xhr, error);
      }
    },
    complete: function () {
      if (typeof opts.final === 'function') {
        opts.final();
      }
    }
  }));
};

/* 标签页插件 */
$.fn.initTag = function (opts) {
  if (!opts) {
    // change: function(data) 当标签页发送改变事件返回 data为点击的数据信息
    opts = {};
  }
  var $this = $(this);
  var $header = $this.find('.tag-header');
  var $container = $this.find('.tag-container');
  //控制标签切换
  $header.on('click', '>div', function () {
    $header.find('>div.active').removeClass('active');
    $container.find('>div.active').removeClass('active');
    $(this).addClass('active');
    $container.find('[data-id="' + $(this).attr('data-id') + '"]').addClass('active');
    if (opts.change && typeof opts.change === 'function') {
      opts.change();
    }
  });
  $header.on('click', '.tag-remove', function () {
    var id = $(this).parent().attr('data-id');
    $header.find('[data-id="' + id + '"]').remove();
    $container.find('[data-id="' + id + '"]').remove();
    //触发最后一个标签打开
    setTimeout(function () {
      $header.find('>div:last-child').trigger('click');
    });
  });
  return {
    $this: $this,
    $header: $header,
    $container: $container,
    add: function (id, header, html) {
      var $tab = $header.find('[data-id="' + id + '"]');
      if ($tab && $tab.length > 0) {
        this.goto(id);
        return;
      }
      $header.append('<div data-id="' + id + '">' + header + '</div>');
      $container.append('<div data-id="' + id + '">' + html + '</div>');
      $container.find('[data-toggle="page"]').initPage();
    },
    remove: function (id) {
      $header.find('[data-id="' + id + '"]').remove();
      $container.find('[data-id="' + id + '"]').remove();
    },
    goto: function(id) {
      $header.find('[data-id="' + id + '"]').trigger('click');
    }
  }
};

$.fn.initPage = function () {
  var $this = $(this);
  var data = $this.data();
  if($.isEmpty(data.url)){
    return;
  }
  $.ajax(data.url, {
    success: function (res) {
      $this.html(res)
    },
    error: function (xhr, error) {
      $this.html(error)
    }
  });
  $this.removeAttr('data-toggle');
  $this.removeAttr('data-url');
};

/* 树插件 */
$.fn.initTree = function (data, opts) {
  if (!opts) {
    // render: function(data) //返回展示的html  data:单条数据的信息
    // click: function(data) 点击事件返回 data为点击的数据信息
    opts = {
      onlyOne: false
    };
  }
  if ($.isArray(data)) {
    var root = [], temp = {}, map = {};
    /* 处理数据 */
    //循环记录
    for (var i = 0; i < data.length; i++) {
      var item = data[i], pId = item['parentId'];
      map[item.id] = item;
      if ($.isEmpty(pId)) {
        root.push(item);
      } else {
        if (!(temp[pId] && temp[pId].length > 0)) {
          temp[pId] = [];
        }
        temp[pId].push(item);
      }
    }
    //设置父子关系
    var assignData = function (root, level) {
      for (var j = 0; j < root.length; j++) {
        var item = root[j], pId = item['id'];
        if (temp[pId] && temp[pId].length > 0) {
          root[j].children = temp[pId];
          for (var m = 0; m < root[j].children.length; m++) {
            root[j].children[m].level = level + 1;
          }
          root[j].level = level;
          assignData(root[j].children, level + 1);
        }
      }
    };
    assignData(root, 1);
    /* 数据处理完毕，组装DOM */
    var $this = $(this);
    var tpl = '';
    var left = 1.8;
    var assignDom = function (menus) {
      for (var i = 0; i < menus.length; i++) {
        var item = menus[i];
        var hasChild = false;
        if (item.children && item.children.length > 0) {
          hasChild = true;
        }
        var diyDom = '<span>' + item.name + '</span>';
        if (opts.render && typeof opts.render === 'function') {
          diyDom = opts.render(item);
        }
        tpl += '<div class="tree-item" data-id="' + item.id + '" style="padding-left: ' + (left * item.level) + 'rem;">' + diyDom
          // 目录右侧图标
          + (hasChild ? '<i class="tree-item-right iconfont icon-right"></i>' : '')
          + '</div>';
        if (hasChild) {
          tpl += '<div class="tree-item-children">';
          assignDom(item.children);
          tpl += '</div>';
        }
      }
    };
    assignDom(root);
    $this.html(tpl);

    $this.on('click', '.tree-item', function () {
      var $that = $(this);
      var $next = $that.next();
      if (opts.click && typeof opts.click === 'function') {
        opts.click(map[$that.attr('data-id')]);
      }
      if ($next.is('.tree-item-children')) {
        if (!$next.is(':hidden')) {
          $next.slideToggle('fast');
          return;
        }
        if (opts.onlyOne) {
          $that.parent().find('>.tree-item-children').slideUp('fast');
        }
        $next.slideToggle('fast');
      }
    });
  }
};

/* 加载层 */
$.fn.initLoading = function () {
  var $this = $(this);
  var loadingObj = {};
  $this.find('[data-loading]').each(function () {
    var $item = $(this);
    var flag = $item.data('loading');
    loadingObj[flag] = $item;
  });
  return {
    isOpen: function (flag) {
      var $item = loadingObj[flag];
      return $item.hasClass('icon-loading');
    },
    open: function (flag) {
      var $item = loadingObj[flag];
      if ($item.parent().is('button')) {
        $item.parent().attr('disabled', true);
      }
      $item.addClass('icon-spin iconfont icon-loading');
    },
    close: function (flag) {
      var $item = loadingObj[flag];
      if ($item.parent().is('button')) {
        $item.parent().attr('disabled', false);
      }
      $item.removeClass('icon-spin iconfont icon-loading');
    }
  }
};

/* 表单验证 */
$.fn.initValidate = function () {
  var $this = $(this);
  var res = true;
  $this.find('[data-rule]').each(function () {
    var $item = $(this);
    var rule = $item.data('rule');
    var label = $item.attr('data-label');
    label = $.isEmpty(label) ? '' : label;
    var rules = rule.split(",");
    if ($.isEmpty(rules)) {
      return true;
    }
    var $par = $item.parent();
    var method = {
      showError: function (error) {
        var $error = $par.find('span.form-error');
        if ($error && $error.length > 0) {
          $error.text(error);
          $error.fadeIn(200);
        } else {
          $par.append('<span class="form-error" style="display: none;">' + error + '</span>');
          $error = $par.find('span.form-error');
          $error.css({
            left: $item.position().left,
            top: $item.position().top + $item.height() + 4
          });
          $error.fadeIn(200);
        }
      },
      hideError: function () {
        var $error = $par.find('span.form-error');
        if ($error && $error.length > 0) {
          $error.remove();
        }
      },
      checkError: function () {
        method.hideError();
        var val = $item.val();
        for (var i = 0; i < rules.length; i++) {
          if (rules.indexOf('range')) {
            //do something ...
          }
          switch (rules[i]) {
            case "required":
              if ($.isEmpty(val)) {
                method.showError(label + '不能为空');
                res = false;
                return;
              }
              method.hideError();
              break;
            case "idCard":
              break;
            case "phone":
              break;
            case "mobile":
              break;
            case "integer":
              break;
            case "number":
              break;
            default:
          }
        }
      }
    };

    var event = 'change';
    if ($item.is('input') || $item.is('textarea')) {
      event = 'blur';
    }
    //自动触发校验
    $item.on(event, function () {
      method.checkError();
    });
    //绑定自定义事件
    $item.on('checkError', function () {
      method.checkError();
    });
  });
  return {
    $this: $this,
    checkAll: function () {
      res = true;
      $this.find('[data-rule]').each(function () {
        $(this).trigger('checkError');
      });
      $this.find('span.form-error:eq(0)').parent().find('[data-rule]').trigger('focus');
      return res;
    },
    check: function (name) {
      res = true;
      $this.find('[name="' + name + '"]').trigger('checkError');
      return res;
    }
  };
};

/* 事件绑定 */
/**
 * jquery 动态事件绑定  且DOM上只要给出data-id属性 则会返回相应的JQ对象以供方便使用，若无则不返回
 * @param events 要绑定的事件默认绑定['click','change','blur','focus']函数，如果需要绑定其他或者自定义函数可传入此参数 例如: ['mouseover', 'keydown', 'keyup']
 * @param getOptsFun 绑定的函数要求返回一个方法对象例如：return {method1: function(){}, method2: function(){}}
 */
$.fn.initBindEvent = function (events, getOptsFun) {
  var $this = $(this);
  if (events == null) {
    events = [];
  }
  //只有一个参数
  if (!getOptsFun) {
    getOptsFun = events;
    events = [];
  }
  if (typeof getOptsFun !== 'function') {
    console.warn("事件绑定失败，请传入函数式参数返回");
    return;
  }
  events.push('click');
  events.push('change');
  events.push('blur');
  events.push('focus');
  var opts = getOptsFun();
  var $objs = {};
  //点击事件
  for (var i = 0; i < events.length; i++) {
    $this.find('[data-' + events[i] + ']').each(function () {
      var $item = $(this);
      var event = $item.data(events[i]);
      $item.on(events[i], function () {
        if (opts && opts[event] && typeof opts[event] === "function") {
          opts[event]($this.data('params'));
          if ($item.data('id')) {
            $objs[$item.data('id')] = $item;
          }
        } else {
          console.warn("绑定的时间[" + event + "]不存在或不是函数");
        }
      })
    });
  }
  return $objs;
};


//helper 一次初始化所有组件
$.fn.initPlugin = function (params) {
  var $this = $(this);
  var validates = [];
  var defaults = {
    //事件绑定第一个参数
    events: null,
    //事件绑定第二个参数
    methods: function () {
      return {};
    }
  };
  var opts = $.extend({}, defaults, params);

  //初始化校验
  $this.find('[data-toggle="validate"]').each(function () {
    var $item = $(this);
    var res = $item.initValidate();
    validates.push(res);
  });

  $this.find('[data-toggle="page"]').each(function () {
    var $item = $(this);
    $item.initPage();
  });

  //绑定loading，可自由开启
  var $loading = $this.initLoading();

  //绑定事件
  var $event = $this.initBindEvent(opts.events, opts.methods);
  return {
    //返回多个验证集合，用于区别多个form
    forms: validates,
    //返回第一个的验证（适用于大多数的单form页面）
    form: validates.length > 0 ? validates[0] : {},
    //返回绑定事件的jq对象
    $event: $event,
    //返回loading对象可以自由操作loading
    $loading: $loading
  }
};