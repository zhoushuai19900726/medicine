/**
 @ Name：layui.regionCheckBox 中国省市复选框
 @ Author：wanmianji
 */

;!function () {
    'use strict';

    var new_element = document.createElement('script');
    new_element.setAttribute('type', 'text/javascript');
    document.body.appendChild(new_element);
}();

var regionList;
layui.define('form', function (exports) {

    'use strict';

    var $ = layui.$
        , form = layui.form
        , MOD_NAME = 'regionCheckBox', ELEM = '.layui-regionContent'
        , regionCheckBox = {
        index: layui.regionCheckBox ? (layui.regionCheckBox.index + 10000) : 0

        , set: function (options) {
            var that = this;
            that.config = $.extend({}, that.config, options);
            return that;
        }

        , on: function (events, callback) {
            return layui.onevent.call(this, MOD_NAME, events, callback);
        }
    }
        , thisIns = function () {
        var that = this
            , options = that.config
            , id = options.id || options.index;

        return {
            reload: function (options) {
                that.config = $.extend({}, that.config, options);
                that.render();
            }
            , val: function (valueArr) {
                setValue(options, valueArr);
            }
            , config: options
        };
    }
        , Class = function (options) {
        var that = this;
        that.index = ++regionCheckBox.index;
        that.config = $.extend({}, that.config, regionCheckBox.config, options);
        that.render();
    };


    Class.prototype.config = {
        value: []
        , width: '550px'
        , border: true
        , change: function (result) {
        }
        , ready: function () {
        }
    };

    Class.prototype.render = function () {
        var that = this
            , options = that.config;

        options.elem = $(options.elem);
        regionList = options.data;
        var id = options.elem.attr('id');

        if (!options.elem.hasClass('layui-form') && options.elem.parents('.layui-form').length == 0) {
            options.elem.addClass('layui-form');
        }
        options.elem.addClass('layui-regionContent');
        options.elem.css('width', options.width);
        if (!options.border) {
            options.elem.css('border', 'none');
        }
        options.elem.attr('lay-filter', 'region-' + id);

        options.elem.html(getCheckBoxs(options));

        //初始值
        setValue(options, options.value);

        options.elem.find('.parent').mouseover(function () {
            $(this).find('.city').show();
        });
        options.elem.find('.parent').mouseout(function () {
            $(this).find('.city').hide();
        });

        form.on('checkbox(regionCheckBox-' + id + ')', function (data) {
            if (data.elem.value == '所有地域') { //选择所有地域
                if (data.elem.checked) {
                    options.elem.find(':checkbox').prop('checked', true);
                } else {
                    options.elem.find(':checkbox').prop('checked', false);
                }
            } else {
                //选择省（不包括直辖市）
                if ($(data.elem).parent().hasClass('parent')) {
                    if (data.elem.checked) {
                        $(data.elem).parent().find('.city :checkbox').prop('checked', true);
                    } else {
                        $(data.elem).parent().find('.city :checkbox').prop('checked', false);
                    }
                }
                //选择城市
                if ($(data.elem).parent().hasClass('city')) {
                    $(data.elem).parents('.parent').attr('name', options.name);
                    if (data.elem.checked) {
                        var is_all = true;
                        $(data.elem).parent().find(':checkbox').each(function (i, item) {
                            if (!item.checked) {
                                is_all = false;
                                return false;
                            }
                        });
                        if (is_all) {
                            $(data.elem).parents('.parent').find(':checkbox:first').prop('checked', true);
                        }
                    } else {
                        $(data.elem).parents('.parent').find(':checkbox:first').prop('checked', false);
                    }
                }
                //选择除所有地域外任意
                if (data.elem.checked) {
                    var is_all = true;
                    options.elem.find(':checkbox[value!="所有地域"]').each(function (i, item) {
                        if (!item.checked) {
                            is_all = false;
                            return false;
                        }
                    });
                    if (is_all) {
                        options.elem.find(':checkbox[value="所有地域"]').prop('checked', true);
                    }
                } else {
                    options.elem.find(':checkbox[value="所有地域"]').prop('checked', false);
                }
            }
            form.render('checkbox', options.elem.attr('lay-filter'));

            renderParentDom(options.elem);
            initName(options);

            options.change(data);
        });

        options.ready();
    }

    function getCheckBoxs(options) {
        var name = options.name,
            id = options.elem.attr('id'),
            skin = 'primary';
        var boxs = '<div class="layui-form-item" style="margin-left:15px;"><input type="checkbox" parent="-1" name="' + name + '" value="所有地域" title="所有地域" lay-skin="' + skin + '" lay-filter="regionCheckBox-' + id + '"></div>';
        for (var area in regionList) {
            boxs += '<div class="layui-form-item" style="margin-bottom:0;">' + '<label class="layui-form-label area">' + area + '：</label><div class="layui-input-block province"><ul>';
            for (var province in regionList[area]) {
                var city_num = regionList[area][province].length;
                boxs += '<li' + (city_num > 0 ? ' class="parent"' : '') + '>' + '<input type="checkbox" parent="0" name="' + name + '" value="' + province.split('-')[0] + '" title="' + province.split('-')[1] + '" lay-skin="' + skin + '" lay-filter="regionCheckBox-' + id + '">';
                if (city_num > 0) {
                    boxs += '<div class="city">';
                    for (var i = 0; i < city_num; i++) {
                        var city = regionList[area][province][i];
                        boxs += '<input type="checkbox" parent="' + province.split('-')[0] + '" parentTitle="' + province.split('-')[1] + '" name="' + name + '" value="' + city.id + '" title="' + city.name + '" lay-skin="' + skin + '" lay-filter="regionCheckBox-' + id + '">';
                    }
                    boxs += '</div>';
                }
                boxs += '</li>';
            }
            boxs += '</div></div>';
        }
        return boxs;
    }

    function setValue(options, valueArr) {
        options.elem.find(':checkbox').prop('checked', false);
        if (valueArr.indexOf('所有地域') > -1) {
            options.elem.find(':checkbox').prop('checked', true);
        } else {
            if (typeof valueArr == 'string') {
                valueArr = valueArr.split(',');
            }
            for (var i = 0; i < valueArr.length; i++) {
                var value = valueArr[i];

                if (value.indexOf('-') < 0) { //省
                    var $elem = options.elem.find(':checkbox[value="' + value + '"]');
                    $elem.prop('checked', true);
                    $elem.parent().find('.city :checkbox').prop('checked', true);
                } else {
                    var $elem1 = options.elem.find(':checkbox[value="' + value.split('-')[0] + '"]');
                    var $elem2 = options.elem.find(':checkbox[value="' + value.split('-')[1] + '"]');
                    $elem1.prop('checked', true);
                    $elem2.prop('checked', true);
                }
            }
        }

        form.render('checkbox', options.elem.attr('lay-filter'));

        renderParentDom(options.elem);
        initName(options);


    }

    function initName(options) {
        var $elem = options.elem;

        $elem.find(':checkbox').attr('name', options.name);

        if ($elem.find(':checkbox[value="所有地域"]').prop('checked')) {
            $elem.find(':checkbox[value!="所有地域"]').removeAttr('name');
        } else {
            $elem.find('.parent').find(':checkbox:first:checked').each(function () {
                $(this).parent().find('.city :checkbox').removeAttr('name');
            });
        }
    }

    function renderParentDom(elem) {
        elem.find('.parent').find(':checkbox:first').each(function () {
            var allCount = $(this).parent().find('.city :checkbox').length;
            var selectedCount = 0;
            $(this).parent().find('.city :checkbox').each(function (i, item) {
                if (item.checked) {
                    selectedCount++;
                }
            });
            if(selectedCount > 0 && selectedCount < allCount){
                $(this).parent().find('.layui-icon:first').removeClass('layui-icon-ok');
                $(this).parent().find('.layui-icon:first').css('border-color', '#5FB878');
                $(this).parent().find('.layui-icon:first').css('background-color', '#5FB878');
            }
        });
    }

    regionCheckBox.render = function (options) {
        var ins = new Class(options);
        return thisIns.call(ins);
    };

    layui.link(layui.cache.base + 'regionCheckBox/regionCheckBox.css?v=1', function () {

    }, 'regionCheckBox');

    exports('regionCheckBox', regionCheckBox);
});
