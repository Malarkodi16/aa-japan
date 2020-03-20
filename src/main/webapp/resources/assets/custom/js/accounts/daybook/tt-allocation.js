//own pop up dataTable hidden
$('#hidden-table').addClass('hidden');

$(function() {
    $.ajaxSetup({
        async: false
    });

    var table = $('#table-ttAllocation').DataTable({

        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/sales/tt-allocation-dataSource",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },

        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "data": "remitDate",

        }, {
            targets: 1,
            "data": "remitTypeName"
        }, {
            targets: 2,
            "data": "remitter"
        }, {
            targets: 3,
            "data": "bankName"
        }, {
            targets: 4,
            "className": "dt-right details-control",
            "data": "amount",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' "data-m-dec="' + (row.currency != 2 ? 0 : 2) + '">' + data + '</span>';
            }
        }, {
            targets: 5,
            "className": "dt-right details-control",
            "data": "balanceAmount",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="' + row.currencySymbol + ' "data-m-dec="' + (row.currency != 2 ? 0 : 2) + '">' + data + '</span>';
            }
        }, {
            targets: 6,
            "render": function(data, type, row) {
                var html = ''
                html += '<button href="#" class="ml-5 btn btn-primary btn-xs" data-toggle="modal" data-target="#allocationModal" data-backdrop="static" data-keyboard="false">Own</button>'
                return html;
            }
        }, {
            targets: 7,
            "data": "bank",
            "visible": false,
        }, {
            targets: 8,
            "data": "remitType",
            "visible": false,
        }],
        "drawCallback": function(settings, json) {
            $('#table-ttAllocation span.autonumber').autoNumeric('init')
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

    $.getJSON(myContextPath + "/data/mRemitType.json", function(data) {
        mRemitTypeJson = data;
        $('#remitTypeFilter').select2({
            allowClear: true,
            width: '100%',
            placeholder: 'All',
            data: $.map(mRemitTypeJson, function(item) {
                return {
                    id: item.remitSeq,
                    text: item.remitType,
                };
            })
        })

    });
    $('#bankFilter').change(function() {
        var selectedVal = $(this).find('option:selected').val();

        filterBank = $('#bankFilter').val();
        table.draw();
    });

    $('#remitTypeFilter').change(function() {
        var selectedVal = $(this).find('option:selected').val();

        filterRemType = $('#remitTypeFilter').val();
        table.draw();
    });

    var filterBank = $('#bankFilter').find('option:selected').val();
    var filterRemType = $('#remitTypeFilter').find('option:selected').val();
    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {

        //Due Date Filter
        if (typeof date != 'undefined' && date.length != '') {
            if (aData[0].length == 0 || aData[0] != date) {
                return false;
            }
        }
        if (typeof filterBank != 'undefined' && filterBank.length != '') {
            if (aData[7].length == 0 || aData[7] != filterBank) {
                return false;
            }
        }
        if (typeof filterRemType != 'undefined' && filterRemType.length != '') {
            if (aData[8].length == 0 || aData[8] != filterRemType) {
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

    var tableLcAllocation = $('#Lc-Allocation-details-table').DataTable({
        "pageLength": 25,
        "ajax": {
            url: myContextPath + "/sales/tt-lcAllocation-dataSource-list",
            data: function(data) {
                data.daybookId = $('#allocationModal').find('input[name="refId"]').val();
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
                return '<span class="autonumber" data-a-sign="¥ ">' + data + '</span>';
            }
        }, {
            targets: 4,
            "data": "received",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="¥ ">' + data + '</span>';
            }
        }, {
            targets: 5,

            "data": "lcBalance",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-a-sign="¥ ">' + data + '</span>';
            }
        }, {
            targets: 6,

            "render": function(data, type, row) {
                var html = ''

                html += '<input type="text" class="form-control autonumber priceAllocation" data-v-min="0" data-a-sign="¥ " name="priceAllocation" />'
                return html;
            }
        }],
        "drawCallback": function(settings, json) {
            $('#Lc-Allocation-details-table span.autonumber,input.autonumber').autoNumeric('init')
        }

    })

    tableLcAllocation.on('keyup', 'input.priceAllocation', function() {

        updateLcFooter();

    })

    function updateLcFooter() {
        var totalAmount = getAutonumericValue($('#allocationModal').find('input[name="actualAmount"]'));
        var advanceAmount = getAutonumericValue($('#allocationModal').find('input[name="advanceAmount"]'));
        var balanceAmount = getAutonumericValue($('#allocationModal').find('input[name="balanceAmount"]'));
        var amountCalculation = totalAmount;
        //         tableLcAllocation.rows({
        //             page: 'current'
        //         }).every(function(rowIdx, tableLoop, rowLoop) {
        //             var tr = $(this.node());
        //             var data = tableLcAllocation.row($(tr)).data();
        //             console.log(data)
        //             if (amountCalculation > 0) {
        //                 if (data.balance <= amountCalculation) {
        //                     setAutonumericValue($(tr).find('input[name="priceAllocation"]'), data.balance);
        //                     amountCalculation -= data.balance;
        //                 } else {
        //                     setAutonumericValue($(tr).find('input[name="priceAllocation"]'), amountCalculation);
        //                     amountCalculation = 0;
        //                 }

        //             } else {
        //                 setAutonumericValue($(tr).find('input[name="priceAllocation"]'), 0);
        //             }
        //         });

        var intVal = function(i) {
            return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
        };
        var isValid = function(val) {
            return typeof val === 'undefined' || val == null ? 0 : val;
        }
        var priceAllocationTotal = tableLcAllocation.column(6, {
            page: 'All'
        }).nodes().reduce(function(a, b) {

            var amount = Number(isValid($(b).find('input[name="priceAllocation"]').autoNumeric('init').autoNumeric('get')));
            return intVal(a) + amount;
        }, 0);
        priceAllocationTotal = priceAllocationTotal + Number(advanceAmount);
        setAutonumericValue($('#allocationModal').find('input[name="advanceAmount"]'), advanceAmount);
        setAutonumericValue($('#allocationModal').find('input[name="allocatedAmount"]'), priceAllocationTotal);
        setAutonumericValue($('#allocationModal').find('input[name="balanceAmount"]'), (totalAmount - priceAllocationTotal));

    }

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
        var totalAmount = getAutonumericValue($('#allocationModal').find('input[name="actualAmount"]'));
        var advanceAmount = getAutonumericValue($('#allocationModal').find('input[name="advanceAmount"]'));
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
        priceAllocationTotal = priceAllocationTotal + Number(advanceAmount);
        setAutonumericValue($('#allocationModal').find('input[name="advanceAmount"]'), advanceAmount);
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
        var totalAmount = getAutonumericValue($('#allocationModal').find('input[name="actualAmount"]'));
        var advanceAmount = getAutonumericValue($('#allocationModal').find('input[name="advanceAmount"]'));
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
        priceAllocationTotal += Number(advanceAmount);
        setAutonumericValue($('#allocationModal').find('input[name="allocatedAmount"]'), priceAllocationTotal);
        setAutonumericValue($('#allocationModal').find('input[name="balanceAmount"]'), (totalAmount - priceAllocationTotal));

    }

    // expand details
    $('#allocationModal').find('select[name="custId"],select[name="allocationType"],select[name="exchangeRate"],input[name="exchangeRate1"],input[name="exchangeRate2"]').on('change', function() {
        var allocationValue = $("#allocationType option:selected").text();
        var allocationType = $("#allocationType option:selected").val();
        var currency = $('#allocationModal').find('input[name="currency"]').val();
        var daybookId = $('#allocationModal').find('input[name="refId"]').val();
        var customer = $('#allocationModal').find('select[name="custId"]').val();
        var customerData = $('#allocationModal').find('select[name="custId"]').find('option:selected').data('data');
        var totalAmount = getAutonumericValue($('#allocationModal').find('input[name="amount"]'));
        var exchangeRate = $('#allocationModal').find('select[name="exchangeRate"]').val();
        var exchangeRate1 = $('#allocationModal').find('input[name="exchangeRate1"]').val();
        var exchangeRate2 = $('#allocationModal').find('input[name="exchangeRate2"]').val();
        if ((customer != "" || customer == null) && !isEmpty(allocationType)) {
            if (customerData.data.currencyType == 1 || currency == 1) {
                $('#exchangeRate1').val(1)
                $('#exRate1').addClass('hidden')
            } else {
                $('#exRate1').removeClass('hidden')
            }
            if (!isEmpty(exchangeRate) && exchangeRate == 4) {
                $('#exchangeRate1').val('').prop('readonly', false)
                $('#exchangeRate2').val('').prop('readonly', false)
            } else {
                $('#exchangeRate1').prop('readonly', true)
                $('#exchangeRate2').prop('readonly', true)
            }

            var queryString = "?daybookId=" + daybookId + "&currency=" + currency + "&cstmrCurrency=" + customerData.data.currencyType + "&sourceAmt=" + totalAmount + "&exchangeValue=" + exchangeRate;
            if (allocationValue == "Unit Allocation") {
                $('#advance').removeClass('hidden');
                if (customerData.data.currencyType != currency) {
                    $('#rateFilter').removeClass('hidden');
                    if (!isEmpty(exchangeRate) && exchangeRate != 4) {
                        let response = exchangeRateChecking(queryString);
                        var data = response.data;
                        if (response.status == "success") {
                            $('#hidden-table').removeClass('hidden');
                            modalAllocation(data, customerData.data.currencyType)
                        }

                    } else if (!isEmpty(exchangeRate) && exchangeRate == 4 && !isEmpty(exchangeRate1) && !isEmpty(exchangeRate2)) {
                        modalReAllocation(customerData.data.currencyType)
                        $('#hidden-table').removeClass('hidden');
                    } else {
                        $('#hidden-table').addClass('hidden');
                    }
                } else {
                    $('#rateFilter').addClass('hidden');
                    let response = exchangeRateChecking(queryString);
                    var data = response.data;
                    if (response.status == "success") {
                        $('#hidden-table').removeClass('hidden');
                        modalAllocation(data, customerData.data.currencyType)
                    }
                }

                $('#hidden-table-fifo').addClass('hidden');
                $('#hidden-table-lc').addClass('hidden');
                tableUnitAllocation.ajax.reload()
                updateFooter();
            } else if (allocationValue == "FIFO") {
                $('#advance').removeClass('hidden');
                if (customerData.data.currencyType != currency) {
                    $('#rateFilter').removeClass('hidden');
                    if (!isEmpty(exchangeRate) && exchangeRate != 4) {
                        let response = exchangeRateChecking(queryString);
                        var data = response.data;
                        if (response.status == "success") {
                            $('#hidden-table-fifo').removeClass('hidden');
                            modalAllocation(data, customerData.data.currencyType)
                        }

                    } else if (!isEmpty(exchangeRate) && exchangeRate == 4 && !isEmpty(exchangeRate1) && !isEmpty(exchangeRate2)) {
                        modalReAllocation(customerData.data.currencyType)
                        $('#hidden-table-fifo').removeClass('hidden');
                    } else {
                        $('#hidden-table-fifo').addClass('hidden');
                    }
                } else {
                    $('#rateFilter').addClass('hidden');
                    let response = exchangeRateChecking(queryString);
                    var data = response.data;
                    if (response.status == "success") {
                        $('#hidden-table-fifo').removeClass('hidden');
                        modalAllocation(data, customerData.data.currencyType)
                    }
                }
                $('#hidden-table').addClass('hidden');
                $('#hidden-table-lc').addClass('hidden');
                tableFifoAllocation.ajax.reload()
                updateFifoTable()

            } else if (allocationValue == "LC") {
                $('#advance').removeClass('hidden');
                $('#rateFilter').addClass('hidden');
                let response = exchangeRateChecking(queryString);
                var data = response.data;
                if (response.status == "success") {
                    $('#hidden-table-lc').removeClass('hidden');
                    modalAllocation(data, customerData.data.currencyType)
                }

                $('#hidden-table').addClass('hidden');
                $('#hidden-table-fifo').addClass('hidden');
                tableLcAllocation.ajax.reload()
                updateLcFooter()

            } else {
                $('#advance').removeClass('hidden');
                if (customerData.data.currencyType != currency) {
                    $('#rateFilter').removeClass('hidden');
                    if (!isEmpty(exchangeRate) && exchangeRate != 4) {
                        let response = exchangeRateChecking(queryString);
                        var data = response.data;
                        if (response.status == "success") {
                            modalAllocation(data, customerData.data.currencyType)
                        }

                    } else if (!isEmpty(exchangeRate) && exchangeRate == 4 && !isEmpty(exchangeRate1) && !isEmpty(exchangeRate2)) {
                        modalReAllocation(customerData.data.currencyType)
                    }
                } else {
                    $('#rateFilter').addClass('hidden');
                    let response = exchangeRateChecking(queryString);
                    var data = response.data;
                    if (response.status == "success") {
                        modalAllocation(data, customerData.data.currencyType)
                    }
                }
                $('#hidden-table').addClass('hidden');
                $('#hidden-table-fifo').addClass('hidden');
                $('#hidden-table-lc').addClass('hidden');
            }
        } else {
            $('#rateFilter').addClass('hidden');
            $('#advance').addClass('hidden');
            $('#hidden-table').addClass('hidden');
            $('#hidden-table-fifo').addClass('hidden');
            $('#hidden-table-lc').addClass('hidden');
        }

    });

    $('#allocationModal').on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        var triggerElement = $(event.relatedTarget);
        var tr = triggerElement.closest('tr');
        var data = table.row(tr).data();
        var amount = ifNotValid(data.balanceAmount, '');
        var id = ifNotValid(data.daybookId, '');
        var itemElement = $('#allocationModal').closest('div');
        if (!isEmpty(data["customer"])) {
            $.get({
                url: myContextPath + "/customer/data/" + data["customer"],
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
        let select = $(itemElement).find('select[name="exchangeRate"]');
        $(itemElement).find('input[name="amount"].autonumber').val(amount).autoNumeric('init').autoNumeric('update', {
            aSign: data.currencySymbol + ' ',
            mDec: (data.currency == 2) ? 2 : 0
        });
        $(itemElement).find('input[name="amount"]#amount').val(amount);
        $(itemElement).find('input[name="balanceAmount"].autonumber').val(amount).autoNumeric('init').autoNumeric('update', {
            aSign: data.currencySymbol + ' ',
            mDec: (data.currency == 2) ? 2 : 0
        });
        $(itemElement).find('input[name="balanceAmount"]#balanceAmount').val(amount);
        $(itemElement).find('input[name="allocatedAmount"].autonumber').val(0).autoNumeric('init').autoNumeric('update', {
            aSign: data.currencySymbol + ' ',
            mDec: (data.currency == 2) ? 2 : 0
        });
        $(itemElement).find('input[name="allocatedAmount"]#allocatedAmount').val(0);
        setAutonumericValue($(itemElement).find('input[name="actualAmount"].autonumber'), 0);
        $(itemElement).find('input[name="actualAmount"]#actualAmount').val(0);
        $(itemElement).find('input[name="advanceAmount"].autonumber').val(0).autoNumeric('init').autoNumeric('update', {
            aSign: data.currencySymbol + ' ',
            mDec: (data.currency == 2) ? 2 : 0
        });
        $(itemElement).find('input[name="advanceAmount"]#advanceAmount').val(0);
        $(itemElement).find('input[name="refId"]').val(id);
        $(itemElement).find('input[name="currency"]').val(data.currency);

    }).on('hidden.bs.modal', function() {
        resetElementInput(this)
        $(this).find('select[name="exchangeRate"]').val('');
        $(this).find('select[name="allocationType"]').val('');
        $(this).find('.select2-hidden-accessible').val('').trigger('change');
        $('#hidden-table').addClass('hidden');
    }).on('keyup', 'input[name="advanceAmount"]', function() {
        let select = $('#allocationModal').find('select[name="allocationType"]').val();
        if (select == 1) {
            updateFifoTable();
        } else if (select == 2) {
            updateFooter();
        } else if (select == 5) {
            updateLcFooter();
        } else {
            var totalAmount = getAutonumericValue($('#allocationModal').find('input[name="actualAmount"]'));
            var advanceAmount = getAutonumericValue($('#allocationModal').find('input[name="advanceAmount"]'));
            var balanceAmount = getAutonumericValue($('#allocationModal').find('input[name="balanceAmount"]'));
            setAutonumericValue($('#allocationModal').find('input[name="advanceAmount"]'), advanceAmount);
            setAutonumericValue($('#allocationModal').find('input[name="allocatedAmount"]'), advanceAmount);
            setAutonumericValue($('#allocationModal').find('input[name="balanceAmount"]'), (totalAmount - advanceAmount));
        }

    });

    $("#save-ttAllocation").on('click', function() {

        var object;
        var transactionDetails = $('#allocationModal')
        object = getFormData(transactionDetails.find('input,select'));
        var advanceAmount = getAutonumericValue(transactionDetails.find('input[name="advanceAmount"]'))
        var balanceAmount = getAutonumericValue(transactionDetails.find('input[name="balanceAmount"]'))

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

        } else if (object.allocationType == 5) {
            var dataArr = [];
            tableLcAllocation.rows().every(function(rowIdx, tableLoop, rowLoop) {
                var tr = tableLcAllocation.row(this).node();
                var data = {
                    'id': $(tr).find('input[name="id"]').val(),
                    'priceAllocation': getAutonumericValue($(tr).find('input[name="priceAllocation"]'))
                }
                dataArr.push(data);
            })
        }
        if (advanceAmount > 0 || balanceAmount <= 0) {
            if (!confirm($.i18n.prop('common.confirm.save'))) {
                return false;
            } else {
                var queryString = "?customerId=" + object.custId + "&allocationType=" + object.allocationType + "&amount=" + object.amount + "&daybookId=" + object.refId + "&exchangeRate1=" + object.exchangeRate1 + "&exchangeRate2=" + object.exchangeRate2 + "&advanceAmount=" + advanceAmount;
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
                    url: myContextPath + "/sales/tt-create" + queryString,
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
            }
        } else {
            var amount = prompt('Do you Want to allocate balance amount as advance?', balanceAmount, 'prompt')
            if (amount > balanceAmount) {
                alert('Advance Amount should be less than or equal to the balance amount')
            }
            if (amount) {
                advanceAmount = amount;
            }
            var queryString = "?customerId=" + object.custId + "&allocationType=" + object.allocationType + "&amount=" + object.amount + "&daybookId=" + object.refId + "&exchangeRate1=" + object.exchangeRate1 + "&exchangeRate2=" + object.exchangeRate2 + "&advanceAmount=" + advanceAmount;
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
                url: myContextPath + "/sales/tt-create" + queryString,
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

        }

    })
})
function getTransporterFee(element) {
    var response;
    var closest_container = $('#transportInfo');
    var category = $('#subcategory').val();
    var from = closest_container.find('#transportPickupLoc').val()
    var to = closest_container.find('#transportDropupLoc').val()
    var queryString = "?category=" + category + "&from=" + from + "&to=" + to
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: 'get',
        async: false,
        url: myContextPath + "/transport/charge" + queryString,
        success: function(data) {
            response = data;
        }
    })
    return response;
}
function exchangeRateChecking(queryString) {
    var response;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        url: myContextPath + "/accounts/exchangerate/checking" + queryString,
        type: "get",
        cache: false,
        dataType: "json",
        async: false,
        success: function(data) {
            response = data;
        }

    })
    return response;

}

