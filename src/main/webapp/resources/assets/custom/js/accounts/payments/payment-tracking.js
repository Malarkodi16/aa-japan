var filterAuctionHouse;
var invoicePaymentDetails = $('#invoicePaymentDetails');
var invoicePaymentDetailsDetailTable;
var tableEle = invoicePaymentDetails.find('table')
$(function() {
    var supplierFilter;
    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
        supplierJson = data;
        $('#supplier').select2({
            allowClear: true,
            width: '100%',
            data: $.map(supplierJson, function(item) {
                return {
                    id: item.supplierCode,
                    text: item.company,
                    data: item
                };
            })
        })
    })
    $.getJSON(myContextPath + "/data/general/suppliers.json", function(data) {
        remitterJson = data;
        $('select#genaralSupplierFilter').select2({
            allowClear: true,
            placeholder: 'Select Remiter',
            width: '100%',
            data: $.map(remitterJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        })

    })
    $.getJSON(myContextPath + "/data/transporters.json", function(data) {
        transporterJson = data;
        $('#transporter').select2({
            allowClear: true,
            width: '100%',
            data: $.map(transporterJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        })
    });
    // Select Forwarder
    $.getJSON(myContextPath + "/data/forwarders.json", function(data) {
        $('#forwarderFilter').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).val('').trigger("change");
    });
    $.getJSON(myContextPath + "/data/inspectioncompanies.json", function(data) {
        let inspectionCompanyJson = data;
        $('#inspectionCompanyFilter').select2({
            allowClear: true,
            width: '100%',
            data: $.map(inspectionCompanyJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        })

    });
    $('#supplier').select2({
        allowClear: true,
        placeholder: 'All',
    }).on("change", function(event) {
        var supplier = $(this).find('option:selected').val();
        let auctionHouseArr = [];
        $.each(supplierJson, function(i, item) {
            if (item.supplierCode === supplier) {
                auctionHouseArr = item.supplierLocations;
                return false;
            }

        });
        $('#auctionHouseId').empty();
        $('#auctionHouseId').select2({
            placeholder: "All",
            allowClear: true,
            data: $.map(auctionHouseArr, function(item) {
                return {
                    id: item.id,
                    text: item.auctionHouse,
                    data: item
                };
            })
        }).val('').trigger("change");
    })

    $('#invoiceTypeFilter').select2({
        allowClear: true,
        placeholder: 'All',
    }).on("change", function(event) {
        var purchaseFilter = $(this).find('option:selected').val();

        $('#table-filter-purchased-date').val('');
        $('#supplier').val('').trigger('change');
        $('#transporter').val('').trigger('change');
        $('#forwarderFilter').val('').trigger('change');
        $('#genaralSupplierFilter').val('').trigger('change');
        if (purchaseFilter == "") {
            $('#auctionHouseDiv').addClass('hidden')
            $('#supplierFilterDiv').addClass('hidden')
            $('#transporterFilterDiv').addClass('hidden')
            $('#forwarderFilterDiv').addClass('hidden')
            $('#genaralSupplierFilterDiv').addClass('hidden')
        } else if (purchaseFilter == "0") {
            $('#auctionHouseDiv').removeClass('hidden')
            $('#supplierFilterDiv').removeClass('hidden')
            $('#transporterFilterDiv').addClass('hidden')
            $('#forwarderFilterDiv').addClass('hidden')
            $('#genaralSupplierFilterDiv').addClass('hidden')
        } else if (purchaseFilter == "1") {
            $('#auctionHouseDiv').addClass('hidden')
            $('#supplierFilterDiv').addClass('hidden')
            $('#transporterFilterDiv').removeClass('hidden')
            $('#forwarderFilterDiv').addClass('hidden')
            $('#genaralSupplierFilterDiv').addClass('hidden')
        } else if (purchaseFilter == "4") {
            $('#auctionHouseDiv').addClass('hidden')
            $('#supplierFilterDiv').addClass('hidden')
            $('#transporterFilterDiv').addClass('hidden')
            $('#forwarderFilterDiv').addClass('hidden')
            $('#genaralSupplierFilterDiv').removeClass('hidden')
        } else if (purchaseFilter == "5") {
            $('#auctionHouseDiv').addClass('hidden')
            $('#supplierFilterDiv').addClass('hidden')
            $('#transporterFilterDiv').addClass('hidden')
            $('#forwarderFilterDiv').addClass('hidden')
            $('#genaralSupplierFilterDiv').addClass('hidden')
            $('#inspectionCompanyFilterDiv').removeClass('hidden')
        } else if (purchaseFilter != "0" && purchaseFilter != "1") {
            $('#auctionHouseDiv').addClass('hidden')
            $('#supplierFilterDiv').addClass('hidden')
            $('#transporterFilterDiv').addClass('hidden')
            $('#forwarderFilterDiv').removeClass('hidden')
            $('#genaralSupplierFilterDiv').addClass('hidden')
        }

        if (purchaseFilter == 3) {
            table.column(6).visible(true)
            table.column(8).visible(true)
            table.column(10).visible(true)
        } else {
            table.column(6).visible(false)
            table.column(8).visible(false)
            table.column(10).visible(false)
        }
    })

    // Date range picker
    var invoice_min;
    var invoice_max;
    $('#table-filter-purchased-date').daterangepicker({
        autoUpdateInput: false,
        showDropdowns: true,
        //minYear: 1901,
        //maxYear: parseInt(moment().format('YYYY')),
        "autoApply": true,
        "minDate": "01/01/1970",
        "maxDate": moment().format('MM/DD/YYYY')
    }).on("apply.daterangepicker", function(e, picker) {
        invoice_min = picker.startDate.format('DD-MM-YYYY');
        invoice_max = picker.endDate.format('DD-MM-YYYY');
        picker.element.val(invoice_min + ' - ' + invoice_max);
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        invoice_min = '';
        invoice_max = '';
        $('#table-filter-purchased-date').val('');
        $(this).remove();

    })

    var table_element = $('#table-auction-payment');
    var table = table_element.DataTable({
         "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": {
            url: myContextPath + "/accounts/payment/tracking/data-list",
            data: function(data) {
                data.type = $('#invoiceTypeFilter').val();
                data.remitter = $('#supplier').val() || $('#transporter').val() || $('#forwarderFilter').val() || $('#genaralSupplierFilter').val() || $('#inspectionCompanyFilter').val();
                data.fromDate = invoice_min;
                data.toDate = invoice_max;
            }
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "className": "details-control",
            "data": "invoiceDate"

        }, {
            targets: 1,
            "className": "details-control",
            visible: false,
            "render": function(data, type, row) {
                return row.invoiceNo
                //.replace(/^.*?([^a-z-]\d+)$/gmi, '$1');
            }

        }, {
            targets: 2,
            "className": "details-control",
            "data": "invoiceNo"

        }, {
            targets: 3,
            "className": "details-control",
            "data": "refNo"

        }, {
            targets: 4,
            "className": "details-control",
            "data": "invoiceName",
            "render": function(data, type, row) {
                var data;
                data = data == null ? '' : data;
                if (!isEmpty(row.remitter)) {
                    if (row.remitter.toLowerCase() == 'others') {
                        data = row.remitterOthers;
                        return data;
                    } else {
                        return data;
                    }
                }
                return data;

            }
        }, {

            targets: 5,
            "className": "dt-right details-control",
            "type": "num-fmt",
            "data": "totalAmount",
            "render": function(data, type, row) {
                return '<span class="autonumber totalAmount"data-a-sign="¥ " data-m-dec="0">' + ifNotValid(data, 0.0) + '</span>';
            }
        }, {

            targets: 6,
            "visible": false,
            "className": "dt-right details-control",
            "type": "num-fmt",
            "data": "totalAmountUsd",
            "render": function(data, type, row) {
                return '<span class="autonumber totalAmountUsd"data-a-sign="$ " data-m-dec="0">' + ifNotValid(data, 0.0) + '</span>';
            }
        }, {

            targets: 7,
            "className": "dt-right details-control",
            "type": "num-fmt",
            "data": "paidAmount",
            "render": function(data, type, row) {
                //                 return '<a href="#" data-target="#modal-invoice-payments" title="View payments" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-code="' + row.invoiceNo + '"><span class="autonumber"data-a-sign="¥ " data-m-dec="0">' + ifNotValid(data, 0.0) + '</span></a>';
                return '<span class="autonumber paidAmount"data-a-sign="¥ " data-m-dec="0">' + ifNotValid(data, 0.0) + '</span>';
            }
        }, {

            targets: 8,
            "visible": false,
            "className": "dt-right details-control",
            "type": "num-fmt",
            "data": "paidAmountUsd",
            "render": function(data, type, row) {
                return '<a href="#" data-target="#modal-invoice-payments" title="View payments" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-code="' + row.invoiceNo + '"><span class="autonumber paidAmountUsd"data-a-sign="$ " data-m-dec="0">' + ifNotValid(data, 0.0) + '</span></a>';
            }
        }, {

            targets: 9,
            "className": "dt-right details-control",
            "type": "num-fmt",
            "data": "balance",
            "render": function(data, type, row) {
                return '<span class="autonumber balance"data-a-sign="¥ " data-m-dec="0">' + ifNotValid(data, 0.0) + '</span>';
            }
        }, {

            targets: 10,
            "visible": false,
            "className": "dt-right details-control",
            "type": "num-fmt",
            "data": "balanceUsd",
            "render": function(data, type, row) {
                return '<span class="autonumber balanceUsd"data-a-sign="$ " data-m-dec="0">' + ifNotValid(data, 0.0) + '</span>';
            }
        }, {
            targets: 11,
            "data": "approvedDate",
            "className": "details-control",
        }, {
            targets: 12,
            "data": "approvedBy",
            "className": "details-control",
        }, {
            targets: 13,
            "data": "paymentApproveStatus",
            "render": function(data, type, row) {
                var paymentStatus;
                var className;
                if ($('#invoiceTypeFilter').val() == 5) {
                    if (data == 0) {
                        paymentStatus = "INVOICE_Booking"
                        className = "default"
                    } else if (data == 1) {
                        paymentStatus = "INVOICE_APPROVED"
                        className = "default"
                    } else if (data == 2) {
                        paymentStatus = "PAYMENT_PROCESSING"
                        className = "info"
                    } else if (data == 3) {
                        paymentStatus = "PAYMENT_PARTIALLY_DONE"
                        className = "primary"
                    } else if (data == 4) {
                        paymentStatus = "PAYMENT_COMPLETED"
                        className = "warning"
                    } else if (data == 5) {
                        paymentStatus = "PAYMENT_FREEZED"
                        className = "success"
                    } else if (data == 6) {
                        paymentStatus = "PAYMENT_CANCELLED"
                        className = "danger"
                    }
                } else {
                    if (data == 0) {
                        paymentStatus = "PAYMENT_NOT_APPROVED"
                        className = "danger"
                    } else if (data == 1) {
                        paymentStatus = "PAYMENT_APPROVED"
                        className = "default"
                    } else if (data == 2) {
                        paymentStatus = "PAYMENT_COMPLETED"
                        className = "default"
                    } else if (data == 3) {
                        paymentStatus = "PAYMENT_CANCELLED"
                        className = "danger"
                    } else if (data == 4) {
                        paymentStatus = "PAYMENT_FREEZED"
                        className = "success"
                    } else if (data == 5) {
                        paymentStatus = "PAYMENT_PARTIALLY_DONE"
                        className = "warning"
                    }
                }

                return '<span class="label label-' + className + '">' + paymentStatus + '</span>';
            }

        }, {
            targets: 14,
            "data": "",
            "render": function(data, type, row) {
                var html = '';
                if (row.invoiceUpload == 1) {
                    html += '<a href="' + myContextPath + '/get/' + row.invoiceAttachmentDiskFilename + '?path=' + row.attachmentDirectory + '&from=upload&invoiceNo=' + row.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-file-pdf-o"></i>View Invoice</a>';
                }
                return html;
            }
        }, {
            targets: 15,
            "visible": false,
            "data": "sAuctionHouseId",
            "className": "details-control"
        }],
        "footerCallback": function(row, data, start, end, display) {
            var table = this.api();
            updateFooter(table);
        },
        "drawCallback": function(settings, json) {
            table_element.find('span.autonumber').autoNumeric('init')

        },

        /*excel export*/
        //         buttons: [{
        //             extend: 'excel',
        //             text: 'Export All',
        //             title: '',
        //             filename: function() {
        //                 var d = new Date();
        //                 return 'PaymentTracking_' + d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();
        //             },
        //             attr: {
        //                 type: "button",
        //                 id: 'dt_excel_export_all'
        //             },
        //             exportOptions: {
        //                 columns: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
        //             },
        //             customize: function(xlsx) {
        //                 var sheet = xlsx.xl.worksheets['sheet1.xml'];
        //                 var downrows = 5;
        //                 var clRow = $('row', sheet);
        //                 //update Row
        //                 clRow.each(function() {
        //                     var attr = $(this).attr('r');
        //                     var ind = parseInt(attr);
        //                     ind = ind + downrows;
        //                     $(this).attr("r", ind);
        //                 });

        //                 // Update  row > c
        //                 $('row c ', sheet).each(function() {
        //                     var attr = $(this).attr('r');
        //                     var pre = attr.substring(0, 1);
        //                     var ind = parseInt(attr.substring(1, attr.length));
        //                     ind = ind + downrows;
        //                     $(this).attr("r", pre + ind);
        //                 });

        //                 function Addrow(index, data) {
        //                     msg = '<row r="' + index + '">'
        //                     for (i = 0; i < data.length; i++) {
        //                         var key = data[i].k;
        //                         var value = data[i].v;
        //                         msg += '<c t="inlineStr" r="' + key + index + '" s="42">';
        //                         msg += '<is>';
        //                         msg += '<t>' + value + '</t>';
        //                         msg += '</is>';
        //                         msg += '</c>';
        //                     }
        //                     msg += '</row>';
        //                     return msg;
        //                 }

        //                 var d = new Date();
        //                 let currentDate = d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();

        //                 //insert
        //                 var r2 = Addrow(2, [{
        //                     k: 'A',
        //                     v: 'Company'
        //                 }, {
        //                     k: 'B',
        //                     v: 'AA Japan'
        //                 }]);
        //                 var r3 = Addrow(3, [{
        //                     k: 'A',
        //                     v: 'Title'
        //                 }, {
        //                     k: 'B',
        //                     v: 'Payment Tracking'
        //                 }]);
        //                 var r4 = Addrow(4, [{
        //                     k: 'A',
        //                     v: 'Date'
        //                 }, {
        //                     k: 'B',
        //                     v: !isEmpty(invoice_min) ? invoice_min + " - " + invoice_max : currentDate
        //                 }]);

        //                 sheet.childNodes[0].childNodes[1].innerHTML = r2 + r3 + r4 + sheet.childNodes[0].childNodes[1].innerHTML;
        //             }
        //         }]

    });

    $("#excel_export_all").on("click", function() {
        //         table.button("#dt_excel_export_all").trigger();
        let parm = {};
        parm['type'] = $('select[name="invoiceType"]').val();
        parm['supplier'] = $('#supplier').val() || $('#transporter').val() || $('#forwarderFilter').val() || $('#inspectionCompanyFilter').val();
        parm['invoiceDateFrom'] = invoice_min;
        parm['invoiceDateTo'] = invoice_max;
        if (!isEmpty(parm.type)) {
            $.redirect(myContextPath + '/accounts/report/payment/tracking/report', parm, 'get');
        }

    });

    function updateFooter(table) {

        var intVal = function(i) {
            return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
        };
        var isValid = function(val) {
            return typeof val === 'undefined' || val == null ? 0 : val;
        }
        // page total
        // purchase cost total
        var amountTotal = table.column(5, {
            page: 'current'
        }).nodes().reduce(function(a, b) {

            var amount = Number(isValid($(b).find('span.totalAmount').autoNumeric('init').autoNumeric('get')));
            var total = Number((amount));

            return intVal(a) + total;
        }, 0);
        // purchase cost tax total
        var amountTotalUsd = table.column(6, {
            page: 'current'
        }).nodes().reduce(function(a, b) {

            var amount = Number(isValid($(b).find('span.totalAmountUsd').autoNumeric('init').autoNumeric('get')));
            var total = Number((amount));

            return intVal(a) + total;
        }, 0);
        // commission amount total
        var paidAmountTotal = table.column(7, {
            page: 'current'
        }).nodes().reduce(function(a, b) {
            var amount = Number(isValid($(b).find('span.paidAmount').autoNumeric('init').autoNumeric('get')));

            var total = Number(amount);

            return intVal(a) + total;
        }, 0);

        var paidAmountTotalUsd = table.column(8, {
            page: 'current'
        }).nodes().reduce(function(a, b) {
            var amount = Number(isValid($(b).find('span.paidAmountUsd').autoNumeric('init').autoNumeric('get')));

            var total = Number(amount);

            return intVal(a) + total;
        }, 0);

        var balanceAmount = table.column(9, {
            page: 'current'
        }).nodes().reduce(function(a, b) {
            var amount = Number(isValid($(b).find('span.balance').autoNumeric('init').autoNumeric('get')));

            var total = Number(amount);

            return intVal(a) + total;
        }, 0);

        var balanceAmountUsd = table.column(10, {
            page: 'current'
        }).nodes().reduce(function(a, b) {
            var amount = Number(isValid($(b).find('span.balanceUsd').autoNumeric('init').autoNumeric('get')));

            var total = Number(amount);

            return intVal(a) + total;
        }, 0);
        //         // commission amount tax total
        //         var pageCommissionTaxTotal = table.column(10, {
        //             page: 'current'
        //         }).nodes().reduce(function(a, b) {
        //             var amount = Number(isValid($(b).find('input[name="commision"]').autoNumeric('init').autoNumeric('get')));
        //             var tax = Number(isValid($(b).find('input[name="commisionTax"]').autoNumeric('init').autoNumeric('get')));
        //             var commisionTax = Number(isValid($(b).find('input[name="commisionTaxAmount"]').autoNumeric('init').autoNumeric('get')));
        //             var taxAmount = Number((amount * tax) / 100);
        //             var commisionTaxAmount = Number(ifNotValid(commisionTax, taxAmount));

        //             return intVal(a) + commisionTaxAmount;
        //         }, 0);
        //         // road tax total
        //         var pageRoadTaxTotal = table.column(11, {
        //             page: 'current'
        //         }).nodes().reduce(function(a, b) {
        //             var amount = Number(isValid($(b).find('input[name="roadTax"]').autoNumeric('init').autoNumeric('get')));
        //             return intVal(a) + amount;
        //         }, 0);
        //         // recycle amount total
        //         var pageRecycleTotal = table.column(12, {
        //             page: 'current'
        //         }).nodes().reduce(function(a, b) {
        //             var amount = Number(isValid($(b).find('input[name="recycle"]').autoNumeric('init').autoNumeric('get')));
        //             return intVal(a) + amount;
        //         }, 0);

        //         // others amount total
        //         var pageOtherChargesTotal = table.column(13, {
        //             page: 'current'
        //         }).nodes().reduce(function(a, b) {
        //             var amount = Number(isValid($(b).find('input[name="otherCharges"]').autoNumeric('init').autoNumeric('get')));
        //             return intVal(a) + amount;
        //         }, 0);
        //         // others tax total
        //         var pageOtherChargesTaxTotal = table.column(13, {
        //             page: 'current'
        //         }).nodes().reduce(function(a, b) {
        //             var amount = Number(isValid($(b).find('input[name="otherCharges"]').autoNumeric('init').autoNumeric('get')));
        //             var tax = Number(isValid($(b).find('input[name="othersTaxValue"]').autoNumeric('init').autoNumeric('get')));
        //             var othersTax = Number(isValid($(b).find('input[name="othersCostTaxAmount"]').autoNumeric('init').autoNumeric('get')));
        //             var taxAmount = Number((amount * tax) / 100);
        //             var othersTaxAmount = Number(ifNotValid(othersTax, taxAmount));

        //             return intVal(a) + othersTaxAmount;
        //         }, 0);

        // $('span.autonumber.total,span.autonumber.pagetotal').autoNumeric('destroy');

        $('#table-auction-payment>tfoot>tr.sum').find('span.autonumber.pagetotal.amountTotal').autoNumeric('init').autoNumeric('set', amountTotal);

        //         $('#table-purchased>tfoot>tr.taxtotal').find('span.autonumber.pagetotal.commisionTaxTotal').autoNumeric('init').autoNumeric('set', pageCommissionTaxTotal);

        //         $('#table-purchased>tfoot>tr.taxtotal').find('span.autonumber.pagetotal.otherChargesTaxTotal').autoNumeric('init').autoNumeric('set', pageOtherChargesTaxTotal);

        $('#table-auction-payment>tfoot>tr.sum').find('span.autonumber.pagetotal.amountTotalUsd').autoNumeric('init').autoNumeric('set', amountTotalUsd);

        $('#table-auction-payment>tfoot>tr.sum').find('span.autonumber.pagetotal.paidAmount').autoNumeric('init').autoNumeric('set', paidAmountTotal);

        $('#table-auction-payment>tfoot>tr.sum').find('span.autonumber.pagetotal.paidAmountUsd').autoNumeric('init').autoNumeric('set', paidAmountTotalUsd);

        $('#table-auction-payment>tfoot>tr.sum').find('span.autonumber.pagetotal.balance').autoNumeric('init').autoNumeric('set', balanceAmount);

        $('#table-auction-payment>tfoot>tr.sum').find('span.autonumber.pagetotal.balanceUsd').autoNumeric('init').autoNumeric('set', balanceAmountUsd);

        //         var pageOverallTotal = Number(pagePurchaseCostTotal) + Number(pagePurchaseCostTaxTotal) + Number(pageCommissionTotal) + Number(pageCommissionTaxTotal) + Number(pageRoadTaxTotal) + Number(pageRecycleTotal) + Number(pageOtherChargesTotal) + Number(pageOtherChargesTaxTotal);

        //         $('#table-purchased tfoot').find('tr#grandTotal').find('.pagetotal').autoNumeric('init').autoNumeric('set', pageOverallTotal);

    }

    $('#btn-search').on('click', function() {
        table.ajax.reload();
    })
    $('#auctionHouseId').select2({
        placeholder: "Auction House",
        allowClear: true
    }).on("change", function(event) {
        filterAuctionHouse = $('#auctionHouseId').find('option:selected').val();
        table.draw();
    })

    $('.select2').select2({
        width: '100%',
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true,
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
    $('#table-auction-payment tbody').on('click', 'tr td .selRow', function() {
        var row = $(this).closest('tr');
        if (!row.hasClass('selected')) {
            row.addClass('selected');
        } else {
            row.removeClass('selected');
        }
        var selRowCount = $('.selected').length;
        if (selRowCount > 0) {

            $('#approvePayment').prop("disabled", false);
        } else {
            $('#approvePayment').prop("disabled", true);
        }
    });
    $('#select-all').click(function() {
        //	 $('#table-auction-payment thead').on('click', 'tr td #select-all', function () {
        if ($('#select-all').prop('checked') == true) {
            $('#approvePayment').prop("disabled", false);
        } else {
            $('#approvePayment').prop("disabled", true);
        }
        $('input:checkbox').not(this).prop('checked', this.checked);
    });

    $('#table-auction-payment tbody').on('click', 'td.details-control', function() {
        // $(this).closest('tbody').find('.container-fluid').hide();
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
                    $(row.node()).removeClass('shown');
                }

            })
            var rowData = row.data();
            var childHtml = '';
            let invoiceTypeFlag = $('#invoiceTypeFilter').val()
            //             if (invoiceTypeFlag == 0) {
            childHtml = format(rowData)
            //             } else if (invoiceTypeFlag == 1) {
            //                 childHtml = transportFormat(rowData)
            //             } else if (invoiceTypeFlag == 2) {
            //                 childHtml = forwarderFormat(rowData)
            //             } else if (invoiceTypeFlag == 3) {
            //                 childHtml = freightShippingFormat(rowData)
            //             } else if (invoiceTypeFlag == 4) {
            //                 childHtml = tinvFormat(rowData)
            //             }

            row.child(childHtml).show();
            row.child().find('span.autonumber').autoNumeric('init');
            tr.addClass('shown');

        }
    });

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //Auction Housee filter
        if (typeof filterAuctionHouse != 'undefined' && filterAuctionHouse.length != '') {
            if (aData[15].length == 0 || aData[15] != filterAuctionHouse) {
                return false;
            }
        }
        return true;
    });

    //image_preview
    let modal_image_preview = $("#modal-invoice-preview");
    modal_image_preview.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(e.relatedTarget);
        var data = table.row($(targetElement).closest('tr')).data();
        let filename = data.invoiceAttachmentDiskFilename;
        if (!isEmpty(filename)) {
            $('embed.image_preview').attr('src', myContextPath + '/get/' + filename + '?path=' + data.attachmentDirectory + '&from=upload');
        } else {
            $('embed.image_preview').attr('src', myContextPath + '/resources/assets/images/no-image-icon.png');
        }

    }).on('click', 'button#btn-print', function(e) {
        let path = $(this).closest('.modal').find('.modal-body>img').attr('src');
        let data = {};
        data["printable"] = path;
        data["type"] = 'image';
        printJS(data);
    });

    //invoice payments Detail Modal
    var invoicePaymentsDetailsEle = $('#modal-invoice-payments')
    var invoicePaymentsDetailsModalBody = invoicePaymentsDetailsEle.find('#invoicePaymentDetails');
    invoicePaymentsDetailsEle.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var rowData = table.row($(event.relatedTarget).closest('tr')).data();
        invoicePaymentsDetailsModalBody.find('input[name="invoiceNo"]').val(rowData.invoiceNo)
        setPaymentDetailsData(rowData.invoiceNo);
    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
        $('#invoicePaymentDetails').find('table').dataTable().fnDestroy();
    });

});
function format(rowData) {
    var element = $('#clone-container>#payment-approve-details>.clone-element').clone();
    $(element).find('input[name="paymentinvoiceNo"]').val(rowData.invoiceNo);
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    let invoiceNo = rowData.invoiceNo;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "get",
        processData: false,
        url: myContextPath + "/accounts/payment/invoicePayments/data-source?code=" + invoiceNo,
        contentType: false,
        async: false,
        success: function(data) {
            data = data.data;
            for (var i = 0; i < data.length; i++) {
                var row = $(rowClone).clone();
                $(row).find('td.s-no').html(i + 1);
                $(row).find('td.paymentVoucherNo').html(ifNotValid(data[i].code, ''));
                $(row).find('td.date').html(ifNotValid(data[i].sApprovedDate, ''));
                $(row).find('td.bank').html(ifNotValid(data[i].bankName, ''));
                $(row).find('td.amount span.autonumber').html(ifNotValid(data[i].amount, ''));
                $(row).find('td.action').append('<a href="' + myContextPath + '/get/' + data[i].bankStatementAttachmentDiskFilename + '?path=' + data[i].attachmentDirectory + '&from=upload&invoiceNo=' + data[i].invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-eye"></i></a>');
                $(row).removeClass('hide');
                $(element).find('table>tbody').append(row);
            }
        }
    });

    return element;

}
//Purchase Format
// function format_(rowData) {
//     var element = $('#clone-container>#payment-approve-details>.clone-element').clone();
//     $(element).find('input[name="paymentinvoiceNo"]').val(rowData.invoiceNo)
//     var tbody = '';
//     var rowClone = $(element).find('table>tbody').find('tr.clone-row');
//     let purchaseCostTotal = 0
//       , purchaseCostTaxTotal = 0
//       , commisionTotal = 0
//       , commisionTaxTotal = 0
//       , roadTaxTotal = 0
//       , recycleTotal = 0
//       , otherChargesTotal = 0;
//     for (var i = 0; i < rowData.approvePaymentItems.length; i++) {
//         var row = $(rowClone).clone();
//         if (rowData.approvePaymentItems[i].type == 'REAUCTION') {
//             row.css('color', 'red');
//         }
//         purchaseCostTotal += Number(ifNotValid(rowData.approvePaymentItems[i].purchaseCost, 0));
//         purchaseCostTaxTotal += Number(ifNotValid(rowData.approvePaymentItems[i].purchaseCostTaxAmount, 0));
//         commisionTotal += Number(ifNotValid(rowData.approvePaymentItems[i].commision, 0));
//         commisionTaxTotal += Number(ifNotValid(rowData.approvePaymentItems[i].commisionTaxAmount, 0));
//         roadTaxTotal += Number(ifNotValid(rowData.approvePaymentItems[i].roadTax, 0));
//         recycleTotal += Number(ifNotValid(rowData.approvePaymentItems[i].recycle, 0));
//         otherChargesTotal += Number(ifNotValid(rowData.approvePaymentItems[i].otherCharges, 0));

