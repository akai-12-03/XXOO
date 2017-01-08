"use strict";
define("aboutus", function(require) {
	var $ = require('jQuery');
	$(function() {
		//tab切换
		$('.about-tab > a').click(function() {
			$(this).addClass('active').siblings().removeClass('active');
			var index = $('.about-tab > a').index(this);
			$('.aboutus-content > .content-item').eq(index).show().siblings().hide();
		});
		//展开，收缩
		$(".question-list").each(function(index) {
			$(".question-list").eq(index).click(function() {
				if ($(".question-list .answer").eq(index).is(":visible")) {
					$(".question-list").eq(index).find("img").attr("src", "../public/images/arrow-right.png").css({
						"width": "15px",
						"height": "24px"
					});
					$(".answer").eq(index).css("display", "none");
				} else {
					$(".question-list").eq(index).find("img").attr("src", "../public/images/arrow-slide.png").css({
						"width": "24px",
						"height": "16px"
					});
					$(".answer").eq(index).css("display", "block");
				}
			});
		});
	})
})