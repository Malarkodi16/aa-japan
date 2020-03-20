$(function() {
    var tableEle = $('#manufacture-table')
    var table = tableEle.DataTable({
    	"dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength" : 25,
        "ordering": true,
        "ajax": myContextPath + "/documents/year-of-manufacture/list",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },

        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            className: 'select-checkbox',
            "data": "id",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<input class="selectBox" type="checkbox" data-stockno="' + row.stockNo + '" value="' + data + '">';
                }
                return data;
            }
        }, {
            targets: 1,
            "data": "maker"
        }, {
            targets: 2,
            "data": "model",
        }, {
            targets: 3,
            "data": "modelNo",
        }, {
            targets: 4,
            "data": "frame",
        }, {
            targets: 5,
            "data": "formatedSerialNoFrom",
        }, {
            targets: 6,
            "data": "formatedSerialNoTo",
        }, {
            targets: 7,
            "data": "manufactureYear",
        }, {
            targets: 8,
            "name": "Action",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                var html = '';
                if (type === 'display') {
                    html += '<a href="' + myContextPath + '/documents/year-of-manufacture/edit/' + row.code + '" class="ml-5 btn btn-info btn-xs" id="edit-yearOfManufacture"><i class="fa fa-fw fa-edit"></i></a>'
                    html += '<a href="#" class="ml-5 btn btn-warning btn-xs deleteButton"><i class="fa fa-fw fa-trash-o"></i></a>'
                    return '<div class="form-inline" style="width:100px;">' + html + '</div>';
                }
                return data;
            }
        }]
    })
    
    //Global Search
    $('#manufacture-table-filter-search').keyup(function() {
        table.search($(this).val()).draw();
    });
    //Table Length Filter
    $('#manufacture-table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });


    //create Year of manufacture
    $('#create-manufacture-year').on('')

    $('#manufacture-table').on('click', '.deleteButton ', function() {
        if (!confirm('Do you want to delete?')) {
            return false;
        }
        var data = table.row($(this).closest('tr')).data();
        var id = data.id;
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "get",
            data: JSON.stringify(id),
            url: myContextPath + "/documents/year-of-manufacture/delete/" + id,
            contentType: "application/json",
            success: function(status) {
                table.ajax.reload();
            },
            error: function(status) {
                var alertEle = $('#alert-block').addClass('alert-danger');
                $(alertEle).removeClass('alert-success')
                $(alertEle).css('display', '').html('<strong>Warning!</strong>Year Of Manu is not deleted').fadeIn().delay(3000).fadeOut();
            }
        })
    })
})
