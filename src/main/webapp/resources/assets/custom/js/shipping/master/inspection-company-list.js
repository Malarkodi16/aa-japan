$(function() {
    // Datatable Filter

    $('#table-filter-search').keyup(function() {
        table_inspection_company_list.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
        table_inspection_company_list.page.len($(this).val()).draw();
    });

    /* Port List Data-table */
    var table_inspection_company_list_Ele = $('#table-master-inspection-company-list');
    var table_inspection_company_list = table_inspection_company_list_Ele.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/master/inspection/list-data",
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            orderable: false,
            className: 'details-control',
            "data": "name",
            "render": function(data, type, row) {
                return '<div class="container-fluid"><div class="row"><div class="col-md-12" style="width:600px"><h5 class="font-bold"><i class="fa fa-plus-square-o pull-left" name="icon"></i> ' + data + '</h5></div></div></div>';
            }
        }, {
            targets: 1,
            "className": "details-control",
            "data": "code",
            "visible": false
        }, {
            targets: 2,
            "className": 'align-center',
            "render": function(data, type, row) {
                var html = '';
                if (type === 'display') {
                    html += '<a href="' + myContextPath + '/master/inspection/company/edit/' + row.code + '" class="ml-5 btn btn-info btn-xs"><i class="fa fa-fw fa-edit"></i></a>'
                    html += '<a href="#" class="ml-5 btn btn-warning btn-xs delete-inspection btn-delete"><i class="fa fa-fw fa-trash-o"></i></a>'
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
    table_inspection_company_list.on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table_inspection_company_list.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
        } else {
            table_inspection_company_list.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = table_inspection_company_list.row(rowIdx);
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
        var element = $('#inspection-company-list-details-view>.inspection-company-detail-view').clone();

        element.find('input[name="code"]').val(rowData.code)
        var tbody = '';
        var rowClone = $(element).find('table>tbody').find('tr.clone-row');
        if (rowData.locations.length == 0) {
            $(element).addClass('hide');
        } else {

            for (var i = 0; i < rowData.locations.length; i++) {
                var row = $(rowClone).clone();
                row.attr('data-json', JSON.stringify(rowData.locations[i]));
                let status = ""
                  , className = "";
                if (ifNotValid(rowData.locations[i].status, '') == 0) {
                    $(row).find('td.status>span.label').addClass('label-default');
                    $(row).find('td.status>span.label').html('Initiated');
                }
                $(row).find('td.s-no>span').html(i + 1);
                $(row).find('td.s-no>input').val(ifNotValid(rowData.locations[i].locationId, ''))

                $(row).find('td.locationName').html(ifNotValid(rowData.locations[i].locationName, ''));
                $(row).find('td.zip').html(ifNotValid(rowData.locations[i].zip, ''));
                $(row).find('td.tel').html(ifNotValid(rowData.locations[i].tel, ''));
                $(row).find('td.fax').html(ifNotValid(rowData.locations[i].fax, ''));
                $(row).find('td.contactPerson').html(ifNotValid(rowData.locations[i].contactPerson, ''));

                $(row).find('td.address').html(ifNotValid(rowData.locations[i].address, ''));

                $(row).removeClass('hide');
                $(element).find('table>tbody').append(row);

            }
        }

        return element;
    }

    $('#table-master-inspection-company-list').on('click', '.delete-inspection', function() {
        if (!confirm($.i18n.prop('common.confirm.delete'))) {
            return false;
        }
        var data = table_inspection_company_list.row($(this).closest('tr')).data();
        var code = data.code;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            data: JSON.stringify(code),
            url: myContextPath + "/master/inspection/delete/" + code,
            contentType: "application/json",
            success: function(status) {
                table_inspection_company_list.ajax.reload();
            },
            error: function(status) {
                var alertEle = $('#alert-block').addClass('alert-danger');
                $(alertEle).removeClass('alert-success')
                $(alertEle).css('display', '').html('<strong>Warning!</strong> Inspection Company is not deleted').fadeIn().delay(3000).fadeOut();
            }
        })
        //' + myContextPath + '/a/supplier/delete/' + row.supplierCode + '
    })

    $('#table-master-inspection-company-list').on('click', '#delete-location', function() {
        if (!confirm($.i18n.prop('common.confirm.delete'))) {
            return false;
        }
        var locationId = $(this).closest('tr').find('input[name="locationId"]').val();
        var code = $('#table-master-inspection-company-list').find('input[name="code"]').val();

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            //data: JSON.stringify(itemId),
            url: myContextPath + "/master/inspection/location/delete?locationId=" + locationId + "&code=" + code,
            contentType: "application/json",
            success: function(data) {
                console.log("deleted successfully");
                table_inspection_company_list.ajax.reload();
            }
        });
    });
})