//         $(row).find('td.s-no').html(i + 1);
//         $(row).find('td.chassisNo').html(ifNotValid(rowData.approvePaymentItems[i].chassisNo, ''));
//         $(row).find('td.type').html(ifNotValid(rowData.approvePaymentItems[i].type, ''));
//         $(row).find('td.purchaseCost span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].purchaseCost, ''));
//         $(row).find('td.purchaseCostTax span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].purchaseCostTaxAmount, ''));
//         $(row).find('td.commision span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].commision, ''));
//         $(row).find('td.commisionTax span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].commisionTaxAmount, ''));
//         $(row).find('td.roadTax span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].roadTax, ''));
//         $(row).find('td.recycle span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].recycle, ''));
//         $(row).find('td.otherCharges span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].otherCharges, ''));
//         $(row).removeClass('hide');
//         $(element).find('table>tbody').append(row);
//     }
//     let footer = $(element).find('table>tfoot')

//     $(footer).find('td.purchaseCost span.autonumber').html(purchaseCostTotal);
//     $(footer).find('td.purchaseCostTax span.autonumber').html(purchaseCostTaxTotal);
//     $(footer).find('td.commision span.autonumber').html(commisionTotal);
//     $(footer).find('td.commisionTax span.autonumber').html(commisionTaxTotal);
//     $(footer).find('td.roadTax span.autonumber').html(roadTaxTotal);
//     $(footer).find('td.recycle span.autonumber').html(recycleTotal);
//     $(footer).find('td.otherCharges span.autonumber').html(otherChargesTotal);

