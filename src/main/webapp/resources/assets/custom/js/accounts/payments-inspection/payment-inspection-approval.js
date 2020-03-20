var table;
var invoicePaymentDetails = $('#invoicePaymentDetails');
var invoicePaymentDetailsDetailTable;
var tableEle = invoicePaymentDetails.find('table')
$(function() {

    var inspectionCompany;
    $.getJSON(myContextPath + "/data/inspectioncompanies.json", function(data) {
        let inspectionCompanyJson = data;
        $('#table-filter-inspection').select2({
            allowClear: true,
            width: '100%',
            data: $.map(inspectionCompanyJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).on('change', function() {
            inspectionCompany = $(this).val();
            table.draw();
        })

    });

    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    })

    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    }
    ;// Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    var tableEle = $('#table-inspection-invoice');
    table = tableEle.DataTable({
         "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": true,
        "ajax": {
            url: myContextPath + "/accounts/payment/inspection-payment-approval-datasource",

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
            "data": "invoiceDate"
        }, {
            targets: 1,
            "className": "details-control",
            "data": "invoiceNo"
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
            "data": "refNo"
        }, {
            targets: 4,
            "className": "details-control",
            "data": "inspectionCompany",
        }, {
            targets: 5,
            "className": "details-control dt-right",
            "data": "invoiceTotal",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 6,
            "visible": true,
            "data": "invoiceNo",
            "width": "150px",
            "render": function(data, type, row) {
                var html = '';
                // html = '<button type="button" name="fileupload" class="btn btn-success ml-5 btn-xs" title="bank statement upload" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-statement-upload"><i class="fa fa-fw fa-cloud-upload"></i></button>'
                html += '<a href="' + myContextPath + '/get/' + row.invoiceAttachmentDiskFilename + '?path=' + row.attachmentDirectory + '&from=upload&invoiceNo=' + row.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs" name="preview_link"><i class="fa fa-fw fa-eye"></i></a>';
                html += '<button type="button" id="view payments" class="btn btn-warning ml-5 btn-xs" data-target="#modal-invoice-payments" title="View payments" data-backdrop="static" data-keyboard="false" data-toggle="modal"><i class="fa fa-external-link"></i></button>'
                html += '<button type="button" id="freeze" class="btn btn-primary ml-5 btn-xs freeze" title="Freeze"><i class="fa fa-fw fa-check"></i></button>'
                html += '<button type="button" id="cancel" class="btn btn-danger ml-5 btn-xs" title="Cancel" data-target="#modal-payment-cancel" data-backdrop="static" data-keyboard="false" data-toggle="modal"><i class="fa fa-fw fa-close"></i></button>'

                return html;
            }
        }, {
            targets: 7,
            "visible": false,
            "data": "inspectionCompanyId",
            "className": "details-control",
        }, {
            targets: 8,
            "visible": false,
            "data": "paymentStatus",
            "className": "details-control",
        }],

        "drawCallback": function(settings, json) {
            tableEle.find('input.autonumber,span.autonumber').autoNumeric('init')

        },
        "fnRowCallback": function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            if (aData.paymentStatus == "6") {
                $('td', nRow).css('background-color', '#ef503d');
            }
        }

    });

    var invoice_date;
    $('#table-filter-invoice-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: false
    }).on("change", function(e, picker) {
        invoice_date = $(this).val();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.draw();
    });
    $('.input-group').on('click', '.clear-date', function() {
        invoice_date = '';
        $('#table-filter-invoice-date').val('');
        $(this).remove();
        table.draw();

    });

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {

        //Supplier filter
        if (typeof inspectionCompany != 'undefined' && inspectionCompany.length != '') {
            if (aData[7].length == 0 || aData[7] != inspectionCompany) {
                return false;
            }
        }
        //Due Date Filter
        if (typeof invoice_date != 'undefined' && invoice_date.length != '') {
            if (aData[0].length == 0 || aData[0] != invoice_date) {
                return false;
            }
        }
        return true;
    });

    //expand details
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
                    tr.removeClass('shown');
                }

            })
            tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
            var detailsElement = inspectionFormat(row.data());
            row.child(detailsElement).show();
            detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
            tr.addClass('shown');
        }
    });

    //Freeze Payment
    table.on('click', '.freeze', function(event) {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        var rowData = table.row($(event.currentTarget).closest('tr')).data();
        var data = {}
        data.invoiceNo = rowData.id;
        data.paymentStatus = rowData.paymentStatus;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/payment/freeze-inspection-payment",
            contentType: "application/json",
            async: false,
            success: function(data) {

                table.ajax.reload();
            }
        });
    });

    //Cancel Payment
    let payment_cancel_modal = $('#modal-payment-cancel');
    $(payment_cancel_modal).on('show.bs.modal', function(event) {
        var rowData = table.row($(event.relatedTarget).closest('tr')).data();
        $(this).find('input[name="invoiceNo"]').val(rowData.id)
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
            url: myContextPath + "/accounts/payment/cancel-inspection-payment",
            contentType: "application/json",
            async: false,
            success: function(data) {
                table.ajax.reload();
            }
        });
        return result;
    })

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
    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
        $('#invoicePaymentDetails').find('table').dataTable().fnDestroy();
    });
});

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
            url: myContextPath + "/accounts/payment/inspection/statement/upload?code=" + code,
            contentType: false,
            async: false,
            success: function(data) {
                result = data;
            }
        });
        return result;
    }

})

//Inspection Format
function inspectionFormat(rowData) {
    var element = $('#inspection-clone-container>#payment-inspection-approve-details>.clone-element').clone();
    $(element).find('input[name="invoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    let taxTotal = 0;
    let amountTotal = 0;
    let totalTaxIncluded = 0;
    for (var i = 0; i < rowData.items.length; i++) {
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.stockNo').html(ifNotValid(rowData.items[i].stockNo, ''));
        $(row).find('td.chassisNo').html(ifNotValid(rowData.items[i].chassisNo, ''));
        $(row).find('td.maker').html(ifNotValid(rowData.items[i].maker, ''));
        $(row).find('td.model').html(ifNotValid(rowData.items[i].model, ''));
        $(row).find('td.amount>span').html(ifNotValid(rowData.items[i].amount, ''));
        $(row).find('td.tax>span').html(ifNotValid(rowData.items[i].taxAmount, ''));
        $(row).find('td.total>span').html(ifNotValid(rowData.items[i].totalTaxIncluded, ''));
        taxTotal += Number(ifNotValid(rowData.items[i].taxAmount, 0));
        amountTotal += Number(ifNotValid(rowData.items[i].amount, 0));
        totalTaxIncluded += Number(ifNotValid(rowData.items[i].totalTaxIncluded, 0));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);
    }
    $(element).find('table>tfoot td.amount>span').html(amountTotal);
    $(element).find('table>tfoot td.tax>span').html(taxTotal);
    $(element).find('table>tfoot td.total>span').html(totalTaxIncluded);
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
