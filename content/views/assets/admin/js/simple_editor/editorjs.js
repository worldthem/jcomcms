var parms = [{
	"cmd": "aCommandName",
	"desc": "A DOMString representing the name of the command"
}, {
	"cmd": "aShowDefaultUI",
	"desc": "A Boolean indicating whether the default user interface should be shown. This is not implemented in Mozilla."
}, {
	"cmd": "aValueArgument",
	"desc": "A DOMString representing some commands (such as insertimage) require an extra value argument (the image's url). Pass an argument of null if no argument is needed."
}];
var commands = [  {
	"cmd": "undo",
	"icon": "undo",
	"desc": "Undoes the last executed command."
}, {
	"cmd": "redo",
	"icon": "repeat",
	"desc": "Redoes the previous undo command."
},{
	"cmd": "bold",
	"icon": "bold",
	"desc": "Toggles bold on/off for the selection or at the insertion point. (Internet Explorer uses the STRONG tag instead of B.)"
}, {
	"cmd": "italic",
	"icon": "italic",
	"desc": "Toggles italics on/off for the selection or at the insertion point. (Internet Explorer uses the EM tag instead of I.)"
}, {
	"cmd": "underline",
	"icon": "underline",
	"desc": "Toggles underline on/off for the selection or at the insertion point."
}, {
	"cmd": "strikeThrough",
	"icon": "strikethrough",
	"desc": "Toggles strikethrough on/off for the selection or at the insertion point."
}, {
	"cmd": "contentReadOnly",
	"desc": "Makes the content document either read-only or editable. This requires a boolean true/false to be passed in as a value argument. (Not supported by Internet Explorer.)"
}, {
	"cmd": "copy",
	"icon": "clipboard",
	"desc": "Copies the current selection to the clipboard. Clipboard capability must be enabled in the user.js preference file. See"
}, {
	"cmd": "cut",
	"icon": "scissors",
	"desc": "Cuts the current selection and copies it to the clipboard. Clipboard capability must be enabled in the user.js preference file. See"
}, {
	"cmd": "increaseFontSize",
    "icon": "long-arrow-up",
	"desc": "Adds a BIG tag around the selection or at the insertion point. (Not supported by Internet Explorer.)"
}, {
	"cmd": "decreaseFontSize",
    "icon": "long-arrow-down",
	"desc": "Adds a SMALL tag around the selection or at the insertion point. (Not supported by Internet Explorer.)"
}, {
	"cmd": "delete",
	"icon": "trash-o",
	"desc": "Deletes the current selection."
}, {
	"cmd": "enableInlineTableEditing",
	"desc": "Enables or disables the table row and column insertion and deletion controls. (Not supported by Internet Explorer.)"
}, {
	"cmd": "enableObjectResizing",
	"desc": "Enables or disables the resize handles on images and other resizable objects. (Not supported by Internet Explorer.)"
}, {
	"cmd": "fontName",
	"val": "'Inconsolata', monospace",
    "icon": "paragraph",
    "windowtype": "fontfamily",
	"desc": "Changes the font name for the selection or at the insertion point. This requires a font name string (\"Arial\" for example) to be passed in as a value argument."
}, {
	"cmd": "fontSize",
	"val": "1-7",
    "windowtype": "size",
	"icon": "text-height",
	"desc": "Changes the font size for the selection or at the insertion point. This requires an HTML font size (1-7) to be passed in as a value argument."
}, {
	"cmd": "foreColor",
	"val": "rgba(0,0,0,.5)",
    "icon": "font",
    "windowtype": "colorWindow",
    "style": "style='color:#C96;'",
	"desc": "Changes a font color for the selection or at the insertion point. This requires a color value string to be passed in as a value argument."
}, {
	"cmd": "backColor",
	"val": "red",
    "icon": "font",
    "windowtype": "colorWindow",
    "style": "style='background:#C96;'",
	"desc": "Changes the document background color. In styleWithCss mode, it affects the background color of the containing block instead. This requires a color value string to be passed in as a value argument. (Internet Explorer uses this to set text background color.)"
},{
	"cmd": "hiliteColor",
	"val": "Orange",
    "icon": "font",
    "windowtype": "colorWindow",
    "style": "style='background:#C96;'",
	"desc": "Changes the background color for the selection or at the insertion point. Requires a color value string to be passed in as a value argument. UseCSS must be turned on for this to function. (Not supported by Internet Explorer.)"
}, {
	"cmd": "formatBlock",
    "val": "<blockquote>",
	"desc": "Adds an HTML block-style tag around the line containing the current selection, replacing the block element containing the line if one exists (in Firefox, BLOCKQUOTE is the exception - it will wrap any containing block element). Requires a tag-name string to be passed in as a value argument. Virtually all block style tags can be used (eg. \"H1\", \"P\", \"DL\", \"BLOCKQUOTE\"). (Internet Explorer supports only heading tags H1 - H6, ADDRESS, and PRE, which must also include the tag delimiters &lt; &gt;, such as \"&lt;H1&gt;\".)"
}, {
	"cmd": "forwardDelete",
	"desc": "Deletes the character ahead of the cursor's position.  It is the same as hitting the delete key."
}, {
	"cmd": "heading",
	"val": "h3",
	"icon": "header",
    "windowtype": "header",
	"desc": "Adds a heading tag around a selection or insertion point line. Requires the tag-name string to be passed in as a value argument (i.e. \"H1\", \"H6\"). (Not supported by Internet Explorer and Safari.)"
}, {
	"cmd": "indent",
	"icon": "indent",
	"desc": "Indents the line containing the selection or insertion point. In Firefox, if the selection spans multiple lines at different levels of indentation, only the least indented lines in the selection will be indented."
}, {
	"cmd": "insertBrOnReturn",
	"desc": "Controls whether the Enter key inserts a br tag or splits the current block element into two. (Not supported by Internet Explorer.)"
}, {
	"cmd": "insertHorizontalRule",
    "icon": "window-minimize",
	"desc": "Inserts a horizontal rule at the insertion point (deletes selection)."
}, {
	"cmd": "insertHTML",
	"val": "<h3>Life is great</h3>",
	"icon": "code",
    "pupap": "html",
	"desc": "Inserts an HTML string at the insertion point (deletes selection). Requires a valid HTML string to be passed in as a value argument. (Not supported by Internet Explorer.)"
}, {
	"cmd": "insertOrderedList",
	"icon": "list-ol",
	"desc": "Creates a numbered ordered list for the selection or at the insertion point."
}, {
	"cmd": "insertUnorderedList",
	"icon": "list-ul",
	"desc": "Creates a bulleted unordered list for the selection or at the insertion point."
}, {
	"cmd": "insertParagraph",
	"icon": "paragraph",
	"desc": "Inserts a paragraph around the selection or the current line. (Internet Explorer inserts a paragraph at the insertion point and deletes the selection.)"
}, {
	"cmd": "insertText",
	"val": "",
	"icon": "file-text-o",
	"desc": "Inserts the given plain text at the insertion point (deletes selection)."
},  {
	"cmd": "justifyCenter",
	"icon": "align-center",
	"desc": "Centers the selection or insertion point."
}, {
	"cmd": "justifyFull",
	"icon": "align-justify",
	"desc": "Justifies the selection or insertion point."
}, {
	"cmd": "justifyLeft",
	"icon": "align-left",
	"desc": "Justifies the selection or insertion point to the left."
}, {
	"cmd": "justifyRight",
	"icon": "align-right",
	"desc": "Right-justifies the selection or the insertion point."
}, {
	"cmd": "outdent",
	"icon": "outdent",
	"desc": "Outdents the line containing the selection or insertion point."
}, {
	"cmd": "paste",
	"icon": "clipboard",
	"desc": "Pastes the clipboard contents at the insertion point (replaces current selection). Clipboard capability must be enabled in the user.js preference file. See"
},  {
	"cmd": "removeFormat",
    "icon": "eraser",
	"desc": "Removes all formatting from the current selection."
}, {
	"cmd": "selectAll",
	"desc": "Selects all of the content of the editable region."
}, {
	"cmd": "subscript",
	"icon": "subscript",
	"desc": "Toggles subscript on/off for the selection or at the insertion point."
}, {
	"cmd": "superscript",
	"icon": "superscript",
	"desc": "Toggles superscript on/off for the selection or at the insertion point."
},  {
	"cmd": "createLink",
	"val": "",
	"icon": "link",
	"desc": "Creates an anchor link from the selection, only if there is a selection. This requires the HREF URI string to be passed in as a value argument. The URI must contain at least a single character, which may be a white space. (Internet Explorer will create a link with a null URI value.)"
},  {
	"cmd": "unlink",
	"icon": "chain-broken",
	"desc": "Removes the anchor tag from a selected anchor link."
}, {
	"cmd": "useCSS ",
	"desc": "Toggles the use of HTML tags or CSS for the generated markup. Requires a boolean true/false as a value argument. NOTE: This argument is logically backwards (i.e. use false to use CSS, true to use HTML). (Not supported by Internet Explorer.) This has been deprecated; use the styleWithCSS command instead."
}, {
	"cmd": "styleWithCSS",
	"desc": "Replaces the useCSS command; argument works as expected, i.e. true modifies/generates style attributes in markup, false generates formatting elements."
}, {
	"cmd": "insertImage",
	"val": "http://dummyimage.com/160x90",
	"icon": "picture-o",
    "pupap":"img",
	"desc": "Inserts an image at the insertion point (deletes selection). Requires the image SRC URI string to be passed in as a value argument. The URI must contain at least a single character, which may be a white space. (Internet Explorer will create a link with a null URI value.)"
}, {
	"cmd": "insertImage",
	"val": "",
	"icon": "picture-o",
    "desc": "Inserts an image at the insertion point (deletes selection). Requires the image SRC URI string to be passed in as a value argument. The URI must contain at least a single character, which may be a white space. (Internet Explorer will create a link with a null URI value.)"
}, {
	"cmd": "insertIcon",
    "icon": "flag",
    "pupap":"icons",
	"desc": "Inserts a icon"
}];

