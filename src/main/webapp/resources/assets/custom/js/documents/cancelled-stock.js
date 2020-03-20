$(function() {
//    $.getJSON(myContextPath + "/data/reauction-cancel-dashboard/status-count", function(data) {
//        setReauctionCancelDashboardCount(data.data)
//    });
    // Customize Datatable
    $('#table-filter-search').keyup(function() {
        table.search($(this).val()).draw();
    });
    $('#table-filter-length').change(function() {
        table.page.len($(this).val()).draw();
    });
    
  //Setting count in dashboard
    setDocumentDashboardStatus();
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
            //orderable: false,
            "width": '200px',
            "data": "auctionHouseName"

        }
//         , {
//             targets: 4,
//             orderable: false,
//             "width": '200px',
//             "data": "cancellationCharge",
//             "render": function(data, type, row) {
//                 data = data == null ? '' : data;
//                 return '<span class="autonumber" data-a-sign="&yen; " data-m-dec="0">' + data + '</span>';
//             }

//         }, {
//             targets: 5,
//             orderable: false,
//             "width": '200px',
//             "data": "auctionHouse",
//             "render": function(data, type, row) {
//                 data = data == null ? '' : data;
//                 if (type === 'display') {
//                     return '<a href="#" class="btn btn-primary btn-xs" data-toggle="modal" data-target="#modal-cancelled" data-backdrop="static" data-keyboard="false"><i class="fa fa-fw fa-check"></i>Update</a>';
//                 }
//                 return data;
//             }
//         }
        ],
        "drawCallback": function(settings, json) {
            $('input.autonumber,span.autonumber').autoNumeric('init');

        }

    });
    var modalCancelledBtn;
    var modalCancelledElement = $('#modal-cancelled')
    modalCancelledElement.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        modalCancelledBtn = $(e.relatedTarget);
        var data = table.row($(modalCancelledBtn).closest('tr')).data();
        $(this).find('#rowData').attr('data-json', JSON.stringify(data));
    }).on('hidden.bs.modal', function() {
        resetElementInput($(this));
    }).on('click', '#btn-save-cancellation-details', function() {
        var rowData = $(modalCancelledElement).find('#rowData').attr('data-json');
        rowData=JSON.parse(rowData);
        var id = ifNotValid(rowData.id, '');
        var cancellationCharge = $('#cancellation-charge').autoNumeric('get')
        var data = {};
        data.cancellationCharge = ifNotValid(cancellationCharge, '');
        var params = 'id=' + id
        $.ajax({
            beforeSend: function() {
                $('#spinner').show()
            },
            complete: function() {
                $('#spinner').hide();
            },
            type: "post",
            data: JSON.stringify(data),
            url: myContextPath + "/stock/purchase/cancelled/details/update?" + params,
            contentType: "application/json",
            success: function(data) {
                if (data.status === 'success') {
                    var tr = $(modalCancelledBtn).closest('tr');
                    var row = table.row(tr);
                    row.data(data.data).invalidate();
                    $(tr).find('input.autonumber,span.autonumber').autoNumeric('init');
                    $(modalCancelledElement).modal('toggle');
                }

            }
        });
    })

    //stock details modal update
    var stockDetailsModal = $('#modal-stock-details');
    var stockDetailsModalBody = stockDetailsModal.find('#modal-stock-details-body');
    var stockDetailsModalBodyDiv = stockDetailsModal.find('#cloneable-items');
    var stockCloneElement = $('#stock-details-html>.stock-details');
    stockDetailsModalBodyDiv.slimScroll({
        start: 'bottom',
        height: ''
    });
    stockDetailsModal.on('show.bs.modal', function(e) {
        if (e.namespace != 'bs.modal') {
            return;
        }
        var targetElement = $(e.relatedTarget);
        var stockNo = targetElement.attr('data-stockNo');
        stockCloneElement.clone().appendTo(stockDetailsModalBody);
        //updateStockDetailsData
        updateStockDetailsData(stockDetailsModal, stockNo)
    }).on('hidden.bs.modal', function() {
        stockDetailsModalBody.html('');
    })
    
    function setDocumentDashboardStatus() {
    $.getJSON(myContextPath + "/data/documents-dashboard/data-count", function(data) {
        $('#notReceived-count').html(data.data.notReceived);
        $('#received-count').html(data.data.received + "/" + data.data.receivedRikuji);
        $('#certificate-count').html(data.data.exportCertificate);
        $('#nameTransfer-count').html(data.data.nameTransfer + "/" + data.data.domestic + "/" + data.data.parts + "/" + data.data.shuppin);
        //$('#domestic-count').html(data.data.domestic);
    });
    $.getJSON(myContextPath + "/data/reauction-cancel-dashboard/status-count", function(data) {
    	$('#cancel-stock').html(data.data.cancelStock);
    });
}
})
