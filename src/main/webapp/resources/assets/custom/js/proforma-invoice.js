let countriesJson;
$(function() {
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })
    $.validator.addMethod("valid-fields", function(value, element, parms) {
        let row = $(element).closest('.row');
        let estimatedDeparture = $(row).find('input[name="estimatedDeparture"]').val();
        let estimatedArrival = $(row).find('input[name="estimatedArrival"]').val();

        if (isEmpty(estimatedDeparture) && isEmpty(estimatedArrival)) {
            return false;
        } else {
            return true;
        }
    }, "Fill Required Fileds");
    jQuery.validator.addClassRules('valid-required', {
        'required': true
    });
    $('#create-shipping-instruction-form').validate({
        highlight: function(element) {
            $(element).parent().addClass("has-error");
        },
        unhighlight: function(element) {
            $(element).parent().removeClass("has-error");
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
            'consigneeId': 'required',
            'destCountry': 'required',
            'destPort': 'required',
            'yard': 'required',
            'paymentType': 'required',
            'estimatedArrival': {
                'required': false,
                'valid-fields': true
            },
            'estimatedDeparture': {
                'required': false,
                'valid-fields': true
            }

        }
    });

})

$(function() {
    $.getJSON(myContextPath + "/data/sales-dashboard/status-count", function(data) {
        $('#inquiry-count').html(data.data.inquiry);
        $('#porforma-count').html(data.data.porforma);
        $('#reserved-count').html(data.data.reserved);
        $('#shipping-count').html(data.data.shipping);
        $('#sales-count').html(data.data.salesorder);
        $('#status-count').html(data.data.status);
    });
    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        countriesJson = data;
    })
    // Currency aSign
    $.getJSON(myContextPath + "/data/currency.json", function(data) {
        var currencyJson = data;
        $('#currency1,#currency2').select2({
            allowClear: true,
            width: '100%',
            data: $.map(currencyJson, function(item) {
                return {
                    id: item.currencySeq,
                    text: item.currency,
                    data: item
                };
            })
        }).val('').trigger('change');
    })

    var isAdminManagerValue = $('input[name="isAdminManager"]').val();
    isAdminManagerValue = (isAdminManagerValue == 1) ? true : false;
    var tableEle = $('#table-proformainvoice');
    var table = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": true,
        "order": [[1, "desc"]],
        //or asc 

        "ajax": {
            url: myContextPath + "/sales/proformainvoicelist-data",
            data: function(data) {
                if (!$('input[name="showMine"]').is(':checked')) {
                    data.flag = 0;
                } else {
                    data.flag = 1;
                }
                return data;
            }
        },
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
            "data": "invoiceNo",
            "visible": false,
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" type="checkbox" data-invoiceNo="' + row.invoiceNo + '" value="' + data + '">';
                }
                return data;
            }
        }, {
            targets: 1,
            "className": "details-control",
            "data": "date",

        }, {
            targets: 2,
            "className": "details-control",
            "data": "invoiceNo",

        }, {
            targets: 3,
            "className": "details-control",
            "data": "customerId",
            "render": function(data, type, row) {
                return row.firstName + ' ' + row.lastName;
            }
        }, {
            targets: 4,
            "className": "details-control",
            "data": "consigneeId",
            "render": function(data, type, row) {
                return row.cFirstName + ' ' + row.cLastName;
            }
        }, {
            targets: 5,
            "className": "details-control",
            "data": "notifypartyId",
            "render": function(data, type, row) {
                return !isEmpty(data) ? row.npFirstName : "";
            }
        }, {
            targets: 6,
            "className": "details-control",
            "data": "invoiceBy",
            "visible": isAdminManagerValue
        }, {
            targets: 7,
            "className": "details-control",
            "data": "paymentType"
        }, {
            targets: 8,
            "className": "dt-right details-control",
            "data": "total",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="' + row.currencyDetails.symbol + ' " data-m-dec="' + (row.currencyDetails.currencySeq != 2 ? 0 : 2) + '">' + data + '</span>';
            }
        }, {
            targets: 9,
            "data": "invoiceNo",
            "className": 'align-center',
            orderable: false,
            "render": function(data, type, row) {
                var html = ''
                html += '<a href="#" class="ml-5 btn btn-success btn-xs" id="btn-create-shipping" name="btn-create-shipping-instruction" title="Arrange shipping instruction" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-create-shipping"><i class="fa fa-ship"></i></a>'
                html += '<a href="#" class="ml-5 btn btn-warning btn-xs" id="btn-create-sales-order" name="btn-create-sales-order" title="Create Sales Order" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-create-sales"><i class="fa fa-shopping-cart"></i></a>'
                //html += '<a href="#" class="ml-5 btn btn-default btn-xs" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-download-invoice"><i class="fa fa-fw fa-download"></i></a>'
                html += '<a href="#" class="ml-5 btn btn-info btn-xs" data-backdrop="static" title="Edit Porforma Invoice" data-keyboard="false" data-toggle="modal" data-target="#modal-create-proforma"><i class="fa fa-edit"></i></a>'
                html += '<a class="ml-5 btn btn-danger btn-xs" title="Delete Porforma Invoice" name="delete_proformaInvoice"><i class="fa fa-close"></i></a>'
                html += '<a href="' + myContextPath + '/download/proforma/invoice/' + data + '.pdf" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-download"></i></a>'
                return html;
            }
        }, {
            targets: 10,
            orderable: false,
            "searchable": true,
            "visible": false,
            "data": "customerId"
        }],
        "drawCallback": function(settings, json) {
            $('span.autonumber').autoNumeric('init');

        }

    });
    //     var userName = $('#userInfo').text();
    //     $('#showMine').change(function() {
    //         if ($(this).is(':checked')) {
    //             $.fn.dataTable.ext.search.push(function(settings, aData, dataIndex) {
    //                 return aData[6].toLowerCase() === userName.toLowerCase()
    //             })
    //         } else {
    //             $.fn.dataTable.ext.search.pop()
    //         }
    //         table.draw()
    //     })

    $('#showMine').change(function() {
        table.ajax.reload()
    })
    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/$/g, "");
    }
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
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
            $(tableEle).find("th.select-checkbox>input").removeClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(tableEle).find("th.select-checkbox>input").addClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', true);

        }

    }).on("deselect", function() {
        if (table.rows({
            selected: true,
            page: 'current'
        }).count() !== table.rows({
            page: 'current'
        }).count()) {
            $(tableEle).find("th.select-checkbox>input").removeClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', false);
        } else {
            $(tableEle).find("th.select-checkbox>input").addClass("selected");
            $(tableEle).find("th.select-checkbox>input").prop('checked', true);

        }

    }).on("click", 'a[name="delete_proformaInvoice"]', function() {
        if (!confirm($.i18n.prop('common.confirm.update'))) {
            return false;
        }
        let tr = $(this).closest('tr')
        let row = table.row(tr)
        let data = row.data();
        response = deleteProformaInvoiceId(data.invoiceNo);
        if (response.status == "success") {
            table.ajax.reload()
        }
    })
    table.on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
        } else {
            showDetails(tr);
        }
    });

    function showDetails(tr) {
        // var tr = $(this).closest('tr');
        var row = table.row(tr)
        table.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
            var row = table.row(rowIdx);
            if (row.child.isShown()) {
                row.child.hide();
                tr.removeClass('shown');
            }
        })
        //row.child(format(row.data())).show();
        var detailsElement = format(row.data());
        row.child(detailsElement).show();
        $('span.autonumber').autoNumeric('init')
        tr.addClass('shown');
        tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
    }
    // Date range picker
    var purchased_min;
    var purchased_max;
    $('#table-filter-invoice-date').daterangepicker({
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
        $('#table-filter-invoice-date').val('');
        $(this).remove();

    })
    //filter By Customer Search
    $('#custselectId').select2({
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
    var filterCustomer = $('#custselectId').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        //date filter
        if (typeof purchased_min != 'undefined' && purchased_min.length != '') {
            if (aData[1].length == 0) {
                return false;
            }
            if (typeof aData._date == 'undefined') {
                aData._date = moment(aData[1], 'DD-MM-YYYY')._d.getTime();
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
            if (aData[10].length == 0 || aData[10] != filterCustomer) {
                return false;
            }
        }
        return true;
    });

    var modalDownloadInvoice = $('#modal-download-invoice');
    var targetEle;
    modalDownloadInvoice.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        targetEle = e.relatedTarget;

        $('#currency1').on('change', function() {
            var selectedVal = $(this).val();
            if (!isEmpty(selectedVal)) {
                $('#bankOne').removeClass('hidden');
                // Bank Json
                $.getJSON(myContextPath + "/data/bankOnCurrencyType.json?currencyType=" + parseInt(selectedVal), function(data) {
                    var bankJson = data;
                    $('#bank1').select2({
                        allowClear: true,
                        width: '100%',
                        data: $.map(bankJson, function(item) {
                            return {
                                id: item.bankSeq,
                                text: item.bankName,
                                data: item
                            };
                        })
                    }).val('').trigger('change');
                })
            }
        })

        $('#currency2').on('change', function() {
            var selectedVal = $(this).val();
            if (!isEmpty(selectedVal)) {
                $('#bankTwo').removeClass('hidden');
                // Bank Json
                $.getJSON(myContextPath + "/data/bankOnCurrencyType.json?currencyType=" + parseInt(selectedVal), function(data) {
                    var bankJson = data;
                    $('#bank2').select2({
                        allowClear: true,
                        width: '100%',
                        data: $.map(bankJson, function(item) {
                            return {
                                id: item.bankSeq,
                                text: item.bankName,
                                data: item
                            };
                        })
                    }).val('').trigger('change');
                })
            }
        })
    }).on('hidden.bs.modal', function() {
        $('#bankOne').addClass('hidden');
        $('#bankTwo').addClass('hidden');
        $('#currency1,#currency2').val('').trigger('change')
    }).on('click', "#btn-download-invoice", function(event) {
        var rowdata = table.row(targetEle.closest('tr')).data();
        var data = {};
        //data[currency1] = modalDownloadInvoice.find()
        data = getFormData($("#modal-download-invoice").find('.form-control'));
        var queryString = "?bank1=" + data.bank1 + "&bank2=" + data.bank2 + "&invoiceNo=" + rowdata.invoiceNo + "&currency1=" + data.currency1 + "&currency2=" + data.currency2
        $.redirect(myContextPath + "/download/proforma/invoice/pdf" + queryString, '', 'GET')
        modalDownloadInvoice.modal('toggle');
    })
    $.fn.outerHTML = function() {
        return $(this).clone().wrap('<div></div>').parent().html();
    }
    function format(rowData) {
        var ifNotValid = function(val, str) {
            return typeof val === 'undefined' || val == null ? str : val;
        }
        var currencySymbol = isEmpty(rowData.currencyDetails) ? '' : ifNotValid(rowData.currencyDetails.symbol, '');

        var tbody = '';
        for (var i = 0; i < rowData.items.length; i++) {

            let status = rowData.items[i].status == 5 ? "Created" : "idle"
            let className = rowData.items[i].status == 5 ? "label-success" : "label-warning"

            var td = '<td class="align-center s-no"><input type="checkbox" name="selectBox" /></td>';
            td += '<td class="align-center"><input type="hidden" name="stockNo" value="' + ifNotValid(rowData.items[i].stockNo, '') + '"/>' + ifNotValid(rowData.items[i].stockNo, '') + '</td>';
            td += '<td class="align-center">' + ifNotValid(rowData.items[i].model, '') + '</td>';
            td += '<td class="align-center">' + ifNotValid(rowData.items[i].chassisNo, '') + '</td>';
            td += '<td class="align-center">' + ifNotValid(rowData.items[i].firstRegDate, '') + '</td>';
            td += '<td class="align-center">' + ifNotValid(rowData.items[i].hsCode, '') + '</td>';
            td += '<td class="dt-right"><span class="autonumber" data-a-sign="' + ifNotValid(rowData.currencyDetails.symbol) + ' " data-m-dec="' + (rowData.currencyDetails.currencySeq != 2 ? 0 : 2) + '">' + ifNotValid(rowData.items[i].fob, 0) + '</span></td>';
            td += '<td class="dt-right"><span class="autonumber" data-a-sign="' + ifNotValid(rowData.currencyDetails.symbol) + ' " data-m-dec="' + (rowData.currencyDetails.currencySeq != 2 ? 0 : 2) + '">' + ifNotValid(rowData.items[i].insurance, 0) + '</span></td>';
            td += '<td class="dt-right"><span class="autonumber" data-a-sign="' + ifNotValid(rowData.currencyDetails.symbol) + ' " data-m-dec="' + (rowData.currencyDetails.currencySeq != 2 ? 0 : 2) + '">' + ifNotValid(rowData.items[i].shipping, 0) + '</span></td>';
            td += '<td class="dt-right"><span class="autonumber" data-a-sign="' + ifNotValid(rowData.currencyDetails.symbol) + ' " data-m-dec="' + (rowData.currencyDetails.currencySeq != 2 ? 0 : 2) + '">' + ifNotValid(rowData.items[i].freight, 0) + '</span></td>';
            td += '<td class="dt-right"><span class="label ' + className + '">' + status + '</span></td>';
            let tr = $('<tr>' + td + '</tr>').attr('data-json', JSON.stringify(rowData.items[i]));
            tbody += tr.outerHTML();

        }
        var tfoot = '<tr>';
        tfoot += '<td class="align-center" style="width: 50px;text-align: center;font-weight: bold;" colspan="6">Total</td>';
        tfoot += '<td style="width: 60px" class="dt-right bg-ghostwhite"><span class="autonumber" data-a-sign="' + ifNotValid(rowData.currencyDetails.symbol, '') + '" data-m-dec="' + (rowData.currencyDetails.currencySeq != 2 ? 0 : 2) + '">' + ifNotValid(rowData.fobTotal, 0) + '</span></td>';
        tfoot += '<td style="width: 60px" class="dt-right bg-ghostwhite"><span class="autonumber" data-a-sign="&yen; " data-m-dec="' + (rowData.currencyDetails.currencySeq != 2 ? 0 : 2) + '">' + ifNotValid(rowData.insuranceTotal, 0) + '</span></td>';
        tfoot += '<td style="width: 60px" class="dt-right bg-ghostwhite"><span class="autonumber" data-a-sign="&yen; " data-m-dec="' + (rowData.currencyDetails.currencySeq != 2 ? 0 : 2) + '">' + ifNotValid(rowData.shippingTotal, 0) + '</span></td>';
        tfoot += '<td style="width: 60px" class="dt-right bg-ghostwhite"><span class="autonumber" data-a-sign="&yen; " data-m-dec="' + (rowData.currencyDetails.currencySeq != 2 ? 0 : 2) + '">' + ifNotValid(rowData.freightTotal, 0) + '</span></td>';
        tfoot += '<td class="align-center" style="width: 50px;text-align: center;font-weight: bold;"></td>';
        tfoot += '</tr>';
        var html = '<div class="box-body no-padding bg-darkgray"><div class="order-item-container"><input type="hidden" name="invoiceOrderNo" value="' + rowData.invoiceNo + '"/>' + '    <table class="table table-bordered" id="#proforma-invoice-element">' + '<thead><tr><th class="align-center bg-ghostwhite" style="width: 10px">#</th><th style="width: 60px" class="align-center bg-ghostwhite">Stock No</th><th style="width: 100px" class="align-center bg-ghostwhite">Maker/Model</th><th style="width: 100px" class="align-center bg-ghostwhite">Chasis No</th><th class="align-center bg-ghostwhite">Year</th><th class="align-center bg-ghostwhite" style="width: 70px">HS Code</th><th class="align-center bg-ghostwhite">FOB</th><th class="align-center bg-ghostwhite">Insurance</th><th class="align-center bg-ghostwhite">Inspection</th><th class="align-center bg-ghostwhite">Freight</th><th class="align-center bg-ghostwhite">Sales Order</th></tr></thead><tbody>' + tbody + '</tbody><tfoot>' + tfoot + '</tfoot></table></div></div>';
        return html;
    }
    //Model Show 
    $.getJSON(myContextPath + "/data/currency.json", function(data) {
        //         currencyJson = data;
        $('#currencyType,#currencySalesType').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.currencySeq,
                    text: item.currency,
                    data: item
                };
            })
        })
    })
    var modalCreateProforma = $('#modal-create-proforma');
    modalCreateProforma.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }

        var rowdata = table.row(e.relatedTarget.closest('tr')).data();
        $(this).find('input[name="invoiceId"]').val(rowdata.invoiceNo);
        var element;
        var i = 0;
        for (let j = 0; i < rowdata.items.length; j++) {
            var data = rowdata.items[i];
            var stockNo = ifNotValid(data.stockNo, '');
            var maker = ifNotValid(data.maker, '');
            var model = ifNotValid(data.model, '');
            var chassisNo = ifNotValid(data.chassisNo, '');
            var firstRegDate = ifNotValid(data.firstRegDate, '');
            var hsCode = ifNotValid(data.hsCode, '');
            var fob = ifNotValid(data.fob, 0);
            var insurance = ifNotValid(data.insurance, 0);
            var shipping = ifNotValid(data.shipping, 0);
            var freight = ifNotValid(data.freight, 0);
            var total = ifNotValid(data.total, 0);
            var vehicleCategoryCode = ifNotValid(data.vehicleCategoryCode, 0);

            element = $('#item-invoice-clone').find('.item-invoice').clone();
            $(element).appendTo('#item-invoice-clone-container');

            $(element).find('input[name="stockNo"]').val(stockNo);
            $(element).find('input[name="maker"]').val(maker);
            $(element).find('input[name="model"]').val(model);
            $(element).find('input[name="chassisNo"]').val(chassisNo);
            $(element).find('input[name="firstRegDate"]').val(firstRegDate);
            $(element).find('input[name="hsCode"]').val(ifNotValid(data.hsCode, ''));
            $(element).find('input[name="fob"]').autoNumeric('init').autoNumeric('set', fob)
            $(element).find('input[name="insurance"]').autoNumeric('init').autoNumeric('set', insurance)
            $(element).find('input[name="shipping"]').autoNumeric('init').autoNumeric('set', shipping)
            $(element).find('input[name="freight"]').autoNumeric('init').autoNumeric('set', freight)
            $(element).find('input[name="total"]').autoNumeric('init').autoNumeric('set', total)
            $(element).find('.calculation').trigger('keyup');
            i++;
        }

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

        $(this).find('select[name="consigneeId"]').val(rowdata.consigneeId).trigger("change");
        $(this).find('select[name="notifypartyId"]').val(rowdata.notifypartyId).trigger("change");
        $(this).find('input.autonumber').autoNumeric('init');

    }).on('hidden.bs.modal', function() {
        $(this).find('#item-invoice-clone-container').html('');
        $(this).find("input,textarea,select").val([]);
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
        var currencyEle = modalCreateProforma.find('#currencyType')
        if (!isEmpty(data)) {
            currencyEle.val(data.currencyType).trigger('change').addClass('readonly');
        } else {
            currencyEle.val('').trigger('change');
        }
        //payment type
        var data = $(this).select2('data')[0].data;
        var paymentEle = modalCreateProforma.find('#paymentType')
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
                        width: '150px',
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

    }).on('change', 'select[name="paymentType"]', function() {
        var paymentType = $(this).val();
        if (paymentType == "FOB") {
            $('#proforma-invoice-container').find('input[name="freight"]').val(0).prop('disabled', true);
            //             $('#proforma-invoice-container').find('input[name="freight"]').val(0);
            $('#proforma-invoice-container').find('input[name="freightTotal"]').val(0);
        } else {
            $('#proforma-invoice-container').find('input[name="freight"]').prop('disabled', false);
        }
        if (paymentType === "C&F") {
            $('#proforma-invoice-container').find('input[name="insurance"]').val(0).prop('disabled', true);
            $('#proforma-invoice-container').find('input[name="insuranceTotal"]').val(0)

        } else {
            $('#proforma-invoice-container').find('input[name="insurance"]').prop('disabled', false);
        }
    }).on('keyup', "input.calculation", function() {
        doCalculation(this)
    }).on('change', 'input[name="total"]', function() {
        doCalculation(this)
    }).on('click', '#btn-save-proforma-invoice', function() {
        if (!$('#edit-proforma-form').find('input,select,textarea').valid()) {
            return;
        }
        var autoNumericElements = $('#modal-create-proforma').find('input.autonumber');
        autoNumericSetRawValue(autoNumericElements)
        var objectArr = [];
        var data = {};
        data = getFormData($("#proforma-invoice-container").find('.invoiceData'));
        //         object = $('#item-invoice-container').find('.item-invoice').serialize();
        $("#item-invoice-container").find('.item-invoice').each(function() {
            object = getFormData($(this).find('input,select,textarea'));
            objectArr.push(object);
        });
        data['items'] = objectArr.filter(value=>JSON.stringify(value) !== '{}');
        let invoiceId = modalCreateProforma.find('input[name="invoiceId"]').val();
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/invoice/proforma/invoice/edit?invoiceId=" + invoiceId,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    table.ajax.reload();
                    modalCreateProforma.modal('toggle');
                }

            }
        });
    }).on('change', '#currencyType', function() {
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
    function doCalculation(element) {
        var closestEle = $(element).closest('.row');
        var fob = Number(getAutonumericValue($(closestEle).find('input[name="fob"]')));
        var insurance = Number(getAutonumericValue($(closestEle).find('input[name="insurance"]')));
        var freight = Number(getAutonumericValue($(closestEle).find('input[name="freight"]')));
        var shipping = Number(getAutonumericValue($(closestEle).find('input[name="shipping"]')));
        var total = fob + insurance + freight + shipping;
        //setAutonumericValue($(closestEle).find('input[name="total"]'), total);

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
        $('#proforma-invoice-container #item-invoice-container').find('input[name="fob"]').each(function(index) {
            fobTotal += Number(getAutonumericValue($(this)))
        })
        var insuranceTotal = 0;
        $('#proforma-invoice-container #item-invoice-container').find('input[name="insurance"]').each(function(index) {
            insuranceTotal += Number(getAutonumericValue($(this)))
        })
        var freightTotal = 0;
        $('#proforma-invoice-container #item-invoice-container').find('input[name="freight"]').each(function(index) {
            freightTotal += Number(getAutonumericValue($(this)))
        })
        var shippingTotal = 0;
        $('#proforma-invoice-container #item-invoice-container').find('input[name="shipping"]').each(function(index) {
            shippingTotal += Number(getAutonumericValue($(this)))
        })
        var amountTotal = 0;
        $('#proforma-invoice-container #item-invoice-container').find('input[name="total"]').each(function(index) {
            amountTotal += Number(getAutonumericValue($(this)))
        })
        setAutonumericValue($('#proforma-invoice-container #fobTotal'), fobTotal)
        setAutonumericValue($('#proforma-invoice-container #insuranceTotal'), insuranceTotal)
        setAutonumericValue($('#proforma-invoice-container #freightTotal'), freightTotal)
        setAutonumericValue($('#proforma-invoice-container #shippingTotal'), shippingTotal)
        setAutonumericValue($('#proforma-invoice-container #grand-total'), amountTotal)
    }
    //on open Shipping instruction modal
    var modalArangeShippingBtn, salesInvoiceData;
    $('#modal-create-shipping').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        modalArangeShippingBtn = $(e.relatedTarget);

        salesInvoiceData = table.row(modalArangeShippingBtn.closest('tr')).data();

        let items = [];
        var row = table.row(modalArangeShippingBtn.closest('tr'));
        if (row.child.isShown()) {
            let selectedRow = row.child().find('div.order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:checked')
            if (selectedRow.length > 0) {

                $.each(selectedRow, function(item) {
                    let data = $(this).closest('tr').attr('data-json');
                    data = JSON.parse(data);
                    items.push(data);
                })

            } else {
                let selectedRow = row.child().find('div.order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:unChecked')
                $.each(selectedRow, function(item) {
                    let data = $(this).closest('tr').attr('data-json');
                    data = JSON.parse(data);
                    items.push(data);
                })
            }

        } else {
            showDetails(modalArangeShippingBtn.closest('tr'));

            var row = table.row(modalArangeShippingBtn.closest('tr'));
            let selectedRow = row.child().find('div.order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:unChecked')

            $.each(selectedRow, function(item) {
                let data = $(this).closest('tr').attr('data-json');
                data = JSON.parse(data);
                items.push(data);
            })
        }

        //check valid
        for (var i = 0; i < items.length; i++) {
            if (items[i].shippingInstructionStatus == 1) {
                if (items.length == 1) {
                    alert($.i18n.prop('alert.stock.reserve.shipping.instruction.already.arranged.single'));
                } else {
                    alert($.i18n.prop('alert.stock.reserve.shipping.instruction.already.arranged.multiple'));
                }

                return e.preventDefault();
            }
        }

        var element;
        var i = 0;
        items.forEach(function(data) {
            // var data = table.row(this).data();
            var stockNo = ifNotValid(data.stockNo, '');
            var chassisNo = ifNotValid(data.chassisNo, '');
            var customerId = ifNotValid(salesInvoiceData.customerId, '');
            var customerName = ifNotValid(salesInvoiceData.firstName, '');
            var customerLastName = ifNotValid(salesInvoiceData.lastName, '');

            if (i != 0) {
                element = $('#item-shipping-clone').find('.item-shipping').clone();
                element.find('input[name="estimatedType"]').attr('name', 'estimatedType' + i);
                element.find('input[name="estimatedType' + i + '"][value="0"]').iCheck('check').trigger('change')
                $(element).appendTo('#item-shipping-clone-container');
            } else {
                element = $('#item-shipping-container').find('.item-shipping');
            }
            $(element).find('input[type="radio"].estimated-data.minimal').iCheck({
                checkboxClass: 'icheckbox_minimal-blue',
                radioClass: 'iradio_minimal-blue'
            })
            $(element).find('.datePicker').datepicker({
                format: "mm/yyyy",
                autoclose: true,
                viewMode: "months",
                minViewMode: "months"
            })
            $(element).find('input[name="stockNo"]').val(stockNo);
            $(element).find('input[name="chassisNo"]').val(chassisNo);
            $(element).find('input[name="customerId"]').val(customerId);
            $(element).find('input[name="customernameId"]').val(customerName + ' ' + customerLastName);
            i++;
            $(element).find('.estimatedDate').addClass('hidden');
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
                        $(element).find('#cFirstshippingName').select2({
                            allowClear: true,
                            //                             width: '100%',
                            data: $.map(consArray, function(consigneeNotifyparties) {
                                return {
                                    id: consigneeNotifyparties.id,
                                    text: consigneeNotifyparties.cFirstName + ' ' + consigneeNotifyparties.cLastName
                                };
                            })
                        }).val(selectedVal).trigger("change");
                        let npArray = data.data.consigneeNotifyparties.filter(function(index) {
                            return !isEmpty(index.npFirstName);
                        })
                        $(element).find('#npFirstshippingName').select2({
                            allowClear: true,
                            //                             width: '100%',
                            data: $.map(npArray, function(consigneeNotifyparties) {
                                return {
                                    id: consigneeNotifyparties.id,
                                    text: consigneeNotifyparties.npFirstName + ' ' + consigneeNotifyparties.npLastName
                                };
                            })
                        }).val(selectedVal).trigger("change");
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
                        }).val(data.data.country).trigger('change');
                        $(element).find('select[name="destPort"]').val(data.data.port).trigger('change');
                        $(element).find('select[name="yard"]').val(data.data.yard).trigger('change');
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
        });

        //Slim Scroll 
        $('#item-shipping-container').slimScroll({
            start: 'bottom',
            height: ''
        });

        //Create Shipping Instruction
        $('#item-shipping-container').find('.select2-select').select2({
            allowClear: true,
            //             width: '100%',
        })

    }).on('change', 'select[name="destCountry"]', function() {
        var closestEle = $(this).closest('.item-shipping');
        var country = $(this).find('option:selected').val();
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

    }).on('hidden.bs.modal', function() {
        $(this).find('#item-shipping-clone-container').html('');
        $(this).find("input,textarea,select").val([]);
    }).on('ifChecked', '.estimated-data', function() {
        if ($(this).val() == 0) {
            $(this).closest('.estimatedData').find('.estimatedDate').addClass('hidden');
        } else if ($(this).val() == 1) {
            $(this).closest('.estimatedData').find('.estimatedDate').addClass('hidden');
        } else if ($(this).val() == 2) {
            $(this).closest('.estimatedData').find('.estimatedDate').removeClass('hidden');
        }

    }).on('click', '#btn-save-shipping', function(event) {
        if (!$('#create-shipping-instruction-form').find('input,select,textarea').valid()) {
            return;
        }
        var objectArr = [];
        $("#item-shipping-container").find('.item-shipping').each(function() {
            var data = {};
            object = getFormData($(this).find('input,select,textarea'));
            estimatedTypeEle = $(this).find('.estimated-data')
            var estimatedType = '';
            for (var i = 0; i < estimatedTypeEle.length; i++) {
                if (estimatedTypeEle[i].checked) {
                    estimatedType = estimatedTypeEle[i].value;
                }
            }
            data.stockNo = object.stockNo;
            data.notifypartyId = isEmpty(object.notifypartyId) ? null : object.notifypartyId;
            data.scheduleType = estimatedType;
            data.destPort = object.destPort;
            data.destCountry = object.destCountry;
            data.customernameId = object.customernameId;
            data.customerId = object.customerId;
            data.consigneeId = object.consigneeId;
            data.chassisNo = object.chassisNo;
            data.yard = object.yard;
            objectArr.push(data);
        });

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            async: true,
            type: "post",
            data: JSON.stringify(objectArr),
            url: myContextPath + "/invoice/shipping/order/save",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    $('#modal-create-shipping').modal('toggle');
                    $.redirect(myContextPath + '/sales/status', '', 'GET');
                    localStorage.setItem("aaj-sales-dashboard-active-nav", '3');
                }

            }
        });
    })

    $('#paymentType').select2({
        width: '100%',
        allowClear: true,
    });
    //Create Shipping Instruction
    $('#item-shipping-container').find('select.select2,select.select2-select').select2({
        allowClear: true,
        width: '100%',
    })
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

    var modalCreateSalesBtn, salesOrderData;
    $('#modal-create-sales').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }

        modalCreateSalesBtn = $(e.relatedTarget);

        salesOrderData = table.row(modalCreateSalesBtn.closest('tr')).data();

        let items = [];
        var row = table.row(modalCreateSalesBtn.closest('tr'));
        if (row.child.isShown()) {
            let selectedRow = row.child().find('div.order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:checked')
            if (selectedRow.length > 0) {

                $.each(selectedRow, function(item) {
                    let data = $(this).closest('tr').attr('data-json');
                    data = JSON.parse(data);
                    items.push(data);
                })

            } else {
                let selectedRow = row.child().find('div.order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:unChecked')
                $.each(selectedRow, function(item) {
                    let data = $(this).closest('tr').attr('data-json');
                    data = JSON.parse(data);
                    items.push(data);
                })
            }

        } else {
            showDetails(modalCreateSalesBtn.closest('tr'));

            var row = table.row(modalCreateSalesBtn.closest('tr'));
            let selectedRow = row.child().find('div.order-item-container table>tbody>tr:not(tr.hide)').find('input[type="checkbox"]:unChecked')

            $.each(selectedRow, function(item) {
                let data = $(this).closest('tr').attr('data-json');
                data = JSON.parse(data);
                items.push(data);
            })
        }

        //check valid
        for (var i = 0; i < items.length; i++) {
            if (items[i].status == 5) {
                if (items.length == 1) {
                    alert($.i18n.prop('alert.porforma.invoice.sales.order.single'));
                } else {
                    alert($.i18n.prop('alert.porforma.invoice.sales.order.multiple'));
                }

                return e.preventDefault();
            }
        }

        let custdfdata;
        var element;
        var i = 0;
        items.forEach(function(data) {
            var stockNo = ifNotValid(data.stockNo, '');
            var maker = ifNotValid(data.maker, '');
            var model = ifNotValid(data.model, '');
            var chassisNo = ifNotValid(data.chassisNo, '');
            var firstRegDate = ifNotValid(data.firstRegDate, '');
            var price = ifNotValid(data.total, '');
            var custName = ifNotValid(data.firstName, '');
            var custLastName = ifNotValid(data.lastName, '');
            var customerId = ifNotValid(data.customerId, '');

            if (i != 0) {
                element = $('#item-sales-clone').find('.item-sales').clone();
                $(element).appendTo('#item-sales-clone-container');
            } else {
                element = $('#item-sales-container').find('.item-sales');
            }
            $(element).find('input[name="stockNo"]').val(stockNo);
            $(element).find('input[name="maker"]').val(maker);
            $(element).find('input[name="model"]').val(model);
            $(element).find('input[name="chassisNo"]').val(chassisNo);
            $(element).find('input[name="firstRegDate"]').val(firstRegDate);
            $(element).find('input[name="total"]').autoNumeric('init').autoNumeric('set', price)

            i++;
        });
        $('#sales-details').find('#customernameSalesId').val(salesOrderData.firstName + ' ' + salesOrderData.lastName);
        $('#sales-details').find('#customerSalesId').val(salesOrderData.customerId);
        $(this).find('input.autonumber').autoNumeric('init');
        var customerId = ifNotValid(salesOrderData.customerId, '');
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
            success: function(custdfdata) {
                if (custdfdata.status == 'success') {
                    var selectedVal = '';
                    if (custdfdata.data.consigneeNotifyparties.length == 1) {
                        selectedVal = custdfdata.data.consigneeNotifyparties[0].id;
                    }
                    let consArray = custdfdata.data.consigneeNotifyparties.filter(function(index) {
                        return !isEmpty(index.cFirstName);
                    })
                    $('#cFirstsalesName').empty();
                    $('#cFirstsalesName').select2({
                        allowClear: true,
                        width: '150px',
                        data: $.map(consArray, function(consigneeNotifyparties) {
                            return {
                                id: consigneeNotifyparties.id,
                                text: consigneeNotifyparties.cFirstName + ' ' + consigneeNotifyparties.cLastName
                            };
                        })
                    }).val(selectedVal).trigger("change");

                    let npArray = custdfdata.data.consigneeNotifyparties.filter(function(index) {
                        return !isEmpty(index.npFirstName);
                    })

                    $('#npFirstsalesName').empty();
                    $('#npFirstsalesName').select2({
                        allowClear: true,
                        width: '150px',
                        data: $.map(npArray, function(consigneeNotifyparties) {
                            return {
                                id: consigneeNotifyparties.id,
                                text: consigneeNotifyparties.npFirstName + ' ' + consigneeNotifyparties.npLastName
                            };
                        })
                    }).val(selectedVal).trigger("change");

                    var currencyEle = $('#currencySalesType')
                    if (!isEmpty(custdfdata)) {
                        currencyEle.val(custdfdata.data.currencyType).trigger('change').addClass('readonly');
                    } else {
                        currencyEle.val('').trigger('change');
                    }

                    var paymentTypeEle = $('#paymentSalesType')
                    if (!isEmpty(custdfdata)) {
                        paymentTypeEle.val(custdfdata.data.paymentType).trigger('change');
                    } else {
                        paymentTypeEle.val('').trigger('change');
                    }
                }
            },
            error: function(e) {
                console.log("ERROR: ", e);

            }
        });

        var amountTotal = 0;
        $('#item-sales-container').find('input[name="total"]').each(function(index) {
            amountTotal += Number($(this).autoNumeric('get'))
        })
        $('#grand-total-sales').autoNumeric('set', amountTotal);
        //$(this).find('.calculation').trigger('keyup');
    }).on('hidden.bs.modal', function() {
        $(this).find('#item-sales-clone-container').html('');
        $(this).find("input,textarea,select").val([]);
    });

    $('#currencySalesType').on('change', function() {
        var data = $(this).select2('data')[0].data;
        if (!isEmpty(data)) {
            if (data.currency == "Yen") {
                $('#modal-create-sales').find('input.autonumber').autoNumeric('init').autoNumeric('update', {
                    aSign: data.symbol + ' ',
                    mDec: 0
                });
            } else {
                $('#modal-create-sales').find('input.autonumber').autoNumeric('init').autoNumeric('update', {
                    aSign: data.symbol + ' ',
                    mDec: 2
                });
            }
        }
    })

    //Total Calculation FOB,Freight,Insurance ,Total Amount 
    $('#item-sales-container').on('keyup', ".calculation", function() {
        var closestEle = $(this).closest('.row');

        var fob = Number($(closestEle).find('input[name="fob"]').autoNumeric('get'));
        var insurance = Number($(closestEle).find('input[name="insurance"]').autoNumeric('get'));
        var freight = Number($(closestEle).find('input[name="freight"]').autoNumeric('get'));
        var shipping = Number($(closestEle).find('input[name="shipping"]').autoNumeric('get'));
        var totalEle = $(closestEle).find('input[name="total"]');
        var total = fob + insurance + freight + shipping;
        //$(totalEle).val(total);
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
            $(this).autoNumeric('set', 0).trigger('keyup');
            return;
        }
        var fobEle = $(closestEle).find('input[name="fob"]');
        $(fobEle).autoNumeric('set', fob);
        //total
        var fobTotal = 0;
        $('#item-sales-container').find('input[name="fob"]').each(function(index) {
            fobTotal += Number($(this).autoNumeric('get'))
        })
        var insuranceTotal = 0;
        $('#item-sales-container').find('input[name="insurance"]').each(function(index) {
            insuranceTotal += Number($(this).autoNumeric('get'))
        })
        var freightTotal = 0;
        $('#item-sales-container').find('input[name="freight"]').each(function(index) {
            freightTotal += Number($(this).autoNumeric('get'))
        })
        var shippingTotal = 0;
        $('#item-sales-container').find('input[name="shipping"]').each(function(index) {
            shippingTotal += Number($(this).autoNumeric('get'))
        })
        var amountTotal = 0;
        $('#item-sales-container').find('input[name="total"]').each(function(index) {
            amountTotal += Number($(this).autoNumeric('get'))
        })

        $('#fobsalesTotal').autoNumeric('set', fobTotal);
        $('#insurancesalesTotal').autoNumeric('set', insuranceTotal);
        $('#freightsalesTotal').autoNumeric('set', freightTotal);
        $('#shippingsalesTotal').autoNumeric('set', shippingTotal);
        $('#grand-total-sales').autoNumeric('set', amountTotal);

    })

    // Create Sales Invoice Order

    $('#btn-generate-sales-invoice').on('click', function() {
        if (!$('#create-sales-form').valid()) {
            return;
        }
        var autoNumericElements = $('#modal-create-sales').find('input.autonumber');
        autoNumericSetRawValue(autoNumericElements)
        var objectArr = [];
        var data = {};
        var invoiceDtl = getFormData($("#sales-invoice-container").find('.salesData'));
        var object;
        $("#item-sales-container").find('.item-sales').each(function() {
            object = {};
            object = getFormData($(this).find('input,select,textarea'));
            objectArr.push(object);
        });
        data['invoiceItems'] = objectArr;
        data['invoiceDtl'] = invoiceDtl;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/invoice/sales/order/save",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    $('#modal-create-sales').modal('toggle')
                    table.ajax.reload();

                } else {
                    $('#modal-create-sales').modal('toggle')
                    $('#alert-block').removeClass('alert-success').addClass('alert-danger');
                    $('#alert-block').css('display', 'block').html('<strong>Failed!</strong> ' + data.message);
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                }

            }
        });
    })

    $('#paymentSalesType').on('change', function() {
        var closestEle = $(this).closest('.row');
        var id = $(this).val();
        if (id == "FOB") {
            $('#item-sales-container').find('input[name="freight"]').prop('disabled', true);
            // $('#item-sales-container').find('input[name="freight"]').val(0);
            // $('#totalSalesDiv').find('input[name="freightTotal"]').val(0);

        } else {
            $('#item-sales-container').find('input[name="freight"]').prop('disabled', false);

        }
    })
    $('#item-sales-container').slimScroll({
        start: 'bottom',
        height: ''
    });

    $('#sales-invoice-container').on('change', 'select[name="paymentType"]', function() {
        var paymentType = $(this).val();
        if (paymentType === "C&F") {
            $('#item-sales-container').find('input[name="insurance"]').prop('readonly', true);
            //$('#item-sales-container').find('input[name="insurance"]').val(0);
            //$('#totalSalesDiv').find('input[name="insuranceTotal"]').val(0);
        } else {
            $('#item-sales-container').find('input[name="insurance"]').prop('readonly', false);
        }

    })

    let NP_add_element = $('#add-ntfy-party')
    NP_add_element.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(event.relatedTarget);
        var customerId = $(targetElement).closest('div.item-shipping').find('input#customerId').val();
        $(this).find('input[name="code"]').val(customerId);
        // modalAddElementTriggerEle = $(event.relatedTarget);
    }).on('hidden.bs.modal', function() {
        $(this).find('span.help-block').html('')
        $(this).find('.has-error').removeClass('has-error')
        $(this).find('input,select').val('').trigger('change');
    }).on('click', 'button#save-NP', function() {
        let isValid = $('#formAddNotifyParty').find('input[name="notifyPartyName"], input[name="notifyPartyAddr"]').valid()
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

                                $('#modal-create-shipping').find('select[name="notifypartyId"]').empty();
                                $('#modal-create-shipping').find('select[name="notifypartyId"]').select2({
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

    let consignee_add_element = $('#add-consigner')
    consignee_add_element.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(event.relatedTarget);
        var customerId = $(targetElement).closest('div.item-shipping').find('input#customerId').val();
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
                                $('#modal-create-shipping').find('select[name="consigneeId"]').empty();
                                $('#modal-create-shipping').find('select[name="consigneeId"]').select2({
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

    let Sales_NP_add_element = $('#add-sales-ntfy-party')
    Sales_NP_add_element.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(event.relatedTarget);
        var customerId = salesOrderData.customerId;
        //$('#modal-create-sales').find('input#customernameSalesId').val();
        $(this).find('input[name="code"]').val(customerId);
        // modalAddElementTriggerEle = $(event.relatedTarget);
    }).on('hidden.bs.modal', function() {
        $(this).find('span.help-block').html('')
        $(this).find('.has-error').removeClass('has-error')
        $(this).find('input,select').val('').trigger('change');
    }).on('click', 'button#save-sales-NP', function() {
        let isValid = $('#formSalesAddNotifyParty').find('input[name="salesNotifyPartyName"], input[name="salesNotifyPartyAddr"]').valid()
        if (!isValid) {
            return false;
        }
        let data = {};
        data['code'] = Sales_NP_add_element.find('input[name="code"]').val();
        data['notifyPartyName'] = Sales_NP_add_element.find('input[name="salesNotifyPartyName"]').val();
        data['notifyPartyAddr'] = Sales_NP_add_element.find('input[name="salesNotifyPartyAddr"]').val();

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
                    $('#add-sales-ntfy-party').modal('toggle');

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

                                $('#modal-create-sales').find('select[name="notifypartyId"]').empty();
                                $('#modal-create-sales').find('select[name="notifypartyId"]').select2({
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

    let sales_consignee_add_element = $('#add-sales-consigner')
    sales_consignee_add_element.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(event.relatedTarget);
        var customerId = salesOrderData.customerId;
        $(this).find('input[name="code"]').val(customerId);
        // modalAddElementTriggerEle = $(event.relatedTarget);
    }).on('hidden.bs.modal', function() {
        $(this).find('span.help-block').html('')
        $(this).find('.has-error').removeClass('has-error')
        $(this).find('input,select').val('').trigger('change');
    }).on('click', 'button#save-sales-consig', function() {
        let isValid = $('#formSalesAddConsignee').find('input[name="salesConsigneeName"], input[name="salesConsigneeAddr"]').valid()
        if (!isValid) {
            return false;
        }
        let data = {};
        data['code'] = sales_consignee_add_element.find('input[name="code"]').val();
        data['consigneeName'] = sales_consignee_add_element.find('input[name="salesConsigneeName"]').val();
        data['consigneeAddr'] = sales_consignee_add_element.find('input[name="salesConsigneeAddr"]').val();

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
                    $('#add-sales-consigner').modal('toggle');

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
                                $('#modal-create-sales').find('select[name="consigneeId"]').empty();
                                $('#modal-create-sales').find('select[name="consigneeId"]').select2({
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

    let porfoma_NP_add_element = $('#add-porfoma-ntfy-party')
    porfoma_NP_add_element.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(event.relatedTarget);
        var customerId = $('#modal-create-proforma').find('select#customerCode').val();
        $(this).find('input[name="code"]').val(customerId);
        // modalAddElementTriggerEle = $(event.relatedTarget);
    }).on('hidden.bs.modal', function() {
        $(this).find('span.help-block').html('')
        $(this).find('.has-error').removeClass('has-error')
        $(this).find('input,select').val('').trigger('change');
    }).on('click', 'button#save-porfoma-NP', function() {
        let isValid = $('#formPorfomaAddNotifyParty').find('input[name="porfomaNotifyPartyName"], input[name="porfomaNotifyPartyAddr"]').valid()
        if (!isValid) {
            return false;
        }
        let data = {};
        data['code'] = porfoma_NP_add_element.find('input[name="code"]').val();
        data['notifyPartyName'] = porfoma_NP_add_element.find('input[name="porfomaNotifyPartyName"]').val();
        data['notifyPartyAddr'] = porfoma_NP_add_element.find('input[name="porfomaNotifyPartyAddr"]').val();

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
                    $('#add-porfoma-ntfy-party').modal('toggle');

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

                                $('#modal-create-proforma').find('select[name="notifypartyId"]').empty();
                                $('#modal-create-proforma').find('select[name="notifypartyId"]').select2({
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

    let porfoma_consignee_add_element = $('#add-porfoma-consigner')
    porfoma_consignee_add_element.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(event.relatedTarget);
        var customerId = $('#modal-create-proforma').find('select#customerCode').val();
        $(this).find('input[name="code"]').val(customerId);
        // modalAddElementTriggerEle = $(event.relatedTarget);
    }).on('hidden.bs.modal', function() {
        $(this).find('span.help-block').html('')
        $(this).find('.has-error').removeClass('has-error')
        $(this).find('input,select').val('').trigger('change');
    }).on('click', 'button#save-porfoma-consig', function() {
        let isValid = $('#formPorfomaAddConsignee').find('input[name="porfomaConsigneeName"], input[name="porfomaConsigneeAddr"]').valid()
        if (!isValid) {
            return false;
        }
        let data = {};
        data['code'] = porfoma_consignee_add_element.find('input[name="code"]').val();
        data['consigneeName'] = porfoma_consignee_add_element.find('input[name="porfomaConsigneeName"]').val();
        data['consigneeAddr'] = porfoma_consignee_add_element.find('input[name="porfomaConsigneeAddr"]').val();

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
                    $('#add-porfoma-consigner').modal('toggle');

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
                                $('#modal-create-proforma').find('select[name="consigneeId"]').empty();
                                $('#modal-create-proforma').find('select[name="consigneeId"]').select2({
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

})

function deleteProformaInvoiceId(invoiceNo) {
    let result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "delete",
        url: myContextPath + "/sales/delete/proformaInvoice?invoiceNo=" + invoiceNo,
        contentType: false,
        async: false,
        success: function(data) {
            result = data
        }
    });

    return result;
}
