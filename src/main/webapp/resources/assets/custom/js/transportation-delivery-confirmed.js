var tableDeliveryConfirmed;
let filterTransporterDeliveryConfirmed;
$(function() {
    $('#transporterDeliveryConfirmed').select2({
        allowClear: true
    }).on("change", function(event) {
        filterTransporterDeliveryConfirmed = $(this).find('option:selected').val();
        tableDeliveryConfirmed.draw();
    });
    var modalAcceptElement = $('#modal-delivered');
    var modalAcceptTriggerBtnEle
    $(modalAcceptElement).on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        if (tableDeliveryConfirmed.rows({
            selected: true,
            page: 'all'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'))
            return e.preventDefault();
        }
        modalAcceptTriggerBtnEle = $(e.relatedTarget);
        var data = tableDeliveryConfirmed.rows({
            selected: true,
            page: 'all'
        }).data().toArray();

        var count = 0;
        var item_tr_ele = $(this).find('table>tbody>tr.clone.hide');
        for (var i = 0; i < data.length; i++) {
            var element = $(item_tr_ele).clone();
            element.attr('data-json', JSON.stringify(data));
            $(element).find('input[name="amount"]').autoNumeric('init');
            $(element).find('td.sno').html(count + 1);
            $(element).find('td.stockNo>span').html(data[i].stockNo);
            $(element).find('td.stockNo>input[name="stockNo"]').val(data[i].stockNo);
            $(element).find('td.shuppinNo').html(data[i].lotNo);
            $(element).find('td.chassisNo').html(data[i].chassisNo);
            $(element).find('td.pickupLocation').html(data[i].sPickupLocation);
            $(element).find('td.dropLocation').html(data[i].sDropLocation);
            $(element).find('input[name="etd"]').val(ifNotValid(data[i].etd, ''));
            $(element).find('input[name="orderId"]').val(ifNotValid(data[i].invoiceNo, ''));
            $(element).find('input[name="transporterId"]').val(ifNotValid(data[i].transporter, ''));
            $(element).find('input[name="amount"]').autoNumeric('init').autoNumeric('set', ifNotValid(data[i].charge, 0));
            $(element).find('input[name="amount"]').prop('disabled', true);
            //disable if payment is initiated
            $.get({
                url: myContextPath + "/transport/invoice/status",
                async: false
            }, {
                orderId: data[i].invoiceNo,
                stockNo: data[i].stockNo
            }, function(data, status) {
                if (data === 0) {
                    $(element).find('input[name="amount"]').prop('disabled', false);
                }
            });
            count++;
            $(element).removeClass('clone hide').addClass('delete-on-close data')
            $(element).appendTo($(this).find('table>tbody'));

        }
        $(this).find('input[name="amount"]').trigger('keyup');
        // Date picker
        $(this).find('.datepicker').datepicker({
            format: "dd-mm-yyyy",
            autoclose: true
        }).on('change', function() {
            $(this).valid();

        })
        $(this).find('input.autonumber').autoNumeric('init');
    }).on('hidden.bs.modal', function() {
        $(this).find('table>tbody>tr.delete-on-close').remove();
        $(this).find('#subTotal').val('');
        $(this).find('#taxIncuded').val('');
    }).on('click', '.btn-remove-item', function() {
        if (modalAcceptElement.find('tr.data').length > 1) {
            $(this).closest('tr').remove();
        }
        var total = 0;
        $(modalAcceptElement).find('input[name="amount"]').each(function() {
            total += Number($(this).autoNumeric('get'));
        })
        $(modalAcceptElement).find('#subTotal').html(total);
        var tax = $.i18n.prop('common.tax');
        var taxAmount = Number((total * tax) / 100);
        var totalTaxIncluded = Number((total + taxAmount));
        $(modalAcceptElement).find('#taxIncuded').html(totalTaxIncluded);
    }).on('keyup', 'input[name="amount"]', function() {
        var total = 0;
        $(modalAcceptElement).find('input[name="amount"]').each(function() {
            total += Number($(this).autoNumeric('init').autoNumeric('get'));
        })
        var tax = $.i18n.prop('common.tax');
        var taxAmount = Number((total * tax) / 100);
        var totalTaxIncluded = Number((total + taxAmount));
        $(modalAcceptElement).find('#subTotal>span').autoNumeric('init').autoNumeric('set', total);
        $(modalAcceptElement).find('#taxIncuded>span').autoNumeric('init').autoNumeric('set', totalTaxIncluded);
    }).on('click', '#btn-save-invoice', function() {
        if (!confirm($.i18n.prop('confirm.transport.completed'))) {
            return false;
        }
        if (!modalAcceptElement.find('#acceptForm').find('input').valid()) {
            return false;
        }
        var dataArray = [];

        $(modalAcceptElement).find('table>tbody>tr.data').each(function() {
            var data = {};
            data.orderId = ifNotValid($(this).find('input[name="orderId"]').val(), '')
            data.stockNo = ifNotValid($(this).find('input[name="stockNo"]').val(), '')
            data.etd = ifNotValid($(this).find('input[name="etd"]').val(), '')
            data.amount = getAutonumericValue($(this).find('input[name="amount"]'));
            dataArray.push(data);
        })

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(dataArray),
            url: myContextPath + "/transport/confirm/delivered",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    tableDeliveryConfirmed.ajax.reload();
                    $(modalAcceptElement).modal('toggle');

                }

            }
        });
    })

    var tableDeliveryConfirmed_ele = $('#table-transport-delivery-confirmed');
    tableDeliveryConfirmed = tableDeliveryConfirmed_ele.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "ordering": false,
        "autowidth": false,
        "ajax": {
            url: myContextPath + "/transport/deliveryConfirmed/list/datasource",
            'data': function(data) {
                var selected = $('input[name="radioShowTable"]:checked').val();
                data["show"] = selected;
                return data;
            },
            beforeSend: function() {
                $('div#transport-delivery-confirmed>.overlay').show()
            },
            complete: function() {
                $('div#transport-delivery-confirmed>.overlay').hide();
            }
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
            data: 'purchaseDate'
        }, {
            targets: 2,
            data: 'chassisNo'
        }, {
            targets: 3,
            data: 'model'
        }, {
            targets: 4,
            data: 'supplierName'
        }, {
            targets: 5,
            data: 'auctionHouse'
        }, {
            targets: 6,
            data: 'sPickupLocation'
        }, {
            targets: 7,
            data: 'sDropLocation'
        }, {
            targets: 8,
            data: 'lotNo'
        }, {
            targets: 9,
            data: 'destinationCountry'
        }, {
            targets: 10,
            data: 'etd'
        }, {
            targets: 11,
            data: 'charge'
        }, {
            targets: 12,
            data: 'transportationCount'
        }]
    });

    tableDeliveryConfirmed.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            tableDeliveryConfirmed.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            tableDeliveryConfirmed.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            tableDeliveryConfirmed.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            tableDeliveryConfirmed.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function() {
        if (tableDeliveryConfirmed.rows({
            selected: true,
            page: 'current'
        }).count() !== tableDeliveryConfirmed.rows({
            page: 'current'
        }).count()) {
            $(tableDeliveryConfirmed_ele).find("th.select-checkbox>input").removeClass("selected");
            $(tableDeliveryConfirmed_ele).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(tableDeliveryConfirmed_ele).find("th.select-checkbox>input").addClass("selected");
            $(tableDeliveryConfirmed_ele).find("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (tableDeliveryConfirmed.rows({
            selected: true,
            page: 'current'
        }).count() !== tableDeliveryConfirmed.rows({
            page: 'current'
        }).count()) {
            $(tableDeliveryConfirmed_ele).find("th.select-checkbox>input").removeClass("selected");
            $(tableDeliveryConfirmed_ele).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(tableDeliveryConfirmed_ele).find("th.select-checkbox>input").addClass("selected");
            $(tableDeliveryConfirmed_ele).find("th.select-checkbox>input").prop('checked', true);

        }

    });
    $('#table-delivery-confirmed-filter-search').keyup(function() {
        tableDeliveryConfirmed.search($(this).val()).draw();
    });
    $('#table-delivery-confirmed-filter-length').change(function() {
        tableDeliveryConfirmed.page.len($(this).val()).draw();
    });
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex, rowData, i, j) {
        if (oSettings.sTableId == 'table-transport-delivery-confirmed') {
            if (typeof filterTransporterDeliveryConfirmed != 'undefined' && filterTransporterDeliveryConfirmed.length != '') {
                if (rowData.transporter.length == 0 || rowData.transporter != filterTransporterDeliveryConfirmed) {
                    return false;
                }
            }
        }

        return true;
    });
})
