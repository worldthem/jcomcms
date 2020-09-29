 (function() {
     timer = 0;
   /*
     $$('.search_fieldajax').addEventListener('keyup', function(e) {
           if (timer) {
                clearTimeout(timer);
            }
            timer = setTimeout(WSearch, 1000); 
      });
      */
     
     $$$(".require").each(function() { 
         this.addEventListener('click', function(e) {
             removeClass(this, 'invalid');
          });
           this.addEventListener('keyup', function(e) {
             removeClass(this, 'invalid');
          });
     });  
     
    
     $$$(".open-modal").each(function() { 
        var id_modal= this.getAttribute('data-modal');
         $$(id_modal).style.display  = display_value;
         document.body.style.overflow = overwflow_value;
     });
    
    // login button
    $$$(".login_link").each(function() { 
           this.addEventListener('click', function(e) {
               modaljs('#login_window', 'show'); 
              return setTab('tab1-1');
          });
     });
    // registration button
    $$$(".rigistration_link").each(function() { 
           this.addEventListener('click', function(e) {
               modaljs('#login_window', 'show'); 
              return setTab('tab2-2');
          });
     });
     
    // when click outside close the popup
    window.onclick = function(event) {
        var event_main = event.target;
        $$$(".modal-window").each(function() {
            if (event.target == this) {
                   this.style.display = "none";
                   document.body.style.overflow= "visible";
               }
          });
          if(event_main.className == "zoom_image"){
             close_zoom();
          }
        
           
          
          if(event_main.className == "searchButtonHeader"){
            toogleDisplay(".searchFormHeader");
           }
          if(event_main.className == "filtersButtonHeader"){
            toogleDisplay(".filtersHeader",'flex');
           } 
      } 
      
        document.addEventListener('swipe-left', function(e) {
                   var ClassBlock= e.target.className;
                  if(ClassBlock == "img_zoom") {
                      navigation_zoom('next');
                   } 
         });
        document.addEventListener('swipe-right', function(e) {
                  var ClassBlock= e.target.className;
                  if(ClassBlock == "img_zoom") {
                      navigation_zoom('back');
                   } 
                    
         }); 
         
  
   // login button
    $$$(".payment_method").each(function() { 
           this.addEventListener('click', function(e) {
                
              $$$(".info-payment").each(function() {
                 this.style.display="none";
              });
              var uniqueClass= this.getAttribute('data-unic');
               if(uniqueClass !== null){
                 $$("."+uniqueClass).style.display="block";
               }
               
          });
     });
  
  
}());
 
function toogleDisplay(classOpen, displayType='block'){
     var checkOpen = $$(classOpen).style.display;
        if(checkOpen==displayType){
           $$(classOpen).style.display="none"; 
        }else{
            $$(classOpen).style.display=displayType;
        }
 } 
 
function findAncestor (el, cls) {
    while ((el = el.parentElement) && !el.classList.contains(cls));
    return el;
} 


 /*Start Gallery*/
 
function originalSizeImg(){
    var check_class= $$(".zoom_image").classList.contains('extrazoom') ;
    if ( check_class != false ) {
          removeClass($$(".zoom_image") , 'extrazoom');
          document.body.style.overflow= "visible";
    }else{
          addClass($$(".zoom_image") , 'extrazoom');
          document.body.style.overflow= "hidden";
    }
} 
 
function get_image_inarray(){
    var array_img = [];
     $$$(".gallery_product").each(function() {
         array_img.push(this.getAttribute('href')) ;
      });
   return  array_img.length>0 ? array_img : false;   
} 
 
