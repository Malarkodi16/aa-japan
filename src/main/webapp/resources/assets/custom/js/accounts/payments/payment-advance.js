$(function() {
    // Select2 initialization
    $('select.select2').select2({
        width: '100%',
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true,
    });
    //Dashboard initialization
    $.getJSON(myContextPath + "/data/accounts/payment-booking-count", function(data) {
        setPaymentBookingDashboardStatus(data.data)
    });
    function regex_escape(text) {
    	return text.replace(/,/g, "").replace(/\./gi, "").replace(/¥/g, "");
    	};
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });
    //icheck
    $('input[type="radio"][name="paymentType"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        if ($(this).val() == "0") {} else if ($(this).val() == "1") {}
    });
    $('input[type="radio"][name="radioShowTable"].minimal').iCheck({
        checkboxClass: 'icheckbox_minimal-blue',
        radioClass: 'iradio_minimal-blue'
    }).on('ifChecked', function(e) {
        if ($(this).val() == "0") {
            table.column(6).visible(true);
            table.ajax.reload();
        } else if ($(this).val() == "1") {
            table.column(6).visible(true);
            table.ajax.reload();
        } else if ($(this).val() == "2") {
            table.column(6).visible(false);
            table.ajax.reload();
        }
    });
    //DatePickre initialization
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

    });
    //Select initialization
    $('select[name="remitterType"],select[name="remitter"]').select2({
        allowClear: true,
        width: "100%"
    }).val('').trigger("change");
    //automuric initialization
    $('.amount').autoNumeric('init');
    var remitterSelect = $('select[name="remitter"]')
    $('select[name="remitterType"]').on('change', function() {
        remitterSelect.empty();
        var remitType = $(this).val()
        if (remitType == "SUPPLIER") {
            $.getJSON(myContextPath + "/data/suppliers.json", function(data) {
                forwarderJson = data;
                $(remitterSelect).select2({
                    allowClear: true,
                    width: '100%',
                    data: $.map(data, function(item) {
                        return {
                            id: item.supplierCode,
                            text: item.company
                        };
                    })
                }).val('').trigger("change");
            })
        } else if (remitType == "FORWARDER") {
            $.getJSON(myContextPath + "/data/forwarders.json", function(data) {
                forwarderJson = data;
                $(remitterSelect).select2({
                    allowClear: true,
                    width: '100%',
                    data: $.map(data, function(item) {
                        return {
                            id: item.code,
                            text: item.name
                        };
                    })
                }).val('').trigger("change");
            })
        } else if (remitType == "TRANSPORTER") {
            $.getJSON(myContextPath + "/data/transporters.json", function(data) {
                forwarderJson = data;
                $(remitterSelect).select2({
                    allowClear: true,
                    width: '100%',
                    data: $.map(data, function(item) {
                        return {
                            id: item.code,
                            text: item.name
                        };
                    })
                }).val('').trigger("change");
            })
        }
    })

    //Add Payment
    var addPayment = $('#add-payment')
    $(addPayment).on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
    }).on('hidden.bs.modal', function() {
        $(this).find('select,input[type="text"]').val('').trigger('change');
    }).on('click', '#charge-save', function() {

        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
         if (!add_payment_details_form.valid()) {
            return false;
        }
        var data = {};
        data['paymentType'] = $(addPayment).find('input[name="paymentType"]:checked').val();
        data['remitterType'] = $(addPayment).find('select[name="remitterType"]').val();
        data['remitterId'] = $(addPayment).find('select[name="remitter"]').val();
        data['amount'] = $(addPayment).find('input[name="amount"]').autoNumeric('get');
        let response = updatePayment(data)
        if (response.status == 'success') {
            $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Invoice uploaded.');
            $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                $("#alert-block").slideUp(500);
            });
            $(addPayment).modal('toggle');
            table.ajax.reload();
        }
    })
    var updatePayment = function(data) {
        let result;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/payment/createAdvanceAndPrePayment",
            async: false,
            contentType: "application/json",
            success: function(data) {
                result = data;
            }
        });
        return result;
    }

    var tableEle = $('#table-advance-prepayment');
    table = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ordering": true,
        "ajax": {
            url: myContextPath + "/accounts/payment/createAdvanceAndPrePayment/data-source",
            "data": function(data) {
                var selected = $('input[name="radioShowTable"]:checked').val();
                data["flag"] = selected;
                return data;
            }
        },

        select: {
            style: 'single',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            className: 'select-checkbox',
            "data": "remitterId",
            "visible": false,
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox"  name="selRow" type="checkbox" data-remitterId="' + row.remitterId + '" value="' + data + '">';
                }
                return data;
            }
        }, {
            targets: 1,
            "className": "details-control",
            "data": "createdDate"

        }, {
            targets: 2,
            "className": "details-control",
            "data": "paymentType",
            "render": function(data, type, row) {
                var data;
                if (data == 0) {
                    data = "Advance"
                } else if (data == 1) {
                    data = "Pre Payment"
                }
                return data
            }

        }, {
            targets: 3,
            "className": "details-control",
            "data": "remitterType"
        }, {
            targets: 4,
            "data": "remitTo",
            "className": "details-control",
            "className": "vcenter"
        }, {

            targets: 5,
            "className": "dt-right",
            "type": "num-fmt",
            "data": "amount",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="¥ " data-m-dec="0">' + data + '</span>';
            }
        }, {
            targets: 6,
            "data": "remitterId",
            "render": function(data, type, row) {
                var selected = $('input[name="radioShowTable"]:checked').val();
                var button;
                if (selected == "0") {
                    button = '<button type="button" name="approve" id="approve" class="btn btn-primary  ml-5 btn-xs" title="Approve Payment"><i class="fa fa-check"></i></button>';
                } else if (selected == "1") {
                    button = '<button type="button" name="complete" class="btn btn-primary  ml-5 btn-xs" title="Completed Payment" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-approve-payment"><i class="fa fa-check"></i></button>';
                }
                return button;
            }
        }],

        "drawCallback": function(settings, json) {
            tableEle.find('input.autonumber,span.autonumber').autoNumeric('init')

        }

    });

    //Approve Payment
    table.on('click', '#approve', function(event) {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return;
        }
        var rowData = table.row($(this).closest('tr')).data();
        var closestTr = $(this).closest('tr')
        var row = table.row(closestTr)
        let params = rowData.id;
        approvePayment($.param({
            "id": params
        }));

    })

    var approvePayment = function(params) {
        let result;
        $('#spinner').show();
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            processData: false,
            url: myContextPath + "/accounts/payment/approve/advanceAndPrePayment?" + params,
            contentType: false,
            success: function(data) {
                if (data.status == 'success') {
                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Payment Approved.');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });

                    table.ajax.reload();
                    setPaymentBookingDashboardStatus(data);
                }
            }
        });
        return result;
    }

    let modal_approve_payment = $("#modal-approve-payment");
    var statusUpdateEle;
    modal_approve_payment.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        statusUpdateEle = $(e.relatedTarget);
    }).on('hidden.bs.modal', function() {
        $(this).find('select,input,textarea').val('').trigger('change');
    }).on('click', '#approve', function() {
        var rowData = table.row($(statusUpdateEle).closest('tr')).data();
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        if (!payment_details_form.valid()) {
            return false;
        }

        let data = {}
        autoNumericSetRawValue(modal_approve_payment.find('input[name="amount"]'))
        data['bank'] = modal_approve_payment.find('select[name="bank"]').val();
        data['approvedDate'] = modal_approve_payment.find('input[name="approvedDate"]').val();
        data['remarks'] = modal_approve_payment.find('textarea[name="remarks"]').val();
        let params = rowData.id;
        var url =  myContextPath + "/accounts/payment/approve/advance/pre-payment?id=" + params
        let response = approvePaymentComplete(data, url);
    }).on('change', 'select[name="bank"]', function() {
        let val = $(this).val();
        if (!isEmpty(val)) {
            modal_approve_payment.find('.modal-title.bank-balance').removeClass('hidden');
            $.getJSON(myContextPath + "/bank/currentBalance?bankId=" + val + '&currency=' + 1, function(data) {
                modal_approve_payment.find('.modal-title.bank-balance>span.amount').autoNumeric('init').autoNumeric('set', data.data)
            });

        } else {
            modal_approve_payment.find('.modal-title.bank-balance').addClass('hidden').find('span.amount').autoNumeric('init').autoNumeric('set', 0);
        }
    });
    var approvePaymentComplete = function(data, url) {
        let result;

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
            url: url,
            async: false,
            success: function(data) {
                if (data.status == 'success') {
                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Payment Approved.');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                    modal_approve_payment.modal('toggle');
                    table.ajax.reload();
                }
            }
        });
        return result;
    }

    let payment_details_form = $('#modal-approve-payment form#payment-detail-form');
    //validation
    payment_details_form.validate({

        highlight: function(element) {

            $(element).parent().parent().addClass("has-error");
        },
        unhighlight: function(element) {
            $(element).parent().parent().removeClass("has-error");
        },
        errorPlacement: function(error, element) {
            var isFound = false;
            var itr = 0;
            while (!isFound && itr < 5) {
                var e = $(element).parent();
                if (e.find('.help-block').length > 0) {
                    isFound = true;
                }
                element = e;
                itr++;
            }
            if (isFound) {
                $(element).find('.help-block').text(error.text());
            }

        },
        success: function(element) {
            var isFound = false;
            var itr = 0;
            while (!isFound && itr < 5) {
                var e = $(element).parent();
                if (e.find('.help-block').length > 0) {
                    isFound = true;
                }
                element = e;
                itr++;
            }
            if (isFound) {
                $(element).find('.help-block').hide();
            }

        },
        rules: {
            'bank': 'required',
            'approvedDate': 'required'

        }
    });

    let add_payment_details_form = $('#add-payment form#add-payment-detail-form');
    //validation
    add_payment_details_form.validate({

        highlight: function(element) {
            $(element).parent().parent().addClass("has-error");
        },
        unhighlight: function(element) {
            $(element).parent().parent().removeClass("has-error");
        },
        errorPlacement: function(error, element) {
            var isFound = false;
            var itr = 0;
            while (!isFound && itr < 5) {
                var e = $(element).parent();
                if (e.find('.help-block').length > 0) {
                    isFound = true;
                }
                element = e;
                itr++;
            }
            if (isFound) {
                $(element).find('.help-block').text(error.text());
            }

        },
        success: function(element) {
            var isFound = false;
            var itr = 0;
            while (!isFound && itr < 5) {
                var e = $(element).parent();
                if (e.find('.help-block').length > 0) {
                    isFound = true;
                }
                element = e;
                itr++;
            }
            if (isFound) {
                $(element).find('.help-block').hide();
            }

        },
        rules: {
            'remitterType': 'required',
            'remitter': 'required',
            'amount': 'required',

        }
    });

})

function setPaymentBookingDashboardStatus(data) {
    $('#count-auction').html(data.auction);
    $('#count-transport').html(data.transport);
    $('#count-freight').html(data.freight);
    $('#count-others').html(data.others);
    $('#count-storage').html(data.storage);
    $('#count-prepayment').html(data.paymentAdvance);

}
