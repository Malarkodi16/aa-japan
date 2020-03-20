var inventoryCostBody = $('#inventoryCostBody');
var tableInventoryEle = inventoryCostBody.find('table')
$(function() {
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });
    // Maker Model Json
    $.getJSON(myContextPath + "/data/makerModel.json", function(data) {
        makersJson = data;
        var ele = $('.maker-dropdown');
        $(ele).select2({
            width: '100%',
            allowClear: true,
            data: $.map(makersJson, function(item) {
                return {
                    id: item.name,
                    text: item.name,
                    data: item
                };
            })
        }).val('').trigger("change");
    })

    //Current Location
    var currentLocation;
    $.getJSON(myContextPath + "/data/locations.json", function(data) {
        currentLocation = data;
        var ele = $('#currentLocation');
        $(ele).select2({
            width: '100%',
            allowClear: true,
            data: $.map(currentLocation, function(item) {
                return {
                    id: item.displayName,
                    text: item.displayName,
                    data: item
                };
            })
        }).on('change', function() {
            currentLocation = $(this).val();
            table.draw();
        }).val('').trigger("change");
    })

    $('#makers').on('change', function() {
        var data = $(this).select2('data');
        var modelEle = $('#models');
        $(modelEle).empty();
        if (data.length > 0 && !isEmpty(data[0].data)) {
            var modelList = data[0].data.models;
            $(modelEle).select2({
                allowClear: true,
                data: $.map(modelList, function(item) {
                    return {
                        id: item.modelName,
                        text: item.modelName,
                        data: item
                    };
                })
            }).val('').trigger("change");
        }
        table.draw();
    });

    $('#models').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true,
    }).on('change', function() {
        var model = $(this).val();
        table.draw();
    });

    var purchased_min;
    var purchased_max;
    $('#table-filter-purchase-date').daterangepicker({
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
        table.draw();
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        purchased_min = '';
        purchased_max = '';
        table.draw();
        $('#table-filter-purchase-date').val('');
        $(this).remove();

    })

    //Balance Statement Datatable
    var tableEle = $('#table-report-inventory-value');
    var table = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": true,
        "ajax": myContextPath + "/accounts/report/inventoryValueReport/list",
        columnDefs: [{
            "targets": '_all',
            "defaultContent": "",

        }, {
            targets: 0,
            "className": "details-control",
            "data": "stockNo",

        }, {
            targets: 1,
            "className": "details-control",
            "data": "chassisNo",

        }, {
            targets: 2,
            "className": "details-control",
            "data": "maker",

        }, {
            targets: 3,
            "className": "details-control",
            "data": "model",

        }, {
            targets: 4,
            "className": "details-control",
            "data": "purchaseDate",

        }, {
            targets: 5,
            "className": "details-control",
            "data": "reserve",
            "render": function(data, type, row) {
                var data;
                if (row.reserve == 0) {
                    data = "NOT RESERVED"
                } else if (row.reserve == 1) {
                    data = "RESERVED"
                }
                return data
            }

        }, {
            targets: 6,
            "className": "details-control",
            "data": "reservedPersonName",

        },
        {
            targets: 7,
            "className": "details-control",
            "data": "lastLocation",

        }, {
            targets: 8,
            "className": "dt-right details-control",
            "data": "purchaseCost",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="purchaseCost" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 9,
            "className": "dt-right details-control",
            "data": "commisionCost",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="commisionCost" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 10,
            "className": "dt-right details-control",
            "data": "otherCharges",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="otherCharges" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 11,
            "className": "dt-right details-control",
            "data": "inventoryCost",
            "render": function(data, type, row) {
                data = data == 0 ? '' : data;
                    // return '<a href="#" data-toggle="modal" name="stockNo" data-target="#modal-inventoryCost-details" data-stockNo="' + row.stockNo + '"> <span class="autonumber" name="inventoryCost" data-a-sign="¥ " data-m-dec="0">' + ifNotValid(data, '') + '</span></a>';
                    // commented on click data view
                return '<span class="autonumber" name="inventoryCost" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }],
        "footerCallback": function(row, data, start, end, display) {
            var table = this.api();
            updateFooter(table);
        },
        "drawCallback": function(settings, json) {
            tableEle.find('span.autonumber').autoNumeric('init')
        }
    })
     function regex_escape(text) {
    	return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    	};

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        var filterMaker = $('#makers').find('option:selected').val();
        var filterModel = $('#models').find('option:selected').val();
        //date filter
        if (typeof purchased_min != 'undefined' && purchased_min.length != '') {
            if (aData[4].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[4], 'DD-MM-YYYY')._d.getTime();
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
        if (typeof filterMaker != 'undefined' && filterMaker.length != '') {
            if (aData[2].length == 0 || aData[2] != filterMaker) {
                return false;
            }
        }
        // Supplier filter
        if (typeof filterModel != 'undefined' && filterModel.length != '') {
            if (aData[3].length == 0 || aData[3] != filterModel) {
                return false;
            }
        }
        //Location Filter
        if (typeof currentLocation != 'undefined' && currentLocation.length != '') {
            if (aData[7].length == 0 || aData[7] != currentLocation) {
                return false;
            }
        }
        return true;
    });

    //Sub Account Detail Modal
    var inventoryCostDetailsEle = $('#modal-inventoryCost-details')
    inventoryCostDetailsEle.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(event.relatedTarget);
        var stockNo = targetElement.attr('data-stockNo');
        getInventoryInvoicesData(stockNo);
    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
        $('#inventoryCostBody').find('table').dataTable().fnDestroy();
    });
});

