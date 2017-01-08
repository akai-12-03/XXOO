/*! Tomcat360.com (c) 2015 
	Author: Renzhao
*/
define("modules/form", ["utils", "jQuery", "log", "validator"], function(t, i, e) {
	"use strict";
	var n, r = (t("utils"), t("jQuery")),
		o = t("log"),
		a = o.log,
		s = o.error,
		u = o.warn;
	if ("undefined" == typeof r || "undefined" == typeof r.fn || "string" != typeof r.fn.jquery) return void s("Can not find jQuery.");
	o.$(), a("Module Form is loading...");
	var l = {
			isBlurValidate: !1,
			isKeyUpValidate: !1,
			usernameRules: {
				min: 6,
				max: 16
			},
			passwordRules: {
				min: 6,
				max: 16,
				hasBlank: !1,
				onlyNumber: !0,
				onlyLetter: !0
			}
		},
		f = function(t, i) {
			if (a("form is initializing..."), this.selector = t || ".form", "string" != typeof this.selector) return this;
			if (this.form = r(this.selector).first(), 0 === this.form.length) return u("Can not find Form, please initialize Form by yourself.", "[" + this.selector + "]"), this;
			var e = this._init(i);
			return a("form is initialized."), e
		};
	f.prototype = {
		_init: function(i) {
			var e = t("validator"),
				o = this,
				a = this.form;
			return this.options = r.extend(!0, {}, l, i), n = e(this.options), this.rules = this.options.rules || [], this.inputs = a.find("input:not(disabled):not([type=hidden]):not([type=checkbox]):not([type=radio])"), this.options.isBlurValidate && this.inputs.blur(function(t) {
				var i = r(this);
				o._validate(i)
			}), this.inputs.focus(function() {
				o.clearNotice(r(this))
			}), a.submit(function(t) {
				var i = r("input[data-validator='mustchecked']");
				i.length > 0 && (i.prop("checked") || (t.preventDefault(), alert("请先阅读注册协议！")));
				for (var e = 0, n = o.inputs.length; n > e; e++) {
					var a = o.inputs.eq(e);
					if (!o._validate(a)) return t.preventDefault(), !1
				}
			}), this
		},
		_getNoticeContainer: function(t) {
			return t.find("~.tip").length > 0 ? t.find("~.tip").first() : t.parent().find("~.tip").length > 0 ? t.parent().find("~.tip").first() : r()
		},
		notice: function(t, i) {
			var e = this._getNoticeContainer(i);
			e.length > 0 ? e.text(t) : u(i, "has no notice container.")
		},
		clearNotice: function(t) {
			var i = this._getNoticeContainer(t);
			i.length > 0 ? i.text("") : u(t, "has no notice container.")
		},
		_validate: function(t) {
			var i = this,
				e = t.attr("name");
			"undefined" == typeof e && u(t, "has no attribute name.");
			var o = t.data("validator");
			"undefined" == typeof o && u(t, "has no validator type.");
			var a, s, l = i.rules,
				f = [],
				d = 0;
			for (a = l.length; a > d; d++) {
				var h = l[d];
				if (h.name === e) {
					f.push(h.validator);
					break
				}
			}
			var p = !0;
			o in n ? (s = "repassword" === o ? n[o](t.val(), n, t, r("input[data-validator=password]")) : n[o](t.val(), n, t), i.notice(s.msg, t), s.status || (p = !1)) : u(t, "has no default validator.");
			for (d = 0, a = f.length; a > d; d++) {
				var c = f[d];
				if (s = c(t.val(), n, t), i.notice(s.msg, t), !s.status) {
					p = !1;
					break
				}
			}
			return p
		},
		submit: function(t) {
			return this.form.submit(t), this
		}
	}, e.exports = f, a("Module Form is loaded.")
});