function zoom_image(obj){
    var img_link= obj.getAttribute('href');
    var div = document.createElement("div");
        div.setAttribute('class', 'zoom_image');
        var navigation = !get_image_inarray() ? '': '<button class="zoom_button left_zoom" onclick="return navigation_zoom(\'back\');"><div><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M11.28 15.7l-1.34 1.37L5 12l4.94-5.07 1.34 1.38-2.68 2.72H19v1.94H8.6z"></path></path></svg></div></button>'+
                                                    '<button class="zoom_button right_zoom" onclick="return navigation_zoom(\'next\');"><div><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M15.4 12.97l-2.68 2.72 1.34 1.38L19 12l-4.94-5.07-1.34 1.38 2.68 2.72H5v1.94z"></path></svg></div></button>';
        
        div.innerHTML = "<button class='zoom_button zoomIcon' onclick='return originalSizeImg();'><div><svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'><path d='M18.7 17.3l-3-3a5.9 5.9 0 0 0-.6-7.6 5.9 5.9 0 0 0-8.4 0 5.9 5.9 0 0 0 0 8.4 5.9 5.9 0 0 0 7.7.7l3 3a1 1 0 0 0 1.3 0c.4-.5.4-1 0-1.5zM8.1 13.8a4 4 0 0 1 0-5.7 4 4 0 0 1 5.7 0 4 4 0 0 1 0 5.7 4 4 0 0 1-5.7 0z'></path></svg></div></button>" +
                        "<button class='zoom_button close_zoom' onclick='return close_zoom();'><div><svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'><path d='M12 10.6L6.6 5.2 5.2 6.6l5.4 5.4-5.4 5.4 1.4 1.4 5.4-5.4 5.4 5.4 1.4-1.4-5.4-5.4 5.4-5.4-1.4-1.4-5.4 5.4z'></path></svg></div></a></button> " +
                          navigation +
                        "<img class='img_zoom' src='"+img_link+"'>"+
                        "<div class='windowLoadAjax'></div>";
        document.body.appendChild(div);
    return false;
}

 
function close_zoom(){
    $$('.zoom_image').remove();
    document.body.style.overflow= "visible";
   return false; 
 } 
  
  function navigation_zoom(direction =""){
    var current =$$(".img_zoom").getAttribute('src');
    
    var array_img = get_image_inarray();
    var index_is = array_img.indexOf(current);
    var count_array = array_img.length;
    var srcIMG = '';   
    if(direction =="next"){
       srcIMG = (index_is + 1) == count_array ? array_img[0] : array_img[(index_is + 1)];
     }else{
       srcIMG = index_is  ==  0 ? array_img[count_array - 1] : array_img[(index_is - 1)]; 
     }
    
     waitLoadImg(srcIMG, ".img_zoom", ".windowLoadAjax" );
      removeClass($$(".zoom_image") , 'extrazoom');
      document.body.style.overflow= "visible";
    return false;
  }
 
 function waitLoadImg(newUrlImg='', classImg='', classLoad='' ){
       $$(classLoad).style.display="block";
     
        var image =  $$(classImg);
        var downloadingImage = new Image();
        downloadingImage.onload = function(){
            image.src = this.src;
            $$(classLoad).style.display="none";   
        };
        downloadingImage.src = newUrlImg;
      
 }
  /*end Gallery*/
 
 function replace_image_with(obj) {
        var url_img= obj.getAttribute('href');
        waitLoadImg(url_img, ".big_img_product",'.position_imageload');
         $$(".main_image_link").href  = url_img;
      
    return false;
}
 
 
 function modaljs(obj, action, type=''){
        var id_modal= typeof(obj) == "object"?  obj.getAttribute('data-modal') : obj;  
        
        $$(id_modal).style.display  = action== "show" ? "block": "none";;
        document.body.style.overflow = action== "show" ? "hidden": "visible";
         if(type=="ajax"){ 
           var data_url= obj.getAttribute('data-url');
           calUrl(data_url, '#load_content_mini' );
           //wajax('#load_content_mini', data_ajax ,'','yes');
         }
  }
 
 function call_url_ajax(obj,result){
    var data_ajax= obj.getAttribute('data-ajax');
      wajax(result, data_ajax );
      return false;
 }
 
 
 
