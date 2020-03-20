var invoiceTypeFlag = 0, supplierJson, bankJson;
let modal_approve_item_payment, form_container_allocation_element, payment_processing_step = 0;
$(function() {

    $.getJSON(myContextPath + '/data/bank.json', function(data) {
        bankJson = data;
        getBankData('#bank', bankJson);
    })

    var table_element = $('#table-auction-payment');
    var table = table_element.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": true,
        "ajax": {
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            url: myContextPath + "/accounts/payment-data",
            data: function(data) {
                data.flag = invoiceTypeFlag;
            }
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
                    return '<input class="selectBox selRow" name="selRow" id="selRow_' + data + '" type="checkbox"  data-stockno="' + row.stockNo + '" value="' + data + '">';
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
            "data": "refNo"

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
            "data": "invoiceName",
            "render": function(data, type, row) {
                var data;
                data = data == null ? '' : data;
                if (!isEmpty(row.remitter)) {
                    if (row.remitter.toLowerCase() == 'others') {
                        data = row.remitterOthers;
                        return data;
                    } else {
                        return data;
                    }
                }
                return data;

            }
        }, {
            targets: 6,
            "data": "auctionHouseName",
            "className": "details-control",
        }, {
            targets: 7,
            "data": "dueDate",
            "className": "details-control",
        }, {

            targets: 8,
            "className": "dt-right",
            "type": "num-fmt",
            "data": "totalAmount",
            "render": function(data, type, row) {
                return '<span class="autonumber"data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {

            targets: 9,
            "className": "dt-right",
            "type": "num-fmt",
            "data": "invoiceAmountReceived",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + ifNotValid(row.invoiceAmountReceived, 0) + '</span>';
            }
        }, {

            targets: 10,
            "className": "dt-right",
            "type": "num-fmt",
            "data": "invoiceBalanceAmount",
            "render": function(data, type, row) {
                return '<span class="autonumber"data-a-sign="¥ " data-m-dec="0">' + ifNotValid(row.invoiceBalanceAmount, 0) + '</span>';
            }
        }, {

            targets: 11,
            "className": "dt-right",
            "type": "num-fmt",
            "visible": false,
            "render": function(data, type, row) {
                let html = "";
                if (!(row.totalAmountUsd == 0 && row.containerUsd > 0)) {
                    html = '<span class="autonumber"data-a-sign="$ " data-m-dec="0">' + ifNotValid(row.totalAmountUsd, 0.0) + '</span>'
                } else {
                    html = '<span class="autonumber"data-a-sign="$ " data-m-dec="0">' + ifNotValid(row.containerUsd, 0.0) + '</span>'
                }
                return html;
            }
        }, {

            targets: 12,
            "className": "dt-right",
            "type": "num-fmt",
            "visible": false,
            "data": "invoiceBalanceAmountUsd",
            "render": function(data, type, row) {
                return '<span class="autonumber"data-a-sign="$ " data-m-dec="0">' + ifNotValid(row.invoiceBalanceAmountUsd, 0) + '</span>';
            }
        }, {
            targets: 13,
            "visible": false,
            "data": "supplierId",
            "className": "details-control"
        }, {
            targets: 14,
            "data": "remarks",
            "className": "details-control"
        }, {
            targets: 15,
            "data": "refAttachmentDiskFilename",
            "width": "120px",
            "render": function(data, type, row) {
                var html = '';
                html += '<button type="button" name="fileupload" class="btn btn-primary ml-5 btn-xs" title="invoice upload" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-invoice-upload"><i class="fa fa-fw fa-cloud-upload"></i></button>'

                if (row.attachmentDirectory == "auction_invoice") {
                    html += '<a href="' + myContextPath + '/get/' + row.invoiceAttachmentDiskFilename + '?path=' + row.attachmentDirectory + '&from=upload&invoiceNo=' + row.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-eye"></i></a>';
                    html += '<a href="#" class="ml-5 btn btn-primary btn-xs" data-backdrop="static" data-keyboard="false" data-toggle="modal" title="Edit Due Date" data-target="#modal-edit-dueDate"><i class="fa fa-fw fa-edit"></i></a>';
                } else {
                    html += '<a href="' + myContextPath + '/get/' + row.invoiceAttachmentDiskFilename + '?path=' + row.attachmentDirectory + '&from=upload&invoiceNo=' + row.invoiceNo + '" target="_blank" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-eye"></i></a>';
                    //                     html += '<a href="#" class="ml-5 btn btn-default btn-xs" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-invoice-preview"><i class="fa fa-fw fa-eye"></i></a>';
                }

                if (row.type == "FREIGHT") {

                    if (row.invoiceType == 1) {
                        html += '<a href="#" class="ml-5 btn btn-success btn-xs" data-backdrop="static" data-keyboard="false" data-toggle="modal" title="Complete payment" data-target="#modal-approve-payment-items"><i class="fa fa-fw fa-check"></i></a>';
                    } else if (row.invoiceType == 2) {
                        html += '<a href="#" class="ml-5 btn btn-success btn-xs btn-approve-payment" data-backdrop="static" data-keyboard="false" data-toggle="modal" title="Complete payment" data-target="#modal-approve-payment"><i class="fa fa-fw fa-check"></i></a>';

                        html += '<button type="button" class="btn btn-default ml-5 btn-xs" title="Show invoice items" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-container-invoice-items"><i class="fa fa-fw fa-file-text-o"></i></button>'
                    }

                }

                return html
            }

        }],
        "footerCallback": function(row, data, start, end, display) {
            var table = this.api();
            updateFooter(table);
        },
        "drawCallback": function(settings, json) {
            table_element.find('span.autonumber').autoNumeric('init')

        },
        /*excel export*/
        buttons: [{
            extend: 'excel',
            text: 'Export All',
            title: '',
            filename: function() {
                var d = new Date();
                return 'payment-processing_' + d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();
            },
            attr: {
                type: "button",
                id: 'dt_excel_export_all'
            },
            exportOptions: {
                columns: [1, 2, 3, 4, 5, 6, 7, 8, 9]
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
                    v: 'Payment Processing'
                }]);
                var r4 = Addrow(4, [{
                    k: 'A',
                    v: 'Date'
                }, {
                    k: 'B',
                    v: currentDate
                }]);

                sheet.childNodes[0].childNodes[1].innerHTML = r2 + r3 + r4 + sheet.childNodes[0].childNodes[1].innerHTML;
            }
        }]

    });
    $("#excel_export_all").on("click", function() {
        table.button("#dt_excel_export_all").trigger();

    });
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
            $(table_element).find("th.select-checkbox>input").removeClass("selected");
            $(table_element).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(table_element).find("th.select-checkbox>input").addClass("selected");
            $(table_element).find("th.select-checkbox>input").prop('checked', true);
        }

    }).on("deselect", function() {
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() !== table.rows({
            page: 'current'
        }).count()) {
            $(table_element).find("th.select-checkbox>input").removeClass("selected");
            $(table_element).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(table_element).find("th.select-checkbox>input").addClass("selected");
            $(table_element).find("th.select-checkbox>input").prop('checked', true);

        }

    });
    $('.statusFilter').click(function() {
        var value = $(this).data("custom-value");
        $("#invoiceType").val(value);
        $.ajax({
            "url": myContextPath + "/accounts/auction-data",
            "data": {
                "status": $("#invoiceType").val()
            }
        }).done(function(result) {
            table.clear();
            table.rows.add(result.data).draw();
        }).fail(function(jqXHR, textStatus, errorThrown) {// needs to implement if it fails
        });

    });
    $('#invoiceTypeFilter').select2({
        width: '100%',
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true,
    }).change(function() {
        invoiceTypeFlag = $(this).val();
        if (isEmpty(invoiceTypeFlag)) {
            $('div.supplirFilter').hide();
        } else if (invoiceTypeFlag == 0) {
            $('div.supplirFilter').hide()
            $('div#purchasedSupplierWrapper').show();
        } else if (invoiceTypeFlag == 1) {
            $('div.supplirFilter').hide()
            $('div#transportSupplierWrapper').show();
        } else if (invoiceTypeFlag == 2 || invoiceTypeFlag == 3) {
            $('div.supplirFilter').hide()
            $('div#forwarderSupplierWrapper').show();
        } else if (invoiceTypeFlag == 4) {
            $('div.supplirFilter').hide()
            $('div#genaralSupplierWrapper').show();
        } else if (invoiceTypeFlag == 5) {
            $('div.supplirFilter').hide()
            $('div#inspectionCompanyWrapper').show();
        }

        let tFootEle = $('#table-auction-payment').find('tfoot>tr');

        if (invoiceTypeFlag == 3) {
            table.column(11).visible(true)
            table.column(12).visible(true)
            $('#approvePayment').hide();
            tFootEle.find('th.dollarValue').removeClass('hidden');
        } else {
            $('#approvePayment').show();
            table.column(11).visible(false)
            table.column(12).visible(false)
            tFootEle.find('th.dollarValue').addClass('hidden');
        }

        if (invoiceTypeFlag == 1) {
            table.column(3).visible(true)
        } else {
            table.column(3).visible(false)
        }

        if (invoiceTypeFlag == 0) {
            table.column(4).visible(true)
            table.column(6).visible(true)
        } else {
            table.column(4).visible(false)
            table.column(6).visible(false)
        }
        table.ajax.reload()
        //table Draw
    })
    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\./gi, "").replace(/¥/g, "");
    }
    ;$('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });
    $('#table-auction-payment tbody').on('click', 'tr td .selRow', function() {
        var row = $(this).closest('tr');
        if (!row.hasClass('selected')) {
            row.addClass('selected');
        } else {
            row.removeClass('selected');
        }
        var selRowCount = $('.selected').length;
        if (selRowCount > 0) {

            $('#approvePayment').prop("disabled", false);
        } else {
            $('#approvePayment').prop("disabled", true);
        }
    });
    $('#select-all').click(function() {
        //	 $('#table-auction-payment thead').on('click', 'tr td #select-all', function () {
        if ($('#select-all').prop('checked') == true) {
            $('#approvePayment').prop("disabled", false);
        } else {
            $('#approvePayment').prop("disabled", true);
        }
        $('input:checkbox').not(this).prop('checked', this.checked);
    });

    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: true
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

    var invoiceDateFilter;
    $('.invoiceDateDatepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: true
    }).on("change", function(e, picker) {
        invoiceDateFilter = $(this).val();
        table.draw();
    });

    var purchasedSupplierFilter, transportSupplierFilter, forwarderSupplierFilter, genaralSupplierFilter, inspectionCompanyFilter;
    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
        supplierJson = data;
        $('select#supplier').select2({
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
            purchasedSupplierFilter = $(this).val();
            table.draw();
        })
    });
    $.getJSON(myContextPath + "/data/transporters.json", function(data) {
        transportersJson = data;
        $('select#transportSupplier').select2({
            allowClear: true,
            width: '100%',
            data: $.map(transportersJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).on("change", function(event) {
            transportSupplierFilter = $(this).val();
            table.draw();
        })

    });
    $.getJSON(myContextPath + "/data/forwarders.json", function(data) {
        $('select#forwarderSupplier').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).on("change", function(event) {
            forwarderSupplierFilter = $(this).val();
            table.draw();
        })
    })
    $.getJSON(myContextPath + "/data/general/suppliers.json", function(data) {
        remitterJson = data;
        $('select#genaralSupplier').select2({
            allowClear: true,
            width: '100%',
            data: $.map(remitterJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).on("change", function(event) {
            genaralSupplierFilter = $(this).val();
            table.draw();
        })

    })
    $.getJSON(myContextPath + "/data/inspectioncompanies.json", function(data) {
        inspCompanyJson = data;
        $('select#inspectionCompany').select2({
            allowClear: true,
            width: '100%',
            data: $.map(inspCompanyJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).on("change", function(event) {
            inspectionCompanyFilter = $(this).val();
            table.draw();
        })

    })
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //filter

        if (invoiceTypeFlag == 0 && typeof purchasedSupplierFilter != 'undefined' && purchasedSupplierFilter.length != '') {
            if (aData[13].length == 0 || aData[13] != purchasedSupplierFilter) {
                return false;
            }
        } else if (invoiceTypeFlag == 1 && typeof transportSupplierFilter != 'undefined' && transportSupplierFilter.length != '') {
            if (aData[13].length == 0 || aData[13] != transportSupplierFilter) {
                return false;
            }
        } else if ((invoiceTypeFlag == 2 || invoiceTypeFlag == 3) && typeof forwarderSupplierFilter != 'undefined' && forwarderSupplierFilter.length != '') {
            if (aData[13].length == 0 || aData[13] != forwarderSupplierFilter) {
                return false;
            }
        } else if (invoiceTypeFlag == 4 && typeof genaralSupplierFilter != 'undefined' && genaralSupplierFilter.length != '') {
            if (aData[13].length == 0 || aData[13] != genaralSupplierFilter) {
                return false;
            }
        } else if (invoiceTypeFlag == 5 && typeof inspectionCompanyFilter != 'undefined' && inspectionCompanyFilter.length != '') {
            if (aData[13].length == 0 || aData[13] != inspectionCompanyFilter) {
                return false;
            }
        }

        if (typeof dueDateFilter != 'undefined' && dueDateFilter.length != '') {
            if (aData[7].length == 0 || aData[7] != dueDateFilter) {
                return false;
            }
        }
        if (typeof invoiceDateFilter != 'undefined' && invoiceDateFilter.length != '') {
            if (aData[1].length == 0 || aData[1] != invoiceDateFilter) {
                return false;
            }
        }
        return true;
    })

    $('#table-auction-payment tbody').on('click', 'td.details-control', function() {
        // $(this).closest('tbody').find('.container-fluid').hide();
        var tr = $(this).closest('tr');
        var row = table.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
        } else {
            table.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = table.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    $(row.node()).removeClass('shown');
                }

            })
            var rowData = row.data();
            var childHtml = '';
            if (invoiceTypeFlag == 0) {
                childHtml = format(rowData)
            } else if (invoiceTypeFlag == 1) {
                childHtml = transportFormat(rowData)
            } else if (invoiceTypeFlag == 2) {
                childHtml = forwarderFormat(rowData)
            } else if (invoiceTypeFlag == 3) {
                if (rowData.invoiceType == 1) {
                    childHtml = freightShippingFormat(rowData)
                } else {
                    childHtml = formatInvoiceContainerDetails(rowData)
                }

            } else if (invoiceTypeFlag == 4) {
                childHtml = tinvFormat(rowData)
            } else if (invoiceTypeFlag == 5) {
                childHtml = inspectionFormat(rowData)
            }

            row.child(childHtml).show();
            row.child().find('span.autonumber').autoNumeric('init');
            tr.addClass('shown');

        }
    });

    //approve payment
    $('.select2-select').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true

    })

    //file uplaod
    $('#modal-invoice-upload').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(e.relatedTarget);
        var data = table.row($(targetElement).closest('tr')).data();
        $(this).find('input[name="refNo"]').val(data.refNo);
        $(this).find('input[name="invoiceId"]').val(data.invoiceNo);
    }).on('hidden.bs.modal', function() {
        $(this).find('input').val('');
    }).on('click', '#upload', function() {

        if (!$('#modal-invoice-upload form#form-file-upload').valid()) {
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
        let refNo = $('#modal-invoice-upload input[name="refNo"]').val();
        let filterValue = $("#invoiceTypeFilter").val();
        let url;
        let queryString;
        if (filterValue == 0) {
            url = "/accounts/approve/payment/auction/upload/invoice"
            queryString = "?id=" + id
        } else if (filterValue == 1) {
            url = "/accounts/approve/payment/transport/invoice/upload"
            queryString = "?invoiceRefNo=" + id + "&refNo=" + refNo
        } else if (filterValue == 2) {
            url = "/accounts/approve/payment/storageandphotos/invoice/upload"
            queryString = "?id=" + id
        } else if (filterValue == 3) {
            url = "/accounts/approve/payment/shipping/invoice/upload"
            queryString = "?id=" + id
        } else if (filterValue == 4) {
            url = "/accounts/cc"
            queryString = "?id=" + id
        } else if (filterValue == 5) {
            url = "/accounts/payment/inspection/invoice/upload"
            queryString = "?invoiceNo=" + id
        }
        let response = uploadFile(data, url, queryString)
        if (response.status == 'success') {
            $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Invoice uploaded.');
            $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                $("#alert-block").slideUp(500);
            });
            $('#modal-invoice-upload').modal('toggle');
            table.ajax.reload();

        }
    })
    var uploadFile = function(data, url, queryString) {
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
            url: myContextPath + url + queryString,
            contentType: false,
            async: false,
            success: function(data) {
                result = data;
            }
        });
        return result;
    }
    let payment_details_form = $('#modal-approve-payment form#payment-detail-form');

    //image_preview
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

    }).on('click', 'button#btn-print', function(e) {
        let path = $(this).closest('.modal').find('.modal-body>embed').attr('src');
        let data = {};
        data["printable"] = path;
        data["type"] = 'image';
        printJS(data);
    })
    modal_approve_item_payment = $("#modal-approve-payment-items");
    form_container_allocation_element = modal_approve_item_payment.find('form#payment-detail-form');
    payment_processing_step = 0;
    let itemContainer = modal_approve_item_payment.find('ul#itemContainer');
    var rowData;
    modal_approve_item_payment.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        let triggerEle = $(event.relatedTarget)
        payment_processing_step = 0;
        showTab(payment_processing_step);
        let tr = triggerEle.closest('tr');
        var row = table.row(tr);
        let item_table = $(row.child()).find('table');
        let selected_items = $(item_table).find('tr>td>input[type="checkbox"]:checked').toArray()
        if (selected_items.length == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return event.preventDefault();
        }
        let rowData = row.data();
        let totalAmountWithoutFreightInYen = selected_items.reduce(function(a, b) {
            return a + Number($(b).attr('data-total-without-freight-yen'))
        }, 0)
        let totalFreightInYen = selected_items.filter(item=>$(item).attr('data-freight-payment-status') == 0).reduce(function(a, b) {
            return a + Number($(b).attr('data-freight-in-yen'))
        }, 0)
        let totalFreightInUsd = selected_items.filter(item=>$(item).attr('data-freight-payment-status') == 0).reduce(function(a, b) {
            return a + Number($(b).attr('data-freight-in-usd'))
        }, 0)
        let totalAmountInYen = totalAmountWithoutFreightInYen + totalFreightInYen;
        let selectedIds = selected_items.map(item=>$(item).attr('data-id'));
        let selectedData = selected_items.map(item=>{
            let data = JSON.parse($(item).attr('data-json'));
            let obj = {};
            obj["id"] = data.id
            obj["chassisNo"] = data.chassisNo;
            return obj;

        }
        )
        //render selected items
        let item_ele = $('#cloneable-wrapper').find('li#paymentProcessingItems');
        for (let i = 0; i < selectedData.length; i++) {
            let li = item_ele.clone();
            li.find('b.chassisNo').html(selectedData[i].chassisNo);
            li.find('input[name="invoiceId"]').val(selectedData[i].id)
            itemContainer.append(li);
        }
        //         let totalAmountInUsd = totalAmountInYen / rowData.exchangeRate;
        modal_approve_item_payment.find('input[name="amount"]').prop('readonly', true);
        modal_approve_item_payment.find('input[name="totalAmountInYen"]').val(totalAmountInYen);
        modal_approve_item_payment.find('input[name="totalFreightInYen"]').val(totalFreightInYen);
        modal_approve_item_payment.find('input[name="totalFreightInUsd"]').val(totalFreightInUsd);
        modal_approve_item_payment.find('input[name="ids"]').val(selectedIds);
        modal_approve_item_payment.find('input[name="approvedDate"]').datepicker("setDate", new Date())
        modal_approve_item_payment.find('select.select2-select, select.select2').select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true

        })
    }).on('hidden.bs.modal', function() {
        itemContainer.html('');
        $(this).find('.payment-processing-step').css('display', 'none');
        $(this).find('select,input,textarea:not(input[type="hidden"])').val('').trigger('change');

    }).on('change', 'select[name="paymentType"]', function(event) {
        let paymentType = $(this).val();
        let amountEle = modal_approve_item_payment.find('input[name="amount"]');
        let totalAmountInYen = modal_approve_item_payment.find('input[name="totalAmountInYen"]').val();
        let totalFreightInYen = modal_approve_item_payment.find('input[name="totalFreightInYen"]').val();
        let totalFreightInUsd = modal_approve_item_payment.find('input[name="totalFreightInUsd"]').val();
        if (!isEmpty(paymentType) && paymentType == 1) {
            $.getJSON(myContextPath + '/data/bankOnCurrencyType.json' + "?currencyType=1", function(data) {
                getBankData(modal_approve_item_payment.find('select#bank'), data);
            })
            setAutonumericValue(amountEle, totalFreightInYen)
            amountEle.autoNumeric('update', {
                aSign: "¥ "
            });

        } else if (!isEmpty(paymentType) && paymentType == 2) {
            $.getJSON(myContextPath + '/data/bankOnCurrencyType.json' + "?currencyType=2", function(data) {
                getBankData(modal_approve_item_payment.find('select#bank'), data);
            })
            setAutonumericValue(amountEle, totalFreightInUsd)
            amountEle.autoNumeric('update', {
                aSign: "$ "
            });
        } else if (!isEmpty(paymentType) && paymentType == 3) {
            $.getJSON(myContextPath + '/data/bankOnCurrencyType.json' + "?currencyType=1", function(data) {
                getBankData(modal_approve_item_payment.find('select#bank'), data);
            })
            setAutonumericValue(amountEle, totalAmountInYen)
            amountEle.autoNumeric('update', {
                aSign: "¥ "
            });
        } else {
            amountEle.val('')
            modal_approve_item_payment.find('select#bank').empty();
        }

    }).on('click', '#approve', function() {

        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        if (!$('#modal-approve-payment-items form#payment-detail-form').find('input,select,textarea').valid()) {
            return false;
        }
        if (getAutonumericValue(modal_approve_item_payment.find('input[name="amount"]')) <= 0) {
            alert('No payment found!')
            return false;
        }
        let params = table.rows({
            selected: true,
            page: 'current'
        }).data().toArray().map(row=>row.id);

        let items_ele = modal_approve_item_payment.find('ul#itemContainer').children().toArray();
        let items = [];
        for (let i = 0; i < items_ele.length; i++) {
            let obj = {};
            obj["invoiceId"] = $(items_ele[i]).find('input[name="invoiceId"]').val();
            obj["blStatus"] = $(items_ele[i]).find('select[name="blStatus"]').val();
            items.push(obj)
        }
        let data = {}
        data['items'] = items;
        data['bank'] = modal_approve_item_payment.find('select[name="bank"]').val();
        data['approvedDate'] = modal_approve_item_payment.find('input[name="approvedDate"]').val();
        data['remarks'] = modal_approve_item_payment.find('textarea[name="remarks"]').val();
        data['paymentType'] = modal_approve_item_payment.find('select[name="paymentType"]').find('option:selected').val();
        data['ids'] = modal_approve_item_payment.find('input[name="ids"]').val().split(",")
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            contentType: "application/json",
            url: myContextPath + '/accounts/approve/payment/freightShipping/roro',
            async: true,
            success: function(data) {
                if (data.status == 'success') {
                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Payment Approved.');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                    modal_approve_item_payment.modal('toggle');
                    table.ajax.reload();
                }
            }
        });

    }).on('change', 'select[name="bank"]', function() {
        let val = $(this).val();
        if (!isEmpty(val)) {
            modal_approve_item_payment.find('.modal-title.bank-balance').removeClass('hidden');
            $.getJSON(myContextPath + "/bank/currentBalance?bankId=" + val + '&currency=' + 1, function(data) {
                modal_approve_item_payment.find('.modal-title.bank-balance>span.amount').autoNumeric('init').autoNumeric('set', data.data)
            });

        } else {
            modal_approve_item_payment.find('.modal-title.bank-balance').addClass('hidden').find('span.amount').autoNumeric('init').autoNumeric('set', 0);
        }
    });

    let modal_approve_payment = $("#modal-approve-payment");
    var rowData;
    let freightShippingTriggerEle;
    modal_approve_payment.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        freightShippingTriggerEle = $(e.relatedTarget);
        let filterValue = $("#invoiceTypeFilter").val();
        if (filterValue != 3) {
            if (table.rows({
                selected: true,
                page: 'current'
            }).count() == 0) {
                alert($.i18n.prop('common.alert.stock.noselection'))
                return e.preventDefault();
            }
            rowData = table.rows({
                selected: true,
            }).data().toArray();
        } else {
            let tr = freightShippingTriggerEle.closest('tr');
            rowData = table.rows(tr).data().toArray();
        }

        var isValid = isValidSelection(rowData);
        if (!isValid) {
            alert($.i18n.prop('page.account.payment.processing.selected.different.supplier'))
            return e.preventDefault();
        }
        modal_approve_payment.find('input[name="approvedDate"]').datepicker("setDate", new Date())
        $.getJSON(myContextPath + '/data/bankOnCurrencyType.json' + "?currencyType=" + 1, function(data) {
            getBankData(modal_approve_payment.find('select#bank'), data);
        })

        let balanceAmountToPay = rowData.reduce(function(a, b) {
            return a + b.invoiceBalanceAmount;
        }, 0);
        modal_approve_payment.find('input[name="amount"]').val(balanceAmountToPay)
        if (rowData.length > 1) {
            modal_approve_payment.find('input[name="amount"]').prop('disabled', 'disabled');
        } else if (rowData.length == 1) {
            modal_approve_payment.find('input[name="amount"]').prop('disabled', '');
        }
        setAutonumericValue($(this).find('input[name="balanceAmount"]'), balanceAmountToPay);
        var amountEle = $(this).find('#amount');
        let amountTopay = balanceAmountToPay;
        if (Number(amountTopay) <= 0) {
            amountEle.prop('readonly', true)
        } else {
            amountEle.prop('readonly', false)
        }
        setAutonumericValue(amountEle, ifNotValid(balanceAmountToPay, 0));

    }).on('hidden.bs.modal', function() {
        $(this).find('select,input,textarea:not(input[type="hidden"])').val('').trigger('change');
        $('.select2-select, .select2').select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true

        })
    }).on('click', '#approve', function() {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        if (!payment_details_form.find('input,select,textarea').valid()) {
            return false;
        }
        var selectedflag = $("#invoiceTypeFilter").val()
        let params;
        if (selectedflag != 3) {
            params = table.rows({
                selected: true,
                page: 'current'
            }).data().toArray().map(row=>row.invoiceNo);
        } else {
            let tr = $(freightShippingTriggerEle).closest('tr');
            params = table.rows(tr).data().toArray().map(row=>row.invoiceNo);
        }

        let data = {}
        autoNumericSetRawValue(modal_approve_payment.find('input[name="amount"]'))
        data['bank'] = modal_approve_payment.find('select[name="bank"]').val();
        data['approvedDate'] = modal_approve_payment.find('input[name="approvedDate"]').val();
        data['amount'] = modal_approve_payment.find('input[name="amount"]').val();
        data['remarks'] = modal_approve_payment.find('textarea[name="remarks"]').val();
        // data['paymentType'] = modal_approve_payment.find('select[name="paymentType"]').find('option:selected').val();

        var url;
        if (selectedflag == 0) {
            url = myContextPath + "/accounts/approve/auction?" + $.param({
                "id": params
            })
        } else if (selectedflag == 1) {
            url = myContextPath + "/accounts/approve/transport?" + $.param({
                "id": params
            })
        } else if (selectedflag == 2) {
            url = myContextPath + "/accounts/approve/payment/storageAndPhoto?" + $.param({
                "id": params
            })
        } else if (selectedflag == 3) {
            url = myContextPath + "/accounts/approve/payment/freightShipping?" + $.param({
                "id": params
            })
        } else if (selectedflag == 4) {
            url = myContextPath + "/accounts/approve/others?" + $.param({
                "id": params
            })
        } else if (selectedflag == 5) {
            url = myContextPath + "/accounts/payment/approve/inspection?" + $.param({
                "id": params
            })
        }
        let response = approvePayment(data, url);
        if (response.status == 'success') {
            $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Payment Approved.');
            $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                $("#alert-block").slideUp(500);
            });
            modal_approve_payment.modal('toggle');
            table.ajax.reload();
        }

    }).on('change', 'select[name="bank"]', function() {
        let val = $(this).val();
        if (!isEmpty(val)) {
            modal_approve_payment.find('.modal-title.bank-balance').removeClass('hidden');
            $.getJSON(myContextPath + "/bank/currentBalance?bankId=" + val + '&currency=' + 1, function(data) {
                modal_approve_payment.find('.modal-title.bank-balance>span.amount').autoNumeric('init').autoNumeric('set', data.data)
            });

        } else {
            modal_approve_payment.find('.modal-title.bank-balance').addClass('hidden').find('span.amount').autoNumeric('init').autoNumeric('set', 0);
        }
    });
    var approvePayment = function(data, url) {
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
            contentType: "application/json",
            url: url,
            async: false,
            success: function(data) {
                result = data;
            }
        });
        return result;
    }

    let editDueDateEle = $('#modal-edit-dueDate');
    var triggerEle;
    $(editDueDateEle).on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        triggerEle = $(event.relatedTarget)
        var data = table.row($(triggerEle).closest('tr')).data();
        $(editDueDateEle).find('input[name="dueDate"]').val(data.dueDate);
        $(editDueDateEle).find('input[name="invoiceNo"]').val(data.id);
    }).on('hidden.bs.modal', function() {
        $(this).find('input').val('').trigger('change');
    }).on('click', '#save-dueDate', function() {
        if (!$('#payment-dueDate-edit').valid()) {
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }

        let dueDate = $(editDueDateEle).find('input[name="dueDate"]').val();
        let invoiceNo = $(editDueDateEle).find('input[name="invoiceNo"]').val();
        var queryString = "?invoiceNo=" + invoiceNo + "&dueDate=" + dueDate;

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            url: myContextPath + "/accounts/payment/edit/DueDate/invoiceApprove" + queryString,
            contentType: false,
            success: function(data) {
                if (data.status == 'success') {

                    $(editDueDateEle).modal('toggle');
                    //                     var row = table.row($(triggerEle).closest('tr'));
                    //                     if (!isEmpty(data.data)) {
                    //                         row.data(data.data).invalidate();
                    //                     }
                    table.ajax.reload();

                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Payment Approved.');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                }
            }
        });
    })

    let modal_container_invoice_item = $("#modal-container-invoice-items");
    modal_container_invoice_item.on('show.bs.modal', function(e) {
        var targetElement = $(e.relatedTarget);
        var data = table.row($(targetElement).closest('tr')).data();
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
});

