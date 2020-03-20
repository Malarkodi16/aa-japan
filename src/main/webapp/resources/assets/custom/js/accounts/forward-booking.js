$(function() {
    // Select2 initialization
    $('select.select2').select2({
        width: '100%',
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true,
    });
    // DatePicker initialization
    $('.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true,
        defaultDate: new Date()
    }).on('change', function() {
        $(this).valid();

    })
    // price fields autonumeric
    $('.autonumeric').autoNumeric('init');
    function regex_escape(text) {
    	return text.replace(/,/g, "").replace(/\.[0-9]{1,}/gi, "").replace(/¥/g, "");
    	};

    // Customize Datatable
    $('#table-filter-search').keyup(function() {
         var query = regex_escape($(this).val());
        table.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });
    // Bank Json
    $.getJSON(myContextPath + "/data/bank.json", function(data) {
        bankJson = data;
        $('#bank').select2({
            allowClear: true,
            width: '100%',
            data: $.map(bankJson, function(item) {
                return {
                    id: item.bankSeq,
                    text: item.bankName,
                    data: item
                };
            })
        }).val('').trigger('change');
    })
    // Currency aSign
    $.getJSON(myContextPath + "/data/currency.json", function(data) {
        currencyJson = data;
        $('#currency').select2({
            allowClear: true,
            width: '100%',
            data: $.map(currencyJson, function(item) {
                return {
                    id: item.currencySeq,
                    text: item.currency,
                    data: item
                };
            })
        }).val('').trigger('change');
    })

    //Add Payment
    var forwardBBookingEle = $('#add-booking')
    $(forwardBBookingEle).on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        $(forwardBBookingEle).find('input[name="bookingDate"]').datepicker("setDate", new Date());
    }).on('hidden.bs.modal', function() {
        $(this).find('select,input[type="text"]').val('').trigger('change');
    }).on('click', '#forward-booking-save', function() {

        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }
        if (!forward_booking_form.valid()) {
            return false;
        }
        data = {};
        data['bookingDate'] = forwardBBookingEle.find('input[name="bookingDate"]').val();
        data['bank'] = forwardBBookingEle.find('select[name="bank"]').val();
        data['closingDate'] = forwardBBookingEle.find('input[name="closingDate"]').val();
        data['currency'] = forwardBBookingEle.find('select[name="currency"]').val();
        data['amount'] = forwardBBookingEle.find('input[name="amount"]').autoNumeric('get');
        data['currentExchangeRate'] = forwardBBookingEle.find('input[name="currentExchangeRate"]').autoNumeric('get');
        data['bookingExchangeRate'] = forwardBBookingEle.find('input[name="bookingExchangeRate"]').autoNumeric('get');

        let response = saveForwardBooking(data)
        if (response.status == 'success') {
            $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Booking Success.');
            $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                $("#alert-block").slideUp(500);
            });
            $(forwardBBookingEle).modal('toggle');
            table.ajax.reload();
        }
    }).on('change', 'select[name="bank"]', function() {
        let val = $(this).val();
        if (!isEmpty(val)) {
            forwardBBookingEle.find('.modal-title.bank-balance').removeClass('hidden');
            $.getJSON(myContextPath + "/bank/currentBalance?bankId=" + val + '&currency=' + 1, function(data) {
                forwardBBookingEle.find('.modal-title.bank-balance>span.amount').autoNumeric('init').autoNumeric('set', data.data)
            });

        } else {
            forwardBBookingEle.find('.modal-title.bank-balance').addClass('hidden').find('span.amount').autoNumeric('init').autoNumeric('set', 0);
        }
    }).on('change', 'select[name="currency"]', function() {
        var data = $(this).select2('data')[0].data;
        let val = $(this).val();
        if (!isEmpty(data)) {
            $(this).closest('.item').find('input.autonumeric').autoNumeric('update', {
                mDec: val != 2 ? 0 : 2,
                aSign: data.symbol + ' '
            });
            $(this).closest('.item').find('input[name="currentExchangeRate"]').autoNumeric('set', data.exchangeRate);

        } else {
            $(this).closest('.item').find('input[name="currentExchangeRate"]').val("");
        }
    });

    var tableEle = $('#table-forward-booking');
    table = tableEle.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ordering": true,
        "ajax": myContextPath + "/accounts/forward/booking/list/data-source",
        select: {
            style: 'single',
            selector: 'td:first-child>input'
        },
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "className": "details-control",
            "data": "bookingDate"

        }, {
            targets: 1,
            "className": "details-control",
            "data": "bank"

        }, {
            targets: 2,
            "className": "details-control",
            "data": "closingDate"
        }, {
            targets: 3,
            "data": "currency",
            "className": "details-control",
        }, {
            targets: 4,
            "data": "amount",
            "className": "dt-right details-control",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                return '<span class="autonumber" data-a-sign="'+row.symbol +' " data-m-dec="' + (row.currency != 2 ? 0 : 2) + '">' + data + '</span>';
            }
        }, {
            targets: 5,
            "data": "currentExchangeRate",
            "className": "dt-right details-control",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                    return '<span class="autonumber"  data-a-sign="'+row.symbol +' " data-m-dec="' + (row.currency != 2 ? 0 : 2) + '">' + data + '</span>';
            }
        }, {
            targets: 6,
            "data": "bookingExchangeRate",
            "className": "dt-right details-control",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                    return '<span class="autonumber" data-a-sign="'+row.symbol +' " data-m-dec="' + (row.currency != 2 ? 0 : 2) + '">' + data + '</span>';
            }
        }],

        "drawCallback": function(settings, json) {
            tableEle.find('span.autonumber').autoNumeric('init')

        }

    });
});

let forward_booking_form = $('#add-booking form#forward-booking-form');
//validation
forward_booking_form.validate({

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
        'bookingDate': 'required',
        'bank': 'required',
        'closingDate': 'required',
        'currency': 'required',
        'amount': 'required',
        'currentExchangeRate': 'required',
        'bookingExchangeRate': 'required',

    }
});

var saveForwardBooking = function(data) {
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
        url: myContextPath + "/accounts/forward/booking/save",
        async: false,
        contentType: "application/json",
        success: function(data) {
            result = data;
        }
    });
    return result;
}

function initBankData(bankEle, bankJson) {}

function initCurrencyData(currencyEle, currencyJson) {}
