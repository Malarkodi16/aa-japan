var countriesJson;
var transactionDetails = $('#transactionDetailsBody');
var transactionDetailsEle = transactionDetails.find('table')
$(function() {

    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        countriesJson = data;

    })

    //customer
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
    //     $.getJSON(myContextPath + "/sales/customerlist-data", function(data) {
    //         $("#edit-customer").select2({
    //             allowClear: true,
    //             width: '100%',
    //             data: $.map(data, function(item) {
    //                 return {
    //                     id: item.country,
    //                     text: item.country
    //                 };
    //             })
    //         }).val('').trigger("change");
    //     });

    //Date Range
    var purchased_min;
    var purchased_max;
    $('#date-range').daterangepicker({
        autoUpdateInput: false
    }).on("apply.daterangepicker", function(e, picker) {
        purchased_min = picker.startDate;
        purchased_max = picker.endDate;
        purchased_min = purchased_min.format('DD-MM-YYYY')
        purchased_max = purchased_max.format('DD-MM-YYYY')
        picker.element.val(purchased_min + ' - ' + purchased_max);
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
    });
    $('#date-form-group').on('click', '.clear-date', function() {
        purchased_min = '';
        purchased_max = '';
        $('#date-range').val('');
        $(this).remove();

    });

    $('#search').on('click', function() {
        customer_transaction.ajax.reload();
    });

    $('#exportExcelNew').on('click', function() {
        let customerId = !isEmpty($('#custId').find('option:selected').val()) ? $('#custId').find('option:selected').val() : $('#customerId').val();
        let min_date = purchased_min || '';
        let max_date = purchased_max || '';
        let queryString = "?customerId=" + customerId + "&min_date=" + min_date + "&max_date=" + max_date
        $.redirect(myContextPath + '/sales/customertransaction/export/excel/new' + queryString, '', 'GET');
    })

    jQuery.fn.DataTable.Api.register('buttons.exportData()', function(options) {
        if (this.context.length) {
            let customerId = $('#custId').find('option:selected').val();
            let min_date = purchased_min || '';
            let max_date = purchased_max || '';
            var data = {
                customerId: customerId,
                from: min_date,
                to: max_date
            }
            var reportData = getCustomerTransactionReport(data);
            var columnDefs = getExcelExportColumnDefs();
            let header = columnDefs.map((col)=>col.name);
            return {
                body: formatExcelData(reportData.data, columnDefs),
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
    var customer_transaction_Ele = $('#customer-month-transaction');
    var customer_transaction = customer_transaction_Ele.DataTable({
        "pageLength": 25,
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "ordering": false,
        "ajax": {
            url: myContextPath + "/sales/customertransaction/search",
            data: function(data) {
                data.customerId = !isEmpty($('#custId').find('option:selected').val()) ? $('#custId').find('option:selected').val() : $('#customerId').val();
                data.min_date = purchased_min || '';
                data.max_date = purchased_max || '';
            }
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            orderable: false,
            className: 'details-control',
            "data": "vessel",
            "render": function(data, type, row) {
                return '<div class="container-fluid"><div class="row bg-lightgreen"><div class="col-md-12"><h5 class="font-bold">' + row.date + " - CLICK TO VIEW " + '<i class="fa fa-plus-square-o pull-right"></i></h5></div></div></div>';
            }
        }],
        buttons: [$.extend(true, {}, exportoptions, {
            extend: 'excelHtml5'
        })],
        "fnDrawCallback": function(oSettings) {
            $(oSettings.nTHead).hide();
        }

    });
    $('#exportExcel').on('click', function() {
        customer_transaction.button("#dt_excel_export").trigger();
    })

    updateFields();
    //expand details
    customer_transaction.on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = customer_transaction.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
        } else {
            customer_transaction.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = customer_transaction.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                }

            })
            var masterTableDate = row.data();
            let broughtForwardResponse = broughtForward(masterTableDate.customerId, masterTableDate.date)
            if (broughtForwardResponse.status == 'success') {
                let stockResponse = stockItems(masterTableDate.customerId, masterTableDate.date);
                // if (!isEmpty(stockResponse) && !isEmpty(stockResponse.data)) {
                let paidAmount = paymentAmount(masterTableDate.customerId, masterTableDate.date)
                row.child(formatTable(stockResponse.data, broughtForwardResponse, paidAmount.data, masterTableDate.date, masterTableDate.currencySymbol, masterTableDate.currency)).show();
                tr.addClass('shown');
                // }
            }

        }
    });

    var stockItems = function(customerId, date) {
        let result;
        let queryString = "?customerId=" + customerId + "&date=" + date
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            url: myContextPath + "/sales/customertransaction/search/onExpand" + queryString,
            async: false,
            success: function(data) {
                result = data;
            }
        });
        return result;
    }

    //Sub Account Detail Modal
    var viewTransactionDetailsEle = $('#modal-view-transactions')
    var transactionDetailsModalBody = viewTransactionDetailsEle.find('#transactionDetailsBody');
    viewTransactionDetailsEle.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(event.relatedTarget);
        var date = targetElement.attr('data-date');
        setTransactionDetailsData($('#custId').val(), date);
    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
        $('#transactionDetailsBody').find('table').dataTable().fnDestroy();
    });

})
var broughtForward = function(customerId, date) {
    let result;
    var queryString = "?customerId=" + customerId + "&date=" + date
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "get",
        url: myContextPath + "/sales/customertransaction/broughtForward" + queryString,
        async: false,
        success: function(data) {
            result = data;
        }
    });
    return result;
}