//Purchase Format
function format(rowData) {
    var element = $('#clone-container>#payment-approve-details>.clone-element').clone();
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
        $(row).find('td.lotNo').html(ifNotValid(rowData.approvePaymentItems[i].lotNo, ''));
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
function format1(rowData) {
    var element = $('#clone-container>#payment-approve-details>.clone-element').clone();
    $(element).find('input[name="paymentinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.approvePaymentItems.length; i++) {
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.stockNo').html(ifNotValid(rowData.approvePaymentItems[i].stockNo, ''));
        $(row).find('td.purchaseCost>span').html(ifNotValid(rowData.approvePaymentItems[i].purchaseCost, ''));
        $(row).find('td.commision>span').html(ifNotValid(rowData.approvePaymentItems[i].commision, ''));
        $(row).find('td.roadTax>span').html(ifNotValid(rowData.approvePaymentItems[i].roadTax, ''));
        $(row).find('td.recycle>span').html(ifNotValid(rowData.approvePaymentItems[i].recycle, ''));
        $(row).find('td.otherCharges>span').html(ifNotValid(rowData.approvePaymentItems[i].otherCharges, ''));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);
    }
    return element;

}
//Transport Format
function transportFormat(rowData) {
    var element = $('#transport-clone-container>#payment-transport-approve-details>.clone-element').clone();
    $(element).find('input[name="transportinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    var total_amount = 0
      , tax_amount_total = 0;
    for (var i = 0; i < rowData.approvePaymentItems.length; i++) {
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.chassisNo').html(ifNotValid(rowData.approvePaymentItems[i].chassisNo, ''));
        $(row).find('td.maker').html(ifNotValid(rowData.approvePaymentItems[i].maker, ''));
        $(row).find('td.model').html(ifNotValid(rowData.approvePaymentItems[i].model, ''));
        $(row).find('td.taxamount>span').html(ifNotValid(rowData.approvePaymentItems[i].taxamount, ''));
        $(row).find('td.amount>span').html(ifNotValid(rowData.approvePaymentItems[i].amount, ''));
        $(row).removeClass('hide');
        total_amount += Number(ifNotValid(rowData.approvePaymentItems[i].amount, 0))
        tax_amount_total += Number(ifNotValid(rowData.approvePaymentItems[i].taxamount, 0))
        $(element).find('table>tbody').append(row);
    }
    $(element).find('table>tfoot td.amount>span').html(total_amount);
    $(element).find('table>tfoot td.taxamount>span').html(tax_amount_total);
    return element;

}
//t_inv Format
function tinvFormat(rowData) {
    var element = $('#others-clone-container>#others-invoice-details>.clone-element').clone();
    $(element).find('input[name="paymentinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    let amountTotal = 0;
    let taxTotal = 0;
    let totalTaxIncluded = 0;
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.approvePaymentItems.length; i++) {
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.category').html((ifNotValid(rowData.approvePaymentItems[i].invoiceType, '').toLowerCase() != 'others' ? ifNotValid(rowData.approvePaymentItems[i].category + ' [' + ifNotValid(rowData.approvePaymentItems[i].tkcCode, "") + '] - ' + ifNotValid(rowData.approvePaymentItems[i].tkcDescription, ""), '') : ifNotValid(rowData.approvePaymentItems[i].categoryOthers, '')));
        $(row).find('td.description').html(ifNotValid(rowData.approvePaymentItems[i].description, ''));
        $(row).find('td.amountInYen span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].amountInYen, ''));
        $(row).find('td.taxAmount span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].taxAmount, ''));
        $(row).find('td.hiddenTaxAmount input.hiddenTaxAmount').html(ifNotValid(rowData.approvePaymentItems[i].taxAmount, ''));
        $(row).find('td.totalAmount span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].totalWithTax, ''));
        $(row).find('td.amount span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].amount, ''));
        $(row).find('td.currency').html(ifNotValid(rowData.approvePaymentItems[i].sourceCurrency, ''));
        $(row).find('td.exchangeRate span.autonumber').html(ifNotValid(rowData.approvePaymentItems[i].exchangeRate, ''));
        amountTotal += Number(ifNotValid(rowData.approvePaymentItems[i].amountInYen, 0));
        taxTotal += Number(ifNotValid(rowData.approvePaymentItems[i].taxAmount, 0));
        totalTaxIncluded += Number(ifNotValid(rowData.approvePaymentItems[i].totalWithTax, 0));

        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);
    }
    $(element).find('table>tfoot td.amount>span').html(amountTotal);
    $(element).find('table>tfoot td.taxTotal>span').html(taxTotal);
    $(element).find('table>tfoot td.taxIncluded>span').html(totalTaxIncluded);
    return element;

}

function tinvFormat1(rowData) {
    var element = $('#tinv-clone-container>#payment-tinv-approve-details>.clone-element').clone();
    $(element).find('input[name="tinvinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    let amountTotal = 0;
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.approvePaymentItems.length; i++) {
        amountTotal += Number(ifNotValid(rowData.approvePaymentItems[i].amountInYen, 0));
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.category').html(ifNotValid(rowData.approvePaymentItems[i].category, ''));
        $(row).find('td.description').html(ifNotValid(rowData.approvePaymentItems[i].description, ''));
        $(row).find('td.amount>span').html(ifNotValid(rowData.approvePaymentItems[i].amountInYen, ''));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);
    }
    $(element).find('table>tfoot td.amount>span').html(amountTotal);
    return element;

}
//Forwarder Invoice Format
function forwarderFormat(rowData) {
    var element = $('#forwarder-clone-container>#payment-forwarder-approve-details>.clone-element>.forwarder-item-container').clone();
    $(element).find('input[name="forwarderinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    let amountTotal = 0;
    var cloningElement = $('#table-cloning-element').find('table').clone();
    $(element).append(cloningElement);
    for (var i = 0; i < rowData.metaData.length; i++) {
        $('<th class="align-center">' + rowData.metaData[i].title + '</th>').appendTo($(element).find('tr.header'))
        $('<td class="align-center ' + rowData.metaData[i].attribute + '"><span class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>').appendTo($(element).find('tr.clone-row'))

    }
    $('<th class="align-center">Remarks</th>').appendTo($(element).find('tr.header'))
    $('<td class="align-center remarks"></td>').appendTo($(element).find('tr.clone-row'))
    $('<th class="align-center">Total</th>').appendTo($(element).find('tr.header'))
    $('<td class="align-center total"><span class="autonumber" data-a-sign="¥ " data-m-dec="0"></span></td>').appendTo($(element).find('tr.clone-row'))
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.approvePaymentItems.length; i++) {
        let totalRowAmount = 0;
        var columnSpan = 3;
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.chassisNo').html(ifNotValid(rowData.approvePaymentItems[i].chassisNo, ''));
        for (var j = 0; j < rowData.metaData.length; j++) {
            $(row).find('td.' + rowData.metaData[j].attribute + ' ' + 'span.autonumber').autoNumeric('init').autoNumeric('set', ifNotValid(rowData.approvePaymentItems[i].chargesList[rowData.metaData[j].attribute + 'TotalAmount'], '')).autoNumeric('update', {
                aSign: rowData.currencySymbol + ' ',
                mDec: (rowData.currency == 2) ? 2 : 0
            });
            totalRowAmount += Number(rowData.approvePaymentItems[i].chargesList[rowData.metaData[j].attribute + 'TotalAmount']);
            columnSpan += 1;
        }
        $(element).find('table>tfoot>tr>td.footerColumn').attr('colspan', columnSpan);
        $(row).find('td.remarks').html(ifNotValid(rowData.approvePaymentItems[i].remarks, ''));
        $(row).find('td.total span.autonumber').autoNumeric('init').autoNumeric('set', ifNotValid(totalRowAmount, 0)).autoNumeric('update', {
            aSign: rowData.currencySymbol + ' ',
            mDec: (rowData.currency == 2) ? 2 : 0
        });
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);
    }
    $(element).find('table>tfoot td.amount>span').autoNumeric('init').autoNumeric('set', rowData.totalAmount).autoNumeric('update', {
        aSign: rowData.currencySymbol + ' ',
        mDec: (rowData.currency == 2) ? 2 : 0
    });
    return element;

}
//Freight Shipping Invoice Format
function freightShippingFormat(rowData) {
    var element = $('#freightshipping-clone-container>#payment-freightshipping-approve-details>.clone-element').clone();
    $(element).find('input[name="freightshippinginvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.approvePaymentItems.length; i++) {
        var row = $(rowClone).clone();
        let data = JSON.stringify(rowData.approvePaymentItems[i]);
        $(row).find('td.s-no').html('<input type="checkbox" data-json=' + data + ' data-freight-payment-status="' + rowData.approvePaymentItems[i].freightPaymentStatus + '" data-freight-in-usd="' + rowData.approvePaymentItems[i].freightChargeUsd + '" data-freight-in-yen="' + rowData.approvePaymentItems[i].freightCharge + '" data-total-in-yen="' + rowData.approvePaymentItems[i].totalWithFreightInYen + '" data-total-without-freight-yen="' + rowData.approvePaymentItems[i].totalWithOutFreightInYen + '" data-id="' + rowData.approvePaymentItems[i].id + '" name="itemSelect"/>');
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

function getBankData(element, value) {
    $(element).empty();
    $(element).select2({
        allowClear: true,
        width: '100%',
        data: $.map(value, function(item) {
            return {
                id: item.bankSeq,
                text: item.bankName,
                data: item
            };
        })
    }).val('').trigger('change')
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
//Inspection Format
function inspectionFormat(rowData) {
    var element = $('#inspection-clone-container>#payment-inspection-approve-details>.clone-element').clone();
    $(element).find('input[name="invoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    let taxTotal = 0;
    let amountTotal = 0;
    let totalTaxIncluded = 0;
    for (var i = 0; i < rowData.approvePaymentItems.length; i++) {
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.stockNo').html(ifNotValid(rowData.approvePaymentItems[i].stockNo, ''));
        $(row).find('td.chassisNo').html(ifNotValid(rowData.approvePaymentItems[i].chassisNo, ''));
        $(row).find('td.maker').html(ifNotValid(rowData.approvePaymentItems[i].maker, ''));
        $(row).find('td.model').html(ifNotValid(rowData.approvePaymentItems[i].model, ''));
        $(row).find('td.amount>span').html(ifNotValid(rowData.approvePaymentItems[i].amount, ''));
        $(row).find('td.tax>span').html(ifNotValid(rowData.approvePaymentItems[i].taxAmount, ''));
        $(row).find('td.total>span').html(ifNotValid(rowData.approvePaymentItems[i].totalTaxIncluded, ''));
        taxTotal += Number(ifNotValid(rowData.approvePaymentItems[i].taxAmount, 0));
        amountTotal += Number(ifNotValid(rowData.approvePaymentItems[i].amount, 0));
        totalTaxIncluded += Number(ifNotValid(rowData.approvePaymentItems[i].totalTaxIncluded, 0));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);
    }
    $(element).find('table>tfoot td.amount>span').html(amountTotal);
    $(element).find('table>tfoot td.tax>span').html(taxTotal);
    $(element).find('table>tfoot td.total>span').html(totalTaxIncluded);
    return element;

}

function isValidSelection(data) {
    //var tmpPData = ''
    var tmpSupplier = '';
    for (var i = 0; i < data.length; i++) {
        var tableData = data[i];
        if (i == 0) {
            // tmpPData = tableData.purchaseDate
            tmpSupplier = tableData.supplierId;
        }
        //tmpPData == tableData.purchaseDate -> removed purchase date equal
        if (!(tmpSupplier == tableData.supplierId)) {
            return false;
        }
    }
    return true;
}

function updateFooter(table) {

    var intVal = function(i) {
        return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
    };
    var isValid = function(val) {
        return typeof val === 'undefined' || val == null ? 0 : val;
    }
    // page total
    var pageTotal = table.column(8, {
        page: 'current'
    }).nodes().reduce(function(a, b) {

        var total = Number(isValid($(b).find('span.autonumber').autoNumeric('init').autoNumeric('get')));

        return intVal(a) + total;
    }, 0);
    //paid amount total
    var pageTotalPaid = table.column(9, {
        page: 'current'
    }).nodes().reduce(function(a, b) {

        var totalPaid = Number(isValid($(b).find('span.autonumber').autoNumeric('init').autoNumeric('get')));

        return intVal(a) + totalPaid;
    }, 0);
    //balance amount total
    var pageTotalBalance = table.column(10, {
        page: 'current'
    }).nodes().reduce(function(a, b) {

        var totalBalance = Number(isValid($(b).find('span.autonumber').autoNumeric('init').autoNumeric('get')));

        return intVal(a) + totalBalance;
    }, 0);

    let footerEle = $('#table-auction-payment').find('tfoot>tr th');
    footerEle.find('span.pagetotal').autoNumeric('init').autoNumeric('set', pageTotal);
    footerEle.find('span.pagetotalpaid').autoNumeric('init').autoNumeric('set', pageTotalPaid);
    footerEle.find('span.pagetotalbalance').autoNumeric('init').autoNumeric('set', pageTotalBalance);

    if ($('#invoiceTypeFilter').val() == 3) {

        // freight shipping Dollar amount total
        var totalDollarAmount = table.column(11, {
            page: 'current'
        }).nodes().reduce(function(a, b) {

            var totalBalance = Number(isValid($(b).find('span.autonumber').autoNumeric('init').autoNumeric('get')));

            return intVal(a) + totalBalance;
        }, 0);

        // freight shipping Dollar amount balance
        var totalDollarBalance = table.column(12, {
            page: 'current'
        }).nodes().reduce(function(a, b) {

            var totalBalance = Number(isValid($(b).find('span.autonumber').autoNumeric('init').autoNumeric('get')));

            return intVal(a) + totalBalance;
        }, 0);

        footerEle.find('span.totalDollarAmount').autoNumeric('init').autoNumeric('set', totalDollarAmount);
        footerEle.find('span.totalDollarBalance').autoNumeric('init').autoNumeric('set', totalDollarBalance);
    }

}
//multi step form
function showTab(n) {
    // This function will display the specified tab of the form ...
    var x = modal_approve_item_payment.find('.payment-processing-step');
    $(x[n]).css('display', 'block');
    // ... and fix the Previous/Next buttons
    if (n == 0) {
        modal_approve_item_payment.find('#btn-previous-step').css('display', 'none');

    } else {
        modal_approve_item_payment.find('#btn-previous-step').css('display', 'inline');

    }
    if (n == (x.length - 1)) {
        modal_approve_item_payment.find('#btn-next-step').css('display', 'none');
        modal_approve_item_payment.find('#btn-previous-step,button#approve').css('display', 'inline');
    } else {
        modal_approve_item_payment.find('#btn-next-step').css('display', 'inline');
        modal_approve_item_payment.find('button#approve').css('display', 'none');
    }
    // ... and run a function that displays the correct step indicator:
    //     fixStepIndicator(n)
}
// // Display the current tab

// function fixStepIndicator(n) {
//     // This function removes the "active" class of all steps...
//     var i, x = modal_approve_item_payment.find('span.step');
//     //     var i, x = document.getElementsByClassName("step");
//     for (i = 0; i < x.length; i++) {
//         $(x[i]).removeClass('active');
//         //.css('background-color', '');
//     }
//     //... and adds the "active" class to the current step:
//     $(x[n]).addClass('active');

// }
function paymentProcessingStepNextPrev(n) {
    // This function will figure out which tab to display
    var x = modal_approve_item_payment.find('.payment-processing-step');
    //     var x = document.getElementsByClassName("tab");
    // Exit the function if any field in the current tab is invalid:
    if (n == 1 && !form_container_allocation_element.find('select[name="blStatus"]').valid()) {
        return false;
    }
    //     else if (n == 1 && form_container_allocation_element.valid()) {
    //         $(modal_approve_item_payment.find('.step')[payment_processing_step]).css('background-color', '#4CAF50');
    //     }

    // Hide the current tab:
    $(x[payment_processing_step]).css('display', 'none');
    // Increase or decrease the current tab by 1:
    payment_processing_step = payment_processing_step + n;
    // if you have reached the end of the form... :
    if (payment_processing_step >= x.length) {
        return false;
    }
    // Otherwise, display the correct tab:
    showTab(payment_processing_step);
}
