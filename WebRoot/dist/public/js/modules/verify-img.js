/*! Tomcat360.com (c) 2015 
	Author: Renzhao
*/
define("modules/verify-img",["jQuery","log"],function(i,t,n){"use strict";var e=i("jQuery"),r=i("log"),s=r.log,o=r.error,f=r.warn;if("undefined"==typeof e||"undefined"==typeof e.fn||"string"!=typeof e.fn.jquery)return void o("Can not find jQuery.");r.$(),s("Module VerifyImg is loading...");var u=function(i){return s("VerifyImg is initializing..."),this.version="0.0.1",this.selector=i||".verifyimg-box","string"==typeof this.selector&&(this.container=e(this.selector)),0===this.container.length?(f("Can not find",this.selector),this):(this.input=this.container.find("input"),this.img=this.container.find("img"),this._init(),s("VerifyImg is initialized."),this)};u.prototype={_init:function(){var i=this;return this.img.click(function(){i.update()}),i},update:function(){var i=this.img.attr("src").split("?")[0];return i+="?"+Math.random(),this.img.attr("src",i),this}},n.exports=u,s("Module VerifyImg is loaded.")});