function paymentAmount(customerId, date) {
    let result;
    var queryString = "?customerId=" + customerId + "&date=" + date
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "get",
        url: myContextPath + "/sales/customertransaction/paymentAmount" + queryString,
        async: false,
        success: function(data) {
            result = data;
        }
    });
    return result;
}

function formatBroughtForward(element, bForward, currencySymbol, currency) {
    var broughtForwardRowClone = $(element).find('table>tbody').find('tr.broughtForwardRow');
    $(broughtForwardRowClone).find('td.broughtForward>span').attr('data-a-sign', ifNotValid(currencySymbol) + ' ').attr('data-m-dec', currency == 2 ? 2 : 0).html(bForward.data);
    $(broughtForwardRowClone).removeClass('hide');
    $(element).find('table>tbody').append(broughtForwardRowClone);
}
function formatTable(rowData, bForwardResponse, paidAmount, masterTableDate, currencySymbol, currency) {
    var element = $('#customer-transaction-details-view>.detail-view').clone();
    var tbody = '';
    var tfoot = '';
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    var totalClone = $(element).find('table>tfoot').find('tr.clone-total');
    var balanceClone = $(element).find('table>tfoot').find('tr.balanceReceipt');
    var totalBalanceClone = $(element).find('table>tfoot').find('tr.clone-total-balance');
    var priceTotal = 0
      , price = 0;
    var currencySymbol;
    // append brought forward
    formatBroughtForward(element, bForwardResponse, currencySymbol, currency)
    // append other rowdata
    for (var i = 0; i < rowData.length; i++) {
        var row = $(rowClone).clone();
        let status = ""
          , className = "";
        if (ifNotValid(rowData[i].status, '') == 0) {
            $(row).find('td.status>span.label').addClass('label-default');
            $(row).find('td.status>span.label').html('');
        }
        var JsonData = JSON.stringify(rowData[i]);

        $(row).attr('data-json', JsonData);

        priceTotal += ifNotValid(rowData[i].price, '')
        price += ifNotValid(rowData[i].price, '')

        $(row).find('td.s-no>span').val(JsonData);
        $(row).find('td.s-no').html(i + 1);
        $(row).find('td.invoiceNo').html(ifNotValid(rowData[i].invoiceNo, ''));
        $(row).find('td.date').html(ifNotValid(rowData[i].date, ''));
        $(row).find('td.stockNo').html(ifNotValid(rowData[i].stockNo, ''));
        $(row).find('td.lotNo').html(ifNotValid(rowData[i].lotNo, ''));
        $(row).find('td.auction').html(ifNotValid(rowData[i].auction, ''));
        $(row).find('td.chassisNo').html(ifNotValid(rowData[i].chassisNo, ''));
        $(row).find('td.purchasePrice span').attr('data-a-sign', '¥ ').html(ifNotValid(rowData[i].purchaseCost, ''));
        $(row).find('td.commission span').attr('data-a-sign', '¥ ').html(ifNotValid(rowData[i].commision, ''));
        $(row).find('td.additional span').attr('data-a-sign', '¥ ').html(ifNotValid(rowData[i].additionalCharges, ''));
        $(row).find('td.price span').attr('data-a-sign', ifNotValid(currencySymbol) + ' ').attr('data-m-dec', currency == 2 ? 2 : 0).html(ifNotValid(rowData[i].price, ''));

        $(row).find('td.lcNo').html(ifNotValid(rowData[i].lcNumber, ''));
        $(row).find('td.receivedAmount span').attr('data-a-sign', ifNotValid(currencySymbol) + ' ').attr('data-m-dec', currency == 2 ? 2 : 0).html(ifNotValid(rowData[i].receivedAmount, ''));
        $(row).find('td.invoiceNo').html(ifNotValid(rowData[i].invoiceNo, ''));
        $(row).find('td.customerId').html(ifNotValid(rowData[i].customerId, ''));
        $(row).find('td.fName').html(ifNotValid(rowData[i].firstName, ''));
        $(row).find('td.forwarder').html(ifNotValid(rowData[i].forwarder, ''));
        $(row).find('td.etd').html(ifNotValid(rowData[i].etd, ''));
        $(row).find('td.eta').html(ifNotValid(rowData[i].eta, ''));
        $(row).find('td.dhlNo').html(ifNotValid(rowData[i].dhlNo, ''))
        $(row).find('td.vessal').html(ifNotValid(rowData[i].vesselName, ''))
        //$(row).find('td.containerNo').html(ifNotValid(rowData[i].containerNo, ''))

        if (rowData[i].shippingStatus == 0) {
            $(row).find('td.shippingStatus>span').html(ifNotValid('Idle', '')).addClass('label-default')
            //action
            $(row).find('td.action').removeClass('hidden')
        } else if (rowData[i].shippingStatus == 1) {
            $(row).find('td.shippingStatus>span').html(ifNotValid('Shipping Instruction Given', '')).addClass('label-success')
            $(row).find('td.action').addClass('hidden')
        }
        $(row).removeClass('hide');
        $(element).find('table>tbody').append(row);

    }

    $(totalClone).find('th');
    var totalAndBForward = priceTotal + bForwardResponse.data
    $(totalClone).find('th.priceTotal span').attr('data-a-sign', ifNotValid(currencySymbol) + ' ').attr('data-m-dec', currency == 2 ? 2 : 0).html(totalAndBForward);
    $(balanceClone).find('th.balanceReceipt span').attr('data-a-sign', ifNotValid(currencySymbol) + ' ').attr('data-m-dec', currency == 2 ? 2 : 0).html(paidAmount);
    //paidAmount
    $(balanceClone).find('th.showTrans button').attr('data-date', masterTableDate);
    $(totalBalanceClone).find('th.totalBalanceReceipt span').attr('data-a-sign', ifNotValid(currencySymbol) + ' ').attr('data-m-dec', currency == 2 ? 2 : 0).html(totalAndBForward + paidAmount);
    $(element).find('span.autonumber').autoNumeric('init');
    return element;
}

