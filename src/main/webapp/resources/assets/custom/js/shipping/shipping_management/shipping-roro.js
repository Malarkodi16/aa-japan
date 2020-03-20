var countriesJson, country, port, transportersJson, shippingInstructionId, orginCountryList;
var tableRequestFromSales;
var table_shipping_requested;
var radioStatus;
$(function() {
    let shippingChassisNoMatchIndexArr = [];
    let orginPortMatchIndexArr = [];
    $(document).on('focus', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').addClass('highlight');
    });
    $(document).on('blur', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').removeClass('highlight');
    })
    shippingInstructionId = $('#request-from-sales-filter-id').val()
    $(document).on('focus', '.select2-selection--single', function(e) {
        select2_open = $(this).parent().parent().siblings('select');
        select2_open.select2('open');
    });
    $(document).on('change', '.has-error', function() {

        $(this).find('input,select,textarea').valid();
    })

    //set status
    setShippingDashboardStatus();
    $.getJSON(myContextPath + "/japan/find-port", function(data) {
        japanJson = data;
        $('div#edit-shipping-request-modal').find('select[name="orginPort"]').select2({
            allowClear: true,
            width: '100%',
            data: $.map(japanJson.port, function(item) {
                return {
                    id: item,
                    text: item,
                    data: item
                }
            })

        }).val('').trigger('change');
    })
    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        countryJson = data;
        var elements = $('.country-dropdown');
        elements.select2({
            allowClear: true,
            width: '100%',
            data: $.map(countryJson, function(item) {
                return {
                    id: item.country,
                    text: item.country,
                    data: item
                };
            })
        })

    })

    $('#port-filter-for-shipping').select2({
        allowClear: true,
        width: '100%'
    })
    $.getJSON(myContextPath + "/data/forwarders.json", function(data) {

        $('select[name="forwarder"]').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        });
    })
    $('#country-filter-available-for-shipping').on('change', function() {
        var val = ifNotValid($(this).val(), '');
        $('#port-filter-for-shipping').empty();
        if (!isEmpty(val)) {
            var data = filterOneFromListByKeyAndValue(countryJson, "country", val);
            if (data != null) {
                $('#port-filter-for-shipping').select2({
                    allowClear: true,
                    width: '100%',
                    data: $.map(data.port, function(item) {
                        return {
                            id: item,
                            text: item
                        };
                    })
                }).val('').trigger('change');
            }

        }
        table_shipping_requested.draw();
    });
    $('#port-filter-for-shipping').on('change', function() {
        table_shipping_requested.draw();
    })
    $.getJSON(myContextPath + "/japan/find-port", function(data) {
        japanPortJson = data;
        $('#orgin-port-filter-for-shipping').select2({
            allowClear: true,
            width: '100%',
            data: $.map(japanPortJson.port, function(item) {
                return {
                    id: item,
                    text: item,
                    data: item
                }
            })

        }).val('').trigger('change');
    })
    $.getJSON(myContextPath + "/data/locations.json", function(data) {
        locations = data;
        $('#current-location-filter-for-shipping').select2({
            allowClear: true,
            width: '100%',
            data: $.map(locations, function(item) {
                return {
                    id: item.code,
                    text: item.displayName,
                    data: item
                };
            })
        }).val('').trigger('change');
    })

    $.getJSON(myContextPath + "/data/shipname.json", function(data) {
        shipNameJson = data;
        var vessalAndVoyageNoEle = $('#vessalAndVoyageNo');
        vessalAndVoyageNoEle.empty();
        vessalAndVoyageNoEle.select2({
            allowClear: true,
            width: '100%',
            data: $.map(shipNameJson, function(item) {
                return {
                    id: item.name,
                    text: item.name
                };
            })
        }).val('').trigger("change");
    });
    $('#vessalAndVoyageNo').on('change', function() {
        table_shipping_requested.draw();
    });
    var etdDate;
    $('#table-filter-etd-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: false
    }).on("change", function(e, picker) {
        etdDate = $(this).val();
        $(this).closest('.input-group').find('.etd-clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon etd-clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table_shipping_requested.draw();
    });
    $('.input-group').on('click', '.etd-clear-date', function() {
        etdDate = '';
        $('#table-filter-etd-date').val('');
        $(this).remove();
        table_shipping_requested.draw();

    })

    var etaDate;
    $('#table-filter-eta-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: false
    }).on("change", function(e, picker) {
        etaDate = $(this).val();
        $(this).closest('.input-group').find('.eta-clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon eta-clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table_shipping_requested.draw();
    });
    $('.input-group').on('click', '.eta-clear-date', function() {
        etaDate = '';
        $('#table-filter-eta-date').val('');
        $(this).remove();
        table_shipping_requested.draw();

    });

    $('input[type="radio"][name="roroAllcationStatus"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        table_shipping_requested.ajax.reload()
    });
    $('input#blUpdateFlag').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        $('div#modal-update-bl div#itemBlNoContainer').show();
        $('div#modal-update-bl input[name="blNoAll"]').val('').prop('disabled', true);

    }).on('ifUnchecked', function(e) {
        $('div#modal-update-bl div#itemBlNoContainer').hide();
        $('div#modal-update-bl input[name="blNoAll"]').val('').prop('disabled', false);
    });
    var shippingRequestedShipFilterELe = $('#ship-filter');
    shippingRequestedShipFilterELe.select2({
        allowClear: true,
        width: '100%',
    }).on('change', function() {
        port = $(this).val();
        // table_shipping_requested.draw();
    })

    $('#current-location-filter-for-shipping').select2({
        allowClear: true,
        width: '100%',
    }).on('change', function() {
        table_shipping_requested.draw();
    })

    $.fn.dataTable.ext.search.push(function(settings, data, dataIndex) {
        if (settings.sTableId == 'table-shipping-requested') {
            var term = $('#table-shipping-requested-filter-search').val().toLowerCase();
            var purchased_date = $('#table-filter-purchased-date').val();

            var vesselName = $('select#vessalAndVoyageNo').find('option:selected').val();
            var destCountryFilter = $('select#country-filter-available-for-shipping').find('option:selected').val();

            var orginPortFilter = $('select#orgin-port-filter-for-shipping').find('option:selected').val();
            var currentLocationFilter = $('select#current-location-filter-for-shipping').find('option:selected').val();
            var orderItem = JSON.parse(data[4]);
            var row = table_shipping_requested.row(dataIndex);
            var tr = $(row.node());
            if (row.child.isShown()) {
                row.child.hide();
                tr.removeClass('shown');
                tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
            }
            let highlightArr = [];
            let isFound = false;
            let highlightOrginPortArr = [];
            let isOrginPortFound = false;
            for (var i = 0; i < orderItem.length; i++) {
                if (!isEmpty(term)) {
                    if (ifNotValid(orderItem[i]["chassisNo"], '').toLowerCase().indexOf(term) != -1) {
                        highlightArr.push(orderItem[i].shipmentRequestId);
                        isFound = true;

                    }
                } else if (!isEmpty(etdDate) && ifNotValid(orderItem[i]["etd"], '').indexOf(etdDate) != -1) {
                    return true;
                } else if (!isEmpty(etaDate) && ifNotValid(orderItem[i]["eta"], '').indexOf(etaDate) != -1) {
                    return true;
                } else if (!isEmpty(orginPortFilter)) {
                    if (ifNotValid(orderItem[i]["orginPort"], '').indexOf(orginPortFilter) != -1) {
                        highlightOrginPortArr.push(orderItem[i].shipmentRequestId);
                        isOrginPortFound = true;
                    }

                } else if (!isEmpty(currentLocationFilter) && ifNotValid(orderItem[i]["locationId"], '').indexOf(currentLocationFilter) != -1) {
                    return true;
                }
                if (isEmpty(term) && isEmpty(etdDate) && isEmpty(etaDate) && isEmpty(orginPortFilter) && isEmpty(currentLocationFilter)) {
                    return true
                }
            }
            if (isFound) {
                let object = {}
                object.index = dataIndex;
                object.matchIdArr = highlightArr;
                shippingChassisNoMatchIndexArr.push(object);
                return true
            }
            if (isOrginPortFound) {
                let object = {}
                object.index = dataIndex;
                object.matchIdArr = highlightOrginPortArr;
                orginPortMatchIndexArr.push(object);
                isOrginPortFound = false;
                return true
            }
        }

        return false;
    })

    // table shipping requested
    var table_shipping_requested_Ele = $('#table-shipping-requested');
    table_shipping_requested = table_shipping_requested_Ele.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": {
            "url": myContextPath + "/shipping/requested/datasource",
            "data": function(data) {

                data["show"] = $('#roro-shipment-filter input[name="roroAllcationStatus"]:checked').val();
                return data;
            }
        },

        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            orderable: false,
            className: 'details-control',
            "data": "vessel",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    var html = '';
                    var actionHtml = '';
                    html += '<a href="' + myContextPath + '/shipping/order/request?shipId=' + row.shipId + '&format=pdf" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-file-pdf-o"></i>Order request [PDF]</a>'

                    if (row.status == 0) {
                        actionHtml += '<a href="#" class="ml-5 btn btn-default btn-xs" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#edit-shipping-request-modal"><i class="fa fa-fw fa-pencil"></i>Edit</a>';
                        actionHtml += '<a href="#" class="ml-5 btn btn-success btn-xs btn-confirm-shipping" data-flag="all"  data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#accept-shipping-request-modal"><i class="fa fa-fw fa-check"></i>Confirm Shipping</a>';
                        //actionHtml += '<a class="ml-5 btn btn-primary btn-xs" name="download_arranged"><i class="fa fa-fw fa-file-excel-o"></i>Export Excel</a>';
                        let originPort = $('#orgin-port-filter-for-shipping').find('option:selected').val();
                        actionHtml += '<a href="' + myContextPath + '/shipping/roro/arranged/export/excel?forwarderId=' + row.forwarderId + '&destCountry=' + row.destCountry.replace('&', '%26') + '&allocationId=' + row.allocationId + '&status=' + row.status + '&originPort=' + originPort + '" class="ml-5 btn btn-primary btn-xs"><i class="fa fa-fw fa-file-excel-o"></i>Export Excel</a>';

                    } else if (row.status == 1) {
                        actionHtml += '<a href="#" class="ml-5 btn btn-default btn-xs" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#edit-shipping-request-modal"><i class="fa fa-fw fa-pencil"></i>Edit</a>';
                        actionHtml += '<a href="' + myContextPath + '/shipping/roro/confirmed/export/excel?scheduleId=' + row.scheduleId + '&&destCountry=' + row.destCountry.replace('&', '%26') + '" class="ml-5 btn btn-primary btn-xs"><i class="fa fa-fw fa-file-excel-o"></i>Export Excel</a>';
                        actionHtml += '<a href="' + myContextPath + '/shipping/roro/export/excel?scheduleId=' + row.scheduleId + '" class="ml-5 btn btn-primary btn-xs"><i class="fa fa-fw fa-pencil"></i>Export Excel</a>';
                        actionHtml += '<a href="#" class="ml-5 btn btn-default btn-xs btn-update-bl" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-update-bl"><i class="fa fa-fw fa-pencil"></i>Update BL</a>';
                    }
                    // else
                    // if (row.status == 1) {
                    // actionHtml += '<a href="#" class="ml-5 btn btn-primary
                    // btn-xs" data-backdrop="static" data-flag="roro"
                    // data-keyboard="false" data-toggle="modal"
                    // data-target="#modal-update-vessel-confirmed"><i class="fa
                    // fa-fw fa-pencil"></i>Update BL</a>';
                    // }
                    if ($('#roro-shipment-filter input[name="roroAllcationStatus"]:checked').val() == 0) {
                        return '<div class="container-fluid"><div class="row"><div class="col-md-12"><div class="details-container details-control pull-left"><h5 class="font-bold"><i class="fa fa-plus-square-o" name="icon"></i><span class="ml-5">' + row.forwarderName + ' - ' + row.destCountry + ' - ' + row.itemCount + ' - [' + row.allocationDate + ']' + '</span></h5></div><div class="action-container pull-right hidden">' + html + '</div><div class="action-container pull-right">' + actionHtml + '</div></div></div></div>';
                    } else {
                        return '<div class="container-fluid"><div class="row"><div class="col-md-12"><div class="details-container details-control pull-left"><h5 class="font-bold"><i class="fa fa-plus-square-o" name="icon"></i><span class="ml-5">' + row.vessel + ' - ' + row.voyageNo + ' - ' + row.destCountry + ' - ' + row.itemCount + ' - [' + row.allocationDate + ']' + '</span></h5></div><div class="action-container pull-right hidden">' + html + '</div><div class="action-container pull-right">' + actionHtml + '</div></div></div></div>';
                    }

                }
            }
        }, {
            targets: 1,
            "className": "details-control",
            "data": "shippingCompanyNo",
            "visible": false
        }, {
            targets: 2,
            "className": "details-control",
            "data": "shipId",
            "visible": false
        }, {
            targets: 3,
            "className": "details-control",
            "data": "voyageNo",
            "visible": false
        }, {
            targets: 4,
            data: 'items',
            "visible": false,
            "render": function(data, type, row) {
                return JSON.stringify(data)
            }
        }, {
            targets: 5,
            "data": "vessel",
            "visible": false
        }, {
            targets: 6,
            "data": "destCountry",
            "visible": false
        }, {
            targets: 7,
            "data": "destPort",
            "visible": false
        }],

        "fnDrawCallback": function(oSettings) {
            $(oSettings.nTHead).hide();
            shippingChassisNoMatchIndexArr = getUnique(shippingChassisNoMatchIndexArr, 'index');
            orginPortMatchIndexArr = getUnique(orginPortMatchIndexArr, 'index');
            for (let i = 0; i < shippingChassisNoMatchIndexArr.length; i++) {
                var row = table_shipping_requested.row(shippingChassisNoMatchIndexArr[i]['index']);
                var tr = $(row.node());
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                    tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
                }
                let matchIdArr = shippingChassisNoMatchIndexArr[i]['matchIdArr'];
                if (matchIdArr.length > 0) {
                    var detailsElement = format(row.data());
                    row.child(detailsElement).show();
                    detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
                    tr.addClass('shown');
                    tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
                    $(detailsElement).find('tr').css("background-color", "");
                    for (let j = 0; j < matchIdArr.length; j++) {
                        $(detailsElement).find('tr[data-id="' + matchIdArr[j] + '"]').css("background-color", "#0dd1ad");
                    }
                }

            }
            shippingChassisNoMatchIndexArr = []
            for (let i = 0; i < orginPortMatchIndexArr.length; i++) {
                var row = table_shipping_requested.row(orginPortMatchIndexArr[i]['index']);
                var tr = $(row.node());
                let data = table_shipping_requested.row(tr).data();
                let matchOrginPortIdArr = orginPortMatchIndexArr[i]['matchIdArr'];
                data = data.items.filter(function(val) {
                    return matchOrginPortIdArr.indexOf(val.shipmentRequestId) != -1;
                });
                if (matchOrginPortIdArr.length > 0) {
                    var detailsElement = format1(data);
                    row.child(detailsElement).show();
                    detailsElement.find('input.autonumber,span.autonumber').autoNumeric('init')
                    tr.addClass('shown');
                    tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
                }
            }

            orginPortMatchIndexArr = []

        }

    });

    $('#orgin-port-filter-for-shipping').select2({
        allowClear: true,
        width: '100%',
    }).on('change', function() {
        table_shipping_requested.ajax.reload();
    })

    // Customize Datatable
    $('#table-shipping-requested-filter-search').keyup(function() {
        table_shipping_requested.search($(this).val()).draw();
    });
    $('#table-shipping-requested-filter-length').change(function() {
        table_shipping_requested.page.len($(this).val()).draw();
    });
    // expand details
    table_shipping_requested.on('click', 'td.details-control div.details-container', function() {
        var tr = $(this).closest('tr');
        var row = table_shipping_requested.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
        } else {
            showDetails(tr);
        }
    })
    function showDetails(tr) {
        // var tr = $(this).closest('tr');
        var row = table_shipping_requested.row(tr)
        table_shipping_requested.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
            var row = table_shipping_requested.row(rowIdx);
            if (row.child.isShown()) {
                row.child.hide();
                tr.removeClass('shown');
            }
        })
        var detailsElement = format(row.data());
        row.child(detailsElement).show();
        //         row.child(format(row.data())).show();
        tr.addClass('shown');
        tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
        var orginPortFilter = $('select#orgin-port-filter-for-shipping').find('option:selected').val();
        if (!isEmpty(orginPortFilter)) {
            table_shipping_requested.draw();
        }

    }

    $(document).on('click', '.select_checkBox', function(e) {
        $(this).closest('tbody').find('td input:checkbox').prop('checked', this.checked);

    })
    // update shipping details
    $('#modal-update-bl').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        let requestItems = [];
        let triggerElement = $(event.relatedTarget);
        var row = table_shipping_requested.row(triggerElement.closest('tr'));
        if (row.child.isShown()) {
            let selectedRow = row.child().find('div.detail-view table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:checked')
            if (selectedRow.length > 0) {

                $.each(selectedRow, function(item) {
                    let tableData = $(this).closest('tr').data('json');
                    requestItems.push(tableData);
                })

            } else {
                alert($.i18n.prop('common.alert.stock.noselection'));
                return event.preventDefault();
            }

        } else {
            showDetails(triggerElement.closest('tr'));
            alert($.i18n.prop('common.alert.stock.noselection'));
            return event.preventDefault();
        }
        //         let rowdata = table_shipping_requested.row(triggerElement.closest('tr')).data();

        let cloneRow = $(this).find('table#table-shipping-details tbody>tr:first-child');
        for (let i = 0; i < requestItems.length; i++) {
            let item = requestItems[i];
            let row = cloneRow.clone();
            row.find('td.sNo').html(i + 1);
            row.find('td.chassisNo>span').html(item.chassisNo);
            row.find('td.chassisNo> input[name="shipmentRequestId"]').val(item.shipmentRequestId);
            row.find('td.blNo input[name="blNo"]').val(item.blNo);
            row.removeClass('hidden');
            $(this).find('table#table-shipping-details tbody').append(row);

        }

    }).on('hidden.bs.modal', function() {
        $(this).find('input[type="text"]').val('');
        $(this).find('input[type="checkbox"]').iCheck('uncheck');
        $(this).find('table#table-shipping-details tbody>tr:not(tr:first-child)').remove();
    }).on('click', 'button#btn-save', function() {

        let elements = $('#modal-update-bl').find('table#table-shipping-details tbody>tr:not(tr:first-child)');
        let dataArr = [];
        let blNo = $('#modal-update-bl input[name="blNoAll"]').val();
        $.each(elements, function(element) {
            let data = {};
            let shipmentRequestId = $(this).find('input[name="shipmentRequestId"]').val();
            if ($('#modal-update-bl input#blUpdateFlag').is(':checked')) {
                blNo = $(this).find('input[name="blNo"]').val();
            }
            data["shipmentRequestId"] = shipmentRequestId;
            data["blNo"] = blNo;
            dataArr.push(data);
        })
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(dataArr),
            async: true,
            url: myContextPath + '/shipping/management/roro/update/blNo',
            contentType: "application/json",
            success: function(data) {
                if (data.status == 'success') {
                    table_shipping_requested.ajax.reload();
                    $('#modal-update-bl').modal('toggle');
                }
            }
        });
    })
    //edit shipping request
    let shipmentRequestIds = [];
    $('#edit-shipping-request-modal').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        let relatedTarget = $(event.relatedTarget);

        var row = table_shipping_requested.row(relatedTarget.closest('tr'));
        if (row.child.isShown()) {
            let selectedRow = row.child().find('div.detail-view table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:checked')
            if (selectedRow.length > 0) {

                $.each(selectedRow, function(item) {
                    let tableData = $(this).closest('tr').data('json');
                    shipmentRequestIds.push(tableData.shipmentRequestId);
                })

            } else {
                alert($.i18n.prop('common.alert.stock.noselection'));
                return event.preventDefault();
            }

        } else {
            showDetails(relatedTarget.closest('tr'));
            alert($.i18n.prop('common.alert.stock.noselection'));
            return event.preventDefault();
        }

        var rowData;
        rowData = table_shipping_requested.row(relatedTarget.closest('tr')).data();
        var data = {};
        data["destCountry"] = ifNotValid(rowData.destCountry, '');
        data["destPort"] = ifNotValid(rowData.destPort, '');
        data["originPort"] = ifNotValid(rowData.orginPort, '');
        let scheduleId = ifNotValid(rowData.scheduleId, '');

        if (!isEmpty(data.destCountry) && !isEmpty(data.destPort)) {
            var response = findAllVessalsAndFwdrByOrginAndDestination(data);
            response = ifNotValid(response.data, {});
            var vessalArr = response.vessals;
            // init vessal dropdown
            $(this).find('select[name="scheduleId"]').select2({
                allowClear: true,
                width: '100%',
                data: $.map(vessalArr, function(item) {
                    return {
                        id: item.scheduleId,
                        text: item.shipName + ' [' + item.shippingCompanyName + '] - ' + item.voyageNo,
                        data: item
                    };
                })
            }).val(scheduleId).trigger('change');

        }
        let shipmentRequestId = rowData.shipmentRequestId

        $(this).find('select[name="forwarder"]').val(ifNotValid(rowData.forwarderId, '')).trigger('change');
        $(this).find('select[name="orginPort"]').val(ifNotValid(rowData.orginPort, '')).trigger('change');

        $(this).find('input#allocationId').val(rowData.allocationId);

    }).on('hidden.bs.modal', function() {
        resetElementInput(this);
        $(this).find('select[name="forwarder"],select[name="scheduleId"]').val('').trigger('change');
        shipmentRequestIds = [];
    }).on('click', '#btn-save', function(event) {
        var modal = $('#edit-shipping-request-modal');
        if (!modal.find('input,select,textarea').valid()) {
            return false;
        }

        var orginPort = ifNotValid(modal.find('select[name="orginPort"]').val(), '');

        var scheduleId = ifNotValid(modal.find('select[name="scheduleId"]').val(), '');
        var forwarderId = ifNotValid(modal.find('select[name="forwarder"]').val(), '');
        var allocationId = ifNotValid(modal.find('input#allocationId').val(), '');
        var showVar = $('#roro-shipment-filter input[name="roroAllcationStatus"]:checked').val();

        var data = {};
        // WARNING field 1name should be matched with entity name
        data['scheduleId'] = scheduleId;
        data['forwarderId'] = forwarderId;
        data['orginPort'] = orginPort;
        data['shipmentRequestId'] = shipmentRequestIds;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            async: true,
            url: myContextPath + '/shipping/request/edit?allocationId=' + allocationId + '&show=' + showVar,
            contentType: "application/json",
            success: function(data) {
                let result = data;
                if (result.status == 'success') {
                    table_shipping_requested.ajax.reload();
                    $('#edit-shipping-request-modal').modal('toggle');
                }
            }
        });

    })
    // on show accept shipping
    var modalShippingAcceptBtn;
    $('#accept-shipping-request-modal').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalShippingAcceptBtn = $(event.relatedTarget);

        var rowData;
        let items = [];
        let flag = modalShippingAcceptBtn.attr('data-flag');
        if (flag == 'single') {
            rowData = modalShippingAcceptBtn.closest('tr').attr('data-json');
            rowData = JSON.parse(rowData);
        } else if (flag == 'all') {
            var row = table_shipping_requested.row(modalShippingAcceptBtn.closest('tr'));
            if (row.child.isShown()) {
                let selectedRow = row.child().find('div.detail-view table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:checked')
                if (selectedRow.length > 0) {

                    $.each(selectedRow, function(item) {
                        let data = $(this).closest('tr').attr('data-json');
                        data = JSON.parse(data);
                        items.push(data);
                    })
                    let rowJson = table_shipping_requested.row(modalShippingAcceptBtn.closest('tr')).data();
                    rowData = {};
                    rowData["destCountry"] = rowJson.destCountry;
                    rowData["forwarderId"] = rowJson.forwarderId;
                    rowData["destPort"] = rowJson.destPort;
                    rowData["items"] = items;
                    rowData["allocationId"] = rowJson.allocationId;

                } else {
                    let selectedRow = row.child().find('div.detail-view table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:unChecked')
                    $.each(selectedRow, function(item) {
                        let data = $(this).closest('tr').attr('data-json');
                        data = JSON.parse(data);
                        items.push(data);
                    })
                    let rowJson = table_shipping_requested.row(modalShippingAcceptBtn.closest('tr')).data();
                    rowData = {};
                    rowData["destCountry"] = rowJson.destCountry;
                    rowData["forwarderId"] = rowJson.forwarderId;
                    rowData["destPort"] = rowJson.destPort;
                    rowData["items"] = items;
                    rowData["allocationId"] = rowJson.allocationId;
                }

            } else {
                showDetails(modalShippingAcceptBtn.closest('tr'));

                var row = table_shipping_requested.row(modalShippingAcceptBtn.closest('tr'));
                let selectedRow = row.child().find('div.detail-view table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:unChecked')

                $.each(selectedRow, function(item) {
                    let data = $(this).closest('tr').attr('data-json');
                    data = JSON.parse(data);
                    items.push(data);
                })
                let rowJson = table_shipping_requested.row(modalShippingAcceptBtn.closest('tr')).data();
                rowData = {};
                rowData["destCountry"] = rowJson.destCountry;
                rowData["forwarderId"] = rowJson.forwarderId;
                rowData["destPort"] = rowJson.destPort;
                rowData["items"] = items;
                rowData["allocationId"] = rowJson.allocationId;
            }

        }

        var data = {};
        data["destCountry"] = ifNotValid(rowData.destCountry, '');
        data["destPort"] = ifNotValid(rowData.destPort, '');
        data["originPort"] = ifNotValid(rowData.orginPort, '');

        let scheduleId;
        if (flag == 'single') {
            scheduleId = ifNotValid(rowData.scheduleId, '');
        }
        if (!isEmpty(data.destCountry) && !isEmpty(data.destPort)) {
            var response = findAllVessalsAndFwdrByOrginAndDestination(data);
            response = ifNotValid(response.data, {});
            var vessalArr = response.vessals;
            // init vessal dropdown
            $(this).find('select[name="scheduleId"]').select2({
                allowClear: true,
                width: '100%',
                data: $.map(vessalArr, function(item) {
                    return {
                        id: item.scheduleId,
                        text: item.shipName + ' [' + item.shippingCompanyName + '] - ' + item.voyageNo,
                        data: item
                    };
                })
            }).val(scheduleId).trigger('change');

        }
        let shipmentRequestId;
        if (flag == 'single') {
            shipmentRequestId = rowData.shipmentRequestId;
        } else if (flag == 'all') {
            shipmentRequestId = items.map(item=>item.shipmentRequestId)
        }
        $(this).find('select[name="forwarder"]').val(ifNotValid(rowData.forwarderId, '')).trigger('change');
        $(this).find('#shipmentRequestId').val(shipmentRequestId);
        $(this).find('input#allocationId').val(rowData.allocationId);
        $(this).find('input#flag').val(flag);
    }).on('hidden.bs.modal', function() {
        resetElementInput(this);
        $(this).find('select[name="forwarder"]').val('').trigger('change');
    }).on('click', '#btn-save', function() {
        var modal = $('#accept-shipping-request-modal');
        if (!modal.find('input,select,textarea').valid()) {
            return false;
        }
        var shipmentRequestId = ifNotValid(modal.find('#shipmentRequestId').val(), '');
        var scheduleId = ifNotValid(modal.find('select[name="scheduleId"]').val(), '');
        var forwarderId = ifNotValid(modal.find('select[name="forwarder"]').val(), '');
        var allocationId = ifNotValid(modal.find('input#allocationId').val(), '');
        var flag = ifNotValid(modal.find('input#flag').val(), '');
        var data = {};
        // WARNING field 1name should be matched with entity name
        data['scheduleId'] = scheduleId;
        data['forwarderId'] = forwarderId;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            async: true,
            url: myContextPath + '/shipping/request/accept?shipmentRequestId=' + shipmentRequestId + '&allocationId=' + allocationId,
            contentType: "application/json",
            success: function(data) {
                let result = data;
                if (result.status == 'success') {
                    if (flag == 'single') {
                        table_shipping_requested.ajax.reload();

                    } else if (flag == 'all') {
                        table_shipping_requested.ajax.reload();
                    }

                    $('#accept-shipping-request-modal').modal('toggle');
                }
            }
        });

    })

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {

        //     let lastLapVehiclesCheck = ifNotValid($('#lastLapVehiclesCheck').is(':checked'), '');
        let destinationCountry = ifNotValid($('#country-filter-available-for-shipping').val(), '');
        let destinationPort = ifNotValid($('#port-filter-for-shipping').val(), '');
        let vesselName = ifNotValid($('#vessalAndVoyageNo').val(), '');

        if (typeof destinationCountry != 'undefined' && destinationCountry.length != '') {
            if (aData[6].length == 0 || aData[6] != destinationCountry) {
                return false;
            }

        }
        if (typeof destinationPort != 'undefined' && destinationPort.length != '') {
            if (aData[7].length == 0 || aData[7] != destinationPort) {
                return false;
            }
        }
        if (typeof vesselName != 'undefined' && vesselName.length != '') {
            if (aData[5].length == 0 || aData[5] != vesselName) {
                return false;
            }
        }
        //             if ('NZE141-9107830' == 0) {}
        //     if (typeof lastLapVehiclesCheck != 'undefined' && lastLapVehiclesCheck == true) {
        //         if (ifNotValid(aData[16], '0') == '0') {
        //             return false;
        //         }
        //     }

        return true;
    });

    // var shippingCompanyNo;
    // $('#shipping-company-filter').on('change', function() {
    // shippingRequestedShipFilterELe.empty()
    // var data = $(this).select2('data');
    // if (typeof data[0].data == 'undefined') {
    // return;
    // }

    // $('#ship-filter').select2({
    // allowClear : true,
    // width : '100%',
    // data : $.map(data[0].data.items, function(item) {
    // return {
    // id : item.shipId,
    // text : item.name

    // };
    // })
    // }).val('').trigger('change');
    // shippingCompanyNo = ifNotValid($(this).val(), '');
    // table_shipping_requested.draw();
    // })
    // var shipId;
    // $('#ship-filter').on('change', function() {
    // shipId = ifNotValid($(this).val(), '');
    // table_shipping_requested.draw();
    // });

    /* Country port filter in table */
    // $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData,
    // iDataIndex) {
    // //Request from sales filters
    // if (oSettings.sTableId == 'table-request-from-sales') {

    // //id filter for notification
    // if (!isEmpty(shippingInstructionId)) {
    // if (aData[0].length == 0 || aData[0] != shippingInstructionId) {
    // return false;
    // }
    // }
    // let forwarder = ifNotValid($(
    // 'select[name="requested-forwarder-filter"]').val(), '');
    // if (typeof forwarder != 'undefined' && forwarder.length != '') {
    // if (aData[17].length == 0 || aData[17] != forwarder) {
    // return false;
    // }

    // }
    // //type filter
    // if (typeof shipmentTypeFilter != 'undefined'
    // && shipmentTypeFilter.length != '') {
    // if (aData[16].length == 0 || aData[16] != shipmentTypeFilter) {
    // return false;
    // }
    // }

    // //<!-- ./. added by krishna -->
    // // departure date && arrival date
    // if (typeof departure_date_min != 'undefined'
    // && departure_date_min.length != '') {
    // if (aData[11].length == 0) {
    // return false;
    // }
    // if (typeof aData._date == 'undefined') {
    // departure = moment(aData[11], 'MM/YYYY')._d.getTime();
    // }
    // if (departure_date_min && !isNaN(departure_date_min)) {
    // if (departure < departure_date_min) {
    // return false;
    // }
    // }
    // if (departure_date_max && !isNaN(departure_date_max)) {
    // if (departure > departure_date_max) {
    // return false;
    // }
    // }

    // }
    // if (typeof arrival_date_min != 'undefined'
    // && arrival_date_min.length != '') {
    // if (aData[12].length == 0) {
    // return false;
    // }
    // if (typeof aData._date == 'undefined') {
    // arrival = moment(aData[12], 'MM/YYYY')._d.getTime();
    // }
    // if (arrival_date_min && !isNaN(arrival_date_min)) {
    // if (arrival < arrival_date_min) {
    // return false;
    // }
    // }
    // if (arrival_date_max && !isNaN(arrival_date_max)) {
    // if (arrival > arrival_date_max) {
    // return false;
    // }
    // }

    // }
    // //country filter
    // if (typeof country != 'undefined' && country.length != '') {
    // if (aData[9].length == 0 || aData[9] != country) {
    // return false;
    // }
    // }

    // //port filter
    // if (typeof port != 'undefined' && port != null) {
    // if (aData[10].length == 0 || aData[10] != port) {
    // return false;
    // }
    // }

    // }

    // return true;
    // });
    // stock details modal update
    var stockDetailsModal = $('#modal-stock-details');
    var stockDetailsModalBody = stockDetailsModal.find('#modal-stock-details-body');
    var stockDetailsModalBodyDiv = stockDetailsModal.find('#cloneable-items');
    var stockCloneElement = $('#stock-details-html>.stock-details');
    stockDetailsModalBodyDiv.slimScroll({
        start: 'bottom',
        height: ''
    });
    stockDetailsModal.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(e.relatedTarget);
        var stockNo = targetElement.attr('data-stockNo');
        stockCloneElement.clone().appendTo(stockDetailsModalBody);
        // updateStockDetailsData
        updateStockDetailsData(stockDetailsModal, stockNo)
    }).on('hidden.bs.modal', function() {
        stockDetailsModalBody.html('');
    })

    // update shipment complete details
    let modal_update_vessel_confirmed = $('#modal-update-vessel-confirmed')
    $('#modal-update-vessel-confirmed').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var triggerElement = $(event.relatedTarget);
        let dataJson = triggerElement.closest('tr').attr('data-json');
        dataJson = JSON.parse(dataJson);

        $(this).find('input[name="blNo"]').val(dataJson.blNo);
        $(this).find('#rowData').val(JSON.stringify(dataJson));
    }).on('hidden.bs.modal', function() {
        $(this).find('input,select').val('').trigger('change');
        $(this).find('#rowData').attr('data-json', '');
    }).on('click', '#btn-save', function() {

        if (!$('#modal-update-vessel-confirmed form').valid()) {
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        let rowdata = {};
        rowdata = modal_update_vessel_confirmed.find('#rowData').val();
        rowdata = JSON.parse(rowdata);
        // let allocationId = rowdata.allocationId;
        let shipmentRequestId = rowdata.shipmentRequestId;
        let data = {}
        data["blNo"] = modal_update_vessel_confirmed.find('input[name="blNo"]').val();
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            async: true,
            url: myContextPath + '/shipping/container/allocation/update/roro/confirmed?shipmentRequestId=' + shipmentRequestId,
            contentType: "application/json",
            success: function(data) {
                if (data.status == 'success') {
                    table_shipping_requested.ajax.reload();
                    modal_update_vessel_confirmed.modal('toggle');
                }
            }
        });

    });

    // update shipment complete details
    let modalRemarkEle = $('#modal-update-remark')
    var triggerElement;
    $(modalRemarkEle).on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        triggerElement = $(event.relatedTarget);

    }).on('hidden.bs.modal', function() {
        $(this).find('input').val('');
        $(this).find('#rowData').attr('data-json', '');
    }).on('click', '#save-remark-modal', function() {
        if (confirm($.i18n.prop('confirm.shipping.request.cancel'))) {
            var reqData = {};
            var tr = $(triggerElement).closest('tr');
            var detailsContainer = tr.closest('.detail-view')
            var data = tr.attr('data-json');
            data = JSON.parse(data);
            reqData["shipmentRequestId"] = data.shipmentRequestId;
            reqData["allocationId"] = data.allocationId;
            reqData["remarks"] = $(modalRemarkEle).find('textarea[name="remarks"]').val();
            var result;
            if (data.status == 0) {
                console.log("arranged")
                result = cancelShippingRequestArranged(reqData);
            } else {
                console.log("confirmed")
                result = cancelShippingRequestConfirmed(reqData);
            }
            if (result.status == 'success') {
                $(modalRemarkEle).modal('toggle')
                table_shipping_requested.ajax.reload();
                //                 var parentRow = detailsContainer.closest('tr').prev('tr');
                //                 var row = table_shipping_requested.row(parentRow);
                //                 if (result.data != null) {
                //                     row.data(result.data).invalidate();
                //                     tr.remove();
                //                     row.child.hide();
                //                 } else {
                //                     row.remove().draw();
                //                 }

            }
        }
    })
})
$(function() {
    $('form#shipping-arrangement-form-update').validate({
        highlight: function(element) {
            $(element).parent().addClass("has-error");
        },
        unhighlight: function(element) {
            $(element).parent().removeClass("has-error");
        },
        onfocusout: function(element) {
            $(element).valid();
        },
        errorPlacement: function(error, element) {
            var isFound = false;
            var itr = 0;
            while (!isFound && itr < 5) {
                var e = $(element).parent();
                if (e.find('.help-block').length > 0) {
                    isFound = true;
                }
                element = e;
                itr++;
            }
            if (isFound) {
                $(element).find('.help-block').text(error.text());
            }

        },
        success: function(element) {
            var isFound = false;
            var itr = 0;
            while (!isFound && itr < 5) {
                var e = $(element).parent();
                if (e.find('.help-block').length > 0) {
                    isFound = true;
                }
                element = e;
                itr++;
            }
            if (isFound) {
                $(element).find('.help-block').hide();
            }

        },
        rules: {
            'scheduleId': 'required',
            'forwarder': 'required'

        }
    });
})

