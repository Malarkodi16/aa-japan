var tableStorage;
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

    $('input[type="checkbox"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    })

    // Customize Datatable
    var cloneRowStockNo = $('#payment-item-table').find('tr.clone-row').find('.stockNo');
    var cloneRowStockNoFirstRow = $('#payment-item-table').find('tr.first-row').find('.stockNo');
    initStockSearchSelect2(cloneRowStockNoFirstRow);
    initStockSearchSelect2(cloneRowStockNo);
    cloneRowStockNo.select2('destroy');

    $('.amount').autoNumeric('init');

    $('#payment-item-table').on('click', '.add-payment-row', function() {
        var addPayment = $('#payment-item-table').find('tr.clone-row').clone();
        addPayment.find('.amount').autoNumeric('init');
        addPayment.removeClass('hide clone-row')
        initStockSearchSelect2($(addPayment).find('.stockNo'));
        addPayment.appendTo('.payment-row-clone-container');
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
        var tableArray = [];
        if (isEmpty($('select[name="paymentFor"]').find('option:selected').val())) {
            var table = $('#payment-item-table');
            let checkBoxData = {};
            table.find('table>tbody>tr.first-row').each(function() {
                var tableData = {};

                autoNumericSetRawValue($(this).find('.amount'))
                tableData = getFormData($(this).find('.form-control'));
                tableData.invoiceNo = data.invoiceNo;
                tableData.refNo = data.refNo;
                tableData.invoiceDate = data.invoiceDate;
                tableData.dueDate = data.dueDate;
                tableData.remitter = data.remitter;
                tableData.currency = data.currency;
                tableArray.push(tableData);
            })
            checkBoxData = getFormData(table.find('table>thead').find('input.form-control'))
            paymentItem(tableArray, checkBoxData, data.invoiceNo);
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
        if (!isEmpty(data.data)) {
            $('.amount').autoNumeric('init').autoNumeric('update', {
                //aSep: '',
                //aDec: '0',
                mDec: val != 2 ? 0 : 2,
                aSign: data.data.symbol + ' '
            });
        }
    })

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

    $(document).on('keyup', 'input.priceCalc', function() {
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

function paymentItem(tableArray, checkBoxData, invoiceNo) {
    let queryString = "?storageTaxCheck=" + ifNotValid(checkBoxData.storageTaxCheck, 0) + "&shippingTaxCheck=" + ifNotValid(checkBoxData.shippingTaxCheck, 0) + "&photoTaxCheck=" + ifNotValid(checkBoxData.photoTaxCheck, 0) + "&blAmendCombineTaxCheck=" + ifNotValid(checkBoxData.blAmendCombineTaxCheck, 0) + "&radiationTaxCheck=" + ifNotValid(checkBoxData.radiationTaxCheck, 0) + "&repairTaxCheck=" + ifNotValid(checkBoxData.repairTaxCheck, 0) + "&yhcTaxCheck=" + ifNotValid(checkBoxData.yhcTaxCheck, 0) + "&inspectionTaxCheck=" + ifNotValid(checkBoxData.inspectionTaxCheck, 0) + "&transportTaxCheck=" + ifNotValid(checkBoxData.transportTaxCheck, 0) + "&freightTaxCheck=" + ifNotValid(checkBoxData.freightTaxCheck, 0) + "&invoiceNo=" + invoiceNo;
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
    $('#count-storage').html(data.storage);
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

    let storageCharge = 0.0;
    $(priceElements).closest('#amount').each(function(index, element) {
        storageCharge += Number(getAutonumericValue($(element)));
    })

    let storageChargeTax = 0.0;
    if ($('#storageTaxCheck').is(':checked')) {
        storageChargeTax = storageCharge * ($.i18n.prop('common.tax') / 100)
    } else {
        storageChargeTax = 0.0;
    }

    let shippingCharge = 0.0;
    $(priceElements).closest('#shippingCharges').each(function(index, element) {
        shippingCharge += Number(getAutonumericValue($(element)));
    })

    let shippingChargeTax = 0.0;
    if ($('#shippingTaxCheck').is(':checked')) {
        shippingChargeTax = shippingCharge * ($.i18n.prop('common.tax') / 100)
    } else {
        shippingChargeTax = 0.0;
    }

    let photoCharges = 0.0;
    $(priceElements).closest('#photoCharges').each(function(index, element) {
        photoCharges += Number(getAutonumericValue($(element)));
    })

    let photoChargesTax = 0.0;
    if ($('#photoTaxCheck').is(':checked')) {
        photoChargesTax = photoCharges * ($.i18n.prop('common.tax') / 100)
    } else {
        photoChargesTax = 0.0;
    }

    let blAmendCombineCharges = 0.0;
    $(priceElements).closest('#blAmendCombineCharges').each(function(index, element) {
        blAmendCombineCharges += Number(getAutonumericValue($(element)));
    })

    let blAmendCombineChargesTax = 0.0;
    if ($('#blAmendCombineTaxCheck').is(':checked')) {
        blAmendCombineChargesTax = blAmendCombineCharges * ($.i18n.prop('common.tax') / 100)
    } else {
        blAmendCombineChargesTax = 0.0;
    }

    let radiationCharges = 0.0;
    $(priceElements).closest('#radiationCharges').each(function(index, element) {
        radiationCharges += Number(getAutonumericValue($(element)));
    })

    let radiationChargesTax = 0.0;
    if ($('#radiationTaxCheck').is(':checked')) {
        radiationChargesTax = radiationCharges * ($.i18n.prop('common.tax') / 100)
    } else {
        radiationChargesTax = 0.0;
    }

    let repairCharges = 0.0;
    $(priceElements).closest('#repairCharges').each(function(index, element) {
        repairCharges += Number(getAutonumericValue($(element)));
    })

    let repairChargesTax = 0.0;
    if ($('#repairTaxCheck').is(':checked')) {
        repairChargesTax = repairCharges * ($.i18n.prop('common.tax') / 100)
    } else {
        repairChargesTax = 0.0;
    }

    let yardHandlingCharges = 0.0;
    $(priceElements).closest('#yardHandlingCharges').each(function(index, element) {
        yardHandlingCharges += Number(getAutonumericValue($(element)));
    })

    let yardHandlingChargesTax = 0.0;
    if ($('#yhcTaxCheck').is(':checked')) {
        yardHandlingChargesTax = yardHandlingCharges * ($.i18n.prop('common.tax') / 100)
    } else {
        yardHandlingChargesTax = 0.0;
    }

    let inspectionCharges = 0.0;
    $(priceElements).closest('#inspectionCharges').each(function(index, element) {
        inspectionCharges += Number(getAutonumericValue($(element)));
    })

    let inspectionChargesTax = 0.0;
    if ($('#inspectionTaxCheck').is(':checked')) {
        inspectionChargesTax = inspectionCharges * ($.i18n.prop('common.tax') / 100)
    } else {
        inspectionChargesTax = 0.0;
    }

    let transportCharges = 0.0;
    $(priceElements).closest('#transportCharges').each(function(index, element) {
        transportCharges += Number(getAutonumericValue($(element)));
    })

    let transportChargesTax = 0.0;
    if ($('#transportTaxCheck').is(':checked')) {
        transportChargesTax = transportCharges * ($.i18n.prop('common.tax') / 100)
    } else {
        transportChargesTax = 0.0;
    }

    let freightCharges = 0.0;
    $(priceElements).closest('#freightCharges').each(function(index, element) {
        freightCharges += Number(getAutonumericValue($(element)));
    })

    let freightChargesTax = 0.0;
    if ($('#freightTaxCheck').is(':checked')) {
        freightChargesTax = freightCharges * ($.i18n.prop('common.tax') / 100)
    } else {
        freightChargesTax = 0.0;
    }

    let totalWithoutTaxEle = $(document).find('#totalWithoutTax');
    let totalAmount = ifNotValid(storageCharge, 0.0) + ifNotValid(shippingCharge, 0.0) + ifNotValid(photoCharges, 0.0) + ifNotValid(blAmendCombineCharges, 0.0) + ifNotValid(radiationCharges, 0.0) + ifNotValid(repairCharges, 0.0) + ifNotValid(yardHandlingCharges, 0.0) + ifNotValid(inspectionCharges, 0.0) + ifNotValid(transportCharges, 0.0) + ifNotValid(freightCharges, 0.0)
    setAutonumericValue(totalWithoutTaxEle, totalAmount);
    //autoNumeric('init').autoNumeric('set', pageOtherChargesTotal)
    let totalWithoutTax = Number(getAutonumericValue(totalWithoutTaxEle));

    let totalTaxEle = $(document).find('#totalTax')
    let taxCharge = ifNotValid(storageChargeTax, 0.0) + ifNotValid(shippingChargeTax, 0.0) + ifNotValid(photoChargesTax, 0.0) + ifNotValid(blAmendCombineChargesTax, 0.0) + ifNotValid(radiationChargesTax, 0.0) + ifNotValid(repairChargesTax, 0.0) + ifNotValid(yardHandlingChargesTax, 0.0) + ifNotValid(inspectionChargesTax, 0.0) + ifNotValid(transportChargesTax, 0.0) + ifNotValid(freightChargesTax, 0.0)
    setAutonumericValue(totalTaxEle, taxCharge);
    let totalTax = Number(getAutonumericValue(totalTaxEle));

    setAutonumericValue($(document).find('#allTotal'), totalWithoutTax + totalTax)
}

function update(code) {
    $.getJSON(myContextPath + '/accounts/storageAndPhotos/info/' + code, function(data) {
        var response = data.data;
        console.log(response)
        $('select#remitter').val(response.remitter).trigger('change');
        $('input[name="refNo"]').val(response.refNo);

        $('input[name="dueDate"]').datepicker('setDate', response["dueDate"]);
        $('input[name="invoiceDate"]').datepicker('setDate', response["invoiceDate"]);

        for (var i = 0; i < response.approvePaymentItems.length; i++) {
            var cloneEle;
            if (i != 0) {
                cloneEle = $('#payment-item-table').find('tr.clone-row').clone();
                cloneEle.removeClass('hide clone-row')
                initStockSearchSelect2($(cloneEle).find('.stockNo'));
                cloneEle.appendTo('.payment-row-clone-container')
                cloneEle.addClass('first-row');
            } else {
                cloneEle = $('#payment-item-table').find('tr.first-row');
            }
            if (!isEmpty(response.approvePaymentItems[i].inspectionCharges) && response.approvePaymentItems[i].inspectionCharges > 0) {
                $('#payment-item-table').find('input[name="inspectionTaxCheck"]').iCheck('check').trigger('change')
            }
            if (!isEmpty(response.approvePaymentItems[i].amount) && response.approvePaymentItems[i].amount > 0) {
                $('#payment-item-table').find('input[name="storageTaxCheck"]').iCheck('check').trigger('change')
            }
            if (!isEmpty(response.approvePaymentItems[i].shippingCharges) && response.approvePaymentItems[i].shippingCharges > 0) {
                $('#payment-item-table').find('input[name="shippingTaxCheck"]').iCheck('check').trigger('change')
            }
            if (!isEmpty(response.approvePaymentItems[i].photoCharges) && response.approvePaymentItems[i].photoCharges > 0) {
                $('#payment-item-table').find('input[name="photoTaxCheck"]').iCheck('check').trigger('change')
            }
            if (!isEmpty(response.approvePaymentItems[i].blAmendCombineCharges) && response.approvePaymentItems[i].blAmendCombineCharges > 0) {
                $('#payment-item-table').find('input[name="blAmendCombineTaxCheck"]').iCheck('check').trigger('change')
            }
            if (!isEmpty(response.approvePaymentItems[i].radiationCharges) && response.approvePaymentItems[i].radiationCharges > 0) {
                $('#payment-item-table').find('input[name="radiationTaxCheck"]').iCheck('check').trigger('change')
            }
            if (!isEmpty(response.approvePaymentItems[i].repairCharges) && response.approvePaymentItems[i].repairCharges > 0) {
                $('#payment-item-table').find('input[name="repairTaxCheck"]').iCheck('check').trigger('change')
            }
            if (!isEmpty(response.approvePaymentItems[i].yardHandlingCharges) && response.approvePaymentItems[i].yardHandlingCharges > 0) {
                $('#payment-item-table').find('input[name="yhcTaxCheck"]').iCheck('check').trigger('change')
            }
            if (!isEmpty(response.approvePaymentItems[i].transportCharges) && response.approvePaymentItems[i].transportCharges > 0) {
                $('#payment-item-table').find('input[name="transportTaxCheck"]').iCheck('check').trigger('change')
            }
            if (!isEmpty(response.approvePaymentItems[i].freightCharges) && response.approvePaymentItems[i].freightCharges > 0) {
                $('#payment-item-table').find('input[name="freightTaxCheck"]').iCheck('check').trigger('change')
            }
            cloneEle.find('.amount').autoNumeric('init');
            setAutonumericValue(cloneEle.find('input[name="amount"]'), ifNotValid(response.approvePaymentItems[i].amount, 0));
            setAutonumericValue(cloneEle.find('input[name="shippingCharges"]'), ifNotValid(response.approvePaymentItems[i].shippingCharges, 0));
            setAutonumericValue(cloneEle.find('input[name="photoCharges"]'), ifNotValid(response.approvePaymentItems[i].photoCharges, ''));
            setAutonumericValue(cloneEle.find('input[name="blAmendCombineCharges"]'), ifNotValid(response.approvePaymentItems[i].blAmendCombineCharges, 0));
            setAutonumericValue(cloneEle.find('input[name="radiationCharges"]'), ifNotValid(response.approvePaymentItems[i].radiationCharges, 0));
            setAutonumericValue(cloneEle.find('input[name="repairCharges"]'), ifNotValid(response.approvePaymentItems[i].repairCharges, 0));
            setAutonumericValue(cloneEle.find('input[name="yardHandlingCharges"]'), ifNotValid(response.approvePaymentItems[i].yardHandlingCharges, 0));
            setAutonumericValue(cloneEle.find('input[name="inspectionCharges"]'), ifNotValid(response.approvePaymentItems[i].inspectionCharges, 0));
            setAutonumericValue(cloneEle.find('input[name="transportCharges"]'), ifNotValid(response.approvePaymentItems[i].transportCharges, 0));
            setAutonumericValue(cloneEle.find('input[name="freightCharges"]'), ifNotValid(response.approvePaymentItems[i].freightCharges, 0));
            $('select[name="currency"]').val(response.currency).trigger('change');
            cloneEle.find('input[name="remarks"]').val(ifNotValid(response.approvePaymentItems[i].remarks, ''))
            $('<option value="' + response.approvePaymentItems[i].stockNo + '">' + response.approvePaymentItems[i].stockNo + ' :: ' + response.approvePaymentItems[i].chassisNo + '</option>').appendTo(cloneEle.find('select[name="stockNo"]'));
            cloneEle.find('select[name="stockNo"]').val(response.approvePaymentItems[i].stockNo).trigger("change");
            priceCalculation();

        }
    })
}