var commandRelation = {};

 function closeDiv(elemDiv = ""){
     $(elemDiv).hide(200);
     return false;
  } 


function supported(cmd) {
	var css = !!document.queryCommandSupported(cmd.cmd) ? "btn-succes" : "btn-error"
	return css;
};

function icon(cmd) {
	return (typeof cmd.icon !== "undefined") ? "fa fa-" + cmd.icon : "";
};
function isOrNot(typeIs) {
	return (typeof typeIs !== "undefined") ? typeIs : "";
};

function doCommand(cmdKey, valWindow = "") {
	var cmd = commandRelation[cmdKey];
	if (supported(cmd) === "btn-error") {
		alert("execCommand(“" + cmd.cmd + "”)\nis not supported in your browser");
		return;
	}
    if(valWindow == ""){
	     val = (typeof cmd.val !== "undefined") ? prompt("Value for " + cmd.cmd + "?", cmd.val) : "";
       }else{
         val = valWindow;
       }
	document.execCommand(cmd.cmd, false, (val || "")); // Thanks to https://codepen.io/bluestreak for finding this bug
}


function supWindow(type_wind="", typeWind = ""){
    var colorPalette = ['#000000', '#FF9966', '#6699FF', '#99FF66', '#CC0000', '#00CC00', '#0000CC', '#333333', '#0066FF', '#FFFFFF'];
    var sizePalette = ['1', '2', '3', '4', '5', '6', '7'];
    var headerPalette = ['H1', 'H2', 'H3', 'H4', 'H5', 'H6'];
    var fontfamilyPalette = ['Monospace', 'Serif', 'Sans Serif', 'Arial', 'Tahoma', 'Impact','Arial Black','Verdana','Georgia','Symbol','Trebuchet MS'];
    var style = "";
    var showText = true;
    
    if(typeWind =="size"){
        var arrayJs = sizePalette;
        style = 'none:';
    }else if(typeWind =="header"){
        var arrayJs = headerPalette;
        style = 'none:';
    }else if(typeWind =="fontfamily"){
        var arrayJs = fontfamilyPalette;
        style = 'font-family:';
    }else{
        var arrayJs = colorPalette;
        style = 'background-color:';
        showText = false;
     }
   
 
    var windowDiv = '<div class="block_info_edit">';
        for (var i = 0; i < arrayJs.length; i++) {
           windowDiv = windowDiv +  '<a href="#" onclick="doCommand(\''+type_wind+'\', \'' + arrayJs[i] + '\'); return false;" style="'+ style + arrayJs[i] + ';" class="palette-item">'+(showText ? arrayJs[i] :'&nbsp;')+'</a>';  
        }
        windowDiv = windowDiv+ "</div>";
      
        
    return windowDiv;
}

