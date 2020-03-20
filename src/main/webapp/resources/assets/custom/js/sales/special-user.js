var makersJson;

$(function() {
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
    $('.autonumber').autoNumeric('init');
    $.getJSON(myContextPath + "/data/categories.json", function(data) {
        var categoryJson = data;
        var ele = $('#vehicleCategories');
        $(ele).select2({

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
    $('#priceMin').hide();
    $('#priceMax').hide();
    $('.priceMin').hide();
    // Year picker
    $('.year-picker').datepicker({
        format: "yyyy",
        viewMode: "years",
        minViewMode: "years",
        autoclose: true
    })
    $.getJSON(myContextPath + "/data/colors.json", function(data) {
        var colorsJson = data;
        var ele = $('#colors');

        $(ele).select2({
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
    //     for filter inquired vehicles
    setFilters();
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
        width: '100%',
        tags: true
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
    $('#subModels').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        width: '100%',
        tags: true
    })
    // Datatable
    var table = $('#table-stock').DataTable({
        "pageLength": 25,
        //         "processing": true,
        //         "serverSide": true,
        "ordering": true,
        //         "searchDelay": 500,
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "language": {
            processing: '<i class="fa fa-spinner fa-spin fa-3x fa-fw"></i><span class="sr-only">Loading...</span> '
        },
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
            "url": myContextPath + "/accounts/specialuser/stock/data",
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
                    return '<input class="selectBox" type="checkbox" id="check-box-select"  data-stockno="' + row.stockNo + '" value="' + data + '">';
                }
                return data;
            }
        }, {
            targets: 1,
            //  orderable: false,
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
            targets: 2,
            // orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "chassisNo",
            "data": "chassisNo"
        }, {
            targets: 3,
            // orderable: false,
            "searchable": false,
            "data": "category"
        }, {
            targets: 4,
            // orderable: false,
            "searchable": false,
            "data": "maker",
            "render": function(data, type, row) {
                return data + "/" + row.model
            }

        }, {
            targets: 5,
            "className": "dt-right",
            // orderable: false,
            "searchable": false,

            "data": "fob",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="&yen; "  data-m-dec="0">' + ifNotValid(data, 0.0) + '</span>'
            }

        }, {
            targets: 6,
            //  orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "destinationCountry",
            "data": "destinationCountry"
        }, {
            targets: 7,
            //  orderable: false,
            "searchable": true,
            "name": "destinationPort",
            "data": "destinationPort"
        }, {
            targets: 8,
            orderable: false,
            "searchable": false,
            "data": "totalTaxIncluded",
            "render": function(data, type, row) {
                var html;
                html = '<a href="#" class="ml-5 btn btn-info btn-xs" data-backdrop="static" data-toggle="modal" data-target="#modal-price-calc" data-stockNo="' + row.stockNo + '"><i class="fa fa-fw fa-calculator"></i>Price Calc</a>'

                return '<div style="width:100px;">' + html + '</div>';
            }

        }],
        "drawCallback": function(settings, json) {
            $("#table-stock").find('.autonumber').autoNumeric('init')

        }

    });
    table.on("keyup", "input.autonumber", function(setting) {
        //auto select checkbox
        var fob = $(this).closest('tr').find('input[name="fob"]').autoNumeric('init').autoNumeric('get');
        var thresholdRange = $(this).closest('tr').find('input[name="thresholdRange"]').autoNumeric('init').autoNumeric('get');

        if (!isEmpty(fob)) {
            var row = $(this).closest('tr');
            row.find('#check-box-select').prop('checked', true);
            var tr = table.row(row).select();
        } else {
            var row = $(this).closest('tr');
            row.find('#check-box-select').prop('checked', false);
            var tr = table.row(row).deselect();

        }

    })
    //filter
    $('input[type="radio"][name="statusFilter"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        //         var btnSaveEle = $('#save-stock');
        //         if ($(this).val() == 0) {
        //             $(btnSaveEle).removeClass('hide');
        //         } else if ($(this).val() == 1) {
        //             $(btnSaveEle).removeClass('hide');
        //         }
        table.ajax.reload();
    })

    $('input[name="showStock"], input[name="excludeProfit"]').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    });

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
    $('#filter-account').on('change', function() {
        table.ajax.reload();
    })
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
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            addSpecialUserListValidations($(this.node()));
        })

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
        table.rows({
            selected: false
        }).every(function(rowIdx, tableLoop, rowLoop) {
            removeSpecialUserListValidations($(this.node()));
        })

    });

    //price calculator
    let modal_update_min_price = $('#modal-update-min-selling-price');
    modal_update_min_price.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }

        if (table.rows({
            selected: true,
            page: 'current'
        }).count() == 0) {
            alert($.i18n.prop('common.alert.stock.noselection'));
            return event.preventDefault();
        }
    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
    }).on('click', '#btn-update-price', function(e) {
        if (!$(modal_update_min_price).find('input.form-control').valid()) {
            return;
        }

        var data = [];
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var rowData = table.row(this).data();
            data.push(rowData.stockNo)
        })
        let minSellingPrice = Number(getAutonumericValue(modal_update_min_price.find('#minSellingPrice')));
        let webPrice = Number(getAutonumericValue(modal_update_min_price.find('#webPrice')));
        let showStock = ifNotValid($('#showStock').is(':checked'), '');
        let queryString = "?sellingPrice=" + minSellingPrice + "&offerPrice=" + webPrice + "&showStock=" + showStock

        $.ajax({
            beforeSend: function() {
                $('#spinner').show();
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/update/sellingPrice/onMultiSelect" + queryString,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    modal_update_min_price.modal('toggle')
                    table.ajax.reload();
                }
            }
        });

    })

    //price calculator
    var targetElement;
    let modal_price_calc = $('#modal-price-calc');
    modal_price_calc.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        targetElement = $(e.relatedTarget);
        var stockNo = targetElement.attr('data-stockNo');
        modal_price_calc.find('input[name="stockNo"]').val(stockNo);
        $.ajax({
            beforeSend: function() {
                $('#spinner').show();
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",

            url: myContextPath + "/stock/price/details?stockNo=" + stockNo,
            contentType: "application/json",

            success: function(data) {
                if (data.status == 'success') {
                    data = data.data;
                    let minSellPriceInDollar = data.minSellingPriceInYen;

                    modal_price_calc.find('#purchasePrice').autoNumeric('init').autoNumeric('set', ifNotValid(data.purchaseCost, 0));
                    modal_price_calc.find('#purchaseCostTaxAmount').autoNumeric('init').autoNumeric('set', ifNotValid(data.purchaseCostTaxAmount, 0));
                    modal_price_calc.find('#commission').autoNumeric('init').autoNumeric('set', ifNotValid(data.commision, 0));
                    modal_price_calc.find('#commisionTaxAmount').autoNumeric('init').autoNumeric('set', ifNotValid(data.commisionTaxAmount, 0));
                    modal_price_calc.find('#recycle').autoNumeric('init').autoNumeric('set', ifNotValid(data.recycle, 0));
                    modal_price_calc.find('#roadTax').autoNumeric('init').autoNumeric('set', ifNotValid(data.roadTax, 0));
                    modal_price_calc.find('#offerPrice').autoNumeric('init').autoNumeric('set', ifNotValid(data.offerPrice, ""));
                    modal_price_calc.find('#otherCharges').autoNumeric('init').autoNumeric('set', ifNotValid(data.otherCharges == 0 ? data.stockOtherCharge : data.otherCharges, 0));
                    modal_price_calc.find('#transportation').autoNumeric('init').autoNumeric('set', ifNotValid(data.transportationCharges == 0 ? data.stockTransportCharge : data.transportationCharges, 0));
                    modal_price_calc.find('#exchangeRate').autoNumeric('init').autoNumeric('set', ifNotValid(data.usdExchangeRate, 0));
                    modal_price_calc.find('#courierCharge').autoNumeric('init').autoNumeric('set', ifNotValid(data.courierCharge, 5000));
                    modal_price_calc.find('#shippingCharge').autoNumeric('init').autoNumeric('set', ifNotValid(data.shippingCharge, 0));
                    modal_price_calc.find('#inspectionCharge').autoNumeric('init').autoNumeric('set', ifNotValid(data.inspectionCharge, 0));
                    modal_price_calc.find('#radiationCharge').autoNumeric('init').autoNumeric('set', ifNotValid(data.radiationCharge, ""));
                    modal_price_calc.find('#freightPerM3').autoNumeric('init').autoNumeric('set', ifNotValid(data.freightPerM3, 0));
                    modal_price_calc.find('#freightCharge').autoNumeric('init').autoNumeric('set', ifNotValid(data.freightCharge, 0));
                    modal_price_calc.find('#profit').autoNumeric('init').autoNumeric('set', ifNotValid(data.profit, 0));
                    modal_price_calc.find('#excludingProfit').autoNumeric('init').autoNumeric('set', ifNotValid(data.excludingProfit, 0));
                    if (data.minSellingPriceInYen == null) {
                        modal_price_calc.find('.price-calc').first().trigger('keyup');
                    } else {
                        modal_price_calc.find('#totalYen').autoNumeric('init').autoNumeric('set', ifNotValid(data.minSellingPriceInYen, 0));
                        modal_price_calc.find('#totalDollar').autoNumeric('init').autoNumeric('set', ifNotValid(data.minSellingPriceInDollar, 0));
                    }

                }

            }
        });
    }).on('hidden.bs.modal', function() {
        $(this).find('input').val('');
        //$('#showStock').iCheck('uncheck');
    }).on('keyup', '.price-calc', function() {
        priceCalculation()
    }).on('ifChanged', '.checkExcludeProfit', function() {
        priceCalculation()
    }).on('click', '#btn-update', function() {
        let data = {};
        let stockNo = modal_price_calc.find('input[name="stockNo"]').val();
        data['sellingPriceInYen'] = getAutonumericValue(modal_price_calc.find('#totalYen'));
        data['sellingPriceInDollar'] = getAutonumericValue(modal_price_calc.find('#totalDollar'));
        data['exchangeRate'] = getAutonumericValue(modal_price_calc.find('#exchangeRate'));
        data['offerPrice'] = getAutonumericValue(modal_price_calc.find('#offerPrice'));
        data['shippingCharge'] = getAutonumericValue(modal_price_calc.find('#shippingCharge'));
        data['inspectionCharge'] = getAutonumericValue(modal_price_calc.find('#inspectionCharge'));
        data['radiationCharge'] = getAutonumericValue(modal_price_calc.find('#radiationCharge'));
        data['freightPerM3'] = getAutonumericValue(modal_price_calc.find('#freightPerM3'));
        data['freightCharge'] = getAutonumericValue(modal_price_calc.find('#freightCharge'));
        data['courierCharge'] = getAutonumericValue(modal_price_calc.find('#courierCharge'));
        data['profit'] = getAutonumericValue(modal_price_calc.find('#profit'));
        data['excludingProfit'] = getAutonumericValue(modal_price_calc.find('#excludingProfit'));
        data['otherCharges'] = getAutonumericValue(modal_price_calc.find('#otherCharges'));
        data['transportCharge'] = getAutonumericValue(modal_price_calc.find('#transportation'));
        let showStock = ifNotValid($('#showStock').is(':checked'), '');
        let queryString = "?stockNo=" + stockNo + "&showStock=" + showStock
        $.ajax({
            beforeSend: function() {
                $('#spinner').show();
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/update/sellingPrice" + queryString,
            contentType: "application/json",
            success: function(data) {
                if (data.status == 'success') {
                    var tr = $(targetElement).closest('tr');
                    var row = table.row(tr);
                    tr.remove();
                    modal_price_calc.modal('toggle');
                }

            }
        });

    })
    var priceCalculation = function() {
        let purchase_price = Number(getAutonumericValue(modal_price_calc.find('#purchasePrice')));
        let purchase_price_tax_amount = Number(getAutonumericValue(modal_price_calc.find('#purchaseCostTaxAmount')));
        let commission = Number(getAutonumericValue(modal_price_calc.find('#commission')));
        let commission_tax_amount = Number(getAutonumericValue(modal_price_calc.find('#commisionTaxAmount')));
        let recycle = Number(getAutonumericValue(modal_price_calc.find('#recycle')));
        let road_Tax = Number(getAutonumericValue(modal_price_calc.find('#roadTax')));
        let other_charges = Number(getAutonumericValue(modal_price_calc.find('#otherCharges')));
        let transportation = Number(getAutonumericValue(modal_price_calc.find('#transportation')));
        let inspection_charge = Number(getAutonumericValue(modal_price_calc.find('#inspectionCharge')));
        let shipping_charge = Number(getAutonumericValue(modal_price_calc.find('#shippingCharge')));
        let radiation_charge = Number(getAutonumericValue(modal_price_calc.find('#radiationCharge')));
        let m3 = Number(getAutonumericValue(modal_price_calc.find('#m3')));
        let freightPerM3 = Number(getAutonumericValue(modal_price_calc.find('#freightPerM3')));
        let freight_charge = Number(getAutonumericValue(modal_price_calc.find('#freightCharge')));
        let courier_charge = Number(getAutonumericValue(modal_price_calc.find('#courierCharge')));
        let total = 0;
        let total_purchase_cost = purchase_price + purchase_price_tax_amount + commission + commission_tax_amount + recycle + road_Tax + other_charges;
        total += total_purchase_cost;
        total += transportation + shipping_charge + radiation_charge + courier_charge + inspection_charge;
        let freightCharge = 0.0;
        if (m3 > 0 && freightPerM3 > 0) {
            freightCharge = (m3 * freightPerM3);
        }
        if (freight_charge < (m3 * freightPerM3) || freight_charge > (m3 * freightPerM3)) {
            freightCharge = freight_charge
        }
        total += freightCharge;
        let profit = Number(getAutonumericValue(modal_price_calc.find('#profit')));
        total += profit;

        let exchangeRate = Number(getAutonumericValue(modal_price_calc.find('#exchangeRate')));
        setAutonumericValue(modal_price_calc.find('#freightCharge'), freightCharge)
        setAutonumericValue(modal_price_calc.find('#totalYen'), total)
        setAutonumericValue(modal_price_calc.find('#totalDollar'), (total / exchangeRate))
        let excludeProfitCheckedValue = 0;
        if (modal_price_calc.find("#excludeProfit").prop("checked")) {
            excludeProfitCheckedValue = total - profit - purchase_price_tax_amount - commission_tax_amount - recycle - road_Tax;
        } else {
            excludeProfitCheckedValue = total - profit
        }
        setAutonumericValue(modal_price_calc.find('#excludingProfit'), excludeProfitCheckedValue)

    }
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
function addSpecialUserListValidations(element) {
    $(element).find('input[name="fob"]').addClass('validate-required');
}
function removeSpecialUserListValidations(element) {
    $(element).find('input[name="fob"]').removeClass('validate-required');

}
function isStockEntryValid(element) {
    var valid = true;
    valid = $(element).find('input[name="fob"]').valid();
    return valid;
}

function format(rowData) {

    var html = '<div class="box-body no-padding bg-darkgray"><div class="table-responsive">' + '    <table class="table table-bordered" style="overflow-x:auto;">' + '        <tr>' + '            <th class="lable">Auction Grade</th>' + '            <td class="word-wrap value"><span class="badge bg-light-blue">' + (typeof rowData.grade != 'undefined' ? rowData.grade : '') + '</span></td>' + '            <th class="lable">Year</th>' + '            <td class="word-wrap value"><span class="badge bg-light-blue">' + (typeof rowData.firstRegDate != 'undefined' ? rowData.firstRegDate : '') + '</span></td>' + '            <th class="lable">Transmission Type</th>' + '            <td class="word-wrap value"><span class="badge bg-light-blue">' + (typeof rowData.transmission != 'undefined' ? rowData.transmission : '') + '</span></td>' + '            <th class="lable">Door/Seat</th>' + '            <td class="word-wrap value"><span class="badge bg-light-blue">' + (typeof rowData.noOfDoors != 'undefined' ? rowData.noOfDoors : '-') + '/' + (typeof rowData.noOfSeat != 'undefined' ? rowData.noOfSeat : '-') + '</span></td>' + '        </tr>' + '        <tr>' + '            <th class="lable">Fuel</th>' + '            <td class="word-wrap value"><span class="badge bg-light-blue">' + (typeof rowData.fuel != 'undefined' ? rowData.fuel : '') + '</span></td>' + '            <th class="lable">Driven</th>' + '            <td class="word-wrap value"><span class="badge bg-light-blue">' + (typeof rowData.driven != 'undefined' ? rowData.driven : '') + '</span></td>' + '            <th class="lable">Mileage</th>' + '            <td class="word-wrap value"><span class="badge bg-light-blue">' + (typeof rowData.mileage != 'undefined' ? rowData.mileage + ' KM' : '') + '</span></td>' + '            <th class="lable">Color</th>' + '            <td class="word-wrap value"><span class="badge bg-light-blue">' + (typeof rowData.color != 'undefined' ? rowData.color : '') + '</span></td>' + '        </tr>' + '        <tr>' + '            <th class="lable">Orgin</th>' + '            <td class="word-wrap value"><span class="badge bg-light-blue">' + (typeof rowData.orgin != 'undefined' ? rowData.orgin : '') + '</span></td>' + '            <th class="lable">CC</th>' + '            <td class="word-wrap value"><span class="badge bg-light-blue">' + (typeof rowData.cc != 'undefined' ? rowData.cc : '') + '</span></td>' + '            <th class="lable"></th>' + '            <td class="word-wrap value"><span class="badge bg-light-blue"></span></td>' + '            <th class="lable "></th>' + '            <td class="word-wrap value"><span class="badge bg-light-blue"></span></td>' + '        </tr>' + '    </table>' + '</div></div>';

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
