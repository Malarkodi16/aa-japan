var supplierJson;
$(function() {
    $('input[type="radio"][name=radioReceivedFilter].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        if ($(this).val() == 0) {
//             table_tax.column(5).visible(false);
            table_tax.column(10).visible(true);
            table_tax.ajax.reload();
        } else if ($(this).val() == 2) {
//             table_tax.column(5).visible(true);
            table_tax.column(10).visible(false);
            table_tax.ajax.reload();
        }
        table_tax.draw();
    })

    $.getJSON(myContextPath + "/data/accounts/claim-count", function(data) {
        setClaimDashboardStatus(data.data)
    });
    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
        supplierJson = data;
    });

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

            $('#purchasedAuctionHouse').select2({
                placeholder: "All"
            })
        } else {
            $(purchasedSupplierEle).prop('disabled', true);
            $('#auctionFields').css('display', 'none')
        }
        var purchaseInfoPosEle = $('#purchasedInfoPos');
        $(purchaseInfoPosEle).val($(purchaseInfoPosEle).attr('data-value')).trigger("change");
        $(purchasedSupplierEle).val('').trigger('change');
        filterPurchaseType = $('#purchaseType').val();
        table_tax.draw();
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
            table_tax.draw();
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
        table_tax.draw();
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
    var filterAuctionHouse, auctionHouseFilterRegex = '';
    $('#purchasedAuctionHouse').select2({
        placeholder: "All",
        allowClear: true
    }).on("change", function(event) {
        filterAuctionHouse = $(this).val();
        auctionHouseFilterRegex = ''
        for (var i = 0; i < filterAuctionHouse.length; i++) {
            auctionHouseFilterRegex += "\\b" + filterAuctionHouse[i] + "\\b|";
        }
        auctionHouseFilterRegex = isEmpty(auctionHouseFilterRegex) ? '' : auctionHouseFilterRegex.substring(0, auctionHouseFilterRegex.length - 1);
        table_tax.draw();
        if ((filterAuctionHouse == null || filterAuctionHouse.length == 0)) {
            $('#purchasedInfoPos').empty();
            return;
        }
        var data = $(this).select2('data');
        if (data.length > 0 && !isEmpty(data[0].data)) {
            var posNos = data[0].data.posNos;
            $('#purchasedInfoPos').empty().select2({
                placeholder: "All",
                allowClear: true,
                data: $.map(posNos, function(item) {
                    return {
                        id: item,
                        text: item
                    };
                })
            }).val('').trigger("change");
            if (posNos.length == 1) {
                $('#purchaseInfoPos').val($('#purchaseInfoPos option:eq(0)').val()).trigger('change');

            }

        }
    })
    $('#purchasedInfoPos').select2({
        placeholder: "All",
        allowClear: true
    }).on("change", function(event) {
        filterPosNo = $(this).find('option:selected').val();
        table_tax.draw();
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
        table_tax.draw();
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        purchased_min = '';
        purchased_max = '';
        table_tax.draw();
        $('#table-filter-purchased-date').val('');
        $(this).remove();

    })

    var table_tax_ele = $('#table-claim-tax');
    var table_tax = $(table_tax_ele).DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": {
            "url": myContextPath + "/accounts/claim/tax/list/datasource",
            "data": function(data) {
                data["show"] = $('input[name="radioReceivedFilter"]:checked').val();
                return data;
            },
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
            orderable: false,
            className: 'select-checkbox',
            "data": "code",
            'visible': false,
            "width": "5px",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">';
                }
                return data;
            }
        }, {
            targets: 1,
            "className": "details-control",
            "width": "50px",
            "data": "stockNo"
        }, {
            targets: 2,
            "className": "details-control",
            "width": "100px",
            "data": "chassisNo"
        }, {
            targets: 3,
            "width": "100px",
            "className": "details-control",
            "data": "auctionInfoLotNo",
        }, {
            targets: 4,
            "width": "100px",
            "className": "dt-right details-control",
            "data": "purchaseCost",
            "render": function(data, type, row) {
                data = data == null ? 0 : data;
                return '<span class="autonumber" name="purchaseCost" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 5,
            "width": "100px",
            "className": "dt-right details-control",
            "data": "commision",
            "render": function(data, type, row) {
                data = data == null ? 0 : data;
                return '<span class="autonumber" name="commision" "data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 6,
            "width": "100px",
            "className": "dt-right details-control",
            "data": "purchaseCostTax",
            "render": function(data, type, row) {
                data = data == null ? 0 : data;
                return '<span class="autonumber" name="purchaseCostTax" "data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 7,
            "width": "100px",
            "className": "dt-right details-control",
            "data": "commisionTax",
            "render": function(data, type, row) {
                data = data == null ? 0 : data;
                return '<span class="autonumber" name="commisionTax" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 8,
            "width": "100px",
            "className": "details-control",
            "visible": false,
            "data": "purchaseTaxClaimStatus",
        }, {
            targets: 9,
            "width": "100px",
            "className": "details-control",
            "visible": false,
            "data": "commisionTaxClaimStatus",
        }, {
            targets: 10,
            "width": "100px",
            "className": "details-control",
            "render": function(data, type, row) {
                var btnHtmlPurchase =   "";
                if (row.purchaseTaxClaimStatus == 0) {
                    btnHtmlPurchase = '<button type="button" class="btn btn-danger btn-xs" id="purchase-claim" name="btn-claim"><i class="fa fa-fw fa-money"></i>Purchase</button>'
                }else{
                    btnHtmlPurchase = '<button type="button" class="btn btn-success btn-xs" id="purchase-claim" name="btn-claim" disabled><i class="fa fa-fw fa-money"></i>Purchase</button>'
                }
                var btnHtml = "";
                if (row.commisionTaxClaimStatus == 0) {
                    btnHtml = '<button type="button" class="btn btn-danger btn-xs" id="commission-claim" name="btn-claim"><i class="fa fa-fw fa-money"></i>Commission</button>'
                }else{
                    btnHtml = '<button type="button" class="btn btn-success btn-xs" id="commission-claim" name="btn-claim" disabled><i class="fa fa-fw fa-money"></i>Commission</button>'
                }
                return btnHtmlPurchase + " " + btnHtml;
            }
        }, {
            targets: 11,
            "orderable": false,
            "visible": false,
            "data": "purchaseDate"
        }, {
            targets: 12,
            "orderable": false,
            "data": "purchaseInfoType",
            "visible": false //"searchable": false
        }, {
            targets: 13,
            "orderable": false,
            "data": "supplierCode",
            "visible": false //"searchable": false
        }, {
            targets: 14,
            "orderable": false,
            "data": "auctionHouseId",
            "visible": false //"searchable": false
        }, {
            targets: 15,
            "orderable": true,
            "data": "auctionInfoPosNo",
            "visible": false

        }],
        "footerCallback": function(row, data, start, end, display) {
            var tableApi = this.api();
            updateFooter(tableApi);
        },
         "drawCallback": function(settings, json) {
            $('#table-claim-tax').find('span.autonumber').autoNumeric('init')

        }

    })

    function regex_escape(text) {
    	return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    	};
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
    	var query = regex_escape($(this).val());
        table_tax.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table_tax.page.len($(this).val()).draw();
    });

    table_tax.on('click', '#purchase-claim', function() {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        var row = table_tax.row($(this).closest('tr'));
        var rowData = row.data();
        var code = rowData.code;
        var response = updateClaimReceivedAmount(code)
        if (response.status === 'success') {
            table_tax.ajax.reload();
        }
    })

    table_tax.on('click', '#commission-claim', function() {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        var row = table_tax.row($(this).closest('tr'));
        var rowData = row.data();
        var code = rowData.code;
        var response = updateCommissionClaimReceivedAmount(code)
        if (response.status === 'success') {
            table_tax.ajax.reload();
        }
    })

    var filterPurchaseType = $('#purchaseType').val();
    var filterSupplierName = $('#purchasedSupplier').find('option:selected').val();
    var filterPosNo = $('#purchasedInfoPos').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //date filter
        if (typeof purchased_min != 'undefined' && purchased_min.length != '') {
            if (aData[11].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[11], 'DD/MM/YYYY')._d.getTime();
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
        //purchase type filter
        if (typeof filterPurchaseType != 'undefined' && filterPurchaseType.length != '') {
            if (aData[12].length == 0 || aData[12] != filterPurchaseType) {
                return false;
            }
        }
        //Supplier filter
        if (typeof filterSupplierName != 'undefined' && filterSupplierName.length != '') {
            if (aData[13].length == 0 || aData[13] != filterSupplierName) {
                return false;
            }
        }

        //Auction Housee filter

        if (typeof filterAuctionHouse != 'undefined' && filterAuctionHouse.length != '') {
            if (aData[14].length == 0 || !aData[14].match(auctionHouseFilterRegex)) {
                return false;
            }
        }

        ///Pos No filter
        if (typeof filterPosNo != 'undefined' && filterPosNo.length != '') {
            if (aData[15].length == 0 || aData[15] != filterPosNo) {
                return false;
            }
        }
        return true;
    });
})

