var categoryJSON
$(function() {
    $(".tel").inputmask({
        mask: "(99) 9999-999-999"
    });

    $(document).on('focus', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').addClass('highlight');
    });
    $(document).on('blur', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').removeClass('highlight');
    })
    //AutoNumeric
    $('.autonumber').autoNumeric('init');

    $(document).on('click', 'button.btn-clone', function(e) {
        var cloneEle = $('#location-container>.item').clone();
        cloneEle.appendTo('#location-clone-conatiner')
        $(".tel").inputmask({
            mask: "(99) 9999-999-999"
        });
        cloneEle.find('.autonumber').autoNumeric('init');
    })
    $('#save-inspection').on('click', function() {
        if (!$("#ins-company").find('input,select').valid()) {
            return;
        }
        var company = getFormData($('#company').find('input'));
        var data = []
        var locationArr = []
        var locationEle
        autoNumericSetRawValue($('input.autonumber'))
        $('#location-clone-conatiner').find('.item').each(function() {

            var location = getFormData($(this).find('.location'));

            locationArr.push(location);
        })

        data = company;
        data['locations'] = locationArr;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/master/inspection/create",
            contentType: "application/json",
            success: function(status) {
                $.redirect(myContextPath + '/master/inspection/list', '', 'GET');
            },
            error: function(status) {
                var alertEle = $('#alert-block').addClass('alert-danger');
                $(alertEle).removeClass('alert-success')
                $(alertEle).css('display', '').html('<strong>Warning!</strong> Location Already exists').fadeIn().delay(3000).fadeOut();
            }
        });
    })

    //delete-maker-item
    $(document).on('click', '.btn-delete', function() {
        if ($('#location-clone-conatiner>.item').length > 1) {
            $(this).closest('.item').remove();
        } else {
            $(this).closest('.item').find('input,select').val('').trigger('change');
        }

    })

    //for edit
    var code = $('#code').val();
    if (!isEmpty(code)) {
        update(code)
    }

})

function update(code) {
    $.getJSON(myContextPath + '/master/inspection/company/info/' + code + '.json', function(data) {
        var response = data.data;
        console.log(response)
        $('input[name="name"]').val(ifNotValid(response.name, ''));
        for (var i = 0; i < response.locations.length; i++) {
            var cloneEle;
            if (i != 0) {
                cloneEle = $('#location-container>.item').clone();
                cloneEle.appendTo('#location-clone-conatiner')
            } else {
                cloneEle = $('#location-clone-conatiner>.item').first();
            }
            $(".tel").inputmask({
                mask: "(99) 9999-999-999"
            });
            cloneEle.find('input[name="locationId"]').val(ifNotValid(response.locations[i].locationId, ''));
            cloneEle.find('input[name="deleteStatus"]').val(ifNotValid(response.locations[i].deleteStatus, ''));
            cloneEle.find('input[name="locationName"]').val(ifNotValid(response.locations[i].locationName, ''));
            cloneEle.find('input[name="zip"]').val(ifNotValid(response.locations[i].zip, ''));
            cloneEle.find('input[name="tel"]').val(ifNotValid(response.locations[i].tel, ''));
            cloneEle.find('input[name="fax"]').val(ifNotValid(response.locations[i].fax, ''));
            cloneEle.find('input[name="contactPerson"]').val(ifNotValid(response.locations[i].contactPerson, ''));
            cloneEle.find('textarea[name="address"]').val(ifNotValid(response.locations[i].address, ''));
        }
    })
}
