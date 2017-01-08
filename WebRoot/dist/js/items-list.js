$(function(){
		var width=$(".proc").width();
        var endWidth=width/2-15;
        if(width<100){
        	$(".process-num2").css("left",(endWidth+2)+"px");
        }else{
        	$(".process-num2").css("left",endWidth+"px");
        } 
        $(".process-num2").css("top","35px");
        $(".process-num2").css("display","inline-block");
        $(".proc").each(function(){
	        var pasent=$(this).find("#pasent").text();
		    $(this).find(".process-num2").text(parseInt(pasent*100)+"%");
	        var animateDiv = $(this).find(".bill-goods-list .process-con");
	        animateEle();
	        function animateEle(){
//	            var playRange = {top: $(window).scrollTop(), bottom: $(window).scrollTop() + $(window).height()};
	            animateDiv.each(function(){
//	                if(playRange.top <= $(this).offset().top && playRange.bottom >= $(this).offset().top && !$(this).data('bPlay')){
	                    $(this).data('bPlay', true);
	                    var nPercent = $(this).parent().find('.process-num2').text().replace(/\%/, '');
	                    $(this).svgCircle({
	                        parent : $(this)[0],
	                        w : 100,
	                        R : 20,
	                        sW : 4,
	                        color : ["#257cff","#257cff", "#257cff"],
	                        perent: [100, nPercent],
	                        speed: 150,
	                        delay: 400
	                    });
//	                }
	            });
	        };
        });
});

//打开侧边栏同时打开中部透明模版
function openRight(){
//侧边栏margin-top顶部菜单栏 暂时不启用
//	var height=$(".title-top").height()+2;
//	$(".fixTitle").css("margin-top",height);
    $(".glassMode").css("display","inline-block");
	var width=$(document).width();
	var endWidth=(width*0.3-40)/2;
	$(".fixTitle i").css("margin-left",endWidth);
	$(".fixTitle").css("display","inline-block");
	$(".fixTitle").animate({width:"30%"})
}
function closeRight(){
	$(".glassMode").css("display","none");
	$(".fixTitle").animate({width:"0%"})
}
