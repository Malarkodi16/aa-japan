var makersJson, categoryJSON, transportCategoryJson;
$(function() {

    // Customize Datatable filer

    $('#table-filter-search').keyup(function() {
        table_maker_model_list.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
        table_maker_model_list.page.len($(this).val()).draw();
    });

    /* Maker Model List Data-table*/

    //table Maker models
    var table_maker_model_list_Ele = $('#table-master-maker-model-list');
    var table_maker_model_list = table_maker_model_list_Ele.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/master/list-data",
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            orderable: false,
            className: 'details-control',
            "data": "vessel",
            "render": function(data, type, row) {
                return '<div class="container-fluid" ><div class="row"><div class="col-md-12" style="width:600px"><h5 class="font-bold"><i class="fa fa-plus-square-o pull-left" name="icon"></i> ' + row.name + ' </h5></div></div></div>';
            }
        }, {
            targets: 1,
            "className": "details-control",
            "data": "maker",
            "visible": false
        }, {
            targets: 2,
            "data": "id",
            orderable: false,
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                var html = '';
                if (type === 'display') {
                    html += '<a type="button" class="btn btn-primary ml-5 btn-xs" title="Add Model" id="add-model" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-add-model"><i class="fa fa-plus"></i></a>';
                    html += '<a href="#" class="ml-5 btn btn-warning btn-xs deleteButton"><i class="fa fa-fw fa-trash-o"></i></a>'
                    //                     html += ' <button class="btn btn-primary btn-xs" type="button" id="excel_export_row"> <i class="fa fa-file-excel-o" aria-hidden="true"> </i> </button>'
                    return '<div class="form-inline" style="width:100px;">' + html + '</div>';
                }
                return data;
            }
        }],

        "fnDrawCallback": function(oSettings) {
            $(oSettings.nTHead).hide();
        }

    });

    $.getJSON(myContextPath + "/master/export-excel", function(data) {
        makersJson = data;
    })
    $("#excel_export_all").on("click", function() {
        JSONToCSVConvertor(makersJson, "Maker Model", true);
    });
    $('#table-master-maker-model-list').on('click', '#excel_export_row', function() {
        var rowData = table_maker_model_list.row($(this).closest('tr')).data();

        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            async: false,
            url: myContextPath + "/master/export-excel-row?code=" + rowData.code,
            contentType: "application/json",
            success: function(data) {
                JSONToCSVConvertor(data, "Maker Model", true);
            },
            error: function(status) {
                var alertEle = $('#alert-block').addClass('alert-danger');
                $(alertEle).removeClass('alert-success')
                $(alertEle).css('display', '').html('<strong>Warning!</strong> Vehicle is not exported to excel').fadeIn().delay(3000).fadeOut();
            }
        })
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
            url: myContextPath + "/master/makermodel/xls",
            contentType: false,
            async: false,
            success: function(data) {
                if (data.status === "success") {
                    $('#alert-block').css('display', 'block').html('<strong>Success! </strong>');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
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

    //expand details
    table_maker_model_list.on('click', 'td.details-control', function() {
        var tr = $(this).closest('tr');
        var row = table_maker_model_list.row(tr);
        if (row.child.isShown()) {
            row.child.hide();
            tr.removeClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
        } else {
            table_maker_model_list.rows('.shown').every(function(rowIdx, tableLoop, rowLoop) {
                var row = table_maker_model_list.row(rowIdx);
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                }

            })
            row.child(format(row.data())).show();
            tr.addClass('shown');
            tr.find('i[name="icon"]').removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
        }
    });

    $('#table-master-maker-model-list').on('click', '.deleteButton ', function() {
        if (!confirm('Do you want to delete?')) {
            return false;
        }
        var data = table_maker_model_list.row($(this).closest('tr')).data();
        var code = data.code;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            data: JSON.stringify(code),
            url: myContextPath + "/master/delete/" + code,
            contentType: "application/json",
            success: function(status) {
                table_maker_model_list.ajax.reload();
            },
            error: function(status) {
                var alertEle = $('#alert-block').addClass('alert-danger');
                $(alertEle).removeClass('alert-success')
                $(alertEle).css('display', '').html('<strong>Warning!</strong> Maker is not deleted').fadeIn().delay(3000).fadeOut();
            }
        })
    })

    $('#table-master-maker-model-list').on('click', '.delete-model ', function(event) {
        if (!confirm('Do you want to delete?')) {
            return false;
        }
        var deleteTriggerEle = $(event.currentTarget);
        var editCode = $(deleteTriggerEle).closest('tr').find('td>input[name="code"]').val();
        var modelList = JSON.parse($(deleteTriggerEle).closest('tr').find('td>input[name="modelList"]').val());
        var queryString = "?code=" + editCode + "&modelName=" + modelList.modelName
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            url: myContextPath + "/master/model/delete" + queryString,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    var tr = $(deleteTriggerEle).closest('table').closest('tr').prev();
                    row = table_maker_model_list.row(tr)
                    row.child.hide();
                    row.data(data.data).invalidate();
                }
            },
            error: function(status) {
                var alertEle = $('#alert-block').addClass('alert-danger');
                $(alertEle).removeClass('alert-success')
                $(alertEle).css('display', '').html('<strong>Warning!</strong> Model is not deleted').fadeIn().delay(3000).fadeOut();
            }
        })
    })

    $('.select2-select').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true
    })
    // get category json
    $.getJSON(myContextPath + "/data/categories.json", function(data) {
        categoryJSON = data
        $('select[name="category"]').select2({
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
    })
    $.getJSON(myContextPath + "/data/transport-category.json", function(data) {
        transportCategoryJson = data;
        $('select[name="transportCategory"]').select2({
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
    // model ele add and update
    var modelEle = $('#modal-add-model');
    var row, modalTriggerEle;
    modelEle.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalTriggerEle = $(event.relatedTarget);
        var tr = $(modalTriggerEle).closest('tr');
        row = table_maker_model_list.row(tr)
        var data = row.data();
        $('input[name="code"]').val(data.code);
        $('.select2-select').select2({
            placeholder: function() {
                return $(this).attr('data-placeholder')
            },
            allowClear: true
        })
        //AutoNumeric
        $('.autonumber').autoNumeric('init');
    }).on('hidden.bs.modal', function() {
        $(this).find('select,input').val('').trigger('change');
        $(this).find('span.help-block').html('');
        $(this).find('.has-error').removeClass('has-error');
    }).on('click', '#save-model', function() {
        if (!$('#modal-add-model').find('.data-required').valid()) {
            return;
        }
        var model = getFormData($(modelEle).find('input,select'));

        //         var modelSubModel = [];
        //         var subM = $('select[name="subModelName"]').val();
        //         for (var i = 0; i < subM.length; i++) {
        //             var subModelData = {};
        //             subModelData.subModelName = subM[i];
        //             modelSubModel.push(subModelData);
        //         }
        //         model.subModel = modelSubModel
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(model),
            url: myContextPath + "/master/save-model",
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    $(modelEle).modal('toggle');
                    row.child.hide();
                    row.data(data.data).invalidate();

                }
            }
        });
    })

    var modelEditEle = $('#modal-edit-model');
    var editRow, modalEditTriggerEle, modelList;
    modelEditEle.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalEditTriggerEle = $(event.relatedTarget);
        var tr = $(modalEditTriggerEle).closest('table').closest('tr').prev();
        editRow = table_maker_model_list.row(tr);

        modelList = JSON.parse($(modalEditTriggerEle).closest('tr').find('td>input[name="modelList"]').val());
        // data to check duplicate sub category
        $(modelEditEle).find('input[name="makerName"]').val((editRow.data()).name)
        $(modelEditEle).find('input[name="modelId"]').val(modelList.modelId)
        $(modelEditEle).find('input[name="modelName"]').val(modelList.modelName).prop('readonly', true);
        $(modelEditEle).find('input[name="m3"]').val(modelList.m3);//.prop('readonly', true);
        $(modelEditEle).find('input[name="length"]').val(modelList.length)
        $(modelEditEle).find('input[name="width"]').val(modelList.width)
        $(modelEditEle).find('input[name="height"]').val(modelList.height)
        $(modelEditEle).find('input[name="category"]').val(modelList.category)
        $(modelEditEle).find('select[name="transportCategory"]').val(modelList.transportCategory).trigger('change')
        $(modelEditEle).find('select[name="category"]').val(ifNotValid(modelList.category, '')).trigger('change');//.prop('disabled', true);
        $(modelEditEle).find('select[name="subcategory"]').val(ifNotValid(modelList.subcategory, '')).trigger('change');
    }).on('hidden.bs.modal', function() {
        resetElementInput(this);
        $(this).find('span.help-block').html('');
        $(this).find('.has-error').removeClass('has-error');
    }).on('click', '#edit-model', function() {

        if (!$('#modal-edit-model-form').find('select').valid()) {
            return false;
        }
        var model = getFormData($(modelEditEle).find('input,select'));
        model.modelId = modelList.modelId;
        rowData = editRow.data();
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "put",
            async: true,
            data: JSON.stringify(model),
            url: myContextPath + "/master/edit-model?makerName=" + rowData.name,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    $(modelEditEle).modal('toggle');
                    editRow.child.hide();
                    editRow.data(data.data).invalidate();

                }
            }
        });

    })
});

