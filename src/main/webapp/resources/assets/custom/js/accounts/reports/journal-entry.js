$(function() {
    $(document).on('focus', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').addClass('highlight');
    });
    $(document).on('blur', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').removeClass('highlight');
    })
    $('.select2-tag').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true
    })
    /*numbers*/
    $('.autonumber').autoNumeric('init');

    $('#transactionDate').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    })

    $.getJSON(myContextPath + "/data/accnameFilter.json", function(data) {
        mcoaJson = data;
        $('[name="code"]').select2({
            allowClear: true,
            width: '100%',
            placeholder: 'All',
            data: $.map(mcoaJson, function(item) {
                return {
                    id: item.code,
                    text: item.subAccount + '(' + item.code + ')',
                };
            })
        })

    })
    $.getJSON(myContextPath + "/data/currency.json", function(data) {
        mCurrencyJson = data;
        $('[name="currency"]').select2({
            allowClear: true,
            width: '100%',
            placeholder: 'All',
            data: $.map(mCurrencyJson, function(item) {
                return {
                    id: item.currencySeq,
                    text: item.symbol + '(' + item.currency + ')',
                };
            })
        })

    })
    $('#clone-container-journalEntry').cloneya({
        minimum: 1,
        maximum: 999,
        cloneThis: '.clone-container-journalEntry-toclone',
        valueClone: false,
        dataClone: false,
        deepClone: false,
        cloneButton: '.row>.col-md-2>.clone',
        deleteButton: '.row>.col-md-2>.delete',
        clonePosition: 'after',
        serializeID: true,
        serializeIndex: true,

        preserveChildCount: true
    }).on('before_clone.cloneya', function(event, toclone) {
        $(toclone).find('.select2-tag').each(function() {
            if ($(this).data('select2')) {
                $(this).select2('destroy');
            }
        });

    }).on('after_append.cloneya', function(event, toclone, newclone) {
        $(toclone).find('.select2-tag').select2({
            placeholder: function() {
                return $(this).attr('data-placeholder')
            },
            allowClear: true
        });
        $(newclone).find('.select2-tag').select2({
            placeholder: function() {
                return $(this).attr('data-placeholder')
            },
            allowClear: true
           
        }).val('').trigger('change');
        $('.autonumber').autoNumeric('init');

    });

    $('#btn-save-entry').on('click', function() {

        var creditTotal = 0;
        var debitTotal = 0;
        if (!$('#journalEntryForm').valid()) {
            return;
        }
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        var objectArr = [];
        var data = {};
        var transactionDate = $("#transactionDate").val();
        autoNumericSetRawValue($("#journalEntryForm").find('input[name="amount"]'))
        $("#journalEntryForm").find('.journalEntry').each(function() {
            $(this).find('.clone-container-journalEntry-toclone').each(function() {
                var object = {};
                var rowCredit = getFormData($(this).find('.credit').find('input,select,textarea'));
                objectArr.push(rowCredit);
            });

        });

        var creditCheck = objectArr.filter((transactionType)=>{
            return transactionType.type == 1;
        }
        ).map((transactionType)=>{
            return Number(transactionType.amount)
        }
        );
        if (!(creditCheck.length == 0)) {
            var credit = objectArr.filter((transactionType)=>{
                return transactionType.type == 1;
            }
            ).map((transactionType)=>{
                return Number(transactionType.amount)
            }
            ).reduce((sum,transactionType)=>{
                return sum + Number(transactionType);
            }
            );
        }

        var debitCheck = objectArr.filter((transactionType)=>{
            return transactionType.type == 0;
        }
        ).map((transactionType)=>{
            return Number(transactionType.amount)
        }
        );
        if (!(debitCheck.length == 0)) {
            var Debit = objectArr.filter((transactionType)=>{
                return transactionType.type == 0;
            }
            ).map((transactionType)=>{
                return Number(transactionType.amount)
            }
            ).reduce((sum,transactionType)=>{
                return sum + Number(transactionType);
            }
            );
        }

        if (Debit != credit) {
            alert($.i18n.prop('alert.transaction.credit.debit.amount'))
            return;
        }
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(objectArr),
            url: myContextPath + "/accounts/report/journalEntry/save?date=" + transactionDate,
            contentType: "application/json",
            success: function(jnlNo) {
                $('#alert-block').css('display', 'block').html('<strong>Success!</strong>Journal Entry Saved and the Journal Ref. No is ' + ': ' + jnlNo.data);
                $("#journalEntryForm").find('.credit').find('input,select,textarea').val('');
                $("#journalEntryForm").find('.select2-tag').select2({
                    placeholder: function() {
                        return $(this).attr('data-placeholder')
                    },
                    allowClear: true
                });
                $('.datepicker').val('');
                $('.autonumber').autoNumeric('init');
            }
        });

    })
})
