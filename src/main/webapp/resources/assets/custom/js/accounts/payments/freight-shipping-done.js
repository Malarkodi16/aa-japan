var invoicePaymentDetails = $('#invoicePaymentDetails');
var invoicePaymentDetailsDetailTable;
var tableEle = invoicePaymentDetails.find('table')
$(function() {
    $.getJSON(myContextPath + "/data/forwarders.json", function(data) {
        forwarderJson = data;
        $('select#forwarderFilter').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).val('').trigger("change");
    })
    let forwarderFilter='';
    $('select#forwarderFilter').on('change', function() {
        forwarderFilter=$(this).val();
        table_payment_completed_container.draw();
    })
    $.getJSON(myContextPath + "/data/accounts/payment/approval/payment-booking-count", function(data) {
        setPaymentBookingDashboardStatus(data.data)
    });

    var table_payment_completed_container_Ele = $('#table-roro-payment-completed');
    table_payment_completed_container = table_payment_completed_container_Ele.DataTable({
         "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/freight/shiiping/invoice/paymentCompleted",
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            orderable: false,
            className: 'details-control',
            "data": "invoiceNo"

        }, {
            targets: 1,
            "className": "details-control",
            "data": "paymentVoucherNo",
            "render": function(data, type, row) {
                return !isEmpty(data) ? data.substring(0, data.length - 3) : "";
            }

        }, {
            targets: 2,
            "className": "details-control",
            "data": "createdDate"

        }, {
            targets: 3,
            "className": "details-control",
            "data": "invoiceName"
        }, {
            targets: 4,
            "data": "dueDate",
            "className": "details-control",
            "className": "vcenter"
        }, {
            targets: 5,
            "className": "dt-right",
            "type": "num-fmt",
            "data": "totalAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
                }
            }
        }, {
            targets: 6,
            "className": "dt-right",
            "type": "num-fmt",
            "data": "totalAmountUsd",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<span class="autonumber" data-a-sign="$ " data-m-dec="0">' + data + '</span>';
                }
            }
        }, {
            targets: 7,
            "className": "details-control",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                let html = '';
                if (type === 'display') {
                    html += '<a href="' + myContextPath + '/get/' + row.invoiceAttachmentDiskFilename + '?path=' + row.attachmentDirectory + '&from=upload&invoiceNo=' + row.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-eye"></i></a>';
                    html += '<button type="button" id="view payments" class="btn btn-warning ml-5 btn-xs" data-target="#modal-invoice-payments" title="View payments" data-backdrop="static" data-keyboard="false" data-toggle="modal"><i class="fa fa-external-link"></i></button>'
                    html += '<button type="button" id="freeze" class="btn btn-primary ml-5 btn-xs freeze" title="Freeze"> <i class="fa fa-fw fa-check"></i></button>'
                    html += '<button type="button" id="cancel" class="btn btn-danger ml-5 btn-xs" title="Cancel" data-target="#modal-payment-cancel" data-backdrop="static" data-keyboard="false" data-toggle="modal"><i class="fa fa-fw fa-close"></i></button>'
                    return html;
                }
            }
        }, {
            targets: 8,
            "data": "supplierId",
            "visible": false
        }, {
            targets: 9,
            visible: false,
            "data": "paymentApprove",
        }],
        "fnRowCallback": function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            if (aData.paymentApprove == "3") {
                $('td', nRow).css('background-color', '#ef503d');
            }
        },
        "drawCallback": function(settings, json) {
            table_payment_completed_container_Ele.find('span.autonumber').autoNumeric('init')

        }
    });
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {

        //forwarder country filter        
        if (!isEmpty(forwarderFilter)) {
            if (aData[8].length == 0 || aData[8] != forwarderFilter) {
                return false;
            }
        }

        return true;
    })
    /* table on click to append Item Container */

    table_payment_completed_container.on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table_payment_completed_container.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
        } else {
            table_payment_completed_container.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = table_payment_completed_container.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                }

            })
            let rowData = row.data();
            var detailsElement;
            if (rowData.invoiceType == 1) {
                detailsElement = format(rowData)
            } else {
                detailsElement = formatInvoiceContainerDetails(rowData)
            }
            row.child(detailsElement).show();
            detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
            tr.addClass('shown');
        }

    });

    //Freeze Payment
    table_payment_completed_container.on('click', '.freeze', function(event) {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        var row = table_payment_completed_container.row($(event.currentTarget).closest('tr'));
        var rowData = row.data();
        var data = {}
        data.invoiceNo = rowData.invoiceNo;
        data.paymentApprove = rowData.paymentApprove;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/freight/freeze-freight-payment",
            contentType: "application/json",
            async: false,
            success: function(data) {
                table_payment_completed_container.ajax.reload();

            }
        });

    })

    //Cancel Payment
    let payment_cancel_modal = $('#modal-payment-cancel');
    $(payment_cancel_modal).on('show.bs.modal', function(event) {
        var rowData = table_payment_completed_container.row($(event.relatedTarget).closest('tr')).data();
        $(this).find('input[name="invoiceNo"]').val(rowData.invoiceNo)
    }).on('hide.bs.modal', function() {
        $(this).find('input,textarea').val('');
    }).on('click', '#cancel-payment', function() {
        if (!$('#payment-cancel-form').valid()) {
            return false;
        }
        var row = table_payment_completed_container.row($(event.currentTarget).closest('tr'));
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        var paymentData = getFormData($(payment_cancel_modal).find('input,textarea'));
        var data = {};
        data["invoiceNo"] = paymentData.invoiceNo;
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
            url: myContextPath + "/freight/cancel-freight-payment",
            contentType: "application/json",
            async: false,
            success: function(data) {
                if (data.status == "success") {
                    row.data(data.data).invalidate();
                }
            }
        });
        //return result;
    })
    //invoice payments Detail Modal
    var invoicePaymentsDetailsEle = $('#modal-invoice-payments')
    var invoicePaymentsDetailsModalBody = invoicePaymentsDetailsEle.find('#invoicePaymentDetails');
    invoicePaymentsDetailsEle.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var rowData = table_payment_completed_container.row($(event.relatedTarget).closest('tr')).data();
        invoicePaymentsDetailsModalBody.find('input[name="invoiceNo"]').val(rowData.invoiceNo)
        setPaymentDetailsData(rowData.invoiceNo);
    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
        $('#invoicePaymentDetails').find('table').dataTable().fnDestroy();
    });
    // upload invoice image
    let invoice_upload_form = $('#modal-statement-upload form#form-file-upload');
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
            url: myContextPath + "/accounts/approve/payment/shipping/statement/upload?code=" + code,
            contentType: false,
            async: false,
            success: function(data) {
                result = data;
            }
        });
        return result;
    }
});
function format(rowData) {
    var element = $('#clone-container>#roro-container-details>.clone-element').clone();
    $(element).find('input[name="invoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.items.length; i++) {
        var row = $(rowClone).clone();

        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.chassisNo').html(ifNotValid(rowData.items[i].chassisNo, ''));
        $(row).find('td.freightCharge>span').html(ifNotValid(rowData.items[i].freightCharge, 0.0));
        $(row).find('td.freightChargeUsd>span').html(ifNotValid(rowData.items[i].freightChargeUsd, 0.0));
        $(row).find('td.shippingCharge>span').html(ifNotValid(rowData.items[i].shippingCharge, 0.0));
        $(row).find('td.inspectionCharge>span').html(ifNotValid(rowData.items[i].inspectionCharge, 0.0));
        $(row).find('td.radiationCharge>span').html(ifNotValid(rowData.items[i].radiationCharge, 0.0));
        $(row).find('td.otherCharges>span').html(ifNotValid(rowData.items[i].otherCharges, 0.0));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);

    }

    return element;
}
function formatInvoiceContainerDetails(rowData) {
    var element = $('#clone-container>#invoice-details>.clone-element').clone();
    $(element).find('input[name="invoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.invoiceItems.length; i++) {
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.quantity').html(ifNotValid(rowData.invoiceItems[i].quantity, ''));
        $(row).find('td.description').html(ifNotValid(rowData.invoiceItems[i].description, ''));
        $(row).find('td.usd>span').html(ifNotValid(rowData.invoiceItems[i].usd, ''));
        $(row).find('td.zar>span').html(ifNotValid(rowData.invoiceItems[i].zar, ''));
        $(row).find('td.unitPrice>span').html(ifNotValid(rowData.invoiceItems[i].unitPrice, ''));
        $(row).find('td.amount>span').html(ifNotValid(rowData.invoiceItems[i].amount, ''));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);

    }

    return element;
}
function setPaymentBookingDashboardStatus(data) {
    $('#count-auction').html(data.auctionApproval);
    $('#count-transport').html(data.transportApproval);
    $('#count-freight').html(data.freightApproval);
    $('#count-others').html(data.othersApproval);
    $('#count-storage').html(data.storageApproval);
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
            "name": "paymentVoucherNo",
            "data": "paymentVoucherNo"
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
                    table_payment_completed_container.ajax.reload();
                    invoicePaymentDetailsDetailTable.ajax.reload();

                }
            });
        }
    })

}
