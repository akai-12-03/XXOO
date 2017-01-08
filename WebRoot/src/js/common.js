//yyyy-MM-dd HH:mm:ss
function change_picktime(date){
		if (date){
		WdatePicker({dateFmt:''+date+''});
		}else{
			WdatePicker();
		}
}	


$(document).ready(function() {
	//topline QQ下拉菜单					   
	$('.topQQ').hover(function(){
	  $('.topQQ2').css('display','block');
	  $('.topQQ3').css('display','block');
	  $('.topQQ4').css('display','block');
	  $('.topQQ5').css('display','block');
	  $('.topQQ6').css('display','block');
	});
	$('.topLine .fleft').mouseleave(function(){
	  $('.topQQ2').hide();
	  $('.topQQ3').hide();
	  $('.topQQ4').hide();
	  $('.topQQ5').hide();
	  $('.topQQ6').hide();
	});

	
	//左边 伸缩
	$('.aboutLeft dt').click(function(){
	  $(this).addClass("on");
	  $(this).siblings().removeClass("on");
	  $('.aboutLeft dd').hide(300);
	  $(this).next("dd").show(300);
	});
   
   
   
});

/*
$(function(){
	function qq(){
		$("#online_qq_tab a").click(function(){
			$("#onlineService").toggle('normal');
		})
	}
	qq();
})
*/
$(function() {
    // 默认延时时间, 单位ms
    var defaultDalayTime = 300;
    // 延时函数, 用于hover事件
	$.fn.hoverDelay = function(options){
        var defaults = {
            hoverDuring: defaultDalayTime,
            outDuring: defaultDalayTime,
            hoverEvent: function(){},
            outEvent: function(){}
        };
        options = $.extend(defaults,options || {});
        var hoverTimer, outTimer, that = this;
        return $(this).each(function(){
            $(this).hover(function(){
                clearTimeout(outTimer);
                hoverTimer = setTimeout(function(){options.hoverEvent.apply(that)}, options.hoverDuring);
            },function(){
                clearTimeout(hoverTimer);
                outTimer = setTimeout(function(){options.outEvent.apply(that)}, options.outDuring);
            });    
        });
    }    
	  
	// 具体内容外框
	$("#online_qq_layer").hoverDelay({
        hoverEvent: function(){
           $("#onlineService").show(defaultDalayTime);    
        },
        outEvent: function(){
           $("#onlineService").hide(defaultDalayTime);
        }

	});
    
    // QQ
    $("#online_qq_tab .QQ").hoverDelay({
        hoverEvent: function(){
           $("#Calculator_Con").hide();  
		   $("#qjweinxin_con").hide();
		    $("#sina_img").hide();  
		   $("#wechat_img").hide();
           $("#qqHot").show(defaultDalayTime);    
        }
	});
	//电话
	$("#online_qq_tab .phone").hoverDelay({
        hoverEvent: function(){
           $("#Calculator_Con").hide();  
		   $("#qjweinxin_con").hide();
		    $("#sina_img").hide();  
		   $("#wechat_img").hide();
           $("#qqHot").show(defaultDalayTime);    
        }
	});
	    // weixin
    $("#online_qq_tab .qjweixin").hoverDelay({
        hoverEvent: function(){
          
		   $("#Calculator_Con").hide();  
		   $("#qqHot").hide(); 
		    $("#sina_img").hide();  
		   $("#wechat_img").hide();
           $("#qjweinxin_con").show(defaultDalayTime);   
        }
	});
        // sina
    $("#online_qq_tab .sina").hoverDelay({
        hoverEvent: function(){
          
		   $("#Calculator_Con").hide();  
		   $("#qqHot").hide();   
		   $("#wechat_img").hide();
           $("#qjweinxin_con").hide();
		   $("#sina_img").show(defaultDalayTime);   
        }
	});
	    // wechat
    $("#online_qq_tab .wechat").hoverDelay({
        hoverEvent: function(){
          
		   $("#Calculator_Con").hide();  
		   $("#qqHot").hide(); 
		    $("#sina_img").hide();  
           $("#qjweinxin_con").hide();
		   $("#wechat_img").hide();   
        }
	});
    // 计算收益
    $("#online_qq_tab .Calculator").hoverDelay({
        hoverEvent: function(){
           $("#qqHot").hide();
		   $("#qjweinxin_con").hide(); 
		    $("#sina_img").hide();  
		   $("#wechat_img").hide();  
           $("#Calculator_Con").show(defaultDalayTime);
			calculatorText();
        }
	});
    
    function calculatorText(){
		if($("#calculator_mounth").is(":checked"))//判断按月还是按天算
		//按月
		{
			//显示和隐藏天数和月份输入框
			$("#dvday_con").hide();
			$("#dvday").hide();
			$("#dvmounth_con").show();
			$("#dvmounth").show();
			$("#day_time_con").val("0")
			$("#mounth_time_con").val("1");
		}
		//按天
		else
		{
			//显示和隐藏天数和月份输入框
			$("#dvmounth_con").hide();
			$("#dvmounth").hide();
			$("#dvday_con").show();
			$("#dvday").show();
			$("#mounth_time_con").val("0");
			$("#day_time_con").val("28")
		}
		commonProfit();
	}
	$(".Calculator_text").click(function(){calculatorText()});
	
	//计算器计算
	function commonProfit(){

		var money =  $("#cash_con").val();//本金
		var money =	 money .replace(/[^0-9.]/g, '')//本金只取数值

		var calcul =  $("#calcul_con").val();//年利率
		var calcul =  calcul.replace(/[^0-9.]/g, '');//年利率

		var time =  $("#mounth_time_con").val();//时间（月）
		var times =  Number(time.replace(/[^0-9.]/g, ''));//时间（月）只取数值

		var daytime =  $("#day_time_con").val();//时间（天）
		var daytimes =  Number(daytime.replace(/[^0-9.]/g, ''));//时间（月）只取数值

		//利息
		var result1   = (money*calcul*times)/1200;
		var resultday  = parseFloat((money*calcul*daytimes)/36500);
		var resultall = result1 + resultday;

		//总额
		var result3  = resultall;
		var moneyStr = result3.toString();
		if(moneyStr.indexOf(".")>0){
			var str = moneyStr.toString().split(".")[1];
			if(str.length>2){
				str = str.toString().substr(0,2);
			}
			$(".relust_con").val(moneyStr.toString().split(".")[0]+"."+str+"元");
		}else{
			$(".relust_con").val(moneyStr+"元");
		}
	}
	$(".calculator_in").bind('keyup change',function(){commonProfit();});

    $(function(){
        var $this = $(".notice_bg");
		var scrollTimer;
		$this.hover(function(){
			  clearInterval(scrollTimer);
		 },function(){
		   scrollTimer = setInterval(function(){
						 scrollNews( $this );
					}, 3000 );
		}).trigger("mouseleave");
});
    var scrollNews = function(obj){
    	var $self = obj.find("dl:first"); 
    	var lineHeight = $self.find("dd:first").height(); //获取行高
    	$self.animate({ "marginTop" : -lineHeight +"px" }, 600 , function(){
    	$self.css({marginTop:0}).find("dd:first").appendTo($self); //appendTo能直接移动元素
    	})
    };
});
$(function(){
	$('#nav-menu .menu1 > li#userInfo').hover(function(){
		$(this).find('.children').animate({ opacity:'show', height:'show' },200).parents('.stmenu').addClass('navhover');
	}, function() {
		$('.children').stop(true,true).hide().parents('.stmenu').removeClass('navhover');
	});
});
function b() {
	//h = $(window).height()-1600,
	t = $(document).scrollTop(),
	t > 900 ? $("#moquu_top").show() : $("#moquu_top").hide()
}
$(document).ready(function() {
	b(),
	$("#moquu_top").click(function() {
		var speed=400;//滑动的速度
        $('body,html').animate({ scrollTop: 0 }, speed);
        return false;
	})
}),
$(window).scroll(function() {
	b()
});

