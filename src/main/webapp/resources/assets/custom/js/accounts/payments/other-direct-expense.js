var tableStorage, metaData = [];
$(function() {

    //for edit
    var code = $('#invoiceNo').val();
    if (!isEmpty(code)) {
        update(code)
    }

    $.ajaxSetup({
        async: false
    });

    $('.select2-select').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true

    })
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })

    $.getJSON(myContextPath + "/data/accounts/payment-booking-count", function(data) {
        setPaymentBookingDashboardStatus(data.data)
    });

    $.getJSON(myContextPath + "/data/accname.json", function(data) {
        //         var coacategorytypeJson = data;
        $('#add-new-category select#accountType').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.code,
                    text: item.subAccount + '(' + item.account + ')' + '(' + item.code + ')',
                };
            })
        }).val('').trigger("change");
    })

    $.getJSON(myContextPath + "/data/otherDirectExpense.json", function(data) {

        $('select[name="requested-type-filter"]').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.code,
                    text: item.type
                };
            })
        });
    })

    $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
        supplierJson = data;
        $('select#supplier').select2({
            allowClear: true,
            width: '100%',
            data: $.map(supplierJson, function(item) {
                return {
                    id: item.supplierCode,
                    text: item.company,
                    data: item
                };
            })
        }).on('change', function() {//             purchasedSupplierFilter = $(this).val();
        //             table.draw();
        })
    });
    $.getJSON(myContextPath + "/data/transporters.json", function(data) {
        transportersJson = data;
        $('select#transportSupplier').select2({
            allowClear: true,
            width: '100%',
            data: $.map(transportersJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).on("change", function(event) {//             transportSupplierFilter = $(this).val();
        //             table.draw();
        })

    });
    $.getJSON(myContextPath + "/data/forwarders.json", function(data) {
        $('select#forwarderSupplier').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).on("change", function(event) {//             forwarderSupplierFilter = $(this).val();
        //             table.draw();
        })
    })
    $.getJSON(myContextPath + "/data/general/suppliers.json", function(data) {
        remitterJson = data;
        $('select#genaralSupplier').select2({
            allowClear: true,
            width: '100%',
            data: $.map(remitterJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).on("change", function(event) {//             genaralSupplierFilter = $(this).val();
        //             table.draw();
        })

    })
    $.getJSON(myContextPath + "/data/inspectioncompanies.json", function(data) {
        inspCompanyJson = data;
        $('select#inspectionCompany').select2({
            allowClear: true,
            width: '100%',
            data: $.map(inspCompanyJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).on("change", function(event) {//             inspectionCompanyFilter = $(this).val();
        //             table.draw();
        })

    })

    $('input[type="checkbox"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    })

    $('#invoiceTypeFilter').select2({
        width: '100%',
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true,
    }).change(function() {
        invoiceTypeFlag = $(this).val();
        if (isEmpty(invoiceTypeFlag)) {
            $('div.supplirFilter').hide();
        } else if (invoiceTypeFlag == 0) {
            $('div.supplirFilter').hide()
            $('div.supplirFilter').find('select[name="supplier"]').val('').trigger('change')
            $('div#purchasedSupplierWrapper').show();
        } else if (invoiceTypeFlag == 1) {
            $('div.supplirFilter').hide()
            $('div.supplirFilter').find('select[name="supplier"]').val('').trigger('change')
            $('div#transportSupplierWrapper').show();
        } else if (invoiceTypeFlag == 2 || invoiceTypeFlag == 3) {
            $('div.supplirFilter').hide()
            $('div.supplirFilter').find('select[name="supplier"]').val('').trigger('change')
            $('div#forwarderSupplierWrapper').show();
        } else if (invoiceTypeFlag == 4) {
            $('div.supplirFilter').hide()
            $('div.supplirFilter').find('select[name="supplier"]').val('').trigger('change')
            $('div#genaralSupplierWrapper').show();
        } else if (invoiceTypeFlag == 5) {
            $('div.supplirFilter').hide()
            $('div.supplirFilter').find('select[name="supplier"]').val('').trigger('change')
            $('div#inspectionCompanyWrapper').show();
        }
    })

    $('#showFields').on('click', function() {
        var values = $('select[name="requested-type-filter"]').find('option:selected').val();
        $('#editFields').removeClass('hidden')
        $('#showFields').addClass('hidden')
        if (!isEmpty(values)) {
            $('#payment-item-table').removeClass('hidden')
            var cloningElement = $('#table-cloning-element').find('table').clone();
            $('#payment-item-table').append(cloningElement);
            var columnSpan = 3;
            $.each($('select[name="requested-type-filter"] option:selected'), function() {
                columnSpan += 1;
                var value = $(this).text();
                var attribute = $(this).val();
                var object = {}
                object.title = value;
                object.attribute = attribute;
                metaData.push(object);
                $('<th><label>&nbsp;</label><div class="form-control col-md-1 pull right" style="width: 45px"><input type="checkbox" id="' + attribute + '" name="' + attribute + 'TaxInclusive" class="form-control chargeData taxInclusive minimal" /></div><label class="mt-10" style="width: 175px">' + value + '</label></th>').appendTo($('#payment-item-table').find('tr.header'))
                $('<td><div class="form-inline">' + '<input type="text" name="' + attribute + '"  id="' + attribute + '" class="form-control chargeData amount priceCalc" data-a-sign="&yen; " data-v-min="0" data-m-dec="0" style="width: 150px"></br>' + '<input type="hidden" name="' + attribute + 'Tax" class="form-control amount hiddenTaxPer mt-5"  data-v-max="100" data-v-min="0" data-m-dec="0" data-a-sign=" %" data-p-sign="s">' + '<input type="text" name="' + attribute + 'TaxPer" class="form-control amount chargeData taxPer mr-5 mt-5" value="10" data-a-sign=" %" data-p-sign="s" data-m-dec="0" style="width: 55px">' + '<input type="hidden" name="hidden' + attribute + 'TaxAmt" class="form-control amount hiddenTaxValue mt-5" data-m-dec="0" data-a-sign="¥ " data-p-sign="s">' + '<input type="text" name="' + attribute + 'TaxAmount" class="form-control amount chargeData taxCalc ml-5 mt-5" data-a-sign="¥ " data-m-dec="0" style="width: 85px">' + '<input type="hidden" name="' + attribute + 'TotalAmount" class="form-control chargeData amount totalAmount ml-5 mt-5" data-a-sign="¥ " data-m-dec="0">' + '</div></td>').appendTo($('#payment-item-table').find('tr.clone-row'))
                $('<td><div class="form-inline">' + '<input type="text" name="' + attribute + '"  id="' + attribute + '" class="form-control chargeData amount priceCalc" data-a-sign="&yen; " data-v-min="0" data-m-dec="0" style="width: 150px"></br>' + '<input type="hidden" name="' + attribute + 'Tax" class="form-control amount hiddenTaxPer mt-5"  data-v-max="100" data-v-min="0" data-m-dec="0" data-a-sign=" %" data-p-sign="s">' + '<input type="text" name="' + attribute + 'TaxPer" class="form-control amount chargeData taxPer mr-5 mt-5" value="10" data-a-sign=" %" data-p-sign="s" data-m-dec="0" style="width: 55px">' + '<input type="hidden" name="hidden' + attribute + 'TaxAmt" class="form-control amount hiddenTaxValue mt-5" data-m-dec="0" data-a-sign="¥ " data-p-sign="s">' + '<input type="text" name="' + attribute + 'TaxAmount" class="form-control amount chargeData taxCalc ml-5 mt-5" data-a-sign="¥ " data-m-dec="0" style="width: 85px">' + '<input type="hidden" name="' + attribute + 'TotalAmount" class="form-control chargeData amount totalAmount ml-5 mt-5" data-a-sign="¥ " data-m-dec="0">' + '</div></td>').appendTo($('#payment-item-table').find('tr.first-row'))
                $('<th></th>').appendTo($('#payment-item-table').find('tfoot>tr.sum'))
                $('<th></th>').appendTo($('#payment-item-table').find('tfoot>tr.taxtotal'))
                $('<th></th>').appendTo($('#payment-item-table').find('tfoot>tr.grandTotal'))

            });
            $('<th><label>Remarks</label></th>').appendTo($('#payment-item-table').find('tr.header'))
            $('<td><div class="form-group div-width-120px"><input class="form-control chargeData" name="remarks" id="remarks" type="text"></div></td>').appendTo($('#payment-item-table').find('tr.first-row'))
            $('<td><div class="form-group div-width-120px"><input class="form-control chargeData" name="remarks" id="remarks" type="text"></div></td>').appendTo($('#payment-item-table').find('tr.clone-row'))
            $('#payment-item-table').find('table>thead>tr.columnHead').find('>th.head-column').attr('colspan', columnSpan);
            $('#payment-item-table').find('table>tbody>tr').find('.amount').autoNumeric('init');
            $('input[type="checkbox"].minimal').iCheck({
                checkboxClass: 'icheckbox_minimal-blue',
                radioClass: 'iradio_minimal-blue'
            })
            var cloneStockNoFirstRow = $('#payment-item-table').find('tr.first-row').find('.stockNo');
            initStockSearchSelect2(cloneStockNoFirstRow);
            $('select[name="requested-type-filter"]').attr('disabled', true);

        } else {
            $('#payment-item-table').addClass('hidden')
        }

    })

    $('#editFields').on('click', function() {
        $('select[name="requested-type-filter"]').val([]).trigger('change');
        $('select[name="requested-type-filter"]').attr('disabled', false);
        $('#payment-item-table').html('')
        $('#showFields').removeClass('hidden')
        $('#editFields').addClass('hidden')
        metaData = [];

    })

    $('#payment-item-table').on("change", 'td input.priceCalc, td input.taxPer', function(setting) {
        let td = $(this).closest('td');
        var th = td.closest('table').find('th').eq(td.index() + 1);
        var totalAmt = Number(ifNotValid(td.find('input.totalAmount').autoNumeric('init').autoNumeric('get'), 0));
        var pCost = Number(td.find('input.priceCalc').autoNumeric('init').autoNumeric('get'));
        var pTax = td.find('input.taxPer').autoNumeric('init').autoNumeric('get');
        var pTot = (pCost * pTax) / 100;
        if (th.find('input.taxInclusive').is(':checked')) {
            pCost = (pCost / (1 + (pTax / 100)));
            pTot = pCost * pTax / 100;
        } else {}
        var pTotTax = pCost + pTot;
        $(this).closest('td').find('input.priceCalc').autoNumeric('init').autoNumeric('set', pCost)
        $(this).closest('td').find('input.taxPer').autoNumeric('init').autoNumeric('set', pTax)
        $(this).closest('td').find('input.hiddenTaxValue').autoNumeric('init').autoNumeric('set', pTot)
        $(this).closest('td').find('input.taxCalc').autoNumeric('init').autoNumeric('set', pTot)
        $(this).closest('td').find('input.totalAmount').autoNumeric('init').autoNumeric('set', pTotTax)
        priceCalculation()
    })

    $('#payment-item-table').on("change", 'td input.taxCalc', function(setting) {
        var purFlag = false;
        let td = $(this).closest('td')
        let amount = getAutonumericValue($(td).find('input.priceCalc'));
        let hiddenPurCost = Number(getAutonumericValue($(td).find('input.hiddenTaxValue')));
        let purchaseCostTaxAmount = getAutonumericValue($(td).find('input.taxCalc'));
        var purCostTaxAmtDiff = hiddenPurCost - purchaseCostTaxAmount;
        if (purCostTaxAmtDiff == 1 || purCostTaxAmtDiff == -1 || purCostTaxAmtDiff == 0) {
            $(td).find('input.taxCalc').autoNumeric('init').autoNumeric('set', purchaseCostTaxAmount);
            $(td).find('input.totalAmount').autoNumeric('init').autoNumeric('set', (amount + purchaseCostTaxAmount));
        } else {
            $(td).find('input.taxCalc').autoNumeric('init').autoNumeric('set', hiddenPurCost);
            purFlag = true;
            $(td).find('input.taxCalc').css("border", "1px solid #FA0F1B");
        }
        if (purFlag) {
            alert($.i18n.prop('page.accounts.save.taxamount.invalid'))
            priceCalculation();
            return false;
        }

    });

    $('#payment-item-table').on('ifChecked', 'th input.taxInclusive', function() {
        let th = $(this).closest('th')
        var thIndex = th.index()
        th.find('input.taxInclusive').is(':checked')
        $('#payment-item-table').find('table>tbody>tr:visible').each(function() {
            let tr = $(this).closest('tr');
            let td = tr.find('td').eq(thIndex);
            console.log(Number(td.find('input.priceCalc').autoNumeric('init').autoNumeric('get')))
            var taxAmt = Number(ifNotValid(td.find('input.taxCalc').autoNumeric('init').autoNumeric('get'), 0));
            var amount = Number(ifNotValid(td.find('input.priceCalc').autoNumeric('init').autoNumeric('get'), 0));
            var taxPer = Number(ifNotValid(td.find('input.taxPer').autoNumeric('init').autoNumeric('get'), 0));

            amount = (amount / (1 + (taxPer / 100)));
            taxAmt = amount * taxPer / 100;
            td.find('input.priceCalc').autoNumeric('init').autoNumeric('set', amount)
            td.find('input.taxPer').autoNumeric('init').autoNumeric('set', taxPer)
            td.find('input.taxCalc').autoNumeric('init').autoNumeric('set', taxAmt)
            td.find('input.hiddenTaxValue').autoNumeric('init').autoNumeric('set', taxAmt)
            td.find('input.totalAmount').autoNumeric('init').autoNumeric('set', (amount + taxAmt))
            priceCalculation();
        })

    })

    $('#payment-item-table').on('ifUnchecked', 'th input.taxInclusive', function() {
        let th = $(this).closest('th')
        var thIndex = th.index()
        th.find('input.taxInclusive').is(':checked')
        $('#payment-item-table').find('table>tbody>tr:visible').each(function() {
            let tr = $(this).closest('tr');
            let td = tr.find('td').eq(thIndex);
            console.log(Number(td.find('input.priceCalc').autoNumeric('init').autoNumeric('get')))
            var taxAmt = Number(ifNotValid(td.find('input.taxCalc').autoNumeric('init').autoNumeric('get'), 0));
            var amount = Number(ifNotValid(td.find('input.priceCalc').autoNumeric('init').autoNumeric('get'), 0));
            var taxPer = Number(ifNotValid(td.find('input.taxPer').autoNumeric('init').autoNumeric('get'), 0));
            var totalAmt = Number(ifNotValid(td.find('input.totalAmount').autoNumeric('init').autoNumeric('get'), 0));

            amount = totalAmt;
            taxAmt = (amount * taxPer) / 100;

            td.find('input.priceCalc').autoNumeric('init').autoNumeric('set', amount)
            td.find('input.taxPer').autoNumeric('init').autoNumeric('set', taxPer)
            td.find('input.taxCalc').autoNumeric('init').autoNumeric('set', taxAmt)
            td.find('input.hiddenTaxValue').autoNumeric('init').autoNumeric('set', taxAmt)
            td.find('input.totalAmount').autoNumeric('init').autoNumeric('set', (amount + taxAmt))
            priceCalculation()
        })

    })

    // Customize Datatable
    //     var cloneRowStockNo = $('#payment-item-table').find('tr.clone-row').find('.stockNo');
    //     var cloneRowStockNoFirstRow = $('#payment-item-table').find('tr.first-row').find('.stockNo');
    //     initStockSearchSelect2(cloneRowStockNoFirstRow);
    //     initStockSearchSelect2(cloneRowStockNo);
    //     cloneRowStockNo.select2('destroy');

    $('.amount').autoNumeric('init');

    $('#payment-item-table').on('click', '.add-payment-row', function() {
        var addPayment = $('#payment-item-table').find('tr.clone-row').clone();
        addPayment.find('.amount').autoNumeric('init');
        addPayment.removeClass('hide clone-row')
        initStockSearchSelect2($(addPayment).find('.stockNo'));
        addPayment.appendTo($('#payment-item-table').find('.payment-row-clone-container'));
        addPayment.addClass('first-row');
    })

    $('#payment-item-table').on('click', '.delete-payment-row', function(e) {

        var deletePayment = $('#payment-item-table').find('tbody>tr');
        if (deletePayment.length > 2) {
            $(this).closest('tr').remove();
            priceCalculation();
        } else {
            $('#payment-item-table').find('table>thead').find('th').find('input').each(function() {
                $(this).iCheck('uncheck');
            });
            deletePayment.find('input').val('');
            deletePayment.find('select').val('').trigger('change');
            priceCalculation();
        }
    })

    $('#upload-csv').on('click', function() {
        $('#payment-item-table').addClass('hidden');
        $('#upload-csv-div').removeClass('hidden');
        $('#upload-csv-div-btn').addClass('hidden');
        $('#enter-data-div-btn').removeClass('hidden');
        $('#payment-item-table').find('select,input').val('').trigger('change');
        $('div.totalTaxCalc').addClass('hidden');
    })
    $('#enter-data').on('click', function() {
        $('#payment-item-table').removeClass('hidden');
        $('#upload-csv-div').addClass('hidden');
        $('#upload-csv-div-btn').removeClass('hidden');
        $('#enter-data-div-btn').addClass('hidden');
        $('#upload-csv-div').find('select,input').val('').trigger('change');
        $('div.totalTaxCalc').removeClass('hidden');

    })

    $('#save-storagePhotos').on('click', function() {
        if (!$('#storage-photos-form').find('input,select,textarea').valid()) {
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        var data = {};
        data = getFormData($('.storage-data'));
        var exchangeRateValue = getAutonumericValue($('#storage-photos-form').find('#exchangeRateValue'))
        var tableArray = [];
        if (isEmpty($('select[name="paymentFor"]').find('option:selected').val())) {
            var table = $('#payment-item-table');
            let checkBoxData = {};
            table.find('table>tbody>tr.first-row').each(function() {
                var tableData = {};
                var chargesList = {};
                var totalAmount = getAutonumericValue(table.find('table>tfoot>tr.grandTotal').find('span.autonumber.pagetotal.totalAmount'))

                autoNumericSetRawValue($(this).find('.amount'))
                chargesList = getFormData($(this).find('.chargeData'));
                tableData.invoiceNo = data.invoiceNo;
                tableData.refNo = data.refNo;
                tableData.invoiceDate = data.invoiceDate;
                tableData.dueDate = data.dueDate;
                tableData.remitter = (data.invoiceType == 0) ? data.supplier : (data.invoiceType == 1) ? data.transportSupplier : (data.invoiceType == 2) ? data.forwarderSupplier : (data.invoiceType == 4) ? data.genaralSupplier : (data.invoiceType == 5) ? data.inspectionCompany : data.forwarderSupplier;
                tableData.currency = data.currency;
                tableData.invoiceType = data.invoiceType;
                tableData.exchangeRate = data.exchangeRate;
                tableData.exchangeRateValue = exchangeRateValue;
                tableData.totalAmount = totalAmount;
                tableData.remarks = chargesList.remarks;
                tableData.stockNo = chargesList.stockNo;
                tableData.chargesList = chargesList;
                $.each($('select[name="requested-type-filter"] option:selected'), function() {
                    var attribute = $(this).val() + "TaxInclusive";
                    if (table.find('table>thead>tr.header').find('input[name="' + attribute + '"]').is(':checked')) {
                        chargesList[attribute] = 1;
                    } else {
                        chargesList[attribute] = 0;
                    }
                });
                tableData.metaData = metaData;
                tableArray.push(tableData);
            })
            paymentItem(tableArray, data.invoiceNo);
        } else {
            var fileData = new FormData();
            var csvUploadedEle = $('#upload-csv-div input[name="csvUploaded"]');
            if (csvUploadedEle.prop('files').length > 0) {
                let file = csvUploadedEle.prop('files')[0];
                fileData.append("csvFile", file);
                fileData.append("invoiceNo", data.invoiceNo);
                fileData.append("refNo", data.refNo);
                fileData.append("invoiceDate", data.invoiceDate);
                fileData.append("dueDate", data.dueDate);
                fileData.append("remitter", data.remitter);
                fileData.append("currency", data.currency);
                fileData.append("paymentFor", $('select[name="paymentFor"]').find('option:selected').val());
                let isChecked = $('#taxCheck').is(':checked');
                fileData.append("taxCheck", isChecked ? 1 : 0);
            } else {
                return false;
            }
            uploadCsv(fileData)
        }

    })

    $('#paymentFor').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true
    })

    $('#remitter').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true
    })

    // Select Forwarder
    $.getJSON(myContextPath + "/data/forwarders.json", function(data) {
        $('#remitter').select2({
            allowClear: true,
            width: '100%',
            data: $.map(data, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        }).val('').trigger("change");
    })

    $.getJSON(myContextPath + "/data/currency.json", function(data) {
        $('#currency').select2({
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

    $('#currency').on('change', function() {
        var data = $(this).find(':selected').data('data');
        let val = $(this).val();
        if (!isEmpty(data.id) && data.id != 1) {
            $('#exchangeRateDiv').removeClass('hidden')
        } else {
            $('#exchangeRate').val('').trigger('change')
            $('#exchangeRateDiv').addClass('hidden')
        }
        if (!isEmpty(data.data)) {
            $('.amount').autoNumeric('init').autoNumeric('update', {
                //aSep: '',
                //aDec: '0',
                mDec: val != 2 ? 0 : 2,
                aSign: data.data.symbol + ' '
            });
        }
    })

    $('#exchangeRate').on('change', function() {
        let val = $(this).val();
        var curency = $('#currency').find('option:selected').val();
        var data = $('#currency').find(':selected').data('data');
        if (!isEmpty(val) && val == 4) {
            $('#exRate1').removeClass('hidden')
            $('#exchangeRateValue').autoNumeric('init').autoNumeric('update', {
                //aSep: '',
                //aDec: '0',
                mDec: curency != 2 ? 0 : 2,
                aSign: data.data.symbol + ' '
            });
        } else {
            $('#exchangeRateValue').val('').trigger('change')
            $('#exRate1').addClass('hidden')
        }
    })

    // Add new category
    var addPaymentEle = $('#add-new-category')
    $(addPaymentEle).on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }

    }).on('hide.bs.modal', function(e) {

        $(this).find('input,textarea').val('');
        $(this).find('select').val('').trigger('change');
        //         account();
    }).on('click', '#btn-create-category', function() {
        if (!$('#add-new-category-form').valid()) {
            return false;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return;
        }
        var data = {};
        data['type'] = $('#categoryDesc').val();
        data['coaCode'] = $('#accountType').val();

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/create/newExpense",
            contentType: "application/json",
            async: true,
            success: function(data) {

                $('#add-new-category').modal('toggle');

                //                 $.getJSON(myContextPath + "/data/paymentCategory.json", function(data) {
                //                     //                     var cloneRowCategory = $('#payment-item-table').find('tr.clone-row').find('.category');
                //                     //                     var CategoryFirstRow = $('#payment-item-table').find('tr.first-row').find('.category');
                //                     //                     var StockFirstRow = $('#payment-item-table').find('tr.first-row').find('.stockNo');
                //                     $('#add-payment .category').select2({
                //                         allowClear: true,
                //                         width: "150px",
                //                         data: $.map(data, function(item) {
                //                             return {
                //                                 id: item.categoryCode,
                //                                 text: item.category
                //                             };
                //                         })
                //                      }).val('').trigger("change");

                //                 });
            }
        });
    });

    // set today date
    $('input[name="invoiceDate"]').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).datepicker("setDate", new Date());

    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    })
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })

    $(document).on('keyup', 'input.priceCalc,input.taxCalc', function() {
        priceCalculation()
    }).on('ifChecked', '#storageTaxCheck', function() {
        priceCalculation()
    }).on('ifUnchecked', '#storageTaxCheck', function() {
        priceCalculation()
    }).on('ifChecked', '#shippingTaxCheck', function() {
        priceCalculation()
    }).on('ifUnchecked', '#shippingTaxCheck', function() {
        priceCalculation()
    }).on('ifChecked', '#photoTaxCheck', function() {
        priceCalculation()
    }).on('ifUnchecked', '#photoTaxCheck', function() {
        priceCalculation()
    }).on('ifChecked', '#blAmendCombineTaxCheck', function() {
        priceCalculation()
    }).on('ifUnchecked', '#blAmendCombineTaxCheck', function() {
        priceCalculation()
    }).on('ifChecked', '#radiationTaxCheck', function() {
        priceCalculation()
    }).on('ifUnchecked', '#radiationTaxCheck', function() {
        priceCalculation()
    }).on('ifChecked', '#repairTaxCheck', function() {
        priceCalculation()
    }).on('ifUnchecked', '#repairTaxCheck', function() {
        priceCalculation()
    }).on('ifChecked', '#yhcTaxCheck', function() {
        priceCalculation()
    }).on('ifUnchecked', '#yhcTaxCheck', function() {
        priceCalculation()
    }).on('ifChecked', '#inspectionTaxCheck', function() {
        priceCalculation()
    }).on('ifUnchecked', '#inspectionTaxCheck', function() {
        priceCalculation()
    }).on('ifChecked', '#transportTaxCheck', function() {
        priceCalculation()
    }).on('ifUnchecked', '#transportTaxCheck', function() {
        priceCalculation()
    }).on('ifChecked', '#freightTaxCheck', function() {
        priceCalculation()
    }).on('ifUnchecked', '#freightTaxCheck', function() {
        priceCalculation()
    })

})
function initStockSearchSelect2(element) {
    element.select2({
        allowClear: true,
        minimumInputLength: 2,
        width: "200px",
        ajax: {
            url: myContextPath + "/stock/stockNo-search",
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
                            id: item.stockNo,
                            text: item.stockNo + ' :: ' + item.chassisNo
                        });
                    });
                }
                return {
                    results: results
                }
            }
        }
    })
}

