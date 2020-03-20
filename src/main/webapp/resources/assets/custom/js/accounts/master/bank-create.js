var currencyJson
$(function() {

    $.getJSON(myContextPath + "/data/currency.json", function(data) {
        currencyJson = data;
        $('#currencyType').select2({
            allowClear: true,
            width: '100%',
            placeholder: 'All',
            data: $.map(currencyJson, function(item) {
                return {
                    id: item.currencySeq,
                    text: item.currency + ' ( ' + item.symbol + ' ) '
                };
            })
        })
    });

    $.getJSON(myContextPath + "/data/accname.json", function(data) {
        var coacategorytypeJson = data;
        $('#coaCode').select2({
            allowClear: true,
            width: '100%',
            data: $.map(coacategorytypeJson, function(item) {
                return {
                    id: item.code,
                    text: item.subAccount + '(' + item.account + ')' + '(' + item.code + ')',
                };
            })
        }).val('').trigger("change");
    })

    $('#btn-save').on('click', function() {
        if (!$("#bank-create-form").find('input,select').valid()) {
            return;
        }
        var bankData = getFormData($('#bank-create-form').find('input,select'));

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(bankData),
            url: myContextPath + "/master/save-bank",
            contentType: "application/json",
            success: function(status) {
              
            }
        });
    })

})
