$( function() {
    $('#page_contents').designMode = "on";
	var colorPalette = ['000000', 'FF9966', '6699FF', '99FF66', 'CC0000', '00CC00', '0000CC', '333333', '0066FF', 'FFFFFF'];
    var forePalette = $('.fore-palette');
    var backPalette = $('.back-palette');

  for (var i = 0; i < colorPalette.length; i++) {
    forePalette.append('<a href="#" data-command="foreColor" data-value="' + '' + colorPalette[i] + '" style="background-color:' + '#' + colorPalette[i] + ';" class="palette-item"></a>');
    backPalette.append('<a href="#" data-command="backColor" data-value="' + '' + colorPalette[i] + '" style="background-color:' + '#' + colorPalette[i] + ';" class="palette-item"></a>');
  }
   for (var j = 1; j < 8; j++) {
    $('.size-palette').append('<a href="#" data-command="fontSize" data-value="' + '#' + j + '"">' + j + '</a>');
   }

  $('.toolbar a').on( "click", function(e) {
    var command = $(this).data('command');
    //var cmd = commandRelation[command];
    
    //document.execCommand('styleWithCSS', false, true);
    if (command == 'h1' || command == 'h2' || command == 'p') {
      document.execCommand('formatBlock', false, command);
    }
    if (command == 'foreColor' || command == 'backColor') {
        //alert($(this).data('value'));
        alert($(this).data('command'));
        var val = 'rgba(0,0,0,.5)';
        document.execCommand($(this).data('command'), false, (val || "") );  
    }
    if (command == 'createlink' || command == 'insertimage') {
      url = prompt('Enter the link here: ', 'http:\/\/');
      document.execCommand($(this).data('command'), false, url);
    } else document.execCommand($(this).data('command'), false, null);
     return false;
  });
});  