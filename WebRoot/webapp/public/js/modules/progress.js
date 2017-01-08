/*! Tomcat360.com (c) 2015 
	Author: Renzhao
*/
define("modules/progress",["jQuery","log"],function(e,r,t){"use strict";var i=e("jQuery"),n=e("log"),s=n.log,a=n.warn,o=n.error;if("undefined"==typeof i||"undefined"==typeof i.fn||"string"!=typeof i.fn.jquery)return void o("Can not find jQuery.");n.$(),s("Module Progress is loading...");var d=["default","yellow","red","blue","black","green"],u={width:"default",percent:50,color:"default",animate:!0,speed:1,autorun:!0,delay:0,time:2e3},l=function(e){return"string"==typeof e&&"default"===e.toLowerCase()?!0:null==e?!0:!1},c=function(e){if("number"==typeof e)return isNaN(e)?100:e>100?100:0>e?0:e;var r=parseInt(e);return c(r)},f=function(){var e=/msie\s+(\d+?\.\d+?)/gi,r=e.exec(navigator.userAgent);if(i.isArray(r)&&r.length>=2){var t=parseFloat(r[1]);if(0>t-10)return!0}return!1},g='<div class="progress-box"><div class="progress-inner"></div></div><div class="progress-num"></div>',h='<div class="progress-box"><div class="progress-inner progress-ie"></div></div><div class="progress-num"></div>',p=function(e){var r=e.data("width"),t=e.data("percent"),i=e.hasClass("animate"),n=e.data("color"),s=e.data("speed"),a=e.data("autorun"),o=e.data("delay"),d=e.data("time"),u={};return r&&(u.width=r),void 0!==t&&(u.percent=t),i&&(u.animate=i),n&&(u.color=n),s&&(u.speed=s),a&&(u.autorun=a),o&&(u.delay=o),d&&(u.time=d),u},v=function(e,r){return s("Progress is initializing..."),this.selector=e||".progress-container","string"!=typeof this.selector?this:(this.container=i(this.selector),0===this.container.length?(a("Can not find progress container."),this):(this.init(this.container,r),f()&&(a("Add IE fix."),i(".progress-inner").each(function(e,r){i(r).hasClass("progress-ie")||i(r).addClass("progress-ie")})),"undefined"!=typeof r&&r.autorun&&(s(12345,this.container),this.run(this.container)),s("Progress is initialized."),this))};v.prototype={init:function(e,r){var t=this;if(e.length>1)return e.each(function(e,n){t.init(i(n),r)}),this;if(e.data("init"))return this;if(e.find("div.progress").length>0)return t.data("init",!0),t.data("end",!0),e;var n=p(e),s=i.extend({},u,r,n),a={};l(s.width)||(a.width=s.width),f()?e.html(h):e.html(g);var o=e.children(".progress"),c=e.find(".progress-inner");return o.css(a),i.inArray(s.color.toLowerCase(),d)>=0&&(l(s.color)||o.addClass(s.color)),s.animate&&o.addClass("animate"),c.css("width","0%"),e.data("config",s),e.data("init",!0),t},run:function(e){var r=this;if(0===e.length)return e;if(e.length>1)return e.each(function(e,t){r.run(i(t))}),e;if(e.data("end"))return e;var t=e.data("config"),n=e.find(".progress-inner"),s=c(t.percent),a=e.find(".progress-num");return t.autorun?n.css("width","0%").delay(t.delay).animate({width:s+"%"},t.time,function(){e.data("end",!0)}):(n.css("width",s+"%"),e.data("end",!0)),a.text(s+"%"),r}},t.exports=v,s("Module Progress is loaded.")});