// removeClass   - check js-helper
// addClass  - check js-helper
 
 function setTab(id){
       $$$(".tab_buttons").each(function() { 
          removeClass(this , 'active'); 
        });
        
       $$$(".tab-pane").each(function() { 
          addClass(this , 'display_none');
        }); 
        
        addClass($$("."+id) , 'active');
        removeClass($$("#"+id) , 'display_none'); 
       
     return false;     
 }
 
 
 function hide_elem(id_element){
    $$(id_element).style.display="none";
    return false;
 }
 
 
 function stars_point(id_parent, id_input, star){
    document.getElementById(id_input).value = star ;
    document.getElementById(id_parent).className = "stars"+star;
 }
 
 function update_price(obj) {
  var price = obj.options[obj.selectedIndex].getAttribute('price');
  var max_qtu = obj.options[obj.selectedIndex].getAttribute('qtu');
  if(price !='0'){
      $$('#price_each').innerHTML= price;
    }
      $$('#max_qtu').innerHTML= max_qtu; 
 }
  
  /*this function is for search*/
function WSearch (){ 
    var data_input = "?s=" + $$('.searchText').value + "&cat=" + $$('.searchCat').value + "&mode=ajax";
    var url = $$('#SearchForm').getAttribute('action');
        url = url+data_input;
    var attrContainer = $$('#SearchForm').getAttribute('data-attr'); 
     
      calUrl(url, attrContainer );  
}  

function parseSerialise(data='', isIn=''){
   var variable = data.split('&');
   var returnData = "";
  
   for(var i=0;i<variable.length;i++){
     var inner = variable[i].split('=');
     if(inner[0] == isIn){
         returnData = inner[1];
        break;
     }
   }
   return returnData;
}
/*Get the right response in depend of the ajax response*/
function show_result(response_get, result_class ){
   
    if (response_get.indexOf("~") >= 0){
         var main =  response_get.split('~~~');
           //that's mean user do some staff on cart page
           if(main[0]=="[cart]"){
              $$('.update_ajax_cal').innerHTML=main[1] ;
              $$(result_class).innerHTML='<span class="fa_ok"></span>'; 
            }else if(main[0]=="[script]"){
                 var script_ce = main[1].split(';');
                  for(var i=0; i<script_ce.length; i++){
                    eval(script_ce[i]);
                  }
                $$(result_class).innerHTML='';
            }else{
         
           $$('.small_cart').style.display="block";
          
           if(main[0] == "ok"){
             $$(result_class).innerHTML='<span class="fa_ok"></span>'; 
           }else{
             $$(result_class).innerHTML=main[0] ; 
           }
           
           $$('.load_content_smallcart').innerHTML=main[1] ;
           }
           
       }else if(response_get === "reload"){
          $$(result_class).innerHTML= '<span class="fa_ok"></span>';
          window.location.reload(); 
      }else if(response_get === "ok"){
          $$(result_class).innerHTML= '<span class="fa_ok"></span>';
      }else{
          $$(result_class).innerHTML=response_get;
      }
}
 
function wajax(id_result, data,  form='', no_load='') {
  var xhttp= new XMLHttpRequest(); 
  
  var params = form !="" ?  serialize($$(form)) : data; 
  var token= document.head.querySelector("[name=csrf-token]").content;
      params =  params + '&_token='+token;
     
   $$(id_result).innerHTML = no_load !=""? '<div class="container_loiad"><div class="loader" id="loader"></div><div class="loader" id="loader2"></div><div class="loader" id="loader3"></div><div class="loader" id="loader4"></div> <span id="loaderText">LOADING...</span></div>' : '<img src="'+url_assets+'/img_theme/ajax-loader.gif" style="width: 20px;height: 20px;" />' ;
   
    xhttp.open("POST", WajaxURL,true );
    
    xhttp.setRequestHeader("X-CSRF-Token", token);
    xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhttp.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
    
  xhttp.onreadystatechange = function() {
     if (this.readyState == 4 && this.status == 200) {
           show_result(this.responseText.trim(), id_result );
          
          if(data=="reset"){
             $$(form).reset();
           }
           
            var redirect =  parseSerialise(params, 'redirect');
            if(redirect != '') {
                 window.location.href = redirect;
                 }
     }else{
       // $$(id_result).innerHTML = 'Please write to webmaster, thank you!';
     }
  };
  
  xhttp.send(params);
}

