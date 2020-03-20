var supplierJson, forwarderJson, forwarderDetailJson;
$(function() {

    $.getJSON(myContextPath + "/data/accounts/payment-booking-count", function(data) {
        setPaymentBookingDashboardStatus(data.data)
    });
    $.getJSON(myContextPath + "/data/forwarders.json", function(data) {
        forwarderJson = data;
        $('#container-filter-frwdr').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).val('').trigger("change");
    })
    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
        supplierJson = data;
    })

    $('.datepicker ').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: true
    }).on('change', function() {
        $(this).valid();

    });

    var shippingContainerElement = $('#shipping-container');
    $.getJSON(myContextPath + "/shipping/vessalsAndVoyageNo.json", function(data) {
        shippingContainerElement.find('#container-filter-vessel').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data.data, function(item) {
                return {
                    id: item.scheduleId,
                    text: item.shipName + ' / ' + item.voyageNo + ' [' + item.shippingCompanyName + ']'
                };
            })
        });
    })

    //table shipping requested

    var table_shipping_container = shippingContainerElement.find('#table-shipping-container').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        "ajax": {
            "url": myContextPath + "/freight/shippingRequestRoRo",
            "type": "get",
            "data": function(data) {
                data.forwarder = shippingContainerElement.find('#container-filter-frwdr').val();
                data.scheduleIds = shippingContainerElement.find('#container-filter-vessel').val();
                //                return JSON.stringify(data);
            }
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "orderable": false,
            className: 'select-checkbox',
            "data": "id",
            "width": "10px",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" id="check-box-select" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">' + '<input type="hidden" name="stockNo" value="' + row.stockNo + '"/>' + '<input type="hidden" name="invoiceType" value="/>' + '<input type="hidden" name="invoiceName" value="' + row.supplierCode + '"/>' + '<input type="hidden" name="supplierCode" value="' + row.supplierCode + '"/>' + '<input type="hidden" name="supplierName" value="' + row.supplierName + '"/>' + '<input type="hidden" name="model" value="' + row.model + '"/>' + '<input type="hidden" name="maker" value="' + row.maker + '"/>';

                }
                return data;
            }
        }, {
            targets: 1,
            orderable: false,
            className: 'details-control',
            "data": "vessel",
            "render": function(data, type, row) {
                return '<div class="container-fluid"><div class="row"><div class="col-md-12"><h5 class="font-bold">' + row.shippingCompanyName + ' - ' + row.shipName + ' [' + row.voyageNo + ']<i class="fa fa-plus-square-o pull-right" name="icon"></i></h5></div></div></div>';
            }
        }, {
            targets: 2,
            "className": "details-control",
            "data": "shippingCompanyNo",
            "visible": false
        }, {
            targets: 3,
            "className": "details-control",
            "data": "shipId",
            "visible": false
        }],
        "fnDrawCallback": function(oSettings) {
            $(oSettings.nTHead).hide();
        }

    });
    var detailsDataTable;
    table_shipping_container.on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table_shipping_container.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
        } else {
            table_shipping_container.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = table_shipping_container.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                }

            })

            var rowData = row.data();

            var detailElement = format(rowData);
            var rowWidth = $(row.node()).closest('table').width();
            detailElement.width(rowWidth - 40);
            row.child(detailElement).show();

            var detailsTable = detailElement.find('table#table-shipping-details');
            var detailTableData = rowData.items;
            //init datatable
            detailsDataTable = initContainerShippingDetailsDatatable(detailsTable, detailTableData)
            //get all port
            var orginPortArr = [];
            var destPortArr = [];
            $(detailTableData).each(function(index, item) {
                orginPortArr.push(item.orginPort);
                destPortArr.push(item.destPort);
            })
            //get unique port
            orginPortArr = unique(orginPortArr);
            destPortArr = unique(destPortArr);
            detailElement.find('select[name="orginPortFilter"]').select2({
                allowClear: true,
                width: '100%',
                data: $.map(orginPortArr, function(item) {
                    return {
                        id: item,
                        text: item
                    };
                })
            });
            detailElement.find('select[name="destPortFilter"]').select2({
                allowClear: true,
                width: '100%',
                data: $.map(destPortArr, function(item) {
                    return {
                        id: item,
                        text: item
                    };
                })
            });
            detailElement.find('input[name="freightCharges"]').on('change', function(e) {
                let freightCharge = $(this).val();
                if (!isEmpty(freightCharge)) {
                    detailElement.find('input[name="perM3Usd"], input[name="exchangeRate"]').prop('readonly', true)
                } else {
                    detailElement.find('input[name="perM3Usd"], input[name="exchangeRate"]').prop('readonly', false)
                }
            })
            detailElement.find('input[name="perM3Usd"]').on('change', function(e) {
                let m3Value = $(this).val();
                if (!isEmpty(m3Value)) {
                    detailElement.find('input[name="freightCharges"]').prop('readonly', true)
                } else {
                    detailElement.find('input[name="freightCharges"]').prop('readonly', false)
                }
            })
            detailElement.find('select[name="orginPortFilter"],select[name="destPortFilter"]').on('change', function(e) {
                var forwarderId = shippingContainerElement.find('#container-filter-frwdr').val()
                var orginPort = detailElement.find('select[name="orginPortFilter"]').val();
                var destinationPort = detailElement.find('select[name="destPortFilter"]').val();
                var params = 'frwdrId=' + forwarderId;
                params += '&orginPortFilter=' + orginPort;
                params += '&destPortFilter=' + destinationPort;

                if (!isEmpty(forwarderId) && !isEmpty(orginPort) && !isEmpty(destinationPort)) {
                    $.getJSON(myContextPath + "/data/forwarderDetail.json?" + params, function(data) {
                        forwarderDetailJson = data;
                        var row = $(this).closest('tr');
                        detailElement.find('input[name="freightCharges"]').autoNumeric('init').autoNumeric('set', forwarderDetailJson.freightCharge);
                        detailElement.find('input[name="shippingCharges"]').autoNumeric('init').autoNumeric('set', forwarderDetailJson.shippingCharge);
                        detailElement.find('input[name="inspectionCharges"]').autoNumeric('init').autoNumeric('set', forwarderDetailJson.inspectionCharge);
                        detailElement.find('input[name="radiationCharges"]').autoNumeric('init').autoNumeric('set', forwarderDetailJson.radiationCharge);

                    })
                } else {
                    var row = $(this).closest('tr');
                    detailElement.find('input[name="freightCharges"]').autoNumeric('init').autoNumeric('set', '');
                    detailElement.find('input[name="shippingCharges"]').autoNumeric('init').autoNumeric('set', '');
                    detailElement.find('input[name="inspectionCharges"]').autoNumeric('init').autoNumeric('set', '');
                    detailElement.find('input[name="radiationCharges"]').autoNumeric('init').autoNumeric('set', '');
                }
                detailsDataTable.draw();

            })

            //update charges
            detailElement.find('#btn-update-amount').on('click', function(e) {

                var row = $(this).closest('tr');
                var freightCharges = row.find('input[name="freightCharges"]').autoNumeric('get');
                var shippingCharges = row.find('input[name="shippingCharges"]').autoNumeric('get');
                var inspectionCharges = row.find('input[name="inspectionCharges"]').autoNumeric('get');
                var radiationCharges = row.find('input[name="radiationCharges"]').autoNumeric('get');
                var perM3Usd = row.find('input[name="perM3Usd"]').autoNumeric('get');
                var exchangeRate = row.find('input[name="exchangeRate"]').autoNumeric('get');
                var validateUpdation = 0;
                var data = [];

                detailsDataTable.rows({
                    selected: true,
                    page: 'current'
                }).every(function(rowIdx, tableLoop, rowLoop) {
                    var row = detailsDataTable.row(this);
                    let rowData = row.data();
                    if (!isEmpty(exchangeRate)) {//                         detailsDataTable.cell(row, 8).data(perM3Usd);
                    //                         detailsDataTable.cell(row, 10).data(perM3Usd * rowData.m3);
                    }
                    let m3 = getAutonumericValue($(detailsDataTable.cell(row, 4).node()).find('span'));
                    if (!isEmpty(exchangeRate)) {
                        setAutonumericValue($(detailsDataTable.cell(row, 8).node()).find('span'), perM3Usd);
                        setAutonumericValue($(detailsDataTable.cell(row, 9).node()).find('input[name="freightCharge"]'), perM3Usd * exchangeRate * m3);
                        setAutonumericValue($(detailsDataTable.cell(row, 10).node()).find('input[name="exchangeRate"]'), exchangeRate);
                        setAutonumericValue($(detailsDataTable.cell(row, 10).node()).find('input[name="freightChargeUsd"]'), perM3Usd * m3);
                    } else {
                        setAutonumericValue($(detailsDataTable.cell(row, 8).node()).find('span'), 0.0);
                        setAutonumericValue($(detailsDataTable.cell(row, 9).node()).find('input[name="freightCharge"]'), freightCharges * m3);
                        setAutonumericValue($(detailsDataTable.cell(row, 10).node()).find('input[name="exchangeRate"]'), 0.0);
                        setAutonumericValue($(detailsDataTable.cell(row, 10).node()).find('input[name="freightChargeUsd"]'), 0.0);
                    }
                    setAutonumericValue($(detailsDataTable.cell(row, 11).node()).find('input[name="shippingCharge"]'), shippingCharges);
                    setAutonumericValue($(detailsDataTable.cell(row, 12).node()).find('input[name="inspectionCharge"]'), inspectionCharges);
                    setAutonumericValue($(detailsDataTable.cell(row, 13).node()).find('input[name="radiationCharge"]'), radiationCharges);
                })
                updateDetailsFooter(detailsDataTable);
            })

            //Create Invoice Modal
            //             var modalTriggerBtnEle;
            //update ither charges
            //update charges
            //             

            tr.addClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
        }
    });
    $('#create-container-invoice').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }

        $('#container-forwarder').select2({
            allowClear: true,
            width: '100%',
            data: $.map(forwarderJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).val('').trigger("change");
        modalTriggerBtnEle = $(event.relatedTarget);
    }).on('hidden.bs.modal', function() {
        resetElementInput(this)
    }).on('click', '#save-container', function() {
        if (!$('#freight-shipping-form').valid()) {
            return;
        }
        var containerShihppingEle = $(modalTriggerBtnEle).closest('.shipping-container-detail-view');
        var selectedRowsEle = $(containerShihppingEle).find('table>tbody>tr').find('td:first-child').find('input:checked').closest('tr');
        var shipmentRequestIds = [];
        var containerInvcJson = [];
        var object;
        var freight = $('#containerModal')
        object = getFormData(freight.find('input,select'));
        detailsDataTable.rows({
            selected: true
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = {};

            var row = detailsDataTable.row(this);
            data.shipmentRequestId = detailsDataTable.cell(row, 0).data();
            data.stockNo = detailsDataTable.cell(row, 1).data();
            data.m3 = getAutonumericValue($(detailsDataTable.cell(row, 4).node()).find('span'));
            data.length = getAutonumericValue($(detailsDataTable.cell(row, 5).node()).find('input'));
            data.width = getAutonumericValue($(detailsDataTable.cell(row, 6).node()).find('input'));
            data.height = getAutonumericValue($(detailsDataTable.cell(row, 7).node()).find('input'));

            data.perM3Usd = getAutonumericValue($(detailsDataTable.cell(row, 8).node()).find('span'));
            data.freightCharge = getAutonumericValue($(detailsDataTable.cell(row, 9).node()).find('input'));
            data.exchangeRate = getAutonumericValue($(detailsDataTable.cell(row, 10).node()).find('input[name="exchangeRate"]'));
            data.freightChargeUsd = getAutonumericValue($(detailsDataTable.cell(row, 10).node()).find('input[name="freightChargeUsd"]'));
            data.shippingCharge = getAutonumericValue($(detailsDataTable.cell(row, 11).node()).find('input'));
            data.inspectionCharge = getAutonumericValue($(detailsDataTable.cell(row, 12).node()).find('input'));
            data.radiationCharge = getAutonumericValue($(detailsDataTable.cell(row, 13).node()).find('input'));
            data.otherCharges = getAutonumericValue($(detailsDataTable.cell(row, 14).node()).find('input'));
            // data.blNo = $(detailsDataTable.cell(row, 14).node()).find('input').val();
            data.invoiceNo = object.invoiceNo;
            data.forwarder = object.code;
            //             data.date = object.date;
            data.dueDate = object.dueDate;
            containerInvcJson.push(data);
        })

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(containerInvcJson),
            url: myContextPath + "/freight/roro/invoice/create",
            contentType: "application/json",
            async: false,
            success: function(status) {
                var alertEle = $('#alert-block');
                $('#create-container-invoice').modal('toggle');
                table_shipping_container.ajax.reload();

            }
        })

    })
    $('#container-filter-search').on('click', function() {
        table_shipping_container.ajax.reload();
    })
    table_shipping_container.on("keyup", "input.m3Calc", function() {
        let row = $(this).closest('tr');
        let length = getAutonumericValue(row.find('input[name="length"]'));
        let width = getAutonumericValue(row.find('input[name="width"]'));
        let height = getAutonumericValue(row.find('input[name="height"]'));
        let m3Element = row.find('span.m3');
        let m3 = (length * width * height) / 1000000;
        let exchangeRate = getAutonumericValue(row.find('input[name="exchangeRate"]'))
        setAutonumericValue(row.find('td>span.m3'), m3);
        let perM3Usd = getAutonumericValue(row.find('td>span.perM3Usd'));
        setAutonumericValue(row.find('td>span.freightChargeUsd'), m3 * perM3Usd);
        setAutonumericValue(row.find('td>span.freightCharge'), (m3 * perM3Usd) * exchangeRate);
        updateDetailsFooter(detailsDataTable);
    })
    table_shipping_container.on("change", 'input[name="otherCharges"]', function() {
        detailsDataTable.draw();
    })
})

