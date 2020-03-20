$(function() {
    $(".phone").inputmask({
        mask: "(99) 9999-999-999"
    });
    $(".fax").inputmask({
        mask: "(999) 999-9999"
    });
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
        allowClear: true,
        tags: true
    })

    $('#clone-container-location').cloneya({
        minimum: 1,
        maximum: 999,
        cloneThis: '.clone-container-location-toclone',
        valueClone: false,
        dataClone: false,
        deepClone: false,
        cloneButton: '.clone-btn>.col-md-12>.clone',
        deleteButton: '.clone-btn>.col-md-12>.delete',
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
            allowClear: true,
            tags: true
        });
        $(newclone).find('.select2-tag').select2({
            placeholder: function() {
                return $(this).attr('data-placeholder')
            },
            allowClear: true,
            tags: true
        }).val('').trigger('change');
        $(".phone").inputmask({
            mask: "(99) 9999-999-999"
        });
        $(".fax").inputmask({
            mask: "(999) 999-9999"
        });

    });

    $('select[name="type"]').on('change', function() {
        var value = $(this).find('option:selected').val();
        if (value == 'auction') {
            $('.auction-info').removeClass('hidden');
        } else {
            $('.auction-info').addClass('hidden');
        }
    })
    /*numbers*/
    $('.autonumber').autoNumeric('init');

    // supplier form submit
    $('#supplierForm').on('submit', function() {
        autoNumericSetRawValue($('input.autonumber'))
    })

    $('#supplierForm').on('change', 'select[name="type"]', function() {
        $('#supplierForm').find('input.auctionHouse, select.posNo').val('').trigger('change')
    })
    //for edit
    var supplierCode = $('#supplierCode').val();
    if (!isEmpty(supplierCode)) {
        update(supplierCode)
    }

})

function update(supplierCode) {
    $.getJSON(myContextPath + '/a/supplier/infoActive/' + supplierCode + '.json', function(data) {
        data = data.data;
        var type = ifNotValid(data.type, '');
        var company = ifNotValid(data.company, '');
        var maxDueDays = ifNotValid(data.maxDueDays, '');
        $('#modelSupplierType').val(data.type).trigger('change');
        $('input[name="maxDueDays"]').val(data.maxDueDays);
        $('input[name="maxCreditAmount"]').autoNumeric('init').autoNumeric('set', ifNotValid(data.maxCreditAmount, 0));
        $('input[name="company"]').val(data.company);
        $('input[name="maxCreditAmount"]').val(data.maxCreditAmount);
        for (var i = 0; i < data.supplierLocations.length; i++) {
            if (i != 0) {
                $('button.clone').last().click()
            }
            if (type == 'auction') {
                $('input[name="supplierLocations[' + i + '].auctionHouse"]').val(ifNotValid(data.supplierLocations[i].auctionHouse, ''))
                var posTags = "[" + String(ifNotValid(data.supplierLocations[i].posNos)) + "]";
                var posNosTags = data.supplierLocations[i].posNos
                $('select[name="supplierLocations[' + i + '].posNos"]').select2({
                    allowClear: true,
                    tags: true,
                    width: '100%',
                    data: $.map(posNosTags, function(item) {
                        return {
                            id: item,
                            text: item,
                        };
                    })
                }).val(posNosTags).trigger('change');

            }
            $('input[name="supplierLocations[' + i + '].address"]').val(ifNotValid(data.supplierLocations[i].address, ''));

            $('input[name="supplierLocations[' + i + '].email"]').val(ifNotValid(data.supplierLocations[i].email, ''));
            $('input[name="supplierLocations[' + i + '].phone"]').val(ifNotValid(data.supplierLocations[i].phone, ''));
            $('input[name="supplierLocations[' + i + '].fax"]').val(ifNotValid(data.supplierLocations[i].fax, ''));
            $('input[name="supplierLocations[' + i + '].id"]').val(ifNotValid(data.supplierLocations[i].id, ''));
            $('input[name="supplierLocations[' + i + '].deleteStatus"]').val(ifNotValid(data.supplierLocations[i].deleteStatus, ''));

        }
    });
}
