define(function(require, exports, module){
	var lang = {
		//Basic
		'Seajs is ready, version:': 'Seajs已经就绪。版本:',
		'jQuery is ready, version:': 'jQuery已经就绪。版本:',
		'Can not find jQuery.': '找不到jQuery。',
		
		//Tab
		'Module Tab is loading...': '正在加载选项卡模块...',
		'Module Tab is loaded.': '选项卡模块加载完成。',
		'Can not find tab container, please initialize tab by yourself.': '找不到选项卡容器，请尝试自行初始化选项卡',
		'Can not find tab headers, please check your html': '找不到选项卡头部，请检查html',
		'tab is initializing...': '选项卡正在初始化...',
		'tab is initialized.': '选项卡初始化完毕。',

		//Accordion
		'Module Accordion is loading...': '正在加载手风琴模块...',
		'Module Accordion is loaded.': '手风琴模块加载完成。',
		'accordion is initializing...': '手风琴正在初始化...',
		'accordion is initialized.': '手风琴初始化完毕。',

		//Form
		'Module Form is loading...': '正在加载表单模块...',
		'Module Form is loaded.': '表单模块加载完成。',
		'form is initializing...': '表单正在初始化...',
		'form is initialized.': '表单初始化完毕。',
		'Can not find Form, please initialize Form by yourself.': '找不到表单，请尝试自行初始化表单',
		'has no attribute name.': '缺少name属性。',
		'has no notice container.': '缺少提示信息的容器。',
		'has no default validator.': '没有默认的校验器。',
		'has no validator type.': '未设置校验类型。',

		//VerifyTel
		'Module VerifyTel is loading...': '正在加载短信验证码模块...',
		'VerifyTel is initializing...': '短信验证码模块正在初始化...',
		'VerifyTel is initialized.': '短信验证码模块初始化完毕。',
		'Module VerifyTel is loaded.': '短信验证码模块加载完成。',
		//VerifyImg
		'Module VerifyImg is loading...': '正在加载图片验证码模块...',
		'VerifyImg is initializing...': '图片验证码模块正在初始化...',
		'VerifyImg is initialized.': '图片验证码模块初始化完毕。',
		'Module VerifyImg is loaded.': '图片验证码模块加载完成。',

		//Validator
		'Module Validator is loading...': '正在加载验证器模块...',
		'Module Validator is loaded.': '验证器模块加载完成。',

		//Progress
		'Module Progress is loading...': '正在加载进度条模块...',
		'Module Progress is loaded.': '进度条模块加载完成。',
		'Can not find progress container.': '找不到进度条容器',
		'Add IE fix.': '增加IE补丁修正Progress的条纹',
		'Progress is initializing...': '进度条模块正在初始化...',
		'Progress is initialized.': '进度条模块初始化完毕。',

		//Ring
		'Module Ring is loading...': '正在加载环状进度条模块...',
		'Module Ring is loaded.': '环状进度条模块加载完成。',
		'Ring is initializing...': '环状进度条模块正在初始化...',
		'Ring is initialized.': '环状进度条模块初始化完毕。',

		//Slider
		'Module Slider is loading...': '正在加载轮播模块...',
		'Module Slider is loaded.': '轮播模块加载完成。',
		'Slider is initializing...': '轮播模块正在初始化...',
		'Slider is initialized.': '轮播模块初始化完毕。',

		//Editor
		'Module Editor is loading...': '正在加载编辑器模块...',
		'Module Editor is loaded.': '编辑器模块加载完成。',
		'Editor is initializing...': '编辑器正在初始化...',
		'Editor is initialized.': '编辑器初始化完毕。',

		//Fancybox
		'Module Fancybox is loading...': '正在加载Fancybox模块...',
		'Module Fancybox is loaded.': 'Fancybox模块加载完成。',
		'fancybox css is loading...': '正在加载fancybox的样式...',
		'fancybox-mousewheel is loading...': '正在加载fancybox的鼠标滚轮补丁...',
		'fancybox is loading...': '正在加载fancybox...',
		'fancybox-buttons is loading...': '正在加载fancybox的button补丁...',

		//Module Dialog is loading...
		'Module Dialog is loading...': '正在加载对话框模块...',
		'Module Dialog is loaded.': '对话框模块加载完成。',
		'dialog is initializing...': '对话框正在初始化...',
		'dialog is initialized.': '对话框初始化完毕。'
	};
	module.exports = function(str){
		if(typeof str === 'string' && typeof lang[str] === 'string'){
			return lang[str];
		}
		else{
			return str;
		}
	};
});