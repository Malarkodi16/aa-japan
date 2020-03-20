$(function() {

    //Getting bill-of-landing details
    $.getJSON(myContextPath + "/data/blData.json", function(data) {
        blNoJson = data;
        $('#billOfLandingNo').select2({
            allowClear: true,
            width: '100%',
            data: $.map(blNoJson, function(item) {
                return {
                    id: item.blNo,
                    text: item.blNo,
                    data: item
                };
            })
        }).val('').trigger('change').on('change', function() {
            var blNo = $(this).val();
            billOfLanding(blNo);
        })
    })

    $('#btn-save').on('click', function() {
        var data = getFormData($('.bof'));
        console.log(data);
    })
    
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    })

})

//null check
function billOfLanding(data) {
    if (isEmpty(data)) {
        $('textarea[name="consignee"]').val('');
        $('textarea[name="notifyParty"]').val('');
        $('input[name="vesselVoyage"]').val('');
        $('input[name="portOfLoading"]').val('');
        $('input[name="portOfDischarge"]').val('');
        $('input[name="portOfDelivery"]').val('');
        $('input[name="finalDestination"]').val('');
        return
    }
    blRequest(data);
}

//ajax call
function blRequest(data) {
    $.ajax({
        beforeSend: function() {
            $('#spinner').show();
        },
        complete: function() {
            $('#spinner').hide();
        },
        type: "get",
        url: myContextPath + "/accounts/billoflanding/blNo/" + data,
        contentType: "application/json",
        async: false,
        success: function(data) {
            var blData = data[0];
            $('textarea[name="consignee"]').val(blData.consignee);
            $('textarea[name="notifyParty"]').val(blData.notifyParty);
            $('input[name="vesselVoyage"]').val(blData.vesselVoyage);
            $('input[name="portOfLoading"]').val(blData.portOfLoading);
            $('input[name="portOfDischarge"]').val(blData.portOfDischarge);
            $('input[name="portOfDelivery"]').val(blData.portOfDelivery);
            $('input[name="finalDestination"]').val(blData.finalDestination);
        }
    });
}
