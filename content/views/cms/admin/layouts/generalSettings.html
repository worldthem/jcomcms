<?php   
   $opt = @$v['option'];
   $option = isset($v['function'])? $opt() : @explode("|",@$opt); 
   $value = isset($v['parent']) ? @\Wh::get_settings_key($v['parent'], $k) :  @\Wh::get_settings($k, "value1");
   $type = @$v['type'];
   $n_input = isset($v['parent']) ? $v['parent'].'['.$k.']'  : $k;
?> 
<tr>
  <th>{!! @$v['title'] !!}</th>
    <td>
       <div class="col-md-12">
        @if($type=="select")
            <select name="{{$n_input}}" class="form-control">
                   <option value="">{{ _l('Select') }}</option>
                     @foreach($option as $voption)
                       <?php $k_opt= is_array($voption)? $voption[0]: $voption ;
                             $v_opt= is_array($voption)? $voption[1]: $voption ;
                        ?>
                       <option value="{{ $k_opt }}" {{@$value ==$k_opt? 'selected=""':''}}>{{$v_opt}}</option>
                     @endforeach
            </select>
         
         @elseif($type=="input")
           <input type="text" class="form-control" name="{{$n_input}}" value="{{ $value }}" />
         
          @elseif($type=="checkbox")
          <label class="switch">
            <input type="checkbox" class="checkbox" name="{{$n_input}}"   {{@$value == @$option[0]? 'checked=""':''}} value="{{ @$option[0] }}" />
            <span class="slider round"></span>
          </label>   
         @elseif($type=="textarea")
             <textarea name="{{$n_input}}" class="form-control">{{ $value }}</textarea>
         @endif
       
       </div>   
     
      
    </td> 
        
        
 </tr>