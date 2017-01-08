"use strict";
define("myhome", function(require) {
	/*var Accordion = require("accordion");
	new Accordion();*/
	var Form = require("form");
	new Form('#changepwd-form', {
		isBlurValidate: true,
		'rules': [
			]
		});
	new Form('#jypwd-form', {
		isBlurValidate: true,
		'rules': [
			]
		});
	var VerifyTel = require("verify-tel");
	new VerifyTel(undefined, {
		url: ROOT_URL + 'sendcode.html',
		'telphone': '#telphone',
		'type':'jyname',
		'jyname':'#telphone'
		});
	var VerifyImg = require("verify-img");
	new VerifyImg();
	
})