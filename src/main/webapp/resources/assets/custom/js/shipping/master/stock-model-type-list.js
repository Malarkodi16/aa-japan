$(function() {
    // Datatable Filter

    $('#table-filter-search').keyup(function() {
        table_stock_model_list.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
        table_stock_model_list.page.len($(this).val()).draw();
    });

    /* Port List Data-table */
    var table_stock_model_list_Ele = $('#table-master-stock-type-list');
    var table_stock_model_list = table_stock_model_list_Ele.DataTable({
        "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
        "pageLength": 25,
        "ajax": myContextPath + "/master/stockType/list-data",
        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "name": "Code",
            "data": "code",
        }, {
            targets: 1,
            "data": "modelType",
        }, {
            targets: 2,
            "data": "maker",
        },{
            targets: 3,
            "data": "model",
        },{
            targets: 4,
            "data": "category",
        },{
            targets: 5,
            "data": "subcategory",
        },{
            targets: 6,
            "data": "transmission",
        },{
            targets: 7,
            "data": "fuel",
        },{
            targets: 8,
            "data": "driven",
        },{
            targets: 9,
            "data": "cc",
        },{
            targets: 10,
            "data": "unit",
        },{
            targets: 11,
            "data": "equipment",
        },{
            targets: 12,
            orderable: false,
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                var html = '';
                if (type === 'display') {
                    html += '<a href="' + myContextPath + '/master/stockType/edit/' + row.code + '" class="ml-5 btn btn-info btn-xs"><i class="fa fa-fw fa-edit"></i></a>'
                    html += '<a href="#" class="ml-5 btn btn-warning btn-xs deleteButton"><i class="fa fa-fw fa-trash-o"></i></a>'
                    return '<div class="form-inline" style="width:100px;">' + html + '</div>';
                }
                return data;
            }
        }]
    });


   $('#table-master-stock-type-list').on('click', '.deleteButton', function(event) {
       if (!confirm('Do you want to delete?')) {
           return false;
       }
       let tr = $(this).closest('tr');
       var data = table_stock_model_list.row(tr).data();
       var code = data.code;
       $.ajax({
           beforeSend: function() {
               $('#spinner').show()
           },
           complete: function() {
               $('#spinner').hide();
           },
           type: "get",
           url: myContextPath + "/master/stockType/delete/" + code,
           contentType: "application/json",
           success: function(data) {
               table_stock_model_list.ajax.reload();
           },
           error: function(status) {
               var alertEle = $('#alert-block').addClass('alert-danger');
               $(alertEle).removeClass('alert-success')
               $(alertEle).css('display', '').html('<strong>Warning!</strong> Forwarder is not deleted').fadeIn().delay(3000).fadeOut();
           }
       })
   })
})