function putContent(){
    document.querySelector(".container_edit").innerHTML = document.querySelector(".formContent").value;
}


function uploadRedactor(){
   //$(class_show_load ).html('<img src="'+url_resources+'admin_assets/img/ajax-loader.gif" style="width:15px;" />');
   $(".loadImgwait i").removeClass("fa-picture-o");
   $(".loadImgwait i").addClass("fa-refresh fa-spin");
 
   var uploadData= [''];
  var newData = new FormData(),length;
  var token =$('meta[name="csrf-token"]').attr('content');
  
        $.each($('input#loadFileEditor').prop("files"), function(i, file) {
             newData.append('file'+i, file);
             //uploadData.push(file);
            length=i;
        }); 
        
        newData.append('length',length);
        
        $.ajax({
			url: '/admin/upload-simple/simple', 
			type: "POST",
            headers: {'X-CSRF-Token': token},
            data: newData,
		    cache: false,
            contentType: false,
            processData: false, 
			success: function (res) {
			    
			  doCommand('insertImage', res);
              $(".loadImgwait i").removeClass("fa-refresh fa-spin");
              $(".loadImgwait i").addClass("fa-picture-o");  
             },
            error: function(jqXHR, textStatus, errorThrown) {
                $(".loadImgwait i").removeClass("fa-refresh fa-spin");
              $(".loadImgwait i").addClass("fa-picture-o"); 
                  alert("Error, Please write to webmaster by using Contact page");
             } 
         });
}

