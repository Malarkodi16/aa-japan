var plateNosJson;
$(function() {
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })
    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
        supplierJson = data;
    });
    $('.datepicker1').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true /*,
        defaultDate: new Date()*/
    }).on('change', function() {
        $(this).valid();

    })

    // Converted Date picker
    var converted_date;
    $('#table-filter-converted-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: true
    }).on("change", function(e, picker) {
        converted_date = $(this).val();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        //         table.search(purchased_date).draw();
        exportTable.draw();

    });
    $('#date-form-group-conv-date').on('click', '.clear-date', function() {

        converted_date = '';
        $('#table-filter-converted-date').val('');
        $(this).remove();
        exportTable.draw();
    })

    $.getJSON(myContextPath + "/data/plate-no-registrations.json", function(data) {
        plateNosJson = data;
        $('#registrationNo1').select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            data: $.map(plateNosJson, function(item) {
                return {
                    id: item.name,
                    text: item.name
                };
            })
        }).val('').trigger("change");
        $('#registrationNo1').select2('destroy');
    })

    $.getJSON(myContextPath + "/data/inspectioncompanies.json", function(data) {
        $('#modal-send-document select[name="inspectionCompanyId"]').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).val('').trigger("change");
    });
    $.getJSON(myContextPath + "/data/shippingCompany.json", function(data) {
        $('#modal-send-document select[name="shippingCompanyId"]').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.shippingCompanyNo,
                    text: item.name
                };
            })
        }).val('').trigger("change");
    });
    $('input[type="radio"].minimal,input[type="checkbox"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    })
    $(document).on('change', '#modal-send-document select[name="exportCertificateStatus"]', function() {
        var selected = $(this).val();

        if (selected == 1) {
            $('#modal-send-document #shipping-company,#documet-sent-wrapper').removeClass('hidden');
            $('#modal-send-document #inspection-company').addClass('hidden');
        } else if (selected == 2) {
            $('#modal-send-document #shipping-company,#documet-sent-wrapper').addClass('hidden');
            $('#modal-send-document #inspection-company').removeClass('hidden');
        }
    })
    $(document).on('ifChecked', 'input[name="docSentStatusFilter"]', function() {
        if ($(this).val() == 0 && $('input[name="exportCertificateStatusFilter"]:checked').val() == 1) {
            $('#btn-send-document').addClass('hidden')
            $('#updateDocType').addClass('hidden')
            $('#exportCrReceived').removeClass('hidden')
            $('#nonExportedCrReceived').removeClass('hidden')
            setDatatableColumns(exportTable, [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11])
        } else if ($(this).val() == 1 && $('input[name="exportCertificateStatusFilter"]:checked').val() == 1) {
            $('#btn-send-document').addClass('hidden')
            $('#updateDocType').addClass('hidden')
            $('#exportCrReceived').addClass('hidden')
            $('#nonExportedCrReceived').addClass('hidden')
            setDatatableColumns(exportTable, [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11])
        }
        exportTable.ajax.reload()

    })

    $(document).on('ifChecked', 'input[name="exportCertificateStatusFilter"]', function(e) {
        exportTable.ajax.reload()
        if ($(this).val() == 1) {
            $('#shipping-company-document-sent-status').removeClass('hidden');
        } else {
            $('#shipping-company-document-sent-status').addClass('hidden');
        }
        if ($(this).val() == 0) {
            $('#btn-send-document').removeClass('hidden')
            $('#updateDocType').removeClass('hidden')
            $('#exportCrReceived').addClass('hidden')
            $('#nonExportedCrReceived').addClass('hidden')
            setDatatableColumns(exportTable, [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 15])
        } else if ($(this).val() == 2) {
            $('#btn-send-document').addClass('hidden')
            $('#updateDocType').addClass('hidden')
            $('#exportCrReceived').removeClass('hidden')
            $('#nonExportedCrReceived').removeClass('hidden')
            setDatatableColumns(exportTable, [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11])
        } else if ($(this).val() == 1 && $('input[name="docSentStatusFilter"]:checked').val() == 0) {
            $('#btn-send-document').addClass('hidden')
            $('#updateDocType').addClass('hidden')
            $('#exportCrReceived').removeClass('hidden')
            $('#nonExportedCrReceived').removeClass('hidden')
            setDatatableColumns(exportTable, [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11])
        } else if ($(this).val() == 1 && $('input[name="docSentStatusFilter"]:checked').val() == 1) {
            $('#btn-send-document').addClass('hidden')
            $('#updateDocType').addClass('hidden')
            $('#exportCrReceived').addClass('hidden')
            $('#nonExportedCrReceived').addClass('hidden')
            setDatatableColumns(exportTable, [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11])
        }

    });
    var exportCrTargetElement
    $('#exportCrReceived').on('click', function(e) {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        exportCrTargetElement = $(e.relatedTarget);

        var ids = [];
        exportTable.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var rowData = exportTable.row(this).data();
            ids.push(rowData.id)
        })
        if (ids.length == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return false;
        }
        var response = exportCertificateCrReceived(ids);
        if (response.status == "success") {
            // exportTable.row($(exportCrTargetElement).closest('tr')).remove().draw()
            exportTable.ajax.reload();
        }
    })
    $('#nonExportedCrReceived').on('click', function() {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        var ids = [];
        exportTable.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var rowData = exportTable.row(this).data();
            ids.push(rowData.id)
        })
        if (ids.length == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return false;
        }
        console.log(ids);
        var response = nonExportedCertificateCrReceived(ids);
        if (response.status == "success") {
            // exportTable.row($(exportCrTargetElement).closest('tr')).remove().draw()
            exportTable.ajax.reload();
        }
    })

    jQuery.extend(jQuery.fn.dataTableExt.oSort, {
        "non-empty-string-asc": function(str1, str2) {
            if (str1 == null || str1 == "")
                return 1;
            if (str2 == null || str2 == "")
                return -1;
            return ((str1 < str2) ? -1 : ((str1 > str2) ? 1 : 0));
        },

        "non-empty-string-desc": function(str1, str2) {
            if (str1 == null || str1 == "")
                return 1;
            if (str2 == null || str2 == "")
                return -1;
            return ((str1 < str2) ? 1 : ((str1 > str2) ? -1 : 0));
        }
    });

    //Setting count in dashboard
    setDocumentDashboardStatus();
    var exportTableEle = $('#table-export-certificates');
    var exportTable = exportTableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,

        "language": {
            processing: '<i class="fa fa-spinner fa-spin fa-3x fa-fw"></i><span class="sr-only">Loading...</span> '
        },
        "ajax": {
            'url': myContextPath + "/documents/tracking/export-certificate-list",
            'data': function(data) {
                data["exportCertificateStatus"] = $('input[name="exportCertificateStatusFilter"]:checked').val();
                if (data.exportCertificateStatus == 1) {
                    data["docSentStatus"] = $('input[name="docSentStatusFilter"]:checked').val();
                }
                return data;
            }
        },
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "type": "non-empty-string",
            "targets": '_all'

        }, {
            targets: 0,
            "orderable": false,
            className: 'select-checkbox',
            "data": "stockNo",

            "width": "10px",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" id="check-box-select" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">' + '<input type="hidden" name="stockNo" value="' + row.stockNo + '"/>';

                }
                return data;
            }
        }, {
            targets: 1,
            "data": "chassisNo",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<a href="#" data-toggle="modal" name="stockNo" data-target="#modal-export-certificate-details" data-stockno="' + row.stockNo + '">' + data + '</a>';
                }
                return data;
            }
        }, {
            targets: 2,
            "visible": true,
            //"orderable": false,
            "data": "docConvertTo",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                var docConvertTo;
                if (type == "display") {
                    if (data == "1") {
                        docConvertTo = "Export Certificate";
                        return docConvertTo;
                    }
                }
            }
        }, {
            targets: 3,
            "visible": true,
            // "orderable": false,
            "data": "documentConvertedDate",

        }, {
            targets: 4,
            "className": "dt-right",
            "visible": true,
            //"orderable": false,
            "data": "documentFob",
            "width": "100px",
            "render": function(data, type, row) {
                var html = ''
                html += '<span class="autonumber" data-a-sign="' + ifNotValid("¥ ", '') + ' " data-m-dec="0">' + ifNotValid(data, '') + '</span>'
                return '<div style="width:80px;">' + html + '</div>';
            }
        }, {
            targets: 5,
            "visible": true,
            //"orderable": false,
            "data": "destinationCountry",
        }, {
            targets: 6,
            "visible": true,
            //"orderable": false,
            "data": "destinationPort",
        }, {
            targets: 7,
            "visible": true,
            // "orderable": false,
            "data": "",
        }, {
            targets: 8,
            "visible": true,
            // "orderable": false,
            "data": "docType",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                var documentType;
                if (type == "display") {
                    if (data == "0") {
                        documentType = "MASHO";
                        return documentType;
                    } else if (data == "1") {
                        documentType = "SHAKEN";
                        return documentType;
                    } else {
                        documentType = "";
                        return documentType;
                    }
                }
            }
        }, {
            targets: 9,
            "visible": true,
            // "orderable": false,
            "data": "documentReceivedDate",
        }, {
            targets: 10,
            "visible": true,
            // "orderable": false,
            "data": "oldNumberPlate",
        }, {
            targets: 11,
            "visible": true,
            // "orderable": false,
            "data": "plateNoReceivedDate",
        }, {
            targets: 12,
            "visible": false,
            // "orderable": false,
            "data": "roadTax",
        }, {
            targets: 13,
            "visible": false,
            // "orderable": false,
            "data": "insurance",
        }, {
            targets: 14,
            "visible": false,
            // "orderable": false,
            "data": "stockNo",
            "render": function(data, type, row) {
                var html = ''
                html += '<a href="#" class="btn btn-primary" data-toggle="modal" data-target="#modal-reconvert" data-backdrop="static" data-keyboard="false"><i class="fa fa-fw fa-check"></i>Reconvert</a>'
                return '<div style="width:100px;">' + html + '</div>';
            }
        }, {
            targets: 15,
            "visible": true,
            "orderable": false,
            "data": "stockNo",
            "render": function(data, type, row) {
                var html = ''
                html += '<a href="' + myContextPath + '/documents/tracking/export-certificate/' + data + '.pdf" class="btn btn-default btn-xs"><i class="fa fa-fw fa-download"></i></a>'
                return '<div style="width:100px;">' + html + '</div>';
            }

        }, {
            targets: 16,
            "visible": false,
            "data": "purchaseType"
        }, {
            targets: 17,
            // "orderable": false,
            "data": "supplierCode",
            "visible": false //"searchable": false
        }, {
            targets: 18,
            // "orderable": false,
            "data": "auctionHouseId",
            "visible": false //"searchable": false
        }, {
            targets: 19,
            // "orderable": false,
            "data": "purchaseInfoDate",
            "visible": false //"searchable": false
        }],

        "drawCallback": function(settings, json) {
            exportTableEle.find('span.autonumber').autoNumeric('init')

        }

    })
    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    }
    ;//table Search
    $('#table-export-certificates-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        exportTable.search(query, true, false).draw();
    });
    $('#table-export-certificates-filter-length').change(function() {
        exportTable.page.len($(this).val()).draw();
    });
    exportTable.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            exportTable.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            exportTable.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            exportTable.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            exportTable.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function() {
        if (exportTable.rows({
            selected: true,
            page: 'current'
        }).count() !== exportTable.rows({
            page: 'current'
        }).count()) {
            $(exportTableEle).find("th.select-checkbox>input").removeClass("selected");
            $(exportTableEle).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(exportTableEle).find("th.select-checkbox>input").addClass("selected");
            $(exportTableEle).find("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (exportTable.rows({
            selected: true,
            page: 'current'
        }).count() !== exportTable.rows({
            page: 'current'
        }).count()) {
            $(exportTableEle).find("th.select-checkbox>input").removeClass("selected");
            $(exportTableEle).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(exportTableEle).find("th.select-checkbox>input").addClass("selected");
            $(exportTableEle).find("th.select-checkbox>input").prop('checked', true);

        }

    });
    // Date range picker
    var purchased_min;
    var purchased_max;
    $('#table-filter-purchased-date').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        purchased_min = picker.startDate;
        purchased_max = picker.endDate;
        picker.element.val(purchased_min.format('DD-MM-YYYY') + ' - ' + purchased_max.format('DD-MM-YYYY'));
        purchased_min = purchased_min._d.getTime();
        purchased_max = purchased_max._d.getTime();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        exportTable.draw();
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        purchased_min = '';
        purchased_max = '';
        exportTable.draw();
        $('#table-filter-purchased-date').val('');
        $(this).remove();

    })

    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

    })
    $('#purchaseType').change(function() {
        var selectedVal = $(this).find('option:selected').val();
        var purchasedSupplierEle = $('#purchasedSupplier');
        if (selectedVal.length > 0) {
            $(purchasedSupplierEle).prop('disabled', false);
            $(purchasedSupplierEle).empty();
            var tmpSupplier;
            if (selectedVal == 'auction') {
                tmpSupplier = supplierJson.filter(function(item) {
                    return item.type === 'auction';
                })
                $('#auctionFields').css('display', '')
            } else {
                tmpSupplier = supplierJson.filter(function(item) {
                    return item.type === 'supplier';
                })
                $('#auctionFields').css('display', 'none')
            }
            $(purchasedSupplierEle).select2({
                allowClear: true,
                width: '100%',
                placeholder: 'All',
                data: $.map(tmpSupplier, function(item) {
                    return {
                        id: item.supplierCode,
                        text: item.company
                    };
                })
            })
        } else {
            $(purchasedSupplierEle).prop('disabled', true);
            $('#auctionFields').css('display', 'none')
        }
        var purchaseInfoPosEle = $('#purchasedInfoPos');
        $(purchaseInfoPosEle).val($(purchaseInfoPosEle).attr('data-value')).trigger("change");
        $(purchasedSupplierEle).val('').trigger('change');
        filterPurchaseType = $('#purchaseType').val();
        exportTable.draw();
    });

    $('#purchasedSupplier').select2({
        allowClear: true,
        placeholder: 'All',
    }).on("change", function(event) {
        var supplier = $(this).find('option:selected').val();
        var purchasedType = $('#purchaseType').find('option:selected').val()
        if ((supplier == null || supplier.length == 0) || $('#purchaseType :selected').val() != 'auction') {
            $('#purchasedAuctionHouse').empty();
            $('#purchasedInfoPos').empty();
            filterSupplierName = supplier;
            exportTable.draw();
            return;
        }
        filterSupplierName = supplier;
        var auctionHouseArr = [];
        // var posNosArr = [];
        $.each(supplierJson, function(i, item) {
            if (item.supplierCode === supplier && item.type === purchasedType) {
                auctionHouseArr = item.supplierLocations;
                //posNosArr=item.
                return false;
            }

        });
        exportTable.draw();
        $('#purchasedAuctionHouse').empty().select2({
            placeholder: "All",
            allowClear: true,
            data: $.map(auctionHouseArr, function(item) {
                return {
                    id: item.id,
                    text: item.auctionHouse,
                    data: item
                };
            })
        }).val('').trigger("change");

    });
    $('#purchasedAuctionHouse').select2({
        placeholder: "All",
        allowClear: true
    }).on("change", function(event) {
        filterAuctionHouse = $(this).find('option:selected').val();
        exportTable.draw();

    })
    var filterPurchaseType = $('#purchaseType').val();
    var filterSupplierName = $('#purchasedSupplier').find('option:selected').val();
    var filterAuctionHouse = $('#purchasedAuctionHouse').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //date filter
        if (typeof purchased_min != 'undefined' && purchased_min.length != '') {
            if (aData[19].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[19], 'DD-MM-YYYY')._d.getTime();
            }
            if (purchased_min && !isNaN(purchased_min)) {
                if (aData._date < purchased_min) {
                    return false;
                }
            }
            if (purchased_max && !isNaN(purchased_max)) {
                if (aData._date > purchased_max) {
                    return false;
                }
            }

        }
        if (typeof converted_date != 'undefined' && converted_date.length != '') {
            if (aData[3].length == 0 || aData[3] != converted_date) {
                return false;
            }
        }
        if (typeof filterPurchaseType != 'undefined' && filterPurchaseType.length != '') {
            if (aData[16].length == 0 || aData[16] != filterPurchaseType) {
                return false;
            }
        }
        //Supplier filter
        if (typeof filterSupplierName != 'undefined' && filterSupplierName.length != '') {
            if (aData[17].length == 0 || aData[17] != filterSupplierName) {
                return false;
            }
        }

        //Auction Housee filter
        if (typeof filterAuctionHouse != 'undefined' && filterAuctionHouse.length != '') {
            if (aData[18].length == 0 || aData[18] != filterAuctionHouse) {
                return false;
            }
        }

        return true;
    });

    var modalUpdateDocType = $('#modal-update-docType')
    $(modalUpdateDocType).on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var data = [];
        exportTable.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var rowData = exportTable.row(this).data();
            data.push(rowData.stockNo)
        })
        if (data.length == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return false;
        }

    }).on('hidden.bs.modal', function() {
        $(this).find('select,input').val('').trigger('change');
    }).on('click', '#update-doc', function(e) {
        if (!$('#docType-update').valid() || (!confirm($.i18n.prop('common.confirm.update.docType')))) {
            return;
        }
        var data = [];
        exportTable.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var rowData = exportTable.row(this).data();
            data.push(rowData.stockNo)
        })
        var selectedType = $('input[name="radioShowTable"]:checked').val();
        var documentConvertTo = $(modalUpdateDocType).find('#docConvertNameTransfer').find('#documentConvertTo').val();
        var parm = '?documentConvertTo=' + documentConvertTo
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data),
            url: myContextPath + "/documents/tracking/updateDocumentType" + parm,
            async: false,
            contentType: "application/json",
            success: function(data) {
                setDocumentDashboardStatus();
                exportTable.ajax.reload();
                $('#modal-update-docType').modal('toggle');
                $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Stock Updated.');
                $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                    $("#alert-block").slideUp(500);

                });

            }
        });
    })

    var modalDocSendDetails = $('#modal-send-document');
    modalDocSendDetails.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var count = exportTable.rows({
            selected: true,
            page: 'current'
        }).count();

        if (count == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return e.preventDefault();

        }

    }).on('hidden.bs.modal', function() {
        $(this).find('select[name="shippingCompanyId"],select[name="inspectionCompanyId"],input[type="text"]').val('').trigger('change');

    }).on('click', 'button[name="save"]', function() {
        if (!$('#modal-send-document form#form-document-send-details').find('input,select,textarea').valid()) {
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }

        let selectedRowData = exportTable.rows({
            selected: true,
            page: 'current'
        }).data().toArray();
        let ids = selectedRowData.map(data=>data.id);
        let exportCertificateStatus = modalDocSendDetails.find('select[name="exportCertificateStatus"]').val();
        let shippingCompanyId = modalDocSendDetails.find('select[name="shippingCompanyId"]').val();
        let docOriginalSent = modalDocSendDetails.find('input[name="docOriginalSent"]').is(":checked") ? 1 : 0;
        let docEmailSent = modalDocSendDetails.find('input[name="docEmailSent"]').is(":checked") ? 1 : 0;
        let inspectionCompanyId = modalDocSendDetails.find('select[name="inspectionCompanyId"]').val();
        let docSendDate = modalDocSendDetails.find('input[name="docSendDate"]').val();
        let data = {};
        data["exportCertificateStatus"] = exportCertificateStatus;
        data["shippingCompanyId"] = shippingCompanyId;
        data["docOriginalSent"] = docOriginalSent;
        data["docEmailSent"] = docEmailSent;
        data["inspectionCompanyId"] = inspectionCompanyId;
        data["docSendDate"] = docSendDate;
        let response = saveDocSendDetails(ids, data);
        if (response.status == 'success') {
            exportTable.ajax.reload();
            $(modalDocSendDetails).modal('toggle');
        }
    })
    //     Export Certificate
    var exportCertificateDetailsModal = $('#modal-export-certificate-details');
    var exportCertificateDetailsModalBody = exportCertificateDetailsModal.find('#modal-export-certificate-details-body');
    var exportCertificateDetailsModalBodyDiv = exportCertificateDetailsModal.find('#cloneable-items');
    var exportCertificateCloneElement = $('#export-certificate-details-html>.export-certificate-details');
    exportCertificateDetailsModalBodyDiv.slimScroll({
        start: 'bottom',
        height: '100%'
    });
    exportCertificateDetailsModal.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(e.relatedTarget);
        var rowData = exportTable.row($(targetElement).closest('tr')).data();
        // append stock details
        var stockNo = targetElement.attr('data-stockNo');
        var data = getExportCertificateDetails(stockNo)
        var exportbody = exportCertificateCloneElement.clone()
        exportbody.appendTo(exportCertificateDetailsModalBody);
        exportbody.find('input[name="stockNo"]').val(ifNotValid(stockNo, ''))
        exportbody.find('input[name="sFirstRegDate"]').val(ifNotValid(data.sFirstRegDate, ''))
        exportbody.find('input[name="trademarkVehicle"]').val(ifNotValid(data.trademarkVehicle, ''))
        exportbody.find('input[name="modelType"]').val(ifNotValid(data.modelType, ''))
        exportbody.find('input[name="makerSerialNo"]').val(ifNotValid(data.makerSerialNo, ''))
        exportbody.find('input[name="fixedNumber"]').val(ifNotValid(data.fixedNumber, ''))
        exportbody.find('input[name="fuel"]').val(ifNotValid(data.fuel, ''))

        if (!isEmpty(data.registrationNo1)) {
            exportbody.find('select[name="registrationNo1"]').val(ifNotValid(data.registrationNo1, '')).trigger("change");
        }

        exportbody.find('select[name="unit"]').val(ifNotValid(data.unit, 'L')).trigger("change");
        exportbody.find('input[name="registrationNo2"]').val(ifNotValid(data.registrationNo2, ''))
        exportbody.find('input[name="registrationNo3"]').val(ifNotValid(data.registrationNo3, ''))
        exportbody.find('input[name="registrationNo4"]').val(ifNotValid(data.registrationNo4, ''))
        exportbody.find('input[name="registrationDate"]').val(ifNotValid(data.registrationDate, ''))
        exportbody.find('input[name="engineModel"]').val(ifNotValid(data.engineModel, ''))
        exportbody.find('select[name="classificationOfVehicle"]').val(ifNotValid(data.classificationOfVehicle, ''))
        exportbody.find('select[name="use"]').val(ifNotValid(data.use, ''))
        exportbody.find('select[name="purpose"]').val(ifNotValid(data.purpose, ''))
        exportbody.find('input[name="typeOfBody"]').val(ifNotValid(data.typeOfBody, ''))
        exportbody.find('input[name="maximCarry"]').val(ifNotValid(data.maximCarry, ''))
        exportbody.find('input[name="weight"]').val(ifNotValid(data.weight, ''))
        exportbody.find('input[name="grossWeight"]').val(ifNotValid(data.grossWeight, ''))
        exportbody.find('input[name="engineCapacity"]').val(ifNotValid(data.engineCapacity, ''))
        exportbody.find('input[name="specificationNo"]').val(ifNotValid(data.specificationNo, ''))
        exportbody.find('input[name="classificationNo"]').val(ifNotValid(data.classificationNo, ''))
        exportbody.find('input[name="length"]').val(ifNotValid(data.length, ''))
        exportbody.find('input[name="width"]').val(ifNotValid(data.width, ''))
        exportbody.find('input[name="height"]').val(ifNotValid(data.height, ''))
        exportbody.find('input[name="ffWeight"]').val(ifNotValid(data.ffWeight, ''))
        exportbody.find('input[name="frWeight"]').val(ifNotValid(data.frWeight, ''))
        exportbody.find('input[name="rfWeight"]').val(ifNotValid(data.rfWeight, ''))
        exportbody.find('input[name="rrWeight"]').val(ifNotValid(data.rrWeight, ''))
        exportbody.find('input[name="exportScheduleDate"]').val(ifNotValid(data.exportScheduleDate, ''))
        exportbody.find('textarea[name="remark"]').val(ifNotValid(data.remark, ''))
        exportbody.find('input[name="convertedDate"]').val(!isEmpty(data.convertedDate) ? data.convertedDate : rowData.documentConvertedDate)
        exportbody.find('input[name="serialNo"]').val(ifNotValid(data.serialNo, ''))
        exportbody.find('input[name="referenceNo"]').val(ifNotValid(data.referenceNo, ''))
        exportbody.find('input[name="nameOfOwner"]').val(ifNotValid(data.nameOfOwner, ''))
        exportbody.find('input[name="addressOfOwner"]').val(!isEmpty(data.addressOfOwner) ? data.addressOfOwner : '1-28-21 HAYABUCHI,TSUZUKI-KU,YOKOHAMA-SHI,KANAGAWA-KEN,JAPAN')
        exportbody.find('input[name="nameOfUser"]').val(ifNotValid(data.nameOfUser, ''))
        exportbody.find('input[name="addressOfUser"]').val(ifNotValid(data.addressOfUser, ''))
        //exportbody.find('input[name="localityOfPrincipalOfUse"]').val(ifNotValid(data.localityOfPrincipalOfUse, ''))
        exportbody.find('select[name="registrationNo1"]').select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true
        })
        exportbody.find('select[name="unit"]').select2({
            allowClear: true,
            width: "100px"
        })
        // Initialize Date
        exportCertificateDetailsModalBody.find('.datepicker').datepicker({
            format: "dd-mm-yyyy",
            autoclose: true
        })
        exportCertificateDetailsModalBody.find('#exportScheduleDate').datepicker({
            format: "dd-mm-yyyy",
            changeYear: false,
            changeMonth: true,
            viewMode: "months",
            maxViewMode: "months",
            autoclose: true
        });
        exportCertificateDetailsModalBody.find('#exportScheduleDate').keyup(function() {
            if ($(this).val().length == 2) {
                $(this).val($(this).val() + "-");
            } else if ($(this).val().length == 5) {
                var inputDate = $(this).val() + "-" + new Date().getFullYear();
                var dateParts = inputDate.split("-");
                exportCertificateDetailsModalBody.find('#exportScheduleDate').datepicker({
                    dateFormat: "dd-MM-yyyy"
                }).datepicker('setDate', new Date(+dateParts[2],dateParts[1] - 1,+dateParts[0]));
            }
        });
        exportCertificateDetailsModalBody.find('#convertedDate').datepicker({
            format: "dd-mm-yyyy",
            autoclose: true,
            maxViewMode: 0
        })
        //         .on('show', function() {
        //     // remove the year from the date title before the datepicker show
        //         var dateText  = $(".datepicker-days .datepicker-switch").text().split(" ");
        //         var dateTitle = dateText[0];
        //         $(".datepicker-days .datepicker-switch").text(dateTitle);
        //         $(".datepicker-months .datepicker-switch").css({"visibility":"hidden"});
        // });
    }).on('hidden.bs.modal', function() {
        exportCertificateDetailsModalBody.html('');
    })
    var modalReconvert = $('#modal-reconvert');
    var modalReconvertTriggerBtnEle
    $(modalReconvert).on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        modalReconvertTriggerBtnEle = $(e.relatedTarget);

    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
    }).on('click', '#reconvert-doc', function() {
        if (!$('#reconvert-update').valid() || (!confirm($.i18n.prop('common.confirm.save')))) {
            return;
        }
        var data = {};
        data['documentConvertedDate'] = $('#modal-reconvert').find('#reconvert-date').val();
        data['documentConvertTo'] = $('#modal-reconvert').find('#converTo').val();
        var rowData = exportTable.row($(modalReconvertTriggerBtnEle).closest('tr')).data();
        var row = exportTable.row($(this).closest('tr'))
        data['stockNo'] = ifNotValid(rowData.stockNo, '');
        data['id'] = ifNotValid(rowData.id, '');
        var response = updateReceivedDetails(data);
        if (response.status === 'success') {
            $(modalReconvert).modal('toggle');
            exportTable.ajax.reload();
        }

    })

    //scroll
    $('#modal-export-certificate-details').find('#modal-export-certificate-details-body').slimScroll({
        start: 'bottom',
        height: '650px'
    });
    var modalpdfDetailsEle = $('#modal-export-certificate-details')
    modalpdfDetailsEle.on('click', '#modelSave', function(event) {
        var data = {}
        data = getFormData($('#modal-export-certificate-details').find('input,select,textarea'))
        console.log(data);
        var response = updateExportCertificateDetails(data);
        if (response.status === 'success') {
            $(modalpdfDetailsEle).modal('toggle');
            exportTable.ajax.reload();
        }
    })

    $('#save-fob-costs').on('click', function(e) {

        var selectedRows = exportTable.rows({
            selected: true,
            page: 'current'
        });
        var selectedRowsData = exportTable.rows({
            selected: true,
            page: 'current'
        }).nodes();
        if (selectedRows.count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return e.preventDefault();

        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return;
        }
        var rowDatas = selectedRows.data();
        if (!$('#form-export-fob').valid() || !isPurchaseEntryValid($(selectedRows.nodes()))) {
            alert($.i18n.prop('common.alert.validation.required'));
            return e.preventDefault();
        } else {//$(this).closest('tr').find('#check-box-select');
        }

        autoNumericSetRawValue($(selectedRowsData).find('input.autonumber'))
        var objectArr = [];
        var object;
        var data = {};
        exportTable.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = $(this.node());
            object = getFormData($(data).find('input'));
            objectArr.push(object);
        });
        data['fobPriceList'] = objectArr;
        console.log(data);
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/documents/tracking/update/fob-price",
            contentType: "application/json",
            success: function(data) {
                var alertEle = $('#alert-block');
                $(alertEle).css('display', '').html('<strong>Success!</strong> Stock saved.');
                $(alertEle).fadeTo(5000, 500).slideUp(500, function() {
                    $(alertEle).slideUp(500);
                    exportTable.ajax.reload();
                });
            }
        });
    })
    var rikujiModalEle = $('#modal-rikuji-remark')
    $(rikujiModalEle).on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        $(rikujiModalEle).find('input[name="rikujiUpdateToOneDate"]').datepicker("setDate", new Date());
        var data = [];
        exportTable.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var rowData = exportTable.row(this).data();
            data.push(rowData.stockNo)
        })
        if (data.length == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return false;
        }
    }).on('hidden.bs.modal', function() {
        resetElementInput(this)
    }).on("click", '#btn-rikuji-remark-submit', function(event) {

        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        var data = [];
        exportTable.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var rowData = exportTable.row(this).data();
            data.push(rowData.stockNo)
        })
        var rikujiUpdateToOneDate = $(rikujiModalEle).find('input[name="rikujiUpdateToOneDate"]').val();
        var rikujiRemarks = $(rikujiModalEle).find('textarea[name="rikujiRemarks"]').val();
        var param = "?rikujiUpdateToOneDate=" + rikujiUpdateToOneDate + "&rikujiRemarks=" + rikujiRemarks + "&flag=" + "EXPORTCERTIFICATE";
        var response = updateRikujiTo1(data, param)
        if (response.status === 'success') {
            $(rikujiModalEle).modal('toggle');
            exportTable.ajax.reload();
            setDocumentDashboardStatus();
        }
    });

})

