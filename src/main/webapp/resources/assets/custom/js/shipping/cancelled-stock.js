$(function() {
    $.getJSON(myContextPath + "/data/reauction-cancel-dashboard/status-count", function(data) {
        setReauctionCancelDashboardCount(data.data)
    });
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        table.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });

    var table = $('#table-cancelled').DataTable({
       "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
       "pageLength" : 25,
       "ordering": true,
        "ajax": myContextPath + "/stock/purchase/cancelled/datasource",
        select: {
            style: 'multi',
            selector: 'td:first-child>input'
        },

        columnDefs: [{
            "targets": '_all',
            "defaultContent": ""
        }, {
            targets: 0,
            "width": '50px',
            "data": "stockNo",
            "render": function(data, type, row) {
                data = data == null ? '' : data;
                if (type === 'display') {
                    return '<a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockno="' + row.stockNo + '">' + data + '</a>';
                }
                return data;
            }

        }, {
            targets: 1,
            "width": '100px',
            "data": "chassisNo"

        }, {
            targets: 2,
            "width": '200px',
            "data": "purchaseDate"

        }, {
            targets: 3,
            "width": '200px',
            "data": "supplierName"

        }, {
            targets: 4,
            orderable: false,
            "width": '200px',
            "data": "auctionHouseName"

        },
        {
            targets: 5,
            orderable: false,
            "name": "Action",
            "render": function(data, type, row) {
                var html = ''
            if (type === 'display') {
            	button = '<button type= "button" class="ml-5 btn btn-primary btn-xs" data-toggle="modal" id="cancel-re-auction" title="move to inventory" data-backdrop="static" data-keyboard="false">Move to Inventory</button>'
                }

                return button;
            }
        }, {
            targets: 6,
            visible: false,
            "name": "Id",
            "data": "id"

        }],
        "drawCallback": function(settings, json) {
            $('input.autonumber,span.autonumber').autoNumeric('init');
           
        }

    })

        
        table.on('click', '#cancel-re-auction', function(event) {
        if (!confirm($.i18n.prop('common.confirm.save'))) {
            return;
        } 
      
       
        var rowData = table.row($(this).closest('tr')).data();
        var closestTr = $(this).closest('tr')
        var row = table.row(closestTr)
        let stockNo =  rowData.stockNo;
        moveToInventory(stockNo);

    })
    
     var moveToInventory = function(stockNo) {
        let result;
        $('#spinner').show();
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "PUT",
            async: true,
//            data: JSON.stringify(params),
            url: myContextPath + "/stock/cancelled/inventory-details?stockNo="+stockNo,
            contentType: "application/json",
            success: function(data) {
            	if (data.status == 'success') {
                    $('#alert-block').css('display', 'block').html('<strong>Success!</strong> Moved To Inventory.');
                    $("#alert-block").fadeTo(5000, 500).slideUp(500, function() {
                        $("#alert-block").slideUp(500);
                    });

                   
                }
            	 table.ajax.reload();
                 setReauctionCancelDashboardCount(data);
            },
            error: function(e) {
                console.log("ERROR: ", e);
            }
        });
    }
    
    
 
})
