function getSelect(demoId, title) {
    return xmSelect.render({
        el: "#" + demoId,
        name: demoId,
        // language: 'en',// 国际化英语
        // layVerify: 'required',
        // layVerType: 'msg',
        // radio: true, // 单选
        // size: 'large',
        direction: 'down',
        theme: {
            color: '#8dc63f',
            // maxColor: 'orange', // 选超的闪烁颜色
        },
        prop: {
            name: 'name',
            value: 'id',
        },
        toolbar: {
            show: true,
            // showIcon: false,// 隐藏图标
            list: ['ALL', 'CLEAR', 'REVERSE']
        },
        model: {
            label: {
                type: 'block',
                block: {
                    //最大显示数量, 0:不限制
                    showCount: 3,
                    //是否显示删除图标
                    showIcon: true,
                }
            }
        },
        // autoRow: true,// 自动换行
        tips: '请选择' + title,
        searchTips: '搜索' + title,
        empty: '呀, 没有数据呢',
        filterable: true,// 搜索
        pageEmptyShow: false,// 无数据 不展示分页
        paging: true, // 分页
        // pageSize: 3,
        // max: 2,
        // maxMethod(seles, item){
        //     alert(`${item.name}不能选了, 已经超了`)
        // },
        data: [],
        on: function (data) {
            // //arr:  当前多选已选中的数据
            // var arr = data.arr;
            // //change, 此次选择变化的数据,数组
            // var change = data.change;
            // //isAdd, 此次操作是新增还是删除
            // var isAdd = data.isAdd;
            // alert('已有: '+arr.length+' 变化: '+change.length+', 状态: ' + isAdd)
        }
    });
}

function setSelectData(objs) {
    $(objs).each(function (index, obj) {
        if(document.getElementById(obj.demoId)){
            var demoSelect = getSelect(obj.demoId, obj.title);
            CoreUtil.sendGet(obj.url, null, function (res) {
                //初始化渲染数据
                if (res.code == 0 && res.data != null) {
                    demoSelect.update({
                        data: res.data,
                    });
                    if(obj.value){
                        demoSelect.setValue(obj.value);
                    }
                }
            });
        }
    });
}

// 级联下拉
function setCascaderSelectData(objs) {
    $(objs).each(function (index, obj) {
        if(document.getElementById(obj.demoId)){
            var demoSelect = getCascaderSelect(obj.demoId, obj.title);
            CoreUtil.sendGet(obj.url, null, function (res) {
                //初始化渲染数据
                if (res.code == 0 && res.data != null) {
                    demoSelect.update({
                        data: res.data,
                    })
                    if(obj.value){
                        demoSelect.setValue(obj.value);
                    }
                }
            });
        }
    });
}

function getCascaderSelect(demoId, title) {
    return xmSelect.render({
        el: "#" + demoId,
        name: demoId,
        theme: {
            color: '#8dc63f',
        },
        cascader: {
            //是否显示级联模式
            show: true,
            //间距
            indent: 240,
            //是否严格遵守父子模式
            strict: true,
        },
        height: '800px',
        direction: 'down',
        prop: {
            name: 'name',
            value: 'id',
        },
        model: {
            label: {
                type: 'block',
                block: {
                    //最大显示数量, 0:不限制
                    showCount: 3,
                    //是否显示删除图标
                    showIcon: true,
                }
            }
        },
        tips: '请选择' + title,
        empty: '呀, 没有数据呢',
        data: [],
        on: function (data) {

        }
    });
}


// 单选选
function setCascaderSelectData2(objs) {
    $(objs).each(function (index, obj) {
        var demoSelect = getCascaderSelect2(obj.demoId, obj.title);
        CoreUtil.sendGet(obj.url, null, function (res) {
            //初始化渲染数据
            if (res.code == 0 && res.data != null) {
                demoSelect.update({
                    data: res.data,
                })
            }
        });
    });
}

// 单选
function getCascaderSelect2(demoId, title) {
    return xmSelect.render({
        el: "#" + demoId,
        name: demoId,
        theme: {
            color: '#8dc63f',
        },
        radio: true,
        cascader: {
            //是否显示级联模式
            show: true,
            //间距
            indent: 240,
            //是否严格遵守父子模式
            strict: true,
        },
        height: '800px',
        direction: 'down',
        prop: {
            name: 'name',
            value: 'id',
        },
        model: {
            label: {
                type: 'block',
                block: {
                    //最大显示数量, 0:不限制
                    showCount: 3,
                    //是否显示删除图标
                    showIcon: true,
                }
            }
        },
        tips: '请选择' + title,
        empty: '呀, 没有数据呢',
        data: [],
        on: function(data){
            if(data.isAdd){
                return data.change.slice(0, 1)
            }
        },
    });
}