// update claim received amount
function updateClaimReceivedAmount(code) {
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
        url: myContextPath + "/accounts/claim/purchase-claim?code=" + code,
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}

function updateCommissionClaimReceivedAmount(code) {
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
        url: myContextPath + "/accounts/claim/commission-claim?code=" + code,
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}

function setClaimDashboardStatus(data) {
    $("#count-tax").html(data.purchaseTax + ' / ' + data.commissionTax);
    $('#count-recycle').html(data.recycle);
    $('#count-carTax').html(data.carTax);
    $('#count-insurance').html(data.insurance);
    $('#count-radiation').html(data.radiation);
    
}

function updateFooter(table) {

    var intVal = function(i) {
        return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
    };
    var isValid = function(val) {
        return typeof val === 'undefined' || val == null ? 0 : val;
    }
    // page total
    // Capital Amount(JPY) Total
    var purchaseCost = table.column(4, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="purchaseCost"]'))));
        return intVal(a) + amount;
    }, 0);
    // Total Payable(JPY)Total
    var commision = table.column(5, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="commision"]'))));
        return intVal(a) + amount;
    }, 0);

    // Closing Balance(JPY)Total
    var purchaseCostTax = table.column(6, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="purchaseCostTax"]'))));

        return intVal(a) + amount;
    }, 0);

    var commisionTax = table.column(7, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="commisionTax"]'))));

        return intVal(a) + amount;
    }, 0);

    $('#table-claim-tax>tfoot>tr.sum').find('span.purchaseCostTotal').autoNumeric('init').autoNumeric('set', purchaseCost);

    $('#table-claim-tax>tfoot>tr.sum').find('span.commissionTotal').autoNumeric('init').autoNumeric('set', commision);

    $('#table-claim-tax>tfoot>tr.sum').find('span.purchaseCostTaxTotal').autoNumeric('init').autoNumeric('set', purchaseCostTax);

    $('#table-claim-tax>tfoot>tr.sum').find('span.commissionTaxTotal').autoNumeric('init').autoNumeric('set', commisionTax);

}
