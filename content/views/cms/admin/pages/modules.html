@extends('admin.layouts.default')

@section('title', _l('Modules'))
@section('content')
 
<div class="col-md-12">
 <div class="card">
        
         <div class="header">
           <h4 class="title">{{ _l('Modules')}}</h4>
        </div>
     <div class="add_new_top">  
    <form action="{{URL::to('/')}}/admin/modules/store" method="post" enctype="multipart/form-data">
          <input type="hidden" name="_token" value="{{ csrf_token() }}">
          <input type="hidden" name="id" value="new">
           
            <div class="col-md-4">
             <label>{{ _l('Add new one') }}</label><br />
              <input type="file" class="form-control" name="module" accept='application/zip'/>
              <small><em>{{ _l('Select module file (zip file)') }}</em></small>
           </div>
          
         <div class="col-md-2"><label><br /></label><br />
           <button type="submit" class="btn btn_small">{{ _l('Add new one') }}</button>  
        </div>
         
     <div class="clear"></div>
    </form> 
   </div> 
      
        
     <div class="content table-responsive table-full-width">
         <table class="table table-hover table-striped">
             <thead>
                 <th>{{ _l('Name') }}</th>
                 <th>{{ _l('Description') }}</th>
                 <th style="width: 150px"> </th>
                 <th style="width: 150px"> </th>
                 <th style="width: 40px"> </th>
             </thead>
             <tbody>
                @foreach ($rows as $k=>$val)
                  <?php $config= \Wh::get_config(@$val, 'all'); ?>
                   <tr> 
                       <td>
                            {{$val}} 
                       </td>  
                       <td> {!! @$config['description'] !!}</td>
                        <td>  
                         @if(isset($config['urlsettings']))
                           <a href="{{ url($config['urlsettings']) }}" > 
                              <i class="fa fa-sliders"></i> {{ _l('Settings') }}
                          </a>
                         @endif 
                        </td> 
                        <td> 
                           <a href="{{URL::to('/')}}/admin/modules/activate/{{@$val}}" class="btn_text" >
                            <span class="{{ in_array($val, $active) ? 'fa_publish':'fa_unpublish'}}"></span>
                            {{ in_array($val, $active) ? _l('Deactivate'): _l('Activate')  }} 
                           </a>
                       </td>
                      
                      <td> 
                        <a href="{{URL::to('/')}}/admin/modules/destroy/{{@$val}}" class="fa_delete" onclick="return confirm('You are sure?') ? true : false;"> </a>
                     </td>
                  </tr>
                @endforeach
             </tbody>
         </table>
      </div>
  </div>
</div>
@stop