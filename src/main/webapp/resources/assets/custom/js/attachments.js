addFile.nextAttachmentId = $('#attachment-size').val();

function addInputFiles(inputEl) {
	var addAttachment = $(inputEl).closest('.attachments_form').find('.add_attachment');
	var clearedFileInput = $(inputEl).clone().val('');
	uploadAndAttachFiles(inputEl.files, inputEl);
	$(inputEl).remove();

	clearedFileInput.prependTo(addAttachment);
	
}

function uploadAndAttachFiles(files, inputEl) {
	$.each(files, function() {
		addFile(inputEl, this, true);
	});
}

function addFile(inputEl, file, eagerUpload) {
	var attachmentsFields = $(inputEl).closest('.attachments_form').find('.attachments_fields');

	var attachmentId = addFile.nextAttachmentId++;
	var fileSpan = $('<span>', {
		id : 'attachments_' + attachmentId,
		'class' : 'row'
	});
	var needDescription = $(inputEl).attr('data-description');

	fileSpan.append($('<span>', {
		'class' : 'col-md-12'
	}).append($('<input>', {
		type : 'hidden',
		'class' : 'filename',
		name : 'stock.attachments[' + attachmentId + '].filename'
	}), $('<input>', {
		type : 'hidden',
		'class' : 'diskFilename',
		name : 'stock.attachments[' + attachmentId + '].diskFilename'
	}), $('<input>', {
		type : 'hidden',
		'class' : 'subDirectory',
		name : 'stock.attachments[' + attachmentId + '].subDirectory'
	}), $('<input>', {
		type : 'hidden',
		'class' : 'fileSize',
		name : 'stock.attachments[' + attachmentId + '].fileSize'
	}),

	$('<i>', {
		'class' : 'fa fa-fw fa-file-photo-o',
		style : 'margin-right: 10px;'
	}), $('<span>', {
		style : 'margin-right: 10px;'
	}).text(file.name + '(' + Math.floor((file.size / 1024)) + ') KB'), $('<div>', {
		'class' : "form-group",
		style : 'display: inline-table;margin-right: 10px;'
	}).append($('<input>', {
		type : 'text',
		style : 'width: 260px;',
		'class' : 'form-control attachments-description',
		'data-validation' : 'length',
		'data-validation-length' : '1-10',
		name : 'stock.attachments[' + attachmentId + '].description',
		placeholder : $(inputEl).attr('data-description-placeholder')
	})).toggle(needDescription), $('<span>', {
		'class' : 'file_upload_status',
		style : 'margin-right: 10px;'
	}).append($('<i>', {
		'class' : 'fa fa-spinner fa-spin',
		style : 'margin-right: 10px;'
	})), $('<span>', {
		'class' : 'file-remove',
		'data-upload-directory' : $('#_stockNo').val().length > 0 ? 'upload' : 'temp',
		'data-id' : ''

	}).append($('<i>', {
		'class' : 'fa fa-fw fa-trash-o',
		style : 'margin-right: 10px;'
	})))

	).appendTo(attachmentsFields);
	var subdirectory = $(inputEl).data("subdirectory");
	$(fileSpan).find('.attachments-description').rules("add", {
		required : true
	})
	uploadSingleFile(file, fileSpan, subdirectory)
	return attachmentId;

}

function uploadSingleFile(file, fileSpan, subdirectory) {
	var formData = new FormData();
	formData.append("file", file);
	formData.append("directory", $('#attaachment_directory').val());
	formData.append("subDirectory", subdirectory);
	formData.append("uploadDirectory", $('#_stockNo').val());
	var status_icon = $(fileSpan).find('.file_upload_status').find('i');
	$.ajax({
		beforeSend : function() {
			$('input:submit', $(fileSpan).parents('form')).attr('disabled', 'disabled');
		},complete : function() {
			 $('input:submit', $(fileSpan).parents('form')).removeAttr('disabled');
		},type:"post",
	    data:formData,
	   
	    url:myContextPath + "/uploadFile",
	    async: true,
	    processData: false,
	    contentType: false,
        cache: false,
	    success: function(data){
	    	var response = data;
				$(fileSpan).find('.filename').val(response.fileName);
				$(fileSpan).find('.diskFilename').val(response.diskFileName);
				$(fileSpan).find('.subDirectory').val(subdirectory);
				$(fileSpan).find('.fileSize').val(response.size);
				$(fileSpan).find('a[name="downloadUrl"]').attr("href", response.fileDownloadUri);
				$(fileSpan).find('.file-remove').attr("data-filename", response.diskFileName);
				$(fileSpan).find('.file-remove').attr("data-directory", response.directory);
				$(fileSpan).find('.file-remove').attr("data-subdirectory", subdirectory);
				$(status_icon).removeClass('fa-spinner fa-spin').css('color', 'green').addClass('fa-check-circle-o');


	      },error:function(){
	    	  $(status_icon).removeClass('fa-spinner fa-spin').css('color', 'red').addClass('fa-times-circle');
	      }
	});
}

function deleteSingleFile(element) {
			var formData = new FormData();
		formData.append("filename", $(element).attr('data-filename'));
		formData.append("directory", $(element).attr('data-directory'));
		formData.append("subdirectory", $(element).attr('data-subdirectory'));
		formData.append("from", $(element).attr('data-upload-directory'));
		formData.append("id", $(element).attr('data-id'));
		var xhr = new XMLHttpRequest();
		xhr.open("DELETE", myContextPath+"/deleteFile");

		xhr.onload = function() {
			if (xhr.status == 200) {
				$(element).closest('.row').remove();
			}
		}

		xhr.send(formData);
	
	
	
	
}