//     return element;

// }
function format1(rowData) {
    var element = $('#clone-container>#payment-approve-details>.clone-element').clone();
    $(element).find('input[name="paymentinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.approvePaymentItems.length; i++) {
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.stockNo').html(ifNotValid(rowData.approvePaymentItems[i].stockNo, ''));
        $(row).find('td.purchaseCost>span').html(ifNotValid(rowData.approvePaymentItems[i].purchaseCost, ''));
        $(row).find('td.commision>span').html(ifNotValid(rowData.approvePaymentItems[i].commision, ''));
        $(row).find('td.roadTax>span').html(ifNotValid(rowData.approvePaymentItems[i].roadTax, ''));
        $(row).find('td.recycle>span').html(ifNotValid(rowData.approvePaymentItems[i].recycle, ''));
        $(row).find('td.otherCharges>span').html(ifNotValid(rowData.approvePaymentItems[i].otherCharges, ''));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);
    }
    return element;

}
//Transport Format
function transportFormat(rowData) {
    var element = $('#transport-clone-container>#payment-transport-approve-details>.clone-element').clone();
    $(element).find('input[name="transportinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    var total_amount = 0
      , tax_amount_total = 0;
    for (var i = 0; i < rowData.approvePaymentItems.length; i++) {
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.chassisNo').html(ifNotValid(rowData.approvePaymentItems[i].chassisNo, ''));
        $(row).find('td.maker').html(ifNotValid(rowData.approvePaymentItems[i].maker, ''));
        $(row).find('td.model').html(ifNotValid(rowData.approvePaymentItems[i].model, ''));
        $(row).find('td.taxamount>span').html(ifNotValid(rowData.approvePaymentItems[i].taxamount, ''));
        $(row).find('td.amount>span').html(ifNotValid(rowData.approvePaymentItems[i].amount, ''));
        $(row).find('td.action').append('<a href="' + myContextPath + '/get/' + rowData.invoiceAttachmentDiskFilename + '?path=' + rowData.attachmentDirectory + '&from=upload&invoiceNo=' + rowData.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-eye"></i></a>');
        $(row).removeClass('hide');
        total_amount += Number(ifNotValid(rowData.approvePaymentItems[i].amount, 0))
        tax_amount_total += Number(ifNotValid(rowData.approvePaymentItems[i].taxamount, 0))
        $(element).find('table>tbody').append(row);
    }
    $(element).find('table>tfoot td.amount>span').html(total_amount);
    $(element).find('table>tfoot td.taxamount>span').html(tax_amount_total);
    return element;

}
//t_inv Format
function tinvFormat(rowData) {
    var element = $('#others-clone-container>#others-invoice-details>.clone-element').clone();
    $(element).find('input[name="paymentinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    let amountTotal = 0;
    let taxTotal = 0;
    let totalTaxIncluded = 0;
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.approvePaymentItems.length; i++) {
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.category').html((ifNotValid(rowData.approvePaymentItems[i].invoiceType, '').toLowerCase() != 'others' ? ifNotValid(rowData.approvePaymentItems[i].category + ' [' + ifNotValid(rowData.approvePaymentItems[i].tkcCode, "") + '] - ' + ifNotValid(rowData.approvePaymentItems[i].tkcDescription, ""), '') : ifNotValid(rowData.approvePaymentItems[i].categoryOthers, '')));
        $(row).find('td.description').html(ifNotValid(rowData.approvePaymentItems[i].description, ''));
        $(row).find('td.amountInYen span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].amountInYen, ''));
        $(row).find('td.taxAmount span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].taxAmount, ''));
        $(row).find('td.hiddenTaxAmount input.hiddenTaxAmount').html(ifNotValid(rowData.approvePaymentItems[i].taxAmount, ''));
        $(row).find('td.totalAmount span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].totalWithTax, ''));
        $(row).find('td.amount span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].amount, ''));
        $(row).find('td.currency').html(ifNotValid(rowData.approvePaymentItems[i].sourceCurrency, ''));
        $(row).find('td.exchangeRate span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].exchangeRate, ''));
        $(row).find('td.action').append('<a href="' + myContextPath + '/get/' + rowData.invoiceAttachmentDiskFilename + '?path=' + rowData.attachmentDirectory + '&from=upload&invoiceNo=' + rowData.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-eye"></i></a>');
        amountTotal += Number(ifNotValid(rowData.approvePaymentItems[i].amountInYen, 0));
        taxTotal += Number(ifNotValid(rowData.approvePaymentItems[i].taxAmount, 0));
        totalTaxIncluded += Number(ifNotValid(rowData.approvePaymentItems[i].totalWithTax, 0));

        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);
    }
    $(element).find('table>tfoot td.amount>span').html(amountTotal);
    $(element).find('table>tfoot td.taxTotal>span').html(taxTotal);
    $(element).find('table>tfoot td.taxIncluded>span').html(totalTaxIncluded);
    return element;

}
//Forwarder Invoice Format
function forwarderFormat(rowData) {
    var element = $('#forwarder-clone-container>#payment-forwarder-approve-details>.clone-element').clone();
    $(element).find('input[name="forwarderinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    let amountTotal = 0;
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.approvePaymentItems.length; i++) {
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.chassisNo').html(ifNotValid(rowData.approvePaymentItems[i].chassisNo, ''));
        $(row).find('td.storage span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].amount, ''));
        $(row).find('td.shipping span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].shippingCharges, ''));
        $(row).find('td.photoCharges span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].photoCharges, ''));
        $(row).find('td.blAmendCombineCharges span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].blAmendCombineCharges, ''));
        $(row).find('td.radiationCharges span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].radiationCharges, ''));
        $(row).find('td.repairCharges span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].repairCharges, ''));
        $(row).find('td.yardHandlingCharges span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].yardHandlingCharges, ''));
        $(row).find('td.inspectionCharges span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].inspectionCharges, ''));
        $(row).find('td.transportCharges span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].transportCharges, ''));
        $(row).find('td.freightCharges span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].freightCharges, ''));
        $(row).find('td.remarks').html(ifNotValid(rowData.approvePaymentItems[i].remarks, ''));
        $(row).find('td.amount span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].totalAmount, ''));
        $(row).find('td.action').append('<a href="' + myContextPath + '/get/' + rowData.invoiceAttachmentDiskFilename + '?path=' + rowData.attachmentDirectory + '&from=upload&invoiceNo=' + rowData.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-eye"></i></a>');
        amountTotal += Number(ifNotValid(rowData.approvePaymentItems[i].totalAmount, 0));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);
    }
    $(element).find('table>tfoot td.amount>span').html(amountTotal);
    return element;

}
//Freight Shipping Invoice Format
function freightShippingFormat(rowData) {
    var element = $('#freightshipping-clone-container>#payment-freightshipping-approve-details>.clone-element').clone();
    $(element).find('input[name="freightshippinginvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.approvePaymentItems.length; i++) {
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.stockNo').html(ifNotValid(rowData.approvePaymentItems[i].stockNo, ''));
        $(row).find('td.freightCharge>span').html(ifNotValid(rowData.approvePaymentItems[i].freightCharge, ''));
        $(row).find('td.shippingCharge>span').html(ifNotValid(rowData.approvePaymentItems[i].shippingCharge, ''));
        $(row).find('td.inspectionCharge>span').html(ifNotValid(rowData.approvePaymentItems[i].inspectionCharge, ''));
        $(row).find('td.radiationCharge>span').html(ifNotValid(rowData.approvePaymentItems[i].radiationCharge, ''));
        $(row).find('td.otherCharges>span').html(ifNotValid(rowData.approvePaymentItems[i].otherCharges, ''));
        $(row).find('td.amount>span').html(ifNotValid(rowData.approvePaymentItems[i].amount, ''));
        $(row).find('td.action').append('<a href="' + myContextPath + '/get/' + rowData.invoiceAttachmentDiskFilename + '?path=' + rowData.attachmentDirectory + '&from=upload&invoiceNo=' + rowData.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-eye"></i></a>');
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);
    }
    return element;
}

function setPaymentDetailsData(code) {

    invoicePaymentDetailsDetailTable = tableEle.DataTable({
         "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": false,
        "ajax": myContextPath + "/accounts/payment/invoicePayments/data-source?code=" + code,
        columnDefs: [{
            "targets": '_all',
            "defaultContent": "",
            "className": "vcenter"
        }, {
            targets: 0,
            "className": "details-control",
            "name": "Date",
            "data": "sApprovedDate",

        }, {
            targets: 1,
            "className": "details-control",
            "name": "Bank",
            "data": "bankName"
        }, {
            targets: 2,
            "className": "details-control",
            "name": "Amount",
            "data": "amount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }],
        "drawCallback": function(settings, json) {
            $('#table-detail-invoice').find('span.autonumber').autoNumeric('init')
        }

    })

}
