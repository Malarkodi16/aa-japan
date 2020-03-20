var supplierJson, table;
$(function() {
    let shippingChassisNoMatchIndexArr = [];
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })

    $.getJSON(myContextPath + "/data/accounts/payment-booking-count", function(data) {
        setPaymentBookingDashboardStatus(data.data)
    });
    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
        supplierJson = data;
        //Auction Company Filter
        $('#auctionCompany').select2({
            allowClear: true,
            width: '100%',
            placeholder: 'All',
            data: $.map(supplierJson, function(item) {
                return {
                    id: item.supplierCode,
                    text: item.company
                };
            })
        }).on("change", function(event) {
            filterSupplierName = $(this).find('option:selected').val();
            table.draw();
        })
    });

    $('#stockNo').select2({
        allowClear: true,
        minimumInputLength: 2,
        width: "100%",
        ajax: {
            url: myContextPath + "/purchased/stock/stockNo-search",
            dataType: 'json',
            delay: 500,
            data: function(params) {
                var query = {
                    search: params.term,
                    type: 'public'
                }
                return query;

            },
            processResults: function(data) {
                var results = [];
                data = data.data;
                if (data != null && data.length > 0) {
                    $.each(data, function(index, item) {
                        results.push({
                            id: item.code,
                            text: item.stockNo + ' :: ' + item.chassisNo
                        });
                        // $(cancellationModal).find('input[name="purchaseInvoiceId"]').val(item.code)
                    });
                }
                return {
                    results: results
                }
            }
        }

    });
    $('.amount').autoNumeric('init');

    var due_date;
    $('#table-filter-due-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: false
    }).on("change", function(e, picker) {
        due_date = $(this).val();
        $(this).closest('.input-group').find('.due-clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon due-clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.draw();
    });
    $('.input-group').on('click', '.due-clear-date', function() {
        due_date = '';
        $('#table-filter-due-date').val('');
        $(this).remove();
        table.draw();

    })

    var invoice_date;
    $('#table-filter-invoice-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: false
    }).on("change", function(e, picker) {
        invoice_date = $(this).val();
        $(this).closest('.input-group').find('.invoice-clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon invoice-clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.draw();
    });
    $('.input-group').on('click', '.invoice-clear-date', function() {
        invoice_date = '';
        $('#table-filter-invoice-date').val('');
        $(this).remove();
        table.draw();

    })

    $('.select2-select').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true

    })

    var tableEle = $('#table-auction-payment');
    table = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": true,
        "search": {
            "smart": false,
        },
        "ajax": {
            url: myContextPath + "/accounts/auction-data",

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
            "data": "invoiceDate"

        }, {
            targets: 2,
            "className": "details-control",
            "data": "invoiceNo"

        }, {
            targets: 3,
            "className": "details-control",
            "data": "auctionRefNo"

        }, {
            targets: 4,
            "className": "details-control",
            "data": "invoiceName"
        }, {
            targets: 5,
            "data": "dueDate",
            "className": "details-control",
            "className": "vcenter"
        }, {

            targets: 6,
            "className": "dt-right",
            "type": "num-fmt",
            "data": "totalAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                //    $.fn.dataTable.render.number( ',', '.', 2, '¥' )
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
            "data": "supplierId",
            "visible": false
        }, {
            targets: 9,
            "data": "remarks"
        }, {
            targets: 10,
            "data": "supplierId",
            "width": "150px",
            "render": function(data, type, row) {
                var html = '';
                html = '<a class="ml-5 btn btn-info btn-xs" title="Edit" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-edit-details"><i class="fa fa-fw fa-edit"></i></a>'
                //html += '<button type="button" data-value="' + row.invoiceNo + '" name="cancel-invoice" class="btn btn-warning ml-5 btn-xs" title="Cancel Invoice"><i class="fa fa-times"></i></button>'
                html += '<button type="button" data-value="' + row.invoiceNo + '" class="ml-5 btn-xs btn btn-danger fa fa-close" data-toggle="modal" data-target="#modal-remark" data-backdrop="static" data-keyboard="false" id="deleteButton"></button>'
                html += '<button type="button" name="fileupload" class="btn btn-primary ml-5 btn-xs" title="invoice upload" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-invoice-upload"><i class="fa fa-fw fa-cloud-upload"></i></button>'
                if (row.invoiceUpload == 1) {
                    // html += '<button type="button" name="viewUpload" class="btn btn-default ml-5 btn-xs" title="View Invoice" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-invoice-preview"><i class="fa fa-fw fa-eye"></i></button>'
                    html += '<a href="' + myContextPath + '/get/' + row.invoiceAttachmentDiskFilename + '?path=' + row.attachmentDirectory + '&from=upload&invoiceNo=' + row.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs" name="preview_link"><i class="fa fa-fw fa-eye"></i></a>';
                }
                // html += '<button type="button" name="addAmount" class="btn btn-info  ml-5 btn-xs" title="Add Cancellation Charge" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-cancellation-amount"><i class="fa fa-plus"></i></button>'

                return html;
            }
        }, {
            targets: 11,
            data: 'approvePaymentItems',
            "visible": false,
            "render": function(data, type, row) {
                return JSON.stringify(data)
            }
        }],

        //         "drawCallback": function(settings, json) {
        //         },
        //        "bStateSave": true,  data-value="' + row.invoiceNo + '"
        "fnStateSave": function(oSettings, oData) {
            localStorage.setItem('offersDataTables', JSON.stringify(oData));
        },
        "fnStateLoad": function(oSettings) {
            return JSON.parse(localStorage.getItem('offersDataTables'));
        },
        "fnDrawCallback": function(oSettings) {
//             $(oSettings.nTHead).hide();
            tableEle.find('input.autonumber,span.autonumber').autoNumeric('init')
            shippingChassisNoMatchIndexArr = getUnique(shippingChassisNoMatchIndexArr, 'index');
            for (let i = 0; i < shippingChassisNoMatchIndexArr.length; i++) {
                var row = table.row(shippingChassisNoMatchIndexArr[i]['index']);
                var tr = $(row.node());
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                    tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                }
                let matchIdArr = shippingChassisNoMatchIndexArr[i]['matchIdArr'];
                if (matchIdArr.length > 0) {
                    var detailsElement = format(row.data());
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

    $('#table-filter-search-chassisNo').keyup(function(e) {
        transportItemCount = 0;
        if (this.value.length >= 3 || this.value.length == 0 || e.keyCode == 13) {
            table.search($(this).val()).draw();
        }

    });

    $("tbody tr").on("click", function() {
        //loop through all td elements in the row
        $(this).find("td").each(function(i) {
            //toggle between adding/removing the 'active' class
            $(this).toggleClass('activeRow');
        });
    });
    table.on("click", 'a[name="preview_link"]', function() {
        let tr = $(this).closest('tr')
        let row = table.row(tr)
        row.deselect();
        let data = row.data();
        data.attachementViewed = 1;
        row.data(data).invalidate();
        tr.find('input.autonumber,span.autonumber').autoNumeric('init')
    })
    table.on("click", 'td>input[type="checkbox"]', function() {
        $(tableEle).find("input[type=checkbox]:checked").not(this).prop("checked", false)
    })
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

    //     
    $.fn.dataTable.ext.search.push(function(settings, data, dataIndex) {
        if (settings.sTableId == 'table-auction-payment') {
            var term = $('#table-filter-search-chassisNo').val().toLowerCase();
            var orderItem = JSON.parse(data[11]);
            var row = table.row(dataIndex);
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
        var row = table.row(tr);
        var rowData = row.data();
        rowDataRemarkStockNo = ifNotValid(rowData.invoiceNo, '')
    }).on('hidden.bs.modal', function() {
        resetElementInput(this)
    }).on("click", '#btn-remark-submit', function(event) {
        let invoiceId = $(this).data("value");
        if (!confirm($.i18n.prop('common.confirm.cancel'))) {
            return false;
        }
        if (!$('#remarkForm').find('textarea[name="cancellationRemarks"]').valid()) {
            return false;
        }
        var data = {};
        data["cancellationRemarks"] = $('#modal-remark').find('textarea[name="cancellationRemarks"]').val();
        data["invoiceId"] = rowDataRemarkStockNo
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
            url: myContextPath + "/accounts/payment/cancel/auction/invoice",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    $('#modal-remark').modal('toggle');
                    table.row(tr).remove().draw();
                }
            }
        });
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
    })

    var filterSupplierName = $('#auctionCompany').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {

        //Supplier filter
        if (typeof filterSupplierName != 'undefined' && filterSupplierName.length != '') {
            if (aData[8].length == 0 || aData[8] != filterSupplierName) {
                return false;
            }
        }

        //Due Date Filter
        if (typeof due_date != 'undefined' && due_date.length != '') {
            if (aData[5].length == 0 || aData[5] != due_date) {
                return false;
            }
        }

        //Invoice Date Filter
        if (typeof invoice_date != 'undefined' && invoice_date.length != '') {
            if (aData[1].length == 0 || aData[1] != invoice_date) {
                return false;
            }
        }

        return true;
    });

    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

    });
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
    // Approve Payment
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
        var rowData = table.row(tr).data();
        $(modelEle).find("#invoiceNo").val(rowData["invoiceNo"]);
        $(modelEle).find("#dueDate").datepicker('setDate', rowData["dueDate"]);
        $(modelEle).find("#auctionRefNo").val(rowData["auctionRefNo"]);
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
        let auctionRefNo = $(modelEle).find('input[name="auctionRefNo"]').val();
        let remarks = $(modelEle).find('input[name="remarks"]').val();
        var queryString = "?invoiceNo=" + invoiceNo + "&dueDate=" + dueDate + "&auctionRefNo=" + auctionRefNo + "&remarks=" + remarks;

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            url: myContextPath + "/accounts/payment/edit/auction-invoice" + queryString,
            contentType: false,
            success: function(data) {
                if (data.status == 'success') {

                    $(modelEle).modal('toggle');
                    //                     var row = table.row($(triggerEle).closest('tr'));
                    //                     if (!isEmpty(data.data)) {
                    //                         row.data(data.data).invalidate();
                    //                     }
                    table.ajax.reload();

                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong>');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                }
            }
        });
    })
    //file uplaod
    $('#modal-invoice-upload').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(e.relatedTarget);
        var data = table.row($(targetElement).closest('tr')).data();
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
            table.ajax.reload();

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
            url: myContextPath + "/accounts/approve/payment/auction/upload/invoice?id=" + id,
            contentType: false,
            async: false,
            success: function(data) {
                result = data;
            }
        });
        return result;
    }
    $('#modal-approve-payment').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return e.preventDefault();
        }
        var rowdata = table.rows({
            selected: true,
            page: 'current'
        }).data();
        var element;
        var i = 0;
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = table.row(this).data();
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

        let params = table.rows({
            selected: true,
            page: 'current'
        }).data().toArray().map(row=>row.id);

        var data = new FormData();

        data.append("dueDate", $('#modal-approve-payment').find('input[name="dueDate"]').val());
        approvePayment(data, $.param({
            "id": params
        }));
        $.getJSON(myContextPath + "/data/accounts/payment-booking-count", function(data) {
            setPaymentBookingDashboardStatus(data.data)
        });

    })

    var approvePayment = function(data, params) {
        let result;
        $('#spinner').show();
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
            url: myContextPath + "/accounts/approve/payment/auction?" + params,
            contentType: false,
            success: function(data) {
                if (data.status == 'success') {
                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Payment Approved.');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                    $('#modal-approve-payment').modal('toggle');

                    table.ajax.reload();
                    setPaymentBookingDashboardStatus(data);
                }
            }
        });
        return result;
    }

    //Add Cancellation Charge
    var cancellationModal = $('#modal-cancellation-amount')
    $(cancellationModal).on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(e.relatedTarget);
        var data = table.row($(targetElement).closest('tr')).data();
        $(this).find('input[name="invoiceId"]').val(data.invoiceNo);
    }).on('hidden.bs.modal', function() {
        $(this).find('input').val('');
    }).on('click', '#charge-save', function() {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        var data = {};
        data['invoiceId'] = $(cancellationModal).find('input[name="invoiceId"]').val();
        data['purchaseInvoiceId'] = $(cancellationModal).find('select[name="stockNo"]').val();
        data['amount'] = $(cancellationModal).find('input[name="amount"]').autoNumeric('get');
        let response = updateCancellationCharge(data)
        if (response.status == 'success') {
            $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Invoice uploaded.');
            $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                $("#alert-block").slideUp(500);
            });
            $('#modal-cancellation-amount').modal('toggle');
            table.ajax.reload();
        }
    })
    var updateCancellationCharge = function(data) {
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
            url: myContextPath + "/accounts/payment/auction-cancellation-charge",
            async: false,
            contentType: "application/json",
            success: function(data) {
                result = data;
            }
        });
        return result;
    }

});

