$(function() {
    $(".shippingTel").inputmask({
        mask: "(99) 9999-999-999"
    });

    let user_details_form = $('#modal-shipping-user-details #user-details-form');
    let modal_shipping_userDetails = $("#modal-shipping-user-details");
    let targetElement;
    modal_shipping_userDetails.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        targetElement = $(e.relatedTarget);

        var rowData = table.row($(targetElement).closest('tr')).data();
        modal_shipping_userDetails.find('input[name="shippingUser"]').val(ifNotValid(rowData.shippingUser, ''))
        modal_shipping_userDetails.find('input[name="shippingId"]').val(ifNotValid(rowData.shippingId, ''))
        modal_shipping_userDetails.find('input[name="shippingTel"]').val(ifNotValid(rowData.shippingTel, ''))
        modal_shipping_userDetails.find('input[name="hsCode"]').val(ifNotValid(rowData.hsCode, ''))

    }).on('hidden.bs.modal', function() {
        $(this).find('input').val('');
    }).on('click', '#userDetails-save', function() {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        if (!user_details_form.valid()) {
            return false;
        }
        var rowData = table.row($(targetElement).closest('tr')).data();

        let data = {};
        data['shippingUser'] = modal_shipping_userDetails.find('input[name="shippingUser"]').val();
        data['shippingId'] = modal_shipping_userDetails.find('input[name="shippingId"]').val();
        data['shippingTel'] = modal_shipping_userDetails.find('input[name="shippingTel"]').val();
        data['hsCode'] = modal_shipping_userDetails.find('input[name="hsCode"]').val();

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            contentType: "application/json",
            url: myContextPath + "/sales/update/userDetails?stockNo=" + rowData.stockNo,
            async: false,
            success: function(data) {
                if (data.status == "success") {
                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong> User details saved.');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                    modal_shipping_userDetails.modal('toggle');
                }
            }
        });
       table.ajax.reload();
    })
})