// Maker/Model list expand details format
function format(rowData) {
    var element = $('#maker-model-list-details-view>.maker-model-detail-view').clone();
    $(element).find('input[name="makerModelCode"]').val(rowData.code)
    var rowClone = $(element).find('table>tbody').find('tr.clone-row');
    if (rowData.models.length == 0) {
        $(element).addClass('hide');
    } else {

        for (var i = 0; i < rowData.models.length; i++) {
            var row = $(rowClone).clone();
            $(row).find('td.s-no>span').html(i + 1);
            $(row).find('td.s-no>input[name="code"]').val(rowData.code);
            $(row).find('td.s-no>input[name="modelList"]').val(JSON.stringify(rowData.models[i]));
            $(row).find('td.modelName').html(ifNotValid(rowData.models[i].modelName, ''))
            $(row).find('td.category').html(ifNotValid(rowData.models[i].category, ''));
            $(row).find('td.transportCategory').html(ifNotValid(rowData.models[i].transportCategory, ''));
            // $(row).find('td.subcategory').html(ifNotValid(rowData.models[i].subcategory, ''));
            $(row).find('td.m3').html(ifNotValid(rowData.models[i].m3, ''));
            $(row).find('td.length').html(ifNotValid(rowData.models[i].length, ''));
            $(row).find('td.width').html(ifNotValid(rowData.models[i].width, ''));
            $(row).find('td.height').html(ifNotValid(rowData.models[i].height, ''));
            $(row).removeClass('hide');
            $(element).find('table>tbody').append(row);

        }
    }

    return element;
}

