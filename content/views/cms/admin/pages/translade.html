@extends('admin.layouts.default')

@section('title', 'Language')
@section('content')
 
<div class="col-md-12">
  <div class="card">
  <div class="col-md-12">
 <h4>
   Google Translate:
   <a href="https://translate.google.com/?sl=en&tl={{$lang}}" target="_blank">
     https://translate.google.com/?sl=en&tl={{$lang}}
  </a>
  </h4>
  </div>
  
  
  @for($ii=0;$ii<2;$ii++)
  <?php  $begin= $ii ==0? 0 : count($rows) / 2; 
         $end= $ii == 0 ? count($rows) / 2 : count($rows) ; 
         $rand= rand();
       ?>
         <div class="col-md-6"> 
          <form action="{{URL::to('/')}}/admin/general-settings/language/fullTranslate" method="post">
            <input type="hidden" name="_token" value="{{ csrf_token() }}">
            <input type="hidden" name="lang" id="langTranslate" value="{{$lang}}">
            <input type="hidden" name="begin" value="{{$begin}}">
            <input type="hidden" name="end" value="{{$end}}">
            <div class="height20px"></div>
               <div class="col-xs-6">
                  <h4>{{_l("Copy all of this and paste in google translate")}}</h4>
                     <a href="#" class="btn btn_small" onclick="copyText('englishText{{$rand}}'); return false;">{{_l("Copy Text")}}</a>
                    <div class="height20px"></div>
                    <div class="englishText" id="englishText{{$rand}}">
                     @for($i=$begin;$i<$end;$i++)
                       {{ $rows[$i] }}<br>
                    @endfor
                   </div> 
               </div>
                <div class="col-xs-6">
                 <h4>{{_l("Paste all the translate text from google translate, one per line")}} </h4>
                     <div class="text_align_right">
                       <button type="submit" class="btn btn_small"><i class="fa fa-cloud-upload"></i> Update</button>
                    </div> 
                    <div class="height10px"></div> 
                   <textarea class="form-control" name="text" style="min-height: 1000px;" placeholder="Paste all the translate from google translate, one per line"></textarea>
                 <div class="height20px"></div>
                 <button type="submit" class="btn btn_small"><i class="fa fa-cloud-upload"></i> {{ _l('Update') }}</button>
              </div>
           
            <div class="clear"></div>
         </form>
       </div>  
    @endfor 
    <div class="clear"></div>
 </div>
</div>   
@stop