function paymentItem(tableArray, invoiceNo) {
    let queryString = "?invoiceNo=" + invoiceNo;
    $.ajax({
        type: "post",
        data: JSON.stringify(tableArray),
        contentType: "application/json",
        url: myContextPath + "/accounts/payment/createStorageAndPhotos" + queryString,
        success: function(status) {
            //             $.redirect(myContextPath + '/accounts/payment/storage-and-photos/list', '', 'GET');
            location.reload();

        },
        error: function(e) {
            console.log("ERROR: ", e);
        }
    })
}

function setPaymentBookingDashboardStatus(data) {
    $('#count-auction').html(data.auction);
    $('#count-transport').html(data.transport);
    $('#count-freight').html(data.freight);
    $('#count-others').html(data.others);
    $('#count-otherDirectExpense').html(data.storage);
    // $('#count-prepayment').html(data.paymentAdvance);

}

function uploadCsv(fileData) {
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "post",
        processData: false,
        data: fileData,
        url: myContextPath + "/accounts/payment/createStorageAndPhotos/csv",
        contentType: false,
        async: false,
        success: function(data) {
            if (data.status == "success") {
                $('#alert-block-success').css('display', 'block').html('<strong>Warning! </strong>' + data.data);
                $("#alert-block-success").fadeTo(5000, 500).slideUp(500, function() {
                    $("#alert-block-success").slideUp(500);
                });
                $.redirect(myContextPath + '/accounts/invoice/approval/storageAndPhotos', '', 'GET');
            } else {
                $('#alert-block-failure').css('display', 'block').html('<strong>Warning! </strong>' + data.data);
                $("#alert-block-failure").fadeTo(5000, 500).slideUp(500, function() {
                    $("#alert-block-failure").slideUp(500);
                });
            }
        },
        error: function(e) {
            console.log("ERROR: ", e);
        }
    });
}

