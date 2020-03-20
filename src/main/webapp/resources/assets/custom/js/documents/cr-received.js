$(function() {
    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
        supplierJson = data;
    });
    //table Search
    $('#table-export-certificates-filter-search').keyup(function() {
        exportTable.search($(this).val()).draw();
    });
    $('#table-export-certificates-filter-length').change(function() {
        exportTable.page.len($(this).val()).draw();
    });

    var modalHandOverDocEle = $('#modal-handover-document')
    var handOverUserEle = $(modalHandOverDocEle).find('select[name="handoverToUserId"]')
    $(handOverUserEle).select2({
        allowClear: true,
        width: "100%"
    })
    $(modalHandOverDocEle).find('select[name="handoverTo"]').on('change', function() {
        $(modalHandOverDocEle).find('#select-User').removeClass('hidden')
        var department = $(this).find('option:selected').val();
        if (department != "RECYCLE_PURPOSE") {
            $.getJSON(myContextPath + "/data/user-details/by/" + department + ".json", function(data) {
                userJson = data;
                $(modalHandOverDocEle).find('#handoverToUserId').empty()
                $(handOverUserEle).select2({
                    allowClear: true,
                    width: '100%',
                    data: $.map(userJson, function(item) {
                        return {
                            id: item.userId,
                            text: item.username
                        };
                    })
                })
            })
        } else {
            $(modalHandOverDocEle).find('#select-User').addClass('hidden')
        }

    })

    exportTableEle = $('#table-export-certificates')
    var exportTable = exportTableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/documents/tracking/cr-received/data-list",
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
            // "orderable": false,
            "data": "shuppinNo",
        }, {
            targets: 3,
            "visible": false,
            // "orderable": false,
            "data": "stockNo",

        }, {
            targets: 4,
            "visible": true,
            // "orderable": false,
            "data": "chassisNo",
        }, {
            targets: 5,
            "visible": true,
            // "orderable": false,
            "data": "supplierName",
        }, {
            targets: 6,
            "visible": true,
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
            "data": "documentConvertedDate",
        }, {
            targets: 11,
            "visible": false,
            "data": "purchaseType"
        }, {
            targets: 12,
            //  "orderable": false,
            "data": "supplierCode",
            "visible": false //"searchable": false
        }, {
            targets: 13,
            // "orderable": false,
            "data": "auctionHouseId",
            "visible": false //"searchable": false
        }]

    })

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
        var posNosArr = [];
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
            if (aData[11].length == 0 || aData[11] != filterPurchaseType) {
                return false;
            }
        }
        //Supplier filter
        if (typeof filterSupplierName != 'undefined' && filterSupplierName.length != '') {
            if (aData[12].length == 0 || aData[12] != filterSupplierName) {
                return false;
            }
        }

        //Auction Housee filter
        if (typeof filterAuctionHouse != 'undefined' && filterAuctionHouse.length != '') {
            if (aData[13].length == 0 || aData[13] != filterAuctionHouse) {
                return false;
            }
        }

        return true;
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

    $(modalHandOverDocEle).on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var data = [];
        exportTable.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var rowData = exportTable.row(this).data();
            data.push(rowData)
        })
        if (data.length == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return false;
        }
    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
        $(modalHandOverDocEle).find('#select-User').removeClass('hidden')
    }).on('click', '#update-cr-doc', function() {
        var data = [];
        exportTable.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var rowData = exportTable.row(this).data();
            data.push(rowData.stockNo)
        })

        var handoverTo = $(modalHandOverDocEle).find('#handoverTo').val();
        var handoverToUserId = $(modalHandOverDocEle).find('#handoverToUserId').val();
        var param = '?handoverTo=' + handoverTo + '&handoverToUserId=' + handoverToUserId
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data),
            url: myContextPath + "/documents/tracking/cr-received/update" + param,
            async: false,
            contentType: "application/json",
            success: function(data) {
                exportTable.ajax.reload();
                $(modalHandOverDocEle).modal('toggle');

            }
        });
    })
})
