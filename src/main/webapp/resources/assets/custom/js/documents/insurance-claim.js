$(function() {
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

    });
    $(this).find('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

    })

    //table Search
    $('#table-filter-search').keyup(function() {
        table_insurance.search($(this).val()).draw();
    });
    $('#table-insurance-claim-filter-length').change(function() {
        table_insurance.page.len($(this).val()).draw();
    });

    //recycle datatable
    var table_insurance_ele = $('#table-claim-insurance');
    var table_insurance = $(table_insurance_ele).DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
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
            "data": "insuranceApplyDate"
        }, {
            targets: 1,
            "data": "chassisNo"

        }, {
            targets: 2,
            "data": "company"

        }, {
            targets: 3,
            "data": "insuranceNo"

        }, {
            targets: 4,
            "data": "ownerAddress"

        }, {
            targets: 5,
            "data": "ownerName"

        }, {
            targets: 6,
            "data": "fromYear"

        }, {
            targets: 7,
            "data": "fromMonth"

        }, {
            targets: 8,
            "data": "fromDate"

        }, {
            targets: 9,
            "data": "toYear"

        }, {
            targets: 10,
            "data": "toMonth"
        }, {
            targets: 11,
            "data": "toDate"
        }, {
            targets: 12,
            "data": "period"
        }]

    });

    $("#save-excel").on("click", function() {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }

        var fileData = new FormData();
        var excelUploadedEle = $('input[name="excelUploaded"]');
        if (excelUploadedEle.prop('files').length > 0) {
            let file = excelUploadedEle.prop('files')[0];
            fileData.append("excelFile", file);
            fileData.append("date", $('input[name="insuranceApplyDate"]').val());
        } else {
            return false;
        }
        uploadExcel(fileData)

    });

    function uploadExcel(fileData) {
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            processData: false,
            data: fileData,
            url: myContextPath + "/accounts/claim/insurance/apply/uploadExcelFile",
            contentType: false,
            async: false,
            success: function(data) {
                if (data.status === "success") {
                    console.log(data)
                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong>');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                    alert(data.data);
                } else {
                    $('#alert-block').css('display', 'block').html('<strong>Warning! </strong>');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                }
            },
            error: function(e) {
                console.log("ERROR: ", e);
            }
        });
    }

});
