var transport_invoice_list;
var transportersJson;
$(function() {
     let shippingChassisNoMatchIndexArr = [];
    $.getJSON(myContextPath + "/data/transporters.json", function(data) {
        transportersJson = data;
        $('#table-filter-transporter').select2({
            allowClear: true,
            placeholder: 'Select Transporter',
            width: '100%',
            data: $.map(transportersJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).on("change", function(event) {
            filterTransporterName = $(this).find('option:selected').val();
            transport_invoice_list.draw();
        })

    });

    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    }
    ;// Customize Datatable
    $('#transport-invoice-list #table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        transport_invoice_list.search(query, true, false).draw();
    });
    $('#transport-invoice-list #table-filter-length').change(function() {
        transport_invoice_list.page.len($(this).val()).draw();
    });

    var tableEle = $('#table-transport-invoice');
    transport_invoice_list = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/accounts/payment/transport-invoice-datasource",
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
            "className": "select-checkbox",
            "width": "10px",
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
            "data": "dueDate"
        }, {
            targets: 2,
            "className": "details-control",
            "data": "invoiceDate"
        },{
            targets: 3,
            "className": "details-control",
            "data": "refNo",
        },{
            targets: 4,
            "className": "details-control",
            "data": "invoiceRefNo",
        }, {
            targets: 5,
            "className": "details-control",
            "data": "transporterName",
        }, {
            targets: 6,
            "className": "details-control",
            "className": "dt-right",
            "data": "invoiceTotal",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
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
            "width": "100px",
            "render": function(data, type, row) {
                var html;
                html = '<a class="ml-5 btn btn-info btn-xs" title="Edit" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-edit-details"><i class="fa fa-fw fa-edit"></i></a>'
                html += '<button type="button" data-value="' + row.invoiceRefNo + '" class="ml-5 btn-xs btn btn-danger fa fa-close" data-toggle="modal" data-target="#modal-remark" data-backdrop="static" data-keyboard="false" id="deleteButton"></button>'
                html += '<button type="button" name="fileupload" class="btn btn-primary ml-5 btn-xs" title="invoice upload" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-invoice-upload"><i class="fa fa-fw fa-cloud-upload"></i></button>';
                if (row.invoiceUpload == 1) {
                    html += '<a href="' + myContextPath + '/get/' + row.invoiceAttachmentDiskFilename + '?path=' + row.attachmentDirectory + '&from=upload&invoiceNo=' + row.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs" name="preview_link"><i class="fa fa-fw fa-eye"></i></a>';
                    //                     html += '<button type="button" name="viewUpload" class="btn btn-default ml-5 btn-xs" title="View Invoice" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-invoice-preview"><i class="fa fa-fw fa-eye"></i></button>'
                }
                return html
            }
        }, {
            targets: 9,
            'visible': false,
            "data": "transporterId"

        }, {
            targets: 10,
            data: 'items',
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
                var row = transport_invoice_list.row(shippingChassisNoMatchIndexArr[i]['index']);
                var tr = $(row.node());
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                    tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                }
                let matchIdArr = shippingChassisNoMatchIndexArr[i]['matchIdArr'];
                if (matchIdArr.length > 0) {
                    var detailsElement = transportFormat(row.data());
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
    tableEle.on("click", 'td>input[type="checkbox"]', function() {
        $(tableEle).find("input[type=checkbox]:checked").not(this).prop("checked", false)
    })
    transport_invoice_list.on("click", 'a[name="preview_link"]', function() {
        let tr = $(this).closest('tr')
        let row = transport_invoice_list.row(tr)
        row.deselect();
        let data = row.data();
        data.attachementViewed = 1;
        updateAttachmentViewedStatus(data.invoiceRefNo, data.refNo);
        //invoiceRefNo
        row.data(data).invalidate();
        tr.find('input.autonumber,span.autonumber').autoNumeric('init')
    })
    //expand details
    transport_invoice_list.on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = transport_invoice_list.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
        } else {
            transport_invoice_list.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = transport_invoice_list.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                }

            })
            tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
            var detailsElement = transportFormat(row.data());
            row.child(detailsElement).show();
            detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
            tr.addClass('shown');
        }
    });

    transport_invoice_list.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            transport_invoice_list.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            transport_invoice_list.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            transport_invoice_list.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            transport_invoice_list.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function(e) {
        if (transport_invoice_list.rows({
            selected: true,
            page: 'current'
        }).count() !== transport_invoice_list.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);
        }

        var count = transport_invoice_list.rows({
            selected: true,
            page: 'current'
        }).count();

        if (count > 0) {
            let selectedData = transport_invoice_list.rows({
                selected: true,
                page: 'current'
            }).data();

            for (let i = 0; i < selectedData.length; i++) {
                for (let j = 0; j < selectedData[i].items.length; j++) {
                    if (selectedData[i].items[j].status == 2) {
                        alert($.i18n.prop('need.admin.approval'));
                    }
                }
            }
        }

    }).on("deselect", function() {
        if (transport_invoice_list.rows({
            selected: true,
            page: 'current'
        }).count() !== transport_invoice_list.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }

    })


    $('#table-filter-search-chassisNo').keyup(function(e) {
        if (this.value.length >= 3 || this.value.length == 0 || e.keyCode == 13) {
            transport_invoice_list.search($(this).val()).draw();
        }

    });

    $.fn.dataTable.ext.search.push(function(settings, data, dataIndex) {
        if (settings.sTableId == 'table-transport-invoice') {
            var term = $('#table-filter-search-chassisNo').val().toLowerCase();
            var orderItem = JSON.parse(data[10]);
            var row = transport_invoice_list.row(dataIndex);
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
                        highlightArr.push(orderItem[i].stockNo);
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


    //update transportation status
    var statusUpdateEle;
    var rowDataRemarkStockNo;
    $('#modal-remark').on('show.bs.modal', function(event) {
        statusUpdateEle = $(event.relatedTarget);
        var tr = $(statusUpdateEle).closest('tr');
        var row = transport_invoice_list.row(tr);
        var rowData = row.data();
        rowDataRemarkStockNo = ifNotValid(rowData.invoiceRefNo, '')
    }).on('hidden.bs.modal', function() {
        resetElementInput(this)
    }).on("click", '#btn-remark-submit', function(event) {
        let invoiceRefNo = $(this).data("value");
        if (!confirm($.i18n.prop('common.confirm.cancel'))) {
            return false;
        }
        if (!$('#remarkForm').find('textarea[name="cancellationRemarks"]').valid()) {
            return false;
        }
        var data = {};
        data["cancellationRemarks"] = $('#modal-remark').find('textarea[name="cancellationRemarks"]').val();
        data["invoiceRefNo"] = rowDataRemarkStockNo
        var tr = $(statusUpdateEle).closest('tr');
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/payment/cancel/transport/invoice",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    $('#modal-remark').modal('toggle');
                    transport_invoice_list.row(tr).remove().draw();
                }
            }
        });
    })
    //approve payment
    let payment_details_form = $('#modal-approve-payment form#payment-detail-form');
    //validation
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
            "invoiceFile": 'required'
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
        var rowData = transport_invoice_list.row(tr).data();
        $(modelEle).find("#invoiceRefNo").val(rowData["invoiceRefNo"]);
        $(modelEle).find("#preRefNo").val(rowData["refNo"]);
        $(modelEle).find("#dueDate").datepicker('setDate', rowData["dueDate"]);
        $(modelEle).find("#refNo").val(rowData["refNo"]);
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
        let invoiceRefNo = $(modelEle).find('input[name="invoiceRefNo"]').val();
        let preRefNo = $(modelEle).find('input[name="preRefNo"]').val();
        let refNo = $(modelEle).find('input[name="refNo"]').val();
        var queryString = "?invoiceRefNo=" + invoiceRefNo + "&dueDate=" + dueDate + "&refNo=" + refNo + "&preRefNo=" + preRefNo;

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            url: myContextPath + "/accounts/payment/edit/transport-invoice" + queryString,
            contentType: false,
            success: function(data) {
                if (data.status == 'success') {

                    $(modelEle).modal('toggle');
                    //                     var row = table.row($(triggerEle).closest('tr'));
                    //                     if (!isEmpty(data.data)) {
                    //                         row.data(data.data).invalidate();
                    //                     }
                    transport_invoice_list.ajax.reload();

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
        if (transport_invoice_list.rows({
            selected: true,
            page: 'current'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return e.preventDefault();
        }
        var rowdata = transport_invoice_list.rows({
            selected: true,
            page: 'current'
        }).data();

        for (let i = 0; i < rowdata.length; i++) {
            for (let j = 0; j < rowdata[i].items.length; j++) {
                if (rowdata[i].items[j].status == 2) {
                    alert($.i18n.prop('need.admin.approval'));
                    return e.preventDefault();
                }
            }
        }

        var element;
        var i = 0;
        transport_invoice_list.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = transport_invoice_list.row(this).data();
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

        let params = transport_invoice_list.rows({
            selected: true,
            page: 'current'
        }).data();
        //invoiceRefNo
        var data = new FormData();

        data.append("dueDate", $('#modal-approve-payment').find('input[name="dueDate"]').val());
        let response = approvePayment(data, $.param({
            "invoiceRefNo": params[0].invoiceRefNo,
            "refNo": params[0].refNo
        }));
        if (response.status == 'success') {
            $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Payment Approved.');
            $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                $("#alert-block").slideUp(500);
            });
            $('#modal-approve-payment').modal('toggle');

            transport_invoice_list.ajax.reload();
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
            data: data,
            processData: false,
            url: myContextPath + "/accounts/approve/payment/transport?" + params,
            contentType: false,
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
        var data = transport_invoice_list.row($(targetElement).closest('tr')).data();
        let filename = data.invoiceAttachmentDiskFilename;
        if (!isEmpty(filename)) {
            updateAttachmentViewedStatus(data.invoiceRefNo, data.refNo);
            //invoiceRefNo
            $('embed.image_preview').attr('src', myContextPath + '/get/' + filename + '?path=' + data.attachmentDirectory + '&from=upload');
            transport_invoice_list.ajax.reload();
        } else {
            $('embed.image_preview').attr('src', myContextPath + '/resources/assets/images/no-image-icon.png');
        }

    })

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
        transport_invoice_list.draw();
    });
    $('.input-group').on('click', '.clear-date', function() {
        due_date = '';
        $('#table-filter-due-date').val('');
        $(this).remove();
        transport_invoice_list.draw();

    })

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
        transport_invoice_list.draw();
    });
    $('.input-group').on('click', '.clear-date', function() {
        invoice_date = '';
        $('#table-filter-due-date').val('');
        $(this).remove();
        transport_invoice_list.draw();

    })

    var filterTransporterName = $('#table-filter-transporter').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //Due Date Filter
        if (typeof due_date != 'undefined' && due_date.length != '') {
            if (aData[1].length == 0 || aData[1] != due_date) {
                return false;
            }
        }
        if (typeof invoice_date != 'undefined' && invoice_date.length != '') {
            if (aData[2].length == 0 || aData[2] != invoice_date) {
                return false;
            }
        }
        if (typeof filterTransporterName != 'undefined' && filterTransporterName.length != '') {
            if (aData[9].length == 0 || aData[9] != filterTransporterName) {
                return false;
            }
        }
        if (oSettings.sTableId == 'table-transport-completed') {
            let transporter = tranport_complete_transporter_filter.val();
            if (!isEmpty(transporter)) {
                if (aData[9].length == 0 || aData[9] != transporter) {
                    return false;
                }
            }
            if (!isEmpty(tranport_complete_date_filter.val())) {
                let from_date = tranport_complete_date_filter.data('daterangepicker').startDate;
                let to_date = tranport_complete_date_filter.data('daterangepicker').endDate;
                if (moment(aData[1], 'DD-MM-YYYY') < from_date) {
                    return false;
                }
                if (moment(aData[1], 'DD-MM-YYYY') > to_date) {
                    return false;
                }
            }

        }
        return true;
    });

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
        var data = transport_invoice_list.row($(targetElement).closest('tr')).data();
        $(this).find('input[name="invoiceRefNo"]').val(data.invoiceRefNo);
        $(this).find('input[name="refNo"]').val(data.refNo);
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
        let invoiceRefNo = $('#modal-invoice-upload input[name="invoiceRefNo"]').val();
        let refNo = $('#modal-invoice-upload input[name="refNo"]').val();
        let response = uploadFile(data, invoiceRefNo, refNo)
        if (response.status == 'success') {
            $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Invoice uploaded.');
            $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                $("#alert-block").slideUp(500);
            });
            $('#modal-invoice-upload').modal('toggle');
            transport_invoice_list.ajax.reload();
        }
    })
    var uploadFile = function(data, invoiceRefNo, refNo) {
        let result;
        let queryString = "?invoiceRefNo=" + invoiceRefNo + "&refNo=" + refNo
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
            url: myContextPath + "/accounts/approve/payment/transport/invoice/upload" + queryString,
            contentType: false,
            async: false,
            success: function(data) {
                result = data;
            }
        });
        return result;
    }

})
//common for page
$(function() {
    $.getJSON(myContextPath + "/data/accounts/payment-booking-count", function(data) {
        setPaymentBookingDashboardStatus(data.data)
    });
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
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
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

    });

    $('input[type="radio"][name=radioShowTable].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        if ($(this).val() == 0) {
            $('#transport-invoice-list').addClass('hidden')
            $('#transport-invoice-create').removeClass('hidden')
            transport_completed.ajax.reload();
        } else if ($(this).val() == 1) {
            $('#transport-invoice-list').removeClass('hidden')
            $('#transport-invoice-create').addClass('hidden')
            transport_invoice_list.ajax.reload();
        }
    })

})

