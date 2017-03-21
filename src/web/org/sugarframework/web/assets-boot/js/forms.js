$("form").submit(function(e) {
			var form = $(this);
			var geturl;
			var originalText = form.find('button').text();

			geturl = $.ajax({
				url : form.attr('action'),
				type : form.attr('method'),
				data : form.serialize(),
				success : function(response) {

					if (geturl.status == 278) {
						window.location.href = geturl.responseText;
					}else if(geturl.status == 279){
						error(geturl.responseText);
						form.find('button').text(originalText);
						form.find('fieldset').removeAttr("disabled");
						
					} else {
						if (geturl.responseText) {
							success(geturl.responseText);
						}
					}
					form.find('button').text(originalText);
					form.find('fieldset').removeAttr("disabled");

				},
				error : function(xhr, ajaxOptions, thrownError) {
					error(thrownError);
					form.find('button').text(originalText);
					form.find('fieldset').removeAttr("disabled");
				}

			});

			form.find('button').text("Executing...");
			form.find('fieldset').attr("disabled", "disabled");

			return false;
		});