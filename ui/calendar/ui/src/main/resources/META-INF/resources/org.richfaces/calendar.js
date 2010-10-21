/*if(!window.LOG){
	window.LOG = {warn:function(){}};
}*/

// TODO: try to change RichFaces.$ to $$ if possible

(function ($, rf) {
	
	rf.ui = rf.ui || {};
	
	//calendar templates
	var CalendarView = {
		getControl: function(text, attributes, functionName, paramsStr) {
			var attr = $.extend({
				onclick: (functionName ? "RichFaces.$$('Calendar',this)."+functionName+"("+(paramsStr ? paramsStr : "")+");" : "")+"return true;"
			}, attributes);
			return new E('div',attr,[new T(text)]);
		},

		getSelectedDateControl: function(calendar) {
			
			if (!calendar.selectedDate || calendar.params.showApplyButton) return "";
			
			var text = rf.calendarUtils.formatDate(calendar.selectedDate,(calendar.timeType ? calendar.datePattern : calendar.params.datePattern), calendar.params.monthLabels, calendar.params.monthLabelsShort);
			var onclick = "RichFaces.$$('Calendar',this).showSelectedDate(); return true;"
			var markup = ( calendar.params.disabled ? 
							new E('div', {'class': 'rich-calendar-tool-btn-disabled'}, [new ET(text)]) : 
							new E('div', {'class': 'rich-calendar-tool-btn', 'onclick': onclick}, [new ET(text)]) );
	
			return markup;
		},

		getTimeControl: function(calendar) {
			
			if (!calendar.selectedDate || !calendar.timeType) return "";
			
			var text = rf.calendarUtils.formatDate(calendar.selectedDate, calendar.timePattern, calendar.params.monthLabels, calendar.params.monthLabelsShort);
	
			var onmouseover = "jQuery(this).removeClass('rich-calendar-tool-btn-press');";
			var onmouseout = "jQuery(this).addClass('rich-calendar-tool-btn-press');";
			var onclick = "RichFaces.$$('Calendar',this).showTimeEditor();return true;";
			var markup = calendar.params.disabled || calendar.params.readonly ? 
						new E('div', {'class': 'rich-calendar-tool-btn-disabled'}, [new ET(text)]) : 
						new E('div', {'class': 'rich-calendar-tool-btn rich-calendar-tool-btn-hover rich-calendar-tool-btn-press', 'onclick': onclick,
								'onmouseover': + onmouseover ,
								'onmouseout' : + onmouseout}, [new ET(text)]);
	
			return markup;
		},

		toolButtonAttributes: {className: "rich-calendar-tool-btn", onmouseover:"this.className='rich-calendar-tool-btn rich-calendar-tool-btn-hover'", onmouseout:"this.className='rich-calendar-tool-btn'", onmousedown:"this.className='rich-calendar-tool-btn rich-calendar-tool-btn-hover rich-calendar-tool-btn-press'", onmouseup:"this.className='rich-calendar-tool-btn rich-calendar-tool-btn-hover'"},
		nextYearControl: function (context) {
			return (!context.calendar.params.disabled ? CalendarView.getControl(">>", CalendarView.toolButtonAttributes, "nextYear") : "");
		},
		previousYearControl: function (context) {
			return (!context.calendar.params.disabled ? CalendarView.getControl("<<", CalendarView.toolButtonAttributes, "prevYear") : "");
		},
		nextMonthControl: function (context) {
			return (!context.calendar.params.disabled ? CalendarView.getControl(">", CalendarView.toolButtonAttributes, "nextMonth") : "");
		},
		previousMonthControl: function (context) {
			return (!context.calendar.params.disabled ? CalendarView.getControl("<", CalendarView.toolButtonAttributes, "prevMonth") : "");
		},
		currentMonthControl: function (context) {
			var text = rf.calendarUtils.formatDate(context.calendar.getCurrentDate(), "MMMM, yyyy", context.monthLabels, context.monthLabelsShort);
			var markup = context.calendar.params.disabled ?
						 new E('div',{className: "rich-calendar-tool-btn-disabled"},[new T(text)]) :
						 CalendarView.getControl(text, CalendarView.toolButtonAttributes, "showDateEditor");
			return markup;
		},
		todayControl: function (context) {
			return (!context.calendar.params.disabled && context.calendar.params.todayControlMode!='hidden' ? CalendarView.getControl(context.controlLabels.today, CalendarView.toolButtonAttributes, "today") : "");
		},
		closeControl: function (context) {
			return (context.calendar.params.popup ? CalendarView.getControl(context.controlLabels.close, CalendarView.toolButtonAttributes, "close", "false") : "");
		},
		applyControl: function (context) {
			return (!context.calendar.params.disabled && !context.calendar.params.readonly && context.calendar.params.showApplyButton ? CalendarView.getControl(context.controlLabels.apply, CalendarView.toolButtonAttributes, "close", "true") : "");
		},
		cleanControl: function (context) {
			return (!context.calendar.params.disabled && !context.calendar.params.readonly && context.calendar.selectedDate ? CalendarView.getControl(context.controlLabels.clean, CalendarView.toolButtonAttributes, "resetSelectedDate") : "");
		},
	
		selectedDateControl: function (context) {	return CalendarView.getSelectedDateControl(context.calendar);},
		timeControl: function (context) {	return CalendarView.getTimeControl(context.calendar);},
		timeEditorFields: function (context) {return context.calendar.timePatternHtml;},

		header: [
			new E('table',{'border': '0', 'cellpadding': '0', 'cellspacing': '0', 'width': '100%'},
				[
					new E('tbody',{},
					[
						new E('tr',{},
						[
							new E('td',{'class': 'rich-calendar-tool'},
							[
								new ET(function (context) { return rf.calendarTemplates.evalMacro("previousYearControl", context)})
							]),
							new E('td',{'class': 'rich-calendar-tool'},
							[
								new ET(function (context) { return rf.calendarTemplates.evalMacro("previousMonthControl", context)})
							]),
							new E('td',{'class': 'rich-calendar-month'},
							[
								new ET(function (context) { return rf.calendarTemplates.evalMacro("currentMonthControl", context)})
							]),				
							new E('td',{'class': 'rich-calendar-tool'},
							[
								new ET(function (context) { return rf.calendarTemplates.evalMacro("nextMonthControl", context)})
							]),
							new E('td',{'class': 'rich-calendar-tool'},
							[
								new ET(function (context) { return rf.calendarTemplates.evalMacro("nextYearControl", context)})
							]),
							new E('td',{'class': 'rich-calendar-tool rich-calendar-tool-close', 'style':function(context){return (this.isEmpty ? 'display:none;' : '');}},
							[
								new ET(function (context) { return rf.calendarTemplates.evalMacro("closeControl", context)})
							])
						])
					])
				]
			)],
		
		footer: [
			new E('table',{'border': '0', 'cellpadding': '0', 'cellspacing': '0', 'width': '100%'},
				[
					new E('tbody',{},
					[
						new E('tr',{},
						[
							new E('td',{'class': 'rich-calendar-toolfooter', 'style':function(context){return (this.isEmpty ? 'display:none;' : '');}},
							[
								new ET(function (context) { return rf.calendarTemplates.evalMacro("selectedDateControl", context)})
							]),
							new E('td',{'class': 'rich-calendar-toolfooter', 'style':function(context){return (this.isEmpty ? 'display:none;' : '');}},
							[
								new ET(function (context) { return rf.calendarTemplates.evalMacro("cleanControl", context)})
							]),
							new E('td',{'class': 'rich-calendar-toolfooter', 'style':function(context){return (this.isEmpty ? 'display:none;' : '');}},
							[
								new ET(function (context) { return rf.calendarTemplates.evalMacro("timeControl", context)})
							]),
							new E('td',{'class': 'rich-calendar-toolfooter', 'style': 'background-image:none;', 'width': '100%'}, []),
							new E('td',{'class': 'rich-calendar-toolfooter', 'style':function(context){return (this.isEmpty ? 'display:none;' : '')+(context.calendar.params.disabled || context.calendar.params.readonly || !context.calendar.params.showApplyButton ? 'background-image:none;' : '');}},
							[
								new ET(function (context) { return rf.calendarTemplates.evalMacro("todayControl", context)})
							]),
							new E('td',{'class': 'rich-calendar-toolfooter', 'style':function(context){return (this.isEmpty ? 'display:none;' : '')+'background-image:none;';}},
							[
								new ET(function (context) { return rf.calendarTemplates.evalMacro("applyControl", context)})
							])
						])
					])
				]
			)],
		
		timeEditorLayout: [
	
		        new E('table',{'id': function(context){return context.calendar.TIME_EDITOR_LAYOUT_ID}, 'border': '0', 'cellpadding': '0', 'cellspacing': '0', 'class': 'rich-calendar-time-layout'},
				[
					new E('tbody',{},
					[
						new E('tr',{},
						[
							new E('td',{'class': 'rich-calendar-time-layout-fields', 'colspan': '2', 'align': 'center'},
							[
								new ET(function (context) { return rf.calendarTemplates.evalMacro("timeEditorFields", context)})
							])
						]),
						new E('tr',{},
						[
							new E('td',{'class': 'rich-calendar-time-layout-ok'},
							[
								new E('div',{'id': function(context){return context.calendar.TIME_EDITOR_BUTTON_OK}, 'class': 'rich-calendar-time-btn', 'style': 'float:right;', 'onmousedown': "jQuery(this).addClass('rich-calendar-time-btn-press');", 'onmouseout': "jQuery(this).removeClass('rich-calendar-time-btn-press');", 'onmouseup': "jQuery(this).removeClass('rich-calendar-time-btn-press');", 'onclick': function(context){return "RichFaces.$('"+context.calendar.id+"').hideTimeEditor(true)";}},
								[
									new E('span',{},
									[
										new ET(function (context) { return context.controlLabels.ok; })
									])
								])
							])
							,
							new E('td',{'class': 'rich-calendar-time-layout-cancel'},
							[
								new E('div',{'id': function(context){return context.calendar.TIME_EDITOR_BUTTON_CANCEL}, 'class': 'rich-calendar-time-btn', 'style': 'float:left;', 'onmousedown': "jQuery(this).addClass('rich-calendar-time-btn-press');", 'onmouseout': "jQuery(this).removeClass('rich-calendar-time-btn-press');", 'onmouseup': "jQuery(this).removeClass('rich-calendar-time-btn-press');", 'onclick': function(context){return "RichFaces.$('"+context.calendar.id+"').hideTimeEditor(false)";}},
								[
									new E('span',{},
									[
										new ET(function (context) { return context.controlLabels.cancel; })
									])
								])
							])
						])
					])
				]
			)],

		dayList: [new ET(function (context) { return context.day})],
		weekNumber: [new ET(function (context) { return context.weekNumber})],
		weekDay: [new ET(function (context) { return context.weekDayLabelShort})]
	};
	// calendar templates end
	
	// calendar context
	var CalendarContext = function(calendar) {
    	this.calendar=calendar;
		this.monthLabels=calendar.params.monthLabels;
		this.monthLabelsShort=calendar.params.monthLabelsShort;
		this.weekDayLabels=calendar.params.weekDayLabels;
		this.weekDayLabelsShort=calendar.params.weekDayLabelsShort;
		this.controlLabels=calendar.params.labels;
	};
	
	$.extend(CalendarContext.prototype, {
		nextYearControl: CalendarView.nextYearControl,
		previousYearControl: CalendarView.previousYearControl,
		nextMonthControl: CalendarView.nextMonthControl,
		previousMonthControl: CalendarView.previousMonthControl,
		currentMonthControl: CalendarView.currentMonthControl,
		selectedDateControl: CalendarView.selectedDateControl,
		cleanControl: CalendarView.cleanControl,
		timeControl: CalendarView.timeControl,
		todayControl: CalendarView.todayControl,
		closeControl: CalendarView.closeControl,
		applyControl: CalendarView.applyControl,
		timeEditorFields: CalendarView.timeEditorFields,
	});

	// must be :defaultTime, minDaysInFirstWeek, firstWeekday, weekDayLabels, weekDayLabelsShort, monthLabels, monthLabelsShort
	
	// defaults definition
	var defaultOptions = {
			showWeekDaysBar: true,
			showWeeksBar: true,
			datePattern: "MMM d, yyyy",
			horizontalOffset: 0,
			verticalOffset: 0,
			dayListMarkup: CalendarView.dayList,
			weekNumberMarkup: CalendarView.weekNumber,
			weekDayMarkup: CalendarView.weekDay,
			headerMarkup: CalendarView.header,
			footerMarkup: CalendarView.footer,
			isDayEnabled: function (context) {return true;},
			dayStyleClass: function (context) {return "";},
			showHeader: true,
			showFooter: true,
			direction: "AA",
			jointPoint: "AA",
			popup: true,
			boundaryDatesMode: "inactive",
			todayControlMode: "select",
			style: "",
			className: "",
			disabled: false,
			readonly: false,
			enableManualInput: false,
			showInput: true,
			resetTimeOnDateSelect: false,
			style: "z-index: 3;",
			showApplyButton: false,
			selectedDate: null,
			currentDate: null,
			defaultTime: {hours:12,minutes:0, seconds:0},
			hidePopupOnScroll: true
	};
	
	var defaultLabels = {apply:'Apply', today:'Today', clean:'Clean', ok:'OK', cancel:'Cancel', close:'x'};
	
	// Constructor definition
	rf.ui.Calendar = function(componentId, locale, options, markups) {
		
		// dayListMarkup - day cell markup
		//		context: {day, date, weekNumber, weekDayNumber, isWeekend, isCurrentMonth,  elementId, component}
		// weekNumberMarkup - week number cell markup
		//		context: {weekNumber, elementId, component}
		// weekDayMarkup - week day cell markup
		//		context: {weekDayLabel, weekDayLabelShort, weekDayNumber, isWeekend, elementId, component}

		// headerMarkup
		// footerMarkup
		// optionalHeaderMarkup - user defined header (optional)
		// optionalFooterMarkup - user defined footer (optional)
		
		// currentDate - date to show month (day not used) (mm/yyyy) 
		// selectedDate - selected date (mm/dd/yyyy)
		// weekDayLabels - collection of week day labels keyed by week day numbers
		// weekDayLabelsShort - collection of week day short labels keyed by week day numbers
		// minDaysInFirstWeek - locale-specific constant defining number of days in the first week
		// firstWeekDay - (0..6) locale-specific constant defining number of the first week day
		// showWeekDaysBar - show WeekDays Bar [default value is true]
		// showWeeksBar - show Weeks numbers bar [default value is true]
		// showApplyButton
		// showHeader
		// showFooter
		
		// POPUP description
		// direction - [top-left, top-right, bottom-left, bottom-right, auto]
		// jointPoint - [top-left, top-right, bottom-left, bottom-right]
		// popup - true
		// id+PopupButton, id+InputDate,  
				
		// boundaryDatesMode - boundary dates onclick action:
		// 						"inactive" or undefined - no action (default)
		//						"scroll" - change current month
		//						"select" - change current month and select date
		//
		// todayControlMode - today control onclick action:
		//						"scroll"
		//						"select"
		//						"hidden"
		
		// isDayEnabled - end-developer JS function
		// dayStyleClass - end-developer JS function that provide style class for day's cells.
		
		// dayCellClass - add div to day cell with class 'rich-calendar-cell-div' and add this class to TD if defined  
		// style - table style
		// styleClass - table class
		
		// disabled
		// readonly
		
		//var _d = new Date();
		
		// call constructor of parent class
		$super.constructor.call(this, componentId);

		this.namespace = "."+rf.Event.createNamespace(this.name, componentId);
		
		//create parameters
		//this.options = $.extend(this.options, defaultOptions, options);
		this.params = $.extend({}, defaultOptions, locales[locale], options, markups);
		
		// labels
		var value = options.labels || {};
		for (var name in defaultLabels) {
			if (!value[name]) value[name] = defaultLabels[name];
		}
		this.params.labels = value;
		
		this.popupOffset = {dx:this.params.horizontalOffset, dy:this.params.verticalOffset};
			
		//
		if (!this.params.popup) this.params.showApplyButton = false;
		
		//
		this.params.boundaryDatesMode = this.params.boundaryDatesMode.toLowerCase();
		this.params.todayControlMode = this.params.todayControlMode.toLowerCase();
			
		// time
		this.setTimeProperties();
			
		this.customDayListMarkup = (this.params.dayListMarkup!=CalendarView.dayList);
			
		this.currentDate = this.params.currentDate ? this.params.currentDate : (this.params.selectedDate ? this.params.selectedDate : new Date());
		this.currentDate.setDate(1);
		this.selectedDate = this.params.selectedDate;
					
		this.todayDate = new Date();
			
		this.firstWeekendDayNumber = 6-this.params.firstWeekDay;
		this.secondWeekendDayNumber = (this.params.firstWeekDay>0 ? 7-this.params.firstWeekDay : 0);
			
		this.calendarContext = new CalendarContext(this);
		
		// TODO: move it from constructor
		this.DATE_ELEMENT_ID = this.id+'DayCell';
		this.WEEKNUMBER_BAR_ID = this.id+"WeekNum";
		this.WEEKNUMBER_ELEMENT_ID = this.WEEKNUMBER_BAR_ID+'Cell';
		this.WEEKDAY_BAR_ID = this.id+"WeekDay";
		this.WEEKDAY_ELEMENT_ID = this.WEEKDAY_BAR_ID+'Cell';
		this.POPUP_ID = this.id+'Popup';
		this.POPUP_BUTTON_ID = this.id+'PopupButton';
		this.INPUT_DATE_ID = this.id+'InputDate';
		this.EDITOR_ID = this.id+'Editor';
		this.EDITOR_SHADOW_ID = this.id+'EditorShadow';

		this.TIME_EDITOR_LAYOUT_ID = this.id+'TimeEditorLayout';
		this.DATE_EDITOR_LAYOUT_ID = this.id+'DateEditorLayout';
		this.EDITOR_LAYOUT_SHADOW_ID = this.id+'EditorLayoutShadow';
		this.TIME_EDITOR_BUTTON_OK = this.id+'TimeEditorButtonOk';
		this.TIME_EDITOR_BUTTON_CANCEL = this.id+'TimeEditorButtonCancel';
		this.DATE_EDITOR_BUTTON_OK = this.id+'DateEditorButtonOk';
		this.DATE_EDITOR_BUTTON_CANCEL = this.id+'DateEditorButtonCancel';
			
		this.firstDateIndex = 0;
		
		this.daysData = {startDate:null, days:[]};
		this.days = [];
		this.todayCellId = null;
		this.todayCellColor = "";

		this.selectedDateCellId = null;
		this.selectedDateCellColor = "";
		
		var popupStyles = "";
		this.isVisible = true;
		if (this.params.popup==true)
		{
			// popup mode initialisation
			popupStyles = "display:none; position:absolute;"
			this.isVisible = false;
		}

		var tempStr = "RichFaces.$('"+this.id+"').";

		var htmlTextHeader = '<table id="'+this.id+'" border="0" cellpadding="0" cellspacing="0" class="rich-calendar-exterior rich-calendar-popup '+this.params.styleClass+'" style="'+popupStyles+this.params.style+'" onclick="'+tempStr+'skipEventOnCollapse=true;"><tbody>';
		var colspan = (this.params.showWeeksBar ? "8" : "7");
		var htmlHeaderOptional = (this.params.optionalHeaderMarkup) ? '<tr><td class="rich-calendar-header-optional" colspan="'+colspan+'" id="'+this.id+'HeaderOptional"></td></tr>' : '';
		var htmlFooterOptional = (this.params.optionalFooterMarkup) ? '<tr><td class="rich-calendar-footer-optional" colspan="'+colspan+'" id="'+this.id+'FooterOptional"></td></tr>' : '';
		var htmlControlsHeader = (this.params.showHeader ? '<tr><td class="rich-calendar-header" colspan="'+colspan+'" id="'+this.id+'Header"></td></tr>' : '');
		var htmlControlsFooter = (this.params.showFooter ? '<tr><td class="rich-calendar-footer" colspan="'+colspan+'" id="'+this.id+'Footer"></td></tr>' : '');
		var htmlTextFooter = '</tbody></table>'

		// days bar creation
		var styleClass;
		var bottomStyleClass;
		var htmlTextWeekDayBar=[];
		var context;

		var eventsStr = this.params.disabled || this.params.readonly ? '' : 'onclick="'+tempStr+'eventCellOnClick(event, this);" onmouseover="'+tempStr+'eventCellOnMouseOver(event, this);" onmouseout="'+tempStr+'eventCellOnMouseOut(event, this);"';	
		if (this.params.showWeekDaysBar)
		{ 
			htmlTextWeekDayBar.push('<tr id="'+this.WEEKDAY_BAR_ID+'">');
			if (this.params.showWeeksBar) htmlTextWeekDayBar.push('<td class="rich-calendar-days"><br/></td>');
			var weekDayCounter = this.params.firstWeekDay;
			for (var i=0;i<7;i++)
			{
				context = {weekDayLabel: this.params.weekDayLabels[weekDayCounter], weekDayLabelShort: this.params.weekDayLabelsShort[weekDayCounter], weekDayNumber:weekDayCounter, isWeekend:this.isWeekend(i), elementId:this.WEEKDAY_ELEMENT_ID+i, component:this}; 
				var weekDayHtml = this.evaluateMarkup(this.params.weekDayMarkup, context );
				if (weekDayCounter==6) weekDayCounter=0; else weekDayCounter++;

				styleClass = "rich-calendar-days";
				if (context.isWeekend)
				{
					styleClass += " rich-calendar-weekends";
				}
				if (i==6) styleClass += " rich-right-cell";
				htmlTextWeekDayBar.push('<td class="'+styleClass+'" id="'+context.elementId+'">'+weekDayHtml+'</td>');
			}
			htmlTextWeekDayBar.push('</tr>\n');
		}

		// week & weekNumber creation
		var htmlTextWeek=[];
		var p=0;
		this.dayCellClassName = [];

		for (k=1;k<7;k++)
		{
			bottomStyleClass = (k==6 ? "rich-bottom-cell " : "");			
			htmlTextWeek.push('<tr id="'+this.WEEKNUMBER_BAR_ID+k+'">');
			if (this.params.showWeeksBar)
			{
				context = {weekNumber: k, elementId:this.WEEKNUMBER_ELEMENT_ID+k, component:this}; 
				var weekNumberHtml = this.evaluateMarkup(this.params.weekNumberMarkup, context );
				htmlTextWeek.push('<td class="rich-calendar-week '+bottomStyleClass+'" id="'+context.elementId+'">'+weekNumberHtml+'</td>');
			}
			
			// day cells creation 
			for (var i=0;i<7;i++)
			{
				styleClass = bottomStyleClass+(!this.params.dayCellClass ? "rich-calendar-cell-size" : (!this.customDayListMarkup ? this.params.dayCellClass : ""))+" rich-calendar-cell";
				if (i==this.firstWeekendDayNumber || i==this.secondWeekendDayNumber) styleClass+=" rich-calendar-holly";
				if (i==6) styleClass+=" rich-right-cell";
				
				this.dayCellClassName.push(styleClass);
				htmlTextWeek.push('<td class="'+styleClass+'" id="'+this.DATE_ELEMENT_ID+p+'" '+
				eventsStr+
				'>'+(this.customDayListMarkup ? '<div class="rich-calendar-cell-div'+(this.params.dayCellClass ? ' '+this.params.dayCellClass : '')+'"></div>' : '')+'</td>');
				p++;
			}
			htmlTextWeek.push('</tr>');
		}
			
		var div = rf.getDomElement(this.id);
		div = $(div).replaceWith(htmlTextHeader+htmlHeaderOptional+htmlControlsHeader+htmlTextWeekDayBar.join('')+htmlTextWeek.join('')+htmlControlsFooter+htmlFooterOptional+htmlTextFooter);				
		this.attachToDom(this.id); // TODO: optimize double $
		
		// memory leaks fix // from old 3.3.x code, may be not needed now
		div = null;
		
		// add onclick event handlers to input field and popup button
		if (this.params.popup && !this.params.disabled)
		{
			var handler = new Function ('event', "RichFaces.$('"+this.id+"').doSwitch();");
			rf.Event.bindById(this.POPUP_BUTTON_ID, "click"+this.namespace, handler, this);
			if (!this.params.enableManualInput) 
			{
				rf.Event.bindById(this.INPUT_DATE_ID, "click"+this.namespace, handler, this);				
			}
		}
		
		this.scrollElements = null;
		
		//alert(new Date().getTime()-_d.getTime());
			
	};
		
	// Extend component class and add protected methods from parent class to our container
	rf.BaseComponent.extend(rf.ui.Calendar);

	// define super class link
	var $super = rf.ui.Calendar.$super;
	
	// static methods definition
	var locales = {};

	rf.ui.Calendar.addLocale = function (locale, symbols) {
		if (!locales[locale]) {
			locales[locale] = symbols;
		}
	};
	
	/*
	 * Prototype definition
	 */
	$.extend(rf.ui.Calendar.prototype, {
		name: "Calendar",
		destructor: function()
		{
			if (this.params.popup && this.isVisible)
			{
				this.scrollElements && rf.Event.unbindScrollEventHandlers(this.scrollElements, this);
				this.scrollElements = null;
				rf.Event.unbind(window.document, "click"+this.namespace);
			}
		},
		
		dateEditorSelectYear: function(value)
		{
			if (this.dateEditorYearID)
			{
				$(rf.getDomElement(this.dateEditorYearID)).removeClass('rich-calendar-editor-btn-selected');
			}
			this.dateEditorYear = this.dateEditorStartYear + value;
			this.dateEditorYearID = this.DATE_EDITOR_LAYOUT_ID+'Y'+value;
			$(rf.getDomElement(this.dateEditorYearID)).addClass('rich-calendar-editor-btn-selected');
		},
		
		dateEditorSelectMonth: function(value)
		{
			this.dateEditorMonth = value;
			$(rf.getDomElement(this.dateEditorMonthID)).removeClass('rich-calendar-editor-btn-selected');
			this.dateEditorMonthID = this.DATE_EDITOR_LAYOUT_ID+'M'+value;
			$(rf.getDomElement(this.dateEditorMonthID)).addClass('rich-calendar-editor-btn-selected');
		},
		
		scrollEditorYear: function(value)
		{
			var element = rf.getDomElement(this.DATE_EDITOR_LAYOUT_ID+'TR');

			if (this.dateEditorYearID)
			{
				$(rf.getDomElement(this.dateEditorYearID)).removeClass('rich-calendar-editor-btn-selected');
				this.dateEditorYearID='';
			}

			if (!value)
			{
				// update month selection when open editor (value == 0)
				if (this.dateEditorMonth != this.getCurrentMonth())
				{
					this.dateEditorMonth = this.getCurrentMonth();
					$(rf.getDomElement(this.dateEditorMonthID)).removeClass('rich-calendar-editor-btn-selected');
					this.dateEditorMonthID = this.DATE_EDITOR_LAYOUT_ID+'M'+this.dateEditorMonth;
					$(rf.getDomElement(this.dateEditorMonthID)).addClass('rich-calendar-editor-btn-selected');
				}			
			}
			
			if (element)
			{
				var div;
				var year = this.dateEditorStartYear = this.dateEditorStartYear+value*10;
				for (var i=0;i<5;i++)
				{
					element = element.nextSibling;
					div = element.firstChild.nextSibling.nextSibling;
					div.firstChild.innerHTML=year;
					if (year == this.dateEditorYear)
					{
						$(div.firstChild).addClass('rich-calendar-editor-btn-selected');
						this.dateEditorYearID = div.firstChild.id;
					}
					div = div.nextSibling;
					div.firstChild.innerHTML=year+5;
					if (year+5  == this.dateEditorYear)
					{
						$(div.firstChild).addClass('rich-calendar-editor-btn-selected');
						this.dateEditorYearID = div.firstChild.id;
					}
					year++;
				}
			}
		},
		
		updateDateEditor: function()
		{
			this.dateEditorYear = this.getCurrentYear();
			this.dateEditorStartYear = this.getCurrentYear() - 4;
			this.scrollEditorYear(0);
		},

		updateTimeEditor: function()
		{
			var th=rf.getDomElement(this.id+'TimeHours');
			var ts=rf.getDomElement(this.id+'TimeSign');
			var tm=rf.getDomElement(this.id+'TimeMinutes');
					
			var h = this.selectedDate.getHours();
			var m = this.selectedDate.getMinutes();
			if (this.timeType==2)
			{
				var a = (h<12 ? 'AM' : 'PM');
				ts.value = a;
				h = (h==0 ? '12' : (h>12 ? h-12 : h));
			}
			th.value = (this.timeHoursDigits==2 && h<10 ? '0'+h : h);
			tm.value = (m<10 ? '0'+m : m);
			
			if (this.showSeconds) {
				var tsec=rf.getDomElement(this.id+'TimeSeconds');
				var s = this.selectedDate.getSeconds();
				tsec.value = (s<10 ? '0'+s : s);
			}
		},


		createEditor: function()
		{
			var element = $(rf.getDomElement(this.id));
			var zindex = parseInt(element.css('z-index'), 10);
			var htmlBegin = '<div id="'+this.EDITOR_SHADOW_ID+'" class="rich-calendar-editor-shadow" style="position:absolute; display:none;z-index:'+zindex+'"></div><table border="0" cellpadding="0" cellspacing="0" id="'+this.EDITOR_ID+'" style="position:absolute; display:none;z-index:'+(zindex+1)+'" onclick="RichFaces.$(\''+this.id+'\').skipEventOnCollapse=true;"><tbody><tr><td class="rich-calendar-editor-container" align="center"><div style="position:relative; width:100%">';
			var htmlContent = '<div id="'+this.EDITOR_LAYOUT_SHADOW_ID+'" class="rich-calendar-editor-layout-shadow"></div>';
			
			var htmlEnd = '</div></td></tr></tbody></table>';
			element.after(htmlBegin+htmlContent+htmlEnd);

			this.isEditorCreated = true;

			return rf.getDomElement(this.EDITOR_ID);
		},

		createTimeEditorLayout: function(editor)
		{
			$(rf.getDomElement(this.EDITOR_LAYOUT_SHADOW_ID)).after(this.evaluateMarkup(CalendarView.timeEditorLayout, this.calendarContext));

			var th=rf.getDomElement(this.id+'TimeHours');
			var ts;
			var tm=rf.getDomElement(this.id+'TimeMinutes');
			if (this.timeType==1)
			{
				sbjQuery(th).SpinButton({digits:this.timeHoursDigits,min:0,max:23});
			}
			else
			{
				sbjQuery(th).SpinButton({digits:this.timeHoursDigits,min:1,max:12});
				ts=rf.getDomElement(this.id+'TimeSign');				
				sbjQuery(ts).SpinButton({});
			}
			sbjQuery(tm).SpinButton({digits:2,min:0,max:59});
			if (this.showSeconds) {
				var tsec=rf.getDomElement(this.id+'TimeSeconds');
				sbjQuery(tsec).SpinButton({digits:2,min:0,max:59});
			}
			
			this.correctEditorButtons(editor, this.TIME_EDITOR_BUTTON_OK, this.TIME_EDITOR_BUTTON_CANCEL);
			
			this.isTimeEditorLayoutCreated = true;
		},
		
		correctEditorButtons: function(editor, buttonID1, buttonID2)
		{
			var button1 = rf.getDomElement(buttonID1);
			var button2 = rf.getDomElement(buttonID2);
			editor.style.visibility = "hidden";
			editor.style.display = "";
			var width1 = $(button1.firstChild).width(); 
			var width2 = $(button2.firstChild).width();
			editor.style.display = "none";
			editor.style.visibility = "";
					
			if (width1!=width2)
			{
				button1.style.width = button2.style.width = (width1>width2 ? width1 : width2)+"px";
			}
		},
		
		createDECell: function(id, value, buttonType, param, className)
		{
			if (buttonType==0)
			{
				return '<div id="'+id+'" class="rich-calendar-editor-btn'+(className ? ' '+className : '')+
				                      '" onmouseover="this.className=\'rich-calendar-editor-btn rich-calendar-editor-tool-over\';" onmouseout="this.className=\'rich-calendar-editor-btn\';" onmousedown="this.className=\'rich-calendar-editor-btn rich-calendar-editor-tool-press\';" onmouseup="this.className=\'rich-calendar-editor-btn rich-calendar-editor-tool-over\';" onclick="RichFaces.$(\''+this.id+'\').scrollEditorYear('+param+');">'+value+'</div>';
			}
			else 
			{
				var onclick = (buttonType==1 ? 'RichFaces.$(\''+this.id+'\').dateEditorSelectMonth('+param+');':
						   				    'RichFaces.$(\''+this.id+'\').dateEditorSelectYear('+param+');' );
				return '<div id="'+id+'" class="rich-calendar-editor-btn'+(className ? ' '+className : '')+
									  '" onmouseover="jQuery(this).addClass(\'rich-calendar-editor-btn-over\');" onmouseout="$(this).removeClass(\'rich-calendar-editor-btn-over\');" onclick="'+onclick+'">'+value+'</div>';
			}
		},

		createDateEditorLayout: function(editor)
		{
			var htmlBegin = '<table id="'+this.DATE_EDITOR_LAYOUT_ID+'" class="rich-calendar-date-layout" border="0" cellpadding="0" cellspacing="0"><tbody><tr id="'+this.DATE_EDITOR_LAYOUT_ID+'TR">';
			var htmlEnd = '</tr></tbody></table>';
			var month = 0;
			this.dateEditorYear = this.getCurrentYear();
			var year = this.dateEditorStartYear = this.dateEditorYear-4;
			var htmlContent = '<td align="center">'+this.createDECell(this.DATE_EDITOR_LAYOUT_ID+'M'+month, this.params.monthLabelsShort[month], 1, month)+'</td>'
							 +'<td align="center" class="rich-calendar-date-layout-split">'+this.createDECell(this.DATE_EDITOR_LAYOUT_ID+'M'+(month+6), this.params.monthLabelsShort[month+6], 1, month+6)+'</td>'
							 +'<td align="center">'+this.createDECell('','&lt;', 0, -1)+'</td>'
							 +'<td align="center">'+this.createDECell('','&gt;', 0, 1)+'</td>';
				month++;
			
			for (var i=0;i<5;i++)
			{
				htmlContent+='</tr><tr><td align="center">'+this.createDECell(this.DATE_EDITOR_LAYOUT_ID+'M'+month, this.params.monthLabelsShort[month], 1, month)+'</td>'
							+'<td align="center" class="rich-calendar-date-layout-split">'+this.createDECell(this.DATE_EDITOR_LAYOUT_ID+'M'+(month+6), this.params.monthLabelsShort[month+6], 1, month+6)+'</td>'
							+'<td align="center">'+this.createDECell(this.DATE_EDITOR_LAYOUT_ID+'Y'+i, year, 2, i, (i==4 ? 'rich-calendar-editor-btn-selected' : ''))+'</td>'
							+'<td align="center">'+this.createDECell(this.DATE_EDITOR_LAYOUT_ID+'Y'+(i+5), year+5, 2, i+5)+'</td>';
				month++;
				year++;
			}
			this.dateEditorYearID = this.DATE_EDITOR_LAYOUT_ID+'Y4';
			this.dateEditorMonth = this.getCurrentMonth();
			this.dateEditorMonthID = this.DATE_EDITOR_LAYOUT_ID+'M'+this.dateEditorMonth;
			
			htmlContent+='</tr><tr><td colspan="2" class="rich-calendar-date-layout-ok">'+
						 '<div id="'+this.DATE_EDITOR_BUTTON_OK+'" class="rich-calendar-time-btn" style="float:right;" onmousedown="jQuery(this).addClass(\'rich-calendar-time-btn-press\');" onmouseout="$(this).removeClass(\'rich-calendar-time-btn-press\');" onmouseup="$(this).removeClass(\'rich-calendar-time-btn-press\');" onclick="RichFaces.$(\''+this.id+'\').hideDateEditor(true);"><span>'+this.params.labels.ok+'</span></div>'+
						 '</td><td colspan="2" class="rich-calendar-date-layout-cancel">'+
						 '<div id="'+this.DATE_EDITOR_BUTTON_CANCEL+'" class="rich-calendar-time-btn" style="float:left;" onmousedown="jQuery(this).addClass(\'rich-calendar-time-btn-press\');" onmouseout="$(this).removeClass(\'rich-calendar-time-btn-press\');" onmouseup="$(this).removeClass(\'rich-calendar-time-btn-press\');" onclick="RichFaces.$(\''+this.id+'\').hideDateEditor(false);"><span>'+this.params.labels.cancel+'</span></div>'+
						 '</td>';


			$(rf.getDomElement(this.EDITOR_LAYOUT_SHADOW_ID)).after(htmlBegin+htmlContent+htmlEnd);
			
			$(rf.getDomElement(this.dateEditorMonthID)).addClass('rich-calendar-editor-btn-selected');
			
			this.correctEditorButtons(editor, this.DATE_EDITOR_BUTTON_OK, this.DATE_EDITOR_BUTTON_CANCEL);
			
			this.isDateEditorLayoutCreated = true;
		},	
		
		createSpinnerTable: function(id) {
			return '<table cellspacing="0" cellpadding="0" border="0"><tbody><tr>'+
						'<td class="rich-calendar-spinner-input-container">'+
							'<input id="' + id + '" name="' + id + '" class="rich-calendar-spinner-input" type="text" />'+
						'</td>'+	
						'<td class="rich-calendar-spinner-buttons">'+
							'<table border="0" cellspacing="0" cellpadding="0"><tbody>'+
								'<tr><td>'+
									'<div id="'+id+'BtnUp" class="rich-calendar-spinner-up"'+
										' onmousedown="this.className=\'rich-calendar-spinner-up rich-calendar-spinner-pressed\'"'+
										' onmouseup="this.className=\'rich-calendar-spinner-up\'"'+ 
										' onmouseout="this.className=\'rich-calendar-spinner-up\'"><span></span></div>'+
								'</td></tr>'+
								'<tr><td>'+
									'<div id="'+id+'BtnDown" class="rich-calendar-spinner-down"'+
										' onmousedown="this.className=\'rich-calendar-spinner-down rich-calendar-spinner-pressed\'"'+
										' onmouseup="this.className=\'rich-calendar-spinner-down\'"'+
										' onmouseout="this.className=\'rich-calendar-spinner-down\'"><span></span></div>'+
								'</td></tr>'+
							'</tbody></table>'+
						'</td>'+
					'</tr></tbody></table>';
		},
		
		setTimeProperties: function() {
			this.timeType = 0;

			var dateTimePattern = this.params.datePattern;
			var pattern = [];
			var re = /(\\\\|\\[yMdaHhms])|(y+|M+|d+|a|H{1,2}|h{1,2}|m{2}|s{2})/g;
			var r;
			while (r = re.exec(dateTimePattern))
				if (!r[1])
	  				pattern.push({str:r[0],marker:r[2],idx:r.index});
	  		
	  		var datePattern = "";
	  		var timePattern = "";
	  		
			var digits,h,hh,m,s,a;
			var id = this.id;
			
			var getString = function (p) {
				return (p.length==0 ? obj.marker : dateTimePattern.substring(pattern[i-1].str.length+pattern[i-1].idx, obj.idx+obj.str.length));
			};
			
	  		for (var i=0;i<pattern.length;i++)
	  		{
	  			var obj = pattern[i];
	  			var ch = obj.marker.charAt(0);
	  			if (ch=='y'||ch=='M'||ch=='d') datePattern+=getString(datePattern);
	  			else if (ch=='a')
	  			{
	  				a=true;
	  				timePattern+=getString(timePattern);
	  			}
	  			else if (ch=='H')
	  			{
	  				h=true;
	  				digits=obj.marker.length;
	  				timePattern+=getString(timePattern);
	  			}
	  			else if (ch=='h')
	  			{
	  				hh=true;
	  				digits=obj.marker.length;
	  				timePattern+=getString(timePattern);
	  			}
	  			else if (ch=='m')
	  			{
	  				m=true;
	  				timePattern+=getString(timePattern);
	  			}
				else if (ch=='s')
	  			{
	  				this.showSeconds=true;
	  				timePattern+=getString(timePattern);
	  			}
	  			
	  			
	  		}
	  		this.datePattern = datePattern;
	  		this.timePattern = timePattern;

	  		var calendar = this;
	  		
			this.timePatternHtml = timePattern.replace(/(\\\\|\\[yMdaHhms])|(H{1,2}|h{1,2}|m{2}|s{2}|a)/g,
				function($1,$2,$3) {
					if ($2) return $2.charAt(1);
					switch ($3) {
			            case 'a'  : return '</td><td>'+calendar.createSpinnerTable(id+'TimeSign')+'</td><td>';
			            case 'H'  :
			            case 'HH' :
			            case 'h'  :
			            case 'hh' : return '</td><td>'+calendar.createSpinnerTable(id+'TimeHours')+'</td><td>';
			            case 'mm' : return '</td><td>'+calendar.createSpinnerTable(id+'TimeMinutes')+'</td><td>';
						case 'ss' : return '</td><td>'+calendar.createSpinnerTable(id+'TimeSeconds')+'</td><td>';
					}
				}
			);
			
			this.timePatternHtml = '<table border="0" cellpadding="0"><tbody><tr><td>'+this.timePatternHtml+'</td></tr></tbody></table>';
	  		
			if (m && h)
			{
				this.timeType = 1;
			}
			else if (m && hh && a) 
			{
				this.timeType = 2;
			}
			this.timeHoursDigits = digits;
		},
		
		eventOnScroll: function (e) {
			this.doCollapse();
		},
		
		doCollapse: function() {
			
			if (!this.params.popup || !this.isVisible) return;
			
			var element = rf.getDomElement(this.id);
			
			if (this.invokeEvent("collapse", element))
			{
				if (this.isEditorVisible) this.hideEditor();
				this.scrollElements && rf.Event.unbindScrollEventHandlers(this.scrollElements, this);
				this.scrollElements = null;
				rf.Event.unbind(window.document, "click"+this.namespace);
				
				$(element).hide();
				this.isVisible = false;

			}
		},

		collapse: function() {
			this.doCollapse();
		},
		
		doExpand: function(e) {
			if (!this.isRendered) {
				this.isRendered = true;
				this.render();
			}
			this.skipEventOnCollapse = false;
			if (e && e.type=='click') this.skipEventOnCollapse = true;
			if (!this.params.popup || this.isVisible) return;
			
			var element = rf.getDomElement(this.id);

			if (this.invokeEvent("expand", element, e))
			{
				var base = rf.getDomElement(this.POPUP_ID)
				var baseInput = base.firstChild;
				var baseButton = baseInput.nextSibling;
				
				if (baseInput && baseInput.value!=undefined)
				{
					this.selectDate(baseInput.value, false, {event:e, element:element});
				}
				
				//rect calculation
				
				if (this.params.showInput)
				{
					base = base.children;
				} else {
					base = baseButton;
				};
						 
				$(element).setPosition(base, {type:"DROPDOWN", from: this.params.jointPoint, to:this.params.direction, offset: this.popupOffset}).show();
				
				this.isVisible = true;

				rf.Event.bind(window.document, "click"+this.namespace, this.eventOnCollapse, this);
				
				this.scrollElements && rf.Event.unbindScrollEventHandlers(this.scrollElements, this);
				this.scrollElements = null;
				if (this.params.hidePopupOnScroll) {
					this.scrollElements = rf.Event.bindScrollEventHandlers(element, this.eventOnScroll, this);
				}
			}
		},

		expand: function(e) {
			this.doExpand(e);
		},
		
		doSwitch: function(e) {
			this.isVisible ? this.doCollapse() : this.doExpand(e);
		},

		switchState: function(e) {
			this.doSwitch(e);
		},
		
		eventOnCollapse: function (e) {
			if (this.skipEventOnCollapse)
			{
				this.skipEventOnCollapse = false;
				return true;
			}

			if (e.target.id == this.POPUP_BUTTON_ID || (!this.params.enableManualInput && e.target.id == this.INPUT_DATE_ID) ) return true;
			
			this.doCollapse();
			
			return true;
		},
		
		setInputField: function(dateStr, event)
		{
			var field = rf.getDomElement(this.INPUT_DATE_ID);
			if (field.value!=dateStr)
			{
				field.value=dateStr;
				this.invokeEvent("changed",field, event, this.selectedDate);
			}
		},
		
		getCurrentDate: function() {
			return this.currentDate;
		},	
		getSelectedDate: function() {
			if (!this.selectedDate) return null; else return this.selectedDate;
		},
		getSelectedDateString: function(pattern) {
			if (!this.selectedDate) return "";
			if (!pattern) pattern = this.params.datePattern;
			return rf.calendarUtils.formatDate(this.selectedDate, pattern, this.params.monthLabels, this.params.monthLabelsShort);
		},

		getPrevYear: function() {
			var value = this.currentDate.getFullYear()-1;
			if (value<0) value = 0;
			return value;
		},
		getPrevMonth: function(asMonthLabel) {
			var value = this.currentDate.getMonth()-1;
			if (value < 0 ) value = 11;
			if (asMonthLabel)
			{
				return this.params.monthLabels[value];
			} else return value;
		},
		getCurrentYear: function() {
			return this.currentDate.getFullYear();
		},
		getCurrentMonth: function(asMonthLabel) {
			var value = this.currentDate.getMonth();
			if (asMonthLabel)
			{
				return this.params.monthLabels[value];
			} else return value;
		},
		getNextYear: function() {
			return this.currentDate.getFullYear()+1;
		},
		getNextMonth: function(asMonthLabel) {
			var value = this.currentDate.getMonth()+1;
			if (value > 11 ) value = 0;
			if (asMonthLabel)
			{
				return this.params.monthLabels[value];
			} else return value;
		},
		
		isWeekend: function(weekday) {
			return (weekday == this.firstWeekendDayNumber || weekday == this.secondWeekendDayNumber);
		},
		
		setupTimeForDate: function (date) {
			if (this.selectedDate && (!this.params.resetTimeOnDateSelect || 
				(this.selectedDate.getFullYear() == date.getFullYear() && 
				this.selectedDate.getMonth() == date.getMonth() &&
				this.selectedDate.getDate() == date.getDate())))
			{
				date.setHours(this.selectedDate.getHours());
				date.setMinutes(this.selectedDate.getMinutes());
				date.setSeconds(this.selectedDate.getSeconds());
			} else
			{
				date.setHours(this.params.defaultTime.hours);
				date.setMinutes(this.params.defaultTime.minutes);
				date.setSeconds(this.params.defaultTime.seconds);
			}
		},
		
		eventCellOnClick: function (e, obj) {
			var daydata = this.days[parseInt(obj.id.substr(this.DATE_ELEMENT_ID.length),10)];
			if (daydata.enabled && daydata._month==0)
			{
				var date=new Date(this.currentDate);
				date.setDate(daydata.day);
				if (this.timeType)
				{
					this.setupTimeForDate(date);
				}
				
				if (this.selectDate(date,true, {event:e, element:obj}) && !this.params.showApplyButton)
				{
					this.doCollapse();
				}
					
			} else if (daydata._month!=0){
				if (this.params.boundaryDatesMode == "scroll") 
					if (daydata._month==-1) this.prevMonth(); else this.nextMonth();
				else if (this.params.boundaryDatesMode == "select") 
				{
					var date = new Date(daydata.date);
					if (this.timeType)
					{
						this.setupTimeForDate(date);
					}
					
					if (this.selectDate(date, false, {event:e, element:obj}) && !this.params.showApplyButton)
					{
					 	this.doCollapse();
					}
				}
			}
		},

		eventCellOnMouseOver: function (e, obj) {
			var daydata = this.days[parseInt(obj.id.substr(this.DATE_ELEMENT_ID.length),10)];
			if (this.invokeEvent("datemouseover", obj, e, daydata.date) && daydata.enabled)
			{
				if (daydata._month==0 && obj.id!=this.selectedDateCellId && obj.id!=this.todayCellId) {
					$(obj).addClass('rich-calendar-hover');
				}
			}
		},
		
		eventCellOnMouseOut: function (e, obj) {
			var daydata = this.days[parseInt(obj.id.substr(this.DATE_ELEMENT_ID.length),10)];
			if (this.invokeEvent("datemouseout", obj, e, daydata.date) && daydata.enabled)
			{
				if (daydata._month==0 && obj.id!=this.selectedDateCellId && obj.id!=this.todayCellId) {
					$(obj).removeClass('rich-calendar-hover');
				}
			}
		},

		load:function(daysData, isAjaxMode)	{
			//	startDate,
			//	daysData:array[]
			//	{
			//			day
			//			enabled boolean
			//			text1: 'Meeting...',
			//			text2: 'Meeting...'
			//			tooltip 
			//			hasTooltip 
			//			styleClass
			//	}
			
			
			if (daysData) {
				this.daysData = this.indexData(daysData, isAjaxMode);
			} else {
				this.daysData = null;
			}
			
			this.isRendered = false;
			if (this.isVisible) {
				this.render();
			}; 
			
			if (typeof this.afterLoad=='function') 
			{
				this.afterLoad();
				this.afterLoad=null;
			}
		},
		
		indexData:function(daysData, isAjaxMode) {
			var dateYear = daysData.startDate.getFullYear();
			var dateMonth = daysData.startDate.getMonth();
			
			daysData.index = [];
			daysData.index[dateYear+'-'+dateMonth] = 0;
			if (isAjaxMode)
			{
				this.currentDate = daysData.startDate;
				this.currentDate.setDate(1);
				return daysData;
			}
			var idx = rf.calendarUtils.daysInMonthByDate(daysData.startDate)-daysData.startDate.getDate()+1;
			
			while (daysData.days[idx])
			{
				if (dateMonth==11) {dateYear++; dateMonth=0;} else dateMonth++;
				daysData.index[dateYear+'-'+dateMonth] = idx;
				idx+= (32 - new Date(dateYear, dateMonth, 32).getDate());
			}
			return daysData;
		},
		
		getCellBackgroundColor: function(element)
		{	
			return $(element).css('background-color');
		},
		
		clearEffect: function (element_id, className, className1)
		{
			if (element_id)
			{
				var e = $(rf.getDomElement(element_id)).stop(true, true);
				if (className) e.removeClass(className);
				if (className1) e.addClass(className1);
			}
			return null;
		},
		
		render:function() {
			//var _d=new Date();
			this.isRendered = true;
			this.todayDate = new Date();		
			
			var currentYear = this.getCurrentYear();
			var currentMonth = this.getCurrentMonth();
			
			var todayflag = (currentYear == this.todayDate.getFullYear() && currentMonth == this.todayDate.getMonth());
			var todaydate =  this.todayDate.getDate();
			
			var selectedflag = this.selectedDate && (currentYear == this.selectedDate.getFullYear() && currentMonth == this.selectedDate.getMonth())
			var selecteddate = this.selectedDate && this.selectedDate.getDate();

			var wd = rf.calendarUtils.getDay(this.currentDate, this.params.firstWeekDay);
			var currentMonthDays = rf.calendarUtils.daysInMonthByDate(this.currentDate);
			var previousMonthDays = rf.calendarUtils.daysInMonth(currentYear, currentMonth-1);
			
			var p=0;
			var month=-1;
			this.days = [];
			var dayCounter = previousMonthDays  - wd + 1;
			
			// previuos month days
			if (wd>0) while (dayCounter<=previousMonthDays)
			{
				this.days.push({day:dayCounter, isWeekend: this.isWeekend(p), _month:month}); dayCounter++; p++;
			}
				
			dayCounter = 1;
			month=0;
			
			this.firstDateIndex = p;

			// current month days
			if (this.daysData && this.daysData.index[currentYear+'-'+currentMonth]!=undefined)
			{
				var idx = this.daysData.index[currentYear+'-'+currentMonth];
				if (this.daysData.startDate.getFullYear()==currentYear && this.daysData.startDate.getMonth()==currentMonth)
				{
					var firstDay = firstDay=(this.daysData.days[idx].day ? this.daysData.days[idx].day : this.daysData.startDate.getDate());
					while (dayCounter<firstDay)
					{
						this.days.push({day:dayCounter, isWeekend:this.isWeekend(p%7), _month:month});
					
						dayCounter++;
						p++;
					}
				}
				
				var len = this.daysData.days.length;
				var obj;
				var flag;
				while (idx<len && dayCounter<=currentMonthDays)
				{
					flag = this.isWeekend(p%7);
					obj = this.daysData.days[idx];
					obj.day = dayCounter;
					obj.isWeekend = flag;
					obj._month = month;
					this.days.push(obj);
					idx++;
					dayCounter++;
					p++;
				}
			}
			while (p<42)
			{
				if (dayCounter>currentMonthDays) {dayCounter=1; month=1;}
				this.days.push({day:dayCounter, isWeekend: this.isWeekend(p%7), _month:month});
				dayCounter++;
				p++;
			}
			
			// render
			this.renderHF();
			
			//days render
			p=0;
			var element;
			var dataobj;
			var wn;
			if (this.params.showWeeksBar) wn = rf.calendarUtils.weekNumber(currentYear, currentMonth, this.params.minDaysInFirstWeek, this.params.firstWeekDay); /// fix it
			this.selectedDayElement=null;
			var weekflag=true;

			var e;
			
			var boundaryDatesModeFlag = (this.params.boundaryDatesMode == "scroll" || this.params.boundaryDatesMode == "select");
			
			this.todayCellId = this.clearEffect(this.todayCellId);
			this.selectedDateCellId = this.clearEffect(this.selectedDateCellId);
			
			//var _d=new Date();
			var obj = rf.getDomElement(this.WEEKNUMBER_BAR_ID+"1");
			for (var k=1;k<7;k++)
			{
				//
				dataobj = this.days[p];
				
				element = obj.firstChild;
				var weeknumber; 

				// week number update			
				if (this.params.showWeeksBar)
				{
					// TODO: fix:  there is no weekNumber in dataobj if showWeeksBar == false;
					if (weekflag && currentMonth==11 &&
					   (k==5||k==6) &&
					   (dataobj._month==1 || (7 - (currentMonthDays - dataobj.day + 1)) >= this.params.minDaysInFirstWeek) )
					{
						wn=1;
						weekflag=false;
					}
					weeknumber = wn;
				    element.innerHTML = this.evaluateMarkup(this.params.weekNumberMarkup, {weekNumber: wn++, elementId:element.id, component:this} );
				    if (k==1&&wn>52) wn=1;
				    element = element.nextSibling;
				}
				
				var weekdaycounter = this.params.firstWeekDay;
				var contentElement = null;

				while (element)
				{
					dataobj.elementId=element.id;
					dataobj.date=new Date(currentYear, currentMonth+dataobj._month, dataobj.day);
					dataobj.weekNumber = weeknumber;
					dataobj.component = this;
					dataobj.isCurrentMonth = (dataobj._month==0);
					dataobj.weekDayNumber = weekdaycounter;

					// call user function to get day state
					if (dataobj.enabled != false) dataobj.enabled = this.params.isDayEnabled(dataobj);
					// call user function to custom class style
					if (!dataobj.styleClass) dataobj.customStyleClass = this.params.dayStyleClass(dataobj);
					else
					{
						var styleclass = this.params.dayStyleClass(dataobj);
						dataobj.customStyleClass = dataobj.styleClass;
						if (styleclass) dataobj.customStyleClass += " " + styleclass;
					}

					contentElement = (this.customDayListMarkup ? element.firstChild : element);
					contentElement.innerHTML = this.evaluateMarkup(this.params.dayListMarkup, dataobj );

					if (weekdaycounter==6) weekdaycounter=0; else weekdaycounter++;
					
					var classNames = this.dayCellClassName[p];
					
					// class styles
					if (dataobj._month!=0) 
					{
						classNames+=' rich-calendar-boundary-dates';
						if (!this.params.disabled && !this.params.readonly && boundaryDatesModeFlag)
						{
							classNames+=' rich-calendar-btn';
						}
					}
					else 
					{
						if (todayflag && dataobj.day==todaydate) 
						{
							this.todayCellId = element.id;
							this.todayCellColor = this.getCellBackgroundColor(element);
							classNames+=" rich-calendar-today";
						}
					
						if (selectedflag && dataobj.day==selecteddate)
						{
							this.selectedDateCellId = element.id;
							this.selectedDateCellColor = this.getCellBackgroundColor(element);
							classNames+=" rich-calendar-select";
						} 
						else if (!this.params.disabled && !this.params.readonly && dataobj.enabled) classNames+=' rich-calendar-btn';

						// add custom style class
						if (dataobj.customStyleClass) 
						{
							classNames+=' '+dataobj.customStyleClass;
						}
					}
					element.className = classNames;
					
					p++;

					dataobj = this.days[p];
					element=element.nextSibling;
				}
				obj = obj.nextSibling;
			}
			
			//alert(new Date().getTime()-_d.getTime());
			
		},

		renderHF: function()
		{
			if (this.params.showHeader) this.renderMarkup(this.params.headerMarkup, this.id+"Header", this.calendarContext);
			if (this.params.showFooter) this.renderMarkup(this.params.footerMarkup, this.id+"Footer", this.calendarContext);
			
			this.renderHeaderOptional();
			this.renderFooterOptional();			
		},

		renderHeaderOptional: function()
		{
			this.renderMarkup(this.params.optionalHeaderMarkup, this.id+"HeaderOptional", this.calendarContext);
		},	

		renderFooterOptional: function()
		{
			this.renderMarkup(this.params.optionalFooterMarkup, this.id+"FooterOptional", this.calendarContext);
		},
		
		renderMarkup: function (markup, elementId, context)
		{
			if (!markup) return;

			var e = rf.getDomElement(elementId);
			if (!e) return; 
		
			e.innerHTML = this.evaluateMarkup(markup, context);
		},
		
		evaluateMarkup: function(markup, context)
		{
			if (!markup) return "";
			
			var result = [];
			var m;
			for (var i=0; i<markup.length; i++) {
				m = markup[i];
				if (m['getContent']) {
					result.push(m.getContent(context));
				}
			}
			return result.join('');
		},
		
		onUpdate: function()
		{
			var formattedDate = rf.calendarUtils.formatDate(this.getCurrentDate(),"MM/yyyy");
			rf.getDomElement(this.id+'InputCurrentDate').value=formattedDate;
			
			if (this.submitFunction)
				this.submitFunction.call(this, formattedDate);
			else
				this.render();
		},
		
		nextMonth: function() {
			this.changeCurrentDateOffset(0,1);
		},
		
		prevMonth: function() {
			this.changeCurrentDateOffset(0,-1);
		},
		
		nextYear: function() {
			this.changeCurrentDateOffset(1,0);
		},
		
		prevYear: function() {
			this.changeCurrentDateOffset(-1,0);
		},
		
		changeCurrentDate: function(year, month, noUpdate) {
			if (this.getCurrentMonth()!=month || this.getCurrentYear()!=year)
			{
				var date = new Date(year, month,1);
				if (this.invokeEvent("currentdateselect", rf.getDomElement(this.id), null, date))
				{
					// fix for RF-2450.
					// Additional event is fired: after the hidden input with current date
					// value is updated in function onUpdate() and then
					// the "currentdateselected" Event is fired.
					this.currentDate = date;
					if (noUpdate) this.render(); else this.onUpdate();
					this.invokeEvent("currentdateselected", rf.getDomElement(this.id), null, date);
					return true;
				}
			}
			return false;
		},
		
		changeCurrentDateOffset: function(yearOffset, monthOffset) {
			var date = new Date(this.currentDate.getFullYear()+yearOffset, this.currentDate.getMonth()+monthOffset,1);
				
			if (this.invokeEvent("currentdateselect", rf.getDomElement(this.id), null, date))
			{
				// fix for RF-2450.
				// Additional event is fired: after the hidden input with current date
				// value is updated in function onUpdate() and then
				// the "currentdateselected" Event is fired.
				this.currentDate = date;
				this.onUpdate();
				this.invokeEvent("currentdateselected", rf.getDomElement(this.id), null, date);
			}
		},

		today: function(noUpdate, noHighlight) {

				var now = new Date();
		
				var nowyear = now.getFullYear();
				var nowmonth = now.getMonth();
				var nowdate = now.getDate();
				var updateflag = false;
				
				if (nowdate!=this.todayDate.getDate()) {updateflag=true; this.todayDate = now;}
				
				if (nowyear != this.currentDate.getFullYear() || nowmonth != this.currentDate.getMonth() )
				{
					updateflag = true;
					this.currentDate = new Date(nowyear, nowmonth, 1);
				}
		
				if (this.params.todayControlMode=='select')
				{
					noHighlight=true;
				}
				
				if (updateflag)
				{
					if (noUpdate) this.render(); else this.onUpdate();
				}
				else
				{
					// highlight today
					
					if (this.isVisible && this.todayCellId && !noHighlight)
					{
						this.clearEffect(this.todayCellId);
						if (this.todayCellColor!="transparent")
						{
							$(rf.getDomElement(this.todayCellId)).effect("highlight", {easing:'easeInOutSine', color: this.todayCellColor}, 300);
						}
					}
				}
		
				// todayControl select mode
				if (this.params.todayControlMode=='select' && !this.params.disabled && !this.params.readonly)
					if (updateflag && !noUpdate && this.submitFunction)
					{
						this.afterLoad = this.selectToday;
					}
					else this.selectToday();
			
		},

		selectToday: function()
		{
			if (this.todayCellId)
			{
				var daydata = this.days[parseInt(this.todayCellId.substr(this.DATE_ELEMENT_ID.length),10)];
				var today = new Date();
				var date = new Date(today.getFullYear(), today.getMonth(), today.getDate());
				if (this.timeType)
				{
					this.setupTimeForDate(date);
				}
				if (daydata.enabled && this.selectDate(date,true) && !this.params.showApplyButton)
				{
					this.doCollapse();
				}
			}		
		},
		
		selectDate: function(date, noUpdate, eventData) {
			
			if (!eventData)
			{
				eventData = {event: null, element: null};
			}
			
			var oldSelectedDate = this.selectedDate;
			var newSelectedDate;
			if (date)
			{
				if (typeof date=='string') 
				{
					date = rf.calendarUtils.parseDate(date,this.params.datePattern, this.params.monthLabels, this.params.monthLabelsShort);
				}
				newSelectedDate = date;
			}
			else
			{
				newSelectedDate = null;
			}

			// fire user event
			var flag = true;
			var isDateChange = false;
			if ( (oldSelectedDate - newSelectedDate) && (oldSelectedDate!=null || newSelectedDate!=null) )
			{
				isDateChange = true;
				flag = this.invokeEvent("dateselect", eventData.element, eventData.event, date);
			}	
			
			if (flag)
			{		   
				if (newSelectedDate!=null)
				{
					if (newSelectedDate.getMonth()==this.currentDate.getMonth() && newSelectedDate.getFullYear()==this.currentDate.getFullYear())
					{
						this.selectedDate = newSelectedDate;
						if (!oldSelectedDate || (oldSelectedDate - this.selectedDate))
						{
							// find cell and change style class
							var e = $(rf.getDomElement(this.DATE_ELEMENT_ID+(this.firstDateIndex + this.selectedDate.getDate()-1)));
							
							this.clearEffect(this.selectedDateCellId, "rich-calendar-select", (this.params.disabled || this.params.readonly ? null : "rich-calendar-btn"));
							this.selectedDateCellId = e.attr('id');
							this.selectedDateCellColor = this.getCellBackgroundColor(e);
		
							e.removeClass("rich-calendar-btn");
							e.removeClass("rich-calendar-hover");
							e.addClass("rich-calendar-select");
		
							this.renderHF();
						}
						else if (this.timeType!=0) this.renderHF();
					}
					else
					{
						//RF-5600
						this.selectedDate = newSelectedDate;

						// change currentDate and call this.onUpdate();
						if (this.changeCurrentDate(newSelectedDate.getFullYear(), newSelectedDate.getMonth(), noUpdate))
						{
							//this.selectedDate = newSelectedDate;
						} else {
							this.selectedDate = oldSelectedDate;
							isDateChange = false;
						}
					}
				}
				else
				{
					this.selectedDate = null;

					this.clearEffect(this.selectedDateCellId, "rich-calendar-select", (this.params.disabled || this.params.readonly ? null : "rich-calendar-btn"));
					
					if (this.selectedDateCellId)
					{
						this.selectedDateCellId = null;
						this.renderHF();					
					}
					
					var date = new Date();
					if (this.currentDate.getMonth()==date.getMonth() && this.currentDate.getFullYear()==date.getFullYear())
					{
						this.renderHF();
					}
					
					var todayControlMode = this.params.todayControlMode;
					this.params.todayControlMode = '';
					this.today(noUpdate, true);
					this.params.todayControlMode = todayControlMode;
				}
				
				// call user event
				if (isDateChange)
				{
					this.invokeEvent("dateselected", eventData.element, eventData.event, this.selectedDate);
					if (!this.params.showApplyButton)
					{
						this.setInputField(this.selectedDate!=null ? this.getSelectedDateString(this.params.datePattern) : "", eventData.event);
					}
				}
			}
			
			return isDateChange;			
		},
		
		resetSelectedDate: function()
		{
			if (!this.selectedDate) return;
			if (this.invokeEvent("dateselect", null, null, null))
			{
				this.selectedDate = null;
				this.invokeEvent("dateselected", null, null, null);
				
				this.selectedDateCellId = this.clearEffect(this.selectedDateCellId, "rich-calendar-select", (this.params.disabled || this.params.readonly ? null : "rich-calendar-btn"));
				 
				this.renderHF();
				if (!this.params.showApplyButton)
				{
					this.setInputField("", null);
					this.doCollapse();
				}
			}
		},
		
		showSelectedDate: function()
		{	
			if (!this.selectedDate) return;
			if (this.currentDate.getMonth()!=this.selectedDate.getMonth() || this.currentDate.getFullYear()!=this.selectedDate.getFullYear())
			{
				this.currentDate = new Date(this.selectedDate);
				this.currentDate.setDate(1);
				this.onUpdate();
			}
			else
			{
				// highlight Selected Date
				if (this.isVisible && this.selectedDateCellId)
				{
					this.clearEffect(this.selectedDateCellId);
					if (this.selectedDateCellColor!="transparent")
					{
						$(rf.getDomElement(this.selectedDateCellId)).effect("highlight", {easing:'easeInOutSine', color: this.selectedDateCellColor}, 300);

					}
				}			
			}
		},
		
		close: function(updateDate)
		{
			if (updateDate)
			{
				this.setInputField(this.getSelectedDateString(this.params.datePattern), null);
			}		
			this.doCollapse();
		},
		
		clonePosition: function (source, elements, offset)
		{
			var jqe = $(source);
			if (!elements.length) elements = [elements];
			offset = offset || {left:0,top:0};
			var width = jqe.outerWidth()+"px", height = jqe.outerHeight()+"px";
			var pos = jqe.position();
			var left = Math.floor(pos.left)+offset.left+"px", top = Math.floor(pos.top)+offset.top+"px";
			var element;
			for (var i = 0; i<elements.length;i++) {
				element = elements[i];
				element.style.width = width;
				element.style.height = height;
				element.style.left = left;
				element.style.top = top;
			}
		},
		
		showTimeEditor: function()
		{
			var editor;
			if (this.timeType==0) return;
			if (!this.isEditorCreated) editor = this.createEditor();
			else editor = rf.getDomElement(this.EDITOR_ID);
			if (!this.isTimeEditorLayoutCreated) this.createTimeEditorLayout(editor);
			
			$(rf.getDomElement(this.TIME_EDITOR_LAYOUT_ID)).show();
			
			var editor_shadow = rf.getDomElement(this.EDITOR_SHADOW_ID);
			
			this.clonePosition(rf.getDomElement(this.id), [editor, editor_shadow]);
			
			this.updateTimeEditor();
			
			$(editor_shadow).show();
			
			$(editor).show();
			
			this.clonePosition(rf.getDomElement(this.TIME_EDITOR_LAYOUT_ID), rf.getDomElement(this.EDITOR_LAYOUT_SHADOW_ID), {left: 3, top: 3});
			this.isEditorVisible = true;		
		},

		hideEditor: function()
		{
			if (this.isTimeEditorLayoutCreated) $(rf.getDomElement(this.TIME_EDITOR_LAYOUT_ID)).hide();
			if (this.isDateEditorLayoutCreated) $(rf.getDomElement(this.DATE_EDITOR_LAYOUT_ID)).hide();
			$(rf.getDomElement(this.EDITOR_ID)).hide();
			$(rf.getDomElement(this.EDITOR_SHADOW_ID)).hide();
			this.isEditorVisible = false;		
		},
		
		hideTimeEditor: function(updateTime)
		{
			this.hideEditor();
			if (updateTime && this.selectedDate)
			{
				var s = this.showSeconds ? parseInt(rf.getDomElement(this.id+'TimeSeconds').value,10) : this.params.defaultTime.seconds;
				var m = parseInt(rf.getDomElement(this.id+'TimeMinutes').value,10);
				var h = parseInt(rf.getDomElement(this.id+'TimeHours').value,10);
				if (this.timeType==2)
				{
					if (rf.getDomElement(this.id+'TimeSign').value.toLowerCase()=="am")
					{
						if (h==12) h = 0;					
					}
					else
					{
						if (h!=12) h+=12;
					}
				}
				var date = new Date(this.selectedDate.getFullYear(), this.selectedDate.getMonth(), this.selectedDate.getDate(), h, m, s);
				if (date-this.selectedDate && this.invokeEvent("timeselect",null, null, date))
				{
					this.selectedDate = date;
					this.renderHF();
					if (!this.params.popup || !this.params.showApplyButton) this.setInputField(this.getSelectedDateString(this.params.datePattern), null);
					this.invokeEvent("timeselected",null, null, this.selectedDate);
				}
			}
			if (this.params.popup && !this.params.showApplyButton) this.close(false);		
		},
		
		showDateEditor: function()
		{
			var editor;
			if (!this.isEditorCreated) editor = this.createEditor();
			else editor = rf.getDomElement(this.EDITOR_ID);
			if (!this.isDateEditorLayoutCreated) this.createDateEditorLayout(editor);
			else this.updateDateEditor();
		
			$(rf.getDomElement(this.DATE_EDITOR_LAYOUT_ID)).show();
				
			var editor_shadow = rf.getDomElement(this.EDITOR_SHADOW_ID);
				
			this.clonePosition(rf.getDomElement(this.id), [editor, editor_shadow]);
				
			$(editor_shadow).show();
			$(editor).show();
				
			this.clonePosition(rf.getDomElement(this.DATE_EDITOR_LAYOUT_ID), rf.getDomElement(this.EDITOR_LAYOUT_SHADOW_ID), {left: 3, top: 3});
				
			this.isEditorVisible = true;
		},
		
		hideDateEditor: function(updateCurrentDate)
		{
			this.hideEditor();
			if (updateCurrentDate)
			{
				this.changeCurrentDate(this.dateEditorYear, this.dateEditorMonth);
			}
		},
		
		getNamespace: function () {
			return this.namespace;
		}
	});
})(jQuery, RichFaces);