var elementValueNotEmpty = function(elements) {
    for (var i = 0; i < elements.length; i++) {
        if (isEmpty($(elements[i]).val())) {
            return false;
        }
    }
    return true;
}
function updateReceivedDetails(data) {
    var response = "";
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        async: false,
        data: JSON.stringify(data),
        url: myContextPath + "/documents/tracking/update/reconvert",
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}
function setDocumentDashboardStatus() {
    $.getJSON(myContextPath + "/data/documents-dashboard/data-count", function(data) {
        $('#notReceived-count').html(data.data.notReceived);
        $('#received-count').html(data.data.received + "/" + data.data.receivedRikuji);
        $('#certificate-count').html(data.data.exportCertificate);
        $('#nameTransfer-count').html(data.data.nameTransfer + "/" + data.data.domestic + "/" + data.data.parts + "/" + data.data.shuppin);
        //$('#domestic-count').html(data.data.domestic);
    });
    $.getJSON(myContextPath + "/data/reauction-cancel-dashboard/status-count", function(data) {
        $('#cancel-stock').html(data.data.cancelStock);
    });
}

function updateExportCertificateDetails(data) {
    var response = "";
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        async: false,
        data: JSON.stringify(data),
        url: myContextPath + "/documents/tracking/exportCerficate/create",
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}
function getExportCertificateDetails(stockNo) {
    var response = "";
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "get",
        async: false,
        url: myContextPath + "/documents/tracking/get/exportCertificate/" + stockNo,
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}
function saveDocSendDetails(ids, data) {
    var params = {};
    params["ids"] = ids;
    var response = "";
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        async: false,
        data: JSON.stringify(data),
        url: myContextPath + "/documents/tracking/update/exportcertificate/documentdetails?" + $.param(params),
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}
function exportCertificateCrReceived(ids) {

    var response = "";
    var params = {};
    params["ids"] = ids;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        async: false,
        url: myContextPath + "/documents/tracking/update/exportcertificate/originalReceived?" + $.param(params),
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}
function setDatatableColumns(datatable, columnsshow) {
    var index = datatable.columns().indexes();
    var columnshide = $.grep(index, function(el) {
        return $.inArray(el, columnsshow) == -1
    });
    datatable.columns(columnsshow).visible(true);
    datatable.columns(columnshide).visible(false);
}

function updateRikujiTo1(data, param) {
    var response = "";
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "put",
        async: false,
        data: JSON.stringify(data),
        url: myContextPath + "/documents/tracking/updateRikujiStatus1" + param,
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}

function nonExportedCertificateCrReceived(ids) {

    var response = "";
    var params = {};
    params["ids"] = ids;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        async: false,
        url: myContextPath + "/documents/tracking/update/exportcertificate/nonExportedCrReceived?" + $.param(params),
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}
