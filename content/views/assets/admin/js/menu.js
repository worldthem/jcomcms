$( function() {
      // $(document.body).on('click', ".menu-box li", function(event){
        
        $(document.body).on('click', ".delete_ittem", function(event){
            if (confirm("Are you sure to delete this ittem?")) {
              $(this).parent().remove();
            }
            return false; 
        });
        
        $(document.body).on('click', ".makeMegaMenu", function(event){
           var parentClass =  $(this).parent().parent().attr("class");
               parentClass = typeof parentClass != 'undefined' ? parentClass : "nothing";
               
           if(!parentClass.includes("megaMenu")){     
              $(this).parent().parent().addClass("megaMenu");
            }else{
              $(this).parent().parent().removeClass("megaMenu");  
            }
            
         });
         
       $( ".menu-box" ).delegate( "li", "click", function(event) {
            var get_this_class = $(this).attr("class");
                get_this_class = typeof get_this_class != 'undefined' ? get_this_class : "nothing";
            var tagName= event.target.nodeName.toLowerCase(); 
             if (tagName =='li'){   
               $(".menu-box li").removeClass("activeIn");
       
               if(!get_this_class.includes("activeIn")){  
                 $(this).addClass("activeIn"); 
               } 
               event.stopPropagation();
             }  
         });
          
      

    $(document.body).on('click', ".fa_edit", function(event){
         remove_icon('.link_edit_text');
        var edit_elemnt = $(this).parent().find('.simple_link').first();
           $('.menu-box li a').removeClass("editThisPoint");
           
           edit_elemnt.addClass("editThisPoint");
        
         $('.edit_link_div').css("display","block");
         $('.link_edit_text').val(edit_elemnt.text());
         $('.link_edit_href, .link_edit_hidden_href').val(edit_elemnt.attr('href'));
         
          
         var icon_is = edit_elemnt.find('i').attr('class');
          
         if(typeof icon_is != 'undefined'){
           $('.icon_link_edit').after("<i class='icon_result "+edit_elemnt.find('i').attr('class')+"'></i><a href='#' class='fa_delete remove_button' onclick='return remove_icon(\".link_edit_text\")'></a>"); 
           $('.link_edit_text').data('icons',edit_elemnt.find('i').attr('class').replace('fa ',''));
         }else{
           $('.link_edit_text').data("icons",""); 
         }
         
        return false; 
    });
      
      
      
         
    $('.menu_open').on('click',  function(event){
           $('.paddinginside').removeClass("active");
           $(this).parent().find('.paddinginside').addClass("active");
           return false;
      });
      
    $('.close_edit_div').on('click',  function(event){
          $('.edit_link_div').css("display","none");
        return false;
      });
  
     $('.submit_form').on('click',  function(event){
         clearDataSave();
         $("#response").val($(".sortable").html());
         return true;
      });
    
    
     
   $('#icons_list a').on('click',  function(event){
         var icon_class = $(this).data("icon");
         var wich_class= $(".icon_class").val();
         var after_class= $(".icon_after").val();
         $(wich_class).data('icons',icon_class);
         $(after_class).after("<i class='icon_result fa "+icon_class+"'></i><a href='#' class='fa_delete remove_button' onclick='return remove_icon(\""+wich_class+"\")'></a>"); 
         $('#load_icons_modal').modal('hide');
         
          return false;
      });
    
      $('.sortable').nestedSortable({
		forcePlaceholderSize: true,
		items: 'li',
		handle: 'a.fa_move',
		placeholder: 'menu-highlight',
		listType: 'ul',
		maxLevels: 3,
		opacity: .6,
	}); 
      
     initiateMenu();
     
  });
  
  function initiateMenu(textReturn='none'){
    var text = '<a href="#" class="fa_move move_bdn"></a>'+
               '<a href="#" class="fa_delete fa_small delete_ittem"></a>' +
               '<a href="#" class="fa_edit fa_small"></a>';
    if(textReturn == 'none'){ 
     $('.menu-box li').prepend(text);  
     $('.menu-box > ul > li').prepend('<label class="setMegaMenu"> <input type="checkbox" class="makeMegaMenu" > Mega Menu </label>');         
      CheckMegaMenu();
     }else{
        return text;
     }
 }
  
  function CheckMegaMenu(){
    setTimeout(function() {
      $('.menu-box li.megaMenu .makeMegaMenu').prop('checked', true);
    }, 1000);
  }
  
  function clearDataSave(){
    $('.move_bdn').remove();
    $('.delete_ittem').remove();
    $('.fa_small').remove();
    $('.setMegaMenu').remove();
  }
 
  
  function simple_link(select_class='',type_get='select'){
     var link_href  = type_get=='text'? $(select_class+'_href').val() : $( select_class +' option:selected').val();
     var link_text  = type_get=='text'? $(select_class).val(): $( select_class +' option:selected').text();
     //var attr= select_class==''? 'link' : $( select_class +' option:selected').data("info");
     var icon_insert= $(select_class).data('icons') ? '<i class="fa '+$(select_class).data('icons')+'"></i>':"";
     
     var is_login_link = link_href =='#login'?  ' login_link' : '';
     var is_registr = link_href =='#registration'?  ' rigistration_link' : '';
      
      var text = '<li>' + 
                    '<a href="'+link_href.replace(/\s/g, '')+'"  class="simple_link'+ is_login_link + is_registr +'">'+icon_insert+link_text.trim()+'</a>' +
                '</li>';
      
      if( $(".menu-list li.activeIn").length ){
        if($(".menu-list li.activeIn ul").length){
           $(".menu-list li.activeIn ul").append(text);
        }else{
           $(".menu-list li.activeIn").append('<ul>'+text+'</ul>');
        }
          
      }else{
        $(".menu-list").append(text);
      }
     
          clearDataSave();              
          initiateMenu();
          
      return false;                        
  }
