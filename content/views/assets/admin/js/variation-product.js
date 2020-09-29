  $(document).ready(function(){
      $(document.body).on('keyup', ".wsugestion", function(event){ 
          var data_id= $(this).data("name");
          
          show_sugestion(this);
          var key = loopSugestion(this, data_id, "id");
          
          var fieldsKey = this.parentElement.querySelector(".field"+data_id);
          
          if(key=="" && this.value != ""){
               key = randomStr(4);
              fieldsKey.value = key;
              
          }else if(key !="" && this.value != ""){
              fieldsKey.value = key;
              getotherLang(this, key);
          }else{
            fieldsKey.value ="";
          }
          
          replaceNameSugestion(this, key);
    });
         
 });
 
 function replaceNameSugestion(obj, key){
   $(obj).parent().find(".replaceName").each(function(){
        var sugestionName = $(this).attr('name');
        $(this).attr("name", sugestionName.replace(/\]\[[\s\S]*?\]\[/, '\]\[' + key + '\]\[')); 
   });
 }
  
 function getotherLang(obj, key){
    var data_id= $(obj).data("name");
    var data_listlang = $(obj).data("listlang");
       
    // check if there are multi languages, they are separated by coma in data-listlang
    if(data_listlang.includes(",")){
        var multilang = data_listlang.split(",");
        for(var i=1; i<multilang.length; i++){ // we skip the first because the first is main lang
          obj.parentElement.querySelector(".langthis"+multilang[i]).value= getValByKey(data_id, key, multilang[i]);
        }
     }
  } 
  
function getValByKey(varfieldname, key, lang){
    var obj = eval('typeof ' +varfieldname) === "undefined" ? null : eval(varfieldname);
    if (obj != null) {
        return typeof obj[key][lang] !== 'undefined' ? obj[key][lang] : "";
    }else{
        return "";
    }
 }
  
function loopSugestion(inputObj, type="all"){
    var varfieldname = inputObj.getAttribute('data-name');  
    var obj = eval(varfieldname);
   
    var filter = inputObj.value.toUpperCase();
    var lang = inputObj.getAttribute('data-lang');

    var objectConstructor = ({}).constructor;
    var links = '', keyReturn ='';
    
      for (var key in obj){
         var objName = obj[key].constructor === objectConstructor ?  obj[key][lang] : obj[key];
         
         if(objName.substr(0, filter.length).toUpperCase() == filter.toUpperCase()) {
            links = links + '<a href="'+key+'">'+ objName +'</a>'; 
          }
        
        if(filter.toUpperCase() == objName.toUpperCase()){
           keyReturn = key;
        }
     }
       
      
   return type == "all" ? links : keyReturn;   
}  
  
  // show the sugestion for the curent input
function show_sugestion(inputObj){
    var varfieldname = inputObj.getAttribute('data-name');
     var sugestionsList = inputObj.parentElement.querySelector(".sugestion_elements");
     var fieldsKey = inputObj.parentElement.querySelector(".field"+varfieldname);
     
     sugestionsList.style.display = "block";
     sugestionsList.innerHTML = loopSugestion(inputObj);
      
     $(".sugestion_elements a").bind('click', function(event){
         inputObj.value  = $(this).text();
         fieldsKey.value = $(this).attr("href");
          
          $(".sugestion_elements" ).css("display","none");
          getotherLang(inputObj, $(this).attr("href"));
          replaceNameSugestion(inputObj, $(this).attr("href"));
          return false;
     });
      
}
 
 
 
 
 //hide all sugestion when click outside;
function hide_elements(hede_all=""){
    // Detect all clicks on the document
document.addEventListener("click", function(event) {

// If user clicks inside the element, do nothing
if (event.target.closest(".wsugestion") && hede_all == "") return;

// If user clicks outside the element, hide it!
  var allelem= document.querySelectorAll(".sugestion_elements");
   
  for(var i = 0; i<=allelem.length; i++ ){
    allelem[i].style.display = "none";
    }
 });
}
 hide_elements();
 
  // dublicate variation
 function dublicate(this_elem){
     $('#variation_control input[type=text]').attr('value', function (i, val) { return $(this).val(); });
     var html = this_elem.parentElement.innerHTML; 
     html = html.split("variation[").join("variation["+randomStr(3));
     document.getElementById('variation_control').insertAdjacentHTML('beforeend',"<div class='inner_row background_variation'>"+html+'</div>');
  }
  
 //remove variation
 function  remove_element(this_elem){
    var result = confirm("Want to delete?");
    if (result) {
        this_elem.parentElement.remove(); 
        }
        return false;
     }
 // new variation line 
 function new_element_add(){
   var content_new = document.getElementById('new_element_is').querySelector(".inner_row").innerHTML;
       content_new = content_new.split("variation[").join("variation["+randomStr(3));
       document.getElementById('variation_control').insertAdjacentHTML('beforeend',"<div class='inner_row background_variation'>"+content_new+'</div>');
   // document.getElementById('variation_control').appendChild(content_new);
    return false;
 } 
 
  // go to first checked element in the category
 function go_to_element(){
     var divElem = document.getElementById('categories_right');
     var chElem = document.querySelectorAll(".anchor_scroll");
    if(chElem[0]){
        
          var hight_scroll = divElem.scrollHeight;
          var topPos = divElem.offsetTop;
          divElem.scrollTop =chElem[0].offsetTop - (topPos+20);
           
    }
 }
 
 go_to_element();