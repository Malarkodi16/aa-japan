var categoryJson, makersJson, transmissionTypesJson, fuelTypesJson, supplierJson, locationJson, salesName;

$(function() {

    $(document).on('focus', '.select2-selection--single', function(e) {
        select2_open = $(this).parent().parent().siblings('select');
        select2_open.select2('open');
    });
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })

    // Transmission Type Manual check
    $('#transmission').on('change', function() {
        var transVal = $(this).val();
        if (transVal == "MANUAL") {
            $('#manualTypes').prop("disabled", false);
        } else {
            $('#manualTypes').prop("disabled", true);
            $('#manualTypes').val('').trigger('change');

        }
    })

    /*numbers*/
    $('.autonumber').autoNumeric('init');

    $.ajaxSetup({
        async: false
    });

    $.getJSON(myContextPath + "/data/makerModel.json", function(data) {
        makersJson = data;
        var ele = $('#maker');
        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            data: $.map(makersJson, function(item) {
                return {
                    id: item.name,
                    text: item.name,
                    data: item
                };
            })
        }).val('');
    })

    $('#maker').on('change', function() {
        var data = $(this).select2('data');
        var modelEle = $('#model');
        $(modelEle).empty();
        $('#subcategory').empty();
        $('#category').val('');
        if (data.length > 0 && !isEmpty(data[0].data)) {
            var modelList = data[0].data.models;
            $(modelEle).select2({
                matcher: function(params, data) {
                    return matchStart(params, data);
                },
                allowClear: true,
                data: $.map(modelList, function(item) {
                    return {
                        id: item.modelId,
                        text: item.modelName,
                        data: item
                    };
                })
            }).val('').trigger("change");

        }

    });
    $('#subcategory').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true,
        placeholder: "Select Sub Category",

    })

    $('#model').select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true

    }).on('change', function() {
        var data = $(this).select2('data');
        var categorieEle = $('#category');
        var subcategorieEle = $('#subcategory');
        subcategorieEle.empty();

        if (data.length > 0 && !isEmpty(data[0].data)) {
            var catg = ifNotValid(data[0].data.category, '');
            $.getJSON(myContextPath + "/data/categories.json", function(data) {
                categoryJSON = data;
                categorieEle.attr('value', catg);
                var subcatg = [];
                if (!isEmpty(categoryJSON)) {
                    for (let i = 0; i < categoryJSON.length; i++) {
                        if (catg == categoryJSON[i].name) {
                            subcatg = categoryJSON[i].subCategories
                        }
                    }

                    $(subcategorieEle).select2({
                        matcher: function(params, data) {
                            return matchStart(params, data);
                        },
                        allowClear: true,
                        data: $.map(subcatg, function(item) {
                            return {
                                id: item.name,
                                text: item.name
                            };
                        })
                    }).val($(subcategorieEle).attr('value')).trigger("change");

                }
            })

        }

    });
    $.getJSON(myContextPath + "/data/transmissionTypes.json", function(data) {
        transmissionTypesJson = data;
        var ele = $('#transmission');
        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            data: $.map(transmissionTypesJson, function(item) {
                return {
                    id: item.type,
                    text: item.type
                };
            })
        }).val('').trigger("change");
    })

    $.getJSON(myContextPath + "/data/fuelTypes.json", function(data) {
        fuelTypesJson = data;
        var ele = $('#fuel');
        $(ele).select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            data: $.map(fuelTypesJson, function(item) {
                return {
                    id: item.type,
                    text: item.type,
                    data: item
                };
            })
        })
        $('#fuel').val('').trigger("change");
    });

    // to enable async
    $.ajaxSetup({
        async: true
    });

    $('#btn-save').on('click', function(e) {
        if (!$('#stockModelTypeForm').find('input,select').valid()) {
            return false;
        }
        var stockModelTypeForm = $('#stockModelTypeForm');
        var stock = getFormData(stockModelTypeForm.find('.stock-details-to-save.object'));
        var equip = [];
        var equipEle = $('.stock-details-to-save.array');
        for (var i = 0; i < equipEle.length; i++) {
            if (equipEle[i].checked) {
                var equipment = $(equipEle)[i].value;
                equip.push(equipment)
            }
        }

        var stockModelType = {};
        stockModelType["code"] = stock.code;
        stockModelType["modelType"] = stock.modelType;
        stockModelType["unit"] = stock.unit;
        stockModelType["category"] = stock.category;
        stockModelType["subcategory"] = stock.subcategory;
        stockModelType["equipment"] = equip;
        stockModelType["transmission"] = stock.transmission;
        stockModelType["manualTypes"] = stock.manualTypes;
        stockModelType["fuel"] = stock.fuel
        stockModelType["maker"] = stock.maker;
        stockModelType["model"] = stock.model;
        stockModelType["driven"] = stock.driven;
        stockModelType["cc"] = stock.cc;

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            data: JSON.stringify(stockModelType),
            url: myContextPath + "/master/stockType/edit",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    $.redirect(myContextPath + '/master/stockType/list', '', 'GET');
                }

            }
        })
    })

    //for edit
    var code = $('#code').val();
    if (!isEmpty(code)) {
        update(code)
    }

})

function update(code) {
    $.getJSON(myContextPath + '/master/stockType/info/' + code + '.json', function(data) {
        var response = data.data;
        console.log(response)
        var stockModelForm = $('#stockModelTypeForm');
        stockModelForm.find('input[name="modelType"]').val(ifNotValid(response.modelType, ''));
        $('#maker').val(response.maker).trigger("change");
        $('#subcategory').attr('value', response.subcategory);
        $('#model').val(response.model).trigger("change");
        $('#category').val(response.category).trigger("change");
        stockModelForm.find('select[name="unit"]').val(ifNotValid(response.unit, '')).trigger('change');
        stockModelForm.find('select[name="transmission"]').val(response.transmission).trigger('change');
        if (response.transmission == "MANUAL") {
            stockModelForm.find('select[name="manualTypes"]').val(response.manualTypes).trigger('change');
        }
        stockModelForm.find('input[name="cc"]').val(ifNotValid(response.cc, ''));
        stockModelForm.find('select[name="fuel"]').val(ifNotValid(response.fuel, '')).trigger('change');
        stockModelForm.find('select[name="driven"]').val(ifNotValid(response.driven, '')).trigger('change');
        for (var i = 0; i < response.equipment.length; i++) {
            stockModelForm.find('input[name="equipment"][value="' + response.equipment[i] + '"]').attr('checked', true);
        }

    })
}