function popapWindow(typeWind = ""){
    var HtmlContent = "";
    
    if(typeWind =="img"){
         $('#loadFileEditor').click();
       return false;  
    }else if(typeWind =="icons"){
        //var htmlQuery =document.querySelector("#web-application").innerHTML ;  
            HtmlContent = document.querySelector("#web-application").innerHTML;
            
             
    }else if(typeWind =="html"){
      var htmlQuery =document.querySelector(".container_edit").innerHTML ;  
       HtmlContent = '<textarea class="formContent form-control">'+format(htmlQuery)+'</textarea>'
       +'<a href="#" style="margin-top: 10px;float: left;" onclick="return closeDiv(\'.editor\');" class="btn btn_small">Cancel</a>'
       +'<a href="#" style="margin-top: 10px;float: right;" onclick="putContent(); return closeDiv(\'.editor\');" class="btn btn_small btn_active">Update</a>'; 
    }
    
    document.querySelector(".editor").style.display="block";
    document.querySelector(".blockContent").innerHTML = HtmlContent;
    
    $(".blockContent .fontawesome-icon-list a").click(function() { 
       var dataIcon =  $(this).data("icon");
        
       doCommand('insertHTML', '<span class="fa '+dataIcon+'">&nbsp;</span>');  
       return closeDiv('.editor');
     });
}



function format(html) {
    var tab = '\t';
    var result = '';
    var indent= '';

    html.split(/>\s*</).forEach(function(element) {
        if (element.match( /^\/\w/ )) {
            indent = indent.substring(tab.length);
        }

        result += indent + '<' + element + '>\r\n';

        if (element.match( /^<?\w[^>]*[^\/]$/ )) { 
            indent += tab;              
        }
    });

    return result.substring(1, result.length-3);
}


