var vForm = function (option) {
    // 选择器
    var default_object = {
        selector: "#form",
        sendurl: "",
        cityurl: ""
    };
    this.selector = option.selector || default_object.selector;
    this.url = option.sendurl || default_object.sendurl;
    this.cityurl = option.cityurl || default_object.cityurl;
    // form下input的数量
    this.inputlength = 0;
    // form下正确的input的数量
    this.trueinput = [];
    this.no_null_num = 0;
    // 进行初始化
    this.init();
}
vForm.prototype = {
    // 正则规则
    regEXP: {
        tel_reg: /^1\d{10}$/,
        password_reg: /^(?![^a-zA-Z]+$)(?!\D+$).{6,15}$/,
        only_number: /^\d+$/,
        only_word: /^[a-zA-Z]+$/,
        money: /^\d+(\.\d{1,2})?$/,
        cn_name: /[\u4E00-\u9FA5\uF900-\uFA2D]/,
        bank_num: /^(\d{16}|\d{19})$/,
        person: /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/
    },
    regEXP_length: {
        mix_length: 6,
        max_length: 15,
        max_money_length:12
    },
    // 初始化
    init: function () {
        var that = this;
        // 阻止表单提交
        $(this.selector).submit(false);
        // 向input添加blur事件
        $(
            this.selector + " input[required='required'][type='text'],"
            + this.selector
            + " input[required='required'][type='password'],"
            + this.selector + " select," + this.selector + " input[type='checkbox']").each(function () {
            // 添加input数量开始
            that.inputlength++;
            if ($(this).attr("id") === "password") { // 密码
                that.v_password($(this).attr("id"));
            } else if ($(this).attr("id") === "repassword") { // 重复密码
                that.v_repassword()
            } else if ($(this).attr("id") === "telphone") { // 电话号码
                that.v_tel();
            } else if ($(this).attr("id") === "oldpassword") { // 旧密码
                that.v_password($(this).attr("id"));
            } else if ($(this).attr("id") === "imgcode") { // 图形验证码
                that.v_imgcode();
            } else if ($(this).attr("id") === "telcode") { // 短信验证码
                that.v_telcode();
            } else if ($(this).attr("id") === "money") { // 提现充值金额
                that.v_money();
            } else if ($(this).attr("id") === "dealpassword") { // 交易密码
                that.v_dealpassword();
            } else if ($(this).attr("id") === "cnname") { // 中文姓名
                that.v_cnName();
            } else if ($(this).attr("id") === "banknum") { // 银行卡号
                that.v_bankNum();
            } else if ($(this).attr("id") === "city") { // 选择城市
                that.v_city();
            } else if ($(this).hasClass("noNull") === true) {
                that.v_noNull($(this), that.no_null_num);
                that.no_null_num++;
            } else if ($(this).attr("id") === "xieyi") {
                that.v_xieYi();
            } else if($(this).attr("id") === "person"){
                that.v_person();
            }
        });
        $(this.selector + " img").each(function () {
            $(this).on("click", function () {
                that.imgajax($(this));
            });
        });
    },
    // 正则校验
    doregEXP: function (val, regEXP) {
        var flag = regEXP.test(val);
        return flag;
    },
    // 控制表单开关
    v_switch: function (index, name, flag) {
        var in_arr_index;
        var in_arr = false;
        for (var i = 0; i < index.trueinput.length; i++) {
            if (index.trueinput[i] === name) {
                in_arr = true;
                in_arr_index = i;
            }
        }
        if (in_arr === true) {
            if (flag === false) {
                index.trueinput.splice(in_arr_index, 1);
                $(index.selector).submit(false);
            }
        } else {
            if (flag === true) {
                index.trueinput.push(name);
            }
        }
        console.log(index.inputlength);
        console.log(index.trueinput.length);
        console.log(index.trueinput);
        if (index.trueinput.length === index.inputlength) {
            $(index.selector + " button[type='submit']").on("click", function () {
                $(index.selector).off();
                $(index.selector).submit();
            });
        }
    },
    imgajax: function (op) {
        var url = op.attr('src').split('?')[0];
        url += '?' + Math.random();
        op.attr('src', url);
    },
    //不为空判断
    v_noNull: function (op, index) {
        var that = this;
        var _select = op;
        var _select_tip = op.siblings(".tip");
        _select.on("blur", function () {
            var _val = $(_select).val().trim();
            if (_val === "") {
                _select_tip.text("不能为空");
                that.v_switch(that, "noNull" + index, false);
            } else {
                _select_tip.text("");
                that.v_switch(that, "noNull" + index, true);
            }
        })
    },
    v_person:function(){
        var that = this;
        var _select = $(this.selector + " #person");
        _select.on("blur", function () {
            var val = _select.val().trim();
            var flag = that.doregEXP(val, that.regEXP.person);
            if (val === "") {
                _select.siblings(".tip").text("身份证号码不能为空");
                that.v_switch(that, "telphone", false);
            } else if (flag === false) {
                _select.siblings(".tip").text("身份证号码输入有误");
                that.v_switch(that, "telphone", false);
            } else {
                _select.siblings(".tip").text("");
                that.v_switch(that, "telphone", true);
            }
        });
    },
    // 手机号码验证
    v_tel: function () {
        var that = this;
        var _select = $(this.selector + " #telphone");
        _select.on("blur", function () {
            var val = _select.val().trim();
            var flag = that.doregEXP(val, that.regEXP.tel_reg);
            if (val === "") {
                _select.siblings(".tip").text("手机号码不能为空");
                that.v_switch(that, "telphone", false);
            } else if (flag === false) {
                _select.siblings(".tip").text("手机号码输入有误");
                that.v_switch(that, "telphone", false);
            } else {
                _select.siblings(".tip").text("");
                that.v_switch(that, "telphone", true);
            }
        });
    },
    // 密码验证
    v_password: function (type) {
        var that = this;
        var _select;
        if (type === "password") {
            _select = $(this.selector + " #" + type);
        } else if (type === "oldpassword") {
            _select = $(this.selector + " #" + type);
        }
        _select.on("blur", function () {
            var val = _select.val().trim();
            var flag = that.doregEXP(val, that.regEXP.password_reg);
            if (val === "") {
                _select.siblings(".tip").text("密码不能为空");
                that.v_switch(that, type, false);
            } else if (val.length < that.regEXP_length.mix_length) {
                _select.siblings(".tip").text(
                    "密码不能小于" + that.regEXP_length.mix_length + "位");
                that.v_switch(that, type, false);
            } else if (val.length > that.regEXP_length.max_length) {
                _select.siblings(".tip").text(
                    "密码不能大于" + that.regEXP_length.max_length + "位");
                that.v_switch(that, type, false);
            } else if (that.doregEXP(val, that.regEXP.only_number)) {
                _select.siblings(".tip").text("密码不能为纯数字");
                that.v_switch(that, type, false);
            } else if (that.doregEXP(val, that.regEXP.only_word)) {
                _select.siblings(".tip").text("密码不能为纯字母");
                that.v_switch(that, type, false);
            } else if (flag === false) {
                _select.siblings(".tip").text("密码输入有误");
                that.v_switch(that, type, false);
            } else {
                _select.siblings(".tip").text("");
                that.v_switch(that, type, true);
            }
        });
    },
    // 重复密码验证
    v_repassword: function () {
        var that = this;
        var _select = $(this.selector + " #repassword");
        var password_selector = $(this.selector + " #password");
        _select.on("blur", function () {
            var val = _select.val().trim();
            var re_val = password_selector.val().trim();
            if (val === "") {
                _select.siblings(".tip").text("密码不能为空");
                that.v_switch(that, "repassword", false);
            } else if (val !== re_val) {
                _select.siblings(".tip").text("两次密码不一致");
                that.v_switch(that, "repassword", false);
            } else {
                _select.siblings(".tip").text("");
                that.v_switch(that, "repassword", true);
            }
        });
    },
    // 图形验证码验证
    v_imgcode: function () {
        var that = this;
        var _select = $(this.selector + " #imgcode");
        _select.on("blur", function () {
            var val = _select.val().trim();
            if (val === "") {
                _select.siblings(".tip").text("图形验证码不能为空");
                that.v_switch(that, "imgcode", false);
            } else {
                _select.siblings(".tip").text("");
                that.v_switch(that, "imgcode", true);
            }
        });
    },
    v_xieYi: function () {
        var that = this;
        var _select = $(this.selector + " #xieyi");
        _select.on("click", function () {
            if ($(this).is(":checked")) {
                that.v_switch(that, "xieyi", true);
            } else {
                that.v_switch(that, "xieyi", false);
            }
        });
    },
    // 发送短信验证
    v_telcode: function () {
        var that = this;
        var _select = $(this.selector + " #telcode");
        var tel_select = $(this.selector + " #telphone");
        var tel_select_tip = tel_select.siblings(".tip");
        var tel_button_type = _select.siblings("button").attr("data-type");
        var imgcode_select = $(this.selector + " #imgcode");
        var imgcode_select_tip = imgcode_select.siblings(".tip");
        _select.on("blur", function () {
            var val = _select.val().trim();
            if (val === "") {
                _select.siblings(".tip").text("手机验证码不能为空");
                that.v_switch(that, "telcode", false);
            } else {
                _select.siblings(".tip").text("");
                that.v_switch(that, "telcode", true);
            }
        });
        _select.next().on(
            "click",
            function () {
                var sendtime = 60;
                var tel_flag = false;
                var img_flag = false;
                var tel_val = tel_select.val();
                var imgcode_val = imgcode_select.val();
                var img_val_flag = that.doregEXP(tel_val,
                    that.regEXP.tel_reg);
                if (tel_val === "") {
                    tel_select_tip.text("手机号码不能为空");
                } else if (img_val_flag === false) {
                    tel_select_tip.text("手机号码输入有误");
                } else {
                    tel_flag = true;
                }
                if (imgcode_val === "") {
                    imgcode_select_tip.text("图形验证码不能为空");
                } else {
                    img_flag = true;
                }
                if (tel_flag === true && img_flag === true) {
                    var data_data = {
                        phone: tel_val,
                        imgcode: imgcode_val
                    };
                    if (tel_button_type === "forget") {
                        // 忘记密码奢华尊享按钮数据
                        data_data.opr = "A33";
                    } else {
                        // 登陆注册等数据
                        data_data.opr = $(that.selector + " #opr").val();
                    }
                    $.ajax({
                        url: that.url,
                        data: data_data,
                        success: function (data) {
                            console.log(data);
                            if (data == 1) {
                                var time = setInterval(function () {
                                    _select.siblings("button").attr(
                                        "disabled", true);
                                    _select.siblings("button").text(
                                        sendtime + "秒");
                                    sendtime--;
                                    if (sendtime < 0) {
                                        clearInterval(time);
                                        _select.siblings("button").attr(
                                            "disabled", false);
                                        _select.siblings("button").text(
                                            "重新发送");
                                        sendtime = 60;
                                    }
                                }, 1000);
                            } else if (data == 2) {
                                that.v_animate("此手机号已被注册");
                            } else if (data == 3) {
                                that.v_animate("系统错误");
                            } else if (data == 4) {
                                that.v_animate("用户不存在");
                            } else if (data == 5) {
                                that.v_animate("手机与用户不符");
                            } else if (data == 98) {
                                that.v_animate("图形验证码错误");
                            }
                        },
                        error: function () {
                            alert("网络异常");
                        }
                    });
                }
            })
    },
    //动画依赖animate.css
    v_animate: function (string) {
        $(".errmsg").on('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function () {
            $(".errmsg").fadeOut(2000);
        });
        $(".errmsg").text(string).css("display", "inline-block").addClass("tada");
    },
    // 充值提现
    v_money: function () {
        var that = this;
        var _select = $(that.selector + " #money");
        var _select_tip = $(that.selector + " #money").siblings(".tip");
        _select.on("blur", function () {
            var _val = $(_select).val().trim();
            var flag = that.doregEXP(_val, that.regEXP.money);
            if (_val === "") {
                _select_tip.text("金额不能为空");
                that.v_switch(that, "money", false);
            } else if (flag === false) {
                _select_tip.text("金额输入有误");
                that.v_switch(that, "money", false);
            } else if(_val.length > that.regEXP_length.max_money_length){
                _select_tip.text("金额输入过大");
                that.v_switch(that, "money", false);
            }else {
                _select_tip.text("");
                that.v_switch(that, "money", true);
            }
        })
    },
    // 交易密码
    v_dealpassword: function () {
        var that = this;
        var _select = $(that.selector + " #dealpassword");
        var _select_tip = $(that.selector + " #dealpassword").siblings(".tip");
        _select.on("blur", function () {
            var _val = $(_select).val().trim();
            if (_val === "") {
                _select_tip.text("交易密码不能为空");
                that.v_switch(that, "dealpassword", false);
            } else {
                _select_tip.text("");
                that.v_switch(that, "dealpassword", true);
            }
        })
    },
    // 中文验证
    v_cnName: function () {
        var that = this;
        var _select = $(that.selector + " #cnname");
        var _select_tip = $(that.selector + " #cnname").siblings(".tip");
        _select.on("blur", function () {
            var _val = $(_select).val().trim();
            var flag = that.doregEXP(_val, that.regEXP.cn_name);
            if (_val === "") {
                _select_tip.text("姓名不能为空");
                that.v_switch(that, "cnname", false);
            } else if (flag === false) {
                _select_tip.text("姓名错误");
                that.v_switch(that, "cnname", false);
            } else {
                _select_tip.text("");
                that.v_switch(that, "cnname", true);
            }
        })
    },
    // 银行卡号验证
    v_bankNum: function () {
        var that = this;
        var _select = $(that.selector + " #banknum");
        var _select_tip = $(that.selector + " #banknum").siblings(".tip");
        _select.on("blur", function () {
            var _val = $(_select).val().trim();
            var flag = that.doregEXP(_val, that.regEXP.bank_num);
            if (_val === "") {
                _select_tip.text("银行卡号不能为空");
                that.v_switch(that, "banknum", false);
            } else if (flag === false) {
                _select_tip.text("银行卡号错误");
                that.v_switch(that, "banknum", false);
            } else {
                _select_tip.text("");
                that.v_switch(that, "banknum", true);
            }
        })
    },
    // 选择城市
    v_city: function () {
        var that = this;
        var _select = $(that.selector + " #city");
        var _select_minicity = $(that.selector + " #minicity");
        _select_minicity.on("change", function () {
            $(that.selector + " #minicity>option[value='" + $(this).val() + "']").attr("checked", "checked");
            $(that.selector + " #minicity>option[value='" + $(this).val() + "']").siblings().removeAttr("checked");
        });
        _select.on("change", function () {
            _select_minicity.empty().append("<option>请选择</option>");
            var _val = $(that.selector + " #city option[value='" + $(this).val() + "']").attr("data-value");
            $(that.selector + " #city option[value='" + $(this).val() + "']").attr("checked", "checked").siblings().removeAttr("checked");
            $.ajax({
                type: "post",
                url: that.cityurl,
                data: {
                    province: _val
                },
                dataType: "json",
                success: function (data) {
                    for (var i in data) {
                        _select_minicity.prepend("<option value='" + data[i].city + "'>" + data[i].city + "</option>");
                    }
                }
            });
        });
        _select.on("blur", function () {
            var _val = $(_select).val().trim();
            if (_val === "请选择") {
                that.v_switch(that, "city", false);
            } else {
                that.v_switch(that, "city", true);
            }
        });
        _select_minicity.on("blur", function () {
            var _val = $(_select_minicity).val().trim();
            if (_val === "请选择") {
                that.v_switch(that, "minicity", false);
            } else {
                that.v_switch(that, "minicity", true);
            }
        });
        $(that.selector + " #bank").on("blur", function () {
            var _val = $(that.selector + " #bank").val().trim();
            if (_val === "请选择") {
                that.v_switch(that, "bank", false);
            } else {
                that.v_switch(that, "bank", true);
            }
        });
    }
};
