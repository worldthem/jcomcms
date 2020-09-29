@extends('admin.layouts.default')
@section('title', 'Import products')
@section('content')
 
<div class="col-md-12">
 <div class="card">
     <div class="header" >
      <!-- Step 1  -->
           <form action="{{URL::to('/')}}/admin/import-step-one" method="post" enctype="multipart/form-data"> 
                <input type="hidden" name="_token" value="{{ csrf_token() }}">
                 <div class="col-md-3"><br /></div> 
                  <div class="col-xs-1 margin-top" >
                  <input type="text" name="separator" class="form-small" value="," /> 
                  <small> {{ _l('Separator') }} </small>
                 </div>
                 <div class="col-xs-1 margin-top" >
                  <input type="number" name="rows" class="form-small" value="5" /> 
                  <small> {{ _l('NR. rows') }} </small>
                 </div>
                 <div class="col-xs-3 margin-top" >
                   <input type="file" name="import" /> 
                   <small> {{ _l('Select the CSV file') }}</small>
                 </div>
                 
                  <div class="col-xs-3">
                    <button type="submit" class="btn"> {{ _l('Import') }}</button>
                 </div>  
        
                
             </form>
             <div class="clearfix"></div>
     </div>
    
     
     <?php 
        $file_name =  @\Wh::get_settings("import_data_from_csv_file_name","value");
     ?>
    @if(!empty($file_name)) 
     <hr/>
     <table class="table_scroll">
     <tr>
        <td>
          <a href="{{url("/admin/import-step-two/2")}}" class="btn btn_small">
           <strong> {{ $file_name }}</strong>
          </a> 
       </td>
       <td>  
            <a href="{{url("/admin/import-step-two/2")}}" class="btn btn_small">
              {{ _l('Step 2') }}
            </a>
       </td>
       <td>  
              <a href="{{url("/admin/import-step-4")}}" class="btn btn_small">
               {{ _l('Images') }}
             </a>
       </td>
     </tr>
     </table>
      @endif
     <hr/>
      <!-- Step 2  -->
      @if(!empty($row))
         <div class="scroll_bottom">
           <table class="table_scroll">
           <?php $j=0;?>
             @foreach($row as $val) 
               <tr>
                      <td style="min-width:50px;">
                         <a href="{{URL::to('/')}}/admin/import-step-two/{{$j}}">  {{ _l('Select Row') }} </a>
                     </td>
                  @foreach($val as $val2)
                      <td style="{{ !empty($val2)&& strlen(@$val2)>22 ? 'min-width: 200px;' : ''}} {{ empty($val2) ? 'width: 10px;' : ''}}">
                         {{$val2}}
                     </td> 
                  @endforeach  
               </tr>
                <?php $j++;?>
              @endforeach
           </table>
        </div>
      @endif
      
      <!-- Step 3  -->
      
      
       @if(!empty($row_single))
       <?php 
       $settings_variation= @json_decode(\Wh::get_settings("variation_list", "value1"),true); 
       $settings_specification= @json_decode(\Wh::get_settings("specification_list", "value1"),true);
      
       
       $fields = ['SKU'=>'SKU',
                  //'id'=>'ID (only numbers)',
                  'title'=>'Title',
                  //'cpu'=>'CPU',
                  'cat'=>'Categories',
                  'brand'=>'Brand', 
                  'price'=>'Regular price',
                  'sale_price'=>'Sale price',
                  'qtu'=>'Quantity',
                  'weight'=>'Weight',
                  'metad'=>'Meta description',
                  'metak'=>'Meta keyword',
                  'cpu_store'=>'External URL',
                  'text'=>'Description',
                  'description'=>'Small Description',
                  'main_image'=>'Main image',
                  'gallery'=>'Gallery',
                    ];
      $fields_variaion= [
          'sku'=>'SKU',
          'price'=>'Price',
          'qtu'=>'Quantity',
          'weight'=>'Weight',
      ];
      
      $settings_attributes = @\Wh::get_variation("_attributes_");  
                   
      if(!empty($settings_attributes)){
         foreach ($settings_attributes as $key=> $val){
            foreach($val as $k1=>$v1){
                    if($key=="variations"){
                       $fields_variaion[$k1] = @$v1['all']['name']; 
                    }else{
                       $fields[$k1] = @$v1['all']['name']; 
                    }
              }
           } 
       }
       
       ?>
       <form action="/admin/import-step-three" method="post" >
          <input type="hidden" name="_token" value="{{ csrf_token() }}">
          
          <p>
          
             <ul>
               <li> <b>{{ _l('Before you import make a backup of database') }} !!!</b> </li>
               <li>SKU  -  {{ _l('is a unique number could be numbers and leters or only number, you need to have one') }}<br /></li>
               
               <li> {{ _l('if you do not have an SKU than fill only column') }} <b> {{ _l('Parent') }}</b><br /></li>
               <li> {{ _l('if you don not select nothing for  column') }} <b> {{ _l('Condition if is child') }}</b>  {{ _l('than all products will be a parents') }}<br /></li>
               <li> {{ _l('If you want to update just fill the column') }} <b> {{ _l('Parent') }}</b><br /></li>
               <li> {{ _l('If you want to import the images then put them in folder') }} <b>/public/imgproduct</b></li>
               <li> {{ _l('if you have more then 200 products then you need to do on local') }} <strong> {{ _l('hosting or VPS') }}</strong>  {{ _l('and') }} <strong> {{ _l('increse the time of execution script to 1200(20minutes)') }} </strong></li>
             </ul>
           </p>
          
          
            <table class="table hovet_table">
            @if(count($row_single)>1)
               <th> Header </th>
               @endif 
               <th> Header </th>
               <th style="width: 120px;">  {{ _l('Parent') }}</th>
               @if(!empty($settings_attributes['variations']))
               <th style="width: 120px;">  {{ _l('Variation)') }}</th>
               <th style="width: 120px;">  {{ _l('Condition if is parrent') }}</th>
               <th style="width: 120px;">  {{ _l('Variation') }}</th>
               @endif
                <?php for($i=0; $i<$count_total;$i++ ){ ?>
                    <tr>
                     @if(count($row_single)>1)
                         <td>
                            <?php echo @$row_single[0][$i]?>
                         </td>
                      @endif   
                        <td>
                            <?php echo @$row_single[1][$i]?>
                        </td> 
                       <td>
                            <select class="form-control" name="parent[]"> 
                               <option value=""> {{ _l('Select') }}</option>
                               @foreach($fields as $key=>$name)
                                 <option value="{{$key}}">{{$name}}</option>
                               @endforeach
                           </select>
                       </td>
                     @if(!empty($settings_attributes['variations'])) 
                       <td>
                            <select class="form-control" name="child[]">
                               <option value=""> {{ _l('Select') }}</option> 
                               @foreach($fields_variaion as $key=>$name)
                                 <option value="{{$key}}">{{$name}}</option>
                               @endforeach
                           </select>
                       </td>
                        
                        <td>
                            <select class="form-control" name="condition_parent[]"> 
                                 <option value=""> {{ _l('Select') }}</option>
                                 <option value="empty"> {{ _l('Is empty(for variation this field will not be empty)') }}</option>
                                 <option value="notempty"> {{ _l('Not empty(for variation this fild will be empty)') }}</option>
                                 <option value="same"> {{ _l('SKU is the same and all of them are variations') }}</option>
                              </select>
                              <div class="clear" style="height: 3px;"></div>
                              <input type="text" name="condition_parent_input[]" placeholder=" {{ _l('or enter your value') }}" value="" class="control-form" />
                       </td>
                        
                       <td>
                            <select class="form-control" name="condition_child[]"> 
                                 <option value=""> {{ _l('Select') }}</option>
                                 <option value="SKU"> {{ _l('If this field is the same like parent SKU, That SKU what you select for column Parent') }} </option>
                            </select>
                       </td>
                      @endif 
                    </tr> 
                 <?php }?>  
            </table>
                    <div class="col-xs-12 text_align_center">
                    <button type="submit" class="btn">Import</button>
                     <p>
                        {{ _l('This process will take few minutes') }}
                     </p>
                  </div>
       </form>
     @endif
      
           @if(!empty($final))
             <div class="col-md-12">
              <p> {{ _l('Imported') }}:<br />
                {{$updated[0]}}- {{ _l('Main products') }} <br />
                {{$updated[1]}}- {{ _l('Variation of product') }} <br />
              </p>
             </div>
           @endif 
      
          @if(isset($images_import))
          
           <script type="text/javascript">
               <!--
            	    $(function (){
                         $("#startAjaxRequest").click(function () {
                            load_images();
                         });
                      });
                 -->
            </script>
          
          
             <div class="col-md-12 text_align_center">
                 <p>
                     1) {{ _l("Upload your images in folder public/import by using FTP (FileZilla) or hosting File Manager") }}<br />
                     2) {{ _l("After finish uploading push the button below") }}<br />
                   
                 </p>
                 
                 <button id="startAjaxRequest" class="btn">{{ _l("Start Import Images") }}</button>
                
                 <div id="loading" style="display: none;">
                    <img src="{{url('/resources/admin_assets/img/ajax-loader.gif')}}" />
                 </div>
                 <p><i>  {{ _l("If button not working you can import them manualy just update the link multiple time, when you will see: done, that's mean done") }}{{url("/admin/import-step-5")}}</i></p>
                 <div class="height40px"></div>
             </div>
           @endif 
      
      <div class="clearfix"></div>
 </div>
</div>
@stop