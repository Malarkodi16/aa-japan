var makerJson, modelJson
$(function() {
    $.getJSON(myContextPath + "/data/makers.json", function(data) {
        makerJson = data;
        $('#maker').select2({
            allowClear: true,
            width: '100%',
            data: $.map(makerJson, function(item) {
                return {
                    id: item.name,
                    text: item.name,
                    data: item
                };
            })
        }).val('').trigger('change');
    })

    $('#maker').on('change', function() {
        var makerData = $(this).find(':selected').data();
        $('#model').empty();
        if (!isEmpty(makerData)) {
            if (!isEmpty(makerData.data.data)) {
                var modelData = makerData.data.data.models
                $('#model').select2({
                    allowClear: true,
                    width: '100%',
                    data: $.map(modelData, function(item) {
                        return {
                            id: item,
                            text: item

                        };
                    })
                }).val('').trigger('change');
            } else {
                $('#model').select2().val('').trigger('change');
            }
        }
    })

    //yearpicker
    $('.datepicker').datepicker({
        format: 'yyyy',
        viewMode: "years",
        minViewMode: "years",
        autoclose: true,
        clearBtn: true
    })

    //Form Save

       
    $("#save-manufacture").on('click', function() {
        var data = getFormData($('#manufacture-of-year-form').find('input,select'));
         if (!$('#manufacture-of-year-form').valid()) {
            return false;
        }
          $('#maker').val(data.maker).trigger('change');
          $('#model').val(data.model).trigger('change');
          $('input[name="modelNo"]').val(data.modelNo);
          $('input[name="frame"]').val(data.frame);
          $('input[name="formatedSerialNoFrom"]').val(data.formatedSerialNoFrom);
          $('input[name="formatedSerialNoTo"]').val(data.formatedSerialNoTo);
          $('input[name="manufactureYear"]').val(data.manufactureYear);
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/documents/year-of-manufacture/save",
            contentType: "application/json",
            success: function(data) {
                $('#manufacture-of-year-form').find('input,select').val('').trigger('change')
                   if (data.status === 'success') {
           
                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong>Manufactured Year Saved.');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                }
            }
        });
    })

    //Form reset
    $("#cancel").on('click', function() {
        $('#manufacture-of-year-form').find('input,select').val('').trigger('change')
    })

      //for edit
    var code = $('#code').val();
    if (!isEmpty(code)) {
        update(code)
    }
})
function update(code) {
    $.getJSON(myContextPath + '/documents/year-of-manufacture/manufactureDate/infoActive/' + code + '.json', function(data) {
        data = data.data;
         $('#maker').val(data.maker).trigger('change');
         $('#model').val(data.model).trigger('change');
         $('input[name="modelNo"]').val(data.modelNo);
         $('input[name="frame"]').val(data.frame);
         $('input[name="formatedSerialNoFrom"]').val(data.formatedSerialNoFrom);
         $('input[name="formatedSerialNoTo"]').val(data.formatedSerialNoTo);
          $('input[name="manufactureYear"]').val(data.manufactureYear);

    });
}
