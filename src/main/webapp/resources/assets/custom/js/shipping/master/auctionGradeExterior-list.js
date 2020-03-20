$(function() {
    // Datatable Filter

    $('#table-filter-search').keyup(function() {
        table_gradeExterior_list.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
        table_gradeExterior_list.page.len($(this).val()).draw();
    });

    /* Port List Data-table */
    var table_gradeExterior_list_Ele = $('#table-master-exteriorGrade-list');
    var table_gradeExterior_list = table_gradeExterior_list_Ele.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/master/auctionGradeExterior/list-data",
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "name": "Grade",
            "data": "grade",
        }, {
            targets: 1,
            orderable: false,
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                var html = '';	
                html += '<a class="ml-5 btn btn-info btn-xs" title="Edit Grade" id="edit-grade" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#modal-add-gradeExterior" name="editExteriorGrade" value="edit"><i class="fa fa-fw fa-edit"></i></a>';
                html += '<a href="#" class="ml-5 btn btn-warning btn-xs deleteButton"><i class="fa fa-fw fa-trash-o"></i></a>'
                return '<div class="form-inline" style="width:100px;">' + html + '</div>';
            }
        }]
    });

    let modal_add_gradeExterior = $('#modal-add-gradeExterior');
    let row, modalTriggerEle;
    modal_add_gradeExterior.on('show.bs.modal', function(event) {
        if (event.namespace != 'bs.modal') {
            return;
        }
        modalTriggerEle = $(event.relatedTarget);
        row = table_gradeExterior_list.row($(modalTriggerEle).closest('tr'))
        var rowData = row.data();

        $('input[name="grade"]').val(!isEmpty(rowData) ? rowData.grade : "");
        $('input[name="id"]').val(!isEmpty(rowData) ? rowData.id : "");
    }).on('hidden.bs.modal', function() {
        $(this).find('input').val('');
        $(this).find('.help-block').text('')
    }).on('click', '#save-exteriorGrade', function() {
        if (!$('#gradeExterior-form').find('input[name="grade"]').valid()) {
            return false;
        }
        var data = getFormData($("#gradeExterior-form").find('input'));
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: 'post',
            data: JSON.stringify(data),
            url: myContextPath + "/master/auctionGradeExterior/save",
            contentType: "application/json",

            success: function(data) {
                if (data.status === 'success') {
                    if (!isEmpty(row.data())) {
                        row.data(data.data).invalidate();
                    } else {
                        table_gradeExterior_list.ajax.reload();
                    }
                    $('#modal-add-gradeExterior').modal('toggle');
                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Grade Saved.');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });
                }
            }
        });
    })

    $('#table-master-exteriorGrade-list').on('click', '.deleteButton', function(event) {
        if (!confirm('Do you want to delete?')) {
            return false;
        }
        let tr = $(this).closest('tr');
        var data = table_gradeExterior_list.row(tr).data();
        var id = data.id;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            url: myContextPath + "/master/auctionGradeExterior/delete/" + id,
            contentType: "application/json",
            success: function(data) {
                table_gradeExterior_list.ajax.reload();
            },
            error: function(status) {
                var alertEle = $('#alert-block').addClass('alert-danger');
                $(alertEle).removeClass('alert-success')
                $(alertEle).css('display', '').html('<strong>Warning!</strong> grade is not deleted').fadeIn().delay(3000).fadeOut();
            }
        })
    })
})
