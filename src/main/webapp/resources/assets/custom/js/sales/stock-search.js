var makersJson;

//TODO: disable select all checkbox when all check boxes are disabled
$(function() {

    $.ajaxSetup({
        async: false
    });

    $.getJSON(myContextPath + "/data/sales-dashboard/status-count", function(data) {
        $('#inquiry-count').html(data.data.inquiry);
        $('#porforma-count').html(data.data.porforma);
        $('#reserved-count').html(data.data.reserved);
        $('#shipping-count').html(data.data.shipping);
        $('#sales-count').html(data.data.salesorder);
        $('#status-count').html(data.data.status);
    });

    $.getJSON(myContextPath + "/data/colors.json", function(data) {
        var colorsJson = data;
        var ele = $('#colors');

        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            data: $.map(colorsJson, function(item) {
                return {
                    id: item.color,
                    text: item.color
                };
            })
        });
    })
    $.getJSON(myContextPath + "/data/transmissionTypes.json", function(data) {
        var transmissionTypesJson = data;
        var ele = $('#transmissions');
        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            placeholder: function() {
                $(this).attr('data-placeholder');
            },
            width: '100%',
            data: $.map(transmissionTypesJson, function(item) {
                return {
                    id: item.type,
                    text: item.type
                };
            })
        });
    })
    $.getJSON(myContextPath + "/data/categories.json", function(data) {
        var categoryJson = data;
        var ele = $('#vehicleCategories');
        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            minimumResultsForSearch: -1,
            placeholder: function() {
                $(this).attr('data-placeholder');
            },

            width: '100%',
            data: $.map(categoryJson, function(item) {
                var childrenArr = [];
                $.each(item.subCategories, function(i, val) {
                    childrenArr.push({
                        id: val.name,
                        text: val.name
                    })
                })
                return {
                    text: item.name,
                    children: childrenArr
                };
            })
        });

    })
    $.getJSON(myContextPath + "/data/makerModel.json", function(data) {
        makersJson = data;
        var ele = $('#makers');
        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            width: '100%',
            data: $.map(makersJson, function(item) {
                return {
                    id: item.name,
                    text: item.name,
                    data: item
                };
            })
        });
    })

    $.getJSON(myContextPath + "/data/currency.json", function(data) {
        $('#currency').select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.currencySeq,
                    text: item.currency,
                    data: item
                };
            })
        });

    })
    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        var countriesJson = data;
        $('#destinationCountry').select2({
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
    $.ajaxSetup({
        async: true
    });
    $(document).on('change', '#makers', function() {
        var maker = $(this).val();
        var modelEle = $('#models');
        var modelList = [];
        var matchVal = 'item.name'
          , logic = '||'
          , condition = '';
        for (i = 0; i < maker.length; i++) {
            condition += matchVal + '==="' + maker[i] + '"||';
        }
        condition = condition.replace(/(.*?)([\|\|]+?)$/, '$1');
        $.each(makersJson, function(i, item) {
            if (eval(condition)) {
                $.merge(modelList, item.models);

            }

        });
        var prevSelectedValMaker = modelEle.val();
        if (typeof modelList == 'undefined' || modelList.length == 0) {

            return;
        }
        $(modelEle).empty();
        $(modelEle).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            width: '100%',
            data: $.map(modelList, function(item) {
                return {
                    id: item.modelName,
                    text: item.modelName,
                    data: item
                };
            })
        }).val(prevSelectedValMaker).trigger('change');

    });
    //     for filter inquired vehicles
    setFilters();
    // Year picker
    $('.year-picker').datepicker({
        format: "yyyy",
        viewMode: "years",
        minViewMode: "years",
        autoclose: true
    })

    $('.autonumber').autoNumeric('init');
    $('input[type="checkbox"][name="statusFilter"].minimal, input[type="radio"][name="statusFilter"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        var btnLockEle = $('#btn-lock');
        var btnUnlockEle = $('#btn-unlock');
        var btnReserveEle = $('#btn-reserve');
        var btnUnreserveEle = $('#btn-unreserve');

        if ($(this).val() == 0) {
            $(btnLockEle).removeClass('hide');
            $(btnUnlockEle).removeClass('hide');
            $(btnReserveEle).removeClass('hide');
            $(btnUnreserveEle).addClass('hide');
            table.column(13).visible(false);
        } else if ($(this).val() == 1) {
            $(btnLockEle).addClass('hide');
            $(btnUnlockEle).addClass('hide');
            $(btnReserveEle).addClass('hide');
            $(btnUnreserveEle).removeClass('hide');
            table.column(13).visible(true);
        } else if ($(this).val() == 2 || $(this).val() == 3) {
            $(btnLockEle).addClass('hide');
            $(btnUnlockEle).addClass('hide');
            $(btnReserveEle).addClass('hide');
            $(btnUnreserveEle).addClass('hide');
            table.column(13).visible(true);
        } else if ($(this).val() == 4) {
            $(btnLockEle).addClass('hide');
            $(btnUnlockEle).addClass('hide');
            $(btnReserveEle).addClass('hide');
            $(btnUnreserveEle).addClass('hide');
            table.column(13).visible(true);
        }

        table.ajax.reload();
    })
    $('#btn-unreserve').on('click', function() {
        var alert = confirm($.i18n.prop('confirm.stock.unreserve'));
        if (alert == true) {
            var stockNo = [];
            table.rows({
                selected: true,
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                var data = table.row(this).data();
                stockNo.push(data.stockNo);
                table.ajax.reload();
            })

            $.ajax({
                beforeSend: function() {
                    $('#spinner').show()
                },
                complete: function() {
                    $('#spinner').hide();
                },
                type: "put",
                data: JSON.stringify(stockNo),
                url: myContextPath + "/stock/unReserve",
                async: true,
                contentType: "application/json",
                success: function(data) {
                    $('#alert-block').css('display', 'block').html('<strong>Failure!</strong> ' + data.message + '');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                    table.ajax.reload();
                }
            });
        } else {
            table.ajax.reload();
        }
    })
    $('#filter-account').on('change', function() {
        table.ajax.reload();
    })
    $('[data-toggle="tooltip"]').tooltip();

    $('#purchaseDate').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        purchased_min = picker.startDate.format('DD-MM-YYYY');
        purchased_max = picker.endDate.format('DD-MM-YYYY');
        picker.element.val(purchased_min + ' - ' + purchased_max);
        $('#purchaseDateFrom').val(purchased_min);
        $('#purchaseDateTo').val(purchased_max);
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))

    });
    $('#date-form-group').on('click', '.clear-date', function() {
        $('#purchaseDate').val('');
        $('#purchaseDateFrom').val('');
        $('#purchaseDateTo').val('');

        $(this).remove();

    })

    $('#models').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        width: '100%',
    })
    $('#models').on('change', function() {
        var data = $(this).select2('data');
        var model = $(this).val();
        var subModelEle = $('#subModels');
        var subModelList = [];
        var matchVal = 'data.text'
          , logic = '||'
          , condition = '';
        if (!isEmpty(model)) {

            for (i = 0; i < model.length; i++) {
                condition += matchVal + '==="' + model[i] + '"||';
            }
        }
        condition = condition.replace(/(.*?)([\|\|]+?)$/, '$1');
        $.each(data, function(i, data) {
            if (eval(condition)) {
                $.merge(subModelList, ifNotValid(data.data.subModel, []));

            }

        });

        var prevSelectedVal = subModelEle.val();
        if (isEmpty(subModelList)) {

            return;
        }
        $(subModelEle).empty();
        $(subModelEle).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            width: '100%',

            data: $.map(subModelList, function(item) {
                return {
                    id: item.subModelName,
                    text: item.subModelName
                };
            })
        }).val(prevSelectedVal).trigger('change');

    });

    $('#subModels').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        width: '100%',
    })
    $('#lotNos').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        width: '100%',
        tags: true
    })
    $('#grades').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        width: '100%',
        tags: true
    })
    $('#stockNos').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        width: '100%',
        tags: true
    })
    $('#auctionGrades').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        width: '100%',
        tags: true
    })
    $('#shifts').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true,
        width: '100%'
    })
    $('#modelTypes').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        width: '100%',
        tags: true
    })
    $('#keywords').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        width: '100%',
        tags: true
    })
    $('#options').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        width: '100%',
        tags: true
    })
    jQuery.fn.DataTable.Api.register('buttons.exportData()', function(options) {
        if (this.context.length) {
            var data = table.ajax.json().data;

            var columnDefs = getExcelExportColumnDefs();
            let header = columnDefs.map((col)=>col.name);
            return {
                body: formatExcelData(data, columnDefs),
                header: header
            };
        }
    });
    var exportoptions = {
        filename: function() {
            var d = new Date();
            return 'Stock_' + d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();
        },
        attr: {
            type: "button",
            id: 'dt_excel_export'
        },
        title: ''
    };
    // Datatable

    var table = $('#table-stock').DataTable({
        //         "processing": true,
        //         "serverSide": true,
        "pageLength": 25,
        //         "searchDelay": 500,
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "ordering": false,
        "ajax": {
            beforeSend: function() {
                let spinner = $('#spinner').clone()
                spinner.attr('id', 'datatableSpinner');
                $('body').append(spinner);
                spinner.show();
            },
            complete: function() {
                $('#datatableSpinner').remove();
            },
            "url": myContextPath + "/sales/stock-search-data",
            "data": function(data) {
                var criteria = $('#filter-container').find('input,select').serializeJSON();
                data['criteria'] = JSON.parse(criteria);
                data['criteria'].flag = $('input[type="radio"][name="statusFilter"]:checked').val();
                data['criteria'].account = $('#filter-account>option:selected').val();
                return data;

            }
        },
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        //         "order": [[2, "asc"]],
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            orderable: false,
            "searchable": false,
            className: 'select-checkbox',
            "name": "id",
            "data": "id",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    var isDisabled = "";
                    var icon = "";
                    var iconHtml = "";
                    var toolTipMessage = "";

                    if (row.reserve == 1 && row.reservedInfo.salesPersonId != userId) {
                        isDisabled = 'disabled'
                        sec: authorize = "hasRole('ROLE_SALES') and hasRole('ROLE_SALES_MANAGER')"
                        isDisabled = 'enabled'

                        toolTipMessage = "Reserved by " + row.userName;
                        icon = "fa-ticket";

                    } else if (row.isLocked == 1 && row.lockedBy != userId) {
                        isDisabled = 'disabled'
                        toolTipMessage = "Ask " + row.lockedBySalesPersonName;
                        icon = "fa-lock";
                    } else if (row.isLocked == 1 && row.lockedBy == userId) {
                        icon = "fa-lock";
                    }
                    if (icon.length != 0) {
                        iconHtml = '<i class="fa fa-fw ' + icon + '" data-toggle="tooltip" title="' + toolTipMessage + '" data-placement="top" class="red-tooltip"></i>'
                    }
                    return iconHtml + '<input class="selectBox" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '" ' + isDisabled + '>';
                }
                return data;
            }
        }, {
            targets: 1,
            orderable: false,
            visible: true,
            "data": "purchaseInfo.sDate"

        }, {
            targets: 2,
            orderable: false,
            "searchable": true,
            "name": "stockNo",
            "data": "stockNo",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {

                    return '<input type="hidden" name="stockNo" value="' + data + '"/><a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockNo="' + data + '">' + data + '</a>'
                }
                return data;
            }

        }, {
            targets: 3,
            orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "chassisNo",
            "data": "chassisNo"
        }, {
            targets: 4,
            "className": "details-control",
            orderable: false,
            "searchable": true,
            "data": "modelType",
            "name": "modelType"
        }, {
            targets: 5,
            orderable: false,
            "searchable": true,
            "data": "maker",
            "name": "maker"
        }, {
            targets: 6,
            orderable: false,
            "searchable": true,
            "data": "model",
            "name": "model"
        }, {
            targets: 7,
            orderable: false,
            visible: true,
            "searchable": false,
            "data": "grade"

        }, {
            targets: 8,
            "className": "dt-right",
            orderable: false,
            "searchable": false,
            "data": "fob",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + data + '</span>';
            }

        }, {
            targets: 9,
            orderable: false,
            "searchable": false,
            "data": "firstRegDate"

        }, {
            targets: 10,
            orderable: false,
            visible: true,
            "searchable": false,
            "data": "buyingPrice",
            visible: false,
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 11,
            orderable: false,
            visible: true,
            "searchable": false,
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
            orderable: false,
            visible: true,
            "searchable": false,
            "data": "equipment"

        }, {
            targets: 13,
            orderable: false,
            "searchable": true,
            "data": "customerName",
            "name": "Customer"
        }, {
            targets: 14,
            "orderable": false,
            "width": '100px',
            "data": "stockNo",
            "searchable": false,
            visible: false,
            "render": function(data, type, row) {
                var html;

                html = '<a href="#" class="ml-5 btn btn-info btn-xs" data-backdrop="static" data-toggle="modal" data-target="#modal-price-calc"><i class="fa fa-fw fa-calculator"></i>Price Calc</a>'

                return '<div style="width:100px;">' + html + '</div>';
            }
        }],
        //                 buttons: [$.extend(true, {}, exportoptions, {
        //                     extend: 'excelHtml5'
        //                 })],
        buttons: [$.extend(true, {}, exportoptions, {
            extend: 'excelHtml5'
        }), 'colvis'],
        "drawCallback": function(settings, json) {
            $('span.autonumber').autoNumeric('init')
            $('#filter-container').find('input.autonumber').autoNumeric('init')
        },
        "initComplete": function(settings, json) {},

    });
    table.column(13).visible(false);

    $('#btn-export-excel').on('click', function() {
        table.button("#dt_excel_export").trigger();
    })
    $('#table-stock tbody').on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
        } else {
            table.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = table.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                }

            })
            row.child(format(row.data())).show();
            tr.addClass('shown');
        }
    });

    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        table.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });
    $('#btn-apply-filter').click(function() {
        autoNumericSetRawValue($('#filter-container').find('.autonumber'))
        var selectedRows = table.rows({
            selected: true,
            page: 'current'
        }).nodes();
        autoNumericSetRawValue($(selectedRows).find('span.autonumber'))
        table.ajax.reload();
    });
    $('#btn-reset-filter').click(function() {
        var elements = $('#filter-container').find('.autonumber');
        resetElementInput(elements);
        table.ajax.reload();
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

    });
    var ifNotValid = function(val, str) {
        return typeof val === 'undefined' || val == null ? str : val;
    }
    //price calculator
    $('#modal-price-calc').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
    }).on('keyup', '.price-calc', function() {
        priceCalculation()
    })
    var priceCalculation = function() {
        let modal_element = $('#modal-price-calc');
        let purchase_price = Number(modal_element.find('#purchasePrice').autoNumeric('get'));
        let purchase_tax_percent = Number(modal_element.find('#purchasePriceTax').autoNumeric('get'));
        let purchase_tax = purchase_price * (purchase_tax_percent / 100);
        let purchase_price_tax_total = (purchase_price + purchase_tax);
        let recycle = Number(modal_element.find('#recycle').autoNumeric('get'));
        let m3 = Number(modal_element.find('#m3').autoNumeric('get'));
        let transport = Number(modal_element.find('#transport').autoNumeric('get'));
        let shipping_charges = Number(modal_element.find('#shippingCharges').autoNumeric('get'));
        let courier = Number(modal_element.find('#courier').autoNumeric('get'));
        let inspection = Number(modal_element.find('#inspection').autoNumeric('get'));
        let marine_insurance = Number(modal_element.find('#marineInsurance').autoNumeric('get'));
        let other_charges = Number(modal_element.find('#otherCharges').autoNumeric('get'));
        let total = (purchase_price_tax_total + recycle + m3 + transport + shipping_charges + courier + inspection + marine_insurance + other_charges);
        modal_element.find('#totalYen').autoNumeric('set', total);
    }

    //Model Show 
    var modalCreateProforma = $('#modal-create-proforma');
    modalCreateProforma.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        //validate custome
        var selectedRowData = table.rows({
            selected: true,
            page: 'current'
        }).data()
        if (selectedRowData.length == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return false;
        }

        var element;
        var i = 0;

        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = table.row(this).data();
            var stockNo = ifNotValid(data.stockNo, '');
            var maker = ifNotValid(data.maker, '');
            var model = ifNotValid(data.model, '');
            var chassisNo = ifNotValid(data.chassisNo, '');
            var firstRegDate = ifNotValid(data.firstRegDate, '');
            var price = ifNotValid(data.reservedInfo.price, 0.0);

            element = $('#item-invoice-clone').find('.item-invoice').clone();
            $(element).appendTo('#item-invoice-clone-container');

            $(element).find('input[name="stockNo"]').val(stockNo);
            $(element).find('input[name="maker"]').val(maker);
            $(element).find('input[name="model"]').val(model);
            $(element).find('input[name="chassisNo"]').val(chassisNo);
            $(element).find('input[name="firstRegDate"]').val(firstRegDate);
            $(element).find('input[name="hsCode"]').val(ifNotValid(data.hsCode, ''));
            if (data.destinationCountry == "SRI LANKA") {
                $(element).find('input[name="hsCode"]').addClass('required');
            } else {
                $(element).find('input[name="hsCode"]').removeClass('required');
            }
            $(element).find('input[name="fob"]').autoNumeric('init').autoNumeric('set', price);
            $(element).find('input[name="insurance"]').autoNumeric('init').autoNumeric('set', 0.0);
            $(element).find('input[name="shipping"]').autoNumeric('init').autoNumeric('set', 0.0);
            $(element).find('input[name="freight"]').autoNumeric('init').autoNumeric('set', 0.0);
            $(element).find('input[name="total"]').autoNumeric('init').autoNumeric('set', price);
            $(element).find('.calculation').trigger('keyup');
            i++;
        });

        $('#customerCode').select2({
            allowClear: true,
            placeholder: 'Search customer email',
            minimumInputLength: 2,
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
        })

        $(this).find('input.autonumber').autoNumeric('init');

    }).on('hidden.bs.modal', function() {
        //         $(this).find('#item-invoice-container .item-invoice div.row').not(':first').remove();
        $(this).find('#item-invoice-clone-container').html('');
        $(this).find("input,textarea").val('');
        $(this).find("select").val('').trigger('change');
    }).on('click', '.btn-remove-item', function() {
        var performaElement = $(modalCreateProforma).find('#item-invoice-clone-container div.row');
        if (performaElement.length > 1) {
            $(this).closest('div.row').remove();
            $(modalCreateProforma).find('.calculation').trigger('keyup');
        }
    }).on("change", 'select[name="customerId"]', function(event) {
        var id = $(this).val();
        if (id == null || id.length == 0) {
            modalCreateProforma.find('#cFirstName').empty();
            modalCreateProforma.find('#npFirstName').empty();
            return;
        }
        var data = $(this).select2('data')[0].data;
        var currencyEle = $('#currencyType')
        if (!isEmpty(data)) {
            currencyEle.val(data.currencyType).trigger('change').addClass('readonly');
        } else {
            currencyEle.val('').trigger('change');
        }
        //payment type
        var data = $(this).select2('data')[0].data;
        var paymentEle = $('#paymentType')
        if (!isEmpty(data)) {
            paymentEle.val(data.paymentType).trigger('change');
        } else {
            paymentEle.val('').trigger('change');
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
                    $('#cFirstName').empty();
                    $('#cFirstName').select2({
                        allowClear: true,
                        width: '100%',
                        data: $.map(consArray, function(consigneeNotifyparties) {
                            return {
                                id: consigneeNotifyparties.id,
                                text: consigneeNotifyparties.cFirstName + ' ' + consigneeNotifyparties.cLastName
                            };
                        })
                    }).val(selectedVal).trigger("change");

                    $('#npFirstName').empty();
                    let npArray = data.data.consigneeNotifyparties.filter(function(index) {
                        return !isEmpty(index.npFirstName);
                    })
                    $('#npFirstName').select2({
                        allowClear: true,
                        width: '100%',
                        data: $.map(npArray, function(consigneeNotifyparties) {
                            return {
                                id: consigneeNotifyparties.id,
                                text: consigneeNotifyparties.npFirstName
                            };
                        })
                    }).val(selectedVal).trigger("change");

                }
            },
            error: function(e) {
                console.log("ERROR: ", e);

            }
        });

    })

    $('#item-invoice-container').slimScroll({
        start: 'bottom',
        height: ''
    });
    $('#cFirstName').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        width: '100%',
        allowClear: true,
    });
    $('#npFirstName').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        width: '100%',
        allowClear: true,
    });
    $('#paymentType').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        width: '100%',
        allowClear: true,
    });
    $('#paymentSalesType').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        width: '100%',
        allowClear: true,
    });
    //  $.getJSON(myContextPath + "/data/currency.json", function(data) {
    //         currencyJson = data;
    //     })
    //Curency Proforma Json
    $.getJSON(myContextPath + "/data/currency.json", function(data) {
        currencyJson = data;
        $('#currencyType,#currencySalesType').select2({
            allowClear: true,
            width: '100%',
            data: $.map(currencyJson, function(item) {
                return {
                    id: item.currencySeq,
                    text: item.currency,
                    data: item
                };
            })
        })
    })

    $('#item-invoice-container').on('keyup', ".calculation", function() {
        doCalculation(this)
    }).on('change', 'input[name="total"]', function() {
        doCalculation(this)
    })
    function doCalculation(element) {
        var closestEle = $(element).closest('.row');

        var fob = Number($(closestEle).find('input[name="fob"]').autoNumeric('get'));
        var insurance = Number($(closestEle).find('input[name="insurance"]').autoNumeric('get'));
        var freight = Number($(closestEle).find('input[name="freight"]').autoNumeric('get'));
        var shipping = Number($(closestEle).find('input[name="shipping"]').autoNumeric('get'));
        var totalEle = $(closestEle).find('input[name="total"]');
        var total = fob + insurance + freight + shipping;
        //$(totalEle).autoNumeric('set', total);
        var element;
        var intVal = function(i) {
            return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
        };
        var isValid = function(val) {
            return typeof val === 'undefined' || val == null ? 0 : val;
        }

        var total = Number($(closestEle).find('input[name="total"]').autoNumeric('get'));
        var totalWithoutFob = insurance + freight + shipping;
        fob = total - totalWithoutFob;
        if (fob < 0) {
            alert($.i18n.prop('alert.profomainvoice.create.EqualsPrice'))
            $(element).autoNumeric('set', 0).trigger('keyup');
            return;
        }
        var fobEle = $(closestEle).find('input[name="fob"]');
        $(fobEle).autoNumeric('set', fob);

        //total
        var fobTotal = 0;
        $('#item-invoice-container').find('input[name="fob"]').each(function(index) {
            fobTotal += Number($(this).autoNumeric('get'))
        })
        var insuranceTotal = 0;
        $('#item-invoice-container').find('input[name="insurance"]').each(function(index) {
            insuranceTotal += Number($(this).autoNumeric('get'))
        })
        var freightTotal = 0;
        $('#item-invoice-container').find('input[name="freight"]').each(function(index) {
            freightTotal += Number($(this).autoNumeric('get'))
        })
        var shippingTotal = 0;
        $('#item-invoice-container').find('input[name="shipping"]').each(function(index) {
            shippingTotal += Number($(this).autoNumeric('get'))
        })
        var amountTotal = 0;
        $('#item-invoice-container').find('input[name="total"]').each(function(index) {
            amountTotal += Number($(this).autoNumeric('get'))
        })

        $('#fobTotal').autoNumeric('set', fobTotal);
        $('#insuranceTotal').autoNumeric('set', insuranceTotal);
        $('#freightTotal').autoNumeric('set', freightTotal);
        $('#shippingTotal').autoNumeric('set', shippingTotal);
        $('#grand-total').autoNumeric('set', amountTotal);

    }

    // Create Proforma Invoice Order

    $('#btn-generate-proforma-invoice').on('click', function() {
        if (!$('#create-proforma-form').find('input,select').valid()) {
            return;
        }
        var autoNumericElements = $('#modal-create-proforma').find('input.autonumber');
        autoNumericSetRawValue(autoNumericElements)
        var objectArr = [];
        var data = {};
        data = getFormData($("#proforma-invoice-container").find('.invoiceData'));
        object = $('#item-invoice-container').find('.item-invoice').serialize();
        $("#item-invoice-container").find('.item-invoice').each(function() {
            object = getFormData($(this).find('input,select,textarea'));
            objectArr.push(object);
        });
        data['items'] = objectArr.filter(value=>JSON.stringify(value) !== '{}');

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/invoice/proforma/order/save",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {

                    $.redirect(myContextPath + '/sales/proformainvoice', '', 'GET');
                    localStorage.setItem("aaj-sales-dashboard-active-nav", '1');
                }

            }
        });
    })
    $('#currencyType').on('change', function() {
        var data = $(this).select2('data')[0].data;
        if (!isEmpty(data)) {
            if (data.currency == "Yen") {
                modalCreateProforma.find('input.autonumber').autoNumeric('init').autoNumeric('update', {
                    aSign: data.symbol + ' ',
                    mDec: 0
                });
            } else {
                modalCreateProforma.find('input.autonumber').autoNumeric('init').autoNumeric('update', {
                    aSign: data.symbol + ' ',
                    mDec: 2
                });
            }
        }
    })

    //on open reserve modal
    $('#modal-reserve').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var data = [];
        table.rows({
            selected: true,
            page: 'all'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var rowData = table.row(this).data();
            data.push(rowData.stockNo)
        })
        if (data.length == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return false;
        } else {

            var reserveItemHtml = '<tr class="item clone"> <td><input type="hidden" name="stockNo" value=""/><span class="stockNo"></span></td> <td><span class="makerModel"></span></td> <td><span class="chassisNo"></span></td><td><span class="minimumSellingPrice autonumber" data-a-sign="&yen; "></span></td>  <td><div class="element-wrapper"><input type="text" name="price" class="form-control price autonumber required" data-threshold="" placeholder="Enter" data-a-sign="&yen; "/><span class="help-block"></span></div></td> </tr>'
            table.rows({
                selected: true,
                page: 'all'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                var data = table.row(this).data();

                var stockNo = ifNotValid(data.stockNo, '');
                var chassisNo = ifNotValid(data.chassisNo, '');
                var maker = ifNotValid(data.maker, '');
                var model = ifNotValid(data.model, '');
                var threshold = ifNotValid(data.thresholdRange, 0);
                var fob = ifNotValid(data.fob, 0);
                var element = $(reserveItemHtml);

                $(element).appendTo('#table-reserve-item>.item-container');
                $(element).find('input[name="stockNo"]').val(stockNo);
                $(element).find('.stockNo').text(stockNo);
                $(element).find('.chassisNo').text(chassisNo);
                $(element).find('.minimumSellingPrice').text(fob).attr('data-fob', fob);
                $(element).find('input[name="price"]').attr('data-fob', fob);
                $(element).find('.makerModel').text(maker + ' / ' + model);

            });
        }
        $(this).find('input.autonumber,span.autonumber').autoNumeric('init');

    }).on('hidden.bs.modal', function() {
        $('#table-reserve-item').find('.item-container').html('');
        $("#custId").empty();

    }).on('click', '#btn-reserve', function() {
        if (!$('#reserveForm').valid() || (!confirm($.i18n.prop('common.confirm.reserve')))) {
            return;
        }
        var data = [];
        var autoNumericElements = $('#table-reserve-item').find('input.autonumber');
        autoNumericSetRawValue(autoNumericElements)
        $('#table-reserve-item').find('.item-container').find('tr').each(function() {
            data.push(getFormData($(this).find('input[name="stockNo"],input[name="price"]')));
        });
        var currency = $('#modal-reserve').find('#currency').val();
        var custId = $("#custId").find(":selected").val();
        var parm = '?custId=' + custId
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data),
            url: myContextPath + "/stock/reserve" + parm,
            async: false,
            contentType: "application/json",
            success: function(data) {
                if (data.status == 'success') {
                    $('#modal-reserve').modal('toggle');
                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Stock reserved.');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                    table.ajax.reload();
                } else if (data.status == 'failed') {
                    alert(data.message);
                }

            }
        });
    })
    //lock stock 
    $('#btn-lock').click(function() {
        var data = [];
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var rowData = table.row(this).data();
            data.push(rowData.stockNo)
        })
        if (data.length == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return;
        } else {
            if (!confirm($.i18n.prop('common.confirm.lock'))) {
                return;
            }
        }
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data),
            url: myContextPath + "/stock/lock",
            async: false,
            contentType: "application/json",
            success: function(data) {

                $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Stock locked.');
                $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                    $("#alert-block").slideUp(500);
                });
                table.ajax.reload();
            }
        });

    })
    //Unlock stock 
    $('#btn-unlock').click(function() {
        var data = [];
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var rowData = table.row(this).data();
            data.push(rowData.stockNo)
        })
        if (data.length == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return;
        } else {
            if (!confirm($.i18n.prop('common.confirm.unlock'))) {
                return;
            }
        }
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(data),
            url: myContextPath + "/stock/unlock",
            async: false,
            contentType: "application/json",
            success: function(data) {

                $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Stock Unlocked.');
                $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                    $("#alert-block").slideUp(500);
                });
                table.ajax.reload();
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
    //reserve customer server side search 

    $('#custId').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true,
        placeholder: 'Search customer email',
        minimumInputLength: 2,
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
    }).on('change', function() {
        var data = $(this).select2('data')[0].data;
        var currencyEle = $('#currency')
        if (!isEmpty(data)) {
            currencyEle.val(data.currencyType).trigger('change');
        } else {
            currencyEle.val('').trigger('change');
        }

    })
    $('#modal-reserve #currency').on('change', function() {
        var data = $(this).select2('data')
        if (typeof data != undefined || data.length > 0) {
            data = data[0].data;
        }
        if (!isEmpty(data)) {
            $('#modal-reserve').find('input.autonumber,span.autonumber').autoNumeric('update', {
                aSign: data.symbol + ' '
            });
        }
        let currencySeq = $(this).val();
        if (isEmpty(currencySeq)) {
            return false;
        } else {
            updateSellingPrice(currencySeq, data.symbol);
        }
    })
    function updateSellingPrice(currency, symbol) {
        let modalReserve = $('#modal-reserve').find('span.autonumber');
        if (currency == 1) {
            modalReserve.each(function() {
                setAutonumericValue($(this), Number($(this).attr('data-fob')));
                $(this).autoNumeric('update', {
                    aSign: symbol + ' '
                });
            });
            return;
        }
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",

            url: myContextPath + "/currency/exchangeRate?currency=" + currency,
            async: false,
            contentType: "application/json",
            success: function(data) {
                modalReserve.each(function() {
                    setAutonumericValue($(this), Number($(this).attr('data-fob')) / data.data);
                    $(this).autoNumeric('update', {
                        aSign: symbol + ' '
                    });
                });
            }
        });

    }
})

