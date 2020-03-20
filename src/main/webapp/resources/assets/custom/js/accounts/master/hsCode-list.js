$(function() {
    //Dropdown
    var categoryJSON, categoryNameFilter
    $.getJSON(myContextPath + "/data/categories.json", function(data) {
        categoryJSON = data;
        $('#table-filter-category').select2({
            allowClear: true,
            width: '100%',
            data: $.map(categoryJSON, function(item) {
                return {
                    id: item.name,
                    text: item.name,
                    data: item
                };
            })
        }).on('change', function() {
            categoryNameFilter = $(this).val();
            table_hsCode_list.draw();
        })

        //         $('#categoryDrop').select2({
        //             allowClear: true,
        //             width: '100%',
        //             data: $.map(categoryJSON, function(item) {
        //                 return {
        //                     id: item.name,
        //                     text: item.name,
        //                     data: item
        //                 };
        //             })
        //         }).val('').trigger('change');

        var categoryFilter = data;
        $('#categoryDrop').select2({
            matcher: function(params, data) {
                return matchStart(params, data);
            },
            allowClear: true,
            width: '100%',
            data: $.map(categoryFilter, function(item) {
                return {
                    id: item.name,
                    text: item.name,
                    data: item
                };
            })
        })
        $('#categoryDrop').val($('#categoryDrop').attr('data-value')).trigger("change");
    })

    var subCategoryElement = $('#subCategoryDrop');
    subCategoryElement.select2({
        matcher: function(params, data) {
            return matchStart(params, data);
        },
        allowClear: true,
        width: '100%',
    })
    $('#categoryDrop').on('change', function() {
        subCategoryElement.empty();
        var data = $(this).find('option:selected').data('data');
        if (!isEmpty(data && data.data)) {
            $('#subCategoryDrop').select2({
                matcher: function(params, data) {
                    return matchStart(params, data);
                },
                allowClear: true,
                width: '100%',
                data: $.map(data.data.subCategories, function(item) {
                    return {
                        id: item.name,
                        text: item.name

                    };
                })
            })
            $('#subCategoryDrop').val($('#subCategoryDrop').attr('data-value')).trigger("change");
        }

    })

    var table_hsCode_list_Ele = $('#table-master-hsCode-list');
    var table_hsCode_list = table_hsCode_list_Ele.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/master/hsCode/list-data",
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "name": "cc",
            "data": "cc",
            "render": function(data, type, row) {
                return '<span class="autonumber" data-m-dec="0">' + ifNotValid(data, '') + '</span>';
            }
        }, {
            targets: 1,
            
            "data": "category",

        }, {
            targets: 2,
            
            "data": "subCategory",

        }, {
            targets: 3,
            "name": "hsCode",
            "data": "hsCode",

        }, {
            targets: 4,
            orderable: false,
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                var html = '';
                html += '<a class="ml-5 btn btn-info btn-xs" title="Edit hsCode" id="edit-hsCode" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-add-hsCode" name="edithsCode" value="edit"><i class="fa fa-fw fa-edit"></i></a>';
                html += '<a href="#" class="ml-5 btn btn-warning btn-xs deleteButton"><i class="fa fa-fw fa-trash-o"></i></a>'
                return '<div class="form-inline" style="width:100px;">' + html + '</div>';
            }
        
//         }, {
//             targets: 5,
//             "name": "category",
//             "data": "category",
//             "visible":false
        }],
        "drawCallback": function(settings, json) {
            $('span.autonumber').autoNumeric('init');

        }
    });

    $('#table-filter-search').keyup(function() {
        var query = regex_escape($(this).val());
        table_hsCode_list.search(query, true, false).draw();
    });
    $('#table-filter-length').change(function() {
        table_hsCode_list.page.len($(this).val()).draw();
    });

    $.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
        if (typeof categoryNameFilter != 'undefined' && categoryNameFilter.length != '') {
            if (aData[1].length == 0 || aData[1] != categoryNameFilter) {
                return false;
            }
        }
        return true;
    });

    let modal_add_hsCode = $('#modal-add-hsCode');
    let row, modalTriggerEle;
    modal_add_hsCode.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modal_add_hsCode.find('input.autonumber').autoNumeric('init')
        modalTriggerEle = $(event.relatedTarget);
        row = table_hsCode_list.row($(modalTriggerEle).closest('tr'))
        var rowData = row.data();

        $('input[name="code"]').val(!isEmpty(rowData) ? rowData.code : "");
        $('input[name="cc"]').val(!isEmpty(rowData) ? rowData.cc : "");
        $('#categoryDrop').val(!isEmpty(rowData) ? rowData.categoryName : "").trigger('change');
        $('#subCategoryDrop').val(!isEmpty(rowData) ? rowData.subCategory : "").trigger('change');
        $('input[name="hsCode"]').val(!isEmpty(rowData) ? rowData.hsCode : "");
    }).on('hidden.bs.modal', function() {
        $(this).find('input').val('');
        $(this).find('.help-block').text('')
    }).on('click', '#save-hsCode', function() {
        if (!$('#hsCode-form').valid()) {
            return false;
        }
        /*if (!$("#hsCode-form").valid()) {
            return false;
        }*/
        var data = {};
        data = getFormData($("#hsCode-form").find('input[name="code"],input[name="hsCode"],select[name="category"],select[name="subCategory"]'));
        data.cc = $("#hsCode-form").find('input[name="cc"]').autoNumeric('get');
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: 'post',
            data: JSON.stringify(data),
            url: myContextPath + "/master/hsCode/save",
            contentType: "application/json",

            success: function(data) {
                if (data.status === 'success') {
                    if (!isEmpty(row.data())) {
                        row.data(data.data).invalidate();
                    }
                    table_hsCode_list.ajax.reload();
                    $('#modal-add-hsCode').modal('toggle');
                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong>HS Code Saved.');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                }
            }
        });
    })

    $("#save-excel").on("click", function() {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return false;
        }

        var fileData = new FormData();
        var excelUploadedEle = $('input[name="excelUploaded"]');
        if (excelUploadedEle.prop('files').length > 0) {
            let file = excelUploadedEle.prop('files')[0];
            fileData.append("excelFile", file);
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
            url: myContextPath + "/master/hsCode/upload/xls",
            contentType: false,
            async: false,
            success: function(data) {
                if (data.status === "success") {
                    $('#alert-block').removeClass('alert-warning').addClass('alert-success').css('display', 'block').html('<strong>Success! </strong>');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                    table_hsCode_list.ajax.reload();
                } else {
                    $('#alert-block').removeClass('alert-success').addClass('alert-warning').css('display', 'block').html('<strong>' + data.status + '</strong>');
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

    $('#table-master-hsCode-list').on('click', '.deleteButton', function(event) {
        if (!confirm('Do you want to delete?')) {
            return false;
        }
        let tr = $(this).closest('tr');
        var data = table_hsCode_list.row(tr).data();
        var id = data.code;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            url: myContextPath + "/master/hsCode/delete/" + id,
            contentType: "application/json",
            success: function(data) {
                table_hsCode_list.ajax.reload();
            },
            error: function(status) {
                var alertEle = $('#alert-block').addClass('alert-danger');
                $(alertEle).removeClass('alert-success')
                $(alertEle).css('display', '').html('<strong>Warning!</strong> HS Code is not deleted').fadeIn().delay(3000).fadeOut();
            }
        })
    })
})
