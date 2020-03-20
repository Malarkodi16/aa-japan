var countriesJson;
$(function() {
    var table = $('#table-sales-summary').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": true,
        "ajax": {
            url: myContextPath + "/accounts/report/sales-summary-list"
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": "",

        }, {
            targets: 0,
            "className": "align-center",
            "data": "customerName"
        }, {
            targets: 1,
            "className": "align-center",
            "data": "salesPersonName"
        }, {
            targets: 2,
            "className": "align-center",
            "data": "maker",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    data = row.maker + " / " + row.model
                }
                return data;
            }
        }, {
            targets: 3,
            "className": "align-center",
            "data": "chassisNo"

        }, {
            targets: 4,
            "className": "dt-right",
            "data": "sellingPrice",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 5,
            "className": "dt-right",
            "data": "exchangeRate",
            "width": "50px",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="exchangeRate" data-a-sign="¥ ">' + data + '</span>';
            }
        }, {
            targets: 6,
            "className": "dt-right",
            "data": "exchangeRateSellingPrice",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" name="exchangeRateSellingPrice" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 7,
            "className": "dt-right",
            "data": "costOfGoods",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<a href="#" data-toggle="modal" data-target="#modal-sales-details" data-stockno="' + row.stockNo + '"><span class="autonumber" name="costOfGoods" data-a-sign="¥ " data-m-dec="0">' + data + '</span></a>';
                return;
            }
        }, {
            targets: 8,
            "className": "dt-right",
            "data": "margin",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber"  name="margin" data-a-sign="¥ " data-m-dec="0">' + ifNotValid(data, '') + '</span>';
            }
        }, {
            targets: 9,
            "className": "align-center",
            "data": "purchaseDate"
        }, {
            targets: 10,
            "className": "align-center",
            "data": "soldDate"
        }, {
            targets: 11,
            "className": "align-center",
            "data": "marginPercentage",
            "render": function(data, type, row) {
                data = data == -Infinity ? 0 : data;
                return '<span class="autonumber" name="marginPercentage" data-m-dec="0" data-a-sign=" %" data-p-sign="s">' + ifNotValid(data, 0) + '</span>';
            }
        }, {
            targets: 12,
            "className": "align-center",
            "data": "destinationPort",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display' && !(isEmpty(row.destinationCountry) && isEmpty(row.destinationPort))) {
                    data = ifNotValid(row.destinationCountry, '') + " / " + ifNotValid(row.destinationPort, '')
                }
                return data;
            }
        }, {
            targets: 13,
            "data": "etd"
        }, {
            targets: 14,
            "data": "eta"
        }, {
            targets: 15,
            "data": "location"
        }, {
            targets: 16,
            "className": "align-center",
            "data": "status",
            "render": function(data, type, row) {
                var data;
                var className = "default";
                if (row.status == 0) {
                    data = "NOT_RECEIVED"
                } else if (row.status == 1) {
                    data = "RECEIVED"
                } else if (row.status == 2) {
                    data = "RECEIVED_PARTIAL"
                } else if (row.status == 3) {
                    data = "INVOICE CANCELLED"
                }
                return '<span class="label label-' + className + '">' + ifNotValid(data, '') + '</span>';
            }
        }, {
            targets: 17,
            "visible": false,
            "data": "salesPerson"
        }, {
            targets: 18,
            "visible": false,
            "data": "model"
        }, {
            targets: 19,
            "visible": false,
            "data": "destinationCountry"
        }, {
            targets: 20,
            "visible": false,
            "data": "locationId"
        }, {
            targets: 21,
            "visible": false,
            "data": "customerId"
        }, {
            targets: 22,
            "visible": false,
            "data": "isBidding"
        }, {
            targets: 23,
            "visible": false,
            "data": "destinationPort"
        }],
        "footerCallback": function(row, data, start, end, display) {
            var tableApi = this.api();
            updateFooter(tableApi);
        },
        "drawCallback": function(settings, json) {
            $('#table-sales-summary').find('span.autonumber').autoNumeric('init')
        },
        /*excel export*/
        buttons: [{
            extend: 'excel',
            text: 'Export All',
            title: '',
            filename: function() {
                var d = new Date();
                return 'SalesSummaryReport_' + d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();
            },
            attr: {
                type: "button",
                id: 'dt_excel_export_all'
            },
            exportOptions: {
                columns: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]
            }
        }]

    });

    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    }
    ;// Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });
    // Date range picker
    var purchased_min;
    var purchased_max;
    $('#table-filter-purchased-date').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        purchased_min = picker.startDate;
        purchased_max = picker.endDate;
        picker.element.val(purchased_min.format('DD-MM-YYYY') + ' - ' + purchased_max.format('DD-MM-YYYY'));
        purchased_min = purchased_min._d.getTime();
        purchased_max = purchased_max._d.getTime();
        $(this).closest('.input-group').find('.clear-date').remove();
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
        $('#table-filter-purchased-date').val('');
        $(this).remove();

    })
    // Date range picker
    var sold_min;
    var sold_max;
    $('#table-filter-sold-date').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        sold_min = picker.startDate;
        sold_max = picker.endDate;
        picker.element.val(sold_min.format('DD-MM-YYYY') + ' - ' + sold_max.format('DD-MM-YYYY'));
        sold_min = sold_min._d.getTime();
        sold_max = sold_max._d.getTime();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.draw();
    });
    $('#soldDate-form-group').on('click', '.clear-date', function() {
        sold_min = '';
        sold_max = '';
        table.draw();
        $('#table-filter-sold-date').val('');
        $(this).remove();

    })
    //Export Excel
    $("#excel_export_all").on("click", function() {
        $("#groupselect").attr("checked", false);
        $(".selectBox").attr("checked", false);
        //table.rows('.selected').deselect();
        table.button("#dt_excel_export_all").trigger();

    });
    //customer filter
    $('#custId').select2({
        allowClear: true,
        minimumInputLength: 2,
        ajax: {
            url: myContextPath + "/customer/admin/search",
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
    });
    $.getJSON(myContextPath + "/user/getRoleSales", function(data) {
        var salesJson = data;
        var ele = $('#select_sales_staff');
        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                var staff = item.username + +(item.userId)
                return {
                    id: item.userId,
                    text: item.username + ' ' + '( ' + item.userId + ' )'
                };
            })
        }).val($(ele).attr('data-value')).trigger("change");
    })
    //office location
    $.getJSON(myContextPath + "/data/mOfficeLocation.json", function(data) {
        officeLocationJson = data;
        $('#sales-location-filter').select2({
            allowClear: true,
            width: '100%',
            data: $.map(officeLocationJson, function(item) {
                return {
                    id: item.code,
                    text: item.location
                };
            })
        }).val('').trigger('change');
    })
    //maker dropdown
    var makerFilterElement = $('#maker-filter');
    var modelFilterElement = $('#model-filter');
    $.getJSON(myContextPath + "/data/makers.json", function(data) {
        $(makerFilterElement).select2({
            allowClear: true,
            data: $.map(data, function(item) {
                return {
                    id: item.name,
                    text: item.name,
                    data: item
                };
            })
        });

    })
    makerFilterElement.on('change', function() {
        var data = makerFilterElement.select2('data')[0].data;
        modelFilterElement.empty();
        if (isEmpty(data)) {
            return;
        }
        $(modelFilterElement).select2({
            allowClear: true,
            data: $.map(data.models, function(item) {
                return {
                    id: item,
                    text: item
                };
            })
        }).val('').trigger('change');
    })
    //maker element
    $(modelFilterElement).select2({
        allowClear: true
    });
    //stock type
    $('#purchaseType').select2({
        allowClear: true
    });
    //Country Json
    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        countriesJson = data;
        $('#destinationCountry').select2({
            allowClear: true,
            width: '100%',
            data: $.map(countriesJson, function(item) {
                return {
                    id: item.country,
                    text: item.country
                };
            })
        }).val('').trigger('change');
    })
    //Country based Port Filter
    $('#destinationCountry').on('change', function() {
        var val = ifNotValid($(this).val(), '');
        destinationPort.empty();
        if (!isEmpty(val)) {
            var data = filterOneFromListByKeyAndValue(countriesJson, "country", val);
            if (data != null) {
                destinationPort.select2({
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
    });
    //Port Filter 
    var port;
    var destinationPort = $('#destinationPort');
    destinationPort.select2({
        allowClear: true,
        width: '100%',
    }).on('change', function() {
        port = $(this).val();
    })

    //Filter with Customer
    var customerName;
    $('.customer').change(function() {
        customerName = $(this).val();
        table.draw();

    });
    //Filter with Location
    var locationName;
    $('#sales-location-filter').change(function() {
        locationName = $(this).val();
        table.draw();

    });
    var stockType;
    $('#purchaseType').change(function() {
        stockType = $(this).val();
        table.draw();

    });
    var maker;
    $('#maker-filter').change(function() {
        maker = $(this).find('option:selected').val();
        table.draw();
    });
    var model;
    $('#model-filter').change(function() {
        model = $(this).find('option:selected').val();
        table.draw();
    });
    var countryFilter;
    $('#destinationCountry').change(function() {
        countryFilter = $(this).find('option:selected').val();
        table.draw();
    });
    var portFilter;
    $('#destinationPort').change(function() {
        portFilter = $(this).find('option:selected').val();
        table.draw();
    });
    var salesName;
    $('#select_sales_staff').change(function() {
        salesName = $(this).find('option:selected').val();
        table.draw();
    });
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {

        //orgin port filter

        if (typeof customerName != 'undefined' && customerName.length != '') {
            if (aData[21].length == 0 || aData[21] != customerName) {
                return false;
            }
        }
        if (typeof salesName != 'undefined' && salesName.length != '') {
            if (aData[17].length == 0 || aData[17] != salesName) {
                return false;
            }
        }
        if (typeof maker != 'undefined' && maker.length != '') {
            if (aData[2].length == 0 || aData[2] != maker) {
                return false;
            }
        }
        if (typeof model != 'undefined' && model.length != '') {
            if (aData[18].length == 0 || aData[18] != model) {
                return false;
            }
        }
        if (typeof countryFilter != 'undefined' && countryFilter.length != '') {
            if (aData[19].length == 0 || aData[19] != countryFilter) {
                return false;
            }
        }
        if (typeof portFilter != 'undefined' && portFilter.length != '') {
            if (aData[23].length == 0 || aData[23] != portFilter) {
                return false;
            }
        }
        if (typeof locationName != 'undefined' && locationName.length != '') {
            if (aData[20].length == 0 || aData[20] != locationName) {
                return false;
            }
        }
        if (typeof stockType != 'undefined' && stockType.length != '') {
            if (aData[22].length == 0 || aData[22] != stockType) {
                return false;
            }
        }
        //Purchase date filter
        if (typeof purchased_min != 'undefined' && purchased_min.length != '') {
            if (aData[9].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[9], 'DD/MM/YYYY')._d.getTime();
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
        //Sold date filter
        if (typeof sold_min != 'undefined' && sold_min.length != '') {
            if (aData[10].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[10], 'DD/MM/YYYY')._d.getTime();
            }
            if (sold_min && !isNaN(sold_min)) {
                if (aData._date < sold_min) {
                    return false;
                }
            }
            if (sold_max && !isNaN(sold_max)) {
                if (aData._date > sold_max) {
                    return false;
                }
            }

        }

        return true;
    })

    var modal_sales_details = $('#modal-sales-details');
    var row, modalTriggerEle;
    $(modal_sales_details).on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalTriggerEle = $(event.relatedTarget);
        var tr = $(modalTriggerEle).closest('tr');
        var rowData = table.row(tr).data();
        $(modal_sales_details).find('table>tbody>tr.clone').find("td.chassisNo").html(rowData["chassisNo"]);
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            url: myContextPath + "/accounts/sales-summary-list/" + rowData.stockNo,
            contentType: "application/json",
            success: function(data) {
                setSalesDetailsData(modal_sales_details, data);

            }
        });
        
    }).on('hidden.bs.modal', function() {
        $(this).find('input,select').val('').trigger('change');

    })

})

