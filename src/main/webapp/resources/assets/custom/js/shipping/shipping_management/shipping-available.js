let transportersJson,staffFilterValue;
$(function() {
    let countryJson, japanJson, forwardersJson, locationJson;
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
    //set status
    setShippingDashboardStatus();
    //scroll
    $('#modal-arrange-shipping-available').find('#item-container').slimScroll({
        start: 'bottom',
        height: ''
    });
    $('#item-vehicle-container').slimScroll({
        start: 'bottom',
        height: ''
    });
    $('input[type="radio"].minimal,input[type="checkbox"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    })
    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        countryJson = data;
        var elements = $('.country-dropdown,select[name="destCountry"]');
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
        }).val('').trigger('change');

        //        $('#modal-arrange-shippment-item').find('.country-dropdown').select2('destroy');
    })
    $.getJSON(myContextPath + "/data/salesPerson", function(data) {
        var salesPersonJson = data;
        $('#staffFilter').select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            width: '100%',
            data: $.map(salesPersonJson, function(item) {
                return {
                    id: item.userId,
                    text: item.username
                };
            })
        }).val('').trigger("change");

        var newOption = new Option("Empty","-1",false,false);
        // Append it to the select
        $('#staffFilter').append(newOption);

    })
  
    $('#staffFilter').on('change', function(event) {
        staffFilterValue = $(this).find('option:selected').val();
        tableAvailableForShipping.draw();
    })
    $.getJSON(myContextPath + "/data/forwarders.json", function(data) {
        forwardersJson = data;
        $('#transport-items').find('select[name="forwarder"]').select2({
            placeholder: "Select forwarder",
            allowClear: true,
            width: '100%',
            data: $.map(forwardersJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).val('').trigger('change');
        $('#item-vechicle-clone').find('select[name="forwarder"]').select2('destroy')

    })
    $.getJSON(myContextPath + "/japan/find-port", function(data) {
        japanJson = data;
        $('#modal-arrange-shipping-instruction').find('select[name="orginPort"]')
        $('#shipment-port-filter-for-shipping').select2({
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
    //transport arrangement dropdown options
    $.getJSON(myContextPath + "/data/locations.json", function(data) {
        locationJson = data;
        $('#transport-items').find('select[name="pickupLocation"],select[name="dropLocation"]').select2({

            allowClear: true,
            width: '100%',
            data: $.map(locationJson, function(item) {
                return {
                    id: item.code,
                    text: item.displayName
                };
            })
        });
        var newOption = new Option("Others","others",false,false);
        // Append it to the select
        $('#transport-items').find('select[name="pickupLocation"],select[name="dropLocation"]').append(newOption);
        $('#item-vechicle-clone').find('select[name="pickupLocation"],select[name="dropLocation"]').select2('destroy')
    })
    // transportersJson
    $.getJSON(myContextPath + "/data/transporters.json", function(data) {
        transportersJson = data;
        $('#modal-arrange-transport').find('.transporter').select2({
            placeholder: "Select Transporter",
            allowClear: true,
            width: '100%',
            data: $.map(transportersJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).val('').trigger('change');
        $('#item-vechicle-clone').find('select[name="transporter"]').select2('destroy');
    })

    // location json filter
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

    //teble available for shipping
    let tableAvailableForShippingEle = $('#table-available-for-shipping')
    let tableAvailableForShipping = tableAvailableForShippingEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": true,
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
            "data": "purchaseDate"

        }, {
            targets: 4,
            "width": "100px",
            "className": "details-control",
            "data": "length"

        }, {
            targets: 5,
            "width": "100px",
            "className": "details-control",
            "data": "width"
        }, {
            targets: 6,
            "width": "100px",
            "className": "details-control",
            "data": "height"
        }, {
            targets: 7,
            "width": "150px",
            "data": "destinationCountry",
            "className": 'align-center'

        }, {
            targets: 8,
            "width": "100px",
            "className": "details-control",
            "data": "destinationPort"
        }, {
            targets: 9,
            "width": "100px",
            "className": "details-control",
            "data": "currentLocation",
        }, {
            targets: 10,
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
            targets: 11,
            "width": "100px",
            "className": "details-control",
            "data": "shipmentType",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    var status = '';
                    var className = "label-default";
                    if (data == 1) {
                        status = 'RORO';
                        className = "label-info";
                    } else if (data == "2") {
                        status = "CONTAINER"
                        className = "label-warning"

                    }
                    return '<span class="label ' + className + '" style="width:100px">' + status + '</span>'
                }
                return data;
            }
        }, {
            targets: 12,
            "width": "100px",
            "className": "details-control",
            "data": "forwarder",
        }, {
            targets: 13,
            "width": "80px",
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
            targets: 14,
            "width": "80px",
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
                        status = 'Inspection Instruction Given';
                        className = "label-success";
                    }
                    return '<span class="label ' + className + '" style="min-width:100px">' + status + '</span>'
                }
                return data;
            }
        }, {
            targets: 15,
            "width": "80px",
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
                        className = "label-success"

                    }
                    return '<span class="label ' + className + '" style="width:100px">' + status + '</span>'
                }
                return data;
            }
        }, {
            targets: 16,
            "data": "documentConvertedDate"

        }, {
            targets: 17,
            // orderable: false,
            "searchable": true,
            "data": "instructedBy"
        }, {
            targets: 18,
            "width": "80px",
            "visible": false,
            "className": "details-control",
            "data": "status"

        }, {
            targets: 19,
            "visible": false,
            "data": "forwarder"

        }, {
            targets: 20,
            "visible": false,
            "data": "shipmentOriginPort"

        }, {
            targets: 21,
            "visible": false,
            "data": "lastTransportLocation"

        }, {
            targets: 22,
            "visible": false,
            "data": "customerFN"
        }, {
            targets: 23,
            "data": "instructedBy",
            "visible": false

        }, {
            targets: 24,
            "data": "salesPersonId",
            "visible": false

        }],
        "fnRowCallback": function(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            if (aData.status == "2") {
                $('td', nRow).css('background-color', '#ef503d');
            }
            if (aData.status == "1") {
                $('td', nRow).css('background-color', '#ffc34d');
            }

        },

        /*excel export*/
        buttons: [{
            extend: 'excel',
            text: 'Export All',
            title: '',
            filename: function() {
                var d = new Date();
                return 'AvailableForShipping_' + d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();
            },
            attr: {
                type: "button",
                id: 'dt_excel_export_all'
            },
            exportOptions: {
                columns: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12],
            },
            customize: function(xlsx) {
                var sheet = xlsx.xl.worksheets['sheet1.xml'];
                var downrows = 5;
                var clRow = $('row', sheet);
                //update Row
                clRow.each(function() {
                    var attr = $(this).attr('r');
                    var ind = parseInt(attr);
                    ind = ind + downrows;
                    $(this).attr("r", ind);
                });

                // Update  row > c
                $('row c ', sheet).each(function() {
                    var attr = $(this).attr('r');
                    var pre = attr.substring(0, 1);
                    var ind = parseInt(attr.substring(1, attr.length));
                    ind = ind + downrows;
                    $(this).attr("r", pre + ind);
                });

                function Addrow(index, data) {
                    msg = '<row r="' + index + '">'
                    for (i = 0; i < data.length; i++) {
                        var key = data[i].k;
                        var value = data[i].v;
                        msg += '<c t="inlineStr" r="' + key + index + '" s="42">';
                        msg += '<is>';
                        msg += '<t>' + value + '</t>';
                        msg += '</is>';
                        msg += '</c>';
                    }
                    msg += '</row>';
                    return msg;
                }

                var d = new Date();

                //insert
                var r2 = Addrow(2, [{
                    k: 'A',
                    v: 'Company'
                }, {
                    k: 'B',
                    v: 'AA Japan'
                }]);
                0
                var r3 = Addrow(3, [{
                    k: 'A',
                    v: 'Title'
                }, {
                    k: 'B',
                    v: 'Available For Shipping Report'
                }]);
                var r4 = Addrow(4, [{
                    k: 'A',
                    v: 'Date'
                }, {
                    k: 'B',
                    v: d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear()
                }]);

                sheet.childNodes[0].childNodes[1].innerHTML = r2 + r3 + r4 + sheet.childNodes[0].childNodes[1].innerHTML;
            }
        }],

    });

    $("#excel_export_all").on("click", function() {
        tableAvailableForShipping.button("#dt_excel_export_all").trigger();

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

    $('#current-location-filter-for-shipping').select2({
        allowClear: true,
        width: '100%',
    }).on('change', function() {
        tableAvailableForShipping.draw();
    })
    $('#shipment-port-filter-for-shipping').select2({
        allowClear: true,
        width: '100%',
    }).on('change', function() {
        tableAvailableForShipping.draw();
    })

    $('#country-filter-available-for-shipping').on('change', function() {
        var val = ifNotValid($(this).val(), '');
        availableStockPortFilterELe.empty();
        if (!isEmpty(val)) {
            var data = filterOneFromListByKeyAndValue(countryJson, "country", val);
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
            var posNo = ifNotValid(data.auctionInfoPosNo, '');
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
            var posNos = ifNotValid(data.posNos, '');
            var forwarder = ifNotValid(data.forwarder, '');
            var transportCategory = ifNotValid(data.transportCategory, '');
            //var inspectionFlag = ifNotValid(data.inspectionFlag, '');

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
            $(element).find('input[name="transportCategory"]').val(transportCategory);
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
            })
            if (data.auctionHouse == null) {
                $(element).find('.hidePosNo').addClass('hidden');
            }
            $(element).find('select[name="posNo"]').empty()
            $(element).find('select[name="posNo"]').select2({
                allowClear: true,
                width: '100%',
                data: $.map(posNos, function(item) {
                    return {
                        id: item,
                        text: item
                    };
                })
            }).val(posNo).trigger('change');
            $(element).find('select[name="destinationCountry"]').val(destinationCountry).trigger('change');
            $(element).find('select[name="destinationPort"]').val(destinationPort).trigger('change');
            $(element).find('input[name="data"]').val(transporter);
            $(element).find('input[name="category"]').val(category + '-' + subCategory);
            $(element).find('input[name="subcategory"]').val(subCategory);
            $(element).find('select[name="forwarder"]').val(forwarder).trigger('change');
            //            if (inspectionFlag == 1) {
            //                $(element).find('input[name="inspectionFlag"]').attr('checked', true);
            //            } else {
            //                $(element).find('input[name="inspectionFlag"]').attr('checked', false);
            //            }

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
        $(this).find('.hidePosNo').removeClass('hidden');
        $(this).find('#transport-schedule-details').find('input[name="selectedtype"][value="0"]').iCheck('check');
        $(this).find('#transport-schedule-details').find('input[type="text"]').val('');
        $(this).find('#transport-schedule-details').find('select').val([]);
        $(this).find('#transport-remark').find('textarea').val('');
        $(this).find('#item-vehicle-container').find("input,textarea,select").val([]);
        $(this).find('#item-vehicle-container').find('select.select2').val('').trigger('change');
        //$(this).find('input[type="checkbox"]').iCheck('uncheck');
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
                    text: !isEmpty(item.amount) ? item.name + ' [ ¥ ' + ifNotValid(item.amount, '') + ' ]' : item.name,
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
                    text: !isEmpty(item.amount) ? item.name + ' [ ¥ ' + ifNotValid(item.amount, '') + ' ]' : item.name,
                    data: item
                };
            })
        })

        var toSelectVal = '';
        if (!isEmpty(transporterData) && !autopopulate) {
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
            //$(this).closest('.item-vehicle').find('input[name="inspectionFlag"]').attr('checked', data.inspectionFlag == 1 ? true : false);
        }

    }).on('click', '#btn-create-transport-order', function() {
        if (!$('#transport-arrangement-form').valid()) {
            return;
        }

        var objectArr = [];
        var data = {};
        autoNumericSetRawValue($('#modal-arrange-transport').find('.charge'))
        var scheduleDetails = getFormData($('#transport-schedule-details').find('.schedule-data'));
        var selectedType = getFormData($('#transport-schedule-details').find('.selected-type'));
        var transportComment = getFormData($('#transport-comment').find('.comment'));
        $("#item-vehicle-container").find('.item-vehicle').each(function() {
            var object = {};
            object = getFormData($(this).find('input,select,textarea'));
            //            let isChecked = $(this).find('input[name="inspectionFlag"]').is(":checked");
            //            if (isChecked) {
            //                object.inspectionFlag = 1
            //            } else {
            //                object.inspectionFlag = 0
            //            }
            object.pickupDate = scheduleDetails.pickupDate;
            object.pickupTime = scheduleDetails.pickupTime;
            object.deliveryDate = scheduleDetails.deliveryDate;
            object.scheduleType = selectedType.selectedtype;
            object.deliveryTime = scheduleDetails.deliveryTime;
            object.selectedDate = scheduleDetails.selecteddate;
            object.comment = transportComment.comment;
            objectArr.push(object);
        });

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(objectArr),
            url: myContextPath + "/transport/order/save",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {

                    tableAvailableForShipping.ajax.reload();
                    $('#modal-arrange-transport').modal('toggle');
                }
            }
        });
    });

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

        let country = $('#country-filter-available-for-shipping').val();
        //         let port = $('#port-filter-for-shipping').val();
        if (isEmpty(country)) {
            alert($.i18n.prop('alert.instruction.select.destCountry'));
            return e.preventDefault();
        }

        if (tableAvailableForShipping.rows({
            selected: true,
            page: 'all'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return e.preventDefault();
        }

        $(this).find('select[name="consigneeId"],select[name="notifypartyId"],select[name="destPort"],select[name="vessalName"]').select2({
            allowClear: true,
            width: '100%'
        })

        $(this).find('#estimatedDate').addClass('hidden');
        var rowdata = tableAvailableForShipping.rows({
            selected: true,
            page: 'all'
        }).data();

        var isValid = isValidSelection(rowdata);
        if (!isValid) {
            alert($.i18n.prop('page.shipping.arrangement.validation.destination.country.notsame'))
            return e.preventDefault();
        }
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

        $(this).find('select[name="destCountry"]').val(rowdata[0].destinationCountry).trigger('change');
        $(this).find('select[name="destPort"]').val(rowdata[0].destinationPort).trigger('change');
        if (rowdata[0].inspectionFlag == 1) {
            $(this).find('input[name="inspectionFlag"]').attr('checked', true);
        } else {
            $(this).find('input[name="inspectionFlag"]').attr('checked', false);
        }

    }).on('hidden.bs.modal', function() {
        $(this).find('#estimatedData').find('input[name="estimatedType"][value="0"]').iCheck('check').trigger('change');
        $(this).find('.item-shipping').find('input[name="estimatedDeparture"], input[name="estimatedArrival"]').val('');
        $(this).find('.item-shipping').find('select').val('').trigger('change');
        $(this).removeData();
        $(this).find('input[name="inspectionFlag"]').attr('checked', false);
        //         $(this).removeData();
    }).on('change', 'select[name="customerName"]', function() {
        var shippingInstructionEle = $('#modal-arrange-shipping-instruction').find('.item-shipping');
        var consignee = $(this).find(':selected').data('data');
        var data = consignee.data
        $(shippingInstructionEle).find('#cFirstshippingName').empty();
        $(shippingInstructionEle).find('#npFirstshippingName').empty();
        if (!isEmpty(data)) {
            let consArray = data.consigneeNotifyparties.filter(function(index) {
                return !isEmpty(index.cFirstName);
            })
            $('#cFirstshippingName').select2({
                allowClear: true,
                width: '100%',
                data: $.map(consArray, function(item) {
                    return {
                        id: item.id,
                        text: item.cFirstName
                    };
                })
            }).val('').trigger('change');
            let npArray = data.consigneeNotifyparties.filter(function(index) {
                return !isEmpty(index.npFirstName);
            })
            $('#npFirstshippingName').select2({
                allowClear: true,
                width: '100%',
                data: $.map(npArray, function(item) {
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
                        id: item.id,
                        text: item.yardName
                    };
                })
            }).val('').trigger("change");
            if (yard.length == 1) {
                $('#item-shipping-container').find('#yard').val(yard[0].yardName).trigger('change')
            }
            $('#item-shipping-container').find('input[name="inspectionFlag"]').attr('checked', data.inspectionFlag == 1 ? true : false);
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
            page: 'all'
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
            data.paymentType = object.paymentType
            data.inspectionFlag = object.inspectionFlag
            data.estimatedDeparture = object.estimatedDeparture
            data.estimatedArrival = object.estimatedArrival

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

    //inspection module start
    $('#arrange-inspection-modal').on('show.bs.modal', function(e) {
        if (tableAvailableForShipping.rows({
            selected: true,
            page: 'current'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return e.preventDefault();
        }
        tableAvailableForShipping.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = tableAvailableForShipping.row(this).data();
            if (data.inspectionStatus == 1) {
                alert($.i18n.prop("alert.shipping.instruction.given") + " " + data.chassisNo);
                return e.preventDefault();
            }
        })
    }).on('hidden.bs.modal', function() {
        resetElementInput(this)
    }).on('click', '#inspection-submit', function() {
        if (!$('#form-arrange-inspection').valid()) {
            return;
        }
        var data_stock = [];
        tableAvailableForShipping.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = tableAvailableForShipping.row(this).data();
            data_stock.push(data.stockNo);
        })
        var arrangeInspectionData = $('#arrange-inspection-modal');
        var object = getFormData(arrangeInspectionData.find('input,select,textarea'));
        var inspectionOrderData = [];
        for (var i = 0; i < data_stock.length; i++) {
            data = {};
            data.stockNo = data_stock[i];
            data.destCountry = object.country;
            inspectionOrderData.push(data);
        }
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(inspectionOrderData),
            url: myContextPath + "/inspection/instruction/save",
            contentType: "application/json",
            success: function(status) {
                tableAvailableForShipping.ajax.reload();
                $('#arrange-inspection-modal').modal('toggle');

            }
        })

    })

    //stock details modal update
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
        //updateStockDetailsData
        updateStockDetailsData(stockDetailsModal, stockNo)
    }).on('hidden.bs.modal', function() {
        stockDetailsModalBody.html('');
    })
})
$.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
    //     let staffFilter = $('#staffFilter').find('option:selected').val();
    let lastLapVehiclesCheck = ifNotValid($('#lastLapVehiclesCheck').is(':checked'), '');
    let destinationCountry = ifNotValid($('#country-filter-available-for-shipping').val(), '');
    let currentLocation = ifNotValid($('#current-location-filter-for-shipping').val(), '');
    let shipmentOriginPort = ifNotValid($('#shipment-port-filter-for-shipping').val(), '');

    if (typeof destinationCountry != 'undefined' && destinationCountry.length != '') {
        if (aData[7].length == 0 || aData[7] != destinationCountry) {
            return false;
        }

    }
    if (typeof currentLocation != 'undefined' && currentLocation.length != '') {
        if (aData[21].length == 0 || aData[21] != currentLocation) {
            return false;
        }
    }
    if (typeof shipmentOriginPort != 'undefined' && shipmentOriginPort.length != '') {
        if (aData[20].length == 0 || aData[20] != shipmentOriginPort) {
            return false;
        }
    }
    //             if ('NZE141-9107830' == 0) {}
    if (typeof lastLapVehiclesCheck != 'undefined' && lastLapVehiclesCheck == true) {
        if (ifNotValid(aData[18], '0') == '0') {
            return false;
        }
    }
    if (typeof staffFilterValue != 'undefined' && staffFilterValue.length != '') {
        if (staffFilterValue != -1 && (aData[24].length == 0 || aData[24] != staffFilterValue)) {
            return false;
        } else if (staffFilterValue == -1 && !isEmpty(aData[24])) {
            return false;
        }
    }

    return true;
});
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
    var transportCategory = closest_container.find('input[name="transportCategory"]').val();
    var from = closest_container.find('select[name="pickupLocation"]').val()
    var to = closest_container.find('select[name="dropLocation"]').val()
    var queryString = "?transportCategory=" + transportCategory + "&from=" + from + "&to=" + to
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
function isValidSelection(data) {
    //var tmpPData = ''
    var tmpPSupplier = '';
    for (var i = 0; i < data.length; i++) {
        var tableData = data[i];
        if (i == 0) {
            // tmpPData = tableData.purchaseDate
            tmpCountry = tableData.destinationCountry;
        }
        //tmpPData == tableData.purchaseDate -> removed purchase date equal
        if (!(tmpCountry == tableData.destinationCountry)) {
            return false;
        }
    }
    return true;
}
