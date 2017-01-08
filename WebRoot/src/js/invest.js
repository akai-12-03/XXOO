"use strict";
define("invest", function(require) {
	var Ring = require("ring");
	new Ring('.circle-progress', {
		width: 100,
		height: 100,
		styles: {
			"ring-width": "6",
			"ring-color": "#ededed",
			"font-color": "#a51d1a",
			"fill-color": "#a51d1a"
		}
	});
	var Tab = require("tab");
	new Tab();
	var $ = require('jQuery');
	var Progress = require("progress");
	new Progress('.progress', {
		'autorun': true,
		'width': 290
	});
	var closewin=require('modules/closewin');
	new closewin();
	$(function ($) {
		        var img = new Image();
				var imgwidth=document.querySelectorAll(".data-tab1 img");
				var imgwidth2=document.querySelectorAll(".data-tab2 img");
				for(var i = 0 ; i<imgwidth.length ; i++){
					img.src=$(imgwidth[i]).attr("src");
					if(img.width >= 960){
						imgwidth[i].style.width="960px";
					}
				}
				for(var i = 0 ; i<imgwidth2.length ; i++){
					img.src=$(imgwidth2[i]).attr("src");
					if(img.width >= 400){
						imgwidth2[i].style.width="400px";
					}
				} 
			
//	    $(".item[data-tab=2]").on("click",function(){
//	    	$(".data-tab2 img").each(function(){
//	    		
//	    	})
//	    })
		// 计算当前填写的金额产生的预期收益
		function profit(){
			/*$('#tendermoney').on('keyup', function(){*/
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
				var award = parseFloat($.trim($('#award').val()));
				if(isNaN(award)){
					award = 0;
				}
				var partAccount = parseFloat($.trim($('#partAccount').val()));
				if(isNaN(partAccount)){
					partAccount = 0;
				}
				var account = parseFloat($.trim($('#account').val()));
				if(isNaN(account)){
					account = 0;
				}
				var funds = parseFloat($.trim($('#funds').val()));
				if(isNaN(funds)){
					funds = 0;
				}
				var awardValue=0;
				if (award == 1) {// 按固定金额分摊奖励
					awardValue = funds / account * money;
				} else if (award == 2) {// 按投标金额比例
					awardValue = money * partAccount / 100;
				} else {
					awardValue = 0;
				}
				if(awardValue==0){
					if(parseInt(sy)==sy){
						$('em.red').text(sy);
					}else{
						$('em.red').text(sy.toString().substring(0,sy.toString().indexOf(".")+3));
					}	
				}else{
					if(parseInt(awardValue)==awardValue){
						$('em.red').text(sy.toString().substring(0,sy.toString().indexOf(".")+3)+"+"+awardValue);
					}else{
						$('em.red').text(sy.toString().substring(0,sy.toString().indexOf(".")+3)+"+"+awardValue.toString().substring(0,awardValue.toString().indexOf(".")+3));
					}
				}
			/*});*/
		}
		$("#tendermoney").keyup(function(){
			profit();
		})
		$("#zdtrbutton").click(function(){
			profit();
		})
		//倒计时
		function GetRTime(){
			 $(".detail-djs").each(function(){
			 	var payTime=$(this).attr("data-endtime");
			    var EndTime=new Date(payTime.substring(0,4),payTime.substring(5,7)-1,payTime.substring(8,10),payTime.substring(11,13),payTime.substring(14,16),payTime.substring(17,19));//年月日时分秒，月要减去1
			    var NowTime = new Date();
			    var t =EndTime.getTime() - NowTime.getTime();
			    var d=0;
			    var h=0;
			    var m=0;
			    var s=0;
			    if(t>=0){
			      d=Math.floor(t/1000/60/60/24);
			      h=Math.floor(t/1000/60/60%24);
			      m=Math.floor(t/1000/60%60);
			      s=Math.floor(t/1000%60);
			      $(this).html(d + "天"+h + "时"+m + "分"+s + "秒");
			    }
			    else{
			    	 $(this).html("<font color='red'>倒计时结束！</font>");
			    }
			 })	    
	  }
	  setInterval(GetRTime,0);
	});
})