var exchangerateJson, specialExchangeRateJson={};
$(function() {
    //set latest exchangerate
    $.getJSON(myContextPath + '/get/exchange.json', function(data) {
        exchangerateJson = data;
        for (let i = 0; i < exchangerateJson.length; i++) {
            $('span#currency_' + exchangerateJson[i].currencySeq).html(exchangerateJson[i].exchangeRate);
            $('#exchange_rate_date').html(exchangerateJson[i].lastModifiedDate);
        }
    })

    //set latest specialExchangeRate
    $.getJSON(myContextPath + '/get/special/exchange.json', function(data) {
        specialExchangeRateJson = data;
        //         for (let i = 0; i < specialExchangeRateJson.length; i++) {
        //             $('span#currency_' + exchangerateJson[i].currencySeq).html(exchangerateJson[i].exchangeRate);
        //             $('#exchange_rate_date').html(exchangerateJson[i].lastModifiedDate);
        //         }
    })
    //check exchangerate updated for today

    //edit exchangerate
    $("#exchangerate-modal").on('show.bs.modal', function(event) {
        for (let i = 0; i < exchangerateJson.length; i++) {
            let exchangerate = exchangerateJson[i];
            let element = $(this).find('input#currency_exchange_rate_' + exchangerate.currencySeq);
            setAutonumericValue(element, exchangerate.exchangeRate);
            element = $(this).find('input#currency_sales_exchange_rate_' + exchangerate.currencySeq);
            setAutonumericValue(element, exchangerate.salesExchangeRate);
            element = $(this).find('input#currency_special_exchange_rate_' + exchangerate.currencySeq);
            setAutonumericValue(element, exchangerate.specialExchangeRate);
            $(this).find('.autonumeric').autoNumeric('init')
        }
    }).on('change', 'input[name="exchangeRate"]', function() {
        var usExchRate = Number(getAutonumericValue($('#exchangerate-modal').find('input#currency_exchange_rate_2')))
        var ausExchRate = Number(getAutonumericValue($('#exchangerate-modal').find('input#currency_exchange_rate_3')))
        var poundExchRate = Number(getAutonumericValue($('#exchangerate-modal').find('input#currency_exchange_rate_4')))

        setAutonumericValue($('#exchangerate-modal').find('input#currency_sales_exchange_rate_2'), (ifNotValid(usExchRate, 0) - ifNotValid(specialExchangeRateJson.usDlSalesExchange, 1.75)));
        setAutonumericValue($('#exchangerate-modal').find('input#currency_special_exchange_rate_2'), (ifNotValid(usExchRate, 0) + ifNotValid(specialExchangeRateJson.usDlSpclExchange, 0.80)));

        setAutonumericValue($('#exchangerate-modal').find('input#currency_sales_exchange_rate_3'), (ifNotValid(ausExchRate, 0) - ifNotValid(specialExchangeRateJson.ausDlSalesExchange, 1.75)));
        setAutonumericValue($('#exchangerate-modal').find('input#currency_special_exchange_rate_3'), (ifNotValid(ausExchRate, 0) + ifNotValid(specialExchangeRateJson.ausDlSpclExchange, 0.80)));

        setAutonumericValue($('#exchangerate-modal').find('input#currency_sales_exchange_rate_4'), (ifNotValid(poundExchRate, 0) - ifNotValid(specialExchangeRateJson.poundSalesExchange, 1.75)));
        setAutonumericValue($('#exchangerate-modal').find('input#currency_special_exchange_rate_4'), (ifNotValid(poundExchRate, 0) + ifNotValid(specialExchangeRateJson.poundSpclExchange, 0.80)));

    }).on('click', '#saveExchg', function() {
        autoNumericSetRawValue($("#exchangerate-modal").find('.autonumeric'))
        var objectArr = [];
        var object;
        var data = {};
        var usData = getFormData($("#exchangerate-modal").find('tr.us input'));
        var americanData = getFormData($("#exchangerate-modal").find('tr.american input'));
        var poundData = getFormData($("#exchangerate-modal").find('tr.pound input'));
        objectArr.push(usData);
        objectArr.push(americanData);
        objectArr.push(poundData);

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(objectArr),
            url: myContextPath + "/accounts/save/exchage",
            contentType: "application/json",
            async: true,
            success: function(data) {
                exchangerateJson = data.data;
                $('#exchangerate-modal').modal('toggle');
                for (let i = 0; i < exchangerateJson.length; i++) {
                    $('span#currency_' + exchangerateJson[i].currencySeq).html(exchangerateJson[i].exchangeRate);
                    $('#exchange_rate_date').html(exchangerateJson[i].lastModifiedDate);
                }
            }
        });

    });

});
