$(function() {

	 $('#table-filter-search').keyup(function() {
		 table_ship_list.search($(this).val()).draw();
	    });
	    $('#table-filter-length').change(function() {
	    	table_ship_list.page.len($(this).val()).draw();
	    });

    var table_ship_list_Ele = $('#table-master-ship-list');
    var table_ship_list = table_ship_list_Ele.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/master/ship/list-data",
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "name": "shipId",
            "data": "shipId",
        }, {
            targets: 1,
            "name": "name",
            "data": "name",
            
        },
        {
            targets: 2,
            orderable: false,
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                var html = '';
                if (type === 'display') {
                html += '<a class="ml-5 btn btn-info btn-xs" title="Edit Ship" id="edit-ship" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-add-ship" name="editship" value="edit"><i class="fa fa-fw fa-edit"></i></a>';
                html += '<a href="#" class="ml-5 btn btn-warning btn-xs deleteButton"><i class="fa fa-fw fa-trash-o"></i></a>'
                return '<div class="form-inline" style="width:100px;">' + html + '</div>';
            }
        return data;
    }
}]
});

    
   
   

    let modal_add_ship = $('#modal-add-ship');
    let row, modalTriggerEle;
    modal_add_ship.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalTriggerEle = $(event.relatedTarget);
        row = table_ship_list.row($(modalTriggerEle).closest('tr'))
        var rowData = row.data();
        $('input[name="shipId"]').val(!isEmpty(rowData) ? rowData.shipId : "");
        $('input[name="name"]').val(!isEmpty(rowData) ? rowData.name : "");
  
    }).on('hidden.bs.modal', function() {
        $(this).find('input').val('');
        $(this).find('.help-block').text('')
    }).on('click', '#add-ship', function() {
    //	let isValid=$('#ship-form').find('input[name="name"]').valid()
        if (!$('#ship-form').valid()) {
            return false;
        }
    	
        var data = getFormData($("#ship-form").find('input'));
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: 'post',
            data: JSON.stringify(data),
            url: myContextPath + "/master/ship/save",
            contentType: "application/json",

            success: function(data) {
                if (data.status === 'success') {
                    if (!isEmpty(row.data())) {
                        row.data(data.data).invalidate();
                    } else{
                    table_ship_list.ajax.reload();
                    }
                    $('#modal-add-ship').modal('toggle');
                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong>Ship Name Saved.');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                }
            }
        });
    })

    $('#table-master-ship-list').on('click', '.deleteButton', function(event) {
        if (!confirm('Do you want to delete?')) {
            return false;
        }
        let tr = $(this).closest('tr');
        var data = table_ship_list.row(tr).data();
        var shipId = data.shipId;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            url: myContextPath + "/master/ship/delete/" + shipId,
            contentType: "application/json",
            success: function(data) {
            	table_ship_list.ajax.reload();
            },
            error: function(status) {
                var alertEle = $('#alert-block').addClass('alert-danger');
                $(alertEle).removeClass('alert-success')
                $(alertEle).css('display', '').html('<strong>Warning!</strong> Ship is not deleted').fadeIn().delay(3000).fadeOut();
            }
        })
    })
})
