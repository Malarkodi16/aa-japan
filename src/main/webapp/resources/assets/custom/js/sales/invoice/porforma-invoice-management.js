$(function() {
    $('.autonumber').autoNumeric('init');
    var tableEle = $('#table-proformainvoice');
    var table = tableEle.DataTable({
        "pageLength": 25,
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "ajax": myContextPath + "/sales/proformainvoicelist-data",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        /*Datatable Columns*/
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            orderable: false,
            className: 'select-checkbox',
            "data": "id",
            "visible": false,
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" type="checkbox" data-invoiceNo="' + row.invoiceNo + '" value="' + data + '">';
                }
                return data;
            }
        }, {
            targets: 1,
            "className": "details-control",
            "data": "date",

        }, {
            targets: 2,
            "className": "details-control",
            "data": "invoiceNo",

        }, {
            targets: 3,
            "className": "details-control",
            "data": "customerId",
            "render": function(data, type, row) {
                return row.firstName + ' ' + row.lastName;
            }
        }, {
            targets: 4,
            "className": "details-control",
            "data": "consigneeId",
            "render": function(data, type, row) {
                return row.cFirstName + ' ' + row.cLastName;
            }
        }, {
            targets: 5,
            "className": "details-control",
            "data": "notifypartyId",
            "render": function(data, type, row) {
                return row.npFirstName + ' ' + row.npLastName;
            }
        }, {
            targets: 6,
            "className": "details-control",
            "data": "paymentType"
        }, {
            targets: 7,
            "className": "dt-right details-control",
            "data": "total",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="' + row.currencyDetails.symbol + ' " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 8,
            "data": "invoiceNo",
            "className": 'align-center',
            "render": function(data, type, row) {
                var html = ''
                html += '<a href="' + myContextPath + '/download/proforma/invoice/' + data + '.pdf" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-download"></i></a>'
                return html;
            }
        }, {
            targets: 9,
            "data": "customerId",
            "visible": false
        }],

        /*excel export*/
        buttons: [{
            extend: 'excel',
            text: 'Export All',
            title: '',
            filename: function() {
                var d = new Date();
                return 'PorformaInvoiceList_' + d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();
            },
            attr: {
                type: "button",
                id: 'dt_excel_export_all'
            },
            exportOptions: {
                columns: [1, 2, 3, 4, 5, 6, 7],
            }
        }],
        "drawCallback": function(settings, json) {
            $('span.autonumber').autoNumeric('init')
            $('#filter-container').find('input.autonumber').autoNumeric('init')
        }

    });

    $("#excel_export_all").on("click", function() {
        $("#groupselect").attr("checked", false);
        $(".selectBox").attr("checked", false);
        table.rows('.selected').deselect();
        table.button("#dt_excel_export_all").trigger();

    });
    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\./gi, "").replace(/¥/g, "");
    }
    ;$('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    //dashboard count
    $.getJSON(myContextPath + "/data/sales-dashboard/status-count", function(data) {
        $('#porforma-count').html(data.data.porforma);
        $('#sales-count').html(data.data.salesorder);
    });

    table.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            table.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            table.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            table.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            table.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function() {
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() !== table.rows({
            page: 'current'
        }).count()) {
            $(tableEle).find("th.select-checkbox>input").removeClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(tableEle).find("th.select-checkbox>input").addClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() !== table.rows({
            page: 'current'
        }).count()) {
            $(tableEle).find("th.select-checkbox>input").removeClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(tableEle).find("th.select-checkbox>input").addClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', true);

        }

    });
    table.on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
        } else {
            table.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = table.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                }

            })
            var detailsElement = format(row.data());
            row.child(detailsElement).show();
            row.child().find('span.autonumber').autoNumeric('init')
            tr.addClass('shown');
        }
    });

    /* Customer Id Select2 Filter */

    $('#custId').select2({
        allowClear: true,
        minimumInputLength: 2,
        ajax: {
            url: myContextPath + "/customer/search?flag=customer",
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

    //Filter with Customer
    var customerId;
    $('.customer').change(function() {
        customerId = $(this).val();
        table.draw();

    });

    // Date range picker
    var purchased_min;
    var purchased_max;
    $('#table-filter-invoice-date').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        purchased_min = picker.startDate;
        purchased_max = picker.endDate;
        picker.element.val(purchased_min.format('DD-MM-YYYY') + ' - ' + purchased_max.format('DD-MM-YYYY'));
        purchased_min = purchased_min._d.getTime();
        purchased_max = purchased_max._d.getTime();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.draw();
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        purchased_min = '';
        purchased_max = '';
        table.draw();
        $('#table-filter-invoice-date').val('');
        $(this).remove();

    })
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //date filter
        if (typeof purchased_min != 'undefined' && purchased_min.length != '') {
            if (aData[1].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[1], 'DD-MM-YYYY')._d.getTime();
            }
            if (purchased_min && !isNaN(purchased_min)) {
                if (aData._date < purchased_min) {
                    return false;
                }
            }
            if (purchased_max && !isNaN(purchased_max)) {
                if (aData._date > purchased_max) {
                    return false;
                }
            }

        }
        if (typeof customerId != 'undefined' && customerId.length != '') {
            if (aData[9].length == 0 || aData[9] != customerId) {
                return false;
            }
        }
        return true;
    });

    function format(rowData) {

        var ifNotValid = function(val, str) {
            return typeof val === 'undefined' || val == null ? str : val;
        }
        var tbody = '';
        for (var i = 0; i < rowData.items.length; i++) {
            tbody += '<tr>' + '<td>' + (i + 1) + '</td><td class="align-center"><input type="hidden" name="stockNo" value="' + ifNotValid(rowData.items[i].stockNo, '') + '"/>' + ifNotValid(rowData.items[i].stockNo, '') + '</td><td class="align-center">' + ifNotValid(rowData.items[i].model, '') + '</td><td class="align-center">' + ifNotValid(rowData.items[i].chassisNo, '') + '</td><td class="align-center">' + ifNotValid(rowData.items[i].firstRegDate, '') + '</td><td class="dt-right"><span class="autonumber" data-a-sign="' + rowData.currencyDetails.symbol + ' " data-m-dec="0">' + ifNotValid(rowData.items[i].fob, '') + '</span></td><td class="dt-right"><span class="autonumber" data-a-sign="' + rowData.currencyDetails.symbol + ' " data-m-dec="0">' + ifNotValid(rowData.items[i].insurance, '') + '</span></td><td class="dt-right"><span class="autonumber" data-a-sign="' + rowData.currencyDetails.symbol + ' " data-m-dec="0">' + ifNotValid(rowData.items[i].freight, '') + '</span></td>'
        }

        var html = '<div class="box-body no-padding bg-darkgray"><div class="order-item-container"><input type="hidden" name="invoiceOrderNo" value="' + rowData.id + '"/>' + '    <table class="table table-bordered">' + '<thead><tr><th class="align-center bg-ghostwhite" style="width: 10px">#</th><th style="width: 60px" class="align-center bg-ghostwhite">Stock No</th><th style="width: 100px" class="align-center bg-ghostwhite">Maker/Model</th><th style="width: 100px" class="align-center bg-ghostwhite">Chasis No</th><th class="align-center bg-ghostwhite">Year</th><th class="align-center bg-ghostwhite">FOB</th><th class="align-center bg-ghostwhite">Insurance</th><th class="align-center bg-ghostwhite">Freight</th></tr></thead>' + '<tbody>' + tbody + '</tbody>' + '<tfoot><tr><td class="align-center" style="width: 50px;text-align: center;font-weight: bold;" colspan="5">Total</td><td style="width: 60px" class="dt-right bg-ghostwhite"><span class="autonumber" data-a-sign="' + rowData.currencyDetails.symbol + ' " data-m-dec="0">' + rowData.fobTotal + '</span></td><td style="width: 60px" class="dt-right bg-ghostwhite"><span class="autonumber" data-a-sign="' + rowData.currencyDetails.symbol + ' " data-m-dec="0">' + rowData.insuranceTotal + '</span></td><td style="width: 60px" class="dt-right bg-ghostwhite"><span class="autonumber" data-a-sign="' + rowData.currencyDetails.symbol + ' " data-m-dec="0">' + rowData.freightTotal + '</span></td></tr></tfoot>' + '</table>' + '</div>' + '</div>';
        console.log(html);
        return html;
    }

})
