var supplierJson, forwarderJson, forwarderDetailJson;
$(function() {
    let shippingChassisNoMatchIndexArr = [];
    $.getJSON(myContextPath + "/data/accounts/payment-booking-count", function(data) {
        setPaymentBookingDashboardStatus(data.data)
    });

    $.getJSON(myContextPath + "/data/forwarders.json", function(data) {
        forwarderJson = data;
        $('#container-filter-frwdr').select2({
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

    // select2 initialize
    $('#paymentType').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true,
        placeholder: "Select Payment Type",

    })

    // datepicker initialize
    $('.datepicker ').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: true
    }).on('change', function() {
        $(this).valid();
    });
    // table shipping requested
    var table_invoice_created_container_Ele = $('#table-roro-invoice-created');
    var table_invoice_created_container = table_invoice_created_container_Ele.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/freight/shipping/all/invoice",
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
            "data": "createdDate"

        }, {
            targets: 2,
            "className": "details-control",
            "data": "invoiceNo"

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
            "data": "totalFreightAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {

            targets: 6,
            "className": "dt-right",
            "type": "num-fmt",
            "data": "otherTotalAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {

            targets: 7,
            "className": "dt-right",
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
            "data": "totalAmountUsd",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="$ " data-m-dec="2">' + data + '</span>';
            }
        }, {
            targets: 9,
            "data": "supplierId",
            "render": function(data, type, row) {
                var html = '';
                html = '<a class="ml-5 btn btn-info btn-xs" title="Edit" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-edit-details"><i class="fa fa-fw fa-edit"></i></a>'
                html += '<button type="button" name="fileupload" class="btn btn-primary ml-5 btn-xs" title="invoice upload" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-invoice-upload"><i class="fa fa-fw fa-cloud-upload"></i></button>'
                if (row.invoiceUpload == 1) {
                    html += '<a href="' + myContextPath + '/get/' + row.invoiceAttachmentDiskFilename + '?path=' + row.attachmentDirectory + '&from=upload&invoiceNo=' + row.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs" name="preview_link"><i class="fa fa-fw fa-eye"></i></a>';
                }

                return html;
            }
        }, {
            targets: 10,
            "data": "supplierId",
            "visible": false
        }, {
            targets: 11,
            data: 'approvePaymentItems',
            "visible": false,
            "render": function(data, type, row) {
                return JSON.stringify(data)
            }
        }],
        "fnDrawCallback": function(oSettings) {
//             $(oSettings.nTHead).hide();
            table_invoice_created_container_Ele.find('input.autonumber,span.autonumber').autoNumeric('init')
            shippingChassisNoMatchIndexArr = getUnique(shippingChassisNoMatchIndexArr, 'index');
            for (let i = 0; i < shippingChassisNoMatchIndexArr.length; i++) {
                var row = table_invoice_created_container.row(shippingChassisNoMatchIndexArr[i]['index']);
                var tr = $(row.node());
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                    tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                }
                let matchIdArr = shippingChassisNoMatchIndexArr[i]['matchIdArr'];
                if (matchIdArr.length > 0) {
                    var detailsElement = formatRoroCreatedInvoiceContainer(row.data());
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
    table_invoice_created_container.on("click", 'a[name="preview_link"]', function() {
        let tr = $(this).closest('tr')
        let row = table_invoice_created_container.row(tr)
        row.deselect();
        let data = row.data();
        data.attachementViewed = 1;
        updateAttachmentViewedStatus(data.id);
        row.data(data).invalidate();
        tr.find('input.autonumber,span.autonumber').autoNumeric('init')
    })
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        table_invoice_created_container.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
        table_invoice_created_container.page.len($(this).val()).draw();
    });
    /* table on click to append Item Container */
    table_invoice_created_container.on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table_invoice_created_container.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
        } else {
            table_invoice_created_container.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = table_invoice_created_container.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    $(row.node()).removeClass('shown');
                    $(row.node()).find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                }

            })
            tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
            var detailsElement = formatRoroCreatedInvoiceContainer(row.data());
            row.child(detailsElement).show();
            detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
            tr.addClass('shown');
        }
    });
    table_invoice_created_container.on("click", 'td>input[type="checkbox"]', function() {
        $(table_invoice_created_container_Ele).find("input[type=checkbox]:checked").not(this).prop("checked", false)
    })
    table_invoice_created_container.on("select", function() {

        if (table_invoice_created_container.rows({
            selected: true,
            page: 'current'
        }).count() !== table_invoice_created_container.rows({
            page: 'current'
        }).count()) {
            $(table_invoice_created_container_Ele).find("th.select-checkbox>input").removeClass("selected");
            $(table_invoice_created_container_Ele).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(table_invoice_created_container_Ele).find("th.select-checkbox>input").addClass("selected");
            $(table_invoice_created_container_Ele).find("th.select-checkbox>input").prop('checked', true);
        }

    }).on("deselect", function() {
        if (table_invoice_created_container.rows({
            selected: true,
            page: 'current'
        }).count() !== table_invoice_created_container.rows({
            page: 'current'
        }).count()) {
            $(table_invoice_created_container_Ele).find("th.select-checkbox>input").removeClass("selected");
            $(table_invoice_created_container_Ele).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(table_invoice_created_container_Ele).find("th.select-checkbox>input").addClass("selected");
            $(table_invoice_created_container_Ele).find("th.select-checkbox>input").prop('checked', true);

        }

    });

     $('#table-filter-search-chassisNo').keyup(function(e) {
        if (this.value.length >= 3 || this.value.length == 0 || e.keyCode == 13) {
            table_invoice_created_container.search($(this).val()).draw();
        }

    });

    $.fn.dataTable.ext.search.push(function(settings, data, dataIndex) {
        if (settings.sTableId == 'table-roro-invoice-created') {
            var term = $('#table-filter-search-chassisNo').val().toLowerCase();
            var orderItem = JSON.parse(data[11]);
            var row = table_invoice_created_container.row(dataIndex);
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

    $.fn.dataTable.ext.search.push(function(settings, data, dataIndex) {
        var term = $('#table-filter-search').val().toLowerCase();
        var orderItem = JSON.parse(data[11]);
        for (var i = 0; i < orderItem.length; i++) {

            if (isEmpty(term)) {
                return true
            } else if (!isEmpty(term)) {
                if (~ifNotValid(orderItem[i]["chassisNo"], '').toLowerCase().indexOf(term)) {
                    return true;
                } else if (~ifNotValid(data[2], '').toLowerCase().indexOf(term)) {
                    return true;
                } else if (~ifNotValid(data[3], '').toLowerCase().indexOf(term)) {
                    return true;
                }
            }
        }
        return false;
    })

    var filterSupplierName;
    $('#container-filter-frwdr').select2({
        allowClear: true,
        placeholder: 'All',
    }).on("change", function(event) {
        filterSupplierName = $(this).find('option:selected').val();
        table_invoice_created_container.draw();
    });

    var dueDateFilter;
    $('.dueDateDatepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: true
    }).on("change", function(e, picker) {
        dueDateFilter = $(this).val();
        table_invoice_created_container.draw();
    });

    filterSupplierName = $('#container-filter-frwdr').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {

        //Due Date filter
        if (typeof dueDateFilter != 'undefined' && dueDateFilter.length != '') {
            if (aData[4].length == 0 || aData[4] != dueDateFilter) {
                return false;
            }
        }
        //forwader type filter
        if (typeof filterSupplierName != 'undefined' && filterSupplierName.length != '') {
            if (aData[10].length == 0 || aData[10] != filterSupplierName) {
                return false;
            }
        }
        return true
    })

    var modelEle = $('#modal-edit-details');
    var row, modalTriggerEle;
    modelEle.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalTriggerEle = $(event.relatedTarget);
        var tr = $(modalTriggerEle).closest('tr');
        var rowData = table_invoice_created_container.row(tr).data();
        $(modelEle).find("#invoiceNo").val(rowData["invoiceNo"]);
        $(modelEle).find("#dueDate").datepicker('setDate', rowData["dueDate"]);
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
        var queryString = "?invoiceNo=" + invoiceNo + "&dueDate=" + dueDate;

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            url: myContextPath + "/accounts/payment/edit/freight-shipping-invoice" + queryString,
            contentType: false,
            success: function(data) {
                if (data.status == 'success') {

                    $(modelEle).modal('toggle');
                    //                     var row = table.row($(triggerEle).closest('tr'));
                    //                     if (!isEmpty(data.data)) {
                    //                         row.data(data.data).invalidate();
                    //                     }
                    table_invoice_created_container.ajax.reload();

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
        if (table_invoice_created_container.rows({
            selected: true,
            page: 'current'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return e.preventDefault();
        }
        var rowdata = table_invoice_created_container.rows({
            selected: true,
            page: 'current'
        }).data();
        var element;
        var i = 0;
        table_invoice_created_container.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {

            var data = table_invoice_created_container.row(this).data();
            if (data.totalAmountUsd > 0) {
                $('#modal-approve-payment').find('#ifFreightUsd').removeClass('hidden');
            }
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
        $('#modal-approve-payment').find('#ifFreightUsd').addClass('hidden');
    }).on('click', '#approve', function() {
        if (!payment_details_form.valid()) {
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }

        let params = table_invoice_created_container.rows({
            selected: true,
            page: 'current'
        }).data().toArray().map(row=>row.id);
        let data = {}

        data['dueDate'] = $('#modal-approve-payment').find('input[name="dueDate"]').val();
        data['paymentType'] = $('#modal-approve-payment').find('select[name="paymentType"]').find('option:selected').val();
        let response = approvePayment(data, $.param({
            "id": params
        }));
        if (response.status == 'success') {
            $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Payment Approved.');
            $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                $("#alert-block").slideUp(500);
            });
            $('#modal-approve-payment').modal('toggle');

            table_invoice_created_container.ajax.reload();
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
            url: myContextPath + "/freight/accounts/approve?" + params,
            contentType: "application/json",
            async: false,
            success: function(data) {
                result = data;
            }
        });
        return result;
    }
    // upload invoice image
    let invoice_upload_form = $('#modal-invoice-upload form#form-file-upload');
    // validation
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
    // file uplaod
    $('#modal-invoice-upload').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(e.relatedTarget);
        var data = table_invoice_created_container.row($(targetElement).closest('tr')).data();
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
            url: myContextPath + "/accounts/approve/payment/shipping/invoice/upload?id=" + id,
            contentType: false,
            async: true,
            success: function(response) {
                if (response.status == 'success') {
                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Invoice uploaded.');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                    $('#modal-invoice-upload').modal('toggle');
                    table_invoice_created_container.ajax.reload();

                }
            }
        });

    })
    let modal_image_preview = $("#modal-invoice-preview");
    modal_image_preview.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(e.relatedTarget);
        var data = table_invoice_created_container.row($(targetElement).closest('tr')).data();
        let filename = data.invoiceAttachmentDiskFilename;
        if (!isEmpty(filename)) {
            $.ajax({
                beforeSend: function() {
                    $('#spinner').show()
                },
                complete: function() {
                    $('#spinner').hide();
                },
                type: "post",
                url: myContextPath + "/accounts/payment/update/attachmentView/shipping/invoice?id=" + data.invoiceNo,
                contentType: false,
                async: true,
                success: function(data) {}
            });
            $('embed.image_preview').attr('src', myContextPath + '/get/' + filename + '?path=' + data.attachmentDirectory + '&from=upload');
            table_invoice_created_container.ajax.reload();
        } else {
            $('embed.image_preview').attr('src', myContextPath + '/resources/assets/images/no-image-icon.png');
        }

    })
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
        url: myContextPath + "/accounts/payment/update/attachmentView/shipping/invoice?id=" + invoiceNo,
        contentType: false,
        async: false,
        success: function(data) {
            console.log("viewed")
        }
    });
}

