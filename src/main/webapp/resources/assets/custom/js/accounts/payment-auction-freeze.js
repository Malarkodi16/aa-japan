var supplierJson;
var invoicePaymentDetails = $('#invoicePaymentDetails');
var invoicePaymentDetailsDetailTable;
var tableElement = invoicePaymentDetails.find('table')
let invoice_format_min, invoice_format_max;
$(function() {
    $.getJSON(myContextPath + "/data/accounts/freeze/payment-booking-count", function(data) {
        setPaymentBookingDashboardStatus(data.data)
    });
    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
        supplierJson = data;
        //Auction Company Filter
        $('#auctionCompany').select2({
            allowClear: true,
            width: '100%',
            placeholder: 'All',
            data: $.map(supplierJson, function(item) {
                return {
                    id: item.supplierCode,
                    text: item.company
                };
            })
        }).on("change", function(event) {
            filterSupplierName = $(this).find('option:selected').val();
            table.draw();
        })
    });

    var due_date;
    $('#table-filter-due-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: false
    }).on("change", function(e, picker) {
        due_date = $(this).val();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.search(due_date).draw();
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        due_date = '';
        $('#table-filter-due-date').val('');
        table.search(due_date).draw();
        $(this).remove();

    })

    $('.select2-select').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true

    })
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })

    var tableEle = $('#table-auction-payment');
    var table = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": true,
        "ajax": {
            url: myContextPath + "/accounts/auction-data/freezed",

        },
        select: {
            style: 'single',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            className: 'select-checkbox',
            orderable : false,
            "data": "id",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox"  name="selRow" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">';
                }
                return data;
            }
        }, {
            targets: 1,
            "className": "details-control",
            "data": "invoiceDate"

        }, {
            targets: 2,
            "className": "details-control",
            "data": "invoiceNo"

        }, {
            targets: 3,
            "className": "details-control",
            "data": "supplier"
        }, {
            targets: 4,
            "className": "details-control",
            "data": "auctionHouse"
        }, {
            targets: 5,
            "data": "dueDate",
            "className": "details-control",
            "className": "vcenter"
        }, {

            targets: 6,
            "className": "dt-right",
            "type": "num-fmt",
            "data": "totalAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 7,
            "data": "refAttachmentDiskFilename",

            "render": function(data, type, row) {
                let freezeStatus = 'success';
                let allTTCopyUploaded = true;
                for (let i = 0; i < row.invoiceTransaction.length; i++) {
                    if (isEmpty(row.invoiceTransaction[i].bankStatementAttachmentFilename)) {
                        allTTCopyUploaded = false;
                        break;
                    }
                }
                if (!allTTCopyUploaded) {
                    freezeStatus = 'warning'
                }
                var html = '';
                if (row.invoiceUpload == 1) {
                    //return '<a href="#" class="ml-5 btn btn-default btn-xs" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-invoice-preview"><i class="fa fa-fw fa-file-pdf-o"></i>View Invoice</a>';
                    //                 html += '<a href="' + myContextPath + '/get/' + row.invoiceAttachmentDiskFilename + '?path=' + row.attachmentDirectory + '&from=upload&invoiceNo=' + row.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs"> <i class="fa fa-fw fa-file-pdf-o"></i>View Invoice</a>';
                    html += '<a href="' + myContextPath + '/get/' + row.invoiceAttachmentDiskFilename + '?path=' + row.attachmentDirectory + '&from=upload&invoiceNo=' + row.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-eye"></i></a>';
                }
                html += '<button type="button" id="view payments" class="btn btn-' + freezeStatus + ' ml-5 btn-xs" data-target="#modal-invoice-payments" title="View payments" data-backdrop="static" data-keyboard="false" data-toggle="modal"><i class="fa fa-external-link"></i></button>'
                return html;
            }

        }, {
            targets: 8,
            "data": "supplierId",
            "visible": false
        }],

        "drawCallback": function(settings, json) {
            tableEle.find('input.autonumber,span.autonumber').autoNumeric('init')

        },

        /*excel export*/
        buttons: [{
            extend: 'excel',
            text: 'Export All',
            title: '',
            filename: function() {
                var d = new Date();
                return 'AuctionPayment_' + d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();
            },
            attr: {
                type: "button",
                id: 'dt_excel_export_all'
            },
            exportOptions: {
                columns: [1, 2, 3, 4, 5, 6],
            },
            customize: function(xlsx) {
                var sheet = xlsx.xl.worksheets['sheet1.xml'];
                var downrows = 5;
                var clRow = $('row', sheet);
                //update Row
                clRow.each(function() {
                    var attr = $(this).attr('r');
                    var ind = parseInt(attr);
                    ind = ind + downrows;
                    $(this).attr("r", ind);
                });

                // Update  row > c
                $('row c ', sheet).each(function() {
                    var attr = $(this).attr('r');
                    var pre = attr.substring(0, 1);
                    var ind = parseInt(attr.substring(1, attr.length));
                    ind = ind + downrows;
                    $(this).attr("r", pre + ind);
                });

                function Addrow(index, data) {
                    msg = '<row r="' + index + '">'
                    for (i = 0; i < data.length; i++) {
                        var key = data[i].k;
                        var value = data[i].v;
                        msg += '<c t="inlineStr" r="' + key + index + '" s="42">';
                        msg += '<is>';
                        msg += '<t>' + value + '</t>';
                        msg += '</is>';
                        msg += '</c>';
                    }
                    msg += '</row>';
                    return msg;
                }

                var d = new Date();
                let currentDate = d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();

                //insert
                var r2 = Addrow(2, [{
                    k: 'A',
                    v: 'Company'
                }, {
                    k: 'B',
                    v: 'AA Japan'
                }]);
                var r3 = Addrow(3, [{
                    k: 'A',
                    v: 'Title'
                }, {
                    k: 'B',
                    v: 'Payment Freezed'
                }]);
                var r4 = Addrow(4, [{
                    k: 'A',
                    v: 'Date'
                }, {
                    k: 'B',
                    v: !isEmpty(invoice_format_min) ? invoice_format_min + " - " + invoice_format_max : currentDate
                }]);

                sheet.childNodes[0].childNodes[1].innerHTML = r2 + r3 + r4 + sheet.childNodes[0].childNodes[1].innerHTML;
            }
        }]

    });

    $("#excel_export_all").on("click", function() {
        table.button("#dt_excel_export_all").trigger();

    });

    function setPaymentDetailsData(code) {

        invoicePaymentDetailsDetailTable = tableElement.DataTable({
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
                "data": "paymentVoucherNo",

            }, {
                targets: 1,
                "data": "sApprovedDate",

            }, {
                targets: 2,
                "data": "bankName"
            }, {
                targets: 3,
                "data": "amount",
                "render": function(data, type, row) {
                    data = data == null ? '' : data;
                    if (type === 'display') {
                        return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
                    }
                }
            }, {
                targets: 4,
                "visible": true,
                "data": "invoiceNo",
                "width": "150px",
                "render": function(data, type, row) {
                    var html = '';
                    html += '<a href="' + myContextPath + '/get/' + row.bankStatementAttachmentDiskFilename + '?path=' + row.attachmentDirectory + '&from=upload&invoiceNo=' + row.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-eye"></i></a>';
                    return html;
                }
            }],
            "drawCallback": function(settings, json) {
                $('#table-detail-invoice').find('span.autonumber').autoNumeric('init')
            }

        })

    }

    //Date Range
    var invoice_min;
    var invoice_max;
    $('#table-filter-invoice-date').daterangepicker({
        autoUpdateInput: false,
        showDropdowns: true,
    }).on("apply.daterangepicker", function(e, picker) {
        invoice_min = picker.startDate;
        invoice_max = picker.endDate;
        picker.element.val(invoice_min.format('DD-MM-YYYY') + ' - ' + invoice_max.format('DD-MM-YYYY'));
        invoice_min = invoice_min._d.getTime();
        invoice_max = invoice_max._d.getTime();
        invoice_format_min = picker.startDate.format('DD-MM-YYYY');
        invoice_format_max = picker.endDate.format('DD-MM-YYYY');
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.draw();
    });
    $('#invoiceDate-form-group').on('click', '.clear-date', function() {
        invoice_min = '';
        invoice_max = '';
        table.draw();
        $('#table-filter-invoice-date').val('');
        $(this).remove();

    });

    //invoice payments Detail Modal
    var invoicePaymentsDetailsEle = $('#modal-invoice-payments')
    var invoicePaymentsDetailsModalBody = invoicePaymentsDetailsEle.find('#invoicePaymentDetails');
    invoicePaymentsDetailsEle.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var rowData = table.row($(event.relatedTarget).closest('tr')).data();
        invoicePaymentsDetailsModalBody.find('input[name="invoiceNo"]').val(rowData.id)
        setPaymentDetailsData(rowData.id);
        invoicePaymentsDetailsModalBody.find('input[name="rowData"]').val(JSON.stringify(rowData));
        invoicePaymentsDetailsEle.find('span.invoiceNo').html(rowData.invoiceNo)
        invoicePaymentsDetailsEle.find('span.supplierName').html(rowData.supplierName + '/' + rowData.auctionHouseName)
    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
        $('#invoicePaymentDetails').find('table').dataTable().fnDestroy();
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
    })
    table.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            table.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            table.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input.selectBox').prop('checked', false);

            });
        } else {
            table.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            table.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input.selectBox').prop('checked', true);

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
    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\./gi, "").replace(/¥/g, "");
    }
    ;// Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    table.on('click', 'td.details-control', function() {
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
            var detailsElement = format(row.data());
            row.child(detailsElement).show();
            detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
            tr.addClass('shown');
        }
    });

    var filterSupplierName = $('#auctionCompany').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        if (oSettings.sTableId == 'table-auction-payment') {

            //Supplier filter
            if (typeof filterSupplierName != 'undefined' && filterSupplierName.length != '') {
                if (aData[8].length == 0 || aData[8] != filterSupplierName) {
                    return false;
                }
            }

            if (typeof invoice_min != 'undefined' && invoice_min.length != '') {
                if (aData[1].length == 0) {
                    return false;
                }
                if (typeof aData._date == 'undefined') {
                    aData._date = moment(aData[1], 'DD-MM-YYYY')._d.getTime();
                }
                if (invoice_min && !isNaN(invoice_min)) {
                    if (aData._date < invoice_min) {
                        return false;
                    }
                }
                if (invoice_max && !isNaN(invoice_max)) {
                    if (aData._date > invoice_max) {
                        return false;
                    }
                }
                return true;

            }

            return true;
        }else{
            return true;
        }
    });

    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

    });

});

