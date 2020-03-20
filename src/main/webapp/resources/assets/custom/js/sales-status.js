var table, rowselectedData, countriesJson;
$(function() {
    $.getJSON(myContextPath + "/data/sales-dashboard/status-count", function(data) {
        $('#inquiry-count').html(data.data.inquiry);
        $('#porforma-count').html(data.data.porforma);
        $('#reserved-count').html(data.data.reserved);
        $('#shipping-count').html(data.data.shipping);
        $('#sales-count').html(data.data.salesorder);
        $('#status-count').html(data.data.status);
    });
    //set status
    setShippingDashboardStatus();
    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        var countriesJson = data;
        $('#countryFilter').select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            width: '100%',
            data: $.map(countriesJson, function(item) {
                return {
                    id: item.country,
                    text: item.country
                };
            })
        }).val('').trigger("change");
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
    })

    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    }
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });
    $('input[type="checkbox"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    })
    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        countriesJson = data;
    })
    //     $('input[name=radioReceivedFilter]').iCheck({
    //         checkboxClass: 'icheckbox_minimal-blue',
    //         radioClass: 'iradio_minimal-blue'
    //     }).on('ifChecked', function(e) {
    //         if ($(this).val() == 0) {
    //             table_carTax.column(5).visible(false);
    //             table_carTax.column(6).visible(false);
    //             $('button.claim-button').show();
    //         } else if ($(this).val() == 2) {
    //             table_carTax.column(5).visible(true);
    //             table_carTax.column(6).visible(true);
    //             $('button.claim-button').hide();
    //         }
    //         table_carTax.draw();
    //     })
    // Datatable
    let booleanValue = $('input[name=screenNameFlag]').val() == "shipping" ? false : true;
    table = $('#table-shipping-instruction').DataTable({
        "pageLength": 25,
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "ajax": {
            url: myContextPath + "/sales/shipping-status-data",
            data: function(data) {
                if ($('input[name=screenNameFlag]').val() == "shipping") {
                    data.flag = 1;
                } else {
                    if (!$('input[name="showMine"]').is(':checked')) {
                        data.flag = 0;
                    } else {
                        data.flag = 1;
                    }
                }
                return data;
            }
        },
        "ordering": true,
        "order": [[20, "desc"]],
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": "",
            "targets": 20,
            "type": "date-eu"
        }, {
            targets: 0,
            orderable: false,
            "searchable": false,
            className: 'select-checkbox',
            "name": "id",
            "data": "id",
            "visible": false,
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">';
                }
                return data;
            }
        }, {
            targets: 1,
            // orderable: false,
            "searchable": true,
            "visible": false,
            "className": "details-control",
            "name": "date",
            "data": "date",

        }, {
            targets: 2,
            // orderable: false,
            "searchable": true,
            "className": "details-control",
            "name": "stockNo",
            "data": "stockNo",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockno="' + row.stockNo + '">' + data + '</a>';
                }
                return data;
            }

        }, {
            targets: 3,
            // orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "chassisNo",
            "data": "chassisNo"
        }, {
            targets: 4,
            "className": "details-control",
            //orderable: false,
            "searchable": true,
            "data": "customerFN"
        }, {
            targets: 5,
            // orderable: false,
            "searchable": true,
            "visible": false,
            "data": "consigneeFN"
        }, {
            targets: 6,
            // orderable: false,
            "searchable": true,
            "visible": false,
            "data": "notifyPartyFN"
        }, {
            targets: 7,
            // orderable: false,
            "searchable": true,
            "data": "bookingDetails"
        }, {
            targets: 8,
            // orderable: false,
            //             "searchable": false,
            "data": "destCountry"
        }, {
            targets: 9,
            //  orderable: false,
            "searchable": true,
            "visible": false,
            "data": "destPort"
        }, {
            targets: 10,
            // orderable: false,
            "searchable": true,
            "data": "lcNo"
        }, {
            targets: 11,
            // orderable: false,
            "searchable": true,
            "data": "reserveDate"
        }, {
            targets: 12,
            //  orderable: false,
            "searchable": true,
            "data": "vesselId"
        }, {
            targets: 13,
            // orderable: false,
            "searchable": true,
            "data": "etd"
        }, {
            targets: 14,
            //  orderable: false,
            "searchable": true,
            "data": "eta"
        }, {
            targets: 15,
            //  orderable: false,
            "searchable": true,
            "data": "status",
            "render": function(data, type, row) {
                var status;
                var className;
                if (data == 0) {
                    status = "Request Initiated"
                    className = "default"
                } else if (data == "1") {
                    status = "Shipping Aranged"
                    className = "success"
                }
                return '<span class="label label-' + className + '">' + status + '</span>';
            }
        }, {
            targets: 16,
            //  orderable: false,
            "searchable": true,
            "data": "remarks"
        }, {
            targets: 17,
            //  orderable: false,
            "searchable": true,
            "data": "dhlNo"
        }, {
            targets: 18,
            //  orderable: false,
            "searchable": true,
            "data": "createdDate"
        }, {
            targets: 19,
            "data": "purchasePrice",
            "visible": !booleanValue,
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                //    $.fn.dataTable.render.number( ',', '.', 2, '¥' )
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 20,
            orderable: false,
            "searchable": true,
            "visible": booleanValue,
            "data": "chassisNo",
            "render": function(data, type, row) {
                var hideClass = row.status == 0 ? '' : 'hidden';
                var addClass = row.status == 1 ? '' : 'hidden';
                var html = ''
                html += '<a href="button" class="ml-5 btn btn-info btn-xs" title="Update User Details" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-shipping-user-details"><i class="fa fa-user"></i></a>'
                html += '<a href="#" class="ml-5 btn btn-primary btn-xs ' + addClass + '" data-backdrop="static" title="Edit customer Details" data-keyboard="false" data-toggle="modal" data-target="#modal-change-customer"><i class="fa fa-edit"></i></a>'
                html += '<a type="button" class="btn btn-primary ml-5 btn-xs ' + hideClass + '" title="Edit Shipping Instruction" data-toggle="modal" data-target="#modal-edit-shipping-instruction"><i class="fa fa-fw fa-edit"></i></a>'
                //html += '<a href="#" class="ml-5 btn btn-danger btn-xs ' + hideClass + '"  title="Delete" name="cancel"><i class="fa fa-fw fa-remove"></i></a>'
                html += '<a href="#" class="ml-5 btn btn-danger btn-xs ' + hideClass + '" data-target="#modal-cancel-stock" data-toggle="modal" data-backdrop="static"><i class="fa fa-fw fa-close"></i> </a>'
                return html;
            }
        }, {
            targets: 21,
            "data": "instructedBy",
            "visible": false

        }, {
            targets: 22,
            "data": "customerId",
            "visible": false

        }, {
            targets: 23,
            "data": "salesPersonId",
            "visible": false

        }],
        "fnDrawCallback": function(oSettings) {
            $('#table-shipping-instruction').find('input.autonumber,span.autonumber').autoNumeric('init')
        }

    });
    table.on('click', 'a[name="cancel"]', function() {
        if (!confirm($.i18n.prop('common.confirm.delete'))) {
            return false;
        }
        var data = table.row($(this).closest('tr')).data();
        let shippingInstructionId = data.shippingInstructionId;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            url: myContextPath + "/sales/delete/shippingInstruction/" + shippingInstructionId,
            success: function(data) {
                if (data == 'success') {
                    table.ajax.reload();
                    var alertEle = $('#alert-block');
                    $(alertEle).css('display', '').html('<strong>Success!</strong> Shipping Instruction Deleted.');
                    $(alertEle).fadeTo(5000, 500).slideUp(500, function() {
                        $(alertEle).slideUp(500);
                    });
                }
            }
        });

    })

    //on cancel stock modal
    var modalCancelStockTriggerEle;
    var modalCancelStock = $('#modal-cancel-stock');
    modalCancelStock.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalCancelStockTriggerEle = $(event.relatedTarget);
        var data = table.row($(modalCancelStockTriggerEle).closest('tr')).data();
        modalCancelStock.find('#rowData').attr('data-json', JSON.stringify(data));
        modalCancelStock.find('textarea[name="remarks"]').val(data.remarks);
    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
    }).on('click', '#save-remark-modal', function() {
        var row = table.row($(modalCancelStockTriggerEle).closest('tr'));
        var rowData = modalCancelStock.find('#rowData').attr('data-json');
        rowData = JSON.parse(rowData);
        var data = {};
        var shippingInstructionId = rowData.shippingInstructionId;
        data["remarks"] = modalCancelStock.find('textarea[name="remarks"]').val();
        var response = cancelStock(shippingInstructionId, data)
        //updateClaimReceivedAmount
        if (response === 'success') {
            table.ajax.reload();
            modalCancelStock.modal('toggle');
        }
    })

    //     var userName = $('#userInfo').text();
    //     $('#showMine').change(function() {
    //         if ($(this).is(':checked')) {
    //             $.fn.dataTable.ext.search.push(function(settings, aData, dataIndex) {
    //                 return aData[19].toLowerCase() === userName.toLowerCase()
    //             })
    //         } else {
    //             $.fn.dataTable.ext.search.pop()
    //         }
    //         table.draw()
    //     })
    $('#showMine').change(function() {
        table.ajax.reload()
    })

    //filter By Customer Search
    $('#custselectId').select2({
        allowClear: true,
        placeholder: 'Search customer email',
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
                            text: item.companyName + ' :: ' + item.firstName + ' ' + item.lastName + '(' + item.nickName + ')'
                        });
                    });
                }
                return {
                    results: results
                }

            }

        }
    }).on('change', function(event) {
        filterCustomer = $(this).find('option:selected').val();
        table.draw();
    })

    $('#countryFilter').select2({
        allowClear: true,
        placeholder: 'Country Filter Search'
    }).on('change', function(event) {
        countryFilter = $(this).find('option:selected').val();
        table.draw();
    })

    $('#staffFilter').select2({
        allowClear: true,
        placeholder: 'Staff Filter Search'
    }).on('change', function(event) {
        staffFilter = $(this).find('option:selected').val();
        table.draw();
    })

    // Date range picker
    var purchased_min;
    var purchased_max;
    $('#table-filter-reserve-date').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        purchased_min = picker.startDate;
        purchased_max = picker.endDate;
        picker.element.val(purchased_min.format('DD-MM-YYYY') + ' - ' + purchased_max.format('DD-MM-YYYY'));
        purchased_min = purchased_min._d.getTime();
        purchased_max = purchased_max._d.getTime();
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
        $('#table-filter-reserve-date').val('');
        $(this).remove();

    });
    // Date range picker
    var intruction_date_min;
    var intruction_date_max;
    $('#table-filter-instruction-date').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        intruction_date_min = picker.startDate;
        intruction_date_max = picker.endDate;
        picker.element.val(intruction_date_min.format('DD-MM-YYYY') + ' - ' + intruction_date_max.format('DD-MM-YYYY'));
        intruction_date_min = intruction_date_min._d.getTime();
        intruction_date_max = intruction_date_max._d.getTime();
        $('<div>', {
            'class': 'input-group-addon clear-instruction-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.draw();
    });
    $('#date-instruction-form-group').on('click', '.clear-instruction-date', function() {
        intruction_date_min = '';
        intruction_date_max = '';
        table.draw();
        $('#table-filter-instruction-date').val('');
        $(this).remove();

    });
    var filterCustomer = $('#custselectId').find('option:selected').val();
    let countryFilter = $('#countryFilter').find('option:selected').val();
    let staffFilter = $('#staffFilter').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //date filter
        if (typeof purchased_min != 'undefined' && purchased_min.length != '') {
            if (aData[11].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[11], 'DD-MM-YYYY')._d.getTime();
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
        if (typeof filterCustomer != 'undefined' && filterCustomer.length != '') {
            if (aData[22].length == 0 || aData[22] != filterCustomer) {
                return false;
            }
        }
        if (typeof countryFilter != 'undefined' && countryFilter.length != '') {
            if (aData[8].length == 0 || aData[8] != countryFilter) {
                return false;
            }
        }
        if (typeof staffFilter != 'undefined' && staffFilter.length != '') {
            if (aData[23].length == 0 || aData[23] != staffFilter) {
                return false;
            }
        }
        //instruction date filter
        if (typeof intruction_date_min != 'undefined' && intruction_date_min.length != '') {
            if (aData[18].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[18], 'DD-MM-YYYY')._d.getTime();
            }
            if (intruction_date_min && !isNaN(intruction_date_min)) {
                if (aData._date < intruction_date_min) {
                    return false;
                }
            }
            if (intruction_date_max && !isNaN(intruction_date_max)) {
                if (aData._date > intruction_date_max) {
                    return false;
                }
            }

        }
        return true;
    });

    $('#btn-apply-filter').click(function() {
        table.draw();
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
    var modalTriggerEle;
    $('#modal-edit-shipping-instruction').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var element;

        modalTriggerEle = $(e.relatedTarget);
        var tr = $(modalTriggerEle).closest('tr');
        var rowData = table.row(tr).data();

        //         rowselectedData = table.rows(e.relatedTarget.closest('tr')).data();
        if (rowData.status == 1) {
            alert('Please Select request initiated stock');
            return e.preventDefault();
        }
        var stockNo = ifNotValid(rowData.stockNo, '');
        var chassisNo = ifNotValid(rowData.chassisNo, '');
        var customerId = ifNotValid(rowData.customerId, '');
        var customerName = ifNotValid(rowData.customerFN, '');
        var consigneeId = ifNotValid(rowData.consigneeId, '');
        var notifypartyId = ifNotValid(rowData.notifypartyId, '');
        var customerLastName = ifNotValid(rowData.customerLN, '');
        var inspectionFlag = ifNotValid(rowData.inspectionFlag, 0);
        var shippingInstructionId = ifNotValid(rowData.shippingInstructionId, '');
        var paymentType = ifNotValid(rowData.paymentType, '');
        var destCountry = ifNotValid(rowData.destCountry, '');
        var destPort = ifNotValid(rowData.destPort, '');
        var yard = ifNotValid(rowData.yard, '');
        var scheduleType = ifNotValid(rowData.scheduleType, 0);
        var estimatedArrival = ifNotValid(rowData.estimatedArrival, '');
        var estimatedDeparture = ifNotValid(rowData.estimatedDeparture, '');
        element = $('#item-shipping-container').find('.item-shipping');
        $(element).find('input[type="radio"].estimated-data.minimal').iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        })
        if (scheduleType == 0) {
            $(element).find('input[name="estimatedType"][value="' + scheduleType + '"]').iCheck('check').trigger('change')
            $(element).find('.estimatedDate').addClass('hidden');
        } else if (scheduleType == 1) {
            $(element).find('input[name="estimatedType"][value="' + scheduleType + '"]').iCheck('check').trigger('change')
            $(element).find('.estimatedDate').addClass('hidden');
        } else if (scheduleType == 2) {
            $(element).find('input[name="estimatedType"][value="' + scheduleType + '"]').iCheck('check').trigger('change')
            $(element).find('.estimatedDate').removeClass('hidden');
        }

        $(element).find('.datePicker').datepicker({
            format: "mm/yyyy",
            autoclose: true,
            viewMode: "months",
            minViewMode: "months"
        })
        $(element).find('input[name="estimatedDeparture"]').datepicker('setDate', estimatedDeparture);
        $(element).find('input[name="estimatedArrival"]').datepicker('setDate', estimatedArrival);
        $(element).find('input[name="stockNo"]').val(stockNo);
        $(element).find('input[name="chassisNo"]').val(chassisNo);
        $(element).find('input[name="customerId"]').val(customerId);
        $(element).find('input[name="shippingInstructionId"]').val(shippingInstructionId);
        $(element).find('input[name="customernameId"]').val(customerName + ' ' + customerLastName);
        $(element).find('select[name="paymentType"]').val(paymentType).trigger('change');
        if (inspectionFlag == 1) {
            $(element).find('input[name="inspectionFlag"]').attr('checked', true);
        } else {
            $(element).find('input[name="inspectionFlag"]').attr('checked', false);
        }
        var id = customerId;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "GET",
            async: false,
            url: myContextPath + "/customer/data/" + id,
            success: function(data) {
                if (data.status == 'success') {
                    var selectedVal = '';
                    if (data.data.consigneeNotifyparties.length == 1) {
                        selectedVal = data.data.consigneeNotifyparties[0].id;
                    }
                    let consArray = data.data.consigneeNotifyparties.filter(function(index) {
                        return !isEmpty(index.cFirstName);
                    })
                    $(element).find('select[name="consigneeId"]').empty()
                    $(element).find('#cFirstshippingName').select2({
                        allowClear: true,
                        width: '100%',
                        data: $.map(consArray, function(consigneeNotifyparties) {
                            return {
                                id: consigneeNotifyparties.id,
                                text: consigneeNotifyparties.cFirstName + ' ' + consigneeNotifyparties.cLastName
                            };
                        })
                    }).val(consigneeId).trigger("change");
                    let npArray = data.data.consigneeNotifyparties.filter(function(index) {
                        return !isEmpty(index.npFirstName);
                    })
                    $(element).find('select[name="notifypartyId"]').empty()
                    $(element).find('#npFirstshippingName').select2({
                        allowClear: true,
                        width: '100%',
                        data: $.map(npArray, function(consigneeNotifyparties) {
                            return {
                                id: consigneeNotifyparties.id,
                                text: consigneeNotifyparties.npFirstName + ' ' + consigneeNotifyparties.npLastName
                            };
                        })
                    }).val(notifypartyId).trigger("change");
                    $(element).find('select[name="destCountry"]').select2({
                        allowClear: true,
                        width: '100%',
                        data: $.map(countriesJson, function(item) {
                            return {
                                id: item.country,
                                text: item.country,
                                data: item
                            };
                        })
                    }).val(destCountry).trigger('change');
                    $(element).find('select[name="destPort"]').val(destPort).trigger('change');
                    $(element).find('select[name="yard"]').val(yard).trigger('change');
                    $(element).find('.datePicker').datepicker({
                        format: "dd-mm-yyyy",
                        autoclose: true
                    })
                }
            },
            error: function(e) {
                console.log("ERROR: ", e);
            }
        });

        //Create Shipping Instruction
        $('#item-shipping-container').find('.select2-select').select2({
            allowClear: true,
            width: '100%',
        })

    }).on('change', 'select[name="destCountry"]', function() {
        var closestEle = $(this).closest('.item-shipping');
        var country = $(this).find('option:selected').val();
        let data = $(this).find('option:selected').data();
        let countryData = data.data;
        var portList;
        var yardList;
        $.each(countriesJson, function(i, item) {
            if (item.country == country) {
                portList = item.port;
                yardList = item.yardDetails;
                return false;
            }
        });
        if (country.toUpperCase() == 'KENYA') {
            $(closestEle).find('#yardFields').removeClass('hidden');
        } else {
            $(closestEle).find('#yardFields').addClass('hidden')
        }
        $(closestEle).find('select[name="destPort"]').empty();
        $(closestEle).find('select[name="yard"]').empty();
        if (typeof portList == 'undefined') {

            return;
        }
        $(closestEle).find('select[name="destPort"]').select2({
            allowClear: true,
            data: $.map(portList, function(item) {
                return {
                    id: item,
                    text: item
                };
            })
        }).val('').trigger("change");
        $(closestEle).find('select[name="yard"]').select2({
            allowClear: true,
            data: $.map(yardList, function(item) {
                return {
                    id: item.id,
                    text: item.yardName
                };
            })
        }).val('').trigger("change");
        //         if (!isEmpty(countryData.data)) {
        //             $(closestEle).find('input[name="inspectionFlag"]').attr('checked', countryData.data.inspectionFlag == 1 ? true : false);
        //         }

    }).on('hidden.bs.modal', function() {
        $(this).removeData();
        //         $(this).find('form')[0].reset();
    }).on('ifChecked', '.estimated-data', function() {
        if ($(this).val() == 0) {
            $('#modal-edit-shipping-instruction').find('.estimatedDate').addClass('hidden');
            $('#modal-edit-shipping-instruction').find('.estimatedDate').find('input').val('');
        } else if ($(this).val() == 1) {
            $('#modal-edit-shipping-instruction').find('.estimatedDate').addClass('hidden');
            $('#modal-edit-shipping-instruction').find('.estimatedDate').find('input').val('');
        } else if ($(this).val() == 2) {
            $('#modal-edit-shipping-instruction').find('.estimatedDate').removeClass('hidden');

        }
    }).on('click', '#btn-edit-shipping', function(event) {
        if (!($('#edit-shipping-instruction-form').valid())) {
            return false;
        }

        var data = {};
        var object = getFormData($("#item-shipping-container").find('.item-shipping').find('input,select,textarea'));
        var estimatedType = $("input[name ='estimatedType']:checked").val()

        data.stockNo = object.stockNo;
        data.notifypartyId = object.notifypartyId;
        data.scheduleType = estimatedType;
        data.destPort = object.destPort;
        data.destCountry = object.destCountry;
        data.customernameId = object.customernameId;
        data.customerId = object.customerId;
        data.consigneeId = object.consigneeId;
        data.chassisNo = object.chassisNo;
        data.paymentType = object.paymentType
        data.inspectionFlag = object.inspectionFlag
        data.shippingInstructionId = object.shippingInstructionId
        data.yard = object.yard
        data.estimatedArrival = object.estimatedArrival
        data.estimatedDeparture = object.estimatedDeparture

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data),
            url: myContextPath + "/invoice/shipping/order/edit",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    table.ajax.reload();
                    $('#modal-edit-shipping-instruction').modal('toggle');
                }

            }
        });
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




    var modalCreateProforma = $('#modal-change-customer');
    modalCreateProforma.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }

        var rowdata = table.row(e.relatedTarget.closest('tr')).data();
        $(this).find('input[name="id"]').val(rowdata.id);
        var element;
       

        var customerId = rowdata.customerId;
        var custIdElem = modalCreateProforma.find('select[name="customerId"]');
        if (!isEmpty(customerId)) {
            $.ajax(myContextPath + "/customer/data/" + customerId, {
                type: "get",
                contentType: "application/json",
                async: false
            }).done(function(data) {
                data = data.data
                custIdElem.select2({
                    "data": [{
                        id: data.code,
                        text: data.code + ' :: ' + data.firstName + ' ' + data.lastName + '(' + data.nickName + ')',
                        data: data
                    }]
                }).val(data.code).trigger('change');

            });
        }
        customerInitSelect2(custIdElem);

        $(this).find('select[name="editConsigneeId"]').val(rowdata.consigneeId).trigger("change");
        $(this).find('select[name="editNotifypartyId"]').val(rowdata.notifypartyId).trigger("change");
        $(this).find('input.autonumber').autoNumeric('init');

    }).on('hidden.bs.modal', function() {
       
        $(this).find("input,textarea,select").val([]);
    }).on("change", 'select[name="customerId"]', function(event) {
        var id = $(this).val();
        if (id == null || id.length == 0) {
            modalCreateProforma.find('#editcFirstshippingName').empty();
            modalCreateProforma.find('#editnpFirstshippingName').empty();
            return;
        }
        var data = $(this).select2('data')[0].data;
        var currencyEle = modalCreateProforma.find('#currencyType')
        if (!isEmpty(data)) {
            currencyEle.val(data.currencyType).trigger('change').addClass('readonly');
        } else {
            currencyEle.val('').trigger('change');
        }
        
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "GET",
            async: false,
            url: myContextPath + "/customer/data/" + id,
            success: function(data) {
                if (data.status == 'success') {
                    var selectedVal = '';
                    if (data.data.consigneeNotifyparties.length == 1) {
                        selectedVal = data.data.consigneeNotifyparties[0].id;
                    }
                    let consArray = data.data.consigneeNotifyparties.filter(function(index) {
                        return !isEmpty(index.cFirstName);
                    })
                    $('#editcFirstshippingName').empty();
                    $('#editcFirstshippingName').select2({
                        allowClear: true,
                        width: '150px',
                        data: $.map(consArray, function(consigneeNotifyparties) {
                            return {
                                id: consigneeNotifyparties.id,
                                text: consigneeNotifyparties.cFirstName + ' ' + consigneeNotifyparties.cLastName
                            };
                        })
                    }).val(selectedVal).trigger("change");

                    $('#editnpFirstshippingName').empty();
                    let npArray = data.data.consigneeNotifyparties.filter(function(index) {
                        return !isEmpty(index.npFirstName);
                    })
                    $('#editnpFirstshippingName').select2({
                        allowClear: true,
                        width: '150px',
                        data: $.map(npArray, function(consigneeNotifyparties) {
                            return {
                                id: consigneeNotifyparties.id,
                                text: consigneeNotifyparties.npFirstName + ' ' + consigneeNotifyparties.npLastName
                            };
                        })
                    }).val(selectedVal).trigger("change");

                }
            },
            error: function(e) {
                console.log("ERROR: ", e);

            }
        });

    }).on('click', 'button#change-customer-save', function() {
        let isValid = $('#customer-edit-form').find('select[name="customerId"], select[name="editConsigneeId"], select[name="editNotifypartyId"]').valid()
        if (!isValid) {
            return false;
        }
        let data = {};
        data['id'] = $('#modal-change-customer').find('input[name="id"]').val();
        data['customerId'] = $('#modal-change-customer').find('select[name="customerId"]').val();
        data['consigneeId'] = $('#modal-change-customer').find('select[name="editConsigneeId"]').val();
        data['notifypartyId'] = $('#modal-change-customer').find('select[name="editNotifypartyId"]').val();

        console.log(data);

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: 'post',
            data: JSON.stringify(data),
            url: myContextPath + "/sales/edit",
            contentType: "application/json",

            success: function(data) {
                if (data.status === 'success') {
//                     if (!isEmpty(row.data())) {
//                         row.data(data.data).invalidate();
//                     } else{
                    table.ajax.reload();
                //}
                    $('#modal-change-customer').modal('toggle');
                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong>Edited Customer.');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });

                }
                 
            }
        });

        })





        let consignee_add_element = $('#add-consigner')
    var addConsigneeTargetElement;
    consignee_add_element.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        addConsigneeTargetElement = $(event.relatedTarget);
        var customerId = $('#modal-change-customer').find('select#customerCode').val();
        $(this).find('input[name="code"]').val(customerId);
        // modalAddElementTriggerEle = $(event.relatedTarget);
    }).on('hidden.bs.modal', function() {
        $(this).find('span.help-block').html('')
        $(this).find('.has-error').removeClass('has-error')
        $(this).find('input,select').val('').trigger('change');
    }).on('click', 'button#save-consig', function() {
         let isValid = $('#formAddConsignee').find('input[name="consigneeName"], input[name="consigneeAddr"]').valid()
        if (!isValid) {
            return false;
        }
        let data = {};
        data['code'] = consignee_add_element.find('input[name="code"]').val();
        data['consigneeName'] = consignee_add_element.find('input[name="consigneeName"]').val();
        data['consigneeAddr'] = consignee_add_element.find('input[name="consigneeAddr"]').val();

        console.log(data);
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/sales/addConsignee?code=" + data.code + "&consigneeName=" + data.consigneeName + "&consigneeAddr=" + data.consigneeAddr,
            contentType: "application/json",
            success: function(data) {

                if (data.status === 'success') {
                    $('#add-consigner').modal('toggle');

                    $.ajax({
                        beforeSend: function() {
                            $('#spinner').show()
                        },
                        complete: function() {
                            $('#spinner').hide();
                        },
                        type: "GET",
                        async: false,
                        url: myContextPath + "/customer/data/" + data.data.code,
                        success: function(custdfdata) {
                            if (custdfdata.status == 'success') {
                                var selectedVal = '';
                                if (custdfdata.data.consigneeNotifyparties.length == 1) {
                                    selectedVal = custdfdata.data.consigneeNotifyparties[0].id;
                                }
                                let consArray = custdfdata.data.consigneeNotifyparties.filter(function(index) {
                                    return !isEmpty(index.cFirstName);
                                })
                                let container=$(addConsigneeTargetElement).closest('.modal-body')
                                container.find('select[name="editConsigneeId"]').empty();
                                container.find('select[name="editConsigneeId"]').select2({
                                    allowClear: true,
                                    width: '150px',
                                    data: $.map(consArray, function(consigneeNotifyparties) {
                                        return {
                                            id: consigneeNotifyparties.id,
                                            text: consigneeNotifyparties.cFirstName + ' ' + consigneeNotifyparties.cLastName
                                        };
                                    })
                                });
                            }
                        },
                        error: function(e) {
                            console.log("ERROR: ", e);

                        }
                    });

                }
            }
        });
    })

        let NP_add_element = $('#add-ntfy-party')
    var addNotifyPartyTargetElement;
    NP_add_element.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        addNotifyPartyTargetElement = $(event.relatedTarget);
        var customerId = $('#modal-change-customer').find('select#customerCode').val();
        $(this).find('input[name="code"]').val(customerId);
        // modalAddElementTriggerEle = $(event.relatedTarget);
    }).on('hidden.bs.modal', function() {
        $(this).find('span.help-block').html('')
        $(this).find('.has-error').removeClass('has-error')
        $(this).find('input,select').val('').trigger('change');
    }).on('click', 'button#save-NP', function() {


        let isValid=$('#formAddNotifyParty').find('input[name="notifyPartyName"], input[name="notifyPartyAddr"]').valid()
        if (!isValid) {
            return false;
        }
        let data = {};
        data['code'] = NP_add_element.find('input[name="code"]').val();
        data['notifyPartyName'] = NP_add_element.find('input[name="notifyPartyName"]').val();
        data['notifyPartyAddr'] = NP_add_element.find('input[name="notifyPartyAddr"]').val();

        console.log(data);
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/sales/addNotifyParty?code=" + data.code + "&notifyPartyName=" + data.notifyPartyName + "&notifyPartyAddr=" + data.notifyPartyAddr,
            contentType: "application/json",
            success: function(data) {

                if (data.status === 'success') {
                    $('#add-ntfy-party').modal('toggle');

                    $.ajax({
                        beforeSend: function() {
                            $('#spinner').show()
                        },
                        complete: function() {
                            $('#spinner').hide();
                        },
                        type: "GET",
                        async: false,
                        url: myContextPath + "/customer/data/" + data.data.code,
                        success: function(custdfdata) {
                            if (custdfdata.status == 'success') {
                                var selectedVal = '';
                                if (custdfdata.data.consigneeNotifyparties.length == 1) {
                                    selectedVal = custdfdata.data.consigneeNotifyparties[0].id;
                                }

                                let npArray = custdfdata.data.consigneeNotifyparties.filter(function(index) {
                                    return !isEmpty(index.npFirstName);
                                })
                                let container=$(addNotifyPartyTargetElement).closest('.modal-body')
                                container.find('select[name="editNotifypartyId"]').empty();
                                container.find('select[name="editNotifypartyId"]').select2({
                                    allowClear: true,
                                    width: '150px',
                                    data: $.map(npArray, function(consigneeNotifyparties) {
                                        return {
                                            id: consigneeNotifyparties.id,
                                            text: consigneeNotifyparties.npFirstName + ' ' + consigneeNotifyparties.npLastName
                                        };
                                    })
                                });
                            }
                        },
                        error: function(e) {
                            console.log("ERROR: ", e);

                        }

                    });

                }
            }
        });
    })

});
function cancelStock(shippingInstructionId, data) {
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
        url: myContextPath + "/sales/delete/shippingInstruction?shippingInstructionId=" + shippingInstructionId,
        contentType: "application/json",
        success: function(data) {
            response = data;
        }
    });
    return response;
}

    function customerInitSelect2(element, value) {
        $(element).select2({
            allowClear: true,
            minimumInputLength: 2,
            triggerChange: true,
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
    }
