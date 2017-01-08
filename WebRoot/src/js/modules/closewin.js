define(function(require, exports, module){
	"use strict";
	var $ = require('jQuery');
	var closewin =function(){
		this.config();
	}
	closewin.prototype={
		config:function(){
			$(".close_btn").on("click",function(){
				$("#tipBox").css("display","none");
			    $(".mask").css("display","none");
			})	
		}
	}
    module.exports=closewin;
});