function calUrl(url, id_result='', noresult = '') {
  var xhttp= new XMLHttpRequest(); 
  
  if(noresult ==''){
     $$(id_result).innerHTML =  '<div class="container_loiad"><div class="loader" id="loader"></div><div class="loader" id="loader2"></div><div class="loader" id="loader3"></div><div class="loader" id="loader4"></div> <span id="loaderText">LOADING...</span></div>' ;
   }
   
   xhttp.open("GET", url,true );
    
   xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
   xhttp.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
    
  xhttp.onreadystatechange = function() {
     if(this.readyState == 4 && this.status == 200) {
        if(noresult =='' || noresult ==".loadgenerated"){
            $$(id_result).innerHTML = this.responseText.trim(); 
            if(noresult ==".loadgenerated"){
                        $$(noresult).remove();
                         new LazyLoad();
                        }
        } 
      } 
  };
  xhttp.send('');
}

function simplePost(urlAjax, divAttr, data='', form='', loadContainre='') {
  var xhttp= new XMLHttpRequest(); 
  
  var params = form !="" ?  serialize($$(form)) : data; 
  var token= document.head.querySelector("[name=_csrf_header]").content;
      params =  params + '&_token='+token;

   if(loadContainre != ""){
     $$(loadContainre).innerHTML = '<img src="'+cms_assets+'/img_theme/ajax-loader.gif" style="width: 20px;height: 20px;" />';
   }

    xhttp.open("POST", urlAjax,true );
    
    xhttp.setRequestHeader("X-CSRF-Token", token);
    xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhttp.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
    
  xhttp.onreadystatechange = function() {
     if (this.readyState == 4 && this.status == 200) {
                    var responseText = this.responseText.trim();
                    if(data=="reset"){
                        $$(form).reset();
                     }

                     // if answer is reload than will update the page
                     if(responseText=="reload"){
                             window.location.reload(true);
                             responseText ="Success";
                       }

                        // if ansver is reload than will update the page
                        if(this.responseText.trim().includes("redirect::")){
                               window.location.replace(responseText.replace("redirect::",""));
                               responseText ="Success";
                          }

                 // when add to cart it show on right side a cart
                if(data.includes("smallCart")){
                    $$('.small_cart').style.display="block";
                     if(loadContainre != ""){
                        $$(loadContainre).innerHTML = '';
                      }
                  }

                 $$(divAttr).innerHTML = responseText;

                var redirect =  parseSerialise(params, 'redirect');
                if(redirect != '') {
                      window.location.href = redirect;
                    }
     }else{
        $$(divAttr).innerHTML = "Some code problem please notify the owner of website, thank you!";
     }
  };
  
  xhttp.send(params);
}
 

