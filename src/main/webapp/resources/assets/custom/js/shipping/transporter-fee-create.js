$(function() {
    $(document).on('focus', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').addClass('highlight');
    });
    $(document).on('blur', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').removeClass('highlight');
    })
    $(document).on('focus', '.select2-selection--single', function(e) {
        select2_open = $(this).parent().parent().siblings('select');
        select2_open.select2('open');
    });
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })
    $('.select2').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true,
    });

    // Maker model
    $('#maker').on('change', function() {
        var data = $(this).select2('data');
        var modelEle = $('#model');
        $(modelEle).empty();
        $('#category').val('');
        if (data.length > 0 && !isEmpty(data[0].data)) {
            var modelList = data[0].data.models;
            $(modelEle).select2({
                allowClear: true,
                data: $.map(modelList, function(item) {
                    return {
                        id: item.modelName,
                        text: item.modelName,
                        data: item
                    };
                })
            }).val('').trigger("change");
        }

    });
    $("#from,#to").on('change', function() {
        var from = $('#from').val();
        var to = $('#to').val();
        if (!isEmpty(from) && from == (to)) {
            $('#to').val('').trigger('change');
            alert($.i18n.prop('alert.from.to.should.not.same'));
        }
    })
    $.ajaxSetup({
        async: false
    });
    $.getJSON(myContextPath + "/data/transporters.json", function(data) {

        transportersJson = data;
        $('#transporter').select2({
            placeholder: "Select Transporter",
            allowClear: true,
            width: '100%',
            data: $.map(transportersJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        });
    })

    $.getJSON(myContextPath + "/data/transport-category.json", function(data) {

        transportCategoryJson = data;
        $('#transportCategory').select2({
            placeholder: "Transport Category",
            allowClear: true,
            width: '100%',
            data: $.map(transportCategoryJson, function(item) {
                return {
                    id: item.name,
                    text: item.name
                };
            })
        });
    })

    $.getJSON(myContextPath + "/data/locations.json", function(data) {
        locationJson = data;
        $('#from,#to').select2({
            allowClear: true,
            width: '100%',
            data: $.map(locationJson, function(item) {
                return {
                    id: item.code,
                    text: item.displayName
                };
            })
        })
        $('#from').val($('#from').attr('data-value')).trigger("change");
        $('#to').val($('#to').attr('data-value')).trigger("change");

    })

    $.getJSON(myContextPath + "/data/makerModel.json", function(data) {
        makersJson = data;
        var ele = $('#maker');
        $(ele).select2({
            allowClear: true,
            data: $.map(makersJson, function(item) {
                return {
                    id: item.name,
                    text: item.name,
                    data: item
                };
            })
        }).val($(ele).attr('data-value')).trigger("change");
        $('#model').val($('#model').attr('data-value')).trigger("change");
    })
//     $.getJSON(myContextPath + "/data/categories.json", function(data) {
//         var categoryJson = data;
//         var ele = $('#categories');
//         $(ele).select2({
//             matcher: function(params, data) {
//                 return matchStart(params, data);
//             },
//             width: '100%',
//             data: $.map(categoryJson, function(item) {
//                 var childrenArr = [];
//                 $.each(item.subCategories, function(i, val) {
//                     childrenArr.push({
//                         id: val.name,
//                         text: val.name
//                     })
//                 })
//                 return {
//                     text: item.name,
//                     children: childrenArr
//                 };
//             })
//         });

//     })
    $.ajaxSetup({
        async: true
    });

    /*numbers*/
    $('.autonumber').autoNumeric('init');

    $('#transporter-fee-Form').on('submit', function() {
        autoNumericSetRawValue($('input.autonumber'))
    })

    //for edit
    var id = $('#id').val();
    if (!isEmpty(id)) {
        update(id)
    }

    $('.reset').on('click', function() {
        location.reload();
    })
});

function update(id) {
    $.getJSON(myContextPath + '/transport/transporter/fee/info/' + id + '.json', function(data) {
        data = data.data;
        console.log(data);
        var amount = ifNotValid(data.amount, '');
        var transporter = ifNotValid(data.transporter, '');
        var from = ifNotValid(data.from, '');
        var to = ifNotValid(data.to, '');
        var categories = ifNotValid(data.categories, []);
        var transportCategory = ifNotValid(data.transportCategory, '');

        $('select[name="transporter"]').val(transporter).trigger('change');
        $('select[name="from"]').val(from).trigger('change');
        $('select[name="to"]').val(to).trigger('change');
        $('select[name="categories"]').val(categories).trigger('change');
        $('select[name="transportCategory"]').val(transportCategory).trigger('change');

        $('input[name="amount"]').autoNumeric('set', amount);
    });
}
