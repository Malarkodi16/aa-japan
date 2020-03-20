var editPaymentEle, editPaymentItemEle;
$(function() {

    editPaymentEle = $('#edit-payment')
    editPaymentItemEle = $('#edit-payment-item-table')
    $(editPaymentEle).on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }

        var rowdata = tableInvoice.rows(event.relatedTarget.closest('tr')).data();
        if (!isEmpty(rowdata[0])) {
            rowdata = rowdata[0];
        }

        var subtotal = 0;
        setAutonumericValue($(editPaymentItemEle).find('tfoot').find('span.autonumber'), subtotal);
        $('#edit-payment .autonumber').autoNumeric('init');

        $('input[name="invoiceNo"]').val(rowdata.invoiceNo);
        $('select[name="remitter"]').val(rowdata.remitter).trigger('change');
        $('input[name="refNo"]').val(rowdata.refNo);
        $('input[name="date"]').datepicker('setDate', rowdata.invoiceDate);
        $('input[name="dueDate"]').datepicker('setDate', rowdata.dueDate);
        var element;
        var j = 0;
        for (var i = 0; i < rowdata.approvePaymentItems.length; i++) {

            var iteratingData = rowdata.approvePaymentItems[i];

            if (j != 0) {
                var cloneRow = $(editPaymentItemEle).find('tr.clone-row').clone();
                cloneRow.find('.autonumber');
                cloneRow.removeClass('hide clone-row');
                cloneRow.appendTo('.edit-payment-row-clone-container');
                element = cloneRow.addClass('invoice-item');
            } else {
                element = $(editPaymentItemEle).find('.invoice-item');
            }

            $(element).find('input[name="id"]').val(iteratingData.id);
            $(element).find('select[name="category"]').val(iteratingData.categoryCode).trigger('change');
            $(element).find('input[name="description"]').val(iteratingData.description);
            $(element).find('input[name="amountInYen"]').val(iteratingData.amountInYen);
            if (iteratingData.taxInclusive == "true") {
                $(element).find('input[name="taxInclusive"]').prop('checked', true);
            } else {
                $(element).find('input[name="taxInclusive"]').prop('checked', false);
            }
            $(element).find('input[name="taxPercent"]').val(iteratingData.taxPercentage);
            $(element).find('input[name="taxAmount"]').val(iteratingData.taxAmount);
            $(element).find('input[name="hiddenTaxAmount"]').val(iteratingData.taxAmount);
            $(element).find('input[name="totalAmount"]').val(iteratingData.taxIncludedAmount);
            $(element).find('input[name="sourceCurrency"]').val(iteratingData.sourceCurrency);
            $(element).find('input[name="amount"]').val(iteratingData.amount);
            $(element).find('input[name="exchangeRate"]').val(iteratingData.exchangeRate);
            editUpdateFooter()
            j++;
        }

    }).on('hidden.bs.modal', function() {
        $(this).find('table>tbody>tr.invoice-item').not(':first').remove();
        $(this).find('input[name="taxAmount"]').css('border', '');
        $(this).find('input,select').val('').trigger('change');
        $(this).find('form').trigger('reset');

    }).on('click', '.add-payment-row', function() {

        var editPayment = $(editPaymentItemEle).find('tr.clone-row').clone();
        editPayment.find('.autonumber');
        editPayment.removeClass('hide clone-row');
        editPayment.find('.category').select2({
            allowClear: true,
            width: "250px"
        })
        editPayment.appendTo('.edit-payment-row-clone-container');
        editPayment.addClass('invoice-item');
        var commonTax = $.i18n.prop('common.tax');
        $(editPaymentItemEle).find('input[name="taxPercent"]').val(Number(commonTax));
    }).on('click', '.delete-payment-row', function(e) {

        var deletePayment = $(editPaymentItemEle).find('tbody>tr');
        if (deletePayment.length > 2) {
            $(this).closest('tr').remove();
        } else {
            deletePayment.find('input').val('');
            deletePayment.find('select').val('').trigger('change');
        }
        editUpdateFooter();
    }).on('keyup', '.amount', function(e) {
        var excRateEle = $(this).closest('tr').find('.exchangeRate');
        var srcAmt = getAutonumericValue($(this));
        var totAmt = getAutonumericValue($(this).closest('tr').find('.totalAmount'));
        var exc = totAmt / srcAmt;
        if (exc > 0 && exc != 'Infinity') {
            excRateEle.val(exc);
        }
    }).on("keyup", "input.autonumber", function(setting) {
        var tr = $(this).closest('tr');
        //update subtotal
        var amount = Number(ifNotValid(tr.find('input[name="amountInYen"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxAmt = Number(ifNotValid(tr.find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('get'), 0));
        var totalAmt = amount + taxAmt;
        tr.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalAmt);
        //update table footer`
        editUpdateFooter();
    }).on('change', '.taxPercent', function(e) {
        let tr = $(this).closest('tr');
        var amount = Number(ifNotValid(tr.find('input[name="amountInYen"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxPer = Number(ifNotValid(tr.find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('get'), 0));
        var totalAmt = Number(ifNotValid(tr.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxAmt;
        let isTax = tr.find('input[name="taxInclusive"]').is(':checked');
        if (isTax) {

            amount = totalAmt / (1 + (taxPer / 100));
            taxAmt = amount * taxPer / 100;
        } else {
            taxAmt = amount * taxPer / 100;
        }
        var totalAmt = amount + taxAmt;
        $(this).closest('tr').find('input[name="amountInYen"]').autoNumeric('init').autoNumeric('set', amount)
        $(this).closest('tr').find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('set', taxPer)
        $(this).closest('tr').find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        $(this).closest('tr').find('input[name="hiddenTaxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        $(this).closest('tr').find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalAmt)
        editUpdateFooter();
    }).on('change', '.amountInYen', function(e) {
        let tr = $(this).closest('tr');
        var amount = Number(ifNotValid(tr.find('input[name="amountInYen"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxPer = Number(ifNotValid(tr.find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('get'), 0));
        var totalAmt = Number(ifNotValid(tr.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxAmt = amount * taxPer / 100
        let isTax = tr.find('input[name="taxInclusive"]').is(':checked');
        if (isTax) {
            amount = amount / (1 + (taxPer / 100));
            taxAmt = amount * taxPer / 100;
        } else {}
        var totalAmt = amount + taxAmt;
        $(this).closest('tr').find('input[name="amountInYen"]').autoNumeric('init').autoNumeric('set', amount)
        $(this).closest('tr').find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('set', taxPer)
        $(this).closest('tr').find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        $(this).closest('tr').find('input[name="hiddenTaxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        $(this).closest('tr').find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalAmt)
        editUpdateFooter();
    }).on('click', '.taxInclusive', function(e) {
        let tr = $(this).closest('tr');
        var taxAmt;
        var amount = Number(ifNotValid(tr.find('input[name="amountInYen"]').autoNumeric('init').autoNumeric('get'), 0));
        var taxPer = Number(ifNotValid(tr.find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('get'), 0));
        var totalAmt = Number(ifNotValid(tr.find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('get'), 0));
        if ($(this).is(':checked')) {
            amount = amount / (1 + (taxPer / 100));
            taxAmt = amount * taxPer / 100;
        } else {
            amount = totalAmt;
            taxAmt = (amount * taxPer) / 100;
        }
        totalAmt = amount + taxAmt;
        $(this).closest('tr').find('input[name="amountInYen"]').autoNumeric('init').autoNumeric('set', amount)
        $(this).closest('tr').find('input[name="taxPercent"]').autoNumeric('init').autoNumeric('set', taxPer)
        $(this).closest('tr').find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        $(this).closest('tr').find('input[name="hiddenTaxAmount"]').autoNumeric('init').autoNumeric('set', taxAmt)
        $(this).closest('tr').find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalAmt)
        editUpdateFooter();
    }).on('click', '#save-other-payments', function() {
        let validationFields = $('#other-form').find('select[name="remitter"],input[name="refNo"],input[name="date"],input[name="dueDate"]' + ',input[name="dueDate"],select[name="category"],input[name="amountInYen"]')
        if (!validationFields.valid()) {
            return false;
        }

        var flag = false;
        var data = {};

        var tableArray = [];
        data = getFormData($(editPaymentEle).find('.other-data'));
        //         var otherPaymentData = $('.payment-row-clone-container');
        var table = $('#edit-payment-item-table');
        table.find('table>tbody>tr.invoice-item').each(function() {
            let amountValue = Number(getAutonumericValue($(this).find('input[name="amountInYen"]')));
            let totalAmountValue = Number(getAutonumericValue($(this).find('input[name="totalAmount"]')));
            let hiddenTax = Number(getAutonumericValue($(this).find('input[name="hiddenTaxAmount"]')));
            let orgTaxAmount = Number(getAutonumericValue($(this).find('input[name="taxAmount"]')));
            var taxDiff = hiddenTax - orgTaxAmount;
            var totalValue = 0;
            if (taxDiff == 1 || taxDiff == -1 || taxDiff == 0) {
                totalValue = amountValue + orgTaxAmount;
                $(this).find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', orgTaxAmount);
                $(this).find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalValue);
            } else {
                totalValue = amountValue + hiddenTax;
                $(this).find('input[name="taxAmount"]').autoNumeric('init').autoNumeric('set', hiddenTax);
                $(this).find('input[name="totalAmount"]').autoNumeric('init').autoNumeric('set', totalValue);
                flag = true;
                $(this).find('input[name="taxAmount"]').css("border", "1px solid #FA0F1B");
            }

            var tableData = {};

            tableData.invoiceNo = data.invoiceNo;
            tableData.refNo = data.refNo;
            tableData.invoiceDate = data.date;
            tableData.dueDate = data.dueDate;
            tableData.remitter = data.remitter;
            tableData.remitterOthers = data.remitterOthers;
            tableData.id = $(this).find('input[name="id"]').val();
            tableData.category = $(this).find('select[name="category"]').val();
            tableData.categoryOthers = $(this).find('input[name="categoryOthers"]').val();
            tableData.description = $(this).find('input[name="description"]').val();
            tableData.amountInYen = getAutonumericValue($(this).find('input[name="amountInYen"]'));
            tableData.taxPercentage = getAutonumericValue($(this).find('input[name="taxPercent"]'));
            tableData.taxAmount = getAutonumericValue($(this).find('input[name="taxAmount"]'));
            tableData.taxIncludedAmount = getAutonumericValue($(this).find('input[name="totalAmount"]'));
            let isTax = $(this).find('input[name="taxInclusive"]').is(':checked');
            if (isTax) {
                tableData.taxInclusive = true;
            } else {
                tableData.taxInclusive = false;
            }
            tableData.sourceCurrency = $(this).find('input[name="sourceCurrency"]').val();
            tableData.amount = getAutonumericValue($(this).find('input[name="amount"]'));
            tableData.exchangeRate = getAutonumericValue($(this).find('input[name="exchangeRate"]'));
            tableArray.push(tableData);
        })

        if (flag) {
            alert($.i18n.prop('page.accounts.save.taxamount.invalid'))
            editUpdateFooter();
            return false;
        }

        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        $.ajax({
            type: "post",
            data: JSON.stringify(tableArray),
            contentType: "application/json",
            url: myContextPath + "/accounts/payment/save-other-payments-edit",
            success: function(status) {
                $('#edit-payment').modal('toggle')
                //                 $('#add-payment').find('#stockNo').val('').trigger('change');
                //                 $('#add-payment').find('#category').val('').trigger('change');
                tableInvoice.ajax.reload();
            },
            error: function(e) {
                console.log("ERROR: ", e);
            }
        })

    })

})

function editUpdateFooter() {
    var total = 0;
    var totalTax = 0;
    var totalTaxIncluded = 0;
    $(editPaymentItemEle).find('input[name="amountInYen"]').each(function() {
        let valueTotal = $(this).val();
        !isEmpty(valueTotal) ? total += Number($(this).autoNumeric('init').autoNumeric('get')) : total += 0.0;
    })
    setAutonumericValue($(editPaymentItemEle).find('tfoot>tr>td.amountInYenTotal').find('span.autonumber.amountInYenTotal').autoNumeric('init'), total);
    $(editPaymentItemEle).find('input[name="taxAmount"]').each(function() {
        let valueTotalTax = $(this).val();
        !isEmpty(valueTotalTax) ? totalTax += Number($(this).autoNumeric('init').autoNumeric('get')) : totalTax += 0.0;
    })
    setAutonumericValue($(editPaymentItemEle).find('tfoot>tr>td.taxTotal').find('span.autonumber.taxTotal').autoNumeric('init'), totalTax);
    $(editPaymentItemEle).find('input[name="totalAmount"]').each(function() {
        let valueTotalTaxInc = $(this).val();
        !isEmpty(valueTotalTaxInc) ? totalTaxIncluded += Number($(this).autoNumeric('init').autoNumeric('get')) : totalTaxIncluded += 0.0;

    })
    setAutonumericValue($(editPaymentItemEle).find('tfoot>tr>td.footerTotal').find('span.autonumber.footerTotal').autoNumeric('init'), totalTaxIncluded);
}
