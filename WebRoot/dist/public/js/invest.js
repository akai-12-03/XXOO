/*! Tomcat360.com (c) 2015 
	Author: Renzhao
*/
"use strict";define("invest",["jQuery"],function(e){var t=e("jQuery");t(function(e){e("#tendermoney").on("keyup",function(){var t=parseFloat(e.trim(e("#tender-apr").data("apr")));isNaN(t)&&(t=0);var r=parseInt(e.trim(e("#tender-time").data("num")));isNaN(r)&&(r=0);var a=parseFloat(e.trim(e("#tendermoney").val()));isNaN(a)&&(a=0);var n=parseInt(e.trim(e("#tender-time").data("unit")));if(1!==n)var i=a*t*r/1200;else var i=a*t*r/36500;e("em.red").text(i.toFixed(2))})})});