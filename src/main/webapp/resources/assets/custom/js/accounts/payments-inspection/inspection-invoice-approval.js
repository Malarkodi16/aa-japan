let inspection_approval_list;
$(function() {
    let shippingChassisNoMatchIndexArr = [];
    $.getJSON(myContextPath + "/data/accounts/payment-booking-count", function(data) {
        setPaymentBookingDashboardStatus(data.data)
    });
  
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
        inspection_approval_list.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        inspection_approval_list.page.len($(this).val()).draw();
    });

    var tableEle = $('#table-inspection-invoice');
    inspection_approval_list = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/accounts/payment/inspection-approval-datasource",
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
            "data": "invoiceNo"
        }, {
            targets: 2,
            "className": "details-control",
            "data": "refNo"
        }, {
            targets: 3,
            "className": "details-control",
            "data": "dueDate"
        }, {
            targets: 4,
            "className": "details-control",
            "data": "company"
        }, {
            targets: 5,
            "className": "details-control",
            "data": "totalAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + ifNotValid(row.totalAmount, 0) + '</span>';
            }
        }, {
            targets: 6,
            "className": "details-control",
            "data": "invoiceUpload",
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
            targets: 7,
            "width": "100px",
            "render": function(data, type, row) {
                var html;
                html = '<a class="ml-5 btn btn-info btn-xs" title="Edit" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-edit-details"><i class="fa fa-fw fa-edit"></i></a>'
                html += '<button type="button" name="fileupload" class="btn btn-primary ml-5 btn-xs" title="invoice upload" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-invoice-upload"><i class="fa fa-fw fa-cloud-upload"></i></button>';
                if (row.invoiceUpload == 1) {
                    html += '<a href="' + myContextPath + '/get/' + row.invoiceAttachmentDiskFilename + '?path=' + row.attachmentDirectory + '&from=upload&invoiceNo=' + row.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs" name="preview_link"><i class="fa fa-fw fa-eye"></i></a>';
                    //                     html += '<button type="button" name="viewUpload" class="btn btn-default ml-5 btn-xs" title="View Invoice" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-invoice-preview"><i class="fa fa-fw fa-eye"></i></button>'
                }
                return html
            }
        }, {
            targets: 8,
            'visible': false,
            "data": "companyId"

        }, {
            targets: 9,
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
                var row = inspection_approval_list.row(shippingChassisNoMatchIndexArr[i]['index']);
                var tr = $(row.node());
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                    tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                }
                let matchIdArr = shippingChassisNoMatchIndexArr[i]['matchIdArr'];
                if (matchIdArr.length > 0) {
                    var detailsElement = inspectionFormat(row.data());
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

    inspection_approval_list.on("click", 'a[name="preview_link"]', function() {
        let tr = $(this).closest('tr')
        let row = inspection_approval_list.row(tr)
        row.deselect();
        let data = row.data();
        data.attachmentViewed = 1;
        updateAttachmentViewedStatus(data.invoiceNo);
        //invoiceNo
        row.data(data).invalidate();
    })

    //expand details
    inspection_approval_list.on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = inspection_approval_list.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
        } else {
            inspection_approval_list.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = inspection_approval_list.row(rowIdx);
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

    $('#table-filter-search-chassisNo').keyup(function(e) {
        if (this.value.length >= 3 || this.value.length == 0 || e.keyCode == 13) {
            inspection_approval_list.search($(this).val()).draw();
        }

    });

    $.fn.dataTable.ext.search.push(function(settings, data, dataIndex) {
        if (settings.sTableId == 'table-inspection-invoice') {
            var term = $('#table-filter-search-chassisNo').val().toLowerCase();
            var orderItem = JSON.parse(data[9]);
            var row = inspection_approval_list.row(dataIndex);
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

    var modelEle = $('#modal-edit-details');
    var row, modalTriggerEle;
    modelEle.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalTriggerEle = $(event.relatedTarget);
        var tr = $(modalTriggerEle).closest('tr');
        var rowData = inspection_approval_list.row(tr).data();
        $(modelEle).find("#invoiceNo").val(rowData["invoiceNo"]);
        $(modelEle).find("#refNo").val(rowData["refNo"]);
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
        let refNo = $(modelEle).find('input[name="refNo"]').val();
        var queryString = "?invoiceNo=" + invoiceNo + "&dueDate=" + dueDate + "&refNo=" + refNo;

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            url: myContextPath + "/accounts/payment/inspection/edit-invoice" + queryString,
            contentType: false,
            success: function(data) {
                if (data.status == 'success') {

                    $(modelEle).modal('toggle');
                    inspection_approval_list.ajax.reload();

                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong>');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                }
            }
        });
    });

    //approve payment
    let payment_details_form = $('#modal-approve-payment form#payment-detail-form');
    $('#modal-approve-payment').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        if (inspection_approval_list.rows({
            selected: true,
            page: 'current'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return e.preventDefault();
        }
        var rowdata = inspection_approval_list.rows({
            selected: true,
            page: 'current'
        }).data();

        var element;
        var i = 0;
        inspection_approval_list.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = inspection_approval_list.row(this).data();
            if (data.invoiceUpload == 0) {
                alert($.i18n.prop('alert.invoice.not.uploaded'))
                return e.preventDefault();
            } else if (data.attachmentViewed == 0) {
                alert($.i18n.prop('alert.invoice.not.viewed'))
                return e.preventDefault();
            } else {
                var stockNo = ifNotValid(data.stockNo, '');
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

        let params = inspection_approval_list.rows({
            selected: true,
            page: 'current'
        }).data();
        //invoiceNo
        var data = new FormData();

        data.append("dueDate", $('#modal-approve-payment').find('input[name="dueDate"]').val());
        let response = approvePayment(data, $.param({
            "invoiceNo": params[0].invoiceNo
        }));
        if (response.status == 'success') {
            $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Payment Approved.');
            $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                $("#alert-block").slideUp(500);
            });
            $('#modal-approve-payment').modal('toggle');

            inspection_approval_list.ajax.reload();
            //setPaymentBookingDashboardStatus(data);
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
            url: myContextPath + "/accounts/payment/inspection/booked/approve?" + params,
            contentType: false,
            async: false,
            success: function(data) {
                result = data;
            }
        });
        return result;
    }

    var filterInspectionCompany;
    $('#table-filter-inspection').on("change", function() {
        filterInspectionCompany = $(this).find('option:selected').val();
        inspection_approval_list.draw();
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
        inspection_approval_list.draw();
    });
    $('.input-group').on('click', '.clear-date', function() {
        due_date = '';
        $('#table-filter-due-date').val('');
        $(this).remove();
        inspection_approval_list.draw();

    })

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //Transport filter
        if (typeof filterInspectionCompany != 'undefined' && filterInspectionCompany.length != '') {
            if (aData[8].length == 0 || aData[8] != filterInspectionCompany) {
                return false;
            }
        }

        //Due Date Filter
        if (typeof due_date != 'undefined' && due_date.length != '') {
            if (aData[3].length == 0 || aData[3] != due_date) {
                return false;
            }
        }

        return true;
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
        var data = inspection_approval_list.row($(targetElement).closest('tr')).data();
        $(this).find('input[name="invoiceNo"]').val(data.invoiceNo);
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
        let invoiceNo = $('#modal-invoice-upload input[name="invoiceNo"]').val();
        let response = uploadFile(data, invoiceNo)
        if (response.status == 'success') {
            $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Invoice uploaded.');
            $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                $("#alert-block").slideUp(500);
            });
            $('#modal-invoice-upload').modal('toggle');
            inspection_approval_list.ajax.reload();
        }
    })
    var uploadFile = function(data, invoiceNo) {
        let result;
        let queryString = "?invoiceNo=" + invoiceNo
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
            url: myContextPath + "/accounts/payment/inspection/invoice/upload" + queryString,
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
        row.attr('data-id', rowData.items[i].chassisNo);
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

function updateAttachmentViewedStatus(invoiceNo) {
    let queryString = "?invoiceNo=" + invoiceNo
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        url: myContextPath + "/accounts/payment/inspection/update/attachmentView" + queryString,
        contentType: false,
        async: false,
        success: function(data) {
            console.log("viewed")
        }
    });
}
  function setPaymentBookingDashboardStatus(data) {
        $('#count-approval-auction').html(data.auctionApproval);
        $('#count-approval-transport').html(data.transportApproval);
        $('#count-approval-freight').html(data.freight);
        $('#count-approval-others').html(data.others);
        $('#count-approval-storage').html(data.storage);
        // $('#count-prepayment').html(data.paymentAdvance);

    }