function findAllVessalsAndFwdrByOrginAndDestination(data) {
    var result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "get",
        data: data,
        async: false,
        url: myContextPath + '/shipping/vessalsAndFwdr.json',
        contentType: "application/json",
        success: function(data) {
            result = data;
        }
    });
    return result;
}
function saveShippingRequest(data) {
    var result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        data: JSON.stringify(data),
        async: false,
        url: myContextPath + '/shipping/request/roro/save',
        contentType: "application/json",
        success: function(data) {
            result = data;
        }
    });
    return result;
}

function cancelShippingRequestArranged(data) {
    var result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        async: false,
        data: data,
        url: myContextPath + '/shipping/request/cancel/arranged',
        success: function(data) {
            result = data;
        }
    });
    return result;
}
function cancelShippingRequestConfirmed(data) {
    var result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        async: false,
        data: data,
        url: myContextPath + '/shipping/request/cancel/confirmed',
        success: function(data) {
            result = data;
        }
    });
    return result;
}
function format(rowData) {
    var element = $('#shipping-requested-details-view>.detail-view').clone();
    element.find('input[name="shipId"]').val(rowData.shipId)
    var tbody = '';
    if (rowData.status == 0) {
        $(element).find('table th.action,th.remarks,td.remarks').removeClass('hidden');
        $(element).find('table td').find('a.accept-shipping,a.cancel-shipping').removeClass('hidden');
    } else if (rowData.status == 1) {
        $(element).find('table th.action,th.blNo,td.blNo').removeClass('hidden');
        $(element).find('table td').find('a.update-bl,a.cancel-shipping').removeClass('hidden');
    } else if (rowData.status == 6) {
        $(element).find('table th.action,th.blNo,td.blNo').removeClass('hidden');
        $(element).find('table td').find('a.update-bl').removeClass('hidden');
    } else if (rowData.status == 3) {
        $(element).find('table th.remarks,td.remarks').removeClass('hidden');
    }
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.items.length; i++) {
        var row = $(rowClone).clone();
        row.attr('data-json', JSON.stringify(rowData.items[i]));
        let status = ""
          , className = "";
        if (ifNotValid(rowData.items[i].status, '') == 0) {
            $(row).find('td.status>span.label').addClass('label-default');
            $(row).find('td.status>span.label').html('Initiated');
        } else if (ifNotValid(rowData.items[i].status, '') == 1) {
            $(row).find('td.status>span.label').addClass('label-success');
            $(row).find('td.status>span.label').html('Confirmed');
        } else if (ifNotValid(rowData.items[i].status, '') == 3) {
            $(row).find('td.status>span.label').addClass('label-danger');
            $(row).find('td.status>span.label').html('Cancelled');
        }
        // $(row).find('td.s-no').html(i + 1);
        row.attr('data-id', rowData.items[i].shipmentRequestId)
        data = rowData.items[i].stockNo == null ? '' : rowData.items[i].stockNo;
        $(row).find('td.stockNo').html('<input type="hidden" name="stockNo" value="' + data + '"/><a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockNo="' + data + '">' + data + '</a>');
        $(row).find('td.chassisNo').html(ifNotValid(rowData.items[i].chassisNo, ''));
        $(row).find('td.purchaseDate').html(ifNotValid(rowData.items[i].purchaseDate, ''));
        $(row).find('td.locationName').html(ifNotValid(rowData.items[i].locationName, ''));
        $(row).find('td.originPort').html(ifNotValid(rowData.items[i].orginPort, ''));
        $(row).find('td.vessel').html(ifNotValid(rowData.items[i].vessel, ''));
        $(row).find('td.destinationPort').html(ifNotValid(rowData.items[i].destPort, ''));
        $(row).find('td.destinationCountry').html(ifNotValid(rowData.items[i].destCountry, ''));
        $(row).find('td.blNo').html(ifNotValid(rowData.items[i].blNo, ''));
        $(row).find('td.etd').html(ifNotValid(rowData.items[i].sEtd, ''));
        $(row).find('td.eta').html(ifNotValid(rowData.items[i].sEta, ''));
        $(row).find('td.inspectionDate').html(ifNotValid(rowData.items[i].inspectionDate, ''));
        $(row).find('td.inspectionStatus').html(ifNotValid(rowData.items[i].inspectionStatus, ''));
        $(row).find('td.inspectionDateOfIssue').html(ifNotValid(rowData.items[i].inspectionDateOfIssue, ''));
        $(row).find('td.remarks').html(ifNotValid(rowData.items[i].remarks, ''));
        $(row).find('td.documentConvertedDate').html(ifNotValid(rowData.items[i].documentConvertedDate, ''));

        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);

    }

    return element;
}

