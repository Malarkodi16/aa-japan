$(function() {

    $("#saveExchangeRate").on('click', function() {
        var data = getFormData($('#special-exchange-form').find('input'));
        data.usDlSalesExchange = parseFloat(data.usSalesExchangeRate);
        data.usDlSpclExchange = parseFloat(data.usSpecialExchangeRate);
        data.ausDlSalesExchange = parseFloat(data.auSalesExchangeRate);
        data.ausDlSpclExchange = parseFloat(data.auSpecialExchangeRate);
        data.poundSalesExchange = parseFloat(data.poundSalesExchangeRate);
        data.poundSpclExchange = parseFloat(data.poundSpecialExchangeRate);
        if (!$('#special-exchange-form').valid()) {
            return false;
        }
 
        $('input[name="usSalesExchangeRate"]').val(data.usDlSalesExchange);
        $('input[name="usSpecialExchangeRate"]').val(data.usDlSpclExchange);
        $('input[name="auSalesExchangeRate"]').val(data.ausDlSalesExchange);
        $('input[name="auSpecialExchangeRate"]').val(data.ausDlSpclExchange);
        $('input[name="poundSalesExchangeRate"]').val(data.poundSalesExchange);
        $('input[name="poundSpecialExchangeRate"]').val(data.poundSpclExchange);

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/master/specialExchageRate/save",
            contentType: "application/json",
            success: function(data) {
                $('#special-exchange-form').find('input').val('').trigger('change')
                   if (data.status === 'success') {
                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong>Exchange Rate Saved.');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                }
            }
        });
    })

     //Form reset
    $("#cancel").on('click', function() {
        $('#special-exchange-form').find('input').val('').trigger('change')
    })
})
