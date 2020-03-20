$(function() {

    // Date range picker
    var remit_date_min;
    var remit_date_max;
    $('#table-filter-created-date').daterangepicker({
        autoUpdateInput: false,
        showDropdowns: true,
        //minYear: 1901,
        //maxYear: parseInt(moment().format('YYYY')),
        "autoApply": true,
        "minDate": "01/01/1970",
        "maxDate": moment().format('MM/DD/YYYY')
    }).on("apply.daterangepicker", function(e, picker) {
        remit_date_min = picker.startDate.format('DD-MM-YYYY');
        remit_date_max = picker.endDate.format('DD-MM-YYYY');
        picker.element.val(remit_date_min + ' - ' + remit_date_max);
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        remit_date_min = '';
        remit_date_max = '';
        $('#table-filter-created-date').val('');
        $(this).remove();

    })

    var tableEle = $('#table-ttApprove');
    table = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": {
            url: myContextPath + "/daybook/tt-owned/data-source",
            data: function(data) {
                data.fromDate = remit_date_min;
                data.toDate = remit_date_max;
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
            targets: 0,
            "className": "details-control",
            "data": "createdDate",

        }, {
            targets: 1,
            "className": "details-control",
            "data": "daybookId",

        }, {
            targets: 2,
            "className": "details-control",
            "data": "chassisNo",

        }, {
            targets: 3,
            "className": "details-control",
            "data": "allocationType",
            "render": function(data, type, row) {
                let value = '';
                if (data == 1) {
                    value = 'FIFO';
                } else if (data == 2) {
                    value = 'UNIT ALLOCATION';
                } else if (data == 3) {
                    value = 'ADVANCE';
                } else if (data == 4) {
                    value = 'DEPOSITE';
                } else if (data == 5) {
                    value = 'LC';
                }
                return value;
            }
        }, {
            targets: 4,
            "className": "details-control",
            "data": "remitter"
        }, {
            targets: 5,
            "className": "details-control",
            "data": "bank"
        }, {
            targets: 6,
            "className": "details-control",
            "data": "amount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' "  data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 7,
            "className": "details-control",
            "data": "customerFn"
        }, {
            targets: 8,
            "className": "details-control",
            "data": "salesPerson"
        }, {
            targets: 9,
            orderable: false,
            "data": null,
            "render": function(data, type, row) {
                var html = '';
                html += '<button type="button" id="view payments" class="btn btn-warning ml-5 btn-xs" data-target="#modal-invoice-payments" title="View payments" data-backdrop="static" data-keyboard="false" data-toggle="modal"><i class="fa fa-external-link"></i></button>'
                return html;
            }
        }, {
            targets: 10,
            "visible": false,
            "data": "bankSeq"
        }, {
            targets: 11,
            "visible": false,
            "data": "customerId"
        }, {
            targets: 12,
            "visible": false,
            "data": "salesPersonId"
        }],
        "drawCallback": function(settings, json) {
            $(tableEle).find('span.autonumber').autoNumeric('init')
        }

    });

    $('#btn-search').on('click', function() {
        table.ajax.reload();
    })

    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\./gi, "").replace(/\s+/g, "").replace(/¥/g, "");
    }
    ;// Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

    });

    var date;
    $('#table-filter-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: false
    }).on("change", function(e, picker) {
        date = $(this).val();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.draw();
    });
    $('.input-group').on('click', '.clear-date', function() {
        date = '';
        $('#table-filter-date').val('');
        $(this).remove();
        table.draw();

    })

    $.getJSON(myContextPath + '/data/bank.json', function(data) {
        let bankJson = data;
        $('#bankFilter').select2({
            allowClear: true,
            width: '100%',
            data: $.map(bankJson, function(item) {
                return {
                    id: item.bankSeq,
                    text: item.bankName,
                    data: item
                };
            })
        })
    })

    $.getJSON(myContextPath + "/user/getRoleSales", function(data) {
        var salesJson = data;
        $('#salesPersonFilter').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                var staff = item.username + +(item.userId)
                return {
                    id: item.userId,
                    text: item.username + ' ' + '( ' + item.userId + ' )'
                };
            })
        })
    })

    $('#customerFilter').select2({
        allowClear: true,
        minimumInputLength: 2,
        ajax: {
            url: myContextPath + "/customer/getApprovedCustomer",
            dataType: 'json',
            delay: 500,
            data: function(params) {
                var query = {
                    search: params.term,
                    type: 'public'
                }
                return query;

            },
            processResults: function(data) {
                var results = [];
                data = data.data;
                if (data != null && data.length > 0) {
                    $.each(data, function(index, item) {
                        results.push({
                            id: item.code,
                            text: item.companyName + ' :: ' + item.firstName + ' ' + item.lastName + '(' + item.nickName + ')'
                        });
                    });
                }
                return {
                    results: results
                }

            }

        }
    });

    var filterBank = $('#bankFilter').find('option:selected').val();
    var filterCustomer = $('#customerFilter').find('option:selected').val();
    var filterSalesPrsn = $('#salesPersonFilter').find('option:selected').val();

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {

        //Due Date Filter
        if (typeof date != 'undefined' && date.length != '') {
            if (aData[0].length == 0 || aData[0] != date) {
                return false;
            }
        }

        if (typeof filterBank != 'undefined' && filterBank.length != '') {
            if (aData[10].length == 0 || aData[10] != filterBank) {
                return false;
            }
        }

        if (typeof filterCustomer != 'undefined' && filterCustomer.length != '') {
            if (aData[11].length == 0 || aData[11] != filterCustomer) {
                return false;
            }
        }

        if (typeof filterSalesPrsn != 'undefined' && filterSalesPrsn.length != '') {
            if (aData[12].length == 0 || aData[12] != filterSalesPrsn) {
                return false;
            }
        }

        return true;
    });

    $('#bankFilter').change(function() {
        var selectedVal = $(this).find('option:selected').val();
        filterBank = $('#bankFilter').val();
        table.draw();
    });

    $('#salesPersonFilter').change(function() {
        var selectedVal = $(this).find('option:selected').val();
        filterSalesPrsn = $('#salesPersonFilter').val();
        table.draw();
    });

    $('#customerFilter').change(function() {
        var selectedVal = $(this).find('option:selected').val();
        filterCustomer = $('#customerFilter').val();
        table.draw();
    });

    //Approve Payment
    table.on('click', '#approve', function(event) {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return;
        }
        var rowData = table.row($(this).closest('tr')).data();
        var closestTr = $(this).closest('tr')
        var row = table.row(closestTr)
        let params = rowData.id;
        approvePayment($.param({
            "id": params
        }));

    })
})