function init() {
      var html = '';
	  var template = '<a href="#" class="%btnClass%" title="%cmd% - %desc%" onmousedown="event.preventDefault();" onclick="doCommand(\'%cmd%\'); return false;"><i class="%iconClass%" %style%></i></a>';
	  var templateWindows = '<div class="btn_parrent"><i class="%iconClass%" %style%></i>%window%</div>';
      var pupap = '<div class="btn_parrent%imgloadclass%" onclick="popapWindow(\'%typevar%\')";><i class="%iconClass%" %style%></i></div>';
      
    commands.map(function(command, i) {
	   if(typeof command.icon !== "undefined"){
		commandRelation[command.cmd] = command;
		var temp =  (typeof command.windowtype !== "undefined") ? templateWindows  : (supported(command) == "btn-succes" ?  template : "");
            temp =  (typeof command.pupap !== "undefined") ? pupap : temp ;
		temp = temp.replace(/%iconClass%/gi, icon(command));
		temp = temp.replace(/%desc%/gi, command.desc);
		temp = temp.replace(/%btnClass%/gi, supported(command));
		temp = temp.replace(/%cmd%/gi, command.cmd);
        temp = temp.replace(/%style%/gi, isOrNot(command.style) );
        temp = temp.replace(/%window%/gi, (typeof command.windowtype !== "undefined") ? supWindow(command.cmd, command.windowtype) : "" ) ;
        temp = temp.replace(/%typevar%/gi, (typeof command.pupap !== "undefined") ?  command.pupap : "" ) ;
        temp = temp.replace(/%imgloadclass%/gi, (typeof command.pupap !== "undefined") ?   (command.pupap=="img"? " loadImgwait":"") : "" ) ;
		html+=temp;
        }
	});
    
    var i = 1;
    var typeEditor = "";
    //var aside_from_top = document.querySelector(".tinymce").offsetTop;
    $(".tinymce").map(function() {
            i++;
            typeEditor = $(this).data("typeeditor");
            $(this).addClass("tinimceTextarea"+i);
            $(this).before( '<div class="toolbarEditor toolbar"></div>');
            var divMain = typeEditor =="full" ? '<div class="container_edit container_edit_full"> <div id="page_contents" class="tinimceEditor'+i+'"> '+$(this).val()+'</div></div>':'<div class="container_edit tinimceEditor'+i+'">'+$(this).val()+'</div>';
            $(this).before(divMain);
            //$(".tinimceEditor"+i).html();
            $(".toolbarEditor").html('<div class="editor_bottons">'+html+'</div>');
      });
      
       $(".tinymce").css("display","none");
    

    
    //document.querySelector(".container_edit").innerHTML = $(".tinymce").val();
    //document.querySelector(".toolbarEditor").innerHTML = '<div class="editor_bottons">'+html+'</div>';
    
    
    if(typeEditor == "full") {
       
    //setTimeout(function () {      
     var aside_from_top = $(".container_edit").offset().top;
     $("#page_contents div div.inside_grid").attr("contenteditable","true");
     //alert(aside_from_top);
    $(window).bind('scroll',function(){
       if($(window).scrollTop() > aside_from_top){
          $('.toolbar').addClass('fixed_toolbar');
        }else{
          $('.toolbar').removeClass('fixed_toolbar');  
        }
      });
       //}, 2000); 
  }else{
    $(".container_edit").attr("contenteditable","true");
  }
       
  document.execCommand('styleWithCSS', false, true);
  document.execCommand('enableObjectResizing', false, true);
  document.execCommand('defaultParagraphSeparator', false, 'p');
  /*
  // when new line should add a a <p>, by default was <div>  
 document.querySelector(".container_edit").addEventListener('keypress', function(ev){
    if(ev.keyCode == '13')
        document.execCommand('formatBlock', false, 'p');
}, false);
*/
 
 
}
 
function setCursor(pos, p) { 
    s = window.getSelection(),
    r = document.createRange();
    r.setStart(p, pos);
    r.setEnd(p, pos);
    s.removeAllRanges();
    s.addRange(r);
}

 $( function() { 
   init();

 $("body").append('<div class="editor"><div class="closs_editor" onclick="return closeDiv(\'.editor\');">x</div><div class="blockContent"></div></div><input type="file" id="loadFileEditor" onchange="uploadRedactor();" style="display:none;"/>');

    var input = $('input[type=submit]');
    var button = $('button[type=submit]');
    var form = input.length > 0 ? $(input[0].form) : false;
        form = form === false ?  (button.length > 0 ? $(button[0].form): false ) : form; 
        
        
     if(form !==false && $("textarea.tinymce")[0]){
        //form.onsubmit
        
        // Save data
           form.submit(function() { 
               $(".edit_text_this").remove();
               $(".inside_grid").removeAttr("contenteditable");
               $("#page_contents img").removeAttr("_moz_resizing");
               $("#page_contents img").removeAttr("_moz_dirty");
               
                var j=1;
               $(".tinymce").map(function() {
                    j++;
                    var htmlGet = $(".tinimceEditor"+j).html(); 
                    $(".tinimceTextarea"+j).val(htmlGet);
                     
             });
             return true;
           });
     }

 });