function priceCalculation() {
    var priceElements = $('#payment-item-table').find('table>tbody').find('td:visible').find('input.priceCalc');
    var taxElements = $('#payment-item-table').find('table>tbody').find('td:visible').find('input.taxCalc');
    let totalAmountWithoutTax = 0.0;
    $(priceElements).closest('.amount').each(function(index, element) {
        totalAmountWithoutTax += Number(getAutonumericValue($(element)));
    })

    let totalTax = 0.0;
    $(taxElements).closest('.amount').each(function(index, element) {
        totalTax += Number(getAutonumericValue($(element)));
    })

    let totalAmount = totalAmountWithoutTax + totalTax;

    let totalWithoutTaxEle = $('#payment-item-table>table>tfoot>tr.sum').find('span.autonumber.pagetotal.totalWithOutTax').autoNumeric('init').autoNumeric('set', totalAmountWithoutTax);
    let totalTaxEle = $('#payment-item-table>table>tfoot>tr.taxtotal').find('span.autonumber.pagetotal.taxTotal').autoNumeric('init').autoNumeric('set', totalTax);
    let totalAmountEle = $('#payment-item-table>table>tfoot>tr.grandTotal').find('span.autonumber.pagetotal.totalAmount').autoNumeric('init').autoNumeric('set', totalAmount);
}

