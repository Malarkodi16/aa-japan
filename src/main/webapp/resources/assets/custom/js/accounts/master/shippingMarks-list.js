$(function() {

	 $('#table-filter-search').keyup(function() {
		 table_shippingMarks_list.search($(this).val()).draw();
	    });
	    $('#table-filter-length').change(function() {
	    	table_shippingMarks_list.page.len($(this).val()).draw();
	    });

    var table_shippingMarks_list_Ele = $('#table-master-shippingMarks-list');
    var table_shippingMarks_list = table_shippingMarks_list_Ele.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/accounts/master/shippingMarks/list-data",
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "name": "marksId",
            "data": "marksId",
        }, {
            targets: 1,
            "name": "name",
            "data": "name",
            
        },{
            targets: 2,
            "name": "shippingMarks",
            "data": "shippingMarks",
            
        },
       
        {
            targets: 3,
            orderable: false,
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                var html = '';
                if (type === 'display') {
                html += '<a class="ml-5 btn btn-info btn-xs" title="Edit Shipping Marks" id="edit-shippingMarks" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-add-shippingMarks" name="editshippingMarks" value="edit"><i class="fa fa-fw fa-edit"></i></a>';
                html += '<a href="#" class="ml-5 btn btn-warning btn-xs deleteButton"><i class="fa fa-fw fa-trash-o"></i></a>'
                return '<div class="form-inline" style="width:100px;">' + html + '</div>';
            }
        return data;
    }
}]
});

    
   
   

    let modal_add_shippingMarks = $('#modal-add-shippingMarks');
    let row, modalTriggerEle;
    modal_add_shippingMarks.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalTriggerEle = $(event.relatedTarget);
        row = table_shippingMarks_list.row($(modalTriggerEle).closest('tr'))
        var rowData = row.data();
        $('input[name="marksId"]').val(!isEmpty(rowData) ? rowData.marksId : "");
        $('input[name="name"]').val(!isEmpty(rowData) ? rowData.name : "");
        $('textarea[name="shippingMarks"]').val(!isEmpty(rowData) ? rowData.shippingMarks : "");
       
    }).on('hidden.bs.modal', function() {
        $(this).find('input').val('');
        $(this).find('.help-block').text('')
    }).on('click', '#save-shippingMarks', function() {
    	
    	let isValid=$('#shippingMarks-form').find('input[name="name"], input[textarea="shippingMarks"]').valid()
        if (!isValid) {
            return false;
        }
    	
        var data = getFormData($("#shippingMarks-form").find('input,textarea'));
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: 'post',
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/master/shippingMarks/save",
            contentType: "application/json",

            success: function(data) {
                if (data.status === 'success') {
                    if (!isEmpty(row.data())) {
                        row.data(data.data).invalidate();
                    } else{
                    	table_shippingMarks_list.ajax.reload();
                    }
                    $('#modal-add-shippingMarks').modal('toggle');
                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong>Shipping Marks Saved.');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                }
            }
        });
    })

    $('#table-master-shippingMarks-list').on('click', '.deleteButton', function(event) {
        if (!confirm('Do you want to delete?')) {
            return false;
        }
        let tr = $(this).closest('tr');
        var data = table_shippingMarks_list.row(tr).data();
        var marksId = data.marksId;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            url: myContextPath + "/accounts/master/shippingMarks/delete/" + marksId,
            contentType: "application/json",
            success: function(data) {
            	table_shippingMarks_list.ajax.reload();
            },
            error: function(status) {
                var alertEle = $('#alert-block').addClass('alert-danger');
                $(alertEle).removeClass('alert-success')
                $(alertEle).css('display', '').html('<strong>Warning!</strong> Shipping Marks is not deleted').fadeIn().delay(3000).fadeOut();
            }
        })
    })
})