function updateAttachmentViewedStatus(invoiceNo) {
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        url: myContextPath + "/accounts/payment/update/attachmentView/others?id=" + invoiceNo,
        contentType: false,
        async: false,
        success: function(data) {
            console.log("viewed")
        }
    });
}

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
        if (rowData.approvePaymentItems[i].type == 'CANCELLATION CHARGES') {
            row.css('color', 'blue');
        }
        purchaseCostTotal += Number(ifNotValid(rowData.approvePaymentItems[i].purchaseCost, 0));
        purchaseCostTaxTotal += Number(ifNotValid(rowData.approvePaymentItems[i].purchaseCostTaxAmount, 0));
        commisionTotal += Number(ifNotValid(rowData.approvePaymentItems[i].commision, 0));
        commisionTaxTotal += Number(ifNotValid(rowData.approvePaymentItems[i].commisionTaxAmount, 0));
        roadTaxTotal += Number(ifNotValid(rowData.approvePaymentItems[i].roadTax, 0));
        recycleTotal += Number(ifNotValid(rowData.approvePaymentItems[i].recycle, 0));
        otherChargesTotal += Number(ifNotValid(rowData.approvePaymentItems[i].otherCharges, 0));
        otherChargesTaxTotal += Number(ifNotValid(rowData.approvePaymentItems[i].othersCostTaxAmount, 0));

        row.attr('data-id', rowData.approvePaymentItems[i].stockNo);
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
function setPaymentBookingDashboardStatus(data) {
    $('#count-approval-auction').html(data.auctionApproval);
    $('#count-approval-transport').html(data.transportApproval);
    $('#count-approval-freight').html(data.freight);
    $('#count-approval-others').html(data.others);
    $('#count-approval-storage').html(data.storage);
    // $('#count-prepayment').html(data.paymentAdvance);

}
