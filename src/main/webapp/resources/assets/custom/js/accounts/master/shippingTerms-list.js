$(function() {

	 $('#table-filter-search').keyup(function() {
		 table_shippingTerms_list.search($(this).val()).draw();
	    });
	    $('#table-filter-length').change(function() {
	    	table_shippingTerms_list.page.len($(this).val()).draw();
	    });

    var table_shippingTerms_list_Ele = $('#table-master-shippingTerms-list');
    var table_shippingTerms_list = table_shippingTerms_list_Ele.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/accounts/master/shippingTerms/list-data",
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "name": "termsId",
            "data": "termsId",
        }, {
            targets: 1,
            "name": "name",
            "data": "name",
            
        },{
            targets: 2,
            "name": "shippingTerms",
            "data": "shippingTerms",
            
        },
       
        {
            targets: 3,
            orderable: false,
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                var html = '';
                if (type === 'display') {
                html += '<a class="ml-5 btn btn-info btn-xs" title="Edit Shipping Terms" id="edit-shippingTerms" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-add-shippingTerms" name="editshippingTerms" value="edit"><i class="fa fa-fw fa-edit"></i></a>';
                html += '<a href="#" class="ml-5 btn btn-warning btn-xs deleteButton"><i class="fa fa-fw fa-trash-o"></i></a>'
                return '<div class="form-inline" style="width:100px;">' + html + '</div>';
            }
        return data;
    }
}]
});

    
   
   

    let modal_add_shippingTerms = $('#modal-add-shippingTerms');
    let row, modalTriggerEle;
    modal_add_shippingTerms.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalTriggerEle = $(event.relatedTarget);
        row = table_shippingTerms_list.row($(modalTriggerEle).closest('tr'))
        var rowData = row.data();
        $('input[name="termsId"]').val(!isEmpty(rowData) ? rowData.termsId : "");
        $('input[name="name"]').val(!isEmpty(rowData) ? rowData.name : "");
        $('textarea[name="shippingTerms"]').val(!isEmpty(rowData) ? rowData.shippingTerms : "");
       
    }).on('hidden.bs.modal', function() {
        $(this).find('input').val('');
        $(this).find('.help-block').text('')
    }).on('click', '#save-shippingTerms', function() {
    	
    	let isValid=$('#shippingTerms-form').find('input[name="name"], textarea[name="shippingTerms"]').valid()
        if (!isValid) {
            return false;
        }
    	
        var data = getFormData($("#shippingTerms-form").find('input,textarea'));
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: 'post',
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/master/shippingTerms/save",
            contentType: "application/json",

            success: function(data) {
                if (data.status === 'success') {
                    if (!isEmpty(row.data())) {
                        row.data(data.data).invalidate();
                    } else{
                    	table_shippingTerms_list.ajax.reload();
                    }
                    $('#modal-add-shippingTerms').modal('toggle');
                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong>Shipping Term Saved.');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                }
            }
        });
    })

    $('#table-master-shippingTerms-list').on('click', '.deleteButton', function(event) {
        if (!confirm('Do you want to delete?')) {
            return false;
        }
        let tr = $(this).closest('tr');
        var data = table_shippingTerms_list.row(tr).data();
        var termsId = data.termsId;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            url: myContextPath + "/accounts/master/shippingTerms/delete/" + termsId,
            contentType: "application/json",
            success: function(data) {
            	table_shippingTerms_list.ajax.reload();
            },
            error: function(status) {
                var alertEle = $('#alert-block').addClass('alert-danger');
                $(alertEle).removeClass('alert-success')
                $(alertEle).css('display', '').html('<strong>Warning!</strong> Shipping Term is not deleted').fadeIn().delay(3000).fadeOut();
            }
        })
    })
})
