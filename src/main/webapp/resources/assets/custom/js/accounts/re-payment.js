$(function() {

    // preclose icheckbox_minimal
    $('#preClose').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    });
    // get bank json
    $.getJSON(myContextPath + "/data/bank.json", function(data) {
        $('#bank').select2({
            data: $.map(data, function(item) {
                return {
                    id: item.bankSeq,
                    text: item.bankName
                };
            })
        });
    })

    // Date picker
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true

    })
    // initialize select2
    $('select.select2').select2({});
    var searchData;
    $('#loanId').select2({
        allowClear: true,
        minimumInputLength: 2,
        width: "100%",
        ajax: {
            url: myContextPath + "/accounts/loan-search",
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
                searchData = data;
                if (data != null && data.length > 0) {
                    $.each(data, function(index, item) {
                        results.push({
                            id: item.loanId,
                            text: item.loanId + ' :: ' + item.reference
                        });
                    });
                }
                return {
                    results: results
                }
            }
        }
    });

    $('#preClose, #loanId').on('ifChecked ifUnchecked change', function() {
        $('#savings').addClass('hidden')
        var loanId = $('#loanId').find('option:selected').val();
        var preClose = $('#preClose').is(":checked") ? 1 : 0;
        var response = searchLoanData(loanId, preClose);
        if (!isEmpty(response.data)) {
            $('#bank').val(response.data.bank).trigger('change');
            $('#installmentAmount').val(response.data.installmentAmount);
            $('#loanType').val(response.data.loanType).trigger('change');
            $('#paymentDate').datepicker('setDate', response.data.paymentDate)
            $('#savingAccount').val(response.data.savingAccount)
            $('#loanDtlId').val(response.data.loanDtlId)
            $('#loanId').val(response.data.loanId)
            if (response.data.savingAccount == 1) {
                $('#savings').removeClass('hidden');
                $('#savingsBankAccount').val(response.data.savingsBankAccount);
                $('#savingsAccountAmount').val(response.data.savingsAccountAmount);
            }
            //AutoNumeric
            $('.autonumber').autoNumeric('init');
        } else {
            $('input').val('');
            $('#bank,#loanType').val('').trigger('change');
        }
    })
})

function searchLoanData(loanId, preClose) {
    var response;
    var querytring = "?loanId=" + loanId + "&flag=" + preClose;
    $.ajax({
        beforeSend: function() {
            $('#spinner').show()
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: 'get',
        async: false,
        url: myContextPath + "/accounts/findOne/loan-search" + querytring,
        success: function(data) {
            response = data;
        }
    })
    return response;
}
