/*工具类*/
var CoreUtil = (function () {
    var coreUtil = {};

    /*GET*/
    coreUtil.sendGet = function (url, params, ft) {
        this.sendAJAX(url, params, ft, "GET")
    }

    /*POST*/
    coreUtil.sendPost = function (url, params, ft) {
        this.sendAJAX(url, JSON.stringify(params), ft, "POST")
    }
    /*PUT*/
    coreUtil.sendPut = function (url, params, ft) {
        this.sendAJAX(url, JSON.stringify(params), ft, "PUT")
    }
    /*DELETE*/
    coreUtil.sendDelete = function (url, params, ft) {
        this.sendAJAX(url, JSON.stringify(params), ft, "DELETE")
    }


    /*ajax*/
    coreUtil.sendAJAX = function (url, params, ft, method) {
        var loadIndex = top.layer.load(0, {shade: false});
        $.ajax({
            url: url,
            cache: false,
            async: true,
            data: params,
            type: method,
            contentType: 'application/json; charset=UTF-8',
            dataType: "json",
            beforeSend: function (request) {
                request.setRequestHeader("authorization", CoreUtil.getData("access_token"));
            },
            success: function (res) {
                top.layer.close(loadIndex);
                if (res.code == 0) {
                    if (ft != null && ft != undefined) {
                        ft(res);
                    }
                } else if (res.code == 401001) { //凭证过期重新登录
                    layer.msg("凭证过期请重新登录", {time: 2000}, function () {
                        top.window.location.href = "/index/login"
                    })
                } else if (res.code == 401008) { //凭证过期重新登录
                    layer.msg("抱歉！您暂无权限", {time: 2000})
                } else {
                    layer.msg(res.msg);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                top.layer.close(loadIndex);
                if (XMLHttpRequest.status == 404) {
                    top.window.location.href = "/index/404";
                } else {
                    layer.msg("服务器好像出了点问题！请稍后试试");
                }
            }
        })
    }


    /*存入本地缓存*/
    coreUtil.setData = function (key, value) {
        layui.data('LocalData', {
            key: key,
            value: value
        })
    };
    /*从本地缓存拿数据*/
    coreUtil.getData = function (key) {
        var localData = layui.data('LocalData');
        return localData[key];
    };

    //判断字符是否为空的方法
    coreUtil.isEmpty = function (obj) {
        if (typeof obj == "undefined" || obj == null || obj == "") {
            return true;
        } else {
            return false;
        }
    }

    //字典数据回显
    coreUtil.selectDictLabel = function (datas, value) {
        datas = JSON.parse(datas);
        var label = "";
        $.each(datas, function (index, dict) {
            if (dict.value == ('' + value)) {
                label = dict.label;
                return false;
            }
        });
        //匹配不到，返回未知
        if (CoreUtil.isEmpty(label)) {
            return "未知";
        }
        return label;
    }


    /*GET*/
    coreUtil.sendSyncGet = function (url, params, ft) {
        this.sendSyncAJAX(url, params, ft, "GET")
    }

    /*POST*/
    coreUtil.sendSyncPost = function (url, params, ft) {
        this.sendSyncAJAX(url, JSON.stringify(params), ft, "POST")
    }
    /*PUT*/
    coreUtil.sendSyncPut = function (url, params, ft) {
        this.sendSyncAJAX(url, JSON.stringify(params), ft, "PUT")
    }
    /*DELETE*/
    coreUtil.sendSyncDelete = function (url, params, ft) {
        this.sendSyncAJAX(url, JSON.stringify(params), ft, "DELETE")
    }


    /*ajax*/
    coreUtil.sendSyncAJAX = function (url, params, ft, method) {
        var loadIndex = top.layer.load(0, {shade: false});
        $.ajax({
            url: url,
            cache: false,
            async: false,
            data: params,
            type: method,
            contentType: 'application/json; charset=UTF-8',
            dataType: "json",
            beforeSend: function (request) {
                request.setRequestHeader("authorization", CoreUtil.getData("access_token"));
            },
            success: function (res) {
                top.layer.close(loadIndex);
                if (res.code == 0) {
                    if (ft != null && ft != undefined) {
                        ft(res);
                    }
                } else if (res.code == 401001) { //凭证过期重新登录
                    layer.msg("凭证过期请重新登录", {time: 2000}, function () {
                        top.window.location.href = "/index/login"
                    })
                } else if (res.code == 401008) { //凭证过期重新登录
                    layer.msg("抱歉！您暂无权限", {time: 2000})
                } else {
                    layer.msg(res.msg);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                top.layer.close(loadIndex);
                if (XMLHttpRequest.status == 404) {
                    top.window.location.href = "/index/404";
                } else {
                    layer.msg("服务器好像出了点问题！请稍后试试");
                }
            }
        })
    }


    coreUtil.calcDescartes = function (array) {
        if (array.length < 2) return array[0] || [];
        return [].reduce.call(array, function (col, set) {
            var res = [];
            col.forEach(function (c) {
                set.forEach(function (s) {
                    var t = [].concat(Array.isArray(c) ? c : [c]);
                    t.push(s);
                    res.push(t);
                })
            });
            return res;
        });
    }

    coreUtil.subSet = function (arr1, arr2) {
        var set1 = new Set(arr1);
        var set2 = new Set(arr2);
        var subset = [];
        for (let item of set1) {
            if (!set2.has(item)) {
                subset.push(item);
            }
        }
        return subset;
    };

    // 函数参数必须是字符串，因为二代身份证号码是十八位，而在javascript中，十八位的数值会超出计算范围，造成不精确的结果，导致最后两位和计算的值不一致，从而该函数出现错误。
// 详情查看javascript的数值范围
    coreUtil.checkIDCard = function (idcode) {
        // 加权因子
        var weight_factor = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
        // 校验码
        var check_code = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];

        var code = idcode + "";
        var last = idcode[17];//最后一位

        var seventeen = code.substring(0, 17);

        // ISO 7064:1983.MOD 11-2
        // 判断最后一位校验码是否正确
        var arr = seventeen.split("");
        var len = arr.length;
        var num = 0;
        for (var i = 0; i < len; i++) {
            num = num + arr[i] * weight_factor[i];
        }

        // 获取余数
        var resisue = num % 11;
        var last_no = check_code[resisue];

        // 格式的正则
        // 正则思路
        /*
        第一位不可能是0
        第二位到第六位可以是0-9
        第七位到第十位是年份，所以七八位为19或者20
        十一位和十二位是月份，这两位是01-12之间的数值
        十三位和十四位是日期，是从01-31之间的数值
        十五，十六，十七都是数字0-9
        十八位可能是数字0-9，也可能是X
        */
        var idcard_patter = /^[1-9][0-9]{5}([1][9][0-9]{2}|[2][0][0|1][0-9])([0][1-9]|[1][0|1|2])([0][1-9]|[1|2][0-9]|[3][0|1])[0-9]{3}([0-9]|[X])$/;

        // 判断格式是否正确
        var format = idcard_patter.test(idcode);

        // 返回验证结果，校验码和格式同时正确才算是合法的身份证号码
        return last === last_no && format;
    };

    coreUtil.checkPone  = function (telephone) {
        var myreg = /^1[0-9]{10}$/;
        return myreg.test(telephone);
    };
    coreUtil.checkEmail  = function (email) {
        var strRegex = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
        return strRegex.test(email);
    };


    return coreUtil;
})(CoreUtil, window);
