/*! Tomcat360.com (c) 2015 
	Author: Renzhao
*/
"use strict";define("index",["slides","jQuery","progress"],function(n){function e(n){var e=n.find("ul:first"),i=e.find("li:first").height();e.animate({marginTop:-i+"px"},600,function(){e.css({marginTop:0}).find("li:first").appendTo(e)})}var i=n("slides");new i;var r=n("jQuery"),s=n("progress");new s(".progress",{autorun:!0,width:200}),r(function(){var n,i=r("#scrollNews");i.hover(function(){clearInterval(n)},function(){n=setInterval(function(){e(i)},3e3)}).trigger("mouseleave")})});