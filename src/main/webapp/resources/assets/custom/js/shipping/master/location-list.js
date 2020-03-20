$(function() {
	// Customize Datatable

	$('#table-filter-search').keyup(function() {
		table_location_list.search($(this).val()).draw();
	});
	$('#table-filter-length').change(function() {
		table_location_list.page.len($(this).val()).draw();
	});
	
	/* Location Data-table*/

    var table_location_list_Ele = $('#table-master-location-list');
    var table_location_list = table_location_list_Ele.DataTable({
       "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
       "pageLength" : 25,
        "ajax": myContextPath + "/master/location/list-data",
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "name": "Type",
            "data": "type",
            "render": function(data, type, row) {
            	if(!isEmpty(row.type))
            	return row.type.charAt(0).toUpperCase() + row.type.slice(1);
            }
        }, {
            targets: 1,
            "name": "Company",
            "data": "displayName",
        }, {
            targets: 2,
            "name": "Address",
            "data": "address",
        }, {
            targets: 3,
            "name": "Phone",
            "data": "phone",
        }, {
            targets: 4,
            "name": "Fax",
            "data": "fax",
        }, {
            targets: 5,
            "name": "atsukai",
            "data": "atsukai",
        }, {
            targets: 6,
            "name": "tantousha",
            "data": "tantousha",
        }, {
            targets: 7,
            "name": "Fax",
            "data": "fax",
             "render": function(data, type, row) {
             	let shipmentType=ifNotValid(row.shipmentType,'');
             	if(shipmentType==1){
             		return 'RORO';
             	}else if(shipmentType==2){
             		return 'CONTAINER';
             	}
             	return '';
             }
        }, {
            targets: 8,
            "name": "shipmentOriginPort",
            "data": "shipmentOriginPort",
        }, {
            targets:9,
            "name": "Action",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                var html = '';
                if (type === 'display') {
                    html += '<a href="' + myContextPath + '/master/location/edit/' + row.code + '" class="ml-5 btn btn-info btn-xs" id="edit-location"><i class="fa fa-fw fa-edit"></i></a>'
                    html += '<a href="#" class="ml-5 btn btn-warning btn-xs deleteButton"><i class="fa fa-fw fa-trash-o"></i></a>'
                    return '<div class="form-inline" style="width:100px;">' + html + '</div>';
                }
                return data;
            }
        }]
    });
    $('#table-master-location-list').on('click', '.deleteButton ', function() {
        if (!confirm('Do you want to delete?')) {
            return false;
        }
        var data = table_location_list.row($(this).closest('tr')).data();
        var id = data.id;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            data: JSON.stringify(id),
            url: myContextPath + "/master/location/delete/" + id,
            contentType: "application/json",
            success: function(status) {
                table_location_list.ajax.reload();
            },
            error: function(status) {
                var alertEle = $('#alert-block').addClass('alert-danger');
                $(alertEle).removeClass('alert-success')
                $(alertEle).css('display', '').html('<strong>Warning!</strong> Transport fee is not deleted').fadeIn().delay(3000).fadeOut();
            }
        })
    })
})