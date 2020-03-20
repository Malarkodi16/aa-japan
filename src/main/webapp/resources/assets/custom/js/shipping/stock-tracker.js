$(function() {
    $('#reportCreateForm').validate({
        highlight: function(element) {
            $(element).parent().addClass("has-error");
        },
        unhighlight: function(element) {
            $(element).parent().removeClass("has-error");
        },
        onfocusout: function(element) {
            $(element).valid();
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

            'name': 'required',
            'fields': 'required',
            'period': 'required',
            'from': 'required',
            'to': 'required'

        }
    });
});
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
    // Date picker
    $('input.datepicker').datepicker({
        format: "dd-mm-yyyy",
        autoclose: true
    }).on('change', function() {
        $(this).valid();

    })
    $('.select2').select2({
        placeholder: function() {
            return $(this).attr('data-placeholder')
        },
        allowClear: true,
    });

    $.getJSON(myContextPath + "/data/customfields.json", function(data) {

        fieldJson = data;
        $('#customFields').select2({
            placeholder: "Select Fields",
            allowClear: true,
            width: '100%',
            data: $.map(fieldJson, function(item) {
                return {
                    id: item.fieldId,
                    text: item.displayName
                };
            })
        });
    })
    $('#customFields').on("select2:select", function(evt) {
        var element = evt.params.data.element;
        var $element = $(element);

        $element.detach();
        $(this).append($element);
        $(this).trigger("change");
    });
    $.getJSON(myContextPath + "/report/list", function(data) {
        customListJson = data;
        $('#customLists').select2({
            placeholder: "Select Report",
            allowClear: true,
            width: '100%',
            data: $.map(customListJson, function(item) {
                return {
                    id: item.code,
                    text: item.name
                };
            })
        });
    })
    $('#customLists').on('change', function() {
        let val = $(this).val();
        if (!isEmpty(val)) {
            $('button#btn-create-report').html('Update Report');
        } else {
            $('button#btn-create-report').html('Create Report');
        }
    })
    $('#modal-create-report').on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        let code = $('select[name="customLists"]').val();
        if (!isEmpty(code)) {
            $(this).find('input[name="code"]').val(code);
            $.ajax({
                beforeSend: function() {
                    $('#spinner').show()
                },
                complete: function() {
                    $('#spinner').hide();
                },
                url: myContextPath + "/report/find?code=" + code,
                type: 'GET',
                success: function(data) {

                    $('#modal-create-report').find('select[name="fields"]').val(data.fields).trigger('change');
                    $('#modal-create-report').find('input[name="name"]').val(data.name);
                    $('#modal-create-report').find('select[name="period"]').val(data.period).trigger('change');
                    $('#modal-create-report').find('input[name="from"]').val(data.from);
                    $('#modal-create-report').find('input[name="to"]').val(data.to);
                },

            });
        }

    }).on('hidden.bs.modal', function() {
        $(this).find('input,select').val([]).trigger('change');
    }).on('click', 'button#btn-create-report', function(e) {
        if (!$('form#reportCreateForm').find('input,select').valid()) {
            return false;
        }
        let data = {};
        data.name = $('#modal-create-report').find('input[name="name"]').val();
        data.fields = $('#modal-create-report').find('select[name="fields"]').val();
        data.period = $('#modal-create-report').find('select[name="period"]').val();
        data.from = $('#modal-create-report').find('input[name="from"]').val();
        data.to = $('#modal-create-report').find('input[name="to"]').val();

        let code = $('#modal-create-report').find('input[name="code"]').val();
        if (!isEmpty(code)) {
            data.code = code;
        }
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            async: true,
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/report/save",
            contentType: "application/json",
            success: function(data) {
                $('#customLists').empty();
                $.getJSON(myContextPath + "/report/list", function(data) {
                    $('#customLists').select2({
                        placeholder: "Select Report",
                        allowClear: true,
                        width: '100%',
                        data: $.map(data, function(item) {
                            return {
                                id: item.code,
                                text: item.name
                            };
                        })
                    }).val('').trigger('change');
                })

                $('#modal-create-report').modal('toggle');

                let alertBlockEle = $('#alert-block');
                setTimeout(function() {
                    $(alertBlockEle).removeClass('hidden');
                    $(alertBlockEle).find('span').html('Report Saved Successfully');
                    ;
                }, 1000);

            }
        });
    }).on('change','select[name="period"]',function(){
        let val=$(this).val();
        if(val=='period'){
            $('div.custom-date').show();
        }else{
            $('div.custom-date').hide();
            $('div.custom-date').find('input').val('');
        }

    })
    let tableReportEle = $('table#table-report');
    let tableReport;
    $('#btn-search').on('click', function() {
        var cusFld = $('#customLists').val();
        if (isEmpty(cusFld)) {
            return false;
        }
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            url: myContextPath + "/report/dynamicReport/data-source",
            data: {
                customLists: cusFld
            },
            contentType: "text",
            error: function() {
                $('#info').html('<p>An error has occurred</p>');
            },
            type: 'GET',
            dataType: 'json',
            success: function(data) {
                let columnMetaData = data.columnMetaData;
                let tableData = data.data;
                var my_columns = [];
                for (let i = 0; i < columnMetaData.length; i++) {
                    var my_item = {};
                    my_item.data = columnMetaData[i].projectAs;
                    my_item.title = columnMetaData[i].displayName;
                    my_columns.push(my_item);
                }
                if (typeof tableReport != 'undefined') {
                    tableReport.clear().destroy();
                    tableReportEle.html('');
                }
                initDataTable(my_columns, tableData.data);
            },

        });

    });

    $("#excel_export_all").on("click", function() {
        tableReport.button("#dt_excel_export_all").trigger();
    });

    function initDataTable(columnsInfo, data) {

        tableReport = tableReportEle.DataTable({
            "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
            "pageLength": 25,
            "destroy": true,
            "language": {
                "zeroRecords": "Loding.."
            },
            "data": data,
            columnDefs: [{
                "targets": '_all',
                "defaultContent": ""
            }],
            columns: columnsInfo,
            buttons: [{
                extend: 'excel',
                text: 'Export All',
                title: '',
                filename: function() {
                    var d = new Date();
                    return 'Report_' + d.getDate() + '-' + (d.getMonth() + 1) + '-' + d.getFullYear();
                },
                attr: {
                    type: "button",
                    id: 'dt_excel_export_all'
                }//                 ,
                //                 exportOptions: {
                //                     columns: [0]
                //                 }
            }]
        });
    }
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        tableReport.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
        tableReport.page.len($(this).val()).draw();
    });
});
