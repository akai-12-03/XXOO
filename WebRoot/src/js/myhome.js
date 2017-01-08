"use strict";
define("myhome", function(require) {
	var $ = require("jQuery");
	var Accordion = require("accordion");
	new Accordion();
	var Tab = require("tab");
	new Tab();
	var VerifyTel = require("verify-tel");
	new VerifyTel(undefined, {
		url: ROOT_URL + '/sendcode.html',
		'telphone': '#telphone'
	});
	var VerifyImg = require("verify-img");
	new VerifyImg();
	var Form = require("form");
	new Form('#common-form', {
		isBlurValidate: true,
		'rules': []
	});
	var closewin=require('modules/closewin');
	new closewin();
	$("#tmoney-sub").on("click",function(){
		if($("#tmomey").val() <= 1){
			$("#tmoney-form").submit(false);
			alert("提现金额需要大于1元");
		}else{
			$("form").off();
		}
	})
})