function format1(rowData) {
    var element = $('#shipping-requested-details-view>.detail-view').clone();
    element.find('input[name="shipId"]').val(rowData.shipId)
    var tbody = '';
    if (rowData.status == 0) {
        $(element).find('table th.action,th.remarks,td.remarks').removeClass('hidden');
        $(element).find('table td').find('a.accept-shipping,a.cancel-shipping').removeClass('hidden');
    } else if (rowData.status == 1) {
        $(element).find('table th.action,th.blNo,td.blNo').removeClass('hidden');
        $(element).find('table td').find('a.update-bl,a.cancel-shipping').removeClass('hidden');
    } else if (rowData.status == 6) {
        $(element).find('table th.action,th.blNo,td.blNo').removeClass('hidden');
        $(element).find('table td').find('a.update-bl').removeClass('hidden');
    } else if (rowData.status == 3) {
        $(element).find('table th.remarks,td.remarks').removeClass('hidden');
    }
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.length; i++) {
        var row = $(rowClone).clone();
        row.attr('data-json', JSON.stringify(rowData[i]));
        let status = ""
          , className = "";
        if (ifNotValid(rowData[i].status, '') == 0) {
            $(row).find('td.status>span.label').addClass('label-default');
            $(row).find('td.status>span.label').html('Initiated');
        } else if (ifNotValid(rowData[i].status, '') == 1) {
            $(row).find('td.status>span.label').addClass('label-success');
            $(row).find('td.status>span.label').html('Confirmed');
        } else if (ifNotValid(rowData[i].status, '') == 3) {
            $(row).find('td.status>span.label').addClass('label-danger');
            $(row).find('td.status>span.label').html('Cancelled');
        }
        // $(row).find('td.s-no').html(i + 1);
        row.attr('data-id', rowData[i].shipmentRequestId)
        data = rowData[i].stockNo == null ? '' : rowData[i].stockNo;
        $(row).find('td.stockNo').html('<input type="hidden" name="stockNo" value="' + data + '"/><a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockNo="' + data + '">' + data + '</a>');
        $(row).find('td.chassisNo').html(ifNotValid(rowData[i].chassisNo, ''));
        $(row).find('td.purchaseDate').html(ifNotValid(rowData[i].purchaseDate, ''));
        $(row).find('td.locationName').html(ifNotValid(rowData[i].locationName, ''));
        $(row).find('td.originPort').html(ifNotValid(rowData[i].orginPort, ''));
        $(row).find('td.vessel').html(ifNotValid(rowData[i].vessel, ''));
        $(row).find('td.destinationPort').html(ifNotValid(rowData[i].destPort, ''));
        $(row).find('td.destinationCountry').html(ifNotValid(rowData[i].destCountry, ''));
        $(row).find('td.blNo').html(ifNotValid(rowData[i].blNo, ''));
        $(row).find('td.etd').html(ifNotValid(rowData[i].sEtd, ''));
        $(row).find('td.eta').html(ifNotValid(rowData[i].sEta, ''));
        $(row).find('td.remarks').html(ifNotValid(rowData[i].remarks, ''));
        $(row).find('td.documentConvertedDate').html(ifNotValid(rowData[i].documentConvertedDate, ''));

        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);

    }

    return element;
}

// update remarks to request from sales
function updateRemarks(id, data) {
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
        data: JSON.stringify(data),
        url: myContextPath + "/shipping/requestFromSales/remarks?id=" + id,
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}
function initCustomerSelect2(element) {
    // init customer search dropdown
    $(element).select2({
        allowClear: true,
        minimumInputLength: 2,
        width: '100%',
        ajax: {
            url: myContextPath + "/customer/search?flag=customer",
            dataType: 'json',
            delay: 500,
            data: function(params) {
                var query = {
                    search: params.term,
                    type: 'public'
                }
                return query;

            },
            processResults: function(data) {
                var results = [];
                data = data.data;
                $(this)
                if (data != null && data.length > 0) {
                    $.each(data, function(index, item) {
                        results.push({
                            id: item.code,
                            text: item.companyName + ' :: ' + item.firstName + ' ' + item.lastName + '(' + item.nickName + ')',
                            data: item
                        });
                    });
                }
                return {
                    results: results
                }

            }

        }
    })
}
