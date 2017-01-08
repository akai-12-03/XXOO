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
})