
(function() {
    
   // login button
    $$$(".topMenu li ul li a.active-menu").each(function() { 
        this.parentElement.parentElement.style.display= "block";
     });
     
     $$$(".topMenu li ul li ul li a.active-menu").each(function() { 
       this.parentElement.parentElement.parentElement.parentElement.style.display= "block";
     });
     
     $$$(".topMenu li a.active-menu").each(function() { 
         addClass(this.parentElement , 'active-parrent');
     });
     
  
}());

function findAncestor(el, cls="") {
    while ((el = el.parentElement));
    return el;
}
 