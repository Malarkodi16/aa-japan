$(function() {
    var table = $('#table-inventory').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
    	"pageLength" : 25,
        "ajax": myContextPath + "/inventory/inventorylist-data",
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
            "render": function(data, type, row) {
                var data;
                var className = "default";
                if (row.shippingStatus == 0) {
                    data = "INITIATED"
                } else if (row.shippingStatus == 1) {
                    data = "ACCEPTED"
                } else if (row.shippingStatus == 2) {
                    data = "RESCHEDULED"
                } else if (row.shippingStatus == 3) {
                    data = "CANCELLED"
                } else if (row.shippingStatus == 4) {
                    data = "CONTAINER_CONFIRMED"
                } else if (row.shippingStatus == 5) {
                    data = "VESSEL_CONFIRMED"
                }
                return '<span class="label label-' + className + '">' + ifNotValid(data, '') + '</span>';
            }
        }, {
            targets: 4,
            "className": "details-control",
            "data": "etd"
        }, {
            targets: 5,
            "className": "details-control",
            "data": "eta"
        }, {
            targets: 6,
            "className": "dt-right details-control",
            "data": "sellingPrice",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 7,
            "className": "details-control",
            "data": "paymentType"
        }, {
            targets: 8,
            "className": "details-control",
            "data": "paymentStatus",
            "render": function(data, type, row) {
                var data;
                var className = "default";
                if (row.paymentStatus == 0) {
                    data = "NOT_RECEIVED"
                } else if (row.paymentStatus == 1) {
                    data = "RECEIVED"
                } else if (row.paymentStatus == 2) {
                    data = "RECEIVED_PARTIAL"
                }
                return '<span class="label label-' + className + '">' + ifNotValid(data, '') + '</span>';
            }
        }, {
            targets: 9,
            "className": "details-control",
            "data": "inventoryStatus"
        }],
        "drawCallback": function(settings, json) {
            $('#table-auction-payment').find('span.autonumber').autoNumeric('init')
        }

    });
     function regex_escape(text) {
    	return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    	};
    $('#table-filter-search').keyup(function() {
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
    var paymentType;
    $('#paymentTypeFilter').change(function() {
        paymentType = $(this).val();
        table.draw();
        //table Draw
    })

    var paymentStatus;
    $('#paymentStatusFilter').change(function() {
        paymentStatus = $(this).val();
        table.draw();
        //table Draw
    })

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //PaymentType filter
        if (typeof paymentType != 'undefined' && paymentType != null && paymentType.length != '') {
            if (aData[7].length == 0 || aData[7] != paymentType) {
                return false;
            }
        }
        //PaymentStatus filter
        if (typeof paymentStatus != 'undefined' && paymentStatus != null && paymentStatus.length != '') {
            if (aData[8].length == 0 || aData[8] != paymentStatus) {
                return false;
            }
        }
        return true;
    });

})
