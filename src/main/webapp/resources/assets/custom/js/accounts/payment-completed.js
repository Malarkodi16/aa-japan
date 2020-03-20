var supplierJson;
var invoicePaymentDetails = $('#invoicePaymentDetails');
var invoicePaymentDetailsDetailTable;
var tableEle = invoicePaymentDetails.find('table')
var table;
$(function() {
    $.getJSON(myContextPath + "/data/accounts/payment/approval/payment-booking-count", function(data) {
        setPaymentBookingDashboardStatus(data.data)
    });
    var supplierFilter;
    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
        supplierJson = data;
        $('.supplier').select2({
            allowClear: true,
            width: '100%',
            data: $.map(supplierJson, function(item) {
                return {
                    id: item.supplierCode,
                    text: item.company,
                    data: item
                };
            })
        }).on('change', function() {
            supplierFilter = $(this).val();
            table.draw();
        })
    });

    $('.select2-select').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true

    })
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })

    var tableEle = $('#table-payment-completed');
    table = tableEle.DataTable({
         "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": true,
        "bSearchable": true,
        "bFilter": true,
        "ajax": {
            url: myContextPath + "/accounts/auction-data/completed"
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
            "data": "auctionRefNo"

        }, {
            targets: 5,
            "className": "details-control",
            "data": "supplier"
        }, {}, {
            targets: 6,
            "className": "details-control",
            "data": "auctionHouse"
        }, {
            targets: 7,
            "data": "dueDate",
            "className": "details-control",
            "className": "vcenter"
        }, {

            targets: 8,
            "className": "dt-right",
            "type": "num-fmt",
            "data": "totalAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {

            targets: 9,
            "className": "dt-right",
            "type": "num-fmt",
            "data": "invoiceAmountReceived",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + row.invoiceAmountReceived + '</span>';

            }
        }, {

            targets: 10,
            "className": "dt-right",
            "type": "num-fmt",
            "data": "invoiceBalanceAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 11,
            "data": "remarks"
        }, {
            targets: 12,
            orderable: false,
            "visible": true,
            "data": "invoiceNo",
            "width": "150px",
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
                // html = '<button type="button" name="fileupload" class="btn btn-success ml-5 btn-xs" title="Statement upload" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-statementUpload-upload"><i class="fa fa-fw fa-cloud-upload"></i></button>'                
                // html += '<button type="button" id="view" class="btn btn-default ml-5 btn-xs" data-target="#modal-invoice-preview" title="View" data-backdrop="static" data-keyboard="false" data-toggle="modal"><i class="fa fa-fw fa-eye"></i></button>'
                html += '<a href="' + myContextPath + '/get/' + row.invoiceAttachmentDiskFilename + '?path=' + row.attachmentDirectory + '&from=upload&invoiceNo=' + row.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-eye"></i></a>';
                html += '<button type="button" id="view payments" class="btn btn-' + freezeStatus + ' ml-5 btn-xs" data-target="#modal-invoice-payments" title="View payments" data-backdrop="static" data-keyboard="false" data-toggle="modal"><i class="fa fa-external-link"></i></button>'
                html += '<button type="button" id="freeze" class="btn btn-primary ml-5 btn-xs freeze" title="Freeze"><i class="fa fa-fw fa-check"></i></button>'
                html += '<button type="button" id="cancel" class="btn btn-danger ml-5 btn-xs" title="Cancel" data-target="#modal-payment-cancel" data-backdrop="static" data-keyboard="false" data-toggle="modal"><i class="fa fa-fw fa-close"></i></button>'

                return html;
            }
        }, {
            targets: 13,
            "visible": false,
            "data": "supplierId",
            "className": "details-control",
        }, {
            targets: 14,
            "visible": false,
            "data": "approvePaymentStatus",
            "className": "details-control",
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
                return 'AuctionPaymentDone_' + d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();
            },
            attr: {
                type: "button",
                id: 'dt_excel_export_all'
            },
            exportOptions: {
                columns: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
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
                    v: 'Payment Completed'
                }]);
                var r4 = Addrow(4, [{
                    k: 'A',
                    v: 'Date'
                }, {
                    k: 'B',
                    v: !isEmpty(paymentDateFilter) ? paymentDateFilter : currentDate
                }]);

                sheet.childNodes[0].childNodes[1].innerHTML = r2 + r3 + r4 + sheet.childNodes[0].childNodes[1].innerHTML;
            }
        }],
        "fnRowCallback": function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            if (aData.approvePaymentStatus == "3") {
                $('td', nRow).css('background-color', '#ef503d');
            }
        },

    });

    $("#excel_export_all").on("click", function() {
        table.button("#dt_excel_export_all").trigger();

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
    $('#table-filter-due-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: false,
        orientation: "bottom"
    }).on("change", function(e, picker) {
        dueDateFilter = $(this).val();
        $(this).closest('.input-group').find('.due-clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon due-clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.draw();
    });
    $('.input-group').on('click', '.due-clear-date', function() {
        dueDateFilter = '';
        $('#table-filter-due-date').val('');
        $(this).remove();
        table.draw();

    })

    var paymentDateFilter;
    $('#table-filter-invoice-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: false,
        orientation: "bottom"
    }).on("change", function(e, picker) {
        paymentDateFilter = $(this).val();
        $(this).closest('.input-group').find('.invoice-clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon invoice-clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.draw();
    });
    $('.input-group').on('click', '.invoice-clear-date', function() {
        paymentDateFilter = '';
        $('#table-filter-invoice-date').val('');
        $(this).remove();
        table.draw();

    })

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        if (oSettings.sTableId == "table-payment-completed") {
            //Supplier filter
            if (typeof supplierFilter != 'undefined' && supplierFilter.length != '') {
                if (aData[13].length == 0 || aData[13] != supplierFilter) {
                    return false;
                }
            }
            //Due Date Filter
            if (typeof dueDateFilter != 'undefined' && dueDateFilter.length != '') {
                if (aData[7].length == 0 || aData[7] != dueDateFilter) {
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
            url: myContextPath + "/accounts/cancel-auction-payment",
            contentType: "application/json",
            async: false,
            success: function(data) {
                result = data;
                table.ajax.reload();
            }
        });
        return result;
    })

    $('#table-payment-completed').on('click', '.freeze', function(event) {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        var rowData = table.row($(event.currentTarget).closest('tr')).data();
        if (rowData.approvePaymentStatus == 5) {
            alert($.i18n.prop('page.account.payment.approval.partial.frezze'))
            return event.preventDefault();
        }
        let response = getInvoicePaymentTransactionDetails(rowData.id)
        let isInvoiceUploaded = false;
        $.each(response.data, function(index, value) {
            if (isEmpty(value.bankStatementAttachmentFilename)) {
                isInvoiceUploaded = true
            }
        });
        if (isInvoiceUploaded) {
            alert($.i18n.prop('page.accounts.payments.approval.freeze.check.bankstatement.upload'))
            return event.preventDefault();
        }
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
            url: myContextPath + "/accounts/freeze-auction-payment",
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
        invoicePaymentsDetailsModalBody.find('input[name="rowData"]').val(JSON.stringify(rowData));
        invoicePaymentsDetailsEle.find('span.invoiceNo').html(rowData.invoiceNo)
        invoicePaymentsDetailsEle.find('span.supplierName').html(rowData.supplierName + '/' + rowData.auctionHouseName)
    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
        $('#invoicePaymentDetails').find('table').dataTable().fnDestroy();
    });

});
//invoice upload
$(function() {
    // upload invoice image
    let invoice_upload_form = $('#modal-statementUpload-upload form#form-file-upload');
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
    $('#modal-statementUpload-upload').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(e.relatedTarget);
        var data = invoicePaymentDetailsDetailTable.row($(targetElement).closest('tr')).data();
        let invoiceData = $(targetElement).closest('.modal-body').find('input[name="rowData"]').val();
        $(this).find('input[name="code"]').val(data.paymentVoucherNo);
        invoiceData = JSON.parse(invoiceData);
        //         $(this).find('input[name="invoiceData"]').val(invoiceData);
        //         $(this).find('input[name="rowData"]').val(JSON.stringify(data));
        let infoBlock = $(this).find('div.information');
        infoBlock.find('span.remitTo').html(invoiceData.supplierName + "/" + invoiceData.auctionHouseName);
        infoBlock.find('span.bank').html(data.bankName);
        setAutonumericValue(infoBlock.find('span.amount'), data.amount);

    }).on('hidden.bs.modal', function() {
        $(this).find('input').val('');
    }).on('click', '#upload', function() {
        if (!invoice_upload_form.valid()) {
            return false;
        }
        if (!isValidFileSelected($('#modal-statementUpload-upload input[name="invoiceFile"]'))) {
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }

        var data = new FormData();
        var invoiceFileEle = $('#modal-statementUpload-upload input[name="invoiceFile"]');
        if (invoiceFileEle.prop('files').length > 0) {
            let file = invoiceFileEle.prop('files')[0];
            data.append("invoiceFile", file);
        } else {
            return false;
        }
        let code = $('#modal-statementUpload-upload input[name="code"]').val();
        let response = uploadFile(data, code)
        if (response.status == 'success') {
            $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Invoice uploaded.');
            $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                $("#alert-block").slideUp(500);
            });
            invoicePaymentDetailsDetailTable.ajax.reload();
            table.ajax.reload();
            $('#modal-statementUpload-upload').modal('toggle');

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
            url: myContextPath + "/accounts/payment/auction/upload/bankStatement?paymentVoucherNo=" + code,
            contentType: false,
            async: false,
            success: function(data) {
                result = data;
            }
        });
        return result;
    }
    $('table#table-detail-invoice').on('click', 'button[name="btn-cancel-payment"]', function() {
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
})
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
                html = '<button type="button" name="fileupload" class="btn btn-success ml-5 btn-xs" title="Statement upload" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-statementUpload-upload"><i class="fa fa-fw fa-cloud-upload"></i></button>'

                html += '<a href="' + myContextPath + '/get/' + row.bankStatementAttachmentDiskFilename + '?path=' + row.attachmentDirectory + '&from=upload&invoiceNo=' + row.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-eye"></i></a>';
                html += '<button type="button" class="btn btn-warning ml-5 btn-xs" name="btn-cancel-payment"><i class="fa fa-remove"></i></button>'

                return html;
            }
        }],
        "drawCallback": function(settings, json) {
            $('#table-detail-invoice').find('span.autonumber').autoNumeric('init')
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

function getInvoicePaymentTransactionDetails(code) {
    var response;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: 'get',
        async: false,
        url: myContextPath + "/accounts/payment/invoicePayments/data-source?code=" + code,
        success: function(data) {
            response = data;
        }
    })
    return response;
}
