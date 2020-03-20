$(function() {
    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
        supplierJson = data;
    });
    
    var exportTrackingTable = $('#table-export-certificates-tracking').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ajax": myContextPath + "/documents/tracking/export-certificate-tracking-list",
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
            "data": "stockNo",
            "visible": false,
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
            "visible": true,
            //"orderable": false,
            "data": "chassisNo",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockno="' + row.stockNo + '">' + data + '</a>';
                }
                return data;
            }
        }, {
            targets: 2,
            "data": "purchaseInfoDate"
        }, {
            targets: 3,
            //"orderable": false,
            "data": "shuppinNo",
        }, {
            targets: 4,
           // "orderable": false,
            "data": "supplierName",
        }, {
            targets: 5,
            //"orderable": false,
            "data": "auctionHouse",
        }, {
            targets: 6,
           // "orderable": false,
            "data": "handoverTo",
            "visible": true,
            "render": function(data, type, row) {
                if (data == 0) {
                    data = "ACCOUNTS"
                } else if (data == 1) {
                    data = "SALES"
                } else if (data == 2) {
                    data = "RECYCLE PURPOSE"
                } else if (data == 3) {
                    data = "RIKUJI"
                } else if (data == 4) {
                    data = "SHIPPING"
                } else if (data == 5) {
                    data = "INSPECTION"
                } else if (data == 6) {
                    data = "DOCUMENTS"
                }
                return data;
            }
        }, {
            targets: 7,
           // "orderable": false,
            "data": "handOverPerson",
            "visible": true //"searchable": false
        }, {
            targets: 8,
            "visible": false,
            "data": "purchaseType"
        }, {
            targets: 9,
           // "orderable": false,
            "data": "supplierCode",
            "visible": false //"searchable": false
        }, {
            targets: 10,
           // "orderable": false,
            "data": "auctionHouseId",
            "visible": false //"searchable": false
        }]

    })

    //table Search
    $('#table-export-certificates-tracking-filter-search').keyup(function() {
        exportTrackingTable.search($(this).val()).draw();
    });
    $('#table-export-certificates-tracking-filter-length').change(function() {
        exportTrackingTable.page.len($(this).val()).draw();
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
        exportTrackingTable.draw();
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        purchased_min = '';
        purchased_max = '';
        exportTrackingTable.draw();
        $('#table-filter-purchased-date').val('');
        $(this).remove();

    })
    var filterPurchaseType;
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
        exportTrackingTable.draw();
    });

    var filterSupplierName
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
            exportTrackingTable.draw();
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
        exportTrackingTable.draw();
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
    var filterAuctionHouse
    $('#purchasedAuctionHouse').select2({
        placeholder: "All",
        allowClear: true
    }).on("change", function(event) {
        filterAuctionHouse = $(this).find('option:selected').val();
        exportTrackingTable.draw();

    })

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //date filter

        if (typeof purchased_min != 'undefined' && purchased_min.length != '') {
            if (aData[2].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[2], 'DD-MM-YYYY')._d.getTime();
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
            if (aData[8].length == 0 || aData[8] != filterPurchaseType) {
                return false;
            }
        }
        //Supplier filter
        if (typeof filterSupplierName != 'undefined' && filterSupplierName.length != '') {
            if (aData[9].length == 0 || aData[9] != filterSupplierName) {
                return false;
            }
        }

        //Auction Housee filter
        if (typeof filterAuctionHouse != 'undefined' && filterAuctionHouse.length != '') {
            if (aData[10].length == 0 || aData[10] != filterAuctionHouse) {
                return false;
            }
        }

        return true;
    });

    //  Stock Modal
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

})
