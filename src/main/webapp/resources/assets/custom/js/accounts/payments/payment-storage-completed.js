var table, remittersJson, suppliersJson, transportersJson, forwardersJson, generalSuppliersJson, insCompJson, invoiceTypeFlag;
var invoicePaymentDetails = $('#invoicePaymentDetails');
var invoicePaymentDetailsDetailTable;
var tableEle = invoicePaymentDetails.find('table')
$(function() {
    $.getJSON(myContextPath + "/data/accounts/payment/approval/payment-booking-count", function(data) {
        setPaymentBookingDashboardStatus(data.data)
    });
//     var forwarderFilter;
//     $.getJSON(myContextPath + "/data/forwarders.json", function(data) {
//         forwarderJson = data;
//         $('.forwarder').select2({
//             allowClear: true,
//             width: '100%',
//             data: $.map(forwarderJson, function(item) {
//                 return {
//                     id: item.code,
//                     text: item.name,
//                     data: item
//                 };
//             })
//         }).on('change', function() {
//             forwarderFilter = $(this).val();
//             table.draw();
//         })
//     });

     // Select Forwarder
    $.getJSON(myContextPath + "/data/forwarders.json", function(data) {
        forwardersJson = data;

    })

    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
        suppliersJson = data;
        $('#forwarder').select2({
            allowClear: true,
            width: '100%',
            placeholder: 'Select Forwarder',
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
        $('#forwarder').empty().select2({
            allowClear: true,
            width: '100%',
            placeholder: 'Select Forwarder',
            data: $.map(remittersJson, function(item) {
                return {
                    id: (invoiceTypeFlag == 0) ? item.supplierCode : item.code,
                    text: (invoiceTypeFlag == 0) ? item.company : item.name
                };
            })
        }).val('').trigger('change')

    })

    var tableEle = $('#table-storage-photos-completed');
    table = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": true,
        "ajax": {
            url: myContextPath + "/accounts/payment/payment-storage-and-photos-completed",

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
            orderable: false,
            className: 'select-checkbox',
            "data": "id",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" name="selRow" type="checkbox" data-stockno="' + row.invoiceNo + '" value="' + data + '">';
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
            "data": "paymentVoucherNo",
            "render": function(data, type, row) {
                return !isEmpty(data) ? data.substring(0, data.length - 3) : "";
            }

        }, {
            targets: 3,
            "className": "details-control",
            "data": "invoiceNo"

        }, {
            targets: 4,
            "className": "details-control",
            "data": "refNo"

        }, {
            targets: 5,
            "className": "details-control",
            "data": "forwarderName"
        }, {
            targets: 6,
            "data": "dueDate",
            "className": "details-control",

        }, {

            targets: 7,
            "className": "details-control dt-right",
            "type": "num-fmt",
            "data": "totalAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {

            targets: 8,
            "className": "dt-right",
            "type": "num-fmt",
            "data": "invoiceBalanceAmount",
            "render": function(data, type, row) {
                return '<span class="autonumber"data-a-sign="¥ " data-m-dec="0">' + ifNotValid(row.invoiceBalanceAmount, 0) + '</span>';
            }
        }, {
            targets: 9,
            "visible": true,
            "data": "invoiceNo",
            "width": "200px",
            "render": function(data, type, row) {
                var html = '';
                // html = '<button type="button" id="fileUpload" class="btn btn-success ml-5 btn-xs fileUpload" title="Statement Upload" data-target="#modal-bankStatement-upload" data-keyboard="false" data-toggle="modal"><i class="fa fa-fw fa-upload"></i></button>'
                if (row.invoiceUpload == 1) {
                    html += '<a href="' + myContextPath + '/get/' + row.invoiceAttachmentDiskFilename + '?path=' + row.attachmentDirectory + '&from=upload&invoiceNo=' + row.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs" name="preview_link"><i class="fa fa-fw fa-eye"></i></a>';
                }
                html += '<button type="button" id="view payments" class="btn btn-warning ml-5 btn-xs" data-target="#modal-invoice-payments" title="View payments" data-backdrop="static" data-keyboard="false" data-toggle="modal"><i class="fa fa-external-link"></i></button>'
                html += '<button type="button" id="freeze" class="btn btn-primary ml-5 btn-xs freeze" title="Freeze"><i class="fa fa-fw fa-check"></i></button>'
                html += '<button type="button" id="cancel" class="btn btn-danger ml-5 btn-xs" title="Cancel" data-target="#modal-payment-cancel" data-backdrop="static" data-keyboard="false" data-toggle="modal"><i class="fa fa-fw fa-close"></i></button>'
                return '<div>' + html + '</div>';
            }
        }, {
            targets: 10,
            "visible": false,
            "data": "remitter",
            "className": "details-control",
        }, {
            targets: 11,
            "visible": false,
            "data": "approvePaymentStatus",
            "className": "details-control",
        }],

        "drawCallback": function(settings, json) {
            tableEle.find('input.autonumber,span.autonumber').autoNumeric('init')

        },
        "fnRowCallback": function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            if (aData.approvePaymentStatus == "3") {
                $('td', nRow).css('background-color', '#ef503d');
            }
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
    // CheckBox Select
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

    })
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
    var dueDateFilter;
    $('.dueDateDatepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: true
    }).on("change", function(e, picker) {
        dueDateFilter = $(this).val();
        table.draw();
    });

    var paymentDateFilter;
    $('.paymentDateDatepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: true
    }).on("change", function(e, picker) {
        paymentDateFilter = $(this).val();
        table.draw();
    });

     // Select Draw change filter
    var forwarderFilter;
    $('select[name="forwarder"]').on('change', function() {
        forwarderFilter = $(this).find('option:selected').val();
        table.draw();
    });

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {

        //Supplier filter
        if (typeof forwarderFilter != 'undefined' && forwarderFilter.length != '') {
            if (aData[10].length == 0 || aData[10] != forwarderFilter) {
                return false;
            }
        }
        //Due Date Filter
        if (typeof dueDateFilter != 'undefined' && dueDateFilter.length != '') {
            if (aData[5].length == 0 || aData[5] != dueDateFilter) {
                return false;
            }
        }

        //Payment Date Filter
        if (typeof paymentDateFilter != 'undefined' && paymentDateFilter.length != '') {
            if (aData[1].length == 0 || aData[1] != paymentDateFilter) {
                return false;
            }
        }
        return true;
    });

    //Cancel Modal

    let payment_cancel_modal = $('#modal-payment-cancel');
    $(payment_cancel_modal).on('show.bs.modal', function(event) {
        var rowData = table.row($(event.relatedTarget).closest('tr')).data();
        console.log(rowData);
        $(this).find('input[name="invoiceNo"]').val(rowData.invoiceNo)
    }).on('hide.bs.modal', function() {
        $(this).find('input,textarea').val('');
    }).on('click', '#cancel-payment', function() {
        if (!$('#payment-cancel-form').valid()) {
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        var paymentData = getFormData($(payment_cancel_modal).find('input,textarea'));
        var invoiceNo = [];
        var data = {};
        invoiceNo.push(paymentData.invoiceNo);
        data["invoiceNo"] = invoiceNo;
        data["cancelledRemarks"] = paymentData.cancelledRemarks;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/payment/cancel-storage-payment",
            contentType: "application/json",
            async: false,
            success: function(data) {
                result = data;
                table.ajax.reload();
            }
        });
        return result;
    })

    $('#table-storage-photos-completed').on('click', '.freeze', function(event) {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        var rowData = table.row($(event.currentTarget).closest('tr')).data();
        var data = {}
        var invoiceNo = []
        invoiceNo.push(rowData.invoiceNo)
        data.invoiceNo = invoiceNo;
        data.paymentApprove = rowData.approvePaymentStatus;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/payment/freeze-storage-payment",
            contentType: "application/json",
            async: false,
            success: function(data) {
                result = data;
                table.ajax.reload();
            }
        });
        return result;
    })

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

    let modal_bank_image_preview = $("#modal-bank-invoice-preview");
    modal_bank_image_preview.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(e.relatedTarget);
        var data = invoicePaymentDetailsDetailTable.row($(targetElement).closest('tr')).data();
        let filename = data.bankStatementAttachmentDiskFilename;
        if (!isEmpty(filename)) {
            $('img[name="image_preview"]').attr('src', myContextPath + '/downloadFile/' + filename + '?path=' + data.attachmentDirectory + '&from=upload');
        } else {
            $('img[name="image_preview"]').attr('src', myContextPath + '/resources/assets/images/no-image-icon.png');
        }
    })

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
})