function simple_link_all(select_class='',type_get='select'){ 
      var text ='';
     var i=0, main =0, open=2;
    $(select_class+' option').each(function(){
      var link_href = $(this).val();
      var link_text = $(this).text();
      var dataAttr = $(this).data('val');
      
            if(dataAttr == "main"){
                var beginD = i != 0 ? '</li></ul>' : '';
                
                text = text+ beginD + '<li>' + 
                                        '<a href="'+link_href.replace(/\s/g, '')+'"  class="simple_link">'+link_text.trim()+'</a>'+
                                      '<ul>';
                main = i;
                open = 4;
             }
             
        if(main != i){
           text = text+ '<li>' + 
                           '<a href="'+link_href.replace(/\s/g, '')+'"  class="simple_link">'+link_text.trim()+'</a>' +
                        '</li>';
          }
          
         
        if(dataAttr == "main" && main != i && open == 4){
              open = 3;
         }
       
        i++;
    });
    
    text = text.replace("<ul></li></ul>", "</li>");
    text = open == 4 ? text + "</li></ul>" : text ;
      
     
    $(".menu-list").append(text);
    
     clearDataSave();              
     initiateMenu();
          
    return false;
  }
  
   function simple_link_update(){
       
      var icon_insert= $('.link_edit_text').data('icons') ? '<i class="fa '+$('.link_edit_text').data('icons')+'"></i>':"";
      $(".editThisPoint").attr("href",$('.link_edit_href').val());  
      $(".editThisPoint").html(icon_insert + $('.link_edit_text').val());                      
       
      $('.edit_link_div').css("display","none");
       $('.menu-box li a').removeClass("editThisPoint");
      return false;                       
  }
  
  
 function select_url_fromlist(obj, class_enter_link, class_enter_text){
     $(class_enter_link).val($(obj).val());
    $(class_enter_text).val($(obj).find("option:selected").text());
 } 
  
  
 function search_icon(value_element) {
    var value = value_element.toLowerCase();  
    $("#icons_list .col-sm-4").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
 } 

function icon_setup(class_select='', obj){
   $(".icon_class").val(class_select); 
   $(".icon_after").val("."+$(obj).attr("class")); 
   remove_icon(class_select);
 }
 
 function remove_icon(class_remove){
   $(class_remove).data('icons','');
   $('.remove_button').remove(); 
   $(".icon_result").remove();
   return false;  
 }
 
