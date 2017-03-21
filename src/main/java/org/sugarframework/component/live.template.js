var i = setInterval(function() {
	$.ajax({
		type : 'GET',
		url : document.URL + '?field=%s',
		success : function(response) {
			$('#%s').replaceWith(response);
			%s
		}
	});
}, 5000);