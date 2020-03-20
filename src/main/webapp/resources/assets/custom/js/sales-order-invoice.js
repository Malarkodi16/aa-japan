var countriesJson, screenNameFlag;
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

var table, tableStocks;

$(function() {

    $('input[type="radio"][name=radioShowTable].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    })

    //Radio Button with Container 
    $('input[type="radio"][name=radioShowTable].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        if ($(this).val() == 0) {
            $('#purshase-stock-container').removeClass('hidden');
            $('#purshase-stock-invoice-container').addClass('hidden')
            $('#btn-create-shipping').removeClass('hidden')
            tableStocks.ajax.reload()
        } else if ($(this).val() == 1) {
            $('#purshase-stock-container').addClass('hidden');
            $('#purshase-stock-invoice-container').removeClass('hidden')
            $('#btn-create-shipping').addClass('hidden')
            table.ajax.reload()
        }
    });

    screenNameFlag = $('input[name="screenNameFlag"]').val();

    //     function regex_escape(text) {
    //         return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/$/g, "");
    //     }
    ;$('#table-filter-search').keyup(function() {
        //         var query = regex_escape($(this).val());
        table.search($(this).val()).draw();
        tableStocks.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
        tableStocks.page.len($(this).val()).draw();
    });

    $('#custId').select2({
        allowClear: true,
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

    /*Date picke function change*/

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

    $.fn.dataTable.ext.search.push(function(settings, data, dataIndex) {
        if (settings.sTableId == "table-sales-order-invoice") {
            var term = $('#table-filter-search').val().toLowerCase();
            var salesInvoiceDetails = JSON.parse(data[10]);
            for (var i = 0; i < salesInvoiceDetails.length; i++) {
                if (!isEmpty(term)) {
                    if (~ifNotValid(salesInvoiceDetails[i]["chassisNo"], '').toLowerCase().indexOf(term))
                        return true;
                    if (~ifNotValid(salesInvoiceDetails[i]["stockNo"], '').toLowerCase().indexOf(term))
                        return true;
                    if (~ifNotValid(salesInvoiceDetails[i]["maker"], '').toLowerCase().indexOf(term))
                        return true;
                    if (~ifNotValid(salesInvoiceDetails[i]["model"], '').toLowerCase().indexOf(term))
                        return true;
                    if (~ifNotValid(salesInvoiceDetails[i]["total"]+"", '').toLowerCase().indexOf(term))
                        return true;
                    if (~ifNotValid(salesInvoiceDetails[i]["status"]+"", '').toLowerCase().indexOf(term))
                        return true;
                }
                if (isEmpty(term)) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    });

    $('#excel_export_all').on('click', function() {
        $.redirect(myContextPath + '/sales/order/export/excel/new', '', 'GET');
    })

    /* Sales order Stock list Data-table*/
    tableStocks = $('#table-sales-order-stock').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "order": [[13, "desc"]],
        //         "aaSorting": [[0, "desc"]],
        "ajax": {
            'url': myContextPath + "/sales/orderStock/list/datasource",
            'data': function(data) {
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
            "targets": 12,
            "type": "date-eu"
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
                    return '<input class="selectBox" type="checkbox" data-id="' + row.stockNo + '" value="' + data + '">'
                }
                return data;
            }
        }, {
            targets: 1,
            "data": "invoiceNo"
        }, {
            targets: 2,
            "data": "stockNo"
        }, {
            targets: 3,
            "data": "chassisNo"
        }, {
            targets: 4,
            "data": "maker"
        }, {
            targets: 5,
            "data": "model"
        }, {
            targets: 6,
            "data": "customerFN"
        }, {
            targets: 7,
            "data": "cFirstName"
        }, {
            targets: 8,
            "data": "npFirstName"
        }, {
            targets: 9,
            "data": "orderedBy"
        }, {
            targets: 10,
            "data": "total",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' " data-m-dec="' + (row.currencyType != 2 ? 0 : 2) + '">' + data + '</span>';
            }
        }, {
            targets: 11,
            "data": "status",
            "render": function(data, type, row) {

                var status;
                var className;
                if (row.status == 0) {
                    status = "Not Received"
                    className = "default"

                } else if (row.status == 1) {
                    status = "Payment Received"
                    className = "success"
                } else if (row.status == 2) {
                    status = "Paymeny Received Partial"
                    className = "warning"
                }

                return '<span class="label label-' + className + '">' + status + '</span>'
            },

        }, {
            targets: 12,
            "data": "shippingStatus",
            "render": function(data, type, row) {
                var status;
                var className;
                if (data == 0) {
                    status = "Not Arranged"
                    className = "default"

                } else if (data == 1) {
                    status = "Arranged"
                    className = "success"
                }
                return '<span class="label label-' + className + '">' + status + '</span>'
            },

        }, {
            targets: 13,
            //"visible": false,
            "data": "createdDate",
            "type": "date-eu"
        }, {
            targets: 14,
            "data": "total",
            "render": function(data, type, row) {
                return isEmpty(row.shipName) ? '' : row.shipName + ' [' + row.shippingCompanyName + ']';
            }
        }, {
            targets: 15,
            "data": "etd"
        }, {
            targets: 16,
            "data": "eta"
        }, {
            targets: 17,
            "data": "customerId",
            "searchable": true,
            "visible": false,
        }],
        "drawCallback": function(settings, json) {
            $('input.autonumber,span.autonumber').autoNumeric('init')

        }

    });
    $('#showMine').change(function() {
        tableStocks.ajax.reload();
        table.ajax.reload();

    })

    /* Sales order invoice list Data-table*/

    var isAdminManagerValue = $('input[name="isAdminManager"]').val();
    isAdminManagerValue = (isAdminManagerValue == 1) ? true : false;
    table = $('#table-sales-order-invoice').DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": true,
        "order": [[8, "desc"]],
        "ajax": {
            url: myContextPath + "/sales/orderInvoice/list/datasource",
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
            "targets":8, "type":"date-eu"
        }, {
            targets: 0,
            orderable: false,
            "visible": false,
            className: 'select-checkbox',
            "data": "id",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" type="checkbox" data-id="' + row.invoiceNo + '" value="' + data + '">'
                }
                return data;
            }
        }, {
            targets: 1,
            "className": "details-control",
            "data": "invoiceNo"
        }, {
            targets: 2,
            "className": "details-control",
            "data": "fCustomerName",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return data;
            }
        }, {
            targets: 3,
            "className": "details-control",
            "data": "fConsigneeName",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return data;
            }
        }, {
            targets: 4,
            "className": "details-control",
            "data": "fNotifyName",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return data;
            }
        }, {
            targets: 5,
            "className": "details-control",
            "data": "paymentType"
        }, {
            targets: 6,
            "className": "details-control",
            "data": "orderedBy",
            "visible": isAdminManagerValue
        }, {
            targets: 7,
            "className": "dt-right details-control",
            "data": "allTtotal",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' " data-m-dec="' + (row.currencyType != 2 ? 0 : 2) + '">' + data + '</span>';

            }
        }, {
            targets: 8,
            "className": "details-control",
            "data": "createdDate"
        }, {
            targets: 9,
            "data": "invoiceNo",
            "className": 'align-center',
            orderable: false,
            "render": function(data, type, row) {
                var classNew = screenNameFlag == 'sales' ? 'hidden' : '';
                let accountsScreen = screenNameFlag == 'accounts' ? 'hidden' : '';
                var html = ''
                html += '<a href="#" class="ml-5 btn btn-info btn-xs ' + classNew + '" name="edit-invoice" title="Edit Sales Order" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-edit-sales"><i class="fa fa-fw fa-edit"></i></a>'
                html += '<a href="#" class="ml-5 btn btn-success btn-xs ' + accountsScreen + ' " id="btn-create-shipping" name="btn-create-shipping-instruction" title="Arrange shipping instruction" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-create-shipping"><i class="fa fa-ship"></i></a>'
                html += '<a href="#" class="ml-5 btn btn-danger btn-xs ' + classNew + '" name="cancel"><i class="fa fa-fw fa-remove"></i></a>'
                html += '<a href="' + myContextPath + '/download/sales/invoice/' + row.invoiceNo + '.pdf" class="ml-5 btn btn-default btn-xs"><i class="fa fa-fw fa-download"></i></a>'
                return html;
            }
        }, {
            targets: 10,
            data: 'salesInvoiceDetails',
            "visible": false,
            "render": function(data, type, row) {
                return JSON.stringify(data)
            }
        }, {
            targets: 11,
            "data": "customerId",
            "searchable": true,
            "visible": false,
        }, {
            targets: 12,
            "visible": false,
            "data": "salesPerson"
        }],
        "drawCallback": function(settings, json) {
            $('input.autonumber,span.autonumber').autoNumeric('init')

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
    //     $('#showMine').change(function() {
    //         table.ajax.reload()
    //     })
    tableStocks.on("click", "th.select-checkbox>input", function() {
        if (!$(this).is(':checked')) {
            tableStocks.rows({
                page: 'current'
            }).deselect();
            $("th.select-checkbox").removeClass("selected");
            tableStocks.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', false);

            });
        } else {
            tableStocks.rows({
                page: 'current'
            }).select();
            $("th.select-checkbox").addClass("selected");
            tableStocks.rows({
                page: 'current'
            }).every(function(rowIdx, tableLoop, rowLoop) {
                $(this.node()).find('td:first>input[class="selectBox"]').prop('checked', true);

            });
        }
    }).on("select", function() {

        if (tableStocks.rows({
            selected: true,
            page: 'current'
        }).count() !== tableStocks.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }
        var count = tableStocks.rows({
            selected: true,
            page: 'current'
        }).count();
        var actioBtn = $('button.enable-on-select')
        if (count > 0) {
            actioBtn.removeAttr("disabled");
        } else {
            actioBtn.attr("disabled", "disabled");
        }
    }).on("deselect", function() {
        if (tableStocks.rows({
            selected: true,
            page: 'current'
        }).count() !== tableStocks.rows({
            page: 'current'
        }).count()) {
            $("th.select-checkbox>input").removeClass("selected");
            $("th.select-checkbox>input").prop('checked', false);
        } else {
            $("th.select-checkbox>input").addClass("selected");
            $("th.select-checkbox>input").prop('checked', true);

        }
        var count = tableStocks.rows({
            selected: true,
            page: 'current'
        }).count();
        var actioBtn = $('button.enable-on-select')
        if (count > 0) {
            actioBtn.removeAttr("disabled");
        } else {
            actioBtn.attr("disabled", "disabled");
        }

    });
    var filterCustomer, filterStaff;
    $('#custId').on('change', function() {
        filterCustomer = $(this).find('option:selected').val();
        table.draw();
    })

    $('#select_staff').on('change', function() {
        filterStaff = $(this).find('option:selected').val();
        table.draw();
    });

    // Date range picker
    var invoice_min;
    var invoice_max;
    $('#table-filter-invoice-date').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        invoice_min = picker.startDate;
        invoice_max = picker.endDate;
        picker.element.val(invoice_min.format('DD-MM-YYYY') + ' - ' + invoice_max.format('DD-MM-YYYY'));
        invoice_min = invoice_min._d.getTime();
        invoice_max = invoice_max._d.getTime();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        //         table.draw();
        //         tableStocks.draw();
        var date = $("input[name='radioShowTable']:checked").val();
        if (date == 0) {
            tableStocks.draw();
        } else if (date == 1) {
            table.draw();
        }

        //         on('ifChecked', function(e) {
        //         if ($(this).val() == 0) {
        //         	tableStocks.draw();
        //         } else if ($(this).val() == 1) {
        //         	table.draw();
        //         }
        //     });

        //table.draw();
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        invoice_min = '';
        invoice_max = '';
        //         table.draw();
        //         tableStocks.draw();
        var date = $("input[name='radioShowTable']:checked").val();
        if (date == 0) {
            tableStocks.draw();
        } else if (date == 1) {
            table.draw();
        }
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
    	var customersFilter = $("input[name='radioShowTable']:checked").val();
        if (customersFilter == 0) {
            tableStocks.draw();
        } else if (customersFilter == 1) {
            table.draw();
        }

        table.draw();
    })

    var filterCustomer = $('#custselectId').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {

        if (oSettings.sTableId == 'table-sales-order-invoice') {
            //date filter
            if (typeof invoice_min != 'undefined' && invoice_min.length != '') {
                if (aData[8].length == 0) {
                    return false;
                }
                if (typeof aData._date == 'undefined') {
                    aData._date = moment(aData[8], 'DD-MM-YYYY')._d.getTime();
                }
                if (invoice_min && !isNaN(invoice_min)) {
                    if (aData._date < invoice_min) {
                        return false;
                    }
                }
                if (invoice_max && !isNaN(invoice_max)) {
                    if (aData._date > invoice_max) {
                        return false;
                    }
                }

            }
            if (typeof filterCustomer != 'undefined' && filterCustomer.length != '') {
                if (aData[11].length == 0 || aData[11] != filterCustomer) {
                    return false;
                }
            }

            //Customer filter
            //             if (typeof filterCustomer != 'undefined' && filterCustomer.length != '') {
            //                 if (aData[11].length == 0 || aData[11] != filterCustomer) {
            //                     return false;
            //                 }
            //             }
            //             //Staff filter
            //             if (typeof filterStaff != 'undefined' && filterStaff.length != '') {
            //                 if (aData[12].length == 0 || aData[12] != filterStaff) {
            //                     return false;
            //                 }
            //             }

        } else if (oSettings.sTableId == 'table-sales-order-stock') {
            //date filter
            if (typeof invoice_min != 'undefined' && invoice_min.length != '') {
                if (aData[13].length == 0) {
                    return false;
                }
                if (typeof aData._date == 'undefined') {
                    aData._date = moment(aData[13], 'DD-MM-YYYY')._d.getTime();
                }
                if (invoice_min && !isNaN(invoice_min)) {
                    if (aData._date < invoice_min) {
                        return false;
                    }
                }
                if (invoice_max && !isNaN(invoice_max)) {
                    if (aData._date > invoice_max) {
                        return false;
                    }
                }

            }

            if (typeof filterCustomer != 'undefined' && filterCustomer.length != '') {
                if (aData[17].length == 0 || aData[17] != filterCustomer) {
                    return false;
                }
            }
            /*
           //Customer filter
           if (typeof filterCustomer != 'undefined' && filterCustomer.length != '') {
               if (aData[11].length == 0 || aData[11] != filterCustomer) {
                   return false;
               }
           }
           //Staff filter
           if (typeof filterStaff != 'undefined' && filterStaff.length != '') {
               if (aData[12].length == 0 || aData[12] != filterStaff) {
                   return false;
               }
           }*/

        } else {
            return true;
        }
        return true;

    });

    //on open Shipping modal
    let elementCreateShippingStock = $('#modal-create-shipping-stock');
    elementCreateShippingStock.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var rowdata = tableStocks.rows({
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

        var element;
        var i = 0;
        tableStocks.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var data = tableStocks.row(this).data();
            var stockNo = ifNotValid(data.stockNo, '');
            var chassisNo = ifNotValid(data.chassisNo, '');
            var customerId = ifNotValid(data.customerId, '');
            var customerFN = ifNotValid(data.customerFN, '');
            // var customerLastName = ifNotValid(data.lastName, '');
            var inspectionFlag = ifNotValid(data.inspectionFlag, '');

            if (i != 0) {
                element = elementCreateShippingStock.find('#item-shipping-clone').find('.item-shipping').clone();
                element.find('input[name="estimatedType"]').attr('name', 'estimatedType' + i);
                element.find('input[name="estimatedType' + i + '"][value="0"]').iCheck('check').trigger('change')
                $(element).appendTo('div#modal-create-shipping-stock div#item-shipping-clone-container');
            } else {
                element = elementCreateShippingStock.find('#item-shipping-container').find('.item-shipping');
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
            $(element).find('input[name="customernameId"]').val(customerFN);
            i++;
            $(element).find('.estimatedDate').addClass('hidden');
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
                            // width: '100%',
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
                        $(element).find('select[name="notifypartyId"]').empty()
                        $(element).find('#npFirstshippingName').select2({
                            allowClear: true,
                            // width: '100%',
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
            //width: '100%',
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
        if (!isEmpty(countryData.data)) {
            $(closestEle).find('input[name="inspectionFlag"]').attr('checked', countryData.data.inspectionFlag == 1 ? true : false).trigger('change');
        }

    }).on('hidden.bs.modal', function() {
        $(this).find('#item-shipping-clone-container').html('');
        $(this).find("input,textarea,select").val([]);
        $(this).find('input[name="inspectionFlag"]').attr('checked', false);
    }).on('ifChecked', '.estimated-data', function() {
        if ($(this).val() == 0) {
            $(this).closest('.estimatedData').find('.estimatedDate').addClass('hidden');
            $(this).closest('.estimatedData').find('.estimatedDate').find('input').val('');
        } else if ($(this).val() == 1) {
            $(this).closest('.estimatedData').find('.estimatedDate').addClass('hidden');
            $(this).closest('.estimatedData').find('.estimatedDate').find('input').val('');
        } else if ($(this).val() == 2) {
            $(this).closest('.estimatedData').find('.estimatedDate').removeClass('hidden');
        }

    }).on('click', '#btn-save-shipping-inst', function(event) {
        if (!(elementCreateShippingStock.find('#create-shipping-instruction-form').find('input,select').valid())) {
            return false;
        }
        var objectArr = [];
        elementCreateShippingStock.find("#item-shipping-container").find('.item-shipping').each(function() {
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
            data.yard = object.yard
            data.estimatedArrival = object.estimatedArrival
            data.estimatedDeparture = object.estimatedDeparture
            objectArr.push(data);
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
            url: myContextPath + "/invoice/shipping/order/save",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    $.redirect(myContextPath + '/sales/status', '', 'GET');
                    localStorage.setItem("aaj-sales-dashboard-active-nav", '5');
                }

            }
        });
    })

    $.getJSON(myContextPath + "/data/currency.json", function(data) {
        //         currencyJson = data;
        $('#currencySalesType').select2({
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

    var modalCreateSalesBtn, salesOrderData;
    var modalCreateSalesOrder = $('#modal-edit-sales');
    modalCreateSalesOrder.on('show.bs.modal', function(e) {
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
            if (items[i].status != 0) {
                alert($.i18n.prop('alert.edit.sales.order.inventory'))

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
        $('#sales-details').find('#invoiceNo').val(salesOrderData.invoiceNo);
        $('#sales-details').find('#cFirstName').attr('data-value', salesOrderData.consigneeId)
        $('#sales-details').find('#npFirstName').attr('data-value', salesOrderData.notifypartyId)
        $(this).find('input.autonumber').autoNumeric('init');
        var customerId = ifNotValid(salesOrderData.customerId, '');
        var consigneeId = ifNotValid(salesOrderData.consigneeId, '');
        var notifypartyId = ifNotValid(salesOrderData.notifypartyId, '');

        var custIdElem = modalCreateSalesOrder.find('select[name="customerId"]');
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
                        text: data.companyName + ' :: ' + data.firstName + ' ' + data.lastName + '(' + data.nickName + ')',
                        data: data
                    }]
                }).val(data.code).trigger('change');

            });

            customerInitSelect2(custIdElem, {
                id: salesOrderData.customerId,
                text: salesOrderData.companyName + ' :: ' + salesOrderData.customerFN + ' ' + salesOrderData.customerLN + '(' + salesOrderData.nickName + ')',
            });
        } else {
            alert($.i18n.prop('alert.customer.noselection'))
            return false;
        }

        var amountTotal = 0;
        $('#item-sales-container').find('input[name="total"]').each(function(index) {
            amountTotal += Number($(this).autoNumeric('get'))
        })
        $('#grand-total-sales').autoNumeric('set', amountTotal);
        //$(this).find('.calculation').trigger('keyup');
    }).on('hidden.bs.modal', function() {
        $(this).find('#item-sales-clone-container').html('');
        $(this).find("input,textarea,select").val([]);
    }).on('click', '.btn-remove-item', function() {
        var salesElement = modalCreateSalesOrder.find('#item-sales-clone-container div.row');
        if (salesElement.length > 1) {
            $(this).closest('div.item-sales').remove();
            modalCreateSalesOrder.find('.calculation').trigger('keyup');
        }
    }).on("change", 'select[name="customerId"]', function(event) {
        var id = $(this).val();
        if (id == null || id.length == 0) {
            modalCreateSalesOrder.find('#cFirstName').empty();
            modalCreateSalesOrder.find('#npFirstName').empty();
            return;
        }
        var data = $(this).select2('data')[0].data;
        var currencyEle = modalCreateSalesOrder.find('#currencySalesType')
        if (!isEmpty(data)) {
            currencyEle.val(data.currencyType).trigger('change').addClass('readonly');
        } else {
            currencyEle.val('').trigger('change');
        }
        //payment type
        var data = $(this).select2('data')[0].data;
        var paymentEle = modalCreateSalesOrder.find('#paymentSalesType')
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
                    }).val($('#cFirstName').attr('data-value')).trigger("change");

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
                                text: consigneeNotifyparties.npFirstName + ' ' + consigneeNotifyparties.npLastName
                            };
                        })
                    }).val($('#npFirstName').attr('data-value')).trigger("change");

                }
            },
            error: function(e) {
                console.log("ERROR: ", e);

            }
        });

    })

    $('#currencySalesType').on('change', function() {
        var data = $(this).select2('data')[0].data;
        if (!isEmpty(data)) {
            if (data.currency == "Yen") {
                modalCreateSalesOrder.find('input.autonumber').autoNumeric('init').autoNumeric('update', {
                    aSign: data.symbol + ' ',
                    mDec: 0
                });
            } else {
                modalCreateSalesOrder.find('input.autonumber').autoNumeric('init').autoNumeric('update', {
                    aSign: data.symbol + ' ',
                    mDec: 2
                });
            }
        }
    })

    //Total Calculation FOB,Freight,Insurance ,Total Amount 
    $('#item-sales-container').on('keyup', ".calculation", function() {
        var closestEle = $(this).closest('.row');
        var totalEle = $(closestEle).find('input[name="total"]');
        //$(totalEle).val(total);
        var element;
        var intVal = function(i) {
            return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
        };
        var isValid = function(val) {
            return typeof val === 'undefined' || val == null ? 0 : val;
        }

        var amountTotal = 0;
        $('#item-sales-container').find('input[name="total"]').each(function(index) {
            amountTotal += Number($(this).autoNumeric('get'))
        })
        $('#grand-total-sales').autoNumeric('set', amountTotal);

    })

    // Create Sales Invoice Order

    $('#btn-generate-sales-invoice').on('click', function() {
        if (!$('#create-sales-form').valid()) {
            return;
        }
        var autoNumericElements = modalCreateSalesOrder.find('input.autonumber');
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
            url: myContextPath + "/invoice/sales/order/edit",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    //                     $.redirect(myContextPath + '/sales/sales-order-invoice-list', '', 'GET');
                    //                     localStorage.setItem("aaj-sales-dashboard-active-nav", '4');
                    modalCreateSalesOrder.modal('toggle')
                    table.ajax.reload();

                }

            }
        });
    })

    $('#paymentSalesType').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        width: '100%',
        allowClear: true,
    }).on('change', function() {
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
            var customerName = ifNotValid(salesInvoiceData.cFirstName, '');
            var customerLastName = ifNotValid(salesInvoiceData.cLastName, '');

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
                            //width: '100%',
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
                            // width: '100%',
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
            // width: '100%',
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
            data.notifypartyId = object.notifypartyId;
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
            type: "post",
            data: JSON.stringify(objectArr),
            url: myContextPath + "/invoice/shipping/order/save",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    $.redirect(myContextPath + '/sales/status', '', 'GET');
                    localStorage.setItem("aaj-sales-dashboard-active-nav", '3');
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
        var customerId = $(addNotifyPartyTargetElement).closest('div.item-shipping').find('input#customerId').val();
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
                                container.find('select[name="notifypartyId"]').empty();
                                container.find('select[name="notifypartyId"]').select2({
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
    var addConsigneeTargetElement;
    consignee_add_element.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        addConsigneeTargetElement = $(event.relatedTarget);
        var customerId = $(addConsigneeTargetElement).closest('div.item-shipping').find('input#customerId').val();
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
                                container.find('select[name="consigneeId"]').empty();
                                container.find('select[name="consigneeId"]').select2({
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

    /*table on click to append sales invoice*/
    table.on('click', 'a[name="cancel"]', function() {
        if (!confirm($.i18n.prop('common.confirm.cancel'))) {
            return false;
        }
        var data = table.row($(this).closest('tr')).data();
        let invoiceNo = data.invoiceNo;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            url: myContextPath + "/invoice/sales/order/cancel/" + invoiceNo,
            success: function(data) {
                if (data.status == 'success') {
                    table.ajax.reload();
                    var alertEle = $('#alert-block');
                    $(alertEle).css('display', '').html('<strong>Success!</strong> Order Cancelled.');
                    $(alertEle).fadeTo(5000, 500).slideUp(500, function() {
                        $(alertEle).slideUp(500);
                    });
                } else if (data.status == 'failed') {
                    alert(data.message)
                }

            }
        });

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
        row.child(format(row.data())).show();
        tr.addClass('shown');
        tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
    }
});

/*function to format invoice details*/

function format(rowData) {
    var element = $('#clone-container>#sales-order-invoice-rearrange>.clone-element').clone();
    $(element).find('input[name="salesOrderinvoiceNo"]').val(rowData.invoiceNo)
    var tbody = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    for (var i = 0; i < rowData.salesInvoiceDetails.length; i++) {
        var row = $(rowClone).clone();
        row.attr('data-json', JSON.stringify(rowData.salesInvoiceDetails[i]));
        var status;
        var className;
        if (rowData.salesInvoiceDetails[i].status == 0) {
            status = "Not Received"
            className = "label-default"

        } else if (rowData.salesInvoiceDetails[i].status == 1) {
            status = "Payment Received"
            className = "label-success"
        } else if (rowData.salesInvoiceDetails[i].status == 2) {
            status = "Paymeny Received Partial"
            className = "label-warning"
        }

        var actionHtml = ''
        if (rowData.salesInvoiceDetails[i].status == 1) {
            actionHtml = '<a href="#" class="ml-5 btn btn-danger btn-xs stock status-update" data-flag="specific" data-orderId="' + rowData.code + '" data-toggle="modal" data-target="#modal-reason" data-backdrop="static" data-keyboard="false"><i class="fa fa-fw fa-close"></i></a>'
        }

        // $(row).find('td.s-no').html(i + 1);
        $(row).find('td.stockNo>span').html(ifNotValid(rowData.salesInvoiceDetails[i].stockNo, ''));
        $(row).find('td.stockNo>input').val(ifNotValid(rowData.salesInvoiceDetails[i].stockNo, ''));
        $(row).find('td.chassisNo').html(ifNotValid(rowData.salesInvoiceDetails[i].chassisNo, ''));
        $(row).find('td.maker').html(ifNotValid(rowData.salesInvoiceDetails[i].maker, ''));
        $(row).find('td.model').html(ifNotValid(rowData.salesInvoiceDetails[i].model, ''));
        $(row).find('td.total span').attr('data-a-sign', ifNotValid(rowData.salesInvoiceDetails[i].currencySymbol, '¥') + ' ').html(ifNotValid(rowData.salesInvoiceDetails[i].total, 0));
        $(row).find('td.status>span').addClass(className).html(status);
        $(row).find('td.action').html(actionHtml);
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);

    }
    $(element).find('span.autonumber').autoNumeric('init');

    return element;
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
                $(this.$element).empty();
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

        },
        initSelection: function(element, callback) {
            callback({
                id: value.id,
                text: value.text,
                data: value
            });
        }
    });
}
