$(function() {
    // Datatable Filter

    $('#table-filter-search').keyup(function() {
        table_generalSupplier_list.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
        table_generalSupplier_list.page.len($(this).val()).draw();
    });

    /* Port List Data-table */
    var table_generalSupplier_list_Ele = $('#table-master-generalSupplier-list');
    var table_generalSupplier_list = table_generalSupplier_list_Ele.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/accounts/master/generalSupplier/list-data",
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "name": "Code",
            "data": "code",
        }, {
            targets: 1,
            "data": "name",
        }, {
            targets: 2,
            orderable: false,
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                var html = '';
                if (type === 'display') {
                    html += '<a class="ml-5 btn btn-info btn-xs" title="Edit generalSupplier" id="edit-generalSupplier" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-add-generalSupplier" name="editgeneralSupplier" value="edit"><i class="fa fa-fw fa-edit"></i></a>';
                    html += '<a href="#" class="ml-5 btn btn-warning btn-xs deleteButton"><i class="fa fa-fw fa-trash-o"></i></a>'
                    return '<div class="form-inline" style="width:100px;">' + html + '</div>';
                }
                return data;
            }
        }]
    });

    let modal_add_generalSupplier = $('#modal-add-generalSupplier');
    let row, modalTriggerEle;
    modal_add_generalSupplier.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalTriggerEle = $(event.relatedTarget);
        row = table_generalSupplier_list.row($(modalTriggerEle).closest('tr'))
        var rowData = row.data();
        $('input[name="code"]').val(!isEmpty(rowData) ? rowData.code : "");
        $('input[name="name"]').val(!isEmpty(rowData) ? rowData.name : "");
    }).on('hidden.bs.modal', function() {
        $(this).find('input').val('');
        $(this).find('.help-block').text('')
    }).on('click', '#save-generalSupplier', function() {
        let isValid=$('#generalSupplier-form').find('input[name="name"]').valid()
        if (!isValid) {
            return false;
        }
        var data = getFormData($("#generalSupplier-form").find('input'));
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: 'post',
            data: JSON.stringify(data),
            url: myContextPath + "/accounts/master/generalSupplier/create",
            contentType: "application/json",

            success: function(data) {
                if (data.status === 'success') {
                    if (!isEmpty(row.data())) {
                        row.data(data.data).invalidate();
                    } else {
                        table_generalSupplier_list.ajax.reload();
                    }
                    $('#modal-add-generalSupplier').modal('toggle');
                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Supplier Saved.');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                }
            }
        });
    })

    $('#table-master-generalSupplier-list').on('click', '.deleteButton', function(event) {
        if (!confirm('Do you want to delete?')) {
            return false;
        }
        let tr = $(this).closest('tr');
        var data = table_generalSupplier_list.row(tr).data();
        var code = data.code;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            url: myContextPath + "/accounts/master/generalSupplier/delete/" + code,
            contentType: "application/json",
            success: function(data) {
                table_generalSupplier_list.ajax.reload();
            },
            error: function(status) {
                var alertEle = $('#alert-block').addClass('alert-danger');
                $(alertEle).removeClass('alert-success')
                $(alertEle).css('display', '').html('<strong>Warning!</strong> Supplier is not deleted').fadeIn().delay(3000).fadeOut();
            }
        })
    })
})