/**************** tab ****************/
function qiehuan(num){
		for(var id = 0;id<=5;id++)
		{
			if(id==num)
			{
				document.getElementById("on"+id).style.display="block";
				document.getElementById("myon"+id).className="on"; 
			}
			else
			{
				document.getElementById("on"+id).style.display="none";
				document.getElementById("myon"+id).className="";
		}
	}
	
}

function signin(){
	jQuery.ajax({
	    type: 'POST',
	    dataType: "json",
	    url: ROOT_URL + '/signin.html',
	    success: function(result){
	    	console.log(result)
	    	if(result.status === 1){
		    	$('.unsign').text('已签到');
		    	$('.unsign').attr('title', '今日已签到');
		    	$('.unsign').off('click');
		    	$('.unsign').removeAttr('href');
		    	$('.unsign').append('<span class="sign_bg">'+result.msg+'</span>');
		    	$('.unsign').toggleClass('unsign sign');
				$("#qd_mask").hide();
				$(".qd_layer").hide();
				setTimeout(function(){
					$(".sign_bg").remove();
				}, 5000);
	    	}
	    	else{
	    		$('.unsign').append('<span class="sign_bg">'+result.msg+'</span>');
	    		setTimeout(function(){
					$(".sign_bg").remove();
				}, 5000);
	    	}
	    	
	     }
	});
}