function formatRoroCreatedInvoiceContainer(rowData) {
    var element = $('#clone-container>#roro-container-details>.clone-element').clone();
    $(element).find('input[name="invoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.approvePaymentItems.length; i++) {
        var row = $(rowClone).clone();
        row.attr('data-id', rowData.approvePaymentItems[i].chassisNo);
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.chassisNo').html(ifNotValid(rowData.approvePaymentItems[i].chassisNo, ''));
        $(row).find('td.freightCharge>span').html(ifNotValid(rowData.approvePaymentItems[i].freightCharge, 0.0));
        $(row).find('td.freightChargeUsd>span').html(ifNotValid(rowData.approvePaymentItems[i].freightChargeUsd, 0.0));
        $(row).find('td.shippingCharge>span').html(ifNotValid(rowData.approvePaymentItems[i].shippingCharge, 0.0));
        $(row).find('td.inspectionCharge>span').html(ifNotValid(rowData.approvePaymentItems[i].inspectionCharge, 0.0));
        $(row).find('td.radiationCharge>span').html(ifNotValid(rowData.approvePaymentItems[i].radiationCharge, 0.0));
        $(row).find('td.otherCharges>span').html(ifNotValid(rowData.approvePaymentItems[i].otherCharges, 0.0));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);

    }

    return element;
}
// freight-shipping-container.js