//Transport Format
function transportFormat(rowData) {
    var element = $('#transport-clone-container>#payment-transport-approve-details>.clone-element').clone();
    $(element).find('input[name="transportinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    let taxTotal = 0;
    let amountTotal = 0;
    let totalTaxIncluded = 0;
    for (var i = 0; i < rowData.items.length; i++) {
        var row = $(rowClone).clone();
        var stockStatus;
        var stockClassName;
        if (rowData.items[i].status == 1) {
            stockStatus = ""
            stockClassName = "label-default"
        } else if (rowData.items[i].status == 2) {
            stockStatus = "Need Approval"
            stockClassName = "label-warning"
        }
        row.attr('data-id', rowData.items[i].stockNo);
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.stockNo').html(ifNotValid(rowData.items[i].stockNo, ''));
        $(row).find('td.chassisNo').html(ifNotValid(rowData.items[i].chassisNo, ''));
        $(row).find('td.maker').html(ifNotValid(rowData.items[i].maker, ''));
        $(row).find('td.model').html(ifNotValid(rowData.items[i].model, ''));
        $(row).find('td.pickupLocation').html(rowData.items[i].pickupLocation.toUpperCase() === "OTHERS" ? rowData.items[i].pickupLocationCustom : rowData.items[i].pickupLocationName);
        $(row).find('td.dropLocation').html(rowData.items[i].dropLocation.toUpperCase() === "OTHERS" ? rowData.items[i].dropLocationCustom : rowData.items[i].dropLocationName);
        $(row).find('td.amount>span').html(ifNotValid(rowData.items[i].amount, ''));
        $(row).find('td.tax>span').html(ifNotValid(rowData.items[i].taxAmount, ''));
        $(row).find('td.total>span').html(ifNotValid(rowData.items[i].totalAmount, ''));
        $(row).find('td.stockStatus>span').addClass(stockClassName).html(stockStatus);
        taxTotal += Number(ifNotValid(rowData.items[i].taxAmount, 0));
        amountTotal += Number(ifNotValid(rowData.items[i].amount, 0));
        totalTaxIncluded += Number(ifNotValid(rowData.items[i].totalAmount, 0));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);
    }
    $(element).find('table>tfoot td.amount>span').html(amountTotal);
    $(element).find('table>tfoot td.tax>span').html(taxTotal);
    $(element).find('table>tfoot td.total>span').html(totalTaxIncluded);
    return element;

}
function setPaymentBookingDashboardStatus(data) {
    $('#count-approval-auction').html(data.auctionApproval);
    $('#count-approval-transport').html(data.transportApproval);
    $('#count-approval-freight').html(data.freight);
    $('#count-approval-others').html(data.others);
    $('#count-approval-storage').html(data.storage);
    // $('#count-prepayment').html(data.paymentAdvance);

}
function updateAttachmentViewedStatus(invoiceRefNo, refNo) {
    let queryString = "?invoiceRefNo=" + invoiceRefNo + "&refNo=" + refNo
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        url: myContextPath + "/accounts/payment/update/attachmentView/transport" + queryString,
        contentType: false,
        async: false,
        success: function(data) {
            console.log("viewed")
        }
    });
}
