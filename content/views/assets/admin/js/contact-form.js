function uniqId() {
  return "name"+Math.round(new Date().getTime() + (Math.random() * 100));
}
function getRandomInt(max) {
  return Math.floor(Math.random() * Math.floor(max));
}

$(function() {
      
    //add new block
    $('.newField').click(function(){
      var html_code = $(".fields_html").html().replace(/random/g , function() {
                              return uniqId();
                            });
            var rand = uniqId();
         html_code = html_code.replace(/unicid/g , function() {
                            return rand;
                });



       $(".fields_list").append(html_code);



       
      return false;
    }); 
    
    //Remove block
      $( ".fields_list" ).delegate( ".delete_field", "click", function(){
        $(this).parent().parent().remove();
        return false;
      });
    
});    