/*! Tomcat360.com (c) 2015 
	Author: Renzhao
*/
"use strict";
define("signup", ["jQuery", "verify-tel", "verify-img", "form"], function(n) {
	var o = n("jQuery"),
		s = n("verify-tel");
	new s(void 0, {
		url: ROOT_URL + "/sendcode.html",
		telphone: "#telphone"
	});
	var i = n("verify-img");
	new i;
	var e = n("form");
	new e("#signup-form", {
		isBlurValidate: !0,
		rules: []
	}), o(function(n) {
		n("#agreement").hover(function() {
			n(this).stop().animate({
				opacity: "1"
			}, 600)
		}, function() {
			n(this).stop().animate({
				opacity: "0.9"
			}, 1e3)
		}).on("click", function() {
			n("body").append("<div id='mask'></div>"), n("#mask").addClass("mask").fadeIn("slow"), n("#LoginBox1").fadeIn("slow")
		}), n(".close_btn").hover(function() {
			n(this).css({
				color: "black"
			})
		}, function() {
			n(this).css({
				color: "#999"
			})
		}).on("click", function() {
			n("#LoginBox1").fadeOut("fast"), n("#mask").css({
				display: "none"
			})
		})
	}), o(function(n) {
		n("#success").hover(function() {
			n(this).stop().animate({
				opacity: "1"
			}, 600)
		}, function() {
			n(this).stop().animate({
				opacity: "0.9"
			}, 1e3)
		}).on("click", function() {
			n("body").append("<div id='mask'></div>"), n("#mask").addClass("mask").fadeIn("slow"), n("#successBox").fadeIn("slow")
		}), n(".close_btn").hover(function() {
			n(this).css({
				color: "black"
			})
		}, function() {
			n(this).css({
				color: "#999"
			})
		}).on("click", function() {
			n("#successBox").fadeOut("fast"), n("#mask").css({
				display: "none"
			})
		})
	})
});