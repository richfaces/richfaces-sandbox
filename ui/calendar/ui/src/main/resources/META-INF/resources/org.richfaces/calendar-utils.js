(function ($, rf) {
	
	rf.calendarUtils = rf.calendarUtils || {};
	
	var getDefaultMonthNames = function(shortNames)
	{
		return (shortNames
				? ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec']
				: ['January','February','March','April','May','June','July','August','September','October','November','December']);
	};
	
	$.extend(rf.calendarUtils, {
		// TODO: rewrite this function or use the same function if exists
		/*clonePosition: function (elements, source)
		{
				if (!elements.length) elements = [elements];
				var offset = Position.cumulativeOffset(source);
				offset = {left:offset[0], top:offset[1]};
				var offsetTemp;
				if (source.style.position!='absolute')
				{
					offsetTemp = Position.realOffset(source);
					offset.left -= offsetTemp.left;
					offset.top -= offsetTemp.top;
					offsetTemp = Richfaces.Calendar.getWindowScrollOffset();
					offset.left += offsetTemp.left;
					offset.top += offsetTemp.top;
				}
	
				for (var i=0;i<elements.length;i++)
				{
					offsetTemp = Richfaces.Calendar.getParentOffset(elements[i]);
					elements[i].style.left = (offset.left - offsetTemp.left) + 'px';
					elements[i].style.top = (offset.top - offsetTemp.top) + 'px';
				}
				return offset;
		}*/
	
		//TODO: not used
		/*Object.extend(Event, {
			findElementByAttr : function(event, tagName, attribute, value, flag) {
		    	var element = Event.findElement(event, tagName);
		    	while (!element[attribute] || (flag ? element[attribute].indexOf(value)!=0 : element[attribute]!=value) )
		    	{
			      	element = element.parentNode;
			    }
		    	return element;
			}
		});
	
		Object.extend(Element, {
			replaceClassName : function (element, whichClassName, toClassName) {
				if (!(element = $(element))) return;
			    var e = Element.classNames(element);
			    e.remove(whichClassName);
			    e.add(toClassName);
			    return element;
			}
		});*/
		
		// TODO: move joinArray to richfaces utils
		joinArray: function(array, begin, end, separator)
		{
			var value = '';
			if (array.length!=0) value = begin+array.pop()+end;
			while (array.length)
				value = begin+array.pop()+end+separator+value;
			return value;
		},

		getMonthByLabel: function (monthLabel, monthNames) {
		    var toLowerMonthLabel = monthLabel.toLowerCase();
		    var i = 0;
		    while (i < monthNames.length) {
		        if (monthNames[i].toLowerCase() == toLowerMonthLabel) {
		            return i;
		        }
		        
		        i++;
		    }
		},

		/* Year:
		 *	y,yy - 00-99
		 *	yyy+ - 1999
		 * Month:
		 *	M - 1-12
		 *	MM - 01-12
		 *	MMM - short (Jul)
		 *	MMMM+ - long (July)
		 * Date:
		 *	d - 1-31
		 *	dd+ - 01-31 */
		parseDate: function(dateString, pattern, monthNames, monthNamesShort)
		{
			var re = /([.*+?^<>=!:${}()[\]\/\\])/g;
			var monthNamesStr
			var monthNamesShortStr;
			if (!monthNames) {
				monthNames = getDefaultMonthNames();
				monthNamesStr = monthNames.join('|');
			} else {
				monthNamesStr = monthNames.join('|').replace(re, '\\$1');
			}

			if (!monthNamesShort) {
				monthNamesShort = getDefaultMonthNames(true);
				monthNamesShortStr = monthNamesShort.join('|');
			} else {
				monthNamesShortStr = monthNamesShort.join('|').replace(re, '\\$1');
			}
			
			var counter=1;
			var y,m,d;
			var a,h,min,s;
			var shortLabel=false;
			
			pattern = pattern.replace(/([.*+?^<>=!:${}()|[\]\/\\])/g, '\\$1');
			pattern = pattern.replace(/(y+|M+|d+|a|H{1,2}|h{1,2}|m{2}|s{2})/g,
				function($1) {
					switch ($1) {
			            case 'y'  :
			            case 'yy' : y=counter; counter++; return '(\\d{2})';
			            case 'MM' : m=counter; counter++; return '(\\d{2})';
			            case 'M'  : m=counter; counter++; return '(\\d{1,2})';
			            case 'd'  : d=counter; counter++; return '(\\d{1,2})';
			            case 'MMM': m=counter; counter++; shortLabel=true; return '('+monthNamesShortStr+')';
			            case 'a'  : a=counter; counter++; return '(AM|am|PM|pm)?';
			            case 'HH' :
			            case 'hh' : h=counter; counter++; return '(\\d{2})?';
			            case 'H'  :
			            case 'h'  : h=counter; counter++; return '(\\d{1,2})?';
			            case 'mm' : min=counter; counter++; return '(\\d{2})?';
			            case 'ss' : s=counter; counter++; return '(\\d{2})?';

					}
			        // y+,M+,d+
					var ch = $1.charAt(0);
					if (ch=='y') {y=counter; counter++; return '(\\d{3,4})'};
					if (ch=='M') {m=counter; counter++; return '('+monthNamesStr+')'};
					if (ch=='d') {d=counter; counter++; return '(\\d{2})'};
				}
			);

			var re = new RegExp(pattern,'i');
			var match = dateString.match(re);
			if (match!=null)
			{
				// set default century start
				var correctYear = false;
				var defaultCenturyStart = new Date();
				defaultCenturyStart.setFullYear(defaultCenturyStart.getFullYear()-80);
				
				var yy = parseInt(match[y],10);
				if (isNaN(yy)) return null;
				else if (yy<100){
					// calculate full year if year has only two digits
					var defaultCenturyStartYear = defaultCenturyStart.getFullYear();
					var ambiguousTwoDigitYear = defaultCenturyStartYear % 100;
					correctYear = yy == ambiguousTwoDigitYear;
					yy += Math.floor(defaultCenturyStartYear/100)*100 + (yy < ambiguousTwoDigitYear ? 100 : 0);
				}
				
				var mm = parseInt(match[m],10); if (isNaN(mm)) mm = this.getMonthByLabel(match[m], shortLabel ? monthNamesShort : monthNames); else if (--mm<0 || mm>11) return null;
				var addDay = correctYear ? 1 : 0;
				var dd = parseInt(match[d],10); if (isNaN(dd) || dd<1 || dd>this.daysInMonth(yy, mm) + addDay) return null;
				
				var date = new Date(yy, mm, dd);

				// time parsing
				if (min!=undefined && h!=undefined)
				{			
					var hh,mmin,aa;
					mmin = parseInt(match[min],10); if (isNaN(mmin) || mmin<0 || mmin>59) return null;
					hh = parseInt(match[h],10); if (isNaN(hh)) return null;
					if (a!=undefined)
					{
						aa = match[a];
						if (!aa) return null;
						aa = aa.toLowerCase();
						if ((aa!='am' && aa!='pm') || hh<1 || hh>12) return null;
						if (aa=='pm')
						{
							if (hh!=12) hh+=12;
						} else if (hh==12) hh = 0;
					}
					else if (hh<0 || hh>23) return null;
					
					date.setHours(hh); date.setMinutes(mmin);
					if (s!=undefined)
					{
						sec = parseInt(match[s], 10); if (isNaN(sec) || sec<0 || sec>59) return null;
						date.setSeconds(sec);
					}
				}
				
				if (correctYear) {
					if (date.getTime() < defaultCenturyStart.getTime()) {
							date.setFullYear(yy + 100);
					}
					if (date.getMonth() != mm) return null;
				}
				
				return date;
			}
			return null;
		},

		formatDate: function(date, pattern, monthNames, monthNamesShort) {
			if (!monthNames) monthNames = getDefaultMonthNames();
			if (!monthNamesShort) monthNamesShort = getDefaultMonthNames(true);
			var mm,dd,hh,min,sec;
		    var result = pattern.replace(/(\\\\|\\[yMdaHhms])|(y+|M+|d+|a|H{1,2}|h{1,2}|m{2}|s{2})/g,
		        function($1,$2,$3) {
		        	if ($2) return $2.charAt(1);
					switch ($3) {
			            case 'y':
			            case 'yy':  return date.getYear().toString().slice(-2);
			            case 'M':   return (date.getMonth()+1);
			            case 'MM':  return ((mm = date.getMonth()+1)<10 ? '0'+mm : mm);
			            case 'MMM': return monthNamesShort[date.getMonth()];
				        case 'd':   return date.getDate();
			            case 'a'  : return (date.getHours()<12 ? 'AM' : 'PM');
			            case 'HH' : return ((hh = date.getHours())<10 ? '0'+hh : hh);
			            case 'H'  : return date.getHours();
			            case 'hh' : return ((hh = date.getHours())==0 ? '12' : (hh<10 ? '0'+hh : (hh>21 ? hh-12 : (hh>12) ? '0'+(hh-12) : hh)));
			            case 'h'  : return ((hh = date.getHours())==0 ? '12' : (hh>12 ? hh-12 : hh));
			            case 'mm' : return ((min = date.getMinutes())<10 ? '0'+min : min);
						case 'ss' : return ((sec = date.getSeconds())<10 ? '0'+sec : sec);
					}
			        // y+,M+,d+
					var ch = $3.charAt(0);
					if (ch=='y') return date.getFullYear();
					if (ch=='M') return monthNames[date.getMonth()];
					if (ch=='d') return ((dd = date.getDate())<10 ? '0'+dd : dd);
				}
			);
			return result;
		},

		isLeapYear: function(year) {
			return new Date(year, 1, 29).getDate()==29;
		},

		daysInMonth: function(year,month) {
			return 32 - new Date(year, month, 32).getDate();
		},

		daysInMonthByDate: function(date) {
			return 32 - new Date(date.getFullYear(), date.getMonth(), 32).getDate();
		},

		getDay: function(date, firstWeekDay ) {
			var value = date.getDay() - firstWeekDay;
			if (value < 0) value = 7 + value;
			return value;
		},

		getFirstWeek: function(year, mdifw, fdow) {
			var date = new Date(year,0,1);
			var firstday = this.getDay(date, fdow);
			
			var weeknumber = (7-firstday<mdifw) ? 0 : 1;
			
			return {date:date, firstDay:firstday, weekNumber:weeknumber, mdifw:mdifw, fdow:fdow};
		},

		getLastWeekOfPrevYear: function(o) {
			var year = o.date.getFullYear()-1;
			var days = (isLeapYear(year) ? 366 : 365);
			var obj = this.getFirstWeek(year, o.mdifw, o.fdow);
			days = (days - 7 + o.firstDay);
			var weeks = Math.floor(days/7)+1;
			  
			return  weeks+obj.weekNumber;
		},

		weekNumber: function(year, month, mdifw, fdow) {
			
			var o = this.getFirstWeek(year, mdifw, fdow);
			
			if (month==0) 
			{
				if (o.weekNumber==1) return 1;
				return getLastWeekOfPrevYear(o);
			}
			var	oneweek =  604800000;
			var d = new Date(year, month,1);
				d.setDate( 1+o.firstDay + (this.getDay(d,fdow)==0?1:0));
				
			weeknumber = o.weekNumber + Math.floor((d.getTime() - o.date.getTime()) / oneweek);
			
			return weeknumber;
		}
		
	});
	
	rf.calendarTemplates = rf.calendarTemplates || {};
	
	$.extend(rf.calendarTemplates, (function (){

		var VARIABLE_NAME_PATTERN = /^\s*[_,A-Z,a-z][\w,_\.]*\s*$/;
		
		var getObjectValue = function (str, object) {
			var a=str.split(".");
			var value=object[a[0]];
			var c=1;
			while (value && c<a.length) value = value[a[c++]];
			return (value ? value : "");
		};
		
		return  {
			evalMacro: function(template, object)
			{
				var _value_="";
				// variable evaluation
				if (VARIABLE_NAME_PATTERN.test(template))
				{
					if (template.indexOf('.')==-1) {
						_value_ = object[template];
						if (!_value_)	_value_=window[template];
					}
					// object's variable evaluation
					else {
						_value_ = getObjectValue(template, object);
						if (!_value_) _value_=getObjectValue(template, window);
					}
					if (_value_ && typeof _value_=='function') _value_ = _value_(object);
					if (!_value_) _value_=""; 		
				}
				//js string evaluation
				else {
					try {
						if (object.eval) {
							_value_ = object.eval(template);
						}
						else with (object) {
								_value_ = eval(template) ;
						}
						
						if (typeof _value_ == 'function') {
							_value_ = _value_(object);
						}
					} catch (e) { LOG.warn("Exception: "+e.Message + "\n[" + template + "]"); }
				}
				return _value_;
			}
		};
	})());

})(jQuery, RichFaces);