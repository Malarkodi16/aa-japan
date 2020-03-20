var supplierJson;
$(function() {
    $.getJSON(myContextPath + "/data/accounts/freeze/payment-booking-count", function(data) {
        setPaymentBookingDashboardStatus(data.data)
    });
    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
        supplierJson = data;
        //Auction Company Filter
        $('#auctionCompany').select2({
            allowClear: true,
            width: '100%',
            placeholder: 'All',
            data: $.map(supplierJson, function(item) {
                return {
                    id: item.supplierCode,
                    text: item.company
                };
            })
        }).on("change", function(event) {
            filterSupplierName = $(this).find('option:selected').val();
            table.draw();
        })
    });

    var due_date;
    $('#table-filter-due-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: false
    }).on("change", function(e, picker) {
        due_date = $(this).val();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.search(due_date).draw();
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        due_date = '';
        $('#table-filter-due-date').val('');
        table.search(due_date).draw();
        $(this).remove();

    })
      $('#paymentDate-form-group').on('click', '.clear-date', function() {
        due_date = '';
        $('#table-filter-payment-date').val('');
        table.search(due_date).draw();
        $(this).remove();

    })
    var payment_date;
    $('#table-filter-payment-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: false
    }).on("change", function(e, picker) {
        payment_date = $(this).val();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.search(payment_date).draw();
    });

    $('.select2-select').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true

    })
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })

    var tableEle = $('#table-auction-payment');
    var table = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ordering": true,
        "ajax": {
            url: myContextPath + "/accounts/freight-shipping/freezed",

        },
        select: {
            style: 'single',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            className: 'select-checkbox',
            visible:false,
            "data": "id",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox"  name="selRow" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">';
                }
                return data;
            }
        }, {
            targets: 1,
            "className": "details-control",
            "data": "createdDate"

        }, {
            targets: 2,
            "className": "details-control",
            "data": "invoiceNo"

        }, {
            targets: 3,
            "className": "details-control",
            "data": "invoiceName"
        }, {
            targets: 4,
            "data": "dueDate",
            "className": "details-control",
            "className": "vcenter"
        }, {

            targets: 5,
            "className": "dt-right",
            "type": "num-fmt",
            "data": "totalAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                    return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {

            targets: 6,
            "className": "dt-right",
            "type": "num-fmt",
            "data": "totalAmountUsd",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<span class="autonumber" data-a-sign="$ " data-m-dec="0">' + data + '</span>';
                }
            }
        }, {
            targets: 7,
            "data": "supplierId",
            "visible": false
        }],

        "drawCallback": function(settings, json) {
            tableEle.find('input.autonumber,span.autonumber').autoNumeric('init')

        }

    });
    table.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            table.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            table.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input.selectBox').prop('checked', false);

            });
        } else {
            table.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            table.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input.selectBox').prop('checked', true);

            });
        }
    }).on("select", function() {
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() !== table.rows({
            page: 'current'
        }).count()) {
            $(tableEle).find("th.select-checkbox>input").removeClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(tableEle).find("th.select-checkbox>input").addClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() !== table.rows({
            page: 'current'
        }).count()) {
            $(tableEle).find("th.select-checkbox>input").removeClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(tableEle).find("th.select-checkbox>input").addClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', true);

        }

    });
    function regex_escape(text) {
    	return text.replace(/,/g, "").replace(/\./gi, "").replace(/¥/g, "");
    };
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    table.on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
        } else {
            table.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = table.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    $(row.node()).removeClass('shown');
                    $(row.node()).find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                }

            })
            tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
            var detailsElement = freightShippingFormat(row.data());
            row.child(detailsElement).show();
            detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
            tr.addClass('shown');
        }
    });

    var filterSupplierName = $('#auctionCompany').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {

        //Supplier filter
        if (typeof filterSupplierName != 'undefined' && filterSupplierName.length != '') {
            if (aData[6].length == 0 || aData[6] != filterSupplierName) {
                return false;
            }
        }

        return true;
    });

    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

    });

});

function setPaymentBookingDashboardStatus(data) {
    $('#count-auction').html(data.auctionFreezed);
    $('#count-transport').html(data.transportFreezed);
    $('#count-freight').html(data.freightFreezed);
    $('#count-others').html(data.othersFreezed);
    $('#count-storage').html(data.storageFreezed);

}

//Freight Shipping Invoice Format
function freightShippingFormat(rowData) {
  var element = $('#clone-container>#invoice-details>.clone-element').clone();
    $(element).find('input[name="paymentinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.approvePaymentItems.length; i++) {
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.chassisNo').html(ifNotValid(rowData.approvePaymentItems[i].chassisNo, ''));
        $(row).find('td.freightCharge>span').html(ifNotValid(rowData.approvePaymentItems[i].freightCharge, 0.0));
        $(row).find('td.freightChargeUsd>span').html(ifNotValid(rowData.approvePaymentItems[i].freightChargeUsd, 0.0));
        $(row).find('td.shippingCharge>span').html(ifNotValid(rowData.approvePaymentItems[i].shippingCharge, 0.0));
        $(row).find('td.inspectionCharge>span').html(ifNotValid(rowData.approvePaymentItems[i].inspectionCharge, 0.0));
        $(row).find('td.radiationCharge>span').html(ifNotValid(rowData.approvePaymentItems[i].radiationCharge, 0.0));
        $(row).find('td.otherCharges>span').html(ifNotValid(rowData.approvePaymentItems[i].otherCharges, 0.0));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);
    }
    return element;

}


