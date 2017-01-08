"use strict";
define("forget", function(require) {
	var Form = require("form");
	new Form('#forget-form', {
		isBlurValidate: true,
		'rules': []
	});
	var VerifyImg = require("verify-img");
	new VerifyImg();
	var VerifyTel = require("verify-tel");
	new VerifyTel(undefined, {
		url: ROOT_URL + '/sendcode.html',
		'telphone': '#telphone',
		'type': 'forget'
	});
})