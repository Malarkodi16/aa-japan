$('#domestic-container').addClass('hidden')
$(function() {
    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
        supplierJson = data;
        $('#reAuctionSupplier').select2({
            allowClear: true,
            width: '100%',
            placeholder: 'Supplier',
            data: $.map(data, function(item) {
                return {
                    id: item.supplierCode,
                    text: item.company,
                    data: item
                };
            })
        })
    });
    $('input[type="radio"][name=radioShowTable].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        $('button#updateReauctionDetails').hide();
        if ($(this).val() == 2) {
            nameTransferTable.ajax.reload()
            setDatatableColumns(nameTransferTable, [0, 1, 2, 3, 4, 7, 8, 9, 10])
        } else if ($(this).val() == 3) {
            nameTransferTable.ajax.reload()
            setDatatableColumns(nameTransferTable, [0, 1, 2, 3, 4, 7, 8, 9, 17, 18])
        } else if ($(this).val() == 5) {
            nameTransferTable.ajax.reload()
            setDatatableColumns(nameTransferTable, [0, 1, 2, 3, 4, 7, 8, 9, 17, 18])
        } else if ($(this).val() == 6) {
            nameTransferTable.ajax.reload()
            setDatatableColumns(nameTransferTable, [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19])
            $('button#updateReauctionDetails').show();
        }
    })
    //Setting count in dashboard
    setDocumentDashboardStatus();
    var nameTransferTable = $('#table-name-transfer').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": {
            'url': myContextPath + "/documents/tracking/name-transfer-list",
            'data': function(data) {
                var selected = $('input[name="radioShowTable"]:checked').val();
                data["docConvertTo"] = selected;
                return data;
            }
        },
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": "",
            "className": "vcenter"
        }, {
            targets: 0,
            "orderable": false,
            className: 'select-checkbox',
            //             "visible": false,
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
            "data": "purchaseInfoDate"
        }, {
            targets: 2,
            "visible": true,
            //"orderable": false,
            "data": "shuppinNo",
        }, {
            targets: 3,
            "visible": true,
            //"orderable": false,
            "data": "stockNo",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockno="' + row.stockNo + '">' + data + '</a>';
                }
                return data;
            }
        }, {
            targets: 4,
            "visible": true,
            //"orderable": false,
            "data": "chassisNo",
        }, {
            targets: 5,
            "visible": false,
            // "orderable": false,
            "data": "supplierName",
        }, {
            targets: 6,
            "visible": false,
            // "orderable": false,
            "data": "auctionHouse",
        }, {
            targets: 7,
            "visible": true,
            // "orderable": false,
            "data": "firstRegDate",
        }, {
            targets: 8,
            "visible": true,
            //"orderable": false,
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
            //"orderable": false,
            "data": "documentReceivedDate",
        }, {
            targets: 10,
            "visible": true,
            //"orderable": false,
            "data": "documentConvertedDate",
        }, {
            targets: 11,
            "visible": false,
            // "orderable": false,
            "data": "roadTax",
        }, {
            targets: 12,
            "visible": false,
            // "orderable": false,
            "data": "insurance",
        }, {
            targets: 13,
            "visible": false,
            // "orderable": false,
            "data": "reconvert"
        }, {
            targets: 14,
            "visible": false,
            "data": "purchaseType"
        }, {
            targets: 15,
            // "orderable": false,
            "data": "supplierCode",
            "visible": false //"searchable": false
        }, {
            targets: 16,
            // "orderable": false,
            "data": "auctionHouseId",
            "visible": false //"searchable": false
        }, {
            targets: 17,
            "visible": true,
            //"orderable": false,
            "data": "numberPlateReceivedDate",
        }, {
            targets: 18,
            "visible": true,
            // "orderable": false,
            "data": "oldNumberPlate",
        }, {
            targets: 19,
            "visible": true,
            // "orderable": false,
            "data": "reauctionDate",
        }]

    })
    //table Search
    $('#table-name-transfer-filter-search').keyup(function() {
        nameTransferTable.search($(this).val()).draw();
    });
    $('#table-name-transfer-filter-length').change(function() {
        nameTransferTable.page.len($(this).val()).draw();
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
        nameTransferTable.draw();
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        purchased_min = '';
        purchased_max = '';
        nameTransferTable.draw();
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
        } else {
            $(purchasedSupplierEle).prop('disabled', true);
            $('#auctionFields').css('display', 'none')
        }
        var purchaseInfoPosEle = $('#purchasedInfoPos');
        $(purchaseInfoPosEle).val($(purchaseInfoPosEle).attr('data-value')).trigger("change");
        $(purchasedSupplierEle).val('').trigger('change');
        filterPurchaseType = $('#purchaseType').val();
        nameTransferTable.draw();
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
            nameTransferTable.draw();
            return;
        }
        filterSupplierName = supplier;
        var auctionHouseArr = [];
        var posNosArr = [];
        $.each(supplierJson, function(i, item) {
            if (item.supplierCode === supplier && item.type === purchasedType) {
                auctionHouseArr = item.supplierLocations;
                //posNosArr=item.
                return false;
            }

        });
        nameTransferTable.draw();
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
    $('#reAuctionAuctionHouse').select2({
        placeholder: "Auction House",
        allowClear: true
    })
    $('#reAuctionSupplier').select2({
        allowClear: true,
        placeholder: 'Auction House',
    }).on("change", function(event) {
        var data = $(this).select2('data');
        if (data.length > 0 && !isEmpty(data[0].data)) {
            var locations = data[0].data.supplierLocations;
            $('#reAuctionAuctionHouse').empty().select2({
                placeholder: "Auction House",
                allowClear: true,
                data: $.map(locations, function(item) {
                    return {
                        id: item.id,
                        text: item.auctionHouse
                    };
                })
            }).val('').trigger("change");
        }
    })
    $('#purchasedAuctionHouse').select2({
        placeholder: "All",
        allowClear: true
    }).on("change", function(event) {
        filterAuctionHouse = $(this).find('option:selected').val();
        nameTransferTable.draw();

    })

    // Name transfer
    var filterPurchaseType = $('#purchaseType').val();
    var filterSupplierName = $('#purchasedSupplier').find('option:selected').val();
    var filterAuctionHouse = $('#purchasedAuctionHouse').find('option:selected').val();
    //     // Domestic
    //     var filterPurchaseTypeDomestic = $('#purchaseTypeDomestic').val();
    //     var filterSupplierNameDomestic = $('#purchasedSupplierDomestic').find('option:selected').val();
    //     var filterAuctionHouseDomestic = $('#purchasedAuctionHouseDomestic').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //date filter
        if (oSettings.sTableId == "table-name-transfer") {
            if (typeof purchased_min != 'undefined' && purchased_min.length != '') {
                if (aData[1].length == 0) {
                    return false;
                }
                if (typeof aData._date == 'undefined') {
                    aData._date = moment(aData[1], 'DD-MM-YYYY')._d.getTime();
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

            if (typeof filterPurchaseType != 'undefined' && filterPurchaseType.length != '') {
                if (aData[14].length == 0 || aData[14] != filterPurchaseType) {
                    return false;
                }
            }
            //Supplier filter
            if (typeof filterSupplierName != 'undefined' && filterSupplierName.length != '') {
                if (aData[15].length == 0 || aData[15] != filterSupplierName) {
                    return false;
                }
            }

            //Auction Housee filter
            if (typeof filterAuctionHouse != 'undefined' && filterAuctionHouse.length != '') {
                if (aData[16].length == 0 || aData[16] != filterAuctionHouse) {
                    return false;
                }
            }
        }

        return true;
    });

    var modalUpdateDocType = $('#modal-update-docType')
    $(modalUpdateDocType).on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var selectedType = $('input[name="radioShowTable"]:checked').val();
        if (selectedType == "2") {
            $(modalUpdateDocType).find('#docConvertNameTransfer').removeClass('hidden')
            $(modalUpdateDocType).find('#docConvertDomestic').addClass('hidden')
            $(modalUpdateDocType).find('#docConvertParts').addClass('hidden')
            $(modalUpdateDocType).find('#docConvertShuppin').addClass('hidden')
            $(modalUpdateDocType).find('#docConvertReceived').addClass('hidden')
            $(modalUpdateDocType).find('#docConvertNotReceived').addClass('hidden')
        } else if (selectedType == "3") {
            $(modalUpdateDocType).find('#docConvertDomestic').removeClass('hidden')
            $(modalUpdateDocType).find('#docConvertNameTransfer').addClass('hidden')
            $(modalUpdateDocType).find('#docConvertParts').addClass('hidden')
            $(modalUpdateDocType).find('#docConvertShuppin').addClass('hidden')
            $(modalUpdateDocType).find('#docConvertReceived').addClass('hidden')
            $(modalUpdateDocType).find('#docConvertNotReceived').addClass('hidden')
        } else if (selectedType == "5") {
            $(modalUpdateDocType).find('#docConvertParts').removeClass('hidden')
            $(modalUpdateDocType).find('#docConvertNameTransfer').addClass('hidden')
            $(modalUpdateDocType).find('#docConvertDomestic').addClass('hidden')
            $(modalUpdateDocType).find('#docConvertShuppin').addClass('hidden')
            $(modalUpdateDocType).find('#docConvertReceived').addClass('hidden')
            $(modalUpdateDocType).find('#docConvertNotReceived').addClass('hidden')
        } else if (selectedType == "6") {
            $(modalUpdateDocType).find('#docConvertShuppin').removeClass('hidden')
            $(modalUpdateDocType).find('#docConvertNameTransfer').addClass('hidden')
            $(modalUpdateDocType).find('#docConvertParts').addClass('hidden')
            $(modalUpdateDocType).find('#docConvertDomestic').addClass('hidden')
            $(modalUpdateDocType).find('#docConvertReceived').addClass('hidden')
            $(modalUpdateDocType).find('#docConvertNotReceived').addClass('hidden')
        } 

        var data = [];
        nameTransferTable.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var rowData = nameTransferTable.row(this).data();
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
        nameTransferTable.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var rowData = nameTransferTable.row(this).data();
            data.push(rowData.stockNo)
        })
        var selectedType = $('input[name="radioShowTable"]:checked').val();
        var documentConvertTo;
        if (selectedType == "2") {
            documentConvertTo = $(modalUpdateDocType).find('#docConvertNameTransfer').find('#documentConvertTo').val();
        } else if (selectedType == "3") {
            documentConvertTo = $(modalUpdateDocType).find('#docConvertDomestic').find('#documentConvertTo').val();
        } else if (selectedType == "5") {
            documentConvertTo = $(modalUpdateDocType).find('#docConvertParts').find('#documentConvertTo').val();
        } else if (selectedType == "6") {
            documentConvertTo = $(modalUpdateDocType).find('#docConvertShuppin').find('#documentConvertTo').val();
        } 

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
            url: myContextPath + "/documents/tracking/updateConvertedType" + parm,
            async: false,
            contentType: "application/json",
            success: function(data) {
                setDocumentDashboardStatus();
                nameTransferTable.ajax.reload();
                $('#modal-update-docType').modal('toggle');
                $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Stock Updated.');
                $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                    $("#alert-block").slideUp(500);

                });

            }
        });
    })
    //update shuppin details
    let modalUpdateReauction = $('div#modal-update-reauction');
    modalUpdateReauction.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var data = [];
        nameTransferTable.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var rowData = nameTransferTable.row(this).data();
            data.push(rowData.stockNo)
        })
        if (data.length == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return false;
        }
    }).on('click', "button#save-reauction", function() {
        let oParams = {};
        let data = {};
        data["supplier"] = modalUpdateReauction.find('select#reAuctionSupplier').val();
        data["auctionHouse"] = modalUpdateReauction.find('select#reAuctionAuctionHouse').val();
        data["reauctionDate"] = modalUpdateReauction.find('input[name="reauctionDate"]').val();
        let stockNos = [];
        nameTransferTable.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var rowData = nameTransferTable.row(this).data();
            stockNos.push(rowData.stockNo)
        })
        oParams["stockNos"] = stockNos;
        let sParams = $.param(oParams);
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data),
            url: myContextPath + "/documents/tracking/updateReauctionDetails?" + sParams,
            async: true,
            contentType: "application/json",
            success: function(data) {
                nameTransferTable.ajax.reload();
                modalUpdateReauction.modal('toggle');
                $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Stock Updated.');
                $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                    $("#alert-block").slideUp(500);

                });

            }
        });
    })
    //     Stock Modal
    var stockDetailsModal = $('#modal-stock-details');
    var stockDetailsModalBody = stockDetailsModal.find('#modal-stock-details-body');
    var stockDetailsModalBodyDiv = stockDetailsModal.find('#cloneable-items');
    var stockCloneElement = $('#stock-details-html>.stock-details');
    stockDetailsModalBodyDiv.slimScroll({
        start: 'bottom',
        height: ''
    });
    stockDetailsModal.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(e.relatedTarget);
        var stockNo = targetElement.attr('data-stockNo');
        stockCloneElement.clone().appendTo(stockDetailsModalBody);
        //updateStockDetailsData
        updateStockDetailsData(stockDetailsModal, stockNo)
    }).on('hidden.bs.modal', function() {
        stockDetailsModalBody.html('');
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
        var rowData = nameTransferTable.row($(modalReconvertTriggerBtnEle).closest('tr')).data();
        var row = nameTransferTable.row($(this).closest('tr'))
        data['stockNo'] = ifNotValid(rowData.stockNo, '');
        data['id'] = ifNotValid(rowData.id, '');
        var response = updateReceivedDetails(data);
        if (response.status === 'success') {
            $(modalReconvert).modal('toggle');
            nameTransferTable.ajax.reload();
        }

    })

})
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
    });
    $.getJSON(myContextPath + "/data/reauction-cancel-dashboard/status-count", function(data) {
        $('#cancel-stock').html(data.data.cancelStock);
    });
}
function setDatatableColumns(datatable, columnsshow) {
    var index = datatable.columns().indexes();
    var columnshide = $.grep(index, function(el) {
        return $.inArray(el, columnsshow) == -1
    });
    datatable.columns(columnsshow).visible(true);
    datatable.columns(columnshide).visible(false);
}
