//首页圆形进度条
function crl(){
	    var width=$(document).width();   
        var endWidth=width/2-85;
        $(".process-num").css("left",endWidth);    
        $(".process-num").css("display","inline-block");
	    var pasent=$("#pasent").text();
	    $(".process-num").text(parseInt(pasent*100)+"%");
        var animateDiv = $('.bill-goods-list .process-con');
        animateEle();
        var height=$(".process-wrap").height();
        var endHeight=(height-170)/2-2;
        $(".process-num").css("top",endHeight);
        function animateEle(){
            var playRange = {top: $(window).scrollTop(), bottom: $(window).scrollTop() + $(window).height()};
            animateDiv.each(function(){
                if(playRange.top <= $(this).offset().top && playRange.bottom >= $(this).offset().top && !$(this).data('bPlay')){
                    $(this).data('bPlay', true);
                    var nPercent = $(this).parent().find('.process-num').text().replace(/\%/, '');
                    $(this).svgCircle({
                        parent : $(this)[0],
                        w : 208,
                        R : 100,
                        sW : 4,
                        color : ["#257cff","#257cff", "#257cff"],
                        perent: [100, nPercent],
                        speed: 150,
                        delay: 400
                    });
                }
            });
        };
};
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
