var categoryJSON
$(function() {

    $(document).on('focus', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').addClass('highlight');
    });
    $(document).on('blur', 'input,select,textarea,.select2', function() {
        $(this).closest('.element-wrapper').removeClass('highlight');
    })
    //AutoNumeric
    $('.autonumber').autoNumeric('init');

    //     $('.select2-tag').select2({
    //         placeholder: function() {
    //             return $(this).attr('data-placeholder')
    //         },
    //         allowClear: true,
    //         tags: true  
    //     })
    //     $('#model-container').find('.select2-tag').select2('destroy');

    $.getJSON(myContextPath + "/data/categories.json", function(data) {
        categoryJSON = data
        $('.category').select2({
            allowClear: true,
            width: '100%',
            data: $.map(categoryJSON, function(item) {
                return {
                    id: item.name,
                    text: item.name,
                    data: item
                };
            })
        }).val('').trigger('change');
        $('#model-container').find('select[name="category"]').select2('destroy');

    })
    //     $('#model-clone-conatiner').find('select[name="subcategory"]').select2({
    //         allowClear: true,
    //         width: '100%'
    //     });
    //     $(document).on('change', '.category', function() {
    //         var categoryData = $(this).find('option:selected').data('data')
    //         var subCategorieData = categoryData.data
    //         var closestElement = $(this).closest('.item');
    //         closestElement.find('select[name="subcategory"]').empty();

    //         if (!isEmpty(subCategorieData) || subCategorieData != undefined) {
    //             closestElement.find('.subcategory').select2({
    //                 allowClear: true,
    //                 width: '100%',
    //                 data: $.map(subCategorieData.subCategories, function(item) {
    //                     return {
    //                         id: item.name,
    //                         text: item.name
    //                     };
    //                 })
    //             }).val('').trigger('change')
    //         }
    //     })

    $(document).on('click', 'button.add-maker', function(e) {
        var cloneEle = $('#model-container>.item').clone();
        cloneEle.appendTo('#model-clone-conatiner')
        //         cloneEle.find('.select2-tag').select2({
        //             placeholder: function() {
        //                 return $(this).attr('data-placeholder')
        //             },
        //             allowClear: true,
        //             tags: true
        //         })
        cloneEle.find('select[name="category"]').select2({
            allowClear: true,
            width: '100%',
            data: $.map(categoryJSON, function(item) {
                return {
                    id: item.name,
                    text: item.name,
                    data: item
                };
            })
        }).val('').trigger('change');
        //         cloneEle.find('select[name="subcategory"]').select2({
        //             allowClear: true,
        //             width: '100%'
        //         });
        cloneEle.find('.autonumber').autoNumeric('init');
    })
    $('#save-maker').on('click', function() {
        if (!$("#maker-model").find('input,select').valid()) {
            return;
        }
        var maker = getFormData($('#maker').find('input'));
        var data = []
        var modelArr = []
        var modelEle
        autoNumericSetRawValue($('input.autonumber'))
        $('#model-clone-conatiner').find('.item').each(function() {
            //             var modelSubModel = [];
            var model = getFormData($(this).find('.model'));
            //             var subM = $(this).find('.subModel').find('select').val()
            //             for (var i = 0; i < subM.length; i++) {
            //                 var subModelData = {};
            //                 subModelData.subModelName = subM[i];
            //                 modelSubModel.push(subModelData);
            //             }
            //             model.subModel = modelSubModel
            modelArr.push(model);
        })

        data = maker;
        data['models'] = modelArr;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/master/save-maker",
            contentType: "application/json",
            success: function(status) {
                $.redirect(myContextPath + '/master/list', '', 'GET');
            },
            error: function(status) {
                var alertEle = $('#alert-block').addClass('alert-danger');
                $(alertEle).removeClass('alert-success')
                $(alertEle).css('display', '').html('<strong>Warning!</strong> Model Already exists').fadeIn().delay(3000).fadeOut();
            }
        });
    })

    //delete-maker-item
    $(document).on('click', '.delete-maker-item', function() {
        if ($('#model-clone-conatiner>.item').length > 1) {
            $(this).closest('.item').remove();
        } else {
            $(this).closest('.item').find('input,select').val('').trigger('change');
        }

    })

    //for edit
    var code = $('#code').val();
    if (!isEmpty(code)) {
        editMaker(code)
    }

})

function editMaker(code) {
    $.getJSON(myContextPath + '/master/maker/getInfo/' + code + '.json', function(data) {
        var response = data.data;
        console.log(response)
        $('input[name="name"]').val(ifNotValid(response.name, ''));
        for (var i = 0; i < response.models.length; i++) {
            var cloneEle;
            if (i != 0) {
                cloneEle = $('#model-container>.item').clone();
                cloneEle.appendTo('#model-clone-conatiner')
            } else {
                cloneEle = $('#model-clone-conatiner>.item').first();
            }
            cloneEle.find('input[name="modelName"]').val(ifNotValid(response.models[i].modelName, ''));
            var selectedSubModel = []
            if (!isEmpty(response.models[i].subModel)) {
                selectedSubModel = response.models[i].subModel.map(sub=>sub.subModelName);

            }
            cloneEle.find('select[name="subModelName"]').select2({
                width: '100%',
                tags: true,
                data: $.map(selectedSubModel, function(item) {
                    return {
                        id: item,
                        text: item
                    };
                })
            }).val(selectedSubModel).trigger('change');
            cloneEle.find('select[name="category"]').select2({
                allowClear: true,
                data: $.map(response.models, function(item) {
                    return {
                        id: item.category,
                        text: item.category
                    };
                })
            }).val(response.models[i].category).trigger("change");
            //             cloneEle.find('select[name="subcategory"]').select2({
            //                 allowClear: true,
            //                 data: $.map(response.models, function(item) {
            //                     return {
            //                         id: item.subcategory,
            //                         text: item.subcategory
            //                     };
            //                 })
            //             }).val(response.models[i].subcategory).trigger("change");
            cloneEle.find('input[name="m3"]').val(ifNotValid(response.models[i].m3, ''));
            cloneEle.find('input[name="length"]').val(ifNotValid(response.models[i].length, ''));
            cloneEle.find('input[name="width"]').val(ifNotValid(response.models[i].width, ''));
            cloneEle.find('input[name="height"]').val(ifNotValid(response.models[i].height, ''));
        }
    })
}