function format(rowData) {

    var html = '<div class="box-body no-padding bg-darkgray"><div class="table-responsive">' + '    <table class="table table-bordered" style="overflow-x:auto;">' + '        <tr>' + '            <th style="background-color: #9ab9de;">Grade</th>' + '            <td class="word-wrap value"><span>' + ifNotValid(rowData.grade, '') + '</span></td>' + '            <th style="background-color: #9ab9de;">Year</th>' + '            <td class="word-wrap value"><span>' + ifNotValid(rowData.firstRegDate, '') + '</span></td>' + '            <th style="background-color: #9ab9de;">Transmission Type</th>' + '            <td class="word-wrap value"><span>' + ifNotValid(rowData.transmission, '') + '</span></td>' + '            <th style="background-color: #9ab9de;">Door/Seat</th>' + '            <td class="word-wrap value"><span>' + ifNotValid(rowData.noOfDoors, '-') + '/' + ifNotValid(rowData.noOfSeat, '-') + '</span></td>' + '        </tr>' + '        <tr>' + '            <th style="background-color: #9ab9de;">Fuel</th>' + '            <td class="word-wrap value"><span>' + ifNotValid(rowData.fuel, '') + '</span></td>' + '            <th style="background-color: #9ab9de;">Driven</th>' + '            <td class="word-wrap value"><span>' + ifNotValid(rowData.driven, '') + '</span></td>' + '            <th style="background-color: #9ab9de;">Mileage</th>' + '            <td class="word-wrap value"><span>' + (typeof rowData.mileage != 'undefined' ? rowData.mileage + ' KM' : '') + '</span></td>' + '            <th style="background-color: #9ab9de;">Color</th>' + '            <td class="word-wrap value"><span>' + ifNotValid(rowData.color, '') + '</span></td>' + '        </tr>' + '        <tr>' + '            <th style="background-color: #9ab9de;">Orgin</th>' + '            <td class="word-wrap value"><span>' + ifNotValid(rowData.orgin, '') + '</span></td>' + '            <th style="background-color: #9ab9de;">CC</th>' + '            <td class="word-wrap value"><span>' + ifNotValid(rowData.cc, '') + '</span></td>' + '            <th class="lable"></th>' + '            <td class="word-wrap value"><span></span></td>' + '            <th style="background-color: #9ab9de;">Auction Grade</th>' + '            <td class="word-wrap value"><span>' + ifNotValid(rowData.auctionGrade, '') + '</span></td>' + '        </tr>' + '    </table>' + '</div></div>';

    return html;
}
function setFilters() {
    var makerEle = $('#makers');
    if (!isEmpty(makerEle.attr('data-value'))) {
        makerEle.val(makerEle.attr('data-value')).trigger('change')
    }
    var modelsEle = $('#models');
    if (!isEmpty(modelsEle.attr('data-value'))) {
        modelsEle.val(modelsEle.attr('data-value')).trigger('change');
    }
    var vehicleCategoriesEle = $('#vehicleCategories');
    if (!isEmpty(vehicleCategoriesEle.attr('data-value'))) {
        vehicleCategoriesEle.val(vehicleCategoriesEle.attr('data-value')).trigger('change');
    }
}
function formatExcelData(data, header) {

    let results = data.map((item)=>{
        var result = [];
        for (let i = 0; i < header.length; i++) {
            var value = ifNotValid(item[header[i].data], '')
            //             if (typeof value == 'string') {
            //             } else if (typeof value == 'array') {
            //                 value = value.toString();
            //             }
            result.push(value)
        }
        return result
    }
    )
    return results
}
function getExcelExportColumnDefs() {
    return [{
        'data': 'stockNo',
        'name': 'Stock No'
    }, {
        'data': 'chassisNo',
        'name': 'ChassisNo'
    }, {
        'data': 'firstRegDate',
        'name': 'Reg. Date'
    }, {
        'data': 'maker',
        'name': 'Maker'
    }, {
        'data': 'model',
        'name': 'Model'
    }, {
        'data': 'category',
        'name': 'Category'
    }, {
        'data': 'subcategory',
        'name': 'Sub Category'
    }, {
        'data': 'modelType',
        'name': 'MdelType'
    }, {
        'data': 'grade',
        'name': 'Grade'
    }, {
        'data': 'transmission',
        'name': 'Transmission'
    }, {
        'data': 'noOfDoors',
        'name': 'No. of Doors'
    }, {
        'data': 'noOfSeat',
        'name': 'No Of Seat'
    }, {
        'data': 'fuel',
        'name': 'Fuel'
    }, {
        'data': 'driven',
        'name': 'Driven'
    }, {
        'data': 'equipment',
        'name': 'Equipment'
    }, {
        'data': 'fob',
        'name': 'Selling Price'
    }, {
        'data': 'buyingPrice',
        'name': 'Buying Price'
    }]
}