function optionsShowProducts(obj, valName="",keyget=""){
    // check if check box is checked or not
    var isCheck = obj.checked ? true : false;
    
    var regex = /[?&]([^=#]+)=([^&#]*)/g,
    url = window.location.href,
    params = {},
    match;
    //get GET param and put them in array
    while(match = regex.exec(url)) {
        params[match[1]] = match[2];
    }
    
    var newUrl = "";
    var checkKey = false;
      // start buld the new url
    for (let [key, value] of Object.entries(params)) {
        var newVal = value;
        // if key is equal with key keyget
        if(keyget == key){
            newVal = newVal+"-"+valName;
            if(!isCheck){ // check if checkbox is checked
                if(value.includes("-"+valName)){ // if valName is in the midle or end 
                    newVal = value.replace("-"+valName,"");
                }else if(value.includes(valName+"-")){ // if valName is in the begin
                    newVal = value.replace(valName+"-","");
                }else{ // than only valName is in the string so no need for replace
                   newVal = "";
                }
                // in any casy if are some double -- then replace
                newVal = newVal.replace("--","");
            }
            // key is in array
            checkKey = true; 
          }
              // build the new url wher newVal maybe empty or not so we check
             newUrl = newUrl == "" ? (newVal != "" ? key+ "=" +newVal : "")  : (newVal != "" ? newUrl +"&"+ key+ "="+newVal : newUrl ); 
         }
        
           // here may be two way the keyget maybe in array or not, and  newUrl may be empty so we check
           newUrl = !checkKey  ? (newUrl ==""? keyget + "="+ valName : newUrl +"&"+ keyget + "="+valName ): newUrl;
           // get url without parametrs
           var urlFinal = location.pathname+"?"+newUrl;   //location.protocol + '//' + location.host + location.pathname
            // replace in top bar
           window.history.pushState("", "", urlFinal);
            
            var loadClass = document.createElement("div");
            loadClass.innerHTML = "";
            loadClass.classList.add("loadgenerated");
            obj.parentElement.parentElement.parentElement.style.position = "relative";
            obj.parentElement.parentElement.parentElement.appendChild(loadClass);
          
           
        // make the ajax call
         calUrl(urlFinal,  '.productLists','.loadgenerated');
}
 


function quickEdit(object='') {
 var url= object.getAttribute("href");
 
 $$('.quickEditWindow').style.display="block";
 $$('.quickEditWindow').innerHTML = '<a href="#" class="closeEdit" onclick="window.location.reload();">X</a><iframe src="'+url+'"></iframe>';
  document.getElementsByTagName("BODY")[0].style.overflow="hidden";
 return false;
}
 

function checkout_finall(id_result, data,  form='' ){

  var token_stripe =  $$('.stripe_token').value;
  if($$('.stripePaymentradio').checked && token_stripe ==''){
       $$(id_result).innerHTML = '<img src="'+cms_assets+'/img_theme/ajax-loader.gif" style="width: 20px;height: 20px;" />';
              var querynew= "";
              $$$(".cl_deliverymethod").each(function() {
                 if(this.checked){
                      querynew=  "?d="+this.value  ;
                  }
              });


              var response = fetch(base_url+'payment-stripe/stripe-get-secret'+querynew).then(function(response) {
                  return response.json();
                }).then(function(responseJson) {
                  var clientSecret = responseJson.client_secret;

                    stripe.confirmCardPayment(clientSecret, {
                             payment_method: {
                               card: card
                             }
                           }).then(function(result) {
                             if (result.error) {
                               // Show error to your customer (e.g., insufficient funds)
                                $$('#card-errors').innerHTML = result.error.message  ;
                                $$(id_result).innerHTML = result.error.message ;
                             } else {
                               // The payment has been processed!
                               if (result.paymentIntent.status === 'succeeded') {
                                 // Show a success message to your customer
                                 // There's a risk of the customer closing the window before callback
                                 // execution. Set up a webhook or plugin to listen for the
                                 // payment_intent.succeeded event that handles any business critical
                                 // post-payment actions.
                                  $$('.stripe_token').value="go next";
                                  $$(id_result).innerHTML = "Payment successful";
                                  $$("#checkoutform").submit();
                                 return true;
                               }
                             }
                           });
                });
               return false;

    }
    
    return true;
 }

   function hide_show_shipping(th, x) {
      if ( th.checked == true ) {
         $$(x).style.display="block";

          $$$('.requiredNeed').each(function() {
              this.required =true;
            });
     }else{
         $$(x).style.display="none";
         $$$('.requiredNeed').each(function() {
                this.required =false;
           });
     }

 }
 
  function hide_show_is(th, x) {
     if ( th.checked == true ) {
        $$(x).style.display="block";
    }else{
        $$(x).style.display="none";
    }
    
}
 

function shipping_calculate(single_price=0, total_price){
    $$('.delivery_cost').innerHTML= single_price;
    $$('.total_price').innerHTML= total_price;
};

function menu_show_is(obj, menu_class= '') {
    var check_class= $$(menu_class).classList.contains('display_top_menu') ;
    if ( check_class != false ) {
          removeClass($$(menu_class),"display_top_menu");
          removeClass(obj,"change");
    }else{
          addClass($$(menu_class),"display_top_menu");
          addClass(obj,"change");
    }

}

  function IncrementDecrement(typeIncrement="+"){
      var quantitySingle = parseInt($$('.qtu_entered').value);
        if(typeIncrement == '-'){
            $$('.qtu_entered').value = quantitySingle < 2 ? 1 : quantitySingle - 1;
         }else{
            $$('.qtu_entered').value = quantitySingle + 1;
         }
 }
 