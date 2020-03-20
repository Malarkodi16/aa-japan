var table;
var invoicePaymentDetails = $('#invoicePaymentDetails');
var invoicePaymentDetailsDetailTable;
var tableEle = invoicePaymentDetails.find('table')
$(function() {

    $.getJSON(myContextPath + "/data/accounts/payment/approval/payment-booking-count", function(data) {
        setPaymentBookingDashboardStatus(data.data)
    });

    var supplierFilter;
    $.getJSON(myContextPath + "/data/transporters.json", function(data) {
        transporterJson = data;
        $('.transporter').select2({
            allowClear: true,
            width: '100%',
            data: $.map(transporterJson, function(item) {
                return {
                    id: item.code,
                    text: item.name,
                    data: item
                };
            })
        }).on('change', function() {
            supplierFilter = $(this).val();
            table.draw();
        })
    });

    var tableEle = $('#table-transport-invoice-completed');
    table = tableEle.DataTable({
         "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": true,
        "ajax": {
            url: myContextPath + "/accounts/payment/transport-payment-completed-datasource",

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
            "searchable": false,
            "className": "select-checkbox",
            "name": "id",
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
            "data": "refNo"
        }, {
            targets: 3,
            "className": "details-control",
            "data": "invoiceRefNo"
        },{
            targets: 4,
            "className": "details-control",
            "data": "paymentVoucherNo",
            "render": function(data, type, row) {
                return !isEmpty(data) ? data.substring(0, data.length - 3) : "";
            }
        }, {
            targets: 5,
            "className": "details-control",
            "data": "transporterName",
        }, {
            targets: 6,
            "className": "details-control dt-right",
            "data": "invoiceTotal",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 7,
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
            targets: 8,
            "visible": false,
            "data": "transporterId",
            "className": "details-control",
        }, {
            targets: 9,
            "visible": false,
            "data": "paymentApprove",
            "className": "details-control",
        }],

        "drawCallback": function(settings, json) {
            tableEle.find('input.autonumber,span.autonumber').autoNumeric('init')

        },
        "fnRowCallback": function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            if (aData.paymentApprove == "3") {
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

    //Expand Table
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

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {

        //Supplier filter
        if (typeof supplierFilter != 'undefined' && supplierFilter.length != '') {
            if (aData[8].length == 0 || aData[8] != supplierFilter) {
                return false;
            }
        }
        //Due Date Filter
        if (typeof dueDateFilter != 'undefined' && dueDateFilter.length != '') {
            if (aData[1].length == 0 || aData[1] != dueDateFilter) {
                return false;
            }
        }

        //Payment Date Filter
        //         if (typeof paymentDateFilter != 'undefined' && paymentDateFilter.length != '') {
        //             if (aData[1].length == 0 || aData[1] != paymentDateFilter) {
        //                 return false;
        //             }
        //         }
        return true;
    });

    //Cancel Payment
    let payment_cancel_modal = $('#modal-payment-cancel');
    $(payment_cancel_modal).on('show.bs.modal', function(event) {
        var rowData = table.row($(event.relatedTarget).closest('tr')).data();
        console.log(rowData);
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
            url: myContextPath + "/accounts/payment/cancel-transport-payment",
            contentType: "application/json",
            async: false,
            success: function(data) {
                table.ajax.reload();
            }
        });
        return result;
    })

    //Freeze Payment
    $('#table-transport-invoice-completed').on('click', '.freeze', function(event) {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        var rowData = table.row($(event.currentTarget).closest('tr')).data();
        var data = {}
        var invoiceNo = []
        invoiceNo.push(rowData.id)
        data.invoiceNo = invoiceNo;
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
            url: myContextPath + "/accounts/payment/freeze-transport-payment",
            contentType: "application/json",
            async: false,
            success: function(data) {

                table.ajax.reload();
            }
        });

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
        $('img[name="image_preview"]').attr('src', myContextPath + '/downloadFile/' + filename + '?path=' + data.attachmentDirectory + '&from=upload');
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
            url: myContextPath + "/accounts/approve/payment/transport/statement/upload?code=" + code,
            contentType: false,
            async: false,
            success: function(data) {
                result = data;
            }
        });
        return result;
    }

})
function format(rowData) {
    var element = $('#transport-clone-container>#payment-transport-approve-details>.clone-element').clone();
    $(element).find('input[name="transportinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    let taxTotal = 0;
    let amountTotal = 0;
    for (var i = 0; i < rowData.items.length; i++) {
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.stockNo').html(ifNotValid(rowData.items[i].stockNo, ''));
        $(row).find('td.chassisNo').html(ifNotValid(rowData.items[i].chassisNo, ''));
        $(row).find('td.maker').html(ifNotValid(rowData.items[i].maker, ''));
        $(row).find('td.model').html(ifNotValid(rowData.items[i].model, ''));
        $(row).find('td.amount>span').html(ifNotValid(rowData.items[i].amount, ''));
        $(row).find('td.tax>span').html(ifNotValid(rowData.items[i].taxAmount, ''));
        taxTotal += Number(ifNotValid(rowData.items[i].taxAmount, 0));
        amountTotal += Number(ifNotValid(rowData.items[i].amount, 0));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);
    }
    $(element).find('table>tfoot td.amount>span').html(amountTotal);
    $(element).find('table>tfoot td.tax>span').html(taxTotal);
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
