$(function() {
    $.getJSON(myContextPath + "/data/accounts/claim-count", function(data) {
        setClaimDashboardStatus(data.data)
    });
    $('input.autonumber').autoNumeric('init')
    $('input[type="radio"][name=radioReceivedFilter].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        let val = $(this).val();
        if (val == 1) {
            $('form#form-received-upload').removeClass('hidden');
        } else {
            $('form#form-received-upload').addClass('hidden');
        }

    })

    //recycle datatable
    var table_insurance_ele = $('#table-claim-insurance');
    var table_insurance = $(table_insurance_ele).DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ordering": true,
        "aaSorting": [[0, "desc"]],
        "ajax": myContextPath + "/accounts/claim/insurance/applied/datasource",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "data": "chassisNo"

        }, {
            targets: 1,
            "data": "company"

        }, {
            targets: 2,
            "data": "insuranceNo"

        }, {
            targets: 3,
            "data": "ownerAddress"

        }, {
            targets: 4,
            "data": "ownerName"

        }, {
            targets: 5,
            "data": "fromYear"

        }, {
            targets: 6,
            "data": "fromMonth"

        }, {
            targets: 7,
            "data": "fromDate"

        }, {
            targets: 8,
            "data": "toYear"

        }, {
            targets: 9,
            "data": "toMonth"
        }, {
            targets: 10,
            "data": "toDate"
        }, {
            targets: 11,
            "data": "period"
        }]

    });

});
function setClaimDashboardStatus(data) {
    $("#count-tax").html(data.purchaseTax + ' / ' + data.commissionTax);
    $('#count-recycle').html(data.recycle);
    $('#count-carTax').html(data.carTax);
    $('#count-insurance').html(data.insurance);
    $('#count-radiation').html(data.radiation);
    
}