function JSONToCSVConvertor(JSONData, ReportTitle, ShowLabel) {
    //If JSONData is not an object then JSON.parse will parse the JSON string in an Object
    var arrData1 = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;
    var arrData = arrData1.data

    var CSV = '';
    //Set Report title in first row or line

    CSV += ReportTitle + '\r\n\n';

    //This condition will generate the Label/Header
    if (ShowLabel) {
        var row = "";

        //This loop will extract the label from 1st index of on array
        for (var index in arrData[0]) {

            // Making first letter to capital
            var title = index.charAt(0).toUpperCase() + index.slice(1)
            //Now convert each value to string and comma-seprated
            row += title + ',';
        }

        row = row.slice(0, -1);

        //append Label row with line break
        CSV += row + '\r\n';
    }

    //1st loop is to extract each row
    for (var i = 0; i < arrData.length; i++) {
        var row = "";

        //2nd loop will extract each column and convert it in string comma-seprated
        for (var index in arrData[i]) {
            row += '"' + arrData[i][index] + '",';
        }

        row.slice(0, row.length - 1);

        //add a line break after each row
        CSV += row + '\r\n';
    }

    if (CSV == '') {
        alert("Invalid data");
        return;
    }

    //Generate a file name
    var fileName = "MyReport_";
    //this will remove the blank-spaces from the title and replace it with an underscore
    fileName += ReportTitle.replace(/ /g, "_");
    fileName += "_" + new Date();
    //Initialize file format you want csv or xls
    var uri = 'data:text/csv;charset=utf-8,' + escape(CSV);
    // Now the little tricky part.
    // you can use either>> window.open(uri);
    // but this will not work in some browsers
    // or you will not get the correct file extension    

    //this trick will generate a temp <a /> tag
    var link = document.createElement("a");
    link.href = uri;

    //set the visibility hidden so it will not effect on your web-layout
    link.style = "visibility:hidden";
    link.download = fileName + ".csv";

    //this part will append the anchor tag and remove it after automatic click
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

}
