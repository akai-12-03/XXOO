/*! Tomcat360.com (c) 2015 
	Author: Renzhao
*/
define("modules/cookie",[],function(t,e,r){"use strict";var i=window.document;Array.prototype.indexOf||(Array.prototype.indexOf=function(t,e){var r;if(null==this)throw new TypeError('"this" is null or not defined');var i=Object(this),n=i.length>>>0;if(0===n)return-1;var o=+e||0;if(Math.abs(o)===1/0&&(o=0),o>=n)return-1;for(r=Math.max(o>=0?o:n-Math.abs(o),0);n>r;){if(r in i&&i[r]===t)return r;r++}return-1}),r.exports={list:function(){for(var t=i.cookie,e=t.split("; "),r={},n=0;n<e.length;n++){var o=e[n].split("=");r[o[0]]=o[1]}return r},get:function(t){var e=this.list();return void 0!==t?e[t]:e},set:function(t,e,r){var n=t+"="+escape(e);"number"==typeof r&&r>0||(r=30);var o=new Date,s=3600*r*1e3;return o.setTime(o.getTime()+s),n+="; expires"+o.toGMTString(),i.cookie=n,this.get(t)},add:function(t,e){var r=this.get(t);if(void 0===r)return this.set(t,e);if(""===r)return this.set(t,e);var n=r.split(","),o=n.indexOf(escape(e));0>o&&n.push(escape(e));var s=n.join(","),a=t+"="+s+"; ";return i.cookie=a,this.get(t)},reply:function(t,e){for(var r=this.get(t),n=[],o=r.split(","),s=0;s<o.length;s++)o[s]!==e.toString()&&n.push(o[s]);var a=n.join(","),u=t+"="+a+"; ";return i.cookie=u,this.get(t)},del:function(t){return i.cookie=t+"=;expires="+new Date(0).toGMTString(),this.list()}}});