// invoice upload
$(function() {
    // upload invoice image
    let invoice_upload_form = $('#modal-statement-upload form#form-file-upload');
    //validation
    invoice_upload_form.validate({
        highlight: function(element) {
            $(element).parent().parent().addClass("has-error");
        },
        unhighlight: function(element) {
            $(element).parent().parent().removeClass("has-error");
        },
        errorPlacement: function(error, element) {
            var isFound = false;
            var itr = 0;
            while (!isFound && itr < 5) {
                var e = $(element).parent();
                if (e.find('.help-block').length > 0) {
                    isFound = true;
                }
                element = e;
                itr++;
            }
            if (isFound) {
                $(element).find('.help-block').text(error.text());
            }

        },
        success: function(element) {
            var isFound = false;
            var itr = 0;
            while (!isFound && itr < 5) {
                var e = $(element).parent();
                if (e.find('.help-block').length > 0) {
                    isFound = true;
                }
                element = e;
                itr++;
            }
            if (isFound) {
                $(element).find('.help-block').hide();
            }

        },
        rules: {
            "invoiceFile": 'required'
        }
    });

    //file uplaod
    $('#modal-statement-upload').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(e.relatedTarget);
        var data = invoicePaymentDetailsDetailTable.row($(targetElement).closest('tr')).data();
        $(this).find('input[name="code"]').val(data.code);
    }).on('hidden.bs.modal', function() {
        $(this).find('input').val('');
    }).on('click', '#upload', function() {
        if (!invoice_upload_form.valid()) {
            return false;
        }
        if (!isValidFileSelected($('#modal-statement-upload input[name="invoiceFile"]'))) {
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }

        var data = new FormData();
        var invoiceFileEle = $('#modal-statement-upload input[name="invoiceFile"]');
        if (invoiceFileEle.prop('files').length > 0) {
            let file = invoiceFileEle.prop('files')[0];
            data.append("invoiceFile", file);
        } else {
            return false;
        }
        let code = $('#modal-statement-upload input[name="code"]').val();
        let response = uploadFile(data, code)
        if (response.status == 'success') {
            $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Invoice uploaded.');
            $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                $("#alert-block").slideUp(500);
            });
            invoicePaymentDetailsDetailTable.ajax.reload();
            $('#modal-statement-upload').modal('toggle');

        }
    })
    var uploadFile = function(data, code) {
        let result;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            processData: false,
            data: data,
            url: myContextPath + "/accounts/payment/storageandphotos/bankStatement/upload?code=" + code,
            contentType: false,
            async: false,
            success: function(data) {
                result = data;
            }
        });
        return result;
    }

})

