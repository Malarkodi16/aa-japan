// Customize Datatable
$('#table-filter-search').keyup(function() {
    table_supplier_list.search($(this).val()).draw();
});
$('#table-filter-length').change(function() {
    table_supplier_list.page.len($(this).val()).draw();
});

/* Supplier List Data-table*/

//table shipping requested
var table_supplier_list_Ele = $('#table-master-supplier-list');
var table_supplier_list = table_supplier_list_Ele.DataTable({
    "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
    "pageLength" : 25,
    "ajax": myContextPath + "/a/supplier/list/datasource",
    columnDefs: [{
        "targets": '_all',
        "defaultContent": ""
    }, {
        targets: 0,
        orderable: false,
        className: 'details-control',
        "data": "vessel",
        "render": function(data, type, row) {
            return '<div class="container-fluid"><div class="row"><div class="col-md-12" style="width:600px"><h5 class="font-bold"><i class="fa fa-plus-square-o pull-left" name="icon"></i> ' + row.company + ' - ' + row.type + ' [' + row.supplierCode + ']</h5></div></div></div>';
        }
    }, {
        targets: 1,
        "className": "details-control",
        "data": "supplierCode",
        "visible": false
    }, {
        targets: 2,
        "data": "invoiceNo",
        "className": 'align-center',
        "render": function(data, type, row) {
            data = data == null ? '' : data;
            var html = '';
            if (type === 'display') {
                html += '<a href="' + myContextPath + '/a/supplier/edit/' + row.supplierCode + '" class="ml-5 btn btn-info btn-xs"><i class="fa fa-fw fa-edit"></i></a>'
                html += '<a href="#" class="ml-5 btn btn-warning btn-xs delete-supplier btn-delete"><i class="fa fa-fw fa-trash-o"></i></a>'
                return '<div class="form-inline" style="width:100px;">' + html + '</div>';
            }
            return data;
        }
    }],

    "fnDrawCallback": function(oSettings) {
        $(oSettings.nTHead).hide();
    }

});

//expand details
table_supplier_list.on('click', 'td.details-control', function() {
    var tr = $(this).closest('tr');
    var row = table_supplier_list.row(tr);
    if (row.child.isShown()) {
        row.child.hide();
        tr.removeClass('shown');
        tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
    } else {
        table_supplier_list.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
            var row = table_supplier_list.row(rowIdx);
            if (row.child.isShown()) {
                row.child.hide();
                tr.removeClass('shown');
            }

        })
        row.child(format(row.data())).show();
        tr.addClass('shown');
        tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
    }
});

// supplier list expand details format
function format(rowData) {
    var element = $('#supplier-list-details-view>.supplier-detail-view').clone();
    if (rowData.type == 'supplier') {
        $(element).find('th.auction-house,td.auction-house').addClass('hidden')
    }
    element.find('input[name="supplierCode"]').val(rowData.supplierCode)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    if (rowData.supplierLocations.length == 0 && rowData.type == "supplier") {
        $(element).addClass('hide');
    } else {

        for (var i = 0; i < rowData.supplierLocations.length; i++) {
            var row = $(rowClone).clone();
            row.attr('data-json', JSON.stringify(rowData.supplierLocations[i]));
            let status = ""
              , className = "";
            if (ifNotValid(rowData.supplierLocations[i].status, '') == 0) {
                $(row).find('td.status>span.label').addClass('label-default');
                $(row).find('td.status>span.label').html('Initiated');
            }
            $(row).find('td.s-no>span').html(i + 1);
            $(row).find('td.s-no>input').val(ifNotValid(rowData.supplierLocations[i].id, ''))

            $(row).find('td.auctionHouse').html(ifNotValid(rowData.supplierLocations[i].auctionHouse, ''));
            $(row).find('td.address').html(ifNotValid(rowData.supplierLocations[i].address, ''));
            $(row).find('td.phone').html(ifNotValid(rowData.supplierLocations[i].phone, ''));
            $(row).find('td.email').html(ifNotValid(rowData.supplierLocations[i].email, ''));
            $(row).find('td.fax').html(ifNotValid(rowData.supplierLocations[i].fax, ''));
            var formattedPosNos;
            if (rowData.supplierLocations[i].posNos != null) {
                formattedPosNos = rowData.supplierLocations[i].posNos.toString();
            }
            $(row).find('td.posNos').html(ifNotValid(formattedPosNos, ''));

            $(row).removeClass('hide');
            $(element).find('table>tbody').append(row);

        }
    }

    return element;
}

$('#table-master-supplier-list').on('click', '.delete-supplier', function() {
    if (!confirm($.i18n.prop('common.confirm.delete'))) {
        return false;
    }
    var data = table_supplier_list.row($(this).closest('tr')).data();
    var id = data.supplierCode;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "get",
        data: JSON.stringify(id),
        url: myContextPath + "/a/supplier/delete/" + id,
        contentType: "application/json",
        success: function(status) {
            location.reload();
        },
        error: function(status) {
            var alertEle = $('#alert-block').addClass('alert-danger');
            $(alertEle).removeClass('alert-success')
            $(alertEle).css('display', '').html('<strong>Warning!</strong> Transport fee is not deleted').fadeIn().delay(3000).fadeOut();
        }
    })
    //' + myContextPath + '/a/supplier/delete/' + row.supplierCode + '
})

$('#table-master-supplier-list').on('click', '#delete-supplierLocation', function() {
    if (!confirm($.i18n.prop('common.confirm.delete'))) {
        return false;
    }
    var itemId = $(this).closest('tr').find('input[name="itemId"]').val();
    var supplierCode = $('#table-master-supplier-list').find('input[name="supplierCode"]').val();

    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "get",
        //data: JSON.stringify(itemId),
        url: myContextPath + "/a/supplier/location/delete?itemId=" + itemId + "&supplierCode=" + supplierCode,
        contentType: "application/json",
        success: function(data) {
            console.log("deleted successfully");
            table_supplier_list.ajax.reload();
        }
    });
});
