$(function() {

	 $('#table-filter-search').keyup(function() {
		 table_shippingCompany_list.search($(this).val()).draw();
	    });
	    $('#table-filter-length').change(function() {
	    	table_shippingCompany_list.page.len($(this).val()).draw();
	    });

    var table_shippingCompany_list_Ele = $('#table-master-shippingCompany-list');
    var table_shippingCompany_list = table_shippingCompany_list_Ele.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/master/shippingCompany/list-data",
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "name": "shippingCompany",
            "data": "name",
        }, {
            targets: 1,
            "name": "address",
            "data": "shipCompAddr",
            
        },{
            targets: 2,
            "name": "email",
            "data": "shipCompMail",
            
        },
        {
            targets: 3,
            "name": "mobileNumber",
            "data": "mobileNo",
            
        },
        {
            targets: 4,
            orderable: false,
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                var html = '';
                if (type === 'display') {
                html += '<a class="ml-5 btn btn-info btn-xs" title="Edit Shipping Company" id="edit-shippingCompany" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-add-shippingCompany" name="editshippingCompany" value="edit"><i class="fa fa-fw fa-edit"></i></a>';
                html += '<a href="#" class="ml-5 btn btn-warning btn-xs deleteButton"><i class="fa fa-fw fa-trash-o"></i></a>'
                return '<div class="form-inline" style="width:100px;">' + html + '</div>';
            }
        return data;
    }
}]
});

    
   
   

    let modal_add_shippingCompany = $('#modal-add-shippingCompany');
    let row, modalTriggerEle;
    modal_add_shippingCompany.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalTriggerEle = $(event.relatedTarget);
        row = table_shippingCompany_list.row($(modalTriggerEle).closest('tr'))
        var rowData = row.data();
        $('input[name="shippingCompanyNo"]').val(!isEmpty(rowData) ? rowData.shippingCompanyNo : "");
        $('input[name="name"]').val(!isEmpty(rowData) ? rowData.name : "");
        $('input[name="shipCompAddr"]').val(!isEmpty(rowData) ? rowData.shipCompAddr : "");
        $('input[name="shipCompMail"]').val(!isEmpty(rowData) ? rowData.shipCompMail : "");
        $('input[name="mobileNo"]').val(!isEmpty(rowData) ? rowData.mobileNo : "");
    }).on('hidden.bs.modal', function() {
        $(this).find('input').val('');
        $(this).find('.help-block').text('')
    }).on('click', '#add-shipComp', function() {
    	let isValid=$('#shippingCompany-form').find('input[name="name"]').valid()
        if (!isValid) {
            return false;
        }
    	
        var data = getFormData($("#shippingCompany-form").find('input'));
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: 'post',
            data: JSON.stringify(data),
            url: myContextPath + "/master/shippingCompany/save",
            contentType: "application/json",

            success: function(data) {
                if (data.status === 'success') {
                    if (!isEmpty(row.data())) {
                        row.data(data.data).invalidate();
                    } else{
                    table_shippingCompany_list.ajax.reload();
                    }
                    $('#modal-add-shippingCompany').modal('toggle');
                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong>Shipping Company Saved.');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                }
            }
        });
    })

    $('#table-master-shippingCompany-list').on('click', '.deleteButton', function(event) {
        if (!confirm('Do you want to delete?')) {
            return false;
        }
        let tr = $(this).closest('tr');
        var data = table_shippingCompany_list.row(tr).data();
        var shippingCompanyNo = data.shippingCompanyNo;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            url: myContextPath + "/master/shippingCompany/delete/" + shippingCompanyNo,
            contentType: "application/json",
            success: function(data) {
            	table_shippingCompany_list.ajax.reload();
            },
            error: function(status) {
                var alertEle = $('#alert-block').addClass('alert-danger');
                $(alertEle).removeClass('alert-success')
                $(alertEle).css('display', '').html('<strong>Warning!</strong> Shipping Company is not deleted').fadeIn().delay(3000).fadeOut();
            }
        })
    })
})
