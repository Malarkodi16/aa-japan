var tableStorage;
$(function() {
    let shippingChassisNoMatchIndexArr = [];
    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    }
    ;$('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        tableStorage.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        tableStorage.page.len($(this).val()).draw();
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
    })

    $.getJSON(myContextPath + "/data/currency.json", function(data) {
        $('#currency').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.currencySeq,
                    text: item.currency,
                    data: item
                };
            })
        });
    })

    $.getJSON(myContextPath + "/data/accounts/payment-booking-count", function(data) {
        setPaymentBookingDashboardStatus(data.data)
    });

    /* Storage Photos invoice list Data-table */

    var tableEle = $('#table-storage-photos-forwarder-invoice');
    tableStorage = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/accounts/payment/storage-and-photos-datasource",
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
            "visible": false,
            "data": "category"
        }, {
            targets: 4,
            "className": "details-control",
            "data": "forwarderName",
        }, {
            targets: 5,
            "className": "details-control",
            "data": "dueDate"
        }, {
            targets: 6,
            "className": "details-control dt-right",
            "data": "amount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 7,
            "data": "invoiceUpload",
            "className": "details-control",
            "render": function(data, type, row) {
                var status;
                var className;
                if (data == 0) {
                    status = "INVOICE_NOT_UPLOADED"
                    className = "default"
                } else if (data == 1) {
                    status = "INVOICE_UPLOADED"
                    className = "success"
                }
                return '<span class="label label-' + className + '" style="min-width:100px">' + status + '</span>';
            }
        }, {
            targets: 8,
            "className": "details-control",
            "data": "remitter",
            "visible": false
        }, {
            targets: 9,
            "width": "100px",
            "render": function(data, type, row) {
                var html;
                html = '<a href="' + myContextPath + '/accounts/storageAndPhotos/edit/' + row.invoiceNo + '" class="ml-5 btn btn-info btn-xs"><i class="fa fa-fw fa-edit"></i></a>'
                //                 html = '<a class="ml-5 btn btn-info btn-xs" title="Edit" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-edit-details"><i class="fa fa-fw fa-edit"></i></a>'
                html += '<button type="button" name="fileupload" class="btn btn-primary ml-5 btn-xs" title="invoice upload" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-invoice-upload"><i class="fa fa-fw fa-cloud-upload"></i></button>';
                if (row.invoiceUpload == 1) {
                    html += '<a href="' + myContextPath + '/get/' + row.invoiceAttachmentDiskFilename + '?path=' + row.attachmentDirectory + '&from=upload&invoiceNo=' + row.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs" name="preview_link"><i class="fa fa-fw fa-eye"></i></a>';
                }
                return html
            }
        }, {
            targets: 10,
            data: 'approvePaymentItems',
            "visible": false,
            "render": function(data, type, row) {
                return JSON.stringify(data)
            }
        }],
        "fnDrawCallback": function(oSettings) {
//             $(oSettings.nTHead).hide();
            tableEle.find('input.autonumber,span.autonumber').autoNumeric('init')
            shippingChassisNoMatchIndexArr = getUnique(shippingChassisNoMatchIndexArr, 'index');
            for (let i = 0; i < shippingChassisNoMatchIndexArr.length; i++) {
                var row = tableStorage.row(shippingChassisNoMatchIndexArr[i]['index']);
                var tr = $(row.node());
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                    tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                }
                let matchIdArr = shippingChassisNoMatchIndexArr[i]['matchIdArr'];
                if (matchIdArr.length > 0) {
                    var detailsElement = storageFormat(row.data());
                    row.child(detailsElement).show();
                    detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
                    tr.addClass('shown');
                    tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
                    $(detailsElement).find('tr').css("background-color", "");
                    for (let j = 0; j < matchIdArr.length; j++) {
                        $(detailsElement).find('tr[data-id="' + matchIdArr[j] + '"]').css("background-color", "#0dd1ad");
                    }
                }

            }
            shippingChassisNoMatchIndexArr = []
        }

    });
    tableStorage.on("click", 'a[name="preview_link"]', function() {
        let tr = $(this).closest('tr')
        let row = tableStorage.row(tr)
        row.deselect();
        let data = row.data();
        data.attachementViewed = 1;
        updateAttachmentViewedStatus(data.invoiceNo);
        row.data(data).invalidate();
        tr.find('input.autonumber,span.autonumber').autoNumeric('init')
    })
    tableEle.on("click", 'td>input[type="checkbox"]', function() {
        $(tableEle).find("input[type=checkbox]:checked").not(this).prop("checked", false)
    })

    $('#table-filter-search-chassisNo').keyup(function(e) {
        if (this.value.length >= 3 || this.value.length == 0 || e.keyCode == 13) {
            tableStorage.search($(this).val()).draw();
        }

    });

    $.fn.dataTable.ext.search.push(function(settings, data, dataIndex) {
        if (settings.sTableId == 'table-storage-photos-forwarder-invoice') {
            var term = $('#table-filter-search-chassisNo').val().toLowerCase();
            var orderItem = JSON.parse(data[10]);
            var row = tableStorage.row(dataIndex);
            var tr = $(row.node());
            if (row.child.isShown()) {
                row.child.hide();
                tr.removeClass('shown');
                tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
            }
            let highlightArr = [];
            let isFound = false;
            for (var i = 0; i < orderItem.length; i++) {
                if (!isEmpty(term)) {
                    if (ifNotValid(orderItem[i]["chassisNo"], '').toLowerCase().indexOf(term) != -1) {
                        highlightArr.push(orderItem[i].chassisNo);
                        isFound = true;

                    }
                }
                if (isEmpty(term)) {
                    return true
                }
            }
            if (isFound) {
                let object = {}
                object.index = dataIndex;
                object.matchIdArr = highlightArr;
                shippingChassisNoMatchIndexArr.push(object);
                return true
            }
        }

        return false;
    })
    // Select Draw change filter
    var forwarderFilter, categoryFilter;
    $('select[name="forwarderFilter"]').on('change', function() {
        forwarderFilter = $(this).val();
        tableStorage.draw();
    });
    // DataTable Filter
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        // date filter
        if (typeof forwarderFilter != 'undefined' && forwarderFilter.length != '') {
            if (aData[8].length == 0 || aData[8] != forwarderFilter) {
                return false;
            }
        }
        return true;
    });

    tableStorage.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            tableStorage.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            tableStorage.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            tableStorage.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            tableStorage.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function() {
        if (tableStorage.rows({
            selected: true,
            page: 'current'
        }).count() !== tableStorage.rows({
            page: 'current'
        }).count()) {
            $(tableEle).find("th.select-checkbox>input").removeClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(tableEle).find("th.select-checkbox>input").addClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (tableStorage.rows({
            selected: true,
            page: 'current'
        }).count() !== tableStorage.rows({
            page: 'current'
        }).count()) {
            $(tableEle).find("th.select-checkbox>input").removeClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(tableEle).find("th.select-checkbox>input").addClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', true);

        }

    })
    tableStorage.on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = tableStorage.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
        } else {
            tableStorage.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = tableStorage.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    $(row.node()).removeClass('shown');
                    $(row.node()).find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                }

            })
            tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
            var detailsElement = storageFormat(row.data());
            row.child(detailsElement).show();
            detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
            tr.addClass('shown');
        }
    });

    // approve payment
    $('.select2-select').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true
    })
    $('input[name="dueDate"]').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    })
    let payment_details_form = $('#modal-approve-payment form#payment-detail-form');
    // validation
    payment_details_form.validate({

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
            'dueDate': 'required',

        }
    });

    var modelEle = $('#modal-edit-details');
    var row, modalTriggerEle;
    modelEle.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalTriggerEle = $(event.relatedTarget);
        var tr = $(modalTriggerEle).closest('tr');
        var rowData = tableStorage.row(tr).data();
        $(modelEle).find("#invoiceNo").val(rowData["invoiceNo"]);
        $(modelEle).find("#dueDate").datepicker('setDate', rowData["dueDate"]);
        $(modelEle).find("#refNo").val(rowData["refNo"]);
        $(modelEle).find("#remarks").val(rowData["remarks"]);
    }).on('hidden.bs.modal', function() {
        $(this).find('select,input').val('').trigger('change');
        $(this).find('span.help-block').html('');
        $(this).find('.has-error').removeClass('has-error');
    }).on('click', '#edit', function() {
        if (!$('#edit-payment-form').find('input').valid()) {
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }

        let dueDate = $(modelEle).find('input[name="dueDate"]').val();
        let invoiceNo = $(modelEle).find('input[name="invoiceNo"]').val();
        let refNo = $(modelEle).find('input[name="refNo"]').val();
        let remarks = $(modelEle).find('input[name="remarks"]').val();
        var queryString = "?invoiceNo=" + invoiceNo + "&dueDate=" + dueDate + "&refNo=" + refNo + "&remarks=" + remarks;

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            url: myContextPath + "/accounts/payment/edit/storage-photos" + queryString,
            contentType: false,
            success: function(data) {
                if (data.status == 'success') {

                    $(modelEle).modal('toggle');
                    //                     var row = table.row($(triggerEle).closest('tr'));
                    //                     if (!isEmpty(data.data)) {
                    //                         row.data(data.data).invalidate();
                    //                     }
                    tableStorage.ajax.reload();

                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong>');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                }
            }
        });
    })
    $('#modal-approve-payment').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        if (tableStorage.rows({
            selected: true,
            page: 'current'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return e.preventDefault();
        }
        var rowdata = tableStorage.rows({
            selected: true,
            page: 'current'
        }).data();
        var element;
        var i = 0;
        tableStorage.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = tableStorage.row(this).data();
            if (data.invoiceUpload == 0) {
                alert($.i18n.prop('alert.invoice.not.uploaded'))
                return e.preventDefault();
            } else if (data.attachementViewed == 0) {
                alert($.i18n.prop('alert.invoice.not.viewed'))
                return e.preventDefault();
            } else {
                var stockNo = ifNotValid(data.stockNo, '');
                var chassisNo = ifNotValid(data.chassisNo, '');
                var dueDate = ifNotValid(data.dueDate, '');
                $('#modal-approve-payment').find('input[name="dueDate"]').datepicker("setDate", dueDate);
                i++;
            }
        });
    }).on('hidden.bs.modal', function() {
        $(this).find('select,input,textarea').val('').trigger('change');
    }).on('click', '#approve', function() {
        if (!payment_details_form.valid()) {
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }

        let params = tableStorage.rows({
            selected: true,
            page: 'current'
        }).data().toArray().map(row=>row.invoiceNo);
        let data = {}

        data['dueDate'] = $('#modal-approve-payment').find('input[name="dueDate"]').val();
        let response = approvePayment(data, $.param({
            "id": params
        }));
        if (response.status == 'success') {
            $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Payment Approved.');
            $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                $("#alert-block").slideUp(500);
            });
            $('#modal-approve-payment').modal('toggle');

            tableStorage.ajax.reload();
            setPaymentBookingDashboardStatus(data);
        }

    })
    var approvePayment = function(data, params) {
        let result;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/approve/storageandphoto?" + params,
            contentType: "application/json",
            async: false,
            success: function(data) {
                result = data;
            }
        });
        return result;
    }

    let modal_image_preview = $("#modal-invoice-preview");
    modal_image_preview.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(e.relatedTarget);
        var data = tableStorage.row($(targetElement).closest('tr')).data();
        let filename = data.invoiceAttachmentDiskFilename;
        if (!isEmpty(filename)) {
            updateAttachmentViewedStatus(data.invoiceNo);
            $('embed.image_preview').attr('src', myContextPath + '/get/' + filename + '?path=' + data.attachmentDirectory + '&from=upload');
            tableStorage.ajax.reload();
        } else {
            $('embed.image_preview').attr('src', myContextPath + '/resources/assets/images/no-image-icon.png');
        }
    })
})
// invoice upload
$(function() {
    // upload invoice image
    let invoice_upload_form = $('#modal-invoice-upload form#form-file-upload');
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
    $('#modal-invoice-upload').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(e.relatedTarget);
        var data = tableStorage.row($(targetElement).closest('tr')).data();
        $(this).find('input[name="invoiceId"]').val(data.invoiceNo);
    }).on('hidden.bs.modal', function() {
        $(this).find('input').val('');
    }).on('click', '#upload', function() {
        if (!invoice_upload_form.valid()) {
            return false;
        }
        if (!isValidFileSelected($('#modal-invoice-upload input[name="invoiceFile"]'))) {
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }

        var data = new FormData();
        var invoiceFileEle = $('#modal-invoice-upload input[name="invoiceFile"]');
        if (invoiceFileEle.prop('files').length > 0) {
            let file = invoiceFileEle.prop('files')[0];
            data.append("invoiceFile", file);
        } else {
            return false;
        }
        let id = $('#modal-invoice-upload input[name="invoiceId"]').val();
        let response = uploadFile(data, id)
        if (response.status == 'success') {
            $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Invoice uploaded.');
            $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                $("#alert-block").slideUp(500);
            });
            $('#modal-invoice-upload').modal('toggle');
            tableStorage.ajax.reload();
        }
    })
    var uploadFile = function(data, id) {
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
            url: myContextPath + "/accounts/approve/payment/storageandphotos/invoice/upload?id=" + id,
            contentType: false,
            async: false,
            success: function(data) {
                result = data;
            }
        });
        return result;
    }

})
function setPaymentBookingDashboardStatus(data) {
    $('#count-approval-auction').html(data.auctionApproval);
    $('#count-approval-transport').html(data.transportApproval);
    $('#count-approval-freight').html(data.freight);
    $('#count-approval-others').html(data.others);
    $('#count-approval-storage').html(data.storage);
    // $('#count-prepayment').html(data.paymentAdvance);

}
function updateAttachmentViewedStatus(invoiceNo) {
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        url: myContextPath + "/accounts/payment/update/attachmentView/storagePhotos?id=" + invoiceNo,
        contentType: false,
        async: false,
        success: function(data) {
            console.log("viewed")
        }
    });
}
//Purchase Format
function storageFormat(rowData) {
    var element = $('#storage-clone-container>#storage-invoice-details>.clone-element').clone();
    $(element).find('input[name="paymentinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    let amountTotal = 0;
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.approvePaymentItems.length; i++) {
        var row = $(rowClone).clone();
        row.attr('data-id', rowData.approvePaymentItems[i].chassisNo);
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.chassisNo').html(ifNotValid(rowData.approvePaymentItems[i].chassisNo, ''));
        $(row).find('td.storage span.autonumber').autoNumeric('init').autoNumeric('set', ifNotValid(rowData.approvePaymentItems[i].amount, '')).autoNumeric('update', {
            aSign: rowData.approvePaymentItems[i].currencySymbol + ' '
        });
        $(row).find('td.shipping span.autonumber').autoNumeric('init').autoNumeric('set', ifNotValid(rowData.approvePaymentItems[i].shippingCharges, '')).autoNumeric('update', {
            aSign: rowData.approvePaymentItems[i].currencySymbol + ' '
        });
        $(row).find('td.photoCharges span.autonumber').autoNumeric('init').autoNumeric('set', ifNotValid(rowData.approvePaymentItems[i].photoCharges, '')).autoNumeric('update', {
            aSign: rowData.approvePaymentItems[i].currencySymbol + ' '
        });
        $(row).find('td.blAmendCombineCharges span.autonumber').autoNumeric('init').autoNumeric('set', ifNotValid(rowData.approvePaymentItems[i].blAmendCombineCharges, '')).autoNumeric('update', {
            aSign: rowData.approvePaymentItems[i].currencySymbol + ' '
        });
        $(row).find('td.radiationCharges span.autonumber').autoNumeric('init').autoNumeric('set', ifNotValid(rowData.approvePaymentItems[i].radiationCharges, '')).autoNumeric('update', {
            aSign: rowData.approvePaymentItems[i].currencySymbol + ' '
        });
        $(row).find('td.yardHandlingCharges span.autonumber').autoNumeric('init').autoNumeric('set', ifNotValid(rowData.approvePaymentItems[i].yardHandlingCharges, '')).autoNumeric('update', {
            aSign: rowData.approvePaymentItems[i].currencySymbol + ' '
        });
        $(row).find('td.inspectionCharges span.autonumber').autoNumeric('init').autoNumeric('set', ifNotValid(rowData.approvePaymentItems[i].inspectionCharges, '')).autoNumeric('update', {
            aSign: rowData.approvePaymentItems[i].currencySymbol + ' '
        });
        $(row).find('td.transportCharges span.autonumber').autoNumeric('init').autoNumeric('set', ifNotValid(rowData.approvePaymentItems[i].transportCharges, '')).autoNumeric('update', {
            aSign: rowData.approvePaymentItems[i].currencySymbol + ' '
        });
        $(row).find('td.freightCharges span.autonumber').autoNumeric('init').autoNumeric('set', ifNotValid(rowData.approvePaymentItems[i].freightCharges, '')).autoNumeric('update', {
            aSign: rowData.approvePaymentItems[i].currencySymbol + ' '
        });
        $(row).find('td.amount span.autonumber').autoNumeric('init').autoNumeric('set', ifNotValid(rowData.approvePaymentItems[i].totalAmount, '')).autoNumeric('update', {
            aSign: rowData.approvePaymentItems[i].currencySymbol + ' '
        });
        $(row).find('td.remarks').html(ifNotValid(rowData.approvePaymentItems[i].remarks, ''));
        amountTotal += Number(ifNotValid(rowData.approvePaymentItems[i].totalAmount, 0));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);
    }
    $(element).find('table>tfoot td.amount>span').autoNumeric('init').autoNumeric('set', amountTotal).autoNumeric('update', {
        aSign: rowData.currencySymbol + ' '
    });
    return element;

}