function setTransactionDetailsData(customerId, date) {
    let queryString = "?customerId=" + customerId + "&date=" + date
    transactionDetailTable = transactionDetailsEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ordering": false,
        "ajax": myContextPath + "/sales/customertransaction/onMonth" + queryString,
        columnDefs: [{
            "targets": '_all',
            "defaultContent": "",
            "className": "vcenter"
        }, {
            targets: 0,
            className: 'details-control',
            "name": "Code",
            "data": "createdDate",

        }, {
            targets: 1,
            "className": "dt-right details-control",
            "name": "Date",
            "data": "amount",
            render: function(data, type, row) {
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' "  data-m-dec="' + row.currency == 2 ? 2 : 0 + '">' + data + '</span>';
            }

        }],
        "drawCallback": function(settings, json) {
            $(transactionDetailsEle).find('span.autonumber').autoNumeric('init')
        }

    })

}
$('#modal-create-shipping').on('show.bs.modal', function(e) {
    if (e.namespace != 'bs.modal') {
        return;
    }
    var i = 0;
    var buttuonTarget = e.relatedTarget;
    var data = $(buttuonTarget).closest('tr.clone-row').attr('data-json');
    data = JSON.parse(data);
    if (data.shippingStatus == 1) {
        return false;
    }
    //var data = table.row(this).data();
    var stockNo = ifNotValid(data.stockNo, '');
    var chassisNo = ifNotValid(data.chassisNo, '');
    var customerId = ifNotValid(data.customerId, '');
    var customerName = ifNotValid(data.firstName, '');
    var customerLastName = ifNotValid(data.lastName, '');

    if (i != 0) {
        element = $('#item-shipping-clone').find('.item-shipping').clone();
        $(element).appendTo('#item-shipping-clone-container');
    } else {
        element = $('#item-shipping-container').find('.item-shipping');
    }
    $(element).find('input[name="stockNo"]').val(stockNo);
    $(element).find('input[name="chassisNo"]').val(chassisNo);
    $(element).find('input[name="customerId"]').val(customerId);
    $(element).find('input[name="customernameId"]').val(customerName + ' ' + customerLastName);
    i++;

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
                $(element).find('#cFirstshippingName').empty();
                $(element).find('#cFirstshippingName').select2({
                    allowClear: true,
                    width: '125%',
                    data: $.map(consArray, function(consigneeNotifyparties) {
                        return {
                            id: consigneeNotifyparties.id,
                            text: consigneeNotifyparties.cFirstName + ' ' + consigneeNotifyparties.cLastName
                        };
                    })
                }).val(selectedVal).trigger("change");
                $(element).find('#npFirstshippingName').empty();
                let npArray = data.data.consigneeNotifyparties.filter(function(index) {
                    return !isEmpty(index.npFirstName);
                })

                $(element).find('#npFirstshippingName').select2({
                    allowClear: true,
                    width: '125%',
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
                            text: item.country
                        };
                    })
                }).val(data.data.country).trigger('change');
                $(element).find('select[name="destPort"]').val(data.data.port).trigger('change');
                $(element).find('select[name="yard"]').val(data.data.yardDetails).trigger('change');
            }
        },
        error: function(e) {
            console.log("ERROR: ", e);

        }
    });

    //Create Shipping Instruction

    $('#item-shipping-container').find('select[name="destPort"]').select2({
        allowClear: true,
        width: '100%',
        placeholder: 'Search Destination Port',

    })
    $('#item-shipping-container').find('select[name="yard"]').select2({
        allowClear: true,
        width: '100%',
        placeholder: 'Search Yard',

    })
}).on('change', 'select[name="destCountry"]', function() {
    var closestEle = $(this).closest('.row');
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
    if (country == 'Kenya') {
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
});
$('#btn-save-shipping').on('click', function(event) {
    var objectArr = [];
    var data = {};
    $("#item-shipping-container").find('.item-shipping').each(function() {
        object = getFormData($(this).find('input,select,textarea'));
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
        url: myContextPath + "/invoice/shipping/order/save",
        contentType: "application/json",
        success: function(data) {
            if (data.status === 'success') {
                $('#modal-create-shipping').modal('toggle');
                var alertEle = $('#alert-block');
                $(alertEle).css('display', '').html('<strong>Success!</strong> Shipping Instruction created.');
            }

        }
    });
})

function updateFields() {
    var customerId = $('#customerId').val();
    var customerBlock = $('#customer-block');
    var searchCustomer;
    var editCustomer;
    if (!isEmpty(customerId)) {
        $(customerBlock).addClass('hidden');
        $(customerBlock).find('#custId').append('<option value="' + customerId + '"></option>');
        $(customerBlock).find('#custId').val(customerId).trigger('change');

    }

}
function formatExcelData(data, header) {
    let results = data.map((item)=>{
        var result = [];
        for (let i = 0; i < header.length; i++) {
            var value = ifNotValid(item[header[i].data], '')
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
        'data': 'date',
        'name': 'Purchase Month'
    }, {
        'data': 'lotNo',
        'name': 'Lot No.'
    }, {
        'data': 'auction',
        'name': 'Supplier'
    }, {
        'data': 'purchaseCost',
        'name': 'Purchase Price'
    }, {
        'data': 'commision',
        'name': 'Commission'
    }, {
        'data': 'additionalCharges',
        'name': 'Additional Charges'
    }, {
        'data': 'price',
        'name': 'Reserve Price'
    }, {
        'data': 'lcNumber',
        'name': 'Lc Number'
    }, {
        'data': 'vesselName',
        'name': 'Vessel Name'
    }, {
        'data': 'containerNo',
        'name': 'Container No.'
    }, {
        'data': 'etd',
        'name': 'ETD'
    }, {
        'data': 'eta',
        'name': 'ETA'
    }, {
        'data': 'dhlNo',
        'name': 'DHL No.'
    }]
}
function getCustomerTransactionReport(data) {
    var result;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "GET",
        async: false,
        data: data,
        url: myContextPath + '/sales/customertransaction/export/excel',
        success: function(data) {
            result = data;

        }
    });
    return result;
}