function modalAllocation(data, currency) {
    $('#allocationModal').find('input[name="actualAmount"].autonumber').val(data.actualAmount).autoNumeric('init').autoNumeric('update', {
        aSign: data.currencySymbol,
        mDec: (currency == 2) ? 2 : 0
    });
    $('#allocationModal').find('input[name="actualAmount"]#actualAmount').val(data.actualAmount)
    $('#allocationModal').find('input[name="balanceAmount"].autonumber').val(data.actualAmount).autoNumeric('init').autoNumeric('update', {
        aSign: data.currencySymbol,
        mDec: (currency == 2) ? 2 : 0
    });
    $('#allocationModal').find('input[name="balanceAmount"]#balanceAmount').val(data.actualAmount)
    $('#allocationModal').find('input[name="allocatedAmount"].autonumber').val(0).autoNumeric('init').autoNumeric('update', {
        aSign: data.currencySymbol,
        mDec: (currency == 2) ? 2 : 0
    });
    $('#allocationModal').find('input[name="allocatedAmount"]#allocatedAmount').val(0)
    $('#allocationModal').find('input[name="advanceAmount"].autonumber').val(0).autoNumeric('init').autoNumeric('update', {
        aSign: data.currencySymbol,
        mDec: (currency == 2) ? 2 : 0
    });
    $('#allocationModal').find('input[name="advanceAmount"]#advanceAmount').val(0)
    $('#allocationModal').find('input[name="exchangeRate1"]').val(data.exchangeRate1);
    $('#allocationModal').find('input[name="exchangeRate2"]').val(data.exchangeRate2);
}