function format(rowData) {
    return $('#clone-elements>#shipping-container-detail-view').clone();
}
function updateDetailsFooter(api) {
    var freight = api.column(9, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        return Number(a) + Number(getAutonumericValue($(b).find('input')));
    }, 0);
    //set footer
    setAutonumericValue($(api.column(9).footer()).find('span.autonumber.pagetotal.freightTotal'), freight)

    var freightUsd = api.column(10, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        return Number(a) + Number(getAutonumericValue($(b).find('input[name="freightChargeUsd"]')));
    }, 0);

    //set footer
    setAutonumericValue($(api.column(10).footer()).find('span.autonumber.pagetotal.freightTotalUsd'), freightUsd)

    var shipping = api.column(11, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        return Number(a) + Number(getAutonumericValue($(b).find('input')));
    }, 0);
    //set footer
    setAutonumericValue($(api.column(11).footer()).find('span.autonumber.pagetotal.shippingTotal'), shipping)

    var inspection = api.column(12, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        return Number(a) + Number(getAutonumericValue($(b).find('input')));
    }, 0);
    //set footer
    setAutonumericValue($(api.column(12).footer()).find('span.autonumber.pagetotal.inspectionTotal'), inspection)

    var radiation = api.column(13, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        return Number(a) + Number(getAutonumericValue($(b).find('input')));
    }, 0);
    //set footer
    setAutonumericValue($(api.column(13).footer()).find('span.autonumber.pagetotal.radiationTotal'), radiation)

    var others = api.column(14, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        return Number(a) + Number(getAutonumericValue($(b).find('input')));
    }, 0);
    //set footer
    setAutonumericValue($(api.column(14).footer()).find('span.autonumber.pagetotal.otherChargesTotal'), others)

}
function initContainerShippingDetailsDatatable(element, data) {
    var tableEle = element
    $('#shipping-container-detail-view').find('table.frwdr-charge-table').find('.frwdr-charge').find('input.autonumeric').autoNumeric('init');

    var table = element.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "data": data,
        "bPaginate": false,
        "bInfo": false,
        "select": {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        "ordering": false,
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            className: 'select-checkbox',
            "data": "shipmentRequestId",
            "width": "10px",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" type="checkbox" value="' + data.shipmentRequestId + '">';

                }
                return data;
            }
        }, {
            targets: 1,
            "data": "stockNo",

        }, {
            targets: 2,
            "data": "chassisNo",

        }, {
            targets: 3,
            "data": "customerFirstName",

        }, {
            targets: 4,
            "data": "m3",

            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<span style="width:150px" class="autonumber m3" data-m-dec="3">' + data + '</span>';
                }
            }

        }, {
            targets: 5,

            "data": "length",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input style="width:100%" name="length" type="text" class="form-control m3Calc autonumber" value="' + data + '">';

                }
                return data;
            }

        }, {
            targets: 6,

            "data": "width",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input style="width:100%" name="width" type="text" class="form-control m3Calc autonumber" value="' + data + '">';

                }
                return data;
            }

        }, {
            targets: 7,

            "data": "height",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input style="width:100%" name="height" type="text" class="form-control m3Calc autonumber" value="' + data + '">';

                }
                return data;
            }

        }, {
            "targets": 8,
            "data": "perM3Usd",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<span class="autonumber perM3Usd" data-a-sign="$ ">' + data + '</span>';
                }
            }

        }, {
            targets: 9,
            "data": "freightCharge",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input style="width:70px" type="text" data-a-sign="¥ " data-m-dec="0" class="form-control autonumber" name="freightCharge" value="' + data + '">';
                }
            }

        }, {
            targets: 10,
            "data": "freightChargeUsd",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input type="hidden" name="exchangeRate" value=""/><input style="width:70px" type="text" data-a-sign="$ " class="form-control autonumber" name="freightChargeUsd" value="' + data + '">';
                }
            }

        }, {
            targets: 11,
            "data": "shippingCharge",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input style="width:70px" type="text" data-a-sign="¥ " data-m-dec="0" class="form-control autonumber" name="shippingCharge" value="' + data + '">';
                }
            }

        }, {
            targets: 12,
            "data": "inspectionCharge",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input style="width:70px" type="text" data-a-sign="¥ " data-m-dec="0" class="form-control autonumber" name="inspectionCharge" value="' + data + '">';
                }
            }

        }, {
            targets: 13,
            "data": "radiationCharge",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input style="width:70px" type="text" data-a-sign="¥ " data-m-dec="0" class="form-control autonumber" name="radiationCharge" value="' + data + '">';
                }
            }

        }, {
            targets: 14,

            "data": "stockNo",
            "render": function(data, type, row) {
                return '<input style="width:80px" type="text" data-a-sign="¥ " data-m-dec="0" class="form-control autonumber" name="otherCharges" value="">';

            }
        }, {
            targets: 15,

            "visible": false,
            "render": function(data, type, row) {
                return '<input style="width:100px" type="text" class="form-control" value="">';

            }
        }, {
            targets: 16,
            "data": "orginPort",
            "visible": false

        }, {
            targets: 17,
            "data": "destPort",
            "visible": false

        }],
        "drawCallback": function(settings, json) {
            tableEle.find('input.autonumber,span.autonumber').autoNumeric('init')

        },
        "footerCallback": function(row, data, start, end, display) {
            var api = this.api(), data;
            updateDetailsFooter(api);

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

    })

    return table;
}

