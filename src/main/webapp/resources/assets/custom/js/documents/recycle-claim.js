$(function() {
    setDocumentDashboardStatus();
    $('input.autonumber').autoNumeric('init')
    $('input[type="radio"][name=radioReceivedFilter].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        let val = $(this).val();
        if (val == 0) {
            $('form#form-received-upload').removeClass('hidden');
        }else{
            $('form#form-received-upload').addClass('hidden');
        }
        
        table_recycle.ajax.reload();
    })

    /* Date picke function change */

    var recycle_min;
    var recycle_max;
    $('#table-filter-recycle-claim').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        recycle_min = picker.startDate;
        recycle_max = picker.endDate;
        picker.element.val(recycle_min.format('DD-MM-YYYY') + ' - ' + recycle_max.format('DD-MM-YYYY'));
        recycle_min = recycle_min._d.getTime();
        recycle_max = recycle_max._d.getTime();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table_recycle.draw();
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        recycle_min = '';
        recycle_max = '';
        table_recycle.draw();
        $('#table-filter-recycle-claim').val('');
        $(this).remove();

    });

    //recycle datatable
    var table_recycle_ele = $('#table-claim-recycle');
    var table_recycle = $(table_recycle_ele).DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ajax": {
            url: myContextPath + "/documents/recycle/claim/list/datasource",
            data: function(data) {
                var status = $('input[name="radioReceivedFilter"]:checked').val();
                data["status"] = status;
            }
        },
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets:0,
            "className": "details-control",
            "width": "50px",
            "data": "purchaseDate"
        }, {
            targets: 1,
            "className": "details-control",
            "width": "100px",
            "data": "chassisNo"
        }, {
            targets:2,
            "className": "dt-right details-control",
            "width": "100px",
            "render": function(data, type, row) {
                var html = ''
                html += '<span class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + ifNotValid(row.recycle, 0) + '</span>'
                return html;
            }
        },{
            targets: 3,
            "className": "dt-right details-control",
            "width": "100px",
            "render": function(data, type, row) {
                var html = ''
                html += '<span class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + ifNotValid(row.recycleClaimCharge, '') + '</span>'
                return html;
            }
        },{
            targets: 4,
            "className": "dt-right details-control",
            "width": "100px",
            "render": function(data, type, row) {
                var html = ''
                html += '<span class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + ifNotValid(row.recycleClaimInterest, '') + '</span>'
                return html;
            }
        }, {
            targets: 5,
            "className": "dt-right details-control",
            "width": "100px",
            "render": function(data, type, row) {
                var html = ''
                html += '<span class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + ifNotValid(row.recycleClaimReceivedAmount, '') + '</span>'
                return html;
            }
        }, {
            targets: 5,
            "className": "dt-right details-control",
            "width": "100px",
            "render": function(data, type, row) {
                var html = ''
                html += '<span class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + ifNotValid(row.recycleClaimReceivedAmount, '') + '</span>'
                return html;
            }
        }, {
            targets:6,
            "className": "details-control",
            "width": "50px",
            "data": "recycleClaimAppliedDate"
        }, {
            targets: 7,
            "className": "details-control",
            "width": "50px",
            "data": "recycleClaimReceivedDate"
        },{
            targets: 8,
            'visible': false,
            "data": "recycleClaimStatus",
        }],
        "drawCallback": function(settings, json) {
            $('span.autonumber').autoNumeric('init')

        }

    });
    function regex_escape(text) {
    	return text.replace(/,/g, "").replace(/\./gi, "").replace(/¥/g, "");
    	};
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table_recycle.search(query, true, false).draw();
    });

     $('#table-recycle-claim-filter-length').change(function() {
        table_recycle.page.len($(this).val()).draw();
    });

    //icheck select and deselect
    table_recycle.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            table_recycle.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            table_recycle.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            table_recycle.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            table_recycle.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function() {
        if (table_recycle.rows({
            selected: true,
            page: 'current'
        }).count() !== table_recycle.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (table_recycle.rows({
            selected: true,
            page: 'current'
        }).count() !== table_recycle.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }

    }).on('click', 'button[name="btn-claim"]', function() {
        var row = table_recycle.row($(this).closest('tr'));
        var data = row.data();
        var response = claimRecycle(data.id);
        if (response.status === 'success') {
            if (!isEmpty(response.data)) {
                row.data(response.data).invalidate();
                table_recycle.draw();
            }
        }
    });

    // Receivable Received draw dataTable
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        if (oSettings.sTableId == 'table-claim-recycle') {
            if (typeof recycle_min != 'undefined' && recycle_min.length != '') {
                if (aData[0].length == 0) {
                    return false;
                }
                if (typeof aData._date == 'undefined') {
                    aData._date = moment(aData[0], 'DD-MM-YYYY')._d.getTime();
                }
                if (recycle_min && !isNaN(recycle_min)) {
                    if (aData._date < recycle_min) {
                        return false;
                    }
                }
                if (recycle_max && !isNaN(recycle_max)) {
                    if (aData._date > recycle_max) {
                        return false;
                    }
                }
            }
        }

        return true;
    });

});
function setDocumentDashboardStatus() {
    $.getJSON(myContextPath + "/data/documents-dashboard/recycle-data-count", function(data) {
        $('#recycle-claim-count').html(data.data.recycleClaim + "/" + data.data.recycleReceived);
    });
}
