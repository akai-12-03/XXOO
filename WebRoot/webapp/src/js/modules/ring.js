/**
 * Module Ring v1.0
 * 环状进度条
 *
 * @author: lixiaodong
 *
 * @description: 绘画一个圆环进度条，使用 `raphael` 矢量图进行绘画。
 *
 * @parameters:
 *		id: String 				// 默认为 ".circle"
 *
 * 		width: Number 			// 默认为 64，整个元素的宽度
 * 		height: Number 			// 默认为 64，整个元素的高度
 *
 * 		styles: {
 * 			"ring-width": Number 		// 默认为 6，圆环宽度
 *			"ring-color": String 		// 默认为 #xxoo，圆环环的颜色
 *			"back-color": String 		// 默认为 #xxoo，背景色
 *			"fill-color": String 		// 默认为 #xx00，圆环进度颜色
 *
 *			"font-size": Number		// 默认为 14，字体大小
 *			"font-color": String 		// 默认为 black，字体颜色
 * 		}
 *
 *	@usage:
 *	```html
 *		<div class="circle">30%</div>
 *		<div class="mycircle">45%</div>
 *	```
 *	```javascript
 *		var ring = require("ring");
 *		new ring;
 *
 *		new ring(".mycircle", {
 *			"ring-color": "blue",
 *			"font-color": "#0f0"
 *		});
 *	```
 */
define(function(require, exports, module){
	"use strict";

	var $ = require("jQuery");
	var _ = require('log');

	var log = _.log;
	var warn = _.warn;
	var error = _.error;

	var raphael = require("raphael");
	log('raphael is ready, version:', raphael.version);

	if(typeof $ !== 'undefined' && typeof $.fn !== 'undefined' && typeof $.fn.jquery === 'string'){
		_.$();
		log('Module Ring is loading...');
	}
	else{
		error('Can not find jQuery.');
		return;
	}

	var Ring = function(selector, params){
		log('Ring is initializing...');
		this.rings = selector || ".circle";
		this.version = "0.0.1";

		if("string" === typeof this.rings){
			this.rings = $(this.rings);
		}

		if(this.rings.length === 0){
			return this;
		}

		this.params = {};
		this.setConfig(params);

		this._init();
		log('Ring is initialized.');
		return this;
	};

	Ring.prototype = {
		_init: function(){
			var self = this;
			self.rings.each(function(index, item){
				var el = self.rings.eq(index);
				self._draw(el);
			});

			return self;
		},

		setConfig: function(params){
			params = params || {};

			this.params.width = params.width || 0;
			this.params.height = params.height || 0;
			this.params.styles = {};
			params.styles = params.styles || {};
			this.params.styles["ring-width"] = params.styles["ring-width"] || 6;
			this.params.styles["ring-color"] = params.styles["ring-color"] || "#dcdcdc";
			this.params.styles["back-color"] = params.styles["back-color"] || "white";
			this.params.styles["fill-color"] = params.styles["fill-color"] || "#fe852c";

			this.params.styles["font-color"] = params.styles["font-color"] || "#fe852c";
			this.params.styles["font-size"] = params.styles["font-size"] || 14;

			this._ringStyles = {
				"stroke": this.params.styles["ring-color"],
				"fill": this.params.styles["back-color"],
				"stroke-width": this.params.styles["ring-width"]
			};

			this._fontStyles = {
				"font-size": this.params.styles["font-size"],
				"fill": this.params.styles["font-color"],
				"text-anchor": "middle"
			};

			return this;
		},

		_getPage: function(el){
			var width = this.params.width || el.innerWidth() || 64;
			var height = this.params.height || el.innerHeight() || 64;
			var min = (width > height ? height : width);
			var paper = raphael(el[0], min, min);
			var r = parseInt(min / 2);

			paper.customAttributes.arc = function(value, total, R){
				var alpha = 360 / total * value;
				var a = (90 - alpha) * Math.PI / 180;
				var x = r + R * Math.cos(a);
				var y = r - R * Math.sin(a);
				var path;
				if(total === value){
					path = [["M", r, r - R], ["A", R, R, 0, 1, 1, r - 0.01, r - R]];
				}
				else{
					path = [["M", r, r - R], ["A", R, R, 0, +(alpha > 180), 1, x, y]];
				}

				return { path: path };
			};

			return {
				getPager: function(){
					return paper;
				},
				getRadial: function(){
					return r;
				}
			};
		},

		_draw: function(el){
			var self = this;
			var rawText = el.text();
			el.empty();
			var value = parseInt(rawText.split("%")[0]);
			if(100 < value){
				value = 100;
			}

			var paperObj = self._getPage(el);
			var paper = paperObj.getPager();
			var r = paperObj.getRadial();
			var R = r * 2 / 3;

			var circle = paper.path().attr(self._ringStyles).attr({ arc: [100, 100, R]});
			var arc = paper.path()
				.attr(self._ringStyles)
				.attr({
					"stroke": self.params.styles["fill-color"],
					"fill": "rgba(0,0,0,0)"
				})
				.attr({ arc: [0.01, 150, R]});

			arc.animate({ arc: [value, 100, R]}, 900, ">");

			paper.text(r, r, value + "%")
				.attr(self._fontStyles);

			return self;
		}
	};

	module.exports = Ring;
	log('Module Ring is loaded.');
});