function getInventoryInvoicesData(stockNo) {

    inventoryCostDetailTable = tableInventoryEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": false,
        "ajax": {
            url: myContextPath + "/accounts/report/inventoryInvoiceCosts/list",
            data: function(data) {
                data.stockNo = stockNo;
            }
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": "",
            "className": "vcenter"
        }, {
            targets: 0,
            "className": "details-control",
            "name": "Type",
            "data": "type",

        }, {
            targets: 1,
            "className": "details-control",
            "name": "Amount",
            "data": "amount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
                }
            }
        }],
        "drawCallback": function(settings, json) {
            $('#table-detail-inventoryCost').find('span.autonumber').autoNumeric('init')
        }

    })

}
function updateFooter(table) {

    var intVal = function(i) {
        return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
    };
    var isValid = function(val) {
        return typeof val === 'undefined' || val == null ? 0 : val;
    }
    // page total
    // purchase cost total
    var pagePurchaseTaxTotal = table.column(8, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid($(b).find('span[name="purchaseCost"]').autoNumeric('init').autoNumeric('get')));
        return intVal(a) + amount;
    }, 0);
    // commission amount tax total
    var pageCommissionTaxTotal = table.column(9, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid($(b).find('span[name="commisionCost"]').autoNumeric('init').autoNumeric('get')));
        return intVal(a) + amount;
    }, 0);

    // Other amount total
    var pageOtherChargesTotal = table.column(10, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid($(b).find('span[name="otherCharges"]').autoNumeric('init').autoNumeric('get')));

        return intVal(a) + amount;
    }, 0);

    // Cost Of Goods amount total
    var pageInventoryCostTotal = table.column(11, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid($(b).find('span[name="inventoryCost"]').autoNumeric('init').autoNumeric('get')));

        return intVal(a) + amount;
    }, 0);

    //         $('span.autonumber.total,span.autonumber.pagetotal').autoNumeric('destroy');
    $('#table-report-inventory-value>tfoot>tr.sum').find('span.purchaseTotal').autoNumeric('init').autoNumeric('set', pagePurchaseTaxTotal);

    $('#table-report-inventory-value>tfoot>tr.sum').find('span.commisionTotal').autoNumeric('init').autoNumeric('set', pageCommissionTaxTotal);

    $('#table-report-inventory-value>tfoot>tr.sum').find('span.othersTotal').autoNumeric('init').autoNumeric('set', pageOtherChargesTotal);

    $('#table-report-inventory-value>tfoot>tr.sum').find('span.inventoryCost').autoNumeric('init').autoNumeric('set', pageInventoryCostTotal);

    //var pageOverallTotal = Number(pagePurchaseCostTotal) + Number(pageCommissionTotal) + Number(pageRoadTaxTotal) + Number(pageRecycleTotal) + Number(pageOtherChargesTotal);

    // $('#table-purchased tfoot').find('tr#grandTotal').find('.pagetotal').autoNumeric('init').autoNumeric('set', pageOverallTotal);

}
