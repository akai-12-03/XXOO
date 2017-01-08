/**
 * Module Progress v1.0
 * 条纹（可动）横向滚动进度条
 *
 * @author: Jerry
 * @email: superzcj_001@163.com
 * @copyright 2015 Jerry
 *
 * default config:
 * {
 *	'width': 400,
 *	'percent': 50,
 *	'color': 'default'
 * }
 */

/*jshint eqnull:true */
define(function(require, exports, module){
	"use strict";

	var $ = require('jQuery');
	var _ = require('log');
	var log = _.log;
	var warn = _.warn;
	var error = _.error;

	if(typeof $ !== 'undefined' && typeof $.fn !== 'undefined' && typeof $.fn.jquery === 'string'){
		_.$();
		log('Module Progress is loading...');
	}
	else{
		error('Can not find jQuery.');
		return;
	}

	var color = [
		'default',
		'yellow',
		'red',
		'blue',
		'black',
		'green'
	];
	var defaultConfig = {
		'width': 'default',
		'percent': 50,
		'color': 'default',
		'animate': true,
		'speed': 1,
		'autorun': true,
		'delay': 0,
		'time': 2000
	};

	var isDefault = function(str){
		if(typeof str === 'string' && str.toLowerCase() === 'default'){
			return true;
		}
		else if(str == null){
			return true;
		}
		else{
			return false;
		}
	};

	var toPercent = function(num){
		if(typeof num === 'number'){
			return isNaN(num) ? 100 : num > 100 ? 100 : num < 0 ? 0 : num;
		}
		else{
			var _num = parseInt(num);
			return toPercent(_num);
		}
	};

	var checkIESupport = function(){
		var regIE = /msie\s+(\d+?\.\d+?)/ig;
		var checkResult = regIE.exec(navigator.userAgent);
		if($.isArray(checkResult) && checkResult.length >= 2){
			var version = parseFloat(checkResult[1]);
			// console.log(version, checkResult[1]);
			if(version - 10 < 0){
				return true;
			}
		}
		return false;
	};

	var progressTpl = '<div class="progress-box"><div class="progress-inner"></div></div><div class="progress-num"></div>';
	var progressIETpl = '<div class="progress-box"><div class="progress-inner progress-ie"></div></div><div class="progress-num"></div>';

	var getConfig = function(el){
		var width = el.data('width');
		var percent = el.data('percent');
		var animate = el.hasClass('animate');
		var color = el.data('color');
		var speed = el.data('speed');
		var autorun = el.data('autorun');
		var delay = el.data('delay');
		var time = el.data('time');
		// console.log(width, percent, animate, color, speed, autorun, delay, time);
		var ret = {};
		if(width){
			ret.width = width;
		}
		// console.log(percent);
		if(percent !== undefined){
			ret.percent = percent;
		}
		if(animate){
			ret.animate = animate;
		}
		if(color){
			ret.color = color;
		}
		if(speed){
			ret.speed = speed;
		}
		if(autorun){
			ret.autorun = autorun;
		}
		if(delay){
			ret.delay = delay;
		}
		if(time){
			ret.time = time;
		}
		return ret;
	};


	var Progress = function(selector, _config){
		// var $this = $(selector);
		log('Progress is initializing...');
		this.selector = selector || '.progress-container';

		if(typeof this.selector === 'string'){
			this.container = $(this.selector);
		}
		else{
			return this;
		}

		if(this.container.length === 0){
			warn('Can not find progress container.');
			return this;
		}

		this.init(this.container, _config);

		if(checkIESupport()){
			warn('Add IE fix.');
			$('.progress-inner').each(function(index, el){
				if(!$(el).hasClass('progress-ie')){
					$(el).addClass('progress-ie');
				}
			});
		}

		if(typeof _config !== 'undefined' && _config.autorun){
			log(12345, this.container);
			this.run(this.container);
		}
		log('Progress is initialized.');
		return this;
	};

	Progress.prototype = {
		init: function($el, _config){

			var $this = this;
			if($el.length > 1){
				$el.each(function(index, el){
					$this.init($(el), _config);
				});
				return this;
			}
			if($el.data('init')){
				return this;
			}
			if($el.find('div.progress').length > 0){
				$this.data('init', true);
				$this.data('end', true);
				return $el;
			}

			var __config = getConfig($el);
			// console.log(__config);
			var config = $.extend({}, defaultConfig, _config, __config);

			var css = {};

			if(!isDefault(config.width)){
				css.width = config.width;
			}

			if(checkIESupport()){
				$el.html(progressIETpl);
			}
			else{
				$el.html(progressTpl);
			}
			var $progress = $el.children('.progress');
			var $progressValue = $el.find('.progress-inner');
			$progress.css(css);
			if($.inArray(config.color.toLowerCase(), color) >= 0){
				if(!isDefault(config.color)){
					$progress.addClass(config.color);
				}
			}
			if(config.animate){
				$progress.addClass('animate');
			}

			$progressValue.css('width', '0%');

			$el.data('config', config);
			$el.data('init', true);

			return $this;
		},
		run: function($el){
			var $this = this;
			if($el.length === 0){
				return $el;
			}

			if($el.length > 1){
				$el.each(function(index, el){
					$this.run($(el));
				});
				return $el;
			}

			// var se = document.documentElement.clientHeight;
			// var top = $el[0].getBoundingClientRect().top;
			// if(top > se){
			// 	$(window).one('scroll', function(e){
			// 		$this.run($el);
			// 	});
			// 	return $el;
			// }

			if($el.data('end')){
				return $el;
			}

			var config = $el.data('config')

			var $progressValue = $el.find('.progress-inner');
			var percent = toPercent(config.percent);
            var $num=$el.find('.progress-num');
            // log($num);
			if(config.autorun){
				$progressValue.css('width', '0%')
					.delay(config.delay)
					.animate({'width': percent + '%'}, config.time, function(){
						$el.data('end', true);
					});
			}
			else{
				$progressValue.css('width', percent + '%');
				$el.data('end', true);
			}
             $num.text(percent+ '%');
			return $this;
		}
	};

	module.exports = Progress;
	log('Module Progress is loaded.');
});
