//own pop up dataTable hidden
$('#hidden-table').addClass('hidden');
$(function() {

    var tableEle = $('#table-ttApprove');
    table = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/daybook/tt-owned-advance-dataSource",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },

        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "className": "details-control",
            "data": "createdDate",

        }, {
            targets: 1,
            "className": "details-control",
            "data": "daybookId",

        }, {
            targets: 2,
            "className": "details-control",
            "data": "chassisNo",

        }, {
            targets: 3,
            "className": "details-control",
            "data": "allocationType",
            "render": function(data, type, row) {
                let value = '';
                if (data == 1) {
                    value = 'FIFO';
                } else if (data == 2) {
                    value = 'UNIT ALLOCATION';
                } else if (data == 3) {
                    value = 'ADVANCE';
                } else if (data == 4) {
                    value = 'DEPOSITE';
                } else if (data == 5) {
                    value = 'LC';
                }
                return value;
            }
        }, {
            targets: 4,
            "className": "details-control",
            "data": "remitter"
        }, {
            targets: 5,
            "className": "details-control",
            "data": "bank"
        }, {
            targets: 6,
            "className": "details-control",
            "data": "amount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' "  data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 7,
            "className": "details-control",
            "data": "allocatedAmount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="' + row.customerCurrencySymbol + ' "  data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 8,
            "className": "details-control",
            "data": "advanceOwned",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="' + row.customerCurrencySymbol + ' "  data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 9,
            "className": "details-control",
            "data": "customerFn"
        }, {
            targets: 10,
            "className": "details-control",
            "data": "salesPerson"
        }, {
            targets: 11,
            orderable: false,
            "data": null,
            "render": function(data, type, row) {
                var html = ''
                html += '<button href="#" class="ml-5 btn btn-primary btn-xs" data-toggle="modal" data-target="#allocationModal" data-backdrop="static" data-keyboard="false">Own</button>'
                html += '<button type="button" name="refund" id="refund" class="btn btn-primary  ml-5 btn-xs" title="Refund" data-toggle="modal" data-target="#modal-refund-amount" data-backdrop="static" data-keyboard="false">Refund</button>';
                return html;

            }
        }, {
            targets: 12,
            "visible": false,
            "data": "bankSeq"
        }, {
            targets: 13,
            "visible": false,
            "data": "customerId"
        }, {
            targets: 14,
            "visible": false,
            "data": "salesPersonId"
        }],
        "drawCallback": function(settings, json) {
            $(tableEle).find('span.autonumber').autoNumeric('init')
        }

    });
    function regex_escape(text) {
        return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/\s+/g, "").replace(/¥/g, "");
    }
    ;// Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    //     table.on('click', 'button#refund', function(event) {
    //         if (!confirm($.i18n.prop('common.confirm.save'))) {
    //             return;
    //         }
    //         var rowData = table.row($(this).closest('tr')).data();
    //         var response = refundToCustomer(rowData.id)
    //         if (response.status === 'success') {

    //             // row.data(response.data).invalidate();
    //             table.ajax.reload();

    //         }

    //     })
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

    });

    var date;
    $('#table-filter-date').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        clearBtn: false
    }).on("change", function(e, picker) {
        date = $(this).val();
        $(this).closest('.input-group').find('.clear-date').remove();
        $('<div>', {
            'class': 'input-group-addon clear-date'
        }).append($('<i>', {
            'class': 'fa fa-times'
        })).appendTo($(this).closest('.input-group'))
        table.draw();
    });
    $('.input-group').on('click', '.clear-date', function() {
        date = '';
        $('#table-filter-date').val('');
        $(this).remove();
        table.draw();

    })

    $.getJSON(myContextPath + '/data/bank.json', function(data) {
        bankJson = data;
        $('#bankFilter').select2({
            allowClear: true,
            width: '100%',
            data: $.map(bankJson, function(item) {
                return {
                    id: item.bankSeq,
                    text: item.bankName,
                    data: item
                };
            })
        })
    })

    $('#bankFilter').change(function() {
        var selectedVal = $(this).find('option:selected').val();

        filterBank = $('#bankFilter').val();
        table.draw();
    });
    var filterBank = $('#bankFilter').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {

        //Due Date Filter
        if (typeof date != 'undefined' && date.length != '') {
            if (aData[0].length == 0 || aData[0] != date) {
                return false;
            }
        }
        if (typeof filterBank != 'undefined' && filterBank.length != '') {
            if (aData[12].length == 0 || aData[12] != filterBank) {
                return false;
            }
        }

        return true;
    });

    /* Customer Id Select2 Filter */

    $('#custId').select2({
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

    var tableUnitAllocation = $('#Unit-Allocation-details-table').DataTable({
        "pageLength": 25,
        "ajax": {
            url: myContextPath + "/sales/tt-unitAllocation-dataSource-list",
            data: function(data) {
                data.customer = $('#allocationModal').find('select[name="custId"]').val();
            },
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
            className: 'select-checkbox',
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox"  name="selRow" type="checkbox" data-stockno="' + row.stockNo + '">';
                }
                return data;
            }
        }, {
            targets: 1,

            "data": "stockNo",
            "render": function(data, type, row) {
                return '<input type="hidden" class="form-control" name="id"  value="' + row.id + '"/>' + data
            }

        }, {
            targets: 2,

            "data": "chassisNo",

        }, {
            targets: 3,

            "data": "etd",

        }, {
            targets: 4,

            "data": "total",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' ">' + data + '</span>';
            }
        }, {
            targets: 5,
            "data": "received",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' ">' + data + '</span>';
            }
        }, {
            targets: 6,

            "data": "balance",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' ">' + data + '</span>';
            }
        }, {
            targets: 7,

            "render": function(data, type, row) {
                var html = ''

                html += '<input type="text" class="form-control autonumber priceAllocation" data-v-min="0" data-a-sign="' + row.currencySymbol + ' " name="priceAllocation" />'
                return html;
            }
        }],
        "drawCallback": function(settings, json) {
            $('#Unit-Allocation-details-table span.autonumber,input.autonumber').autoNumeric('init')
        }

    })

    tableUnitAllocation.on('click', 'td>input[type="checkbox"]', function() {
        var tr = $(this).closest('tr');
        var data = tableUnitAllocation.row($(tr)).data();
        var balanceAmount = getAutonumericValue($('#allocationModal').find('input[name="balanceAmount"]'));
        var allocationAmount;
        if ($(this).is(':checked')) {
            if (data.balance <= balanceAmount) {
                setAutonumericValue($(tr).find('input[name="priceAllocation"]'), data.balance);
                updateFooter();
            } else {
                setAutonumericValue($(tr).find('input[name="priceAllocation"]'), balanceAmount);
                updateFooter();
            }

        } else {
            $(tr).find('input[name="priceAllocation"]').val('')
            updateFooter();
        }

    })

    tableUnitAllocation.on('keyup', 'input.priceAllocation', function() {

        updateFooter();

    })

    function updateFooter() {
        var totalAmount = getAutonumericValue($('#allocationModal').find('input[name="amount"]'));
        var balanceAmount = getAutonumericValue($('#allocationModal').find('input[name="balanceAmount"]'));

        var intVal = function(i) {
            return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
        };
        var isValid = function(val) {
            return typeof val === 'undefined' || val == null ? 0 : val;
        }
        var priceAllocationTotal = tableUnitAllocation.column(7, {
            page: 'All'
        }).nodes().reduce(function(a, b) {

            var amount = Number(isValid($(b).find('input[name="priceAllocation"]').autoNumeric('init').autoNumeric('get')));
            return intVal(a) + amount;
        }, 0);
        setAutonumericValue($('#allocationModal').find('input[name="allocatedAmount"]'), priceAllocationTotal);
        setAutonumericValue($('#allocationModal').find('input[name="balanceAmount"]'), (totalAmount - priceAllocationTotal));

    }

    var tableFifoAllocation = $('#Fifo-Allocation-details-table').DataTable({
        "pageLength": 25,

        "ajax": {
            url: myContextPath + "/sales/tt-fifoAllocation-dataSource-list",
            async: false,
            data: function(data) {
                data.customer = $('#allocationModal').find('select[name="custId"]').val();
            },
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
            render: function(data, type, row, meta) {
                return meta.row + meta.settings._iDisplayStart + 1;
            }
        }, {
            targets: 1,

            "data": "stockNo",
            "render": function(data, type, row) {
                return '<input type="hidden" class="form-control" name="id"  value="' + row.id + '"/>' + data
            }

        }, {
            targets: 2,

            "data": "chassisNo",

        }, {
            targets: 3,

            "data": "total",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' ">' + data + '</span>';
            }
        }, {
            targets: 4,
            "data": "received",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' ">' + data + '</span>';
            }
        }, {
            targets: 5,

            "data": "balance",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' ">' + data + '</span>';
            }
        }, {
            targets: 6,

            "render": function(data, type, row) {
                var html = ''

                html += '<input type="text" class="form-control autonumber priceAllocation" data-v-min="0" data-a-sign="' + row.currencySymbol + ' " name="priceAllocation"/>'
                return html;
            }
        }],
        "drawCallback": function(settings, json) {
            $('#Fifo-Allocation-details-table span.autonumber,input.autonumber').autoNumeric('init')
            $('#Fifo-Allocation-details-table input.priceAllocation').attr('readonly', true)
        }

    })

    function updateFifoTable() {
        var totalAmount = getAutonumericValue($('#allocationModal').find('input[name="amount"]'));
        var balanceAmount = getAutonumericValue($('#allocationModal').find('input[name="balanceAmount"]'));
        var amountCalculation = totalAmount;
        tableFifoAllocation.rows({
            page: 'current'
        }).every(function(rowIdx, tableLoop, rowLoop) {
            var tr = $(this.node());
            var data = tableFifoAllocation.row($(tr)).data();
            console.log(data)
            if (amountCalculation > 0) {
                if (data.balance <= amountCalculation) {
                    setAutonumericValue($(tr).find('input[name="priceAllocation"]'), data.balance);
                    amountCalculation -= data.balance;
                } else {
                    setAutonumericValue($(tr).find('input[name="priceAllocation"]'), amountCalculation);
                    amountCalculation = 0;
                }

            } else {
                setAutonumericValue($(tr).find('input[name="priceAllocation"]'), 0);
            }
        });
        var intVal = function(i) {
            return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
        };
        var isValid = function(val) {
            return typeof val === 'undefined' || val == null ? 0 : val;
        }
        var priceAllocationTotal = tableFifoAllocation.column(6, {
            page: 'All'
        }).nodes().reduce(function(a, b) {

            var amount = Number(isValid($(b).find('input[name="priceAllocation"]').autoNumeric('init').autoNumeric('get')));
            return intVal(a) + amount;
        }, 0);
        setAutonumericValue($('#allocationModal').find('input[name="allocatedAmount"]'), priceAllocationTotal);
        setAutonumericValue($('#allocationModal').find('input[name="balanceAmount"]'), (totalAmount - priceAllocationTotal));

    }

    // expand details
    $('#allocationModal').find('select[name="custId"],select[name="allocationType"]').on('change', function() {
        var allocationValue = $("#allocationType option:selected").text();
        var customer = $('#allocationModal').find('select[name="custId"]').val();
        if ((customer != "" || customer != null) && allocationValue == "Unit Allocation") {
            $('#hidden-table').removeClass('hidden');
            $('#hidden-table-fifo').addClass('hidden');
            tableUnitAllocation.ajax.reload()
            updateFooter();
        } else if ((customer != "" || customer != null) && allocationValue == "FIFO") {
            $('#hidden-table-fifo').removeClass('hidden');
            $('#hidden-table').addClass('hidden');
            tableFifoAllocation.ajax.reload()
            updateFifoTable()
        } else {
            $('#hidden-table-fifo').addClass('hidden');
            $('#hidden-table').addClass('hidden');
        }

    });

    $('#allocationModal').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var triggerElement = $(event.relatedTarget);
        var tr = triggerElement.closest('tr');
        var data = table.row(tr).data();
        var amount = ifNotValid(data.advanceOwned, '');
        var id = ifNotValid(data.daybookId, '');
        var itemElement = $('#allocationModal').closest('div');
        if (!isEmpty(data["customerId"])) {
            $.get({
                url: myContextPath + "/customer/data/" + data["customerId"],
                async: false
            }, function(data) {
                custData = data.data;
                let selectCustomer = itemElement.find('select[name="custId"]');
                selectCustomer.empty();
                let option = $('<option value="' + custData.code + '">' + custData.companyName + ' :: ' + custData.firstName + '(' + custData.nickName + ')</option>');
                option.data('data', {
                    id: custData.code,
                    text: custData.companyName + ' :: ' + custData.firstName + '(' + custData.nickName + ')',
                    data: custData
                });
                option.appendTo(selectCustomer);
                selectCustomer.val(custData.code).trigger("change")

            });
        }
        $(itemElement).find('select[name="custId"]').attr('disabled', true)
        $(itemElement).find('input[name="amount"].autonumber').val(amount).autoNumeric('init').autoNumeric('update', {
            aSign: data.customerCurrencySymbol + ' ',
            mDec: (data.customerCurrency == 2) ? 2 : 0
        });
        $(itemElement).find('input[name="amount"]#amount').val(amount);
        $(itemElement).find('input[name="balanceAmount"].autonumber').val(amount).autoNumeric('init').autoNumeric('update', {
            aSign: data.customerCurrencySymbol + ' ',
            mDec: (data.customerCurrency == 2) ? 2 : 0
        });
        $(itemElement).find('input[name="balanceAmount"]#balanceAmount').val(amount);
        $(itemElement).find('input[name="allocatedAmount"].autonumber').val(0).autoNumeric('init').autoNumeric('update', {
            aSign: data.customerCurrencySymbol + ' ',
            mDec: (data.customerCurrency == 2) ? 2 : 0
        });
        $(itemElement).find('input[name="allocatedAmount"]#allocatedAmount').val(0);
        $(itemElement).find('input[name="refId"]').val(id);
        $(itemElement).find('input[name="customerId"]').val(data.customerId);
        $(itemElement).find('input[name="transId"]').val(data.id);
        $(itemElement).find('input[name="currency"]').val(data.currency);

    }).on('hidden.bs.modal', function() {
        resetElementInput(this)
        $(this).find('select[name="allocationType"]').val('');
        $(this).find('.select2-hidden-accessible').val('').trigger('change');
        $('#hidden-table').addClass('hidden');
    });

    $("#save-ttAllocation").on('click', function() {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        var object;
        var transactionDetails = $('#allocationModal')
        object = getFormData(transactionDetails.find('input,select'));
        var queryString = "?customerId=" + object.customerId + "&allocationType=" + object.allocationType + "&amount=" + object.amount + "&daybookId=" + object.refId + "&transId=" + object.transId;
        if (object.allocationType == 2) {
            var dataArr = [];
            tableUnitAllocation.rows().every(function(rowIdx, tableLoop, rowLoop) {
                var tr = tableUnitAllocation.row(this).node();
                var data = {
                    'id': $(tr).find('input[name="id"]').val(),
                    'priceAllocation': getAutonumericValue($(tr).find('input[name="priceAllocation"]'))
                }
                dataArr.push(data);
            })

        }
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "POST",
            async: false,
            data: JSON.stringify(dataArr),
            url: myContextPath + "/sales/tt-reAllocate" + queryString,
            contentType: "application/json",
            success: function(data) {
                if (data.status == "success") {
                    $('#allocationModal').modal('toggle');
                    table.ajax.reload();
                } else {
                    alert(data.message);
                }

            },
            error: function(e) {
                console.log("ERROR: ", e);
            }
        });
    })

    $('#modal-refund-amount').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var triggerElement = $(event.relatedTarget);
        var tr = triggerElement.closest('tr');
        var data = table.row(tr).data();
        var amount = ifNotValid(data.amount, '');
        var id = ifNotValid(data.daybookId, '');
        //         var itemElement = $('#allocationModal').closest('div');
        $(this).find('input[name="amount"].autonumber').val(amount).autoNumeric('init').autoNumeric('update', {
            aSign: data.currencySymbol + ' ',
            mDec: (data.currency == 2) ? 2 : 0
        });
        $(itemElement).find('input[name="refId"]').val(id);
        $(itemElement).find('input[name="transId"]').val(data.id);

    }).on('hidden.bs.modal', function() {
        resetElementInput(this)
        $(this).find('.select2-hidden-accessible').val('').trigger('change');
        $('#hidden-table').addClass('hidden');
    });

    $("#approve").on('click', function() {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        var object;
        var transactionDetails = $('#modal-refund-amount')
        object = getFormData(transactionDetails.find('input'));
        var queryString = "?amount=" + object.amount + "&daybookId=" + object.refId + "&id=" + object.transId;

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            async: false,
            url: myContextPath + "/daybook/transaction/customer-refund" + queryString,
            contentType: "application/json",
            success: function(data) {
                if (data.status == "success") {
                    $('#modal-refund-amount').modal('toggle');
                    table.ajax.reload();
                }
            }
        });
    })
})