$.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
    //container shipping filter
    if (oSettings.sTableId == 'table-shipping-details') {
        //destination country filter
        var container = $(oSettings.nTable).closest('.shipping-container-detail-view');
        var orginPort = container.find('select[name="orginPortFilter"]').val();
        var destPort = container.find('select[name="destPortFilter"]').val();
        if (!isEmpty(orginPort)) {
            if (aData[15].length == 0 || aData[15] != orginPort) {
                return false;
            }
        }
        if (!isEmpty(destPort)) {
            if (aData[16].length == 0 || aData[16] != destPort) {
                return false;
            }
        }
    }

    return true;
})

function setPaymentBookingDashboardStatus(data) {
    $('#count-auction').html(data.auction);
    $('#count-transport').html(data.transport);
    $('#count-freight').html(data.freight);
    $('#count-others').html(data.others);
    $('#count-storage').html(data.storage);
    $('#count-prepayment').html(data.paymentAdvance);

}

function formatRoroCreatedInvoiceContainer(rowData) {
    var element = $('#clone-container>#roro-container-details>.clone-element').clone();
    $(element).find('input[name="invoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.approvePaymentItems.length; i++) {
        var row = $(rowClone).clone();
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.stockNo').html(ifNotValid(rowData.approvePaymentItems[i].stockNo, ''));
        $(row).find('td.freightCharge>span').html(ifNotValid(rowData.approvePaymentItems[i].freightCharge, ''));
        $(row).find('td.shippingCharge>span').html(ifNotValid(rowData.approvePaymentItems[i].shippingCharge, ''));
        $(row).find('td.inspectionCharge>span').html(ifNotValid(rowData.approvePaymentItems[i].inspectionCharge, ''));
        $(row).find('td.radiationCharge>span').html(ifNotValid(rowData.approvePaymentItems[i].radiationCharge, ''));
        $(row).find('td.otherCharges>span').html(ifNotValid(rowData.approvePaymentItems[i].otherCharges, ''));
        $(row).find('td.amount>span').html(ifNotValid(rowData.approvePaymentItems[i].amount, ''));
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);

    }

    return element;
}
//freight-shipping-container.js
