
function validatePageInputs(){
	// validate parameters if filled in
	$("input").each(function() {
		if (this.value != "") {
			this.focus();
			this.blur();
		}
	});
			
	window.scrollTo(0,0);
	
	/*
	 * TODO
	 * This code never works, because has-error is injected asynchonously
	 */
	/*
	if ($( ".has-error" ).length ) {
		$( ".has-error" ).first().focus();
	}else{
		window.scrollTo(0,0);
	}
	*/
	
}

function validateParameter(mName, paramName, component, id, dValue,
				buttonId) {

			var form = component.parent().parent().parent();

			var val = component.val();

			geturl = $.ajax({
				url : 'validate',
				type : 'post',
				data : {
					methodName : mName,
					parameterName : paramName,
					value : val
				},
				success : function(response) {
					
					// validation error
					if (geturl.status == 277) {
						$('#' + id).find('.help-block').remove();
						$('#' + id).find('.groupy')
								.append(
										"<div class='help-block'>" + geturl.responseText
												+ "</div>");
						$('#' + id).removeClass('has-success')
								.addClass('has-error');

						form.find('button').attr("disabled", "disabled");
					}else{

						$('#' + id).find('.help-block').remove();
						$('#' + id).removeClass('has-error');
	
						var found = false;
	
						form.find(".has-error").each(function() {
							found = true;
						});
	
						if (found == false) {
							form.find('button').removeAttr("disabled");
						}
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {

					$('#' + id).find('.help-block').remove();
					$('#' + id).find('.groupy')
							.append(
									"<div class='help-block'>" + thrownError
											+ "</div>");
					$('#' + id).removeClass('has-success')
							.addClass('has-error');

					form.find('button').attr("disabled", "disabled");

				}

			});

		}