function modalReAllocation(customerCurrency) {
    var amount = 0;
    var currency = $('#allocationModal').find('input[name="currency"]').val();
    var exchangeRate1 = $('#allocationModal').find('input[name="exchangeRate1"]').val();
    var exchangeRate2 = $('#allocationModal').find('input[name="exchangeRate2"]').val();
    var srcAmt = getAutonumericValue($('#allocationModal').find('input[name="amount"]'));
    var currencySymbol = (customerCurrency == 1) ? '¥ ' : (customerCurrency == 2) ? '$ ' : 'AUS $ ';
    if (customerCurrency == 1 && currency != 1) {
        amount = (srcAmt / exchangeRate1) * exchangeRate2;
    } else {
        amount = (srcAmt * exchangeRate1) / exchangeRate2;
    }
    $('#allocationModal').find('input[name="actualAmount"].autonumber').val(amount).autoNumeric('init').autoNumeric('update', {
        aSign: currencySymbol,
        mDec: (customerCurrency == 2) ? 2 : 0
    });
    $('#allocationModal').find('input[name="actualAmount"]#actualAmount').val(amount)
    $('#allocationModal').find('input[name="balanceAmount"].autonumber').val(amount).autoNumeric('init').autoNumeric('update', {
        aSign: currencySymbol,
        mDec: (customerCurrency == 2) ? 2 : 0
    });
    $('#allocationModal').find('input[name="balanceAmount"]#balanceAmount').val(amount)
    $('#allocationModal').find('input[name="allocatedAmount"].autonumber').val(0).autoNumeric('init').autoNumeric('update', {
        aSign: currencySymbol,
        mDec: (customerCurrency == 2) ? 2 : 0
    });
    $('#allocationModal').find('input[name="allocatedAmount"]#allocatedAmount').val(0)
    $('#allocationModal').find('input[name="advanceAmount"].autonumber').val(0).autoNumeric('init').autoNumeric('update', {
        aSign: currencySymbol,
        mDec: (customerCurrency == 2) ? 2 : 0
    });
    $('#allocationModal').find('input[name="advanceAmount"]#advanceAmount').val(0)
}
