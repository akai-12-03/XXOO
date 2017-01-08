/*! Tomcat360.com (c) 2015 
	Author: Renzhao
*/
define("modules/ring",["jQuery","log","raphael"],function(t,s,i){"use strict";var r=t("jQuery"),e=t("log"),a=e.log,n=(e.warn,e.error),o=t("raphael");if(a("raphael is ready, version:",o.version),"undefined"==typeof r||"undefined"==typeof r.fn||"string"!=typeof r.fn.jquery)return void n("Can not find jQuery.");e.$(),a("Module Ring is loading...");var l=function(t,s){return a("Ring is initializing..."),this.rings=t||".circle",this.version="0.0.1","string"==typeof this.rings&&(this.rings=r(this.rings)),0===this.rings.length?this:(this.params={},this.setConfig(s),this._init(),a("Ring is initialized."),this)};l.prototype={_init:function(){var t=this;return t.rings.each(function(s,i){var r=t.rings.eq(s);t._draw(r)}),t},setConfig:function(t){return t=t||{},this.params.width=t.width||0,this.params.height=t.height||0,this.params.styles={},t.styles=t.styles||{},this.params.styles["ring-width"]=t.styles["ring-width"]||6,this.params.styles["ring-color"]=t.styles["ring-color"]||"#dcdcdc",this.params.styles["back-color"]=t.styles["back-color"]||"white",this.params.styles["fill-color"]=t.styles["fill-color"]||"#fe852c",this.params.styles["font-color"]=t.styles["font-color"]||"#fe852c",this.params.styles["font-size"]=t.styles["font-size"]||14,this._ringStyles={stroke:this.params.styles["ring-color"],fill:this.params.styles["back-color"],"stroke-width":this.params.styles["ring-width"]},this._fontStyles={"font-size":this.params.styles["font-size"],fill:this.params.styles["font-color"],"text-anchor":"middle"},this},_getPage:function(t){var s=this.params.width||t.innerWidth()||64,i=this.params.height||t.innerHeight()||64,r=s>i?i:s,e=o(t[0],r,r),a=parseInt(r/2);return e.customAttributes.arc=function(t,s,i){var r,e=360/s*t,n=(90-e)*Math.PI/180,o=a+i*Math.cos(n),l=a-i*Math.sin(n);return r=s===t?[["M",a,a-i],["A",i,i,0,1,1,a-.01,a-i]]:[["M",a,a-i],["A",i,i,0,+(e>180),1,o,l]],{path:r}},{getPager:function(){return e},getRadial:function(){return a}}},_draw:function(t){var s=this,i=t.text();t.empty();var r=parseFloat(i.split("%")[0]);r>100&&(r=100);var e=s._getPage(t),a=e.getPager(),n=e.getRadial(),o=2*n/3,l=(a.path().attr(s._ringStyles).attr({arc:[100,100,o]}),a.path().attr(s._ringStyles).attr({stroke:s.params.styles["fill-color"],fill:"rgba(0,0,0,0)"}).attr({arc:[.01,150,o]}));return l.animate({arc:[r,100,o]},900,">"),a.text(n,n,r.toFixed(2)+"%").attr(s._fontStyles),s}},i.exports=l,a("Module Ring is loaded.")});