$(function() {
    // set today date
    $('input[name="invoiceDate"]').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).datepicker("setDate", new Date());

    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    })

    //init customer search dropdown
    $(this).find('select[name="customerId"]').select2({
        allowClear: true,
        minimumInputLength: 2,
        width: '100%',
        ajax: {
            url: myContextPath + "/customer/search?flag=customer",
            cache: false,
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
                $(this)
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
    })

    $('.parts-item').find('.taxInclusive').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    })

    $('.autonumber').autoNumeric('init');

    $('#parts-item-table').on('click', '.add-new-row', function() {
        var lastSno = parseInt($('.parts-row-clone-container>tr:last').find('td>span').html());
        var addPayment = $('#parts-item-table').find('tr.clone-row').clone();
        addPayment.find('td.sno>span').html(lastSno + 1);
        addPayment.find('.autonumber').autoNumeric('init');
        addPayment.removeClass('hide clone-row')
        addPayment.appendTo('.parts-row-clone-container');
        addPayment.addClass('first-row');
        addPayment.find('.taxInclusive').iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        })
    })

    $('#parts-item-table').on('click', '.delete-item-row', function(e) {

        var deletePayment = $('#parts-item-table').find('tbody>tr');
        if (deletePayment.length > 2) {
            $(this).closest('tr').remove();
            $('#parts-item-table').find('table>tbody').find('tr').each(function(index, value) {
                $(this).find('td>span').html(index);
            });
            // priceCalculation();
        } else {
            $('#parts-item-table').find('table>tbody').find('tr').each(function(index, value) {
                $(this).find('td>span').html(index);
            });
            deletePayment.find('input').val('');
            deletePayment.find('select').val('').trigger('change');
            // priceCalculation();
        }
    })

    $('#save-parts-purcahsed').on('click', function() {
        if (!$('#parts-purchase-create-form').find('input.required, select.required').valid()) {
            return false;
        }
        var tableArray = [];
        var data = {};
        data = getFormData($('#partsPurchase').find('.form-control'));
        var table = $('#parts-item-table');
        table.find('table>tbody>tr:visible').each(function() {
            var tableData = {};

            tableData.invoiceNo = data.invoiceNo;
            tableData.invoiceDate = data.invoiceDate;
            tableData.customerId = data.customerId;
            tableData.partNo = $(this).find('input[name="partNo"]').val();
            tableData.partDescription = $(this).find('textarea[name="partDescription"]').val();
            tableData.unitPrice = getAutonumericValue($(this).find('input[name="unitPrice"]'));
            tableData.quantity = getAutonumericValue($(this).find('input[name="quantity"]'));
            tableData.amount = getAutonumericValue($(this).find('input[name="amount"]'));
            tableData.taxInclusive = $(this).find('input[name="taxInclusive"]').is(':checked');
            tableData.taxAmount = getAutonumericValue($(this).find('input[name="taxAmount"]'));
            tableData.taxIncludedAmount = getAutonumericValue($(this).find('input[name="taxIncludedAmount"]'));
            tableArray.push(tableData);
        })

        $.ajax({
            type: "post",
            data: JSON.stringify(tableArray),
            contentType: "application/json",
            url: myContextPath + "/parts/save-purchased-parts",
            success: function(status) {
            	$.redirect(myContextPath + '/parts/view/page', '', 'GET');
            },
            error: function(e) {
                console.log("ERROR: ", e);
            }
        })
    })

    $(document).on('keyup', '.amountCalc', function() {
        var closestTr = $(this).closest('tr')
        priceCalculation(closestTr)
    }).on('ifChecked', '.taxInclusive', function() {
        var closestTr = $(this).closest('tr')
        priceCalculation(closestTr)
    }).on('ifUnchecked', '.taxInclusive', function() {
        var closestTr = $(this).closest('tr')
        priceCalculation(closestTr)
    })

})

function priceCalculation(closestTr) {
    let unitPrice = Number(getAutonumericValue(closestTr.find('input[name="unitPrice"]')));
    let quantity = Number(getAutonumericValue(closestTr.find('input[name="quantity"]')));

    setAutonumericValue(closestTr.find('input[name="amount"]'), ifNotValid(unitPrice, 0.0) * ifNotValid(quantity, 0.0));
    let amount = Number(getAutonumericValue(closestTr.find('input[name="amount"]')));
    if (closestTr.find('.taxInclusive').is(':checked')) {
        setAutonumericValue(closestTr.find('input[name="taxAmount"]'), amount * ($.i18n.prop('common.tax') / 100));
    }else {
        setAutonumericValue(closestTr.find('input[name="taxAmount"]'), 0.0);
    }

    let taxAmount = Number(getAutonumericValue(closestTr.find('input[name="taxAmount"]')));

    setAutonumericValue(closestTr.find('input[name="taxIncludedAmount"]'), ifNotValid(amount, 0.0) + ifNotValid(taxAmount, 0.0));
}
