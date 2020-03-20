$(function() {

    var tableEle = $('#table-cost-of-sales');
    var table = tableEle.DataTable({
        "dom": '<<t>ip>',
        "pageLength" : 25,
        "ajax": myContextPath + "/costofsales/sales-converted-list",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "orderable": false,
            className: 'select-checkbox',
            "data": "id",
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
            "data": "stockNo"
        }, {
            targets: 2,
            "className": "details-control",
            "data": "chassisNo"
        }, {
            targets: 3,
            "className": "details-control",
            "data": "type"
        }, {
            targets: 4,
            "className": "dt-right",
            "type": "num-fmt",
            "className": "details-control",
            "data": "sellingPrice",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' " data-m-dec="0">' + data + '</span>';
                }
            }
        }, {
            targets: 5,
            "className": "dt-right",
            "type": "num-fmt",
            "className": "details-control",
            "data": "exchangeRateSellingPrice",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') { 
                    return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
                }
            }
        }, {
            targets: 6,
            "className": "dt-right",
            "type": "num-fmt",
            "data": "purchasePrice",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
                }
            }
        }, {
            targets: 7,
            "className": "dt-right",
            "type": "num-fmt",
            "data": "margin",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
                }
            }

        }, {
            targets: 8,
            "className": "details-control",
            "data": "etd",
            "visible": false
        }, {
            targets: 9,
            "className": "details-control",
            "data": "eta",
            "visible": false
        }, {
            targets: 10,
            "className": "details-control",
            "data": "purchaseDate"
        }, {
            targets: 11,
            "className": "details-control",
            "data": "salesDate",
            "visible": false
        }],
        "drawCallback": function(settings, json) {
            tableEle.find('input.autonumber,span.autonumber').autoNumeric('init')

        }
    });
    //end of DataTable

    //Table Global Search
    $('#table-filter-search').keyup(function() {
        tableInvoice.search($(this).val()).draw();
    });
    //Table Length
    $('#table-filter-length').change(function() {
        tableInvoice.page.len($(this).val()).draw();
    });

    //Table Select
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
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() !== table.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }

    });

    //DatePicker
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: true
    })

    var salesFilterData;
    $('#salesDate').on('change', function() {
        salesFilterData = $(this).val();
        table.draw();
    })
    $('#date-form-group').on('click', '.clear-date', function() {
        salesFilterData = '';
        $('#salesDate').val('');
        table.search(salesFilterData).draw();
        $(this).remove();

    })

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //sales date filter
        if (typeof salesFilterData != 'undefined' && salesFilterData.length != '') {
            if (aData[11].length == 0 || aData[11] != salesFilterData) {
                return false;
            }
        }

        return true;
    });
})
