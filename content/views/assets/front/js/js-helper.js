// custom lightweight selector implementation
   
  
  window.$$ = function(selector) {
    return document.querySelector(selector) || document.querySelector('.pointdb');
  };

  window.$$$ = function(selector) {
    var items = {},
    results = [],
    length = 0,
    i = 0;
    
    // this doesn't work on IE 8- and Blackberry Browser
    results = Array.prototype.slice.call(document.querySelectorAll(selector));

    length = results.length;

    // add the results to the items object
    for ( ; i < length; ) {
      items[i] = results[i];
      i++;
    }
    
    // add some additional properties to this items object to 
    // make it look like an array
    items.length = length;
    items.splice = [].splice();

    // add an 'each' method to the items
    items.each = function(callback) {
      var i = 0;
      for ( ; i < length; ) {
        callback.call(items[i]);
        i++;
      }
    }

    return items;
  };

 
 

function addClass(el, className) {
  if (el.classList)
    el.classList.add(className)
  else if (!hasClass(el, className)) el.className += " " + className
}

function removeClass(el, className) {
  if (el.classList)
    el.classList.remove(className)
  else if (hasClass(el, className)) {
    var reg = new RegExp('(\\s|^)' + className + '(\\s|$)')
    el.className=el.className.replace(reg, ' ')
  }
}

function serialize(form) {
	if (!form || form.nodeName !== "FORM") {
		return;
	}
	var i, j, q = [];
	for (i = form.elements.length - 1; i >= 0; i = i - 1) {
		if (form.elements[i].name === "") {
			continue;
		}
		switch (form.elements[i].nodeName) {
		case 'INPUT':
			switch (form.elements[i].type) {
			case 'text':
            case 'email':
            case 'number':
			case 'hidden':
			case 'password':
			case 'button':
			case 'reset':
			case 'submit':
				q.push(form.elements[i].name + "=" + encodeURIComponent(form.elements[i].value));
				break;
			case 'checkbox':
			case 'radio':
				if (form.elements[i].checked) {
					q.push(form.elements[i].name + "=" + encodeURIComponent(form.elements[i].value));
				}						
				break;
			case 'file':
				break;
			}
			break;			 
		case 'TEXTAREA':
			q.push(form.elements[i].name + "=" + encodeURIComponent(form.elements[i].value));
			break;
		case 'SELECT':
			switch (form.elements[i].type) {
			case 'select-one':
				q.push(form.elements[i].name + "=" + encodeURIComponent(form.elements[i].value));
				break;
			case 'select-multiple':
				for (j = form.elements[i].options.length - 1; j >= 0; j = j - 1) {
					if (form.elements[i].options[j].selected) {
						q.push(form.elements[i].name + "=" + encodeURIComponent(form.elements[i].options[j].value));
					}
				}
				break;
			}
			break;
		case 'BUTTON':
			switch (form.elements[i].type) {
			case 'reset':
			case 'submit':
			case 'button':
				q.push(form.elements[i].name + "=" + encodeURIComponent(form.elements[i].value));
				break;
			}
			break;
		}
	}
	return q.join("&");
}


(function (window, document) { 
    
   // patch CustomEvent to allow constructor creation (IE/Chrome)
    if (typeof window.CustomEvent !== 'function') {

        window.CustomEvent = function (event, params) {

            params = params || { bubbles: false, cancelable: false, detail: undefined };

            var eventN = document.createEvent('CustomEvent');
            eventN.initCustomEvent(event, params.bubbles, params.cancelable, params.detail);
            return eventN;
        };

        window.CustomEvent.prototype = window.Event.prototype;
    } 
    
    
    
document.addEventListener('touchstart', handleTouchStart, false);
document.addEventListener('touchmove', handleTouchMove, false);
document.addEventListener('touchend', handleTouchEnd, true);

 
var xDown = null;                                                        
var yDown = null;
var direction = null;
var classContainer=null; 
var eventMain = null;

function getTouches(evt) {
  return evt.touches ||             // browser API
         evt.originalEvent.touches; // jQuery
}                                                     

function handleTouchStart(evt) {
    const firstTouch = getTouches(evt)[0];                                      
    xDown = firstTouch.clientX;                                      
    yDown = firstTouch.clientY; 
    eventMain = evt.target;                                     
};
                                               

function handleTouchMove(evt) {
    
    if ( ! xDown || ! yDown ) {
        return;
    }
   

    var xUp = evt.touches[0].clientX;                                    
    var yUp = evt.touches[0].clientY;

    var xDiff = xDown - xUp;
    var yDiff = yDown - yUp;

    if ( Math.abs( xDiff ) > Math.abs( yDiff ) ) {//most significant 
        if ( xDiff > 0 ) {
             // left swipe 
            direction = 'swipe-left';
            
        } else {
            //* right swipe  
           direction = 'swipe-right';
        }                       
    } else {
        if ( yDiff > 0 ) {
            //* up swipe  
            direction = 'swipe-up';
        } else { 
            //* down swipe
            direction = 'swipe-down';  
        }                                                                 
    }
    evt.preventDefault();
    //* reset values  
    xDown = null;
    yDown = null; 
                                                  
}

function handleTouchEnd(evt) {
       if (eventMain !== evt.target) return;
     
      if(direction !=null && direction !==''){
        eventMain.dispatchEvent(new CustomEvent(direction, { bubbles: true, cancelable: true }));
       }
      direction =null;
 } 

}(window, document));