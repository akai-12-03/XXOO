define(function(require, exports, module){
	"use strict";

	var $ = require('jQuery');
	var _log = require('log');

	var log = _log.log;
	var error = _log.error;

	if(typeof $ !== 'undefined' && typeof $.fn !== 'undefined' && typeof $.fn.jquery === 'string'){
		_log.$();
		log('Module Accordion is loading...');

		var Accordion = function(selector, option){
			log('accordion is initializing...');
			this.selector = selector || '.accordion';
			
			if(typeof this.selector === 'string'){
				this.accordion = $(this.selector);
			}
			else{
				return this;
			}

			if(this.accordion.length === 0){
				error('Can not find accordion container, please initialize accordion by yourself.','[' + this.selector + ']');
				return this;
			}

			var ret = this._init(this.accordion, option);

			log('accordion is initialized.');
			return ret;
		};

		Accordion.prototype = {
			_init: function($el, option){
				var $this = this;
				if($el.length > 1){
					$el.each(function(index, el){
						$this._init($(el), option);
					});
					return $el;
				}
				$el.find('li>.accordion-title').on('click', function(){
					if($(this).parent('li').hasClass('active')){
						$(this).parent('li').removeClass('active').children('ul').slideUp('fast');
					}
					else{
						$(this).parent('li').addClass('active').children('ul').slideDown('fast');
						$(this).parent('li').siblings('li').removeClass('active').children('ul').slideUp('fast');
					}
				});

				$el.find('li.active>ul').first().slideDown('fast');
				$el.find('li.active').not(':first()').removeClass('active');
				return $el;
			}
		};

		module.exports = Accordion;

		log('Module Accordion is loaded.');
	}
	else{
		error('Can not find jQuery.');
	}

});