//Purchase Format
function format(rowData) {
    var element = $('#storage-clone-container>#storage-completed-invoice-details>.clone-element>.order-item-container').clone();
    $(element).find('input[name="paymentinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var amount = 0;
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
            "data": "paymentVoucherNo",

        }, {
            targets: 1,
            "className": "details-control",
            "name": "Date",
            "data": "sApprovedDate",

        }, {
            targets: 2,
            "className": "details-control",
            "name": "Bank",
            "data": "bankName"
        }, {
            targets: 3,
            "className": "details-control",
            "name": "Amount",
            "data": "amount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 4,
            "visible": true,
            "data": "invoiceNo",
            "width": "150px",
            "render": function(data, type, row) {
                var html = '';
                html = '<button type="button" name="fileupload" class="btn btn-success ml-5 btn-xs" title="Statement upload" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-statement-upload"><i class="fa fa-fw fa-cloud-upload"></i></button>'
                //                 html += '<button type="button" id="view" class="btn btn-default ml-5 btn-xs" data-target="#modal-bank-invoice-preview" title="View" data-backdrop="static" data-keyboard="false" data-toggle="modal"><i class="fa fa-fw fa-eye"></i></button>'
                html += '<a href="' + myContextPath + '/get/' + row.bankStatementAttachmentDiskFilename + '?path=' + row.attachmentDirectory + '&from=upload&invoiceNo=' + row.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-eye"></i></a>';
                html += '<button type="button" class="btn btn-warning ml-5 btn-xs" name="btn-cancel-payment"><i class="fa fa-remove"></i></button>'
                return html;
            }
        }],
        "drawCallback": function(settings, json) {
            $('#table-detail-invoice').find('span.autonumber').autoNumeric('init')
        }

    }).on('click', 'button[name="btn-cancel-payment"]', function() {
        if (confirm($.i18n.prop('confirm.delete.payment.transaction'))) {
            let data = invoicePaymentDetailsDetailTable.row($(this).closest('tr')).data();
            let paymentVoucherNo = data.paymentVoucherNo;
            $.ajax({
                beforeSend: function() {
                    $('#spinner').show()
                },
                complete: function() {
                    $('#spinner').hide();
                },
                type: "delete",
                processData: false,
                data: data,
                url: myContextPath + "/accounts/delete/invoice/payment/transaction?paymentVoucherNo=" + paymentVoucherNo,
                contentType: false,
                async: false,
                success: function(data) {
                    table.ajax.reload();
                    invoicePaymentDetailsDetailTable.ajax.reload();

                }
            });
        }
    })
}

function setPaymentBookingDashboardStatus(data) {
    $('#count-auction').html(data.auctionApproval);
    $('#count-transport').html(data.transportApproval);
    $('#count-freight').html(data.freightApproval);
    $('#count-others').html(data.othersApproval);
    $('#count-storage').html(data.storageApproval);
}
