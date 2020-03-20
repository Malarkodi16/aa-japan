$(function() {
    //Tab Highlight
    $(document).on('focus', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').addClass('highlight');
    });
    $(document).on('blur', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').removeClass('highlight');
    })

    //On Select Dropdown Highlight
    $(document).on('focus', '.select2-selection--single', function(e) {
        select2_open = $(this).parent().parent().siblings('select');
        select2_open.select2('open');
    });
    $('#type').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true,
    });
    $('#shipmentType').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true,
    });

    // Select shipment origin country and port
    $.getJSON(myContextPath + "/japan/find-port", function(data) {
        japanJson = data;
        $('#shipmentOriginPort').select2({
            allowClear: true,
            width: '100%',
            data: $.map(japanJson.port, function(item) {
                return {
                    id: item,
                    text: item
                };
            })
        })
         $('#shipmentOriginPort').val($('#shipmentOriginPort').attr('data-value')).trigger("change");
    });
    $('#save,#saveAndContinue').on('click', function(event) {
        var isValid = $('#locationForm').valid();
        if (!isValid) {
            return;
        } else if (!confirm('Do you want to save?')) {
            return false;
        }

    })
})