//Purchase Format
function format(rowData) {
    var element = $('#clone-container>#invoice-details>.clone-element').clone();
    $(element).find('input[name="paymentinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    let purchaseCostTotal = 0
      , purchaseCostTaxTotal = 0
      , commisionTotal = 0
      , commisionTaxTotal = 0
      , roadTaxTotal = 0
      , recycleTotal = 0
      , otherChargesTotal = 0
      , otherChargesTaxTotal = 0;
    for (var i = 0; i < rowData.approvePaymentItems.length; i++) {
        var row = $(rowClone).clone();
        if (rowData.approvePaymentItems[i].type == 'REAUCTION') {
            row.css('color', 'red');
        }
        purchaseCostTotal += Number(ifNotValid(rowData.approvePaymentItems[i].purchaseCost, 0));
        purchaseCostTaxTotal += Number(ifNotValid(rowData.approvePaymentItems[i].purchaseCostTaxAmount, 0));
        commisionTotal += Number(ifNotValid(rowData.approvePaymentItems[i].commision, 0));
        commisionTaxTotal += Number(ifNotValid(rowData.approvePaymentItems[i].commisionTaxAmount, 0));
        roadTaxTotal += Number(ifNotValid(rowData.approvePaymentItems[i].roadTax, 0));
        recycleTotal += Number(ifNotValid(rowData.approvePaymentItems[i].recycle, 0));
        otherChargesTotal += Number(ifNotValid(rowData.approvePaymentItems[i].otherCharges, 0));
        otherChargesTaxTotal += Number(ifNotValid(rowData.approvePaymentItems[i].othersCostTaxAmount, 0));

        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.chassisNo').html(ifNotValid(rowData.approvePaymentItems[i].chassisNo, ''));
        $(row).find('td.type').html(ifNotValid(rowData.approvePaymentItems[i].type, ''));
        $(row).find('td.purchaseCost span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].purchaseCost, ''));
        $(row).find('td.purchaseCostTax span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].purchaseCostTaxAmount, ''));
        $(row).find('td.commision span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].commision, ''));
        $(row).find('td.commisionTax span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].commisionTaxAmount, ''));
        $(row).find('td.roadTax span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].roadTax, ''));
        $(row).find('td.recycle span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].recycle, ''));
        $(row).find('td.otherCharges span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].otherCharges, ''));
        $(row).find('td.otherChargesTax span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].othersCostTaxAmount, ''));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);
    }
    let footer = $(element).find('table>tfoot')

    $(footer).find('td.purchaseCost span.autonumber').html(purchaseCostTotal);
    $(footer).find('td.purchaseCostTax span.autonumber').html(purchaseCostTaxTotal);
    $(footer).find('td.commision span.autonumber').html(commisionTotal);
    $(footer).find('td.commisionTax span.autonumber').html(commisionTaxTotal);
    $(footer).find('td.roadTax span.autonumber').html(roadTaxTotal);
    $(footer).find('td.recycle span.autonumber').html(recycleTotal);
    $(footer).find('td.otherCharges span.autonumber').html(otherChargesTotal);
    $(footer).find('td.otherChargesTax span.autonumber').html(otherChargesTaxTotal);

    return element;

}
function setPaymentBookingDashboardStatus(data) {
    $('#count-auction').html(data.auctionFreezed);
    $('#count-transport').html(data.transportFreezed);
    $('#count-freight').html(data.freightFreezed);
    $('#count-others').html(data.othersFreezed);
    $('#count-storage').html(data.storageFreezed);

}
