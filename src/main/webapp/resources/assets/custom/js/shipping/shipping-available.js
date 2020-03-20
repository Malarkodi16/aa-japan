var tableAvailableForShippingEle, tableAvailableForShipping, countryJson, japanJson;
$(function() {

    $(document).on('focus', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').addClass('highlight');
    });
    $(document).on('blur', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').removeClass('highlight');
    })
    $(document).on('focus', '.select2-selection--single', function(e) {
        select2_open = $(this).parent().parent().siblings('select');
        select2_open.select2('open');
    });

    //scroll
    $('#modal-arrange-shipping-available').find('#item-container').slimScroll({
        start: 'bottom',
        height: ''
    });
    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        countriesJson = data;
        var elements = $('.country-dropdown');
        elements.select2({
            allowClear: true,
            width: '100%',
            data: $.map(countriesJson, function(item) {
                return {
                    id: item.country,
                    text: item.country
                };
            })
        }).val('').trigger('change');

        $('#modal-arrange-shippment-item').find('.country-dropdown').select2('destroy');
    })

    //teble available for shipping
    tableAvailableForShippingEle = $('#table-available-for-shipping')
    tableAvailableForShipping = tableAvailableForShippingEle.DataTable({
        "dom": '<ip<t>ip>',
        "pageLength": 25,
        "ajax": {
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            url: myContextPath + "/stock/availableForShipping/datasource",
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
            "width": "10px",
            className: 'select-checkbox',
            "data": "id",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" type="checkbox" value="' + data + '">'
                }
                return data;
            }
        }, {
            targets: 1,
            "width": "100px",
            "className": "details-control",
            "data": "stockNo",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {

                    return '<input type="hidden" name="stockNo" value="' + data + '"/><a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockNo="' + data + '">' + data + '</a>'
                }
                return data;
            }
        }, {
            targets: 2,
            "width": "150px",
            "className": "details-control",
            "data": "chassisNo"
        }, {
            targets: 3,
            "width": "100px",
            "className": "details-control",
            "data": "length"

        }, {
            targets: 4,
            "width": "100px",
            "className": "details-control",
            "data": "width"
        }, {
            targets: 5,
            "width": "100px",
            "className": "details-control",
            "data": "height"
        }, {
            targets: 6,
            "width": "150px",
            "data": "destinationCountry",
            "className": 'align-center'

        }, {
            targets: 7,
            "width": "100px",
            "className": "details-control",
            "data": "destinationPort"
        }, {
            targets: 8,
            "width": "100px",
            "className": "details-control",
            "data": "currentLocation",
        }, {
            targets: 9,
            "width": "100px",
            "className": "details-control",
            "data": "shipmentOriginCountry",
            "render": function(data, type, row) {
                var originPort = '';
                if (isEmpty(data) && isEmpty(row.shipmentOriginPort)) {
                    originPort = '';
                } else if (isEmpty(data) && !isEmpty(row.shipmentOriginPort)) {
                    originPort = row.shipmentOriginPort
                } else if (!isEmpty(data) && isEmpty(row.shipmentOriginPort)) {
                    originPort = data
                } else {
                    originPort = data + " / " + row.shipmentOriginPort
                }
                return originPort
            }
        }, {
            targets: 10,
            "width": "100px",
            "className": "details-control",
            "data": "destinationCountryInspectionStatus",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    var status = 'Yet to Arrange';
                    var className = "label-default";
                    if (data == 0) {
                        status = 'Yet to Arrange';
                        className = "label-default";
                    } else if (data == 1) {
                        status = 'Inspection Done';
                        className = "label-success";
                    }
                    return '<span class="label ' + className + '" style="min-width:100px">' + status + '</span>'
                }
                return data;
            }
        }, {
            targets: 11,
            "width": "100px",
            "className": "details-control",
            "data": "inspectionStatus",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    var status = 'Available';
                    var className = "label-default";
                    if (data == 0) {
                        status = 'Available';
                        className = "label-default";
                    } else if (data == 1) {
                        status = 'In Inspection';
                        className = "label-success";
                    }
                    return '<span class="label ' + className + '" style="min-width:100px">' + status + '</span>'
                }
                return data;
            }
        }, {
            targets: 12,
            "width": "100px",
            "className": "details-control",
            "data": "transportationStatus",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    var status = '';
                    var className = "label-default";
                    if (data == 0) {
                        status = 'Idle';
                        className = "label-default";
                    } else if (data == 1) {
                        status = 'In Transit';
                        className = "label-info";
                    } else if (data == "2") {
                        status = "Completed"
                        className = "success"

                    }
                    return '<span class="label ' + className + '" style="width:100px">' + status + '</span>'
                }
                return data;
            }
        }, {
            targets: 13,
            "width": "100px",
            "visible": false,
            "className": "details-control",
            "data": "status"

        }, {
            targets: 14,

            "visible": false,

            "data": "forwarder"

        }],
        "fnRowCallback": function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            if (aData.status == "2") {
                $('td', nRow).css('background-color', '#ef503d');
            }
            if (aData.status == "1") {
                $('td', nRow).css('background-color', '#ffc34d');
            }

        }
    });

    tableAvailableForShipping.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            tableAvailableForShipping.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            tableAvailableForShipping.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            tableAvailableForShipping.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            tableAvailableForShipping.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function() {
        if (tableAvailableForShipping.rows({
            selected: true,
            page: 'current'
        }).count() !== tableAvailableForShipping.rows({
            page: 'current'
        }).count()) {
            $(tableAvailableForShippingEle).find("th.select-checkbox>input").removeClass("selected");
            $(tableAvailableForShippingEle).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(tableAvailableForShippingEle).find("th.select-checkbox>input").addClass("selected");
            $(tableAvailableForShippingEle).find("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (tableAvailableForShipping.rows({
            selected: true,
            page: 'current'
        }).count() !== tableAvailableForShipping.rows({
            page: 'current'
        }).count()) {
            $(tableAvailableForShippingEle).find("th.select-checkbox>input").removeClass("selected");
            $(tableAvailableForShippingEle).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(tableAvailableForShippingEle).find("th.select-checkbox>input").addClass("selected");
            $(tableAvailableForShippingEle).find("th.select-checkbox>input").prop('checked', true);

        }

    });
    // Customize Datatable
    $('#table-available-for-shipping-filter-search').keyup(function() {
        tableAvailableForShipping.search($(this).val()).draw();
    });
    $('#table-available-for-shipping-filter-length').change(function() {
        tableAvailableForShipping.page.len($(this).val()).draw();
    });


    
    var availableStockPortFilterELe = $('#port-filter-for-shipping');
    availableStockPortFilterELe.select2({
        allowClear: true,
        width: '100%',
    }).on('change', function() {
        tableAvailableForShipping.draw();
    })
    $('#country-filter-available-for-shipping').on('change', function() {
        var val = ifNotValid($(this).val(), '');
        availableStockPortFilterELe.empty();
        if (!isEmpty(val)) {
            var data = filterOneFromListByKeyAndValue(countriesJson, "country", val);
            if (data != null) {
                availableStockPortFilterELe.select2({
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
        tableAvailableForShipping.draw();
    });
   
    $(document).on('ifChecked', '#lastLapVehiclesCheck', function() {
        tableAvailableForShipping.draw();
    }).on('ifUnchecked', '#lastLapVehiclesCheck', function() {
        tableAvailableForShipping.draw();
    })

    //arrange shipping for available stocks
    var modalAvailableElement = $('#modal-arrange-shipping-available');
    modalAvailableElement.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        if (tableAvailableForShipping.rows({
            selected: true,
            page: 'current'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return event.preventDefault();

        }
        //destinationCountry check
        var selectedRowData = tableAvailableForShipping.rows({
            selected: true,
            page: 'current'
        }).data()
        var tmpDestCountry;
        var tmpDestPort;
        var isValid = true;
        var destCountry;
        for (var i = 0; i < selectedRowData.length; i++) {
            if (i == 0) {
                tmpDestCountry = selectedRowData[i].destinationCountry;
                tmpDestPort = selectedRowData[i].destinationPort;
            } else if (tmpDestCountry != selectedRowData[i].destinationCountry || tmpDestPort != selectedRowData[i].destinationPort) {
                isValid = false;
                break;
            }
        }
        if (!isValid) {
            alert($.i18n.prop('page.shipping.arrangement.validation.destination.notsame'));
            return event.preventDefault();
        }

        //set default values
        $('select[name="orginCountry"]').val('Japan').trigger('change');
        $('select[name="destCountrySelect"]').val(tmpDestCountry).trigger('change');
        $('input[name="btnIsCustomer"][value="0"]').iCheck('check');

        var triggerElement = $(event.relatedTarget);
        let type = triggerElement.attr('data-type');

        $(this).find('#flag').val(type);

        var modalArrangeShippingItemContainer = $('#modal-arrange-shipping-available').find('#item-container');

        tableAvailableForShipping.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = tableAvailableForShipping.row(this).data();
            let stockNo = ifNotValid(data.stockNo, '');
            let chassisNo = ifNotValid(data.chassisNo, '');
            let destCountry = ifNotValid(data.destinationCountry, '');
            let destPort = ifNotValid(data.destinationPort, '');

            var itemElement = $('#modal-arrange-shippment-item-available>.clone-item').clone();
            $(itemElement).appendTo(modalArrangeShippingItemContainer);
            $(itemElement).find('input[name="shippingInstructionData"]').val(JSON.stringify(data));
            $(itemElement).find('input[name="stockNo"]').val(stockNo);
            $(itemElement).find('input[name="chassisNo"]').val(chassisNo);
            $(itemElement).find('select[name="destCountrySelect"]').val(destCountry).trigger('change').prop('disabled', type == 'available' ? false : true);
            $(itemElement).find('select[name="destPortSelect"]').val(destPort).trigger('change').prop('disabled', type == 'available' ? false : true);
            $(itemElement).find('input[name="destCountry"]').val(destCountry);
            $(itemElement).find('input[name="destPort"]').val(destPort);
            if (destCountry.toUpperCase() == 'KENYA') {
                $('#yardFields').removeClass('hidden');
            } else {
                $('#yardFields').addClass('hidden')
            }
        })

        //init select2
        $(this).find('.select2-select').select2({
            allowClear: true,
            width: '100%',
        });
        //init customer search dropdown
        $(this).find('select[name="customerId"]').select2({
            allowClear: true,
            minimumInputLength: 2,
            width: '100%',
            ajax: {
                url: myContextPath + "/customer/search?flag=customer",
                cache: false,
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
    }).on('hidden.bs.modal', function() {
        $(this).find('#item-container>.clone-item').remove();
        resetElementInput($(this).find('input[type="text"],select,textarea'));
        $(this).find('select').val('').trigger('change');

    }).on('ifChecked', 'input[name="btnIsCustomer"]', function(e) {
        var val = $(this).val();
        if (val == 0) {
            modalAvailableElement.find('.row.branch-details').addClass('hidden')
            var customerElement = modalAvailableElement.find('#item-container').find('.customer-data-block');
            customerElement.removeClass('hidden');
            modalAvailableElement.find('.row.branch-details').find('select').val('').trigger('change');
        } else {
            modalAvailableElement.find('.row.branch-details').removeClass('hidden')
            var customerElement = modalAvailableElement.find('#item-container').find('.customer-data-block');
            customerElement.addClass('hidden');
            customerElement.find('select').val('').trigger('change');
        }

    }).on('change', 'select[name="destCountrySelect"]', function() {
        var element = $(this).closest('.container-fluid');
        var country = ifNotValid($(this).val(), '');
        if (country.toUpperCase() == 'KENYA') {
            $('#yardFields').removeClass('hidden');
        } else {
            $('#yardFields').addClass('hidden');
        }
        $(element).find('input[name="destCountry"]').val(country);
        var vessalElement = $(element).find('select[name="vessel"]');
        element.find('input[name="voyageNo"]').val('');
        element.find('input[name="scheduleId"]').val('');
        element.find('.schedule-container').addClass('hidden');
        vessalElement.empty();
        var portElement = $(element).find('select[name="destPortSelect"]');
        var yardElement = $(element).find('select[name="yardSelect"]');
        portElement.empty();
        yardElement.empty();
        if (country.length == 0) {
            return;
        }
        var data = filterOneFromListByKeyAndValue(countriesJson, "country", country);
        if (data != null) {
            portElement.select2({
                allowClear: true,
                width: '100%',
                data: $.map(data.port, function(item) {
                    return {
                        id: item,
                        text: item
                    };
                })
            }).val('').trigger('change');
            yardElement.select2({
                allowClear: true,
                width: '100%',
                data: $.map(data.yardDetails, function(item) {
                    return {
                        id: item.id,
                        text: item.yardName
                    };
                })
            }).val('').trigger('change');
        }

    }).on('change', 'select[name="destPortSelect"]', function() {
        var destPort = ifNotValid($(this).val(), '');
        var element = $(this).closest('.container-fluid');
        $(element).find('input[name="destPort"]').val(destPort);
        var vessalElement = $(element).find('select[name="vessel"]');
        var forwarderElement = $(element).find('select[name="forwarder"]');

        element.find('input[name="voyageNo"]').val('');
        element.find('input[name="scheduleId"]').val('');
        element.find('.schedule-container').addClass('hidden');
        vessalElement.empty();
        forwarderElement.empty();
        var data = {};
        data["orginCountry"] = ifNotValid($(element).find('select[name="orginCountry"]').val(), '');
        data["orginPort"] = ifNotValid($(element).find('select[name="orginPort"]').val(), '');
        data["destCountry"] = ifNotValid($(element).find('input[name="destCountry"]').val(), '');
        data["destPort"] = destPort;
        if (!isEmpty(data.orginCountry) && !isEmpty(data.orginPort) && !isEmpty(data.destCountry) && !isEmpty(data.destPort)) {
            var response = findAllVessalsAndFwdrByOrginAndDestination(data);
            response = ifNotValid(response.data, {});
            var vessalArr = response.vessals;
            var forwardersArr = response.forwarders;
            //init vessal dropdown
            vessalElement.select2({
                allowClear: true,
                width: '100%',
                data: $.map(vessalArr, function(item) {
                    return {
                        id: item.shipId,
                        text: item.shipName + ' [' + item.shippingCompanyName + ']',
                        data: item
                    };
                })
            }).val('').trigger('change');
            //init forwarder dropdown
            forwarderElement.select2({
                allowClear: true,
                width: '100%',
                data: $.map(forwardersArr, function(item) {
                    return {
                        id: item.forwarderId,
                        text: item.forwarderName,
                        data: item
                    };
                })
            }).val('').trigger('change');
            //             $(vessalElement).attr('data-json', JSON.stringify(vessalArr));
        }

    }).on('change', 'select[name="orginCountry"]', function() {

        var element = $(this).closest('.container-fluid');
        var country = ifNotValid($(this).val(), '');
        var vessalElement = $(element).find('select[name="vessel"]');
        element.find('input[name="voyageNo"]').val('');
        element.find('input[name="scheduleId"]').val('');
        element.find('.schedule-container').addClass('hidden');
        vessalElement.empty();
        var portElement = $(element).find('select[name="orginPort"]');
        portElement.empty();
        if (country.length == 0) {
            return;
        }
        var data = filterOneFromListByKeyAndValue(countriesJson, "country", country);
        if (data != null) {
            portElement.select2({
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

    }).on('change', 'select[name="orginPort"]', function() {
        var element = $(this).closest('.container-fluid');
        var vessalElement = $(element).find('select[name="vessel"]');
        element.find('input[name="voyageNo"]').val('');
        element.find('input[name="scheduleId"]').val('');
        element.find('.schedule-container').addClass('hidden');
        vessalElement.empty();
        var forwarderElement = $(element).find('select[name="forwarder"]');
        forwarderElement.empty();
        var data = {};
        data["orginCountry"] = ifNotValid($(element).find('select[name="orginCountry"]').val(), '');
        data["orginPort"] = ifNotValid($(element).find('select[name="orginPort"]').val(), '');
        data["destCountry"] = ifNotValid($(element).find('input[name="destCountry"]').val(), '');
        data["destPort"] = ifNotValid($(element).find('input[name="destPort"]').val(), '');
        if (!isEmpty(data.orginCountry) && !isEmpty(data.orginPort) && !isEmpty(data.destCountry) && !isEmpty(data.destPort)) {
            var response = findAllVessalsAndFwdrByOrginAndDestination(data);
            response = ifNotValid(response.data, {});
            var vessalArr = response.vessals;
            var forwardersArr = response.forwarders;
            //init vessal dropdown
            vessalElement.select2({
                allowClear: true,
                width: '100%',
                data: $.map(vessalArr, function(item) {
                    return {
                        id: item.shipId,
                        text: item.shipName + ' [' + item.shippingCompanyName + ']',
                        data: item
                    };
                })
            }).val('').trigger('change');
            //init forwarder dropdown
            forwarderElement.select2({
                allowClear: true,
                width: '100%',
                data: $.map(forwardersArr, function(item) {
                    return {
                        id: item.forwarderId,
                        text: item.forwarderName,
                        data: item
                    };
                })
            }).val('').trigger('change');
        }

    }).on('change', 'select[name="vessel"]', function() {

        var element = $(this).closest('.container-fluid');
        var value = ifNotValid($(this).val(), '');
        element.find('input[name="voyageNo"]').val('');
        element.find('input[name="scheduleId"]').val('');
        $('#transport-schedule-details').find('.schedule-container').addClass('hidden');
        if (!isEmpty(value)) {
            var data = $(this).select2('data')[0].data;
            $('#transport-schedule-details').find('.schedule-container>.etd>.date').html(data.sEtd);
            $('#transport-schedule-details').find('.schedule-container>.eta>.date').html(data.sEta);
            $('#transport-schedule-details').find('.schedule-container').removeClass('hidden');
            element.find('input[name="voyageNo"]').val(data.voyageNo);
            element.find('input[name="scheduleId"]').val(data.scheduleId);
        }

    }).on('change', 'select[name="forwarder"]', function() {
        var element = $('#modal-arrange-shipping-available');
        var frwdrChargeContainer = $(element).find('table.frwdr-charge-table>tbody>tr')
        $(frwdrChargeContainer).find('td.frwdr-charge>span').autoNumeric('init').autoNumeric('set', 0);
        var value = ifNotValid($(this).val(), '');
        if (!isEmpty(value)) {
            var data = $(this).select2('data')[0].data;
            $(frwdrChargeContainer).find('td.freight>span').autoNumeric('init').autoNumeric('set', data.freightCharge);
            $(frwdrChargeContainer).find('td.shipping>span').autoNumeric('init').autoNumeric('set', data.shippingCharge);
            $(frwdrChargeContainer).find('td.inspection>span').autoNumeric('init').autoNumeric('set', data.inspectionCharge);
            $(frwdrChargeContainer).find('td.radiation>span').autoNumeric('init').autoNumeric('set', data.radiationCharge);
        }

    }).on('click', "#btn-create-shipping-request", function() {
        if (!$('#shipping-arrangement-form').find('.valid-required-fields').valid()) {
            return;
        }

        var shippingRequestItems = [];
        //         var flag = $('#modal-arrange-shipping-available').find('#flag').val();
        var shippingData = getFormData($('#modal-arrange-shipping-available').find('#transport-schedule-details').find('input,select'))
        var forwarder = ifNotValid(shippingData.forwarder, '');
        var orginCountry = ifNotValid(shippingData.orginCountry, '');
        var orginPort = ifNotValid(shippingData.orginPort, '');
        var yard = ifNotValid(shippingData.yard, '');
        var destCountry = ifNotValid(shippingData.destCountry, '');
        var destPort = ifNotValid(shippingData.destPort, '');
        var bCustomerId = ifNotValid(shippingData.bCustomerId, '');
        var bConsigneeId = ifNotValid(shippingData.bConsigneeId, '');
        var bNotifypartyId = ifNotValid(shippingData.bNotifypartyId, '');
        var vesselId = ifNotValid(shippingData.vessel, '');
        var voyageNo = ifNotValid(shippingData.voyageNo, '');
        var scheduleId = ifNotValid(shippingData.scheduleId, '');
        var shippingType = ifNotValid(shippingData.shippingType, '');
        var toFlag = $('input[name="btnIsCustomer"]:checked').val();
        $('#modal-arrange-shipping-available').find('#item-container>.item').each(function() {
            let data = {};

            var frwdrChargeContainer = $(this).find('table.frwdr-charge-table>tbody>tr')
            var freightCharge = ifNotValid($(frwdrChargeContainer).find('td.freight>span').autoNumeric('init').autoNumeric('get'), 0);
            var shippingCharge = ifNotValid($(frwdrChargeContainer).find('td.shipping>span').autoNumeric('init').autoNumeric('get'), 0);
            var inspectionCharge = ifNotValid($(frwdrChargeContainer).find('td.inspection>span').autoNumeric('init').autoNumeric('get'), 0);
            var radiationCharge = ifNotValid($(frwdrChargeContainer).find('td.radiation>span').autoNumeric('init').autoNumeric('get'), 0);

            let rowData = $(this).find('input[name="shippingInstructionData"]').val();
            rowData = JSON.parse(rowData);

            let customerData = getFormData($(this).find('.customer-data'))
            data["shippingInstructionId"] = ifNotValid(rowData.id, '');
            data["stockNo"] = ifNotValid(rowData.stockNo, '');
            data["customerId"] = ifNotValid(customerData.customerId, '');
            data["consigneeId"] = ifNotValid(customerData.consigneeId, '');
            data["notifypartyId"] = ifNotValid(customerData.notifypartyId, '');
            data["freightCharge"] = freightCharge;
            data["shippingCharge"] = shippingCharge;
            data["inspectionCharge"] = inspectionCharge;
            data["radiationCharge"] = radiationCharge;
            data["vesselId"] = vesselId;
            data["voyageNo"] = voyageNo;
            data["scheduleId"] = scheduleId;
            data["shippingType"] = shippingType;
            data["forwarderId"] = forwarder;
            data["orginCountry"] = orginCountry;
            data["orginPort"] = orginPort;
            data["destCountry"] = destCountry;
            data["yard"] = yard;
            data["destPort"] = destPort;
            data["bCustomerId"] = bCustomerId;
            data["bConsigneeId"] = bConsigneeId;
            data["bNotifypartyId"] = bNotifypartyId;
            data["toFlag"] = toFlag;
            shippingRequestItems.push(data);
        })
        if (shippingRequestItems.length > 0) {
            var result = saveShippingRequest(shippingRequestItems, 'available');
            if (result.status = 'success') {
                tableAvailableForShipping.ajax.reload();
                $('#modal-arrange-shipping-available').modal('toggle');
                var alertEle = $('#alert-block');
                $(alertEle).css('display', '').html('<strong>Success!</strong> Shipping request created.');
                $(alertEle).fadeTo(5000, 500).slideUp(500, function() {
                    $(alertEle).slideUp(500);
                });
              //set status
                setShippingDashboardStatus();
            }
        }
    }).on('click', '.btn-remove-item', function() {
        if ($('#modal-arrange-shipping-available').find('.item').length > 1) {
            $(this).closest('.item').remove();
        }

    }).on('change', 'select[name="customerId"]', function() {
        var element = $(this).closest('.item');
        var data = $(this).select2('data');
        $(element).find('select[name="consigneeId"],select[name="notifypartyId"]').empty();
        if (!isEmpty(data[0].data)) {
            var consigneeNotifyParty = data[0].data.consigneeNotifyparties;
            $(element).find('select[name="consigneeId"]').select2({
                allowClear: true,
                width: '100%',
                data: $.map(consigneeNotifyParty, function(item) {
                    return {
                        id: item.id,
                        text: item.cFirstName + ' ' + item.cLastName
                    };
                })
            }).val('').trigger('change');
            $(element).find('select[name="notifypartyId"]').select2({
                allowClear: true,
                width: '100%',
                data: $.map(consigneeNotifyParty, function(item) {
                    return {
                        id: item.id,
                        text: item.npFirstName + ' ' + item.npLastName
                    };
                })
            }).val('').trigger('change');
        }

    });

    //init customer search dropdown
    $('#modal-arrange-shipping-available').find('select[name="bCustomerId"]').select2({
        allowClear: true,
        minimumInputLength: 2,
        width: '100%',
        ajax: {
            url: myContextPath + "/customer/search?flag=branch",
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
    }).on('change', function() {
        var data = $(this).find(':selected').data('data');
        data = data.data;
        var elements = $('#modal-arrange-shipping-available').find('select[name="bConsigneeId"],select[name="bNotifypartyId"]');
        elements.empty();
        if (!isEmpty(data)) {
            $(elements).select2({
                allowClear: true,
                width: '100%',
                data: $.map(data.consigneeNotifyparties, function(item) {
                    return {
                        id: item.id,
                        text: item.npFirstName + ' ' + item.npLastName
                    };
                })
            }).val('').trigger('change');
        }

    })

    //transport arrangement
    $('#modal-arrange-transport').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }

        if (tableAvailableForShipping.rows({
            selected: true,
            page: 'current'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return e.preventDefault();
        }
        $(this).find('#schedule-date').addClass('hidden');
        $(this).find('#selectedDate').addClass('hidden')
        var rowdata = tableAvailableForShipping.rows({
            selected: true,
            page: 'current'
        }).data();
        //check valid
        for (var i = 0; i < rowdata.length; i++) {
            if (rowdata[i].transportationStatus == 1) {
                if (rowdata.length == 1) {
                    alert($.i18n.prop('alert.stock.transport.validation.already.arranged.single'));
                } else {
                    alert($.i18n.prop('alert.stock.transport.validation.already.arranged.multiple'));
                }

                return e.preventDefault();
            }
        }
        var element;
        var i = 0;
        tableAvailableForShipping.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = tableAvailableForShipping.row(this).data();

            var stockNo = ifNotValid(data.stockNo, '');
            var chassisNo = ifNotValid(data.chassisNo, '');
            var remarks = ifNotValid(data.remarks, '');
            var model = ifNotValid(data.model, '');
            var maker = ifNotValid(data.maker, '');
            var lotNo = ifNotValid(data.lotNo, '');
            var posNo = ifNotValid(data.posNo, '');
            var numberPlate = ifNotValid(data.numberPlate, '');
            var destinationCountry = ifNotValid(data.destinationCountry, '');
            var destinationPort = ifNotValid(data.destinationPort, '');
            var category = ifNotValid(data.category, '');
            var subCategory = ifNotValid(data.subcategory, '');
            var charge = ifNotValid(data.charge, '');
            var pickupLocation = '';
            var pickupLocationCustom = '';
            var dropLocation = '';
            var dropLocationCustom = '';

            if (!isEmpty(data.transportationStatus) && data.transportationStatus == 0 && isEmpty(data.lastTransportLocation)) {
                pickupLocation = ifNotValid(data.pickupLocation, '');
                pickupLocationCustom = ifNotValid(data.pickupLocationCustom, '');
                dropLocation = ifNotValid(data.dropLocation, '');
                dropLocationCustom = ifNotValid(data.dropLocationCustom, '');
            } else {
                pickupLocation = ifNotValid(data.lastTransportLocation, '');
                pickupLocationCustom = ifNotValid(data.lastTransportLocationCustom, '');
                dropLocation = '';
                dropLocationCustom = '';
            }

            var transporter = ifNotValid(data.transporter, '');

            if (i != 0) {
                element = $('#item-vechicle-clone').find('.item-vehicle').clone();

                $(element).appendTo('#item-vehicle-clone-container');
                $('.charge').autoNumeric('init');
            } else {
                element = $('#item-vehicle-container').find('.item-vehicle');
            }
            $(element).find('.select2-select').select2({
                allowClear: true,
                width: '100%'
            });
            $(element).find('.datepicker').datepicker({
                format: "dd-mm-yyyy",
                autoclose: true
            }).on('change', function() {
                $(this).valid();
            })
            $(element).find('input[name="stockNo"]').val(stockNo);
            $(element).find('input[name="chassisNo"]').val(chassisNo);
            $(element).find('textarea[name="remarks"]').val(remarks);
            $(element).find('input[name="model"]').val(model);
            $(element).find('input[name="maker"]').val(maker);
            $(element).find('input[name="lotNo"]').val(lotNo);
            $(element).find('input[name="posNo"]').val(posNo);
            $(element).find('input[name="numberPlate"]').val(numberPlate);
            $(element).find('select[name="destinationCountry"]').select2({
                allowClear: true,
                width: '100%',
                data: $.map(countryJson, function(item) {
                    return {
                        id: item.country,
                        text: item.country,
                        data: item
                    };
                })
            }).val(destinationCountry).trigger('change');
            $(element).find('select[name="destinationPort"]').val(destinationPort).trigger('change');
            $(element).find('input[name="data"]').val(transporter);
            $(element).find('input[name="category"]').val(category + '-' + subCategory);
            $(element).find('input[name="subcategory"]').val(subCategory);

            var closest_container = $(element).find('select[name="pickupLocation"]').closest('.form-group');
            if (pickupLocation.toLowerCase() === 'others') {
                closest_container = $(element).find('select[name="pickupLocation"]').closest('.form-group');
                $(closest_container).find('div.others-input-container').removeClass('hidden').find('textarea').val(pickupLocationCustom)
                $(closest_container).find('.select2').addClass('hidden');
            } else {
                $(element).find('select[name="pickupLocation"]').val(pickupLocation).trigger("change");
                $(closest_container).find('div.others-input-container').addClass('hidden').find('textarea').val('');
                $(closest_container).find('.select2').removeClass('hidden');
            }
            closest_container = $(element).find('select[name="dropLocation"]').closest('.form-group');
            if (dropLocation.toLowerCase() === 'others') {
                $(closest_container).find('div.others-input-container').removeClass('hidden').find('textarea').val(dropLocationCustom)
                $(closest_container).find('.select2').addClass('hidden');
            } else {
                $(element).find('select[name="dropLocation"]').val(dropLocation).trigger("change");
                $(closest_container).find('div.others-input-container').addClass('hidden').find('textarea').val('');
                $(closest_container).find('.select2').removeClass('hidden');
            }
            $(element).find('input[name="charge"]').autoNumeric('init').autoNumeric('set', ifNotValid(charge, 0));
            i++;
        });
    }).on('hidden.bs.modal', function() {
        $(this).find('#item-vehicle-container').find('#item-vehicle-clone-container').html('');
        $(this).find('#transport-schedule-details').find('input[name="selectedtype"][value="0"]').iCheck('check');
        $(this).find('#transport-schedule-details').find('input[type="text"]').val('');
        $(this).find('#transport-schedule-details').find('select').val([]);
        $(this).find('#transport-remark').find('textarea').val('');
        $(this).find('#item-vehicle-container').find("input,textarea,select").val([]);
        $(this).find('#item-vehicle-container').find('select.select2').val('').trigger('change');
    }).on('change', '.select2-select.with-others', function() {
        var selectedVal = $(this).find('option:selected').val();
        var closest_container = $(this).closest('.form-group');
        if (ifNotValid(selectedVal, '').toUpperCase() === 'others'.toUpperCase()) {
            $(closest_container).find('div.others-input-container').removeClass('hidden');
            $(closest_container).find('span.select2').addClass('hidden');
        }
    }).on('ifChecked', 'input[name="selectedtype"]', function() {
        if ($(this).val() == 0) {
            $('#schedule-date').addClass('hidden');
        } else if ($(this).val() == 1) {
            $('#schedule-date').addClass('hidden');
        } else if ($(this).val() == 2) {
            $('#schedule-date').removeClass('hidden');
        }
    }).on('click', 'a.show-dropdown', function() {
        var closest_container = $(this).closest('.form-group');
        $(closest_container).find('select.select2-select').removeClass('hidden').val('').trigger("change");
        $(closest_container).find('textarea.others-input').val('');
        $(closest_container).find('span.select2').removeClass('hidden');
        $(closest_container).find('div.others-input-container').addClass('hidden');

    }).on('change', 'select[name="pickupLocation"]', function() {
        if (isPickupAndDropIsSame($(this))) {
            $(this).val('').trigger('change');
            alert($.i18n.prop('alert.pickupanddrop.location.same'))
            return false;
        }
        var pickupLocationEle = $(this).closest('.item-vehicle').find('select[name="pickupLocation"]');
        var dropLocationEle = $(this).closest('.item-vehicle').find('select[name="dropLocation"]');
        var pickupLocation = pickupLocationEle.val();
        var dropLocation = dropLocationEle.val();
        var transporter = $(this).closest('.item-vehicle').find('select[name="transporter"]');
        var transporterData = $(this).closest('.item-vehicle').find('input[name="data"]').val();
        transporter.empty().trigger('change');
        if (isEmpty(pickupLocation) || isEmpty(dropLocation)) {
            return;
        }
        var data = getTransporter(pickupLocationEle, dropLocationEle);
        var autopopulate = data.autopopulate;
        var transportArr = data.transportArr;

        transporter.select2({
            placeholder: "Select Transporter",
            allowClear: true,
            width: '100%',
            data: $.map(transportArr, function(item) {
                return {
                    id: item.code,
                    text: item.name,
                    data: item
                };
            })
        })

        var toSelectVal = '';
        if (!isEmpty(transporterData)) {
            toSelectVal = transporterData;
        } else if (autopopulate) {
            toSelectVal = transporter.find('option:first').val();
        } else {
            toSelectVal = '';
        }
        transporter.val(toSelectVal).trigger('change');

    }).on('change', 'select[name="dropLocation"]', function() {
        if (isPickupAndDropIsSame($(this))) {
            $(this).val('').trigger('change');
            alert($.i18n.prop('alert.pickupanddrop.location.same'))
            return false;
        }
        var pickupLocationEle = $(this).closest('.item-vehicle').find('select[name="pickupLocation"]');
        var dropLocationEle = $(this).closest('.item-vehicle').find('select[name="dropLocation"]');
        var pickupLocation = pickupLocationEle.val();
        var dropLocation = dropLocationEle.val();
        var transporter = $(this).closest('.item-vehicle').find('select[name="transporter"]');
        var transporterData = $(this).closest('.item-vehicle').find('input[name="data"]').val();
        transporter.empty().trigger('change');
        if (isEmpty(pickupLocation) || isEmpty(dropLocation)) {
            return;
        }
        var data = getTransporter(pickupLocationEle, dropLocationEle);
        var autopopulate = data.autopopulate;
        var transportArr = data.transportArr;

        transporter.select2({
            placeholder: "Select Transporter",
            allowClear: true,
            width: '100%',
            data: $.map(transportArr, function(item) {
                return {
                    id: item.code,
                    text: item.name,
                    data: item
                };
            })
        })

        var toSelectVal = '';
        if (!isEmpty(transporterData)) {
            toSelectVal = transporterData;
        } else if (autopopulate) {
            toSelectVal = transporter.find('option:first').val();
        } else {
            toSelectVal = '';
        }
        transporter.val(toSelectVal).trigger('change');
    }).on('change', 'select[name="transporter"]', function() {
        var charge = $(this).closest('.item-vehicle').find('input[name="charge"]');
        var valData = $(this).find('option:selected').data('data');
        if (!isEmpty(valData)) {
            if (!isEmpty(valData.data)) {
                $(charge).autoNumeric('init').autoNumeric('set', ifNotValid(valData.data.amount, 0));
            }
        } else {
            $(charge).autoNumeric('init').autoNumeric('set', 0);

        }
    }).on('change', 'select[name="destinationCountry"]', function() {
        var data = $(this).find(':selected').data('data');
        var destinationPort = $(this).closest('.item-vehicle').find('select[name="destinationPort"]');
        destinationPort.empty();
        data = data.data;
        if (!isEmpty(data)) {
            destinationPort.select2({
                allowClear: true,
                width: '100%',
                data: $.map(data.port, function(item) {
                    return {
                        id: item,
                        text: item
                    };
                })
            }).val('').trigger("change");
        }
    });

    //Changes By Kishore
    var radioEstimate = $('#modal-arrange-shipping-instruction').find('input[type="radio"][name="estimatedType"].minimal,input[type="radio"][name="selecteddate"].minimal');
    radioEstimate.iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    })
    $('#modal-arrange-shipping-instruction').find('select[name="customerName"]').select2({
        allowClear: true,
        minimumInputLength: 2,
        ajax: {
            url: myContextPath + "/customer/search/all",

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
    });
    $('#modal-arrange-shipping-instruction').find('select[name="destCountry"]').select2({
        allowClear: true,
        width: '100%',
        data: $.map(countryJson, function(item) {
            return {
                id: item.country,
                text: item.country,
                data: item
            }
        })

    }).val('').trigger('change');
    $('#modal-arrange-shipping-instruction').find('select[name="orginPort"]').select2({
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

    $('.datePicker').datepicker({
        format: "mm/yyyy",
        autoclose: true,
        viewMode: "months",
        minViewMode: "months"

    }).on('change', function() {
        $(this).valid();
    })

    $('#modal-arrange-shipping-instruction').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        
        if (tableAvailableForShipping.rows({
            selected: true,
            page: 'current'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return e.preventDefault();
        }

        $(this).find('#estimatedDate').addClass('hidden');
        var rowdata = tableAvailableForShipping.rows({
            selected: true,
            page: 'current'
        }).data();
        //check valid
        for (var i = 0; i < rowdata.length; i++) {
            if (rowdata[i].shippingInstructionStatus == 1) {
                if (rowdata.length == 1) {
                    alert($.i18n.prop('alert.stock.reserve.shipping.instruction.already.arranged.single'));
                } else {
                    alert($.i18n.prop('alert.stock.reserve.shipping.instruction.already.arranged.multiple'));
                }

                return e.preventDefault();
            }
        }

        $(this).find('select[name="consigneeId"],select[name="notifypartyId"],select[name="destPort"],select[name="vessalName"]').select2({
            allowClear: true,
            width: '100%'
        })
        //         $(this).find('select[name="forwarder"]').val(forwarder).trigger('change');

    }).on('hidden.bs.modal', function() {
        $(this).find('#estimatedData').find('input[name="estimatedType"][value="0"]').iCheck('check').trigger('change');
        $(this).find('.item-shipping').find('input[name="estimatedDeparture"], input[name="estimatedArrival"]').val('');
        $(this).find('.item-shipping').find('select').val('').trigger('change');
        $(this).removeData();
        //         $(this).removeData();
    }).on('change', 'select[name="customerName"]', function() {
        var shippingInstructionEle = $('#modal-arrange-shipping-instruction').find('.item-shipping');
        var consignee = $(this).find(':selected').data('data');
        var data = consignee.data
        $(shippingInstructionEle).find('#cFirstshippingName').empty();
        $(shippingInstructionEle).find('#npFirstshippingName').empty();
        if (!isEmpty(data)) {
            $('#cFirstshippingName').select2({
                allowClear: true,
                width: '100%',
                data: $.map(data.consigneeNotifyparties, function(item) {
                    return {
                        id: item.id,
                        text: item.cFirstName
                    };
                })
            }).val('').trigger('change');

            $('#npFirstshippingName').select2({
                allowClear: true,
                width: '100%',
                data: $.map(data.consigneeNotifyparties, function(item) {
                    return {
                        id: item.id,
                        text: item.npFirstName
                    };
                })
            }).val('').trigger('change');
            if (data.consigneeNotifyparties.length == 1) {
                $('#cFirstshippingName').val(data.consigneeNotifyparties[0].id).trigger('change');
                $('#npFirstshippingName').val(data.consigneeNotifyparties[0].id).trigger('change');
            }
        }

    }).on('change', 'select[name="destCountry"]', function() {
        var data = $(this).find(':selected').data('data');
        if (data.id.toUpperCase() == 'KENYA') {
            $('#item-shipping-container').find('#yardFields').removeClass('hidden');
        } else {
            $('#item-shipping-container').find('#yardFields').addClass('hidden')
        }
        $('#destport').empty().trigger('change');
        data = data.data;
        if (!isEmpty(data)) {
            var yardEle;
            $('#destport').select2({
                allowClear: true,
                width: '100%',
                data: $.map(data.port, function(item) {
                    return {
                        id: item,
                        text: item
                    };
                })
            }).val('').trigger("change");
            if (data.port.length == 1) {
                $('#destport').val(data.port[0]).trigger('change')
            }
            var yard = data.yardDetails
            $('#item-shipping-container').find('#yard').select2({
                allowClear: true,
                width: '100%',
                data: $.map(yard, function(item) {
                    return {
                        id: item.yardName,
                        text: item.yardName
                    };
                })
            }).val('').trigger("change");
            if (yard.length == 1) {
                $('#item-shipping-container').find('#yard').val(yard[0].yardName).trigger('change')
            }
        }
    }).on('ifChecked', 'input[name="estimatedType"]', function() {
        if ($(this).val() == 0) {
            $('#modal-arrange-shipping-instruction').find('#estimatedDate').addClass('hidden');
        } else if ($(this).val() == 1) {
            $('#modal-arrange-shipping-instruction').find('#estimatedDate').addClass('hidden');
        } else if ($(this).val() == 2) {
            $('#modal-arrange-shipping-instruction').find('#estimatedDate').removeClass('hidden');
        }

    }).on('click', '#btn-save-shipping-instruction', function(event) {
        if (!$('#shipping-instruction-form').valid()) {
            return;
        }
        var objectArr = [];
        var data_stock = [];
        tableAvailableForShipping.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = tableAvailableForShipping.row(this).data();
            data_stock.push(data.stockNo);
        })
        var arrangeShippingInstruction = $('#modal-arrange-shipping-instruction');
        var object = getFormData(arrangeShippingInstruction.find('input,select,textarea'));
        for (var i = 0; i < data_stock.length; i++) {
            data = {};
            data.stockNo = data_stock[i];
            data.customerId = object.customerName;
            data.consigneeId = object.consigneeId
            data.notifypartyId = object.notifypartyId
            //             data.orginCountry = "JAPAN"
            //             data.orginPort = object.orginPort
            data.destCountry = object.destCountry
            data.destPort = object.destPort
            data.yard = object.yard
            data.scheduleType = object.estimatedType
            //             data.estimatedDeparture = object.estimatedDeparture
            //             data.estimatedArrival = object.estimatedArrival

            objectArr.push(data)
        }

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(objectArr),
            url: myContextPath + "/invoice/shipping/order/save",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    tableAvailableForShipping.ajax.reload();
                    $('#modal-arrange-shipping-instruction').modal('toggle');
                  //set status
                    setShippingDashboardStatus();
                }

            }
        });
    })
})
function setVessal() {//     var element = $('#modal-arrange-shipping-instruction').find('#shipping-instruction-form')
//     var vessalElement = $(element).find('select[name="vessalName"]');
//     var data = {};
//     data["destPort"] = ifNotValid($(element).find('select[name="destPort"]').val(), '');
//     vessalElement.empty();
//     if (!isEmpty(data.destPort)) {
//         var response = findAllVessalsAndFwdrByOrginAndDestination(data);
//         response = ifNotValid(response.data, {});
//         var vessalArr = response.vessals;
//         var forwardersArr = response.forwarders;
//init vessal dropdown
//         if (!isEmpty(vessalArr.length) == 1) {
//             vessalElement.select2({
//                 allowClear: true,
//                 width: '100%',
//                 data: $.map(vessalArr, function(item) {
//                     return {
//                         id: item.shipId,
//                         text: item.shipName + ' [' + item.shippingCompanyName + ']' + ' [ETD= ' + item.etd + ']' + ' [ETA: ' + item.eta + ']',
//                         data: item
//                     };
//                 })
//             }).val();
//         } else {
//             vessalElement.select2({
//                 allowClear: true,
//                 width: '100%',
//                 data: $.map(vessalArr, function(item) {
//                     return {
//                         id: item.shipId,
//                         text: item.shipName + ' [' + item.shippingCompanyName + ']' + ' [ETD= ' + item.etd + ']' + ' [ETA: ' + item.eta + ']',
//                         data: item
//                     };
//                 })
//             }).val('').trigger('change');
//         }

//     }
}
function getTransporter(pickupLocEle, dropLocEle) {
    var pickupLoc = pickupLocEle.val();
    var dropLoc = dropLocEle.val();
    var autopopulate = false;
    var result = [];
    if (pickupLoc.toUpperCase() == 'OTHERS' && dropLoc.toUpperCase() == 'OTHERS') {
        result = transportersJson;
    } else if ((pickupLoc.toUpperCase() == 'OTHERS' && dropLoc.toUpperCase() != 'OTHERS') || (dropLoc.toUpperCase() == 'OTHERS' && pickupLoc.toUpperCase() != 'OTHERS')) {
        result = transportersJson;
    } else if (pickupLoc.toUpperCase() != 'OTHERS' && dropLoc.toUpperCase() != 'OTHERS') {
        var response = getTransporterFee(pickupLocEle);
        result = response.data.list;
        autopopulate = response.data.autopopulate;
//        if (isEmpty(response.data) || response.data.length == 0) {
//            result = transportersJson;
//        } else {
//            result = response.data;
//            autopopulate = true;
//        }
    }
    var data = {};
    data["autopopulate"] = autopopulate;
    data["transportArr"] = result;
    return data;
}
function getTransporterFee(element) {
    var response;
    var closest_container = element.closest('.item-vehicle');
    var category = closest_container.find('input[name="subcategory"]').val();
    var from = closest_container.find('select[name="pickupLocation"]').val()
    var to = closest_container.find('select[name="dropLocation"]').val()
    var queryString = "?category=" + category + "&from=" + from + "&to=" + to
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: 'get',
        async: false,
        url: myContextPath + "/transport/charge" + queryString,
        success: function(data) {
            response = data;
        }
    })
    return response;
}

function isPickupAndDropIsSame(element) {
    var container = $(element).closest('.item-vehicle')
    var from = container.find('select[name="pickupLocation"]').val();
    var to = container.find('select[name="dropLocation"]').val();
    if (!isEmpty(from) && !isEmpty(to) && from == to && from.toUpperCase() != 'OTHERS' && to.toUpperCase() != 'OTHERS') {
        return true;
    }
    return false;
}
