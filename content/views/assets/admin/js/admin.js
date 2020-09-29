$( function() {  
     $('.navbar-toggle').on('click',  function(event){
        
          if(document.querySelector('.collapse') !== null ){
             $('.navbar-collapse').removeClass("collapse");
            }else{
             $('.navbar-collapse').addClass("collapse");
            }
           
          return false;
      });
      
         $('.showAddCat').click(function(){ 
            var catType = $(this).attr('data-show');
             $("#catType_"+catType).toggle();
            return false;
        });  
 });  
  
 function addNewCategory(catType, url='', productID='' ){
    var newTitle = '';
    var title = $("#catType_"+catType + " .catName").map(function() {
           newTitle =  newTitle+ "&title_" + $(this).data("lang")+"="+$(this).val();
        }); 

    var parent = $("#catType_"+catType + " .catParent").val();
    var readyData = "productID="+productID+"&parent="+parent+"&type="+catType +"&methodadd=ajax" + newTitle ;

    access_url(url, ".loadResult_"+catType, ".smallLoad_"+catType, "none", readyData );
     $("#catType_"+catType + " .catName").val("");
    return false;
 } 
  
  
  
function access_url(url, resultShow, load_class="none", get_data="none", readyData="") {
     
 var token =$('meta[name="csrf-token"]').attr('content');
 var notice_class= load_class =="none"? resultShow : load_class;
      $(notice_class).html('<img style="margin:0 auto;" src="'+url_resources+'/img/ajax-loader.gif" width="20" height="20" />');
    
   var str = get_data == "none"?  (readyData !=""? readyData : "" ) : $(get_data).serialize();
   
     $.ajax({
        type: 'POST',
        headers: {'X-CSRF-Token': token},
        url: url,
        data: str,
        //contentType:"application/json",
        success: function(data) {
               //$('.general_load_ajax').css("display","none"); 
               if(load_class !="none"){
                   $(load_class).html("");
                 }
                 
                $(resultShow).html(data); 
             },
            error: function(jqXHR, textStatus, errorThrown) {
                 $(notice_class).html(" Error, Please write to webmaster by using Contact page"); 
             }  
    }) ;
    
  
}


function updateSerialiseFromDiv(url, resultClass, divSerialise="none"  ) {
           
        $(resultClass).html('<img style="margin:0 auto;" src="'+url_resources+'/img/ajax-loader.gif" width="20" height="20" />');
       
        var token =$('meta[name="csrf-token"]').attr('content');
        
        var str = $(divSerialise + ' :input').serialize();

       $.ajax({
        type: 'POST',
        headers: {'X-CSRF-Token': token},
        url: url,
        data: str,
        //contentType:"application/json",
        success: function(data) {
                $(resultClass).html(data); 
             },
            error: function(jqXHR, textStatus, errorThrown) {
                 $(resultClass).html(" Error, Please write to webmaster by using Contact page"); 
             }  
    }) ;
    
    return false;
}


function remove_main_image(id_image='',id_input=''){
   $(id_image).remove();
   $(id_input).val("");
 }

function save_field(url, notice_class, id, confirm_message = "") {

    if(confirm_message =="confirm"){
        var r = confirm("Are you shure?");
        if (r == false){ return false;} 
    }
    
    var token =$('meta[name="csrf-token"]').attr('content');
 
   $(notice_class).html('<img style="margin:0 auto;" src="'+url_resources+'/img/ajax-loader.gif" width="20" height="20" />');
       

    // var str =  'id='+id+"&field="+field+'&data='+value_input;
    var str = $(".saverow"+id+ ' :input' ).serialize();

     $.ajax({
        type: 'POST',
        headers: {'X-CSRF-Token': token},
        url: url,
        data: str,
        //contentType:"application/json",
        success: function(data) {
              $(notice_class).html(data);
             },
            error: function(jqXHR, textStatus, errorThrown) {
                 $(notice_class).html(" Error, Please write to webmaster by using Contact page"); 
             }  
    }) ;
    
  
}

