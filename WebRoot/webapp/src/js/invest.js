"use strict";
define("invest", function(require) {
	var $ = require('jQuery');
	$(function ($) {
		//计算收益
		$('#tendermoney').on('keyup', function(){
			var apr = parseFloat($.trim($('#tender-apr').data('apr')));
			if(isNaN(apr)){
				apr = 0;
			}
			var time = parseInt($.trim($('#tender-time').data('num')));
			if(isNaN(time)){
				time = 0;
			}
			var money = parseFloat($.trim($('#tendermoney').val()));
			if(isNaN(money)){
				money = 0;
			}
			var unit = parseInt($.trim($('#tender-time').data('unit')));
			if(unit !== 1){
				var sy = money * apr * time / 1200;
			}
			else{
				var sy = money * apr * time / 36500;
			}
			
			$('em.red').text(sy.toFixed(2));
		});
	})
})