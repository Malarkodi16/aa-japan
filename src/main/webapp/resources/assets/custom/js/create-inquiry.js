var categoryJson, makersJson, countriesJson;
$(function() {
    $(document).on('focus', '.select2-selection--single', function(e) {
        select2_open = $(this).parent().parent().siblings('select');
        select2_open.select2('open');
    });
    $(document).on('change', '.has-error', function() {
        $(this).find('input,select,textarea').valid();
    })
    if (document.getElementById("alert-block").style.display != "none") {
        $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
            $("#alert-block").slideUp(500);
        });
    }
    $.getJSON(myContextPath + "/data/makerModel.json", function(data) {
        makersJson = data;
        var ele = $('.maker')
        $('#category').val('');
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
    $.getJSON(myContextPath + "/data/countries.json", function(data) {
        countriesJson = data;
        var eleCountry = $('.country')
        $(eleCountry).select2({
            allowClear: true,
            width: '100%',
            data: $.map(countriesJson, function(item) {
                return {
                    id: item.country,
                    text: item.country

                };
            })
        }).val($(eleCountry).attr('data-value')).trigger("change");
    })

    $('#modal-add-customer').on('hide.bs.modal', function() {
        $(this).find("input,textarea,select").val('').end()
        $(this).find("p").text('').end().find("input[type=checkbox], input[type=radio]").prop("checked", "").end();
    });

    $('#inquiry-items-wrapper').on('change', '.maker', function() {
        var maker = $(this).val();
        var modelEle = $(this).closest('.clone-container-inquiry-item-toclone').find('.model');
        $(modelEle).empty();

        var modelList;
        $.each(makersJson, function(i, item) {
            if (item.name == maker) {
                modelList = item.models;
                return false;
            }

        });
        if (typeof modelList == 'undefined') {
            return;
        }
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

    }).on('change', '.model', function() {

        var modelVal = $(this).val();
        //var subModelEle = $(this).closest('.clone-container-inquiry-item-toclone').find('.subModel');
        var categorieEle = $(this).closest('.clone-container-inquiry-item-toclone').find('.category');
        var subcategorieEle = $(this).closest('.clone-container-inquiry-item-toclone').find('.subCategory');
        var categoryAndSubcategoryEle = $(this).closest('.clone-container-inquiry-item-toclone').find('.categoryAndSubcategory');
        //subModelEle.empty();
        $(categorieEle).val('');
        $(subcategorieEle).val('');
        $(categoryAndSubcategoryEle).val('');
        var categorySelect;
        var selectedMaker = $(this).closest('.clone-container-inquiry-item-toclone').find('.maker').val();
        var modelList;
        $.each(makersJson, function(i, item) {
            if (item.name == selectedMaker) {
                modelList = item.models;
                return false;
            }

        });
        //var subModelList;
        $.each(modelList, function(i, item) {
            if (item.modelName == modelVal) {
                categorySelect = item;
                //subModelList = item.subModel;
                return false;
            }
        });
//         $(subModelEle).select2({
//             allowClear: true,
//             data: $.map(subModelList, function(item) {
//                 return {
//                     id: item.subModelName,
//                     text: item.subModelName
//                 };
//             })
//         }).val('').trigger("change");

        if (!isEmpty(categorySelect)) {
            var catg = ifNotValid(categorySelect.category, '');
            var subcatg = ifNotValid(categorySelect.subcategory, '');
            categorieEle.val(catg)
            subcategorieEle.val(subcatg);
            categoryAndSubcategoryEle.val(catg + '->' + subcatg)

        }

    });
    $('.model.select2-select').select2({
        allowClear: true
    })

    //     $('#save_customer').on('click', function() {
    //         if (!$('#customer-form').valid()) {
    //             return;
    //         }
    //         var data = $("#customer-form").serialize();
    //         $.ajax({
    //             beforeSend: function() {
    //                 $('#spinner').show()
    //             },
    //             complete: function() {
    //                 $('#spinner').hide();
    //             },
    //             type: "post",
    //             data: data,
    //             url: myContextPath + "/customer/save",
    //             async: false,
    //             dataType: "json",
    //             success: function(data) {
    //                 $('#modal-add-customer').modal('toggle');
    //                 $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Customer created.');
    //                 $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
    //                     $("#alert-block").slideUp(500);
    //                 });
    //             }
    //         });
    //     })

    $('#customerCode').select2({
        allowClear: true,
        placeholder: 'Search customer email',
        minimumInputLength: 2,
        ajax: {
            url: myContextPath + "/customer/search?flag=customer",
            dataType: 'json',
            delay: 500,
            data: function(params) {
                var query = {
                    search: params.term,
                    type: 'public'
                }
                return query;

            },
            processResults: function(data) {
                var results = [];
                data = data.data;
                if (data != null && data.length > 0) {
                    $.each(data, function(index, item) {
                        results.push({
                            id: item.code,
                            text: item.companyName + ' :: ' + item.firstName + ' ' + item.lastName + '(' + item.nickName + ')'
                        });
                    });
                }
                return {
                    results: results
                }

            }

        }
    }).on("change", function(event) {
        var id = $(this).val();
        var mobile = $('input[name="mobile"]');
        var customerName = $('input[name="customerName"]');
        var skypeId = $('input[name="skypeId"]');
        var companyName = $('input[name="companyName"]');
        if (id == null || id.length == 0) {
            mobile.val('');
            customerName.val('');
            skypeId.val('');
            companyName.val('');
            return;
        }
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "GET",
            async: false,
            url: myContextPath + "/customer/data/" + id,
            success: function(data) {
                if (data.status == 'success') {
                    mobile.val(data.data.mobileNo);
                    customerName.val(data.data.firstName);
                    skypeId.val(data.data.skypeId);
                    companyName.val(data.data.companyName);
                }
            },
            error: function(e) {
                console.log("ERROR: ", e);

            }
        });
    });

    $('.port').select2({
        allowClear: true,
        placeholder: 'Select Port'
    });
    $('#inquiry-items-wrapper').on('change', '.country', function(event) {
        var container = $(this).closest('.clone-container-inquiry-item-toclone');
        container.find('.port').empty();
        var country = $(this).val();
        var portList;
        $.each(countriesJson, function(i, item) {
            if (item.country == country) {
                portList = item.port;
                return false;
            }
        });

        container.find('.port').select2({
            allowClear: true,
            width: '100%',
            data: $.map(portList, function(item) {
                return {
                    id: item,
                    text: item

                };
            })
        }).val('').trigger('change');

    })

    $("#inquiry-item-container").on("click", ".item_remove", function(e) {
        $(this).closest('.row').remove();
    })

    $('#clone-container-inquiry-item').cloneya({
        minimum: 1,
        maximum: 999,
        cloneThis: '.clone-container-inquiry-item-toclone',
        valueClone: false,
        dataClone: false,
        deepClone: false,
        cloneButton: 'div.secnd-row>.clone-btn>.clone',
        deleteButton: 'div.secnd-row>.clone-btn>.delete',
        clonePosition: 'after',
        serializeID: true,
        serializeIndex: true,
        preserveChildCount: true

    }).on('before_clone.cloneya', function(event, toclone) {
        $(toclone).find('.select2-select').select2('destroy');
    }).on('after_append.cloneya', function(event, toclone, newclone) {
        $(toclone).find('select.select2-select').select2({
            allowClear: true
        });

        $(newclone).find('select.select2-select').select2({
            allowClear: true

        });
        $(newclone).find('.port.select2').empty();
        $(newclone).find('.country.select2').empty();

        $(newclone).find('.model.select2-select').empty();

    });
})
