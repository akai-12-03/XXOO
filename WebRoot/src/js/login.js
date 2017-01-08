"use strict";
define("login", function(require) {
	var $ = require('jQuery');
	var Form = require("form");
   	new Form('#login-form', {
		isBlurValidate: true,
		isCheckbox: false,
		'rules': [
		]
	});
	var VerifyImg = require("verify-img");
	new VerifyImg();
	var closewin=require('modules/closewin');
	new closewin();
})