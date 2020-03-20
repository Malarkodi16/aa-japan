let tableRequestFromSales;
$(function() {

    $(document).on('focus', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrappr').addClass('highlight');
    });
    $(document).on('blur', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').removeClass('highlight');
    })
    shippingInstructionId = $('#request-from-sales-filter-id').val();
    $(document).on('focus', '.select2-selection--single', function(e) {
        select2_open = $(this).parent().parent().siblings('select');
        select2_open.select2('open');
    });

    //set status
    setShippingDashboardStatus();
    // Date picker
    $(this).find('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    })

    //icheck
    $('input[type="checkbox"][name="btnIsCustomer"].minimal, input[type="radio"][name="btnIsCustomer"].minimal,input[type="checkbox"][name="lastLap"]').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    })
    var shipmentTypeFilter = $('input[type="radio"][name="shippingType"]:checked').val();
    $('input[type="radio"][name="shippingType"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function() {
        shipmentTypeFilter = $(this).val();
        if (shipmentTypeFilter == 1) {
            $('#btn-roro-shipping').removeClass('hidden');
            $('#btn-container-shipping').addClass('hidden');
        } else if (shipmentTypeFilter == 2) {
            $('#btn-roro-shipping').addClass('hidden');
            $('#btn-container-shipping').removeClass('hidden');
        } else if (shipmentTypeFilter == null || shipmentTypeFilter == 3) {
            $('#btn-roro-shipping').addClass('hidden');
            $('#btn-container-shipping').addClass('hidden');
        }
        tableRequestFromSales.draw();
    })
    $('input[name="showBlankForwarder"]').on('change', function() {
        tableRequestFromSales.ajax.reload()
    })
    $('input[type="radio"][name="containerAllcationStatus"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        table_shipping_container.ajax.reload()
    });
    $('input[type="radio"][name="roroAllcationStatus"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        table_shipping_requested.ajax.reload()
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
                    text: item.country,
                    data: item
                };
            })
        }).val('').trigger('change');

    })

    $.getJSON(myContextPath + "/data/locations.json", function(data) {
        locationJson = data;
        var elements = $('#transport-location-filter');
        elements.select2({
            allowClear: true,
            width: '100%',
            data: $.map(locationJson, function(item) {
                return {
                    id: item.code,
                    text: item.displayName
                };
            })

        })
    });
    $.getJSON(myContextPath + "/japan/find-port", function(data) {
        japanPortJson = data;
        $('#orgin-port-filter-from-sales').select2({
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
    $.getJSON(myContextPath + "/japan/find-port", function(data) {
        japanJson = data;
        $('#modal-arrange-shipping-instruction').find('select[name="orginPort"]')
        $('select[name="orginPort"]').select2({
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
    });
    $.getJSON(myContextPath + "/user/getUser-list", function(data) {
        var salesJson = data.data;
        var ele = $('#select_staff');
        $(ele).select2({
            allowClear: true,
            width: '100%',
            data: $.map(salesJson, function(item) {
                return {
                    id: item.userId,
                    text: item.username + ' ' + '( ' + item.userId + ' )'
                };
            })
        }).val('').trigger("change");
    });

    $('#transport-location-filter').select2({
        allowClear: true,
        width: '100%'
    }).on("change", function(event) {
        tranportLocationFilter = $(this).find('option:selected').val();
        tableRequestFromSales.draw();
    })

    //table request from sales
    var tableEle = $('#table-request-from-sales');
    tableRequestFromSales = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": {
            "url": myContextPath + "/shipping/instruction/from-sales/datasource",
            "data": function(data) {
                data["draw"] = $('#request-from-sales-container').hasClass('hidden') ? false : true
                return data;
            }
        },
        ordering: true,
        "order": [[1, "desc"]],
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": "",
            "targets": 1,
            "type": "date-eu"
        }, {
            targets: 0,
            orderable: false,
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
            "className": "details-control",
            "data": "createdDate"
        }, {
            targets: 2,
            "data": "scheduleType",
            "render": function(data, type, row) {
                let text = '';
                if (!isEmpty(data)) {
                    if (data == 0) {
                        text = 'Immediate';
                    } else if (data == 1) {
                        text = 'Next Available';
                    } else if (data == 2) {
                        text = row.estimatedDeparture;
                    }
                }
                return text;
            }
        }, {
            targets: 3,
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
            targets: 4,
            "className": "details-control",
            "data": "chassisNo"
        }, {
            targets: 5,
            "data": "purchaseDate"

        }, {
            targets: 6,
            "className": "details-control",
            "data": "fCustName"
        }, {
            targets: 7,
            "className": "details-control",
            "data": "fConsigneeName"
        }, {
            targets: 8,
            "className": "details-control",
            "data": "fNotifyPartyName"
        }, {
            targets: 9,

            "data": "destinationCountry",
            "className": 'align-center',
            "render": function(data, type, row) {
                return row.destinationCountry + '/' + row.destinationPort;
            }

        }, {
            targets: 10,
            "data": "firstRegDate",
            "className": 'align-center'

        }, {
            targets: 11,
            "data": "salesPersonName",
            "className": 'align-center'
        }, {
            targets: 12,
            "data": "destinationCountry",
            "className": 'align-center',
            "visible": false
        }, {
            targets: 13,
            "data": "destinationPort",
            "className": 'details-control',
            "visible": false
        }, {
            targets: 14,
            "data": "forwarderName",
            "className": 'details-control'
        }, {
            targets: 15,
            "className": 'details-control',
            "data": 'lastTransportLocation',
            "render": function(data, type, row) {

                return ifNotValid(data, '').toUpperCase() == 'OTHERS' ? ifNotValid(row.lastTransportLocationCustom, '') : ifNotValid(row.lastTransportLocationDisplayname, '');
                ;
            }
        }, {
            targets: 16,
            "data": "originPort"
        }, {
            targets: 17,
            "data": "remarks",
            "className": 'details-control'
        }, {
            targets: 18,
            "data": "documentConvertedDate",
            "className": 'details-control'
        }, {
            targets: 19,
            "className": 'details-control',
            "render": function(data, type, row) {
                let html = '';
                html += '<a href="#" class="btn btn-warning btn-xs" data-target="#modal-update-remark" data-toggle="modal" data-backdrop="static"><i class="fa fa-fw fa-floppy-o"></i> </a>'
                html += '<a href="#" class="ml-5 btn btn-danger btn-xs" data-target="#modal-cancel-stock" data-toggle="modal" data-backdrop="static"><i class="fa fa-fw fa-close"></i> </a>'
                //html += '<a href="#" class="ml-5 btn btn-danger btn-xs cancel-shipping" title="Cancel" data-backdrop="static" style=" width: 120px; " data-keyboard="false" data-toggle="modal" data-target="#modal-update-remark"><iclass="fa fa-fw fa-close"></i> </a>'
                return html
            }
        }, {
            targets: 20,
            "data": "shipmentType",
            "visible": false,
            "render": function(data, type, row) {
                data = (data == null || data == '') ? 0 : data;
                return data;
            }
        }, {
            targets: 21,
            "data": "forwarder",
            "visible": false
        }, {
            targets: 22,
            "data": "lastTransportLocation",
            "visible": false
        }, {
            targets: 23,
            "data": "scheduleType",
            "visible": false
        }, {
            targets: 24,
            "data": "salesPersonId",
            "visible": false
        }],

        /*excel export*/
        buttons: [{
            extend: 'excel',
            text: 'Export All',
            title: '',
            filename: function() {
                var d = new Date();
                return 'ShippingRequested_' + d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();
            },
            attr: {
                type: "button",
                id: 'export_all'
            },
            exportOptions: {
                columns: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14],
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
                    v: 'Shipping Requested Report'
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

    $("#export_excel").on("click", function() {
        tableRequestFromSales.button("#export_all").trigger();

    });
    let requested_forwarder_filter_value;
    $('select[name="requested-forwarder-filter"]').change(function() {
        let val = $(this).val();
        requested_forwarder_filter_value = '';
        let matchStr = '';
        for (let i = 0; i < val.length; i++) {
            matchStr += '\\b' + val[i] + '\\b|';
        }
        if (matchStr.length > 0) {
            requested_forwarder_filter_value = new RegExp(matchStr.substring(0, matchStr.length - 1))
        }

        tableRequestFromSales.draw();
    })

    var orginPortFilterELe = $('#orgin-port-filter-from-sales');
    orginPortFilterELe.select2({
        allowClear: true,
        width: '100%',
    }).on('change', function() {
        tableRequestFromSales.draw();
    })

    $('#current-location-filter-from-sales').select2({
        allowClear: true,
        width: '100%',
    }).on('change', function() {
        tableRequestFromSales.draw();
    })

    $.getJSON(myContextPath + "/data/forwarders.json", function(data) {

        $('select[name="requested-forwarder-filter"],select[name="forwarder"]').select2({
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

    //Shipping Requested
    $.getJSON(myContextPath + "/shipping/data/shipsWithCompanyName", function(data) {
        shippingJson = data.data;
        var elements = $('#shipping-company-filter');
        $('#shipping-company-filter').select2({
            allowClear: true,
            width: '100%',
            data: $.map(shippingJson, function(item) {
                return {
                    id: item.shippingCompanyNo,
                    text: item.shippingCompanyName,
                    data: item
                };
            })
        }).val('').trigger('change');
        $('#cloneable-items').find('#shipping-company-filter').select2('destroy');
    })

    var requestFromSalesPortFilterELe = $('#port-filter-from-sales');
    requestFromSalesPortFilterELe.select2({
        allowClear: true,
        width: '100%',
    }).on('change', function() {
        port = $(this).val();
        tableRequestFromSales.draw();
    })

    $('#estimated-departure-filter').select2({
        allowClear: true,
        width: '100%',
    }).on('change', function() {
        estimatedDepartureFilter = $(this).find('option:selected').val();
        tableRequestFromSales.draw();
    })

    $('#country-filter-from-sales').on('change', function() {
        var val = ifNotValid($(this).val(), '');
        requestFromSalesPortFilterELe.empty();
        if (!isEmpty(val)) {
            var data = filterOneFromListByKeyAndValue(countriesJson, "country", val);
            if (data != null) {
                requestFromSalesPortFilterELe.select2({
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
        country = val;
        tableRequestFromSales.draw();
    });

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

    //on open recycle modal
    var modalUpdateRemarkTriggerEle;
    var modalUpdateRemark = $('#modal-update-remark');
    modalUpdateRemark.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalUpdateRemarkTriggerEle = $(event.relatedTarget);
        var data = tableRequestFromSales.row($(modalUpdateRemarkTriggerEle).closest('tr')).data();
        modalUpdateRemark.find('#rowData').attr('data-json', JSON.stringify(data));
        modalUpdateRemark.find('textarea[name="remarks"]').val(data.remarks);

    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
    }).on('click', '#save-remark-modal', function() {
        var row = tableRequestFromSales.row($(modalUpdateRemarkTriggerEle).closest('tr'));
        var rowData = modalUpdateRemark.find('#rowData').attr('data-json');
        rowData = JSON.parse(rowData);
        var data = {};
        var id = rowData.id;
        data["remarks"] = modalUpdateRemark.find('textarea[name="remarks"]').val();
        var response = updateRemarks(id, data)
        //updateClaimReceivedAmount
        if (response.status === 'success') {
            if (!isEmpty(response.data)) {
                row.data(response.data).invalidate();
                tableRequestFromSales.draw();
            }
            modalUpdateRemark.modal('toggle');
        }
    });

    //on cancel stock modal
    var modalCancelStockTriggerEle;
    var modalCancelStock = $('#modal-cancel-stock');
    modalCancelStock.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalCancelStockTriggerEle = $(event.relatedTarget);
        var data = tableRequestFromSales.row($(modalCancelStockTriggerEle).closest('tr')).data();
        modalCancelStock.find('#rowData').attr('data-json', JSON.stringify(data));
        modalCancelStock.find('textarea[name="remarks"]').val(data.remarks);
    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
    }).on('click', '#save-remark-modal', function() {
        var row = tableRequestFromSales.row($(modalCancelStockTriggerEle).closest('tr'));
        var rowData = modalCancelStock.find('#rowData').attr('data-json');
        rowData = JSON.parse(rowData);
        var data = {};
        var id = rowData.id;
        data["stockNo"] = rowData.stockNo;
        data["remarks"] = modalCancelStock.find('textarea[name="remarks"]').val();
        var response = cancelStock(id, data)
        //updateClaimReceivedAmount
        if (response.status === 'success') {
            tableRequestFromSales.ajax.reload();
            modalCancelStock.modal('toggle');
        }
    })
    //<!-- ./. added by krishna -->

    tableRequestFromSales.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            tableRequestFromSales.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            tableRequestFromSales.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            tableRequestFromSales.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            tableRequestFromSales.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function() {
        if (tableRequestFromSales.rows({
            selected: true,
            page: 'current'
        }).count() !== tableRequestFromSales.rows({
            page: 'current',
            search: 'applied'
        }).count()) {
            $(tableEle).find("th.select-checkbox>input").removeClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(tableEle).find("th.select-checkbox>input").addClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (tableRequestFromSales.rows({
            selected: true,
            page: 'current'
        }).count() !== tableRequestFromSales.rows({
            page: 'current'
        }).count()) {
            $(tableEle).find("th.select-checkbox>input").removeClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(tableEle).find("th.select-checkbox>input").addClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', true);

        }

    });
    // Customize Datatable
    $('#table-request-from-sales-filter-search').keyup(function() {
        tableRequestFromSales.search($(this).val()).draw();
    });
    $('#table-request-from-sales-filter-length').change(function() {
        tableRequestFromSales.page.len($(this).val()).draw();
    });

    //     modal scroll 
    $('#modal-arrange-shipping').find('#item-container').slimScroll({
        start: 'bottom',
        height: ''
    });

    //arrange shipping for sales and rearrange
    $('#modal-arrange-shipping').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var triggerElement = $(event.relatedTarget);
        let forwarder = $('select[name="requested-forwarder-filter"]').val();
        let destCountry = $('select#country-filter-from-sales').val();
        if (isEmpty(forwarder) || isEmpty(destCountry)) {
            alert($.i18n.prop('shipping.request.validate.forwarder'));
            return event.preventDefault();
        }
        if (tableRequestFromSales.rows({
            selected: true,
            page: 'current'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return event.preventDefault();
        }
        let dataArray = tableRequestFromSales.rows({
            selected: true,
            page: 'current'
        }).data().toArray();
        var same = checkIfAllTheSame(dataArray);
        if (same) {
            $(this).find('select[name="orginPort"]').val(dataArray[0].originPort).trigger('change')
        } else {
            $(this).find('select[name="orginPort"]').val('').trigger('change')
        }

        //         $(this).find('select[name="orginCountry"]').val('JAPAN').trigger('change');

        $(this).find('select[name="destCountrySelect"]').val(dataArray[0].destinationCountry).trigger('change');
        $(this).find('select[name="destPortSelect"]').val(dataArray[0].destinationPort).trigger('change');
        $(this).find('input[name="destCountry"]').val(dataArray[0].destinationCountry);
        $(this).find('input[name="destPort"]').val(dataArray[0].destinationPort);
        $(this).find('select[name="yardSelect"]').val(dataArray[0].yard).trigger('change');
        //         $(this).find('select[name="forwarder"]').val(forwarder).trigger('change');
        $(this).find('input[name="yard"]').val(dataArray[0].yard);
        $(this).find('select[name="shippingTypeSelect"]').val(dataArray[0].shipmentType).trigger('change').addClass('readonly');
        $(this).find('input[name="shippingType"]').val(dataArray[0].shipmentType);
        $(this).find('select[name="destCountrySelect"]').addClass('readonly');
        $(this).find('select[name="destPortSelect"]').addClass('readonly');

        if (dataArray[0].destCountry == 'Kenya') {
            $(this).find('#yardFields').removeClass('hidden');
        } else {
            $(this).find('#yardFields').addClass('hidden');
        }

        var tableElement = $(this).find('table#stock-table-details>tbody');
        var rowClone = tableElement.find('tr.clone-row');

        for (var i = 0; i < dataArray.length; i++) {
            var row = $(rowClone).clone();
            $(row).find('td.s-no>span').html(i + 1)
            $(row).find('td.s-no>input').val(dataArray[i].id);
            $(row).find('td.stockNo').html(dataArray[i].stockNo);
            $(row).find('td.chassisNo').html(dataArray[i].chassisNo);
            $(row).find('td.maker').html(dataArray[i].maker);
            $(row).find('td.model').html(dataArray[i].model);
            $(row).find('td.m3').html(dataArray[i].m3);
            $(row).find('td.length').html(dataArray[i].length);
            $(row).find('td.width').html(dataArray[i].width);
            $(row).find('td.height').html(dataArray[i].height);
            $(row).removeClass('hide');
            tableElement.append(row);
        }

        $(this).find('select[name="shippingTypeSelect"]').select2({
            allowClear: true,
            width: '100%',
            "readonly": true
        });

    }).on('hidden.bs.modal', function() {
        $(this).find('#item-container>.clone-item').remove();
        $('#stock-table-details>tbody>.clone-row').not(':first').remove();
    }).on('click', 'a[name="change"]', function() {
        $('#modal-arrange-shipping').find('select[name="destCountrySelect"]').removeClass('readonly');
        $('#modal-arrange-shipping').find('select[name="destPortSelect"]').removeClass('readonly');
    }).on('change', 'select[name="destCountrySelect"]', function() {
        var element = $(this).closest('.item');
        var country = ifNotValid($(this).val(), '');
        if (country.toUpperCase() == 'KENYA') {
            $('#modal-arrange-shipping #yardFields').removeClass('hidden');
        } else {
            $('#modal-arrange-shipping #yardFields').addClass('hidden');
        }
        $(element).find('input[name="destCountry"]').val(country);
        var vessalElement = $(element).find('select[name="scheduleId"]');

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

    }).on('change', 'select[name="orginPort"]', function() {
        var originPort = ifNotValid($(this).val(), '');
        var destPort = $('select[name="destPortSelect"]').find('option:selected').val();
        var element = $(this).closest('.item');
        var vessalElement = $(element).find('select[name="scheduleId"]');
        vessalElement.empty();
        var data = {};
        data["destPort"] = destPort;
        data["originPort"] = originPort;
        if (!isEmpty(data.originPort) && !isEmpty(data.destPort)) {
            var response = findAllVessalsAndFwdrByOrginAndDestination(data);
            response = ifNotValid(response.data, {});
            var vessalArr = response.vessals;
            //             var forwardersArr = response.forwarders;
            //init vessal dropdown
            vessalElement.select2({
                allowClear: true,
                width: '100%',
                data: $.map(vessalArr, function(item) {
                    return {
                        id: item.scheduleId,
                        text: item.shipName + ' [' + item.shippingCompanyName + ']' + ' [ ' + item.voyageNo + ' ]',
                        data: item
                    };
                })
            }).val('').trigger('change');

        }
    }).on('change', 'select[name="destPortSelect"]', function() {
        var destPort = ifNotValid($(this).val(), '');
        var originPort = $('select[name="orginPort"]').find('option:selected').val();
        var element = $(this).closest('.item');
        $(element).find('input[name="destPort"]').val(destPort);
        var vessalElement = $(element).find('select[name="scheduleId"]');
        //         var forwarderElement = $(element).find('select[name="forwarder"]');

        element.find('input[name="voyageNo"]').val('');
        element.find('input[name="scheduleId"]').val('');
        element.find('.schedule-container').addClass('hidden');
        vessalElement.empty();
        //         forwarderElement.empty();
        var data = {};

        data["destCountry"] = ifNotValid($(element).find('input[name="destCountry"]').val(), '');
        data["destPort"] = destPort;
        data["originPort"] = originPort;
        if (!isEmpty(data.destCountry) && !isEmpty(data.destPort)) {
            var response = findAllVessalsAndFwdrByOrginAndDestination(data);
            response = ifNotValid(response.data, {});
            var vessalArr = response.vessals;
            //             var forwardersArr = response.forwarders;
            //init vessal dropdown
            vessalElement.select2({
                allowClear: true,
                width: '100%',
                data: $.map(vessalArr, function(item) {
                    return {
                        id: item.scheduleId,
                        text: item.shipName + ' [' + item.shippingCompanyName + ']' + ' [ ' + item.voyageNo + ' ]',
                        data: item
                    };
                })
            }).val('').trigger('change');

        }

    }).on('change', 'select[name="scheduleId"]', function() {

        var element = $(this).closest('.item');
        var value = ifNotValid($(this).val(), '');

        element.find('.schedule-container').addClass('hidden');
        if (!isEmpty(value)) {
            var data = $(this).select2('data')[0].data;

            element.find('.schedule-container>.eta>.date').html(data.sEta);
            element.find('.schedule-container').removeClass('hidden');
            element.find('input[name="voyageNo"]').val(data.voyageNo);
            element.find('input[name="scheduleId"]').val(data.scheduleId);
        }

    }).on('click', "#btn-create-shipping-request", function(event) {
        event.preventDefault();
        if (!$('#shipping-arrangement-stock-form').valid()) {
            return;
        }
        var buttonEle = $(this);
        //do something
        buttonEle.prop('disabled', true);
        var containerElement = $('#modal-arrange-shipping').find('.modal-body')
        var shippingRequestItems = {};
        let enteredData = getFormData($(containerElement).find('.data-to-save,select[name="yardSelect"]'));
        shippingRequestItems["forwarderId"] = ifNotValid(enteredData.forwarder, '');
        shippingRequestItems["orginCountry"] = ifNotValid(enteredData.orginCountry, '');
        shippingRequestItems["orginPort"] = ifNotValid(enteredData.orginPort, '');
        shippingRequestItems["destCountry"] = ifNotValid(enteredData.destCountry, '');
        shippingRequestItems["destPort"] = ifNotValid(enteredData.destPort, '');
        shippingRequestItems["yardId"] = ifNotValid(enteredData.yardSelect, '');
        shippingRequestItems["yard"] = $('select[name="yardSelect"] option:selected').text();
        var stockDetailsContainer = $(containerElement).find('table#stock-table-details>tbody>tr').not(':first')
        var stockShippingInstructionArray = [];
        stockDetailsContainer.each(function() {
            var stockDetaisObject = {};
            stockDetaisObject["stockNo"] = ifNotValid($(this).find('td.stockNo').html(), 0);
            stockDetaisObject["shippingInstructionId"] = ifNotValid($(this).find('td.s-no>input').val(), 0);
            stockShippingInstructionArray.push(stockDetaisObject);
        })
        shippingRequestItems['stockShippingInstructionArray'] = stockShippingInstructionArray;
        shippingRequestItems["scheduleId"] = ifNotValid(enteredData.scheduleId, '');
        shippingRequestItems["shippingType"] = ifNotValid(enteredData.shippingType, '');

        if (!isEmpty(shippingRequestItems)) {
            var result = saveShippingRequest(shippingRequestItems);
            if (result.status = 'success') {
                setTimeout(function() {
                    buttonEle.prop('disabled', false);
                    tableRequestFromSales.ajax.reload();
                    $('#modal-arrange-shipping').modal('toggle');
                }, 3000);

            }
        }
    }).on('click', '.btn-remove-item', function() {
        if ($('#modal-arrange-shipping').find('.item').length > 1) {
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
    var filterStaff;
    $('#select_staff').on('change', function() {
        filterStaff = $(this).find('option:selected').val();
        tableRequestFromSales.draw();
    });
    /*Country port filter in table*/
    let tranportLocationFilter = $('#transport-location-filter').find('option:selected').val();
    let estimatedDepartureFilter = $('#estimated-departure-filter').find('option:selected').val();

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        if (oSettings.sTableId == 'table-request-from-sales') {
            let orginPortValue = $('#orgin-port-filter-from-sales').find('option:selected').val();
            let showBlankForwarder = $('input[name="showBlankForwarder"]').is(":checked");
            //show blank forwarder
            if (showBlankForwarder && !isEmpty(aData[20])) {
                return false;
            }
            //id filter for notification
            if (!isEmpty(shippingInstructionId)) {
                if (aData[0].length == 0 || aData[0] != shippingInstructionId) {
                    return false;
                }
            }
            //             let forwarder = ifNotValid($('select[name="requested-forwarder-filter"]').val(), '');
            if (typeof requested_forwarder_filter_value != 'undefined' && requested_forwarder_filter_value.length != '') {
                if (aData[21].length == 0 || !requested_forwarder_filter_value.test(aData[21])) {
                    return false;
                }

            }
            //type filter 
            if (typeof shipmentTypeFilter != 'undefined' && shipmentTypeFilter.length != '') {
                if (aData[20].length == 0 || aData[20] != shipmentTypeFilter) {
                    return false;
                }
            }
            //orgin port filter
            if (typeof orginPortValue != 'undefined' && orginPortValue.length != '') {
                if (aData[16].length == 0 || aData[16] != orginPortValue) {
                    return false;
                }

            }

            //country filter 
            if (typeof country != 'undefined' && country.length != '') {
                if (aData[12].length == 0 || aData[12] != country) {
                    return false;
                }
            }

            //port filter 
            if (typeof port != 'undefined' && port != null) {
                if (aData[13].length == 0 || aData[13] != port) {
                    return false;
                }
            }

            //Staff filter
            if (typeof filterStaff != 'undefined' && filterStaff.length != '') {
                if (aData[24].length == 0 || aData[24] != filterStaff) {
                    return false;
                }
            }

            //location filter 
            if (typeof tranportLocationFilter != 'undefined' && tranportLocationFilter.length != '') {
                if (aData[22].length == 0 || aData[22] != tranportLocationFilter) {
                    return false;
                }
            }

            if (typeof estimatedDepartureFilter != 'undefined' && estimatedDepartureFilter.length != '') {
                if (aData[23].length == 0 || aData[23] != estimatedDepartureFilter) {
                    return false;
                }
            }

        }
        return true;
    });

})
function checkIfAllTheSame(arr) {
    var i = 0;
    var allTheSame = true;
    var firstElement = arr[0];
    for (i = 0; i < arr.length; i++) {
        if (arr[i].originPort !== firstElement.originPort) {
            allTheSame = false;
            i = arr.length;
        }
    }
    return allTheSame;
}
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
function saveShippingRequestAcceptDetails(data, id, scheduleId) {
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
        url: myContextPath + '/shipping/request/accept?id=' + id + '&scheduleId=' + scheduleId,
        contentType: "application/json",
        success: function(data) {
            result = data;
        }
    });
    return result;
}
function cancelShippingRequest(data) {
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
        url: myContextPath + '/shipping/request/cancel',
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
    if (rowData.status != 0) {
        $(element).find('table th.action,td.action').addClass('hidden');
    } else {
        $(element).find('table th.action,td.action').removeClass('hidden');
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
        $(row).find('td.s-no').html(i + 1);
        data = rowData.items[i].stockNo == null ? '' : rowData.items[i].stockNo;
        $(row).find('td.stockNo').html('<input type="hidden" name="stockNo" value="' + data + '"/><a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockNo="' + data + '">' + data + '</a>');
        $(row).find('td.chassisNo').html(ifNotValid(rowData.items[i].chassisNo, ''));
        $(row).find('td.shipmentType').html(ifNotValid(rowData.items[i].shippingType, '') == 1 ? 'RORO' : ifNotValid(rowData.items[i].shippingType, '') == 2 ? 'CONTAINER' : '');
        $(row).find('td.forwarder').html(ifNotValid(rowData.items[i].forwarder, ''));
        $(row).find('td.etd').html(ifNotValid(rowData.items[i].sEtd, ''));
        $(row).find('td.eta').html(ifNotValid(rowData.items[i].sEta, ''));

        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);

    }

    return element;
}

//update remarks to request from sales
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
;//cancel stock from request from sales
function cancelStock(id, data) {
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
        url: myContextPath + "/shipping/requestFromSales/cancelStock?id=" + id,
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}
function initCustomerSelect2(element) {
    //init customer search dropdown
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