function randomStr(length) {
   var result           = '';
   var characters       = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
   var charactersLength = characters.length;
   for ( var i = 0; i < length; i++ ) {
      result += characters.charAt(Math.floor(Math.random() * charactersLength));
   }
   return result;
}

function upload_files(obj, id_input, cl_res, class_show_load, parent_id, field_1='' ,field_2=''  ){
   $(class_show_load ).html('<img src="'+url_resources+'/img/ajax-loader.gif" style="width:15px;" />');
   var uploadData= [''];
  var newData = new FormData(),length;
  var token =$('meta[name="csrf-token"]').attr('content');

        $.each($('input'+id_input).prop("files"), function(i, file) {
             newData.append('file'+i, file);
             //uploadData.push(file);
            length=i;
        }); 
     
          
        if(field_1 !=''){
         newData.append('field1', $(field_1).val());
        }
        if(field_2 !=''){
         newData.append('field2', $(field_2).val());
        }  
         newData.append('length',length);
         newData.append('parent_id', parent_id);
         var url_upload = $(obj).attr('data-url');

      $.ajax({
			url: url_upload, 
			type: "POST",
            headers: {'X-CSRF-Token': token},
            data: newData,
            enctype: 'multipart/form-data',
		    cache: false,
            contentType: false,
            processData: false, 
			success: function (res) {
			  // var resultss= res.split("~");
              // $(cl_res ).html(resultss[0]);
               $(cl_res ).html(res);
               $(class_show_load).html("");
             },
            error: function(jqXHR, textStatus, errorThrown) {
                  $(class_show_load).html("");
                 $(cl_res).html(" Error, Please write to webmaster by using Contact page"); 
             } 
         });
}



function check_all(thisis){
       // If user clicks outside the element, hide it!
     var allelem= document.querySelectorAll(".checkboxeach");
  
    if (thisis.checked == true){
       for(var i = 0; i<=allelem.length; i++ ){
           allelem[i].checked = true;
        }
    } else {
         for(var i = 0; i<=allelem.length; i++ ){
           allelem[i].checked = false;
        }
    }
    
}


function edit_row_tr(id){
   $(".edit_text").css("display","inline-table"); 
   $(".hide_edit").removeClass('show_edit') ;
   $(id + " .edit_text").css("display","none"); 
   $(id + " input, "+ id + " select, "+ id + " textarea, "+ id + " label, " + id + " .hide_edit").addClass('show_edit');
}
 
function option_all(){
   $(".disabled_option").css("display","table-row");
}
function option_enabled(){
   $(".disabled_option").css("display","none");
}

function load_images(){
    $("#loading").css("display","block");
 
    var result_is= '';
         $.ajax({
            type:"GET",
            url: url_website+"/admin/import-step-5",
            success: function (data) {
                 result_is = data;
                 if(data=="done"){
                    $("#loading").html('<img src="'+url_resources+'/img/active.png" />');
                  }
              },
            complete: function() {
                 if(result_is=="continue"){
                      setInterval(load_images, 2000); 
                 }else{
                      clearInterval(load_images);
                  }        
              }
               
           });
 } 
 
 function translateText(langShort='', langLang=''){ 
    $("#langTranslate").val(langShort); 
    $(".languageName").text(langLang);
    $(".languageName").attr("href","https://translate.google.com/#view=home&op=translate&sl=en&tl=" + langShort);
 }
 
 function copyText(idDiv) {
  selectText(idDiv);
  document.execCommand("copy");
  alert("Copied");
}

function selectText(containerid) {
    if (document.selection) { // IE
        var range = document.body.createTextRange();
        range.moveToElementText(document.getElementById(containerid));
        range.select();
    } else if (window.getSelection) {
        var range = document.createRange();
        range.selectNode(document.getElementById(containerid));
        window.getSelection().removeAllRanges();
        window.getSelection().addRange(range);
    }
}
 
function setInput(valueSet='', inputClas='', objectThis='' ){
    $(inputClas).val(valueSet);
    $(objectThis).parent().find('.activeIs').removeClass('activeIs');
    $(objectThis).addClass("activeIs");
    
} 