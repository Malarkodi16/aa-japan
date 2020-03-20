// Customize Datatable
$('#table-filter-search').keyup(function() {
    table.search($(this).val()).draw();
});
$('#table-filter-length').change(function() {
    table.page.len($(this).val()).draw();
});
/* Datatable view */

var table = $('#table-lastLapVehicles').DataTable({
    "dom": "<'row'<'col-sm-4'i><'col-sm-8'p>> <t> <'row'<'col-sm-4'i><'col-sm-8'p>>",
    "pageLength" : 25,
    "ajax": myContextPath + "/shipping/lastLap-vehicles/list/datasource",
    select: {
        style: 'multi',
        selector: 'td:first-child>input'
    },

    columnDefs: [{
        "targets": '_all',
        "defaultContent": ""
    }, {
        targets: 0,
        orderable: false,
        className: 'select-checkbox',
        "data": "id",
        "visible": false,
        "render": function(data, type, row) {
            data = data == null ? '' : data;
            if (type === 'display') {
                return '<input class="selectBox" type="checkbox" data-id="' + row.invoiceNo + '" value="' + data + '">'
            }
            return data;
        }
    }, {
        targets: 1,
       // orderable: false,
        "searchable": true,
        className: 'details-control',
        "name": "Stock No",
        "data": "stockNo",
        "render": function(data, type, row) {
            data = data == null ? '' : data;
            if (type === 'display') {
                return '<a href="#" data-toggle="modal" name="stockNo" data-target="#modal-stock-details" data-stockno="' + row.stockNo + '">' + data + '</a>';
            }
            return data;
        }
    }, {
        targets: 2,
      //  orderable: false,
        "searchable": true,
        className: 'details-control',
        "name": "Chassis No",
        "data": "chassisNo",
    }, {
        targets: 3,
     //   orderable: false,
        "searchable": true,
        "className": "details-control",
        "name": "Model",
        "data": "model",

    }, {
        targets: 4,
      //  orderable: false,
        "className": "details-control",
        "searchable": true,
        "name": "First Registration date",
        "data": "firstRegDate"
    }, {
        targets: 5,
      //  orderable: false,
        "className": "details-control",
        "searchable": true,
        "name": "destination country",
        "data": "destinationCountry"
    }, {
        targets: 6,
      //  orderable: false,
        "className": "details-control",
        "searchable": true,
        "name": "Last lap Date",
        "data": "lastlapStatus",
        "className": 'align-center',
        "render": function(data, type, row) {
            var expiryDate;
            var className;
            console.log(moment().subtract(3, 'months').format("DD-MM-YYYY"))
            if (data == 1) {
                expiryDate = row.expiryDate
                className = "warning"
            } else {
                expiryDate = row.expiryDate
                className = "danger"
            }
            return '<span class="label label-' + className + '">' + expiryDate + '</span>';
        }
    }, {
        targets: 7,
      //  orderable: false,
        "className": "details-control",
        "searchable": true,
        "name": "Inspection Status",
        "data": "inspectionStatus",
        "className": 'align-center',
        "render": function(data, type, row) {
            var inspectionStatus;
            var className;
            if (data == 1) {
                inspectionStatus = "Inspection Done"
                className = "success"
            } else {
                inspectionStatus = "Not Inspected"
                className = "default"

            }

            return '<span class="label label-' + className + '">' + inspectionStatus + '</span>';
        }

    }, {
        targets: 8,
      //  orderable: false,
        "className": "details-control",
        "searchable": true,
        "name": "Shipping Status",
        "data": "shippingStatus",
        "className": 'align-center',
        "render": function(data, type, row) {
            var shippingStatus;
            var className;
            if (data == 0) {
                shippingStatus = "Not Shipped"
                className = "default"
            } else {
                shippingStatus = "Shipping Done"
                className = "success"
            }
            return '<span class="label label-' + className + '">' + shippingStatus + '</span>';
        }

    }, {
        targets: 9,
     //   orderable: false,
        visible: false,
        "name": "warning date",
        "data": "warningDate"
    }, {
        targets: 10,
      //  orderable: false,
        visible: false,
        "name": "Expiry date",
        "data": "expiryDate"
    }]

});
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
