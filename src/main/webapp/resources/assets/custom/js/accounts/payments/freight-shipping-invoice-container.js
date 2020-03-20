var supplierJson, forwarderJson, forwarderDetailJson;
$(function() {

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

    //table shipping requested

    $('.datepicker ').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: true
    }).on('change', function() {
        $(this).valid();

    });
    var table_invoice_created_container_Ele = $('#table-roro-invoice-created');
    var table_invoice_created_container = table_invoice_created_container_Ele.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/freight/shipping/container/invoice",
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
                    return '<input class="selectBox"  name="selRow" type="checkbox" value="' + row.invoiceNo + '">';
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
            "data": "remitTo"
        }, {
            targets: 4,
            "data": "dueDate",
            "className": "details-control",
            "className": "vcenter"
        }, {

            targets: 5,
            "className": "dt-right",
            "type": "num-fmt",
            "data": "total",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 6,
            "data": "supplierId",

            "render": function(data, type, row) {
                var html = '';

                html += '<button type="button" name="fileupload" class="btn btn-primary ml-5 btn-xs" title="invoice upload" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-invoice-upload"><i class="fa fa-fw fa-cloud-upload"></i></button>'
                html += '<button type="button" class="btn btn-default ml-5 btn-xs" title="Show invoice items" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-container-invoice-items"><i class="fa fa-fw fa-file-text-o"></i></button>'
                if (row.invoiceUpload == 1) {
                    html += '<button type="button" name="viewUpload" class="btn btn-default ml-5 btn-xs" title="View Invoice" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-invoice-preview"><i class="fa fa-fw fa-eye"></i></button>'
                }

                return html;
            }
        }, {
            targets: 7,
            "className": "details-control",
            "data": "forwarderId",
            "visible": false

        }],

        "drawCallback": function(settings, json) {
            table_invoice_created_container_Ele.find('input.autonumber,span.autonumber').autoNumeric('init')

        }

    });

    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    }
    ;// Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table_invoice_created_container.search(query, true, false).draw();
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

        let params = table_invoice_created_container.rows({
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
            if (aData[7].length == 0 || aData[7] != filterSupplierName) {
                return false;
            }
        }
        return true
    })

    //file uplaod
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
    let modal_container_invoice_item = $("#modal-container-invoice-items");
    modal_container_invoice_item.on('show.bs.modal', function(e) {
        var targetElement = $(e.relatedTarget);
        var data = table_invoice_created_container.row($(targetElement).closest('tr')).data();
        let invoiceNo = data.invoiceNo;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            url: myContextPath + "/freight/shipping/container/invoice/items/" + invoiceNo,
            contentType: false,
            async: true,
            success: function(data) {
                initInvoiceItemsDatatable(data)
            }
        });

    })
    let table_invoice_item = $('#modal-container-invoice-items table#table-invoice-items');
    function initInvoiceItemsDatatable(data) {
        table_invoice_item.DataTable({
            "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
            "pageLength": 25,
            "destroy": true,
            "language": {
                "zeroRecords": " "
            },
            "bPaginate": false,
            "data": data,
            columnDefs: [{
                "targets": '_all',
                "defaultContent": ""
            }, {
                "targets": 0,
                "data": "chassisNo"
            }, {
                targets: 1,
                "data": "maker"
            }, {
                targets: 2,
                "data": "model"
            }, {
                targets: 3,
                "data": "m3"
            }, {
                targets: 4,
                "data": "amount",
                "render": function(data, type, row) {
                    data = data == null ? '' : data;
                    if (type === 'display') {
                        return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
                    }
                }
            }],

            "drawCallback": function(settings, json) {
                table_invoice_item.find('input.autonumber,span.autonumber').autoNumeric('init')

            }
        });
    }
})

function setPaymentBookingDashboardStatus(data) {
    $('#count-auction').html(data.auction);
    $('#count-transport').html(data.transport);
    $('#count-freight').html(data.freight);
    $('#count-others').html(data.others);
    $('#count-storage').html(data.storage);
    $('#count-prepayment').html(data.paymentAdvance);

}

function formatRoroCreatedInvoiceContainer(rowData) {
    var element = $('#clone-container>#invoice-details>.clone-element').clone();
    $(element).find('input[name="invoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.items.length; i++) {
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.quantity').html(ifNotValid(rowData.items[i].quantity, ''));
        $(row).find('td.description').html(ifNotValid(rowData.items[i].description, ''));
        $(row).find('td.usd>span').html(ifNotValid(rowData.items[i].usd, ''));
        $(row).find('td.zar>span').html(ifNotValid(rowData.items[i].zar, ''));
        $(row).find('td.unitPrice>span').html(ifNotValid(rowData.items[i].unitPrice, ''));
        $(row).find('td.amount>span').html(ifNotValid(rowData.items[i].amount, ''));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);

    }

    return element;
}
