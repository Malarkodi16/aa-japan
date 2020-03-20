$(function() {
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        table.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    //Curency Proforma Json
    $.getJSON(myContextPath + "/data/currency.json", function(data) {
        currencyJson = data;
        $('#currencySalesType').select2({
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
    });

    //customer
    $('#custselectId').select2({
        allowClear: true,
        minimumInputLength: 2,
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
    });

    $('#paymentSalesType').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        width: '100%',
        allowClear: true,
    });

    // Datatable 
    var tableEle = $('#table-branch-salesOrder');
    var table = tableEle.DataTable({
        "ajax": myContextPath + "/accounts/create/branch-salesOrder/datasource",
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        "order": [[2, "asc"]],
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
                    return '<input class="selectBox" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">';
                }
                return data;
            }
        }, {
            targets: 1,
            //orderable: false,
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
            targets: 2,
            //orderable: false,
            "className": "details-control",
            "searchable": true,
            "name": "chassisNo",
            "data": "chassisNo"
        }, {
            targets: 3,
            "className": "details-control",
           // orderable: false,
            "searchable": true,
            "data": "category"
        }, {
            targets: 4,
            //orderable: false,
            "searchable": true,
            "data": "maker"
        }, {
            targets: 5,
           // orderable: false,
            "searchable": true,
            "data": "model"
        }, {
            targets: 6,
            //orderable: false,
            "searchable": false,
            "data": "destCountry"
        }, {
            targets: 7,
           // orderable: false,
            "searchable": true,
            "data": "destPort"
        }, {
            targets: 8,
           // orderable: false,
            "searchable": true,
            "data": "customerId",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return row.firstName + ' ' + row.lastName + '(' + row.nickName + ')';
            }
        }, {
            targets: 9,
           // orderable: false,
            "searchable": true,
            "visible": false,
            "data": "customerId"
        }]
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
        var count = table.rows({
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
        var count = table.rows({
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

    var filterCustomer = $('#custselectId').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        // customer filter
        if (typeof filterCustomer != 'undefined' && filterCustomer.length != '') {
            if (aData[9].length == 0 || aData[9] != filterCustomer) {
                return false;
            }
        }
        return true;
    });

    $('#modal-create-sales').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        //validate custome
        var selectedRowData = table.rows({
            selected: true,
            page: 'current'
        }).data()
        var tmpCustId;
        var isValid = true;
        for (var i = 0; i < selectedRowData.length; i++) {
            if (i == 0) {
                tmpCustId = selectedRowData[i].customerId;
            } else if (tmpCustId != selectedRowData[i].customerId) {
                isValid = false;
                break;
            }
        }
        if (!isValid) {
            alert($.i18n.prop('alert.profomainvoice.create.selected.different.cutomer'));
            return e.preventDefault();
        }

        if (isEmpty($('#custselectId').find('option:selected').val())) {
            alert($.i18n.prop('common.alert.branch.noselection'));
            return e.preventDefault();
        }

        var element;
        var i = 0;
        var custdfdata;
        table.rows({
            selected: true,
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            custdfdata = table.row(this).data();
            var data = table.row(this).data();
            var stockNo = ifNotValid(data.stockNo, '');
            var maker = ifNotValid(data.maker, '');
            var model = ifNotValid(data.model, '');
            var chassisNo = ifNotValid(data.chassisNo, '');
            var firstRegDate = ifNotValid(data.firstRegDate, '');
            var total = 0;
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
            $(element).find('input[name="total"]').val(total);
            $('#sales-details').find('#customernameSalesId').val(custName + ' ' + custLastName);
            $('#sales-details').find('#customerSalesId').val(customerId);
            i++;
        });
        $(this).find('input.autonumber').autoNumeric('init');
        var customerId = ifNotValid(custdfdata.customerId, '');
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
                    $('#cFirstsalesName').empty();
                    let consArray = custdfdata.data.consigneeNotifyparties.filter(function(index) {
                        return !isEmpty(index.cFirstName);
                    })
                    $('#cFirstsalesName').select2({
                        allowClear: true,
                        width: '100%',
                        data: $.map(consArray, function(consigneeNotifyparties) {
                            return {
                                id: consigneeNotifyparties.id,
                                text: consigneeNotifyparties.cFirstName + ' ' + consigneeNotifyparties.cLastName
                            };
                        })
                    }).val(selectedVal).trigger("change");

                    $('#npFirstsalesName').empty();
                    let npArray = custdfdata.data.consigneeNotifyparties.filter(function(index) {
                        return !isEmpty(index.npFirstName);
                    })
                    $('#npFirstsalesName').select2({
                        allowClear: true,
                        width: '100%',
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
        $(this).find('.calculation').trigger('keyup');
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

    $('#paymentSalesType').on('change', function() {
        var closestEle = $(this).closest('.row');
        var id = $(this).val();
        if (id == "FOB") {
            $('#item-sales-container').find('input[name="freight"]').prop('disabled', true);
            $('#item-sales-container').find('input[name="freight"]').val(0);
            $('#totalSalesDiv').find('input[name="freightTotal"]').val(0);

        } else {
            $('#item-sales-container').find('input[name="freight"]').prop('disabled', false);

        }
    })

    //Total Calculation FOB,Freight,Insurance ,Total Amount 
    $('#item-sales-container').on('keyup', ".calculation", function() {
        var closestEle = $(this).closest('.row');
        var total = Number($(closestEle).find('input[name="total"]').autoNumeric('get'));
        var totalEle = $(closestEle).find('input[name="total"]');
        var element;
        var intVal = function(i) {
            return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
        };
        var isValid = function(val) {
            return typeof val === 'undefined' || val == null ? 0 : val;
        }
        //total
        var amountTotal = 0;
        $('#item-sales-container').find('input[name="total"]').each(function(index) {
            amountTotal += Number($(this).autoNumeric('get'))
        })
        $('#grand-total-sales').autoNumeric('set', amountTotal);
        $(totalEle).autoNumeric('set', total);

        return;

    })
    
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
            url: myContextPath + "/invoice/branch/sales/order/save",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    $('#modal-create-sales').modal('toggle');
                    var alertEle = $('#alert-block');
                    $(alertEle).css('display', '').html('<strong>Success!</strong> Stock saved.');
                    $(alertEle).fadeTo(5000, 500).slideUp(500, function() {
                        $(alertEle).slideUp(500);
                        table.ajax.reload();
                    });
                   
                }

            }
        });
    })

});