function update(code) {
    $.getJSON(myContextPath + '/accounts/storageAndPhotos/info/' + code, function(data) {
        var response = data.data;
        var showValues = [];
        $.each(response.metaData, function(i, l) {
            showValues.push(l.attribute);
        })
        var select = (response.invoiceType == 0) ? 'supplier' : (response.invoiceType == 1) ? 'transportSupplier' : (response.invoiceType == 2) ? 'forwarderSupplier' : (response.invoiceType == 4) ? 'genaralSupplier' : (response.invoiceType == 5) ? 'inspectionCompany' : 'forwarderSupplier'
        $('select#invoiceTypeFilter').val(response.invoiceType).trigger('change');
        $("select#" + select).val(response.remitter).trigger('change');
        $('input[name="refNo"]').val(response.refNo);
        $('select#currency').val(response.currency).trigger('change');
        $('select#exchangeRate').val(response.exchangeRate).trigger('change');
        $('input[name="dueDate"]').datepicker('setDate', response["dueDate"]);
        $('input[name="invoiceDate"]').datepicker('setDate', response["invoiceDate"]);
        $('select[name="requested-type-filter"]').val(showValues).trigger('change')
        $('#showFields').click();

        for (var i = 0; i < response.approvePaymentItems.length; i++) {
            var cloneEle;
            if (i != 0) {
                cloneEle = $('#payment-item-table').find('tr.clone-row').clone();
                cloneEle.removeClass('hide clone-row')
                cloneEle.addClass('first-row');
                cloneEle.appendTo('.payment-row-clone-container');
                let secndRow = $('#payment-item-table').find('.payment-row-clone-container').find('tr:last');
                initStockSearchSelect2($(secndRow).find('.stockNo'));

                $('<option value="' + response.approvePaymentItems[i].stockNo + '">' + response.approvePaymentItems[i].stockNo + ' :: ' + response.approvePaymentItems[i].chassisNo + '</option>').appendTo(secndRow.find('select[name="stockNo"]'));
                secndRow.find('.stockNo').val(response.approvePaymentItems[i].stockNo).trigger("change");

                secndRow.find('input[name="remarks"]').val(ifNotValid(response.approvePaymentItems[i].remarks, ''))

                for (var j = 0; j < response.metaData.length; j++) {
                    var attributeValue = response.metaData[j].attribute
                    if (response.approvePaymentItems[i].chargesList[attributeValue + "TaxInclusive"] == 1) {
                        $('#payment-item-table').find('input[name="' + attributeValue + 'TaxInclusive"]').iCheck('check').trigger('change')
                    }
                    setAutonumericValue($(secndRow).find('input[name="' + attributeValue + '"]'), ifNotValid(response.approvePaymentItems[i].chargesList[attributeValue], ''))
                    setAutonumericValue($(secndRow).find('input[name="' + attributeValue + 'TaxPer"]'), ifNotValid(response.approvePaymentItems[i].chargesList[attributeValue + "TaxPer"], ''))
                    setAutonumericValue($(secndRow).find('input[name="' + attributeValue + 'Tax"]'), ifNotValid(response.approvePaymentItems[i].chargesList[attributeValue + "TaxPer"], ''))
                    setAutonumericValue($(secndRow).find('input[name="' + attributeValue + 'TaxAmount"]'), ifNotValid(response.approvePaymentItems[i].chargesList[attributeValue + "TaxAmount"], ''))
                    setAutonumericValue($(secndRow).find('input[name="' + attributeValue + 'TaxAmt"]'), ifNotValid(response.approvePaymentItems[i].chargesList[attributeValue + "TaxAmount"], ''))
                    setAutonumericValue($(secndRow).find('input[name="' + attributeValue + 'TotalAmount"]'), ifNotValid(response.approvePaymentItems[i].chargesList[attributeValue + "TotalAmount"], ''))

                }
            } else {
                cloneEle = $('#payment-item-table').find('tr.first-row');
                cloneEle.find('input[name="remarks"]').val(ifNotValid(response.approvePaymentItems[i].remarks, ''))
                $('<option value="' + response.approvePaymentItems[i].stockNo + '">' + response.approvePaymentItems[i].stockNo + ' :: ' + response.approvePaymentItems[i].chassisNo + '</option>').appendTo(cloneEle.find('select[name="stockNo"]'));
                cloneEle.find('select[name="stockNo"]').val(response.approvePaymentItems[i].stockNo).trigger("change");
                for (var j = 0; j < response.metaData.length; j++) {
                    var attributeValue = response.metaData[j].attribute
                    if (response.approvePaymentItems[i].chargesList[attributeValue + "TaxInclusive"] == 1) {
                        $('#payment-item-table').find('input[name="' + attributeValue + 'TaxInclusive"]').iCheck('check').trigger('change')
                    }
                    setAutonumericValue($(cloneEle).find('input[name="' + attributeValue + '"]'), ifNotValid(response.approvePaymentItems[i].chargesList[attributeValue], ''))
                    setAutonumericValue($(cloneEle).find('input[name="' + attributeValue + 'Tax"]'), ifNotValid(response.approvePaymentItems[i].chargesList[attributeValue + "TaxPer"], ''))
                    setAutonumericValue($(cloneEle).find('input[name="' + attributeValue + 'TaxPer"]'), ifNotValid(response.approvePaymentItems[i].chargesList[attributeValue + "TaxPer"], ''))
                    setAutonumericValue($(cloneEle).find('input[name="' + attributeValue + 'TaxAmount"]'), ifNotValid(response.approvePaymentItems[i].chargesList[attributeValue + "TaxAmount"], ''))
                    setAutonumericValue($(cloneEle).find('input[name="hidden' + attributeValue + 'TaxAmt"]'), ifNotValid(response.approvePaymentItems[i].chargesList[attributeValue + "TaxAmount"], ''))
                    setAutonumericValue($(cloneEle).find('input[name="' + attributeValue + 'TotalAmount"]'), ifNotValid(response.approvePaymentItems[i].chargesList[attributeValue + "TotalAmount"], ''))

                }
            }
            //             cloneEle.find('select[name="stockNo"]').val(response.approvePaymentItems[i].stockNo).trigger("change");
            priceCalculation();

        }
    })
}
