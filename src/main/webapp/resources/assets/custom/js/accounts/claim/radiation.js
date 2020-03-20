$(function() {
     $.getJSON(myContextPath + "/data/accounts/claim-count", function(data) {
        setClaimDashboardStatus(data.data)
    });
    $('input[type="radio"][name=radioReceivedFilter].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        if ($(this).val() == 0) {
            table_radiation.column(6).visible(false);
            table_radiation.column(5).visible(true);
            table_radiation.ajax.reload();
        } else if ($(this).val() == 2) {
            table_radiation.column(6).visible(true);
            table_radiation.column(5).visible(false);
            table_radiation.ajax.reload();
        }
        table_radiation.draw();
    })
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
        table_radiation.draw();
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
            table_radiation.draw();
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
        table_radiation.draw();
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
        table_radiation.draw();
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
        table_radiation.draw();
    });

    /* Date picke function change */

    var recycle_min;
    var recycle_max;
    $('#table-filter-recycle-claim').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        recycle_min = picker.startDate;
        recycle_max = picker.endDate;
        picker.element.val(recycle_min.format('DD-MM-YYYY') + ' - ' + recycle_max.format('DD-MM-YYYY'));
        recycle_min = recycle_min._d.getTime();
        recycle_max = recycle_max._d.getTime();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table_radiation.draw();
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        recycle_min = '';
        recycle_max = '';
        table_radiation.draw();
        $('#table-filter-recycle-claim').val('');
        $(this).remove();

    });

    var table_radiation_ele = $('#table-claim-radiation');
    var table_radiation = $(table_radiation_ele).DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/accounts/claim/radiation/list/datasource",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": "",
        }, {
            targets: 0,
            "className": "details-control",
            "width": "50px",
            "data": "stockNo"
        }, {
            targets: 1,
            "className": "details-control",
            "width": "100px",
            "data": "chassisNo"
        }, {
            targets: 2,
            "width": "100px",
            "className": "details-control",
            "data": "shippingCompany",
        }, {
            targets: 3,
            "width": "100px",
            "className": "details-control",
            "data": "forwarder",
        }, {
            targets: 4,
            "width": "100px",
            "className": "details-control",
            "data": "createdDate",
        }, {
            targets: 5,
            "className": "dt-right details-control",
            "width": "100px",
            "render": function(data, type, row) {
                var html = ''
                html += '<span class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + ifNotValid(row.radiationCharge, 0) + '</span>'
                return html;
            }
        }, {
            targets: 6,
            "className": "dt-right details-control",
            "width": "100px",
            "render": function(data, type, row) {
                var html = ''
                html += '<span class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + ifNotValid(row.radiationClaimReceivedAmount, '') + '</span>'
                return html;
            }
        }, {
            targets: 7,
            "width": "100px",
            "className": "details-control",
            "data": "radiationClaimStatus",
            "render": function(data, type, row) {

                var btnHtml = "";
                if (row.radiationClaimStatus == 0) {
                    btnHtml = '<button type="button" class="btn btn-danger btn-xs" name="btn-claim" style=" width: 75px; "><i class="fa fa-fw fa-money"></i> Claim</button>'
                } else if (row.radiationClaimStatus == 2) {
                    btnHtml = '<button type="button" class="btn btn-success btn-xs" style=" width: 75px; "><i class="fa fa-fw fa-check-circle-o"></i> Received</button>'
                }
                return btnHtml;
            }
        }, {
            targets: 8,
            "orderable": false,
            "data": "radiationClaimStatus",
            "visible": false //"searchable": false
        }, {
            targets: 9,
            "orderable": false,
            "data": "purchaseInfoType",
            "visible": false //"searchable": false
        }, {
            targets: 10,
            "orderable": false,
            "data": "supplierCode",
            "visible": false //"searchable": false
        }, {
            targets: 11,
            "orderable": false,
            "data": "auctionHouseId",
            "visible": false //"searchable": false
        }, {
            targets: 12,
            "orderable": true,
            "data": "auctionInfoPosNo",
            "visible": false

        }],
        "drawCallback": function(settings, json) {
            $('span.autonumber').autoNumeric('init')

        }

    });

    var filterPurchaseType = $('#purchaseType').val();
    var filterSupplierName = $('#purchasedSupplier').find('option:selected').val();
    var filterPosNo = $('#purchasedInfoPos').find('option:selected').val();

    //Receivable Received draw dataTable
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        var status = $('input[name="radioReceivedFilter"]:checked').val();
        if (!isEmpty(status) && status == 2) {
            if (aData[7].length == 0 || aData[7] == 0 || aData[7] == 1) {
                return false;
            }
        } else if (!isEmpty(status) && status != 2) {
            if (aData[7].length == 0 || aData[7] == 2) {
                return false;
            }
        }
        if (typeof carTax_min != 'undefined' && carTax_min.length != '') {
            if (aData[3].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[3], 'DD-MM-YYYY')._d.getTime();
            }
            if (carTax_min && !isNaN(carTax_min)) {
                if (aData._date < carTax_min) {
                    return false;
                }
            }
            if (carTax_max && !isNaN(carTax_max)) {
                if (aData._date > carTax_max) {
                    return false;
                }
            }
        }
        //purchase type filter
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
            if (aData[10].length == 0 || !aData[10].match(auctionHouseFilterRegex)) {
                return false;
            }
        }

        ///Pos No filter
        if (typeof filterPosNo != 'undefined' && filterPosNo.length != '') {
            if (aData[11].length == 0 || aData[11] != filterPosNo) {
                return false;
            }
        }

        return true;
    })

});

function setClaimDashboardStatus(data) {
    $("#count-tax").html(data.purchaseTax + ' / ' + data.commissionTax);
    $('#count-recycle').html(data.recycle);
    $('#count-carTax').html(data.carTax);
    $('#count-insurance').html(data.insurance);
    $('#count-radiation').html(data.radiation);
}
