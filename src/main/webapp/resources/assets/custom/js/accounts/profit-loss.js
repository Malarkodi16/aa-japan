$(function() {
    var table = $('#table-profit-loss').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/accounts/report/profit-loss-data",
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
            "visible": false,
            className: 'select-checkbox',
            "data": "stockNo",
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
            "data": "stockNo",

        }, {
            targets: 2,
            "className": "details-control",
            "data": "chassisNo"
        }, {
            targets: 3,
            "className": "details-control",
            "data": "stockType",
            "render": function(data, type, row) {
                var data;
                var className = "default";
                if (row.stockType == 0) {
                    data = "STOCK"
                } else if (row.stockType == 1) {
                    data = "BIDDING"
                }
                return '<span class="label label-' + className + '">' + ifNotValid(data, '') + '</span>';
            }
        }, {
            targets: 4,
            "className": "details-control",
            "data": "firstName"
        }, {
            targets: 5,
            "className": "dt-right",
            "data": "purchaseCost",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="purchaseCost" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 6,
            "className": "dt-right",
            "data": "purchaseCostTaxAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="purchaseCostTaxAmount" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 7,
            "className": "dt-right",
            "data": "commision",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="commision" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 8,
            "className": "dt-right",
            "data": "commisionTaxAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="commisionTaxAmount" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 9,
           "className": "dt-right",
            "data": "roadTax",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="roadTax" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 10,
            "className": "dt-right",
            "data": "recycle",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="recycle" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 11,
            "className": "dt-right",
            "data": "otherCharges",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="otherCharges" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 12,
            "className": "dt-right",
            "data": "othersCostTaxAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="othersCostTaxAmount" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 13,
            "className": "dt-right",
            "data": "soldPrice",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="soldPrice" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 14,
            "className": "details-control",
            "data": "soldDate"
        }, {
            targets: 15,
            "className": "dt-right",
            "data": "shuppinCommission",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="shuppinCommission" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 16,
            "className": "dt-right",
            "data": "shuppinTaxAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="shuppinTaxAmount" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 17,
            "className": "dt-right",
            "data": "soldCommission",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="soldCommission" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 18,
            "className": "dt-right",
            "data": "soldTaxAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="soldTaxAmount" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 19,
            "className": "dt-right",
            "data": "recycleClaimed",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="recycleClaimed" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 20,
            "className": "dt-right",
            "data": "others",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="others" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 21,
            "className": "dt-right",
            data: "total",
            "render": function(data, type, row) {
                return '<span class="autonumber" name="total" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';

            }
        }, {
            targets: 22,
            "className": "dt-right",
            "data": "soldTotal",
            "render": function(data, type, row) {
                var soldPrice = Number(ifNotValid(row.soldPrice, 0));
                var soldTax = Number(ifNotValid(row.taxAmount, 0));
                var soldTotal = soldPrice + soldTax;
                return '<span class="autonumber" name="soldTotal" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 23,
            "className": "details-control",
            "render": function(data, type, row) {
                var total = Number(ifNotValid(row.total, 0));
                var soldTotal = Number(ifNotValid(row.soldTotal, 0));
                var profitLossAmount = soldTotal - total;
                var status;
                var className;
                if (row.status == 0) {
                    status = "Unsold"
                    className = "default"
                } else if (!row.status == 0 && profitLossAmount > 0) {
                    status = "Profit"
                    className = "success"
                } else if (!row.status == 0 && profitLossAmount < 0) {
                    status = "Loss"
                    className = "danger"
                }
                return '<span class="label label-' + className + '">' + status + '</span>';
            }
        }, {
            targets: 24,
            "className": "dt-right",
            "data": "profitLossAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                var total = Number(ifNotValid(row.total, 0));
                var soldTotal = Number(ifNotValid(row.soldTotal, 0));
                var profitLossAmount;
                if (row.status == 0) {
                    profitLossAmount = "";
                } else {
                    profitLossAmount = soldTotal - total;
                }
                if (profitLossAmount < 0) {
                    profitLossAmount = profitLossAmount * (-1);
                }
                return '<span class="autonumber" name="profitLossAmount" data-a-sign="¥ " data-m-dec="0">' + profitLossAmount + '</span>';
            }
        }, {
            targets: 25,
            "className": "details-control",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                var total = Number(ifNotValid(row.total, 0));
                var soldTotal = Number(ifNotValid(row.soldTotal, 0));
                var profitLossAmount = soldTotal - total;
                if (profitLossAmount < 0) {
                    profitLossAmount = profitLossAmount * (-1);
                }
                var profitLossPercentage;
                if (row.status == 0) {
                    profitLossPercentage = "";
                } else {
                    profitLossPercentage = (profitLossAmount / total) * 100;
                }

                return '<span class="autonumber" data-a-sign=" %" data-p-sign="s" data-m-dec="0">' + profitLossPercentage + '</span>';
            }
        }],
        "footerCallback": function(row, data, start, end, display) {
            var tableApi = this.api();
            updateFooter(tableApi);
        },
        "drawCallback": function(settings, json) {
            $('#table-profit-loss').find('span.autonumber').autoNumeric('init')
        }

    });
    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    }
    ;$('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    $('.select2').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true,
    });
    //    var paymentType;
    //    $('#paymentTypeFilter').change(function() {
    //        paymentType = $(this).val();
    //        table.draw();
    //        //table Draw
    //    })
    //
    //    var paymentStatus;
    //    $('#paymentStatusFilter').change(function() {
    //        paymentStatus = $(this).val();
    //        table.draw();
    //        //table Draw
    //    })
    //
    //    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
    //        //PaymentType filter
    //        if (typeof paymentType != 'undefined' && paymentType != null && paymentType.length != '') {
    //            if (aData[7].length == 0 || aData[7] != paymentType) {
    //                return false;
    //            }
    //        }
    //        //PaymentStatus filter
    //        if (typeof paymentStatus != 'undefined' && paymentStatus != null && paymentStatus.length != '') {
    //            if (aData[8].length == 0 || aData[8] != paymentStatus) {
    //                return false;
    //            }
    //        }
    //        return true;
    //    });

})

