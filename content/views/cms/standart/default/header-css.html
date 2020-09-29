<?php 

if(!empty($optionsData)){
 $optionsPage = @json_decode( $optionsData, true);   
    
$style= json_decode(($optionsPage['style']?? ''), true);
 $cssContent='';
 if(!empty($style)){
       foreach($style as $k_style => $v_style){
          $cssContent .='#'.$k_style.', #'.$k_style.' a , #'.$k_style.' p, #'.$k_style.' button { '.\Wh::checkEmpty(($v_style['font_color'] ?? ''), "color:", ";").'  }
           #'.$k_style.'{ 
                '. \Wh::checkEmpty(($v_style['bg_image'] ?? ''), "background-image: url('", "');") . 
                  (@$v_style['bg_type'] == "fixed" ? "background-attachment:fixed;" :"").   
                   'padding: '.\Wh::checkDefault(($v_style['paddingt'] ?? ''),'20').'px '.\Wh::checkDefault(($v_style['paddingr'] ?? ''),'15').'px '
                              .\Wh::checkDefault(($v_style['paddingb']?? ''),'20').'px '.\Wh::checkDefault(($v_style['paddingl'] ?? ''),'15').'px; 
             }
           #'.$k_style.':before {
                 display:block; '. \Wh::checkEmpty(($v_style['bg_color'] ?? ''), "background:", ";") .' '. \Wh::checkEmpty(($v_style['bg_opacity'] ?? ''), "opacity:", ";") .'
             }
           '. \Wh::checkDefault(($v_style['fullcss'] ?? ''), '');
      }
    }
   
   $cssContent .=  $optionsPage['cssdirect'] ?? '';
 ?>
 
<style type="text/css">
<!--
    {!! @\Wh::minify_css(@$cssContent)  !!}	
-->
</style>
<?php } ?>