var supplierJson, remittersJson, suppliersJson, transportersJson, forwardersJson, generalSuppliersJson, insCompJson, invoiceTypeFlag;
$(function() {
    $.getJSON(myContextPath + "/data/accounts/freeze/payment-booking-count", function(data) {
        setPaymentBookingDashboardStatus(data.data)
    });
    // Select Forwarder
//     $.getJSON(myContextPath + "/data/forwarders.json", function(data) {
//         $('#remitter').select2({
//             allowClear: true,
//             width: '100%',
//             data: $.map(data, function(item) {
//                 return {
//                     id: item.code,
//                     text: item.name
//                 };
//             })
//         }).val('').trigger("change");
//         $('#forwarderFilter').select2({
//             allowClear: true,
//             width: '100%',
//             data: $.map(data, function(item) {
//                 return {
//                     id: item.code,
//                     text: item.name
//                 };
//             })
//         }).val('').trigger("change");
//     })

    $.getJSON(myContextPath + "/data/forwarders.json", function(data) {
        forwardersJson = data;

    })

    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
        suppliersJson = data;
        $('#forwarderFilter').select2({
            allowClear: true,
            width: '100%',
            placeholder: 'Select Remitter',
            data: $.map(suppliersJson, function(item) {
                return {
                    id: item.supplierCode,
                    text: item.company
                };
            })
        })
    })
    $.getJSON(myContextPath + "/data/transporters.json", function(data) {
        transportersJson = data;

    });
    $.getJSON(myContextPath + "/data/general/suppliers.json", function(data) {
        generalSuppliersJson = data;

    })
    $.getJSON(myContextPath + "/data/inspectioncompanies.json", function(data) {
        insCompJson = data;
    })

    $('#invoiceTypeFilter').select2({
        width: '100%',
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true,
    }).change(function() {
        invoiceTypeFlag = $(this).val();
        remittersJson = (invoiceTypeFlag == 0) ? suppliersJson : (invoiceTypeFlag == 1) ? transportersJson : (invoiceTypeFlag == 2) ? forwardersJson : (invoiceTypeFlag == 4) ? generalSuppliersJson : (invoiceTypeFlag == 5) ? insCompJson : forwardersJson;
        $('#forwarderFilter').empty().select2({
            allowClear: true,
            width: '100%',
            placeholder: 'Select Remitter',
            data: $.map(remittersJson, function(item) {
                return {
                    id: (invoiceTypeFlag == 0) ? item.supplierCode : item.code,
                    text: (invoiceTypeFlag == 0) ? item.company : item.name
                };
            })
        }).val('').trigger('change')

    })

    $('#date-form-group').on('click', '.clear-date', function() {
        due_date = '';
        $('#table-filter-due-date').val('');
        table.search(due_date).draw();
        $(this).remove();

    })
    $('#paymentDate-form-group').on('click', '.clear-date', function() {
        due_date = '';
        $('#table-filter-payment-date').val('');
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
            url: myContextPath + "/accounts/storagePhotos-payment/freezed",

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
            orderable: false,
            "searchable": false,
            className: 'select-checkbox',
            "name": "id",
            "data": "id",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    // return '<input class="selectBox selRow" name="selRow" id="selRow_' + data +
                    // '" type="checkbox" data-stockNo="' + row.stockNo + '" value="' + data + '">';
                    return '<input class="selectBox" name="selRow" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">';
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
            "data": "forwarderName",
        }, {
            targets: 4,
            "className": "details-control",
            "data": "dueDate"
        }, {
            targets: 5,
            "className": "details-control dt-right",
            "data": "totalAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 6,
            "data": "refAttachmentDiskFilename",

            "render": function(data, type, row) {
                var html = '';
                if (row.invoiceUpload == 1) {
                    html += '<a href="' + myContextPath + '/get/' + row.invoiceAttachmentDiskFilename + '?path=' + row.attachmentDirectory + '&from=upload&invoiceNo=' + row.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs"> <i class="fa fa-fw fa-file-pdf-o"></i>View Invoice</a>';
                }
                return html;
            }

        }, {
            targets: 7,
            "className": "details-control",
            "data": "remitter",
            "visible": false
        }],

        "drawCallback": function(settings, json) {
            tableEle.find('input.autonumber,span.autonumber').autoNumeric('init')

        }

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
    // DataTable Filter
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        // date filter
        if (typeof forwarderFilter != 'undefined' && forwarderFilter.length != '') {
            if (aData[7].length == 0 || aData[7] != forwarderFilter) {
                return false;
            }
        }
        return true;
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

    // Select Draw change filter
    var forwarderFilter;
    $('select[name="forwarderFilter"]').on('change', function() {
        forwarderFilter = $(this).find('option:selected').val();
        table.draw();
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
    var element = $('#clone-container>#invoice-details>.clone-element>.order-item-container').clone();
    $(element).find('input[name="paymentinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    let amountTotal = 0;
    var cloningElement = $('#table-cloning-element').find('table').clone();
    $(element).append(cloningElement);
    for (var i = 0; i < rowData.metaData.length; i++) {
        $('<th class="align-center">' + rowData.metaData[i].title + '</th>').appendTo($(element).find('tr.header'))
        $('<td class="align-center ' + rowData.metaData[i].attribute + '"><span class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>').appendTo($(element).find('tr.clone-row'))

    }
    $('<th class="align-center">Remarks</th>').appendTo($(element).find('tr.header'))
    $('<td class="align-center remarks"></td>').appendTo($(element).find('tr.clone-row'))
    $('<th class="align-center">Total</th>').appendTo($(element).find('tr.header'))
    $('<td class="align-center total"><span class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>').appendTo($(element).find('tr.clone-row'))
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.approvePaymentItems.length; i++) {
        let totalRowAmount = 0;
        var columnSpan = 3;
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.chassisNo').html(ifNotValid(rowData.approvePaymentItems[i].chassisNo, ''));
        for (var j = 0; j < rowData.metaData.length; j++) {
            $(row).find('td.' + rowData.metaData[j].attribute + ' ' + 'span.autonumber').autoNumeric('init').autoNumeric('set', ifNotValid(rowData.approvePaymentItems[i].chargesList[rowData.metaData[j].attribute + 'TotalAmount'], '')).autoNumeric('update', {
                aSign: rowData.currencySymbol + ' ',
                mDec: (rowData.currency == 2) ? 2 : 0
            });
            totalRowAmount += Number(rowData.approvePaymentItems[i].chargesList[rowData.metaData[j].attribute + 'TotalAmount']);
            columnSpan += 1;
        }
        $(element).find('table>tfoot>tr>td.footerColumn').attr('colspan', columnSpan);
        $(row).find('td.remarks').html(ifNotValid(rowData.approvePaymentItems[i].remarks, ''));
        $(row).find('td.total span.autonumber').autoNumeric('init').autoNumeric('set', ifNotValid(totalRowAmount, 0)).autoNumeric('update', {
            aSign: rowData.currencySymbol + ' ',
            mDec: (rowData.currency == 2) ? 2 : 0
        });
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);

    }
    $(element).find('table>tfoot td.amount>span').autoNumeric('init').autoNumeric('set', rowData.totalAmount).autoNumeric('update', {
        aSign: rowData.currencySymbol + ' ',
        mDec: (rowData.currency == 2) ? 2 : 0
    });

    return element;

}

function setPaymentBookingDashboardStatus(data) {
    $('#count-auction').html(data.auctionFreezed);
    $('#count-transport').html(data.transportFreezed);
    $('#count-freight').html(data.freightFreezed);
    $('#count-others').html(data.othersFreezed);
    $('#count-storage').html(data.storageFreezed);

}