function updateFooter(table) {

    var intVal = function(i) {
        return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
    };
    var isValid = function(val) {
        return typeof val === 'undefined' || val == null ? 0 : val;
    }
    // page total
    // Capital Amount(JPY) Total
    var purchaseCost = table.column(5, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="purchaseCost"]'))));
        return intVal(a) + amount;
    }, 0);
    // Total Payable(JPY)Total
    var purchaseCostTaxAmount = table.column(6, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="purchaseCostTaxAmount"]'))));
        return intVal(a) + amount;
    }, 0);

    // Closing Balance(JPY)Total
    var commision = table.column(7, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="commision"]'))));

        return intVal(a) + amount;
    }, 0);
     var commisionTaxAmount = table.column(8, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="commisionTaxAmount"]'))));

        return intVal(a) + amount;
    }, 0);
     var roadTax = table.column(9, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="roadTax"]'))));

        return intVal(a) + amount;
    }, 0);
     var recycle = table.column(10, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="recycle"]'))));

        return intVal(a) + amount;
    }, 0);
     var otherCharges = table.column(11, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="otherCharges"]'))));

        return intVal(a) + amount;
    }, 0);
     var othersCostTaxAmount = table.column(12, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="othersCostTaxAmount"]'))));

        return intVal(a) + amount;
    }, 0);
     var soldPrice = table.column(13, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="soldPrice"]'))));

        return intVal(a) + amount;
    }, 0);

     var shuppinCommission = table.column(15, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="shuppinCommission"]'))));

        return intVal(a) + amount;
    }, 0);
     var shuppinTaxAmount = table.column(16, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="shuppinTaxAmount"]'))));

        return intVal(a) + amount;
    }, 0);
     var soldCommission = table.column(17, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="soldCommission"]'))));

        return intVal(a) + amount;
    }, 0);
     var soldTaxAmount = table.column(18, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="soldTaxAmount"]'))));

        return intVal(a) + amount;
    }, 0);
     var recycleClaimed = table.column(19, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="recycleClaimed"]'))));

        return intVal(a) + amount;
    }, 0);
     var others = table.column(20, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="others"]'))));

        return intVal(a) + amount;
    }, 0);
     var total = table.column(21, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="total"]'))));

        return intVal(a) + amount;
    }, 0);
     var soldTotal = table.column(22, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="soldTotal"]'))));

        return intVal(a) + amount;
    }, 0);

     var profitLossAmount = table.column(24, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid(getAutonumericValue($(b).find('span[name="profitLossAmount"]'))));

        return intVal(a) + amount;
    }, 0);
    

    $('#table-profit-loss>tfoot>tr.sum').find('span.purchaseCostTotal').autoNumeric('init').autoNumeric('set', purchaseCost);

    $('#table-profit-loss>tfoot>tr.sum').find('span.purchaseTaxTotal').autoNumeric('init').autoNumeric('set', purchaseCostTaxAmount);

    $('#table-profit-loss>tfoot>tr.sum').find('span.commissionTotal').autoNumeric('init').autoNumeric('set', commision);

    $('#table-profit-loss>tfoot>tr.sum').find('span.commisionTaxTotal').autoNumeric('init').autoNumeric('set', commisionTaxAmount);

    $('#table-profit-loss>tfoot>tr.sum').find('span.roadTaxTotal').autoNumeric('init').autoNumeric('set', roadTax);

    $('#table-profit-loss>tfoot>tr.sum').find('span.recycleTotal').autoNumeric('init').autoNumeric('set', recycle);

    $('#table-profit-loss>tfoot>tr.sum').find('span.otherChargesTotal').autoNumeric('init').autoNumeric('set', otherCharges);

    $('#table-profit-loss>tfoot>tr.sum').find('span.otherTaxTotal').autoNumeric('init').autoNumeric('set', othersCostTaxAmount);

    $('#table-profit-loss>tfoot>tr.sum').find('span.soldPriceTotal').autoNumeric('init').autoNumeric('set', soldPrice);

    $('#table-profit-loss>tfoot>tr.sum').find('span.shuppinCommissionTotal').autoNumeric('init').autoNumeric('set', shuppinCommission);

    $('#table-profit-loss>tfoot>tr.sum').find('span.shuppinTaxTotal').autoNumeric('init').autoNumeric('set', shuppinTaxAmount);

    $('#table-profit-loss>tfoot>tr.sum').find('span.soldCommissionTotal').autoNumeric('init').autoNumeric('set', soldCommission);

    $('#table-profit-loss>tfoot>tr.sum').find('span.soldTaxTotal').autoNumeric('init').autoNumeric('set', soldTaxAmount);

    $('#table-profit-loss>tfoot>tr.sum').find('span.recycleClaimedTotal').autoNumeric('init').autoNumeric('set', recycleClaimed);

    $('#table-profit-loss>tfoot>tr.sum').find('span.othersTotal').autoNumeric('init').autoNumeric('set', others);

    $('#table-profit-loss>tfoot>tr.sum').find('span.total').autoNumeric('init').autoNumeric('set', total);

    $('#table-profit-loss>tfoot>tr.sum').find('span.soldTotal').autoNumeric('init').autoNumeric('set', soldTotal);

    $('#table-profit-loss>tfoot>tr.sum').find('span.profitLossTotal').autoNumeric('init').autoNumeric('set', profitLossAmount);

}