function setSalesDetailsData(element, data) {
    $(element).find('td.purchaseCost>span').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.purchaseCost, 0));
    $(element).find('td.purchaseCostTax>span').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.purchaseCostTax, 0));
    $(element).find('td.commision>span').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.commision, 0));
    $(element).find('td.commisionTax>span').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.commisionTax, 0));
    $(element).find('td.recycle>span').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.recycle, 0));
    $(element).find('td.roadTax>span').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.roadTax, 0));
    $(element).find('td.otherCharges>span').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.otherCharges, 0));
    $(element).find('td.otherChargesTax>span').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.otherChargesTax, 0));
    $(element).find('td.transport>span').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.transport, 0));
    $(element).find('td.freight>span').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.freight, 0));
    $(element).find('td.shipping>span').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.shipping, 0));
    $(element).find('td.insurance>span').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.insurance, 0));
    $(element).find('td.radiation>span').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.radiation, 0));
    $(element).find('td.inspection>span').autoNumeric('init').autoNumeric('set', ifNotValid(data.data.inspection, 0));

    updateModalFooter();

}

function updateModalFooter() {
    var totalTaxExcludedAmount = 0;
    var totalTaxAmount = 0;
    var totalTaxIncludedAmount = 0;
    $('#form-sales-summary').find('span.amount').each(function() {
        totalTaxExcludedAmount += Number($(this).autoNumeric('init').autoNumeric('get'));
    })
    $('#modal-sales-details').find('div.summary-container span.total').autoNumeric('init').autoNumeric('set', totalTaxExcludedAmount);

}
function updateFooter(table) {

    var intVal = function(i) {
        return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
    };
    var isValid = function(val) {
        return typeof val === 'undefined' || val == null ? 0 : val;
    }
    // page total
    // purchase cost total
    var pageSellingPriceTotal = table.column(6, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid($(b).find('span[name="exchangeRateSellingPrice"]').autoNumeric('init').autoNumeric('get')));
        return intVal(a) + amount;
    }, 0);
    // commission amount tax total
    var pageCostOfgoodsTotal = table.column(7, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid($(b).find('span[name="costOfGoods"]').autoNumeric('init').autoNumeric('get')));
        return intVal(a) + amount;
    }, 0);

    // Other amount total
    var pageProfitTotal = table.column(8, {
        page: 'current'
    }).nodes().reduce(function(a, b) {
        var amount = Number(isValid($(b).find('span[name="margin"]').autoNumeric('init').autoNumeric('get')));

        return intVal(a) + amount;
    }, 0);

    $('#table-sales-summary>tfoot>tr.sum').find('span.sellingPriceTotal').autoNumeric('init').autoNumeric('set', pageSellingPriceTotal);

    $('#table-sales-summary>tfoot>tr.sum').find('span.costOfgoodsTotal').autoNumeric('init').autoNumeric('set', pageCostOfgoodsTotal);

    $('#table-sales-summary>tfoot>tr.sum').find('span.profitTotal').autoNumeric('init').autoNumeric('set', pageProfitTotal);

}
