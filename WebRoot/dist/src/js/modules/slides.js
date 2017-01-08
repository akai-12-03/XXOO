/**
 *  简易轮播
 *
 *  @description: 一个真的很简易的轮播，哪天空闲就重写了吧。
 *
 *  @parameters:
 *      id: String      // 默认为 '.slider'，只取第一个。
 *
 *  @usage:
 *  ```html
 *      <div class='slider'>
 *          <a class='slide' data-img='path/to/img'></a>
 *      </div>
 *  ```
 *
 *  ```javascript
 *      new slider;
 *  ```
 */

define(function(require, exports, module){
	"use strict";

	var $ = require('jQuery');
	var _ = require('log');

	var log = _.log;
	var warn = _.warn;
	var error = _.error;

	if(typeof $ !== 'undefined' && typeof $.fn !== 'undefined' && typeof $.fn.jquery === 'string'){
		_.$();
		log('Module Slider is loading...');
	}
	else{
		error('Can not find jQuery.');
		return;
	}

	var defaultConfig = {
		'interval': 10
	};
	
	var Slider = function(selector, config){
		log('Slider is initializing...');
		this.selector = selector || '.slider';

		this.config = $.extend({}, defaultConfig, config);
		this.interval = this.config.interval;
		this.version = '0.0.1';

		if(typeof this.selector === 'string'){
			this.container = $(this.selector);
		}
		else{
			return this;
		}

		if(this.container.length === 0){
			error('Can not find slide container, please initialize slide by yourself.','[' + this.selector + ']');
			return this;
		}

		this.slides = this.container.find('.slide');
		this.controllers = this._createControllers(this.slides);

		this.index = 0;
		this.autoPlayId = -1;

		this._init();
		log('Slider is initialized.');
		return this;
	};

	Slider.prototype = {
		_init: function(){
			this.slides.hide();
			this.slides.eq(this.index).show();

			var self = this;
			var style = '<style>';
			this.slides.each(function(index, item){
				item = $(item);
				var img = item.data('img');
				var itemClass = 'slide-'+index.toString();
				item.addClass(itemClass);
				style += '.' + itemClass + '{\n\tbackground-image: url("' + img + '");\n}\n';
				item.removeAttr('data-img');
			});
			style += '</style>';
			$('head').append(style);

			self._bindEvents();

			return self.autoPlay();
		},

		_createControllers: function(slides){
			var self = this;
			var controller = $('<ol></ol>').appendTo(this.container);
			controller.addClass('controller');

			slides.each(function(index, item){
				item = $(item);
				var li = $('<li></li>').appendTo(controller);
				li.click(function(){
					self._bindEvents();
				});
			});

			return controller.children();
		},

		_bindEvents: function(){
			var self = this;

			self.controllers.removeClass('active');
			self.controllers.first().addClass('active');

			self.controllers.click(function(){
				var item = $(this);
				var index = self.controllers.index(item);

				self.playNext(index);
			});

			return self;
		},

		playNext: function(nextIndex){
			if(this.index === nextIndex){
				return this;
			}

			if(this.autoPlayId){
				window.clearInterval(this.autoPlayId);
			}

			this.slides.eq(this.index).fadeOut();
			this.slides.eq(nextIndex).fadeIn();

			this.controllers
				.removeClass('active')
				.eq(nextIndex)
				.addClass('active');

			this.index = nextIndex;

			return this.autoPlay();
		},

		autoPlay: function(){
			var self = this;
			var slideSize = self.slides.length;

			self.autoPlayId = window.setInterval(function(){
				self.playNext((self.index + 1) % slideSize);
			}, this.interval * 1000);

			return self;
		}
	};

	module.exports = Slider;
	log('Module Slider is loaded.');
});
