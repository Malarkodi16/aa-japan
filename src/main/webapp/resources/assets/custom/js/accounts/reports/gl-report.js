$(function() {
    function regex_escape(text) {
    	return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    	};
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });
    // Date range picker
    var createdDate_min;
    var createdDate_max;
    $('#table-filter-date').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        createdDate_min = picker.startDate.format('DD-MM-YYYY');
        createdDate_max = picker.endDate.format('DD-MM-YYYY');
        picker.element.val(createdDate_min + ' - ' + createdDate_max);
        $('#date-form-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))

    });
    $('#date-form-group').on('click', '.clear-date', function() {
        createdDate_min = '';
        createdDate_max = '';
        $('#table-filter-date').val('');
        $(this).remove();
        table.ajax.reload();

    })
    $('#btn-search').on('click', function() {
        table.ajax.reload();
    })
    $('#btn-export-excel').on('click', function() {
        let fromDate = createdDate_min;
        let toDate = createdDate_max;
        let date = $('#table-filter-date').val();
        if (!isEmpty(date)) {
            let parms = '';
            if (!isEmpty(fromDate)) {
                parms = 'fromDate=' + fromDate + '&';
            }
            if (!isEmpty(toDate)) {
                parms += 'toDate=' + toDate + '&';
            }
            $.redirect(myContextPath + '/accounts/report/gl/report?' + parms, '', 'GET');
        } else {
            alert('Please select date.!')
        }
    })
//     var exportoptions = {
//         filename: function() {
//             var d = new Date();
//             return 'GLReport_' + createdDate_max;
//         },
//         attr: {
//             type: "button",
//             id: 'dt_excel_export'
//         },
//         title: ''
//     };
    // Customer Accounts Datatable
    var table = $('#table-report-gl').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ordering": false,
        "ajax": {
                url: myContextPath + "/accounts/report/glReportDataSource",
            data: function(data) {
                data.fromDate = createdDate_min;
                data.toDate = createdDate_max;

            }
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "className" : 'details-control',
            "data": "name",
            "render": function(data, type, row) {
                return '<div class="container-fluid"> <div class="row"> <div class="col-md-12"> <div class="details-container details-control pull-left"> <h5 class="font-bold"><i class="fa fa-plus-square-o" name="icon"></i><span class="ml-5">' + data + '</h5> </div> </div> </div> </div>';
            }

        }, {
            targets: 1,
            "className": "dt-right details-control",
            "data": "total",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }

        }],
        "drawCallback": function(settings, json) {
            $(settings.nTHead).hide();
            $('#table-report-gl').find('span.autonumber').autoNumeric('init')
        }
//         buttons: [$.extend(true, {}, exportoptions, {
//             extend: 'excelHtml5'
//         })]

    });
    table.on('click', 'td>.container-fluid>.row>.col-md-12>div.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
        } else {
            table.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = table.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    $(row.node()).removeClass('shown');
                    $(row.node()).find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                }

            })
            tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
            row.child(format(row.data())).show();

            tr.addClass('shown');
        }
    });

});

function format(rowData) {
    var element = $('#genaral-ledger-transaction>div.details-container').clone();
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.coa_details.length; i++) {
        var row = $(rowClone).clone();
        $(row).find('td.sNo').html(i + 1);
        $(row).find('td.code').html(ifNotValid(rowData.coa_details[i].code, ''));
        $(row).find('td.reportingCategory').html(ifNotValid(rowData.coa_details[i].reportingCategory, ''));
        $(row).find('td.account').html(ifNotValid(rowData.coa_details[i].account, ''));
        $(row).find('td.subAccount').html(ifNotValid(rowData.coa_details[i].subAccount, ''));
        setAutonumericValue($(row).find('td.balance>span'), ifNotValid(rowData.coa_details[i].balance, 0))
        $(row).removeClass('clone-row hide');
        $(element).find('table>tbody').append(row);

    }

    return element;
}
