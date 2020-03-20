$(function() {
    $('input.autonumber').autoNumeric('init')
    $('input[type="radio"][name=radioReceivedFilter].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {

        table_recycle.draw();
    })

    $.getJSON(myContextPath + "/data/accounts/claim-count", function(data) {
        setClaimDashboardStatus(data.data)
    });

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
        "dom": '<<t>ip>',
        "pageLength" : 25,
        "ajax": myContextPath + "/accounts/claim/recycle/list/datasource",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            orderable: false,
            className: 'select-checkbox',
            "data": "stockNo",
            'visible': false,
            "width": "5px",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">';
                }
                return data;
            }
        }, {
            targets: 1,
            "className": "details-control",
            "width": "50px",
            "data": "stockNo"
        }, {
            targets: 2,
            "className": "details-control",
            "width": "100px",
            "data": "chassisNo"
        }, {
            targets: 3,
            "width": "100px",
            "className": "details-control",
            "data": "date",
        }, {
            targets: 4,
            "className": "details-control",
            "width": "100px",
            "render": function(data, type, row) {
                var html = ''
                html += '<span class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + ifNotValid(row.recycle, 0) + '</span>'
                return html;
            }
        }, {
            targets: 5,
            "width": "100px",
            "className": "details-control",
            "data": "recycleClaimStatus",
            "render": function(data, type, row) {

                var btnHtml = "";
                if (row.recycleClaimStatus == 0) {
                    btnHtml = '<button type="button" class="btn btn-danger btn-xs" name="btn-claim" style=" width: 75px; "><i class="fa fa-fw fa-money"></i> Claim</button>'
                } else if (row.recycleClaimStatus == 2) {
                    btnHtml = '<button type="button" class="btn btn-success btn-xs" style=" width: 75px; "><i class="fa fa-fw fa-check-circle-o"></i> Received</button>'
                }
                return btnHtml;
            }
        }, {
            targets: 6,
            'visible': false,
            "data": "recycleClaimStatus",
        }],
        "drawCallback": function(settings, json) {
            $('span.autonumber').autoNumeric('init')

        }

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

        if (!confirm($.i18n.prop('common.confirm.update'))) {
            return;
        }

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
            var status = $('input[name="radioReceivedFilter"]:checked').val();
            if (!isEmpty(status) && status == 2) {
                if (aData[6].length == 0 || aData[6] == 0 || aData[6] == 1) {
                    return false;
                }
            } else if (!isEmpty(status) && status != 2) {
                if (aData[6].length == 0 || aData[6] == 2) {
                    return false;
                }
            }
            if (typeof recycle_min != 'undefined' && recycle_min.length != '') {
                if (aData[3].length == 0) {
                    return false;
                }
                if (typeof aData._date == 'undefined') {
                    aData._date = moment(aData[3], 'DD-MM-YYYY')._d.getTime();
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

// update claim received amount
/*function updateClaimReceivedAmount(id, data) {
    var response = "";
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        async: false,
        data: JSON.stringify(data),
        url: myContextPath + "/accounts/claim/recycle/received?id=" + id,
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}*/

// claim recycle amount
function claimRecycle(id) {
    var response = "";
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        async: false,
        url: myContextPath + "/accounts/claim/recycle?id=" + id,
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}
function setClaimDashboardStatus(data) {
    $("#count-tax").html(data.purchaseTax + ' / ' + data.commissionTax);
    $('#count-recycle').html(data.recycle);

}
