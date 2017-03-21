
function layout(){
	layoutRows();
	layoutSugarComponents();
}

function layoutSugarComponent(component){
	
	component.prepend("<span class='handle glyphicon glyphicon-move orange'><small>"+component.attr('id')+"</small></span>");
	component.css('border','1px dashed orange');
	component.css('margin','0 1em 1em 0');
	component.css('min-height','40px');
	component.parent().sortable({
		connectWith: ".sugar-container",
        tolerance: 'pointer',
        opacity: 0.5,
        revert: 'invalid',
        handle: ".handle",
        forceHelperSize: true ,
        start: function(event, ui) {
            ui.item.startPos = ui.item.index();
        },
        stop: function(event, ui) {
        	
        	var arr = [];
        	var c = 0;
        	$(this).children().each(function() {
        		 var inner = new Object();
        		 
        		 console.log( $(this).attr('id') );
        		 inner.id = $(this).attr('id');
        		 inner.order = c++;
        		 
        		 arr.push(inner);
        	 });
        	
        	 console.log(arr);
        	 
        	 $.ajax({
					url : 'editorder',
					type : 'post',
					data : {
						components : JSON.stringify(arr)
					},
	                success: function(data) {
	                	
	                },
	                error: function(data) {
	                	
	                }
	            });
        }
	});		
	
}


function layoutSugarComponents(){

	$('.sugar-component').each(function() {
		
		layoutSugarComponent($(this));
		
		
		
	});
}

function layoutRows(){

				$('.row').each(function() {
					
					$(this).css('border','1px dashed grey');
					$(this).css('margin','0 1em 1em 0');
					$(this).css('min-height','40px');
					
					 $( this ).children().each(function () {
					    $( this ).prepend("<span class='handle glyphicon glyphicon-move green'><small>"+$(this).attr('id')+"</small></span>");
					    $(this).css('border','1px dashed green');
					    //$(this).css('margin','0 1em 1em 0');
					});
					
				});
			
				$('.row').sortable({
						connectWith: ".row",
				        tolerance: 'pointer',
				        opacity: 0.5,
				        revert: 'invalid',
				        handle: ".handle",
				        forceHelperSize: true ,
				        start: function(event, ui) {
				            ui.item.startPos = ui.item.index();
				        },
				        stop: function(event, ui) {
				        	
				        	var arr = [];
				        	var c = 0;
				        	
				        	console.log("rows:[");
				            
				            $('.row').each(function() {
				            	var inner = new Object();
				            	
				            	var i = 0;

				            	inner.name = $(this).attr('id');
				            	inner.columns = [];
				            	
				            	$(this).children('div').each(function() {
				            		var column = new Object();
					        		column.id = $(this).attr('id');
					        		column.class = $(this).attr('class');
				            		inner.columns[i++] = column;
					        	});	
				            	console.log('}');
				            	arr[c++] = inner;
				            });
				            console.log(arr);
				            
				            if (arr.length > 0){
				            
					            $.ajax({
									url : 'editlayout',
									type : 'post',
									data : {
										pageRows : JSON.stringify(arr)
									},
					                success: function(data) {
					                	
					                },
					                error: function(data) {
					                	
					                }
					            });
				            
				            }
				        }
				 });
				
			
			
}